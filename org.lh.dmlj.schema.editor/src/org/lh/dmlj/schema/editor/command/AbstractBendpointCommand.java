package org.lh.dmlj.schema.editor.command;

import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.SchemaFactory;

public abstract class AbstractBendpointCommand extends Command {

	protected ConnectionPart connectionPart;
	protected int 		 	 index;	
	protected int		 	 oldX;
	protected int		 	 oldY;
	protected int		 	 x;
	protected int		 	 y;
	
	public AbstractBendpointCommand(ConnectionPart connectionPart, int index) {
		super();
		this.connectionPart = connectionPart; 
		this.index = index;
	}
	
	public AbstractBendpointCommand(ConnectionPart connectionPart, int index, 
								    int x, int y) {
		super();
		this.connectionPart = connectionPart; 
		this.index = index;
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
	
}