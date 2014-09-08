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

public class Namesyn_083 {
	
	@TableColumn public static final String ROWID = "NAMESYN_083.ROWID";
	@TableColumn public static final String DEPEND_ON_083 = "NAMESYN_083.DEPEND_ON_083";
	@TableColumn public static final String SYN_NAME_083 = "NAMESYN_083.SYN_NAME_083";
	@TableColumn public static final String RDF_NAM_083 = "NAMESYN_083.RDF_NAM_083";
	
	public static final String COLUMNS = JdbcTools.columnsFor(Namesyn_083.class);
	
	private Namesyn_083 namesyn_083;
	private Rcdsyn_079 rcdsyn_079;
	private Sdr_042 sdr_042;
	
	private long dbkey;
	private String dependOn_083;
	private String synName_083;
	private String rdfNam_083;
	
	public Namesyn_083() {
		super();
	}
	
	public long getDbkey() {
		return dbkey;
	}

	public String getDependOn_083() {
		return dependOn_083;
	}

	public Namesyn_083 getNamesyn_083() {
		return namesyn_083;
	}

	public Rcdsyn_079 getRcdsyn_079() {
		return rcdsyn_079;
	}

	public String getRdfNam_083() {
		return rdfNam_083;
	}

	public Sdr_042 getSdr_042() {
		return sdr_042;
	}

	public String getSynName_083() {
		return synName_083;
	}

	public void setDbkey(long dbkey) {
		this.dbkey = dbkey;
	}

	public void setDependOn_083(String dependOn_083) {
		this.dependOn_083 = JdbcTools.removeTrailingSpaces(dependOn_083);
	}

	public void setNamesyn_083(Namesyn_083 namesyn_083) {
		this.namesyn_083 = namesyn_083;
	}

	public void setRcdsyn_079(Rcdsyn_079 rcdsyn_079) {
		this.rcdsyn_079 = rcdsyn_079;
	}

	public void setRdfNam_083(String rdfNam_083) {
		this.rdfNam_083 = JdbcTools.removeTrailingSpaces(rdfNam_083);
	}

	public void setSdr_042(Sdr_042 sdr_042) {
		this.sdr_042 = sdr_042;
	}

	public void setSynName_083(String synName_083) {
		this.synName_083 = JdbcTools.removeTrailingSpaces(synName_083);
	}

}
