/**
 * Copyright (C) 2016  Luc Hermans
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

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.editor.figure.RecordFigure;

public abstract class AbstractRecordAnchor extends ChopboxAnchor {

	protected ConnectionPart connectionPart;
	protected Schema		 schema;
	
	protected AbstractRecordAnchor(RecordFigure figure, 
									     ConnectionPart connectionPart) {
		super(figure);
		this.connectionPart = connectionPart;	
		this.schema = connectionPart.getMemberRole().getSet().getSchema();
	}	

	/**
	 * Returns the standard chopbox anchor's location, given a reference, for 
	 * the record figure.<br><br>  
	 * This method will only be called if the sourceEndpointLocation (or 
	 * targetEndpointLocation) attribute of the connection's ConnectionPart 
	 * model object is not set.  Subclasses can override this method to return a 
	 * different anchor location.
	 * @param reference The reference point
	 * @return The anchor location in absolute coordinates
	 */
	protected Point getDefaultLocation(Point reference) {
		return super.getLocation(reference);		
	}
	
	@Override
	public final Point getLocation(Point reference) {
		
		PrecisionPoint offset = null;
		
		// get the (unscaled) offset from the model if available; if not, 
		// calculate it once...
		if (getModelEndpoint() != null) {
			// offset stored in the model
			DiagramLocation modelEndpoint = getModelEndpoint();
			offset = 
				new PrecisionPoint(modelEndpoint.getX(), modelEndpoint.getY());
		} else {
			// offset not stored in the model; compute it using both the figure's current location 
			// and the location returned by calling getDefaultLocation()...				
			Rectangle figureBounds = getOwner().getBounds().getCopy();
			getOwner().translateToAbsolute(figureBounds);
			Point defaultLocation = getDefaultLocation(reference);							
			PrecisionPoint workOffset = 
				new PrecisionPoint(defaultLocation.x - figureBounds.x, 
								   defaultLocation.y - figureBounds.y);
			// unscale the offset if needed...
			double zoomLevel = schema.getDiagramData().getZoomLevel();
			if (zoomLevel != 1.0) {
				workOffset.setPreciseX(workOffset.preciseX() / zoomLevel);
				workOffset.setPreciseY(workOffset.preciseY() / zoomLevel);
			}
			offset = workOffset.getPreciseCopy();
		}
		
		// scale the (now available) offset...
		PrecisionPoint scaledOffset = new PrecisionPoint();
		double zoomLevel = schema.getDiagramData().getZoomLevel();
		scaledOffset.setPreciseX(offset.preciseX() * zoomLevel);
		scaledOffset.setPreciseY(offset.preciseY() * zoomLevel);
		
		// compute the anchor location using the figure's bounds and the scaled
		// offset...
		PrecisionPoint anchorLocation = 
			new PrecisionPoint(getOwner().getBounds().getTopLeft());
		getOwner().translateToAbsolute(anchorLocation); 
		anchorLocation.setPreciseX(anchorLocation.preciseX() + 
								   scaledOffset.preciseX());
		anchorLocation.setPreciseY(anchorLocation.preciseY() + 
								   scaledOffset.preciseY());
		
		return anchorLocation;
	}
	
	/**
	 * @return The connection endpoint as an offset to the record figure, as 
	 *         stored in the model
	 */
	protected abstract DiagramLocation getModelEndpoint();	
	
	@Override
	public final Point getReferencePoint() {
		if (getModelEndpoint() != null) {
			
			// we have something stored in the model; let's use it to create the
			// reference point
			DiagramLocation modelEndpoint = getModelEndpoint();
			PrecisionPoint offset = 
				new PrecisionPoint(modelEndpoint.getX(), modelEndpoint.getY());
			
			// scale the (now available) offset...
			PrecisionPoint scaledOffset = new PrecisionPoint();
			double zoomLevel = schema.getDiagramData().getZoomLevel();
			scaledOffset.setPreciseX(offset.preciseX() * zoomLevel);
			scaledOffset.setPreciseY(offset.preciseY() * zoomLevel);
			
			// compute the anchor location using the figure's bounds and the scaled
			// offset...
			PrecisionPoint anchorLocation = 
				new PrecisionPoint(getOwner().getBounds().getTopLeft());
			getOwner().translateToAbsolute(anchorLocation); 
			anchorLocation.setPreciseX(anchorLocation.preciseX() + 
									   scaledOffset.preciseX());
			anchorLocation.setPreciseY(anchorLocation.preciseY() + 
									   scaledOffset.preciseY());
			
			return anchorLocation;			
		} else {
			PrecisionPoint pp = 
					new PrecisionPoint(getOwner().getBounds().getCenter());
			getOwner().translateToAbsolute(pp);
			return pp;
		}
	}	
	
}
