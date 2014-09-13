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
		return JdbcTools.removeTrailingSpaces(getCmt_044().substring(0, 32));
	}

	public int getCmtId_044() {
		return cmtId_044;
	}

	public String getCmt_044() {
		return cmtInfo_044_1 + cmtInfo_044_2; // should always be a String of length 100
	}
	
	public String getCmtInfo_044_1() {
		return cmtInfo_044_1;
	}

	public String getCmtInfo_044_2() {
		return cmtInfo_044_2;
	}

	public short getValLgth2_044() {
		return (short) getVal2_044().trim().length(); // TODO modify this to extract the real column value
	}

	public String getVal1_044() {
		return getCmt_044().substring(4, 38);
	}

	public String getVal2_044() {
		return getCmt_044().substring(38);
	}

	public void setCmtId_044(int cmtId_044) {
		this.cmtId_044 = cmtId_044;
	}

	public void setCmtInfo_044_1(String cmtInfo_044_1) {
		this.cmtInfo_044_1 = cmtInfo_044_1; // trailing spaces are NOT removed here !
	}

	public void setCmtInfo_044_2(String cmtInfo_044_2) {
		this.cmtInfo_044_2 = cmtInfo_044_2; // trailing spaces are NOT removed here !
	}
	
}
