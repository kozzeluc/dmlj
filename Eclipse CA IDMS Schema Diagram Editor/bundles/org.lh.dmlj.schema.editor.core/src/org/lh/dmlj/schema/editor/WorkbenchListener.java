/**
 * Copyright (C) 2022  Luc Hermans
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

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.lh.dmlj.schema.editor.preference.PreferenceConstants;

/**
 * A workbench listener that intercepts the workbench shutdown and deals with any open .schemadsl
 * editors (i.e. closes+saves them or not, or asks the user what to do)
 */
public class WorkbenchListener implements IWorkbenchListener {
	
	private static final boolean PROCEED_WITH_WORKBENCH_SHUTDOWN = true;
	private static final boolean CANCEL_WORKBENCH_SHUTDOWN = false;
	
	private static final String READ_ONLY_EDITORS = "READ_ONLY_EDITORS";
	
	private static List<String> getDirtyEditorKeys(Map<String, List<SchemaEditor>> openSchemaDslEditors) {
		List<String> keys = new ArrayList<>();
		for (String key : openSchemaDslEditors.keySet()) {
			if (!key.equals(READ_ONLY_EDITORS)) {
				if (getFirstDirtyEditor(openSchemaDslEditors.get(key)) != null) {
					keys.add(key);				
				}
			}
		}
		return keys;
	}
	
	private static SchemaEditor getFirstDirtyEditor(List<SchemaEditor> editors) {
		for (SchemaEditor editor : editors) {
			if (editor.isDirty()) {
				return editor;
			}
		}
		return null;
	}

	private static Map<String, List<SchemaEditor>> getOpenSchemaDslEditors() {
		Map<String, List<SchemaEditor>> editors = new HashMap<>();
		for (IWorkbenchWindow workbenchWindow : PlatformUI.getWorkbench().getWorkbenchWindows()) {
			for (IWorkbenchPage workbenchPage : workbenchWindow.getPages()) {
				for (IEditorReference editorReference : workbenchPage.getEditorReferences()) {					
											
					IEditorPart editor = editorReference.getEditor(true);
					if (editor != null && editor instanceof SchemaEditor &&
						((SchemaEditor) editor).getPartName().toLowerCase().endsWith(".schemadsl")) {
						
						String key;
						if (((SchemaEditor) editor).isReadOnlyMode()) {							
							key = READ_ONLY_EDITORS;
						} else {
							IFile file = ((IFileEditorInput) editor.getEditorInput()).getFile();
							URI uri = URI.createFileURI(file.getLocation().toFile().getAbsolutePath());			
							key = new File(uri.toFileString()).getAbsolutePath();							
						}
						if (key != null) {
							if (!editors.containsKey(key)) {
								editors.put(key, new ArrayList<SchemaEditor>());
							}
							editors.get(key).add((SchemaEditor) editor);
						}
					}
				}
			}
		}
		return editors;
	}
	
	@Override
	public boolean preShutdown(IWorkbench workbench, boolean forced) {
		int closeSchemaDslEditors = 
			Plugin.getDefault().getPreferenceStore().getInt(PreferenceConstants.CLOSE_SCHEMADSL_EDITORS);
		if (closeSchemaDslEditors == PreferenceConstants.CLOSE_SCHEMADSL_EDITORS_YES) {
			return closeSchemaDslEditors();
		} else if (closeSchemaDslEditors == PreferenceConstants.CLOSE_SCHEMADSL_EDITORS_NO) {
			return PROCEED_WITH_WORKBENCH_SHUTDOWN;
		} else {
			return askToCloseSchemaDslEditors();
		}
	}

	private boolean askToCloseSchemaDslEditors() {
		
		Map<String, List<SchemaEditor>> openEditors = getOpenSchemaDslEditors();
		
		// don't bother when there are no open .schemadsl editors
		if (openEditors.isEmpty()) {
			return PROCEED_WITH_WORKBENCH_SHUTDOWN;
		}
		
		// ask the user what should happen and act accordingly 
		CloseSchemaDslEditorDialog dialog = 
			new CloseSchemaDslEditorDialog(Display.getCurrent().getActiveShell());
		int answer = dialog.open();
		if ((answer == CloseSchemaDslEditorDialog.YES || answer == CloseSchemaDslEditorDialog.NO) &&
			dialog.isRememberMyDecision()) {
			
			// the user wants his decision to be remembered; he will not be asked the same question,
			// unless he or she specifies this in the preferences
			int closeSchemaDslEditors;
			if (answer == CloseSchemaDslEditorDialog.YES) {
				closeSchemaDslEditors = PreferenceConstants.CLOSE_SCHEMADSL_EDITORS_YES;
			} else {
				closeSchemaDslEditors = PreferenceConstants.CLOSE_SCHEMADSL_EDITORS_NO;
			}
			Plugin.getDefault()
				  .getPreferenceStore()
				  .setValue(PreferenceConstants.CLOSE_SCHEMADSL_EDITORS, closeSchemaDslEditors);
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
		
		final Map<String, List<SchemaEditor>> openEditors = getOpenSchemaDslEditors();
		
		// save dirty editors (but don't close them)
		List<String> keys = getDirtyEditorKeys(openEditors);
		for (String key : keys) {
			
			SchemaEditor dirtyEditor = getFirstDirtyEditor(openEditors.get(key));
			
			Shell shell = Display.getCurrent().getActiveShell();
			String title = "Save Resource";
			String message = "'" + dirtyEditor.getPartName() + "' has been modified. Save changes?";
			String[] buttons = { "Yes", "No", "Cancel" };
			MessageDialog dialog = 
				new MessageDialog(shell, title, null, message, MessageDialog.QUESTION, buttons, 0);
			int response = dialog.open();
			if (response == 2) {
				return CANCEL_WORKBENCH_SHUTDOWN;
			} else if (response == 0) {
				dirtyEditor.doSave(null);
			} else {
				// make sure to reset the editor's dirty flag, or the user will be asked again
				// whether to save or not:
				dirtyEditor.markSaveLocationAndResetDirtyFlag();;
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
	}	

}
