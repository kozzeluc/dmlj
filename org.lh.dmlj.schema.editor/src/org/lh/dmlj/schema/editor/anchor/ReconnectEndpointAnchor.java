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

import org.eclipse.draw2d.AnchorListener;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.editor.figure.RecordFigure;

/**
 * This anchor will be used when the user moves a connection endpoint; it also
 * facilitates the static getRelativeLocation method.
 */
public class ReconnectEndpointAnchor implements ConnectionAnchor {
	
	private RecordFigure   figure;
	private ConnectionPart connectionPart;
	private Point 	       mouseLocation;
	
	/**
	 * Calculates, for a given mouse location, the (unscaled) location where an 
	 * endpoint should go, relative to a given (record) figure.
	 * @param figure The record figure
	 * @param mouseLocation The scaled mouseLocation in absolute (scaled) 
	 *        coordinates
	 * @param zoomLevel The current zoom level
	 * @return the (unscaled) location where the endpoint should go, relative to 
	 *         the given record figure
	 */
	public static PrecisionPoint getRelativeLocation(RecordFigure figure, 
													 Point mouseLocation, 
													 double zoomLevel) {
		
		// get the figure's bounds...
		Rectangle bounds = figure.getBounds().getCopy();
		figure.translateToAbsolute(bounds);
		
		// get the (scaled) location of the mouse pointer relative to the 
		// figure...
		PrecisionPoint pointer = new PrecisionPoint(mouseLocation.x - bounds.x,
													mouseLocation.y - bounds.y);		
		
		// calculate the (scaled) connection point based on both the reference 
		// and the location of the mouse pointer in the figure...
		int[] offset = new int[] {pointer.y, bounds.width - pointer.x, 
								  bounds.height - pointer.y, pointer.x};
		int i = 0;
		for (int j = 1; j < 4; j++) {
			if (offset[j] < offset[i]) {
				i = j;
			}
		}
		double offsetX;
		double offsetY;
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
		
		// unscale the calculated offsets...
		PrecisionPoint result = new PrecisionPoint(offsetX, offsetY);
		RecordFigure.unscale(result, figure, zoomLevel);
		return result;
	}	
	
	public ReconnectEndpointAnchor(RecordFigure figure, Point mouseLocation,
								   ConnectionPart connectionPart) {
		super();
		this.figure = figure;
		this.mouseLocation = mouseLocation;		
		this.connectionPart = connectionPart;
	}

	@Override
	public void addAnchorListener(AnchorListener listener) {		
	}

	@Override
	public Point getLocation(Point reference) {
		// get the location where the endpoint should go, relative to the 
		// figure and unscaled...
		double zoomLevel = connectionPart.getMemberRole()
										 .getSet()
										 .getSchema()
										 .getDiagramData()
										 .getZoomLevel();
		PrecisionPoint result = 
			getRelativeLocation(figure, mouseLocation, zoomLevel);
		// scale the relative location...
		RecordFigure.scale(result, figure, zoomLevel);
		// convert this location to absolute coordinates...
		PrecisionPoint topLeft = 
			new PrecisionPoint(figure.getBounds().getTopLeft().x,
							   figure.getBounds().getTopLeft().y);
		figure.translateToAbsolute(topLeft);
		topLeft.setPreciseX(topLeft.preciseX() + result.preciseX());
		topLeft.setPreciseY(topLeft.preciseY() + result.preciseY());		
		
		return topLeft;
	}

	@Override
	public IFigure getOwner() {
		return figure;
	}

	@Override
	public Point getReferencePoint() {		
		
		// get the current zoom level
		double zoomLevel = connectionPart.getMemberRole()
										 .getSet()
										 .getSchema()
										 .getDiagramData()
										 .getZoomLevel();
		
		// calculate the (unscaled) offset of the mouse pointer relative to the 
		// record's top left corner
		PrecisionPoint relativeLocation = 
			getRelativeLocation(figure, mouseLocation, zoomLevel);
		
		// get the record figure's absolute location
		PrecisionPoint location = 
			new PrecisionPoint(figure.getBounds().preciseX(),
							   figure.getBounds().preciseY());
		figure.translateToAbsolute(location);
		
		// calculate the scaled offset for the new connection end point
		double offsetX = relativeLocation.preciseX() * zoomLevel;
		double offsetY = relativeLocation.preciseY() * zoomLevel;
		
		// calculate the absolute reconnect endpoint location...
		location.setPreciseX(location.preciseX() + offsetX);
		location.setPreciseY(location.preciseY() + offsetY);
		
		// ...and return it to the caller
		return location;
	}

	@Override
	public void removeAnchorListener(AnchorListener listener) {
	}
	
} 
