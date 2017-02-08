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

import java.util.Date;

import org.topicquests.support.api.IResult;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public interface ITupleSpaceModel {

	//////////////////////////
	// Identity
	//////////////////////////

	String uuid();

	//////////////////////////
	// Dates for BC and AD
	//////////////////////////
	
	/**
	 * Returns a long based on inputs
	 * @param isBCE <code>false</code> if date is Current Era (AD)
	 * @param year not valid when year > 200,000,000
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 * @throws Exception
	 */
	long getTimestamp(boolean isBCE, int year, int month, int day, int hour, int minute, int second) throws Exception;

	/**
	 * Returns string in the form G yyyy-MM-dd HH:mm:ss
	 * e.g. AD 1940-01-31 23:59:59  and BC 5000-01-31 23:59:59
	 * @param timestamp
	 * @return
	 */
	String timeStampToString(long timestamp);
	//////////////////////////
	// Sequences
	//////////////////////////

	ISequenceIterator createSequenceIterator();
	
	//////////////////////////
	// Tuples
	//////////////////////////
	/**
	 * Create a serialization of <code>tuple</code> which can be
	 * any tuple including a template, complete with a command message
	 * consisting of <code>verb</code> and <code>lease</code> for the
	 * receiving instane of {@link ITupleSpace}
	 * @param verb
	 * @param lease
	 * @param tuple
	 * @return
	 */
	JSONObject createTupleCarrier(String verb, long lease, JSONObject tuple);
	
	/**
	 * Returns an empty {@link ITuple} and <em>sortNumber</em>
	 * set to <em>now</em>
	 * @param tag
	 * @param userId
	 * @param isPrivate
	 * @return
	 */
	ITuple newTuple(String tag, String userId, boolean isPrivate);
	
	/**
	 * Returns an empty {@link ITuple with a specific <code>sortNumber</code>
	 * @param tag
	 * @param sortNumber
	 * @param userId
	 * @param isPrivate
	 * @return
	 */
	ITuple newTuple(String tag, long sortNumber, String userId, boolean isPrivate);
	
	/**
	 * <p>Return a shell template for forming queries.</p>
	 * <p>NOTE: this system, at present, is not able to process queries
	 * against the CARGO field; internally, we ignore that field, so
	 * including it in a template is not useful.</p>
	 * @param tag
	 * @return
	 */
	ITuple newTupleTemplate(String tag);
	
	/**
	 * Returns an {@link ITuple}
	 * @param tupleId
	 * @param userId only required for private tuples
	 * @return
	 */
	IResult getTuple(String tupleId, String userId);
	
	/**
	 * Returns a list of {@link ITuple} objects based on <code>tupleName</code>
	 * @param tupleName
	 * @param start TODO
	 * @param count TODO
	 * @param sort sorting on long tupleId "inc", "dec", or <code>null</code>
	 * @return
	 */
	//IResult listTuples(String tupleName, int start, int count, String sort);
	
	//IResult deleteTuple(int tupleId);
	
}
