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

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Objects;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.lh.dmlj.schema.editor.dictionary.tools.importtool.context.ContextAttributeKeys;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.DictionarySession;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IRowProcessor;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.Query;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.ui.VirtualKeysConfirmationHandler;
import org.lh.dmlj.schema.editor.dictionary.tools.model.Dictionary;
import org.lh.dmlj.schema.editor.dictionary.tools.table.S010;
import org.lh.dmlj.schema.editor.importtool.AbstractDataEntryPage;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;

public class SchemaSelectionPage extends AbstractDataEntryPage {
	private boolean fillTable = false;
	
	private Table table;
	
	@Override
	public void aboutToShow() {	
		// defer getting the schema list until the user actually presses the Next button (avoid that the user is
		// constantly being prompted for a password when he clicks a dictionary in the dictionary selection page)
		fillTable = true;
	}	
	
	@Override
	public Control createControl(Composite parent) {
		var composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.addPaintListener(e -> {
			if (fillTable) {
				fillTableWithCursorBusy();
				fillTable = false;
			}
		});
		
		table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validatePage(null);
			}
		});
		var gdtable = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gdtable.heightHint = 175;
		table.setLayoutData(gdtable);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		var tblclmnName = new TableColumn(table, SWT.NONE);
		tblclmnName.setWidth(100);
		tblclmnName.setText("Name");
		
		var tblclmnVersion = new TableColumn(table, SWT.RIGHT);
		tblclmnVersion.setWidth(75);
		tblclmnVersion.setText("Version");
		
		var tblclmnDescription = new TableColumn(table, SWT.NONE);
		tblclmnDescription.setWidth(250);
		tblclmnDescription.setText("Description");		
		
		return composite;
	}

	private void fillTable() {
		var tableEntries = new ArrayList<TableEntry>();
		Dictionary dictionary = getContext().getAttribute(ContextAttributeKeys.DICTIONARY);
		var session = new ArrayDeque<DictionarySession>();
		var throwableToPass = new ArrayDeque<Throwable>();
		try {
			var fSession = new DictionarySession(dictionary, "Retrieve valid schema list from dictionary " + dictionary.getId());
			session.push(fSession);
			fSession.open();
			VirtualKeysConfirmationHandler.handleConfirmation(fSession, () -> {
				var query = new Query.Builder().forValidSchemaList(fSession).build();
				fSession.runQuery(query, new IRowProcessor() {
					@Override
					public void processRow(ResultSet row) throws SQLException {
						var sNam010 = JdbcTools.removeTrailingSpaces(row.getString(S010.S_NAM_010));
						var sSer010 = row.getInt(S010.S_SER_010);
						var descr010 = JdbcTools.removeTrailingSpaces(row.getString(S010.DESCR_010));
						tableEntries.add(new TableEntry(sNam010, sSer010, descr010));				
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
		
		table.removeAll();
		for (var tableEntry : tableEntries) {
			var item = new TableItem(table, SWT.NONE);
			item.setText(0, tableEntry.schemaName);
			item.setText(1, String.valueOf(tableEntry.schemaVersion));
			item.setText(2, String.valueOf(tableEntry.description));
		}	
		table.redraw();	
		
		validatePage(throwableToPass.isEmpty() ? null: throwableToPass.pop());
	}

	private void fillTableWithCursorBusy() {
		Dictionary dictionary = getContext().getAttribute(ContextAttributeKeys.DICTIONARY);
		var runnableWithProgress = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				monitor.beginTask("Building valid schema list for dictionary " + dictionary.getId() + "...", IProgressMonitor.UNKNOWN);
				fillTable();
				monitor.done();
			}};
			org.lh.dmlj.schema.editor.Plugin.getDefault().runWithOperationInProgressIndicator(runnableWithProgress);
	}

	private void validatePage(Throwable throwableToPass) {		
		getController().setPageComplete(false);
		var errorMessage = throwableToPass != null ? throwableToPass.getMessage() : null;
		getController().setErrorMessage(errorMessage);
		
		if (table.getSelectionCount() > 0) {			
			var selection = table.getSelection();
			getContext().setAttribute(IDataEntryContext.SCHEMA_NAME, selection[0].getText(0));
			getContext().setAttribute(IDataEntryContext.SCHEMA_VERSION, Short.valueOf(selection[0].getText(1)));
			getController().setPageComplete(true);
		} else {
			getContext().clearAttribute(IDataEntryContext.SCHEMA_NAME);
			getContext().clearAttribute(IDataEntryContext.SCHEMA_VERSION);			
		}		
	}
	
	private static class TableEntry implements Comparable<TableEntry> {
		private String description;
		private String schemaName;
		private int schemaVersion; 		
		
		private TableEntry(String schemaName, int schemaVersion, String description) {
			this.schemaName = schemaName;
			this.schemaVersion = schemaVersion;
			this.description = description;
		}

		@Override
		public int compareTo(TableEntry other) {
			if (schemaName.equals(other.schemaName)) {
				return schemaVersion - other.schemaVersion;
			} else {
				return schemaName.compareTo(other.schemaName);
			}
		}

		@Override
		public int hashCode() {
			return Objects.hash(description, schemaName, schemaVersion);
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
			return Objects.equals(description, other.description)
					&& Objects.equals(schemaName, other.schemaName)
					&& schemaVersion == other.schemaVersion;
		}
		
	}

}
