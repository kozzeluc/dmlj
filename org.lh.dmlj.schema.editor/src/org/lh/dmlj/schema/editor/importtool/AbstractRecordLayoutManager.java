package org.lh.dmlj.schema.editor.importtool;

import java.util.List;
import java.util.Properties;

import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.figure.RecordFigure;

public abstract class AbstractRecordLayoutManager {	
	
	private static final int suggestedLeftMargin = 50;
	private static final int suggestedTopMargin = 100;
	
	public AbstractRecordLayoutManager() {
		super();
	}

	public final int getRecordFigureHeight() {
		return RecordFigure.UNSCALED_HEIGHT;
	}

	public final int getRecordFigureWidth() {
		return RecordFigure.UNSCALED_WIDTH;
	}

	public final int getSuggestedHorizontalIncrement() {
		return (int) (RecordFigure.UNSCALED_WIDTH * 1.5);
	}

	public final int getSuggestedLeftMargin() {
		return suggestedLeftMargin;
	}

	public final int getSuggestedTopMargin() {
		return suggestedTopMargin;
	}

	public final int getSuggestedVerticalIncrement() {
		return 2 * RecordFigure.UNSCALED_HEIGHT;
	}

	public abstract void layout(List<SchemaRecord> records, 
								Properties parameters);
	
	protected void setDiagramData(SchemaRecord record, int x, int y) {		
		DiagramLocation location =
			SchemaFactory.eINSTANCE.createDiagramLocation();
		record.getSchema().getDiagramData().getLocations().add(location);
		record.setDiagramLocation(location);		
		location.setX(x);
		location.setY(y);
		location.setEyecatcher("record " + record.getName());		
	}
	
}