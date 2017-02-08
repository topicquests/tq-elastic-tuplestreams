/*
 * Copyright 2017, TopicQuests
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */

package org.topicquests.tuplespace;
import java.util.*;

import org.topicquests.support.ResultPojo;
import org.topicquests.support.api.IResult;
import org.topicquests.node.provider.Client;
import org.topicquests.tuplespace.api.IElasticConstants;
import org.topicquests.tuplespace.api.ITuple;
import org.topicquests.tuplespace.api.ITupleSpace;
import org.topicquests.tuplespace.api.ITupleSpaceListener;
/**
 * @author jackpark
 *
 */
public class TupleCache {
	private TupleSpaceEnvironment environment;
	private ITupleSpace space;
	private Client provider;
	private int MAX_SIZE;
	private Map<String, ITuple> cache;

	/**
	 * 
	 */
	public TupleCache(TupleSpaceEnvironment env, ITupleSpace s, int maxCount) {
		environment = env;
		space = s;
		provider = environment.getProviderEnvironment().getClient();
		MAX_SIZE = maxCount;

	    cache = new LinkedHashMap<String, ITuple>(MAX_SIZE+1, .75F, true) {
	    	private static final long serialVersionUID = 1;
	    		//@override
	    		protected boolean removeEldestEntry(Map.Entry eldest) {
	    			return size() > MAX_SIZE;
	    		}};

	}
	
	/**
	 * <p>If an {@link ITuple} enters the cache, and a template takes it,
	 * that tuple will never reach the cache or the database</p>
	 * @param tup
	 */
	public IResult add(ITuple tup) {
		if (cache.get(tup.getId()) == null) {
			IResult result  = evalTuple(tup);
			return result;
		}
		return new ResultPojo();
	}
	
	public void remove(String tupleId) {
		synchronized(cache) {
			cache.remove(tupleId);
		}
	}
	
	/**
	 * Remove a template being held here on lease
	 * @param template
	 */
	public void removeTemplate(ITuple template) {
		this.takeHolder.removeLease(template);
		this.readHolder.removeLease(template);
	}
	/**
	 * Can return an empty list
	 * @param tag  "*" is a wildcard
	 * @return
	 */
	public List<ITuple> listByTag(String tag) {
		synchronized(cache) {
			List<ITuple> result = new ArrayList<ITuple>();
			Iterator<String>itr = cache.keySet().iterator();
			String id;
			ITuple t;
			while (itr.hasNext()) {
				id = itr.next();
				t = cache.get(id);
				if (tag.equals("*"))
					result.add(t);
				else if (t.getTag().equals(tag))
					result.add(t);
			}
			return result;
		}
	}

	/**
	 * Can return <code>null</code>
	 * @param id
	 * @return
	 */
	public ITuple getTupleById(String id) {
		synchronized(cache) {
			System.out.println("CacheGet "+id);
			return cache.get(id);
		}
	}
	
	public ITuple getTupleByTemplate(ITuple template) {
		synchronized(cache) {
			List<ITuple> tups = this.listByTag(template.getTag());
			if (tups != null && tups.size() > 0) {
				ITuple t;
				Iterator<ITuple>itr = tups.iterator();
				while (itr.hasNext()) {
					t = itr.next();
					if (t.matches(template)) {
						return t;
					}
				}
			}
		}
		return null;
	}
	
	public int size() {
		synchronized(cache) {
			return cache.size();
		}
	}
	
	public void clear() {
		synchronized(cache) {
			cache.clear();
		}
	}
	/////////////////////////////////
	// Template handling
	// We are here because a given template was not satisfied by a database fetch
	/////////////////////////////////
	private ILeaseHolder takeHolder = new TakeLeaseHolder();
	private ILeaseHolder readHolder = new ReadLeaseHolder();
	
	/**
	 * Return null if <code>tup</code> is matched by a take template
	 * @param tup
	 * @return
	 */
	private IResult evalTuple(final ITuple tup) {
		System.out.println("CacheEval "+tup.toJSONString());
		IResult result = new ResultPojo();
		//pass through takes
		List<Lease> leases = eval(tup, takeHolder, true);
		System.out.println("EVALT "+leases);
		if (leases == null) {
			//send it to the database
			synchronized(cache) {
				//stash against ID
				System.out.println("CacheStash "+tup.getId());
				cache.put(tup.getId(), tup);
			}
			IResult x = provider.getNodeAsJSONObject(tup.getId(), IElasticConstants.INDEX);
			if (x.getResultObject() == null) {
				//only index if not already in database
				System.out.println("INDeXING "+tup.toJSONString());
				result = provider.indexNode(tup.getId(), IElasticConstants.INDEX, tup.getData());
			}	//not captured by take means keep this tuple around and store it
				
				Lease lease;
				IResult r = new ResultPojo();
					
				leases = eval(tup, readHolder, false);
				System.out.println("EVALR "+leases);
				if (leases != null) {
					//satisfy all leases
					Iterator<Lease>itr = leases.iterator();
					while (itr.hasNext()) {
						lease = itr.next();
						r.setResultObject(tup.cloneTuple());
						lease.listener.acceptResult(r);
						lease.host.dropLease(tup.getTag(), lease);
					}
				}
			
			
		}
		return result;
	}
	
	/**
	 * The tuple comparison workhorse
	 * @param tup
	 * @param h
	 * @param isTake 
	 * @return returns a list of matched template Leases or null;
	 */
	private List<Lease> eval(final ITuple tup, ILeaseHolder h, boolean isTake) {
		System.out.println("EVAL- "+tup.toJSONString());
		List<Lease> result = null;
		List<Lease> templates = h.listLeases(tup.getTag());
		System.out.println("EVAL "+templates);
		if (templates != null) {
			Lease lease;
			ITuple template;
			Iterator<Lease>itr = templates.iterator();
			while (itr.hasNext()) {
				lease = itr.next();
				template = lease.tuple;
				if (tup.matches(template)) {
					if (isTake) {
						// take it and run
						IResult r = new ResultPojo();
						r.setResultObject(tup);
						lease.listener.acceptResult(r);
						lease.host.dropLease(tup.getTag(), lease);
						return null;
					}
					if (result == null)
						result = new ArrayList<Lease>();
					result.add(lease);
				}
			}
		}
		System.out.println("EVAL+ "+tup.toJSONString());
		return result;
	}
	
	/**
	 * <p>Here because <code>lease</code> > 0</p>
	 * @param template
	 * @param lease
	 */
	public void addTemplateTake(ITuple template, long lease, ITupleSpaceListener listener) {
		takeHolder.addTemplate(template, lease, listener);
	}
	
	public void addTemplateRead(ITuple template, long lease, ITupleSpaceListener listener) {
		readHolder.addTemplate(template, lease, listener);
	}
	
	interface ILeaseHolder {
		void dropLease(String tag, Lease l);
		void removeLease(ITuple template);
		void addTemplate(ITuple t, long lease, ITupleSpaceListener listener);
		List<Lease> listLeases(String tag);
	}
	class TakeLeaseHolder implements ILeaseHolder {
		Map<String, List<Lease>> leases = new HashMap<String, List<Lease>>();

		@Override
		public void dropLease(String tag, Lease l) {
			synchronized(leases) {
				List<Lease> lx = leases.get(tag);
				lx.remove(l);
			}
		}
		
		@Override
		public void removeLease(ITuple template) {
			synchronized(leases) {
				List<Lease>x = (List<Lease>)leases.get(template.getTag());
				if (x != null) {
					Lease l=null, temp;
					Iterator<Lease> itr = x.iterator();
					while (itr.hasNext()) {
						temp = itr.next();
						if (template.matches(temp.tuple)) {
							l = temp;
							break;
						}
					}
					if (l != null) {
						x.remove(l);
						leases.put(template.getTag(), x);
					}
						
				}
			}
		}
		
		/**
		 * Can return <code>null</code>
		 * @param tag
		 * @return
		 */
		@Override
		public List<Lease> listLeases(String tag) {
			System.out.println("TGETLEASE "+tag+" "+leases);
			return (List<Lease>)leases.get(tag);
		}
		
		@Override
		public void addTemplate(ITuple t, long lease, ITupleSpaceListener listener) {
			synchronized(leases) {
				String tag = t.getTag();
				List<Lease> lx = (List<Lease>)leases.get(tag);
				if (lx == null) 
					lx = new ArrayList<Lease>();
				lx.add(new Lease(t, t.getTag(), lease, this, listener));
				leases.put(t.getTag(), lx );
			}
		}
	}
	
	class ReadLeaseHolder implements ILeaseHolder {
		Map<String, List<Lease>> leases = new HashMap<String, List<Lease>>();

		@Override
		public void dropLease(String tag, Lease l) {
			synchronized(leases) {
				List<Lease> lx = leases.get(tag);
				lx.remove(l);
				System.out.println("RDROP "+leases);
			}
		}
		
		@Override
		public void removeLease(ITuple template) {
			synchronized(leases) {
				List<Lease>x = (List<Lease>)leases.get(template.getTag());
				if (x != null) {
					Lease l=null, temp;
					Iterator<Lease> itr = x.iterator();
					while (itr.hasNext()) {
						temp = itr.next();
						if (template.matches(temp.tuple)) {
							l = temp;
							break;
						}
					}
					if (l != null) {
						x.remove(l);
						leases.put(template.getTag(), x);
					}
						
				}
			}
		}

		/**
		 * Can return <code>null</code>
		 * @param tag
		 * @return
		 */
		public List<Lease> listLeases(String tag) {
			System.out.println("RGETLEASE "+tag+" "+leases);
			return (List<Lease>)leases.get(tag);
		}
		
		public void addTemplate(ITuple t, long lease, ITupleSpaceListener listener) {
			synchronized(leases) {
				String tag = t.getTag();
				List<Lease> lx = (List<Lease>)leases.get(tag);
				if (lx == null) 
					lx = new ArrayList<Lease>();
				System.out.println("READADDTEMP "+t.toJSONString());
				lx.add(new Lease(t, t.getTag(), lease, this, listener));
				leases.put(t.getTag(), lx );
			}
		}
	}
	
	class Lease {
		public ITuple tuple;
		public ITupleSpaceListener listener;
		public ILeaseHolder host;
		
		public Lease(ITuple t, String tag, long lease, ILeaseHolder h, ITupleSpaceListener l) {
			tuple = t;
			listener = l;
			host = h;
			if (lease != Long.MAX_VALUE) {
				try {
					this.wait(lease);
				} catch (Exception e) {}
				host.dropLease(tag, this);
			}//otherwise, wait forever
		}
		
	}
}
