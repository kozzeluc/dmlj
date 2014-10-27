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

public class Constraint_1029 {

	public static final String COMPRESS_1029 = "CONSTRAINT_1029_COMPRESS";
	public static final String DISPLACEMENT_1029 = "CONSTRAINT_1029_DISPLACEMENT";
	public static final String IXBLKCONTAINS_1029 = "CONSTRAINT_1029_IXBLKCONTAINS";
	public static final String NAME_1029 = "CONSTRAINT_1029_NAME";
	public static final String NEXT_1029 = "CONSTRAINT_1029_NEXT";
	public static final String OWNER_1029 = "CONSTRAINT_1029_OWNER";
	public static final String PRIOR_1029 = "CONSTRAINT_1029_PRIOR";
	public static final String REFNEXT_1029 = "CONSTRAINT_1029_REFNEXT";
	public static final String REFPRIOR_1029 = "CONSTRAINT_1029_REFPRIOR";
	public static final String SORTORDER_1029 = "CONSTRAINT_1029_SORTORDER";
	public static final String TABLE_1029 = "CONSTRAINT_1029_TABLE";
	public static final String TYPE_1029 = "CONSTRAINT_1029_TYPE";
	public static final String UNIQUE_1029 = "CONSTRAINT_1029_UNIQUE";
	
	private List<Constkey_1030> constkey_1030s = new ArrayList<>();
	private List<Orderkey_1044> orderkey_1044s = new ArrayList<>();
	private Table_1050 referencedTable_1050;
	private Table_1050 referencingTable_1050;

	private String compress_1029;
	private short displacement_1029;
	private short ixblkcontains_1029;
	private String name_1029;
	private short next_1029;
	private short owner_1029;
	private short prior_1029;
	private short refnext_1029;
	private short refprior_1029;
	private String sortorder_1029;
	private String type_1029;
	private String unique_1029;
	
	public Constraint_1029() {
		super();
	}

	public String getCompress_1029() {
		return compress_1029;
	}

	public List<Constkey_1030> getConstkey_1030s() {
		return constkey_1030s;
	}

	public short getDisplacement_1029() {
		return displacement_1029;
	}

	public short getIxblkcontains_1029() {
		return ixblkcontains_1029;
	}

	public String getName_1029() {
		return name_1029;
	}

	public short getNext_1029() {
		return next_1029;
	}

	public List<Orderkey_1044> getOrderkey_1044s() {
		return orderkey_1044s;
	}

	public short getOwner_1029() {
		return owner_1029;
	}

	public short getPrior_1029() {
		return prior_1029;
	}

	public Table_1050 getReferencedTable_1050() {
		return referencedTable_1050;
	}

	public Table_1050 getReferencingTable_1050() {
		return referencingTable_1050;
	}

	public short getRefnext_1029() {
		return refnext_1029;
	}

	public short getRefprior_1029() {
		return refprior_1029;
	}

	public String getSortorder_1029() {
		return sortorder_1029;
	}

	public String getType_1029() {
		return type_1029;
	}

	public String getUnique_1029() {
		return unique_1029;
	}

	public void setCompress_1029(String compress_1029) {
		this.compress_1029 = JdbcTools.removeTrailingSpaces(compress_1029);
	}

	public void setDisplacement_1029(short displacement_1029) {
		this.displacement_1029 = displacement_1029;
	}

	public void setIxblkcontains_1029(short ixblkcontains_1029) {
		this.ixblkcontains_1029 = ixblkcontains_1029;
	}

	public void setName_1029(String name_1029) {
		this.name_1029 = JdbcTools.removeTrailingSpaces(name_1029);
	}

	public void setNext_1029(short next_1029) {
		this.next_1029 = next_1029;
	}

	public void setOwner_1029(short owner_1029) {
		this.owner_1029 = owner_1029;
	}

	public void setPrior_1029(short prior_1029) {
		this.prior_1029 = prior_1029;
	}

	public void setReferencedTable_1050(Table_1050 referencedTable_1050) {
		this.referencedTable_1050 = referencedTable_1050;
	}

	public void setReferencingTable_1050(Table_1050 referencingTable_1050) {
		this.referencingTable_1050 = referencingTable_1050;
	}

	public void setRefnext_1029(short refnext_1029) {
		this.refnext_1029 = refnext_1029;
	}

	public void setRefprior_1029(short refprior_1029) {
		this.refprior_1029 = refprior_1029;
	}

	public void setSortorder_1029(String sortorder_1029) {
		this.sortorder_1029 = JdbcTools.removeTrailingSpaces(sortorder_1029);
	}

	public void setType_1029(String type_1029) {
		this.type_1029 = JdbcTools.removeTrailingSpaces(type_1029);
	}

	public void setUnique_1029(String unique_1029) {
		this.unique_1029 = JdbcTools.removeTrailingSpaces(unique_1029);
	}

}
