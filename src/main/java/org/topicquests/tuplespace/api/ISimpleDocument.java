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
import java.util.List;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 * <p>Useful for e.g. emails, blog posts, notes</p>
 */
public interface ISimpleDocument extends ITuple {
	
	void setLastReadDate(Date d);
	
	Date getLastReadDate();
	
	void setDocumentType(String t);
	
	String getDocumentType();
	
	void setSubject(String subject);
	
	String getSubject();
	
	void setBody(String body);
	
	String getBody();
	
	List<String> listStreamIds();
	
	void addStreamId(String id);
	
	void removeStreamId(String id);
}
