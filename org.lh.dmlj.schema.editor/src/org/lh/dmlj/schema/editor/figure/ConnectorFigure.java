package org.lh.dmlj.schema.editor.figure;

import org.eclipse.draw2d.ColorConstants;
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
		
		
		setBackgroundColor(ColorConstants.white);
		setForegroundColor(ColorConstants.black);
		
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
}