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

package org.topicquests.ets;

import org.topicquests.support.api.IResult;
import org.topicquests.ets.ui.MainFrame;
import org.topicquests.ets.ui.StreamPanel;
import org.topicquests.tuplespace.TupleSpaceEnvironment;
import org.topicquests.tuplespace.api.IElasticConstants;
import org.topicquests.tuplespace.api.IElasticTupleStreamsModel;
import org.topicquests.tuplespace.api.ISequenceIterator;
import org.topicquests.tuplespace.api.ISimpleDocument;
import org.topicquests.tuplespace.api.IStream;
import org.topicquests.tuplespace.api.ITuple;
import org.topicquests.tuplespace.api.ITupleSpace;
import org.topicquests.tuplespace.api.ITupleSpaceModel;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class DesktopApp {
	private Environment environment;
	private TupleSpaceEnvironment tsEnvironment;
	private ITupleSpace space;
	private ITupleSpaceModel tsModel;
	private IElasticTupleStreamsModel streamModel;
	private MainFrame view;
	private String mainStreamId;
	private IStream myMainStream;
	private String creatorId = "DesktopApp";
	/**
	 * 
	 */
	public DesktopApp(MainFrame v) {
		environment = new Environment();
		view = v;
		tsEnvironment = environment.getTupleSpaceEnvironment();
		tsModel = tsEnvironment.getModel();
		space = tsEnvironment.getTupleSpace();
		streamModel = environment.getTupleStreamModel();
		mainStreamId = environment.getStringProperty("MainStreamId");
		bootMainStream(mainStreamId);
	}

	private void bootMainStream(String streamId) {
		IResult r = getAStream(streamId);
		myMainStream = (IStream)r.getResultObject();
		System.out.println("BootA "+myMainStream);
		if (myMainStream == null) {
			myMainStream = streamModel.newStream(streamId, streamId, creatorId, true, false);
			r = space.putTuple(myMainStream);
		}
		System.out.println("BootB "+myMainStream);
	}
	
	IResult getAStream(String streamId) {
		return streamModel.getStream(streamId, creatorId);
	}
	public String getMainStreamId() {
		return mainStreamId;
	}
	
	public IStream getMainStream() {
		return myMainStream;
	}
	
	public ISequenceIterator getSequenceIterator() {
		return streamModel.getStreamIterator();
	}
	
	/////////////////////////////////////////
	//Plug-in Apps
	// The concept is that we have a container which allows
	// that plugin apps can create specific kinds of streams
	// For the Desktop, we need a mechanism for adding a new
	// Stream Tab to the console
	/////////////////////////////////////////
	/**
	 * An API for spawning substreams off any stream and
	 * adding a tab to the UI
	 * @param streamId
	 * @param streamTag
	 */
	public void addStreamTab(String streamId, String streamTag) {
		StreamPanel newTab = new StreamPanel(environment, this);
		view.addStreamTab(newTab, streamTag);
		IResult r = getAStream(streamId);
		IStream st = (IStream)r.getResultObject();
		if (st != null)
			newTab.setStream(st, false);
		else
			view.toStatus("Missing Stream for "+streamId+" and tag: "+streamTag);
	}
	
	/**
	 * "Close" a tab
	 * @param tab
	 */
	public void removeStreamTab(StreamPanel tab) {
		view.removeStreamTab(tab);
	}
	
	public Environment getEnvironment() {
		return environment;
	}
}
