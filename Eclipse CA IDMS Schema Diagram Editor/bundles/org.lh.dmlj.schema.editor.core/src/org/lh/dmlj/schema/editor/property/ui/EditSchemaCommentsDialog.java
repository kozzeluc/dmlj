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
package org.lh.dmlj.schema.editor.property.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.lh.dmlj.schema.Schema;

public class EditSchemaCommentsDialog extends Dialog {
	
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static final int MAX_LINE_LENGTH = 80;

	private Text textComments;
	private Text textMessage;
	
	private Schema schema;
	private String comments;	
	
	private static boolean suppressKey(int keyCode) {
		return keyCode == SWT.TAB;
	}

	public EditSchemaCommentsDialog(Shell parentShell, Schema schema) {
		super(parentShell);
		this.schema = schema;
		setShellStyle(getShellStyle() | SWT.RESIZE); 
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		String title = 
			"Edit comments for schema " + schema.getName() + " version " + schema.getVersion();
		shell.setText(title);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		textComments = new Text(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textComments.setFont(SWTResourceManager.getFont("Courier New", 10, SWT.NORMAL));
		textComments.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (suppressKey(e.keyCode)) {					
					e.doit = false;
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				validate();
			}
		});
		textComments.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				validate();
			}
		});
		textComments.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		textMessage = new Text(container, SWT.READ_ONLY);
		textMessage.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				textComments.setFocus();
			}
		});
		textMessage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		textMessage.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));

		initialize();
		
		return container;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	public String getNewValue() {
		return comments;		
	}

	@Override
	protected Point getInitialSize() {
		return new Point(715, 300);
	}

	private void initialize() {
		StringBuilder p = new StringBuilder();
		if (schema.getComments() != null) {
			for (String line : schema.getComments()) {
				if (p.length() > 0) {
					p.append(LINE_SEPARATOR);
				}
				p.append(line);
			}
		}
		comments = p.toString();
		textComments.setText(comments);
	}

	private void validate() {
		textMessage.setText("");
		comments = textComments.getText();
		boolean valid = true;
		String[] lines = comments.split(LINE_SEPARATOR);
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].length() > MAX_LINE_LENGTH) {
				textMessage.setText("Line " + (i + 1) + " exceeds " + MAX_LINE_LENGTH + " characters");
				valid = false;
				break;
			}
		}
		getButton(IDialogConstants.OK_ID).setEnabled(valid);
	}

}
