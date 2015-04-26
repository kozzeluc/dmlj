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
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.VsamIndex;
import org.lh.dmlj.schema.editor.command.DeleteSetOrIndexCommandCreationAssistant;
import org.lh.dmlj.schema.editor.command.IModelChangeCommand;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;

public class VsamIndexComponentEditPolicy extends ComponentEditPolicy {

	public VsamIndexComponentEditPolicy() {
		super();
	}
	
	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		@SuppressWarnings("unchecked")
		List<EditPart> editParts = deleteRequest.getEditParts(); 
		if (editParts.size() != 1 || !(editParts.get(0).getModel() instanceof VsamIndex)) {						
			return null;
		}
		VsamIndex vsamIndex = (VsamIndex) editParts.get(0).getModel();
		MemberRole memberRole = vsamIndex.getMemberRole();
		ModelChangeContext context = new ModelChangeContext(ModelChangeType.DELETE_VSAM_INDEX);
		context.putContextData(vsamIndex.getSet());
		IModelChangeCommand command = 
			DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);
		command.setContext(context);
		return (Command) command;
	}
	
}
