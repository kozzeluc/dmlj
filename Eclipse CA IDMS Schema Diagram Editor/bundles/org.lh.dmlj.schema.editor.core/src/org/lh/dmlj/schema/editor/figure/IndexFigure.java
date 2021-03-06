/**
 * Copyright (C) 2015  Luc Hermans
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
package org.lh.dmlj.schema.editor.figure;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolygonShape;
import org.eclipse.draw2d.geometry.Rectangle;

public class IndexFigure extends PolygonShape {
	
	public static final int UNSCALED_HEIGHT = 22;
	public static final int UNSCALED_WIDTH = 22;
	
	
	public IndexFigure() {
		super();
		
		setOpaque(true);
		
		Rectangle r = new Rectangle(0, 0, UNSCALED_WIDTH, UNSCALED_HEIGHT);
		setStart(r.getTopLeft());
		addPoint(r.getTopLeft());
		addPoint(r.getTopRight());
		addPoint(r.getBottom());
		addPoint(r.getTopLeft());
		setEnd(r.getTopLeft());
		setPreferredSize(r.getSize().expand(1, 1));
	}


	public void setName(String name) {
		Label tooltip = new Label(name);
		setToolTip(tooltip);
	}
}
