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

import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolygonShape;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.lh.dmlj.schema.editor.Plugin;

public class ConnectorFigure extends PolygonShape {
	
	public static final int UNSCALED_RADIUS = 10;
	private Ellipse 		ellipse; 
	private Label 			label;
	
	public ConnectorFigure() {
		super();
		
		setOpaque(true);
		
		XYLayout layout = new XYLayout();
		setLayoutManager(layout);
		
		setPreferredSize(2 * UNSCALED_RADIUS, 2 * UNSCALED_RADIUS);
		
		ellipse = new Ellipse();
		ellipse.setLineWidth(1);
		ellipse.setPreferredSize(2 * UNSCALED_RADIUS, 2 * UNSCALED_RADIUS);
		add(ellipse, 
			new Rectangle(new Point(0, 0), ellipse.getPreferredSize()));
		
		label = new Label();
		label.setLabelAlignment(PositionConstants.CENTER);
		label.setFont(Plugin.getDefault().getFigureFont());
		add(label, new Rectangle(new Point(0, 0), 
								 new Dimension(2 * UNSCALED_RADIUS, 
										 	   2 * UNSCALED_RADIUS)));		
	}
	
	public void setLabel(String label) {
		this.label.setText(label);
	}
	
	public void setLineWidth(int w) {
		ellipse.setLineWidth(w);
	}
	
	public void setName(String name) {
		Label tooltip = new Label(name);
		setToolTip(tooltip);
	}
	
}
