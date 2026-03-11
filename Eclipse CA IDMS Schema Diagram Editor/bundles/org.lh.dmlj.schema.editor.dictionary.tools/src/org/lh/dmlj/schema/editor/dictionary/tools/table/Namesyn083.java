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
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.Rowid;

public class Namesyn083 {
	public static final String ROWID = "NAMESYN_083_ROWID";
	public static final String DEPEND_ON_083 = "DEPEND_ON_083";
	public static final String SYN_NAME_083 = "SYN_NAME_083";
	public static final String RDF_NAM_083 = "RDF_NAM_083";
	
	private Namesyn083 ourNamesyn083;
	private Rcdsyn079 rcdsyn079;
	private Sdr042 sdr042;
	
	private Rowid ourRowid;
	private String dependOn083;
	private String synName083;
	private String rdfNam083;
	
	public Rowid getRowid() {
		return ourRowid;
	}

	public String getDependOn083() {
		return dependOn083;
	}

	public Namesyn083 getNamesyn083() {
		return ourNamesyn083;
	}

	public Rcdsyn079 getRcdsyn079() {
		return rcdsyn079;
	}

	public String getRdfNam083() {
		return rdfNam083;
	}

	public Sdr042 getSdr042() {
		return sdr042;
	}

	public String getSynName083() {
		return synName083;
	}

	public void setRowid(Rowid rowid) {
		ourRowid = rowid;
	}

	public void setDependOn083(String dependOn083) {
		this.dependOn083 = JdbcTools.removeTrailingSpaces(dependOn083);
	}

	public void setNamesyn083(Namesyn083 namesyn083) {
		this.ourNamesyn083 = namesyn083;
	}

	public void setRcdsyn079(Rcdsyn079 rcdsyn079) {
		this.rcdsyn079 = rcdsyn079;
	}

	public void setRdfNam083(String rdfNam083) {
		this.rdfNam083 = JdbcTools.removeTrailingSpaces(rdfNam083);
	}

	public void setSdr042(Sdr042 sdr042) {
		this.sdr042 = sdr042;
	}

	public void setSynName083(String synName083) {
		this.synName083 = JdbcTools.removeTrailingSpaces(synName083);
	}

}
