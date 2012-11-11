package org.lh.dmlj.schema.editor.importtool.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
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
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.importtool.AbstractDataEntryPage;
import org.lh.dmlj.schema.editor.importtool.AbstractRecordLayoutManager;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;
import org.lh.dmlj.schema.editor.importtool.IDataEntryPageController;
import org.lh.dmlj.schema.editor.importtool.ISchemaImportTool;
import org.lh.dmlj.schema.editor.importtool.annotation.Context;
import org.lh.dmlj.schema.editor.importtool.annotation.Controller;

public class ImportWizard extends Wizard implements IImportWizard {

	private ImportToolDescriptor 	   	  activeImportToolDescriptor;	
	private IDataEntryContext 		  	  context = 
		new DataEntryContext();
	private List<AbstractDataEntryPage>   dataEntryPages;	
	private List<ImportToolDescriptor> 	  importToolDescriptors = 
		new ArrayList<>();
	private ImportToolSelectionPage    	  importToolSelectionPage;	
	private List<LayoutManagerDescriptor> layoutManagerDescriptors = 
		new ArrayList<>();
	private LayoutManagerSelectionPage	  layoutManagerSelectionPage;
	private OutputFileSelectionPage    	  outputFileSelectionPage;
	private IStructuredSelection 	   	  selection;
	private List<ImportWizardPage>	   	  importWizardPages;
	
	public ImportWizard() {
		super();
		setWindowTitle("Import");
	}

	@Override
	public void addPages() {
		
		importToolSelectionPage = 
			new ImportToolSelectionPage(importToolDescriptors);
		addPage(importToolSelectionPage);
		
		outputFileSelectionPage = new OutputFileSelectionPage(selection);
		addPage(outputFileSelectionPage);		
		
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page == importToolSelectionPage) {
			
			ImportToolDescriptor importToolDescriptor =
				importToolSelectionPage.getImportToolDescriptor();			
			
			if (dataEntryPages == null) {
				
				dataEntryPages = new ArrayList<>();
				importWizardPages = new ArrayList<>();
				
				activeImportToolDescriptor =
					importToolSelectionPage.getImportToolDescriptor();
				List<DataEntryPageDescriptor> pageDescriptors = 
					activeImportToolDescriptor.getPageDescriptors();
				for (DataEntryPageDescriptor pageDescriptor : pageDescriptors) {
					
					AbstractDataEntryPage dataEntryPage =
						pageDescriptor.createDataEntryPage();
					dataEntryPages.add(dataEntryPage);
					
					final ImportWizardPage fimportWizardPage = 
						new ImportWizardPage(dataEntryPage, 
											 pageDescriptor.getName(),
											 pageDescriptor.getMessage());
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
			
			outputFileSelectionPage.setImportToolDescriptor(importToolDescriptor);
			return outputFileSelectionPage;
			
		} else if (page == outputFileSelectionPage) {			
			
			if (!importWizardPages.get(0).isRelevant()) {
				String message = "The first page in import contributions " +
								 "must ALWAYS be relevant.\n\nplug-in: " +
								 activeImportToolDescriptor.getPluginId() + 
								 "\nimportTool: " +
								 activeImportToolDescriptor.getName();
				throw new RuntimeException(message);
			}
			importWizardPages.get(0).aboutToShow();
			return importWizardPages.get(0);
			
		} else if (importWizardPages.contains(page)) {
			
			int i = importWizardPages.indexOf(page);
			if (i < 0) {
				throw new RuntimeException("logic error");
			} else {
				ImportWizardPage nextPage = getNextRelevantPageIndex(i);
				if (nextPage != null) {
					nextPage.aboutToShow();
					return nextPage;
				} else {
					List<LayoutManagerDescriptor> suitableLlayoutManagers = 
						new ArrayList<>();
					for (LayoutManagerDescriptor descriptor :
						 layoutManagerDescriptors) {
						
						String schemaName = 
							context.getAttribute(IDataEntryContext.SCHEMA_NAME);
						short schemaVersion = 
							((Short) context.getAttribute(IDataEntryContext.SCHEMA_VERSION))
							.shortValue();
						if (descriptor.isValidFor(schemaName, schemaVersion)) {
							suitableLlayoutManagers.add(descriptor);
						}
					}
					layoutManagerSelectionPage.setLayoutManagerDescriptors(suitableLlayoutManagers);
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
		
		// build the list of import tool and layout manager descriptors...
		IExtension[] extensions = 
			Platform.getExtensionRegistry()
					.getExtensionPoint(Plugin.PLUGIN_ID, 
									   ExtensionPointConstants.EXTENSION_POINT_ID)
					.getExtensions();
		// ... by traversing all defined extensions in all installed plug-ins:
		for (IExtension extension : extensions) {	
			// for each defined extension, walk the list of elements and process 
			// the (only) one applicable to import tools and layout managers 
			// (there shouldn't be any others but if there are, ignore them)
			for (IConfigurationElement configElement : 
				 extension.getConfigurationElements()) {	
				
				if (configElement.getName()
								 .equals(ExtensionPointConstants.ELEMENT_IMPORT_TOOLS)) {
					
					// process the import tools defined in this extension, 1 by 
					// 1...
					for (IConfigurationElement tool : 
						 configElement.getChildren(ExtensionPointConstants.ELEMENT_IMPORT_TOOL)) {
						
						// instantiate an import tool descriptor and add it to 
						// our list
						ImportToolDescriptor importToolDescriptor =
							new ImportToolDescriptor(extension, tool);						
						importToolDescriptors.add(importToolDescriptor);
						
						// now traverse all data entry pages; these are defined
						// as children of the import tool element
						for (IConfigurationElement page : 
							 tool.getChildren(ExtensionPointConstants.ELEMENT_DATA_ENTRY_PAGE)) {
						
							// instantiate a data entry page and add it to the
							// import tool descriptor's list of data entry pages
							DataEntryPageDescriptor dataEntryPageDescriptor = 
								new DataEntryPageDescriptor(page);
							importToolDescriptor.getPageDescriptors()
												.add(dataEntryPageDescriptor);						
						}
												
					}
				} else if (configElement.getName()
						 				.equals(ExtensionPointConstants.ELEMENT_LAYOUT_MANAGERS)) {
					
					// process the layout managers defined in this extension, 1 
					// by 1...
					for (IConfigurationElement tool : 
						 configElement.getChildren(ExtensionPointConstants.ELEMENT_LAYOUT_MANAGER)) {
						
						// instantiate a layout manager descriptor and add it to 
						// our list
						LayoutManagerDescriptor layoutManagerDescriptor =
							new LayoutManagerDescriptor(extension, tool);						
						layoutManagerDescriptors.add(layoutManagerDescriptor);						
						
						// get the schemas to which the layout manager applies;
						// if nothing is configured, then the layout manager is
						// valid for ALL schemas
						for (IConfigurationElement validFor : 
							 tool.getChildren(ExtensionPointConstants.ELEMENT_VALID_FOR)) {
						
							ValidForDescriptor validForDescriptor =
								new ValidForDescriptor(validFor);
							layoutManagerDescriptor.getValidForDescriptors()
												   .add(validForDescriptor);
						}						
						
					}					
				}
				// we're done since there can only be 1 import tools element in
				// each extension
				break;
			}			
		}		
	}

	@Override
	public boolean performFinish() {		
		
		// get a hold of the import tool and the parameters configured for it
		// in the defining extension
		ISchemaImportTool importTool = 
			activeImportToolDescriptor.getSchemaImportTool();
		Properties importToolParms = 
			activeImportToolDescriptor.getParameters();	
		
		// create the import tool proxy
		final SchemaImportToolProxy proxy = 
			new SchemaImportToolProxy(importTool, context, importToolParms);					
		
		// get a hold of the record layout manager and its (optional) parameters
		final AbstractRecordLayoutManager recordLayoutManager = 
			layoutManagerSelectionPage.getLayoutManagerDescriptor()
									  .getLayoutManager();
		final Properties layoutManagerParms = 
			layoutManagerSelectionPage.getLayoutManagerDescriptor()
									  .getParameters();		
		
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