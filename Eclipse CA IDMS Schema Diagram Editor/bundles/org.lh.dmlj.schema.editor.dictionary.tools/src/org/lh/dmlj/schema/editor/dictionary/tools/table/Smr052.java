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

public class Smr052 {
	public static final String ROWID = "SMR_052_ROWID";
	public static final String DUP_052 = "DUP_052";
	public static final String MR_CNTRL_052 = "MR_CNTRL_052";
	public static final String NXT_DBK_052 = "NXT_DBK_052";
	public static final String OWN_DBK_052 = "OWN_DBK_052";
	public static final String PRI_DBK_052 = "PRI_DBK_052";
	public static final String SET_NAM_052 = "SET_NAM_052";
	public static final String SORT_052 = "SORT_052";
	
	private List<Scr054> scr054s = new ArrayList<>();
	
	private Rowid ourRowid;
	private short dup052;
	private short mrCntrl052;
	private short nxtDbk052;
	private short ownDbk052;
	private short priDbk052;
	private String setNam052;
	private short sort052;
	private Srcd113 srcd113;

	public Rowid getRowid() {
		return ourRowid;
	}

	public short getDup052() {
		return dup052;
	}

	public short getMrCntrl052() {
		return mrCntrl052;
	}

	public short getNxtDbk052() {
		return nxtDbk052;
	}

	public short getOwnDbk052() {
		return ownDbk052;
	}

	public short getPriDbk052() {
		return priDbk052;
	}

	public List<Scr054> getScr054s() {
		return scr054s;
	}

	public String getSetNam052() {
		return setNam052;
	}

	public short getSort052() {
		return sort052;
	}

	public Srcd113 getSrcd113() {
		return srcd113;
	}

	public void setRowid(Rowid rowid) {
		ourRowid = rowid;
	}

	public void setDup052(short dup052) {
		this.dup052 = dup052;
	}

	public void setMrCntrl052(short mrCntrl052) {
		this.mrCntrl052 = mrCntrl052;
	}

	public void setNxtDbk052(short nxtDbk052) {
		this.nxtDbk052 = nxtDbk052;
	}

	public void setOwnDbk052(short ownDbk052) {
		this.ownDbk052 = ownDbk052;
	}

	public void setPriDbk052(short priDbk052) {
		this.priDbk052 = priDbk052;
	}

	public void setSetNam052(String setNam052) {
		this.setNam052 = JdbcTools.removeTrailingSpaces(setNam052);
	}

	public void setSort052(short sort052) {
		this.sort052 = sort052;
	}

	public void setSrcd113(Srcd113 srcd113) {
		this.srcd113 = srcd113;
	}
}
