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
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.command.DeleteConnectorsCommand;
import org.lh.dmlj.schema.editor.command.DeleteIndexCommand;
import org.lh.dmlj.schema.editor.part.IndexEditPart;

public class IndexComponentEditPolicy extends ComponentEditPolicy {

	public IndexComponentEditPolicy() {
		super();
	}
	
	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		List<?> editParts = deleteRequest.getEditParts(); 
		if (editParts.size() != 1 || !(editParts.get(0) instanceof IndexEditPart)) {			
			return null;
		}
		// get the system owner and the only set member
		SystemOwner systemOwner = ((IndexEditPart)editParts.get(0)).getModel();
		MemberRole memberRole = systemOwner.getSet().getMembers().get(0); 
		// we might need to delete the connectors first, so create a chained command when needed
		Command deleteIndexCommand = new DeleteIndexCommand(systemOwner);
		if (memberRole.getConnectionParts().size() > 1) {
			// connectors present, create a compound command
			Command deleteConnectorsCommand = new DeleteConnectorsCommand(memberRole);
			return deleteConnectorsCommand.chain(deleteIndexCommand);
		} else {
			// no connectors present, the simple delete index command will do
			return deleteIndexCommand;
		}
	}
	
}
