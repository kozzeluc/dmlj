/**
 * Copyright (C) 2026  Luc Hermans
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

import org.eclipse.draw2d.geometry.Rectangle;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.common.Pair;

public class PopularSchemaLayoutManager extends AbstractRecordLayoutManager {
	private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	@Override
	public void layout(List<SchemaRecord> records, Properties locations, Properties unused) {
		var schema = records.get(0).getSchema();
		
		// the diagram data for all records should be contained in the Properties object passed via the 'locations' argument; for
		// missing entries result in their records being placed in an overflow row 
		var overflowRow = calculateHighestRow(locations) + 2;
		var overflowColumn = 0;
		for (var schemaRecord : schema.getRecords()) {
			if (locations.containsKey(schemaRecord.getName())) {
				var rectangle = toRectangle(locations.getProperty(schemaRecord.getName()));
				setDiagramData(schemaRecord, rectangle.x, rectangle.y);
			} else {
				setMissingLocation(schemaRecord, overflowRow, overflowColumn);
				overflowColumn += 2;
			}
		}
	}
	
	private int calculateHighestRow(Properties locations) {
		return locations.values().stream()
				.map(Object::toString)
				.map(this::calculateRowAndColumn)
				.mapToInt(Pair::left)
				.max()
				.orElse(-1);
	}
	
	private Rectangle toRectangle(String property) {
		// the first part of the property value passed consists of one or two letters corresponding to the row, the second part
		// is any number from 1 onwards and represents the column
		return toRectangle(calculateRowAndColumn(property));
	}
	
	private Pair<Integer> calculateRowAndColumn(String location) {
		int row;
		int column;
		try {
			row = LETTERS.indexOf(location.substring(0, 1));
			column = Integer.valueOf(String.valueOf(location.substring(1))) - 1;			
		} catch (NumberFormatException e) {
			row = 26 + 26 * LETTERS.indexOf(location.substring(0, 1)) + LETTERS.indexOf(location.substring(1, 2));
			column = Integer.valueOf(String.valueOf(location.substring(2))) - 1;				
		}
		return new Pair<>(row, column);
	}
	
	private void setMissingLocation(SchemaRecord schemaRecord, int row, int column) {
		var rectangle = toRectangle(new Pair<>(row, column));
		setDiagramData(schemaRecord, rectangle.x, rectangle.y);
	}
	
	private Rectangle toRectangle(Pair<Integer> rowAndColumn) {
		var x = getSuggestedLeftMargin() + rowAndColumn.right() * getRecordFigureWidth();
		var y = getSuggestedTopMargin() + rowAndColumn.left() * getSuggestedVerticalIncrement();
		return new Rectangle(x, y, 0, 0);
	}

}
