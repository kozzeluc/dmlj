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

public class Scr054 {	
	public static final String SCR_NAM_054 = "SCR_NAM_054";
	public static final String SCR_POS_054 = "SCR_POS_054";
	public static final String INDEX_054 = "INDEX_054";
	public static final String SORT_054 = "SORT_054";

	private short index054;
	private String scrNam054;
	private short scrPos054;
	private short sort054;

	public short getIndex054() {
		return index054;
	}

	public String getScrNam054() {
		return scrNam054;
	}

	public short getScrPos054() {
		return scrPos054;
	}

	public short getSort054() {
		return sort054;
	}

	public void setIndex054(short index054) {
		this.index054 = index054;
	}

	public void setScrNam054(String scrNam054) {
		this.scrNam054 = JdbcTools.removeTrailingSpaces(scrNam054);
	}

	public void setScrPos054(short scrPos054) {
		this.scrPos054 = scrPos054;
	}

	public void setSort054(short sort054) {
		this.sort054 = sort054;
	}
	
}
