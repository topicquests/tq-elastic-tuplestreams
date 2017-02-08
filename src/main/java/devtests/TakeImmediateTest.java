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

/**
 * @author jackpark
 *
 */
public class TakeImmediateTest {
	private TupleSpaceEnvironment environment;
	private ITupleSpace space;
	private ITupleSpaceModel model;
	private final String tag = "foo"; // from an earlier test
	

	/**
	 * 
	 */
	public TakeImmediateTest() {
		System.out.println("ReadTest-");
		environment = new TupleSpaceEnvironment();
		space = environment.getTupleSpace();
		model = environment.getModel();
		//craft a tuple and a template to fetch it
		ITuple t = model.newTupleTemplate(tag);
		t.setCargoType("TestType");
		IResult r = space.takeImmediate(t.cloneTuple(), null);
		System.out.println("A "+r.getErrorString()+" | "+r.getResultObject());
	}
//FirstPass
//A  | org.topicquests.tuplespace.Tuple@2aa3cd93
//SecondPass
//A  | null
}
