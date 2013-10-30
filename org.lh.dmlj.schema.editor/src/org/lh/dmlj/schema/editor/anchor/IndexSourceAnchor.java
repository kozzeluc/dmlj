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
import org.lh.dmlj.schema.editor.figure.IndexFigure;

/**
 * An anchor that will locate the (owner) connection endpoint at the bottom of 
 * its target index figure.
 */
public class IndexSourceAnchor extends AbstractConnectionAnchor {

	/**
	 * Constructs an IndexSourceAnchor with the given index figure.
	 * @param figure The source (owner) index figure
	 */
	public IndexSourceAnchor(IndexFigure indexFigure) {
		super(indexFigure);
	}

	/** 
	 * This method will always return the index figure's bottom center point.
	 * @see org.eclipse.draw2d.ConnectionAnchor#getLocation(org.eclipse.draw2d.geometry.Point)
	 */
	@Override
	public Point getLocation(Point originalReference) {
		PrecisionPoint origin = 
			new PrecisionPoint(getOwner().getBounds().getBottom());
		getOwner().translateToAbsolute(origin);
		return origin;
	}
	
}
