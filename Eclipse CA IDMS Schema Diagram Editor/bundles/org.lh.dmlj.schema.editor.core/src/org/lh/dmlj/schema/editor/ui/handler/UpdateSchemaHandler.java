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
package org.lh.dmlj.schema.editor.ui.handler;

import java.io.File;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.log.Logger;
import org.lh.dmlj.schema.editor.wizard._import.schema.SchemaImportWizard;

public class UpdateSchemaHandler implements IHandler {

	private static final Logger logger = Logger.getLogger(Plugin.getDefault());
	
	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {		
	}

	@Override
	public void dispose() {
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection;
		try {
			selection = PlatformUI.getWorkbench()
			 					  .getActiveWorkbenchWindow()
					 			  .getSelectionService()
					 			  .getSelection();
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			return null;
		}
		if (selection.isEmpty() || 
			!(selection instanceof IStructuredSelection)) {
			
			System.out.println(getClass().getSimpleName() + 
							   ": selection is empty or not an " +
							   "IStructuredSelection");
			return null;
		}
		IStructuredSelection ss = (IStructuredSelection) selection;
		if (!(ss.getFirstElement() instanceof IFile)) {
			System.out.println(getClass().getSimpleName() + ": the " +
							   "IStructuredSelection's first element is not an " +
							   "IFile");
			return null;
		}
		
		IFile iFile = (IFile) ss.getFirstElement();
		// close the diagram if it is opened in an editor
		IEditorPart editorPart = 
			getOpenFileEditor(iFile.getLocation().toFile());
		if (editorPart != null) {
			
			PlatformUI.getWorkbench()
				 	  .getActiveWorkbenchWindow()
					  .getPages()[0]
					  .closeEditor(editorPart, true);
			
			editorPart = getOpenFileEditor(iFile.getLocation().toFile());
			if (editorPart != null) {
				return null;
			}
			
		}		
		
		SchemaImportWizard wizard = new SchemaImportWizard();
		wizard.setUpdateMode(true);
		wizard.init(PlatformUI.getWorkbench(), ss);		
		
		Shell shell = Display.getCurrent().getActiveShell();
		final WizardDialog wizardDialog = new WizardDialog(shell, wizard);
		wizardDialog.create();
		Display.getCurrent().syncExec(new Runnable() {
			public void run() {
				wizardDialog.open();
			}
		});		
		
		return null;
	}

	public IEditorPart getOpenFileEditor(File file) {
		if (file != null) {
			IPath path = new Path(file.getAbsolutePath());			
			for (IWorkbenchPage page :
				 PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPages()) {
				
				for (IEditorReference editorReference : 
					 page.getEditorReferences()) {
			
					try {
						IEditorInput input = editorReference.getEditorInput();
						if (input instanceof FileEditorInput) {
							FileEditorInput fei = 
								(FileEditorInput)input;
							if (fei.getPath().equals(path)) {
								return editorReference.getEditor(false);								
							}
						}
					} catch (Throwable e) {
					}
				}
			}
		}
		return null;
	}	
	
	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {		
	}

}
