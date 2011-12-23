package org.lh.dmlj.schema.editor.anchor;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.MemberRole;

public abstract class AbstractLockedRecordAnchor extends ChopboxAnchor {

	protected MemberRole 	 memberRole;
	protected PrecisionPoint offset;
	protected boolean		 owner;
	
	protected AbstractLockedRecordAnchor(IFigure figure, MemberRole memberRole,
						 	   			 boolean owner) {
		super(figure);
		this.memberRole = memberRole;
		this.owner = owner;
	}	

	protected abstract Point getDefaultLocation(Point reference);
	
	protected final Point getChopboxLocation(Point reference) {
		return super.getLocation(reference);
	}
	
	@Override
	public final Point getLocation(Point reference) {
		PrecisionPoint figureLocation = 
			new PrecisionPoint(getOwner().getBounds().x,
							   getOwner().getBounds().y);		
		if (owner && memberRole.getDiagramSourceAnchor() == null ||
			!owner && memberRole.getDiagramTargetAnchor() == null) {
			
			if (offset == null) {
				PrecisionPoint superLocation = 
					new PrecisionPoint(getDefaultLocation(reference));
				offset = new PrecisionPoint(superLocation.x - figureLocation.x, 
										    superLocation.y - figureLocation.y);
			}
		} else {			
			DiagramLocation location; // will hold the relative location
			if (owner) {
				location = memberRole.getDiagramSourceAnchor();
			} else {
				location = memberRole.getDiagramTargetAnchor();
			}
			offset = new PrecisionPoint(location.getX(), location.getY());
		}		
		PrecisionPoint topLeft = 
			new PrecisionPoint(getOwner().getBounds().getTopLeft());
		getOwner().translateToAbsolute(topLeft); // necessary !
		topLeft.x += offset.x;
		topLeft.y += offset.y;
		return topLeft;
	}
	
}