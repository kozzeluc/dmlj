/**
 * Copyright (C) 2019  Luc Hermans
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
import java.util.MissingResourceException;
import java.util.Stack;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.PluginPropertiesCache;
import org.lh.dmlj.schema.editor.dictguide.DictguidesRegistry;

public class DictguidesImportWizard extends Wizard implements IImportWizard {	

	private static final String KEY_DESCRIPTION_DICTIONARY_STRUCTURE = 
		"description.dicionary.structure";
	private static final String KEY_DESCRIPTION_SQL = "description.sql";	
	
	private boolean 				   calledFromPreferences = false;
	private DictguidesPdfSelectionPage dictionaryStructurePdfSelectionPage;
	private File 					   dictionaryStructureFile;
	private String 					   dictionaryStructureTitle;
	private DictguidesPdfSelectionPage sqlPdfSelectionPage;
	private File 					   sqlFile;
	private String 					   sqlTitle;
	private DictguidesSummaryPage 	   summaryPage;
	
	public DictguidesImportWizard() {
		new DictguidesImportWizard(false);
	}
	
	public DictguidesImportWizard(boolean calledFromPreferences) {
		super();
		this.calledFromPreferences = calledFromPreferences;
		setWindowTitle("Import");
	}	
	
	@Override
	public void addPages() {
		
		// create and add the dictionary structure ref. guide selection page
		String description;
		try {
			description = 
				PluginPropertiesCache.get(Plugin.getDefault(), KEY_DESCRIPTION_DICTIONARY_STRUCTURE);
		} catch (MissingResourceException e) {
			description = "";;
		}		
		dictionaryStructurePdfSelectionPage =
			new DictguidesPdfSelectionPage("Dictionary Structure Reference Guide", 
										   description);		
		addPage(dictionaryStructurePdfSelectionPage);
		
		// create and add the SQL reference guide selection page		
		try {
			description = PluginPropertiesCache.get(Plugin.getDefault(), KEY_DESCRIPTION_SQL);
		} catch (MissingResourceException e) {
			description = "";;
		}
		sqlPdfSelectionPage =
			new DictguidesPdfSelectionPage("SQL Reference Guide", description);		
		addPage(sqlPdfSelectionPage);
		
		// create and add the Summary page
		summaryPage = new DictguidesSummaryPage(calledFromPreferences);
		addPage(summaryPage);
		
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page == dictionaryStructurePdfSelectionPage) {
			dictionaryStructureFile = 
				dictionaryStructurePdfSelectionPage.getRefGuideFile();
			dictionaryStructureTitle = 
				dictionaryStructurePdfSelectionPage.getRefGuideTitle();
			return sqlPdfSelectionPage;
		} else if (page == sqlPdfSelectionPage) {
			sqlFile = sqlPdfSelectionPage.getRefGuideFile();
			sqlTitle = sqlPdfSelectionPage.getRefGuideTitle();
			summaryPage.setDictStructureRefGuide(dictionaryStructureTitle);
			summaryPage.setSqlRefGuide(sqlTitle);
			return summaryPage;
		}
		return null;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {		
	}

	@Override
	public boolean performFinish() {
		
		final String id = summaryPage.getId();
		final boolean defaultForInfoTab = summaryPage.isDefaultForInfoTab();
		
		// create a Runnable so that we can show the busy pointer when the 
		// DictguidesRegistry instance creates the .zip file, this can take a
		// few seconds
		Stack<Throwable> errors = new Stack<>();
		Runnable runnable = new Runnable() {
			public void run() {		
				try {
					DictguidesRegistry.INSTANCE
									  .createEntry(dictionaryStructureFile,
											  	   dictionaryStructureTitle,
											  	   sqlFile,
											  	   sqlTitle,
											  	   id,
											  	   defaultForInfoTab,
											  	   Plugin.getDefault().createTmpFolder());
				} catch (Throwable t) {
					errors.push(t);
				}
			}
		};
		
		// have the DictguidesRegistry instance create the .zip file while 
		// the busy cursor is shown
		BusyIndicator.showWhile(Display.getCurrent(), runnable);
		
		if (!errors.isEmpty()) {
			MessageDialog.openError(getShell(), "Error", errors.pop().getMessage());
			return false;
		} else {
			return true;
		}
	}

}
