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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.ProcedureCallTime;
import org.lh.dmlj.schema.RecordProcedureCallVerb;
import org.lh.dmlj.schema.VsamLengthType;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IRowProcessor;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.Query;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.SchemaImportSession;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Constraint1029;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Index1041;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Indexkey1042;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Table1050;
import org.lh.dmlj.schema.editor.importtool.IRecordDataCollector;

public class CatalogRecordDataCollector implements IRecordDataCollector<Table1050> {
	private final SchemaImportSession session;
	private Map<String, Index1041> calcKeyElementsMap;
	private Map<String, String> viaSetNames;
	
	public CatalogRecordDataCollector(SchemaImportSession session) {
		this.session = session;
	}
	
	private void buildCalcKeyElementsMapIfNeeded() {
		if (calcKeyElementsMap != null) {
			return;
		}
		calcKeyElementsMap = new HashMap<>();
		var catalogCalcKeyElementListQuery = new Query.Builder().forCatalogCalcKeyElementList().build();
		session.runQuery(catalogCalcKeyElementListQuery, new IRowProcessor() {			
			@Override
			public void processRow(ResultSet row) throws SQLException {
				var name1050 = JdbcTools.removeTrailingSpaces(row.getString(Table1050.NAME_1050)); 				
				Index1041 index1041;
				if (!calcKeyElementsMap.containsKey(name1050)) {
					index1041 = new Index1041();
					index1041.setName1041(row.getString(Index1041.NAME_1041));
					index1041.setUnique1041(row.getString(Index1041.UNIQUE_1041));
					// we don't need a reference from the Index_1041 to a Table_1050
					calcKeyElementsMap.put(name1050, index1041);
				} else {
					index1041 = calcKeyElementsMap.get(name1050);							
				}
				var indexkey1042 = new Indexkey1042();
				indexkey1042.setColumn1042(row.getString(Indexkey1042.COLUMN_1042));
				// the following property is only set for completeness' sake
				indexkey1042.setSortorder1042(row.getString(Indexkey1042.SORTORDER_1042));
				index1041.getIndexkey1042s().add(indexkey1042);
			}
		});
	}
	
	private void buildViaSetNamesMapIfNeeded() {
		if (viaSetNames != null) {
			return;
		}
		viaSetNames = new HashMap<>();
		var catalogViaSetListQuery = new Query.Builder().forCatalogViaSetList().build();
		session.runQuery(catalogViaSetListQuery, new IRowProcessor() {
			@Override
			public void processRow(ResultSet row) throws SQLException {
				var name1050 = JdbcTools.removeTrailingSpaces(row.getString(Table1050.NAME_1050));
				var name1029 = JdbcTools.removeTrailingSpaces(row.getString(Constraint1029.NAME_1029));
				viaSetNames.put(name1050, name1029);
			}
		});
	}
	
	private Index1041 findCalcKeyIndex(Table1050 table1050) {
		buildCalcKeyElementsMapIfNeeded();
		if (calcKeyElementsMap.containsKey(table1050.getName1050())) {					
			return calcKeyElementsMap.get(table1050.getName1050());
		} else {
			String message = "could not find HASH index for table: " + table1050.getName1050();
			throw new IllegalStateException(message);
		}
	}

	@Override
	public String getAreaName(Table1050 table1050) {
		return table1050.getArea1050();
	}

	@Override
	public String getBaseName(Table1050 table1050) {
		return getName(table1050);
	}

	@Override
	public short getBaseVersion(Table1050 table1050) {
		return 1;
	}

	@Override
	public DuplicatesOption getCalcKeyDuplicatesOption(Table1050 table1050) {
		// the definition of the CALC key is maintained through an index named "HASH", so we need that INDEX-1041 first...
		return switch (findCalcKeyIndex(table1050).getUnique1041()) {
			case "Y" -> DuplicatesOption.NOT_ALLOWED;
			default -> DuplicatesOption.LAST;
		};
	}

	@Override
	public Collection<String> getCalcKeyElementNames(Table1050 table1050) {
		// the definition of the CALC key is maintained through an index named "HASH", so we need that INDEX-1041 first...	
		return findCalcKeyIndex(table1050).getIndexkey1042s().stream()
				.map(indexkey1042 -> indexkey1042.getColumn1042().replace("_", "-") + "-" + table1050.getTableid1050())
				.toList();
	}

	@Override
	public LocationMode getLocationMode(Table1050 table1050) {
		return switch (table1050.getLocmode1050()) {
			case "C" -> LocationMode.VIA;
			case "D" -> LocationMode.DIRECT;
			case "H", "U" -> LocationMode.CALC;
			default -> null;
		};
	}

	@Override
	public Short getMinimumFragmentLength(Table1050 table1050) {
		return null;
	}

	@Override
	public Short getMinimumRootLength(Table1050 table1050) {
		return null;
	}

	@Override
	public String getName(Table1050 table1050) {
		return table1050.getName1050().replace("_", "-") + "-" + table1050.getTableid1050();
	}

	@Override
	public Integer getOffsetOffsetPageCount(Table1050 table1050) {
		return null;
	}

	@Override
	public Short getOffsetOffsetPercent(Table1050 table1050) {
		return null;
	}

	@Override
	public Integer getOffsetPageCount(Table1050 table1050) {
		return null;
	}

	@Override
	public Short getOffsetPercent(Table1050 table1050) {
		return null;
	}

	@Override
	public Collection<ProcedureCallTime> getProcedureCallTimes(Table1050 table1050) {
		return Collections.emptyList();
	}

	@Override
	public Collection<RecordProcedureCallVerb> getProcedureCallVerbs(Table1050 contex) {
		return Collections.emptyList();
	}

	@Override
	public Collection<String> getProceduresCalled(Table1050 table1050) {
		return Collections.emptyList();
	}

	@Override
	public short getRecordId(Table1050 table1050) {
		return table1050.getTableid1050();
	}

	@Override
	public String getSymbolicSubareaName(Table1050 table1050) {
		return null;
	}

	@Override
	public String getSynonymName(Table1050 table1050) {
		return getName(table1050);
	}

	@Override
	public short getSynonymVersion(Table1050 table1050) {
		return 1;
	}

	@Override
	public Short getViaDisplacementPageCount(Table1050 table1050) {
		return null;
	}

	@Override
	public String getViaSetName(Table1050 table1050) {
		buildViaSetNamesMapIfNeeded();
		return viaSetNames.get(table1050.getName1050());
	}

	@Override
	public String getViaSymbolicDisplacementName(Table1050 table1050) {
		return null;
	}

	@Override
	public VsamLengthType getVsamLengthType(Table1050 table1050) {
		return null;
	}

	@Override
	public boolean isVsamSpanned(Table1050 table1050) {
		return false;
	}

}
