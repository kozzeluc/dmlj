package org.lh.dmlj.schema.editor.importtool.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;
import org.lh.dmlj.schema.editor.importtool.AbstractDataEntryPage;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;

public class FileSelectionPage extends AbstractDataEntryPage {
	
	private Text textFile;

	public FileSelectionPage() {
		super();
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public Control createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
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
				FileDialog fileDialog = new FileDialog(container.getShell());
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
		
		return container;
	}

	private void validatePage() {
		
		getContext().clearAttribute(IDataEntryContext.SCHEMA_NAME);
		getContext().clearAttribute(IDataEntryContext.SCHEMA_VERSION);
		getContext().clearAttribute(ContextAttributeKeys.SCHEMA_SYNTAX_FILE);
		
		getController().setPageComplete(false);
		getController().setErrorMessage(null);
		
		if (textFile.getText().trim().equals("")) {
			return;
		} else if (!new File(textFile.getText().trim()).exists()) {
			getController().setErrorMessage("File not found");
			return;
		}
		
		String schemaName = null;
		Short schemaVersion = null;
		File file = new File(textFile.getText().trim());
		try {
			BufferedReader in = 
				new BufferedReader(new FileReader(file));
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				int i = line.indexOf("SCHEMA NAME IS ");
				int j = line.indexOf(" VERSION IS "); 
				if (i > -1 && j > -1 && j > i) {
					schemaName = line.substring(i + 15, j);											
					schemaVersion = 
						Short.valueOf(line.substring(j + 12). trim());
					break;
				}
			}						
			in.close();
		} catch (IOException e) {
			getController().setErrorMessage("File does not appear to contain " +
										    "CA IDMS/DB schema syntax");
			return;
		}
		
		if (schemaName == null || schemaVersion == null) {
			getController().setErrorMessage("File does not appear to contain " +
				    						"CA IDMS/DB schema syntax");
			return;
		}
		
		getContext().setAttribute(IDataEntryContext.SCHEMA_NAME, schemaName);
		getContext().setAttribute(IDataEntryContext.SCHEMA_VERSION, schemaVersion);
		getContext().setAttribute(ContextAttributeKeys.SCHEMA_SYNTAX_FILE, file);
		getController().setPageComplete(true);		
		
	}
	
}
