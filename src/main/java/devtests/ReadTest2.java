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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.topicquests.support.api.IResult;
import org.topicquests.tuplespace.TupleSpaceEnvironment;
import org.topicquests.tuplespace.api.ITuple;
import org.topicquests.tuplespace.api.ITupleSpace;
import org.topicquests.tuplespace.api.ITupleSpaceListener;
import org.topicquests.tuplespace.api.ITupleSpaceModel;

import net.minidev.json.JSONObject;

/**
 * 
 * @author jackpark
 *
 */
public class ReadTest2 implements ITupleSpaceListener {
	private TupleSpaceEnvironment environment;
	private ITupleSpace space;
	private ITupleSpaceModel model;
	private final String tag = Long.toString(System.currentTimeMillis());
	private final String creatorId = "jackpark";
	private ITuple t;
	
	public ReadTest2() {
		System.out.println("ReadTest2-");
		environment = new TupleSpaceEnvironment();
		space = environment.getTupleSpace();
		model = environment.getModel();
		//craft a tuple and a template to fetch it
		t = model.newTupleTemplate(tag);
		JSONObject jo = new JSONObject();
		jo.put("hello", "world");
		jo.put("some", "one");
		List<String>foo = new ArrayList<String>();
		foo.add("foobar");
		foo.add("another thing");
		jo.put("alist", foo);
		t.setCargo(jo);
		t.setCargoType("testType");
		t.setProperty("testKey", "testVal");
		System.out.println("READ A");
		space.read(t, Long.MAX_VALUE, creatorId, this);
		System.out.println("READ B");
		t = t.cloneTuple();
		t.setProperty("anotherkey", "testVal");
		t.setId(tag);
		Date d = new Date();
		t.setCreationDate(d);
		t.setSortNumber(d.getTime());
		space.putTuple(t);
		System.out.println("ReadTest2+");
	}
	
	boolean alreadyDid = false;
	@Override
	public void acceptResult(IResult result) {
		System.out.println("DID "+alreadyDid+" "+result.getResultObject());
		if (!alreadyDid && result.getResultObject() != null) {
			ITuple t = (ITuple)result.getResultObject();
			System.out.println(t.toJSONString());
			alreadyDid = true; // avoid endless loop
			//read again from disk
			space.read(t, 0, creatorId, this);
		} else {
			System.out.println("DidGet");
			result = space.getTupleById(tag, creatorId);
			if (result.getResultObject() != null) {
				ITuple t = (ITuple)result.getResultObject();
				System.out.println("X "+t.toJSONString());
			}
		}
	}

}
