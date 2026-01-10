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


public class Namedes186 {
	public static final String CMT_ID_186 = "CMT_ID_186";
	public static final String CMT_INFO_186_1 = "CMT_INFO_186_1";
	
	private int cmtId186;
	private String firstCmtInfo186;

	public int getCmtId186() {
		return cmtId186;
	}

	public String getFirstCmtInfo186() {
		return firstCmtInfo186;
	}

	public String getIxName186() {
		return firstCmtInfo186.substring(0, 32).trim();
	}

	public void setCmtId186(int cmtId186) {
		this.cmtId186 = cmtId186;
	}

	public void setFirstCmtInfo186(String firstCmtInfo186) {
		this.firstCmtInfo186 = firstCmtInfo186; // trailing spaces are NOT removed
	}
	
}
