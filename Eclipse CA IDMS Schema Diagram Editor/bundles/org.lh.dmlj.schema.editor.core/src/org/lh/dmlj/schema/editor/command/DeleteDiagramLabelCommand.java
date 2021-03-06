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
package org.lh.dmlj.schema.editor.command;

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.Schema;

public class DeleteDiagramLabelCommand extends ModelChangeBasicCommand {
	
	private DiagramData diagramData;
	private DiagramLabel diagramLabel;
	private int	i; // index of diagram label diagram location
	
	public DeleteDiagramLabelCommand(Schema schema) {
		super("Delete diagram label");
		this.diagramData = schema.getDiagramData();
	}
	
	@Override
	public void execute() {
		diagramLabel = diagramData.getLabel();
		i = diagramData.getLocations().indexOf(diagramLabel.getDiagramLocation());
		redo();		
	}

	@Override
	public void redo() {		
		Assert.isTrue(diagramData.getLabel() != null);		
		diagramLabel.setDiagramData(null);		
		diagramData.getLocations().remove(diagramLabel.getDiagramLocation());		
	}

	@Override
	public void undo() {
		Assert.isTrue(diagramData.getLabel() == null);	
		diagramData.getLocations().add(i, diagramLabel.getDiagramLocation());		
		diagramLabel.setDiagramData(diagramData);		
	}
	
}
