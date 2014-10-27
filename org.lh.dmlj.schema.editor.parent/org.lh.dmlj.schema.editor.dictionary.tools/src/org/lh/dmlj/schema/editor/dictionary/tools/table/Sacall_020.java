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

public class Sacall_020 {
	
	public static String CALL_PROC_020 = "CALL_PROC_020";
	public static String CALL_TIME_020 = "CALL_TIME_020";
	public static String DBP_ACCESS_020 = "DBP_ACCESS_020";
	public static String DBP_FUNC_020 = "DBP_FUNC_020";
	public static String DBP_MODE_020 = "DBP_MODE_020";
	
	private String callProc_020;
	private String callTime_020;
	private String dbpAccess_020;
	private String dbpFunc_020;
	private String dbpMode_020;

	public Sacall_020() {
		super();
	}
	
	public String getCallProc_020() {
		return callProc_020;
	}

	public String getCallTime_020() {
		return callTime_020;
	}

	public String getDbpAccess_020() {
		return dbpAccess_020;
	}

	public String getDbpFunc_020() {
		return dbpFunc_020;
	}

	public String getDbpMode_020() {
		return dbpMode_020;
	}

	public void setCallProc_020(String callProc_020) {
		this.callProc_020 = JdbcTools.removeTrailingSpaces(callProc_020);
	}

	public void setCallTime_020(String callTime_020) {
		this.callTime_020 = JdbcTools.removeTrailingSpaces(callTime_020);
	}

	public void setDbpAccess_020(String dbpAccess_020) {
		this.dbpAccess_020 = JdbcTools.removeTrailingSpaces(dbpAccess_020);
	}

	public void setDbpFunc_020(String dbpFunc_020) {
		this.dbpFunc_020 = JdbcTools.removeTrailingSpaces(dbpFunc_020);
	}

	public void setDbpMode_020(String dbpMode_020) {
		this.dbpMode_020 = JdbcTools.removeTrailingSpaces(dbpMode_020);
	}

}
