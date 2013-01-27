package org.lh.dmlj.schema.editor.wizard._import.schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.lh.dmlj.schema.editor.common.NamingConventions;
import org.lh.dmlj.schema.editor.common.ValidationResult;
import org.lh.dmlj.schema.editor.extension.OptionExtensionElement;
import org.lh.dmlj.schema.editor.extension.OptionsExtensionElement;
import org.lh.dmlj.schema.editor.importtool.AbstractDataEntryPage;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;

public class OptionsPage extends AbstractDataEntryPage {
	
	private Button 	  				btnDdlcatlod;
	private Button 	  				btnLooak_155;
	private Button 	  				btnOoak_012;	
	private Label     				lblIdmsntwk;
	private OptionsExtensionElement optionsExtensionElement;
	private Map<String, Button> 	optionToButtonMap = new HashMap<>();
	private Text 					textProcedures;				
	
	public OptionsPage(OptionsExtensionElement optionsExtensionElement) {
		super();
		this.optionsExtensionElement = optionsExtensionElement;
	}

	@Override
	public void aboutToShow() {
		
		// show or hide the IDMSNTWK version 1 options section
		
		String schemaName = 
			getContext().getAttribute(IDataEntryContext.SCHEMA_NAME);
		Short schemaVersion = 
			getContext().getAttribute(IDataEntryContext.SCHEMA_VERSION);
						
		boolean b = 
			schemaName != null && schemaVersion != null &&
			schemaName.equals("IDMSNTWK") && schemaVersion.intValue() == 1;		

		lblIdmsntwk.setVisible(b);
		btnOoak_012.setVisible(b);
		btnLooak_155.setVisible(b);
		btnDdlcatlod.setVisible(b);
		// call setVisible for configured IDMSNTWK version 1 options...
		for (OptionExtensionElement optionExtensionElement :
			 optionsExtensionElement.getOptionExtensionElements()) {
			
			if (optionExtensionElement.getIdmsntwkOnly()) {
				String name = optionExtensionElement.getName();
				Button button = optionToButtonMap.get(name);
				button.setVisible(b);
			}
		}
		
		validatePage();
		
	}

	private void createCheckButton(Composite parent,
								   final OptionExtensionElement optionExtensionElement) {
		
		String name = optionExtensionElement.getName().trim();
		if (optionToButtonMap.containsKey(name)) {
			// duplicate option; ignore
			String message = "import tool option '" + name + 
							 "' is already defined (defining plug-in: " + 
							 optionExtensionElement.getPluginId() + ")";
			System.out.println(message);			
			return;
		}
		
		final Button checkButton = new Button(parent, SWT.CHECK);
		checkButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// if the option is selected, reset the option with 
				// which this option is mutually exclusive, if such an
				// option is specified
				if (checkButton.getSelection()) {
					String otherName = 
						optionExtensionElement.getMutuallyExclusiveWith();
					if (otherName != null && !otherName.trim().equals("")) { 
						if (optionToButtonMap.containsKey(otherName)) {
							Button otherCheckButton = 
								optionToButtonMap.get(otherName);
							if (otherCheckButton.getSelection()) {
								otherCheckButton.setSelection(false);
							}
						} else {
							// the extension point element refers to an option 
							// that is not defined
							String message = 
								"import tool option '" + 
								optionExtensionElement.getName() + 
								"' is mutually exclusive with option " + 
								otherName + "', but option '" + otherName + 
								"' is not defined in the import tool " +
								"(defining plug-in: " + 
								optionExtensionElement.getPluginId() + 
								")";
							System.out.println(message);
						}
					}
				}
				// make sure the context gets updated
				validatePage();
			}
		});
		GridData gd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd.horizontalIndent = 5;
		checkButton.setLayoutData(gd);
		boolean initialValue = optionExtensionElement.getInitialValue();
		checkButton.setSelection(initialValue);
		String checkButtonLabel =
			optionExtensionElement.getCheckButtonLabel();
		checkButton.setText(checkButtonLabel);
		optionToButtonMap.put(optionExtensionElement.getName(), checkButton);
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public Control createControl(Composite parent) {
		
		Composite composite = new Composite(parent, SWT.NONE);	
		composite.setLayout(new GridLayout(1, false));
				
		// general options
		Label lblGeneralOptions = new Label(composite, SWT.NONE);
		lblGeneralOptions.setText("General options :");
		Label lblprocedureNames = new Label(composite, SWT.NONE);
		GridData gd_lblprocedureNames = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblprocedureNames.horizontalIndent = 5;
		lblprocedureNames.setLayoutData(gd_lblprocedureNames);
		lblprocedureNames.setText("Database procedures used for COMPRESSION (" +
								  "other than IDMSCOMP; comma-separated list):");		
		textProcedures = new Text(composite, SWT.BORDER);
		textProcedures.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == 13) {
					validatePage();
				}
			}
		});
		textProcedures.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				validatePage();
			}
		});
		GridData gd_textProcedures = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_textProcedures.horizontalIndent = 5;
		textProcedures.setLayoutData(gd_textProcedures);
		// create a check button for each configured option
		for (final OptionExtensionElement optionExtensionElement :
			 optionsExtensionElement.getOptionExtensionElements()) {
			 
			// filter out the options that are only applicable for 
			// IDMSNTWK version 1 schemas
			if (!optionExtensionElement.getIdmsntwkOnly()) {
				createCheckButton(composite, optionExtensionElement);
			}				
		}
		
		// spacer to IDMSNTWK version 1 options
		new Label(composite, SWT.NONE);		
		
		// IDMSNTWK version 1 options
		
		lblIdmsntwk = new Label(composite, SWT.NONE);
		lblIdmsntwk.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblIdmsntwk.setText("IDMSNTWK version 1 options :");
		
		btnOoak_012 = new Button(composite, SWT.CHECK);
		GridData gd_btnOoak_012 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_btnOoak_012.horizontalIndent = 5;
		btnOoak_012.setLayoutData(gd_btnOoak_012);
		btnOoak_012.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validatePage();
			}
		});
		btnOoak_012.setSelection(true);
		btnOoak_012.setText("Add missing offset-expression for record OOAK-012 in area DDLDML");
		
		btnLooak_155 = new Button(composite, SWT.CHECK);
		GridData gd_btnLooak_155 = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_btnLooak_155.horizontalIndent = 5;
		btnLooak_155.setLayoutData(gd_btnLooak_155);
		btnLooak_155.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validatePage();
			}
		});		
		btnLooak_155.setSelection(true);
		btnLooak_155.setText("Add missing offset-expression for record LOOAK-155 in area DDLDCLOD (and DDLCATLOD)");
		
		btnDdlcatlod = new Button(composite, SWT.CHECK);
		GridData gd_btnDdlcatlod = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_btnDdlcatlod.horizontalIndent = 5;
		btnDdlcatlod.setLayoutData(gd_btnDdlcatlod);
		btnDdlcatlod.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validatePage();
			}
		});
		btnDdlcatlod.setSelection(true);
		btnDdlcatlod.setText("Add DDLCATLOD records and sets");
		
		// create a check button for each configured option
		for (final OptionExtensionElement optionExtensionElement :
			 optionsExtensionElement.getOptionExtensionElements()) {
			 
			// filter out the general options
			if (optionExtensionElement.getIdmsntwkOnly()) {
				createCheckButton(composite, optionExtensionElement);
			}				
		}
		
		validatePage();
		
		return composite;
		
	}	
	
	private void validatePage() {		
		
		boolean pageComplete = true;
		getController().setErrorMessage(null);
		
		// fixed general options
		List<String> procedureNames = new ArrayList<>();
		StringTokenizer tokenizer = 
			new StringTokenizer(textProcedures.getText().trim(), ",");
		while (tokenizer.hasMoreTokens()) {
			String procedureName = tokenizer.nextToken().trim().toUpperCase();
			ValidationResult validationResult = 
				NamingConventions.validate(procedureName, 
										   NamingConventions.Type.PROCEDURE_NAME);
			if (validationResult.getStatus() == ValidationResult.Status.OK) {
				if (!procedureNames.contains(procedureName) && 
					!procedureName.equals("IDMSCOMP")) {
					
					procedureNames.add(procedureName);
				}
			} else {
				getController().setErrorMessage(validationResult.getMessage());
				pageComplete = false;
			}
		}
		if (pageComplete) {
			// no errors
			Collections.sort(procedureNames);
			getContext().setAttribute(GeneralContextAttributeKeys.COMPRESSION_PROCEDURE_NAMES, 
			 	  				  	  procedureNames);
			String p = procedureNames.toString();
			textProcedures.setText(p.substring(1, p.length() - 1));
		}
		
		// fixed IDMSNTWK version 1 options
		getContext().setAttribute(GeneralContextAttributeKeys.ADD_OFFSET_FOR_OOAK_012, 
							 	  btnOoak_012.getSelection());
		getContext().setAttribute(GeneralContextAttributeKeys.ADD_OFFSET_FOR_LOOAK_155, 
							 	  btnLooak_155.getSelection());
		getContext().setAttribute(GeneralContextAttributeKeys.ADD_DDLCATLOD, 
			 	  				  btnDdlcatlod.getSelection());
		
		// extension point options
		for (OptionExtensionElement optionExtensionElement :
			 optionsExtensionElement.getOptionExtensionElements()) {
			
			String name = optionExtensionElement.getName().trim();
			Button button = optionToButtonMap.get(name);			
			getContext().setAttribute(name, button.getSelection());			
		}
		
		getController().setPageComplete(pageComplete);
		
	}

}