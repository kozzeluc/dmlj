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

import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;

public class Sam056 {
	public static final String SA_NAM_056 = "SA_NAM_056";
	public static final String SR_NAM_056 = "SR_NAM_056";
	
	private String saNam056;
	private String srNam056;
	
	public String getSaNam056() {
		return saNam056;
	}

	public String getSrNam056() {
		return srNam056;
	}

	public void setSaNam056(String saNam056) {
		this.saNam056 = JdbcTools.removeTrailingSpaces(saNam056);
	}

	public void setSrNam056(String srNam056) {
		this.srNam056 = JdbcTools.removeTrailingSpaces(srNam056);
	}

}
