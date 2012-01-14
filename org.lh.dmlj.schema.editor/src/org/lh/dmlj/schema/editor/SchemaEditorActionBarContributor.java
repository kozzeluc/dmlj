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
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.LabelRetargetAction;

public class SchemaEditorActionBarContributor extends ActionBarContributor {

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
	}	
	
	@Override
	public void contributeToMenu(IMenuManager menuManager) {
		
		super.contributeToMenu(menuManager);
		
		MenuManager viewMenu = new MenuManager("View");
		
		viewMenu.add(getAction(GEFActionConstants.ZOOM_IN));
		viewMenu.add(getAction(GEFActionConstants.ZOOM_OUT));
		
		menuManager.insertAfter(IWorkbenchActionConstants.M_EDIT, viewMenu);
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