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
package org.lh.dmlj.schema.editor.wizard._import.elements;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.extension.RecordElementsImportToolExtensionElement;

public class ImportToolSelectionPage extends WizardPage {
	private static final String CONFIRM_BUTTON_MESSAGE = """
		After selecting a data source, press the 'Select' button so that you can proceed with the next page; you \
		will NOT be able to change your choice once you have pressed the 'Select' button. You will NOT be able \
		to return to this page once you press the 'Next' button.\
		""";
	
	private final List<RecordElementsImportToolExtensionElement> extensionElements;
	private final String recordElementsDSL;
	private RecordElementsImportToolExtensionElement extensionElement;
	
	private Button btnSelect;
	private Combo combo;
	private Text textDescription;
	private Text textCurrentRecordElementsDSL;
	
	public ImportToolSelectionPage(List<RecordElementsImportToolExtensionElement> extensionElements,
			SchemaRecord schemaRecord, String recordElementsDSL) {
		
		super("_importToolSelectionPage", "Elements for Record " + schemaRecord.getName(), null);
		this.extensionElements = extensionElements;
		this.recordElementsDSL = recordElementsDSL;
		setMessage("Select the (data) source; the current record structure will be COMPLETELY replaced");
	}

	@Override
	public void createControl(Composite parent) {		
		var container = new Composite(parent, SWT.NONE);
		setControl(container);				
		container.setLayout(new GridLayout(3, false));
		
		var lblInstalledImportTools = new Label(container, SWT.NONE);
		lblInstalledImportTools.setText("Source:");
		
		combo = new Combo(container, SWT.READ_ONLY);
		var i = extensionElements.size() > 1 ? 1 : 2;		
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, i, 1));		
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {				
				selectImportTool();
			}
		});
		
		// the 'Select' button is only relevant if more than 1 import tool is defined; we use this button to
		// enable the 'Next (page)' button - once this button is enabled, the next pages for the wizard will be
		// added so this is a one time operation with no way back
		if (extensionElements.size() > 1) {
			btnSelect = new Button(container, SWT.NONE);
			btnSelect.setEnabled(false);
			btnSelect.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					combo.setEnabled(false);
					btnSelect.setEnabled(false);
					setPageComplete(true);
				}
			});
			btnSelect.setText("Select");
		}
		
		var lblNewLabel1 = new Label(container, SWT.NONE);
		lblNewLabel1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		
		var lblDescription = new Label(container, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblDescription.setText("Description:");
		
		textDescription = new Text(container, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		var gdText = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1);
		gdText.heightHint = 75;
		gdText.widthHint = 300;
		textDescription.setLayoutData(gdText);
			
		if (extensionElements.size() > 1) {
			combo.add("[select a data source and press the 'Select' button]");
		}		
		extensionElements.stream()
				.map(RecordElementsImportToolExtensionElement::getSource)
				.forEach(combo::add);
		combo.select(0);
		
		var lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		
		var lblCurrentRecordStructure = new Label(container, SWT.WRAP);
		var gdLblCurrentRecordStructure = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gdLblCurrentRecordStructure.widthHint = 75;
		lblCurrentRecordStructure.setLayoutData(gdLblCurrentRecordStructure);
		lblCurrentRecordStructure.setText("Current record elements DSL:");
		
		textCurrentRecordElementsDSL = new Text(container, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textCurrentRecordElementsDSL.setFont(SWTResourceManager.getFont("Courier New", 10, SWT.NORMAL));
		var gdTextCurrentRecordStructure = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gdTextCurrentRecordStructure.heightHint = 200;
		gdTextCurrentRecordStructure.widthHint = 200;
		textCurrentRecordElementsDSL.setLayoutData(gdTextCurrentRecordStructure);
		
		if (extensionElements.size() > 1) {
			// if there is more than 1 import tool available, pressing the 'Select' button will mark the page as complete			
			textDescription.setText(CONFIRM_BUTTON_MESSAGE);
			setPageComplete(false);
		} else {
			// if there is only 1 import tool, there is no point in requiring the 'Select' button to be pressed								
			extensionElement = extensionElements.get(0);
			textDescription.setText(extensionElement.getDescription());			
			setPageComplete(true);		
		}
		
		initialize();
	}
	
	private void initialize() {
		textCurrentRecordElementsDSL.setText(recordElementsDSL);		
	}

	private void selectImportTool() {		
		// there will always be at least 1 import tool
		var i = combo.getSelectionIndex();
		if (extensionElements.size() == 1) {
			extensionElement = extensionElements.get(0);
		} else if (i > 0) {
			extensionElement = extensionElements.get(i - 1);
			btnSelect.setEnabled(true);
			textDescription.setText(extensionElement.getDescription());
		} else {
			extensionElement = null;
			textDescription.setText(CONFIRM_BUTTON_MESSAGE);
			btnSelect.setEnabled(false);
		}
	}
	
	public RecordElementsImportToolExtensionElement getExtensionElement() {
		return extensionElement;
	}
	
}
