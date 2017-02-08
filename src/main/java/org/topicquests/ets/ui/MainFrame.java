/*
 * Copyright 2012,2017 TopicQuests
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

import java.io.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
//import javax.swing.JToolBar;
//import javax.swing.JButton;
//import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.topicquests.ets.DesktopApp;
import org.topicquests.ets.Environment;
//import org.topicquests.support.util.TextFileHandler;
/**
 * @author Jack Park
 * @version 1.0
 */
public class MainFrame
    extends JFrame  {
	private Environment environment;
	private DesktopApp application;
	private StreamPanel streamTab;

  JPanel contentPane;
  BorderLayout borderLayout1 = new BorderLayout();
  JMenuBar jMenuBar1 = new JMenuBar();
  JMenu jMenuFile = new JMenu();
  JMenuItem jMenuFileExit = new JMenuItem();
  JMenu jMenuHelp = new JMenu();
  JMenuItem jMenuHelpAbout = new JMenuItem();
  JLabel statusBar = new JLabel();
  JTabbedPane jTabbedPane1 = new JTabbedPane();
  JScrollPane consoleTab = new JScrollPane();
  JTextArea consoleArea = new JTextArea();

  
  public MainFrame() {
    try {
    	//by setting default close to doNothing, we avoid the issue
    	//of quitting without calling shutDown.
    	//To exit this program, must use File:Exit
      setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
      application = new DesktopApp(this);
      environment = application.getEnvironment();
      jbInit();
      streamTab.setStream(application.getMainStream(), true);
 
//      if (environment.getRdbmsDatabase() != null)
//    	  rdbmsPanel.setConnection(environment.getRdbmsDatabase().getConnection());
    }
    catch (Exception exception) {
      exception.printStackTrace();
      System.exit(0);
    }
  }

   
  /**
   * Component initialization.
   *
   * @throws java.lang.Exception
   */
  private void jbInit() throws Exception {
    contentPane = (JPanel) getContentPane();
    contentPane.setLayout(borderLayout1);
    setSize(new Dimension(800, 600));
    setTitle("Elastic TupleStreams");
    streamTab = new StreamPanel(environment, application);
    statusBar.setText(" ");
    jMenuFile.setText("File");
    jMenuFileExit.setText("Exit");
    jMenuFileExit.addActionListener(new MainFrame_jMenuFileExit_ActionAdapter(this));
    jMenuHelp.setText("Help");
    jMenuHelpAbout.setText("About");
    jMenuHelpAbout.addActionListener(new MainFrame_jMenuHelpAbout_ActionAdapter(this));
    consoleArea.setEditable(false);
    consoleArea.setText("");
    consoleArea.setLineWrap(true);
    jMenuBar1.add(jMenuFile);
    jMenuFile.add(jMenuFileExit);
    jMenuBar1.add(jMenuHelp);
    jMenuHelp.add(jMenuHelpAbout);
    setJMenuBar(jMenuBar1);
    contentPane.add(statusBar, BorderLayout.SOUTH);
    contentPane.add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    jTabbedPane1.add(consoleTab, "Console");
    addStreamTab(streamTab, "MainStream");
    consoleTab.getViewport().add(consoleArea);
    toStatus("Hello");
  }
  


  /**
   * File | Exit action performed.
   *
   * @param actionEvent ActionEvent
   */
  void jMenuFileExit_actionPerformed(ActionEvent actionEvent) {
	  //we can crash before environment is built, so it will be null
	  if (environment != null)
		  environment.shutDown();
    System.exit(0);
  }

  /**
   * Help | About action performed.
   *
   * @param actionEvent ActionEvent
   */
  void jMenuHelpAbout_actionPerformed(ActionEvent actionEvent) {
    MainFrame_AboutBox dlg = new MainFrame_AboutBox(this);
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlg.setLocation( (frmSize.width - dlgSize.width) / 2 + loc.x,
                    (frmSize.height - dlgSize.height) / 2 + loc.y);
    dlg.setModal(true);
    dlg.pack();
    dlg.setVisible(true);
  }

  ///////////////////
  // Extensibility
  ///////////////////
  
  public void addStreamTab(StreamPanel tab, String title) {
	  jTabbedPane1.add(tab, title);
  }
  
  public void removeStreamTab(StreamPanel tab) {
	  jTabbedPane1.remove(tab);
	  //TODO ?? refresh view?
  }
  ///////////////////
  // Commmunications from apps
  ///////////////////
  public void toConsole(String text) {
    consoleArea.append(text+"\n");
  }

  public void toStatus(String text) {
    statusBar.setText(text);
  }

}


class MainFrame_jMenuFileExit_ActionAdapter
    implements ActionListener {
  MainFrame adaptee;

  MainFrame_jMenuFileExit_ActionAdapter(MainFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent actionEvent) {
    adaptee.jMenuFileExit_actionPerformed(actionEvent);
  }
}

class MainFrame_jMenuHelpAbout_ActionAdapter
    implements ActionListener {
  MainFrame adaptee;

  MainFrame_jMenuHelpAbout_ActionAdapter(MainFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent actionEvent) {
    adaptee.jMenuHelpAbout_actionPerformed(actionEvent);
  }

}
