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
package org.lh.dmlj.schema.editor.preference;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.wb.swt.SWTResourceManager;
import org.lh.dmlj.schema.editor.dictguide.DictguidesRegistry;
import org.lh.dmlj.schema.editor.service.ServicesPlugin;
import org.lh.dmlj.schema.editor.service.api.IPdfExtractorService;
import org.lh.dmlj.schema.editor.wizard._import.dictguide.DictguidesImportWizard;

public class ReferenceGuidesPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	private static final String SEGOE_UI = "Segoe UI";
	
	private Button btnDelete;
	private List list;
	private Label lblDictionaryStructureguide;
	private Label lblSqlGuide;	
	
	public ReferenceGuidesPreferencePage() {
		setDescription("Reference Guide settings:");
	}	

	@Override
	public void init(IWorkbench workbench) {
		// init is called before createContents(Composite parent), so the controls are not yet created
	}

	@Override
	protected Control createContents(Composite parent) {
		var container = new Composite(parent, SWT.NONE);		
		var layout = new GridLayout(3, false);
		container.setLayout(layout);
		
		var lblNewLabel = new Text(container, SWT.READ_ONLY | SWT.WRAP | SWT.MULTI);
		var gdLblNewLabel = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
		gdLblNewLabel.widthHint = 150;
		lblNewLabel.setLayoutData(gdLblNewLabel);
		lblNewLabel.setText("Reference Guide combination to use in the \"Info\" tab (Properties view) :");
		
		list = new List(container, SWT.BORDER | SWT.V_SCROLL);
		var gdList = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 3);
		gdList.heightHint = 75;
		list.setLayoutData(gdList);
		new Label(container, SWT.NONE);
		
		var btnImport = new Button(container, SWT.NONE);
		btnImport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				importDictguide();
			}
		});
		var gdBtnImport = new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1);
		gdBtnImport.verticalIndent = 5;
		btnImport.setLayoutData(gdBtnImport);
		btnImport.setText("Import...");
		
		btnDelete = new Button(container, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteDictguide();
			}
		});
		btnDelete.setEnabled(false);
		btnDelete.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
		btnDelete.setText("Delete");
		new Label(container, SWT.NONE);
		lblDictionaryStructureguide = new Label(container, SWT.NONE);
		lblDictionaryStructureguide.setFont(SWTResourceManager.getFont(SEGOE_UI, 9, SWT.BOLD));
		var gdLblDictionaryStructureguide = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gdLblDictionaryStructureguide.horizontalIndent = 10;
		gdLblDictionaryStructureguide.verticalIndent = 5;
		lblDictionaryStructureguide.setLayoutData(gdLblDictionaryStructureguide);
		new Label(container, SWT.NONE);
		lblSqlGuide = new Label(container, SWT.NONE);
		var gdLblSqlGuide = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gdLblSqlGuide.horizontalIndent = 10;
		lblSqlGuide.setLayoutData(gdLblSqlGuide);
		lblSqlGuide.setFont(SWTResourceManager.getFont(SEGOE_UI, 9, SWT.BOLD));
		
		list.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setDocumentTitles();
			}
		});		
		
		var pdfExtractorService = ServicesPlugin.getDefault().getService(IPdfExtractorService.class);
		btnImport.setEnabled(pdfExtractorService != null);
		
		var txtNotePressing = new Text(container, SWT.READ_ONLY | SWT.WRAP);
		txtNotePressing.setText("Note: Pressing the \"Restore Defaults\" button below has NO impact on the above settings.");
		var gdTxtNotePressing = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
		gdTxtNotePressing.widthHint = 100;
		gdTxtNotePressing.verticalIndent = 20;
		txtNotePressing.setLayoutData(gdTxtNotePressing);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		if (pdfExtractorService == null) {
			var lblMsg = new Label(container, SWT.NONE);
			lblMsg.setFont(SWTResourceManager.getFont(SEGOE_UI, 9, SWT.BOLD));
			lblMsg.setForeground(ColorConstants.red);
			var gdLblMsg = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
			gdLblMsg.horizontalIndent = 10;
			gdLblMsg.verticalIndent = 15;
			lblMsg.setLayoutData(gdLblMsg);			
			lblMsg.setText("PDF Extractor Service is NOT available");
			new Label(container, SWT.NONE);
		}
		
		initialize();
		return container;
	}
	
	private void importDictguide() {
		var importWizard = new DictguidesImportWizard(true);
		var wizardDialog = new WizardDialog(getShell(), importWizard);
		wizardDialog.create();
		// we should move the wizard title to plugin.properties...
		wizardDialog.setTitle("CA IDMS/DB Dictionary Structure and SQL Reference Guides");
		Display.getCurrent().syncExec(wizardDialog::open);					
		list.removeAll();
		initialize();		
	}
	
	private void deleteDictguide() {
		DictguidesRegistry.getInstance().deleteEntry(list.getSelection()[0]);
		list.removeAll();
		initialize();		
	}

	private void initialize() {
		list.add("[none]"); // id will never exist because of "<" and ">"
		for (var id : DictguidesRegistry.getInstance().getAllIds()) {
			list.add(id);
		}
		var activeId = DictguidesRegistry.getInstance().getActiveId();
		if (activeId != null) {
			list.select(list.indexOf(activeId));
			var title = DictguidesRegistry.getInstance().getDictionaryStructureTitle(activeId);
			lblDictionaryStructureguide.setText(title);
			title = DictguidesRegistry.getInstance().getSqlTitle(activeId);
			lblSqlGuide.setText(title);
		} else {
			list.select(0);
		}
		setDocumentTitles();
		list.setFocus();
	}

	@Override
	protected void performApply() {
		storeValues();
	}
	
	@Override
	protected void performDefaults() {
		list.select(0);
		setDocumentTitles();
	}
	
	@Override
	public boolean performOk() {
		storeValues();
		return true;
	}
	
	protected void setDocumentTitles() {		
		if (list.getSelectionIndex() == 0) {
			lblDictionaryStructureguide.setText("");
			lblSqlGuide.setText("");
		} else {
			var id = list.getSelection()[0];
			var title = DictguidesRegistry.getInstance().getDictionaryStructureTitle(id);
			lblDictionaryStructureguide.setText(title);
			title = DictguidesRegistry.getInstance().getSqlTitle(id);
			lblSqlGuide.setText(title);
		}
		btnDelete.setEnabled(list.getSelectionIndex() > 0);
	}
	
	private void storeValues() {
		if (list.getSelectionIndex() == 0) {
			DictguidesRegistry.getInstance().setActiveId(null);
		} else {
			DictguidesRegistry.getInstance().setActiveId(list.getSelection()[0]);
		}
	}

}
