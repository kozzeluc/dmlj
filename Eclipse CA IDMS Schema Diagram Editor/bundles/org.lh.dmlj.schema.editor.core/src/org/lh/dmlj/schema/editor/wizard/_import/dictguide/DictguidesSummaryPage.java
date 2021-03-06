/**
 * Copyright (C) 2013  Luc Hermans
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
package org.lh.dmlj.schema.editor.wizard._import.dictguide;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.wb.swt.SWTResourceManager;
import org.lh.dmlj.schema.editor.dictguide.DictguidesRegistry;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class DictguidesSummaryPage extends WizardPage {
	private Button  btnSetAsDefault;
	private boolean calledFromPreferences;
	private boolean defaultForInfoTab;
	private String  id;
	private Label   lblDictrefguideTitle;
	private Label   lblRemark;
	private Label   lblSqlrefguideTitle;	
	private Text    txtId;
	
	/**
	 * Create the wizard.
	 */
	public DictguidesSummaryPage(boolean calledFromPreferences) {
		super("wizardPage");
		this.calledFromPreferences = calledFromPreferences;
		setMessage("Specify a unique id for the reference guide combination");
		setTitle("CA IDMS/DB Dictionary Structure and SQL Reference Guides");
		setDescription("Wizard Page description");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(2, false));
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setText("Id :");
		
		txtId = new Text(container, SWT.BORDER);
		txtId.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				validatePage();
			}
		});
		txtId.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				validatePage();
			}
		});
		txtId.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));		
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		lblDictrefguideTitle = new Label(container, SWT.NONE);
		lblDictrefguideTitle.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		GridData gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		lblDictrefguideTitle.setLayoutData(gd);
		new Label(container, SWT.NONE);
		
		lblSqlrefguideTitle = new Label(container, SWT.NONE);
		lblSqlrefguideTitle.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		GridData gd_1 = new GridData();
		gd_1.horizontalAlignment = SWT.FILL;
		lblSqlrefguideTitle.setLayoutData(gd_1);
		new Label(container, SWT.NONE);
		
		btnSetAsDefault = new Button(container, SWT.CHECK);
		btnSetAsDefault.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validatePage();
			}
		});
		btnSetAsDefault.setSelection(true);
		GridData gd_btnSetAsDefault = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnSetAsDefault.verticalIndent = 5;
		btnSetAsDefault.setLayoutData(gd_btnSetAsDefault);
		btnSetAsDefault.setText("Use this combination in the \"Info\" tab (Properties view) for dictionary related record types");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		lblRemark = new Label(container, SWT.NONE);
		lblRemark.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true, 2, 1));
		lblRemark.setText("Please note :  You can manage reference guide combinations in this plug-in's preferences.");
		
		btnSetAsDefault.setEnabled(!calledFromPreferences);
		btnSetAsDefault.setSelection(!calledFromPreferences);
		lblRemark.setVisible(!calledFromPreferences);		
		
		setPageComplete(false);
		
	}
	
	public String getId() {
		return id;
	}
	
	public boolean isDefaultForInfoTab() {
		return defaultForInfoTab;
	}
	
	void setDictStructureRefGuide(String newValue) {
		lblDictrefguideTitle.setText(newValue);
	}

	void setSqlRefGuide(String newValue) {
		lblSqlrefguideTitle.setText(newValue);
	}
	
	private void validatePage() {
		
		setErrorMessage(null);
		
		if (txtId.getText().trim().equals("")) {
			return;
		}
		
		boolean pageComplete = true;
		
		if (!DictguidesRegistry.INSTANCE.isValid(txtId.getText().trim())) {
			setErrorMessage("Id can contain only letters, digits, spaces, " +
						    "hyphens, underscores and periods");
			pageComplete = false;
		} else if (DictguidesRegistry.INSTANCE.entryExists(txtId.getText().trim())) {
			setErrorMessage("Id is already in use");
			pageComplete = false;
		}
		
		if (pageComplete) {
			id = txtId.getText().trim();
			defaultForInfoTab = btnSetAsDefault.getSelection();
			
		}
		
		setPageComplete(pageComplete);
		
	}
	
}
