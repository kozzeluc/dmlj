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

public class Srcd_113 {
	
	@TableColumn public static final String ROWID = "SRCD_113_ROWID";
	@TableColumn public static final String DSPL_113 = "DSPL_113";
	@TableColumn public static final String MIN_FRAG_113 = "MIN_FRAG_113";
	@TableColumn public static final String MIN_ROOT_113 = "MIN_ROOT_113";
	@TableColumn public static final String MODE_113 = "MODE_113";
	@TableColumn public static final String PAGE_COUNT_113 = "PAGE_COUNT_113";
	@TableColumn public static final String PAGE_COUNT_PERCENT_113 = "PAGE_COUNT_PERCENT_113";
	@TableColumn public static final String PAGE_OFFSET_113 = "PAGE_OFFSET_113";
	@TableColumn public static final String PAGE_OFFSET_PERCENT_113 = "PAGE_OFFSET_PERCENT_113";
	@TableColumn public static final String REC_TYPE_113 = "REC_TYPE_113";
	@TableColumn public static final String SR_ID_113 = "SR_ID_113";
	@TableColumn public static final String SUBAREA_113 = "SUBAREA_113";
	@TableColumn public static final String SYMBOL_DISPLACE_113 = "SYMBOL_DISPLACE_113";
	
	public static final String COLUMNS = JdbcTools.columnsFor(Srcd_113.class);
	
	private long dbkey;
	private Rcdsyn_079 rcdsyn_079;
	private Sam_056 sam_056;

	private short dspl_113;
	private short minFrag_113;
	private short minRoot_113;
	private short mode_113;
	private int pageCount_113;
	private short pageCountPercent_113;
	private int pageOffset_113;
	private short pageOffsetPercent_113;
	private String recType_113;
	private List<Srcall_040> srcall_040s = new ArrayList<>();
	private short srId_113;
	private String subarea_113;
	private String symbolDisplace_113;
	
	public Srcd_113() {
		super();
	}

	public long getDbkey() {
		return dbkey;
	}

	public short getDspl_113() {
		return dspl_113;
	}

	public short getMinFrag_113() {
		return minFrag_113;
	}

	public short getMinRoot_113() {
		return minRoot_113;
	}

	public short getMode_113() {
		return mode_113;
	}

	public int getPageCount_113() {
		return pageCount_113;
	}

	public short getPageCountPercent_113() {
		return pageCountPercent_113;
	}

	public int getPageOffset_113() {
		return pageOffset_113;
	}

	public short getPageOffsetPercent_113() {
		return pageOffsetPercent_113;
	}

	public Rcdsyn_079 getRcdsyn_079() {
		return rcdsyn_079;
	}
	
	public String getRecType_113() {
		return recType_113;
	}
	
	public Sam_056 getSam_056() {
		return sam_056;
	}

	public List<Srcall_040> getSrcall_040s() {
		return srcall_040s;
	}
	
	public short getSrId_113() {
		return srId_113;
	}

	public String getSubarea_113() {
		return subarea_113;
	}

	public String getSymbolDisplace_113() {
		return symbolDisplace_113;
	}

	public void setDbkey(long dbkey) {
		this.dbkey = dbkey;
	}

	public void setDspl_113(short dspl_113) {
		this.dspl_113 = dspl_113;
	}

	public void setMinFrag_113(short minFrag_113) {
		this.minFrag_113 = minFrag_113;
	}

	public void setMinRoot_113(short minRoot_113) {
		this.minRoot_113 = minRoot_113;
	}

	public void setMode_113(short mode_113) {
		this.mode_113 = mode_113;
	}

	public void setPageCount_113(int pageCount_113) {
		this.pageCount_113 = pageCount_113;
	}

	public void setPageCountPercent_113(short pageCountPercent_113) {
		this.pageCountPercent_113 = pageCountPercent_113;
	}

	public void setPageOffset_113(int pageOffset_113) {
		this.pageOffset_113 = pageOffset_113;
	}

	public void setPageOffsetPercent_113(short pageOffsetPercent_113) {
		this.pageOffsetPercent_113 = pageOffsetPercent_113;
	}

	public void setRcdsyn_079(Rcdsyn_079 rcdsyn_079) {
		this.rcdsyn_079 = rcdsyn_079;
	}

	public void setRecType_113(String recType_113) {
		this.recType_113 = JdbcTools.removeTrailingSpaces(recType_113);
	}

	public void setSam_056(Sam_056 sam_056) {		
		this.sam_056 = sam_056;
	}

	public void setSrId_113(short srId_113) {
		this.srId_113 = srId_113;
	}

	public void setSubarea_113(String subarea_113) {
		this.subarea_113 = JdbcTools.removeTrailingSpaces(subarea_113);
	}

	public void setSymbolDisplace_113(String symbolDisplace_113) {
		this.symbolDisplace_113 = JdbcTools.removeTrailingSpaces(symbolDisplace_113);
	}

}
