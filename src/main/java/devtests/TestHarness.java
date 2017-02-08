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

/**
 * @author jackpark
 *
 */
public class TestHarness {

	/**
	 * 
	 */
	public TestHarness() {
		System.out.println("DEVSTART");
		//new FirstSimpleTest();
		//new SecondSimpleTest();
		//new SequenceIteratorTest();
		//new PostSequenceTest();
		//new JodaTimeTest();
		//new ReadTest();
		//new TakeTest();
		//new TakeTest2();
		//new ReadTest2();
		//new QueryBuilderTest();
		//new ReadTest3();
		//new TrivialTestA();
		//new TrivialTestB();
		//new TakeImmediateTest();
		//new ConcurrentTest();
		new BigQueryTest();
		System.out.println("DEVEND");
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestHarness();
	}

}