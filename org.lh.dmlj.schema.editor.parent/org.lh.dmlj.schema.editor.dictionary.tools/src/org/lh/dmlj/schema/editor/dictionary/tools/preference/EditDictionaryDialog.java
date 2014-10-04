package org.lh.dmlj.schema.editor.dictionary.tools.preference;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
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
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.model.Dictionary;

public class EditDictionaryDialog extends TitleAreaDialog {
	
	private Text textId;
	private Text textHostname;
	private Text textPort;
	private Text textDictname;
	private Text textUser;
	private Text textPassword;
	private Text textSchema;
	private Button btnSysdirl;
	private Button btnTestConnection;
	
	private Dictionary dictionary;
	
	private String dictionaryId;
	private String dictionaryHostname;
	private int dictionaryPort;
	private String dictionaryDictname;
	private String dictionaryUser;
	private String dictionaryPassword;
	private String dictionarySchema;
	private boolean dictionarySysdirl;
	
	private boolean idTouched;
	private boolean hostnameTouched;
	private boolean portTouched;
	private boolean dictnameTouched;
	private boolean userTouched;
	private boolean passwordTouched;
	private boolean schemaTouched;
	
	public EditDictionaryDialog(Shell parentShell, Dictionary dictionary) {
		super(parentShell);
		this.dictionary = dictionary;
		setHelpAvailable(false);
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
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setText("Id:");
		
		textId = new Text(container, SWT.BORDER);
		textId.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				idTouched = true;
				validate();
			}
		});
		textId.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				idTouched = true;
				validateAndMoveToNextFieldWhenApplicable(e);
			}
		});
		textId.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		
		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setText("Hostname:");
		
		textHostname = new Text(container, SWT.BORDER);
		textHostname.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				hostnameTouched = true;
				validate();
			}
		});
		textHostname.addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(TraverseEvent e) {
				hostnameTouched = true;
				validateAndMoveToNextFieldWhenApplicable(e);
			}
		});
		textHostname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		
		Label lblNewLabel_2 = new Label(container, SWT.NONE);
		lblNewLabel_2.setText("Port:");
		
		textPort = new Text(container, SWT.BORDER);
		textPort.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				portTouched = true;
				validate();
			}
		});
		textPort.addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(TraverseEvent e) {
				portTouched = true;
				validateAndMoveToNextFieldWhenApplicable(e);
			}
		});
		GridData gd_textPort = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_textPort.widthHint = 50;
		textPort.setLayoutData(gd_textPort);
		
		Label lblNewLabel_3 = new Label(container, SWT.NONE);
		lblNewLabel_3.setText("Dictname:");
		
		textDictname = new Text(container, SWT.BORDER);
		textDictname.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				dictnameTouched = true;
				validate();
			}
		});
		textDictname.addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(TraverseEvent e) {
				dictnameTouched = true;
				validateAndMoveToNextFieldWhenApplicable(e);
			}
		});
		GridData gd_textDictname = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_textDictname.widthHint = 100;
		textDictname.setLayoutData(gd_textDictname);
		
		Label lblNewLabel_4 = new Label(container, SWT.NONE);
		lblNewLabel_4.setText("User:");
		
		textUser = new Text(container, SWT.BORDER);
		textUser.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				userTouched = true;
				validate();
			}
		});
		textUser.addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(TraverseEvent e) {
				userTouched = true;
				validateAndMoveToNextFieldWhenApplicable(e);
			}
		});
		GridData gd_textUser = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_textUser.widthHint = 100;
		textUser.setLayoutData(gd_textUser);
		
		Label lblNewLabel_5 = new Label(container, SWT.NONE);
		lblNewLabel_5.setText("Password:");
		
		textPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textPassword.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				passwordTouched = true;
				validate();
			}
		});
		textPassword.addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(TraverseEvent e) {
				passwordTouched = true;
				validateAndMoveToNextFieldWhenApplicable(e);
			}
		});
		GridData gd_textPassword = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textPassword.widthHint = 100;
		textPassword.setLayoutData(gd_textPassword);
		
		Label lblpromptWhenEmpty = new Label(container, SWT.NONE);
		GridData gd_lblpromptWhenEmpty = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblpromptWhenEmpty.horizontalIndent = 10;
		lblpromptWhenEmpty.setLayoutData(gd_lblpromptWhenEmpty);
		lblpromptWhenEmpty.setText("(prompt when empty)");
		
		Label lblNewLabel_6 = new Label(container, SWT.NONE);
		lblNewLabel_6.setText("Schema:");
		
		textSchema = new Text(container, SWT.BORDER);
		textSchema.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				schemaTouched = true;
				validate();
			}
		});
		textSchema.addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(TraverseEvent e) {
				schemaTouched = true;
				validateAndMoveToNextFieldWhenApplicable(e);
			}
		});
		GridData gd_textSchema = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textSchema.widthHint = 100;
		textSchema.setLayoutData(gd_textSchema);
		new Label(container, SWT.NONE);
		
		btnSysdirl = new Button(container, SWT.CHECK);
		btnSysdirl.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				validateAndMoveToNextFieldWhenApplicable(e);
			}
		});
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
		GridData gd_btnSysdirl = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_btnSysdirl.verticalIndent = 10;
		btnSysdirl.setLayoutData(gd_btnSysdirl);
		btnSysdirl.setText("This is a SYSDIRL dictionary");
		
		btnTestConnection = new Button(container, SWT.NONE);
		btnTestConnection.setEnabled(false);
		btnTestConnection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				testConnection();
			}
		});
		GridData gd_btnTestConnection = new GridData(SWT.CENTER, SWT.CENTER, true, false, 3, 1);
		gd_btnTestConnection.verticalIndent = 20;
		btnTestConnection.setLayoutData(gd_btnTestConnection);
		btnTestConnection.setText("Test Connection");
		
		Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
		gd_label.verticalIndent = 10;
		label.setLayoutData(gd_label);
		
		initializeValues();

		return area;
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

	public String getDictionarySchema() {
		return dictionarySchema;
	}

	public String getDictionaryUser() {
		return dictionaryUser;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(350, 425);
	}
	
	private void initializeValues() {
	
		setTitle("Dictionary properties");
		setMessage("The password you enter is encrypted before it is stored.");
		
		if (dictionary != null) {			
			textId.setText(dictionary.getId());
			textHostname.setText(dictionary.getHostname());
			textPort.setText(String.valueOf(dictionary.getPort()));
			textDictname.setText(dictionary.getDictname());
			textUser.setText(dictionary.getUser());
			if (dictionary.getPassword() != null) {
				textPassword.setText(dictionary.getPassword());
			}
			textSchema.setText(dictionary.getSchema());
			btnSysdirl.setSelection(dictionary.isSysdirl());
		} else {
			textPort.setText("3709");
			String defaultSchema = 
				Plugin.getDefault().getPreferenceStore().getString(PreferenceConstants.DEFAULT_SCHEMA);
			textSchema.setText(defaultSchema);
		}
		
		setFocusAndSelectText(textId);
		
		btnTestConnection.setEnabled(validateInput().validationOK &&
									 Plugin.getDefault().isDriverInstalled());
		
	}

	public boolean isDictionarySysdirl() {
		return dictionarySysdirl;
	}

	private void setFocusAndSelectText(Text field) {
		field.setFocus();
		field.selectAll();
	}

	protected void testConnection() {
		Dictionary tmpDictionary = Dictionary.newTemporaryInstance();
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

	private InputValidationResult validate() {
		
		InputValidationResult inputValidationResult = validateInput();
		setErrorMessage(inputValidationResult.errorMessage);
		
		if (inputValidationResult.validationOK) {
			dictionaryId = textId.getText().trim();
			dictionaryHostname = textHostname.getText().trim();
			dictionaryPort = Integer.valueOf(textPort.getText().trim()).intValue();
			dictionaryDictname = textDictname.getText().trim();
			dictionaryUser = textUser.getText().trim();
			if (!textPassword.getText().trim().equals("")) {
				dictionaryPassword = textPassword.getText().trim();
			} else {
				dictionaryPassword = null;
			}
			dictionarySchema = textSchema.getText().trim();
			dictionarySysdirl = btnSysdirl.getSelection();
		}
		
		btnTestConnection.setEnabled(inputValidationResult.validationOK && 
									 Plugin.getDefault().isDriverInstalled());
		getButton(IDialogConstants.OK_ID).setEnabled(inputValidationResult.validationOK);
		
		return inputValidationResult;
		
	}
	
	private void validateAndMoveToNextFieldWhenApplicable(TraverseEvent e) {
		if (e.detail != SWT.TRAVERSE_RETURN && e.detail != SWT.TRAVERSE_TAB_NEXT &&
			e.detail != SWT.TRAVERSE_TAB_PREVIOUS) {
			
			return;
		}
		InputValidationResult inputValidationResult = validate();		
		if (e.getSource() == textId) {
			setFocusAndSelectText(textHostname);
		} else if (e.getSource() == textHostname) {
			setFocusAndSelectText(textPort);
		} else if (e.getSource() == textPort) {
			setFocusAndSelectText(textDictname);
		} else if (e.getSource() == textDictname) {
			setFocusAndSelectText(textUser);
		} else if (e.getSource() == textUser) {
			setFocusAndSelectText(textPassword);
		} else if (e.getSource() == textPassword) {
			setFocusAndSelectText(textSchema);
		} else if (e.getSource() == textSchema) {
			if (inputValidationResult.fieldInError != null) {
				setFocusAndSelectText(inputValidationResult.fieldInError);
			} else {				
				btnSysdirl.setFocus();
			}
		} else if (e.getSource() == btnSysdirl) {
			if (inputValidationResult.fieldInError != null) {
				setFocusAndSelectText(inputValidationResult.fieldInError);
			} else {
				btnTestConnection.setFocus();
			}
		}
	}
	
	private InputValidationResult validateInput() {
		if (textId.getText().trim().equals("")) {
			String message = idTouched ? "Id is mandatory" : null;
			return new InputValidationResult(false, message, textId);
		}
		if (textHostname.getText().trim().equals("")) {
			String message = hostnameTouched ? "Hostname is mandatory" : null;
			return new InputValidationResult(false, message, textHostname);
		}
		if (textPort.getText().trim().equals("")) {
			String message = portTouched ? "Port is mandatory" : null;
			return new InputValidationResult(false, message, textPort);
		} else {
			try {
				Integer.parseInt(textPort.getText());
			} catch (NumberFormatException e) {
				String message = portTouched ? "Port is invalid" : null;
				return new InputValidationResult(false, message, textPort);
			}
		}
		if (textDictname.getText().trim().equals("")) {
			String message = dictnameTouched ? "Dictname is mandatory" : null;
			return new InputValidationResult(false, message, textDictname);
		}
		if (textUser.getText().trim().equals("")) {
			String message = userTouched ? "User is mandatory" : null;
			return new InputValidationResult(false, message, textUser);
		}
		if (textPassword.getText().trim().length() > 99) {
			String message = passwordTouched ? "Password is invalid" : null;
			return new InputValidationResult(false, message, textPassword);
		}
		if (textSchema.getText().trim().equals("")) {
			String message = schemaTouched ? "Schema is mandatory" : null;
			return new InputValidationResult(false, message, textSchema);
		}
		return new InputValidationResult(true, null, null);
	}

	public static class InputValidationResult {
		
		private String errorMessage;
		private boolean validationOK;
		private Text fieldInError;
		
		public InputValidationResult(boolean validationOK, String errorMessage, Text fieldInError) {
			super();
			this.validationOK = validationOK;
			this.errorMessage = errorMessage;
			this.fieldInError = fieldInError;
		}
		
	}

}
