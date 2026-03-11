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

import java.util.ArrayList;
import java.util.List;

import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.Rowid;

public class Rcdsyn079 implements IRowidProvider {
	public static final String ROWID = "RCDSYN_079_ROWID";
	public static final String RSYN_NAME_079 = "RSYN_NAME_079";
	public static final String RSYN_VER_079 = "RSYN_VER_079";
	
	private Sr036 sr036;
	private List<Namesyn083> namesyn083s = new ArrayList<>();
	
	private Rowid ourRowid;	
	private String rsynName079;
	private short rsynVer079;
	
	@Override
	public Rowid getRowid() {
		return ourRowid;
	}

	public List<Namesyn083> getNamesyn083s() {
		return namesyn083s;
	}

	public Namesyn083 getNamesyn083(Rowid rowidOfSdr042) {
		for (Namesyn083 namesyn_083 : namesyn083s) {
			var sdr042 = namesyn_083.getSdr042();
			if (sdr042.getRowid().equals(rowidOfSdr042)) {
				return namesyn_083;
			}
		}
		throw new IllegalStateException("internal error: no NAMESYN-083 for an SDR-042 with rowid " + rowidOfSdr042.getHexString() +
				" under the RCDSYN-079 with rowid " + ourRowid.getHexString());
	}

	public Sr036 getSr036() {
		return sr036;
	}

	public String getRsynName079() {
		return rsynName079;
	}

	public short getRsynVer079() {
		return rsynVer079;
	}

	public void setRowid(Rowid rowid) {
		ourRowid = rowid;
	}

	public void setRsynName079(String rsynName079) {
		this.rsynName079 = JdbcTools.removeTrailingSpaces(rsynName079);
	}

	public void setRsynVer079(short rsynVer079) {
		this.rsynVer079 = rsynVer079;
	}

	public void setSr036(Sr036 sr036) {
		this.sr036 = sr036;
	}
	
}
