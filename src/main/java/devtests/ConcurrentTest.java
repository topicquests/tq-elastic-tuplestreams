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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class ConcurrentTest {

	/**
	 * 
	 */
	public ConcurrentTest() {
		Map<String, Object> foo = new ConcurrentHashMap();
		foo.put("Hello", "World");
		foo.put("Foo", "Bar");
		JSONObject jo = new JSONObject(foo);
		System.out.println(jo.toJSONString());
		//{"Hello":"World","Foo":"Bar"}
	}

}
