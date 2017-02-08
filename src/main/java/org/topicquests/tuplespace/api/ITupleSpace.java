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

package org.topicquests.tuplespace.api;

import org.topicquests.support.api.IResult;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public interface ITupleSpace {

	/**
	 * Forms an ElasticSearch query based on <code>template</code>
	 * @param template
	 * @return
	 */
	JSONObject templateToQuery(ITuple template);
	
	/**
	 * Remove a template on lease in the TupleCache
	 * @param template
	 */
	void removeCacheTemplate(ITuple template);
	
	/**
	 * Public tuples are always OK. Otherwise, check credentials
	 * @param t
	 * @param userId
	 * @return
	 */
	boolean checkCredentials(ITuple t, String userId);
	
	boolean checkCredentials(JSONObject t, String userId);
	
	/**
	 * Returns match or blocks
	 * while waiting for a match to appear. Removes from database
	 * @param template
	 * @param waitTime can be 0 to Long.MAX_VALUE
	 * @param userId TODO
	 * @param listener
	 */
	void take(ITuple template, long waitTime, String userId, ITupleSpaceListener listener);
	
	IResult takeImmediate(ITuple template, String userId);
	
	/**
	 * Like <code>in</code> except returns a clone of the match
	 * @param template
	 * @param waitTime can be 0 to Long.MAX_VALUE
	 * @param userId TODO
	 * @param listener
	 * @return
	 */
	void read(ITuple template, long waitTime, String userId, ITupleSpaceListener listener);
	
	/**
	 * Allows an agent to read a tuples responding to <code>template</code>
	 * and watch for them until that <code>template</code> is later removed
	 * @param template
	 * @param userId
	 * @param listener
	 */
	void readAndWatch(ITuple template, String userId, ITupleSpaceListener listener);
	
	IResult readImmediate(ITuple template, String userId);
	/**
	 * Trivial fetch
	 * @param id
	 * @param userId TODO
	 * @return
	 */
	IResult getTupleById(String id, String userId);
	
	/**
	 * Store <code>tuple</code> in database
	 * @param tuple
	 * @return
	 */
	IResult putTuple(ITuple tuple);	
	
	/**
	 * <p>Update a given <code>tupleId</code></p>
	 * @param tupleId TODO
	 * @param updatedTuple
	 * @param checkVerstion
	 * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/docs-update.html
	 * </p>
	 * @return
	 */
	IResult updateTuple(String tupleId, JSONObject updatedTuple, boolean checkVerstion);
	
	IResult removeTuple(String tupleId);
	
	IResult countObjects(JSONObject query);
	
	void shutDown();
	
}
