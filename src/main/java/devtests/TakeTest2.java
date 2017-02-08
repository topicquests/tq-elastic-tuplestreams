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
import org.topicquests.tuplespace.api.ITupleSpaceListener;
import org.topicquests.tuplespace.api.ITupleSpaceModel;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 * <p>One-time test based on previous ReadTest</p>
 */
public class TakeTest2 implements ITupleSpaceListener {
	private TupleSpaceEnvironment environment;
	private ITupleSpace space;
	private ITupleSpaceModel model;
	private final String tag = "1461879094491";//numbers picked from kibana
	private final String creatorId = "jackpark";

	/**
	 * 
	 */
	public TakeTest2() {
		System.out.println("TakeTest2-");
		environment = new TupleSpaceEnvironment();
		space = environment.getTupleSpace();
		model = environment.getModel();
		//craft a tuple and a template to fetch it
		ITuple t = model.newTupleTemplate(tag);
		JSONObject jo = new JSONObject();
		jo.put("hello", "world");
	//	t.setCargo(jo);
		t.setCargoType("testType");
		t.setProperty("testKey", "testVal");
		System.out.println("READ A");
		space.take(t, 0, null, this);
		
		System.out.println("TakeTest2+");
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
/**
TupleQuery {
  "bool" : {
    "must" : [ {
      "term" : {
        "crgtyp" : "testType"
      }
    }, {
      "term" : {
        "testKey" : "testVal"
      }
    } ]
  }
}
*/
