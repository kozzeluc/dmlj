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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.PlatformUI;
import org.lh.dmlj.schema.editor.preference.PreferenceConstants;

/**
 * A workbench listener that intercepts the workbench shutdown and deals with any open .schemadsl editors (i.e.
 * closes+saves them or not, or asks the user what to do)
 */
public class WorkbenchListener implements IWorkbenchListener {
	private static final boolean PROCEED_WITH_WORKBENCH_SHUTDOWN = true;
	private static final boolean CANCEL_WORKBENCH_SHUTDOWN = false;
	
	private static final String READ_ONLY_EDITORS = "READ_ONLY_EDITORS";
	
	private static List<String> getDirtyEditorKeys(Map<String, List<SchemaEditor>> openSchemaDslEditors) {
		var keys = new ArrayList<String>();
		for (var entry : openSchemaDslEditors.entrySet()) {
			var key = entry.getKey();
			if (!key.equals(READ_ONLY_EDITORS) && getFirstDirtyEditor(openSchemaDslEditors.get(key)) != null) {
				keys.add(key);				
			}
		}
		return keys;
	}
	
	private static SchemaEditor getFirstDirtyEditor(List<SchemaEditor> editors) {
		return editors.stream()
				.filter(SchemaEditor::isDirty)
				.findFirst()
				.orElse(null);
	}

	private static Map<String, List<SchemaEditor>> getOpenSchemaDslEditors() {
		var editors = new HashMap<String, List<SchemaEditor>>();
		for (var workbenchWindow : PlatformUI.getWorkbench().getWorkbenchWindows()) {
			for (var workbenchPage : workbenchWindow.getPages()) {
				for (var editorReference : workbenchPage.getEditorReferences()) {	
					var key = getKey(editorReference);
					if (key != null) {
						editors.computeIfAbsent(key, k -> new ArrayList<>())
								.add((SchemaEditor) editorReference.getEditor(true));
					}
				}
			}
		}
		return editors;
	}
	
	private static String getKey(IEditorReference editorReference) {
		var editor = editorReference.getEditor(true);
		if (editor != null && editor instanceof SchemaEditor schemaEditor && 	schemaEditor.getPartName().toLowerCase().endsWith(".schemadsl")) {
			if (schemaEditor.isReadOnlyMode()) {							
				return READ_ONLY_EDITORS;
			} else {
				var file = ((IFileEditorInput) editor.getEditorInput()).getFile();
				var uri = URI.createFileURI(file.getLocation().toFile().getAbsolutePath());			
				return new File(uri.toFileString()).getAbsolutePath();							
			}
		} else {
			return null;
		}
	}
	
	@Override
	public boolean preShutdown(IWorkbench workbench, boolean forced) {
		int closeSchemaDslEditors = 	Plugin.getDefault().getPreferenceStore().getInt(PreferenceConstants.CLOSE_SCHEMADSL_EDITORS);
		if (closeSchemaDslEditors == PreferenceConstants.CLOSE_SCHEMADSL_EDITORS_YES) {
			return closeSchemaDslEditors();
		} else if (closeSchemaDslEditors == PreferenceConstants.CLOSE_SCHEMADSL_EDITORS_NO) {
			return PROCEED_WITH_WORKBENCH_SHUTDOWN;
		} else {
			return askToCloseSchemaDslEditors();
		}
	}

	private boolean askToCloseSchemaDslEditors() {
		var openEditors = getOpenSchemaDslEditors();
		
		// don't bother when there are no open .schemadsl editors
		if (openEditors.isEmpty()) {
			return PROCEED_WITH_WORKBENCH_SHUTDOWN;
		}
		
		// ask the user what should happen and act accordingly 
		var dialog = new CloseSchemaDslEditorDialog(Display.getCurrent().getActiveShell());
		var answer = dialog.open();
		if ((answer == CloseSchemaDslEditorDialog.YES || answer == CloseSchemaDslEditorDialog.NO) && dialog.isRememberMyDecision()) {
			// the user wants his decision to be remembered; he will not be asked the same question, unless he or
			// she specifies this in the preferences
			int closeSchemaDslEditors;
			if (answer == CloseSchemaDslEditorDialog.YES) {
				closeSchemaDslEditors = PreferenceConstants.CLOSE_SCHEMADSL_EDITORS_YES;
			} else {
				closeSchemaDslEditors = PreferenceConstants.CLOSE_SCHEMADSL_EDITORS_NO;
			}
			Plugin.getDefault().getPreferenceStore().setValue(PreferenceConstants.CLOSE_SCHEMADSL_EDITORS, closeSchemaDslEditors);
		}
		if (answer == CloseSchemaDslEditorDialog.YES) {
			return closeSchemaDslEditors();
		} else if (answer == CloseSchemaDslEditorDialog.NO) {
			return PROCEED_WITH_WORKBENCH_SHUTDOWN;
		} else {		
			return CANCEL_WORKBENCH_SHUTDOWN;
		}
	}

	private boolean closeSchemaDslEditors() {
		var openEditors = getOpenSchemaDslEditors();
		
		// save dirty editors (but don't close them)
		var keys = getDirtyEditorKeys(openEditors);
		for (String key : keys) {
			var dirtyEditor = getFirstDirtyEditor(openEditors.get(key));
			if (dirtyEditor != null) {
				var shell = Display.getCurrent().getActiveShell();
				var title = "Save Resource";
				var message = "'" + dirtyEditor.getPartName() + "' has been modified. Save changes?";
				String[] buttons = { "Yes", "No", "Cancel" };
				var dialog = new MessageDialog(shell, title, null, message, MessageDialog.QUESTION, buttons, 0);
				var response = dialog.open();
				if (response == 2) {
					return CANCEL_WORKBENCH_SHUTDOWN;
				} else if (response == 0) {
					dirtyEditor.doSave(null);
				} else {
					// make sure to reset the editor's dirty flag, or the user will be asked again whether to save or not:
					dirtyEditor.markSaveLocationAndResetDirtyFlag();
				}
			}
		}		
		openEditors.values().stream()
				.flatMap(List::stream)
				.forEach(this::closeEditor);
		return PROCEED_WITH_WORKBENCH_SHUTDOWN;
	}
	
	private void closeEditor(SchemaEditor editor) {
		editor.getEditorSite().getPage().closeEditor(editor, false);
	}

	@Override
	public void postShutdown(IWorkbench workbench) {
		// nothing to do here 
	}	

}
