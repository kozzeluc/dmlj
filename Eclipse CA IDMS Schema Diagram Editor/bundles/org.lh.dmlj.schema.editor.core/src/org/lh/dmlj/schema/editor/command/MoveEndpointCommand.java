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

import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.SchemaFactory;

public class MoveEndpointCommand extends ModelChangeBasicCommand {
		
	protected ConnectionPart connectionPart;
	
	protected int newX;
	protected int newY;
	
	protected boolean 		source;		
	private DiagramLocation oldLocation;
	private int 			oldLocationIndex;
	private DiagramLocation newLocation;
	protected ISupplier<ConnectionPart> connectionPartSupplier;	
	
	public MoveEndpointCommand(ConnectionPart connectionPart, int newX, int newY, boolean source) {
		super();
		this.connectionPart = connectionPart;
		this.newX = newX;
		this.newY = newY; 
		this.source = source;		
	}
	
	public MoveEndpointCommand(ISupplier<ConnectionPart> connectionPartSupplier, int newX, int newY, 
							   boolean source) {
		super();
		this.connectionPartSupplier = connectionPartSupplier;
		this.newX = newX;
		this.newY = newY; 
		this.source = source;		
	}	
	
	@Override
	public void execute() {		
		
		if (connectionPartSupplier != null) {
			connectionPart = connectionPartSupplier.supply();
		}
		
		DiagramData diagramData = 
			connectionPart.getMemberRole().getSet().getSchema().getDiagramData();
		
		// depending on whether we're moving the source or target endpoint, save the old location, 
		// if present, and its location in the locations container (the schema's diagram data)
		if (source) {
			oldLocation = connectionPart.getSourceEndpointLocation();
		} else {
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
