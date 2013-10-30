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
package org.lh.dmlj.schema.editor.command;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.Schema;

public class DeleteDiagramLabelCommand extends Command {
	
	private DiagramLabel diagramLabel;
	private Schema 		 schema;	
	
	public DeleteDiagramLabelCommand(Schema schema) {
		super("Delete diagram label");
		this.schema = schema;
	}
	
	@Override
	public void execute() {
		
		diagramLabel = schema.getDiagramData().getLabel();
		Assert.isTrue(diagramLabel != null);
		
		diagramLabel.setDiagramData(null);
		
		schema.getDiagramData().getLocations().remove(diagramLabel.getDiagramLocation());
		
	}

	@Override
	public void undo() {
		
		Assert.isTrue(schema.getDiagramData().getLabel() == null);
	
		schema.getDiagramData().getLocations().add(diagramLabel.getDiagramLocation());
		
		diagramLabel.setDiagramData(schema.getDiagramData());
		
	}
	
	@Override
	public void redo() {
		
		Assert.isTrue(schema.getDiagramData().getLabel() != null);
		
		diagramLabel.setDiagramData(null);
		
		schema.getDiagramData().getLocations().remove(diagramLabel.getDiagramLocation());
		
	}
	
}
