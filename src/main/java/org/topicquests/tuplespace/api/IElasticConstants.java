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

/**
 * @author jackpark
 *
 */
public interface IElasticConstants {
	//Index
	public static final String INDEX = "tuples";
	//IndexFields
	public static final String
		TUPLE_ID					= "tid",
		CREATOR_ID					= "crtr",
		DESCRIPTION					= "desc",
		CATEGORY					= "cat",
//		STREAM_ID					= "sid",
//		PARENT_STREAM_ID			= "psid",
		CREATED_DATE				= "crDt",
		LAST_EDIT_DATE_PROPERTY		= "lEdDt",
		VERSION						= "_ver",
		CARGO_TYPE					= "crgtyp",
		CARGO						= "cargo",
		ALLOW_PARTIAL_MATCH			= "apm",  //"T" or "F"
		IS_PRIVATE_BOOLEAN			= "ispriv",
		ACL_LIST					= "aclst",
//		STATUS						= "stat",
		//might use that instead of CREATED_DATE -- id's a double
//		JULIAN_DATE					= "jlnd",
		//technically same as CREATED_DATE -- is a long
		//this is ok for Lifestreams, but maybe not for streams of historical data
		SORT_DATE					= "srtDt";
	//Carrier commands
	public static final String
		VERB						= "verb",
		COMMAND						= "command",
		LEASE						= "lease";
}
