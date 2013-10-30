/**
 * Copyright (C) 2013  Luc Hermans
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
