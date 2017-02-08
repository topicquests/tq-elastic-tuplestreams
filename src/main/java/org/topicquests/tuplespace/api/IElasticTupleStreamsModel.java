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

package org.topicquests.tuplespace.api;

import org.topicquests.support.api.IResult;

/**
 * @author jackpark
 *
 */
public interface IElasticTupleStreamsModel {
	
	
	//////////////////////////
	// Streams
	//////////////////////////
	
	/**
	 * 
	 * @param parentStreamId
	 * @param streamTag
	 * @param nodeType
	 * @param tupleTag
	 * @param sortDate
	 * @param increasing
	 * @param userId
	 * @return will return the new {@link IStream}
	 */
	IResult spawnNewStream(String parentStreamId, String streamTag, String nodeType, String tupleTag, String sortDate, boolean increasing, 
			String userId);
	//////////////////////////
	//It is increasingly unclear why we need an
	//  IStream and all that stuff, except, as yet another
	//  kind of ITuple which provides a persistence (index) with
	//  description for a particular streamId -- the id of the IStream
	//////////////////////////
	// It further seems clear that persistent substreams can become
	// IStream instances as well.
	// Each substream should know its parent stream.
	// Perhaps each stream should know its substreams
	//////////////////////////
	// We probably need more key-value pairs to support
	// *finding* streams based on criteria
	//////////////////////////
	/**
	 * <p>Create a new Stream, either persistent or not.</p>
	 * <p>If persistent, the resulting {@link ITuple} is persisted.</p>
	 * <p>Otherwise, it is transient and can disappear</p>
	 * @param tag TODO
	 * @param creatorId
	 * @param isPersistent
	 * @param isPrivate TODO
	 * @return
	 */
	IStream  newStream(String tag, String creatorId, boolean isPersistent, boolean isPrivate);
	
	IStream newStream(String id, String tag, String creatorId, boolean isPersistent, boolean isPrivate);
	
	IResult getStream(String streamId, String userId);
	
	/**
	 * If it's a persistent stream, remove everything.
	 * Otherwise, just remove any tuple with <code>streamId</code>
	 * @param streamId
	 * @return
	 */
	IResult removeStream(String streamId);
	
	/**
	 * <p>An {@link ISequenceIterator} is used to iterate along a given IStream</p>
	 * <p>These iterators are not thread safe: one iterator per stream</p>
	 * @return
	 */
	ISequenceIterator getStreamIterator();
	
	IStreamTuple newStreamTuple(String tag, String streamId, String creatorId, boolean isPrivate);
	
	IResult getStreamTuple(String id, String userId);
	
	IResult removeStreamTuple(String id);

	//////////////////////////
	// Tuplespace
	//////////////////////////
	
	ITupleSpaceModel getTupleSpaceModel();
	
	ITupleSpace getTupleSpace();
	
	/////////////////////////
	// Tuple manipulation
	/////////////////////////
	/**
	 * Extract the value of <code>attribute</code> from document
	 * identified by <code>docId</code>
	 * @param docId
	 * @param userId TODO
	 * @param attribute
	 * @return
	 */
	IResult extract(String docId, String userId, String attribute);
	
	/**
	 * Replace the value of <code>attribute><code> in document
	 * identified by <code>docId</code> with <code>value</code>
	 * @param docId
	 * @param userId TODO
	 * @param attribute
	 * @param value
	 * @return
	 */
	IResult replace(String docId, String userId, String attribute, String value);
	
	/**
	 * Change the status of a document identified by <code>docId</code>
	 * @param streamTupleId
	 * @param userId TODO
	 * @return
	 */
	IResult freeze(String streamTupleId, String userId);
	
	////////////////////////////////
	//IT is not clear that append is necessary.
	// IF we were implementing a stream as a linked list,
	// then that makes sense.
	// But, we are not: streams are sorted on a specific long
	// field, not on links among nodes
	// ********************************
	// What this really means is simply to create a Stream Tuple
	// which references the ISimpleDocument, which, itself, must be
	// persisted in tuplespace; there are Two tuples engaged here
	//	One for the document
	//  One to locate that document in a stream
	//		It seems reasonable to think of a document in
	//   	more than one stream, say:
	//		one where it was created (or discovered)
	//		and another related to its entering the context of some project
	/////////////////////////////////
	/**
	 * Given the tuple <code>jsonDoc</code>, append it to
	 * a stream identified by <code>streamId</code> with a new 
	 * (@link IStreamTuple} tuple
	 * @param streamId
	 * @param userId TODO
	 * @param isPrivate TODO
	 * @param jsonDoc
	 * @return
	 */
	IResult append(String streamId, String userId, boolean isPrivate, ISimpleDocument jsonDoc);
	
	////////////////////////////
	//IT is not clear why I need a streamId and docIndex
	//TODO sort this out
	//JUST use readImmediate from tuplespace
	////////////////////////////
	/**
	 * Returns a {@link ISimpleDocument} by way of the given <code>streamId</code>
	 * and <code>docIndex</code>
	 * @param streamId
	 * @param docIndex
	 * @return
	 */
	//IResult read(String streamId, String docIndex);
	
	///////////////////////////////
	//LOTS to think about here:
	//  IF I know the doc, I know its identity which means
	//   I can go straight to that doc and update it
	// Perhaps there is temporal accounting going on, e.g.
	// last update field in its stream tuple(s)
	//TODO: should documents know their stream tuple Ids as well?
	//	a kind of double linking...
	//JUST use putTuple from tuplespace
	//////////////////////////////
	/**
	 * Replaces a <code>jsonDoc</code> at the tuple specified by <code>streamId</code>
	 * and <code>docIncex</code>
	 * @param StreamId
	 * @param streamId
	 * @param jsonDoc
	 * @return
	 */
	//IResult write(String streamId, String docIndex, ISimpleDocument jsonDoc);

	/////////////////////////
	// It seems that streamId would belong in query, so not needed?
	// TODO: think about this:
	// filtering along a stream implies one or both of:
	//  a- the IStream node has enough information to filter on, e.g.
	//    things like cargoType --> document type (filter on emails)
	//    but that's weak
	//  b- filtering is a matter of mapalong where you fetch a document
	//    and apply the filter to it.
	// In fact, the query could just go straight for any document matching
	// query in the database; said document would necessarily need to have
	// streamId as a member of its [MISSING FIELD] myStreams list
	// UPDATE: field added to IStreamTuple
	/////////////////////////
	//RETHINK: this is about grabbing a stream iterator on the given
	//Stream, and applying the query to its documents.
	// That query might be as simple as pulling out all documents of type
	//   email, or, more complex: all emails to/from some entity or with
	//   some particular subject, or containing some phrase
	// IN theory: we don't need the stream iterator since each document
	//  already knows what streams contain it.
	// SO, if each document has a documentType, and if the appropriate
	//  other features are present, then we can craft a query template
	//  which goes directly to TupleSpace and adds those documents
	//  to a new IStream, one which has parentStream as its parent.
	// The process, then, is to:
	//  Form a query
	//  Get an iterator which satisfies that query
	//  IF iterator exists
	//  THEN create a stream. If parentStreamId == null, it's
	//   a stream; otherwise, it's a substream
	//  For each iterator element
	//   add it to the new stream
	// IF no query results
	// RETURN null
	////////////////////////
	/**
	 * Return a stream or substream based on parentStreamId and newTag and <code>query</code>
	 * <code>query</code> must choose specific documents
	 * IF this is a substring, <code>query</code> must include parentStreamId to ensure that
	 * chosen documents belong to the parent stream
	 * @param parentStreamId can be <code>null</code>
	 * @param newTag for the new stream
	 * @param query
	 * @param userId
	 * @param isPersistent
	 * @param isPrivate
	 * @return returns an instance of {@link IStream} or <code>null</code>
	 */
	IResult filter(String parentStreamId, String newTag, ITuple query, String userId, boolean isPersistent, boolean isPrivate);
	
	//IResult retrieve(String streamId, String subStreamId);
	
	///////////////////////////
	//TODO figure out agents
	// Do we need an agent infrastructure down in tuplespace?
	///////////////////////////
	//IResult addAgent(String streamId, String agentId);
	
	//////////////////////////
	//Not sure about this:
	// the ITuple query should be more than enough
	//////////////////////////
	IResult find(String streamId, String pid, ITuple query);
	
	//////////////////////////
	// Documents
	// Documents are persisted with putTuple
	//////////////////////////

	/**
	 * Return a new {@link ISimpleDocument}
	 * @param documentType cannot be <code>null</code>
	 * @param tag cannot be <code>null</code>
	 * @param userId cannot be <code>null</code>
	 * @param isPrivate
	 * @return
	 */
	ISimpleDocument newSimpleDocument(String documentType, String tag, String userId, boolean isPrivate);

	IResult getSimpleDocument(String docId, String userId);

	IResult findDocument(ITuple template, String userId);
	
}
