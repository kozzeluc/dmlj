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

public class Sdes044 {
	public static final String CMT_ID_044 = "CMT_ID_044";
	public static final String CMT_INFO_044_1 = "CMT_INFO_044_1";
	public static final String CMT_INFO_044_2 = "CMT_INFO_044_2";
	
	private int cmtId044;
	private String firstCmtInfo044;
	private String secondCmtInfo044;

	public String getAsfFieldName044() {
		return getFirstCmtInfo044();
	}

	public int getCmtId044() {
		return cmtId044;
	}

	public String getFirstCmtInfo044() {
		return firstCmtInfo044;
	}

	public String getSecondCmtInfo044() {
		return secondCmtInfo044;
	}

	public String getVal1044() {
		return JdbcTools.removeTrailingSpaces(firstCmtInfo044.substring(4, 38));
	}

	public String getVal2044() {
		// we don't have the actual length available because these fields are in a redefining group; we should be
		// getting away with this because if a string value would contain trailing spaces, we are saved by the ending quote
		return JdbcTools.removeTrailingSpaces(firstCmtInfo044.substring(38) + secondCmtInfo044);
	}

	public void setCmtId044(int cmtId044) {
		this.cmtId044 = cmtId044;
	}

	public void setFirstCmtInfo044(String firstCmtInfo044) {
		this.firstCmtInfo044 = firstCmtInfo044;					// trailing spaces are NOT removed
	}

	public void setSecondCmtInfo044(String secondInfo044) {
		this.secondCmtInfo044 = secondInfo044;					// trailing spaces are NOT removed
	}
	
}
