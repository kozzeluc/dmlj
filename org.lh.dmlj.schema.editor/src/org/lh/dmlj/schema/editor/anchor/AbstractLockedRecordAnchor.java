package org.lh.dmlj.schema.editor.anchor;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.editor.figure.RecordFigure;

public abstract class AbstractLockedRecordAnchor extends ChopboxAnchor {

	protected PrecisionPoint lockedOffset;
	protected ConnectionPart connectionPart;
	protected Schema		 schema;
	
	protected AbstractLockedRecordAnchor(RecordFigure figure, 
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
		} else if (lockedOffset == null) {			
			// offset not stored in the model and not previously calculated;
			// compute it using both the figure's current location and the 
			// location returned by calling getDefaultLocation()...				
			Rectangle figureBounds = getOwner().getBounds().getCopy();
			getOwner().translateToAbsolute(figureBounds);
			Point defaultLocation = getDefaultLocation(reference);							
			lockedOffset = 
				new PrecisionPoint(defaultLocation.x - figureBounds.x, 
								   defaultLocation.y - figureBounds.y);
			// unscale the offset if needed...
			double zoomLevel = schema.getDiagramData().getZoomLevel();
			if (zoomLevel != 1.0) {
				lockedOffset.setPreciseX(lockedOffset.preciseX() / zoomLevel);
				lockedOffset.setPreciseY(lockedOffset.preciseY() / zoomLevel);
			}
			offset = lockedOffset.getPreciseCopy();
		} else {
			offset = lockedOffset.getPreciseCopy();
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
	
}