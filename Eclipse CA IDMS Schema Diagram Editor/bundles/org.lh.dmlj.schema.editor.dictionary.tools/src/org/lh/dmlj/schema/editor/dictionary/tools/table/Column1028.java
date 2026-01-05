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

public class Column1028 {
	public static final String NAME_1028 = "COLUMN_1028_NAME";
	public static final String NULLS_1028 = "COLUMN_1028_NULLS";
	public static final String NUMBER_1028 = "COLUMN_1028_NUMBER";
	public static final String TYPE_1028 = "COLUMN_1028_TYPE";
	public static final String VLENGTH_1028 = "COLUMN_1028_VLENGTH";
	
	private Table1050 table1050;
	
	private String name1028;
	private String nulls1028;
	private short number1028;
	private String type1028;
	private short vlength1028;

	public String getName1028() {
		return name1028;
	}

	public String getNulls1028() {
		return nulls1028;
	}

	public short getNumber1028() {
		return number1028;
	}

	public Table1050 getTable1050() {
		return table1050;
	}

	public String getType1028() {
		return type1028;
	}

	public short getVlength1028() {
		return vlength1028;
	}

	public void setName1028(String name1028) {
		this.name1028 = JdbcTools.removeTrailingSpaces(name1028);
	}

	public void setNulls1028(String nulls1028) {
		this.nulls1028 = JdbcTools.removeTrailingSpaces(nulls1028);
	}

	public void setNumber1028(short number1028) {
		this.number1028 = number1028;
	}

	public void setTable1050(Table1050 table1050) {
		this.table1050 = table1050;
	}

	public void setType1028(String type1028) {
		this.type1028 = JdbcTools.removeTrailingSpaces(type1028);
	}

	public void setVlength1028(short vlength1028) {
		this.vlength1028 = vlength1028;
	}
	
}
