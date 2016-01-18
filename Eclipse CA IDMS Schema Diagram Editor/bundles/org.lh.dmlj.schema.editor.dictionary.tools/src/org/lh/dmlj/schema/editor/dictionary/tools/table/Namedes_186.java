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


public class Namedes_186 {

	public static final String CMT_ID_186 = "CMT_ID_186";
	public static final String CMT_INFO_186_1 = "CMT_INFO_186_1";
	
	private int cmtId_186;
	private String cmtInfo_186_1;
	
	public Namedes_186() {
		super();
	}

	public int getCmtId_186() {
		return cmtId_186;
	}

	public String getCmtInfo_186_1() {
		return cmtInfo_186_1;
	}

	public String getIxName_186() {
		return cmtInfo_186_1.substring(0, 32).trim();
	}

	public void setCmtId_186(int cmtId_186) {
		this.cmtId_186 = cmtId_186;
	}

	public void setCmtInfo_186_1(String cmtInfo_1_186) {
		this.cmtInfo_186_1 = cmtInfo_1_186; // trailing spaces are NOT removed
	}
	
}
