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
package org.lh.dmlj.schema.editor.dictionary.tools.table;

import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;

public class Indexkey_1042 {

	public static final String COLUMN_1042 = "INDEXKEY_1042_COLUMN";
	public static final String SORTORDER_1042 = "INDEXKEY_1042_SORTORDER";
	
	private String column_1042;
	private String sortorder_1042;

	public Indexkey_1042() {
		super();
	}

	public String getColumn_1042() {
		return column_1042;
	}

	public String getSortorder_1042() {
		return sortorder_1042;
	}

	public void setColumn_1042(String column_1042) {
		this.column_1042 = JdbcTools.removeTrailingSpaces(column_1042);
	}

	public void setSortorder_1042(String sortorder_1042) {
		this.sortorder_1042 = JdbcTools.removeTrailingSpaces(sortorder_1042);
	}
	
}
