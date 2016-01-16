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
package org.lh.dmlj.schema.editor.wizard._import.dictguide;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class ViewLicenseDialog extends Dialog {
	
	private String licenseName;
	private String licenseText;
	private Text   text;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ViewLicenseDialog(Shell parentShell, String licenseName, String licenseText) {
		super(parentShell);
		this.licenseName = licenseName;
		this.licenseText = licenseText;
		setShellStyle(getShellStyle() | SWT.RESIZE); 
	}
	
	@Override
	protected void configureShell(Shell shell) {		
		super.configureShell(shell);
		shell.setText(licenseName);	
	}	

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		text = new Text(container, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		text.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		text.setFont(SWTResourceManager.getFont("Courier New", 8, SWT.NORMAL));
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		text.setText(licenseText);
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(600, 400);
	}
	
}
