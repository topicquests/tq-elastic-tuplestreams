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
import org.topicquests.es.util.ElasticQueryUtility;
import org.topicquests.node.provider.Client;
import org.topicquests.tuplespace.TupleSpaceEnvironment;
import org.topicquests.tuplespace.api.IElasticConstants;
import org.topicquests.tuplespace.api.ITupleSpace;

/**
 * @author jackpark
 *
 */
public class PostSequenceTest {
	private TupleSpaceEnvironment environment;
	private ITupleSpace space;
	private Client client;
	private ElasticQueryUtility util;
	private final String tag = "mystream";

	/**
	 * 
	 */
	public PostSequenceTest() {
		System.out.println("PostSequenceTest-");
		environment = new TupleSpaceEnvironment();
		client = environment.getProviderEnvironment().getClient();
		util = new ElasticQueryUtility();
		space = environment.getTupleSpace();
		String q = util.getMatchQuery(IElasticConstants.CATEGORY, tag);
		System.out.println("Q "+q);
		IResult r = client.listObjectsByQuery(q, IElasticConstants.INDEX);
		System.out.println("DID "+r.getErrorString()+" | "+r.getResultObject());
		System.out.println("PostSequenceTest-");

	}

}
