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

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.editor.command.DeleteSetOrIndexCommandCreationAssistant;

public class SetComponentEditPolicy extends ComponentEditPolicy {

	private boolean removeMemberOnly = true;
	
	public SetComponentEditPolicy(boolean removeMemberOnly) {
		super();
		this.removeMemberOnly = removeMemberOnly;
	}
	
	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		@SuppressWarnings("unchecked")
		List<EditPart> editParts = deleteRequest.getEditParts(); 
		if (editParts.size() != 1 || !(editParts.get(0).getModel() instanceof ConnectionPart)) {						
			return null;
		}
		// get the connection part and have the right command created
		ConnectionPart connectionPart = (ConnectionPart) editParts.get(0).getModel();
		MemberRole memberRole = connectionPart.getMemberRole();
		if (removeMemberOnly) {
			// create a command to remove the member record type from the set (or delete the set if
			// it's not a multiple-member set)
			return DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);
		} else {
			// create a command to remove the set
			return DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole.getSet());
		}
	}
	
}
