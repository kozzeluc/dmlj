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
import java.util.Arrays;
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
import org.lh.dmlj.schema.editor.dictionary.tools.table.Index1041;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Indexkey1042;
import org.lh.dmlj.schema.editor.importtool.ISetDataCollector;

public class CatalogSystemOwnedIndexDataCollector implements ISetDataCollector<Index1041> {
	private final SchemaImportSession session;
	private Map<String, Index1041> sortKeyElementsMap;
	
	public CatalogSystemOwnedIndexDataCollector(SchemaImportSession session) {
		this.session = session;
	}
	
	private void buildSortKeyElementsMapIfNeeded() {
		if (sortKeyElementsMap != null) {
			return;
		}
		sortKeyElementsMap = new HashMap<>();
		var catalogSortKeyElementListForSystemOwnedSetsQuery = new Query.Builder().forCatalogSortKeyElementListForSystemOwnedSets().build();
		session.runQuery(catalogSortKeyElementListForSystemOwnedSetsQuery, new IRowProcessor() {			
			@Override
			public void processRow(ResultSet row) throws SQLException {
				var name1041 = JdbcTools.removeTrailingSpaces(row.getString(Index1041.NAME_1041)); 				
				Index1041 index1041;
				if (!sortKeyElementsMap.containsKey(name1041)) {
					index1041 = new Index1041();
					index1041.setName1041(name1041);
					index1041.setUnique1041(row.getString(Index1041.UNIQUE_1041));
					// we don't need a reference from the Index_1041 to a Table_1050
					sortKeyElementsMap.put(name1041, index1041);
				} else {
					index1041 = sortKeyElementsMap.get(name1041);							
				}
				var indexkey1042 = new Indexkey1042();
				indexkey1042.setColumn1042(row.getString(Indexkey1042.COLUMN_1042));
				indexkey1042.setSortorder1042(row.getString(Indexkey1042.SORTORDER_1042));
				index1041.getIndexkey1042s().add(indexkey1042);
			}
		});
	}

	@Override
	public Short getDisplacementPageCount(Index1041 index1041) {
		return Short.valueOf(index1041.getDisplacement1041());
	}

	@Override
	public DuplicatesOption getDuplicatesOption(Index1041 index1041, String memberRecordName) {
		return switch (index1041.getUnique1041()) {
			case "Y" -> DuplicatesOption.NOT_ALLOWED;
			default -> DuplicatesOption.LAST;
		};
	}

	@Override
	public Short getKeyCount(Index1041 index1041) {
		return Short.valueOf(index1041.getIxblkcontains1041());
	}

	@Override
	public Short getMemberIndexDbkeyPosition(Index1041 index1041) {
		return null;
	}

	@Override
	public Short getMemberNextDbkeyPosition(Index1041 index1041, String memberRecordName) {
		return null;
	}

	@Override
	public Short getMemberOwnerDbkeyPosition(Index1041 index1041, String memberRecordName) {
		return null;
	}

	@Override
	public Short getMemberPriorDbkeyPosition(Index1041 index1041, String memberRecordName) {
		return null;
	}

	@Override
	public Collection<String> getMemberRecordNames(Index1041 index1041) {
		var table1050 = index1041.getTable1050();
		return Arrays.asList(table1050.getName1050().replace("_", "-") + "-" + table1050.getTableid1050());
	}

	@Override
	public String getName(Index1041 index1041) {
		return index1041.getName1041();
	}

	@Override
	public short getOwnerNextDbkeyPosition(Index1041 index1041) {
		return -1;
	}

	@Override
	public Short getOwnerPriorDbkeyPosition(Index1041 index1041) {
		return null;
	}

	@Override
	public String getOwnerRecordName(Index1041 index1041) {
		return null;
	}

	@Override
	public SetMembershipOption getSetMembershipOption(Index1041 index1041, String memberRecordName) {
		return SetMembershipOption.MANDATORY_AUTOMATIC;
	}

	@Override
	public SetMode getSetMode(Index1041 index1041) {
		return SetMode.INDEXED;
	}

	@Override
	public SetOrder getSetOrder(Index1041 index1041) {
		return SetOrder.SORTED; // where can we derive this from ?
	}

	@Override
	public Collection<String> getSortKeyElements(Index1041 index1041, String memberRecordName) {
		buildSortKeyElementsMapIfNeeded();
		var table1050 = index1041.getTable1050();		
		var list = new ArrayList<String>();
		// mind that we need to use the Index_1041 copy of the sort key elements map...
		for (var indexkey1042 :  sortKeyElementsMap.get(index1041.getName1041()).getIndexkey1042s()) {				
			var elementName = indexkey1042.getColumn1042().replace("_", "-") + "-" + table1050.getTableid1050();
			list.add(elementName);
		}
		return list;
	}

	@Override
	public boolean getSortKeyIsNaturalSequence(Index1041 index1041, String memberRecordName) {
		return true;
	}

	@Override
	public SortSequence getSortSequence(Index1041 index1041, String memberRecordName, String keyElementName) {
		buildSortKeyElementsMapIfNeeded();
		var table1050 = index1041.getTable1050();
		// mind that we need to use the Index_1041 copy of the sort key elements map...
		for (var indexkey1042 : sortKeyElementsMap.get(index1041.getName1041()).getIndexkey1042s()) {	
			var elementName = indexkey1042.getColumn1042().replace("_", "-") + "-" + table1050.getTableid1050();
			if (elementName.equals(keyElementName)) {
				if (indexkey1042.getSortorder1042().equals("A")) {
					return SortSequence.ASCENDING;
				} else {
					return SortSequence.DESCENDING;
				}
			}
		}		
		return null;
	}

	@Override
	public String getSymbolicIndexName(Index1041 index1041) {
		return null;
	}

	@Override
	public String getSystemOwnerAreaName(Index1041 index1041) {
		return index1041.getArea1041();
	}

	@Override
	public Integer getSystemOwnerOffsetOffsetPageCount(Index1041 index1041) {
		return null;
	}

	@Override
	public Short getSystemOwnerOffsetOffsetPercent(Index1041 index1041) {
		return null;
	}

	@Override
	public Integer getSystemOwnerOffsetPageCount(Index1041 index1041) {
		return null;
	}

	@Override
	public Short getSystemOwnerOffsetPercent(Index1041 index1041) {
		return null;
	}

	@Override
	public String getSystemOwnerSymbolicSubareaName(Index1041 index1041) {
		return null;
	}

	@Override
	public boolean isKeyCompressed(Index1041 index1041) {
		return "Y".equals(index1041.getCompress1041());
	}

	@Override
	public boolean isSortedByDbkey(Index1041 index1041) {
		return false;
	}

	@Override
	public boolean isSystemOwned(Index1041 index1041) {
		return true;
	}

}
