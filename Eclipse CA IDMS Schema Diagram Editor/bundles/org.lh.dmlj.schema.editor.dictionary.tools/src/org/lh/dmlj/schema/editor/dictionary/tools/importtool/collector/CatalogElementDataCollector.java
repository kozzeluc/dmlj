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
package org.lh.dmlj.schema.editor.dictionary.tools.importtool.collector;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.lh.dmlj.schema.Usage;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.SchemaImportSession;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Column1028;
import org.lh.dmlj.schema.editor.importtool.IElementDataCollector;

public class CatalogElementDataCollector implements IElementDataCollector<Column1028> {
	private static final String FILLER = "FILLER";

	public CatalogElementDataCollector(SchemaImportSession session) {
	}

	@Override
	public String getBaseName(Column1028 column1028) {
		return getName(column1028);
	}

	@Override
	public String getDependsOnElementName(Column1028 column1028) {
		return null;
	}

	@Override
	public Collection<String> getIndexElementBaseNames(Column1028 column1028) {
		return Collections.emptyList();
	}

	@Override
	public Collection<String> getIndexElementNames(Column1028 column1028) {
		return Collections.emptyList();
	}

	@Override
	public boolean getIsNullable(Column1028 column1028) {
		return column1028.getNulls1028().equals("Y");
	}

	@Override
	public short getLevel(Column1028 column1028) {
		return 2;
	}

	@Override
	public String getName(Column1028 column1028) {
		var table1050 = column1028.getTable1050();
		var p = new StringBuilder(column1028.getName1028().replace("_", "-"));
		if (!p.toString().equals(FILLER)) {
			p.append("-");
			p.append(String.valueOf(table1050.getTableid1050()));
		}
		return p.toString();
	}

	@Override
	public short getOccurrenceCount(Column1028 column1028) {
		return 1;
	}

	@Override
	public String getPicture(Column1028 column1028) {
		return switch (column1028.getType1028()) {
			case "CHARACTER" -> "X(" + column1028.getVlength1028() + ")";
			case "INTEGER", "REAL" -> "S9(8) SYNC";
			case "SMALLINT" -> "S9(4) SYNC";
			case "TIMESTAMP" -> "X(8)";
			case "BINARY" -> getName(column1028).equals(FILLER) ? "X(" + column1028.getVlength1028() + ")" : "X(" + (column1028.getVlength1028() * 8) + ")";
			default -> null;
		};
	}

	@Override
	public String getRedefinedElementName(Column1028 column1028) {
		return null;
	}

	@Override
	public Usage getUsage(Column1028 column1028) {
		return switch (column1028.getType1028()) {
			case "CHARACTER" -> Usage.DISPLAY;
			case "INTEGER", "REAL", "SMALLINT" -> Usage.COMPUTATIONAL;
			case "TIMESTAMP" -> Usage.DISPLAY;
			case "BINARY" -> getName(column1028).equals(FILLER) ? Usage.DISPLAY : Usage.BIT;
			default -> null;
		};
	}

	@Override
	public List<String> getValues(Column1028 column1028) {
		return Collections.emptyList();
	}

}
