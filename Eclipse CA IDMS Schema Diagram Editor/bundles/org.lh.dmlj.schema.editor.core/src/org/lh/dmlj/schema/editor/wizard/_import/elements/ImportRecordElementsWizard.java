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

import static org.lh.dmlj.schema.editor.extension.ExtensionPointConstants.ELEMENT_IMPORT_TOOL;
import static org.lh.dmlj.schema.editor.extension.ExtensionPointConstants.ELEMENT_IMPORT_TOOLS;
import static org.lh.dmlj.schema.editor.extension.ExtensionPointConstants.EXTENSION_POINT_IMPORT_RECORD_ELEMENTS_ID;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.command.IModelChangeCommand;
import org.lh.dmlj.schema.editor.command.SwapRecordElementsCommandCreationAssistant;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.dsl.builder.model.RecordModelBuilder;
import org.lh.dmlj.schema.editor.extension.DataEntryPageExtensionElement;
import org.lh.dmlj.schema.editor.extension.ExtensionElementFactory;
import org.lh.dmlj.schema.editor.extension.RecordElementsImportToolExtensionElement;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;
import org.lh.dmlj.schema.editor.importtool.IDataEntryPageController;
import org.lh.dmlj.schema.editor.wizard._import.ImportWizardPage;
import org.lh.dmlj.schema.editor.wizard._import.schema.DataEntryContext;

public class ImportRecordElementsWizard extends Wizard implements IImportWizard {
	private final SchemaRecord schemaRecord;
	private final IDataEntryContext context = new DataEntryContext();
	private RecordElementsImportToolExtensionElement activeRecordElementsImportToolExtensionElement;
	private List<ImportWizardPage> dataEntryWizardPages;
	private List<RecordElementsImportToolExtensionElement> importToolExtensionElements;
	private ImportToolSelectionPage importToolSelectionPage;
	private boolean initOK;
	private PreviewPage previewPage;
	private RecordElementsImportToolProxy proxy;
	private IModelChangeCommand command;
	
	public ImportRecordElementsWizard(SchemaRecord schemaRecord) {
		this.schemaRecord = schemaRecord;
		setWindowTitle("Import");
	}

	private void addDataEntryPages(RecordElementsImportToolExtensionElement extensionElement) {
		if (dataEntryWizardPages == null) {			
			// keep a reference to the active import tool extension element
			activeRecordElementsImportToolExtensionElement = extensionElement;
			
			// create the list of data entry wizard pages
			dataEntryWizardPages = new ArrayList<>();
			
			// deal with the pre options pages first
			var dataEntryPagesElements = activeRecordElementsImportToolExtensionElement.getDataEntryPageExtensionElements();
			for (var dataEntryPageExtensionElement : dataEntryPagesElements) {
				// create a data entry wizard page and add it to our list and to the wizard
				var importWizardPage = createImportWizardPage(dataEntryPageExtensionElement);
				dataEntryWizardPages.add(importWizardPage);
				addPage(importWizardPage);
			}
		}
	}

	@Override
	public void addPages() {
		var dsl = Tools.generateRecordElementsDSL(schemaRecord);
		importToolSelectionPage = new ImportToolSelectionPage(importToolExtensionElements, schemaRecord, dsl);		
		addPage(importToolSelectionPage);
		previewPage = new PreviewPage(schemaRecord);
		addPage(previewPage);
	}
	
	@Override
	public boolean canFinish() {
		if (dataEntryWizardPages == null) {
			// avoid the 'Finish' button to be enabled too soon
			return false;
		} else {
			return super.canFinish();
		}
	}
	
	private void createAndInjectController(final ImportWizardPage wizardPage) {
		var controller = new IDataEntryPageController() {
			@Override
			public void setErrorMessage(String message) {
				wizardPage.setErrorMessage(message);
			}
			@Override
			public void setPageComplete(boolean pageComplete) {								
				wizardPage.setPageComplete(pageComplete);
			}						
		};
		wizardPage.getDataEntryPage().setController(controller);
	}

	private ImportWizardPage createImportWizardPage(DataEntryPageExtensionElement configElement) {
		// create the data entry page and wrap it in a wizard page
		var dataEntryPage = configElement.createDataEntryPage();
		var importWizardPage = new ImportWizardPage(dataEntryPage, configElement.getName(),
				"Elements for Record " + schemaRecord.getName(), configElement.getMessage());
		dataEntryPage.setContext(context);
		
		// create a controller and inject it in the data entry page's @Controller annotated field
		createAndInjectController(importWizardPage);
				
		return importWizardPage;
	}

	private void disposeImportTool() {
		if (proxy != null) {
			proxy.disposeImportTool();
		}
	}

	public IModelChangeCommand getCommand() {
		// note: the command is NOT yet executed, this is up to the caller of this method
		return command;
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page == importToolSelectionPage) {
			// The user has chosen an import tool and cannot change his/her selection; go get the data entry
			// pages from the import tool extension element and add all import wizard pages. Make sure we build
			// and add the wizard pages only once.
			
			addDataEntryPages(importToolSelectionPage.getExtensionElement());
			
			// get a hold of the import tool and the parameters configured for it in the defining extension and
			// create the import tool proxy 
			var importTool = activeRecordElementsImportToolExtensionElement.getRecordElementsImportTool();
			var importToolParms = activeRecordElementsImportToolExtensionElement.getParameters();
			proxy = new RecordElementsImportToolProxy(importTool, importToolParms);
			
			// return the first import tool data entry page or null if no data entry pages are defined
			if (!dataEntryWizardPages.isEmpty()) {
				dataEntryWizardPages.get(0).aboutToShow();
				return dataEntryWizardPages.get(0);
			} else {
				return null;
			}
		} else if (dataEntryWizardPages != null && dataEntryWizardPages.contains(page)) {
			// we need the next data entry page
			if (dataEntryWizardPages.contains(page)) {
				var nextPage = getNextRelevantPageIndex(dataEntryWizardPages.indexOf(page));
				if (nextPage != null) {					
					nextPage.aboutToShow();
					return nextPage;					
				} else {
					var dummyRecord = new RecordModelBuilder().build("DUMMY");
					dummyRecord.getRootElements().clear();
					dummyRecord.getRootElements().addAll(proxy.invokeImportTool(context));
					var recordElementsDSL = Tools.generateRecordElementsDSL(dummyRecord);									
					previewPage.setRecordElementsDSL(recordElementsDSL);
					return previewPage;
				}
			} else {
				throw new IllegalStateException("logic error");
			}
		}
		return null;
	}

	private ImportWizardPage getNextRelevantPageIndex(int currentPageIndex) {
		for (int nextPageIndex = currentPageIndex + 1; nextPageIndex < dataEntryWizardPages.size(); nextPageIndex++) {
			if (dataEntryWizardPages.get(nextPageIndex).isRelevant()) {
				return dataEntryWizardPages.get(nextPageIndex);
			}
		}		
		return null;	
	}	
	
	public String getRecordName() {
		if (initOK) {
			return Tools.removeTrailingUnderscore(schemaRecord.getName());
		} else {
			throw new IllegalStateException("init() was NOT invoked");
		}
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		context.setAttribute(IDataEntryContext.CURRENT_SCHEMA_RECORD, schemaRecord);
		importToolExtensionElements = ExtensionElementFactory.getExtensionElements(EXTENSION_POINT_IMPORT_RECORD_ELEMENTS_ID,
				ELEMENT_IMPORT_TOOLS, ELEMENT_IMPORT_TOOL, RecordElementsImportToolExtensionElement.class);
		initOK = true;
	}

	@Override
	public boolean performCancel() {
		disposeImportTool();		
		return super.performCancel();
	}
	
	@Override
	public boolean performFinish() {
		IRunnableWithProgress runnableWithProgress = progressMonitor -> {
			progressMonitor.beginTask("Import RecordElements", IProgressMonitor.UNKNOWN);								
			try {											
				var newRootElements = proxy.invokeImportTool(context);										
				var commandContext = new ModelChangeContext(ModelChangeType.SWAP_RECORD_ELEMENTS);
				commandContext.putContextData(schemaRecord);
				command = SwapRecordElementsCommandCreationAssistant.getCommand(schemaRecord, newRootElements);
				command.setContext(commandContext);
			} catch (Exception e) {
				throw new IllegalStateException(e);
			} finally {
				disposeImportTool();					
			}				
			progressMonitor.done();			
		};		
		try {
			Plugin.getDefault().runWithOperationInProgressIndicator(runnableWithProgress);
		} catch (Exception e) {
			// the plug-in's runWithOperationInProgressIndicator method has already logged the error
			var message = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Edit Record Elements", message);
			return false;
		}		
		return true;
	}

}
