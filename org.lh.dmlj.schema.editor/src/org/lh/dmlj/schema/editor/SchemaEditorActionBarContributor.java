package org.lh.dmlj.schema.editor;

import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.LabelRetargetAction;
import org.eclipse.ui.actions.RetargetAction;

public class SchemaEditorActionBarContributor extends ActionBarContributor {

	public SchemaEditorActionBarContributor() {
		super();
	}
	
	protected void buildActions() {
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
		addRetargetAction(new DeleteRetargetAction());
		addRetargetAction(new LabelRetargetAction(ActionFactory.SELECT_ALL.getId(), "Select All"));
		addRetargetAction(new RetargetAction(
				GEFActionConstants.TOGGLE_RULER_VISIBILITY,
				GEFMessages.ToggleRulerVisibility_Label, IAction.AS_CHECK_BOX));

		addRetargetAction(new RetargetAction(
				GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY,
				GEFMessages.ToggleSnapToGeometry_Label, IAction.AS_CHECK_BOX));

		addRetargetAction(new RetargetAction(
				GEFActionConstants.TOGGLE_GRID_VISIBILITY,
				GEFMessages.ToggleGrid_Label, IAction.AS_CHECK_BOX));
		
	}

	public void contributeToMenu(IMenuManager menubar) {
		super.contributeToMenu(menubar);
		MenuManager viewMenu = new MenuManager("Schema Diagram");
		//viewMenu.add(getAction(GEFActionConstants.ZOOM_IN));
		//viewMenu.add(getAction(GEFActionConstants.ZOOM_OUT));
		viewMenu.add(new Separator());
		viewMenu.add(getAction(GEFActionConstants.TOGGLE_RULER_VISIBILITY));
		viewMenu.add(getAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY));
		viewMenu.add(getAction(GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY));
		//viewMenu.add(new Separator());
		//viewMenu.add(getAction(GEFActionConstants.MATCH_SIZE));
		//viewMenu.add(getAction(GEFActionConstants.MATCH_WIDTH));
		//viewMenu.add(getAction(GEFActionConstants.MATCH_HEIGHT));
		menubar.insertAfter(IWorkbenchActionConstants.M_EDIT, viewMenu);
	}	
	
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
		toolBarManager.add(getAction(ActionFactory.REDO.getId()));
	}

	@Override
	protected void declareGlobalActionKeys() {				
	}

}