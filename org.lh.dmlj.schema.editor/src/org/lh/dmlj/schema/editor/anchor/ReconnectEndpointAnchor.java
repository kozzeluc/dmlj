package org.lh.dmlj.schema.editor.anchor;

import org.eclipse.draw2d.AnchorListener;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;

public class ReconnectEndpointAnchor implements ConnectionAnchor {
	
	private IFigure figure;
	private Point 	mouseLocation;
	
	/**
	 * @param figure
	 * @param mouseLocation
	 * @param reference
	 * @return the location where the endpoint should go, relative to the given 
	 *         figure
	 */
	public static Point getRelativeLocation(IFigure figure, Point mouseLocation, 
											Point reference) {
		
		// get the figure's bounds...
		Rectangle bounds = figure.getBounds().getCopy();
		figure.translateToAbsolute(bounds); // necessary !
		
		// get the location of the mouse pointer relative to the figure...
		PrecisionPoint pointer = new PrecisionPoint(mouseLocation.x - bounds.x,
													mouseLocation.y - bounds.y);		
		
		// calculate the connection point based on both the reference and the
		// location of the mouse pointer in the figure...
		int[] offset = new int[] {pointer.y, bounds.width - pointer.x, 
								  bounds.height - pointer.y, pointer.x};
		int i = 0;
		for (int j = 1; j < 4; j++) {
			if (offset[j] < offset[i]) {
				i = j;
			}
		}
		int offsetX;
		int offsetY;
		if (i == 0) {
			offsetX = pointer.x;
			offsetY = 0;
		} else if (i == 1) {
			offsetX = bounds.width();
			offsetY = pointer.y;
		} else if (i == 2) {
			offsetX = pointer.x; 
			offsetY = bounds.height;
		} else {
			offsetX = 0;
			offsetY = pointer.y;
		}
		
		return new PrecisionPoint(offsetX, offsetY);
	}
	
	public ReconnectEndpointAnchor(IFigure figure, Point mouseLocation) {
		super();
		this.figure = figure;
		this.mouseLocation = mouseLocation;		
	}

	@Override
	public void addAnchorListener(AnchorListener listener) {		
	}

	@Override
	public Point getLocation(Point reference) {
		// get the location where the endpoint should go, relative to the 
		// figure...
		Point result = getRelativeLocation(figure, mouseLocation, reference);
		// convert this location to absolute coordinates...
		PrecisionPoint topLeft = 
			new PrecisionPoint(figure.getBounds().getTopLeft());
		figure.translateToAbsolute(topLeft); // necessary !
		topLeft.x += result.x;
		topLeft.y += result.y;
		return topLeft;
	}

	@Override
	public IFigure getOwner() {
		return null;
	}

	@Override
	public Point getReferencePoint() {
		return null;
	}

	@Override
	public void removeAnchorListener(AnchorListener listener) {
	}
	
} 