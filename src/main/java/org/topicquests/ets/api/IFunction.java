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

package org.topicquests.ets.api;

import org.topicquests.support.api.IResult;

/**
 * @author jackpark
 *
 */
public interface IFunction {

	/**
	 * Implementations of this generally operate on an IStream identified by
	 * <code>streamId</code> and are free to interpret <code>param</code>
	 * any way desired. An IFunction can be passed into any procedure.
	 * @param streamId
	 * @param param
	 * @return
	 */
	IResult eval(String streamId, Object param);
}
