package org.lh.dmlj.schema.editor.importtool;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.draw2d.geometry.Rectangle;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;

public class PopularSchemaLayoutManager extends AbstractRecordLayoutManager {
	
	private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";		
	
	public PopularSchemaLayoutManager() {
		super();
	}
	
	@Override
	public void layout(List<SchemaRecord> records, 
					   Properties locations, Properties unused) {				
		
		Schema schema = records.get(0).getSchema();
		
		// the diagram data for all records should be contained in the 
		// Properties object passed via the 'locations' argument		
		
		List<SchemaRecord> notSet = new ArrayList<>();
		for (SchemaRecord record : schema.getRecords()) {	
			// calculate and set the record's diagram data...
			if (locations.containsKey(record.getName())) {
				Rectangle rectangle = 
					toRectangle(locations.getProperty(record.getName()));
				setDiagramData(record, rectangle.x, rectangle.y);
			} else {
				notSet.add(record);
			}
		}	
		if (!notSet.isEmpty()) {
			throw new Error("not all record diagram data set: " + 
							notSet.toString());
		}
				
	}	
	
	private Rectangle toRectangle(String property) {	
		// the first part of the property value passed consists of one or two
		// letters corresponding to the row, the second part is any number from 
		// 1 onwards and represents the column		
		int row;
		int column;		
		try {
			row = LETTERS.indexOf(property.substring(0, 1));
			column = Integer.valueOf(String.valueOf(property.substring(1))) - 1;			
		} catch (NumberFormatException e) {
			row = 26 + 26 * LETTERS.indexOf(property.substring(0, 1)) +
				  LETTERS.indexOf(property.substring(1, 2));
			column = Integer.valueOf(String.valueOf(property.substring(2))) - 1;				
		}
				
		int x = getSuggestedLeftMargin() + column * getRecordFigureWidth();
		int y = getSuggestedTopMargin() + row * getSuggestedVerticalIncrement();
		return new Rectangle(x, y, 0, 0);
	}	

}