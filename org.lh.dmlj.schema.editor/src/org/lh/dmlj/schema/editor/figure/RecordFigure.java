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
import org.eclipse.draw2d.geometry.Rectangle;
import org.lh.dmlj.schema.editor.Plugin;

public class RecordFigure extends Figure {
	
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
	
	public RecordFigure() {
		super();
		
		setBackgroundColor(ColorConstants.white);
		setForegroundColor(ColorConstants.black);
		
		XYLayout layout = new XYLayout();
		setLayoutManager(layout);
		
		setPreferredSize(130, 53);
		
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
		label.setFont(Plugin.getDefault().getFont());
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