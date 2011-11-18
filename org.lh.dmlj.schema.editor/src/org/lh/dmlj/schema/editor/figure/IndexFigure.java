package org.lh.dmlj.schema.editor.figure;

import org.eclipse.draw2d.PolygonShape;
import org.eclipse.draw2d.geometry.Rectangle;

public class IndexFigure extends PolygonShape {
	
	public IndexFigure() {
		super();
		
		Rectangle r = new Rectangle(0, 0, 22, 22);
		setStart(r.getTopLeft());
		addPoint(r.getTopLeft());
		addPoint(r.getTopRight());
		addPoint(r.getBottom());
		addPoint(r.getTopLeft());
		setEnd(r.getTopLeft());
		setPreferredSize(r.getSize().expand(1, 1));
	}
}