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

import org.topicquests.support.ResultPojo;
import org.topicquests.support.api.IResult;
import org.topicquests.es.util.JSONQueryUtil;
import org.topicquests.node.provider.Client;
import org.topicquests.tuplespace.api.IElasticConstants;
import org.topicquests.tuplespace.api.ISequenceIterator;
import org.topicquests.tuplespace.api.ITuple;
import org.topicquests.tuplespace.api.ITupleSpace;

import net.minidev.json.JSONObject;

import java.util.*;

/**
 * @author jackpark
 * <p>Iterate along a given stream
 */
public class TupleSequenceIterator implements ISequenceIterator {
	private TupleSpaceEnvironment environment;
	private ITupleSpace space;
	private Client provider;
	private JSONQueryUtil queryUtil;
	private List<JSONObject> tuples;
	private final int COUNT = 20;
	private int start, maxnum;
	private String sequenceIdKey, sequenceIdValue, sortKey;
	private boolean increasing;
	private String userId;
	/** NOT THREAD SAFE **/
	private JSONObject theQuery;

	/**
	 * 
	 */
	public TupleSequenceIterator(TupleSpaceEnvironment env) {
		environment = env;
		space = environment.getTupleSpace();
		provider = environment.getProviderEnvironment().getClient();
		queryUtil =  new JSONQueryUtil();
		start = 0;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IStreamIterator#startIterator(java.lang.String)
	 */
	@Override
	public IResult startIterator(String sequenceIdKey, String sequenceIdValue, int start, int count, String sortKey, boolean increasing, String userId) {
		this.sequenceIdKey = sequenceIdKey;
		this.sequenceIdValue = sequenceIdValue;
		this.sortKey = sortKey;
		this.increasing = increasing;
		this.start = start;
		this.maxnum = count;
		this.userId = userId;
		IResult r = performQuery();
		if (tuples != null && tuples.size() > 0)
			r.setResultObject(tuples.remove(0));
		return r;
	}
	
	/**
	 * Should just fill tuples or not
	 * Tuples are actually just raw {@link JSONObject} instances
	 * @return returns <code>null</code> and any error message
	 */
	private IResult performQuery() {
		if (theQuery == null) {
			theQuery =  queryUtil.match(sequenceIdKey, sequenceIdValue);
			theQuery = queryUtil.query(theQuery);
			if (increasing)
				theQuery.put("sort", queryUtil.sortFieldAsending(sortKey));
			else
				theQuery.put("sort", queryUtil.sortFieldDescending(sortKey));
			tuples = new ArrayList<JSONObject>();
//			theQuery.put("count", COUNT);

		}
		theQuery.put("from", start);
		IResult result = new ResultPojo();
		String q = theQuery.toJSONString(); 
		System.out.println("ITERATORQ "+q);
		/*if (maxnum > -1) {
			if (start < maxnum) {
				if ((maxnum - start) > COUNT) // trim count if necessary
					theQuery.put("count", (maxnum - start));
			} else {
				return result;  // we  are done;
			}
		} */// else keep going
		IResult r = provider.listObjectsByQuery(q, IElasticConstants.INDEX);
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		System.out.println("ITRS "+result.getErrorString()+" | "+r.getResultObject());
		if (r.getResultObject() != null) {
			
			List<JSONObject> hits = (List<JSONObject>)r.getResultObject();
			if (hits.size() > 0) {
				JSONObject jo;
				start += hits.size();
				Iterator<JSONObject> itr = hits.iterator();
				ITuple t;
				while (itr.hasNext()) {
					jo = itr.next();
					if (space.checkCredentials(jo, userId))
						tuples.add(jo);
				}
			}
		} //otherwise, no more tuples  null means user stops asking for next
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IStreamIterator#next()
	 */
	@Override
	public IResult next() {
		IResult result = new ResultPojo();
		JSONObject t;
		if (tuples.size() == 0) {
			IResult r = performQuery();
			if (r.hasError())
				result.addErrorString(r.getErrorString());
			if (tuples.size() > 0)
				result.setResultObject(tuples.remove(0));
			//otherwise, we're done
		} else {
			t = tuples.remove(0);
			result.setResultObject(t);
		}
		return result;
	}

	@Override
	public IResult startIterator(ITuple template, int start, int count, String sortKey, boolean increasing, String userId) {
		this.theQuery = space.templateToQuery(template);
		this.maxnum = count;
		this.start = start;
		this.userId = userId;
		if (increasing)
			theQuery.put("sort", queryUtil.sortFieldAsending(sortKey));
		else
			theQuery.put("sort", queryUtil.sortFieldDescending(sortKey));
		
		//theQuery.put("count", COUNT);
		System.out.println("TSQ "+theQuery.toJSONString());
		//{"query":{"filtered":{"filter":{"bool":{"must":[{"term":{"cat":""}},{"query":{"match":{"dctyp":"Blog"}}}]}}}},"count":20,"from":0,"sort":{"Created":"asc"}}
		//{"query":{"filtered":{"filter":{"bool":{"must":[{"term":{"cat":""}},{"query":{"match":{"dctyp":"Blog"}}}]}}}},"count":20,"sort":{"crDt":"asc"}}
		//{"query":{"filtered":{"filter":{"bool":{"must":[{"query":{"match":{"dctyp":"Blog"}}}]}}}},"count":20,"sort":{"crDt":"asc"}}
		tuples = new ArrayList<JSONObject>();
		IResult r = performQuery();
		if (tuples.size() > 0)
			r.setResultObject(tuples.remove(0));
		return r;
	}

}
