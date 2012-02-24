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
	
	private Label label;
	
	public ConnectorFigure() {
		super();
		
		
		setBackgroundColor(ColorConstants.white);
		setForegroundColor(ColorConstants.black);
		
		XYLayout layout = new XYLayout();
		setLayoutManager(layout);
		
		setPreferredSize(20, 20);
		
		Ellipse ellipse = new Ellipse();
		ellipse.setLineWidth(1);
		ellipse.setPreferredSize(20, 20);
		add(ellipse, 
			new Rectangle(new Point(0, 0), ellipse.getPreferredSize()));
		
		label = new Label();
		label.setLabelAlignment(PositionConstants.CENTER);
		label.setFont(Plugin.getDefault().getFigureFont());
		add(label, 
			new Rectangle(new Point(0, 0), new Dimension(20, 20)));		
	}
	
	public void setLabel(String label) {
		this.label.setText(label);
	}
}