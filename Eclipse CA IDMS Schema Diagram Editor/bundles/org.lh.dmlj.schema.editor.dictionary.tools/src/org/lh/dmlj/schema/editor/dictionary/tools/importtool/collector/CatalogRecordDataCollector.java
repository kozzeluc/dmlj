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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.ProcedureCallTime;
import org.lh.dmlj.schema.RecordProcedureCallVerb;
import org.lh.dmlj.schema.VsamLengthType;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IQuery;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IRowProcessor;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.Query;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.SchemaImportSession;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Constraint_1029;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Index_1041;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Indexkey_1042;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Table_1050;
import org.lh.dmlj.schema.editor.importtool.IRecordDataCollector;

public class CatalogRecordDataCollector implements IRecordDataCollector<Table_1050> {

	private Map<String, Index_1041> calcKeyElementsMap;
	private SchemaImportSession session;
	private Map<String, String> viaSetNames;
	
	public CatalogRecordDataCollector(SchemaImportSession session) {
		super();
		this.session = session;
	}
	
	private void buildCalcKeyElementsMapIfNeeded() {
		if (calcKeyElementsMap != null) {
			return;
		}
		calcKeyElementsMap = new HashMap<>();
		IQuery catalogCalcKeyElementListQuery = 
			new Query.Builder().forCatalogCalcKeyElementList(session).build();
		session.runQuery(catalogCalcKeyElementListQuery, new IRowProcessor() {			
			@Override
			public void processRow(ResultSet row) throws SQLException {
				String name_1050 = 
					JdbcTools.removeTrailingSpaces(row.getString(Table_1050.NAME_1050)); 				
				Index_1041 index_1041;
				if (!calcKeyElementsMap.containsKey(name_1050)) {
					index_1041 = new Index_1041();
					index_1041.setName_1041(row.getString(Index_1041.NAME_1041));
					index_1041.setUnique_1041(row.getString(Index_1041.UNIQUE_1041));
					// we don't need a reference from the Index_1041 to a Table_1050
					calcKeyElementsMap.put(name_1050, index_1041);
				} else {
					index_1041 = calcKeyElementsMap.get(name_1050);							
				}
				Indexkey_1042 indexkey_1042 = new Indexkey_1042();
				indexkey_1042.setColumn_1042(row.getString(Indexkey_1042.COLUMN_1042));
				// the following property is only set for completeness' sake
				indexkey_1042.setSortorder_1042(row.getString(Indexkey_1042.SORTORDER_1042));
				index_1041.getIndexkey_1042s().add(indexkey_1042);
			}
		});
	}
	
	private void buildViaSetNamesMapIfNeeded() {
		if (viaSetNames != null) {
			return;
		}
		viaSetNames = new HashMap<>();
		IQuery catalogViaSetListQuery = new Query.Builder().forCatalogViaSetList(session).build();
		session.runQuery(catalogViaSetListQuery, new IRowProcessor() {
			@Override
			public void processRow(ResultSet row) throws SQLException {
				String name_1050 = 
					JdbcTools.removeTrailingSpaces(row.getString(Table_1050.NAME_1050));
				String name_1029 = 
					JdbcTools.removeTrailingSpaces(row.getString(Constraint_1029.NAME_1029));
				viaSetNames.put(name_1050, name_1029);
			}
		});				
	}
	
	private Index_1041 findCalcKeyIndex(Table_1050 table_1050) {
		buildCalcKeyElementsMapIfNeeded();
		if (calcKeyElementsMap.containsKey(table_1050.getName_1050())) {					
			return calcKeyElementsMap.get(table_1050.getName_1050());
		} else {
			String message = "could not find HASH index for table: " + table_1050.getName_1050();
			throw new RuntimeException(message);
		}
	}

	@Override
	public String getAreaName(Table_1050 table_1050) {
		return table_1050.getArea_1050();
	}

	@Override
	public String getBaseName(Table_1050 table_1050) {
		return getName(table_1050);
	}

	@Override
	public short getBaseVersion(Table_1050 table_1050) {
		return 1;
	}

	@Override
	public DuplicatesOption getCalcKeyDuplicatesOption(Table_1050 table_1050) {
		// the definition of the CALC key is maintained through an index named "HASH", so we need 
		// that INDEX-1041 first...
		Index_1041 index_1041 = findCalcKeyIndex(table_1050);
		if (index_1041.getUnique_1041().equals("Y")) {		
			return DuplicatesOption.NOT_ALLOWED;
		} else {
			return DuplicatesOption.LAST;
		}
	}

	@Override
	public Collection<String> getCalcKeyElementNames(Table_1050 table_1050) {
		// the definition of the CALC key is maintained through an index named "HASH", so we need 
		// that INDEX-1041 first...
		Index_1041 index_1041 = findCalcKeyIndex(table_1050);	
		List<String> list = new ArrayList<>();		
		for (Indexkey_1042 indexkey_1042 : index_1041.getIndexkey_1042s()) {	
			String elementName = 
				indexkey_1042.getColumn_1042().replaceAll("_", "-") + "-" + table_1050.getTableid_1050();
			list.add(elementName);
		}		
		return list;
	}

	@Override
	public LocationMode getLocationMode(Table_1050 table_1050) {
		if (table_1050.getLocmode_1050().equals("C")) {
			return LocationMode.VIA;
		} else if (table_1050.getLocmode_1050().equals("D")) {
			return LocationMode.DIRECT;
		} else if (table_1050.getLocmode_1050().equals("H") || 
				   table_1050.getLocmode_1050().equals("U")) {
			
			return LocationMode.CALC;
		} else {
			return null;
		}
	}

	@Override
	public Short getMinimumFragmentLength(Table_1050 table_1050) {
		return null;
	}

	@Override
	public Short getMinimumRootLength(Table_1050 table_1050) {
		return null;
	}

	@Override
	public String getName(Table_1050 table_1050) {
		return table_1050.getName_1050().replace("_", "-") + "-" + table_1050.getTableid_1050();
	}

	@Override
	public Integer getOffsetOffsetPageCount(Table_1050 table_1050) {
		return null;
	}

	@Override
	public Short getOffsetOffsetPercent(Table_1050 table_1050) {
		return null;
	}

	@Override
	public Integer getOffsetPageCount(Table_1050 table_1050) {
		return null;
	}

	@Override
	public Short getOffsetPercent(Table_1050 table_1050) {
		return null;
	}

	@Override
	public Collection<ProcedureCallTime> getProcedureCallTimes(Table_1050 table_1050) {
		return Collections.emptyList();
	}

	@Override
	public Collection<RecordProcedureCallVerb> getProcedureCallVerbs(Table_1050 contex) {
		return Collections.emptyList();
	}

	@Override
	public Collection<String> getProceduresCalled(Table_1050 table_1050) {
		return Collections.emptyList();
	}

	@Override
	public short getRecordId(Table_1050 table_1050) {
		return table_1050.getTableid_1050();
	}

	@Override
	public String getSymbolicSubareaName(Table_1050 table_1050) {
		return null;
	}

	@Override
	public String getSynonymName(Table_1050 table_1050) {
		return getName(table_1050);
	}

	@Override
	public short getSynonymVersion(Table_1050 table_1050) {
		return 1;
	}

	@Override
	public Short getViaDisplacementPageCount(Table_1050 table_1050) {
		return null;
	}

	@Override
	public String getViaSetName(Table_1050 table_1050) {
		buildViaSetNamesMapIfNeeded();
		return viaSetNames.get(table_1050.getName_1050());
	}

	@Override
	public String getViaSymbolicDisplacementName(Table_1050 table_1050) {
		return null;
	}

	@Override
	public VsamLengthType getVsamLengthType(Table_1050 table_1050) {
		return null;
	}

	@Override
	public boolean isVsamSpanned(Table_1050 table_1050) {
		return false;
	}

}
