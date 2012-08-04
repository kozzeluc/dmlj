package org.lh.dmlj.schema.editor.wizard;

import static dmlj.core.NavigationType.NEXT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.lh.dmlj.idmsntwk.CalcKey_Ooak_012;
import org.lh.dmlj.idmsntwk.CalcKey_S_010;
import org.lh.dmlj.idmsntwk.Ooak_012;
import org.lh.dmlj.idmsntwk.S_010;

import dmlj.core.IDatabase;

public class NewSchemaPage3 extends WizardPage {
	private Table table;

	protected NewSchemaPage3() {
		super("page3", "Schema", null);		
		setMessage("Select a schema from the dictionary");		
	}

	@Override
	public void createControl(Composite parent) {
		Composite top = new Composite(parent, SWT.NONE);
		
		setControl(top);
		top.setLayout(new GridLayout(1, false));
		
		table = new Table(top, SWT.BORDER | SWT.FULL_SELECTION);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validatePage();
			}
		});
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
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
			
	}
	
	public String getSchemaDescription() {
		if (table.getSelectionCount() > 0) {
			TableItem[] selection = table.getSelection();
			return selection[0].getText(2);
		} else {
			return null;
		}
	}	
	
	public String getSchemaName() {
		if (table.getSelectionCount() > 0) {
			TableItem[] selection = table.getSelection();
			return selection[0].getText(0);
		} else {
			return null;
		}
	}
	
	public Integer getSchemaVersion() {
		if (table.getSelectionCount() > 0) {
			TableItem[] selection = table.getSelection();
			return Integer.valueOf(selection[0].getText(1));
		} else {
			return null;
		}
	}
	
	public void setDictionary(IDatabase dictionary) {
		
		// invalidate the page...
		setPageComplete(false);
		
		// clear the table...
		table.removeAll();
		
		// create an empty list of table entries...
		List<TableEntry> tableEntries = new ArrayList<>();
		
		if (dictionary != null) {
			// first of all, take care of all IDMSNTWK schemas; the IDMSNTWK version
			// 1 schema is not a member of the OOAK-S set...
			Object calcKey = new CalcKey_S_010("IDMSNTWK");
			S_010 s_010a = dictionary.find(S_010.class, calcKey);
			while (s_010a != null) {
				TableEntry tableEntry = 
					new TableEntry(s_010a.getSNam_010(), s_010a.getSSer_010(), 
								   s_010a.getDescr_010());
				tableEntries.add(tableEntry);
				s_010a = dictionary.find(s_010a);
			}		
			
			// take care of all other schemas, ignoring the "NON IDMS" schema
			// and schemas that are in error...
			Ooak_012 ooak_012 = 
				dictionary.find(Ooak_012.class, new CalcKey_Ooak_012("OOAK"));
			for (S_010 s_010 : 
				 dictionary.<S_010>walk(ooak_012, "OOAK-S", NEXT)) {
				
				if (!s_010.getSNam_010().equals("NON IDMS") &&
					!s_010.getSNam_010().equals("NON IDMS") &&
					s_010.getErr_010() == 0) {
					
					TableEntry tableEntry = 
						new TableEntry(s_010.getSNam_010(), s_010.getSSer_010(), 
									   s_010.getDescr_010());
					tableEntries.add(tableEntry);
				}
			}
			
			// sort our list of table entries and add all entries to the table...
			Collections.sort(tableEntries);
			for (TableEntry tableEntry : tableEntries) {
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, tableEntry.schemaName);
				item.setText(1, String.valueOf(tableEntry.schemaVersion));
				item.setText(2, String.valueOf(tableEntry.description));
			}
		}
		
		// refresh the table...
		table.redraw();
	}

	private void validatePage() {
		
		setPageComplete(false);
		setErrorMessage(null);
		
		if (table.getSelectionCount() > 0) {
			setPageComplete(true);
		}
	}
	
	private static class TableEntry implements Comparable<TableEntry> {
		
		private String 	description;
		private String 	schemaName;
		private int 	schemaVersion; 		
		
		private TableEntry(String schemaName, int schemaVersion, 
						   String description) {
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