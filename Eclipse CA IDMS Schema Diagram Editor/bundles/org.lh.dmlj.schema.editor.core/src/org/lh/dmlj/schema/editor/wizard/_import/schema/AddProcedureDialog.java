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
package org.lh.dmlj.schema.editor.wizard._import.schema;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.lh.dmlj.schema.editor.common.NamingConventions;
import org.lh.dmlj.schema.editor.common.ValidationResult;

public class AddProcedureDialog extends TitleAreaDialog {

	private List<String> currentList;
	private String 		 procedureName;
	private Text 		 textProcedureName;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public AddProcedureDialog(Shell parentShell, List<String> currentList) {
		super(parentShell);
		setHelpAvailable(false);
		this.currentList = currentList;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Specify a procedure that compresses a record occurrence's data portion.");
		setTitle("Add database procedure used for COMPRESSION");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Procedure name:");
		
		textProcedureName = new Text(container, SWT.BORDER);
		textProcedureName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				validate();
			}
		});
		textProcedureName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				validate();
			}
		});
		GridData gd_textProcdureName = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_textProcdureName.widthHint = 100;
		textProcedureName.setLayoutData(gd_textProcdureName);

		return area;
	}
	
	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(400, 250);
	}

	public String getProcedureName() {
		return procedureName;
	}
	
	private void validate() {
	
		setErrorMessage(null);
		boolean ok = true;
		
		String p = 
			NamingConventions.Type.PROCEDURE_NAME.toString().toLowerCase().replaceAll("_", " ");

		// convert the name to upper case and check its validity 
		procedureName = textProcedureName.getText().trim().toUpperCase();
		ValidationResult validationResult = 
			NamingConventions.validate(procedureName, NamingConventions.Type.PROCEDURE_NAME);
		if (currentList.contains(procedureName)) {
			String message = "Procedure already specified for compression";
			setErrorMessage(message);
			ok = false;
		} else if (validationResult.getStatus() != ValidationResult.Status.OK) {
			String message = 
				"Invalid " + p + ": " + procedureName + " (" + validationResult.getMessage() + ")";
			setErrorMessage(message);
			ok = false;
		}
				
		getButton(IDialogConstants.OK_ID).setEnabled(ok);
		
	}
	
}
