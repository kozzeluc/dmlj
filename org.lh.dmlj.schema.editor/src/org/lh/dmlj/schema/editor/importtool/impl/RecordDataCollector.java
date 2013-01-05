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

	private static List<String> getProcedureLines(SchemaSyntaxWrapper context,
			  									  String procedureName) {

		List<String> list = new ArrayList<>();
		String scanItem = "         CALL " + procedureName + " ";				
		for (String line : context.getLines()) {
			if (line.startsWith(scanItem)) {				
				list.add(line);
			}
		}		
		return list;
	}	
	
	public RecordDataCollector() {
		super();
	}

	private static boolean containsOccursDependingOnField(SchemaSyntaxWrapper context) {		
		for (String line : context.getLines()) {
			if (line.indexOf(" OCCURS ") > -1 &&
				line.indexOf(" TIMES ") > -1 &&
				line.indexOf(" DEPENDING ON ") > -1) {
				
				return true;
			}
		}
		return false;
	}

	private static boolean isCompressedRecord(SchemaSyntaxWrapper context) {
		return !getProcedureLines(context, "IDMSCOMP").isEmpty();  		
	}

	@Override
	public String getAreaName(SchemaSyntaxWrapper context) {
		for (String line : context.getLines()) {
			if (line.startsWith("         WITHIN AREA ")) {
				return line.substring(21).trim();
			}
		}
		return null;
	}

	@Override
	public DuplicatesOption getCalcKeyDuplicatesOption(SchemaSyntaxWrapper context) {
		for (String line : context.getLines()) {
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
		// TODO at the moment, we only deliver the first CALC-key element, so
		// make sure we can process them all
		for (String line : context.getLines()) {
			if (line.startsWith("         LOCATION MODE IS CALC USING ( ")) {
				String p = line.substring(39).trim();
				list.add(p.substring(0, p.indexOf(" ")));
			}
		}
		return list;
	}

	@Override
	public boolean getCalcKeyIsNaturalSequence(SchemaSyntaxWrapper context) {
		// TODO figure this out
		return false;
	}

	@Override
	public LocationMode getLocationMode(SchemaSyntaxWrapper context) {
		for (String line : context.getLines()) {
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
		for (String line : context.getLines()) {
			if (line.startsWith("         MINIMUM FRAGMENT LENGTH IS ")) {
				int i = line.indexOf(" ", 36);
				String p = line.substring(36, i);
				return Short.valueOf(p);
			}
		}
		return null;
	}

	@Override
	public Short getMinimumRootLength(SchemaSyntaxWrapper context) {
		for (String line : context.getLines()) {
			if (line.startsWith("         MINIMUM ROOT LENGTH IS ")) {
				int i = line.indexOf(" ", 32);
				String p = line.substring(32, i);
				return Short.valueOf(p);
			}
		}
		return null;
	}

	@Override
	public String getName(SchemaSyntaxWrapper context) {
		return context.getLines().get(1).substring(20).trim();
	}

	@Override
	public Integer getOffsetOffsetPageCount(SchemaSyntaxWrapper context) {
		// TODO figure this out
		return null;
	}

	@Override
	public Short getOffsetOffsetPercent(SchemaSyntaxWrapper context) {
		// TODO figure this out
		return null;
	}

	@Override
	public Integer getOffsetPageCount(SchemaSyntaxWrapper context) {
		// TODO figure this out
		return null;
	}

	@Override
	public Short getOffsetPercent(SchemaSyntaxWrapper context) {
		// TODO figure this out
		return null;
	}

	@Override
	public Collection<ProcedureCallTime> getProcedureCallTimes(SchemaSyntaxWrapper context, 
															   String procedureName) {
		// TODO figure this out		
		return null;
	}

	@Override
	public Collection<RecordProcedureCallVerb> getProcedureCallVerbs(SchemaSyntaxWrapper context, 
																	 String procedureName) {
		
		// TODO figure this out		
		return null;
	}

	@Override
	public Collection<String> getProceduresCalled(SchemaSyntaxWrapper context) {
		// TODO figure this out
		return Collections.emptyList();
	}

	@Override
	public short getRecordId(SchemaSyntaxWrapper context) {
		for (String line : context.getLines()) {
			if (line.startsWith("         RECORD ID IS ")) {
				return Short.valueOf(line.substring(22).trim()).shortValue();
			}
		}		
		return -1;
	}

	@Override
	public StorageMode getStorageMode(SchemaSyntaxWrapper context) {
		StringBuilder p = new StringBuilder();
		if (!containsOccursDependingOnField(context)) {
			p.append("FIXED");
		} else {
			p.append("VARIABLE");
		}
		if (isCompressedRecord(context)) {
			p.append(" COMPRESSED");
		}
		String q = p.toString(); 
		if (q.equals("FIXED")) {
			return StorageMode.FIXED;
		} else if (q.equals("FIXED COMPRESSED")) {
			return StorageMode.FIXED_COMPRESSED;
		} else if (q.equals("VARIABLE")) {
			return StorageMode.VARIABLE;
		} else {
			return StorageMode.VARIABLE_COMPRESSED;
		}
	}

	@Override
	public String getSymbolicSubareaName(SchemaSyntaxWrapper context) {
		// TODO figure this out
		return null;
	}

	@Override
	public Short getViaDisplacementPageCount(SchemaSyntaxWrapper recordContext) {
		// TODO figure this out
		return null;
	}

	@Override
	public String getViaSetName(SchemaSyntaxWrapper context) {
		for (String line : context.getLines()) {
			if (line.startsWith("         LOCATION MODE IS VIA ")) {				
				return line.substring(30, line.indexOf(" ", 30));
			}
		}
		return null;
	}

	@Override
	public String getViaSymbolicDisplacementName(SchemaSyntaxWrapper recordContext) {
		// TODO figure this out
		return null;
	}	

}