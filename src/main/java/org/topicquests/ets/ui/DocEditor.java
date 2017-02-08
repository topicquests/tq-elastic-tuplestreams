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
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.topicquests.tuplespace.api.IConstants;


/**
 * @author jackpark
 *
 */
public class DocEditor extends JDialog {
	private StreamPanel host;
	private JPanel jPanel1 = new JPanel();
	private JPanel jPanel2 = new JPanel();
	private JPanel jPanel3 = new JPanel();
	private JPanel jPanel4 = new JPanel();
	
	private JButton cancelButton = new JButton("Cancel");
	private JButton submitButton = new JButton("Submit");
	private JLabel jLabel1 = new JLabel("Tuple Tag ");
	private JLabel jLabel2 = new JLabel("Subject ");
	private JLabel jLabel3 = new JLabel("Doc Type ");
	private JTextField tagField = new JTextField();
	private JTextField subjectField = new JTextField();
	private JScrollPane jScrollPane1 = new JScrollPane();
	private JTextArea	bodyArea = new JTextArea();
	private String[] typeStrings = { "Note", "Blog", "Email", "Calendar", "ToDo" };
	 
    //Create the combo box, select the item at index 4.
    //Indices start at 0, so 4 specifies the pig.
    private JComboBox typeList = new JComboBox(typeStrings);
	private String MY_STREAM_ID;
	private boolean isEdit = false;
	private String docId;

	/**
	 * 
	 */
	public DocEditor(StreamPanel h) {
		host = h;
		this.setLayout(new BorderLayout());
		this.setTitle("Document Editor");
		this.add(jPanel1, BorderLayout.SOUTH);
		this.add(jPanel2, BorderLayout.NORTH);
	    jPanel1.setLayout(new FlowLayout());
	    jPanel1.add(cancelButton);
	    jPanel1.add(submitButton);
	    cancelButton.addActionListener(new
	    		DocEditor_cancelButton_actionAdapter(this));
	    submitButton.addActionListener(new
	    		DocEditor_submitButton_actionAdapter(this));
	    jPanel2.setLayout(new FlowLayout());
	    jPanel2.add(jLabel3);
	    jPanel2.add(typeList);
	    jPanel2.add(jLabel1);
	    tagField.setPreferredSize(new Dimension(200, 20));
	    tagField.setText("");
	    jPanel2.add(tagField);
	    jPanel3.setLayout(new BorderLayout());
	    this.add(jPanel3, BorderLayout.CENTER);
	    jPanel4.setLayout(new FlowLayout());
	    jPanel3.add(jPanel4, BorderLayout.NORTH);
	    jPanel4.add(jLabel2);
	    subjectField.setPreferredSize(new Dimension(400, 20));
	    subjectField.setText("");
	    jPanel4.add(subjectField);
	    jPanel3.add(jScrollPane1, BorderLayout.CENTER);
	    jScrollPane1.getViewport().add(bodyArea);
	    bodyArea.setText("");
	    bodyArea.setLineWrap(true);
	    
	    
	}
	
	/**
	 * For new documents
	 * @param streamId
	 */
	public void showMe(String streamId) {
		this.setTitle("New Document Editor");

		this.docId = null;
		MY_STREAM_ID = streamId;
		tagField.setText("");
		tagField.setEditable(true);
		subjectField.setText("");
		subjectField.setEditable(true);
		bodyArea.setText("");
		bodyArea.setEditable(true);
		this.setVisible(true);
		submitButton.setEnabled(true);
		isEdit = false;
	}
	
	/**
	 * For editing documents
	 * @param docId TODO
	 * @param streamId
	 * @param type TODO
	 * @param tag
	 * @param subject
	 * @param body
	 */
	public void editMe(String docId, String streamId, String type, String tag, String subject, String body) {
		this.setTitle("Document Editor");
		this.docId = docId;
		MY_STREAM_ID = streamId;
		tagField.setText(tag);
		tagField.setEditable(false);
		subjectField.setText(subject);
		subjectField.setEditable(true);;
		bodyArea.setText(body);
		bodyArea.setEditable(true);
		this.setVisible(true);
		submitButton.setEnabled(true);
		isEdit = true;
	}

	/**
	 * For viewing a document
	 * @param streamId
	 * @param type TODO
	 * @param tag
	 * @param subject
	 * @param body
	 */
	public void showMe(String streamId, String type, String tag, String subject, String body) {
		this.setTitle("Document Viewer");
		MY_STREAM_ID = streamId;
		setTypeList(type);
		tagField.setText(tag);
		tagField.setEditable(false);
		subjectField.setText(subject);
		subjectField.setEditable(false);
		bodyArea.setText(body);
		bodyArea.setEditable(false);
		this.setVisible(true);
		submitButton.setEnabled(false);
	}
	//
	//{ "Note", "Blog", "Email", "Calendar", "ToDo" }
	void setTypeList(String type) {
		int which = 0; // note and default
		if (type.equals(IConstants.BLOG_TYPE))
			which = 1;
		else if (type.equals(IConstants.EMAIL_TYPE))
			which = 2;
		else if (type.equals(IConstants.CALENDAR_EVENT_TYPE))
			which = 3;
		else if (type.equals(IConstants.TODO_TYPE))
			which = 4;
		this.typeList.setSelectedIndex(which);
	}

	  public void docEditor_cancelButtonClicked() {
		  this.setVisible(false);
	  }
	  
	  public void docEditor_submitButtonClicked() {
		  if (tagField.getText().equals("")) {
			  JOptionPane.showMessageDialog(null, "Tag field cannot be empty!");
			  return;
		  }

		  host.acceptEditor(docId, MY_STREAM_ID, 
				  (String)typeList.getSelectedItem(),
				  tagField.getText(), subjectField.getText(), 
				  bodyArea.getText(), isEdit);
		  this.setVisible(false);
	  }


}

class DocEditor_submitButton_actionAdapter
		implements ActionListener {
	private DocEditor adaptee;
	DocEditor_submitButton_actionAdapter(DocEditor adaptee) {
		this.adaptee = adaptee;
	}
	
	public void actionPerformed(ActionEvent e) {
		adaptee.docEditor_submitButtonClicked();
	}
}

class DocEditor_cancelButton_actionAdapter
		implements ActionListener {
	private DocEditor adaptee;
	DocEditor_cancelButton_actionAdapter(DocEditor adaptee) {
		this.adaptee = adaptee;
	}
	
	public void actionPerformed(ActionEvent e) {
		adaptee.docEditor_cancelButtonClicked();
	}
}