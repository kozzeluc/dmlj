package org.lh.dmlj.schema.editor.wizard._import.dictguide;

import java.io.File;
import java.util.MissingResourceException;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.lh.dmlj.schema.editor.Plugin;
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
			description = Plugin.getDefault()
						 		.getPluginProperties()
						 		.getString(KEY_DESCRIPTION_DICTIONARY_STRUCTURE);
		} catch (MissingResourceException e) {
			description = "";;
		}		
		dictionaryStructurePdfSelectionPage =
			new DictguidesPdfSelectionPage("Dictionary Structure Reference Guide", 
										   description);		
		addPage(dictionaryStructurePdfSelectionPage);
		
		// create and add the SQL reference guide selection page		
		try {
			description = Plugin.getDefault()
						 		.getPluginProperties()
						 		.getString(KEY_DESCRIPTION_SQL);
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
		Runnable runnable = new Runnable() {
			public void run() {		
				DictguidesRegistry.INSTANCE
								  .createEntry(dictionaryStructureFile,
										  	   dictionaryStructureTitle,
										  	   sqlFile,
										  	   sqlTitle,
										  	   id,
										  	   defaultForInfoTab);						
			}
		};
		
		// have the DictguidesRegistry instance create the .zip file while 
		// the busy cursor is shown
		BusyIndicator.showWhile(Display.getCurrent(), runnable);		
		
		return true;
	}

}
