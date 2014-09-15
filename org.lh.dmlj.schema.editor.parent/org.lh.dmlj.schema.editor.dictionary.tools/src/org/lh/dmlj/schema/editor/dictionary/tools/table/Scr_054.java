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

public class Scr_054 {
	
	public static final String SCR_NAM_054 = "SCR_NAM_054";
	public static final String SCR_POS_054 = "SCR_POS_054";
	public static final String INDEX_054 = "INDEX_054";
	public static final String SORT_054 = "SORT_054";

	private short index_054;
	private String scrNam_054;
	private short scrPos_054;
	private short sort_054;
	
	public Scr_054() {
		super();
	}

	public short getIndex_054() {
		return index_054;
	}

	public String getScrNam_054() {
		return scrNam_054;
	}

	public short getScrPos_054() {
		return scrPos_054;
	}

	public short getSort_054() {
		return sort_054;
	}

	public void setIndex_054(short index_054) {
		this.index_054 = index_054;
	}

	public void setScrNam_054(String scrNam_054) {
		this.scrNam_054 = JdbcTools.removeTrailingSpaces(scrNam_054);
	}

	public void setScrPos_054(short scrPos_054) {
		this.scrPos_054 = scrPos_054;
	}

	public void setSort_054(short sort_054) {
		this.sort_054 = sort_054;
	}
	
}
