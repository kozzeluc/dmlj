/**
 * Copyright (C) 2021  Luc Hermans
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
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.ProcedureCallTime;
import org.lh.dmlj.schema.RecordProcedureCallVerb;
import org.lh.dmlj.schema.VsamLengthType;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IQuery;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IRowProcessor;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.Query;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.SchemaImportSession;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Rcdsyn_079;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sam_056;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Scr_054;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Smr_052;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sr_036;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Srcall_040;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Srcd_113;
import org.lh.dmlj.schema.editor.importtool.IRecordDataCollector;

public class DictionaryRecordDataCollector implements IRecordDataCollector<Srcd_113> {

	private Map<String, Smr_052> calcKeyElementsMap;
	private SchemaImportSession session;
	private Map<String, String> viaSetNames;

	public DictionaryRecordDataCollector(SchemaImportSession session) {
		super();
		this.session = session;
	}
	
	private void buildCalcKeyElementsMapIfNeeded() {
		if (calcKeyElementsMap != null) {
			return;
		}
		calcKeyElementsMap = new HashMap<>();
		IQuery calcKeyElementListQuery = new Query.Builder().forCalcKeyElementList(session).build();
		session.runQuery(calcKeyElementListQuery, new IRowProcessor() {			
			@Override
			public void processRow(ResultSet row) throws SQLException {
				String srNam_056 = 
					JdbcTools.removeTrailingSpaces(row.getString(Sam_056.SR_NAM_056));
				if (!calcKeyElementsMap.containsKey(srNam_056)) {
					Smr_052 smr_052 = new Smr_052();
					// we only take what we need for our SMR-052, so some fields will NOT be set
					smr_052.setRowid(JdbcTools.getRowid(row, Smr_052.ROWID));
					smr_052.setDup_052(row.getShort(Smr_052.DUP_052));
					smr_052.setSetNam_052(row.getString(Smr_052.SET_NAM_052)); // CALC
					calcKeyElementsMap.put(srNam_056, smr_052);
				}
				Smr_052 smr_052 = calcKeyElementsMap.get(srNam_056);
				// we only take what we need for our SCR-054, so some fields will NOT be set
				Scr_054 scr_054 = new Scr_054();
				scr_054.setScrNam_054(row.getString(Scr_054.SCR_NAM_054));
				smr_052.getScr_054s().add(scr_054);		
			}
		});
	}

	private void buildViaSetNamesMapIfNeeded() {
		if (viaSetNames != null) {
			return;
		}
		viaSetNames = new HashMap<>();
		IQuery viaSetListQuery = new Query.Builder().forViaSetList(session).build();
		session.runQuery(viaSetListQuery, new IRowProcessor() {
			@Override
			public void processRow(ResultSet row) throws SQLException {
				String srNam_056 = 
					JdbcTools.removeTrailingSpaces(row.getString(Sam_056.SR_NAM_056));
				String setNam_052 = 
					JdbcTools.removeTrailingSpaces(row.getString(Smr_052.SET_NAM_052));
				viaSetNames.put(srNam_056, setNam_052);
			}
		});				
	}

	@Override
	public String getAreaName(Srcd_113 srcd_113) {
		Sam_056 sam_056 = srcd_113.getSam_056();
		return sam_056.getSaNam_056();
	}

	@Override
	public String getBaseName(Srcd_113 srcd_113) {
		Sr_036 sr_036 = srcd_113.getRcdsyn_079().getSr_036();
		return sr_036.getSrNam_036();
	}

	@Override
	public short getBaseVersion(Srcd_113 srcd_113) {
		Sr_036 sr_036 = srcd_113.getRcdsyn_079().getSr_036();
		return sr_036.getRcdVers_036();
	}

	@Override
	public DuplicatesOption getCalcKeyDuplicatesOption(Srcd_113 srcd_113) {
		buildCalcKeyElementsMapIfNeeded();
		Smr_052 smr_052 = calcKeyElementsMap.get(getName(srcd_113));
		if (smr_052.getDup_052() == 0) {
			return DuplicatesOption.NOT_ALLOWED;
		} else if (smr_052.getDup_052() == 1) {
			return DuplicatesOption.FIRST;
		} else if (smr_052.getDup_052() == 2) {
			return DuplicatesOption.LAST;
		} else if (smr_052.getDup_052() == 3) {
			return DuplicatesOption.UNORDERED;
		} else if (smr_052.getDup_052() == 4) {
			return DuplicatesOption.BY_DBKEY;
		} else {
			return null;
		}
	}

	@Override
	public Collection<String> getCalcKeyElementNames(Srcd_113 srcd_113) {
		buildCalcKeyElementsMapIfNeeded();
		List<String> list = new ArrayList<>();
		Smr_052 smr_052 = calcKeyElementsMap.get(getName(srcd_113));
		for (Scr_054 scr_054 :  smr_052.getScr_054s()) {	
			list.add(scr_054.getScrNam_054());
		}
		return list;
	}

	@Override
	public LocationMode getLocationMode(Srcd_113 srcd_113) {
		if (srcd_113.getMode_113() == 0) {
			return LocationMode.VIA;
		} else if (srcd_113.getMode_113() == 1) {
			return LocationMode.CALC;
		} else if (srcd_113.getMode_113() == 3) {
			return LocationMode.VSAM;
		} else if (srcd_113.getMode_113() == 4) {
			return LocationMode.VSAM_CALC;
		} else {
			return LocationMode.DIRECT;
		}
	}

	@Override
	public Short getMinimumFragmentLength(Srcd_113 srcd_113) {
		if (srcd_113.getRecType_113().equals("V")) {
			return Short.valueOf(srcd_113.getMinFrag_113());
		} else {
			return null;
		}
	}

	@Override
	public Short getMinimumRootLength(Srcd_113 srcd_113) {
		if (srcd_113.getRecType_113().equals("V")) {
			return Short.valueOf((short) (srcd_113.getMinRoot_113() - 4));
		} else {
			return null;
		}
	}

	@Override
	public String getName(Srcd_113 srcd_113) {
		Sam_056 sam_056 = srcd_113.getSam_056();
		return sam_056.getSrNam_056();
	}

	@Override
	public Integer getOffsetOffsetPageCount(Srcd_113 srcd_113) {
		if (srcd_113.getPageOffset_113() > -1) {
			return Integer.valueOf(srcd_113.getPageOffset_113());
		} else {
			return null;
		}
	}

	@Override
	public Short getOffsetOffsetPercent(Srcd_113 srcd_113) {
		if (srcd_113.getPageOffsetPercent_113() > -1) {
			return Short.valueOf(srcd_113.getPageOffsetPercent_113());
		} else {
			return null;
		}
	}

	@Override
	public Integer getOffsetPageCount(Srcd_113 srcd_113) {
		if (srcd_113.getPageCount_113() > -1) {
			return Integer.valueOf(srcd_113.getPageCount_113());
		} else {
			return null;
		}
	}

	@Override
	public Short getOffsetPercent(Srcd_113 srcd_113) {
		if (srcd_113.getPageCountPercent_113() > -1) {
			return Short.valueOf(srcd_113.getPageCountPercent_113());
		} else {
			return null;
		}
	}

	@Override
	public Collection<ProcedureCallTime> getProcedureCallTimes(Srcd_113 srcd_113) {
		List<ProcedureCallTime> list = new ArrayList<>();		
		for (Srcall_040 srcall_040 : srcd_113.getSrcall_040s()) {
			if (srcall_040.getCallTime_040().equals("00")) {
				list.add(ProcedureCallTime.BEFORE);
			} else if (srcall_040.getCallTime_040().equals("01")) {
				list.add(ProcedureCallTime.ON_ERROR_DURING);
			} else if (srcall_040.getCallTime_040().equals("02")) {
				list.add(ProcedureCallTime.AFTER);
			}
			
		}		
		return list;
	}

	@Override
	public Collection<RecordProcedureCallVerb> getProcedureCallVerbs(Srcd_113 srcd_113) {
		List<RecordProcedureCallVerb> list = new ArrayList<>();		
		for (Srcall_040 srcall_040 : srcd_113.getSrcall_040s()) {
			if (!srcall_040.getDbpFunc_040().equals("")) {
				String trigger = srcall_040.getDbpFunc_040().toUpperCase();					
				list.add(RecordProcedureCallVerb.valueOf(trigger.toString()));				
			} else {
				// null doesn't work here and hence the EVERY_DML_FUNCTION verb was created
				list.add(RecordProcedureCallVerb.EVERY_DML_FUNCTION);
			}			
		}		
		return list;
	}

	@Override
	public Collection<String> getProceduresCalled(Srcd_113 srcd_113) {
		List<String> list = new ArrayList<>();		
		for (Srcall_040 srcall_040 : srcd_113.getSrcall_040s()) {
			String procedureName = srcall_040.getCallProc_040();
			list.add(procedureName);
		}		
		return list;
	}

	@Override
	public short getRecordId(Srcd_113 srcd_113) {
		return srcd_113.getSrId_113();
	}

	@Override
	public String getSymbolicSubareaName(Srcd_113 srcd_113) {
		if (!srcd_113.getSubarea_113().equals("")) {
			return srcd_113.getSubarea_113();
		} else {
			return null;
		}
	}

	@Override
	public String getSynonymName(Srcd_113 srcd_113) {
		Rcdsyn_079 rcdsyn_079 = srcd_113.getRcdsyn_079();		
		return rcdsyn_079.getRsynName_079();
	}

	@Override
	public short getSynonymVersion(Srcd_113 srcd_113) {
		Rcdsyn_079 rcdsyn_079 = srcd_113.getRcdsyn_079();		
		return rcdsyn_079.getRsynVer_079();
	}

	@Override
	public Short getViaDisplacementPageCount(Srcd_113 srcd_113) {
		if (srcd_113.getDspl_113() > 0) {
			return Short.valueOf(srcd_113.getDspl_113());
		} else {
			return null;
		}
	}

	@Override
	public String getViaSetName(Srcd_113 srcd_113) {
		buildViaSetNamesMapIfNeeded();
		return viaSetNames.get(getName(srcd_113));
	}

	@Override
	public String getViaSymbolicDisplacementName(Srcd_113 srcd_113) {
		if (!srcd_113.getSymbolDisplace_113().equals("")) {
			return srcd_113.getSymbolDisplace_113();
		} else {
			return null;
		}
	}

	@Override
	public VsamLengthType getVsamLengthType(Srcd_113 srcd_113) {
		if (srcd_113.getRecType_113().equals("F")) {
			return VsamLengthType.FIXED;
		} else if (srcd_113.getRecType_113().equals("V")) {
			return VsamLengthType.VARIABLE;
		} else {
			return null;
		}
	}

	@Override
	public boolean isVsamSpanned(Srcd_113 srcd_113) {
		String vsamType = srcd_113.getVsamType_113();
		return vsamType.equals("D"); // "A" denotes NONSPANNED
	}

}
