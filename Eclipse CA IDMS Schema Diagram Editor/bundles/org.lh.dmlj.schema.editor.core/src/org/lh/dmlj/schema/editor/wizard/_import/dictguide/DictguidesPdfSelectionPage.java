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
package org.lh.dmlj.schema.editor.wizard._import.dictguide;

import java.io.File;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.dictguide.DictguidesRegistry;
import org.lh.dmlj.schema.editor.log.Logger;
import org.lh.dmlj.schema.editor.service.ServicesPlugin;
import org.lh.dmlj.schema.editor.service.api.IPdfExtractorService;

public class DictguidesPdfSelectionPage extends WizardPage {	
	private static final String CA_IDMS_DICTIONARY_STRUCTURE_REFERENCE = "CA IDMS Dictionary Structure Reference";
	private static final String CA_IDMS_SQL_REFERENCE = "CA IDMS SQL Reference";
	
	static final String MANUAL_TYPE_DICTIONARY_STRUCTURE_REFERENCE_GUIDE = "Dictionary Structure Reference Guide";
	static final String MANUAL_TYPE_SQL_REFERENCE_GUIDE = "SQL Reference Guide";
	
	private static final Logger logger = Logger.getLogger(Plugin.getDefault());
	
	private final String manualType;
	private final String description;
	private Label lblTitle;
	private String licenseName;
	private String licenseText;
	private String[] manualTypeTokens;
	private Text textFile;	
	
	private static String getTitle(File file) {
		var title = new String[1];
		// go parse the .pdf document to get the title combined with the release information if available while
		// showing the busy cursor
		BusyIndicator.showWhile(Display.getCurrent(), () -> title[0] = DictguidesRegistry.getInstance().getDocumentTitle(file));
		return title[0];
	}	
		
	public DictguidesPdfSelectionPage(String manualType, String description) {
		super("wizardPage");
		this.manualType = manualType;
		this.description = description;
		manualTypeTokens = manualType.split(" ");
		setMessage("Select the .pdf file containing the " + manualType);
		setTitle("CA IDMS/DB Dictionary Structure and SQL Reference Guides");		
	}
	
	public void createControl(Composite parent) {
		var container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(3, false));
		
		var lblpdfFile = new Label(container, SWT.NONE);
		lblpdfFile.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblpdfFile.setText(".pdf File :");
		
		textFile = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		textFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		var btnBrowse = new Button(container, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				var fileDialog = new FileDialog(container.getShell());
				fileDialog.setFileName(textFile.getText());
				var newValue = fileDialog.open();							
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
		new Label(container, SWT.NONE);
		
		var lblPdfExtractorServiceDescription = new Label(container, SWT.NONE);
		lblPdfExtractorServiceDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		lblPdfExtractorServiceDescription.setText("(PDF Extractor Service description)");
		new Label(container, SWT.NONE);
		
		var linkLicense = new Link(container, SWT.WRAP);
		linkLicense.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 2, 1));
		linkLicense.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new ViewLicenseDialog(Display.getCurrent().getActiveShell(), licenseName, licenseText).open();
			}
		});
		new Label(container, SWT.NONE);
		
		lblTitle = new Label(container, SWT.NONE);
		lblTitle.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		var gdLblTitle = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gdLblTitle.verticalIndent = 20;
		lblTitle.setLayoutData(gdLblTitle);
		new Label(container, SWT.NONE);
		
		var textDescription = new Text(container, SWT.READ_ONLY | SWT.WRAP | SWT.MULTI);
		var gdText = new GridData(SWT.FILL, SWT.FILL, false, true, 2, 1);
		gdText.verticalIndent = 20;
		gdText.widthHint = 300;
		textDescription.setLayoutData(gdText);
		textDescription.setText(description);
		
		var pdfExtractorService = ServicesPlugin.getDefault().getService(IPdfExtractorService.class);
		btnBrowse.setEnabled(pdfExtractorService != null);
		
		if (pdfExtractorService == null) {
			setErrorMessage("PDF Extractor Service is NOT available");
		} else {
			var licensedProductName = pdfExtractorService.getLicensedProductName();
			var licensedProductVersion = pdfExtractorService.getLicensedProductVersion();
			licenseName = pdfExtractorService.getLicenseName();
			licenseText = pdfExtractorService.getLicenseText();
			if (licensedProductName != null) {
				var p = "PDF content is extracted using " + licensedProductName + " version " + licensedProductVersion +".";
				lblPdfExtractorServiceDescription.setText(p);
				// make sure the required license is also copied in the feature project USING THE RIGHT FILE NAME
				linkLicense.setText(String.format("""
						%s version %s is protected by the <a>%s</a>.
						This license is distributed with the CA IDMS/DB Schema Diagram Editor
						(this product); see the '%s.txt' file in your Eclipse
						installation's 'features/org.lh.dmlj.schema.editor_x.y.z.qualifier' folder
						or use the above link to read this license.\
						""", licensedProductName, licensedProductVersion, licenseName, licenseName));
			} else {
				lblPdfExtractorServiceDescription.setVisible(false);
				linkLicense.setVisible(false);
			}
		}
		setPageComplete(false);
	}
	
	public File getRefGuideFile() {
		return new File(textFile.getText().trim());
	}

	public String getRefGuideTitle() {
		return lblTitle.getText();
	}

	private boolean isTitleOK(String title) {
		if (manualType.equals(MANUAL_TYPE_DICTIONARY_STRUCTURE_REFERENCE_GUIDE) && title.equals(CA_IDMS_DICTIONARY_STRUCTURE_REFERENCE) ||
			manualType.equals(MANUAL_TYPE_SQL_REFERENCE_GUIDE) &&title.equals(CA_IDMS_SQL_REFERENCE)) {
			
			return true;
		} else {
			var i = 0;
			for (var token : manualTypeTokens) {
				i = title.indexOf(token, i);
				if (i < 0) {
					return false;
				}
			}
			return true;
		}
	}
	
	private boolean isVersionOK(String title) {
		return title.equals(CA_IDMS_DICTIONARY_STRUCTURE_REFERENCE) || title.equals(CA_IDMS_SQL_REFERENCE) ||
			   title.endsWith(" (r16 SP2)") || title.endsWith(" (r17)") || title.endsWith(" (Version 18.0.00)") ||
			   title.endsWith(" (Release 18.5.00)") || title.endsWith(" (Release 18.5.00, 2nd Edition)");
	}

	private void validatePage() {
		setErrorMessage(null);
		lblTitle.setText("");
		
		if (textFile.getText().trim().equals("")) {
			setPageComplete(false);
			return;
		}
		
		var pageComplete = true;
		
		var file = new File(textFile.getText().trim());
		if (!file.exists()) {
			setErrorMessage("File does not exist");
			pageComplete = false;
		}
	
		try {			
			var title = getTitle(file);					
			if (!isTitleOK(title)) {
				setErrorMessage("Not a " + manualType);
				pageComplete = false;
			} else if (!isVersionOK(title)) {				
				setErrorMessage("The version of the manual you selected is not supported; you can try to proceed but results may be unpredictable");
			}
			lblTitle.setText(title);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setErrorMessage("Not a " + manualType);
			pageComplete = false;
		}		
		setPageComplete(pageComplete);
	}
}
