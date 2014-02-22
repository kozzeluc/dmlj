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
package org.lh.dmlj.schema.editor.property.section;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeListener;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;
import org.lh.dmlj.schema.editor.property.IGraphicalEditorProvider;

/**
 * A TOP LEVEL (abstract) property section exposing the active editor, that listens for model 
 * changes to make sure the information shown is always up-to-date, and that guarantees the 
 * undo/redo retargeted actions to be always available.
 * <br><br>
 * Subclasses should always call the createControls(...) method in their own implementation; the
 * same applies to the aboutToBeHidden(), aboutToBeShown(), dispose() and setInput(...) methods, if
 * overridden.
 * <br><br>
 * The editor should have adapters available for the following types :
 * <ul>
 * <li>IModelChangeProvider: the component that dispatches model changes for the editor</li>
 * <li>ActionRegistry: the object returned by the editor's  getActionRegistry() method</li>
 * </ul>
 * These adapter classes are to be returned by the editor's getAdapter(...) method.
 * <br><br>
 * If an outline page is provided by the editor, that outline page should implement the
 * IGraphicalEditorProvider<?> interface, through which the editor can be obtained.
 * <br><br>
 * @author Luc Hermans
 */
public abstract class AbstractPropertiesSection 
	extends AbstractPropertySection implements IModelChangeListener {

	private boolean					addedRedo; 	
	private boolean 				addedUndo; 
	protected GraphicalEditor  		editor;		  			
	private IModelChangeProvider 	modelChangeProvider;
	protected EObject 				modelObject;
	private TabbedPropertySheetPage page;	
		
	
	public AbstractPropertiesSection() {
		super();
	}
	
	@Override
	public void aboutToBeHidden() {
		clearGlobalActionHandlers();
	}
	
	@Override
	public void aboutToBeShown() {
		// make sure the undo and redo actions are ALWAYS available
		addGlobalActionHandlers((ActionRegistry) editor.getAdapter(ActionRegistry.class));
	}
	
	private final void addGlobalActionHandlers(ActionRegistry actionRegistry) {		
				
		// make sure the undo and redo actions are retargeted - this seems to be not always the case
		// when selecting an item in the outline view without having selected an item in the diagram
		// first:
		Assert.isNotNull(page, "the createControls(...) method of " + getClass().getSimpleName() + 
				 		 " subclasses should always call super.createControls(...): page == null");
		IActionBars bars = page.getSite().getActionBars();
		
		IAction undoAction = bars.getGlobalActionHandler(ActionFactory.UNDO.getId());
		if (undoAction == null) {
			String id = ActionFactory.UNDO.getId();
			bars.setGlobalActionHandler(id, actionRegistry.getAction(id));
			addedUndo = true;
		}
		
		IAction redoAction = bars.getGlobalActionHandler(ActionFactory.REDO.getId());
		if (redoAction == null) {
			String id = ActionFactory.REDO.getId();
			bars.setGlobalActionHandler(id, actionRegistry.getAction(id));
			addedRedo = true;
		}
		
		if (addedUndo || addedRedo) {
			bars.updateActionBars();
		}
		
	}
	
	@Override
	public void afterAddItem(EObject owner, EReference reference, Object item) {
		// whatever model change, just refresh the properties section
		doRefresh();
	}
	
	@Override
	public void afterMoveItem(EObject oldOwner, EReference reference, Object item, 
							  EObject newOwner) {
		
		// whatever model change, just refresh the properties section
		doRefresh();
	}
	
	@Override
	public void afterRemoveItem(EObject owner, EReference reference, Object item) {
		// whatever model change, just refresh the properties section
		doRefresh();
	}
	
	@Override
	public void afterSetFeatures(EObject owner, EStructuralFeature[] features) {
		// whatever model change, just refresh the properties section
		doRefresh();
	}
	
	private void clearGlobalActionHandlers() {
		
		// 'restore' the original action bars if needed
		if (!addedUndo && !addedRedo) {
			return; // neither action was added so there's nothing to do
		}
		
		IActionBars bars = page.getSite().getActionBars();
		
		if (addedUndo) {
			bars.setGlobalActionHandler(ActionFactory.UNDO.getId(), null);
		}
		
		if (addedRedo) {
			bars.setGlobalActionHandler(ActionFactory.REDO.getId(), null);
		}
		
		bars.updateActionBars();
		addedUndo = false;
		addedRedo = false;
		
	}

	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage page) {
		super.createControls(parent, page);
		this.page = page;
	}
	
	@Override
	public void dispose() {
		// remove us as a model change listener if we have a model change provider
		if (modelChangeProvider != null) {
			modelChangeProvider.removeModelChangeListener(this);
		}
	};
	
	private void doRefresh() {									
		// refresh the properties section; we need to invoke the refresh() method asynchronously to 
		// avoid 'Widget is disposed' errors (don't ask why ;-)):
		final Class<?> _class = getClass();
		Display.getCurrent().asyncExec(new Runnable() {
			@Override
			public void run() {
				System.out.println(_class.getSimpleName());
				refresh();
			}				
		});		
	}

	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		
		// remove us as a model change listener if we have a model change provider
		if (modelChangeProvider != null) {
			modelChangeProvider.removeModelChangeListener(this);
			modelChangeProvider = null;
		}		
		
		super.setInput(part, selection);
		
		// we need a GraphicalEditor instance because we need its action registry in order to 
		// extract its undo/redo retargeted actions and its command stack as well (to listen for 
		// changes); in the case of a selection in the outline view (the part is a ContentOutline 
		// instance), we expect to find the editor via the ContentOutline's current page, which 
		// is an outline page that should also be an instance of IGraphicalEditorProvider<?>...
		Assert.isTrue(part instanceof GraphicalEditor || part instanceof ContentOutline, 
					  "part is not a GraphicalEditor, nor a ContentOutline: " + 
					  (part == null ? "null" : part.getClass().getName()));
		if (part instanceof ContentOutline) {
			ContentOutline contentOutline = (ContentOutline) part;
			Assert.isTrue(contentOutline.getCurrentPage() instanceof IPage, 
						  "current page in the outline view is not an IPage");
			IGraphicalEditorProvider<?> outlinePage = 
				(IGraphicalEditorProvider<?>) contentOutline.getCurrentPage();
			editor = outlinePage.getEditor();
		} else {
			editor = (GraphicalEditor) part;
		}		
		
		// set the input from the selection
		EditPart input = (EditPart) ((IStructuredSelection) selection).getFirstElement();
		modelObject = (EObject) ((EditPart) input).getModel();
		
		// make sure the undo and redo actions are available
		addGlobalActionHandlers((ActionRegistry) editor.getAdapter(ActionRegistry.class));
		
	    // we need the editor's model change provider to change the model data
	    modelChangeProvider = (IModelChangeProvider) editor.getAdapter(IModelChangeProvider.class);
	    modelChangeProvider.addModelChangeListener(this);
	
	}	
	
}
