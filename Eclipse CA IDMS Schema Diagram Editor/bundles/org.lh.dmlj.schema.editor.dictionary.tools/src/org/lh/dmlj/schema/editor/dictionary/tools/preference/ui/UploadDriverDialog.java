/**
 * Copyright (C) 2026  Luc Hermans
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
package org.lh.dmlj.schema.editor.dictionary.tools.preference.ui;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.lh.dmlj.schema.editor.dictionary.tools.jar.JarHelper;

public class UploadDriverDialog extends TitleAreaDialog {
	private static final String IMPLEMENTATION_TITLE = "Implementation-Title";
	private static final String TM_CHAR = "\u2122";
	
	private File selectedJarFile;
	private Text textJarFilePath;

	public UploadDriverDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Install the IDMS JDBC Driver");
		setHelpAvailable(false);
	}	
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		var area = (Composite) super.createDialogArea(parent);
		var container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		var lblPath = new Label(container, SWT.NONE);
		lblPath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPath.setText("Path:");
		
		textJarFilePath = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		textJarFilePath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		var btnSelect = new Button(container, SWT.NONE);
		btnSelect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				var fileDialog = new FileDialog(getShell());
				fileDialog.setFilterExtensions(new String[] { "*.jar" });
				var newValue = fileDialog.open();							
				if (newValue != null) {
					textJarFilePath.setText(newValue);
					textJarFilePath.setToolTipText(newValue);
					textJarFilePath.redraw();
			       validatePage();			        			
				} else {
					textJarFilePath.setText("");
					textJarFilePath.setToolTipText("");
					textJarFilePath.redraw();
				}
			}
		});
		btnSelect.setText("Select...");
		
		var labelComment = new Label(container, SWT.WRAP);
		var gdLabelComment = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
		gdLabelComment.verticalIndent = 10;
		gdLabelComment.widthHint = 100;
		labelComment.setLayoutData(gdLabelComment);
		labelComment.setText(
				"""
				Note: the file you're looking for is probably called 'idmsjdbc.jar' and is provided with CA IDMS%s Server.

				The file you select will be copied to your Eclipse installation's 'dropins' folder and the \
				necessary OSGi headers will be added to its 'META-INF/MANIFEST.MF' file so that it effectively \
				becomes an OSGi bundle.

				You will need to restart your workbench in order for the IDMS JDBC driver to be available to the \
				Eclipse CA IDMS/DB Schema Diagram Editor (and other plug-ins).
				""".formatted(TM_CHAR));
		initializeValues();
		btnSelect.setFocus();
		return area;
	}

	private void initializeValues() {
		setTitle(".jar File Selection");
		setMessage("Select the .jar file that contains the IDMS JDBC Driver.");
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 400);
	}
	
	public File getSelectedJarFile () {
		return selectedJarFile;
	}

	private void validatePage() {
		var okButton = getButton(IDialogConstants.OK_ID);
		okButton.setEnabled(false);
		
		if (textJarFilePath.getText().isBlank() || !textJarFilePath.getText().trim().toLowerCase().endsWith(".jar")) {
			return;
		}
		
		Properties manifestHeaders;
		try {
			var jarFile = new File(textJarFilePath.getText().trim());
			manifestHeaders = JarHelper.getManifestHeaders(jarFile);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		if (!manifestHeaders.containsKey(IMPLEMENTATION_TITLE) || !manifestHeaders.getProperty(IMPLEMENTATION_TITLE).contains("IDMS") ||
			!manifestHeaders.getProperty(IMPLEMENTATION_TITLE).contains("JDBC") || !manifestHeaders.containsKey("Implementation-Version") ||
			!manifestHeaders.containsKey("Implementation-Vendor")) {
			
			return;
		}
		
		try {
			selectedJarFile = new File(textJarFilePath.getText().trim());
		} catch (Exception e) {
			// this shouldn't happen since we have previously inspected the .jar file
			throw new IllegalStateException(e);
		}
		okButton.setEnabled(true);
	}
	
}
