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

import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.TableColumn;

public class Sam_056 {

	@TableColumn public static final String SA_NAM_056 = "SA_NAM_056";
	@TableColumn public static final String SR_NAM_056 = "SR_NAM_056";
	
	public static final String COLUMNS = JdbcTools.columnsFor(Sam_056.class);
	
	private String saNam_056;
	private String srNam_056;

	public Sam_056() {
		super();
	}
	
	public String getSaNam_056() {
		return saNam_056;
	}

	public String getSrNam_056() {
		return srNam_056;
	}

	public void setSaNam_056(String saNam_056) {
		this.saNam_056 = JdbcTools.removeTrailingSpaces(saNam_056);
	}

	public void setSrNam_056(String srNam_056) {
		this.srNam_056 = JdbcTools.removeTrailingSpaces(srNam_056);
	}

}
