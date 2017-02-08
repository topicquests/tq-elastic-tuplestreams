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

package org.topicquests.ets;

import java.util.Map;

import org.topicquests.support.config.ConfigPullParser;
//import org.topicquests.node.provider.ProviderEnvironment;
import org.topicquests.tuplespace.StreamModel;
//import org.topicquests.tuplespace.TupleSpace;
import org.topicquests.tuplespace.TupleSpaceEnvironment;
import org.topicquests.tuplespace.api.IElasticTupleStreamsModel;
import org.topicquests.tuplespace.api.ITupleSpace;
import org.topicquests.support.util.LoggingPlatform;

/**
 * @author jackpark
 *
 */
public class Environment {
	private LoggingPlatform log = LoggingPlatform.getInstance("logger.properties");
	private Map<String,Object>properties;
	private IElasticTupleStreamsModel model;
	private TupleSpaceEnvironment tsEnvironment;
	
	/**
	 * 
	 */
	public Environment() {
		try {
		ConfigPullParser p = new ConfigPullParser("config-props.xml");
		properties = p.getProperties();
		tsEnvironment = new TupleSpaceEnvironment();
		model = new StreamModel(this);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public TupleSpaceEnvironment getTupleSpaceEnvironment() {
		return tsEnvironment;
	}
	
	public IElasticTupleStreamsModel getTupleStreamModel() {
		return model;
	}
	
	public ITupleSpace getTuplespace() {
		return tsEnvironment.getTupleSpace();
	}
	
	public void shutDown() {
		
		tsEnvironment.shutDown();
		//TODO
	}
	
	public Map<String, Object> getProperties() {
		return properties;
	}
	
	public String getStringProperty(String key) {
		return (String)properties.get(key);
	}

	public void logDebug(String msg) {
		log.logDebug(msg);
	}

	public void logError(String msg, Exception e) {
		log.logError(msg, e);
	}

}
