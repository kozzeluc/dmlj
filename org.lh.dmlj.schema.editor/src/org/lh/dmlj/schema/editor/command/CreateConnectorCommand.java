package org.lh.dmlj.schema.editor.command;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Point;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaFactory;

public class CreateConnectorCommand extends AbstractConnectorsCommand {
	
	private ConnectionPart 	  connectionPart2;
	private Connector[]    	  connector = new Connector[2];
	private DiagramLocation[] diagramLocation = new DiagramLocation[2];	
	private Point 		   	  location;	

	public CreateConnectorCommand(MemberRole memberRole, Point location) {
		super("Add connectors to connection", memberRole);
		this.location = location;
	}	
	
	@Override
	public void execute() {
		
		Assert.isTrue(memberRole.getConnectionParts().size() == 1);
		
		// instantiate the new model objects (but only once)
		if (connectionPart2 == null) {
			
			// location for first connector: where the user clicked the mouse
			// button
			diagramLocation[0] = 
				SchemaFactory.eINSTANCE.createDiagramLocation();
			diagramLocation[0].setX(location.x);
			diagramLocation[0].setY(location.y);
			diagramLocation[0].setEyecatcher("set connector[0] " +
					 						 memberRole.getSet().getName() + 
					 						 " (" +
					 						 memberRole.getRecord().getName() +
					 						 ")");
			
			// location for second connector: right next to the first connector
			diagramLocation[1] = 
				SchemaFactory.eINSTANCE.createDiagramLocation();
			diagramLocation[1].setX(location.x + 20);
			diagramLocation[1].setY(location.y);
			diagramLocation[1].setEyecatcher("set connector[1] " +
											 memberRole.getSet().getName() + 
											 " (" +
											 memberRole.getRecord().getName() +
											 ")");
			
			// create the first connector and set its location
			connector[0] = SchemaFactory.eINSTANCE.createConnector();
			connector[0].setDiagramLocation(diagramLocation[0]);
			
			// create the second connector and set its location
			connector[1] = SchemaFactory.eINSTANCE.createConnector();
			connector[1].setDiagramLocation(diagramLocation[1]);
			
			// create the second connection part, set its source and target 
			// endpoint locations, if any, and its connector
			connectionPart2 = SchemaFactory.eINSTANCE.createConnectionPart();
			connectionPart2.setSourceEndpointLocation(connector[1].getDiagramLocation());
			DiagramLocation targetEndpointLocation = 
				memberRole.getConnectionParts().get(0).getTargetEndpointLocation();
			connectionPart2.setTargetEndpointLocation(targetEndpointLocation);
			connectionPart2.setConnector(connector[1]);
		}
		
		// first connection part and connector
		memberRole.getConnectionParts()
				  .get(0)
				  .setTargetEndpointLocation(null);
		memberRole.getConnectionParts()
		  		  .get(0)
		  		  .setConnector(connector[0]);
		memberRole.getSet()
		  		  .getSchema()
		  		  .getDiagramData()
		  		  .getConnectors()
		  		  .add(connector[0]);
		memberRole.getSet()
		  		  .getSchema()
		  		  .getDiagramData()
		  		  .getLocations()
		  		  .add(connector[0].getDiagramLocation());
		
		// second connection part and connector
		memberRole.getConnectionParts().add(connectionPart2);
		memberRole.getSet()
				  .getSchema()
				  .getDiagramData()
				  .getConnectionParts()
				  .add(connectionPart2);				
		memberRole.getSet()
		  		  .getSchema()
		  		  .getDiagramData()
		  		  .getLocations()
		  		  .add(connector[1].getDiagramLocation());
		
		// make sure the eye catchers in both connection part's bendpoint
		// locations contain the right (and unique) index
		fixEyecatchers();
		
		// trigger the diagram update
		memberRole.getSet()
		  		  .getSchema()
		  		  .getDiagramData()
		  		  .getConnectors()
		  		  .add(connector[1]); 
				
	}
	
	@Override
	public void undo() {
	
		Assert.isTrue(memberRole.getConnectionParts().size() == 2);
		
		// first connection part and connector
		DiagramLocation targetEndpointLocation = 
			memberRole.getConnectionParts()
				      .get(1)
				  	  .getTargetEndpointLocation();
		memberRole.getConnectionParts()
				  .get(0)
				  .setTargetEndpointLocation(targetEndpointLocation);
		memberRole.getConnectionParts()
		  		  .get(0)
		  		  .setConnector(null);
		memberRole.getSet()
		  		  .getSchema()
		  		  .getDiagramData()
		  		  .getConnectors()
		  		  .remove(connector[0]);
		memberRole.getSet()
		  		  .getSchema()
		  		  .getDiagramData()
		  		  .getLocations()
		  		  .remove(connector[0].getDiagramLocation());
		
		// second connection part and connector
		memberRole.getConnectionParts().remove(connectionPart2);
		memberRole.getSet()
				  .getSchema()
				  .getDiagramData()
				  .getConnectionParts()
				  .remove(connectionPart2);				
		memberRole.getSet()
		  		  .getSchema()
		  		  .getDiagramData()
		  		  .getLocations()
		  		  .remove(connector[1].getDiagramLocation());
		
		// make sure the eye catchers in both connection part's bendpoint
		// locations contain the right (and unique) index
		fixEyecatchers();
		
		// trigger the diagram update
		memberRole.getSet()
		  		  .getSchema()
		  		  .getDiagramData()
		  		  .getConnectors()
		  		  .remove(connector[1]);		
		
	}
}