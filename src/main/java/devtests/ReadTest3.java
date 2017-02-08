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
public class ReadTest3 {
	private TupleSpaceEnvironment environment;
	private ITupleSpace space;
	private ITupleSpaceModel model;
	private final String tag = "1461975808094";//numbers picked from kibana
	private final String creatorId = "jackpark";

	/**
	 * 
	 */
	public ReadTest3() {
		System.out.println("ReadTest3-");
		environment = new TupleSpaceEnvironment();
		space = environment.getTupleSpace();
		model = environment.getModel();
		//craft a tuple and a template to fetch it
		ITuple t = model.newTupleTemplate(tag);
		JSONObject jo = new JSONObject();
		jo.put("hello", "world");
		t.setCargo(jo);
		t.setCargoType("testType");
		t.setProperty("testKey", "testVal");
		System.out.println("READ A "+t.toJSONString());
		IResult r = space.readImmediate(t, null);
		System.out.println(r.getErrorString()+" | "+r.getResultObject());
		System.out.println("ReadTest3+");	}

}
