/**
 * Copyright (C) 2019  Luc Hermans
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
package org.lh.dmlj.schema.editor.property.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.lh.dmlj.schema.editor.property.exception.DSLFacetValidationException;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;

public class EditProcedureCallsDialog extends Dialog {
	
	private IDslFacetModifier dslFacetModifier;
	private Text textDsl;
	private Button btnValidate;
	private Text textMessage;
	private Text textExample;
	private Label lblExample;
	private Label lblProcedureDefinitionDsl;

	public EditProcedureCallsDialog(Shell parentShell, IDslFacetModifier dslFacetModifier) {
		super(parentShell);
		this.dslFacetModifier = dslFacetModifier;
		setShellStyle(getShellStyle() | SWT.RESIZE); 
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Edit procedure calls for " + dslFacetModifier.getModelType() + " " + dslFacetModifier.getModelName());
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		GridLayout gl_area = new GridLayout(2, false);
		gl_area.marginWidth = 11;
		gl_area.marginHeight = 13;
		area.setLayout(gl_area);
		
		lblProcedureDefinitionDsl = new Label(area, SWT.NONE);
		lblProcedureDefinitionDsl.setText("Procedure Definition DSL:");
		new Label(area, SWT.NONE);
		
		textDsl = new Text(area, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textDsl.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode != SWT.ARROW_LEFT && e.keyCode != SWT.ARROW_RIGHT && 
					e.keyCode != SWT.ARROW_UP && e.keyCode != SWT.ARROW_DOWN) {					
					
					getButton(IDialogConstants.OK_ID).setEnabled(false);
				}
			}
		});
		textDsl.setFont(SWTResourceManager.getFont("Courier New", 10, SWT.NORMAL));
		textDsl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		
		btnValidate = new Button(area, SWT.NONE);
		btnValidate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validate();
			}
		});
		btnValidate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnValidate.setText("Validate");
		
		Button btnReset = new Button(area, SWT.NONE);
		btnReset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initialize();
			}
		});
		btnReset.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnReset.setText("Reset");
		
		textMessage = new Text(area, SWT.NO_FOCUS | SWT.READ_ONLY | SWT.WRAP | SWT.MULTI);
		textMessage.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				textDsl.setFocus();
			}
		});
		textMessage.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		textMessage.setBackground(area.getBackground());
		GridData gd_textMessage = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_textMessage.heightHint = 50;
		textMessage.setLayoutData(gd_textMessage);
		textMessage.setText("<message>");
		new Label(area, SWT.NONE);
		
		lblExample = new Label(area, SWT.NONE);
		GridData gd_lblExample = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblExample.verticalIndent = 5;
		lblExample.setLayoutData(gd_lblExample);
		lblExample.setText("Example:");
		new Label(area, SWT.NONE);
		
		textExample = new Text(area, SWT.NO_FOCUS | SWT.READ_ONLY | SWT.WRAP | SWT.MULTI);
		textExample.setText("call 'IDMSCOMP BEFORE STORE'\ncall 'IDMSCOMP BEFORE MODIFY'\ncall 'IDMSDCOM AFTER GET'");
		textExample.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textExample.setFont(SWTResourceManager.getFont("Courier New", 10, SWT.NORMAL));
		textExample.setBackground(area.getBackground());
		new Label(area, SWT.NONE);
		
		initialize();

		return area;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 350);
	}

	private void initialize() {		
		textDsl.setText(dslFacetModifier.getOriginalFacetDefinition());
		textMessage.setText("");
	}

	protected void validate() {		
		boolean valid = true;
		try {
			dslFacetModifier.setModifiedFacetDefinition(textDsl.getText());
			textMessage.setText("");
			textDsl.setText(dslFacetModifier.getModifiedFacetDefinition());
			textDsl.setSelection(0, 0);
		} catch (DSLFacetValidationException e) {
			StringBuilder message = new StringBuilder();
			Throwable cause = e.getCause();
			if (cause != null) {
				message.append(cause.getClass().getSimpleName());
			} else {
				message.append(e.getClass().getSimpleName());
			}
			message.append(": ");
			message.append(e.getMessage());
			textMessage.setText(message.toString());
			valid = false;			
		}		
		getButton(IDialogConstants.OK_ID).setEnabled(valid);
	}
}
