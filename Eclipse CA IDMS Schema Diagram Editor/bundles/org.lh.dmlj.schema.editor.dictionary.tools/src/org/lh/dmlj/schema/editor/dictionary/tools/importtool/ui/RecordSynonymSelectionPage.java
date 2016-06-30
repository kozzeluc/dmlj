/**
 * Copyright (C) 2016  Luc Hermans
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program.  If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact information: kozzeluc@gmail.com.
 */
package org.lh.dmlj.schema.editor.dictionary.tools.importtool.ui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.lh.dmlj.schema.editor.dictionary.tools.importtool.context.ContextAttributeKeys;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IQuery;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IRowProcessor;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.Query;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.RecordElementsImportSession;
import org.lh.dmlj.schema.editor.dictionary.tools.model.Dictionary;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Rcdsyn_079;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sr_036;
import org.lh.dmlj.schema.editor.importtool.AbstractDataEntryPage;

public class RecordSynonymSelectionPage extends AbstractDataEntryPage {

	private Composite composite;
	private Table table;
	private Text textRecordSynonymName;
	private Button btnFind;
	private List<Rcdsyn_079> rcdsyn_079s = new ArrayList<>();
	
	public RecordSynonymSelectionPage() {
		super();
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public Control createControl(Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		
		composite.setLayout(new GridLayout(3, false));
		
		Label lblRecordSynonymName = new Label(composite, SWT.NONE);
		lblRecordSynonymName.setText("Record synonym name:");
		
		textRecordSynonymName = new Text(composite, SWT.BORDER);
		textRecordSynonymName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				validatePage(null);
			}
		});
		textRecordSynonymName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnFind = new Button(composite, SWT.NONE);
		btnFind.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				findRecordSynonyms();
			}
		});
		btnFind.setText("Find");
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_lblNewLabel.verticalIndent = 5;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText("Record synonyms that match your request:");
		
		table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validatePage(null);
			}
		});
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		gd_table.heightHint = 175;
		table.setLayoutData(gd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnSynonymVersion = new TableColumn(table, SWT.RIGHT);
		tblclmnSynonymVersion.setWidth(75);
		tblclmnSynonymVersion.setText("Syn. Version");
		
		TableColumn tblclmnRecordName = new TableColumn(table, SWT.LEFT);
		tblclmnRecordName.setWidth(250);
		tblclmnRecordName.setText("Record Name");	
		
		TableColumn tblclmnRecordVersion = new TableColumn(table, SWT.RIGHT);
		tblclmnRecordVersion.setWidth(105);
		tblclmnRecordVersion.setText("Record Version");
		
		validatePage(null);
		
		return composite;
	}
	
	protected void findRecordSynonyms() {
		table.deselectAll();
		table.removeAll();		
		
		rcdsyn_079s.clear();
		
		final List<TableEntry> tableEntries = new ArrayList<>();
		Dictionary dictionary = getContext().getAttribute(ContextAttributeKeys.DICTIONARY);
		Throwable throwableToPass = null;
		try {
			String recordSynonymName = textRecordSynonymName.getText().trim().toUpperCase();
			textRecordSynonymName.setText(recordSynonymName);
			RecordElementsImportSession session = 
				new RecordElementsImportSession(dictionary, recordSynonymName);
			session.open();	
			IQuery query = new Query.Builder().forRecordSynonymList(session).build();
			session.runQuery(query, new IRowProcessor() {
				@Override
				public void processRow(ResultSet row) throws SQLException {					
					
					Sr_036 sr_036 = new Sr_036();
					sr_036.setDbkey(JdbcTools.getDbkey(row, Sr_036.ROWID));
					sr_036.setSrNam_036(row.getString(Sr_036.SR_NAM_036));
					sr_036.setRcdVers_036(row.getShort(Sr_036.RCD_VERS_036));
									
					Rcdsyn_079 rcdsyn_079 = new Rcdsyn_079();
					rcdsyn_079.setDbkey(JdbcTools.getDbkey(row, Rcdsyn_079.ROWID));
					rcdsyn_079.setRsynName_079(row.getString(Rcdsyn_079.RSYN_NAME_079));
					rcdsyn_079.setRsynVer_079(row.getShort(Rcdsyn_079.RSYN_VER_079));
					rcdsyn_079.setSr_036(sr_036);
					sr_036.setRcdsyn_079(rcdsyn_079);
					rcdsyn_079s.add(rcdsyn_079);					
					
					TableEntry tableEntry = new TableEntry();
					tableEntries.add(tableEntry);	
					tableEntry.recordSynonymVersion = rcdsyn_079.getRsynVer_079();
					tableEntry.recordName = sr_036.getSrNam_036();
					tableEntry.recordVersion = sr_036.getRcdVers_036(); 				
				}
			});
			session.close();
		} catch (Throwable t) {
			throwableToPass = t;
		}
		
		Collections.sort(tableEntries);
		for (TableEntry tableEntry : tableEntries) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, String.valueOf(tableEntry.recordSynonymVersion));
			item.setText(1, tableEntry.recordName);
			item.setText(2, String.valueOf(tableEntry.recordVersion));
		}
		table.redraw();
		
		validatePage(throwableToPass);
	}
	
	private void validatePage(Throwable throwableToPass) {
		
		getController().setPageComplete(false);
		String errorMessage = throwableToPass != null ? throwableToPass.getMessage() : null;
		getController().setErrorMessage(errorMessage);
		
		if (table.getSelectionCount() > 0) {			
			int selectionIndex = table.getSelectionIndex();
			getContext().setAttribute(ContextAttributeKeys.RCDSYN_079, rcdsyn_079s.get(selectionIndex));
			getController().setPageComplete(true);
		} else {
			getContext().clearAttribute(ContextAttributeKeys.RCDSYN_079);			
		}	
		
		btnFind.setEnabled(!textRecordSynonymName.getText().trim().isEmpty());
	}	
	
	private static class TableEntry implements Comparable<TableEntry> {
		
		private int    recordSynonymVersion;
		private String recordName;
		private int    recordVersion;

		@Override
		public int compareTo(TableEntry other) {
			return recordSynonymVersion - other.recordSynonymVersion;
		}
		
	}

}
