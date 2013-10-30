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
