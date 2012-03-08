package org.lh.dmlj.schema.editor.command;

import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaFactory;

public class MoveEndpointCommand extends Command {
	
	private MemberRole memberRole;
	private Integer	   oldX;
	private Integer	   oldY;
	private boolean    owner;
	private int 	   x;
	private int 	   y;	
	
	public MoveEndpointCommand(MemberRole memberRole, int x, int y, 
										 boolean owner) {
		super();
		this.memberRole = memberRole;
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
			oldLocation = 
				memberRole.getConnectionParts().get(0).getSourceEndpointLocation();
		} else {
			oldLocation = 
				memberRole.getConnectionParts().get(0).getTargetEndpointLocation();
		}
		if (oldLocation != null) {
			oldX = oldLocation.getX();
			oldY = oldLocation.getY();
			memberRole.getSet()
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
		newLocation.setEyecatcher("set " + memberRole.getSet().getName() + p);
		
		memberRole.getSet()
		  		  .getSchema()
		  		  .getDiagramData()
		  		  .getLocations()
		  		  .add(newLocation);
	
		if (owner) {
			memberRole.getConnectionParts().get(0).setSourceEndpointLocation(null);
		} else {
			memberRole.getConnectionParts().get(0).setTargetEndpointLocation(null);
		}
		
	}
	
	@Override
	public void undo() {
		
		DiagramLocation newLocation; 
		if (owner) {
			newLocation = 
				memberRole.getConnectionParts().get(0).getSourceEndpointLocation();
		} else {
			newLocation = 
				memberRole.getConnectionParts().get(0).getTargetEndpointLocation();
		}
		memberRole.getSet()
		  		  .getSchema()
		  		  .getDiagramData()
		  		  .getLocations()
		  		  .remove(newLocation);
		
		if (oldX == null || oldY == null) {
			if (owner) {
				memberRole.getConnectionParts()
						  .get(0)
						  .setSourceEndpointLocation(null);
			} else {
				memberRole.getConnectionParts()	
						  .get(0)
						  .setTargetEndpointLocation(null);
			}
			return;
		}
		
		DiagramLocation oldLocation = 
			SchemaFactory.eINSTANCE.createDiagramLocation();
		oldLocation.setX(oldX);
		oldLocation.setY(oldY);
		String p = owner ? "owner" : "member";
		oldLocation.setEyecatcher("set " + p + " connection point" + 
								  memberRole.getSet().getName() + "(" + p + ")");
			
		memberRole.getSet()
		  		  .getSchema()
		  		  .getDiagramData()
		  		  .getLocations()
		  		  .add(oldLocation);
		
		if (owner) {
			memberRole.getConnectionParts()
					  .get(0)
					  .setSourceEndpointLocation(oldLocation);
		} else {
			memberRole.getConnectionParts()
					  .get(0)
					  .setTargetEndpointLocation(oldLocation);
		}
		
	}
	
}