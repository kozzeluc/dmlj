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

import static org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory.ADD_ITEM;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.command.annotation.Item;
import org.lh.dmlj.schema.editor.command.annotation.Owner;
import org.lh.dmlj.schema.editor.command.annotation.Reference;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;

@ModelChange(category=ADD_ITEM)
public class CreateDiagramLabelCommand extends Command {
		
	@Owner 	   private DiagramData  diagramData;
	@Item  	   private DiagramLabel diagramLabel;
		   	   private Point 		location;
	@Reference private EReference   reference = SchemaPackage.eINSTANCE.getDiagramData_Label();
		   	   private Dimension 	size;
	
	public CreateDiagramLabelCommand(Schema schema, Point location, Dimension size) {
		super("Add diagram label");
		diagramData = schema.getDiagramData();
		this.location = location;
		this.size = size;		
	}
	
	@Override
	public void execute() {
		
		// create the diagram label and it's diagram location, but don't them to the schema's 
		// diagram data yet...
		diagramLabel = SchemaFactory.eINSTANCE.createDiagramLabel();
		if (diagramData.getSchema().getDescription() != null && 
			!diagramData.getSchema().getDescription().trim().equals("")) {
			
			diagramLabel.setDescription(diagramData.getSchema().getDescription());
		}
		diagramLabel.setWidth((short) size.width);
		diagramLabel.setHeight((short) size.height);
		DiagramLocation diagramLocation = SchemaFactory.eINSTANCE.createDiagramLocation();
		diagramLabel.setDiagramLocation(diagramLocation);
		diagramLocation.setX(location.x);
		diagramLocation.setY(location.y);
		diagramLocation.setEyecatcher("diagram label");
		
		// all further processing is reusable when redoing the command
		redo();
		
	}	
	
	@Override
	public void redo() {
		// all we need to do here is to add the diagram label and its location to the schema's 
		// diagram data
		Assert.isTrue(diagramData.getLabel() == null);				
		diagramData.getLocations().add(diagramLabel.getDiagramLocation());					
		diagramLabel.setDiagramData(diagramData);		
	}	
	
	@Override
	public void undo() {
		// remove the diagram label and its location from the schema's diagram data
		Assert.isTrue(diagramData.getLabel() != null);		
		diagramLabel.setDiagramData(null);		
		diagramData.getLocations().remove(diagramLabel.getDiagramLocation());		
	}
	
}
