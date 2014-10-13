/**
 * Copyright (C) 2014  Luc Hermans
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
package org.lh.dmlj.schema.editor.dictionary.tools.jdbc;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.lh.dmlj.schema.editor.dictionary.tools.Plugin;
import org.lh.dmlj.schema.editor.dictionary.tools.model.Dictionary;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class PromptForPasswordDialog extends Dialog {
	
	private Text textDictionary;
	private Text textConnectionUrl;
	private Text textPassword;	
	private Button btnStorePassword;
	
	private Dictionary dictionary;
	
	private String password;
	private boolean storePassword;
	private Label lblSchema;
	private Text textSchema;
	
	public PromptForPasswordDialog(Shell parentShell, Dictionary dictionary) {
		super(parentShell);
		this.dictionary = dictionary;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Password required");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));
		
		Label lblDictionary = new Label(container, SWT.NONE);
		lblDictionary.setText("Dictionary:");
		
		textDictionary = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		textDictionary.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblConnectionUrl = new Label(container, SWT.NONE);
		lblConnectionUrl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblConnectionUrl.setText("Connection URL:");
		
		textConnectionUrl = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		textConnectionUrl.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblSchema = new Label(container, SWT.NONE);
		lblSchema.setText("Schema:");
		
		textSchema = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		textSchema.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPassword = new Label(container, SWT.NONE);
		lblPassword.setText("Password:");
		
		textPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textPassword.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				validate();
			}
		});
		textPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				btnStorePassword.setEnabled(dictionary.getInternalId() > -1 &&
											textPassword.getText().trim().length() < 100);
				validate();
			}
		});
		GridData gd_text = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text.widthHint = 100;
		textPassword.setLayoutData(gd_text);
		
		btnStorePassword = new Button(container, SWT.FLAT | SWT.CHECK);
		btnStorePassword.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validate();
			}
		});
		GridData gd_btnCheckButton = new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 2, 1);
		gd_btnCheckButton.verticalIndent = 10;
		btnStorePassword.setLayoutData(gd_btnCheckButton);
		btnStorePassword.setText("Encrypt and store password; don't prompt in the future.");
		
		textDictionary.setText(dictionary.getId());
		textConnectionUrl.setText(dictionary.getConnectionUrl());
		textSchema.setText(dictionary.getSchemaWithDefault(Plugin.getDefault()));
		textPassword.setFocus();
		btnStorePassword.setEnabled(dictionary.getInternalId() > -1);

		return container;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 250);
	}
	
	public String getPassword() {
		return password;
	}
	
	public boolean isStorePassword() {
		return storePassword;
	}
	
	private void validate() {
		password = textPassword.getText().trim();
		storePassword = btnStorePassword.getSelection();
		getButton(IDialogConstants.OK_ID).setEnabled(!textPassword.getText().trim().equals("") &&
													 textPassword.getText().trim().length() < 100);
	}

}
