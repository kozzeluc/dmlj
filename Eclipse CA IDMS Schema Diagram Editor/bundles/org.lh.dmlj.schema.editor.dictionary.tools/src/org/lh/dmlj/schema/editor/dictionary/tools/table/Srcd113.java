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

public class Srcd113 {
	public static final String ROWID = "SRCD_113_ROWID";
	public static final String DSPL_113 = "DSPL_113";
	public static final String MIN_FRAG_113 = "MIN_FRAG_113";
	public static final String MIN_ROOT_113 = "MIN_ROOT_113";
	public static final String MODE_113 = "MODE_113";
	public static final String PAGE_COUNT_113 = "PAGE_COUNT_113";
	public static final String PAGE_COUNT_PERCENT_113 = "PAGE_COUNT_PERCENT_113";
	public static final String PAGE_OFFSET_113 = "PAGE_OFFSET_113";
	public static final String PAGE_OFFSET_PERCENT_113 = "PAGE_OFFSET_PERCENT_113";
	public static final String REC_TYPE_113 = "REC_TYPE_113";
	public static final String SR_ID_113 = "SR_ID_113";
	public static final String SUBAREA_113 = "SUBAREA_113";
	public static final String SYMBOL_DISPLACE_113 = "SYMBOL_DISPLACE_113";
	public static final String VSAM_TYPE_113 = "VSAM_TYPE_113";
	
	private Rowid ourRowid;
	private Rcdsyn079 rcdsyn079;
	private Sam056 sam056;

	private short dspl113;
	private short minFrag113;
	private short minRoot113;
	private short mode113;
	private int pageCount113;
	private short pageCountPercent113;
	private int pageOffset113;
	private short pageOffsetPercent113;
	private String recType113;
	private List<Srcall040> srcall040s = new ArrayList<>();
	private short srId113;
	private String subarea113;
	private String symbolDisplace113;
	private String vsamType113;

	public Rowid getRowid() {
		return ourRowid;
	}

	public short getDspl113() {
		return dspl113;
	}

	public short getMinFrag113() {
		return minFrag113;
	}

	public short getMinRoot113() {
		return minRoot113;
	}

	public short getMode113() {
		return mode113;
	}

	public int getPageCount113() {
		return pageCount113;
	}

	public short getPageCountPercent113() {
		return pageCountPercent113;
	}

	public int getPageOffset113() {
		return pageOffset113;
	}

	public short getPageOffsetPercent113() {
		return pageOffsetPercent113;
	}

	public Rcdsyn079 getRcdsyn079() {
		return rcdsyn079;
	}
	
	public String getRecType113() {
		return recType113;
	}
	
	public Sam056 getSam056() {
		return sam056;
	}

	public List<Srcall040> getSrcall040s() {
		return srcall040s;
	}
	
	public short getSrId113() {
		return srId113;
	}

	public String getSubarea113() {
		return subarea113;
	}

	public String getSymbolDisplace113() {
		return symbolDisplace113;
	}
	
	public String getVsamType113() {
		return vsamType113;
	}

	public void setRowid(Rowid rowid) {
		ourRowid = rowid;
	}

	public void setDspl113(short dspl113) {
		this.dspl113 = dspl113;
	}

	public void setMinFrag113(short minFrag113) {
		this.minFrag113 = minFrag113;
	}

	public void setMinRoot113(short minRoot113) {
		this.minRoot113 = minRoot113;
	}

	public void setMode113(short mode113) {
		this.mode113 = mode113;
	}

	public void setPageCount113(int pageCount113) {
		this.pageCount113 = pageCount113;
	}

	public void setPageCountPercent113(short pageCountPercent113) {
		this.pageCountPercent113 = pageCountPercent113;
	}

	public void setPageOffset113(int pageOffset113) {
		this.pageOffset113 = pageOffset113;
	}

	public void setPageOffsetPercent113(short pageOffsetPercent113) {
		this.pageOffsetPercent113 = pageOffsetPercent113;
	}

	public void setRcdsyn079(Rcdsyn079 rcdsyn079) {
		this.rcdsyn079 = rcdsyn079;
	}

	public void setRecType113(String recType113) {
		this.recType113 = JdbcTools.removeTrailingSpaces(recType113);
	}

	public void setSam056(Sam056 sam056) {
		this.sam056 = sam056;
	}

	public void setSrId113(short srId113) {
		this.srId113 = srId113;
	}

	public void setSubarea113(String subarea113) {
		this.subarea113 = JdbcTools.removeTrailingSpaces(subarea113);
	}

	public void setSymbolDisplace113(String symbolDisplace113) {
		this.symbolDisplace113 = JdbcTools.removeTrailingSpaces(symbolDisplace113);
	}
	
	public void setVsamType113(String vsamType113) {
		this.vsamType113 = JdbcTools.removeTrailingSpaces(vsamType113);
	}

}
