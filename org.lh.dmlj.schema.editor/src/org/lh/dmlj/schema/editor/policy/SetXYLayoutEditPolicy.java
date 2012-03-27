package org.lh.dmlj.schema.editor.policy;

import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.editor.command.CreateConnectorCommand;

/**
 * An edit policy that enables creating connectors.
 */
public class SetXYLayoutEditPolicy extends XYLayoutEditPolicy {

	private ConnectionPart 	   connectionPart;
	private PolylineConnection figure;
	
	public SetXYLayoutEditPolicy(ConnectionPart connectionPart,
								 PolylineConnection figure) {
		super();
		this.connectionPart = connectionPart;
		this.figure = figure;
	}
	
	@Override
	protected Command getCreateCommand(CreateRequest request) {
		
		// make sure the connector tool is used and that the current
		// set line consists of only 1 part
		if (request.getNewObjectType() != Connector.class ||
			connectionPart.getMemberRole().getConnectionParts().size() > 1) {
			
			return null;
		}	
		
		// calculate the unscaled connector location...
        PrecisionPoint p = new PrecisionPoint(request.getLocation().x,
        									  request.getLocation().y); 				
        figure.translateToRelative(p);
		
		return new CreateConnectorCommand(connectionPart.getMemberRole(), p);
	}
	
}