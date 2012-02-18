package org.lh.dmlj.schema.editor.wizard;

import java.io.File;

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
import org.eclipse.ui.INewWizard;
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
import org.lh.dmlj.schema.editor.tool.SchemaImportTool;

import dmlj.core.IDatabase;


public class NewSchemaWizard extends Wizard implements INewWizard {

	private NewSchemaPage1 		 page1;
	private NewSchemaPage2 		 page2;
	private NewSchemaPage3 		 page3;
	private NewSchemaPage4 		 page4;	
	private NewSchemaPage5 		 page5;	
	private IStructuredSelection selection;
	
	public NewSchemaWizard() {
		super();
		setWindowTitle("New");
	}
	
	@Override
	public void addPages() {
		page1 = new NewSchemaPage1(selection);		
		addPage(page1);
		page2 = new NewSchemaPage2();		
		addPage(page2);
		page3 = new NewSchemaPage3();		
		addPage(page3);
		page4 = new NewSchemaPage4();								
		addPage(page4);
		page5 = new NewSchemaPage5();		
		addPage(page5);		
	}	
	
	@Override
	public boolean canFinish() {		
		return page1.getSchemaFile() != null &&
			   page2.getDictionary() != null &&
			   page3.getSchemaName() != null &&
			   (!page3.getSchemaName().equals("IDMSNTWK") ||
			    page3.getSchemaVersion().intValue() != 1 ||
			    !page4.getOptions()[2] ||
			    page5.getCatalog() != null);
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {				
		if (page == page1) {
			return page2;
		} else if (page == page2) {
			page3.setDictionary(page2.getDictionary());			
			return page3;
		} else if (page == page3) {
			// some pages apply only to IDMSNTWK version 1
			String schemaName = page3.getSchemaName();
			Integer schemaVersion = page3.getSchemaVersion();
			if (schemaName != null && schemaVersion != null &&
				schemaName.equals("IDMSNTWK") && 
				schemaVersion.intValue() == 1) {
																
				return page4;
			}
		} else if (page == page4) {
			boolean[] options = page4.getOptions();
			if (options[2]) {
				return page5;
			}
		}
		return null;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {		
		this.selection = selection;
	}	
	
	@Override
	public boolean performFinish() {		
    			
		// gather all information from the wizard pages...				
		File targetFile = page1.getSchemaFile();
		final IDatabase dictionary = page2.getDictionary();
		final String schemaName = page3.getSchemaName();
		final int schemaVersion = page3.getSchemaVersion();
		final boolean[] options;
		final IDatabase catalog;
		if (schemaName.equals("IDMSNTWK") && schemaVersion == 1) {
			options = page4.getOptions();
			if (options[2]) {
				catalog = page5.getCatalog();
			} else {
				catalog = null;
			}
		} else {
			options = new boolean[page4.getOptions().length];
			for (int i = 0; i < options.length; i++) {
				options[i] = false;
			}
			catalog = null;
		}		
						    	    	
		// create the the schema and persist it to the file specified by the 
    	// user; do the work within an operation.		
		IPath fullPath = new Path(targetFile.getAbsolutePath());
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
												
						// create the schema
						SchemaImportTool schemaImportTool =
							new SchemaImportTool(dictionary, schemaName, 
												 schemaVersion, options, 
												 catalog);						
						Schema schema = schemaImportTool.run();
						
						// Create a resource set
						//
						ResourceSet resourceSet = new ResourceSetImpl();

						// Get the URI of the model file.
						//
						URI fileURI = 
							URI.createPlatformResourceURI(modelFile.getFullPath().toString(), true);

						// Create a resource for this file.
						//
						Resource resource = resourceSet.createResource(fileURI);

						// Add the initial model object to the contents.
						//												
						resource.getContents().add(schema);						

						// Save the contents of the resource to the file system.
						//
						resource.save(null);
					} catch (Exception exception) {
						throw new RuntimeException(exception);
					}
					finally {
						progressMonitor.done();
					}
				}
			};
			
		// close dictionary and catalog (ignoring any errors)
		try {
			dictionary.close();
			catalog.close(); 	// possibly null, so possible NPE
		} catch (Throwable t) {			
		}
			
		try {
			getContainer().run(false, false, operation);
		} catch (Throwable e) {
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
