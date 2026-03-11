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

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.FileEditorInput;

/**
 * An instance of this class listens to changes to the file system in the workspace and makes changes accordingly:
 * 1) An open, saved file gets deleted -> close the editor
 * 2) An open file gets renamed or moved -> change the editor's input accordingly
 * It is assumed that the editor input is always of type IFileEditorInput.
 */
public class SchemaEditorResourceTracker implements IResourceChangeListener, IResourceDeltaVisitor {
	private final SchemaEditor schemaEditor;
	
	public SchemaEditorResourceTracker(SchemaEditor schemaEditor) {
		this.schemaEditor = schemaEditor;
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		var delta = event.getDelta();
		try {
			if (delta != null) {
				delta.accept(this);
			}
		} catch (CoreException exception) {
			Plugin.getDefault().getLog().error(exception.getMessage(), exception);
		}
	}
	
	@Override
	public boolean visit(IResourceDelta delta) {
		if (delta == null || !delta.getResource().equals(((IFileEditorInput) schemaEditor.getEditorInput()).getFile())) {
			return true;
		}

		var display = schemaEditor.getSite().getShell().getDisplay();
		if (delta.getKind() == IResourceDelta.REMOVED) {
			if ((IResourceDelta.MOVED_TO & delta.getFlags()) == 0) { 
				// the file was deleted
				// NOTE: The case where an open, unsaved file is deleted is being handled by the PartListener
				// added to the Workbench in the SchemaEditor's initialize() method.
				display.asyncExec(() -> {
					if (!schemaEditor.isDirty()) {
						schemaEditor.closeEditor(false);
					}
				});
			} else {
				// file was moved or renamed
				var newFile = ResourcesPlugin.getWorkspace().getRoot().getFile(delta.getMovedToPath());
				display.asyncExec(() -> schemaEditor.superSetInput(new FileEditorInput(newFile)));
			}
		} else if (delta.getKind() == IResourceDelta.CHANGED && !schemaEditor.isEditorSaving()) {
			// the file was overwritten somehow (could have been replaced by another version in the respository)
			var newFile = ResourcesPlugin.getWorkspace().getRoot().getFile(delta.getFullPath());
			display.asyncExec(() -> {
				schemaEditor.setInput(new FileEditorInput(newFile));
				schemaEditor.flushCommandStack();
			});
		}			
		return false;
	}
	
}
