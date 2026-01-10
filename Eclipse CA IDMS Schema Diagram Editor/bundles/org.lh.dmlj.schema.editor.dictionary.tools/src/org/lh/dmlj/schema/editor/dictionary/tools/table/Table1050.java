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

import java.util.ArrayList;
import java.util.List;

import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.Rowid;

public class Table1050 {
	public static final String ROWID = "TABLE_1050_ROWID";
	public static final String AREA_1050 = "TABLE_1050_AREA";
	public static final String LOCMODE_1050 = "TABLE_1050_LOCMODE";
	public static final String NAME_1050 = "TABLE_1050_NAME";
	public static final String TABLEID_1050 = "TABLE_1050_TABLEID";	
		
	private List<Column1028> column1028s = new ArrayList<>();
	
	private Rowid ourRowid;
	private String area1050;
	private String locmode1050;
	private String name1050;
	private short tableid1050;
	
	public String getArea1050() {
		return area1050;
	}

	public Rowid getRowid() {
		return ourRowid;
	}

	public String getLocmode1050() {
		return locmode1050;
	}

	public String getName1050() {
		return name1050;
	}

	public short getTableid1050() {
		return tableid1050;
	}

	public void setArea1050(String area1050) {
		this.area1050 = JdbcTools.removeTrailingSpaces(area1050);
	}

	public void setRowid(Rowid rowid) {
		ourRowid = rowid;
	}

	public void setLocmode1050(String locmode1050) {
		this.locmode1050 = JdbcTools.removeTrailingSpaces(locmode1050);
	}

	public void setName1050(String name1050) {
		this.name1050 = JdbcTools.removeTrailingSpaces(name1050); 		
	}

	public void setTableid1050(short tableid1050) {
		this.tableid1050 = tableid1050;
	}

	public Column1028 getColumn1028(short columnNumber) {
		for (Column1028 column1028 : getColumn1028s()) {
			if (column1028.getNumber1028() == columnNumber) {
				return column1028;
			}
		}
		throw new IllegalStateException("logic error: column not found (" + getName1050() + "/" + columnNumber + ")");
	}

	public List<Column1028> getColumn1028s() {
		return column1028s;
	}

}
