/**
 * Copyright (C) 2026  Luc Hermans
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
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Handle;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.lh.dmlj.schema.editor.command.ModelChangeCompoundCommand;
import org.lh.dmlj.schema.editor.command.MoveDiagramNodeCommand;
import org.lh.dmlj.schema.editor.part.ConnectorEditPart;
import org.lh.dmlj.schema.editor.part.RecordEditPart;

public class ModifiedNonResizableEditPolicy extends NonResizableEditPolicy {
	
	@Override
	protected List<Handle> createSelectionHandles() {
		var list = new ArrayList<Handle>();
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
	
	@Override
	public Command getCommand(Request request) {
		return fixCommandIfNeeded(request, super.getCommand(request), getHost());
	}
	
	Command fixCommandIfNeeded(Request request, Command command, GraphicalEditPart host) {
		if (commandNeedsFixing(request, command, host)) {
			return extractMoveDiagramNodeCommand((ModelChangeCompoundCommand) command);
		} else {
			return command;
		}
	}
	
	private boolean commandNeedsFixing(Request request, Command command, GraphicalEditPart host) {
		// when moving a record or connector, the SchemaXYLayoutEditPolicy will also create commands to fix
		// bendpoints - when moving a group of items, fixing those bendpoints would make things worse rather than
		// better; for that reason, and because the ModelChangeDispatcher doesn't handle nested compound commands
		// very well, we here remove all bendpoint related commands if needed
		return request instanceof ChangeBoundsRequest changeBoundsRequest && changeBoundsRequest.getEditParts().size() > 1 &&
				(host instanceof RecordEditPart || host instanceof ConnectorEditPart) &&
				command instanceof ModelChangeCompoundCommand compoundCommand && compoundCommand.getCommands().size() > 1 &&
				compoundCommand.getCommands().get(0) instanceof MoveDiagramNodeCommand;
	}
	
	private MoveDiagramNodeCommand extractMoveDiagramNodeCommand(ModelChangeCompoundCommand compoundCommand) {
		var moveDiagramNodeCommand = (MoveDiagramNodeCommand) compoundCommand.getCommands().get(0);
		moveDiagramNodeCommand.setContext(compoundCommand.getContext());
		return moveDiagramNodeCommand;
	}
	
}
