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

package org.topicquests.tuplespace;

import java.util.*;

import org.topicquests.support.ResultPojo;
import org.topicquests.support.api.IResult;
import org.topicquests.ets.Environment;
import org.topicquests.tuplespace.api.IConstants;
import org.topicquests.tuplespace.api.IElasticConstants;
import org.topicquests.tuplespace.api.IElasticTupleStreamsModel;
import org.topicquests.tuplespace.api.ISequenceIterator;
import org.topicquests.tuplespace.api.ISimpleDocument;
import org.topicquests.tuplespace.api.IStream;
import org.topicquests.tuplespace.api.IStreamTuple;
import org.topicquests.tuplespace.api.ITuple;
import org.topicquests.tuplespace.api.ITupleSpace;
import org.topicquests.tuplespace.api.ITupleSpaceModel;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class StreamModel implements IElasticTupleStreamsModel {
	private Environment environment;
	private TupleSpaceEnvironment tsEnvironment;
	private ITupleSpace space;
	private ITupleSpaceModel tsModel;

	/**
	 * 
	 */
	public StreamModel(Environment env) {
		environment = env;
		tsEnvironment = environment.getTupleSpaceEnvironment();
		space = tsEnvironment.getTupleSpace();
		tsModel = tsEnvironment.getModel();
		
	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IElasticTupleStreamsModel#newStream(java.lang.String, java.lang.String, boolean, boolean)
	 */
	@Override
	public IStream newStream(String tag, String creatorId, boolean isPersistent, boolean isPrivate) {
		ITuple t = tsModel.newTuple(tag, creatorId, isPrivate);
		IStream result = (IStream)t;
		return result;
	}
	
	@Override
	public IStream newStream(String id, String tag, String creatorId, boolean isPersistent, boolean isPrivate) {
		IStream result = newStream(tag, creatorId, isPersistent, isPrivate);
		result.setId(id);
		return result;
	}


	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IElasticTupleStreamsModel#getStream(java.lang.String, java.lang.String)
	 */
	@Override
	public IResult getStream(String streamId, String userId) {
		IResult result = tsModel.getTuple(streamId, userId);
		ITuple t = (ITuple)result.getResultObject();
		if (t != null)
			result.setResultObject(t);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IElasticTupleStreamsModel#removeStream(java.lang.String)
	 */
	@Override
	public IResult removeStream(String streamId) {
		return space.removeTuple(streamId);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IElasticTupleStreamsModel#getStreamIterator()
	 */
	@Override
	public ISequenceIterator getStreamIterator() {
		return tsModel.createSequenceIterator();
	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IElasticTupleStreamsModel#newStreamTuple(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public IStreamTuple newStreamTuple(String tag, String streamId, String creatorId, boolean isPrivate) {
		ITuple t = tsModel.newTuple(tag, creatorId, isPrivate);
		IStreamTuple result = (IStreamTuple)t;
		result.setStreamId(streamId);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IElasticTupleStreamsModel#getStreamTuple(java.lang.String, java.lang.String)
	 */
	@Override
	public IResult getStreamTuple(String id, String userId) {
		IResult result = tsModel.getTuple(id, userId);
		ITuple t = (ITuple)result.getResultObject();
		if (t != null)
			result.setResultObject(t);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IElasticTupleStreamsModel#removeStreamTuple(java.lang.String)
	 */
	@Override
	public IResult removeStreamTuple(String id) {
		return space.removeTuple(id);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IElasticTupleStreamsModel#getTupleSpaceModel()
	 */
	@Override
	public ITupleSpaceModel getTupleSpaceModel() {
		return tsModel;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IElasticTupleStreamsModel#getTupleSpace()
	 */
	@Override
	public ITupleSpace getTupleSpace() {
		return space;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IElasticTupleStreamsModel#extract(java.lang.String, java.lang.String)
	 */
	@Override
	public IResult extract(String docId, String userId, String attribute) {
		IResult result = space.getTupleById(docId, userId);
		ITuple t = (ITuple)(result.getResultObject());
		if (t != null) 
			result.setResultObject(t.getProperty(attribute));
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IElasticTupleStreamsModel#replace(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult replace(String docId, String userId, String attribute, String value) {
		IResult result = space.getTupleById(docId, userId);
		ITuple t = (ITuple)(result.getResultObject());
		if (t != null) {
			t.setProperty(attribute, value);
			result = space.putTuple(t);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IElasticTupleStreamsModel#freeze(java.lang.String)
	 */
	@Override
	public IResult freeze(String streamTupleId, String userId) {
		IResult result = space.getTupleById(streamTupleId, userId);
		ITuple t = (ITuple)(result.getResultObject());
		if (t != null) {
			IStreamTuple st = (IStreamTuple)t;
			st.freeze();
			result = space.putTuple(st);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IElasticTupleStreamsModel#append(java.lang.String, org.topicquests.ets.document.api.ISimpleDocument)
	 */
	@Override
	public IResult append(String streamId, String userId, boolean isPrivate, ISimpleDocument jsonDoc) {
		IResult result = this.getStream(streamId, userId);
		IStream s = (IStream)result.getResultObject();
		IStreamTuple st = this.newStreamTuple(s.getTag(), streamId, userId, isPrivate);
		st.setCargoType(IConstants.CARGO_AIR_TYPE);
		st.setCargo(jsonDoc.getId());
		IResult r = space.putTuple(st);
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		result.setResultObject(st);
		jsonDoc.addStreamId(streamId);
		r = space.putTuple(jsonDoc);
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IElasticTupleStreamsModel#filter(org.topicquests.tuplespace.api.ITuple)
	 */
	@Override
	public IResult filter(String parentStreamId, String newTag, ITuple query, String userId, boolean isPersistent, boolean isPrivate) {
		IResult result = new ResultPojo();
		
		ISequenceIterator itr = this.getStreamIterator();
		IResult r = itr.startIterator(query, 0, -1, IElasticConstants.SORT_DATE, true, userId);
		if (r.getResultObject() != null) {
			IStream str = this.newStream(newTag, userId, isPersistent, isPrivate);
			if (parentStreamId != null)
				str.setParentStreamId((String)query.getProperty(IConstants.PARENT_STREAM_ID));
			String streamId = str.getId();
			IResult x = space.putTuple(str);
			if (x.hasError())
				result.addErrorString(x.getErrorString());
			result.setResultObject(str);
			ITuple t;
			while ((t = (ITuple)r.getResultObject()) != null)	{
				if (r.hasError())
					result.addErrorString(r.getErrorString());
				((ISimpleDocument)t).addStreamId(streamId);
				x = space.putTuple(t);
				if (x.hasError())
					result.addErrorString(x.getErrorString());
				r = itr.next();
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IElasticTupleStreamsModel#find(java.lang.String, java.lang.String, org.topicquests.tuplespace.api.ITuple)
	 */
	@Override
	public IResult find(String streamId, String pid, ITuple query) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IElasticTupleStreamsModel#newSimpleDocument(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public ISimpleDocument newSimpleDocument(String documentType, String tag, String userId, boolean isPrivate) {
		ITuple t = tsModel.newTuple(tag, userId, isPrivate);
		ISimpleDocument result = (ISimpleDocument)t;
		result.setDocumentType(documentType);
		//initialize the lastReadDate
		result.setLastReadDate(new Date());
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IElasticTupleStreamsModel#getSimpleDocument(java.lang.String, java.lang.String)
	 */
	@Override
	public IResult getSimpleDocument(String docId, String userId) {
		IResult result = tsModel.getTuple(docId, userId);
		ITuple t = (ITuple)result.getResultObject();
		if (t != null)
			result.setResultObject(t);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IElasticTupleStreamsModel#findDocument(org.topicquests.tuplespace.api.ITuple, java.lang.String)
	 */
	@Override
	public IResult findDocument(ITuple template, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	/////////////
	//TODO
	// NEED a mechanism to permanentaly install the query template in
	// tuplecache to keep this stream live
	////////////
/*******
{
	"query": {
		"filtered": {
			"filter": {
				"bool": {
					"must": [{
						"query": {
							"match": {
								"psid": "MyMainStream"
							}
						}
					}, {
						"query": {
							"match": {
								"dctyp": "blogType"
							}
						}
					}]
				}
			}
		}
	},
	"sort": {
		"crDt": "asc"
	}
}
 */
	@Override
	public IResult spawnNewStream(String parentStreamId, String streamTag, String nodeType, String tupleTag,
			String sortDate, boolean increasing, String userId) {
		IResult result = new ResultPojo();
		ITuple t = tsModel.newTupleTemplate(tupleTag);
		ISimpleDocument template = (ISimpleDocument)t;
		if (!nodeType.equals(""))
			template.setDocumentType(nodeType);
		template.addStreamId(parentStreamId);
		IResult r;
		IStream parentStream = null;
		IStream newStream = this.newStream(streamTag, userId, true, false);
		if (parentStreamId != null) {
			// we must update the parentStream tuple
			newStream.setParentStreamId(parentStreamId);
			r = this.getStream(parentStreamId, userId);
			if (r.hasError())
				result.addErrorString(r.getErrorString());
			parentStream = (IStream)r.getResultObject();
			//assume not null
			parentStream.addChildStreamId(newStream.getId());
			parentStream.update();
			r = space.updateTuple(parentStreamId, parentStream.getData(), false);
			if (r.hasError())
				result.addErrorString(r.getErrorString());
			r = space.putTuple(newStream);
			if (r.hasError())
				result.addErrorString(r.getErrorString());
		}
		r = space.putTuple(newStream);
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		ISequenceIterator itr = tsModel.createSequenceIterator();
		r = itr.startIterator(template, 0, -1, sortDate, increasing, userId);
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		JSONObject jo;
		ISimpleDocument sd;
		IStreamTuple st;
		while (r.getResultObject() != null) {
			jo = (JSONObject)r.getResultObject();
			sd = new Tuple(jo);
			st = this.newStreamTuple(streamTag, newStream.getId(), userId, false);
			st.setCargo(sd.getId());
			st.setCargoType(sd.getDocumentType());
			sd.addStreamId(st.getId());
			r = space.updateTuple(sd.getId(), sd.getData(), false);
			if (r.hasError())
				result.addErrorString(r.getErrorString());
			r = space.putTuple(st);
			if (r.hasError())
				result.addErrorString(r.getErrorString());
		}		
		result.setResultObject(newStream);
		return result;
	}

}
