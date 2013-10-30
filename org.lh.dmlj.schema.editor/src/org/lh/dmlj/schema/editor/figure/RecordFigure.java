/**
 * Copyright (C) 2013  Luc Hermans
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

import java.text.DecimalFormat;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.lh.dmlj.schema.editor.Plugin;

public class RecordFigure extends Figure {
	
	public static final int UNSCALED_WIDTH = 130;
	public static final int UNSCALED_HEIGHT = 53;
	
	private static final DecimalFormat recordIdFormatter = 
		new DecimalFormat("000");
	
	private Label recordNameFigure;
	
	private Label recordIdFigure;
	private Label storageModeFigure;
	private Label recordLengthFigure;
	private Label locationModeFigure;
	
	private Label locationModeDetailsFigure; // CALC-key or VIA-set
	private Label duplicatesOptionFigure;
	
	private Label areaNameFigure;	
	
	/**
	 * Scales the relative anchorPoint on the recordFigure to the given 
	 * zoomLevel.
	 * @param anchorPoint The unscaled relative anchor point 
	 * @param figure The record figure
	 * @param zoomLevel The zoom level to scale up or down to
	 */
	public static void scale(PrecisionPoint anchorPoint, RecordFigure figure, 
							 double zoomLevel) {
	
		Rectangle bounds = figure.getBounds().getCopy();
		figure.translateToAbsolute(bounds);
		
		anchorPoint.setPreciseX(anchorPoint.preciseX() * zoomLevel);
		if (anchorPoint.preciseX() > bounds.width) {
			anchorPoint.setPreciseX(bounds.width);
		}
		
		anchorPoint.setPreciseY(anchorPoint.preciseY() * zoomLevel);
		if (anchorPoint.preciseY() > bounds.height) {
			anchorPoint.setPreciseY(bounds.height);
		}
	}
	
	/**
	 * Unscales the relative anchorPoint on the recordFigure from the given 
	 * zoomLevel.
	 * @param anchorPoint The scaled relative anchor point 
	 * @param figure The record figure
	 * @param zoomLevel The zoom level to unscale from
	 */
	public static void unscale(PrecisionPoint anchorPoint, RecordFigure figure, 
			 				   double zoomLevel) {			
	
		anchorPoint.setPreciseX(anchorPoint.preciseX() / zoomLevel);
		if (anchorPoint.preciseX() > UNSCALED_WIDTH) {
			anchorPoint.setPreciseX(UNSCALED_WIDTH);
		}
		anchorPoint.setPreciseY(anchorPoint.preciseY() / zoomLevel);
		if (anchorPoint.preciseY() > UNSCALED_HEIGHT) {
			anchorPoint.setPreciseY(UNSCALED_HEIGHT);
		}
	}
	
	public RecordFigure() {
		super();
		
		setBackgroundColor(ColorConstants.white);
		setOpaque(true);
		setForegroundColor(ColorConstants.black);
		
		XYLayout layout = new XYLayout();
		setLayoutManager(layout);
		
		setPreferredSize(UNSCALED_WIDTH, UNSCALED_HEIGHT);
		
		recordNameFigure = addLabel(0, 0, 130, 14);

		recordIdFigure = addLabel(0, 13, 38, 14);
		storageModeFigure = addLabel(37, 13, 19, 14);
		recordLengthFigure = addLabel(55, 13, 29, 14);
		locationModeFigure = addLabel(83, 13, 47, 14);
		
		locationModeDetailsFigure = addLabel(0, 26, 102, 14);
		duplicatesOptionFigure = addLabel(101, 26, 29, 14);
		
		areaNameFigure = addLabel(0, 39, 130, 14);		
		
	}
	
	private Label addLabel(int x, int y, int width, int height) {
		Label label = new Label();
		label.setLabelAlignment(PositionConstants.LEFT);
		label.setFont(Plugin.getDefault().getFigureFont());
		label.setBorder(new CompoundBorder(new LineBorder(1), 
				 						   new MarginBorder(0, 4, 0, 0)));		
		add(label, 
			new Rectangle(new Point(x, y), new Dimension(width, height)));
		return label;
	}	
	
	public void setAreaName(String areaName) {
		areaNameFigure.setText(areaName);
	}
	
	public void setDuplicatesOption(String duplicatesOption) {
		duplicatesOptionFigure.setText(duplicatesOption);
	}
	
	public void setLocationMode(String locationMode) {
		locationModeFigure.setText(locationMode);
	}
	
	public void setLocationModeDetails(String locationModeDetails) {
		locationModeDetailsFigure.setText(locationModeDetails);
	}
	
	public void setRecordId(short recordId) {
		recordIdFigure.setText(recordIdFormatter.format(recordId));
	}
	
	public void setRecordLength(int recordLength) {
		recordLengthFigure.setText(String.valueOf(recordLength));
	}
	
	public void setRecordName(String recordName) {
		recordNameFigure.setText(recordName);
	}
	
	public void setStorageMode(String storageMode) {
		storageModeFigure.setText(storageMode);
	}
}
