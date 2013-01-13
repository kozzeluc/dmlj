package org.lh.dmlj.schema.editor.importtool.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.lh.dmlj.schema.editor.importtool.AbstractDataEntryPage;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;

public class OptionsPage extends AbstractDataEntryPage {
	
	private Button 	  btnDdlcatlod;
	private Button 	  btnLooak_155;
	private Button 	  btnOoak_012;
	private Button    btnUseRecordidAsSuffix;
	private Button    btnUse3Digits;
	private Button    btnUse4Digits;
	private Composite composite;
	private Label     lblIdmsntwk;
	
	public OptionsPage() {
		super();
	}

	@Override
	public void aboutToShow() {
		
		String schemaName = 
			getContext().getAttribute(IDataEntryContext.SCHEMA_NAME);
		Short schemaVersion = 
			getContext().getAttribute(IDataEntryContext.SCHEMA_VERSION);
						
		boolean b = 
			schemaName != null && schemaVersion != null &&
			schemaName.equals("IDMSNTWK") && schemaVersion.intValue() == 1;		

		lblIdmsntwk.setVisible(b);
		btnOoak_012.setVisible(b);
		btnLooak_155.setVisible(b);
		btnDdlcatlod.setVisible(b);
		
		validatePage();
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public Control createControl(Composite parent) {
		
		composite = new Composite(parent, SWT.NONE);	

		composite.setLayout(new GridLayout(2, false));
		
		Label lblGeneralOptions = new Label(composite, SWT.NONE);
		lblGeneralOptions.setText("General options :");
		new Label(composite, SWT.NONE);
		
		btnUseRecordidAsSuffix = new Button(composite, SWT.CHECK);
		btnUseRecordidAsSuffix.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean b = btnUseRecordidAsSuffix.getSelection();
				btnUse3Digits.setEnabled(b);
				btnUse4Digits.setEnabled(b);
				validatePage();
			}
		});
		btnUseRecordidAsSuffix.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnUseRecordidAsSuffix.setSelection(true);
		btnUseRecordidAsSuffix.setText("Use record-id as suffix if one is not present and cannot be derived automatically");
		
		btnUse3Digits = new Button(composite, SWT.RADIO);
		btnUse3Digits.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validatePage();
			}
		});
		GridData gd_btnUseDigits = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnUseDigits.horizontalIndent = 20;
		btnUse3Digits.setLayoutData(gd_btnUseDigits);
		btnUse3Digits.setText("Use 3 digits (e.g. '-013')");
		
		btnUse4Digits = new Button(composite, SWT.RADIO);
		btnUse4Digits.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validatePage();
			}
		});
		btnUse4Digits.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnUse4Digits.setSelection(true);
		btnUse4Digits.setText("Use 4 digits (e.g. '-0013')");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		lblIdmsntwk = new Label(composite, SWT.NONE);
		lblIdmsntwk.setText("IDMSNTWK version 1 options :");
		new Label(composite, SWT.NONE);
		
		btnOoak_012 = new Button(composite, SWT.CHECK);
		btnOoak_012.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnOoak_012.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validatePage();
			}
		});
		btnOoak_012.setSelection(true);
		btnOoak_012.setText("Add missing offset-expression for record OOAK-012 in area DDLDML");
		
		btnLooak_155 = new Button(composite, SWT.CHECK);
		btnLooak_155.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		btnLooak_155.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validatePage();
			}
		});		
		btnLooak_155.setSelection(true);
		btnLooak_155.setText("Add missing offset-expression for record LOOAK-155 in area DDLDCLOD (and DDLCATLOD)");
		
		btnDdlcatlod = new Button(composite, SWT.CHECK);
		btnDdlcatlod.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		btnDdlcatlod.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validatePage();
			}
		});
		btnDdlcatlod.setSelection(true);
		btnDdlcatlod.setText("Add DDLCATLOD records and sets");		
		
		return composite;
		
	}
	
	private void validatePage() {
		
		getController().setPageComplete(false);
		getController().setErrorMessage(null);
		
		Short digitCount = new Short((short) -1);
		if (btnUseRecordidAsSuffix.getSelection()) {
			if (btnUse3Digits.getSelection()) {
				digitCount = new Short((short) 3);
			} else {
				digitCount = new Short((short) 4);
			}			
		}
		getContext().setAttribute(ContextAttributeKeys.DIGIT_COUNT_FOR_MISSING_SUFFIXES, 
				  				  digitCount);
		
		getContext().setAttribute(ContextAttributeKeys.ADD_OFFSET_FOR_OOAK_012, 
							 	  btnOoak_012.getSelection());
		getContext().setAttribute(ContextAttributeKeys.ADD_OFFSET_FOR_LOOAK_155, 
							 	  btnLooak_155.getSelection());
		getContext().setAttribute(ContextAttributeKeys.ADD_DDLCATLOD, 
			 	  				  btnDdlcatlod.getSelection());
		
		getController().setPageComplete(true);
		
	}

}