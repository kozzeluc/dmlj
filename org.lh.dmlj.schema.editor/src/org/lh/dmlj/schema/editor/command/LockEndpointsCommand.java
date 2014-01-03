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

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.command.annotation.Features;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.Owner;

@ModelChange(category=SET_FEATURES)
public class LockEndpointsCommand extends Command {

	@Owner	  private MemberRole 			memberRole;
	@Features private EStructuralFeature[] 	features = new EStructuralFeature[] {
		SchemaPackage.eINSTANCE.getConnectionPart_SourceEndpointLocation(),
		SchemaPackage.eINSTANCE.getConnectionPart_TargetEndpointLocation()
	};	
	
	private Point 			newSourceEndpoint;
	private DiagramLocation newSourceEndpointLocation;
	private Point 			newTargetEndpoint;
	private DiagramLocation newTargetEndpointLocation;	
	
	public LockEndpointsCommand(MemberRole memberRole, Point newSourceEndpoint, 
								Point newTargetEndpoint) {
		
		super();
		this.memberRole = memberRole;
		this.newSourceEndpoint = newSourceEndpoint;
		this.newTargetEndpoint = newTargetEndpoint;
	}
	
	@Override
	public void execute() {	
		
		// prepare the source endpoint location if needed
		DiagramLocation oldSourceEndpointLocation = 
			memberRole.getConnectionParts().get(0).getSourceEndpointLocation();		
		if (oldSourceEndpointLocation == null && newSourceEndpoint != null) {	
			// the source endpoint will only be added for user owned sets			
			newSourceEndpointLocation = SchemaFactory.eINSTANCE.createDiagramLocation();
			newSourceEndpointLocation.setX(newSourceEndpoint.x);
			newSourceEndpointLocation.setY(newSourceEndpoint.y);
			newSourceEndpointLocation.setEyecatcher("set " + memberRole.getSet().getName() + 
								   					" owner endpoint (" +
								   					memberRole.getRecord().getName() + ")");								
		}
		
		// prepare the target endpoint location if needed
		DiagramLocation oldTargetEndpointLocation = 
			memberRole.getConnectionParts()
					  .get(memberRole.getConnectionParts().size() - 1)
					  .getTargetEndpointLocation();
		if (oldTargetEndpointLocation == null && newTargetEndpoint != null) {			
			newTargetEndpointLocation = SchemaFactory.eINSTANCE.createDiagramLocation();
			newTargetEndpointLocation.setX(newTargetEndpoint.x);
			newTargetEndpointLocation.setY(newTargetEndpoint.y);
			newTargetEndpointLocation.setEyecatcher("set " + memberRole.getSet().getName() + 
								   					" member endpoint (" +
								   					memberRole.getRecord().getName() + ")");									
		}
		
		// go finish the job
		redo();
		
	}
	
	@Override
	public void redo() {
		
		DiagramData diagramData = memberRole.getSet().getSchema().getDiagramData();
		
		// set the source endpoint location if needed
		if (newSourceEndpointLocation != null) {
			memberRole.getConnectionParts()
					  .get(0)
					  .setSourceEndpointLocation(newSourceEndpointLocation);				
			diagramData.getLocations().add(newSourceEndpointLocation);
		}
		
		// set the target endpoint location if needed
		if (newTargetEndpointLocation != null) {
			memberRole.getConnectionParts()
	  		  		  .get(memberRole.getConnectionParts().size() - 1)
	  		  		  .setTargetEndpointLocation(newTargetEndpointLocation);		
			diagramData.getLocations().add(newTargetEndpointLocation);			
		}
		
	}
	
	@Override
	public void undo() {
		
		DiagramData diagramData = memberRole.getSet().getSchema().getDiagramData();
		
		// remove the source endpoint location if needed
		if (newSourceEndpointLocation != null) {								
			memberRole.getConnectionParts().get(0).setSourceEndpointLocation(null);	
			diagramData.getLocations().remove(newSourceEndpointLocation);
		}
		
		// remove the target endpoint location if needed
		if (newTargetEndpointLocation != null) {
			memberRole.getConnectionParts()
	  		  		  .get(memberRole.getConnectionParts().size() - 1)
	  		  		  .setTargetEndpointLocation(null);
			diagramData.getLocations().remove(newTargetEndpointLocation);						
		}
		
	}
}
