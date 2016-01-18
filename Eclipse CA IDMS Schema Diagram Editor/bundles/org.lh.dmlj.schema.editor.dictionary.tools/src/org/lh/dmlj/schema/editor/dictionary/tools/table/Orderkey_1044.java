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

public class Orderkey_1044 {

	public static final String COLUMN_1044 = "ORDERKEY_1044_COLUMN";
	public static final String CONSTRAINT_1044 = "ORDERKEY_1044_CONSTRAINT";
	public static final String SORTORDER_1044 = "ORDERKEY_1044_SORTORDER";
	
	private String column_1044;
	private String constraint_1044;
	private String sortorder_1044;

	public Orderkey_1044() {
		super();
	}

	public String getColumn_1044() {
		return column_1044;
	}

	public String getConstraint_1044() {
		return constraint_1044;
	}

	public String getSortorder_1044() {
		return sortorder_1044;
	}

	public void setColumn_1044(String column_1044) {
		this.column_1044 = JdbcTools.removeTrailingSpaces(column_1044);
	}

	public void setConstraint_1044(String constraint_1044) {
		this.constraint_1044 = JdbcTools.removeTrailingSpaces(constraint_1044);
	}

	public void setSortorder_1044(String sortorder_1044) {
		this.sortorder_1044 = JdbcTools.removeTrailingSpaces(sortorder_1044);
	}
	
}
