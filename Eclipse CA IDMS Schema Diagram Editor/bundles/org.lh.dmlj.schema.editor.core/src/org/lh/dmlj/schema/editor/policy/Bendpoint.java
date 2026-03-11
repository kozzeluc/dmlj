/**
 * Copyright (C) 2026  Luc Hermans
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

import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.DiagramNode;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.editor.figure.ConnectorFigure;

record Bendpoint(ConnectionPart connectionPart, DiagramLocation diagramLocation) {
	
	static Bendpoint lastOf(ConnectionPart connectionPart) {
		var set = connectionPart.getMemberRole().getSet();
		if (set.getOwner() == null) {
			throw new IllegalArgumentException("not a connectionPart for a user owned set: %s".formatted(set.getName()));
		}
		return new Bendpoint(connectionPart, connectionPart.getBendpointLocations().get(connectionPart.getBendpointLocations().size() - 1));
	}
	
	boolean isHorizontalLineToTargetEndpointLocation() {
		var absoluteLastBendPointY = getSourceDiagramNode().getDiagramLocation().getY() + diagramLocation.getY();
		var absoluteTargetEndpointLocationY = getTargetDiagramNode().getDiagramLocation().getY() + getTargetEndpointLocation().getY();
		return Math.abs(absoluteLastBendPointY - absoluteTargetEndpointLocationY) < 10;
	}
	
	boolean isVerticalLineToTargetEndpointLocation() {
		var absoluteLastBendPointX = getSourceDiagramNode().getDiagramLocation().getX() + diagramLocation.getX();
		var absoluteTargetEndpointLocationX = getTargetDiagramNode().getDiagramLocation().getX() + getTargetEndpointLocation().getX();
		return Math.abs(absoluteLastBendPointX - absoluteTargetEndpointLocationX) < 10;
	}
	
	private DiagramNode getSourceDiagramNode() {
		return connectionPart.getMemberRole().getSet().getOwner().getRecord();
	}
	
	private DiagramNode getTargetDiagramNode() {
		if (connectionPart.getConnector() == null || !isConnectorAtOwnerSide()) {
			return connectionPart.getMemberRole().getRecord();
		} else {
			var replacementConnector = SchemaFactory.eINSTANCE.createConnector();
			var replacementConnectorDiagramLocation = SchemaFactory.eINSTANCE.createDiagramLocation();
			replacementConnectorDiagramLocation.setX(connectionPart.getConnector().getDiagramLocation().getX() + ConnectorFigure.UNSCALED_RADIUS);
			replacementConnectorDiagramLocation.setY(connectionPart.getConnector().getDiagramLocation().getY() + ConnectorFigure.UNSCALED_RADIUS);
			replacementConnector.setDiagramLocation(replacementConnectorDiagramLocation);
			return replacementConnector;
		}
	}
	
	private DiagramLocation getTargetEndpointLocation() {
		if (connectionPart.getConnector() == null || !isConnectorAtOwnerSide()) {
			return connectionPart.getTargetEndpointLocation();
		} else {
			var replacementConnectorEndpointLocation = SchemaFactory.eINSTANCE.createDiagramLocation();
			replacementConnectorEndpointLocation.setX(0);
			replacementConnectorEndpointLocation.setX(0);
			return replacementConnectorEndpointLocation;
		}
	}
	
	boolean isConnectorAtOwnerSide() {
		var connector = connectionPart.getConnector();
		var connectionPartIndex = connector.getConnectionPart().getMemberRole().getConnectionParts()
				.indexOf(connector.getConnectionPart());
		return connectionPartIndex == 0;
	}

	public String getSetName() {
		return connectionPart.getMemberRole().getSet().getName();
	}

	public String getMemberRecordName() {
		return connectionPart.getMemberRole().getRecord().getName();
	}

	public int getIndex() {
		return connectionPart.getBendpointLocations().size() - 1;
	}
	
}
