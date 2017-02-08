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

package org.topicquests.ets.api;

import org.topicquests.support.api.IResult;
import org.topicquests.tuplespace.api.ISimpleDocument;
import org.topicquests.tuplespace.api.IStream;
import org.topicquests.tuplespace.api.ITuple;

/**
 * @author jackpark
 *<p>An API for user intentions</p>
 */
public interface IUXModel {

	/**
	 * Returns a new {@link ISimpleDocument}
	 * @param userId
	 * @param text
	 * @return
	 */
	IResult newDocument(String userId, String text);
	
	/**
	 * Returns a {@link ISimpleDocument}
	 * @param streamId
	 * @param docId
	 * @return
	 */
	IResult getDocument(String streamId, String docId);
	
	/**
	 * Returns a new {@link ISimpleDocument}
	 * @param streamId
	 * @param docId
	 * @return
	 */
	IResult cloneDocument(String streamId, String docId);
	
	IResult updateDocument(ISimpleDocument doc);
	
	/**
	 * Returns either an {@link ISimpleDocument} or <code>null</code>
	 * @param streamId
	 * @param template
	 * @return
	 */
	IResult findDocument(String streamId, ITuple template);
	
	/**
	 * Returns a new {@link IStream}
	 * @param userId
	 * @param description
	 * @return
	 */
	IResult newStream(String userId, String description);
	
	/**
	 * Returns a {@link IStream} where the stream's
	 * description is based on the <code>template</code>
	 * @param streamId
	 * @param template
	 * @return
	 */
	IResult subStream(String streamId, ITuple template);
	
	/**
	 * Returns a list of {@link ISimpleDocument} instances
	 * @param stringId
	 * @param start
	 * @param count
	 * @return
	 */
	IResult listStreamDocuments(String stringId, int start, int count);
}
