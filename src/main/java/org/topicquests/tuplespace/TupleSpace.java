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

import org.elasticsearch.index.query.QueryBuilder;
import org.topicquests.support.ResultPojo;
import org.topicquests.support.api.IResult;
import org.topicquests.es.util.ElasticQueryUtility;
import org.topicquests.es.util.JSONQueryUtil;
import org.topicquests.node.provider.Client;
import org.topicquests.tuplespace.api.ITupleSpace;
import org.topicquests.tuplespace.api.ITupleSpaceListener;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import org.topicquests.tuplespace.api.IElasticConstants;
import org.topicquests.tuplespace.api.ITuple;

/**
 * @author jackpark
 *
 */
public class TupleSpace implements ITupleSpace {
	private TupleSpaceEnvironment environment;
	private Client provider;
	private ElasticQueryUtility queryUtil;
	private JSONQueryUtil queryBuilder;
	private Object waitObject;
	private ReentrantReadWriteLock rwl;


	//////////////////////////////
	// We add an ITuple to the cache when we fetch it.
	// We add an ITuple to the cache when we put it
	//	That's because ITupleSpace rd and in can block while
	//  waiting for a tuple to arrive. They then watch the cache
	//  rather than do a fetch on new arrivals
	//////////////////////////////
	private TupleCache cache;
	private final int cacheSize = 2048;//TODO make config val
	/**
	 * 
	 */
	public TupleSpace(TupleSpaceEnvironment env) {
		environment = env;
		provider = environment.getProviderEnvironment().getClient();
		queryUtil =  new ElasticQueryUtility();
		rwl = new ReentrantReadWriteLock();
		cache = new TupleCache(environment, this, cacheSize);
		queryBuilder = new JSONQueryUtil();
		waitObject = new Object();
	}

	@Override
	public void take(ITuple template, long waitTime, String userId, ITupleSpaceListener listener) {
		IResult result = new ResultPojo();
		ITuple t = this.getFromCache(template);
		System.out.println("TAKE1 "+t);
		if (t != null) {
			if (checkCredentials(t, userId)) {
				result.setResultObject(t);
				listener.acceptResult(result);
				cache.remove(t.getId());
				//remove tuple from database
				removeTuple(t.getId());
			}
			return;
		}
		//OTHERWISE fetch tuple

		String q = templateToQuery(template).toJSONString();
		IResult r = provider.listObjectsByQuery(q, IElasticConstants.INDEX);
		List<JSONObject> l = (List<JSONObject>)r.getResultObject();
		if (l != null && l.size() > 0) {
			t = new Tuple(l.get(0));
			if (checkCredentials(t, userId)) {
				cache.add(t);
				r.setResultObject(t);
					removeTuple(t.getId());
			}
			listener.acceptResult(r);//send the tuple
			return;
		}//otherwise wait for it, or not
		if (waitTime == 0) {
			listener.acceptResult(r);
			return;
		}
		cache.addTemplateTake(template, waitTime, listener);
	}
	
	@Override
	public IResult removeTuple(String tupleId) {
		return provider.deleteNode(tupleId, IElasticConstants.INDEX);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.ILinda#rd(java.lang.String, java.lang.String, org.topicquests.ets.api.IDocument)
	 */
	@Override
	public void read(ITuple template, long waitTime, String userId, ITupleSpaceListener listener) {
		IResult result = new ResultPojo();
		ITuple temp = template.cloneTuple();
		ITuple t = this.getFromCache(temp);
		if (t != null) {
			if (checkCredentials(t, userId)) 
				result.setResultObject(t.cloneTuple());
			listener.acceptResult(result);
			return;
		}
		//otherwise form a database query
		String q = templateToQuery(template).toJSONString();
		IResult r = provider.listObjectsByQuery(q, IElasticConstants.INDEX);
		System.out.println("SpaceRead A "+r.getErrorString()+" "+r.getResultObject());
		List<JSONObject> l = (List<JSONObject>)r.getResultObject();
		if (l != null && l.size() > 0) {
			t = new Tuple(l.get(0));
			cache.add(t);
			r.setResultObject(t.cloneTuple());
			listener.acceptResult(r);//send the tuple
			return;
		}//otherwise wait for it, or not
		if (waitTime == 0) {
			listener.acceptResult(r);
			return;
		}
		//otherwise, sent to TupleCache
		cache.addTemplateRead(temp, waitTime, listener);
	}
	
	@Override
	public void readAndWatch(ITuple template, String userId, ITupleSpaceListener listener) {
		IResult result = new ResultPojo();
		ITuple temp = template.cloneTuple();
		ITuple t = this.getFromCache(temp);
		if (t != null) {
			if (checkCredentials(t, userId)) 
				result.setResultObject(t.cloneTuple());
			listener.acceptResult(result);
			//watch for it
			cache.addTemplateRead(temp, Long.MAX_VALUE, listener);
			return;
		}
		//otherwise form a database query
		String q = templateToQuery(template).toJSONString();
		IResult r = provider.listObjectsByQuery(q, IElasticConstants.INDEX);
		System.out.println("SpaceRead A "+r.getErrorString()+" "+r.getResultObject());
		List<JSONObject> l = (List<JSONObject>)r.getResultObject();
		if (l != null && l.size() > 0) {
			t = new Tuple(l.get(0));
			cache.add(t);
			r.setResultObject(t.cloneTuple());
			listener.acceptResult(r);//send the tuple
		}
		//watch for it
		cache.addTemplateRead(temp, Long.MAX_VALUE, listener);
	}
	
	@Override
	public IResult readImmediate(ITuple template, String userId) {
		System.out.println("SpaceReadImmediate- "+template.toJSONString());
		IResult result = new ResultPojo();
		IResult r;
		ITuple t = this.getFromCache(template.cloneTuple());; //cache.getTupleByTemplate(template);
		if (t != null)
			result.setResultObject(t);
		else {
			String q = templateToQuery(template).toJSONString();
			r = provider.listObjectsByQuery(q, IElasticConstants.INDEX);
			System.out.println("SpaceRead A "+r.getErrorString()+" "+r.getResultObject());
			List<JSONObject> l = (List<JSONObject>)r.getResultObject();
			if (l != null && l.size() > 0) {
				t = new Tuple(l.get(0));
				if (checkCredentials(t, userId)) {
					cache.add(t);
					result.setResultObject(t.cloneTuple());
				}
			}
		}
		return result;
	}

	@Override
	public JSONObject templateToQuery(ITuple template) {
//		System.out.println("TemplateQuery "+template.toJSONString());
		JSONObject result = new JSONObject();
		JSONArray mustmatches = queryBuilder.createJA();
		JSONArray mustterms = queryBuilder.createJA();
		if (!template.getTag().equals(""))
			mustterms.add(queryBuilder.term(IElasticConstants.CATEGORY, template.getTag()));
		if (template.getCargoType() != null)
			mustterms.add(queryBuilder.term(IElasticConstants.CARGO_TYPE, template.getCargoType()));			
		//WE CANNOT QUERY AGAINST CARGO
		JSONObject data = template.getData();
		JSONObject jo;
		//TODO  cargo, etc
		Iterator<String>itr = data.keySet().iterator();
		String key;
		Object val;
		while (itr.hasNext()) {
			key = itr.next();
			System.out.println("KEY "+key);
			//catches cargo and description plus any other properties
			if (!(key.equals(IElasticConstants.CATEGORY) ||
				  key.equals(IElasticConstants.CARGO_TYPE) ||
				  key.equals(IElasticConstants.CARGO) ||
				  key.equals(IElasticConstants.CREATOR_ID))) {
				val = template.getProperty(key);
				if (val instanceof java.util.List) {
					val = ((java.util.List)val).get(0);
				}
				jo = queryBuilder.match(key, val);
				//jo = queryBuilder.query(jo);
				mustmatches.add(jo);
			}
		}
		//JSONObject jx = queryBuilder.query(mustmatches);
		if (mustmatches != null && (mustmatches.size() > 0) )
		mustterms.addAll(mustmatches);
		//must the matches
		//jx = queryBuilder.must(jx);
		//must the terms
		jo = queryBuilder.must(mustterms);

		//boolean query
		jo = queryBuilder.bool(jo);
		//jo = queryBuilder.filter(jo);
		//it's filtered
		//jo = queryBuilder.filtered(jo);
		//the final query
		result.put("query", jo);
System.out.println("TemplateQuery "+result.toJSONString());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.ILinda#shutDown()
	 */
	@Override
	public void shutDown() {
		// TODO Auto-generated method stub

	}


	@Override
	public IResult getTupleById(String id, String userId) {
		System.out.println("SpaceGetById "+id);
		IResult result = new ResultPojo();
		IResult r = provider.getNodeAsJSONObject(id, IElasticConstants.INDEX);
		JSONObject jo = (JSONObject)r.getResultObject();
		if (jo != null) {
			ITuple t = new Tuple(jo);
			if (checkCredentials(t, userId)) {
				cache.add(t);
				result.setResultObject(t);
			}
		}
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		return result;
	}

	@Override
	public IResult putTuple(ITuple tuple) {
		System.out.println("SpacePut "+tuple.toJSONString());
		IResult r =	cache.add(tuple);
		//cache does the storing
		//System.out.println("PutTuple "+tuple.toJSONString());
		return r;
	}
	
	@Override
	public IResult updateTuple(String tupleId, JSONObject updatedTuple, boolean checkVerstion) {
		IResult result = provider.updateFullNode(tupleId, IElasticConstants.INDEX, updatedTuple, checkVerstion); 
		return result;
	}


	/////////////////////////////
	//
	/////////////////////////////
	
	/**
	 * <p>This method is called if an in or rd is waiting for
	 * a new object to enter this space. It will first be placed
	 * in the cache, then sent to the database. Placing it in
	 * the cache will fire an event for which some thread is waiting.
	 * Once waking up from a wait, the caller scans the cache to see
	 * if it is satisfied. If not, it goes back to sleep; otherwise,
	 * it exits with the results</p>
	 * <p>This method is also the first place in or rd looks before
	 * going to the database</p>
	 * @param template
	 * @return can return <code>null</code>
	 */
	private ITuple getFromCache(ITuple template) {
		List<ITuple> tups = cache.listByTag(template.getTag());
		if (tups.size() > 0) {
			ITuple t;
			Iterator<ITuple>itr = tups.iterator();
			while (itr.hasNext()) {
				t = itr.next();
				if (t.matches(template)) {
					return t;
				}
			}
		}
		return null;
	}

	@Override
	public IResult takeImmediate(ITuple template, String userId) {
		System.out.println("SpaceTakeImmediate- "+template.toJSONString());
		IResult result = new ResultPojo();
		IResult r;
		ITuple t = this.getFromCache(template.cloneTuple()); //cache.getTupleByTemplate(template);
		if (t != null && checkCredentials(t, userId)) {
			result.setResultObject(t);
			//remove from cache
			this.cache.remove(t.getId());
		}
		else {
			String q = templateToQuery(template).toJSONString();
			r = provider.listObjectsByQuery(q, IElasticConstants.INDEX);
			System.out.println("SpaceRead A "+r.getErrorString()+" "+r.getResultObject());
			List<JSONObject> l = (List<JSONObject>)r.getResultObject();
			if (l != null && l.size() > 0) {
				t = new Tuple(l.get(0));
				if (checkCredentials(t, userId)) {
					cache.add(t);
					result.setResultObject(t.cloneTuple());
				}
			}
		}
		//if we got it, remove from database
		if (t != null) {
			r = this.removeTuple(t.getId());
			if (r.hasError())
				result.addErrorString(r.getErrorString());
		}
		return result;
	}

	@Override
	public boolean checkCredentials(ITuple t, String userId) {
		if (!t.getIsPrivate())
			return true;   //it's public
		if (userId == null)
			return false; // missing required credentials
		return t.containsACL(userId);
	}

	@Override
	public boolean checkCredentials(JSONObject t, String userId) {
		Object ip = t.get(IElasticConstants.IS_PRIVATE_BOOLEAN);
		boolean isPrivate = false;
		if (ip == null)
			return true; // default if not marked
		if (ip instanceof String)
			isPrivate = ("T".equalsIgnoreCase((String)ip));
		else
			isPrivate = ((Boolean)ip).booleanValue();
		if (!isPrivate)
			return true;
		List<String>acls = (List<String>)t.get(IElasticConstants.ACL_LIST);
		if (acls == null)
			return true; // default
		return acls.contains(userId);
	}

	@Override
	public void removeCacheTemplate(ITuple template) {
		this.cache.removeTemplate(template);
	}

	@Override
	public IResult countObjects(JSONObject query) {
		return provider.count(query.toJSONString(), IElasticConstants.INDEX);
	}



}
