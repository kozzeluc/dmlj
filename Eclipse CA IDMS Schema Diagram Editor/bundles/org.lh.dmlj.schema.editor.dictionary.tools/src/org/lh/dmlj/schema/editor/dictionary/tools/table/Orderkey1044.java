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

public class Orderkey1044 {
	public static final String COLUMN_1044 = "ORDERKEY_1044_COLUMN";
	public static final String CONSTRAINT_1044 = "ORDERKEY_1044_CONSTRAINT";
	public static final String SORTORDER_1044 = "ORDERKEY_1044_SORTORDER";
	
	private String column1044;
	private String constraint1044;
	private String sortorder1044;

	public String getColumn1044() {
		return column1044;
	}

	public String getConstraint1044() {
		return constraint1044;
	}

	public String getSortorder1044() {
		return sortorder1044;
	}

	public void setColumn1044(String column1044) {
		this.column1044 = JdbcTools.removeTrailingSpaces(column1044);
	}

	public void setConstraint1044(String constraint1044) {
		this.constraint1044 = JdbcTools.removeTrailingSpaces(constraint1044);
	}

	public void setSortorder1044(String sortorder1044) {
		this.sortorder1044 = JdbcTools.removeTrailingSpaces(sortorder1044);
	}
	
}
