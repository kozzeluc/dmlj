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

public class Srcall_040 {
	
	@TableColumn public static String CALL_PROC_040 = "SRCALL_040.CALL_PROC_040";
	@TableColumn public static String CALL_TIME_040 = "SRCALL_040.CALL_TIME_040";
	@TableColumn public static String DBP_FUNC_040 = "SRCALL_040.DBP_FUNC_040";
	
	public static final String COLUMNS = JdbcTools.columnsFor(Srcall_040.class);
	
	private String callProc_040;
	private String callTime_040;
	private String dbpFunc_040;
	
	public Srcall_040() {
		super();
	}
	
	public String getCallProc_040() {
		return callProc_040;
	}

	public String getCallTime_040() {
		return callTime_040;
	}

	public String getDbpFunc_040() {
		return dbpFunc_040;
	}

	public void setCallProc_040(String callProc_040) {
		this.callProc_040 = JdbcTools.removeTrailingSpaces(callProc_040);
	}

	public void setCallTime_040(String callTime_040) {
		this.callTime_040 = JdbcTools.removeTrailingSpaces(callTime_040);
	}

	public void setDbpFunc_040(String dbpFunc_040) {
		this.dbpFunc_040 = JdbcTools.removeTrailingSpaces(dbpFunc_040);
	}

}
