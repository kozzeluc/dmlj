/**
 * Copyright (C) 2025  Luc Hermans
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
package org.lh.dmlj.schema.editor.importtool;

import java.util.List;
import java.util.Properties;

import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.figure.RecordFigure;

public abstract class AbstractRecordLayoutManager {
	private static final int SUGGESTED_LEFT_MARGIN = 50;
	private static final int SUGGESTED_TOP_MARGIN = 100;
	
	protected AbstractRecordLayoutManager() {
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
		return SUGGESTED_LEFT_MARGIN;
	}

	public final int getSuggestedTopMargin() {
		return SUGGESTED_TOP_MARGIN;
	}

	public final int getSuggestedVerticalIncrement() {
		return 2 * RecordFigure.UNSCALED_HEIGHT;
	}

	public abstract void layout(List<SchemaRecord> records, Properties configuredParameters, Properties userParameters);
	
	protected void setDiagramData(SchemaRecord schemaRecord, int x, int y) {
		var location = SchemaFactory.eINSTANCE.createDiagramLocation();
		schemaRecord.getSchema().getDiagramData().getLocations().add(location);
		schemaRecord.setDiagramLocation(location);		
		location.setX(x);
		location.setY(y);
		location.setEyecatcher("record " + schemaRecord.getName());		
	}
	
}
