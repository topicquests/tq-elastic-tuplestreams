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

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.topicquests.tuplespace.api.ITuple;
import org.topicquests.support.util.DateUtil;
import org.topicquests.es.util.UniversalTimeStamp;
import org.topicquests.tuplespace.api.IConstants;
import org.topicquests.tuplespace.api.IElasticConstants;
import org.topicquests.tuplespace.api.ISimpleDocument;
import org.topicquests.tuplespace.api.IStream;
import org.topicquests.tuplespace.api.IStreamTuple;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class Tuple implements 
		ITuple, IStream, IStreamTuple, ISimpleDocument {
	protected Map<String, Object> data;
	/**
	 * 
	 */
	public Tuple() {
		data = new ConcurrentHashMap();
	}
	
	public Tuple(JSONObject d) {
		data = new ConcurrentHashMap(d);
	}
	
	///////////////////////
	// ITuple
	///////////////////////


	/* (non-Javadoc)
	 * @see org.topicquests.tuplespace.api.IIdentifiable#getId()
	 */
	@Override
	public String getId() {
		return (String)data.get(IElasticConstants.TUPLE_ID);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.tuplespace.api.IIdentifiable#setId()
	 */
	@Override
	public void setId(String id) {
		data.put(IElasticConstants.TUPLE_ID, id);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.tuplespace.api.IIdentifiable#getCreationDate()
	 */
	@Override
	public Date getCreationDate() {
		Object o = data.get(IElasticConstants.CREATED_DATE);
		System.out.println("CD "+o);
		//return DateUtil.fromIso8601(Long.toString(dx.getTime()));//ateUtil.fromIso8601(dx);
		return new Date();
	}
	@Override
	public Date getLastEditDate() {
		Date dx = (Date)data.get(IElasticConstants.LAST_EDIT_DATE_PROPERTY);
		return DateUtil.fromIso8601(Long.toString(dx.getTime()));//DateUtil.fromIso8601(dx);
	}


	/* (non-Javadoc)
	 * @see org.topicquests.tuplespace.api.IIdentifiable#setCreationDate(java.util.Date)
	 */
	@Override
	public void setCreationDate(Date date) {
		data.put(IElasticConstants.CREATED_DATE, date);
	}
	
	@Override
	public void setLastEditDate(Date d) {
		data.put(IElasticConstants.LAST_EDIT_DATE_PROPERTY, d);
	}


	/* (non-Javadoc)
	 * @see org.topicquests.tuplespace.api.IIdentifiable#getCreatorId()
	 */
	@Override
	public String getCreatorId() {
		return (String)data.get(IElasticConstants.CREATOR_ID);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.tuplespace.api.IIdentifiable#setCreatorId(java.lang.String)
	 */
	@Override
	public void setCreatorId(String id) {
		data.put(IElasticConstants.CREATOR_ID, id);

	}

	/* (non-Javadoc)
	 * @see org.topicquests.tuplespace.api.IIdentifiable#getDescription()
	 */
	@Override
	public String getDescription() {
		return (String)data.get(IElasticConstants.DESCRIPTION);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.tuplespace.api.IIdentifiable#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		data.put(IElasticConstants.DESCRIPTION, description);

	}

	/* (non-Javadoc)
	 * @see org.topicquests.tuplespace.api.ITuple#setCargo(net.minidev.json.JSONObject)
	 */
	@Override
	public void setCargo(Object cargo) {
		data.put(IElasticConstants.CARGO, cargo);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.tuplespace.api.ITuple#getCargo()
	 */
	@Override
	public JSONObject getCargo() {
		return (JSONObject)data.get(IElasticConstants.CARGO);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.tuplespace.api.ITuple#getCargoType()
	 */
	@Override
	public String getCargoType() {
		return (String)data.get(IElasticConstants.CARGO_TYPE);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.tuplespace.api.ITuple#setCargoType(java.lang.String)
	 */
	@Override
	public void setCargoType(String type) {
		data.put(IElasticConstants.CARGO_TYPE, type);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.tuplespace.api.ITuple#setProperty(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setProperty(String key, Object value) {
		data.put(key, value);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.tuplespace.api.ITuple#getProperty(java.lang.String)
	 */
	@Override
	public Object getProperty(String key) {
		return data.get(key);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.tuplespace.api.ITuple#toJSONString()
	 */
	@Override
	public String toJSONString() {
		return new JSONObject(data).toJSONString();
	}

	/* (non-Javadoc)
	 * @see org.topicquests.tuplespace.api.ITuple#getData()
	 */
	@Override
	public JSONObject getData() {
		return new JSONObject(data);
	}

	@Override
	public void setSortNumber(long number) {
		data.put(IElasticConstants.SORT_DATE, number);
	}

	@Override
	public long getSortNumber() {
		Long dx = (Long)data.get(IElasticConstants.SORT_DATE);
		return dx.longValue();
	}

	@Override
	public void setTag(String tag) {
		data.put(IElasticConstants.CATEGORY, tag);
	}

	@Override
	public String getTag() {
		return (String)data.get(IElasticConstants.CATEGORY);
	}

	@Override
	public void setAllowPartialMatch(boolean tf) {
		String t = (tf ? "T" : "F");
		data.put(IElasticConstants.ALLOW_PARTIAL_MATCH, t);
	}

	@Override
	public boolean getAllowPartialMatch() {
		String tf = (String)data.get(IElasticConstants.ALLOW_PARTIAL_MATCH);
		if (tf == null)
			return true; //defaults true
		return tf.equals("T");
	}

	@Override
	public boolean matches(ITuple template) {
		System.out.println("MTUP "+this.toJSONString());
		System.out.println("MTMP "+template.toJSONString());
		String id = this.getId();
		String oid = template.getId();
		boolean sameid = (id != null && oid != null &&
						   id.equals(oid));
		if (sameid)
			return true;
		boolean isPartialMatch = template.getAllowPartialMatch();
		String t = this.getTag();
		String ot = template.getTag();
		boolean teq = ((t == null && ot == null) ||
				 		(t != null && ot != null &&
				 		 t.equals(ot)));
		if (!teq)
			return false;
		JSONObject odata = template.getData();
		//The rules of partial match mean:
		//  if template has a field this doesn't, no match
		//  if this has a field template doesn't, it's ok
		//  all fields which both have must match
		int thissize = data.size();
		int osize = odata.size();
		if (!isPartialMatch && thissize > osize)
			return false;
		//OK, same size or template is smaller -- look at all fields
		Iterator<String>itr = odata.keySet().iterator();
		String key;
		Object o, oo;
		while (itr.hasNext()) {
			key = itr.next();
			o = data.get(key);
			//o is a value from this that other has
			if (o == null)
				return false; //missing key here
			oo = odata.get(key);
			if (!oo.equals(o)) //TODO can this work?
				return false;
		}
		return true; 
	}

	@Override
	public ITuple cloneTuple() {
		//shallow copy
		JSONObject jo = new JSONObject();
		Iterator<String>itr = data.keySet().iterator();
		String key;
		while (itr.hasNext()) {
			key = itr.next();
			jo.put(key, data.get(key));
		}
		return new Tuple(jo);
	}

	@Override
	public void setIsPrivate(boolean isPrivate) {
		data.put(IElasticConstants.IS_PRIVATE_BOOLEAN, isPrivate);
	}

	@Override
	public boolean getIsPrivate() {
		Boolean tf = (Boolean)data.get(IElasticConstants.IS_PRIVATE_BOOLEAN);
		if (tf != null)
			return tf.booleanValue();
		return false; //default value
	}

	@Override
	public void addACL(String id) {
		List<String>l = (List<String>)data.get(IElasticConstants.ACL_LIST);
		if (l == null) l = new ArrayList<String>();
		l.add(id);
		data.put(IElasticConstants.ACL_LIST, l);
	}

	@Override
	public List<String> listACLs() {
		return (List<String>)data.get(IElasticConstants.ACL_LIST);
	}

	@Override
	public boolean containsACL(String id) {
		List<String> l = listACLs();
		if (l != null)
			return l.contains(id);
		return false;
	}

	@Override
	public void update() {
		Date d = new Date();
		this.setLastEditDate(d);
		this.setVersion(Long.toString(d.getTime()));
	}

	@Override
	public void setVersion(String version) {
		data.put(IElasticConstants.VERSION, version);
	}

	@Override
	public String getVersion() {
		return (String)data.get(IElasticConstants.VERSION);
	}

	@Override
	public String getSortDateString() {
		Long l = (Long)data.get(IElasticConstants.SORT_DATE);
		return UniversalTimeStamp.timeStampToString(l.longValue());
	}

	@Override
	public String getCreationDateString() {
		return (String)data.get(IElasticConstants.CREATED_DATE);
	}

	@Override
	public String getLastEditDateString() {
		return (String)data.get(IElasticConstants.LAST_EDIT_DATE_PROPERTY);
	}
	///////////////////////
	// ISimpleDocument
	///////////////////////
	/* (non-Javadoc)
	 * @see org.topicquests.tuplespace.api.ISimpleDocument#setLastReadDate(java.util.Date)
	 */
	@Override
	public void setLastReadDate(Date d) {
		data.put(IConstants.LAST_READ_DATE, d);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.tuplespace.api.ISimpleDocument#getLastReadDate()
	 */
	@Override
	public Date getLastReadDate() {
		Date dx = (Date)data.get(IConstants.LAST_READ_DATE);
		return DateUtil.fromIso8601(Long.toString(dx.getTime()));
	}

	/* (non-Javadoc)
	 * @see org.topicquests.tuplespace.api.ISimpleDocument#setSubject(java.lang.String)
	 */
	@Override
	public void setSubject(String subject) {
		data.put(IConstants.SUBJECT, subject);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.tuplespace.api.ISimpleDocument#getSubject()
	 */
	@Override
	public String getSubject() {
		return (String)data.get(IConstants.SUBJECT);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.tuplespace.api.ISimpleDocument#setBody(java.lang.String)
	 */
	@Override
	public void setBody(String body) {
		data.put(IConstants.BODY, body);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.tuplespace.api.ISimpleDocument#getBody()
	 */
	@Override
	public String getBody() {
		return (String)data.get(IConstants.BODY);
	}

	@Override
	public List<String> listStreamIds() {
		List<String>l = (List<String>)data.get(IConstants.STREAM_ID_LIST);
		return l;
	}

	@Override
	public void addStreamId(String id) {
		List<String>l = (List<String>)data.get(IConstants.STREAM_ID_LIST);
		if (l == null) l = new ArrayList<String>();
		l.add(id);
		data.put(IConstants.STREAM_ID_LIST, l);
		
	}

	@Override
	public void removeStreamId(String id) {
		List<String>l = (List<String>)data.get(IConstants.STREAM_ID_LIST);
		if (l != null) {
			l.remove(id);
			data.put(IConstants.STREAM_ID_LIST, l);
		}				
	}

	@Override
	public void setDocumentType(String t) {
		data.put(IConstants.DOC_TYPE, t);
	}

	@Override
	public String getDocumentType() {
		return (String)data.get(IConstants.DOC_TYPE);
	}

	///////////////////////
	// IStream
	///////////////////////
	
	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IStream#setParentStreamId(java.lang.String)
	 */
	@Override
	public void setParentStreamId(String id) {
		data.put(IConstants.PARENT_STREAM_ID, id);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IStream#getParentStreamId()
	 */
	@Override
	public String getParentStreamId() {
		return (String)data.get(IConstants.PARENT_STREAM_ID);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IStream#setQueryString(java.lang.String)
	 */
	@Override
	public void setQueryString(String query) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IStream#getQueryString()
	 */
	@Override
	public String getQueryString() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IStream#addChildStreamId(java.lang.String)
	 */
	@Override
	public void addChildStreamId(String id) {
		List<String>l = (List<String>)data.get(IConstants.CHILD_STREAM_ID_LIST);
		if (l == null) l = new ArrayList<String>();
		l.add(id);
		data.put(IConstants.CHILD_STREAM_ID_LIST, l);

	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IStream#getLastId()
	 */
	@Override
	public String getLastId() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IStream#setLastId()
	 */
	@Override
	public void setLastId() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IStream#getLastTimestamp()
	 */
	@Override
	public long getLastTimestamp() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IStream#setLastTimestamp()
	 */
	@Override
	public void setLastTimestamp() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IStream#size()
	 */
	@Override
	public long size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> listChildIds() {
		return (List<String>)data.get(IConstants.CHILD_STREAM_ID_LIST);
	}
	///////////////////////
	// IStreamTuple
	///////////////////////
	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IStreamTuple#freeze()
	 */
	@Override
	public void freeze() {
		data.put(IConstants.STATUS, "T");
	}

	/* (non-Javadoc)
	 * @see org.topicquests.ets.api.IStreamTuple#isFrozen()
	 */
	@Override
	public boolean isFrozen() {
		if (data.get(IConstants.STATUS) != null)
			return true;
		return false;
	}

	@Override
	public void setStreamId(String streamId) {
		data.put(IConstants.STREAM_ID, streamId);
	}

	@Override
	public String getStreamId() {
		return (String)data.get(IConstants.STREAM_ID);
	}

}
