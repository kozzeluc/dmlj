package org.lh.dmlj.schema.editor.anchor;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.lh.dmlj.schema.editor.figure.IndexFigure;

public class IndexSourceAnchor extends AbstractConnectionAnchor {

	public IndexSourceAnchor(IndexFigure indexFigure) {
		super(indexFigure);
	}

	@Override
	public Point getLocation(Point originalReference) {
		PrecisionPoint origin = 
			new PrecisionPoint(getOwner().getBounds().getBottom());
		getOwner().translateToAbsolute(origin);
		return origin;
	}
	
}
