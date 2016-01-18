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
package org.lh.dmlj.schema.editor.command;

import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.SchemaFactory;

public abstract class AbstractBendpointCommand extends ModelChangeBasicCommand {

	protected ConnectionPart connectionPart;
	protected int 		 	 connectionPartIndex;	
	protected int		 	 oldX;
	protected int		 	 oldY;
	protected int		 	 x;
	protected int		 	 y;
	
	public AbstractBendpointCommand(ConnectionPart connectionPart, int connectionPartIndex) {
		super();
		this.connectionPart = connectionPart; 
		this.connectionPartIndex = connectionPartIndex;
	}
	
	public AbstractBendpointCommand(ConnectionPart connectionPart, int connectionPartIndex, 
								    int x, int y) {
		super();
		this.connectionPart = connectionPart; 
		this.connectionPartIndex = connectionPartIndex;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Creates a new bendpoint and inserts it at the right index in the 
	 * ConnectionPart (which represents the connection) and as the last element 
	 * of the schema container's diagram data.
	 * @param index the index at which the bendpoints needs to be inserted
	 * @param x the bendpoint's (absolute) x coordinate
	 * @param y the bendpoint's (absolute) y coordinate
	 * @return the newly created and inserted bendpoint
	 */
	protected DiagramLocation insertBendpoint(int index, int x, int y) {
	
		// The line representing a set consists of either 1 or 2 connections; in
		// the latter case a connector is present on both source and target 
		// endpoint locations; the numbering of the eyecatcher indexes for the 
		// second ConnectionPart's bendpoints starts at 1 plus the last 
		// bendpoint of the first ConnectionPart (or zero if the first 
		// ConnectionPart has no bendpoints).  Although the eyecatcher is 
		// purely documentational, this avoids duplicate eyecatcher indexes for
		// a set.
		int eyecatcherIndex = index;
		if (connectionPart.getMemberRole()
						  .getConnectionParts()
						  .indexOf(connectionPart) > 0) {
			
			eyecatcherIndex += connectionPart.getMemberRole()
										     .getConnectionParts()
										     .get(0)
										     .getBendpointLocations()
										     .size();
		}
		
		// create the bendpoint...
		DiagramLocation bendpoint = 
			SchemaFactory.eINSTANCE.createDiagramLocation();
		bendpoint.setX(x);
		bendpoint.setY(y);
		bendpoint.setEyecatcher("bendpoint [" + eyecatcherIndex + "] set " + 
								connectionPart.getMemberRole()
											  .getSet()
											  .getName() + " (" + 
								connectionPart.getMemberRole()
											  .getRecord()
											  .getName() + ")");
		
		// add it to the schema (container)...
		connectionPart.getMemberRole()
					  .getSet()
					  .getSchema()
					  .getDiagramData()
					  .getLocations()
					  .add(bendpoint);

		// insert it at the right place in the connection...
		connectionPart.getBendpointLocations().add(index, bendpoint);
		
		// modify the eyecatcher of subsequent bendpoints, if any...
		int j = connectionPart.getBendpointLocations().size();
		for (int i = index + 1; i < j; i++) {
			DiagramLocation aBendpoint = 
				connectionPart.getBendpointLocations().get(i);
			aBendpoint.setEyecatcher("bendpoint [" + ++eyecatcherIndex + 
									 "] set " + 
									 connectionPart.getMemberRole()
									 			   .getSet()
									 			   .getName() + " (" + 
									 connectionPart.getMemberRole()
									 			   .getRecord()
									 			   .getName() + ")");
		}
		if (connectionPart.getMemberRole().getConnectionParts().size() > 1 &&
			connectionPart.getMemberRole()
				  		  .getConnectionParts()
				  		  .indexOf(connectionPart) == 0) {
			
			for (DiagramLocation aBendpoint : connectionPart.getMemberRole()
															.getConnectionParts()
															.get(1)
															.getBendpointLocations()) {
				
				aBendpoint.setEyecatcher("bendpoint [" + ++eyecatcherIndex + 
						 				 "] set " + 
						 				 connectionPart.getMemberRole()
						 				 			   .getSet()
						 				 			   .getName() + " (" + 
						 				 connectionPart.getMemberRole()
						 				 			   .getRecord()
						 				 			   .getName() + ")");
			}
		}
		
		return bendpoint;
		
	}
	
	/**
	 * Removes a bendpoint from the ConnectionPart (which represents the 
	 * connection and the schema (container).  Upon return, oldX and oldY will 
	 * contain the removed bendpoint's x and y attributes.
	 * @param index the index of the bendpoint to remove
	 */
	protected void removeBendpoint(int index) {
		
		// go grab the bendpoint and save the x and y attributes...
		DiagramLocation bendpoint = 
			connectionPart.getBendpointLocations().get(index);
		oldX = bendpoint.getX();
		oldY = bendpoint.getY();
			
		// remove it from the schema (container)...
		connectionPart.getMemberRole()
					  .getSet()
					  .getSchema()
					  .getDiagramData()
					  .getLocations()
					  .remove(bendpoint);
		
		// remove it from the ConnectionPart...
		connectionPart.getBendpointLocations().remove(index);
		
		// The line representing a set consists of either 1 or 2 connections; in
		// the latter case a connector is present on both source and target 
		// endpoint locations; the numbering of the eyecatcher indexes for the 
		// second ConnectionPart's bendpoints starts at 1 plus the last 
		// bendpoint of the first ConnectionPart (or zero if the first 
		// ConnectionPart has no bendpoints).  Although the eyecatcher is 
		// purely documentational, this avoids duplicate eyecatcher indexes for
		// a set.
		int eyecatcherIndex = index;
		if (connectionPart.getMemberRole()
						  .getConnectionParts()
						  .indexOf(connectionPart) > 0) {
			
			eyecatcherIndex += connectionPart.getMemberRole()
										     .getConnectionParts()
										     .get(0)
										     .getBendpointLocations()
										     .size();
		}		
		
		// modify the eyecatcher of subsequent bendpoints, if any...
		int j = connectionPart.getBendpointLocations().size();
		for (int i = index; i < j; i++) {
			DiagramLocation aBendpoint = 
				connectionPart.getBendpointLocations().get(i);
			aBendpoint.setEyecatcher("bendpoint [" + eyecatcherIndex++ + 
									 "] set " + 
									 connectionPart.getMemberRole()
									 			   .getSet()
									 			   .getName() + " (" + 
									 connectionPart.getMemberRole()
									 			   .getRecord()
									 			   .getName() + ")");
		}
		if (connectionPart.getMemberRole().getConnectionParts().size() > 1 &&
			connectionPart.getMemberRole()
				  		  .getConnectionParts()
				  		  .indexOf(connectionPart) == 0) {
				
			for (DiagramLocation aBendpoint : connectionPart.getMemberRole()
															.getConnectionParts()
															.get(1)
															.getBendpointLocations()) {
					
				aBendpoint.setEyecatcher("bendpoint [" + eyecatcherIndex++ + 
						 				 "] set " + 
						 				 connectionPart.getMemberRole()
						 				 			   .getSet()
						 				 			   .getName() + " (" + 
						 				 connectionPart.getMemberRole()
						 				 			   .getRecord()
						 				 			   .getName() + ")");
			}
		}		
	}
	
	protected void restoreBendpoint(DiagramLocation bendpoint, int connectionPartIndex,
									int locationsIndex) {
		
		// The line representing a set consists of either 1 or 2 connections; in the latter case a 
		// connector is present on both source and target endpoint locations; the numbering of the 
		// eyecatcher indexes for the second ConnectionPart's bendpoints starts at 1 plus the last 
		// bendpoint of the first ConnectionPart (or zero if the first ConnectionPart has no 
		// bendpoints).  Although the eyecatcher is purely documentational, this avoids duplicate 
		// eyecatcher indexes for a set.
		int eyecatcherIndex = connectionPartIndex;
		if (connectionPart.getMemberRole()
						  .getConnectionParts()
						  .indexOf(connectionPart) > 0) {
			
			eyecatcherIndex += connectionPart.getMemberRole()
										     .getConnectionParts()
										     .get(0)
										     .getBendpointLocations()
										     .size();
		}		
		
		// add the bendpoint to the schema again; we need the locations index (inside the diagram
		// data container) here too, not when we're adding a bendpoint, but when undoing the
		// removal of a bendpoint - -1 has to be treated as adding the bendpoint to the end of the 
		// list
		if (locationsIndex > -1) {
			connectionPart.getMemberRole()
						  .getSet()
						  .getSchema()
						  .getDiagramData()
						  .getLocations()
						  .add(locationsIndex, bendpoint);
		} else {
			connectionPart.getMemberRole()
			  .getSet()
			  .getSchema()
			  .getDiagramData()
			  .getLocations()
			  .add(bendpoint);
		}

		// insert it at the right place in the connection again
		connectionPart.getBendpointLocations().add(connectionPartIndex, bendpoint);
		
		// modify the eyecatcher of subsequent bendpoints, if any...
		int j = connectionPart.getBendpointLocations().size();
		for (int i = connectionPartIndex + 1; i < j; i++) {
			DiagramLocation aBendpoint = 
				connectionPart.getBendpointLocations().get(i);
			aBendpoint.setEyecatcher("bendpoint [" + ++eyecatcherIndex + 
									 "] set " + 
									 connectionPart.getMemberRole()
									 			   .getSet()
									 			   .getName() + " (" + 
									 connectionPart.getMemberRole()
									 			   .getRecord()
									 			   .getName() + ")");
		}
		if (connectionPart.getMemberRole().getConnectionParts().size() > 1 &&
			connectionPart.getMemberRole()
				  		  .getConnectionParts()
				  		  .indexOf(connectionPart) == 0) {
			
			for (DiagramLocation aBendpoint : connectionPart.getMemberRole()
															.getConnectionParts()
															.get(1)
															.getBendpointLocations()) {
				
				aBendpoint.setEyecatcher("bendpoint [" + ++eyecatcherIndex + 
						 				 "] set " + 
						 				 connectionPart.getMemberRole()
						 				 			   .getSet()
						 				 			   .getName() + " (" + 
						 				 connectionPart.getMemberRole()
						 				 			   .getRecord()
						 				 			   .getName() + ")");
			}
		}		
		
	}	
	
}
