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
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.lh.dmlj.schema.editor.Plugin;
import org.eclipse.swt.widgets.Label;

public class MainPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private Button btnCentimeters;
	private Button btnInches;
	private Button btnPixels;
	
	private Button btnLogDiagnosticMessages;
	private Label lbldontCheckThis;
	
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
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);
		
		Group composite = new Group(container, SWT.NONE);
		composite.setText("Units");
		composite.setLayout(new GridLayout(3, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		btnCentimeters = new Button(composite, SWT.RADIO);
		btnCentimeters.setBounds(0, 0, 90, 16);
		btnCentimeters.setText("Centimeters");
		
		btnInches = new Button(composite, SWT.RADIO);
		btnInches.setBounds(0, 0, 90, 16);
		btnInches.setText("Inches");
		
		btnPixels = new Button(composite, SWT.RADIO);
		btnPixels.setText("Pixels");
		
		btnLogDiagnosticMessages = new Button(container, SWT.CHECK);
		GridData gd_btnLogDiagnosticMessages = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_btnLogDiagnosticMessages.verticalIndent = 10;
		btnLogDiagnosticMessages.setLayoutData(gd_btnLogDiagnosticMessages);
		btnLogDiagnosticMessages.setText("Log diagnostic messages to the workspace log");
		
		lbldontCheckThis = new Label(container, SWT.NONE);
		GridData gd_lbldontCheckThis = new GridData(SWT.LEFT, SWT.TOP, false, false, 2, 1);
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
		
		Unit unit = Unit.valueOf(store.getDefaultString(PreferenceConstants.UNITS));		
		btnCentimeters.setSelection(unit == Unit.CENTIMETERS);
		btnInches.setSelection(unit == Unit.INCHES);
		btnPixels.setSelection(unit == Unit.PIXELS);
		
		boolean logDiagnosticMessages = 
			store.getDefaultBoolean(PreferenceConstants.LOG_DIAGNISTIC_MESSAGES);
		btnLogDiagnosticMessages.setSelection(logDiagnosticMessages);
		
		doChecks();		
		
	}	
	
	private void initializeValues() {		
		
		IPreferenceStore store = getPreferenceStore();
		
		Unit unit = Unit.valueOf(store.getString(PreferenceConstants.UNITS));		
		btnCentimeters.setSelection(unit == Unit.CENTIMETERS);
		btnInches.setSelection(unit == Unit.INCHES);
		btnPixels.setSelection(unit == Unit.PIXELS);
		
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
		
		Unit unit;
		if (btnCentimeters.getSelection()) {
			unit = Unit.CENTIMETERS;
		} else if (btnInches.getSelection()) {
			unit = Unit.INCHES;
		} else {
			unit = Unit.PIXELS;		
		}
		store.setValue(PreferenceConstants.UNITS, unit.toString());
		
		store.setValue(PreferenceConstants.LOG_DIAGNISTIC_MESSAGES, 
					   Boolean.valueOf(btnLogDiagnosticMessages.getSelection()));
		
		return true;
	}	
	
}