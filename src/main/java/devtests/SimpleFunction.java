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

import org.topicquests.support.ResultPojo;
import org.topicquests.support.api.IResult;
import org.topicquests.ets.api.IFunction;

/**
 * @author jackpark
 *
 */
public class SimpleFunction implements IFunction {

	/**
	 * 
	 */
	public SimpleFunction() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IFunction#eval(java.lang.String, java.lang.Object)
	 */
	@Override
	public IResult eval(String streamId, Object param) {
		IResult result = new ResultPojo();
		// TODO Auto-generated method stub
		return result;
	}

}
