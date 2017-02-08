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

package org.topicquests.ets.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;


//import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
//import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
//import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
//import javax.swing.table.TableModel;

//import org.topicquests.support.util.DateUtil;
import org.topicquests.support.api.IResult;
//import org.topicquests.es.util.ElasticQueryUtility;
import org.topicquests.es.util.JSONQueryUtil;
import org.topicquests.ets.DesktopApp;
import org.topicquests.ets.Environment;
import org.topicquests.tuplespace.Tuple;
import org.topicquests.tuplespace.TupleSpaceEnvironment;
import org.topicquests.tuplespace.api.IConstants;
import org.topicquests.tuplespace.api.IElasticConstants;
import org.topicquests.tuplespace.api.IElasticTupleStreamsModel;
import org.topicquests.tuplespace.api.ISequenceIterator;
import org.topicquests.tuplespace.api.ISimpleDocument;
import org.topicquests.tuplespace.api.IStream;
import org.topicquests.tuplespace.api.IStreamTuple;
//import org.topicquests.tuplespace.api.ITuple;
import org.topicquests.tuplespace.api.ITupleSpace;
import org.topicquests.tuplespace.api.ITupleSpaceModel;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class StreamPanel extends JPanel {
	private Environment environment;
	private DesktopApp host;
	private IElasticTupleStreamsModel model;
	private TupleSpaceEnvironment tsEnvironment;
	private ITupleSpace space;
	private ITupleSpaceModel tsModel;
	private String columnNames[] = { "Locator", "Date", "Description", "Tag" };
	private JTable streamTable = new JTable();
	private DefaultTableModel streamModel = new MyDefaultTableModel();
	private JScrollPane jScrollPane1 = new JScrollPane();
	private JPanel jPanel1 = new JPanel();
	private JButton newButton = new JButton("  New ");
	private JButton filterButton = new JButton("Filter");
	private JButton editButton = new JButton(" Edit ");
	private JButton closeButton = new JButton("Close");
	private DocEditor editor;
	private QueryEditor query;
    private JComboBox subStreamList = new JComboBox();
    private DefaultComboBoxModel subStreamListModel;
    private JLabel substreamsLabel = new JLabel("Substreams");

	private String MY_STREAM_ID;
	private IStream MY_STREAM;
	private String creatorId = "System"; //TODO for now
	// not thread safe
	private String SELECTED_DOC_ID;
	private JSONQueryUtil queryUtil;
	//key = streamtag
	private Map<String,String> subStreamMap = new HashMap<String,String>();



	/**
	 * 
	 */
	public StreamPanel(Environment env, DesktopApp h) {
		environment = env;
		host = h;
		queryUtil = new JSONQueryUtil();
		model = environment.getTupleStreamModel();
		tsEnvironment = environment.getTupleSpaceEnvironment();
		tsModel = tsEnvironment.getModel();
		space = tsEnvironment.getTupleSpace();
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	/**
	 * Each instance of StreamPanel is associated with a
	 * given IStream.
	 * @param streamId
	 * @param isMainStream should be <code>true</code> only for primary stream
	 */
	public void setStream(IStream stream, boolean isMainStream) {
		System.out.println("StreamTab= "+stream.getId());
		MY_STREAM_ID = stream.getId();
		MY_STREAM = stream;
		if (isMainStream)
			closeButton.setEnabled(false);
		paintIndex();
	    populateSubstreamMap();
	    refreshSubstreamList();

	}

	void populateSubstreamMap() {
		List<String>subs = this.MY_STREAM.listChildIds();
		if (subs != null) {
			IResult r;
			String sid;
			Iterator<String>itr = subs.iterator();
			IStreamTuple st;
			while (itr.hasNext()) {
				sid = itr.next();
				r = model.getStreamTuple(sid, creatorId);
				st = (IStreamTuple)r.getResultObject();
				this.addToSubstreamMap(st.getId(), st.getTag());
			}
		}
	}
	
	/**
	 * NOTE: duplicate <code>streamTag</code> entries will kill the first one in.
	 * @param streamId
	 * @param streamTag
	 */
	void addToSubstreamMap(String streamId, String streamTag) {
		this.subStreamMap.put(streamTag, streamId);
		
	}
	
	void refreshSubstreamList() {
		Iterator<String>itr = this.subStreamMap.keySet().iterator();
		while (itr.hasNext())
			subStreamListModel.addElement(itr.next());
	}
	
	void paintIndex() {
		streamModel.setRowCount(0);
		ISequenceIterator itr = model.getStreamIterator();
		JSONObject 	theQuery =  queryUtil.match(IConstants.STREAM_ID, MY_STREAM_ID);
		theQuery = queryUtil.query(theQuery);
		IResult r = space.countObjects(theQuery);
		System.out.println("CT "+r.getErrorString()+" "+r.getResultObject());
		r =	itr.startIterator(IConstants.STREAM_ID, MY_STREAM_ID, 0, -1, IElasticConstants.SORT_DATE, true, creatorId);
		JSONObject jo;
		String docId;
		ISimpleDocument doc;
		while (r.getResultObject() != null) {
			jo = (JSONObject)r.getResultObject();
			docId = (String)jo.get(IElasticConstants.CARGO);
			System.out.println("BOOTING "+docId);
			r = model.getSimpleDocument(docId, creatorId);
			doc = (ISimpleDocument)r.getResultObject();
			System.out.println("DDD "+doc.toJSONString());
			this.addMember(doc.getSortDateString(), doc.getSubject(), doc.getTag(), doc.getId());
			r = itr.next();
		}
	}
	  private void jbInit() throws Exception {
		    this.setLayout(new BorderLayout());
		    streamTable.setModel(streamModel);
		    streamTable.getColumnModel().addColumn(new TableColumn());
		    streamTable.getColumnModel().addColumn(new TableColumn());
		    streamTable.getColumnModel().addColumn(new TableColumn());
		    streamTable.getColumnModel().addColumn(new TableColumn());
		    streamModel.setColumnIdentifiers(columnNames);
		    streamTable.addMouseListener(new
		    		StreamPanel_streamList_mouseAdapter(this));
		    this.add(jScrollPane1, java.awt.BorderLayout.CENTER);
		    //make locator column not visible
		    streamTable.getColumnModel().getColumn(0).setMinWidth(0);
		    streamTable.getColumnModel().getColumn(0).setMaxWidth(0);
		    jScrollPane1.getViewport().add(streamTable);
		    this.add(jPanel1, BorderLayout.NORTH );
		    jPanel1.setLayout(new FlowLayout());
		    newButton.setToolTipText("Create a new document");
		    newButton.addActionListener(new
		    		StreamPanel_newButton_actionAdapter(this));
		    filterButton.setToolTipText("Filter Stream to form substream");
		    filterButton.addActionListener(new
		    		StreamPanel_filterButton_actionAdapter(this));
		    editButton.setToolTipText("Edit chosen document");
		    editButton.addActionListener(new
		    		StreamPanel_editButton_actionAdapter(this));
		    closeButton.addActionListener(new
		    		CloseButton_actionAdapter(this));
		    jPanel1.add(newButton);
		    jPanel1.add(filterButton);
		    jPanel1.add(editButton);
		    jPanel1.add(substreamsLabel);
		    jPanel1.add(subStreamList);
		    jPanel1.add(closeButton);
		    this.editButton.setEnabled(false);
		    subStreamListModel = (DefaultComboBoxModel)subStreamList.getModel();
		    subStreamList.addActionListener(new SubstreamActionAdaptor(this));
		    makeDocEditor();
		    makeQueryEditor();
	  }
	  
	  /**
	   * Callback from editor. Here, we create a document.
	   * @param docId
	   * @param streamId
	   * @param type
	   * @param tag
	   * @param subject
	   * @param body
	   * @param isEdit
	   */
	  public void acceptEditor(String docId, String streamId, String type, String tag, String subject, String body, boolean isEdit) {
		  System.out.println("Got "+docId);
		  System.out.println("Got "+streamId);
		  System.out.println("Got "+type);
		  System.out.println("Got "+tag);
		  System.out.println("Got "+subject);
		  System.out.println("Got "+body);
		  System.out.println("Got "+isEdit);

		  IResult r;
		  ISimpleDocument doc;
		  if (isEdit) {
			  r = model.getSimpleDocument(docId, creatorId);
			  
			  doc = (ISimpleDocument)r.getResultObject();
			  ISimpleDocument original = new Tuple(doc.cloneTuple().getData());
			  doc.setSubject(subject);
			  doc.setBody(body);
			  doc.update();
			  r = space.updateTuple(docId, doc.getData(), false);
			  paintIndex();
		  } else {
			  //create a new IDocument
			  doc = model.newSimpleDocument(typeToType(type), 
					  tag, creatorId, false);
			  doc.setSubject(subject);
			  doc.setBody(body); 
			  //create a new IStreamTuple
			  IStreamTuple st = model.newStreamTuple(tag, streamId, creatorId, false);
			  //couple the two together
			  doc.addStreamId(this.MY_STREAM_ID); //st.getId());
			  st.setCargoType(IConstants.CARGO_AIR_TYPE);
			  st.setCargo(doc.getId());
			  //persist everything
			  r = space.putTuple(doc);
			  r = space.putTuple(st);
			  //add to UI
			  String date = doc.getSortDateString();
			  this.addMember(date, subject, tag, doc.getId());
		  }
	  }
	  
	  public void acceptFilter(String parentStreamId, String streamTag, String nodeType, String tupleTag, String sortDate, boolean increasing) {
		  System.out.println("FILTER "+streamTag+" "+nodeType+" "+tupleTag+" "+sortDate+" "+increasing);
		  //TODO this will move to model
		  IResult r = model.spawnNewStream(parentStreamId, streamTag, typeToType(nodeType), tupleTag, sortDateToKey(sortDate), increasing, creatorId);
		  System.out.println("SPAWN "+r.getErrorString()+" | "+r.getResultObject());
		  if (r.getResultObject() != null) {
			  IStream ns = (IStream)r.getResultObject();
			  //host.addStreamTab(ns.getId(), ns.getTag());
			  this.addToSubstreamMap(ns.getId(), ns.getTag());
			  this.refreshSubstreamList();
		  }
	  }
	  String sortDateToKey(String sortDate) {
		  String result = IElasticConstants.CREATED_DATE; //default
		  
		  return result;
	  }
	  String typeToType(String type) {
		  if (type.equals("Note"))
			  return IConstants.NOTE_TYPE;
		  if (type.equals("Blog"))
			  return IConstants.BLOG_TYPE;
		  if (type.equals("Calendar"))
			  return IConstants.CALENDAR_EVENT_TYPE;
		  if (type.equals("Email")) 
			  return IConstants.EMAIL_TYPE;
		  if (type.equals("ToDo"))
			  return IConstants.TODO_TYPE;
		  else
			  return IConstants.TOPIC_TYPE; //default
	  }
	  void showEditor() {
		  editor.showMe(MY_STREAM_ID);
		  this.editButton.setEnabled(false);
	  }
	  
	  void showQuery() {
		  
		  this.editButton.setEnabled(false);
		  this.query.startQuery(this.MY_STREAM_ID);
	  }
	  
	  void editDocument() {
		  IResult r = model.getSimpleDocument(SELECTED_DOC_ID, creatorId);
		  ISimpleDocument doc = (ISimpleDocument)r.getResultObject();
		  //TODO HUGE ISSUE HERE:
		  //  A GIVEN DOCUMENT might be member of many streams; the stream
		  //  in question could be ambiguous.
		  //TODO for now, we will use the local streamId
		  editor.editMe(doc.getId(), MY_STREAM_ID, doc.getDocumentType(), doc.getTag(), doc.getSubject(), doc.getBody());
		  this.editButton.setEnabled(false);
	  }
	  
	  void displayDocument() {
		  IResult r = model.getSimpleDocument(SELECTED_DOC_ID, creatorId);
		  ISimpleDocument doc = (ISimpleDocument)r.getResultObject();
		  System.out.println("SHOWING "+doc.toJSONString());
		  //TODO HUGE ISSUE HERE:
		  //  A GIVEN DOCUMENT might be member of many streams; the stream
		  //  in question could be ambiguous.
		  //TODO for now, we will use the local streamId
		  editor.showMe(MY_STREAM_ID, doc.getDocumentType(), doc.getTag(), doc.getSubject(), doc.getBody());
		  this.editButton.setEnabled(false);
		  //in the background, update lastread date
		  doc.setLastEditDate(new Date());
		  r = space.updateTuple(SELECTED_DOC_ID, doc.getData(), false);
	  }
	  
	  void makeDocEditor() {
		  editor = new DocEditor(this);
		  editor.setSize(600, 600);
		// Center the window
		    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		    Dimension frameSize = editor.getSize();
		    if (frameSize.height > screenSize.height) {
		      frameSize.height = screenSize.height;
		    }
		    if (frameSize.width > screenSize.width) {
		      frameSize.width = screenSize.width;
		    }
		    editor.setLocation( (screenSize.width - frameSize.width) / 2,
		                      (screenSize.height - frameSize.height) / 2);
	  }
	  void makeQueryEditor() {
		  query = new QueryEditor(this);
		  query.setSize(600, 600);
		// Center the window
		    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		    Dimension frameSize = query.getSize();
		    if (frameSize.height > screenSize.height) {
		      frameSize.height = screenSize.height;
		    }
		    if (frameSize.width > screenSize.width) {
		      frameSize.width = screenSize.width;
		    }
		    query.setLocation( (screenSize.width - frameSize.width) / 2,
		                      (screenSize.height - frameSize.height) / 2);
	  }
	  
	  public void addMember (String date, String description, String tag, String locator) {
		  synchronized(streamTable) {
			  System.out.println("ADDING "+date);
			  streamModel.addRow(new Object[] {locator, date, description, tag});
			  streamTable.repaint();
		  }
	  }

	  public void streamPanel_mouseClicked(MouseEvent e) {
		  int whichRow = streamTable.getSelectionModel().getMinSelectionIndex();
		  SELECTED_DOC_ID = (String)streamModel.getValueAt(whichRow, 0);
		  System.out.println("Selected "+SELECTED_DOC_ID);
		  this.editButton.setEnabled(true);
		  if (e.getClickCount() > 1) {
			  System.out.print("DoubleClick");
			  this.displayDocument();
		  }
	  }
	  
	  public void streamPanel_newButtonClicked() {
		  this.showEditor();
	  }

	  public void streamPanel_filterButtonClicked() {
		  showQuery();
	  }
	  
	  public void streamPanel_editButtonClicked() {
		  editDocument();
	  }
	  public void closeButtonClicked() {
		  host.removeStreamTab(this);
	  }

	  public void substreamListClicked(ActionEvent e) {
		  System.out.println("StreamClick "+this.subStreamList.getSelectedItem());
		  String which = (String)this.subStreamList.getSelectedItem();
		  if (!which.equals("")) {
			  host.addStreamTab(subStreamMap.get(which), which);
		  }
	  }
}

class StreamPanel_streamList_mouseAdapter
		extends MouseAdapter {
	private StreamPanel adaptee;
	StreamPanel_streamList_mouseAdapter(StreamPanel adaptee) {
		this.adaptee = adaptee;
	}

	public void mouseClicked(MouseEvent e) {
	adaptee.streamPanel_mouseClicked(e);
	}
}
class StreamPanel_newButton_actionAdapter
		implements ActionListener {
		private StreamPanel adaptee;
		StreamPanel_newButton_actionAdapter(StreamPanel adaptee) {
		this.adaptee = adaptee;
	}
	
	public void actionPerformed(ActionEvent e) {
		adaptee.streamPanel_newButtonClicked();
	}
}

class StreamPanel_filterButton_actionAdapter
		implements ActionListener {
	private StreamPanel adaptee;
		StreamPanel_filterButton_actionAdapter(StreamPanel adaptee) {
			this.adaptee = adaptee;
	}
	
	public void actionPerformed(ActionEvent e) {
		adaptee.streamPanel_filterButtonClicked();
	}
}
class StreamPanel_editButton_actionAdapter
		implements ActionListener {
	private StreamPanel adaptee;
	StreamPanel_editButton_actionAdapter(StreamPanel adaptee) {
		this.adaptee = adaptee;
	}
	
	public void actionPerformed(ActionEvent e) {
		adaptee.streamPanel_editButtonClicked();
	}
}
class CloseButton_actionAdapter implements ActionListener {
	private StreamPanel adaptee;
	CloseButton_actionAdapter(StreamPanel adaptee) {
		this.adaptee = adaptee;
	}
	
	public void actionPerformed(ActionEvent e) {
		adaptee.closeButtonClicked();
	}
	
}

class SubstreamActionAdaptor implements ActionListener {
	private StreamPanel adaptee;
	SubstreamActionAdaptor(StreamPanel adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.substreamListClicked(e);
	}
}
