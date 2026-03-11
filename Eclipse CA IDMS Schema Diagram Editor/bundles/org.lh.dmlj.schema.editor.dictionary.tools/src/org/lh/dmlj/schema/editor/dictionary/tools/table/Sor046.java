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

public class Sor046 {
	public static final String ROWID = "SOR_046_ROWID";
	public static final String INDEX_DISP_046 = "INDEX_DISP_046";
	public static final String INDEX_MEMBERS_046 = "INDEX_MEMBERS_046";
	public static final String NXT_DBK_046 = "NXT_DBK_046";
	public static final String ORD_046 = "ORD_046";
	public static final String PAGE_COUNT_046 = "PAGE_COUNT_046";
	public static final String PAGE_COUNT_PERCENT_046 = "PAGE_COUNT_PERCENT_046";
	public static final String PAGE_OFFSET_046 = "PAGE_OFFSET_046";
	public static final String PAGE_OFFSET_PERCENT_046 = "PAGE_OFFSET_PERCENT_046";
	public static final String PRI_DBK_046 = "PRI_DBK_046";
	public static final String SA_NAM_046 = "SA_NAM_046";
	public static final String SET_MODE_046 = "SET_MODE_046";
	public static final String SET_NAM_046 = "SET_NAM_046";
	public static final String SET_ORD_046 = "SET_ORD_046";
	public static final String SOR_ID_046 = "SOR_ID_046";
	public static final String SUBAREA_046 = "SUBAREA_046";
	public static final String SYMBOL_INDEX_046 = "SYMBOL_INDEX_046";
	
	private Srcd113 srcd113;
	private List<Smr052> smr052s = new ArrayList<>();
	
	private Rowid ourRowid;
	private short indexDisp046;
	private short indexMembers046;
	private short nxtDbk046;
	private short ord046;
	private int pageCount046;
	private short pageCountPercent046;
	private int pageOffset046;
	private short pageOffsetPercent046;
	private short priDbk046;
	private String saNam046;
	private short setMode046;
	private String setNam046;
	private short setOrd046;
	private short sorId046;
	private String subarea046;
	private String symbolIndex046;

	public Rowid getRowid() {
		return ourRowid;
	}

	public short getIndexDisp046() {
		return indexDisp046;
	}

	public short getIndexMembers046() {
		return indexMembers046;
	}

	public short getNxtDbk046() {
		return nxtDbk046;
	}

	public short getOrd046() {
		return ord046;
	}

	public int getPageCount046() {
		return pageCount046;
	}

	public short getPageCountPercent046() {
		return pageCountPercent046;
	}

	public int getPageOffset046() {
		return pageOffset046;
	}

	public short getPageOffsetPercent046() {
		return pageOffsetPercent046;
	}

	public short getPriDbk046() {
		return priDbk046;
	}

	public String getSaNam046() {
		return saNam046;
	}

	public short getSetMode046() {
		return setMode046;
	}

	public String getSetNam046() {
		return setNam046;
	}

	public short getSetOrd046() {
		return setOrd046;
	}

	public List<Smr052> getSmr052s() {
		return smr052s;
	}

	public short getSorId046() {
		return sorId046;
	}

	public Srcd113 getSrcd113() {
		return srcd113;
	}

	public String getSubarea046() {
		return subarea046;
	}

	public String getSymbolIndex046() {
		return symbolIndex046;
	}

	public void setRowid(Rowid rowid) {
		ourRowid = rowid;
	}

	public void setIndexDisp046(short indexDisp046) {
		this.indexDisp046 = indexDisp046;
	}

	public void setIndexMembers046(short indexMembers046) {
		this.indexMembers046 = indexMembers046;
	}

	public void setNxtDbk046(short nxtDbk046) {
		this.nxtDbk046 = nxtDbk046;
	}

	public void setPageCount046(int pageCount046) {
		this.pageCount046 = pageCount046;
	}

	public void setPageCountPercent046(short pageCountPercent046) {
		this.pageCountPercent046 = pageCountPercent046;
	}

	public void setPageOffset046(int pageOffset046) {
		this.pageOffset046 = pageOffset046;
	}

	public void setPageOffsetPercent046(short pageOffsetPercent046) {
		this.pageOffsetPercent046 = pageOffsetPercent046;
	}

	public void setPriDbk046(short priDbk046) {
		this.priDbk046 = priDbk046;
	}

	public void setSaNam046(String saNam046) {
		this.saNam046 = JdbcTools.removeTrailingSpaces(saNam046);
	}

	public void setOrd046(short ord046) {
		this.ord046 = ord046;
	}

	public void setSetMode046(short setMode046) {
		this.setMode046 = setMode046;
	}

	public void setSetNam046(String setNam046) {
		this.setNam046 = JdbcTools.removeTrailingSpaces(setNam046);
	}

	public void setSetOrd046(short setOrd046) {
		this.setOrd046 = setOrd046;
	}

	public void setSorId046(short sorId046) {
		this.sorId046 = sorId046;
	}

	public void setSrcd113(Srcd113 srcd113) {
		this.srcd113 = srcd113;
	}

	public void setSubarea046(String subarea046) {
		this.subarea046 = JdbcTools.removeTrailingSpaces(subarea046);
	}

	public void setSymbolIndex046(String symbolIndex046) {
		this.symbolIndex046 = JdbcTools.removeTrailingSpaces(symbolIndex046);
	}

}
