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
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.command.DeleteRecordCommandCreationAssistant;

public class RecordComponentEditPolicy extends ComponentEditPolicy {

	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		
		// make sure we are talking about a record and go get it from the delete request
 		@SuppressWarnings("unchecked")
		List<EditPart> editParts = deleteRequest.getEditParts(); 
		if (editParts.size() != 1 || !(editParts.get(0).getModel() instanceof SchemaRecord)) {						
			return null;
		}
		SchemaRecord record = (SchemaRecord) editParts.get(0).getModel();
		
		// have the command created and return it to the caller
		return (Command) DeleteRecordCommandCreationAssistant.getCommand(record);	
	}

}
