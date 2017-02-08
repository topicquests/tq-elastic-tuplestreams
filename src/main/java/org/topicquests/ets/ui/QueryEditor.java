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

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * @author jackpark
 *
 */
public class QueryEditor extends JDialog {
	private StreamPanel host;
	private String parentStreamId=null;
	private JPanel buttonPanel = new JPanel();
	private JPanel topPanel = new JPanel();
	private JPanel centerPanel = new JPanel();
	private JPanel streamSortPanel = new JPanel();
	private JPanel tupleQueryPanel = new JPanel();
	private JPanel streamTagPanel = new JPanel();

	private JButton cancelButton = new JButton("Cancel");
	private JButton submitButton = new JButton("Submit");
	private JLabel tupleTagLabel = new JLabel("Tuple Tag ");
	private JLabel jLabel3 = new JLabel("Doc Type ");
	private JLabel jLabel2 = new JLabel("Sort On ");
	private JLabel streamTagLabel = new JLabel("New Stream Tag ");
	private String[] typeStrings = { "None", "Note", "Blog", "Email", "Calendar", "ToDo" };
	 
    //Create the combo box, select the item at index 4.
    //Indices start at 0, so 4 specifies the pig.
    private JComboBox typeList = new JComboBox(typeStrings);
	private JTextField tagField = new JTextField();
	private JTextField streamTagField = new JTextField();
	private String[] sortStrings = { "None", "Created", "LastEdit", "LastRead", "Sort" };
	private JComboBox sortList = new JComboBox(sortStrings);
	private JRadioButton upButton = new JRadioButton("Increasing");
	private JRadioButton downButton = new JRadioButton("Decreasing");
	private ButtonGroup group = new ButtonGroup();
	/**
	 * 
	 */
	public QueryEditor(StreamPanel h) {
		host = h;
		this.setLayout(new BorderLayout());
		this.setTitle("Filter Query Editor");
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.add(topPanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		topPanel.setLayout(new BorderLayout());
		centerPanel.setLayout(new BorderLayout());
		streamSortPanel.setLayout(new FlowLayout());
		tupleQueryPanel.setLayout(new FlowLayout());
		streamTagPanel.setLayout(new FlowLayout());
		centerPanel.add(streamSortPanel, BorderLayout.NORTH);
	    tupleQueryPanel.add(jLabel3);
	    tupleQueryPanel.add(typeList);
	    tupleQueryPanel.add(tupleTagLabel);
	    streamTagPanel.add(streamTagLabel);
	    streamTagPanel.add(streamTagField);
	    topPanel.add(streamTagPanel, BorderLayout.NORTH);
	    topPanel.add(tupleQueryPanel, BorderLayout.SOUTH);
	    streamTagField.setPreferredSize(new Dimension(200, 20));
	    streamTagField.setText("");
	    tagField.setPreferredSize(new Dimension(200, 20));
	    tagField.setText("");
	    tupleQueryPanel.add(tagField);
	  
	    streamSortPanel.add(jLabel2);
	    streamSortPanel.add(sortList);
	    streamSortPanel.add(upButton);
	    streamSortPanel.add(downButton);
	    group.add(upButton);
	    group.add(downButton);
	    group.setSelected(upButton.getModel(), true);

	    buttonPanel.setLayout(new FlowLayout());
	    buttonPanel.add(cancelButton);
	    buttonPanel.add(submitButton);
	    cancelButton.addActionListener(new
	    		QueryEditor_cancelButton_actionAdapter(this));
	    submitButton.addActionListener(new
	    		QueryEditor_submitButton_actionAdapter(this));

	}

	public void startQuery(String streamId) {
		this.parentStreamId = streamId;
		this.setVisible(true);
	}
	  public void docEditor_cancelButtonClicked() {
		  this.setVisible(false);
	  }
	  public void docEditor_submitButtonClicked() {
		  if (streamTagField.getText().equals("")) {
			  JOptionPane.showMessageDialog(null, "Stream Tag is required!");
			  return;
		  }
		  host.acceptFilter(parentStreamId, this.streamTagField.getText(),(String)typeList.getSelectedItem(), 
				  this.tagField.getText(), (String)sortList.getSelectedItem(), 
				  (group.getSelection().equals(upButton.getModel())));
		  this.setVisible(false);
	  }


}
class QueryEditor_submitButton_actionAdapter
	implements ActionListener {
	private QueryEditor adaptee;
	QueryEditor_submitButton_actionAdapter(QueryEditor adaptee) {
		this.adaptee = adaptee;
	}
	
	public void actionPerformed(ActionEvent e) {
		adaptee.docEditor_submitButtonClicked();
	}
}

class QueryEditor_cancelButton_actionAdapter
	implements ActionListener {
	private QueryEditor adaptee;
	QueryEditor_cancelButton_actionAdapter(QueryEditor adaptee) {
		this.adaptee = adaptee;
	}
	
	public void actionPerformed(ActionEvent e) {
		adaptee.docEditor_cancelButtonClicked();
	}
}