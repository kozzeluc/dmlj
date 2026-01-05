/**
 * Copyright (C) 2025  Luc Hermans
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
package org.lh.dmlj.schema.editor.dictionary.tools.importtool.collector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.SetMembershipOption;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IRowProcessor;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.Query;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.SchemaImportSession;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Constkey1030;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Constraint1029;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Orderkey1044;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Table1050;
import org.lh.dmlj.schema.editor.importtool.ISetDataCollector;

public class CatalogUserOwnedSetDataCollector implements ISetDataCollector<Constraint1029> {
	private final SchemaImportSession session;
	private Map<String, Constraint1029> sortKeyElementsMap;
	
	private static short getAdjustedDbkeyPosition(Table1050 table1050, short dbkeyPosition) {
		if (dbkeyPosition == -1) {
			return dbkeyPosition;
		} else if (table1050.getLocmode1050().equals("H") || table1050.getLocmode1050().equals("U")) {
			// record with location mode CALC
			return (short) (dbkeyPosition - 1);
		} else {
			return (short) (dbkeyPosition + 1);
		}
	}

	public CatalogUserOwnedSetDataCollector(SchemaImportSession session) {
		this.session = session;
	}

	private void buildSortKeyElementsMapIfNeeded() {
		if (sortKeyElementsMap != null) {
			return;
		}
		sortKeyElementsMap = new HashMap<>();
		var catalogSortKeyElementListForUserOwnedSetsQuery = new Query.Builder().forCatalogSortKeyElementListForUserOwnedSetsQuery().build();
		session.runQuery(catalogSortKeyElementListForUserOwnedSetsQuery, new IRowProcessor() {			
			@Override
			public void processRow(ResultSet row) throws SQLException {
				var name1029 = JdbcTools.removeTrailingSpaces(row.getString(Orderkey1044.CONSTRAINT_1044));
				Constraint1029 constraint1029;
				if (!sortKeyElementsMap.containsKey(name1029)) {
					constraint1029 = new Constraint1029();
					constraint1029.setName1029(row.getString(Orderkey1044.CONSTRAINT_1044));					
					// we don't need a reference from the Constraint_1029 to a (referencing) Table_1050 nor do we
					// need any other column value
					sortKeyElementsMap.put(name1029, constraint1029);
				} else {
					constraint1029 = sortKeyElementsMap.get(name1029);							
				}
				var orderkey1044 = new Orderkey1044();
				orderkey1044.setColumn1044(row.getString(Orderkey1044.COLUMN_1044));
				orderkey1044.setConstraint1044(row.getString(Orderkey1044.CONSTRAINT_1044));
				orderkey1044.setSortorder1044(row.getString(Orderkey1044.SORTORDER_1044));
				constraint1029.getOrderkey1044s().add(orderkey1044);
			}
		});
		var catalogForeignKeyListQuery = new Query.Builder().forCatalogForeignKeyList().build();
		session.runQuery(catalogForeignKeyListQuery, new IRowProcessor() {			
			@Override
			public void processRow(ResultSet row) throws SQLException {
				var name1029 = JdbcTools.removeTrailingSpaces(row.getString(Constkey1030.NAME_1030));
				Constraint1029 constraint1029;
				if (!sortKeyElementsMap.containsKey(name1029)) {
					constraint1029 = new Constraint1029();
					constraint1029.setName1029(row.getString(Constkey1030.NAME_1030));					
					// we don't need a reference from the Constraint_1029 to a (referencing) Table_1050 nor do we
					// need any other column value
					sortKeyElementsMap.put(name1029, constraint1029);
				} else {
					constraint1029 = sortKeyElementsMap.get(name1029);							
				}
				var constkey1030 = new Constkey1030();
				constkey1030.setName1030(row.getString(Constkey1030.NAME_1030));
				constkey1030.setNumber1030(row.getShort(Constkey1030.NUMBER_1030));
				constraint1029.getConstkey1030s().add(constkey1030);
			}
		});
	}	
	
	@Override
	public Short getDisplacementPageCount(Constraint1029 constraint1029) {
		return constraint1029.getDisplacement1029();
	}

	@Override
	public DuplicatesOption getDuplicatesOption(Constraint1029 constraint1029, String memberRecordName) {
		if (constraint1029.getUnique1029().equals("Y")) {			
			return DuplicatesOption.NOT_ALLOWED;
		} else {
			return DuplicatesOption.LAST;
		}
	}

	@Override
	public Short getKeyCount(Constraint1029 constraint1029) {
		return constraint1029.getIxblkcontains1029();
	}

	@Override
	public Short getMemberIndexDbkeyPosition(Constraint1029 constraint1029) {
		// indexed sets only
		var table1050 = constraint1029.getReferencingTable1050();
		short i = (short) (constraint1029.getNext1029() / 4);
		return getAdjustedDbkeyPosition(table1050, i); 
	}

	@Override
	public Short getMemberNextDbkeyPosition(Constraint1029 constraint1029, String memberRecordName) {
		// chained sets only
		var table1050 = constraint1029.getReferencingTable1050();
		short i = (short) (constraint1029.getNext1029() / 4);
		return getAdjustedDbkeyPosition(table1050, i);
	}

	@Override
	public Short getMemberOwnerDbkeyPosition(Constraint1029 constraint1029, String memberRecordName) {
		if (constraint1029.getOwner1029() != -1) {
			var table1050 = constraint1029.getReferencingTable1050();
			short i = (short) (constraint1029.getOwner1029() / 4);
			return getAdjustedDbkeyPosition(table1050, i); 		
		} else {
			return null;
		}
	}

	@Override
	public Short getMemberPriorDbkeyPosition(Constraint1029 constraint1029, String memberRecordName) {
		// chained sets only
		var table1050 = constraint1029.getReferencingTable1050();
		short i = (short) (constraint1029.getPrior1029() / 4);
		if (i > -1) {
			return getAdjustedDbkeyPosition(table1050, i);
		} else {
			return null;
		}
	}

	@Override
	public Collection<String> getMemberRecordNames(Constraint1029 constraint1029) {
		var list = new ArrayList<String>();
		var table1050 = constraint1029.getReferencingTable1050();
		var recordName = table1050.getName1050().replace("_", "-") + "-" + table1050.getTableid1050();
		list.add(recordName);
		return list;
	}

	@Override
	public String getName(Constraint1029 constraint1029) {
		return constraint1029.getName1029();
	}

	@Override
	public short getOwnerNextDbkeyPosition(Constraint1029 constraint1029) {
		var table1050 = constraint1029.getReferencedTable1050();
		short i = (short) (constraint1029.getRefnext1029() / 4);		
		return getAdjustedDbkeyPosition(table1050, i);
	}

	@Override
	public Short getOwnerPriorDbkeyPosition(Constraint1029 constraint1029) {
		var table1050 = constraint1029.getReferencedTable1050();
		short i = (short) (constraint1029.getRefprior1029() / 4);
		if (i > -1) {
			return getAdjustedDbkeyPosition(table1050, i);
		} else {
			return null;
		}
	}

	@Override
	public String getOwnerRecordName(Constraint1029 constraint1029) {
		var table1050 = constraint1029.getReferencedTable1050();
		return table1050.getName1050().replace("_", "-") + "-" + table1050.getTableid1050();
	}

	@Override
	public SetMembershipOption getSetMembershipOption(Constraint1029 constraint1029, String memberRecordName) {
		buildSortKeyElementsMapIfNeeded();
		// Unless at least 1 of the constraint key columns is nullable in the referencing table, the membership
		// option is considered to be MANDATORY AUTOMATIC.  In the other case, make it OPTIONAL AUTOMATIC. The
		// only 2 sets/constraints to which this really applies are AREA-TABLE and AREA-INDEX. Mind that we use
		// the Constraint_1029 copy of the sort key elements map here...
		var table1050 = constraint1029.getReferencingTable1050();
		var nullableColumnEncountered = false;		
		// mind that we use the Constraint_1029 copy of the sort key elements map here...
		for (var constkey1030 : sortKeyElementsMap.get(constraint1029.getName1029()).getConstkey1030s()) {
			var columnNumber = constkey1030.getNumber1030();
			var column1028 = table1050.getColumn1028(columnNumber);			
			if (column1028.getNulls1028().equals("Y")) {
				nullableColumnEncountered = true;
			}
		}
		if (nullableColumnEncountered) {
			return SetMembershipOption.OPTIONAL_AUTOMATIC;
		} else {
			return SetMembershipOption.MANDATORY_AUTOMATIC;
		}
	}

	@Override
	public SetMode getSetMode(Constraint1029 constraint1029) {
		if (constraint1029.getType1029().equals("L")) {
			return SetMode.CHAINED;
		} else {
			return SetMode.INDEXED;
		}
	}

	@Override
	public SetOrder getSetOrder(Constraint1029 constraint1029) {
		if (constraint1029.getSortorder1029().equals("")) {
			return SetOrder.LAST;
		} else {
			return SetOrder.SORTED;
		}
	}

	@Override
	public Collection<String> getSortKeyElements(Constraint1029 constraint1029, String memberRecordName) {
		buildSortKeyElementsMapIfNeeded();
		var list = new ArrayList<String>();
		var table1050 = constraint1029.getReferencingTable1050();
		// mind that we use the Constraint_1029 copy of the sort key elements map here...
		for (var orderkey1044 : sortKeyElementsMap.get(constraint1029.getName1029()).getOrderkey1044s()) {
			var elementName = orderkey1044.getColumn1044().replace("_", "-") + "-" + table1050.getTableid1050();
			list.add(elementName);
		}
		return list;
	}

	@Override
	public boolean getSortKeyIsNaturalSequence(Constraint1029 constraint1029, String memberRecordName) {
		return !constraint1029.getName1029().equals("AREA-TABLE") && !constraint1029.getName1029().equals("AREA-INDEX");
	}

	@Override
	public SortSequence getSortSequence(Constraint1029 constraint1029, String memberRecordName, String keyElementName) {
		var table1050 = constraint1029.getReferencingTable1050();
		// mind that we use the Constraint_1029 copy of the sort key elements map here...
		for (var orderkey1044 : sortKeyElementsMap.get(constraint1029.getName1029()).getOrderkey1044s()) {
			var elementName = orderkey1044.getColumn1044().replace("_", "-") + "-" + table1050.getTableid1050();
			if (elementName.equals(keyElementName)) {				
				if (orderkey1044.getSortorder1044().equals("A")) {
					return SortSequence.ASCENDING;
				} else {
					return SortSequence.DESCENDING;
				}								
			}
		}	
		return null;
	}

	@Override
	public String getSymbolicIndexName(Constraint1029 constraint1029) {
		return null;
	}

	@Override
	public String getSystemOwnerAreaName(Constraint1029 constraint1029) {
		return null;
	}

	@Override
	public Integer getSystemOwnerOffsetOffsetPageCount(Constraint1029 constraint1029) {
		return null;
	}

	@Override
	public Short getSystemOwnerOffsetOffsetPercent(Constraint1029 constraint1029) {
		return null;
	}

	@Override
	public Integer getSystemOwnerOffsetPageCount(Constraint1029 constraint1029) {
		return null;
	}

	@Override
	public Short getSystemOwnerOffsetPercent(Constraint1029 constraint1029) {
		return null;
	}

	@Override
	public String getSystemOwnerSymbolicSubareaName(Constraint1029 constraint1029) {
		return null;
	}

	@Override
	public boolean isKeyCompressed(Constraint1029 constraint1029) {
		return constraint1029.getCompress1029().equals("Y");
	}

	@Override
	public boolean isSortedByDbkey(Constraint1029 constraint1029) {
		return false;
	}

	@Override
	public boolean isSystemOwned(Constraint1029 constraint1029) {
		return false;
	}

}
