package org.lh.dmlj.schema.editor.command;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaFactory;

public class CreateConnectorCommand extends Command {
	
	private ConnectionPart 	  connectionPart2;
	private Connector[]    	  connector = new Connector[2];
	private DiagramLocation[] diagramLocation = new DiagramLocation[2];	
	private Point 		   	  location;
	private MemberRole     	  memberRole;

	public CreateConnectorCommand(MemberRole memberRole, Point location) {
		super();
		this.memberRole = memberRole;
		this.location = location;
	}
	
	@Override
	public boolean canUndo() {
		// temporary disable undoability
		return false;
	}
	
	@Override
	public void execute() {
		
		Assert.isTrue(memberRole.getConnectionParts().size() == 1);
		
		// instantiate the new model objects (but only once)
		if (connectionPart2 == null) {
			
			diagramLocation[0] = 
				SchemaFactory.eINSTANCE.createDiagramLocation();
			diagramLocation[0].setX(location.x);
			diagramLocation[0].setY(location.y);
			diagramLocation[0].setEyecatcher("set connector[0] " +
					 						 memberRole.getSet().getName() + 
					 						 " (" +
					 						 memberRole.getRecord().getName() +
					 						 ")");
			
			diagramLocation[1] = 
				SchemaFactory.eINSTANCE.createDiagramLocation();
			diagramLocation[1].setX(location.x + 20);
			diagramLocation[1].setY(location.y);
			diagramLocation[1].setEyecatcher("set connector[1] " +
											 memberRole.getSet().getName() + 
											 " (" +
											 memberRole.getRecord().getName() +
											 ")");
			
			connector[0] = SchemaFactory.eINSTANCE.createConnector();
			connector[0].setDiagramLocation(diagramLocation[0]);
			
			connector[1] = SchemaFactory.eINSTANCE.createConnector();
			connector[1].setDiagramLocation(diagramLocation[1]);
			
			connectionPart2 = SchemaFactory.eINSTANCE.createConnectionPart();
			connectionPart2.setSourceEndpointLocation(connector[1].getLocation());
			DiagramLocation targetEndpointLocation = 
				memberRole.getConnectionParts().get(0).getTargetEndpointLocation();
			connectionPart2.setTargetEndpointLocation(targetEndpointLocation);
			connectionPart2.setConnector(connector[1]);
		}
		
		// first connection part and connector
		memberRole.getConnectionParts()
				  .get(0)
				  .setTargetEndpointLocation(connector[0].getLocation());
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
		  		  .getConnectors()
		  		  .add(connector[1]);
		memberRole.getSet()
		  		  .getSchema()
		  		  .getDiagramData()
		  		  .getLocations()
		  		  .add(connector[1].getDiagramLocation());
	}
	
	@Override
	public void undo() {
		// TODO Auto-generated method stub
		super.undo();
	}
}