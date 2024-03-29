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

import java.util.ArrayList;
import java.util.List;

import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.Rowid;

public class Rcdsyn_079 implements IRowidProvider {
	
	public static final String ROWID = "RCDSYN_079_ROWID";
	public static final String RSYN_NAME_079 = "RSYN_NAME_079";
	public static final String RSYN_VER_079 = "RSYN_VER_079";
	
	private Sr_036 sr_036;
	private List<Namesyn_083> namesyn_083s = new ArrayList<>();
	
	private Rowid rowid;	
	private String rsynName_079;
	private short rsynVer_079;

	public Rcdsyn_079() {
		super();
	}
	
	@Override
	public Rowid getRowid() {
		return rowid;
	}

	public List<Namesyn_083> getNamesyn_083s() {
		return namesyn_083s;
	}

	public Namesyn_083 getNamesyn_083(Rowid rowidOfSdr_042) {
		for (Namesyn_083 namesyn_083 : namesyn_083s) {
			Sdr_042 sdr_042 = namesyn_083.getSdr_042();
			if (sdr_042.getRowid().equals(rowidOfSdr_042)) {
				return namesyn_083;
			}
		}
		throw new RuntimeException("internal error: no NAMESYN-083 for an SDR-042 with rowid " + rowidOfSdr_042.getHexString() + 
								   " under the RCDSYN-079 with rowid " + rowid.getHexString());
	}

	public Sr_036 getSr_036() {
		return sr_036;
	}

	public String getRsynName_079() {
		return rsynName_079;
	}

	public short getRsynVer_079() {
		return rsynVer_079;
	}

	public void setRowid(Rowid rowid) {
		this.rowid = rowid;
	}

	public void setRsynName_079(String rsynName_079) {
		this.rsynName_079 = JdbcTools.removeTrailingSpaces(rsynName_079);
	}

	public void setRsynVer_079(short rsynVer_079) {
		this.rsynVer_079 = rsynVer_079;
	}

	public void setSr_036(Sr_036 sr_036) {
		this.sr_036 = sr_036;
	}
	
}
