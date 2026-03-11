/**
 * Copyright (C) 2025  Luc Hermans
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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IRowProcessor;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.Query;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.RecordElementsImportSession;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.ui.VirtualKeysConfirmationHandler;
import org.lh.dmlj.schema.editor.dictionary.tools.model.Dictionary;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Rcdsyn079;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sr036;
import org.lh.dmlj.schema.editor.importtool.AbstractDataEntryPage;

public class RecordSynonymSelectionPage extends AbstractDataEntryPage {
	private final List<Rcdsyn079> rcdsyn079s = new ArrayList<>();	
	
	private Table table;
	private Text textRecordSynonymName;
	private Button btnFind;	
	
	@Override
	public Control createControl(Composite parent) {
		var composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		
		var lblRecordSynonymName = new Label(composite, SWT.NONE);
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
		
		var lblNewLabel = new Label(composite, SWT.NONE);
		var gdLblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gdLblNewLabel.verticalIndent = 5;
		lblNewLabel.setLayoutData(gdLblNewLabel);
		lblNewLabel.setText("Record synonyms that match your request:");
		
		table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validatePage(null);
			}
		});
		var gdTable = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		gdTable.heightHint = 175;
		table.setLayoutData(gdTable);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		var tblclmnRecordSynonym = new TableColumn(table, SWT.RIGHT);
		tblclmnRecordSynonym.setWidth(225);
		tblclmnRecordSynonym.setText("Record Synonym");
		
		var tblclmnBaseRecord = new TableColumn(table, SWT.LEFT);
		tblclmnBaseRecord.setWidth(225);
		tblclmnBaseRecord.setText("Base Record");
		
		validatePage(null);
		
		return composite;
	}
	
	protected void findRecordSynonyms() {
		var recordSynonymName = textRecordSynonymName.getText().trim().toUpperCase();
		textRecordSynonymName.setText(recordSynonymName);
		
		table.deselectAll();
		table.removeAll();		
		
		rcdsyn079s.clear();
		
		var tableEntries = new ArrayList<TableEntry>();
		Dictionary dictionary = getContext().getAttribute(ContextAttributeKeys.DICTIONARY);
		var session = new ArrayDeque<RecordElementsImportSession>();
		var throwableToPass = new ArrayDeque<Throwable>();
		try {
			var fSession = new RecordElementsImportSession(dictionary, recordSynonymName);
			session.push(fSession);
			fSession.open();
			VirtualKeysConfirmationHandler.handleConfirmation(fSession, () -> {
				var query = new Query.Builder().forRecordSynonymList(fSession).build();
				fSession.runQuery(query, new IRowProcessor() {
					@Override
					public void processRow(ResultSet row) throws SQLException {
						var sr036 = new Sr036();
						sr036.setRowid(JdbcTools.getRowid(row, Sr036.ROWID));
						sr036.setSrNam036(row.getString(Sr036.SR_NAM_036));
						sr036.setRcdVers036(row.getShort(Sr036.RCD_VERS_036));
										
						var rcdsyn079 = new Rcdsyn079();
						rcdsyn079.setRowid(JdbcTools.getRowid(row, Rcdsyn079.ROWID));
						rcdsyn079.setRsynName079(row.getString(Rcdsyn079.RSYN_NAME_079));
						rcdsyn079.setRsynVer079(row.getShort(Rcdsyn079.RSYN_VER_079));
						rcdsyn079.setSr036(sr036);
						sr036.setRcdsyn079(rcdsyn079);
						rcdsyn079s.add(rcdsyn079);					
						
						var tableEntry = new TableEntry();
						tableEntries.add(tableEntry);	
						tableEntry.recordSynonymVersion = rcdsyn079.getRsynVer079();
						tableEntry.recordName = sr036.getSrNam036();
						tableEntry.recordVersion = sr036.getRcdVers036(); 				
					}
				});
			}, () -> throwableToPass.push(new RuntimeException("IDMSNTWK catalog Schema is defined WITH VIRTUAL KEYS")));
		} catch (Exception e) {
			throwableToPass.push(e);
		} finally {
			if (!session.isEmpty()) {
				session.pop().close();
			}
		}
		
		Collections.sort(tableEntries);
		for (var tableEntry : tableEntries) {
			var item = new TableItem(table, SWT.NONE);
			item.setText(0, recordSynonymName + " version " + tableEntry.recordSynonymVersion);
			item.setText(1, tableEntry.recordName + " version " + tableEntry.recordVersion);
		}
		table.redraw();
		
		validatePage(throwableToPass.isEmpty() ? null : throwableToPass.pop());
	}
	
	private void validatePage(Throwable throwableToPass) {
		getController().setPageComplete(false);
		var errorMessage = throwableToPass != null ? throwableToPass.getMessage() : null;
		getController().setErrorMessage(errorMessage);
		
		if (table.getSelectionCount() > 0) {			
			var selectionIndex = table.getSelectionIndex();
			getContext().setAttribute(ContextAttributeKeys.RCDSYN_079, rcdsyn079s.get(selectionIndex));
			getController().setPageComplete(true);
		} else {
			getContext().clearAttribute(ContextAttributeKeys.RCDSYN_079);			
		}
		btnFind.setEnabled(!textRecordSynonymName.getText().trim().isEmpty());
	}	
	
	private static class TableEntry implements Comparable<TableEntry> {
		private int recordSynonymVersion;
		private String recordName;
		private int recordVersion;

		@Override
		public int compareTo(TableEntry other) {
			return recordSynonymVersion - other.recordSynonymVersion;
		}

		@Override
		public int hashCode() {
			return Objects.hash(recordName, recordSynonymVersion, recordVersion);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TableEntry other = (TableEntry) obj;
			return Objects.equals(recordName, other.recordName)
					&& recordSynonymVersion == other.recordSynonymVersion
					&& recordVersion == other.recordVersion;
		}
		
	}

}
