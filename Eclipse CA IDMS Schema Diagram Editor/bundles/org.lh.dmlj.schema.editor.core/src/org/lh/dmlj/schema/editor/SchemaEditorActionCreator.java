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

import java.util.List;

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.print.PrintGraphicalViewerOperation;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.PrintAction;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.SelectAllAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.ui.IWorkbenchPart;
import org.lh.dmlj.schema.editor.preference.PreferenceConstants;

public class SchemaEditorActionCreator {
	private final SchemaEditor schemaEditor;
	
	public SchemaEditorActionCreator(SchemaEditor schemaEditor) {
		this.schemaEditor = schemaEditor;
	}
	
	public void createActions(ActionRegistry registry, List<String> stackActions, List<String> selectionActions) {
		IAction action;

		action = new UndoAction(schemaEditor);
		registry.registerAction(action);
		stackActions.add(action.getId());

		action = new RedoAction(schemaEditor);
		registry.registerAction(action);
		stackActions.add(action.getId());

		action = new SelectAllAction(schemaEditor);
		registry.registerAction(action);

		action = new DeleteAction((IWorkbenchPart) schemaEditor);
		registry.registerAction(action);
		selectionActions.add(action.getId());		

		action = new PrintAction(schemaEditor) {
			@Override
			public void run() {
				var viewer = getWorkbenchPart().getAdapter(GraphicalViewer.class);
				var dialog = new PrintDialog(viewer.getControl().getShell(), SWT.NULL);
				var data = dialog.open();
				if (data != null) {
					var op = new PrintGraphicalViewerOperation(new Printer(data), viewer);
					
					var store = Plugin.getDefault().getPreferenceStore();					
					var topMargin = store.getInt(PreferenceConstants.TOP_MARGIN);
					var leftMargin = store.getInt(PreferenceConstants.LEFT_MARGIN);
					var bottomMargin = store.getInt(PreferenceConstants.BOTTOM_MARGIN);
					var rightMargin = store.getInt(PreferenceConstants.RIGHT_MARGIN);						
					var printMargin = new Insets(topMargin, leftMargin, bottomMargin, rightMargin);
					op.setPrintMargin(printMargin);					
					
					op.run(getWorkbenchPart().getTitle());
				}
			}			
		};
		registry.registerAction(action);
	}

}
