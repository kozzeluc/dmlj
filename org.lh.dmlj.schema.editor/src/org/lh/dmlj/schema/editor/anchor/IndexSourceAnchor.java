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
