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

public class Column_1028 {
	
	public static final String NAME_1028 = "COLUMN_1028_NAME";
	public static final String NULLS_1028 = "COLUMN_1028_NULLS";
	public static final String NUMBER_1028 = "COLUMN_1028_NUMBER";
	public static final String TYPE_1028 = "COLUMN_1028_TYPE";
	public static final String VLENGTH_1028 = "COLUMN_1028_VLENGTH";
	
	private Table_1050 table_1050;
	
	private String name_1028;
	private String nulls_1028;
	private short number_1028;
	private String type_1028;
	private short vlength_1028;
	
	public Column_1028() {
		super();
	}

	public String getName_1028() {
		return name_1028;
	}

	public String getNulls_1028() {
		return nulls_1028;
	}

	public short getNumber_1028() {
		return number_1028;
	}

	public Table_1050 getTable_1050() {
		return table_1050;
	}

	public String getType_1028() {
		return type_1028;
	}

	public short getVlength_1028() {
		return vlength_1028;
	}

	public void setName_1028(String name_1028) {
		this.name_1028 = JdbcTools.removeTrailingSpaces(name_1028);
	}

	public void setNulls_1028(String nulls_1028) {
		this.nulls_1028 = JdbcTools.removeTrailingSpaces(nulls_1028);
	}

	public void setNumber_1028(short number_1028) {
		this.number_1028 = number_1028;
	}

	public void setTable_1050(Table_1050 table_1050) {
		this.table_1050 = table_1050;
	}

	public void setType_1028(String type_1028) {
		this.type_1028 = JdbcTools.removeTrailingSpaces(type_1028);
	}

	public void setVlength_1028(short vlength_1028) {
		this.vlength_1028 = vlength_1028;
	}
	
}
