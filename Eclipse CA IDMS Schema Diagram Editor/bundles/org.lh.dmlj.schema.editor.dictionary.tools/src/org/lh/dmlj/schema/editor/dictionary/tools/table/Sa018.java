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

public class Sa018 {
	public static final String ROWID = "SA_018_ROWID";
	public static final String SA_NAM_018 = "SA_NAM_018";
	
	private Rowid ourRowid;
	private String saNam018;
	private List<Sacall020> sacall020s = new ArrayList<>();	

	public Rowid getRowid() {
		return ourRowid;
	}

	public List<Sacall020> getSacall020s() {
		return sacall020s;
	}

	public String getSaNam018() {
		return saNam018;
	}

	public void setRowid(Rowid rowid) {
		ourRowid = rowid;
	}

	public void setSaNam018(String saNam018) {
		this.saNam018 = JdbcTools.removeTrailingSpaces(saNam018);
	}

}
