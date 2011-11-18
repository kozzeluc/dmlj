package org.lh.dmlj.schema.editor.figure;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class SetDescription extends Label {
	
	private IFigure 		 parent;
	private ConnectionAnchor targetAnchor;

	public SetDescription(String description, IFigure parent, 
						  ConnectionAnchor targetAnchor) {
		super(description);
		this.parent = parent;
		this.targetAnchor = targetAnchor;
	}
	
	
	@Override
	public Rectangle getBounds() {
		if (parent != null && parent.getBounds() != null) {
			Point parentsCenter = parent.getBounds().getCenter();
			Point targetPoint = targetAnchor.getLocation(parentsCenter);
			int x = targetPoint.x;
			int y = targetPoint.y;
			Dimension preferredSize = getPreferredSize();
			return new Rectangle(x, y, preferredSize.width, preferredSize.height);
		} else {
			return new Rectangle(0, 0, 0, 0);
		}
	}
}