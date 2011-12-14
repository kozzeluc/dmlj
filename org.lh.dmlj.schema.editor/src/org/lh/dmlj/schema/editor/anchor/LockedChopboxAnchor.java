package org.lh.dmlj.schema.editor.anchor;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;

public class LockedChopboxAnchor extends ChopboxAnchor {

	private PrecisionPoint offset;
	
	public LockedChopboxAnchor(IFigure figure) {
		super(figure);
	}	

	@Override
	public Point getLocation(Point reference) {
		if (offset == null) {
			PrecisionPoint superLocation = 
				new PrecisionPoint(super.getLocation(reference));
			PrecisionPoint figureLocation = 
				new PrecisionPoint(getOwner().getBounds().x,
								   getOwner().getBounds().y);
			offset = new PrecisionPoint(superLocation.x - figureLocation.x, 
										superLocation.y - figureLocation.y);
		}
		PrecisionPoint topLeft = 
			new PrecisionPoint(getOwner().getBounds().getTopLeft());
		getOwner().translateToAbsolute(topLeft);
		topLeft.x += offset.x;
		topLeft.y += offset.y;
		return topLeft;
	}
	
}