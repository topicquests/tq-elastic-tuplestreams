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

import java.util.Date;
import java.util.UUID;

import org.topicquests.support.api.IResult;
import org.topicquests.es.util.UniversalTimeStamp;
import org.topicquests.tuplespace.api.IElasticConstants;
import org.topicquests.tuplespace.api.ISequenceIterator;
import org.topicquests.tuplespace.api.ITuple;
import org.topicquests.tuplespace.api.ITupleSpace;
import org.topicquests.tuplespace.api.ITupleSpaceModel;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class TupleSpaceModel implements ITupleSpaceModel {
	private TupleSpaceEnvironment environment;
	private ITupleSpace space;
	private UniversalTimeStamp uts;
	/**
	 * 
	 */
	public TupleSpaceModel(TupleSpaceEnvironment env) {
		environment = env;
		space = environment.getTupleSpace();
		uts = new UniversalTimeStamp();
	}

	/* (non-Javadoc)
	 * @see org.topicquests.tuplespace.api.ITupleSpaceModel#newTuple(java.lang.String, java.lang.String)
	 */
	@Override
	public ITuple newTuple(String tag, String userId, boolean isPrivate) {
		ITuple result = newTuple(tag, System.currentTimeMillis(), userId, isPrivate);
		return result;
	}

	@Override
	public ITuple newTuple(String tag, long sortNumber, String userId, boolean isPrivate) {
		ITuple result = new Tuple();
		result.setId(uuid());
		result.setSortNumber(sortNumber);
		result.setCreatorId(userId);
		Date d = new Date();
		result.setCreationDate(d);
		result.setLastEditDate(d);
		result.setVersion(Long.toString(d.getTime()));
		if (tag != null && !tag.equals(""))
				result.setTag(tag);
		result.setIsPrivate(isPrivate);
		result.addACL(userId);
		return result;
	}
	
	@Override
	public ITuple newTupleTemplate(String tag) {
		ITuple result = new Tuple();
		result.setTag(tag);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.tuplespace.api.ITupleSpaceModel#getTuple(int)
	 */
	@Override
	public IResult getTuple(String tupleId, String userId) {
		return space.getTupleById(tupleId, userId);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.tuplespace.api.ITupleSpaceModel#listTuples(java.lang.String)
	 * /
	@Override
	public IResult listTuples(String tupleName, int start, int count, String sort) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.tuplespace.api.ITupleSpaceModel#deleteTuple(int)
	 * /
	@Override
	public IResult deleteTuple(int tupleId) {
		// TODO Auto-generated method stub
		return null;
	}
	*/

	@Override
	public String uuid() {
		return  UUID.randomUUID().toString();
	}

	@Override
	public ISequenceIterator createSequenceIterator() {
		return new TupleSequenceIterator(environment);
	}

	@Override
	public JSONObject createTupleCarrier(String verb, long lease, JSONObject tuple) {
		JSONObject result = new JSONObject();
		JSONObject cmd = new JSONObject();
		cmd.put(IElasticConstants.VERB, verb);
		cmd.put(IElasticConstants.LEASE, new Long(lease));
		result.put(IElasticConstants.COMMAND, cmd);
		result.put(IElasticConstants.CARGO, tuple);
		
		return result;
	}

	@Override
	public long getTimestamp(boolean isBCE, int year, int month, int day, int hour, int minute, int second) throws Exception {
		return uts.getTimeStamp(isBCE, year, month, day, hour, minute, second);
	}

	@Override
	public String timeStampToString(long timestamp) {
		return uts.timeStampToString(timestamp);
	}

}
