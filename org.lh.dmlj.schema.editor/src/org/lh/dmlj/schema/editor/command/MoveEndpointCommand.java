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

import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.SchemaFactory;

public class MoveEndpointCommand extends Command {
	
	private ConnectionPart connectionPart;
	private Integer	   	   oldX;
	private Integer	   	   oldY;
	private boolean    	   owner;
	private int 	   	   x;
	private int 	   	   y;	
	
	public MoveEndpointCommand(ConnectionPart connectionPart, int x, int y, 
							   boolean owner) {
		super();
		this.connectionPart = connectionPart;
		this.x = x;
		this.y = y; 
		this.owner = owner;
	}
	
	@Override
	public void execute() {		
		
		DiagramLocation oldLocation;
		if (owner) {
			oldLocation = connectionPart.getSourceEndpointLocation();
		} else {
			oldLocation = connectionPart.getTargetEndpointLocation();
		}
		if (oldLocation != null) {
			oldX = oldLocation.getX();
			oldY = oldLocation.getY();
			connectionPart.getMemberRole()
						  .getSet()
						  .getSchema()
						  .getDiagramData()
						  .getLocations()
						  .remove(oldLocation);
		}
		
		DiagramLocation newLocation = 
			SchemaFactory.eINSTANCE.createDiagramLocation();
		newLocation.setX(x);
		newLocation.setY(y);
		String p = owner ? " owner endpoint (" : " member endpoint (";
		newLocation.setEyecatcher("set " + 
								  connectionPart.getMemberRole()
												.getSet()
												.getName() + p +
												connectionPart.getMemberRole()
															  .getRecord()
														 	  .getName() + ")");
		
		connectionPart.getMemberRole()
					  .getSet()
					  .getSchema()
					  .getDiagramData()
					  .getLocations()
					  .add(newLocation);
	
		if (owner) {
			connectionPart.setSourceEndpointLocation(newLocation);
		} else {
			connectionPart.setTargetEndpointLocation(newLocation);
		}
		
	}
	
	@Override
	public void undo() {
		
		DiagramLocation newLocation; 
		if (owner) {
			newLocation = connectionPart.getSourceEndpointLocation();
		} else {
			newLocation = connectionPart.getTargetEndpointLocation();
		}
		connectionPart.getMemberRole()
					  .getSet()
					  .getSchema()
					  .getDiagramData()
					  .getLocations()
					  .remove(newLocation);
		
		if (oldX == null || oldY == null) {
			if (owner) {
				connectionPart.setSourceEndpointLocation(null);
			} else {
				connectionPart.setTargetEndpointLocation(null);
			}
			return;
		}
		
		DiagramLocation oldLocation = 
			SchemaFactory.eINSTANCE.createDiagramLocation();
		oldLocation.setX(oldX);
		oldLocation.setY(oldY);
		String p = owner ? "owner " : "member ";
		oldLocation.setEyecatcher("set " + p + " endpoint " + 
								  connectionPart.getMemberRole()
								  				.getSet()
								  				.getName() + " (" + 
								  connectionPart.getMemberRole()
								  				.getRecord()
								  				.getName() + ")");
			
		connectionPart.getMemberRole()
					  .getSet()
					  .getSchema()
					  .getDiagramData()
					  .getLocations()
					  .add(oldLocation);
		
		if (owner) {
			connectionPart.setSourceEndpointLocation(oldLocation);
		} else {
			connectionPart.setTargetEndpointLocation(oldLocation);
		}
		
	}
	
}
