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
		
		DiagramLocation oldLocation;
		if (owner) {
			oldLocation = memberRole.getDiagramSourceAnchor();
		} else {
			oldLocation = memberRole.getDiagramTargetAnchor();
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
			memberRole.setDiagramSourceAnchor(newLocation);
		} else {
			memberRole.setDiagramTargetAnchor(newLocation);
		}
		
	}
	
	@Override
	public void undo() {
		
		DiagramLocation newLocation; 
		if (owner) {
			newLocation = memberRole.getDiagramSourceAnchor();
		} else {
			newLocation = memberRole.getDiagramTargetAnchor();
		}
		memberRole.getSet()
		  		  .getSchema()
		  		  .getDiagramData()
		  		  .getLocations()
		  		  .remove(newLocation);
		
		if (oldX == null || oldY == null) {
			if (owner) {
				memberRole.setDiagramSourceAnchor(null);
			} else {
				memberRole.setDiagramTargetAnchor(null);
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
			memberRole.setDiagramSourceAnchor(oldLocation);
		} else {
			memberRole.setDiagramTargetAnchor(oldLocation);
		}
		
	}
	
}