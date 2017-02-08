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
import org.topicquests.tuplespace.TupleSpaceEnvironment;
import org.topicquests.tuplespace.api.ITuple;
import org.topicquests.tuplespace.api.ITupleSpace;
import org.topicquests.tuplespace.api.ITupleSpaceModel;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class FirstSimpleTest {
	private TupleSpaceEnvironment environment;
	private ITupleSpace space;
	private ITupleSpaceModel model;
	/**
	 * 
	 */
	public FirstSimpleTest() {
		System.out.println("FirstSimpleTest-");
		environment = new TupleSpaceEnvironment();
		space = environment.getTupleSpace();
		model = environment.getModel();
		ITuple t = model.newTuple("foo", "jackpark", false);
		JSONObject jo = new JSONObject();
		jo.put("key1", "first value");
		jo.put("key2", "second value");
		t.setCargo(jo);
		t.setCargoType("TestType");
		IResult r = space.putTuple(t);
		System.out.println("did "+r.getErrorString());
		System.out.println(t.toJSONString());
		
		System.out.println("FirstSimpleTest+");
	}

}
/**
 * ERROR MapperParsingException[failed to parse [cargo]]; nested: IllegalArgumentException[unknown property [key1]];
 * fixed by setting enabled:false on cargo
{
	"crtd": "2016-04-24T16:53:16-07:00",
	"crid": "jackpark",
	"crgtyp": "TestType",
	"cargo": {
		"key1": "first value",
		"key2": "second value"
	},
	"tid": "ab653aea-cdd7-4590-8e79-f843be8bda4b",
	"srtDt": 1461541996169
}
 */
