/**
 * Copyright (C) 2025  Luc Hermans
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
package org.lh.dmlj.schema.editor.anchor;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.editor.figure.ConnectorFigure;

/**
 * An anchor that behaves the same as the EllipseAnchor - this is our own version and it can be of interest in
 * the future because of the calculations being performed.
 */
public class ConnectorAnchor extends AbstractConnectionAnchor {
	private final DiagramData diagramData;
	
	public ConnectorAnchor(ConnectorFigure figure, Connector connector) {
		super(figure);
		diagramData = connector.getConnectionPart().getMemberRole().getSet().getSchema().getDiagramData();
	}

	@Override
	public Point getLocation(Point reference) {
		var connectorFigureCenterLocation = new PrecisionPoint(getOwner().getBounds().getCenter());
		getOwner().translateToAbsolute(connectorFigureCenterLocation);
				
		var referenceLocation = new PrecisionPoint(reference);
		
		// calculate the alpha angle
		var lengthO = referenceLocation.preciseY() >= connectorFigureCenterLocation.preciseY() ?
				referenceLocation.preciseY() - connectorFigureCenterLocation.preciseY() :
				connectorFigureCenterLocation.preciseY() - referenceLocation.preciseY();
	 	var lengthA = referenceLocation.preciseX() >= connectorFigureCenterLocation.preciseX() ?
	 			referenceLocation.preciseX() - connectorFigureCenterLocation.preciseX() :
	 			connectorFigureCenterLocation.preciseX() - referenceLocation.preciseX();
		var aTangent = lengthO / lengthA;
		var a = Math.toDegrees(Math.atan(aTangent));
		if (referenceLocation.preciseX() >= connectorFigureCenterLocation.preciseX()) {
			// right circle half; no angle correction needed for the upper right circle quarter
			if (referenceLocation.preciseY() > connectorFigureCenterLocation.preciseY()) {
				// lower right circle quarter
				a = 360.0d - a;
			}
		} else {
			// left circle half
			if (referenceLocation.preciseY() > connectorFigureCenterLocation.preciseY()) {
				// lower left circle quarter
				a += 180.0d;				
			} else {
				// upper left circle quarter
				a = 180.0d - a;				
			}
		}
		
		// calculate deltaX and -Y
		var scaledRadius = ConnectorFigure.UNSCALED_RADIUS * diagramData.getZoomLevel();
		var deltaX = Math.cos(Math.toRadians(a)) * scaledRadius;
		var deltaY = -Math.sin(Math.toRadians(a)) * scaledRadius;		
		
		return new PrecisionPoint(connectorFigureCenterLocation.preciseX() + deltaX, connectorFigureCenterLocation.preciseY() + deltaY);
	}
	
}
