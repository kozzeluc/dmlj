package org.lh.dmlj.schema.editor.policy;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Handle;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gef.handles.ResizeHandle;
import org.eclipse.gef.tools.SelectEditPartTracker;

public class NonResizableIndexEditPolicy extends NonResizableEditPolicy {

	public NonResizableIndexEditPolicy() {
		super();
	}	
	
	private Handle createHandle(GraphicalEditPart owner, int direction,
								DragTracker t) {
		ResizeHandle handle = new ResizeHandle(owner, direction);
		handle.setCursor(Cursors.ARROW);
		handle.setDragTracker(t);
		return handle;
	}

	protected List<Handle> createSelectionHandles() {
		List<Handle> list = new ArrayList<Handle>();
		GraphicalEditPart part = (GraphicalEditPart) getHost();
		DragTracker tracker = new SelectEditPartTracker(getHost());
		list.add(moveHandle(part, tracker));
		list.add(createHandle(part, PositionConstants.NORTH_WEST, tracker));		
		list.add(createHandle(part, PositionConstants.NORTH_EAST, tracker));		
		list.add(createHandle(part, PositionConstants.SOUTH, tracker));		
		return list;
	}
	
	private Handle moveHandle(GraphicalEditPart owner, DragTracker tracker) {
		MoveHandle moveHandle = new MoveHandle(owner);
		moveHandle.setBorder(null);
		moveHandle.setDragTracker(tracker);
		moveHandle.setCursor(Cursors.ARROW);
		return moveHandle;
	}
}