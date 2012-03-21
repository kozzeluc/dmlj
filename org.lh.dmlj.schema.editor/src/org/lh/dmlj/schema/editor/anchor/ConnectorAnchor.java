package org.lh.dmlj.schema.editor.anchor;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.lh.dmlj.schema.editor.figure.ConnectorFigure;

/**
 * An anchor that behaves the same as the EllipseAnchor - this is our own 
 * version and it can be of interest in the future because the calculations
 * being performed.
 */
public class ConnectorAnchor extends AbstractConnectionAnchor {

	public ConnectorAnchor(ConnectorFigure figure) {
		super(figure);
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
		double deltaX = Math.cos(Math.toRadians(a)) * 
						(double)ConnectorFigure.UNSCALED_RADIUS;
		double deltaY = -Math.sin(Math.toRadians(a)) * 
						(double)ConnectorFigure.UNSCALED_RADIUS;		
		
		PrecisionPoint result = 
			new PrecisionPoint(pOrigin.preciseX() + deltaX,
							   pOrigin.preciseY() + deltaY);
		
		return result;
	}
	
}
