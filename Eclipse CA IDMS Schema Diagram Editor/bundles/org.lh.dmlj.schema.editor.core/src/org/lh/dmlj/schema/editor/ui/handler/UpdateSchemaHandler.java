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
package org.lh.dmlj.schema.editor.ui.handler;

import java.io.File;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.log.Logger;
import org.lh.dmlj.schema.editor.wizard._import.schema.SchemaImportWizard;

public class UpdateSchemaHandler implements IHandler {
	private static final Logger logger = Logger.getLogger(Plugin.getDefault());
	
	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// nothing to do here
	}

	@Override
	public void dispose() {
		// nothing to do here
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		var structuredSelection = getStructuredSelected();
		if (structuredSelection != null && closeEditor((IFile) structuredSelection.getFirstElement())) {
			openSchemaImportWizard(structuredSelection);
		}
		return null;
	}
	
	private IStructuredSelection getStructuredSelected() {
		ISelection selection;
		try {
			selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
		if (selection.isEmpty() || !(selection instanceof IStructuredSelection)) {
			var message = getClass().getSimpleName() + ": selection is empty or not an IStructuredSelection";
			logger.warning(message);
			return null;
		}
		var structuredSelection = (IStructuredSelection) selection;
		if (!(structuredSelection.getFirstElement() instanceof IFile)) {
			var message = getClass().getSimpleName() + ": the IStructuredSelection's first element is not an IFile";
			logger.warning(message);
			return null;
		} else {
			return structuredSelection;
		}
	}
	
	private boolean closeEditor(IFile file) {
		var editorPart = getOpenFileEditor(file.getLocation().toFile());
		if (editorPart != null) {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPages()[0].closeEditor(editorPart, true);
			editorPart = getOpenFileEditor(file.getLocation().toFile());
			return editorPart == null;
		} else {
			return true;
		}
	}

	public IEditorPart getOpenFileEditor(File file) {
		if (file != null) {
			var path = new Path(file.getAbsolutePath());			
			for (var page : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPages()) {
				for (var editorReference : page.getEditorReferences()) {
					try {
						if (editorReference.getEditorInput() instanceof FileEditorInput fei && fei.getPath().equals(path)) {
							return editorReference.getEditor(false);							
						}
					} catch (Exception e) {
						// ignore
					}
				}
			}
		}
		return null;
	}
	
	private void openSchemaImportWizard(IStructuredSelection ss) {
		var wizard = new SchemaImportWizard();
		wizard.setUpdateMode(true);
		wizard.init(PlatformUI.getWorkbench(), ss);		
		
		var shell = Display.getCurrent().getActiveShell();
		var wizardDialog = new WizardDialog(shell, wizard);
		wizardDialog.create();
		Display.getCurrent().syncExec(wizardDialog::open);		
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
		// nothing to do here
	}

}
