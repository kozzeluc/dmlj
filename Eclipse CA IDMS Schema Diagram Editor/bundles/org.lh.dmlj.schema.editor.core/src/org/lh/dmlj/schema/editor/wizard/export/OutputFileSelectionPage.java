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
package org.lh.dmlj.schema.editor.wizard.export;

import java.io.File;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;

public class OutputFileSelectionPage extends WizardPage {
	
	private File file;
	private Text textFile;

	public OutputFileSelectionPage() {
		super("outputFileSelectionPage");
		setTitle("CA IDMS/DB Schema Syntax");
		setDescription("Select the output file");
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		setControl(container);
		Layout layout = new GridLayout(3, false);
		container.setLayout(layout);
		
		Label lblFile = new Label(container, SWT.NONE);
		lblFile.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFile.setText("File :");
		
		textFile = new Text(container, SWT.BORDER);
		textFile.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == 13) {
					validatePage();
				}
			}
		});
		textFile.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				validatePage();
			}
		});
		textFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnBrowse = new Button(container, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(container.getShell(), SWT.SAVE);
				fileDialog.setFileName(textFile.getText());
				String newValue = fileDialog.open();							
				if (newValue != null) {
					textFile.setText(newValue);
					textFile.redraw();
			        validatePage();			        			
				} else {
					textFile.setText("");
					textFile.redraw();
				}
			}
		});
		btnBrowse.setText("Browse...");		
		
		validatePage();
				
	}

	public File getFile() {
		return file;
	}
	
	private void validatePage() {		
		
		boolean pageComplete = true;
		setErrorMessage(null);
		
		file = null;
		
		if (!textFile.getText().trim().equals("")) {
			File folder = new File(textFile.getText().trim()).getParentFile(); 
			if (!folder.exists()) {
				pageComplete = false;
				setErrorMessage("Folder does not exist: " + 
								folder.getAbsolutePath());			
			} else {
				file = new File(textFile.getText().trim());
			}
		} else {
			pageComplete = false;
		}
				
		setPageComplete(pageComplete);		
		
	}
	
}
