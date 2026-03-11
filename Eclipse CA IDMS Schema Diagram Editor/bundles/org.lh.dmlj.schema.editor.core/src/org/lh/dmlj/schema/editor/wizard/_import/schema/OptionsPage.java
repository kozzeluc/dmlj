/**
 * Copyright (C) 2025  Luc Hermans
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
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.common.NamingConventions;
import org.lh.dmlj.schema.editor.common.ValidationResult;
import org.lh.dmlj.schema.editor.extension.OptionExtensionElement;
import org.lh.dmlj.schema.editor.extension.OptionsExtensionElement;
import org.lh.dmlj.schema.editor.importtool.AbstractDataEntryPage;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;
import org.lh.dmlj.schema.editor.preference.PreferenceConstants;

public class OptionsPage extends AbstractDataEntryPage {
	private List<Label> idmsntwkOptionGroupLabels = new ArrayList<>();
	private OptionsExtensionElement optionsExtensionElement;
	private Map<String, Button> optionToButtonMap = new HashMap<>();
	
	private Button btnDdlcatlod;
	private Button btnLooak155;
	private Button btnOoak012;
	private Label lblIdmsntwk;
	private Text textProcedures;				
	
	private static String getOptionGroupLabel(String label) {
		var p = new StringBuilder(label);
		if (p.charAt(p.length() - 1) == ':') {
			p.setLength(p.length() - 1);
		}
		return p.toString().trim() + ":";
	}

	public OptionsPage(OptionsExtensionElement optionsExtensionElement) {
		this.optionsExtensionElement = optionsExtensionElement;
	}

	@Override
	public void aboutToShow() {
		// show or hide the IDMSNTWK version 1 options section
		String schemaName = getContext().getAttribute(IDataEntryContext.SCHEMA_NAME);
		Short schemaVersion = getContext().getAttribute(IDataEntryContext.SCHEMA_VERSION);
						
		var isIdmsntwk = schemaName != null && schemaVersion != null && schemaName.equals("IDMSNTWK") && schemaVersion.intValue() == 1;
		lblIdmsntwk.setVisible(isIdmsntwk);
		btnOoak012.setVisible(isIdmsntwk);
		btnLooak155.setVisible(isIdmsntwk);
		btnDdlcatlod.setVisible(isIdmsntwk);
		// call setVisible for configured IDMSNTWK version 1 options and their group labels...
		for (Label label : idmsntwkOptionGroupLabels) {
			label.setVisible(isIdmsntwk);
		}
		for (var optionExtensionElement : optionsExtensionElement.getOptionExtensionElements()) {
			if (optionExtensionElement.getIdmsntwkOnly()) {
				var button = optionToButtonMap.get(optionExtensionElement.getName());
				button.setVisible(isIdmsntwk);
			}
		}
		validatePage();
	}

	private void createCheckButton(Composite parent, OptionExtensionElement optionExtensionElement, int verticalIndent) {
		var name = optionExtensionElement.getName().trim();
		if (optionToButtonMap.containsKey(name)) {
			// duplicate option; ignore
			var message = "import tool option '" + name + "' is already defined (defining plug-in: " +
					optionExtensionElement.getPluginId() + ")";
			Plugin.getDefault().getLog().warn(message);			
			return;
		}
		
		var checkButton = new Button(parent, SWT.CHECK);		
		checkButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				checkButtonCheckedOrUnchecked(checkButton, optionExtensionElement);
			}
		});
		var gd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd.horizontalIndent = 5;
		if (verticalIndent != 0) {
			gd.verticalIndent = verticalIndent;
		}
		checkButton.setLayoutData(gd);
		var initialValue = optionExtensionElement.getInitialValue();
		checkButton.setSelection(initialValue);
		var checkButtonLabel = optionExtensionElement.getCheckButtonLabel();
		checkButton.setText(checkButtonLabel);
		optionToButtonMap.put(optionExtensionElement.getName(), checkButton);
	}
	
	private void checkButtonCheckedOrUnchecked(Button checkButton, OptionExtensionElement optionExtensionElement) {
		// if the option is selected, reset the option with which this option(s) is mutually exclusive, if specified
		var otherNames = optionExtensionElement.getMutuallyExclusiveWith();
		if (checkButton.getSelection() && otherNames != null && !otherNames.isBlank()) { 
			var tokenizer = new StringTokenizer(otherNames, ",");
			while (tokenizer.hasMoreTokens()) {
				var otherName = tokenizer.nextToken();
				if (optionToButtonMap.containsKey(otherName)) {
					var otherCheckButton = optionToButtonMap.get(otherName);
					if (otherCheckButton.getSelection()) {
						otherCheckButton.setSelection(false);
					}
				} else {
					// the extension point element refers to an option that is not defined
					var message = "import tool option '" + optionExtensionElement.getName() + "' is mutually exclusive with option " +
							otherName + "', but option '" + otherName + "' is not defined in the import tool " +
							"(defining plug-in: " + optionExtensionElement.getPluginId() + ")";
					Plugin.getDefault().getLog().warn(message);
				}
			}
		}
		// make sure the context gets updated
		validatePage();
	}
		
	@Override
	public Control createControl(Composite parent) {
		var composite = new Composite(parent, SWT.NONE);	
		composite.setLayout(new GridLayout(1, false));
					
		createGeneralOptionControls(composite);
		createCheckButtons(composite);
						
		new Label(composite, SWT.NONE); // spacer to IDMSNTWK version 1 options
		createIdmsntwkRelatedControls(composite);
		
		textProcedures.setText(Plugin.getDefault().getPreferenceStore().getString(PreferenceConstants.COMPRESSION_PROCEDURES));
		validatePage();
		return composite;
	}
	
	private void createGeneralOptionControls(Composite composite) {
		// general options
		var lblGeneralOptions = new Label(composite, SWT.NONE);
		lblGeneralOptions.setText("General options :");
		var lblprocedureNames = new Label(composite, SWT.NONE);
		var gdLblprocedureNames = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdLblprocedureNames.horizontalIndent = 5;
		gdLblprocedureNames.verticalIndent = 5;
		lblprocedureNames.setLayoutData(gdLblprocedureNames);
		lblprocedureNames.setText("Database procedures used for COMPRESSION (comma-separated list):");		
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
		var gdTextProcedures = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gdTextProcedures.horizontalIndent = 5;
		textProcedures.setLayoutData(gdTextProcedures);
	}
	
	private void createCheckButtons(Composite composite) {
		// create a check button for each configured option; deal with the ones not belonging to a group first
		// and then process the options group by group		
		for (var optionExtensionElement : optionsExtensionElement.getOptionExtensionElements()) {
			var optionGroup = optionExtensionElement.getGroup();			
			// filter out the options that are only applicable for IDMSNTWK version 1 schemas
			if (!optionExtensionElement.getIdmsntwkOnly() && optionGroup.equals("")) {
				createCheckButton(composite, optionExtensionElement, 0);
			}				
		}
		for (var optionGroupExtensionElement : optionsExtensionElement.getOptionGroupExtensionElements()) {
			boolean labelCreated = false;
			for (var optionExtensionElement : optionsExtensionElement.getOptionExtensionElements()) {
				if (!optionExtensionElement.getIdmsntwkOnly() && optionExtensionElement.getGroup().equals(optionGroupExtensionElement.getName())) {
					if (!labelCreated) {
						var optionGroupLabel = new Label(composite, SWT.NONE);
						optionGroupLabel.setText(getOptionGroupLabel(optionGroupExtensionElement.getLabel()));
						var gdOptionGroupLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
						gdOptionGroupLabel.horizontalIndent = 5;
						gdOptionGroupLabel.verticalIndent = 5;
						optionGroupLabel.setLayoutData(gdOptionGroupLabel);
						labelCreated = true;
					}
					createCheckButton(composite, optionExtensionElement, 0);
				}
			}
		}
	}
	
	private void createIdmsntwkRelatedControls(Composite composite) {
		lblIdmsntwk = new Label(composite, SWT.NONE);
		lblIdmsntwk.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblIdmsntwk.setText("IDMSNTWK version 1 options :");
		
		btnOoak012 = new Button(composite, SWT.CHECK);
		var gdBtnOoak012 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gdBtnOoak012.horizontalIndent = 5;
		gdBtnOoak012.verticalIndent = 5;
		btnOoak012.setLayoutData(gdBtnOoak012);
		btnOoak012.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validatePage();
			}
		});
		btnOoak012.setSelection(true);
		btnOoak012.setText("Add missing offset-expression for record OOAK-012 in area DDLDML");
		
		btnLooak155 = new Button(composite, SWT.CHECK);
		var gdBtnLooak155 = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gdBtnLooak155.horizontalIndent = 5;
		btnLooak155.setLayoutData(gdBtnLooak155);
		btnLooak155.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validatePage();
			}
		});		
		btnLooak155.setSelection(true);
		btnLooak155.setText("Add missing offset-expression for record LOOAK-155 in area DDLDCLOD (and DDLCATLOD)");
		
		btnDdlcatlod = new Button(composite, SWT.CHECK);
		var gdBtnDdlcatlod = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gdBtnDdlcatlod.horizontalIndent = 5;
		btnDdlcatlod.setLayoutData(gdBtnDdlcatlod);
		btnDdlcatlod.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validatePage();
			}
		});
		btnDdlcatlod.setSelection(true);
		btnDdlcatlod.setText("Add DDLCATLOD records and sets");
		
		// create a check button for each configured option; deal with the ones not belonging to a group first
		// and then process the options group by group
		for (var optionExtensionElement : optionsExtensionElement.getOptionExtensionElements()) {
			var optionGroup = optionExtensionElement.getGroup();						
			// filter out the general options
			if (optionExtensionElement.getIdmsntwkOnly() && optionGroup.equals("")) {
				createCheckButton(composite, optionExtensionElement, 0);
			}				
		}
		for (var optionGroupExtensionElement : optionsExtensionElement.getOptionGroupExtensionElements()) {
			var labelCreated = false;
			for (var optionExtensionElement : optionsExtensionElement.getOptionExtensionElements()) {
				if (optionExtensionElement.getIdmsntwkOnly() && optionExtensionElement.getGroup().equals(optionGroupExtensionElement.getName())) {
					if (!labelCreated) {
						var optionGroupLabel = new Label(composite, SWT.NONE);
						optionGroupLabel.setText(getOptionGroupLabel(optionGroupExtensionElement.getLabel()));
						var gdOptionGroupLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
						gdOptionGroupLabel.horizontalIndent = 5;
						gdOptionGroupLabel.verticalIndent = 5;
						optionGroupLabel.setLayoutData(gdOptionGroupLabel);												
						idmsntwkOptionGroupLabels.add(optionGroupLabel);
						labelCreated = true;
					}
					createCheckButton(composite, optionExtensionElement, 0);
				}
			}
		}		
	}
	
	private void validatePage() {
		var pageComplete = true;
		getController().setErrorMessage(null);
		
		// fixed general options
		var procedureNames = new ArrayList<String>();
		var tokenizer = new StringTokenizer(textProcedures.getText().trim(), ",");
		while (tokenizer.hasMoreTokens()) {
			var procedureName = tokenizer.nextToken().trim().toUpperCase();
			var validationResult = NamingConventions.validate(procedureName, NamingConventions.Type.PROCEDURE_NAME);
			if (validationResult.getStatus() == ValidationResult.Status.OK) {
				if (!procedureNames.contains(procedureName)) {
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
			getContext().setAttribute(GeneralContextAttributeKeys.COMPRESSION_PROCEDURE_NAMES, procedureNames);
			var p = procedureNames.toString();
			textProcedures.setText(p.substring(1, p.length() - 1));
		}
		
		// fixed IDMSNTWK version 1 options
		getContext().setAttribute(GeneralContextAttributeKeys.ADD_OFFSET_FOR_OOAK_012, btnOoak012.getSelection());
		getContext().setAttribute(GeneralContextAttributeKeys.ADD_OFFSET_FOR_LOOAK_155, btnLooak155.getSelection());
		getContext().setAttribute(GeneralContextAttributeKeys.ADD_DDLCATLOD, btnDdlcatlod.getSelection());
		
		// extension point options
		for (var optionExtensionElement : optionsExtensionElement.getOptionExtensionElements()) {
			var name = optionExtensionElement.getName().trim();
			var button = optionToButtonMap.get(name);			
			getContext().setAttribute(name, button.getSelection());			
		}
		
		getController().setPageComplete(pageComplete);
	}

}
