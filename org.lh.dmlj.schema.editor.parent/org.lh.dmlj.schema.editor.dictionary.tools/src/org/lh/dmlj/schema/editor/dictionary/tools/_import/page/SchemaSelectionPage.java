/**
 * Copyright (C) 2014  Luc Hermans
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
package org.lh.dmlj.schema.editor.dictionary.tools._import.page;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.lh.dmlj.schema.editor.dictionary.tools._import.common.ContextAttributeKeys;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IRowProcessor;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.ImportSession;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.Query;
import org.lh.dmlj.schema.editor.dictionary.tools.model.Dictionary;
import org.lh.dmlj.schema.editor.dictionary.tools.table.S_010;
import org.lh.dmlj.schema.editor.importtool.AbstractDataEntryPage;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;

public class SchemaSelectionPage extends AbstractDataEntryPage {
	
	private Composite composite;
	private Table table;
	
	private boolean fillTable = false;
	
	public SchemaSelectionPage() {
		super();
	}
	
	@Override
	public void aboutToShow() {	
		// defer getting the schema list until the user actually presses the Next button (avoid that
		// the user is constantly being prompted for a password when he clicks a dictionary in the
		// dictionary selection page)
		fillTable = true;		
	}	

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public Control createControl(Composite parent) {
		
		composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				if (fillTable) {
					fillTable();
					fillTable = false;
				}
			}			
		});
		
		table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validatePage(null);
			}
		});
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table.heightHint = 175;
		table.setLayoutData(gd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnName = new TableColumn(table, SWT.NONE);
		tblclmnName.setWidth(100);
		tblclmnName.setText("Name");
		
		TableColumn tblclmnVersion = new TableColumn(table, SWT.RIGHT);
		tblclmnVersion.setWidth(75);
		tblclmnVersion.setText("Version");
		
		TableColumn tblclmnDescription = new TableColumn(table, SWT.NONE);
		tblclmnDescription.setWidth(250);
		tblclmnDescription.setText("Description");		
		
		return composite;
		
	}

	protected void fillTable() {
		
		final List<TableEntry> tableEntries = new ArrayList<>();
		Dictionary dictionary = getContext().getAttribute(ContextAttributeKeys.DICTIONARY);
		Throwable throwableToPass = null;
		try {
			ImportSession session = new ImportSession(dictionary);
			session.open();	
			Query query = new Query.Builder().forValidSchemaList(session).build();
			session.runQuery(query, new IRowProcessor() {
				@Override
				public void processRow(ResultSet row) throws SQLException {
					String sNam_010 = 
						JdbcTools.removeTrailingSpaces(row.getString(S_010.S_NAM_010));
					int sSer_010 = row.getInt(S_010.S_SER_010);
					String descr_010 = 
						JdbcTools.removeTrailingSpaces(row.getString(S_010.DESCR_010));
					tableEntries.add(new TableEntry(sNam_010, sSer_010, descr_010));				
				}
			});
			session.close();
		} catch (Throwable t) {
			throwableToPass = t;
		}
		
		table.removeAll();
		for (TableEntry tableEntry : tableEntries) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, tableEntry.schemaName);
			item.setText(1, String.valueOf(tableEntry.schemaVersion));
			item.setText(2, String.valueOf(tableEntry.description));
		}	
		table.redraw();	
		
		validatePage(throwableToPass);
		
	}

	private void validatePage(Throwable throwableToPass) {		
		getController().setPageComplete(false);
		String errorMessage = throwableToPass != null ? throwableToPass.getMessage() : null;
		getController().setErrorMessage(errorMessage);
		
		if (table.getSelectionCount() > 0) {			
			TableItem[] selection = table.getSelection();
			getContext().setAttribute(IDataEntryContext.SCHEMA_NAME, 
								 	  selection[0].getText(0));
			getContext().setAttribute(IDataEntryContext.SCHEMA_VERSION,
								 	  Short.valueOf(selection[0].getText(1)));			
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
			super();
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
		
	}

}
