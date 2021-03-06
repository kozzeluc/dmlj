/**
 * Copyright (C) 2018  Luc Hermans
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
package org.lh.dmlj.schema.editor.wizard;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
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
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.log.Logger;
import org.lh.dmlj.schema.editor.preference.PreferenceConstants;
import org.lh.dmlj.schema.editor.wizard.helper.IDiagramDataAttributeProvider;
import org.lh.dmlj.schema.editor.wizard.helper.NewSchemaWizardHelper;


public class NewSchemaWizard extends Wizard implements INewWizard {

	private static final Logger logger = Logger.getLogger(Plugin.getDefault());
	
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
		final File targetFile = page.getSchemaFile();						
						    	    	
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
						
						// create the schema and set its diagram data properties
						final IPreferenceStore store = Plugin.getDefault().getPreferenceStore();					
						IDiagramDataAttributeProvider diagramDataAttributeProvider = 
							new IDiagramDataAttributeProvider() {
								@Override
								public boolean isShowGrid() {
									return store.getBoolean(PreferenceConstants.SHOW_GRID);
								}
								@Override
								public boolean isShowRulers() {
									return store.getBoolean(PreferenceConstants.SHOW_RULERS);
								}
								@Override
								public boolean isSnapToGeometry() {
									return store.getBoolean(PreferenceConstants.SNAP_TO_GEOMETRY);
								}
								@Override
								public boolean isSnapToGrid() {
									return store.getBoolean(PreferenceConstants.SNAP_TO_GRID);
								}
								@Override
								public boolean isSnapToGuides() {
									return store.getBoolean(PreferenceConstants.SNAP_TO_GUIDES);
								}							
						};
						NewSchemaWizardHelper helper = 
							new NewSchemaWizardHelper(diagramDataAttributeProvider);
						Schema schema = helper.createSchema();
						
						Tools.writeToFile(schema, targetFile);
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
			logger.error(e.getMessage(), e);
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
			modelFile.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (Throwable e) {
			logger.error("Error while refreshing workspace", e);
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
