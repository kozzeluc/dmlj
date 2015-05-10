/**
 * Copyright (C) 2015  Luc Hermans
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
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.editor.command.DeleteSetOrIndexCommandCreationAssistant;
import org.lh.dmlj.schema.editor.command.IModelChangeCommand;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;


public class SetDescriptionComponentEditPolicy extends ComponentEditPolicy {

	public SetDescriptionComponentEditPolicy() {
		super();
	}
	
	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		@SuppressWarnings("unchecked")
		List<EditPart> editParts = deleteRequest.getEditParts(); 
		if (editParts.size() != 1 || !(editParts.get(0).getModel() instanceof ConnectionLabel)) {						
			return null;
		}
		// get the connection label and have the right command created
		ConnectionLabel connectionLabel = (ConnectionLabel) editParts.get(0).getModel();
		IModelChangeCommand command = 
			DeleteSetOrIndexCommandCreationAssistant.getCommand(connectionLabel.getMemberRole());
		if (connectionLabel.getMemberRole().getSet().isMultipleMember()) {
			ModelChangeContext context = 
				new ModelChangeContext(ModelChangeType.REMOVE_MEMBER_FROM_SET);
			context.putContextData(connectionLabel.getMemberRole());
			command.setContext(context);
		} else {
			ModelChangeType modelChangeType;
			if (connectionLabel.getMemberRole().getSet().getSystemOwner() != null) {
				modelChangeType = ModelChangeType.DELETE_SYSTEM_OWNED_SET;
			} else if (connectionLabel.getMemberRole().getSet().getVsamIndex() != null) {
				modelChangeType = ModelChangeType.DELETE_VSAM_INDEX;
			} else {
				modelChangeType = ModelChangeType.DELETE_USER_OWNED_SET;
			}
			ModelChangeContext context = new ModelChangeContext(modelChangeType);
			context.putContextData(connectionLabel.getMemberRole().getSet());
			command.setContext(context);
		}
		return (Command) command;
	}
	
}
