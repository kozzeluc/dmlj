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

public class Constraint1029 {
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
	
	private List<Constkey1030> constkey1030s = new ArrayList<>();
	private List<Orderkey1044> orderkey1044s = new ArrayList<>();
	private Table1050 referencedTable1050;
	private Table1050 referencingTable1050;

	private String compress1029;
	private short displacement1029;
	private short ixblkcontains1029;
	private String name1029;
	private short next1029;
	private short owner1029;
	private short prior1029;
	private short refnext1029;
	private short refprior1029;
	private String sortorder1029;
	private String type1029;
	private String unique1029;

	public String getCompress1029() {
		return compress1029;
	}

	public List<Constkey1030> getConstkey1030s() {
		return constkey1030s;
	}

	public short getDisplacement1029() {
		return displacement1029;
	}

	public short getIxblkcontains1029() {
		return ixblkcontains1029;
	}

	public String getName1029() {
		return name1029;
	}

	public short getNext1029() {
		return next1029;
	}

	public List<Orderkey1044> getOrderkey1044s() {
		return orderkey1044s;
	}

	public short getOwner1029() {
		return owner1029;
	}

	public short getPrior1029() {
		return prior1029;
	}

	public Table1050 getReferencedTable1050() {
		return referencedTable1050;
	}

	public Table1050 getReferencingTable1050() {
		return referencingTable1050;
	}

	public short getRefnext1029() {
		return refnext1029;
	}

	public short getRefprior1029() {
		return refprior1029;
	}

	public String getSortorder1029() {
		return sortorder1029;
	}

	public String getType1029() {
		return type1029;
	}

	public String getUnique1029() {
		return unique1029;
	}

	public void setCompress1029(String compress1029) {
		this.compress1029 = JdbcTools.removeTrailingSpaces(compress1029);
	}

	public void setDisplacement1029(short displacement1029) {
		this.displacement1029 = displacement1029;
	}

	public void setIxblkcontains1029(short ixblkcontains1029) {
		this.ixblkcontains1029 = ixblkcontains1029;
	}

	public void setName1029(String name1029) {
		this.name1029 = JdbcTools.removeTrailingSpaces(name1029);
	}

	public void setNext1029(short next1029) {
		this.next1029 = next1029;
	}

	public void setOwner1029(short owner1029) {
		this.owner1029 = owner1029;
	}

	public void setPrior1029(short prior1029) {
		this.prior1029 = prior1029;
	}

	public void setReferencedTable1050(Table1050 referencedTable1050) {
		this.referencedTable1050 = referencedTable1050;
	}

	public void setReferencingTable1050(Table1050 referencingTable1050) {
		this.referencingTable1050 = referencingTable1050;
	}

	public void setRefnext1029(short refnext1029) {
		this.refnext1029 = refnext1029;
	}

	public void setRefprior1029(short refprior1029) {
		this.refprior1029 = refprior1029;
	}

	public void setSortorder1029(String sortorder1029) {
		this.sortorder1029 = JdbcTools.removeTrailingSpaces(sortorder1029);
	}

	public void setType1029(String type1029) {
		this.type1029 = JdbcTools.removeTrailingSpaces(type1029);
	}

	public void setUnique1029(String unique1029) {
		this.unique1029 = JdbcTools.removeTrailingSpaces(unique1029);
	}

}
