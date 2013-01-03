package org.lh.dmlj.schema.editor.wizard;

import static org.lh.dmlj.schema.editor.extension.ExtensionPointConstants.ELEMENT_IMPORT_TOOL;
import static org.lh.dmlj.schema.editor.extension.ExtensionPointConstants.ELEMENT_IMPORT_TOOLS;
import static org.lh.dmlj.schema.editor.extension.ExtensionPointConstants.ELEMENT_LAYOUT_MANAGER;
import static org.lh.dmlj.schema.editor.extension.ExtensionPointConstants.ELEMENT_LAYOUT_MANAGERS;
import static org.lh.dmlj.schema.editor.extension.ExtensionPointConstants.EXTENSION_POINT_IMPORT_ID;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.editor.extension.DataEntryPageExtensionElement;
import org.lh.dmlj.schema.editor.extension.ExtensionElementFactory;
import org.lh.dmlj.schema.editor.extension.ImportToolExtensionElement;
import org.lh.dmlj.schema.editor.extension.LayoutManagerExtensionElement;
import org.lh.dmlj.schema.editor.importtool.AbstractDataEntryPage;
import org.lh.dmlj.schema.editor.importtool.AbstractRecordLayoutManager;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;
import org.lh.dmlj.schema.editor.importtool.IDataEntryPageController;
import org.lh.dmlj.schema.editor.importtool.ISchemaImportTool;
import org.lh.dmlj.schema.editor.importtool.annotation.Context;
import org.lh.dmlj.schema.editor.importtool.annotation.Controller;
import org.lh.dmlj.schema.editor.importtool.impl.DataEntryContext;
import org.lh.dmlj.schema.editor.importtool.impl.LayoutManager;
import org.lh.dmlj.schema.editor.importtool.impl.SchemaImportToolProxy;

public class ImportWizard extends Wizard implements IImportWizard {

	private ImportToolExtensionElement 	  	 	activeImportToolExtensionElement;	
	private IDataEntryContext 		  	  	 	context = 
		new DataEntryContext();
	private List<AbstractDataEntryPage>   	 	dataEntryPages;	
	private List<ImportToolExtensionElement> 	importToolExtensionElements;
	private ImportToolSelectionPage    	  	 	importToolSelectionPage;	
	private List<ImportWizardPage>	   	  		importWizardPages;
	private List<LayoutManagerExtensionElement> layoutManagerExtensionElements;
	private LayoutManagerSelectionPage	  		layoutManagerSelectionPage;
	private OutputFileSelectionPage    	  		outputFileSelectionPage;
	private IStructuredSelection 	   	  		selection;
	
	public ImportWizard() {
		super();
		setWindowTitle("Import");
	}

	@Override
	public void addPages() {
		
		importToolSelectionPage = 
			new ImportToolSelectionPage(importToolExtensionElements);
		addPage(importToolSelectionPage);
		
		outputFileSelectionPage = new OutputFileSelectionPage(selection);
		addPage(outputFileSelectionPage);		
		
	}
	
	@Override
	public boolean canFinish() {
		if (importWizardPages == null) {
			// avoid the 'Finish' button to be enabled too soon
			return false;
		} else {
			return super.canFinish();
		}
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page == importToolSelectionPage) {
			
			ImportToolExtensionElement importToolExtensionElement =
				importToolSelectionPage.getExtensionElement();			
			
			if (dataEntryPages == null) {				
				
				dataEntryPages = new ArrayList<>();
				importWizardPages = new ArrayList<>();
				
				activeImportToolExtensionElement =
					importToolSelectionPage.getExtensionElement();
				List<DataEntryPageExtensionElement> dataEntryPageExtensionElements = 
					activeImportToolExtensionElement.getDataEntryPageExtensionElements();
				for (DataEntryPageExtensionElement dataEntryPageExtensionElement : 
					 dataEntryPageExtensionElements) {
					
					AbstractDataEntryPage dataEntryPage =
						dataEntryPageExtensionElement.createDataEntryPage();
					dataEntryPages.add(dataEntryPage);
					
					final ImportWizardPage fimportWizardPage = 
						new ImportWizardPage(dataEntryPage, 
											 dataEntryPageExtensionElement.getName(),
											 dataEntryPageExtensionElement.getMessage());
					// inject the context in the field annotated with @Context:
					inject(AbstractDataEntryPage.class, 
						   dataEntryPage, Context.class, context);
					IDataEntryPageController controller = 
						new IDataEntryPageController() {
							@Override
							public void setErrorMessage(String message) {
								fimportWizardPage.setErrorMessage(message);
							}
							@Override
							public void setPageComplete(boolean pageComplete) {								
								fimportWizardPage.setPageComplete(pageComplete);
							}						
						};
					// inject the controller in the field annotated with 
					// @Controller
					inject(AbstractDataEntryPage.class, dataEntryPage, 
						   Controller.class, controller);					
					importWizardPages.add(fimportWizardPage);
					
					addPage(fimportWizardPage);
				}
				
				layoutManagerSelectionPage = 
					new LayoutManagerSelectionPage();
				addPage(layoutManagerSelectionPage);
				
			}
			
			outputFileSelectionPage.setImportToolExtensionElement(importToolExtensionElement);
			return outputFileSelectionPage;
			
		} else if (page == outputFileSelectionPage) {			
			
			if (!importWizardPages.get(0).isRelevant()) {
				String message = "The first page in import contributions " +
								 "must ALWAYS be relevant.\n\nplug-in: " +
								 activeImportToolExtensionElement.getPluginId() + 
								 "\nimportTool: " +
								 activeImportToolExtensionElement.getName();
				throw new RuntimeException(message);
			}
			importWizardPages.get(0).aboutToShow();
			return importWizardPages.get(0);
			
		} else if (importWizardPages != null &&
				   importWizardPages.contains(page)) {
			
			int i = importWizardPages.indexOf(page);
			if (i < 0) {
				throw new RuntimeException("logic error");
			} else {
				ImportWizardPage nextPage = getNextRelevantPageIndex(i);
				if (nextPage != null) {
					nextPage.aboutToShow();
					return nextPage;
				} else {
					List<LayoutManagerExtensionElement> suitableLayoutManagers = 
						new ArrayList<>();
					for (LayoutManagerExtensionElement extensionElement :
						 layoutManagerExtensionElements) {
						
						String schemaName = 
							context.getAttribute(IDataEntryContext.SCHEMA_NAME);
						short schemaVersion = 
							((Short) context.getAttribute(IDataEntryContext.SCHEMA_VERSION))
							.shortValue();
						if (extensionElement.isValidFor(schemaName, schemaVersion)) {
							suitableLayoutManagers.add(extensionElement);
						}
					}
					layoutManagerSelectionPage.setExtensionElements(suitableLayoutManagers);
					return layoutManagerSelectionPage;
				}
			}			
			
		}
		return null;
	}
	
	private <T extends Annotation> void inject(Class<?> annotatedClass,
											   Object target,
											   Class<T> annotationClass, 
											   Object value) {
		
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

	private ImportWizardPage getNextRelevantPageIndex(int i) {
		for (int j = i + 1; j < importWizardPages.size(); j++) {
			if (importWizardPages.get(j).isRelevant()) {
				return importWizardPages.get(j);
			}
		}		
		return null;
		
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {		
		
		this.selection = selection;
		
		// build the list of import tool extension elements...
		importToolExtensionElements = 
			ExtensionElementFactory.getExtensionElements(EXTENSION_POINT_IMPORT_ID, 
														 ELEMENT_IMPORT_TOOLS, 
														 ELEMENT_IMPORT_TOOL, 
														 ImportToolExtensionElement.class);
		
		// build the list of layout manager extension elements...
		layoutManagerExtensionElements = 
			ExtensionElementFactory.getExtensionElements(EXTENSION_POINT_IMPORT_ID, 
														 ELEMENT_LAYOUT_MANAGERS, 
														 ELEMENT_LAYOUT_MANAGER, 
														 LayoutManagerExtensionElement.class);		
	}

	@Override
	public boolean performFinish() {		
		
		// get a hold of the import tool and the parameters configured for it
		// in the defining extension
		ISchemaImportTool importTool = 
			activeImportToolExtensionElement.getSchemaImportTool();
		Properties importToolParms = 
			activeImportToolExtensionElement.getParameters();	
		
		// create the import tool proxy
		final SchemaImportToolProxy proxy = 
			new SchemaImportToolProxy(importTool, context, importToolParms);					
		
		// get a hold of the record layout manager and its (optional) parameters
		final AbstractRecordLayoutManager recordLayoutManager = 
			layoutManagerSelectionPage.getExtensionElement().getLayoutManager();
		final Properties layoutManagerParms = 
			layoutManagerSelectionPage.getExtensionElement().getParameters();		
		
		// populate the schema and persist it to the file specified by the user;
    	// do the work within an operation.		
		IPath fullPath = new Path(outputFileSelectionPage.getOutputFile()
														 .getAbsolutePath());
		IPath workspacePath = 
			ResourcesPlugin.getWorkspace().getRoot().getLocation();
		String p = fullPath.toString()
						   .substring(workspacePath.toString().length());
		IPath path = new Path(p);		
		final IFile modelFile = 
			ResourcesPlugin.getWorkspace().getRoot().getFile(path);		
		IWorkbench workbench = PlatformUI.getWorkbench();
		final IWorkbenchWindow workbenchWindow = 
			workbench.getActiveWorkbenchWindow();				
		WorkspaceModifyOperation operation =
			new WorkspaceModifyOperation() {
			
			@Override
			protected void execute(IProgressMonitor progressMonitor) {
				try {
											
					// create the schema and perform validations as we go
					Schema schema = proxy.invokeImportTool();					
					
					// create a layout manager and invoke its layout() method to 
					// set the diagram location for all records, system owners
					// and set labels - perform validations as we go
					LayoutManager layoutManager = 
						new LayoutManager(schema, recordLayoutManager, 
										  layoutManagerParms);
					layoutManager.layout();					
					
					// Create a resource set
					ResourceSet resourceSet = new ResourceSetImpl();

					// Get the URI of the model file.
					//
					URI fileURI = 
						URI.createPlatformResourceURI(modelFile.getFullPath().toString(), true);

					// Create a resource for this file.
					Resource resource = resourceSet.createResource(fileURI);

					// Add the initial model object to the contents.												
					resource.getContents().add(schema);						

					// Save the contents of the resource to the file system.
					resource.save(null);					
					
				} catch (Exception exception) {
					
					// try to delete the file created if it exists
					if (outputFileSelectionPage.getOutputFile().exists()) {
						boolean deleted = false;
						try {
							deleted = 
								outputFileSelectionPage.getOutputFile().delete();
						} catch (Throwable t) {
							deleted = false;							
						}
						if (!deleted) {
							String message = 
								"could not delete file " + 
								outputFileSelectionPage.getOutputFile()
													   .getAbsolutePath() +
								" after a failed CA IDMS/DB schema import";
							System.out.println(message);
						}
					}
					
					throw new RuntimeException(exception);
					
				}
				finally {
					progressMonitor.done();
				}
			}
		};		
			
		try {
			getContainer().run(false, false, operation);
		} catch (Throwable e) {
			e.printStackTrace();
			Throwable cause = e.getCause();
			if (cause != null) {
				// file niet in een project (folder) of project niet open
				MessageDialog.openError(workbenchWindow.getShell(), 
										"File Creation", cause.getMessage());
			} else {
				MessageDialog.openError(workbenchWindow.getShell(), 
										"File Creation", e.getMessage());
			}
			return false;
		}		
		
		// refresh the resource in the workspace to avoid 'Resource is out of 
		// sync with the file system' messages
		try {
			IResource resource = 
				ResourcesPlugin.getWorkspace()
							   .getRoot()
							   .findMember(modelFile.getFullPath());
			resource.refreshLocal(IResource.DEPTH_ZERO, null);
		} catch (Throwable e) {
			e.printStackTrace(); // just log whatever problem we encounter
		}
		
		// Select the new file resource in the current view.
		//		
		IWorkbenchPage page = workbenchWindow.getActivePage();
		final IWorkbenchPart activePart = page.getActivePart();
		if (activePart instanceof ISetSelectionTarget) {
			final ISelection targetSelection = 
				new StructuredSelection(modelFile);
			getShell().getDisplay().asyncExec
				(new Runnable() {
					 public void run() {
						 ((ISetSelectionTarget)activePart).selectReveal(targetSelection);
					 }
				 });
		}
		// Open an editor on the new file (this will only work if the file 
		// extension is .layout)
		try {
			page.openEditor(new FileEditorInput(modelFile),
							workbench.getEditorRegistry()
									 .getDefaultEditor(modelFile.getFullPath()
											 				    .toString())
											 				    .getId());					 	 
		} catch (PartInitException exception) {
			MessageDialog.openError(workbenchWindow.getShell(), "Open Editor", 
									exception.getMessage());
			return false;
		}
		
		// signal the success of the wizard operation...
		return true;		
		
	}
	
}