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

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 * <p>Tuples go into two kinds of tuplespaces:<br/>
 *  Temporal Streams<br/>
 *  Documents</p>
 *  <p>Tuples in Temporal Streams reference Documents</p>
 *  <p>Eventually, Tuples in Temporal Streams can reference
 *  objects outside tuplespace, such as topics in a topic map,
 *  data in databases, e.g. geophysical or financial data</p>
 */
public interface ITuple extends IIdentifiable {
	/**
	 * Tags are like high-level categories
	 * @param tag
	 */
	void setTag(String tag);
	
	String getTag();
	
	/**
	 * <p>A privileged container</p>
	 * <p><code>cargo</code> can be any document, such as a link to an image,
	 * an email, a calendar event, link to a topic in a topic map, etc</p>
	 * @param cargo
	 */
	void setCargo(Object cargo);
	
	/**
	 * Can return <code>null</code>
	 * @return
	 */
	JSONObject getCargo();
	
	String getCargoType();
	
	void setCargoType(String type);
	
	/**
     * Allow for partial matching
     * If true, only the fields in the AntiTuple need match
     * no matter how many other fields are present.
     * Defaults <code>true</code>
     * @param tf
     */
    void setAllowPartialMatch(boolean tf);
    boolean getAllowPartialMatch();
    
    boolean matches(ITuple template);
    
	/**
	 * <code>value</code> can be a string or a collection
	 * @param key
	 * @param value
	 */
	void setProperty(String key, Object value);
	
	/**
	 * Fetch a property identified by <code>key</code>
	 * @param key
	 * @return can return <code>null</code>
	 */
	Object getProperty(String key);

	ITuple cloneTuple();
	
	String toJSONString();
	
	JSONObject getData();
}
