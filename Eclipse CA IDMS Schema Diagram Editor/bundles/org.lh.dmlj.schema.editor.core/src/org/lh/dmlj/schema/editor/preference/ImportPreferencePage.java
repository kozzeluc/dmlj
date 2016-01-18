/**
 * Copyright (C) 2013  Luc Hermans
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
package org.lh.dmlj.schema.editor.preference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.StringTokenizer;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.wizard._import.schema.AddProcedureDialog;

public class ImportPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private Button 	btnRemoveProcedure;
	private List 	listProcedures;
	
	/**
	 * @wbp.parser.constructor
	 */
	public ImportPreferencePage() {
		super();
		setDescription("Import settings:");
	}

	@Override
	protected Control createContents(Composite parent) {
		
		final Composite container = new Composite(parent, SWT.NONE);		
		GridLayout layout = new GridLayout(3, false);
		container.setLayout(layout);
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		lblNewLabel.setText("Database procedures used for COMPRESSION:");
		
		listProcedures = new List(container, SWT.BORDER);
		GridData gd_list = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 3);
		gd_list.widthHint = 100;
		listProcedures.setLayoutData(gd_list);
		
		Label label = new Label(container, SWT.NONE);
		GridData gd_label = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
		gd_label.heightHint = 25;
		label.setLayoutData(gd_label);
		
		Button btnAddProcedure = new Button(container, SWT.NONE);
		btnAddProcedure.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				java.util.List<String> currentList = 
					new ArrayList<>(Arrays.asList(listProcedures.getItems()));
				AddProcedureDialog dialog = 
					new AddProcedureDialog(container.getShell(), currentList);
				if (dialog.open() == IDialogConstants.OK_ID) {
					listProcedures.removeAll();
					currentList.add(dialog.getProcedureName());
					Collections.sort(currentList);
					int i = 0;
					for (String procedureName : currentList) {
						listProcedures.add(procedureName);
						if (procedureName.equals(dialog.getProcedureName())) {
							listProcedures.select(i);
						}
						i += 1;
					}
					enableOrDisable();
				}
			}
		});
		btnAddProcedure.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
		btnAddProcedure.setText("Add...");
		
		new Label(container, SWT.NONE);
		
		btnRemoveProcedure = new Button(container, SWT.NONE);
		btnRemoveProcedure.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int i = listProcedures.getSelectionIndex();
				listProcedures.deselect(i);
				listProcedures.remove(i);
				enableOrDisable();
			}
		});
		btnRemoveProcedure.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnRemoveProcedure.setText("Remove");

		new Label(container, SWT.NONE);
		
		listProcedures.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableOrDisable();
			}
		});
		
		initializeValues();
		
		return container;
		
	}
	
	private void doChecks() {
		// nothing to check
	}

	@Override
	protected IPreferenceStore doGetPreferenceStore() {		
		return Plugin.getDefault().getPreferenceStore();
	}

	protected void enableOrDisable() {
		btnRemoveProcedure.setEnabled(listProcedures.getSelectionCount() > 0);
	}

	private void fillProcedureList(String procedures) {
		listProcedures.removeAll();
		java.util.List<String> procedureNames = new ArrayList<>();
		StringTokenizer tokenizer = new StringTokenizer(procedures.trim(), ",");
		while (tokenizer.hasMoreTokens()) {
			String procedureName = tokenizer.nextToken().trim(); // we assume this is valid
			procedureNames.add(procedureName);
		}
		Collections.sort(procedureNames);
		for (String procedureName : procedureNames) {
			listProcedures.add(procedureName);
		}
	}
	
	@Override
	public void init(IWorkbench workbench) {
	}
	
	private void initializeDefaults() {
		
		IPreferenceStore store = getPreferenceStore();
				
		fillProcedureList(store.getDefaultString(PreferenceConstants.COMPRESSION_PROCEDURES));
		
		doChecks();
		
		enableOrDisable();
		
	}	
	
	private void initializeValues() {		
		
		IPreferenceStore store = getPreferenceStore();
		
		fillProcedureList(store.getString(PreferenceConstants.COMPRESSION_PROCEDURES));		
		
		doChecks();
		
		enableOrDisable();
		
	}
	
	@Override
	protected void performApply() {
		storeValues();
		initializeValues();
	}
	
	@Override
	protected void performDefaults() {
		super.performDefaults();
		initializeDefaults();
	}
	
	@Override
	public boolean performOk() {
		return storeValues();		
	}
	
	private boolean storeValues() {
		
		setErrorMessage(null);
		
		IPreferenceStore store = getPreferenceStore();
		
		StringBuilder p = new StringBuilder();
		for (String procedure : listProcedures.getItems()) {
			if (p.length() > 0) {
				p.append(", ");
			}
			p.append(procedure);
		}
		store.setValue(PreferenceConstants.COMPRESSION_PROCEDURES, p.toString());			
		
		return true;
	}

}
