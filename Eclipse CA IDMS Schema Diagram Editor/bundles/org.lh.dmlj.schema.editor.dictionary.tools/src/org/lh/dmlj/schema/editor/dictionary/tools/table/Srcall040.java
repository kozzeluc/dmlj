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

public class Srcall040 {
	public static final String CALL_PROC_040 = "CALL_PROC_040";
	public static final String CALL_TIME_040 = "CALL_TIME_040";
	public static final String DBP_FUNC_040 = "DBP_FUNC_040";
	
	private String callProc040;
	private String callTime040;
	private String dbpFunc040;
	
	public String getCallProc040() {
		return callProc040;
	}

	public String getCallTime040() {
		return callTime040;
	}

	public String getDbpFunc040() {
		return dbpFunc040;
	}

	public void setCallProc040(String callProc040) {
		this.callProc040 = JdbcTools.removeTrailingSpaces(callProc040);
	}

	public void setCallTime040(String callTime040) {
		this.callTime040 = JdbcTools.removeTrailingSpaces(callTime040);
	}

	public void setDbpFunc040(String dbpFunc040) {
		this.dbpFunc040 = JdbcTools.removeTrailingSpaces(dbpFunc040);
	}

}
