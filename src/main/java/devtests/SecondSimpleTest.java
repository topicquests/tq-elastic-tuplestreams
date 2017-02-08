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

/**
 * @author jackpark
 *
 */
public class SecondSimpleTest {
	private TupleSpaceEnvironment environment;
	private ITupleSpace space;
	//from FirstSimpleTest
	private final String id = "ab653aea-cdd7-4590-8e79-f843be8bda4b";
			

	/**
	 * 
	 */
	public SecondSimpleTest() {
		System.out.println("SecondSimpleTest-");
		environment = new TupleSpaceEnvironment();
		space = environment.getTupleSpace();
		IResult r = space.getTupleById(id, null);
		System.out.println("Did "+r.getErrorString()+" | "+r.getResultObject());
		if (r.getResultObject() != null) {
			ITuple t = (ITuple)r.getResultObject();
			System.out.println(t.toJSONString());

		}

		System.out.println("SecondSimpleTest+");

	}

}
/**
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
