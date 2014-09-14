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

public class Sdes_044 {

	@TableColumn public static final String CMT_ID_044 = "CMT_ID_044";
	@TableColumn public static final String CMT_INFO_044_1 = "CMT_INFO_044_1";
	@TableColumn public static final String CMT_INFO_044_2 = "CMT_INFO_044_2";
	
	public static final String COLUMNS = JdbcTools.columnsFor(Sdes_044.class);
	
	private int cmtId_044;
	private String cmtInfo_044_1;
	private String cmtInfo_044_2;
	
	public Sdes_044() {
		super();
	}

	public String getAsfFieldName_044() {
		return cmtInfo_044_1;
	}

	public int getCmtId_044() {
		return cmtId_044;
	}

	public String getCmtInfo_044_1() {
		return cmtInfo_044_1;
	}

	public String getCmtInfo_044_2() {
		return cmtInfo_044_2;
	}

	public String getVal1_044() {
		return JdbcTools.removeTrailingSpaces(cmtInfo_044_1.substring(4, 38));
	}

	public String getVal2_044() {
		// we don't have the actual length available because these fields are in a redefining group;
		// we should be getting away with this because if a string value would contain trailing
		// spaces, we are saved by the ending quote
		return JdbcTools.removeTrailingSpaces(cmtInfo_044_1.substring(38) + cmtInfo_044_2);
	}

	public void setCmtId_044(int cmtId_044) {
		this.cmtId_044 = cmtId_044;
	}

	public void setCmtInfo_044_1(String cmtInfo_044_1) {
		this.cmtInfo_044_1 = cmtInfo_044_1;					// trailing spaces are NOT removed
	}

	public void setCmtInfo_044_2(String cmtInfo_044_2) {
		this.cmtInfo_044_2 = cmtInfo_044_2;					// trailing spaces are NOT removed
	}
	
}
