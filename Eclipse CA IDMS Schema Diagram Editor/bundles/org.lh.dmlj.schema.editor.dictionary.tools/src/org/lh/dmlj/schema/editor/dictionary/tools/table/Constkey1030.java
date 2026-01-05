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

public class Constkey1030 {
	public static final String NAME_1030 = "CONSTKEY_1030_NAME";
	public static final String NUMBER_1030 = "CONSTKEY_1030_NUMBER";
	
	private String name1030;
	private short number1030;

	public String getName1030() {
		return name1030;
	}

	public short getNumber1030() {
		return number1030;
	}

	public void setName1030(String name1030) {
		this.name1030 = JdbcTools.removeTrailingSpaces(name1030);
	}

	public void setNumber1030(short number1030) {
		this.number1030 = number1030;
	}
	
}
