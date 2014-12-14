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

import static org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory.REMOVE_ITEM;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EReference;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.command.annotation.Item;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.Owner;
import org.lh.dmlj.schema.editor.command.annotation.Reference;

@ModelChange(category=REMOVE_ITEM)
public class DeleteConnectorsCommand extends ModelChangeBasicCommand {

	@Owner 	   protected MemberRole   memberRole;
	@Item	   private ConnectionPart connectionPart2;
	@Reference private EReference 	  reference = 
		SchemaPackage.eINSTANCE.getMemberRole_ConnectionParts();		
			
	private Connector connector1;	
	private int		  connector1Index;
	private int		  connector1LocationIndex;
	private int 	  connectionPart2Index;
	private int		  connector2Index;
	private int		  connector2LocationIndex;

	public DeleteConnectorsCommand(MemberRole memberRole) {
		super("Remove connectors from connection");
		this.memberRole = memberRole;		
	}	
	
	@Override
	public void execute() {
		
		DiagramData diagramData = memberRole.getSet().getSchema().getDiagramData();
		
		// remember the first connection part's connector, together with its indexes in the 
		// connector and locations container (the schema's diagram data) 
		connector1 = memberRole.getConnectionParts().get(0).getConnector();		
		connector1Index = diagramData.getConnectors().indexOf(connector1);
		connector1LocationIndex = 
			diagramData.getLocations().indexOf(connector1.getDiagramLocation());
		
		// remember the second connection part and all of its indexes in the connector and locations 
		// container
		connectionPart2 = memberRole.getConnectionParts().get(1);
		connectionPart2Index = diagramData.getConnectionParts().indexOf(connectionPart2);		
		connector2Index = diagramData.getConnectors().indexOf(connectionPart2.getConnector());
		connector2LocationIndex = 
			diagramData.getLocations().indexOf(connectionPart2.getConnector().getDiagramLocation());
		
		// go finish the job
		redo();
		
	}
	
	@Override
	public void redo() {
		
		ConnectionPart connectionPart1 = memberRole.getConnectionParts().get(0);
		DiagramData diagramData = memberRole.getSet().getSchema().getDiagramData();

		// first connection part and connector
		for (DiagramLocation bendpoint : 
			 new ArrayList<DiagramLocation>(connectionPart2.getBendpointLocations())) {
			
			// copy all the bendpoints from the second to the first connection part
			connectionPart1.getBendpointLocations().add(bendpoint);
		}
		connectionPart1.setTargetEndpointLocation(connectionPart2.getTargetEndpointLocation());
		connectionPart1.setConnector(null);
		diagramData.getConnectors().remove(connector1);
		diagramData.getLocations().remove(connector1.getDiagramLocation());
		
		// second connection part and connector
		memberRole.getConnectionParts().remove(1);
		diagramData.getConnectionParts().remove(connectionPart2);		
		diagramData.getLocations().remove(connectionPart2.getConnector().getDiagramLocation());		
		diagramData.getConnectors().remove(connectionPart2.getConnector());
		
	}

	@Override
	public void undo() {
		
		ConnectionPart connectionPart1 = memberRole.getConnectionParts().get(0);
		DiagramData diagramData = memberRole.getSet().getSchema().getDiagramData();
		
		// first connection part and connector
		for (DiagramLocation bendpoint : connectionPart2.getBendpointLocations()) {
			// remove all the bendpoints from the second connection part
			connectionPart1.getBendpointLocations().remove(bendpoint);			
		}
		connectionPart1.setConnector(connector1);
		diagramData.getConnectors().add(connector1Index, connector1);
		diagramData.getLocations().add(connector1LocationIndex, connector1.getDiagramLocation());						
		connectionPart1.setTargetEndpointLocation(null);		
		
		// second connection part and connector
		memberRole.getConnectionParts().add(connectionPart2);
		diagramData.getConnectionParts().add(connectionPart2Index, connectionPart2);
		connectionPart2.setConnector(connectionPart2.getConnector());
		diagramData.getLocations().add(connector2LocationIndex, 
									   connectionPart2.getConnector().getDiagramLocation());
		diagramData.getConnectors().add(connector2Index, connectionPart2.getConnector()); 
		
	}
	
}
