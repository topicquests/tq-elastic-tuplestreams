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

package org.topicquests.tuplespace;

import java.util.Map;

import org.topicquests.node.provider.ProviderEnvironment;
//import org.topicquests.tuplespace.api.IElasticTupleStreamsModel;
import org.topicquests.tuplespace.api.ITupleSpace;
import org.topicquests.tuplespace.api.ITupleSpaceModel;
import org.topicquests.support.util.LoggingPlatform;

/**
 * @author jackpark
 *
 */
public class TupleSpaceEnvironment {
	private LoggingPlatform log = LoggingPlatform.getInstance("logger.properties");
	private Map<String,Object>properties;
	private ITupleSpace tupleSpace;
	private ITupleSpaceModel model;
	private ProviderEnvironment provider;

	/**
	 * 
	 */
	public TupleSpaceEnvironment() {
		provider = new ProviderEnvironment();
		tupleSpace = new TupleSpace(this);
		model = new TupleSpaceModel(this);
	}

	public ProviderEnvironment getProviderEnvironment() {
		return provider;
	}
	
	public ITupleSpace getTupleSpace() {
		return tupleSpace;
	}

	public ITupleSpaceModel getModel() {
		return model;
	}
	
	public void shutDown() {
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
