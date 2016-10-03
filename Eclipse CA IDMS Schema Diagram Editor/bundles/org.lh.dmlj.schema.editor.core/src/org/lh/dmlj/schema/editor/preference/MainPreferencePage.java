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
package org.lh.dmlj.schema.editor.preference;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.lh.dmlj.schema.editor.Plugin;

public class MainPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	
	private static final String FILE_EXTENSION_SCHEMA = "schema";
	private static final String FILE_EXTENSION_SCHEMADSL = "schemadsl";
	
	private Button btnLogDiagnosticMessages;
	private Label  lbldontCheckThis;
	private Button btnReadOnlyMode;	
	private Group grpDefaultFileExtension;
	private Button btnFileExtensionSchema;
	private Button btnFileExtensionSchemadsl;
	private Group grpCloseschemadslEditors;
	private Button btnCloseSchemaDslYes;
	private Button btnCloseSchemaDslNo;
	private Button btnCloseSchemaDslAskMe;

	/**
	 * @wbp.parser.constructor
	 */
	public MainPreferencePage() {
		super();
		setDescription("General Settings:");
	}
	
	@Override
	protected Control createContents(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NONE);		
		GridLayout layout = new GridLayout(1, false);
		container.setLayout(layout);
		
		grpDefaultFileExtension = new Group(container, SWT.NONE);
		grpDefaultFileExtension.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpDefaultFileExtension.setText("Default File Extension");
		grpDefaultFileExtension.setLayout(new GridLayout(2, false));
		
		btnFileExtensionSchema = new Button(grpDefaultFileExtension, SWT.RADIO);
		btnFileExtensionSchema.setText(".schema (XML/XMI)");
		
		btnFileExtensionSchemadsl = new Button(grpDefaultFileExtension, SWT.RADIO);
		btnFileExtensionSchemadsl.setText(".schemadsl (DSL)");
		
		grpCloseschemadslEditors = new Group(container, SWT.NONE);
		grpCloseschemadslEditors.setLayout(new GridLayout(3, false));
		GridData gd_grpCloseschemadslEditors = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_grpCloseschemadslEditors.verticalIndent = 5;
		grpCloseschemadslEditors.setLayoutData(gd_grpCloseschemadslEditors);
		grpCloseschemadslEditors.setText("Close editors on workbench shutdown (.schemadsl files only) ?");
		
		btnCloseSchemaDslYes = new Button(grpCloseschemadslEditors, SWT.RADIO);
		btnCloseSchemaDslYes.setText("Yes");
		
		btnCloseSchemaDslNo = new Button(grpCloseschemadslEditors, SWT.RADIO);
		btnCloseSchemaDslNo.setText("No");
		
		btnCloseSchemaDslAskMe = new Button(grpCloseschemadslEditors, SWT.RADIO);
		btnCloseSchemaDslAskMe.setText("Ask me");
				
		btnReadOnlyMode = new Button(container, SWT.CHECK);
		GridData gd_btnReadOnly = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 2, 1);
		gd_btnReadOnly.verticalIndent = 10;
		btnReadOnlyMode.setLayoutData(gd_btnReadOnly);
		btnReadOnlyMode.setText("Open editors in read-only mode (requires close/re-open for open editors)");
		
		btnLogDiagnosticMessages = new Button(container, SWT.CHECK);
		btnLogDiagnosticMessages.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 2, 1));
		btnLogDiagnosticMessages.setText("Log diagnostic messages to the workspace log");
				
		lbldontCheckThis = new Label(container, SWT.NONE);
		GridData gd_lbldontCheckThis = new GridData(SWT.LEFT, SWT.FILL, false, false, 2, 1);
		gd_lbldontCheckThis.verticalIndent = -5;
		gd_lbldontCheckThis.horizontalIndent = 17;
		lbldontCheckThis.setLayoutData(gd_lbldontCheckThis);
		lbldontCheckThis.setText("(don't check this option unless asked)");
		
		initializeValues();
		
		return container;
	}

	private void doChecks() {
		// no checks to perform so far
	}
	
	@Override
	protected IPreferenceStore doGetPreferenceStore() {		
		return Plugin.getDefault().getPreferenceStore();
	}	

	@Override
	public void init(IWorkbench workbench) {		
	}
	
	private void initializeDefaults() {
		
		IPreferenceStore store = getPreferenceStore();
		
		String defaultFileExtension = store.getDefaultString(PreferenceConstants.DEFAULT_FILE_EXTENSION);
		btnFileExtensionSchema.setSelection(defaultFileExtension.equals(FILE_EXTENSION_SCHEMA));
		btnFileExtensionSchemadsl.setSelection(defaultFileExtension.equals(FILE_EXTENSION_SCHEMADSL));
		
		int closeSchemaDslEditors = store.getDefaultInt(PreferenceConstants.CLOSE_SCHEMADSL_EDITORS);
		btnCloseSchemaDslYes.setSelection(closeSchemaDslEditors == PreferenceConstants.CLOSE_SCHEMADSL_EDITORS_YES);
		btnCloseSchemaDslNo.setSelection(closeSchemaDslEditors == PreferenceConstants.CLOSE_SCHEMADSL_EDITORS_NO);
		btnCloseSchemaDslAskMe.setSelection(closeSchemaDslEditors == PreferenceConstants.CLOSE_SCHEMADSL_EDITORS_ASK);
		
		boolean readOnlyMode = store.getDefaultBoolean(PreferenceConstants.READ_ONLY_MODE);
		btnReadOnlyMode.setSelection(readOnlyMode);
		
		boolean logDiagnosticMessages = 
			store.getDefaultBoolean(PreferenceConstants.LOG_DIAGNISTIC_MESSAGES);
		btnLogDiagnosticMessages.setSelection(logDiagnosticMessages);
		
		doChecks();		
		
	}	
	
	private void initializeValues() {		
		
		IPreferenceStore store = getPreferenceStore();
		
		String defaultFileExtension = store.getString(PreferenceConstants.DEFAULT_FILE_EXTENSION);
		btnFileExtensionSchema.setSelection(defaultFileExtension.equals(FILE_EXTENSION_SCHEMA));
		btnFileExtensionSchemadsl.setSelection(defaultFileExtension.equals(FILE_EXTENSION_SCHEMADSL));
		
		int closeSchemaDslEditors = store.getInt(PreferenceConstants.CLOSE_SCHEMADSL_EDITORS);
		btnCloseSchemaDslYes.setSelection(closeSchemaDslEditors == PreferenceConstants.CLOSE_SCHEMADSL_EDITORS_YES);
		btnCloseSchemaDslNo.setSelection(closeSchemaDslEditors == PreferenceConstants.CLOSE_SCHEMADSL_EDITORS_NO);
		btnCloseSchemaDslAskMe.setSelection(closeSchemaDslEditors == PreferenceConstants.CLOSE_SCHEMADSL_EDITORS_ASK);
		
		boolean readOnlyMode = store.getBoolean(PreferenceConstants.READ_ONLY_MODE);
		btnReadOnlyMode.setSelection(readOnlyMode);
		
		boolean logDiagnosticMessages = 
			store.getBoolean(PreferenceConstants.LOG_DIAGNISTIC_MESSAGES);
		btnLogDiagnosticMessages.setSelection(logDiagnosticMessages);
		
		doChecks();
		
	}
	
	@Override
	protected void performApply() {
		storeValues();
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
		
		String defaultFileExtension = 
			btnFileExtensionSchemadsl.getSelection() ? FILE_EXTENSION_SCHEMADSL : FILE_EXTENSION_SCHEMA;
		store.setValue(PreferenceConstants.DEFAULT_FILE_EXTENSION, defaultFileExtension);
		
		int closeSchemaDslEditors;
		if (btnCloseSchemaDslYes.getSelection()) {
			closeSchemaDslEditors = PreferenceConstants.CLOSE_SCHEMADSL_EDITORS_YES;
		} else if (btnCloseSchemaDslNo.getSelection()) {
			closeSchemaDslEditors = PreferenceConstants.CLOSE_SCHEMADSL_EDITORS_NO;
		} else {
			closeSchemaDslEditors = PreferenceConstants.CLOSE_SCHEMADSL_EDITORS_ASK;
		}
		store.setValue(PreferenceConstants.CLOSE_SCHEMADSL_EDITORS, closeSchemaDslEditors);
		
		store.setValue(PreferenceConstants.READ_ONLY_MODE, Boolean.valueOf(btnReadOnlyMode.getSelection()));
		
		store.setValue(PreferenceConstants.LOG_DIAGNISTIC_MESSAGES, 
					   Boolean.valueOf(btnLogDiagnosticMessages.getSelection()));
		
		return true;
	}	
}
