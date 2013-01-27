package org.lh.dmlj.schema.editor.wizard._import.schema;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.lh.dmlj.schema.editor.extension.ImportToolExtensionElement;
import org.eclipse.swt.widgets.Button;

public class ImportToolSelectionPage extends WizardPage {
	
	private static final String CONFIRM_BUTTON_MESSAGE = 
		"After selecting a data source, press the 'Confirm' button so that " +
		"you can proceed with the next page; you will only be able to change " +
		"your choice once you have pressed the 'Confirm' button, by using " +
		"this page's 'Back' button and selecting this wizard again.  You " +
		"will NOT be able to return to this page once you press the 'Next' " +
		"button";
	
	private Button 							 btnConfirm;
	private Combo 							 combo;
	private ImportToolExtensionElement 	     extensionElement;
	private List<ImportToolExtensionElement> extensionElements;		
	private Text 							 textDescription;

	public ImportToolSelectionPage(List<ImportToolExtensionElement> extensionElements) {
		super("_importToolSelectionPage", "CA IDMS/DB Schema", null);
		// there will be at least 1 import tool
		this.extensionElements = extensionElements; 
		setMessage("Select the (data) source");
	}

	@Override
	public void createControl(Composite parent) {		
		Composite container = new Composite(parent, SWT.NONE);
		setControl(container);				
		container.setLayout(new GridLayout(3, false));
		
		Label lblInstalledImportTools = new Label(container, SWT.NONE);
		lblInstalledImportTools.setText("Source :");
		
		combo = new Combo(container, SWT.READ_ONLY);
		int i = extensionElements.size() > 1 ? 1 : 2;		
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, i, 1));
		
		// the 'Confirm' button is only relevant if more than 1 import tool is
		// defined; we use this button to enable the 'Next (page)' button - once
		// this button is enabled, the next pages for the wizard will be added
		// so this is a one time operation with no way back
		if (extensionElements.size() > 1) {
			btnConfirm = new Button(container, SWT.NONE);
			btnConfirm.setEnabled(false);
			btnConfirm.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					combo.setEnabled(false);
					btnConfirm.setEnabled(false);
					setPageComplete(true);
				}
			});
			btnConfirm.setText("Confirm");
		}
		
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Label lblDescription = new Label(container, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblDescription.setText("Description :");
		
		textDescription = new Text(container, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_text = new GridData(SWT.FILL, SWT.FILL, false, true, 2, 1);
		gd_text.widthHint = 300;
		textDescription.setLayoutData(gd_text);
		
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {				
				selectImportTool();
			}
		});
			
		if (extensionElements.size() > 1) {
			combo.add("[select a data source and press the 'Confirm' button]");
		}		
		for (ImportToolExtensionElement extensionElement : extensionElements) {
			combo.add(extensionElement.getSource());
		}
		combo.select(0);		
		
		if (extensionElements.size() > 1) {
			// if there is more than 1 import tool available, pressing the 
			// 'Confirm' button will mark the page as complete			
			textDescription.setText(CONFIRM_BUTTON_MESSAGE);
			setPageComplete(false);
		} else {
			// if there is only 1 import tool, there is no point in requiring
			// the 'Confirm' button to be pressed								
			extensionElement = extensionElements.get(0);
			textDescription.setText(extensionElement.getDescription());			
			setPageComplete(true);		
		}
			
	}

	public ImportToolExtensionElement getExtensionElement() {
		return extensionElement;
	}	

	private void selectImportTool() {		
		
		int i = combo.getSelectionIndex();
		if (extensionElements.size() == 1) {
			extensionElement = extensionElements.get(0);
		} else if (i > 0) {
			extensionElement = extensionElements.get(i - 1);
			btnConfirm.setEnabled(true);
			textDescription.setText(extensionElement.getDescription());
		} else {
			extensionElement = null;
			textDescription.setText(CONFIRM_BUTTON_MESSAGE);
			btnConfirm.setEnabled(false);
		}		
		
	}	
	
}