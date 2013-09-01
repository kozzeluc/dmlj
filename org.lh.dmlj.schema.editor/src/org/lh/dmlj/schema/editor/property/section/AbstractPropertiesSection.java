package org.lh.dmlj.schema.editor.property.section;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
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
import org.lh.dmlj.schema.editor.property.IGraphicalEditorProvider;

/**
 * A TOP LEVEL (abstract) property section exposing a GEF editor and its command stack, listens for
 * stack changes (i.e. commands being executed/undone/redone) to make sure the information shown is
 * always up-to-date, and that guarantees the undo/redo retargeted actions to be always available.
 * <br><br>
 * Subclasses should always call the createControls(...) method in their own implementation; the
 * same applies to the aboutToBeHidden(), aboutToBeShown(), dispose() and setInput(...) methods, if
 * overridden.
 * <br><br>
 * The GEF editor should have adapters available for the  following types :
 * <ul>
 * <li>CommandStack: the object returned by the editor's getCommandStack() method</li>
 * <li>ActionRegistry: the object returned by the editor's  getActionRegistry() method</li>
 * </ul>
 * These adapter classes are to be returned by the editor's getAdapter(...) method.
 * <br><br>
 * If an outline page is provided by the GEF editor, that outline page should implement the
 * IGraphicalEditorProvider<?> interface, through which the editor can be obtained.
 * <br><br>
 * @author Luc Hermans
 */
public abstract class AbstractPropertiesSection 
	extends AbstractPropertySection implements CommandStackEventListener {

	private boolean					  addedRedo; 	
	private boolean 				  addedUndo; 
	protected CommandStack 	  		  commandStack;
	protected GraphicalEditor  		  editor;		// the GEF editor
	protected EditPart 				  input; 		// the input edit part
	protected EObject 				  modelObject;  // the input model object
	protected TabbedPropertySheetPage page;	
	protected ISelection 			  selection;
	//private ISelection 			  selectionCopy; // see stackChanged(...)
	
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
		// remove us as a command stack event listener if we have a command stack
		if (commandStack != null) {
			commandStack.removeCommandStackEventListener(this);
		}
	};
	
	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		
		// remove us as a command stack event listener if we have a command stack
		if (commandStack != null) {
			commandStack.removeCommandStackEventListener(this);
			commandStack = null;
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
		
		// store the selection
		Assert.isTrue(selection instanceof IStructuredSelection, "not a IStructuredSelection");
		this.selection = selection;
		
		// set the input from the selection
		input = (EditPart) ((IStructuredSelection) selection).getFirstElement();
		modelObject = (EObject) ((EditPart) input).getModel();
		
		// make sure the undo and redo actions are available
		addGlobalActionHandlers((ActionRegistry) editor.getAdapter(ActionRegistry.class));
		
	    // we need the editor's command stack to change the model data
	    commandStack = (CommandStack) editor.getAdapter(CommandStack.class);
	    commandStack.addCommandStackEventListener(this);
	
	}
	
	@Override
	public final void stackChanged(CommandStackEvent event) {
		// by listening for command stack events we are able to refresh the
		// whole property sheet page; this seems to be the most practical
		// approach in keeping all tabs and sections in sync with the model
		if (event.isPreChangeEvent() &&
			(event.getDetail() == CommandStack.PRE_EXECUTE ||
			 event.getDetail() == CommandStack.PRE_REDO ||
			 event.getDetail() == CommandStack.PRE_UNDO)) {
				
			// for some awkard reason, the setInput method is sometimes called 
			// somewhere between the event's pre- and post processing with a 
			// different selection, so we set aside the current selection here
			//selectionCopy = selection; 												   // <-----
		} else if (event.isPostChangeEvent() &&
			(event.getDetail() == CommandStack.POST_EXECUTE ||
			 event.getDetail() == CommandStack.POST_REDO ||
			 event.getDetail() == CommandStack.POST_UNDO)) {
							
			// we need to execute this asynchronously to avoid Widget' is 
			// disposed' errors (don't ask why ;-)):
			Display.getCurrent().asyncExec(new Runnable() {
				@Override
				public void run() {
					/*// (simulate a) clear (of) the current selection
					page.selectionChanged(getPart(), new StructuredSelection());
					// set the (original) selection again; this will NOT work when we will have
					// the capability to DELETE objects...
					page.selectionChanged(getPart(), selectionCopy);*/					
					
					refresh(); // just call refresh(); this avoids some flickering
				}				
			});
		}
	}	
	
}