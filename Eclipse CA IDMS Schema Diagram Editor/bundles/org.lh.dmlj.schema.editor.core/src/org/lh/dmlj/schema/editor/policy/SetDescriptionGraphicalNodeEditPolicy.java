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
package org.lh.dmlj.schema.editor.policy;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.editor.command.AddMemberToSetCommand;
import org.lh.dmlj.schema.editor.palette.IMultipleMemberSetPlaceHolder;

/**
 * An edit policy that enables adding a member record type to an existing set, making it a multiple-
 * member set, if not already.
 */
public class SetDescriptionGraphicalNodeEditPolicy extends GraphicalNodeEditPolicy {

	private Set	set;
	
	public SetDescriptionGraphicalNodeEditPolicy(ConnectionLabel connectionLabel) {			
		super();		
		set = connectionLabel.getMemberRole().getSet();		
	}
	
	@Override
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		// no connection should ever have the set description edit part as a target
		return null;
	}

	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		// we're only interested in adding member record types to existing sets, thus creating 
		// and maintaining multiple-member sets; it is clear that the owner record will (eventually) 
		// become the connection's source edit part
		if (request.getNewObjectType() != IMultipleMemberSetPlaceHolder.class) {
			return null;
		}
		// if the set is indexed, don't allow adding anymore member record types since this kind of
		// sets is by definition single-member
		if (set.getMode() == SetMode.INDEXED) {
			return null;
		}
		// called when the user wants to add a member record type to an existing chained set 
		if (!(request.getStartCommand() instanceof AddMemberToSetCommand) ||
			((AddMemberToSetCommand) request.getStartCommand()).getSet() != set) {
			
			// avoid creating a new command over and over again for the same set; we only know to
			// which set the user wants to add an additional member record type (but not which)
			request.setStartCommand(new AddMemberToSetCommand(set));
		}
		return request.getStartCommand();
	}

	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		return null;
	}

	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		return null;
	}
	
	@Override
	protected Connection createDummyConnection(Request request) {
		if (!(request instanceof CreateConnectionRequest) ||
			((CreateConnectionRequest) request).getNewObjectType() != IMultipleMemberSetPlaceHolder.class) {
			
			return super.createDummyConnection(request);
		}
		// we're adding a member record type to an existing set; make sure the line that is shown
		// while looking for that member record type is fitted with an arrow at its target end
		PolylineConnection connection = new PolylineConnection();
		PolylineDecoration decoration = new PolylineDecoration();
		decoration.setTemplate(PolylineDecoration.TRIANGLE_TIP);
		connection.setTargetDecoration(decoration);
		return connection;
	}
	
}
