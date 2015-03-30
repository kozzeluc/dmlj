/**
 * Copyright (C) 2014  Luc Hermans
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
package org.lh.dmlj.schema.editor.dictionary.tools.importtool.collector;

import java.util.Collection;
import java.util.Collections;

import org.lh.dmlj.schema.Usage;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.SchemaImportSession;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Column_1028;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Table_1050;
import org.lh.dmlj.schema.editor.importtool.IElementDataCollector;

public class CatalogElementDataCollector implements IElementDataCollector<Column_1028> {

	public CatalogElementDataCollector(SchemaImportSession session) {
		super();
	}

	@Override
	public String getBaseName(Column_1028 column_1028) {
		return getName(column_1028);
	}

	@Override
	public String getDependsOnElementName(Column_1028 column_1028) {
		return null;
	}

	@Override
	public Collection<String> getIndexElementBaseNames(Column_1028 column_1028) {
		return Collections.emptyList();
	}

	@Override
	public Collection<String> getIndexElementNames(Column_1028 column_1028) {
		return Collections.emptyList();
	}

	@Override
	public boolean getIsNullable(Column_1028 column_1028) {
		return column_1028.getNulls_1028().equals("Y");
	}

	@Override
	public short getLevel(Column_1028 column_1028) {
		return 2;
	}

	@Override
	public String getName(Column_1028 column_1028) {
		Table_1050 table_1050 = column_1028.getTable_1050();
		StringBuilder p = new StringBuilder(column_1028.getName_1028().replaceAll("_", "-"));
		if (!p.toString().equals("FILLER")) {
			p.append("-");
			p.append(String.valueOf(table_1050.getTableid_1050()));
		}
		return p.toString();
	}

	@Override
	public short getOccurrenceCount(Column_1028 column_1028) {
		return 1;
	}

	@Override
	public String getPicture(Column_1028 column_1028) {
		String type = column_1028.getType_1028();			
		if (type.equals("CHARACTER")) {
			return "X(" + column_1028.getVlength_1028() + ")";			
		} else if (type.equals("INTEGER")) {
			return "S9(8) SYNC";			
		} else if (type.equals("REAL")) {
			return "S9(8) SYNC";			
		} else if (type.equals("SMALLINT")) {
			return "S9(4) SYNC";			
		} else if (type.equals("TIMESTAMP")) {
			// should we make this kind of element COMPUTATIONAL ?
			return "X(8)";						
		} else if (type.equals("BINARY")) {
			if (getName(column_1028).equals("FILLER")) {
				return "X(" + column_1028.getVlength_1028() + ")";					
			} else {
				return "X(" + (column_1028.getVlength_1028() * 8) + ")";				
			}				
		}
		return null;
	}

	@Override
	public String getRedefinedElementName(Column_1028 column_1028) {
		return null;
	}

	@Override
	public Usage getUsage(Column_1028 column_1028) {
		String type = column_1028.getType_1028();			
		if (type.equals("CHARACTER")) {			
			return Usage.DISPLAY;
		} else if (type.equals("INTEGER")) {			
			return Usage.COMPUTATIONAL;
		} else if (type.equals("REAL")) {			
			return Usage.COMPUTATIONAL;
		} else if (type.equals("SMALLINT")) {			
			return Usage.COMPUTATIONAL;
		} else if (type.equals("TIMESTAMP")) {			
			// should we make this kind of element COMPUTATIONAL ?
			return Usage.DISPLAY;
		} else if (type.equals("BINARY")) {
			if (getName(column_1028).equals("FILLER")) {
				return Usage.DISPLAY;					
			} else {									
				return Usage.BIT;					
			}				
		}
		return null;
	}

	@Override
	public String getValue(Column_1028 column_1028) {
		return null;
	}

}
