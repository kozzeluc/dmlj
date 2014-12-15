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
import org.lh.dmlj.schema.editor.command.IModelChangeCommand;
import org.lh.dmlj.schema.editor.command.infrastructure.IContextDataKeys;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;

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
			ModelChangeType modelChangeType;
			if (memberRole.getSet().getSystemOwner() != null) {
				modelChangeType = ModelChangeType.DELETE_SYSTEM_OWNED_SET;
			} else {
				modelChangeType = ModelChangeType.DELETE_USER_OWNED_SET;
			}
			ModelChangeContext context = new ModelChangeContext(modelChangeType);
			context.getContextData().put(IContextDataKeys.SET_NAME, memberRole.getSet().getName());
			IModelChangeCommand command = 
				DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole.getSet());
			command.setContext(context);
			return (Command) command;
		} else if (!removingLastMember()) {	
			// create a command to remove the member record type from the set
			ModelChangeContext context = 
				new ModelChangeContext(ModelChangeType.REMOVE_MEMBER_FROM_SET);
			context.getContextData().put(IContextDataKeys.SET_NAME, memberRole.getSet().getName());
			context.getContextData().put(IContextDataKeys.RECORD_NAME, 
										 memberRole.getRecord().getName());
			IModelChangeCommand command = 
				DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);
			command.setContext(context);
			return (Command) command;
		}
		return null;
	}

	private boolean removingLastMember() {
		return memberRole.getSet().getMembers().size() == 1;
	}
	
}
