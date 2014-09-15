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

public class Sa_018 {
	
	public static final String ROWID = "SA_018_ROWID";
	public static final String SA_NAM_018 = "SA_NAM_018";
	
	private long dbkey;
	private String saNam_018;
	private List<Sacall_020> sacall_020s = new ArrayList<>();
	
	public Sa_018() {
		super();
	}

	public long getDbkey() {
		return dbkey;
	}

	public List<Sacall_020> getSacall_020s() {
		return sacall_020s;
	}

	public String getSaNam_018() {
		return saNam_018;
	}

	public void setDbkey(long dbkey) {
		this.dbkey = dbkey;
	}

	public void setSaNam_018(String saNam_018) {
		this.saNam_018 = JdbcTools.removeTrailingSpaces(saNam_018);
	}

}
