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
	private Button btnReadOnlyMode;
	private Button btnFileExtensionSchema;
	private Button btnFileExtensionSchemadsl;
	private Button btnCloseSchemaDslYes;
	private Button btnCloseSchemaDslNo;
	private Button btnCloseSchemaDslAskMe;
	private Button btnRunDslWarmUpJobOnStartup;
	
	public MainPreferencePage() {
		setDescription("General Settings:");
	}
	
	@Override
	protected Control createContents(Composite parent) {
		var container = new Composite(parent, SWT.NONE);		
		var layout = new GridLayout(1, false);
		container.setLayout(layout);
		
		var grpDefaultFileExtension = new Group(container, SWT.NONE);
		grpDefaultFileExtension.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpDefaultFileExtension.setText("Default File Extension");
		grpDefaultFileExtension.setLayout(new GridLayout(2, false));
		
		btnFileExtensionSchema = new Button(grpDefaultFileExtension, SWT.RADIO);
		btnFileExtensionSchema.setText(".schema (XML/XMI)");
		
		btnFileExtensionSchemadsl = new Button(grpDefaultFileExtension, SWT.RADIO);
		btnFileExtensionSchemadsl.setText(".schemadsl (DSL)");
		
		var grpCloseschemadslEditors = new Group(container, SWT.NONE);
		grpCloseschemadslEditors.setLayout(new GridLayout(3, false));
		var gdGrpCloseschemadslEditors = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gdGrpCloseschemadslEditors.verticalIndent = 5;
		grpCloseschemadslEditors.setLayoutData(gdGrpCloseschemadslEditors);
		grpCloseschemadslEditors.setText("Close editors on workbench shutdown (.schemadsl files only) ?");
		
		btnCloseSchemaDslYes = new Button(grpCloseschemadslEditors, SWT.RADIO);
		btnCloseSchemaDslYes.setText("Yes");
		
		btnCloseSchemaDslNo = new Button(grpCloseschemadslEditors, SWT.RADIO);
		btnCloseSchemaDslNo.setText("No");
		
		btnCloseSchemaDslAskMe = new Button(grpCloseschemadslEditors, SWT.RADIO);
		btnCloseSchemaDslAskMe.setText("Ask me");
		
		btnRunDslWarmUpJobOnStartup = new Button(container, SWT.CHECK);
		var gdBtnRunDslWarmUpJobOnStartup = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdBtnRunDslWarmUpJobOnStartup.verticalIndent = 10;
		btnRunDslWarmUpJobOnStartup.setLayoutData(gdBtnRunDslWarmUpJobOnStartup);
		btnRunDslWarmUpJobOnStartup.setText("Run DSL warm up job on startup");
				
		btnReadOnlyMode = new Button(container, SWT.CHECK);
		btnReadOnlyMode.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 2, 1));
		btnReadOnlyMode.setText("Open editors in read-only mode (requires close/re-open for open editors)");
		
		btnLogDiagnosticMessages = new Button(container, SWT.CHECK);
		btnLogDiagnosticMessages.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 2, 1));
		btnLogDiagnosticMessages.setText("Log diagnostic messages to the workspace log");
				
		var lbldontCheckThis = new Label(container, SWT.NONE);
		var gdLbldontCheckThis = new GridData(SWT.LEFT, SWT.FILL, false, false, 2, 1);
		gdLbldontCheckThis.verticalIndent = -5;
		gdLbldontCheckThis.horizontalIndent = 17;
		lbldontCheckThis.setLayoutData(gdLbldontCheckThis);
		lbldontCheckThis.setText("(don't check this option unless asked)");
		
		initializeValues();
		
		return container;
	}
	
	@Override
	protected IPreferenceStore doGetPreferenceStore() {		
		return Plugin.getDefault().getPreferenceStore();
	}	

	@Override
	public void init(IWorkbench workbench) {
		// nothing to do here
	}
	
	private void initializeDefaults() {
		var store = getPreferenceStore();
		
		var defaultFileExtension = store.getDefaultString(PreferenceConstants.DEFAULT_FILE_EXTENSION);
		btnFileExtensionSchema.setSelection(defaultFileExtension.equals(FILE_EXTENSION_SCHEMA));
		btnFileExtensionSchemadsl.setSelection(defaultFileExtension.equals(FILE_EXTENSION_SCHEMADSL));
		
		var closeSchemaDslEditors = store.getDefaultInt(PreferenceConstants.CLOSE_SCHEMADSL_EDITORS);
		btnCloseSchemaDslYes.setSelection(closeSchemaDslEditors == PreferenceConstants.CLOSE_SCHEMADSL_EDITORS_YES);
		btnCloseSchemaDslNo.setSelection(closeSchemaDslEditors == PreferenceConstants.CLOSE_SCHEMADSL_EDITORS_NO);
		btnCloseSchemaDslAskMe.setSelection(closeSchemaDslEditors == PreferenceConstants.CLOSE_SCHEMADSL_EDITORS_ASK);
		
		var runDslWarmUpJobOnStartup = store.getDefaultBoolean(PreferenceConstants.RUN_DSL_WARM_UP_JOB_ON_STARTUP);
		btnRunDslWarmUpJobOnStartup.setSelection(runDslWarmUpJobOnStartup);
		
		var readOnlyMode = store.getDefaultBoolean(PreferenceConstants.READ_ONLY_MODE);
		btnReadOnlyMode.setSelection(readOnlyMode);
		
		var logDiagnosticMessages = store.getDefaultBoolean(PreferenceConstants.LOG_DIAGNISTIC_MESSAGES);
		btnLogDiagnosticMessages.setSelection(logDiagnosticMessages);	
	}	
	
	private void initializeValues() {		
		var store = getPreferenceStore();
		
		var defaultFileExtension = store.getString(PreferenceConstants.DEFAULT_FILE_EXTENSION);
		btnFileExtensionSchema.setSelection(defaultFileExtension.equals(FILE_EXTENSION_SCHEMA));
		btnFileExtensionSchemadsl.setSelection(defaultFileExtension.equals(FILE_EXTENSION_SCHEMADSL));
		
		var closeSchemaDslEditors = store.getInt(PreferenceConstants.CLOSE_SCHEMADSL_EDITORS);
		btnCloseSchemaDslYes.setSelection(closeSchemaDslEditors == PreferenceConstants.CLOSE_SCHEMADSL_EDITORS_YES);
		btnCloseSchemaDslNo.setSelection(closeSchemaDslEditors == PreferenceConstants.CLOSE_SCHEMADSL_EDITORS_NO);
		btnCloseSchemaDslAskMe.setSelection(closeSchemaDslEditors == PreferenceConstants.CLOSE_SCHEMADSL_EDITORS_ASK);
		
		var runDslWarmUpJobOnStartup = store.getBoolean(PreferenceConstants.RUN_DSL_WARM_UP_JOB_ON_STARTUP);
		btnRunDslWarmUpJobOnStartup.setSelection(runDslWarmUpJobOnStartup);
		
		var readOnlyMode = store.getBoolean(PreferenceConstants.READ_ONLY_MODE);
		btnReadOnlyMode.setSelection(readOnlyMode);
		
		var logDiagnosticMessages = store.getBoolean(PreferenceConstants.LOG_DIAGNISTIC_MESSAGES);
		btnLogDiagnosticMessages.setSelection(logDiagnosticMessages);
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
		var defaultFileExtension = btnFileExtensionSchemadsl.getSelection() ? FILE_EXTENSION_SCHEMADSL : FILE_EXTENSION_SCHEMA;
		int closeSchemaDslEditors;
		if (btnCloseSchemaDslYes.getSelection()) {
			closeSchemaDslEditors = PreferenceConstants.CLOSE_SCHEMADSL_EDITORS_YES;
		} else if (btnCloseSchemaDslNo.getSelection()) {
			closeSchemaDslEditors = PreferenceConstants.CLOSE_SCHEMADSL_EDITORS_NO;
		} else {
			closeSchemaDslEditors = PreferenceConstants.CLOSE_SCHEMADSL_EDITORS_ASK;
		}
		var store = getPreferenceStore();
		store.setValue(PreferenceConstants.DEFAULT_FILE_EXTENSION, defaultFileExtension);
		store.setValue(PreferenceConstants.CLOSE_SCHEMADSL_EDITORS, closeSchemaDslEditors);
		store.setValue(PreferenceConstants.RUN_DSL_WARM_UP_JOB_ON_STARTUP, btnRunDslWarmUpJobOnStartup.getSelection());
		store.setValue(PreferenceConstants.READ_ONLY_MODE, btnReadOnlyMode.getSelection());
		store.setValue(PreferenceConstants.LOG_DIAGNISTIC_MESSAGES, btnLogDiagnosticMessages.getSelection());
		return true;
	}
	
}
