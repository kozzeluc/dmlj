package org.lh.dmlj.schema.editor.wizard._import.schema;

import java.util.List;
import java.util.Properties;

import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.importtool.AbstractRecordLayoutManager;

public class StandardRecordLayoutManager extends AbstractRecordLayoutManager {

	public StandardRecordLayoutManager() {
		super();
	}

	@Override
	public void layout(List<SchemaRecord> records, Properties parms) {
		// since we know how many records the schema contains, compute the
		// number of columns so that it is about the same as the number of 
		// rows...
		int columnCount = (int) Math.ceil(Math.sqrt((double) records.size()));
		int maxX = getSuggestedLeftMargin() + 
				   (columnCount - 1) * getSuggestedHorizontalIncrement();
		int x = getSuggestedLeftMargin();
		int y = getSuggestedTopMargin();
		for (SchemaRecord record : records) {
			setDiagramData(record, x, y);
			x += getSuggestedHorizontalIncrement();
			if (x > maxX) {
				x = getSuggestedLeftMargin();
				y += getSuggestedVerticalIncrement();
			}
		}		
	}

}