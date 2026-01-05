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

import java.util.function.Function;
import java.util.stream.Stream;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.lh.dmlj.schema.editor.dictionary.tools.Plugin;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.model.Dictionary;
import org.lh.dmlj.schema.editor.dictionary.tools.preference.PreferenceConstants;

public class EditDictionaryDialog extends TitleAreaDialog {
	private static final InputValidationResult VALIDATION_OK = new InputValidationResult(true, null, null);
	
	private final Dictionary dictionary;
	private String dictionaryId;
	private String dictionaryHostname;
	private int dictionaryPort;
	private String dictionaryDictname;
	private String dictionaryUser;
	private String dictionaryPassword;
	private String dictionarySchema;
	private int dictionaryQueryRowidListSizeMaximum;
	private boolean dictionarySysdirl;
	
	private boolean idTouched;
	private boolean hostnameTouched;
	private boolean portTouched;
	private boolean dictnameTouched;
	private boolean userTouched;
	private boolean passwordTouched;
	private boolean schemaTouched;
	private boolean queryRowidListSizeMaximumTouched;
	
	private Text textId;
	private Text textHostname;
	private Text textPort;
	private Text textDictname;
	private Text textUser;
	private Text textPassword;
	private Button btnDefaultSchema;
	private Button btnCustomSchema;
	private Text textCustomSchema;
	private Button btnDefaultQueryRowidListSizeMaximum;
	private Button btnCustomQueryRowidListSizeMaximum;
	private Text textCustomQueryRowidListSizeMaximum;
	private Button btnSysdirl;
	private Button btnTestConnection;
	
	public EditDictionaryDialog(Shell parentShell, Dictionary dictionary) {
		super(parentShell);
		this.dictionary = dictionary;
		setHelpAvailable(false);
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if (dictionary == null) {
			shell.setText("Add Dictionary");
		} else {
			shell.setText("Edit Dictionary");
		}
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
		
		var lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setText("Id:");
		
		textId = new Text(container, SWT.BORDER);
		textId.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				queryRowidListSizeMaximumTouched = true;
				validate();
			}
		});
		textId.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				idTouched = true;
				validate();
			}
		});
		textId.addTraverseListener(e -> {
			idTouched = true;
			validateAndMoveToNextFieldWhenApplicable(e);
		});
		textId.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		
		var lblNewLabel1 = new Label(container, SWT.NONE);
		lblNewLabel1.setText("Hostname:");
		
		textHostname = new Text(container, SWT.BORDER);
		textHostname.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				queryRowidListSizeMaximumTouched = true;
				validate();
			}
		});
		textHostname.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				hostnameTouched = true;
				validate();
			}
		});
		textHostname.addTraverseListener(e -> {
			hostnameTouched = true;
			validateAndMoveToNextFieldWhenApplicable(e);
		});
		textHostname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		
		var lblNewLabel2 = new Label(container, SWT.NONE);
		lblNewLabel2.setText("Port:");
		
		textPort = new Text(container, SWT.BORDER);
		textPort.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				queryRowidListSizeMaximumTouched = true;
				validate();
			}
		});
		textPort.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				portTouched = true;
				validate();
			}
		});
		textPort.addTraverseListener(e -> {
			portTouched = true;
			validateAndMoveToNextFieldWhenApplicable(e);
		});
		var gdTextPort = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gdTextPort.widthHint = 50;
		textPort.setLayoutData(gdTextPort);
		
		var lblNewLabel3 = new Label(container, SWT.NONE);
		lblNewLabel3.setText("Dictname:");
		
		textDictname = new Text(container, SWT.BORDER);
		textDictname.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				queryRowidListSizeMaximumTouched = true;
				validate();
			}
		});
		textDictname.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				dictnameTouched = true;
				validate();
			}
		});
		textDictname.addTraverseListener(e -> {
			dictnameTouched = true;
			validateAndMoveToNextFieldWhenApplicable(e);
		});
		var gdTextDictname = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gdTextDictname.widthHint = 100;
		textDictname.setLayoutData(gdTextDictname);
		
		var lblNewLabel4 = new Label(container, SWT.NONE);
		lblNewLabel4.setText("User:");
		
		textUser = new Text(container, SWT.BORDER);
		textUser.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				queryRowidListSizeMaximumTouched = true;
				validate();
			}
		});
		textUser.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				userTouched = true;
				validate();
			}
		});
		textUser.addTraverseListener(e -> {
			userTouched = true;
			validateAndMoveToNextFieldWhenApplicable(e);
		});
		var gdTextUser = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gdTextUser.widthHint = 100;
		textUser.setLayoutData(gdTextUser);
		
		var lblNewLabel5 = new Label(container, SWT.NONE);
		lblNewLabel5.setText("Password:");
		
		textPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				queryRowidListSizeMaximumTouched = true;
				validate();
			}
		});
		textPassword.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				passwordTouched = true;
				validate();
			}
		});
		textPassword.addTraverseListener(e -> {
			passwordTouched = true;
			validateAndMoveToNextFieldWhenApplicable(e);
		});
		var gdTextPassword = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdTextPassword.widthHint = 100;
		textPassword.setLayoutData(gdTextPassword);
		
		var lblpromptWhenEmpty = new Label(container, SWT.NONE);
		var gdLblpromptWhenEmpty = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdLblpromptWhenEmpty.horizontalIndent = 10;
		lblpromptWhenEmpty.setLayoutData(gdLblpromptWhenEmpty);
		lblpromptWhenEmpty.setText("(prompt when empty)");
		
		var grpSchema = new Group(container, SWT.NONE);
		grpSchema.setLayout(new GridLayout(2, false));
		grpSchema.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		grpSchema.setText("Schema");
		
		btnDefaultSchema = new Button(grpSchema, SWT.RADIO);
		btnDefaultSchema.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validate();
				enableAndDisable();
			}
		});
		btnDefaultSchema.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		btnDefaultSchema.setText("Default (xyz)");
		
		btnCustomSchema = new Button(grpSchema, SWT.RADIO);
		btnCustomSchema.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validate();
				enableAndDisable();
			}
		});
		btnCustomSchema.setText("Custom:");
		
		textCustomSchema = new Text(grpSchema, SWT.BORDER);
		textCustomSchema.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				queryRowidListSizeMaximumTouched = true;
				validate();
			}
		});
		var gdTextCustomSchema = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdTextCustomSchema.widthHint = 100;
		textCustomSchema.setLayoutData(gdTextCustomSchema);
		textCustomSchema.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				schemaTouched = true;
				validate();
			}
		});
		textCustomSchema.addTraverseListener(e -> {
			schemaTouched = true;
			validateAndMoveToNextFieldWhenApplicable(e);
		});
		
		var grpQueryRowidListSizeMaximum = new Group(container, SWT.NONE);
		grpQueryRowidListSizeMaximum.setLayout(new GridLayout(2, false));
		grpQueryRowidListSizeMaximum.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		grpQueryRowidListSizeMaximum.setText("Maximum rowid list size in queries");
		
		btnDefaultQueryRowidListSizeMaximum = new Button(grpQueryRowidListSizeMaximum, SWT.RADIO);
		btnDefaultQueryRowidListSizeMaximum.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validate();
				enableAndDisable();
			}
		});
		btnDefaultQueryRowidListSizeMaximum.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
		btnDefaultQueryRowidListSizeMaximum.setText("Default (47)");
		
		btnCustomQueryRowidListSizeMaximum = new Button(grpQueryRowidListSizeMaximum, SWT.RADIO);
		btnCustomQueryRowidListSizeMaximum.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validate();
				enableAndDisable();
			}
		});
		btnCustomQueryRowidListSizeMaximum.setText("Custom:");
		
		textCustomQueryRowidListSizeMaximum = new Text(grpQueryRowidListSizeMaximum, SWT.BORDER | SWT.RIGHT);
		textCustomQueryRowidListSizeMaximum.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				queryRowidListSizeMaximumTouched = true;
				validate();
			}
		});
		textCustomQueryRowidListSizeMaximum.addTraverseListener(e -> {
			queryRowidListSizeMaximumTouched = true;
			validateAndMoveToNextFieldWhenApplicable(e);
		});
		textCustomQueryRowidListSizeMaximum.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				queryRowidListSizeMaximumTouched = true;
				validate();
			}
		});
		var gdTextCustomQueryRowidListSizeMaximum = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdTextCustomQueryRowidListSizeMaximum.widthHint = 25;
		textCustomQueryRowidListSizeMaximum.setLayoutData(gdTextCustomQueryRowidListSizeMaximum);
		
		btnSysdirl = new Button(container, SWT.CHECK);
		btnSysdirl.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				validate();
			}
		});
		btnSysdirl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validate();
			}
		});
		var gdBtnSysdirl = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gdBtnSysdirl.verticalIndent = 10;
		btnSysdirl.setLayoutData(gdBtnSysdirl);
		btnSysdirl.setText("This is a SYSDIRL dictionary");
		
		btnTestConnection = new Button(container, SWT.NONE);
		btnTestConnection.setEnabled(false);
		btnTestConnection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				testConnection();
			}
		});
		var gdBtnTestConnection = new GridData(SWT.CENTER, SWT.CENTER, true, false, 3, 1);
		gdBtnTestConnection.verticalIndent = 20;
		btnTestConnection.setLayoutData(gdBtnTestConnection);
		btnTestConnection.setText("Test Connection");
		
		Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		var gdLabel = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
		gdLabel.verticalIndent = 10;
		label.setLayoutData(gdLabel);
		
		initializeValues();

		return area;
	}
	
	private void enableAndDisable() {
		textCustomSchema.setEnabled(btnCustomSchema.getSelection());
		textCustomQueryRowidListSizeMaximum.setEnabled(btnCustomQueryRowidListSizeMaximum.getSelection());
	}

	public String getDictionaryDictname() {
		return dictionaryDictname;
	}

	public String getDictionaryId() {
		return dictionaryId;
	}

	public String getDictionaryHostname() {
		return dictionaryHostname;
	}

	public String getDictionaryPassword() {
		return dictionaryPassword;
	}

	public int getDictionaryPort() {
		return dictionaryPort;
	}

	public int getDictionaryQueryRowidListSizeMaximum() {
		return dictionaryQueryRowidListSizeMaximum;
	}

	public String getDictionarySchema() {
		return dictionarySchema;
	}

	public String getDictionaryUser() {
		return dictionaryUser;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(400, 550);
	}
	
	private void initializeValues() {
		setTitle("Dictionary properties");
		setMessage("The password you enter is encrypted before it is stored.");
		
		var defaultSchema = Plugin.getDefault().getPreferenceStore().getString(PreferenceConstants.DEFAULT_SCHEMA);
		btnDefaultSchema.setText("Default (" + defaultSchema + ")");
		
		var defaultQueryRowidListSizeMaximum = Plugin.getDefault().getPreferenceStore().getInt(PreferenceConstants.DEFAULT_QUERY_ROWID_LIST_SIZE_MAXIMUM);
		btnDefaultQueryRowidListSizeMaximum.setText("Default (" + defaultQueryRowidListSizeMaximum + ")");
		
		if (dictionary != null) {			
			textId.setText(dictionary.getId());
			textHostname.setText(dictionary.getHostname());
			textPort.setText(String.valueOf(dictionary.getPort()));
			textDictname.setText(dictionary.getDictname());
			textUser.setText(dictionary.getUser());
			if (dictionary.getPassword() != null) {
				textPassword.setText(dictionary.getPassword());
			}
			
			var schema = dictionary.getSchema();
			var selectDefault = schema.equals(Dictionary.USE_DEFAULT_SCHEMA_INDICATOR); 
			btnDefaultSchema.setSelection(selectDefault);
			btnCustomSchema.setSelection(!selectDefault);
			textCustomSchema.setEnabled(!selectDefault);
			if (!selectDefault) {
				textCustomSchema.setText(schema);
			}
			
			var queryRowidListSizeMaximum = dictionary.getQueryRowidListSizeMaximum();
			selectDefault = queryRowidListSizeMaximum == Dictionary.USE_DEFAULT_QUERY_ROWID_LIST_SIZE_MAXIMUM_INDICATOR;
			btnDefaultQueryRowidListSizeMaximum.setSelection(selectDefault);
			btnCustomQueryRowidListSizeMaximum.setSelection(!selectDefault);
			textCustomQueryRowidListSizeMaximum.setEnabled(!selectDefault);
			if (!selectDefault) {
				textCustomQueryRowidListSizeMaximum.setText(String.valueOf(queryRowidListSizeMaximum));
			}
			
			btnSysdirl.setSelection(dictionary.isSysdirl());
		} else {
			textPort.setText("3709");
			btnDefaultSchema.setSelection(true);
			textCustomSchema.setEnabled(false);
			btnDefaultQueryRowidListSizeMaximum.setSelection(true);
			textCustomQueryRowidListSizeMaximum.setEnabled(false);
		}
		
		enableAndDisable();
		setFocusAndSelectText(textId, null);
		
		btnTestConnection.setEnabled(validateInput().validationOK && Plugin.getDefault().isDriverInstalled());
	}

	public boolean isDictionarySysdirl() {
		return dictionarySysdirl;
	}

	private void setFocusAndSelectText(Control control, InputValidationResult inputValidationResult) {
		if (control == btnTestConnection && inputValidationResult != null && !inputValidationResult.validationOK) {
			// if focus should go to the test connection button but a text field is in error, move the focus to
			// the text field in error (and select its text, if any) and not the test connection button
			inputValidationResult.fieldInError.setFocus();
			inputValidationResult.fieldInError.selectAll();
		} else {	
			control.setFocus();
			if (control instanceof Text text) {
				text.selectAll();
			}
		}
	}

	protected void testConnection() {
		var tmpDictionary = Dictionary.newTemporaryInstance();
		tmpDictionary.setId(dictionaryId);
		tmpDictionary.setHostname(dictionaryHostname);
		tmpDictionary.setPort(dictionaryPort);
		tmpDictionary.setDictname(dictionaryDictname);
		tmpDictionary.setUser(dictionaryUser);
		tmpDictionary.setPassword(dictionaryPassword);
		tmpDictionary.setSchema(dictionarySchema);
		tmpDictionary.setSysdirl(dictionarySysdirl);
		JdbcTools.testConnectionWithOperationInProgressIndicator(tmpDictionary);
	}

	private void validateAndMoveToNextFieldWhenApplicable(TraverseEvent e) {
		if (e.detail != SWT.TRAVERSE_RETURN && e.detail != SWT.TRAVERSE_TAB_NEXT && e.detail != SWT.TRAVERSE_TAB_PREVIOUS) {
			return;
		}
		var inputValidationResult = validate();		
		if (e.getSource() == textId) {
			setFocusAndSelectText(textHostname, inputValidationResult);
		} else if (e.getSource() == textHostname) {
			setFocusAndSelectText(textPort, inputValidationResult);
		} else if (e.getSource() == textPort) {
			setFocusAndSelectText(textDictname, inputValidationResult);
		} else if (e.getSource() == textDictname) {
			setFocusAndSelectText(textUser, inputValidationResult);
		} else if (e.getSource() == textUser) {
			setFocusAndSelectText(textPassword, inputValidationResult);
		} else if (e.getSource() == textPassword) {
			setFocusAndSelectTextForPassword(inputValidationResult);
		} else if (e.getSource() == textCustomSchema) {
			setFocusAndSelectTextForCustomSchema(inputValidationResult);
		} else if (e.getSource() == textCustomQueryRowidListSizeMaximum) {
			setFocusAndSelectText(btnTestConnection, inputValidationResult);
		}
		if (e.detail == SWT.TRAVERSE_RETURN) {
			// make sure to set the event's doit indicator to false in order for our actions to be honored when
			// the return key is pressed
			e.doit = false;
		}
	}
	
	private void setFocusAndSelectTextForPassword(InputValidationResult inputValidationResult) {
		if (btnCustomSchema.getSelection()) {
			setFocusAndSelectText(textCustomSchema, inputValidationResult);
		} else if (btnCustomQueryRowidListSizeMaximum.getSelection()) {
			setFocusAndSelectText(textCustomQueryRowidListSizeMaximum, inputValidationResult);
		} else {
			setFocusAndSelectText(btnTestConnection, inputValidationResult);
		}
	}
	
	private void setFocusAndSelectTextForCustomSchema(InputValidationResult inputValidationResult) {
		if (btnCustomQueryRowidListSizeMaximum.getSelection()) {
			setFocusAndSelectText(textCustomQueryRowidListSizeMaximum, inputValidationResult);
		} else {
			setFocusAndSelectText(btnTestConnection, inputValidationResult);
		}
	}

	private InputValidationResult validate() {
		var inputValidationResult = validateInput();
		setErrorMessage(inputValidationResult.errorMessage);
		
		if (inputValidationResult.validationOK) {
			dictionaryId = textId.getText().trim();
			dictionaryHostname = textHostname.getText().trim();
			dictionaryPort = Integer.parseInt(textPort.getText().trim());
			dictionaryDictname = textDictname.getText().trim();
			dictionaryUser = textUser.getText().trim();
			if (!textPassword.getText().isBlank()) {
				dictionaryPassword = textPassword.getText().trim();
			} else {
				dictionaryPassword = null;
			}
			if (btnDefaultSchema.getSelection()) {
				dictionarySchema = Dictionary.USE_DEFAULT_SCHEMA_INDICATOR;
			} else {
				dictionarySchema = textCustomSchema.getText().trim();							
			}
			if (btnDefaultQueryRowidListSizeMaximum.getSelection()) {
				dictionaryQueryRowidListSizeMaximum = 
					Dictionary.USE_DEFAULT_QUERY_ROWID_LIST_SIZE_MAXIMUM_INDICATOR;
			} else {
				dictionaryQueryRowidListSizeMaximum = 
					Integer.valueOf(textCustomQueryRowidListSizeMaximum.getText().trim());						
			}
			dictionarySysdirl = btnSysdirl.getSelection();
		}
		
		btnTestConnection.setEnabled(inputValidationResult.validationOK && Plugin.getDefault().isDriverInstalled());
		getButton(IDialogConstants.OK_ID).setEnabled(inputValidationResult.validationOK);
		
		return inputValidationResult;
	}
	
	private InputValidationResult validateInput() {
		var inputValidationResult = Stream.of(validateId(), validateHostname(), validatePort(), validateDictname(),
											 validateUser(), validatePassword(), validateCustomSchema(), validateCustomQueryRowidListSizeMaximum())
				.flatMap(Function.identity())
				.findFirst();
		if (inputValidationResult.isPresent()) {
			return inputValidationResult.orElseThrow();
		} else {
			return VALIDATION_OK;
		}
	}
	
	private Stream<InputValidationResult> validateId() {
		if (textId.getText().isBlank()) {
			var message = idTouched ? "Id is mandatory" : null;
			return Stream.of(new InputValidationResult(false, message, textId));
		} else {
			return Stream.empty();
		}
	}
	
	private Stream<InputValidationResult> validateHostname() {
		if (textHostname.getText().isBlank()) {
			var message = hostnameTouched ? "Hostname is mandatory" : null;
			return Stream.of(new InputValidationResult(false, message, textHostname));
		} else {
			return Stream.empty();
		}
	}
	
	private Stream<InputValidationResult> validatePort() {
		if (textPort.getText().isBlank()) {
			var message = portTouched ? "Port is mandatory" : null;
			return Stream.of(new InputValidationResult(false, message, textPort));
		} else {
			try {
				Integer.parseInt(textPort.getText());
				return Stream.empty();
			} catch (NumberFormatException e) {
				var message = portTouched ? "Port is invalid" : null;
				return Stream.of(new InputValidationResult(false, message, textPort));
			}
		}
	}
	
	private Stream<InputValidationResult> validateDictname() {
		if (textDictname.getText().isBlank()) {
			var message = dictnameTouched ? "Dictname is mandatory" : null;
			return Stream.of(new InputValidationResult(false, message, textDictname));
		} else {
			return Stream.empty();
		}
	}
	
	private Stream<InputValidationResult> validateUser() {
		if (textUser.getText().isBlank()) {
			var message = userTouched ? "User is mandatory" : null;
			return Stream.of(new InputValidationResult(false, message, textUser));
		} else {
			return Stream.empty();
		}
	}
	
	private Stream<InputValidationResult> validatePassword() {
		if (textPassword.getText().trim().length() > 99) {
			var message = passwordTouched ? "Password is invalid" : null;
			return Stream.of(new InputValidationResult(false, message, textPassword));
		} else {
			return Stream.empty();
		}
	}
	
	private Stream<InputValidationResult> validateCustomSchema() {
		if (btnCustomSchema.getSelection() && textCustomSchema.getText().isBlank()) {
			var message = schemaTouched ? "Custom schema is mandatory" : null;
			return Stream.of(new InputValidationResult(false, message, textCustomSchema));
		} else {
			return Stream.empty();
		}
	}
	
	private Stream<InputValidationResult> validateCustomQueryRowidListSizeMaximum() {
		if (btnCustomQueryRowidListSizeMaximum.getSelection() && textCustomQueryRowidListSizeMaximum.getText().isBlank()) {
			var message = queryRowidListSizeMaximumTouched ? "Custom maximum rowid list size in queries is mandatory" : null;
			return  Stream.of(new InputValidationResult(false, message, textCustomQueryRowidListSizeMaximum));
		} else if (btnCustomQueryRowidListSizeMaximum.getSelection()) {
			var message = "Custom maximum rowid list size in queries must be a positive number in the range 1 to 1000";
			try {
				var i = Integer.parseInt(textCustomQueryRowidListSizeMaximum.getText().trim());
				if (i < 1 || i > 1000) {
					return  Stream.of(new InputValidationResult(false, message, textCustomQueryRowidListSizeMaximum));
				}	
			} catch (NumberFormatException e) {
				return  Stream.of(new InputValidationResult(false, message, textCustomQueryRowidListSizeMaximum));
			}
		}
		return Stream.empty();
	}

	public static class InputValidationResult {
		private String errorMessage;
		private boolean validationOK;
		private Text fieldInError;
		
		public InputValidationResult(boolean validationOK, String errorMessage, Text fieldInError) {
			this.validationOK = validationOK;
			this.errorMessage = errorMessage;
			this.fieldInError = fieldInError;
		}
		
	}
}
