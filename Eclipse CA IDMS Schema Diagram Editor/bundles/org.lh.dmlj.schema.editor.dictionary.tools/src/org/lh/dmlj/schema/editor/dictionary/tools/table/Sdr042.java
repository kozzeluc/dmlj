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

public class Sdr042 {
	public static final String ROWID = "SDR_042_ROWID";
	public static final String DR_NAM_042 = "DR_NAM_042";
	public static final String DR_LVL_042 = "DR_LVL_042";
	public static final String OCC_042 = "OCC_042";
	public static final String PIC_042 = "PIC_042";
	public static final String USE_042 = "USE_042";	
	
	private List<Sdes044> sdes044s = new ArrayList<>();
	
	private Rowid ourRowid;
	private String drNam042;
	private short drLvl042;
	private short occ042;
	private String pic042;
	private short use042;
	
	public Rowid getRowid() {
		return ourRowid;
	}

	public short getDrLvl042() {
		return drLvl042;
	}

	public String getDrNam042() {
		return drNam042;
	}

	public short getOcc042() {
		return occ042;
	}

	public String getPic042() {
		return pic042;
	}

	public List<Sdes044> getSdes044s() {
		return sdes044s;
	}

	public short getUse042() {
		return use042;
	}

	public void setRowid(Rowid rowid) {
		ourRowid = rowid;
	}

	public void setDrLvl042(short drLvl042) {
		this.drLvl042 = drLvl042;
	}

	public void setDrNam042(String drNam042) {
		this.drNam042 = JdbcTools.removeTrailingSpaces(drNam042);
	}

	public void setOcc042(short occ042) {
		this.occ042 = occ042;
	}

	public void setPic042(String pic042) {
		this.pic042 = JdbcTools.removeTrailingSpaces(pic042);
	}

	public void setUse042(short use042) {
		this.use042 = use042;
	}

}
