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

import static org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory.SET_FEATURES;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.command.annotation.Features;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.Owner;

@ModelChange(category=SET_FEATURES)
public class MoveEndpointCommand extends Command {
		
	@Owner 	  private ConnectionPart 		connectionPart;
	@Features private EStructuralFeature[] 	features;
	
	private int newX;
	private int newY;
	
	private boolean 		source;		
	private DiagramLocation oldLocation;
	private int 			oldLocationIndex;
	private DiagramLocation newLocation;	
	
	public MoveEndpointCommand(ConnectionPart connectionPart, int newX, int newY, boolean source) {
		super();
		this.connectionPart = connectionPart;
		this.newX = newX;
		this.newY = newY; 
		this.source = source;		
	}
	
	@Override
	public void execute() {		
		
		DiagramData diagramData = 
			connectionPart.getMemberRole().getSet().getSchema().getDiagramData();
		
		// depending on whether we're moving the source or target endpoint, set the feature and save
		// the old location, if present, and its location in the locations container (the schema's 
		// diagram data)
		if (source) {
			features = 
				new EStructuralFeature[] {SchemaPackage.eINSTANCE
													   .getConnectionPart_SourceEndpointLocation()};
			oldLocation = connectionPart.getSourceEndpointLocation();
		} else {
			features = 
				new EStructuralFeature[] {SchemaPackage.eINSTANCE
													   .getConnectionPart_TargetEndpointLocation()};
			oldLocation = connectionPart.getTargetEndpointLocation();
		}		
		if (oldLocation != null) {
			oldLocationIndex = diagramData.getLocations().indexOf(oldLocation);
		}
		
		// create the new location
		newLocation = SchemaFactory.eINSTANCE.createDiagramLocation();
		newLocation.setX(newX);
		newLocation.setY(newY);
		String p = source ? " owner endpoint (" : " member endpoint (";
		newLocation.setEyecatcher("set " + connectionPart.getMemberRole().getSet().getName() + p +
								  connectionPart.getMemberRole().getRecord().getName() + ")");
		
		// go finish the job
		redo();
		
	}
	
	@Override
	public void redo() {

		DiagramData diagramData = 
			connectionPart.getMemberRole().getSet().getSchema().getDiagramData(); 
		
		// remove the old location, if any, from the locations container (the schema's diagram data)
		if (oldLocation != null) {
			diagramData.getLocations().remove(oldLocation);
		}		
		
		// set the new source or target endpoint
		if (source) {
			connectionPart.setSourceEndpointLocation(newLocation);
		} else {
			connectionPart.setTargetEndpointLocation(newLocation);
		}
		
		// add the new location to the locations container
		diagramData.getLocations().add(newLocation);
		
	}
	
	@Override
	public void undo() {
		
		DiagramData diagramData = 
			connectionPart.getMemberRole().getSet().getSchema().getDiagramData(); 
				
		// remove the new location from the locations container (the schema's diagram data)
		diagramData.getLocations().remove(newLocation);
		
		// restore the old source or target endpoint (which might be null)
		if (source) {
			connectionPart.setSourceEndpointLocation(oldLocation);
		} else {
			connectionPart.setTargetEndpointLocation(oldLocation);
		}
		
		// if applicable: add the old location to the locations container again, at its original 
		// position
		if (oldLocation != null) {
			diagramData.getLocations().add(oldLocationIndex, oldLocation);
		}
		
	}
	
}
