/**
 * Copyright (C) 2021  Luc Hermans
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
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.Rowid;

public class Sr_036 implements IRowidProvider {
	
	public static final String ROWID = "SR_036_ROWID";
	public static final String SR_NAM_036 = "SR_NAM_036";
	public static final String RCD_VERS_036 = "RCD_VERS_036";
	
	private Rcdsyn_079 rcdsyn_079;	// record synonym referenced by schema
	private Rcdsyn_079 rcdsyn_079b; // base record synonym

	private Rowid rowid;
	private String srNam_036;
	private short rcdVers_036;
	
	public Sr_036() {
		super();
	}

	@Override
	public Rowid getRowid() {
		return rowid;
	}

	public Rcdsyn_079 getRcdsyn_079() {
		return rcdsyn_079;
	}

	public Rcdsyn_079 getRcdsyn_079b() {
		return rcdsyn_079b;
	}

	public short getRcdVers_036() {
		return rcdVers_036;
	}

	public String getSrNam_036() {
		return srNam_036;
	}

	public void setRowid(Rowid rowid) {
		this.rowid = rowid;
	}

	public void setRcdsyn_079(Rcdsyn_079 rcdsyn_079) {
		this.rcdsyn_079 = rcdsyn_079;
	}

	public void setRcdsyn_079b(Rcdsyn_079 rcdsyn_079b) {
		this.rcdsyn_079b = rcdsyn_079b;
	}

	public void setRcdVers_036(short rcdVers_036) {
		this.rcdVers_036 = rcdVers_036;
	}

	public void setSrNam_036(String srNam_036) {
		this.srNam_036 = JdbcTools.removeTrailingSpaces(srNam_036);
	}
	
}
