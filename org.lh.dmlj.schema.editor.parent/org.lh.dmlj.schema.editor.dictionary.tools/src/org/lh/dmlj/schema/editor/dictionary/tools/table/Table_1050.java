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

import java.util.ArrayList;
import java.util.List;

import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;

public class Table_1050 {

	public static final String ROWID = "TABLE_1050_ROWID";
	public static final String AREA_1050 = "TABLE_1050_AREA";
	public static final String LOCMODE_1050 = "TABLE_1050_LOCMODE";
	public static final String NAME_1050 = "TABLE_1050_NAME";
	public static final String TABLEID_1050 = "TABLE_1050_TABLEID";	
		
	private List<Column_1028> column_1028s = new ArrayList<>();;
	
	private long dbkey;
	private String area_1050;
	private String locmode_1050;
	private String name_1050;
	private short tableid_1050;
	
	public Table_1050() {
		super();
	}
	
	public String getArea_1050() {
		return area_1050;
	}

	public long getDbkey() {
		return dbkey;
	}

	public String getLocmode_1050() {
		return locmode_1050;
	}

	public String getName_1050() {
		return name_1050;
	}

	public short getTableid_1050() {
		return tableid_1050;
	}

	public void setArea_1050(String area_1050) {
		this.area_1050 = JdbcTools.removeTrailingSpaces(area_1050);
	}

	public void setDbkey(long dbkey) {
		this.dbkey = dbkey;
	}

	public void setLocmode_1050(String locmode_1050) {
		this.locmode_1050 = JdbcTools.removeTrailingSpaces(locmode_1050);
	}

	public void setName_1050(String name_1050) {
		this.name_1050 = JdbcTools.removeTrailingSpaces(name_1050); 		
	}

	public void setTableid_1050(short tableid_1050) {
		this.tableid_1050 = tableid_1050;
	}

	public Column_1028 getColumn_1028(short columnNumber) {
		for (Column_1028 column_1028 : getColumn_1028s()) {
			if (column_1028.getNumber_1028() == columnNumber) {
				return column_1028;
			}
		}
		throw new RuntimeException("logic error: column not found (" + getName_1050() + "/" + 
								   columnNumber + ")");
	}

	public List<Column_1028> getColumn_1028s() {
		return column_1028s;
	}

}
