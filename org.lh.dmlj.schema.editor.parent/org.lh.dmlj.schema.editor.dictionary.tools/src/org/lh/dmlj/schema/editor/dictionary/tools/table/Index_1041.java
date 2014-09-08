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
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.TableColumn;

public class Index_1041 {

	@TableColumn public static final String AREA_1041 = "INDEX.AREA";
	@TableColumn public static final String COMPRESS_1041 = "INDEX.COMPRESS";
	@TableColumn public static final String DISPLACEMENT_1041 = "INDEX.DISPLACEMENT";
	@TableColumn public static final String IXBLKCONTAINS_1041 = "INDEX.IXBLKCONTAINS";
	@TableColumn public static final String NAME_1041 = "INDEX.NAME";
	@TableColumn public static final String UNIQUE_1041 = "INDEX.UNIQUE";

	public static final String COLUMNS = JdbcTools.columnsFor(Index_1041.class);
	
	private Table_1050 table_1050;
	private List<Indexkey_1042> indexkey_1042s = new ArrayList<>();
	
	private String area_1041;
	private String compress_1041;
	private short displacement_1041;
	private short ixblkcontains_1041;
	private String name_1041;
	private String unique_1041;
	
	public Index_1041() {
		super();
	}

	public String getArea_1041() {
		return area_1041;
	}

	public String getCompress_1041() {
		return compress_1041;
	}

	public short getDisplacement_1041() {
		return displacement_1041;
	}

	public List<Indexkey_1042> getIndexkey_1042s() {
		return indexkey_1042s;
	}

	public short getIxblkcontains_1041() {
		return ixblkcontains_1041;
	}

	public String getName_1041() {
		return name_1041;
	}

	public Table_1050 getTable_1050() {
		return table_1050;
	}

	public String getUnique_1041() {
		return unique_1041;
	}

	public void setArea_1041(String area_1041) {
		this.area_1041 = JdbcTools.removeTrailingSpaces(area_1041);
	}

	public void setDisplacement_1041(short displacement_1041) {
		this.displacement_1041 = displacement_1041;
	}

	public void setIxblkcontains_1041(short ixblkcontains_1041) {
		this.ixblkcontains_1041 = ixblkcontains_1041;
	}

	public void setName_1041(String name_1041) {
		this.name_1041 = JdbcTools.removeTrailingSpaces(name_1041);
	}

	public void setTable_1050(Table_1050 table_1050) {
		this.table_1050 = table_1050;
	}

	public void setUnique_1041(String unique_1041) {
		this.unique_1041 = JdbcTools.removeTrailingSpaces(unique_1041);
	}

	public void setCompress_1041(String compress_1041) {
		this.compress_1041 = JdbcTools.removeTrailingSpaces(compress_1041);
	}
	
}
