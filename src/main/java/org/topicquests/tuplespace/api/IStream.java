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

import java.util.List;

/**
 * @author jackpark
 *
 */
public interface IStream extends ITuple {

	///////////////////////
	//Two clear ways to identify an IStream and its components:
	// Tag
	// streamId
	///////////////////////
	
	///////////////////////
	//AS A SUBSTREAM -- this substream is based on 
	// a particular query template, which, itself,
	// is a query on a particular stream, meaning, the
	// query includes the parent streams ID
	///////////////////////
	void setParentStreamId(String id);
	String getParentStreamId();
	
	void setQueryString(String query);
	String getQueryString();
	
	//////////////////////
	// As a ParentStream
	// It's nice to "know your persistent kids"
	//////////////////////
	
	void addChildStreamId(String id);
	
	/**
	 * Can return <code>null</code>
	 * @return
	 */
	List<String> listChildIds();
	
	///////////////////////
	// Stats
	///////////////////////
	String getLastId();
	void setLastId();
	
	long getLastTimestamp();
	void setLastTimestamp();
	
	long size();
	
}
