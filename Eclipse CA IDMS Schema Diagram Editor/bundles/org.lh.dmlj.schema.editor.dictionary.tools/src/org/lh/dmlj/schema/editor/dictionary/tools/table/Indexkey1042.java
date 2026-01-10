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
package org.lh.dmlj.schema.editor.dictionary.tools.table;

import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;

public class Indexkey1042 {
	public static final String COLUMN_1042 = "INDEXKEY_1042_COLUMN";
	public static final String SORTORDER_1042 = "INDEXKEY_1042_SORTORDER";
	
	private String column1042;
	private String sortorder1042;

	public String getColumn1042() {
		return column1042;
	}

	public String getSortorder1042() {
		return sortorder1042;
	}

	public void setColumn1042(String column1042) {
		this.column1042 = JdbcTools.removeTrailingSpaces(column1042);
	}

	public void setSortorder1042(String sortorder1042) {
		this.sortorder1042 = JdbcTools.removeTrailingSpaces(sortorder1042);
	}
	
}
