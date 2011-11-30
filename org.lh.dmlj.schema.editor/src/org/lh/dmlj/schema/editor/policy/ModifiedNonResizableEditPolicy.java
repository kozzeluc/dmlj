package org.lh.dmlj.schema.editor.policy;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.Handle;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;

public class ModifiedNonResizableEditPolicy extends NonResizableEditPolicy {

	public ModifiedNonResizableEditPolicy() {
		super();
	}
	
	protected List<Handle> createSelectionHandles() {
		List<Handle> list = new ArrayList<>();
		createMoveHandle(list);
		createDragHandle(list, PositionConstants.NORTH_WEST);
		createDragHandle(list, PositionConstants.NORTH);
		createDragHandle(list, PositionConstants.NORTH_EAST);
		createDragHandle(list, PositionConstants.WEST);
		createDragHandle(list, PositionConstants.EAST);
		createDragHandle(list, PositionConstants.SOUTH_WEST);
		createDragHandle(list, PositionConstants.SOUTH);
		createDragHandle(list, PositionConstants.SOUTH_EAST);
		return list;
	}
		
}