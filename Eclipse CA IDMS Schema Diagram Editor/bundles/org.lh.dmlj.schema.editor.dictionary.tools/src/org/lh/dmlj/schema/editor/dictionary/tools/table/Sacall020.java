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

public class Sacall020 {
	public static final String CALL_PROC_020 = "CALL_PROC_020";
	public static final String CALL_TIME_020 = "CALL_TIME_020";
	public static final String DBP_ACCESS_020 = "DBP_ACCESS_020";
	public static final String DBP_FUNC_020 = "DBP_FUNC_020";
	public static final String DBP_MODE_020 = "DBP_MODE_020";
	
	private String callProc020;
	private String callTime020;
	private String dbpAccess020;
	private String dbpFunc020;
	private String dbpMode020;
	
	public String getCallProc020() {
		return callProc020;
	}

	public String getCallTime020() {
		return callTime020;
	}

	public String getDbpAccess020() {
		return dbpAccess020;
	}

	public String getDbpFunc020() {
		return dbpFunc020;
	}

	public String getDbpMode020() {
		return dbpMode020;
	}

	public void setCallProc020(String callProc020) {
		this.callProc020 = JdbcTools.removeTrailingSpaces(callProc020);
	}

	public void setCallTime020(String callTime020) {
		this.callTime020 = JdbcTools.removeTrailingSpaces(callTime020);
	}

	public void setDbpAccess020(String dbpAccess020) {
		this.dbpAccess020 = JdbcTools.removeTrailingSpaces(dbpAccess020);
	}

	public void setDbpFunc020(String dbpFunc020) {
		this.dbpFunc020 = JdbcTools.removeTrailingSpaces(dbpFunc020);
	}

	public void setDbpMode020(String dbpMode020) {
		this.dbpMode020 = JdbcTools.removeTrailingSpaces(dbpMode020);
	}

}
