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
package org.lh.dmlj.schema.editor.dictionary.tools._import.tool.collector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.SetMembershipOption;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IRowProcessor;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.Query;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.SchemaImportSession;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Index_1041;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Indexkey_1042;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Table_1050;
import org.lh.dmlj.schema.editor.importtool.ISetDataCollector;

public class CatalogSystemOwnedIndexDataCollector implements ISetDataCollector<Index_1041> {

	private SchemaImportSession session;
	private Map<String, Index_1041> sortKeyElementsMap;
	
	public CatalogSystemOwnedIndexDataCollector(SchemaImportSession session) {
		super();
		this.session = session;
	}
	
	private void buildSortKeyElementsMapIfNeeded() {
		if (sortKeyElementsMap != null) {
			return;
		}
		sortKeyElementsMap = new HashMap<>();
		Query catalogSortKeyElementListForSystemOwnedSetsQuery = 
			new Query.Builder().forCatalogSortKeyElementListForSystemOwnedSets(session).build();
		session.runQuery(catalogSortKeyElementListForSystemOwnedSetsQuery, new IRowProcessor() {			
			@Override
			public void processRow(ResultSet row) throws SQLException {
				String name_1050 = 
					JdbcTools.removeTrailingSpaces(row.getString(Table_1050.NAME_1050)); 				
				Index_1041 index_1041;
				if (!sortKeyElementsMap.containsKey(name_1050)) {
					index_1041 = new Index_1041();
					index_1041.setName_1041(row.getString(Index_1041.NAME_1041));
					index_1041.setUnique_1041(row.getString(Index_1041.UNIQUE_1041));
					// we don't need a reference from the Index_1041 to a Table_1050
					sortKeyElementsMap.put(name_1050, index_1041);
				} else {
					index_1041 = sortKeyElementsMap.get(name_1050);							
				}
				Indexkey_1042 indexkey_1042 = new Indexkey_1042();
				indexkey_1042.setColumn_1042(row.getString(Indexkey_1042.COLUMN_1042));
				indexkey_1042.setSortorder_1042(row.getString(Indexkey_1042.SORTORDER_1042));
				index_1041.getIndexkey_1042s().add(indexkey_1042);
			}
		});
	}

	@Override
	public Short getDisplacementPageCount(Index_1041 index_1041) {
		return Short.valueOf(index_1041.getDisplacement_1041());
	}

	@Override
	public DuplicatesOption getDuplicatesOption(Index_1041 index_1041, String memberRecordName) {
		if (index_1041.getUnique_1041().equals("Y")) {		
			return DuplicatesOption.NOT_ALLOWED;
		} else {
			return DuplicatesOption.LAST;
		}
	}

	@Override
	public Short getKeyCount(Index_1041 index_1041) {
		return Short.valueOf(index_1041.getIxblkcontains_1041());
	}

	@Override
	public Short getMemberIndexDbkeyPosition(Index_1041 index_1041) {
		return null;
	}

	@Override
	public Short getMemberNextDbkeyPosition(Index_1041 index_1041, String memberRecordName) {
		return null;
	}

	@Override
	public Short getMemberOwnerDbkeyPosition(Index_1041 index_1041, String memberRecordName) {
		return null;
	}

	@Override
	public Short getMemberPriorDbkeyPosition(Index_1041 index_1041, String memberRecordName) {
		return null;
	}

	@Override
	public Collection<String> getMemberRecordNames(Index_1041 index_1041) {
		Table_1050 table_1050 = index_1041.getTable_1050();
		return Arrays.asList(new String[] {table_1050.getName_1050()
													 .replaceAll("_", "-") + "-" + 
										   table_1050.getTableid_1050()});
	}

	@Override
	public String getName(Index_1041 index_1041) {
		return index_1041.getName_1041();
	}

	@Override
	public short getOwnerNextDbkeyPosition(Index_1041 index_1041) {
		return -1;
	}

	@Override
	public Short getOwnerPriorDbkeyPosition(Index_1041 index_1041) {
		return null;
	}

	@Override
	public String getOwnerRecordName(Index_1041 index_1041) {
		return null;
	}

	@Override
	public SetMembershipOption getSetMembershipOption(Index_1041 index_1041, String memberRecordName) {
		return SetMembershipOption.MANDATORY_AUTOMATIC;
	}

	@Override
	public SetMode getSetMode(Index_1041 index_1041) {
		return SetMode.INDEXED;
	}

	@Override
	public SetOrder getSetOrder(Index_1041 index_1041) {
		return SetOrder.SORTED; // where can we derive this from ?
	}

	@Override
	public Collection<String> getSortKeyElements(Index_1041 index_1041, String memberRecordName) {
		buildSortKeyElementsMapIfNeeded();
		Table_1050 table_1050 = index_1041.getTable_1050();		
		List<String> list = new ArrayList<>();
		// mind that we need to use the Index_1041 copy of the sort key elements map...
		for (Indexkey_1042 indexkey_1042 :  sortKeyElementsMap.get(index_1041.getName_1041()).getIndexkey_1042s()) {				
			String elementName = indexkey_1042.getColumn_1042().replaceAll("_", "-") + "-" +
								 table_1050.getTableid_1050();
			list.add(elementName);
		}
		return list;
	}

	@Override
	public boolean getSortKeyIsNaturalSequence(Index_1041 index_1041, String memberRecordName) {
		return true;
	}

	@Override
	public SortSequence getSortSequence(Index_1041 index_1041, String memberRecordName, 
										String keyElementName) {
		
		buildSortKeyElementsMapIfNeeded();
		Table_1050 table_1050 = index_1041.getTable_1050();
		// mind that we need to use the Index_1041 copy of the sort key elements map...
		for (Indexkey_1042 indexkey_1042 : sortKeyElementsMap.get(index_1041.getName_1041()).getIndexkey_1042s()) {	
			String elementName = 
				indexkey_1042.getColumn_1042().replaceAll("_", "-") + "-" +
				table_1050.getTableid_1050();
			if (elementName.equals(keyElementName)) {
				if (indexkey_1042.getSortorder_1042().equals("A")) {
					return SortSequence.ASCENDING;
				} else {
					return SortSequence.DESCENDING;
				}
			}
		}		
		return null;
	}

	@Override
	public String getSymbolicIndexName(Index_1041 index_1041) {
		return null;
	}

	@Override
	public String getSystemOwnerAreaName(Index_1041 index_1041) {
		return index_1041.getArea_1041();
	}

	@Override
	public Integer getSystemOwnerOffsetOffsetPageCount(Index_1041 index_1041) {
		return null;
	}

	@Override
	public Short getSystemOwnerOffsetOffsetPercent(Index_1041 index_1041) {
		return null;
	}

	@Override
	public Integer getSystemOwnerOffsetPageCount(Index_1041 index_1041) {
		return null;
	}

	@Override
	public Short getSystemOwnerOffsetPercent(Index_1041 index_1041) {
		return null;
	}

	@Override
	public String getSystemOwnerSymbolicSubareaName(Index_1041 index_1041) {
		return null;
	}

	@Override
	public boolean isKeyCompressed(Index_1041 index_1041) {
		return index_1041.getCompress_1041().equals("Y");
	}

	@Override
	public boolean isSortedByDbkey(Index_1041 index_1041) {
		return false;
	}

	@Override
	public boolean isSystemOwned(Index_1041 index_1041) {
		return true;
	}

}
