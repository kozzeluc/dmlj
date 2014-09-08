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

import java.util.ArrayList;
import java.util.List;

import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.TableColumn;

public class Smr_052 {
	
	@TableColumn public static final String ROWID = "SMR_052.ROWID";
	@TableColumn public static final String DUP_052 = "SMR_052.DUP_052";
	@TableColumn public static final String MR_CNTRL_052 = "SMR_052.MR_CNTRL_052";
	@TableColumn public static final String NXT_DBK_052 = "SMR_052.NXT_DBK_052";
	@TableColumn public static final String OWN_DBK_052 = "SMR_052.OWN_DBK_052";
	@TableColumn public static final String PRI_DBK_052 = "SMR_052.PRI_DBK_052";
	@TableColumn public static final String SET_NAM_052 = "SMR_052.SET_NAM_052";
	@TableColumn public static final String SORT_052 = "SMR_052.SORT_052";
	
	public static final String COLUMNS = JdbcTools.columnsFor(Smr_052.class);
	
	private List<Scr_054> scr_054s = new ArrayList<>();
	
	private long dbkey;
	private short dup_052;
	private short mrCntrl_052;
	private short nxtDbk_052;
	private short ownDbk_052;
	private short priDbk_052;
	private String setNam_052;
	private short sort_052;
	private Srcd_113 srcd_113;	
	
	public Smr_052() {
		super();
	}

	public long getDbkey() {
		return dbkey;
	}

	public short getDup_052() {
		return dup_052;
	}

	public short getMrCntrl_052() {
		return mrCntrl_052;
	}

	public short getNxtDbk_052() {
		return nxtDbk_052;
	}

	public short getOwnDbk_052() {
		return ownDbk_052;
	}

	public short getPriDbk_052() {
		return priDbk_052;
	}

	public List<Scr_054> getScr_054s() {
		return scr_054s;
	}

	public String getSetNam_052() {
		return setNam_052;
	}

	public short getSort_052() {
		return sort_052;
	}

	public Srcd_113 getSrcd_113() {
		return srcd_113;
	}

	public void setDbkey(long dbkey) {
		this.dbkey = dbkey;
	}

	public void setDup_052(short dup_052) {
		this.dup_052 = dup_052;
	}

	public void setMrCntrl_052(short mrCntrl_052) {
		this.mrCntrl_052 = mrCntrl_052;
	}

	public void setNxtDbk_052(short nxtDbk_052) {
		this.nxtDbk_052 = nxtDbk_052;
	}

	public void setOwnDbk_052(short ownDbk_052) {
		this.ownDbk_052 = ownDbk_052;
	}

	public void setPriDbk_052(short priDbk_052) {
		this.priDbk_052 = priDbk_052;
	}

	public void setSetNam_052(String setNam_052) {
		this.setNam_052 = JdbcTools.removeTrailingSpaces(setNam_052);
	}

	public void setSort_052(short sort_052) {
		this.sort_052 = sort_052;
	}

	public void setSrcd_113(Srcd_113 srcd_113) {
		this.srcd_113 = srcd_113;
	}
}
