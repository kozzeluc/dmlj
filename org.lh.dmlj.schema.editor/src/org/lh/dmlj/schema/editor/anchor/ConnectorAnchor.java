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
package org.lh.dmlj.schema.editor.anchor;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.editor.figure.ConnectorFigure;

/**
 * An anchor that behaves the same as the EllipseAnchor - this is our own 
 * version and it can be of interest in the future because of the calculations
 * being performed.
 */
public class ConnectorAnchor extends AbstractConnectionAnchor {

	private DiagramData diagramData;
	
	public ConnectorAnchor(ConnectorFigure figure, Connector connector) {
		super(figure);
		diagramData = connector.getConnectionPart()
							   .getMemberRole()
							   .getSet()
							   .getSchema()
							   .getDiagramData();
	}

	@Override
	public Point getLocation(Point reference) {
		
		// get the center location of the connector figure
		PrecisionPoint pOrigin = 
			new PrecisionPoint(getOwner().getBounds().getCenter());
		getOwner().translateToAbsolute(pOrigin);
		
		// get the reference location
		PrecisionPoint pReference = new PrecisionPoint(reference);
		
		// calculate the alpha angle
		double lengthO = pReference.preciseY() >= pOrigin.preciseY() ?
						 	 pReference.preciseY() - pOrigin.preciseY() :
						 	 pOrigin.preciseY() - pReference.preciseY();
	 	double lengthA = pReference.preciseX() >= pOrigin.preciseX() ?
			 	 			 pReference.preciseX() - pOrigin.preciseX() :
			 	 			 pOrigin.preciseX() - pReference.preciseX();
		double aTangent = lengthO / lengthA;
		double a = Math.toDegrees(Math.atan(aTangent));
		if (pReference.preciseX() >= pOrigin.preciseX()) {
			// right circle half
			if (pReference.preciseY() > pOrigin.preciseY()) {
				// lower right circle quarter
				a = 360.0d - a;				
			//} else {
				// upper right circle quarter; no angle correction needed				
			}
		} else {
			// left circle half
			if (pReference.preciseY() > pOrigin.preciseY()) {
				// lower left circle quarter
				a += 180.0d;				
			} else {
				// upper left circle quarter
				a = 180.0d - a;				
			}
		}
		
		// calculate deltaX and -Y
		double scaledRadius = 
			(double)ConnectorFigure.UNSCALED_RADIUS * diagramData.getZoomLevel();
		double deltaX = Math.cos(Math.toRadians(a)) * scaledRadius;
		double deltaY = -Math.sin(Math.toRadians(a)) * scaledRadius;		
		
		PrecisionPoint result = 
			new PrecisionPoint(pOrigin.preciseX() + deltaX,
							   pOrigin.preciseY() + deltaY);
		
		return result;
	}
	
}
