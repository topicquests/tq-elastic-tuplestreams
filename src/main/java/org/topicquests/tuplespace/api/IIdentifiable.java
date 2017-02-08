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

/**
 * @author jackpark
 *
 */
public interface IIdentifiable {

	String getId();
	
	void setId(String id);
	
	void setSortNumber(long number);
	long getSortNumber();
	String getSortDateString();
	
	Date getCreationDate();
	String getCreationDateString();
	
	void setCreationDate(Date date);
	
	Date getLastEditDate();
	String getLastEditDateString();
	
	void setLastEditDate(Date d);
	
	String getCreatorId();
	
	void setCreatorId(String id);
	
	String getDescription();
	
	void setDescription(String description);
	
	void setIsPrivate(boolean isPrivate);
	
	boolean getIsPrivate();
	
	void addACL(String id);
	
	boolean containsACL(String id);
	
	/**
	 * Always includes the creator's ID
	 * @return
	 */
	List<String> listACLs();
	
	/**
	 * Will set lastEditDate and Version
	 */
	void update();
	
	/**
	 * Use a  <code>_version_</code> field
	 * @param version
	 */
	void setVersion(String version);
	
	/**
	 * Return the SOLR4 version as a String
	 * @return
	 */
	String getVersion();

}
