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
	private Button btnRemoveProcedure;
	private List listProcedures;
		
	public ImportPreferencePage() {
		setDescription("Import settings:");
	}

	@Override
	protected Control createContents(Composite parent) {
		var container = new Composite(parent, SWT.NONE);		
		var layout = new GridLayout(3, false);
		container.setLayout(layout);
		
		var lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		lblNewLabel.setText("Database procedures used for COMPRESSION:");
		
		listProcedures = new List(container, SWT.BORDER);
		var gdList = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 3);
		gdList.widthHint = 100;
		listProcedures.setLayoutData(gdList);
		
		var label = new Label(container, SWT.NONE);
		var gdLabel = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
		gdLabel.heightHint = 25;
		label.setLayoutData(gdLabel);
		
		var btnAddProcedure = new Button(container, SWT.NONE);
		btnAddProcedure.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addProcedure();
			}
		});
		btnAddProcedure.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
		btnAddProcedure.setText("Add...");
		
		new Label(container, SWT.NONE);
		
		btnRemoveProcedure = new Button(container, SWT.NONE);
		btnRemoveProcedure.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeProcedure();
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
	
	private void addProcedure() {
		var currentList =new ArrayList<>(Arrays.asList(listProcedures.getItems()));
		var dialog = new AddProcedureDialog(getShell(), currentList);
		if (dialog.open() == IDialogConstants.OK_ID) {
			listProcedures.removeAll();
			currentList.add(dialog.getProcedureName());
			Collections.sort(currentList);
			int i = 0;
			for (var procedureName : currentList) {
				listProcedures.add(procedureName);
				if (procedureName.equals(dialog.getProcedureName())) {
					listProcedures.select(i);
				}
				i += 1;
			}
			enableOrDisable();
		}		
	}
	
	private void removeProcedure() {
		var i = listProcedures.getSelectionIndex();
		listProcedures.deselect(i);
		listProcedures.remove(i);
		enableOrDisable();
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
		var procedureNames = new ArrayList<String>();
		var tokenizer = new StringTokenizer(procedures.trim(), ",");
		while (tokenizer.hasMoreTokens()) {
			var procedureName = tokenizer.nextToken().trim(); // we assume this is valid
			procedureNames.add(procedureName);
		}
		Collections.sort(procedureNames);
		for (var procedureName : procedureNames) {
			listProcedures.add(procedureName);
		}
	}
	
	@Override
	public void init(IWorkbench workbench) {
		// nothing to do here
	}
	
	private void initializeDefaults() {
		fillProcedureList(getPreferenceStore().getDefaultString(PreferenceConstants.COMPRESSION_PROCEDURES));
		enableOrDisable();
	}	
	
	private void initializeValues() {		
		fillProcedureList(getPreferenceStore().getString(PreferenceConstants.COMPRESSION_PROCEDURES));		
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
		
		var p = new StringBuilder();
		for (var procedure : listProcedures.getItems()) {
			if (!p.isEmpty()) {
				p.append(", ");
			}
			p.append(procedure);
		}
		getPreferenceStore().setValue(PreferenceConstants.COMPRESSION_PROCEDURES, p.toString());			
		
		return true;
	}

}
