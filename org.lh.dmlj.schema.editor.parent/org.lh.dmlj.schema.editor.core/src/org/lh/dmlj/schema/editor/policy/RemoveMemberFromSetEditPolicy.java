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

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.editor.command.DeleteSetOrIndexCommandCreationAssistant;

public class RemoveMemberFromSetEditPolicy extends ComponentEditPolicy {
	
	private MemberRole memberRole;
	private boolean allowRemovalOfSet;
	
	public RemoveMemberFromSetEditPolicy(MemberRole memberRole, boolean allowRemovalOfSet) {
		super();
		this.memberRole = memberRole;
		this.allowRemovalOfSet = allowRemovalOfSet;
	}
	
	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		if (deleteRequest.getEditParts().size() > 1) {						
			return null;
		}
		if (removingLastMember() && allowRemovalOfSet) {			
			// create a command to remove the set
			return (Command) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole.getSet());
		} else if (!removingLastMember()) {	
			// create a command to remove the member record type from the set
			return (Command) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);			
		}
		return null;
	}

	private boolean removingLastMember() {
		return memberRole.getSet().getMembers().size() == 1;
	}
	
}
