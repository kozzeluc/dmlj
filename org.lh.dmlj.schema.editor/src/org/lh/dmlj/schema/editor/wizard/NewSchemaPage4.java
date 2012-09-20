package org.lh.dmlj.schema.editor.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class NewSchemaPage4 extends WizardPage {

	private Button checkButton1;
	private Button checkButton2;
	private Button checkButton3;	
	
	protected NewSchemaPage4() {
		super("page4", "CA IDMS Schema", null);		
		setMessage("Set options for IDMSNTWK version 1");		
	}

	@Override
	public void createControl(Composite parent) {
		Composite top = new Composite(parent, SWT.NONE);
		
		setControl(top);
		top.setLayout(new GridLayout(1, false));
		
		checkButton1 = new Button(top, SWT.CHECK);
		checkButton1.setSelection(true);
		checkButton1.setText("Add missing offset-expression for record OOAK-012 in area DDLDML");
		
		checkButton2 = new Button(top, SWT.CHECK);
		checkButton2.setSelection(true);
		checkButton2.setText("Add missing offset-expression for record LOOAK-155 in area DDLDCLOD (and DDLCATLOD)");
		
		checkButton3 = new Button(top, SWT.CHECK);		
		checkButton3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setPageComplete(true);
			}
		});
		checkButton3.setSelection(true);
		checkButton3.setText("Add missing catalog records and sets (areas DDLCAT, DDLCATLOD and DDLCATX)");
				
	}	
	
	public boolean[] getOptions() {
		return new boolean[] {checkButton1.getSelection(),
							  checkButton2.getSelection(),
							  checkButton3.getSelection()};
	}

}