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

import java.util.Iterator;

import org.topicquests.support.api.IResult;
import org.topicquests.es.util.JSONQueryUtil;
import org.topicquests.node.provider.Client;
import org.topicquests.tuplespace.TupleSpaceEnvironment;
import org.topicquests.tuplespace.api.IConstants;
import org.topicquests.tuplespace.api.IElasticConstants;
import org.topicquests.tuplespace.api.ITuple;
import org.topicquests.tuplespace.api.ITupleSpace;
import org.topicquests.tuplespace.api.ITupleSpaceModel;
import org.topicquests.tuplespace.api.ISimpleDocument;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class BigQueryTest {
	private TupleSpaceEnvironment environment;
	private ITupleSpace space;
	private ITupleSpaceModel model;
	private JSONQueryUtil queryBuilder;
	private Client provider;


	private final String creatorId = "System";

	/**
	 * 
	 */
	public BigQueryTest() {
		System.out.println("BigQueryTest-");
		environment = new TupleSpaceEnvironment();
		space = environment.getTupleSpace();
		model = environment.getModel();
		queryBuilder = new JSONQueryUtil();
		provider = environment.getProviderEnvironment().getClient();
//MyMainStream
		IResult r = null;
		//craft a tuple and a template to fetch it
		ITuple t = model.newTupleTemplate("");
		((ISimpleDocument)t).setDocumentType(IConstants.TODO_TYPE);
		((ISimpleDocument)t).addStreamId("MyMainStream");
		JSONObject jo = templateToQuery(t);
		r = provider.listObjectsByQuery(jo.toJSONString(), IElasticConstants.INDEX);
		System.out.println("Did "+r.getErrorString()+" "+r.getResultObject());
		System.out.println("BigQueryTest+");
	}

	JSONObject templateToQuery(ITuple template) {
//		System.out.println("TemplateQuery "+template.toJSONString());
		JSONObject result = new JSONObject();
		JSONArray mustmatches = queryBuilder.createJA();
		JSONArray mustterms = queryBuilder.createJA();
		if (!template.getTag().equals(""))
			mustterms.add(queryBuilder.term(IElasticConstants.CATEGORY, template.getTag()));
		if (template.getCargoType() != null)
			mustterms.add(queryBuilder.term(IElasticConstants.CARGO_TYPE, template.getCargoType()));			
		//WE CANNOT QUERY AGAINST CARGO
		JSONObject data = template.getData();
		JSONObject jo;
		//TODO  cargo, etc
		Iterator<String>itr = data.keySet().iterator();
		String key;
		Object val;
		while (itr.hasNext()) {
			key = itr.next();
			System.out.println("KEY "+key);
			//catches cargo and description plus any other properties
			if (!(key.equals(IElasticConstants.CATEGORY) ||
				  key.equals(IElasticConstants.CARGO_TYPE) ||
				  key.equals(IElasticConstants.CARGO) ||
				  key.equals(IElasticConstants.CREATOR_ID))) {
				val = template.getProperty(key);
				if (val instanceof java.util.List) {
					val = ((java.util.List)val).get(0);
				}
				jo = queryBuilder.match(key, val);
				//jo = queryBuilder.query(jo);
				mustmatches.add(jo);
			}
		}
		//JSONObject jx = queryBuilder.query(mustmatches);
		if (mustmatches != null && (mustmatches.size() > 0) )
		mustterms.addAll(mustmatches);
		//must the matches
		//jx = queryBuilder.must(jx);
		//must the terms
		jo = queryBuilder.must(mustterms);

		//boolean query
		jo = queryBuilder.bool(jo);
		//jo = queryBuilder.filter(jo);
		//it's filtered
		//jo = queryBuilder.filtered(jo);
		//the final query
		result.put("query", jo);
System.out.println("TemplateQuery "+result.toJSONString());
		return result;
	}
}
