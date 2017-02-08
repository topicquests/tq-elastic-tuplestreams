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
import org.topicquests.tuplespace.api.IElasticConstants;
import org.topicquests.tuplespace.api.ISequenceIterator;
import org.topicquests.tuplespace.api.ITuple;
import org.topicquests.tuplespace.api.ITupleSpace;
import org.topicquests.tuplespace.api.ITupleSpaceModel;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class SequenceIteratorTest {
	private TupleSpaceEnvironment environment;
	private ITupleSpace space;
	private ITupleSpaceModel model;
	private final String tag = "mystream";
	private final String creatorId = "jackpark";

	/**
	 * 
	 */
	public SequenceIteratorTest() {
		System.out.println("SequenceIteratorTest-");
		environment = new TupleSpaceEnvironment();
		space = environment.getTupleSpace();
		model = environment.getModel();
		ITuple t;
		//craft some tuples
		for (int i=0;i<25;i++) {
			t = model.newTuple(tag, creatorId, false);
			t.setSortNumber((long)i);
			space.putTuple(t);
		}
		//wait a while
		try {
			environment.wait(10000);
		} catch (Exception e) {}
		JSONObject jo;
		ISequenceIterator itr = model.createSequenceIterator();
		IResult r = itr.startIterator(IElasticConstants.CATEGORY, tag, 0, -1, IElasticConstants.SORT_DATE, true, null);
		jo = (JSONObject)r.getResultObject();
		System.out.println("A "+jo);
		while (jo != null) {
			System.out.println(jo.toJSONString());
			r = itr.next();
			jo = (JSONObject)r.getResultObject();
			
		}
		System.out.println("SequenceIteratorTest+");
	}

}
/** Crafted tuples

PutTuple {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"52de83df-8a39-4364-8782-1c89c7d114d6","srtDt":0}
PutTuple {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"fa030356-c0fa-4956-8b0d-d22ed482ff29","srtDt":1}
PutTuple {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"bbfdf8d8-a846-45d3-bc58-4ad38a8e885b","srtDt":2}
PutTuple {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"165a02d9-3b58-4536-8f67-e0ed77d02473","srtDt":3}
PutTuple {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"4de7b90f-5a38-4fee-b3c1-ff40c46dbcc9","srtDt":4}
PutTuple {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"79bd7f98-2338-4bb8-9e06-99c078e830af","srtDt":5}
PutTuple {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"0b63ab3b-c121-4b15-9922-c38a37b68bee","srtDt":6}
PutTuple {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"111dddd3-3553-4058-8885-80bc89353fd2","srtDt":7}
PutTuple {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"2b750b32-3604-43e0-abe0-0b70f7d791b4","srtDt":8}
PutTuple {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"e3d13ebf-9043-4e88-a6dd-1804fc325cc2","srtDt":9}
PutTuple {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"3e0a70c0-0bbd-47b7-9e0a-bb5b40079b5b","srtDt":10}
PutTuple {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"08d89049-0990-41aa-a0ad-3d68c972504d","srtDt":11}
PutTuple {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"7e2731e0-9635-4f35-983b-410b6b321eb6","srtDt":12}
PutTuple {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"1c3948c4-e9d4-4cf8-9427-686a9b9d21fe","srtDt":13}
PutTuple {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"363a4899-dea8-4a12-b984-ba63a35c432c","srtDt":14}
PutTuple {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"b669c87a-9c2c-49ed-9ecb-cbdd06d82383","srtDt":15}
PutTuple {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"2ae96524-dcab-4a69-8ecb-a94dd8bc4b96","srtDt":16}
PutTuple {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"3af9da77-e7de-4620-9379-d5bc8c83c21d","srtDt":17}
PutTuple {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"df5d4f09-0b45-4041-890e-4f1eb663b337","srtDt":18}
PutTuple {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"cb89f4df-710e-418a-86dc-aec0f145c1f9","srtDt":19}
PutTuple {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"b4f8f151-5c97-4367-beea-3534ee2646f5","srtDt":20}
PutTuple {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"8751f079-4878-4f7c-8d38-d31e649cbc8d","srtDt":21}
PutTuple {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"7cc330e4-85be-4df2-b4a0-953e38ce40f9","srtDt":22}
PutTuple {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"b85cf7f1-da68-4a66-97f5-f7dcf711706c","srtDt":23}
PutTuple {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"d42736d6-277a-4620-b9e2-14d3d602c669","srtDt":24}
 * starting query
ITERATORQ {
  "from" : 0,
  "size" : 20,
  "query" : {
    "match" : {
      "cat" : {
        "query" : "mystream",
        "type" : "boolean"
      }
    }
  },
  "sort" : [ {
    "srtDt" : {
      "order" : "asc"
    }
  } ]
}
Client.listObjectsByQuery {"took":13,"timed_out":false,"_shards":{"total":5,"successful":5,"failed":0},"hits":{"total":25,"max_score":null,"hits":[{"_index":"tuples","_type":"core","_id":"52de83df-8a39-4364-8782-1c89c7d114d6","_score":null,"_source":{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"52de83df-8a39-4364-8782-1c89c7d114d6","srtDt":0},"sort":[0]},{"_index":"tuples","_type":"core","_id":"fa030356-c0fa-4956-8b0d-d22ed482ff29","_score":null,"_source":{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"fa030356-c0fa-4956-8b0d-d22ed482ff29","srtDt":1},"sort":[1]},{"_index":"tuples","_type":"core","_id":"bbfdf8d8-a846-45d3-bc58-4ad38a8e885b","_score":null,"_source":{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"bbfdf8d8-a846-45d3-bc58-4ad38a8e885b","srtDt":2},"sort":[2]},{"_index":"tuples","_type":"core","_id":"165a02d9-3b58-4536-8f67-e0ed77d02473","_score":null,"_source":{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"165a02d9-3b58-4536-8f67-e0ed77d02473","srtDt":3},"sort":[3]},{"_index":"tuples","_type":"core","_id":"4de7b90f-5a38-4fee-b3c1-ff40c46dbcc9","_score":null,"_source":{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"4de7b90f-5a38-4fee-b3c1-ff40c46dbcc9","srtDt":4},"sort":[4]},{"_index":"tuples","_type":"core","_id":"79bd7f98-2338-4bb8-9e06-99c078e830af","_score":null,"_source":{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"79bd7f98-2338-4bb8-9e06-99c078e830af","srtDt":5},"sort":[5]},{"_index":"tuples","_type":"core","_id":"0b63ab3b-c121-4b15-9922-c38a37b68bee","_score":null,"_source":{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"0b63ab3b-c121-4b15-9922-c38a37b68bee","srtDt":6},"sort":[6]},{"_index":"tuples","_type":"core","_id":"111dddd3-3553-4058-8885-80bc89353fd2","_score":null,"_source":{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"111dddd3-3553-4058-8885-80bc89353fd2","srtDt":7},"sort":[7]},{"_index":"tuples","_type":"core","_id":"2b750b32-3604-43e0-abe0-0b70f7d791b4","_score":null,"_source":{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"2b750b32-3604-43e0-abe0-0b70f7d791b4","srtDt":8},"sort":[8]},{"_index":"tuples","_type":"core","_id":"e3d13ebf-9043-4e88-a6dd-1804fc325cc2","_score":null,"_source":{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"e3d13ebf-9043-4e88-a6dd-1804fc325cc2","srtDt":9},"sort":[9]},{"_index":"tuples","_type":"core","_id":"3e0a70c0-0bbd-47b7-9e0a-bb5b40079b5b","_score":null,"_source":{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"3e0a70c0-0bbd-47b7-9e0a-bb5b40079b5b","srtDt":10},"sort":[10]},{"_index":"tuples","_type":"core","_id":"08d89049-0990-41aa-a0ad-3d68c972504d","_score":null,"_source":{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"08d89049-0990-41aa-a0ad-3d68c972504d","srtDt":11},"sort":[11]},{"_index":"tuples","_type":"core","_id":"7e2731e0-9635-4f35-983b-410b6b321eb6","_score":null,"_source":{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"7e2731e0-9635-4f35-983b-410b6b321eb6","srtDt":12},"sort":[12]},{"_index":"tuples","_type":"core","_id":"1c3948c4-e9d4-4cf8-9427-686a9b9d21fe","_score":null,"_source":{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"1c3948c4-e9d4-4cf8-9427-686a9b9d21fe","srtDt":13},"sort":[13]},{"_index":"tuples","_type":"core","_id":"363a4899-dea8-4a12-b984-ba63a35c432c","_score":null,"_source":{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"363a4899-dea8-4a12-b984-ba63a35c432c","srtDt":14},"sort":[14]},{"_index":"tuples","_type":"core","_id":"b669c87a-9c2c-49ed-9ecb-cbdd06d82383","_score":null,"_source":{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"b669c87a-9c2c-49ed-9ecb-cbdd06d82383","srtDt":15},"sort":[15]},{"_index":"tuples","_type":"core","_id":"2ae96524-dcab-4a69-8ecb-a94dd8bc4b96","_score":null,"_source":{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"2ae96524-dcab-4a69-8ecb-a94dd8bc4b96","srtDt":16},"sort":[16]},{"_index":"tuples","_type":"core","_id":"3af9da77-e7de-4620-9379-d5bc8c83c21d","_score":null,"_source":{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"3af9da77-e7de-4620-9379-d5bc8c83c21d","srtDt":17},"sort":[17]},{"_index":"tuples","_type":"core","_id":"df5d4f09-0b45-4041-890e-4f1eb663b337","_score":null,"_source":{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"df5d4f09-0b45-4041-890e-4f1eb663b337","srtDt":18},"sort":[18]},{"_index":"tuples","_type":"core","_id":"cb89f4df-710e-418a-86dc-aec0f145c1f9","_score":null,"_source":{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"cb89f4df-710e-418a-86dc-aec0f145c1f9","srtDt":19},"sort":[19]}]}}
Client.listObjectsByQuery-1 null
ITRS  | [{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"52de83df-8a39-4364-8782-1c89c7d114d6","srtDt":0}, {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"fa030356-c0fa-4956-8b0d-d22ed482ff29","srtDt":1}, {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"bbfdf8d8-a846-45d3-bc58-4ad38a8e885b","srtDt":2}, {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"165a02d9-3b58-4536-8f67-e0ed77d02473","srtDt":3}, {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"4de7b90f-5a38-4fee-b3c1-ff40c46dbcc9","srtDt":4}, {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"79bd7f98-2338-4bb8-9e06-99c078e830af","srtDt":5}, {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"0b63ab3b-c121-4b15-9922-c38a37b68bee","srtDt":6}, {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"111dddd3-3553-4058-8885-80bc89353fd2","srtDt":7}, {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"2b750b32-3604-43e0-abe0-0b70f7d791b4","srtDt":8}, {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"e3d13ebf-9043-4e88-a6dd-1804fc325cc2","srtDt":9}, {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"3e0a70c0-0bbd-47b7-9e0a-bb5b40079b5b","srtDt":10}, {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"08d89049-0990-41aa-a0ad-3d68c972504d","srtDt":11}, {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"7e2731e0-9635-4f35-983b-410b6b321eb6","srtDt":12}, {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"1c3948c4-e9d4-4cf8-9427-686a9b9d21fe","srtDt":13}, {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"363a4899-dea8-4a12-b984-ba63a35c432c","srtDt":14}, {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"b669c87a-9c2c-49ed-9ecb-cbdd06d82383","srtDt":15}, {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"2ae96524-dcab-4a69-8ecb-a94dd8bc4b96","srtDt":16}, {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"3af9da77-e7de-4620-9379-d5bc8c83c21d","srtDt":17}, {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"df5d4f09-0b45-4041-890e-4f1eb663b337","srtDt":18}, {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"cb89f4df-710e-418a-86dc-aec0f145c1f9","srtDt":19}]
A org.topicquests.tuplespace.Tuple@1a451d4d
{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"52de83df-8a39-4364-8782-1c89c7d114d6","srtDt":0}
{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"fa030356-c0fa-4956-8b0d-d22ed482ff29","srtDt":1}
{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"bbfdf8d8-a846-45d3-bc58-4ad38a8e885b","srtDt":2}
{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"165a02d9-3b58-4536-8f67-e0ed77d02473","srtDt":3}
{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"4de7b90f-5a38-4fee-b3c1-ff40c46dbcc9","srtDt":4}
{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"79bd7f98-2338-4bb8-9e06-99c078e830af","srtDt":5}
{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"0b63ab3b-c121-4b15-9922-c38a37b68bee","srtDt":6}
{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"111dddd3-3553-4058-8885-80bc89353fd2","srtDt":7}
{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"2b750b32-3604-43e0-abe0-0b70f7d791b4","srtDt":8}
{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"e3d13ebf-9043-4e88-a6dd-1804fc325cc2","srtDt":9}
{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"3e0a70c0-0bbd-47b7-9e0a-bb5b40079b5b","srtDt":10}
{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"08d89049-0990-41aa-a0ad-3d68c972504d","srtDt":11}
{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"7e2731e0-9635-4f35-983b-410b6b321eb6","srtDt":12}
{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"1c3948c4-e9d4-4cf8-9427-686a9b9d21fe","srtDt":13}
{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"363a4899-dea8-4a12-b984-ba63a35c432c","srtDt":14}
{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"b669c87a-9c2c-49ed-9ecb-cbdd06d82383","srtDt":15}
{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"2ae96524-dcab-4a69-8ecb-a94dd8bc4b96","srtDt":16}
{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"3af9da77-e7de-4620-9379-d5bc8c83c21d","srtDt":17}
{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"df5d4f09-0b45-4041-890e-4f1eb663b337","srtDt":18}
{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"cb89f4df-710e-418a-86dc-aec0f145c1f9","srtDt":19}
ITERATORQ {
  "from" : 20,
  "size" : 20,
  "query" : {
    "match" : {
      "cat" : {
        "query" : "mystream",
        "type" : "boolean"
      }
    }
  },
  "sort" : [ {
    "srtDt" : {
      "order" : "asc"
    }
  } ]
}
Client.listObjectsByQuery {"took":5,"timed_out":false,"_shards":{"total":5,"successful":5,"failed":0},"hits":{"total":25,"max_score":null,"hits":[{"_index":"tuples","_type":"core","_id":"b4f8f151-5c97-4367-beea-3534ee2646f5","_score":null,"_source":{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"b4f8f151-5c97-4367-beea-3534ee2646f5","srtDt":20},"sort":[20]},{"_index":"tuples","_type":"core","_id":"8751f079-4878-4f7c-8d38-d31e649cbc8d","_score":null,"_source":{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"8751f079-4878-4f7c-8d38-d31e649cbc8d","srtDt":21},"sort":[21]},{"_index":"tuples","_type":"core","_id":"7cc330e4-85be-4df2-b4a0-953e38ce40f9","_score":null,"_source":{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"7cc330e4-85be-4df2-b4a0-953e38ce40f9","srtDt":22},"sort":[22]},{"_index":"tuples","_type":"core","_id":"b85cf7f1-da68-4a66-97f5-f7dcf711706c","_score":null,"_source":{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"b85cf7f1-da68-4a66-97f5-f7dcf711706c","srtDt":23},"sort":[23]},{"_index":"tuples","_type":"core","_id":"d42736d6-277a-4620-b9e2-14d3d602c669","_score":null,"_source":{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"d42736d6-277a-4620-b9e2-14d3d602c669","srtDt":24},"sort":[24]}]}}
Client.listObjectsByQuery-1 null
ITRS  | [{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"b4f8f151-5c97-4367-beea-3534ee2646f5","srtDt":20}, {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"8751f079-4878-4f7c-8d38-d31e649cbc8d","srtDt":21}, {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"7cc330e4-85be-4df2-b4a0-953e38ce40f9","srtDt":22}, {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"b85cf7f1-da68-4a66-97f5-f7dcf711706c","srtDt":23}, {"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"d42736d6-277a-4620-b9e2-14d3d602c669","srtDt":24}]
{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"b4f8f151-5c97-4367-beea-3534ee2646f5","srtDt":20}
{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"8751f079-4878-4f7c-8d38-d31e649cbc8d","srtDt":21}
{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"7cc330e4-85be-4df2-b4a0-953e38ce40f9","srtDt":22}
{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"b85cf7f1-da68-4a66-97f5-f7dcf711706c","srtDt":23}
{"crtd":"2016-04-26T14:44:56-07:00","crid":"jackpark","cat":"mystream","tid":"d42736d6-277a-4620-b9e2-14d3d602c669","srtDt":24}
ITERATORQ {
  "from" : 25,
  "size" : 20,
  "query" : {
    "match" : {
      "cat" : {
        "query" : "mystream",
        "type" : "boolean"
      }
    }
  },
  "sort" : [ {
    "srtDt" : {
      "order" : "asc"
    }
  } ]
}


 */ 
