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

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.editor.command.DeleteDiagramLabelCommand;
import org.lh.dmlj.schema.editor.command.ModelChangeBasicCommand;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.outline.part.DiagramLabelTreeEditPart;
import org.lh.dmlj.schema.editor.part.DiagramLabelEditPart;

/*
 * An edit policy that allows for the deletion of a (the) diagram label.
 */
public class DiagramLabelComponentEditPolicy extends ComponentEditPolicy {
	
	public DiagramLabelComponentEditPolicy() {
		super();
	}
	
	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		
		List<?> editParts = deleteRequest.getEditParts(); 
		if (editParts.size() > 1) {
			// if more than 1 items are selected in the diagram; don't allow to delete the diagram
			// label by returning null
			return null;
		}
		Assert.isTrue(editParts.size() == 1, "exactly 1 diagram label expected");
		Assert.isTrue(editParts.get(0) instanceof DiagramLabelEditPart ||
					  editParts.get(0) instanceof DiagramLabelTreeEditPart, 
					  "expected a diagram label");
		
		DiagramLabel diagramLabel;
		if (editParts.get(0) instanceof DiagramLabelEditPart) {
			DiagramLabelEditPart editPart = (DiagramLabelEditPart) editParts.get(0); 
			diagramLabel = editPart.getModel();
		} else {
			DiagramLabelTreeEditPart editPart = (DiagramLabelTreeEditPart) editParts.get(0); 
			diagramLabel = editPart.getModel();
		}
		
		ModelChangeContext context = new ModelChangeContext(ModelChangeType.DELETE_DIAGRAM_LABEL);
		ModelChangeBasicCommand command = 
			new DeleteDiagramLabelCommand(diagramLabel.getDiagramData().getSchema());
		command.setContext(context);
		return command;
		
	}	
	
}
