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

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

/**
 * @author jackpark
 *
 */
public class MyDefaultTableModel extends DefaultTableModel {

	/**
	 * 
	 */
	public MyDefaultTableModel() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param rowCount
	 * @param columnCount
	 */
	public MyDefaultTableModel(int rowCount, int columnCount) {
		super(rowCount, columnCount);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param columnNames
	 * @param rowCount
	 */
	public MyDefaultTableModel(Vector columnNames, int rowCount) {
		super(columnNames, rowCount);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param columnNames
	 * @param rowCount
	 */
	public MyDefaultTableModel(Object[] columnNames, int rowCount) {
		super(columnNames, rowCount);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param data
	 * @param columnNames
	 */
	public MyDefaultTableModel(Vector data, Vector columnNames) {
		super(data, columnNames);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param data
	 * @param columnNames
	 */
	public MyDefaultTableModel(Object[][] data, Object[] columnNames) {
		super(data, columnNames);
		// TODO Auto-generated constructor stub
	}

	@Override
    public boolean isCellEditable(int row, int column) { // custom isCellEditable function
        return false;
    }
}
