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
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.Rowid;

public class Sr036 implements IRowidProvider {
	public static final String ROWID = "SR_036_ROWID";
	public static final String SR_NAM_036 = "SR_NAM_036";
	public static final String RCD_VERS_036 = "RCD_VERS_036";
	
	private Rcdsyn079 rcdsyn079;		// record synonym referenced by schema
	private Rcdsyn079 rcdsyn079b;	// base record synonym

	private Rowid ourRowid;
	private String srNam036;
	private short rcdVers036;

	@Override
	public Rowid getRowid() {
		return ourRowid;
	}

	public Rcdsyn079 getRcdsyn079() {
		return rcdsyn079;
	}

	public Rcdsyn079 getRcdsyn079b() {
		return rcdsyn079b;
	}

	public short getRcdVers036() {
		return rcdVers036;
	}

	public String getSrNam036() {
		return srNam036;
	}

	public void setRowid(Rowid rowid) {
		ourRowid = rowid;
	}

	public void setRcdsyn079(Rcdsyn079 rcdsyn079) {
		this.rcdsyn079 = rcdsyn079;
	}

	public void setRcdsyn079b(Rcdsyn079 rcdsyn079b) {
		this.rcdsyn079b = rcdsyn079b;
	}

	public void setRcdVers036(short rcdVers036) {
		this.rcdVers036 = rcdVers036;
	}

	public void setSrNam036(String srNam036) {
		this.srNam036 = JdbcTools.removeTrailingSpaces(srNam036);
	}
	
}
