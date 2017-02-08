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
public interface IConstants {
	//CargoTypes which extend ICargoTypes
	public static final String
		//any local addressable resource like a note
		CARGO_AIR_TYPE				= "airCargo",
		//any online resource, like a webpage, blog post, etc
		CARGO_EXTERNAL_AIR_TYPE		= "extAirCargo",
		CARGO_CALENDAR_TYPE			= "calendarCargo",
		//a specific local air type
		CARGO_EMAIL_TYPE			= "emailCargo",
		//a reference to a topic in an extrnal topic map
		CARGO_TOPIC_TYPE			= "topicCargo",
		//local data
		CARGO_DATA_TYPE				= "dataCargo",
		//external data, e.g. financial or geophysical data
		CARGO_EXTERNAL_DATA_TYPE	= "extDataCargo";
	//Lifestreams tuple keys
	public static final String
		STREAM_ID					= "sid",
		PARENT_STREAM_ID			= "psid",
		CHILD_STREAM_ID_LIST		= "csidl",
		FIRST_MEMBER_ID				= "fmid",
		FIRST_MEMBER_TIMESTAMP		= "fmts",
		LAST_MEMBER_ID				= "lmid",
		LAST_MEMBER_TIMESTAMP		= "lmts",
		STATUS						= "stat",
		SUBJECT						= "subj",
		BODY						= "body",
		DOC_TYPE					= "dctyp",
		LAST_READ_DATE				= "lrDt",

		STREAM_ID_LIST				= "sidl";
	//	JULIAN_DATE					= "jlnd";
	
	public static final String
		EMAIL_TYPE					= "emailType",
		NOTE_TYPE					= "noteType",
		BLOG_TYPE					= "blogType",
		TWEET_TYPE					= "tweetType",
		CALENDAR_EVENT_TYPE			= "calType",
		WIKI_TYPE					= "wikiType",
		TOPIC_TYPE					= "topicType",
		TODO_TYPE					= "todoType";
		

}
