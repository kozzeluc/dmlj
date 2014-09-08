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

	@TableColumn public static final String CMT_ID_044 = "SDES_044.CMT_ID_044";
	@TableColumn public static final String ASF_FIELD_NAME_044 = "SDES_044.ASF_FIELD_NAME_044";
	@TableColumn public static final String VAL_LGTH2_044 = "SDES_044.VAL_LGTH2_044";
	@TableColumn public static final String VAL1_044 = "SDES_044.VAL1_044";
	@TableColumn public static final String VAL2_044 = "SDES_044.VAL2_044";
	
	public static final String COLUMNS = JdbcTools.columnsFor(Sdes_044.class);
	
	private String asfFieldName_044;
	private int cmtId_044;
	private short valLgth2_044;
	private String val1_044;
	private String val2_044;
	
	public Sdes_044() {
		super();
	}

	public String getAsfFieldName_044() {
		return asfFieldName_044;
	}

	public int getCmtId_044() {
		return cmtId_044;
	}

	public short getValLgth2_044() {
		return valLgth2_044;
	}

	public String getVal1_044() {
		return val1_044;
	}

	public String getVal2_044() {
		return val2_044;
	}

	public void setAsfFieldName_044(String asfFieldName_044) {
		this.asfFieldName_044 = asfFieldName_044;
	}

	public void setCmtId_044(int cmtId_044) {
		this.cmtId_044 = cmtId_044;
	}

	public void setVal1_044(String val1_044) {
		this.val1_044 = JdbcTools.removeTrailingSpaces(val1_044);
	}

	public void setVal2_044(String val2_044) {
		this.val2_044 = JdbcTools.removeTrailingSpaces(val2_044);
	}

	public void setValLgth2_044(short valLgth2_044) {
		this.valLgth2_044 = valLgth2_044;
	}
	
}
