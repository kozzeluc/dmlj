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
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.TableColumn;

public class Constkey_1030 {

	@TableColumn public static final String NAME_1030 = "CONSTKEY.NAME";
	@TableColumn public static final String NUMBER_1030 = "CONSTKEY.NUMBER";
	
	public static final String COLUMNS = JdbcTools.columnsFor(Constkey_1030.class);
	
	private String name_1030;
	private short number_1030;

	public Constkey_1030() {
		super();
	}

	public String getName_1030() {
		return name_1030;
	}

	public short getNumber_1030() {
		return number_1030;
	}

	public void setName_1030(String name_1030) {
		this.name_1030 = JdbcTools.removeTrailingSpaces(name_1030);
	}

	public void setNumber_1030(short number_1030) {
		this.number_1030 = number_1030;
	}
	
}
