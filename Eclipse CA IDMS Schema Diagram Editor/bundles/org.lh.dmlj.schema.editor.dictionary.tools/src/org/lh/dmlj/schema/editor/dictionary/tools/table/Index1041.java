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

public class Index1041 {
	public static final String AREA_1041 = "INDEX_1041_AREA";
	public static final String COMPRESS_1041 = "INDEX_1041_COMPRESS";
	public static final String DISPLACEMENT_1041 = "INDEX_1041_DISPLACEMENT";
	public static final String IXBLKCONTAINS_1041 = "INDEX_1041_IXBLKCONTAINS";
	public static final String NAME_1041 = "INDEX_1041_NAME";
	public static final String UNIQUE_1041 = "INDEX_1041_UNIQUE";

	private Table1050 table1050;
	private List<Indexkey1042> indexkey1042s = new ArrayList<>();
	
	private String area1041;
	private String compress1041;
	private short displacement1041;
	private short ixblkcontains1041;
	private String name1041;
	private String unique1041;

	public String getArea1041() {
		return area1041;
	}

	public String getCompress1041() {
		return compress1041;
	}

	public short getDisplacement1041() {
		return displacement1041;
	}

	public List<Indexkey1042> getIndexkey1042s() {
		return indexkey1042s;
	}

	public short getIxblkcontains1041() {
		return ixblkcontains1041;
	}

	public String getName1041() {
		return name1041;
	}

	public Table1050 getTable1050() {
		return table1050;
	}

	public String getUnique1041() {
		return unique1041;
	}

	public void setArea1041(String area1041) {
		this.area1041 = JdbcTools.removeTrailingSpaces(area1041);
	}

	public void setDisplacement1041(short displacement1041) {
		this.displacement1041 = displacement1041;
	}

	public void setIxblkcontains1041(short ixblkcontains1041) {
		this.ixblkcontains1041 = ixblkcontains1041;
	}

	public void setName1041(String name1041) {
		this.name1041 = JdbcTools.removeTrailingSpaces(name1041);
	}

	public void setTable1050(Table1050 table1050) {
		this.table1050 = table1050;
	}

	public void setUnique1041(String unique1041) {
		this.unique1041 = JdbcTools.removeTrailingSpaces(unique1041);
	}

	public void setCompress1041(String compress1041) {
		this.compress1041 = JdbcTools.removeTrailingSpaces(compress1041);
	}
	
}
