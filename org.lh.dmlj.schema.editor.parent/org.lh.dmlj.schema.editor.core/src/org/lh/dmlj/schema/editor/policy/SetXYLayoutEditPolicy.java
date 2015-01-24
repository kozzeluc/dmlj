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
package org.lh.dmlj.schema.editor.policy;

import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.editor.command.CreateConnectorCommand;
import org.lh.dmlj.schema.editor.command.ModelChangeBasicCommand;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;

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
		
        ModelChangeContext context = new ModelChangeContext(ModelChangeType.ADD_CONNECTORS);
        context.putContextData(connectionPart.getMemberRole());
        ModelChangeBasicCommand command = 
			new CreateConnectorCommand(connectionPart.getMemberRole(), p);
		command.setContext(context);
		return command;
	}
	
}
