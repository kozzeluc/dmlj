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
package org.lh.dmlj.schema.editor;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

public class SchemaEditorPartListener implements IPartListener {
	private final SchemaEditor schemaEditor;
	
	public SchemaEditorPartListener(SchemaEditor schemaEditor) {
		this.schemaEditor = schemaEditor;
	}
		
	@Override
	public void partActivated(IWorkbenchPart part) {
		// If an open, unsaved file was deleted, query the user to either do a "Save As" or close the editor.
		if (part != schemaEditor) {
			return;
		}
		if (schemaEditor.getEditorInput() instanceof IFileEditorInput fileEditorInput &&
			!fileEditorInput.getFile().getLocation().toFile().exists()) {
			
			var shell = schemaEditor.getSite().getShell();
			var title = "File Deleted";
			var message = "The file has been deleted from the file system. Do you want to save your changes or " +
					"close the editor without saving?";
			var dialog = new MessageDialog(shell, title, null, message, MessageDialog.QUESTION, new String[] { "Save", "Close" }, 0);
			if (dialog.open() == Window.OK) {
				if (!schemaEditor.performSaveAs()) {
					partActivated(part);
				}
			} else {
				schemaEditor.closeEditor(false);
			}
		}
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
		// ignore
	}

	@Override
	public void partClosed(IWorkbenchPart part) {
		// ignore
	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
		// ignore
	}

	@Override
	public void partOpened(IWorkbenchPart part) {
		// ignore
	}

}
