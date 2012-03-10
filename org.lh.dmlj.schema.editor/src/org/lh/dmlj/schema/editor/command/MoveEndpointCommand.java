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
		
		// we currently don't support split connections (i.e. a set with 2
		// connection parts each with a connector attached)...
		
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
		String p = owner ? " owner endpoint" : " member endpoint";
		newLocation.setEyecatcher("set " + connectionPart.getMemberRole()
														 .getSet()
														 .getName() + p);
		
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
		String p = owner ? "owner" : "member";
		oldLocation.setEyecatcher("set " + p + " connection point" + 
								  connectionPart.getMemberRole()
								  				.getSet()
								  				.getName() + "(" + p + ")");
			
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