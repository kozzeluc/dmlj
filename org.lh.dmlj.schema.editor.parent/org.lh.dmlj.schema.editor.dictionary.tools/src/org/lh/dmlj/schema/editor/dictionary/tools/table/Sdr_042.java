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

public class Sdr_042 {
	
	@TableColumn public static final String ROWID = "SDR_042_ROWID";
	@TableColumn public static final String DR_NAM_042 = "DR_NAM_042";
	@TableColumn public static final String DR_LVL_042 = "DR_LVL_042";
	@TableColumn public static final String OCC_042 = "OCC_042";
	@TableColumn public static final String PIC_042 = "PIC_042";
	@TableColumn public static final String USE_042 = "USE_042";	
	
	public static final String COLUMNS = JdbcTools.columnsFor(Sdr_042.class);
	
	private List<Sdes_044> Sdes_044s = new ArrayList<>();
	
	private long dbkey;
	private String drNam_042;
	private short drLvl_042;
	private short occ_042;
	private String pic_042;
	private short use_042;
	
	public Sdr_042() {
		super();
	}
	
	public long getDbkey() {
		return dbkey;
	}

	public short getDrLvl_042() {
		return drLvl_042;
	}

	public String getDrNam_042() {
		return drNam_042;
	}

	public short getOcc_042() {
		return occ_042;
	}

	public String getPic_042() {
		return pic_042;
	}

	public List<Sdes_044> getSdes_044s() {
		return Sdes_044s;
	}

	public short getUse_042() {
		return use_042;
	}

	public void setDbkey(long dbkey) {
		this.dbkey = dbkey;
	}

	public void setDrLvl_042(short drLvl_042) {
		this.drLvl_042 = drLvl_042;
	}

	public void setDrNam_042(String drNam_042) {
		this.drNam_042 = JdbcTools.removeTrailingSpaces(drNam_042);
	}

	public void setOcc_042(short occ_042) {
		this.occ_042 = occ_042;
	}

	public void setPic_042(String pic_042) {
		this.pic_042 = JdbcTools.removeTrailingSpaces(pic_042);
	}

	public void setUse_042(short use_042) {
		this.use_042 = use_042;
	}

}
