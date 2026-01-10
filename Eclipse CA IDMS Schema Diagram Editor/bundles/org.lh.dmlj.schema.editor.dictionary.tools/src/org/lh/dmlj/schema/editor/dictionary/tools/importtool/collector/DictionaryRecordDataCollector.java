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
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.ProcedureCallTime;
import org.lh.dmlj.schema.RecordProcedureCallVerb;
import org.lh.dmlj.schema.VsamLengthType;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IRowProcessor;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.Query;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.SchemaImportSession;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sam056;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Scr054;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Smr052;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Srcd113;
import org.lh.dmlj.schema.editor.importtool.IRecordDataCollector;

public class DictionaryRecordDataCollector implements IRecordDataCollector<Srcd113> {
	private final SchemaImportSession session;
	private Map<String, Smr052> calcKeyElementsMap;
	private Map<String, String> viaSetNames;

	public DictionaryRecordDataCollector(SchemaImportSession session) {
		this.session = session;
	}
	
	private void buildCalcKeyElementsMapIfNeeded() {
		if (calcKeyElementsMap != null) {
			return;
		}
		calcKeyElementsMap = new HashMap<>();
		var calcKeyElementListQuery = new Query.Builder().forCalcKeyElementList(session).build();
		session.runQuery(calcKeyElementListQuery, new IRowProcessor() {			
			@Override
			public void processRow(ResultSet row) throws SQLException {
				var srNam056 = JdbcTools.removeTrailingSpaces(row.getString(Sam056.SR_NAM_056));
				var smr052 = calcKeyElementsMap.computeIfAbsent(srNam056, unused -> createSmr052(row));
				// we only take what we need for our SCR-054, so some fields will NOT be set
				var scr054 = new Scr054();
				scr054.setScrNam054(row.getString(Scr054.SCR_NAM_054));
				smr052.getScr054s().add(scr054);		
			}
		});
	}
	
	private Smr052 createSmr052(ResultSet row) {
		try {
			var smr052 = new Smr052();
			// we only take what we need for our SMR-052, so some fields will NOT be set
			smr052.setRowid(JdbcTools.getRowid(row, Smr052.ROWID));
			smr052.setDup052(row.getShort(Smr052.DUP_052));
			smr052.setSetNam052(row.getString(Smr052.SET_NAM_052)); // CALC
			return smr052;
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}

	private void buildViaSetNamesMapIfNeeded() {
		if (viaSetNames != null) {
			return;
		}
		viaSetNames = new HashMap<>();
		var viaSetListQuery = new Query.Builder().forViaSetList(session).build();
		session.runQuery(viaSetListQuery, new IRowProcessor() {
			@Override
			public void processRow(ResultSet row) throws SQLException {
				var srNam056 = JdbcTools.removeTrailingSpaces(row.getString(Sam056.SR_NAM_056));
				var setNam052 = JdbcTools.removeTrailingSpaces(row.getString(Smr052.SET_NAM_052));
				viaSetNames.put(srNam056, setNam052);
			}
		});				
	}

	@Override
	public String getAreaName(Srcd113 srcd113) {
		return srcd113.getSam056().getSaNam056();
	}

	@Override
	public String getBaseName(Srcd113 srcd113) {
		return srcd113.getRcdsyn079().getSr036().getSrNam036();
	}

	@Override
	public short getBaseVersion(Srcd113 srcd113) {
		return srcd113.getRcdsyn079().getSr036().getRcdVers036();
	}

	@Override
	public DuplicatesOption getCalcKeyDuplicatesOption(Srcd113 srcd113) {
		buildCalcKeyElementsMapIfNeeded();
		return switch (calcKeyElementsMap.get(getName(srcd113)).getDup052()) {
			case 0 -> DuplicatesOption.NOT_ALLOWED;
			case 1 -> DuplicatesOption.FIRST;
			case 2 -> DuplicatesOption.LAST;
			case 3 -> DuplicatesOption.UNORDERED;
			case 4 -> DuplicatesOption.BY_DBKEY;
			default -> null;
		};
	}

	@Override
	public Collection<String> getCalcKeyElementNames(Srcd113 srcd113) {
		buildCalcKeyElementsMapIfNeeded();
		return calcKeyElementsMap.get(getName(srcd113)).getScr054s().stream()
				.map(Scr054::getScrNam054)
				.toList();
	}

	@Override
	public LocationMode getLocationMode(Srcd113 srcd113) {
		return switch (srcd113.getMode113()) {
			case 0 -> LocationMode.VIA;
			case 1 -> LocationMode.CALC;
			case 3 -> LocationMode.VSAM;
			case 4 -> LocationMode.VSAM_CALC;
			default -> LocationMode.DIRECT;
		};
	}

	@Override
	public Short getMinimumFragmentLength(Srcd113 srcd113) {
		if (srcd113.getRecType113().equals("V")) {
			return srcd113.getMinFrag113();
		} else {
			return null;
		}
	}

	@Override
	public Short getMinimumRootLength(Srcd113 srcd113) {
		if (srcd113.getRecType113().equals("V")) {
			return (short) (srcd113.getMinRoot113() - 4);
		} else {
			return null;
		}
	}

	@Override
	public String getName(Srcd113 srcd113) {
		return srcd113.getSam056().getSrNam056();
	}

	@Override
	public Integer getOffsetOffsetPageCount(Srcd113 srcd113) {
		if (srcd113.getPageOffset113() > -1) {
			return srcd113.getPageOffset113();
		} else {
			return null;
		}
	}

	@Override
	public Short getOffsetOffsetPercent(Srcd113 srcd113) {
		if (srcd113.getPageOffsetPercent113() > -1) {
			return srcd113.getPageOffsetPercent113();
		} else {
			return null;
		}
	}

	@Override
	public Integer getOffsetPageCount(Srcd113 srcd113) {
		if (srcd113.getPageCount113() > -1) {
			return srcd113.getPageCount113();
		} else {
			return null;
		}
	}

	@Override
	public Short getOffsetPercent(Srcd113 srcd113) {
		if (srcd113.getPageCountPercent113() > -1) {
			return srcd113.getPageCountPercent113();
		} else {
			return null;
		}
	}

	@Override
	public Collection<ProcedureCallTime> getProcedureCallTimes(Srcd113 srcd113) {
		var list = new ArrayList<ProcedureCallTime>();		
		for (var srcall040 : srcd113.getSrcall040s()) {
			if (srcall040.getCallTime040().equals("00")) {
				list.add(ProcedureCallTime.BEFORE);
			} else if (srcall040.getCallTime040().equals("01")) {
				list.add(ProcedureCallTime.ON_ERROR_DURING);
			} else if (srcall040.getCallTime040().equals("02")) {
				list.add(ProcedureCallTime.AFTER);
			}
		}		
		return list;
	}

	@Override
	public Collection<RecordProcedureCallVerb> getProcedureCallVerbs(Srcd113 srcd113) {
		var list = new ArrayList<RecordProcedureCallVerb>();		
		for (var srcall040 : srcd113.getSrcall040s()) {
			if (!srcall040.getDbpFunc040().isEmpty()) {
				var trigger = srcall040.getDbpFunc040().toUpperCase();					
				list.add(RecordProcedureCallVerb.valueOf(trigger));
			} else {
				// null doesn't work here and hence the EVERY_DML_FUNCTION verb was created
				list.add(RecordProcedureCallVerb.EVERY_DML_FUNCTION);
			}			
		}		
		return list;
	}

	@Override
	public Collection<String> getProceduresCalled(Srcd113 srcd113) {
		var list = new ArrayList<String>();		
		for (var srcall040 : srcd113.getSrcall040s()) {
			var procedureName = srcall040.getCallProc040();
			list.add(procedureName);
		}		
		return list;
	}

	@Override
	public short getRecordId(Srcd113 srcd113) {
		return srcd113.getSrId113();
	}

	@Override
	public String getSymbolicSubareaName(Srcd113 srcd113) {
		if (!srcd113.getSubarea113().isEmpty()) {
			return srcd113.getSubarea113();
		} else {
			return null;
		}
	}

	@Override
	public String getSynonymName(Srcd113 srcd113) {
		return srcd113.getRcdsyn079().getRsynName079();
	}

	@Override
	public short getSynonymVersion(Srcd113 srcd113) {
		return srcd113.getRcdsyn079().getRsynVer079();
	}

	@Override
	public Short getViaDisplacementPageCount(Srcd113 srcd113) {
		if (srcd113.getDspl113() > 0) {
			return srcd113.getDspl113();
		} else {
			return null;
		}
	}

	@Override
	public String getViaSetName(Srcd113 srcd113) {
		buildViaSetNamesMapIfNeeded();
		return viaSetNames.get(getName(srcd113));
	}

	@Override
	public String getViaSymbolicDisplacementName(Srcd113 srcd113) {
		if (!srcd113.getSymbolDisplace113().isEmpty()) {
			return srcd113.getSymbolDisplace113();
		} else {
			return null;
		}
	}

	@Override
	public VsamLengthType getVsamLengthType(Srcd113 srcd113) {
		if (srcd113.getRecType113().equals("F")) {
			return VsamLengthType.FIXED;
		} else if (srcd113.getRecType113().equals("V")) {
			return VsamLengthType.VARIABLE;
		} else {
			return null;
		}
	}

	@Override
	public boolean isVsamSpanned(Srcd113 srcd113) {
		var vsamType = srcd113.getVsamType113();
		return vsamType.equals("D"); // "A" denotes NONSPANNED
	}

}
