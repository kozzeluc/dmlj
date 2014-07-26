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
	public void layout(List<SchemaRecord> records, Properties configuredParms,
					   Properties userParms) {
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
