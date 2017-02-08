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

import java.util.Date;

import org.topicquests.support.api.IResult;
import org.topicquests.tuplespace.TupleSpaceEnvironment;
import org.topicquests.tuplespace.api.ITuple;
import org.topicquests.tuplespace.api.ITupleSpace;
import org.topicquests.tuplespace.api.ITupleSpaceListener;
import org.topicquests.tuplespace.api.ITupleSpaceModel;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class TakeTest implements ITupleSpaceListener {
	private TupleSpaceEnvironment environment;
	private ITupleSpace space;
	private ITupleSpaceModel model;
	private final String tag = Long.toString(System.currentTimeMillis());
	private final String creatorId = "jackpark";

	/**
	 * 
	 */
	public TakeTest() {
		System.out.println("TakeTest-");
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
		System.out.println("READ A");
		space.take(t, Long.MAX_VALUE, null, this);
		System.out.println("READ B");
		t = t.cloneTuple();
		t.setProperty("anotherkey", "testVal");
		t.setId(tag);
		Date d = new Date();
		t.setCreationDate(d);
		t.setSortNumber(d.getTime());
		space.putTuple(t);
		System.out.println("TakeTest+");
	}

	/* (non-Javadoc)
	 * @see org.topicquests.tuplespace.api.ITupleSpaceListener#acceptResult(org.topicquests.common.api.IResult)
	 */
	@Override
	public void acceptResult(IResult result) {
		System.out.println("DID "+result.getResultObject());
		if (result.getResultObject() != null) {
			ITuple t = (ITuple)result.getResultObject();
			System.out.println(t.toJSONString());
		}
	}

}
