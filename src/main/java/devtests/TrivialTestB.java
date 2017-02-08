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

package devtests;

import org.topicquests.support.api.IResult;
import org.topicquests.es.util.JSONQueryUtil;
import org.topicquests.node.provider.Client;
import org.topicquests.tuplespace.TupleSpaceEnvironment;
import org.topicquests.tuplespace.api.IElasticConstants;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class TrivialTestB {
	private TupleSpaceEnvironment environment;
	private Client provider;
	private final String tag = "MyTag";
	private final String id = "FirstTuple";
	private JSONQueryUtil queryBuilder;


	/**
	 * 
	 */
	public TrivialTestB() {
		environment = new TupleSpaceEnvironment();
		provider = environment.getProviderEnvironment().getClient();
		queryBuilder = new JSONQueryUtil();
		
		//JSONObject tuple = new JSONObject();
		//tuple.put(IElasticConstants.TUPLE_ID, id);
		//tuple.put(IElasticConstants.CATEGORY, tag);
		IResult r = provider.getNodeAsJSONObject(id, IElasticConstants.INDEX);
		System.out.println("A "+r.getErrorString()+" | "+r.getResultObject());
		JSONObject jo = queryBuilder.term(IElasticConstants.CATEGORY, tag);
		jo = queryBuilder.query(jo);
		System.out.println("Q "+jo.toJSONString());
		r = provider.listObjectsByQuery(jo.toJSONString(), IElasticConstants.INDEX);
		
		System.out.println("B "+r.getErrorString()+" | "+r.getResultObject());
		
	
	}

}
