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

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.editor.command.DeleteConnectorsCommand;
import org.lh.dmlj.schema.editor.part.ConnectorEditPart;

public class ConnectorComponentEditPolicy extends ComponentEditPolicy {

	public ConnectorComponentEditPolicy() {
		super();
	}
	
	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		List<?> editParts = deleteRequest.getEditParts(); 
		if (editParts.size() != 1 ||
			!(editParts.get(0) instanceof ConnectorEditPart)) {
			
			return null;
		}
		Connector connector = ((ConnectorEditPart)editParts.get(0)).getModel();
		MemberRole memberRole = connector.getConnectionPart().getMemberRole();
		return new DeleteConnectorsCommand(memberRole);				
	}
	
}
