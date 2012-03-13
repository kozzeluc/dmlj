package org.lh.dmlj.schema.editor.command;

import java.util.ArrayList;
import java.util.List;

import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.editor.figure.ConnectorFigure;

public class DeleteConnectorsCommand extends AbstractConnectorsCommand {

	private ConnectionPart 		  connectionPart1;
	private ConnectionPart 		  connectionPart2;
	private List<DiagramLocation> connectionPart2Bendpoints;
	private Connector 	   		  connector1;
	private Connector 	   		  connector2;
	private DiagramData		      diagramData;
	private DiagramLocation 	  replacementBendpoint1;
	private DiagramLocation 	  replacementBendpoint2;
	private DiagramLocation		  targetEndpointLocation;	

	public DeleteConnectorsCommand(MemberRole memberRole) {
		super("Remove connectors from connection", memberRole);
		diagramData = memberRole.getSet().getSchema().getDiagramData();
	}	
	
	@Override
	public void execute() {
		
		if (connectionPart2Bendpoints == null) {			
			
			// remember the first connection part's connector and create a new
			// bendpoint at the location of that connector (relative to the
			// owner figure); the eyecatcher for the new bendpoint location will 
			// be set later
			connectionPart1 = memberRole.getConnectionParts().get(0); 			
			connector1 = connectionPart1.getConnector();			
			replacementBendpoint1 =
				SchemaFactory.eINSTANCE.createDiagramLocation();
			DiagramLocation ownerLocation;
			if (memberRole.getSet().getOwner() != null) {
				// user owned set
				ownerLocation = memberRole.getSet()
										  .getOwner()
										  .getRecord()
										  .getDiagramLocation();
			} else {
				// system owned set
				ownerLocation = 
					memberRole.getSet().getSystemOwner().getDiagramLocation();
			}
			replacementBendpoint1.setX(connector1.getDiagramLocation().getX() -
									   ownerLocation.getX() + 
									   ConnectorFigure.UNSCALED_RADIUS - 1);
			replacementBendpoint1.setY(connector1.getDiagramLocation().getY() -
									   ownerLocation.getY() +									   
					 				   ConnectorFigure.UNSCALED_RADIUS - 1);			
			
			// remember the second connection part and its connector and create 
			// a new bendpoint at the location of that connector (relative to 
			// the owner figure); the eyecatcher for the new bendpoint location 
			// will be set later
			connectionPart2 = memberRole.getConnectionParts().get(1);
			connector2 = connectionPart2.getConnector();
			replacementBendpoint2 =
				SchemaFactory.eINSTANCE.createDiagramLocation();
			replacementBendpoint2.setX(connector2.getDiagramLocation().getX() -
									   ownerLocation.getX() +									    
									   ConnectorFigure.UNSCALED_RADIUS - 1);
			replacementBendpoint2.setY(connector2.getDiagramLocation().getY() -
									   ownerLocation.getY() +									    
									   ConnectorFigure.UNSCALED_RADIUS - 1);			
			
			// create a new bendpoint for each bendpoint in the second 
			// connection part (if any); the eyecatcher for the new bendpoint
			// location will be set later
			connectionPart2Bendpoints = new ArrayList<>();
			for (DiagramLocation diagramLocation : 
				 connectionPart2.getBendpointLocations()) {				
				
				DiagramLocation copy =
					SchemaFactory.eINSTANCE.createDiagramLocation();
				copy.setX(diagramLocation.getX());
				copy.setY(diagramLocation.getY());
				connectionPart2Bendpoints.add(copy);
			}
			
			// remember the target endpoint location stored in the second 
			// connection part, if any
			targetEndpointLocation = connectionPart2.getTargetEndpointLocation();				
		}
		
		// first connection part and connector
		connectionPart1.setConnector(null);
		diagramData.getConnectors().remove(connector1);
		diagramData.getLocations().remove(connector1.getDiagramLocation());
		if (!connectionPart1.getBendpointLocations().isEmpty() ||
			!connectionPart2.getBendpointLocations().isEmpty()) {
			
			connectionPart1.getBendpointLocations().add(replacementBendpoint1);
			diagramData.getLocations().add(replacementBendpoint1);
			connectionPart1.getBendpointLocations().add(replacementBendpoint2);
			diagramData.getLocations().add(replacementBendpoint2);
			for (DiagramLocation bendpoint : connectionPart2Bendpoints) {
				connectionPart1.getBendpointLocations().add(bendpoint);
				diagramData.getLocations().add(bendpoint);
			}
			connectionPart1.setTargetEndpointLocation(targetEndpointLocation);			
		}
		// second connection part and connector
		memberRole.getConnectionParts().remove(1);
		diagramData.getConnectionParts().remove(connectionPart2);
		connectionPart2.setConnector(null);
		diagramData.getLocations().remove(connector2.getDiagramLocation());
		for (DiagramLocation bendpointLocation : 
			 connectionPart2.getBendpointLocations()) {
			
			diagramData.getLocations().remove(bendpointLocation);
		}
		
		// make sure the eye catchers in both connection part's bendpoint
		// locations contain the right (and unique) index
		fixEyecatchers();
		
		// trigger the diagram update
		diagramData.getConnectors().remove(connector2); 
		
	}

	@Override
	public void undo() {
		
		// first connection part and connector
		connectionPart1.setConnector(connector1);
		diagramData.getConnectors().add(connector1);
		diagramData.getLocations().add(connector1.getDiagramLocation());		
		connectionPart1.getBendpointLocations().remove(replacementBendpoint1);
		diagramData.getLocations().remove(replacementBendpoint1);		
		connectionPart1.getBendpointLocations().remove(replacementBendpoint2);
		diagramData.getLocations().remove(replacementBendpoint2);		
		for (DiagramLocation bendpoint : connectionPart2Bendpoints) {
			connectionPart1.getBendpointLocations().remove(bendpoint);
			diagramData.getLocations().remove(bendpoint);
		}
		connectionPart1.setTargetEndpointLocation(null);		
		
		// second connection part and connector
		memberRole.getConnectionParts().add(connectionPart2);
		diagramData.getConnectionParts().add(connectionPart2);
		connectionPart2.setConnector(connector2);
		diagramData.getLocations().add(connector2.getDiagramLocation());
		for (DiagramLocation bendpointLocation : connectionPart2Bendpoints) {
			connectionPart2.getBendpointLocations().add(bendpointLocation);
			diagramData.getLocations().add(bendpointLocation);
		}
		
		// make sure the eye catchers in both connection part's bendpoint
		// locations contain the right (and unique) index
		fixEyecatchers();
		
		// trigger the diagram update
		diagramData.getConnectors().add(connector2); 
	}
	
}