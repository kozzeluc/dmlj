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
package org.lh.dmlj.schema.editor;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class CloseSchemaDslEditorDialog extends Dialog {

	public static final int YES = 11;
	public static final int NO = 10;
	
	private boolean rememberMyDecision;
	private Button btnRememberMyDecision;

	public CloseSchemaDslEditorDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (IDialogConstants.YES_ID == buttonId) {
			yesPressed();
		} else if (IDialogConstants.NO_ID == buttonId) {
			noPressed();
		} else if (IDialogConstants.CANCEL_ID == buttonId) {
			cancelPressed();
		}
	}
	
	@Override
	protected void configureShell(Shell shell) {		
		super.configureShell(shell);
		shell.setText("Close all .schemadsl files ?");
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.YES_ID, IDialogConstants.YES_LABEL, true);
		createButton(parent, IDialogConstants.NO_ID, IDialogConstants.NO_LABEL, false);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;
		
		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 3));
		lblNewLabel_1.setImage(Display.getCurrent().getSystemImage(SWT.ICON_QUESTION));
		
		Label lblOneOrMore = new Label(container, SWT.WRAP);
		GridData gd_lblOneOrMore = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblOneOrMore.horizontalIndent = 5;
		gd_lblOneOrMore.widthHint = 375;
		lblOneOrMore.setLayoutData(gd_lblOneOrMore);
		lblOneOrMore.setText("One or more .schemadsl resources are opened and will be opened again when the workbench restarts.  Depending on the size of the diagram(s), this may take some time and the workbench will take longer to start.");
		
		Label lblNewLabel = new Label(container, SWT.WRAP);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.horizontalIndent = 5;
		gd_lblNewLabel.widthHint = 375;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText("Do you want to close these editors before exiting the workbench to prevent this from happening ?  (You will be able to save your work after pressing any of the buttons below).");
		
		btnRememberMyDecision = new Button(container, SWT.CHECK);
		btnRememberMyDecision.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				rememberMyDecision = btnRememberMyDecision.getSelection();
			}
		});
		GridData gd_btnRememberMyDecision = new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 1, 1);
		gd_btnRememberMyDecision.horizontalIndent = 5;
		btnRememberMyDecision.setLayoutData(gd_btnRememberMyDecision);
		btnRememberMyDecision.setText("Remember my decision");

		return container;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 250);
	}
	
	public boolean isRememberMyDecision() {
		return rememberMyDecision;
	}
	
	protected void noPressed() {
		setReturnCode(NO);
		close();
	}
	
	protected void yesPressed() {
		setReturnCode(YES);
		close();
	}

}
