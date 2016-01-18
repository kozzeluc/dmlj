/**
 * Copyright (C) 2014  Luc Hermans
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

import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.LabelRetargetAction;
import org.eclipse.ui.actions.RetargetAction;

public class SchemaEditorActionBarContributor extends ActionBarContributor {

	private static final String DIAGRAM_MENU_ID = "org.lh.dmlj.schema.editor.menu.diagram";

	public SchemaEditorActionBarContributor() {
		super();
	}
	
	@Override
	protected void buildActions() {
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
		addRetargetAction(new DeleteRetargetAction());
		addRetargetAction(new LabelRetargetAction(ActionFactory.SELECT_ALL.getId(), 
												  "Select All"));
		
		addRetargetAction(new ZoomInRetargetAction());
		addRetargetAction(new ZoomOutRetargetAction());
		
		//addRetargetAction(new RetargetAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY,
		//				  					 "Grid", IAction.AS_CHECK_BOX));
		
		addRetargetAction(new RetargetAction(ActionFactory.PRINT.getId(), "&Print"));
	}	
	
	@Override
	public void contributeToMenu(IMenuManager menuManager) {

		// we should consider to move the zoom in and out actions to the toolbar
		// and eliminate the Diagram menu altogether
		
		super.contributeToMenu(menuManager);
		
		IMenuManager diagramMenu = menuManager.findMenuUsingPath(DIAGRAM_MENU_ID);		
		
		diagramMenu.add(new Separator());
		diagramMenu.add(getAction(GEFActionConstants.ZOOM_IN));
		diagramMenu.add(getAction(GEFActionConstants.ZOOM_OUT));
		
		//viewMenu.add(new Separator());
		
		//viewMenu.add(getAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY));
		
		menuManager.insertAfter(IWorkbenchActionConstants.M_EDIT, diagramMenu);
	}
	
	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		
		toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
		toolBarManager.add(getAction(ActionFactory.REDO.getId()));
		
		toolBarManager.add(new Separator());
		String[] zoomStrings = new String[] { ZoomManager.FIT_ALL, 
											  ZoomManager.FIT_HEIGHT, 
											  ZoomManager.FIT_WIDTH };
		toolBarManager.add(new ZoomComboContributionItem(getPage(), zoomStrings));
		
	}

	@Override
	protected void declareGlobalActionKeys() {				
	}

}
