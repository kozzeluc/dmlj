/**
 * Copyright (C) 2015  Luc Hermans
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
import java.util.List;
import java.util.Map;

import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.SetMembershipOption;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IQuery;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IRowProcessor;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.Query;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.SchemaImportSession;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Column_1028;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Constkey_1030;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Constraint_1029;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Orderkey_1044;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Table_1050;
import org.lh.dmlj.schema.editor.importtool.ISetDataCollector;

public class CatalogUserOwnedSetDataCollector implements ISetDataCollector<Constraint_1029> {

	private SchemaImportSession session;
	private Map<String, Constraint_1029> sortKeyElementsMap;
	
	private static short getAdjustedDbkeyPosition(Table_1050 table_1050, short dbkeyPosition) {
		if (dbkeyPosition == -1) {
			return dbkeyPosition;
		} else if (table_1050.getLocmode_1050().equals("H") ||
				   table_1050.getLocmode_1050().equals("U")) {
			
			// record with location mode CALC
			return (short) (dbkeyPosition - 1);
		} else {
			return (short) (dbkeyPosition + 1);
		}
	}

	public CatalogUserOwnedSetDataCollector(SchemaImportSession session) {
		super();
		this.session = session;
	}

	private void buildSortKeyElementsMapIfNeeded() {
		if (sortKeyElementsMap != null) {
			return;
		}
		sortKeyElementsMap = new HashMap<>();
		IQuery catalogSortKeyElementListForUserOwnedSetsQuery = 
			new Query.Builder().forCatalogSortKeyElementListForUserOwnedSetsQuery(session).build();
		session.runQuery(catalogSortKeyElementListForUserOwnedSetsQuery, new IRowProcessor() {			
			@Override
			public void processRow(ResultSet row) throws SQLException {
				String name_1029 = 
					JdbcTools.removeTrailingSpaces(row.getString(Orderkey_1044.CONSTRAINT_1044)); 				
				Constraint_1029 constraint_1029;
				if (!sortKeyElementsMap.containsKey(name_1029)) {
					constraint_1029 = new Constraint_1029();
					constraint_1029.setName_1029(row.getString(Orderkey_1044.CONSTRAINT_1044));					
					// we don't need a reference from the Constraint_1029 to a (referencing) 
					// Table_1050 nor do we need any other column value
					sortKeyElementsMap.put(name_1029, constraint_1029);
				} else {
					constraint_1029 = sortKeyElementsMap.get(name_1029);							
				}
				Orderkey_1044 orderkey_1044 = new Orderkey_1044();
				orderkey_1044.setColumn_1044(row.getString(Orderkey_1044.COLUMN_1044));
				orderkey_1044.setConstraint_1044(row.getString(Orderkey_1044.CONSTRAINT_1044));
				orderkey_1044.setSortorder_1044(row.getString(Orderkey_1044.SORTORDER_1044));
				constraint_1029.getOrderkey_1044s().add(orderkey_1044);
			}
		});
		IQuery catalogForeignKeyListQuery = new Query.Builder().forCatalogForeignKeyList().build();
		session.runQuery(catalogForeignKeyListQuery, new IRowProcessor() {			
			@Override
			public void processRow(ResultSet row) throws SQLException {
				String name_1029 = 
					JdbcTools.removeTrailingSpaces(row.getString(Constkey_1030.NAME_1030)); 				
				Constraint_1029 constraint_1029;
				if (!sortKeyElementsMap.containsKey(name_1029)) {
					constraint_1029 = new Constraint_1029();
					constraint_1029.setName_1029(row.getString(Constkey_1030.NAME_1030));					
					// we don't need a reference from the Constraint_1029 to a (referencing) 
					// Table_1050 nor do we need any other column value
					sortKeyElementsMap.put(name_1029, constraint_1029);
				} else {
					constraint_1029 = sortKeyElementsMap.get(name_1029);							
				}
				Constkey_1030 constkey_1030 = new Constkey_1030();
				constkey_1030.setName_1030(row.getString(Constkey_1030.NAME_1030));
				constkey_1030.setNumber_1030(row.getShort(Constkey_1030.NUMBER_1030));
				constraint_1029.getConstkey_1030s().add(constkey_1030);
			}
		});
	}	
	
	@Override
	public Short getDisplacementPageCount(Constraint_1029 constraint_1029) {
		return Short.valueOf(constraint_1029.getDisplacement_1029());
	}

	@Override
	public DuplicatesOption getDuplicatesOption(Constraint_1029 constraint_1029,
												String memberRecordName) {
		
		if (constraint_1029.getUnique_1029().equals("Y")) {			
			return DuplicatesOption.NOT_ALLOWED;
		} else {
			return DuplicatesOption.LAST;
		}
	}

	@Override
	public Short getKeyCount(Constraint_1029 constraint_1029) {
		return Short.valueOf(constraint_1029.getIxblkcontains_1029());
	}

	@Override
	public Short getMemberIndexDbkeyPosition(Constraint_1029 constraint_1029) {
		// indexed sets only
		Table_1050 table_1050 = constraint_1029.getReferencingTable_1050();
		short i = (short) (constraint_1029.getNext_1029() / 4);
		return getAdjustedDbkeyPosition(table_1050, i); 
	}

	@Override
	public Short getMemberNextDbkeyPosition(Constraint_1029 constraint_1029, String memberRecordName) {
		// chained sets only
		Table_1050 table_1050 = constraint_1029.getReferencingTable_1050();
		short i = (short) (constraint_1029.getNext_1029() / 4);
		return getAdjustedDbkeyPosition(table_1050, i);
	}

	@Override
	public Short getMemberOwnerDbkeyPosition(Constraint_1029 constraint_1029, String memberRecordName) {
		if (constraint_1029.getOwner_1029() != -1) {
			Table_1050 table_1050 = constraint_1029.getReferencingTable_1050();
			short i = (short) (constraint_1029.getOwner_1029() / 4);
			return getAdjustedDbkeyPosition(table_1050, i); 		
		} else {
			return null;
		}
	}

	@Override
	public Short getMemberPriorDbkeyPosition(Constraint_1029 constraint_1029, String memberRecordName) {
		// chained sets only
		Table_1050 table_1050 = constraint_1029.getReferencingTable_1050();
		short i = (short) (constraint_1029.getPrior_1029() / 4);
		if (i > -1) {
			return getAdjustedDbkeyPosition(table_1050, i);
		} else {
			return null;
		}
	}

	@Override
	public Collection<String> getMemberRecordNames(Constraint_1029 constraint_1029) {
		List<String> list = new ArrayList<>();
		Table_1050 table_1050 = constraint_1029.getReferencingTable_1050();
		String recordName = 
			table_1050.getName_1050().replaceAll("_", "-") + "-" + table_1050.getTableid_1050();
		list.add(recordName);
		return list;
	}

	@Override
	public String getName(Constraint_1029 constraint_1029) {
		return constraint_1029.getName_1029();
	}

	@Override
	public short getOwnerNextDbkeyPosition(Constraint_1029 constraint_1029) {
		Table_1050 table_1050 = constraint_1029.getReferencedTable_1050();
		short i = (short) (constraint_1029.getRefnext_1029() / 4 );		
		return getAdjustedDbkeyPosition(table_1050, i);
	}

	@Override
	public Short getOwnerPriorDbkeyPosition(Constraint_1029 constraint_1029) {
		Table_1050 table_1050 = constraint_1029.getReferencedTable_1050();
		short i = (short) (constraint_1029.getRefprior_1029() / 4 );
		if (i > -1) {
			return getAdjustedDbkeyPosition(table_1050, i);
		} else {
			return null;
		}
	}

	@Override
	public String getOwnerRecordName(Constraint_1029 constraint_1029) {
		Table_1050 table_1050 = constraint_1029.getReferencedTable_1050();
		String recordName = 
			table_1050.getName_1050().replaceAll("_", "-") + "-" + table_1050.getTableid_1050();
		return recordName;
	}

	@Override
	public SetMembershipOption getSetMembershipOption(Constraint_1029 constraint_1029,
													  String memberRecordName) {
		
		buildSortKeyElementsMapIfNeeded();
		// Unless at least 1 of the constraint key columns is nullable in the referencing table, the 
		// membership option is considered to be MANDATORY AUTOMATIC.  In the other case, make it 
		// OPTIONAL AUTOMATIC.  The only 2 sets/constraints to which this really applies are
		// AREA-TABLE and AREA-INDEX.  Mind that we use the Constraint_1029 copy of the sort key 
		// elements map here...
		Table_1050 table_1050 = constraint_1029.getReferencingTable_1050();
		boolean nullableColumnEncountered = false;		
		// mind that we use the Constraint_1029 copy of the sort key elements map here...
		for (Constkey_1030 constkey_1030 : 
			 sortKeyElementsMap.get(constraint_1029.getName_1029()).getConstkey_1030s()) {	
			
			short columnNumber = constkey_1030.getNumber_1030();
			Column_1028 column_1028 = table_1050.getColumn_1028(columnNumber);			
			if (column_1028.getNulls_1028().equals("Y")) {
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
	public SetMode getSetMode(Constraint_1029 constraint_1029) {
		if (constraint_1029.getType_1029().equals("L")) {
			return SetMode.CHAINED;
		} else {
			return SetMode.INDEXED;
		}
	}

	@Override
	public SetOrder getSetOrder(Constraint_1029 constraint_1029) {
		if (constraint_1029.getSortorder_1029().equals("")) {
			return SetOrder.LAST;
		} else {
			return SetOrder.SORTED;
		}
	}

	@Override
	public Collection<String> getSortKeyElements(Constraint_1029 constraint_1029,
												 String memberRecordName) {
		
		buildSortKeyElementsMapIfNeeded();
		List<String> list = new ArrayList<>();
		Table_1050 table_1050 = constraint_1029.getReferencingTable_1050();
		// mind that we use the Constraint_1029 copy of the sort key elements map here...
		for (Orderkey_1044 orderkey_1044 : 
			 sortKeyElementsMap.get(constraint_1029.getName_1029()).getOrderkey_1044s()) {	
			
			String elementName = orderkey_1044.getColumn_1044().replaceAll("_", "-") + "-" +
								 table_1050.getTableid_1050();
			list.add(elementName);
		}
		return list;
	}

	@Override
	public boolean getSortKeyIsNaturalSequence(Constraint_1029 constraint_1029, 
											   String memberRecordName) {
		
		return !constraint_1029.getName_1029().equals("AREA-TABLE") &&
			   !constraint_1029.getName_1029().equals("AREA-INDEX");
	}

	@Override
	public SortSequence getSortSequence(Constraint_1029 constraint_1029, String memberRecordName, 
										String keyElementName) {
		
		Table_1050 table_1050 = constraint_1029.getReferencingTable_1050();
		// mind that we use the Constraint_1029 copy of the sort key elements map here...
		for (Orderkey_1044 orderkey_1044 : 
			 sortKeyElementsMap.get(constraint_1029.getName_1029()).getOrderkey_1044s()) {

			String elementName = orderkey_1044.getColumn_1044().replaceAll("_", "-") + "-" +
								 table_1050.getTableid_1050();				
			if (elementName.equals(keyElementName)) {				
				if (orderkey_1044.getSortorder_1044().equals("A")) {
					return SortSequence.ASCENDING;
				} else {
					return SortSequence.DESCENDING;
				}								
			}
		}	
		return null;
	}

	@Override
	public String getSymbolicIndexName(Constraint_1029 constraint_1029) {
		return null;
	}

	@Override
	public String getSystemOwnerAreaName(Constraint_1029 constraint_1029) {
		return null;
	}

	@Override
	public Integer getSystemOwnerOffsetOffsetPageCount(Constraint_1029 constraint_1029) {
		return null;
	}

	@Override
	public Short getSystemOwnerOffsetOffsetPercent(Constraint_1029 constraint_1029) {
		return null;
	}

	@Override
	public Integer getSystemOwnerOffsetPageCount(Constraint_1029 constraint_1029) {
		return null;
	}

	@Override
	public Short getSystemOwnerOffsetPercent(Constraint_1029 constraint_1029) {
		return null;
	}

	@Override
	public String getSystemOwnerSymbolicSubareaName(Constraint_1029 constraint_1029) {
		return null;
	}

	@Override
	public boolean isKeyCompressed(Constraint_1029 constraint_1029) {
		return constraint_1029.getCompress_1029().equals("Y");
	}

	@Override
	public boolean isSortedByDbkey(Constraint_1029 constraint_1029) {
		return false;
	}

	@Override
	public boolean isSystemOwned(Constraint_1029 constraint_1029) {
		return false;
	}

}
