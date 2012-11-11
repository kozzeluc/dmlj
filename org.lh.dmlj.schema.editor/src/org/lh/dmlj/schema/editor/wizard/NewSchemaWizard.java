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
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.Ruler;
import org.lh.dmlj.schema.RulerType;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaFactory;


public class NewSchemaWizard extends Wizard implements INewWizard {

	private NewSchemaPage 		 page;
	private IStructuredSelection selection;
	
	public NewSchemaWizard() {
		super();
		setWindowTitle("New");
	}
	
	@Override
	public void addPages() {
		page = new NewSchemaPage(selection);		
		addPage(page);		
	}	
	
	@Override
	public boolean canFinish() {		
		return page.getSchemaFile() != null;
	}	

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {		
		this.selection = selection;
	}	
	
	@Override
	public boolean performFinish() {		
    			
		// gather all information from the wizard pages...				
		File targetFile = page.getSchemaFile();						
						    	    	
		// create the schema and persist it to the file specified by the 
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
												
						// create the schema and set its name and version
						Schema schema = SchemaFactory.eINSTANCE.createSchema();
						schema.setName("NEWSCHEM");
						schema.setVersion((short) 1);
						
						// diagram data with rulers (todo: set these attributes 
						// according to the editor's preferences)...
						DiagramData diagramData = 
							SchemaFactory.eINSTANCE.createDiagramData();
						schema.setDiagramData(diagramData);
						Ruler verticalRuler = 
							SchemaFactory.eINSTANCE.createRuler();
						verticalRuler.setType(RulerType.VERTICAL);
						diagramData.setVerticalRuler(verticalRuler);
						diagramData.getRulers()
								   .add(verticalRuler); // ruler container
						Ruler horizontalRuler = 
							SchemaFactory.eINSTANCE.createRuler();
						horizontalRuler.setType(RulerType.HORIZONTAL);
						diagramData.setHorizontalRuler(horizontalRuler);
						diagramData.getRulers()
								   .add(horizontalRuler); // ruler container						
						
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
