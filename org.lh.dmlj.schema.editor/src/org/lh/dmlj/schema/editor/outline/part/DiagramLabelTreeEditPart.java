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
package org.lh.dmlj.schema.editor.outline.part;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.CommandStack;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.editor.policy.DiagramLabelComponentEditPolicy;

public class DiagramLabelTreeEditPart extends AbstractSchemaTreeEditPart<DiagramLabel> {

	public DiagramLabelTreeEditPart(DiagramLabel diagramLabel, CommandStack commandStack) {
		super(diagramLabel, commandStack);
	}
	
	@Override
	protected void createEditPolicies() {		
	
		// the next edit policy allows for the deletion of a (the) diagram label
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DiagramLabelComponentEditPolicy());
		
	}	

	@Override
	protected String getImagePath() {
		return "icons/label16.GIF";
	}

	@Override
	protected String getNodeText() {
		return "Diagram label";
	}

}
