package org.lh.dmlj.schema.editor.anchor;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.lh.dmlj.schema.editor.figure.RecordFigure;

public class IndexTargetAnchor extends AbstractConnectionAnchor {

	public IndexTargetAnchor(RecordFigure recordFigure) {
		super(recordFigure);
	}

	@Override
	public Point getLocation(Point originalReference) {
		
		PrecisionPoint reference = new PrecisionPoint(originalReference);		
		
		PrecisionPoint top = 
			new PrecisionPoint(getOwner().getBounds().getTop());
		getOwner().translateToAbsolute(top);
		
		PrecisionPoint left = 
			new PrecisionPoint(getOwner().getBounds().getLeft());
		getOwner().translateToAbsolute(left);
		
		if (reference.preciseX() <= top.preciseX() &&
			reference.preciseY() <= left.preciseY()) {
			
			PrecisionPoint topLeft = 
				new PrecisionPoint(getOwner().getBounds().getTopLeft());
			getOwner().translateToAbsolute(topLeft);
			return topLeft;
		}
		
		PrecisionPoint right = 
			new PrecisionPoint(getOwner().getBounds().getRight());
		getOwner().translateToAbsolute(right);
		
		if (reference.preciseX() > top.preciseX() &&
			reference.preciseY() <= right.preciseY()) {
			
			PrecisionPoint topRight = 
				new PrecisionPoint(getOwner().getBounds().getTopRight());
			getOwner().translateToAbsolute(topRight);
			return topRight;
		}
		
		PrecisionPoint bottom = 
			new PrecisionPoint(getOwner().getBounds().getBottom());
		getOwner().translateToAbsolute(bottom);
		
		if (reference.preciseX() <= bottom.preciseX() &&
			reference.preciseY() > left.preciseY()) {
			
			PrecisionPoint bottomLeft = 
				new PrecisionPoint(getOwner().getBounds().getBottomLeft());
			getOwner().translateToAbsolute(bottomLeft);
			return bottomLeft;
		}
		
		PrecisionPoint bottomRight = 
			new PrecisionPoint(getOwner().getBounds().getBottomRight());
		getOwner().translateToAbsolute(bottomRight);
		
		return bottomRight;
	}

}
