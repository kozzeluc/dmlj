package org.lh.dmlj.schema.editor.command;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaFactory;

public class LockEndpointsCommand extends Command {

	private MemberRole memberRole;
	
	private Point 	   ownerEndpoint;
	private Point 	   memberEndpoint;
	
	private boolean    ownerEndpointSet = false;
	private boolean    memberEndpointSet = false;
	
	public LockEndpointsCommand(MemberRole memberRole, Point ownerEndpoint,
								Point memberEndpoint) {
		super();
		this.memberRole = memberRole;
		this.ownerEndpoint = ownerEndpoint;
		this.memberEndpoint = memberEndpoint;
	}
	
	@Override
	public void execute() {	
		if (memberRole.getConnectionParts()
					  .get(0)
					  .getSourceEndpointLocation() == null && 
			ownerEndpoint != null) {
			
			// the owner endpoint will only be added for user owned sets			
			DiagramLocation location = 
				SchemaFactory.eINSTANCE.createDiagramLocation();
			location.setX(ownerEndpoint.x);
			location.setY(ownerEndpoint.y);
			location.setEyecatcher("set " + memberRole.getSet().getName() + 
								   " owner endpoint (" +
								   memberRole.getRecord().getName() + ")");
			memberRole.getConnectionParts()
			  		  .get(0)
			  		  .setSourceEndpointLocation(location);
			
			memberRole.getSet()
	  		  		  .getSchema()
	  		  		  .getDiagramData()
	  		  		  .getLocations()
	  		  		  .add(location);
			
			ownerEndpointSet = true;
		}
		if (memberRole.getConnectionParts()
					  .get(memberRole.getConnectionParts().size() - 1)
					  .getTargetEndpointLocation() == null) {
			
			DiagramLocation location = 
				SchemaFactory.eINSTANCE.createDiagramLocation();
			location.setX(memberEndpoint.x);
			location.setY(memberEndpoint.y);
			location.setEyecatcher("set " + memberRole.getSet().getName() + 
								   " member endpoint (" +
								   memberRole.getRecord().getName() + ")");
			memberRole.getConnectionParts()
			  		  .get(memberRole.getConnectionParts().size() - 1)
					  .setTargetEndpointLocation(location);
				
			memberRole.getSet()
			  		  .getSchema()
			  		  .getDiagramData()
			  		  .getLocations()
			  		  .add(location);
			
			memberEndpointSet = true;
		}
	}
	
	@Override
	public void undo() {
		if (memberEndpointSet) {
			
			DiagramLocation location = 
				memberRole.getConnectionParts().get(0).getTargetEndpointLocation();
			
			memberRole.getSet()
	  		  		  .getSchema()
	  		  		  .getDiagramData()
	  		  		  .getLocations()
	  		  		  .remove(location);
					
			memberRole.getConnectionParts().get(0).setTargetEndpointLocation(null);
			
		}
		if (ownerEndpointSet) {
			
			DiagramLocation location = 
				memberRole.getConnectionParts().get(0).getSourceEndpointLocation();
			
			memberRole.getSet()
	  		  		  .getSchema()
	  		  		  .getDiagramData()
	  		  		  .getLocations()
	  		  		  .remove(location);
					
			memberRole.getConnectionParts().get(0).setSourceEndpointLocation(null);
			
		}
	}
}