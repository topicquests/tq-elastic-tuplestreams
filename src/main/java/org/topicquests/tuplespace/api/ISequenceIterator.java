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

/**
 * @author jackpark
 *
 */
public interface ISequenceIterator {

	/**
	 * <p>Will return an instance of {@link ITuple} if
	 * there is a non-empty stream for the given <code>streamId</code></p>
	 * <p>Otherwise, will return <code>null</code></p>
	 * @param sequenceIdKey property key for identifying this sequence
	 * @param sequenceIdValue property value for identifying this sequence
	 * @param start anywhere
	 * @param count -1 means all; otherwise limit to that value
	 * @param sortKey property key for numeric value to sort
	 * @param increasing 
	 * @param userId TODO
	 * @return return <code>null</code> or list of {@link JSONObject} instances
	 */
	IResult startIterator(String sequenceIdKey, String sequenceIdValue, int start, int count, String sortKey, boolean increasing, String userId);
	
	IResult startIterator(ITuple template, int start, int count, String sortKey, boolean increasing, String userId);
	/**
	 * Will return <code>null</code> when no further objects available.
	 * @return
	 */
	IResult next();
}
