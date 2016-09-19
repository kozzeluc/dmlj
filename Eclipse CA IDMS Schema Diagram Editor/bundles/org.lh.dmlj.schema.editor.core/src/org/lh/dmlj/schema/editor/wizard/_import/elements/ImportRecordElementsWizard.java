/**
 * Copyright (C) 2016  Luc Hermans
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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.command.IModelChangeCommand;
import org.lh.dmlj.schema.editor.command.SwapRecordElementsCommandCreationAssistant;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.dsl.builder.model.RecordModelBuilder;
import org.lh.dmlj.schema.editor.extension.DataEntryPageExtensionElement;
import org.lh.dmlj.schema.editor.extension.ExtensionElementFactory;
import org.lh.dmlj.schema.editor.extension.RecordElementsImportToolExtensionElement;
import org.lh.dmlj.schema.editor.importtool.AbstractDataEntryPage;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;
import org.lh.dmlj.schema.editor.importtool.IDataEntryPageController;
import org.lh.dmlj.schema.editor.importtool.elements.IRecordElementsImportTool;
import org.lh.dmlj.schema.editor.wizard._import.ImportWizardPage;
import org.lh.dmlj.schema.editor.wizard._import.schema.Context;
import org.lh.dmlj.schema.editor.wizard._import.schema.Controller;
import org.lh.dmlj.schema.editor.wizard._import.schema.DataEntryContext;

public class ImportRecordElementsWizard extends Wizard implements IImportWizard {

	private RecordElementsImportToolExtensionElement activeRecordElementsImportToolExtensionElement;	
	private IDataEntryContext context = new DataEntryContext();
	private List<ImportWizardPage> dataEntryWizardPages;
	private List<RecordElementsImportToolExtensionElement> importToolExtensionElements;
	private ImportToolSelectionPage importToolSelectionPage;
	private SchemaRecord record;
	private boolean initOK;
	private PreviewPage previewPage;
	private RecordElementsImportToolProxy proxy;
	private IModelChangeCommand command;
	
	public ImportRecordElementsWizard(SchemaRecord record) {
		super();
		this.record = record;
		setWindowTitle("Import");
	}

	private void addDataEntryPages(RecordElementsImportToolExtensionElement extensionElement) {
		if (dataEntryWizardPages == null) {				
			
			// keep a reference to the active import tool extension element
			activeRecordElementsImportToolExtensionElement = extensionElement;
			
			// create the list of data entry wizard pages
			dataEntryWizardPages = new ArrayList<>();
			
			// deal with the pre options pages first
			List<DataEntryPageExtensionElement> dataEntryPagesElements = 
				activeRecordElementsImportToolExtensionElement.getDataEntryPageExtensionElements();
			for (DataEntryPageExtensionElement dataEntryPageExtensionElement : 
				 dataEntryPagesElements) {
					
				// create a data entry wizard page and add it to our list and to the wizard
				ImportWizardPage importWizardPage = 
					createImportWizardPage(dataEntryPageExtensionElement);					
				dataEntryWizardPages.add(importWizardPage);
				addPage(importWizardPage);
			}
		}
	}

	@Override
	public void addPages() {
		String dsl = Tools.generateRecordElementsDSL(record);
		importToolSelectionPage = new ImportToolSelectionPage(importToolExtensionElements, record, dsl);		
		addPage(importToolSelectionPage);
		previewPage = new PreviewPage(record);
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
		// create a controller and inject it in the data entry page's @Controller annotated field
		IDataEntryPageController controller = 
			new IDataEntryPageController() {
				@Override
				public void setErrorMessage(String message) {
					wizardPage.setErrorMessage(message);
				}
				@Override
				public void setPageComplete(boolean pageComplete) {								
					wizardPage.setPageComplete(pageComplete);
				}						
			};
		inject(AbstractDataEntryPage.class, wizardPage.getDataEntryPage(), Controller.class, 
			   controller);		
	}

	private ImportWizardPage createImportWizardPage(DataEntryPageExtensionElement configElement) {

		// create the data entry page
		AbstractDataEntryPage dataEntryPage = 
		configElement.createDataEntryPage();		
		
		// wrap the data entry page in a wizard page
		ImportWizardPage importWizardPage = 
			new ImportWizardPage(dataEntryPage, configElement.getName(), 
								 "Elements for Record " + record.getName(), configElement.getMessage());
		
		// inject the context in the data entry page's @Context annotated field 
		injectContext(dataEntryPage);		
		
		// create a controller and inject it in the data entry page's @Controller annotated field
		createAndInjectController(importWizardPage);
		
		// return the import wizard page
		return importWizardPage;
		
	}

	private void disposeImportTool() {
		if (proxy != null) {
			proxy.disposeImportTool();
		}
	}

	public IModelChangeCommand getCommand() {
		return command;
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page == importToolSelectionPage) {
			
			// The user has chosen an import tool and cannot change his/her selection; go get the 
			// data entry pages from the import tool extension element and add all import wizard 
			// pages.  Make sure we build and add the wizard pages only once.
			
			addDataEntryPages(importToolSelectionPage.getExtensionElement());
			
			// get a hold of the import tool and the parameters configured for it in the defining
			// extension and create the import tool proxy 
			IRecordElementsImportTool importTool = 
				activeRecordElementsImportToolExtensionElement.getRecordElementsImportTool();
			Properties importToolParms = activeRecordElementsImportToolExtensionElement.getParameters();
			proxy = new RecordElementsImportToolProxy(importTool, importToolParms);
			
			// return the first import tool data entry page or null if no data entry pages are
			// defined
			if (!dataEntryWizardPages.isEmpty()) {
				dataEntryWizardPages.get(0).aboutToShow();
				return dataEntryWizardPages.get(0);
			} else {
				return null;
			}		
		} else if (dataEntryWizardPages != null && dataEntryWizardPages.contains(page)) {
			// we need the next data entry page
			int i = dataEntryWizardPages.indexOf(page);
			if (i < 0) {
				throw new RuntimeException("logic error");
			} else {
				// get the next relevant data entry page, if any
				ImportWizardPage nextPage = getNextRelevantPageIndex(i);
				if (nextPage != null) {					
					// there is a next relevant data entry page, so return it
					nextPage.aboutToShow();
					return nextPage;					
				} else {
					// no next data entry page, prepare the preview page and return it					
					SchemaRecord record = new RecordModelBuilder().build("DUMMY");
					record.getRootElements().clear();
					record.getRootElements().addAll(proxy.invokeImportTool(context));
					String recordElementsDSL = Tools.generateRecordElementsDSL(record);									
					previewPage.setRecordElementsDSL(recordElementsDSL);
					return previewPage;
				}
			}
		}
		return null;
	}

	private ImportWizardPage getNextRelevantPageIndex(int i) {
		for (int j = i + 1; j < dataEntryWizardPages.size(); j++) {
			if (dataEntryWizardPages.get(j).isRelevant()) {
				return dataEntryWizardPages.get(j);
			}
		}		
		return null;	
	}	
	
	public String getRecordName() {
		if (initOK) {
			return Tools.removeTrailingUnderscore(record.getName());
		} else {
			throw new IllegalStateException("init() was NOT invoked");
		}
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		context.setAttribute(IDataEntryContext.CURRENT_SCHEMA_RECORD, record);
		importToolExtensionElements = 
			ExtensionElementFactory.getExtensionElements(EXTENSION_POINT_IMPORT_RECORD_ELEMENTS_ID, 
														 ELEMENT_IMPORT_TOOLS, 
														 ELEMENT_IMPORT_TOOL, 
														 RecordElementsImportToolExtensionElement.class);
		initOK = true;
	}
	
	private <T extends Annotation> void inject(Class<?> annotatedClass, Object target,
			   								   Class<T> annotationClass, Object value) {

		for (Field field : annotatedClass.getDeclaredFields()) {
			if (field.getAnnotation(annotationClass) != null) {
				field.setAccessible(true);
				try {
					field.set(target, value);					
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}						
		}		
	}

	private void injectContext(AbstractDataEntryPage dataEntryPage) {
		// inject the context in the data entry page's @Context annotated field
		inject(AbstractDataEntryPage.class, dataEntryPage, Context.class, context);		
	}

	@Override
	public boolean performCancel() {
		disposeImportTool();		
		return super.performCancel();
	}
	
	@Override
	public boolean performFinish() {
		
		IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress() {			
			@Override
			public void run(IProgressMonitor progressMonitor) {
				progressMonitor.beginTask("Import RecordElements", IProgressMonitor.UNKNOWN);								
				try {											
					List<Element> newRootElements = proxy.invokeImportTool(context);										
					ModelChangeContext context = 
						new ModelChangeContext(ModelChangeType.SWAP_RECORD_ELEMENTS);
					context.putContextData(record);
					command = SwapRecordElementsCommandCreationAssistant.getCommand(record, newRootElements);
					command.setContext(context);
				} catch (Throwable t) {
					throw new RuntimeException(t);					
				}
				finally {
					disposeImportTool();					
				}				
				progressMonitor.done();				
			}
		};		
		try {
			org.lh.dmlj.schema.editor.Plugin.getDefault().runWithOperationInProgressIndicator(runnableWithProgress);
		} catch (Throwable e) {
			e.printStackTrace();
			Throwable cause = e.getCause();
			if (cause != null) {
				MessageDialog.openError(Display.getCurrent().getActiveShell(), 
										"Edit Record Elements", cause.getMessage());
			} else {
				MessageDialog.openError(Display.getCurrent().getActiveShell(), 
										"Edit Record Elements", e.getMessage());
			}
			return false;
		}		
		
		return true;
	}

}
