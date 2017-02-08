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

import org.elasticsearch.index.query.QueryBuilder;
import org.topicquests.es.util.ElasticQueryUtility;
import org.topicquests.es.util.JSONQueryUtil;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class QueryBuilderTest {
	//private ElasticQueryUtility util;
	private JSONQueryUtil queryBuilder;
	/**
	 * 
	 */
	public QueryBuilderTest() {
		//util = new ElasticQueryUtility();
		queryBuilder = new JSONQueryUtil();
		/**
		QueryBuilder b = util.startKeywordQuery("foo", "bar");
		System.out.println("A "+b.toString());
		
		 {
  			"term" : {
    		"foo" : "bar"
  			}
		 }
		 * /
		JSONArray ja = new JSONArray();
		JSONObject theQ = newQ("foo", "bar");
		JSONObject term = newQ("term", theQ);
		ja.add(term);
		theQ = newQ("bah", "blah");
		term = newQ("term", theQ);
		ja.add(term);
		term = newQ("must", ja);
		theQ = newQ("bool", term);
		JSONObject root = newQ("query", theQ);
		System.out.println(root.toJSONString());
		{
			"query": {
				"bool": {
					"must": [{
						"term": {
							"foo": "bar"
						}
					}, {
						"term": {
							"bah": "blah"
						}
					}]
				}
			}
		}
		* /
		JSONArray musts = queryBuilder.createJA();
		JSONObject jo = queryBuilder.term("foo", "bar");
		musts.add(jo);
		jo = queryBuilder.term("bah", "blah");
		musts.add(jo);
		jo = queryBuilder.must(musts);
		jo = queryBuilder.bool(jo);
		jo = queryBuilder.query(jo);
		System.out.println(jo.toJSONString());
		{
			"query": {
				"bool": {
					"must": [{
						"term": {
							"foo": "bar"
						}
					}, {
						"term": {
							"bah": "blah"
						}
					}]
				}
			}
		}
		*/
	}

	JSONObject newQ(String key, JSONObject val) {
		JSONObject jo = new JSONObject();
		jo.put(key, val);
		return jo;
	}
	JSONObject newQ(String key, JSONArray val) {
		JSONObject jo = new JSONObject();
		jo.put(key, val);
		return jo;
	}

	JSONObject newQ(String key, String val) {
		JSONObject jo = new JSONObject();
		jo.put(key, val);
		return jo;
	}
}
