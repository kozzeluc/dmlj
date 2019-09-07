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
	
	private static final String TM_CHAR = "\u2122";
	
	private Text textJarFilePath;
	private File selectedJarFile;

	public UploadDriverDialog(Shell parentShell) {
		super(parentShell);
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
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblPath = new Label(container, SWT.NONE);
		lblPath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPath.setText("Path:");
		
		textJarFilePath = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		textJarFilePath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnSelect = new Button(container, SWT.NONE);
		btnSelect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(getShell());
				fileDialog.setFilterExtensions(new String[] {"*.jar"});
				String newValue = fileDialog.open();							
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
		
		Label labelComment = new Label(container, SWT.WRAP);
		GridData gd_labelComment = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
		gd_labelComment.verticalIndent = 10;
		gd_labelComment.widthHint = 100;
		labelComment.setLayoutData(gd_labelComment);
		labelComment.setText("Note: the file you're looking for is probably called 'idmsjdbc.jar' " +
							 "and is provided with CA IDMS" + TM_CHAR + " Server.\n\nThe file you select will " +
							 "be copied to your Eclipse installation's 'dropins' folder and the " +
							 "necessary OSGi headers will be added to its 'META-INF/MANIFEST.MF' " +
							 "file so that it effectively becomes an OSGi bundle.\n\nYou will " +
							 "need to restart your workbench in order for the IDMS JDBC driver " +
							 "to be available to the Eclipse CA IDMS/DB Schema Diagram Editor " +
							 "(and other plug-ins).");

		initializeValues();
		
		btnSelect.setFocus();
		
		return area;
	}

	private void initializeValues() {
		setTitle(".jar File Selection");
		setMessage("Select the .jar file that contains the IDMS JDBC Driver.");
	}

	protected Point getInitialSize() {
		return new Point(450, 400);
	}
	
	public File getSelectedJarFile () {
		return selectedJarFile;
	}

	private void validatePage() {
		
		Button okButton = getButton(IDialogConstants.OK_ID);
		okButton.setEnabled(false);
		
		if (textJarFilePath.getText().trim().isEmpty()) {
			return;
		}
		
		if (!textJarFilePath.getText().trim().toLowerCase().endsWith(".jar")) {
			return;
		}
		
		Properties manifestHeaders;
		try {
			File jarFile = new File(textJarFilePath.getText().trim());
			manifestHeaders = JarHelper.getManifestHeaders(jarFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (!manifestHeaders.containsKey("Implementation-Title") ||
			manifestHeaders.getProperty("Implementation-Title").indexOf("IDMS") < 0 ||
			manifestHeaders.getProperty("Implementation-Title").indexOf("JDBC") < 0 ||
			!manifestHeaders.containsKey("Implementation-Version") ||
			!manifestHeaders.containsKey("Implementation-Vendor")) {
			
			return;
		}
		
		try {
			selectedJarFile = new File(textJarFilePath.getText().trim());
		} catch (Throwable t) {
			// this shouldn't happen since we have previously inspected the .jar file
			throw new RuntimeException(t);
		}
		
		okButton.setEnabled(true);	
		
	}
}
