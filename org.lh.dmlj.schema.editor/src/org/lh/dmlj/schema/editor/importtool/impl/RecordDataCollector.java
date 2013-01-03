package org.lh.dmlj.schema.editor.importtool.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.ProcedureCallTime;
import org.lh.dmlj.schema.RecordProcedureCallVerb;
import org.lh.dmlj.schema.StorageMode;
import org.lh.dmlj.schema.editor.importtool.IRecordDataCollector;

public class RecordDataCollector 
	implements IRecordDataCollector<SchemaSyntaxWrapper> {

	public RecordDataCollector() {
		super();
	}

	@Override
	public String getAreaName(SchemaSyntaxWrapper context) {
		for (String line : context.getList()) {
			if (line.startsWith("         WITHIN AREA ")) {
				return line.substring(21).trim();
			}
		}
		return null;
	}

	@Override
	public DuplicatesOption getCalcKeyDuplicatesOption(SchemaSyntaxWrapper context) {
		for (String line : context.getList()) {
			if (line.startsWith("             DUPLICATES ARE ")) {
				if (line.substring(28).startsWith("BY DBKEY")) {
					return DuplicatesOption.BY_DBKEY;
				} else if (line.substring(28).startsWith("FIRST")) {
					return DuplicatesOption.FIRST;
				} else if (line.substring(28).startsWith("LAST")) {
					return DuplicatesOption.LAST;
				} else if (line.substring(28).startsWith("NOT ALLOWED")) {
					return DuplicatesOption.NOT_ALLOWED;
				}
			}
		}
		return null;
	}

	@Override
	public Collection<String> getCalcKeyElementNames(SchemaSyntaxWrapper context) {
		List<String> list = new ArrayList<>();
		// at the moment, we only deliver the first CALC-key element
		for (String line : context.getList()) {
			if (line.startsWith("         LOCATION MODE IS CALC USING ( ")) {
				String p = line.substring(39).trim();
				list.add(p.substring(0, p.indexOf(" ")));
			}
		}
		return list;
	}

	@Override
	public boolean getCalcKeyIsNaturalSequence(SchemaSyntaxWrapper context) {
		return false;
	}

	@Override
	public LocationMode getLocationMode(SchemaSyntaxWrapper context) {
		for (String line : context.getList()) {
			if (line.startsWith("         LOCATION MODE IS ")) {
				if (line.substring(26).startsWith("CALC ")) {
					return LocationMode.CALC;
				} else if (line.substring(26).startsWith("VIA ")) {
					return LocationMode.VIA;
				} else if (line.substring(26).startsWith("DIRECT")) {
					return LocationMode.DIRECT;
				}
			}
		}
		return null;
	}

	@Override
	public Short getMinimumFragmentLength(SchemaSyntaxWrapper context) {
		return null;
	}

	@Override
	public Short getMinimumRootLength(SchemaSyntaxWrapper context) {
		return null;
	}

	@Override
	public String getName(SchemaSyntaxWrapper context) {
		return context.getList().get(1).substring(20).trim();
	}

	@Override
	public Integer getOffsetOffsetPageCount(SchemaSyntaxWrapper context) {
		return null;
	}

	@Override
	public Short getOffsetOffsetPercent(SchemaSyntaxWrapper context) {
		return null;
	}

	@Override
	public Integer getOffsetPageCount(SchemaSyntaxWrapper context) {
		return null;
	}

	@Override
	public Short getOffsetPercent(SchemaSyntaxWrapper context) {
		return null;
	}

	@Override
	public Collection<ProcedureCallTime> getProcedureCallTimes(SchemaSyntaxWrapper context, 
															   String procedureName) {
		return null;
	}

	@Override
	public Collection<RecordProcedureCallVerb> getProcedureCallVerbs(SchemaSyntaxWrapper context, 
																	 String procedureName) {
		return null;
	}

	@Override
	public Collection<String> getProceduresCalled(SchemaSyntaxWrapper context) {
		return Collections.emptyList();
	}

	@Override
	public short getRecordId(SchemaSyntaxWrapper context) {
		for (String line : context.getList()) {
			if (line.startsWith("         RECORD ID IS ")) {
				return Short.valueOf(line.substring(22).trim()).shortValue();
			}
		}		
		return -1;
	}

	@Override
	public StorageMode getStorageMode(SchemaSyntaxWrapper context) {
		return StorageMode.FIXED;
	}

	@Override
	public String getSymbolicSubareaName(SchemaSyntaxWrapper context) {
		return null;
	}

	@Override
	public Short getViaDisplacementPageCount(SchemaSyntaxWrapper recordContext) {
		return null;
	}

	@Override
	public String getViaSetName(SchemaSyntaxWrapper context) {
		for (String line : context.getList()) {
			if (line.startsWith("         LOCATION MODE IS VIA ")) {				
				return line.substring(30, line.indexOf(" ", 30));
			}
		}
		return null;
	}

	@Override
	public String getViaSymbolicDisplacementName(SchemaSyntaxWrapper recordContext) {
		return null;
	}

}