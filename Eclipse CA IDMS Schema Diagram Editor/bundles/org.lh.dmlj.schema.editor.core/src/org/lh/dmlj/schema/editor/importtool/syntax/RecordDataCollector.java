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
package org.lh.dmlj.schema.editor.importtool.syntax;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.ProcedureCallTime;
import org.lh.dmlj.schema.RecordProcedureCallVerb;
import org.lh.dmlj.schema.VsamLengthType;
import org.lh.dmlj.schema.editor.importtool.IRecordDataCollector;

public class RecordDataCollector implements IRecordDataCollector<SchemaSyntaxWrapper> {
	private static final String PAGES = " PAGES";
	private static final String ON_ERROR_DURING = "ON ERROR DURING";
	private static final String FOR = " FOR ";
	private static final String OFFSET = " OFFSET ";
	private static final String VERSION = " VERSION ";
	private static final String WITHIN_AREA = "         WITHIN AREA ";

	private static List<String> getProcedureLines(SchemaSyntaxWrapper context) {
		return context.getLines().stream()
				.filter(line -> line.startsWith("         CALL "))
				.toList();
	}

	@Override
	public String getAreaName(SchemaSyntaxWrapper context) {
		for (var line : context.getLines()) {
			if (line.startsWith(WITHIN_AREA)) {
				var p = line.substring(21).trim();
				int i = p.indexOf(" ");
				if (i > -1) {
					return p.substring(0, i);
				} else {
					return p;
				}
			}
		}
		return null;
	}

	@Override
	public String getBaseName(SchemaSyntaxWrapper context) { 
		for (var line : context.getLines()) {
			if (line.startsWith("*+           SYNONYM OF PRIMARY RECORD ")) {
				var i = line.indexOf(VERSION);
				return line.substring(39, i);
			}
		}
		return getSynonymName(context);
	}

	@Override
	public short getBaseVersion(SchemaSyntaxWrapper context) {		
		for (var line : context.getLines()) {
			if (line.startsWith("*+           SYNONYM OF PRIMARY RECORD ")) {
				var i = line.indexOf(VERSION);
				return Short.parseShort(line.substring(i + 9).trim());
			}
		}		
		return getSynonymVersion(context);
	}

	@Override
	public DuplicatesOption getCalcKeyDuplicatesOption(SchemaSyntaxWrapper context) {
		for (var line : context.getLines()) {
			if (line.startsWith("             DUPLICATES ARE ")) {
				if (line.startsWith("BY DBKEY", 28)) {
					return DuplicatesOption.BY_DBKEY;
				} else if (line.startsWith("FIRST", 28)) {
					return DuplicatesOption.FIRST;
				} else if (line.startsWith("LAST", 28)) {
					return DuplicatesOption.LAST;
				} else if (line.startsWith("NOT ALLOWED", 28)) {
					return DuplicatesOption.NOT_ALLOWED;
				} else if (line.startsWith("UNORDERED", 28)) {
					return DuplicatesOption.UNORDERED;
				}
			}
		}
		return null;
	}

	@Override
	public Collection<String> getCalcKeyElementNames(SchemaSyntaxWrapper context) {
		var list = new ArrayList<String>();
		var i = computeFirstLineOfInterestIndex(context);
		while (i < context.getLines().size() && !context.getLines().get(i).trim().equals(")")) {
			var line = context.getLines().get(i);
			var elementNames = extractElementNames(line);
			list.addAll(elementNames);
			if (line.trim().endsWith(" )")) {
				// we've processed all elements
				break;
			} 
			i += 1;
		} 		
		return list;
	}
	
	private int computeFirstLineOfInterestIndex(SchemaSyntaxWrapper context) {
		var i = 0;
		while (!context.getLines().get(i).startsWith("         LOCATION MODE IS CALC USING ( ") &&
			   !context.getLines().get(i).startsWith("         LOCATION MODE IS VSAM CALC USING ( ")) {			
			
			i += 1;
		}
		return i;
	}
	
	private List<String> extractElementNames(String line) {
		var list = new ArrayList<String>();
		int i;
		if (line.startsWith("         LOCATION MODE IS CALC USING ( ")) {	
			i = 39;
		} else if (line.startsWith("         LOCATION MODE IS VSAM CALC USING ( ")) {
			i = 44;
		} else {
			i = 15;
		}
		var p = new StringBuilder(line.substring(i).trim());
		if (p.toString().endsWith(" )")) {
			p.setLength(p.length() - 2);
		}
		var tokenizer = new StringTokenizer(p.toString());
		while (tokenizer.hasMoreTokens()) {
			list.add(tokenizer.nextToken());
		}
		return list;
	}

	@Override
	public LocationMode getLocationMode(SchemaSyntaxWrapper context) {
		for (var line : context.getLines()) {
			if (line.startsWith("         LOCATION MODE IS ")) {
				if (line.startsWith("CALC ", 26)) {
					return LocationMode.CALC;
				} else if (line.startsWith("VIA ", 26)) {
					return LocationMode.VIA;
				} else if (line.startsWith("DIRECT", 26)) {
					return LocationMode.DIRECT;
				} else if (line.startsWith("VSAM CALC", 26)) {
					return LocationMode.VSAM_CALC;
				} else if (line.startsWith("VSAM", 26)) {
					return LocationMode.VSAM;
				}
			}
		}
		return null;
	}

	@Override
	public Short getMinimumFragmentLength(SchemaSyntaxWrapper context) {
		for (var line : context.getLines()) {
			if (line.startsWith("         MINIMUM FRAGMENT LENGTH IS ")) {
				var i = line.indexOf(" ", 36);
				var p = line.substring(36, i);
				return Short.valueOf(p);
			}
		}
		return null;
	}

	@Override
	public Short getMinimumRootLength(SchemaSyntaxWrapper context) {
		for (var line : context.getLines()) {
			if (line.startsWith("         MINIMUM ROOT LENGTH IS ")) {
				var i = line.indexOf(" ", 32);
				var p = line.substring(32, i);
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
		//          WITHIN AREA INS-DEMO-REGION OFFSET 5 PAGES FOR 45 PAGES
		for (var line : context.getLines()) {
			if (line.startsWith(WITHIN_AREA) && line.contains(OFFSET) && line.contains(" PAGES FOR ")) {
				var i = line.indexOf(OFFSET);
				var j = line.indexOf(" PAGES FOR ");
				return Integer.valueOf(line.substring(i + 8, j).trim());
			}
		}
		return null;
	}

	@Override
	public Short getOffsetOffsetPercent(SchemaSyntaxWrapper context) {
		//      WITHIN AREA ALMAI101 OFFSET 5 PERCENT FOR 20 PERCENT
		for (var line : context.getLines()) {
			if (line.startsWith(WITHIN_AREA) && line.contains(OFFSET) && line.contains(" PERCENT FOR ")) {
				var i = line.indexOf(OFFSET);
				var j = line.indexOf(" PERCENT FOR ", i);
				return Short.valueOf(line.substring(i + 8, j).trim());				
			}
		}
		return null;
	}

	@Override
	public Integer getOffsetPageCount(SchemaSyntaxWrapper context) {
		//          WITHIN AREA INS-DEMO-REGION OFFSET 5 PAGES FOR 45 PAGES
		for (var line : context.getLines()) {
			if (line.startsWith(WITHIN_AREA) && line.contains(OFFSET) && line.contains(FOR) && line.trim().endsWith(PAGES)) {
				var i = line.indexOf(FOR);
				var j = line.indexOf(PAGES, i);
				return Integer.valueOf(line.substring(i + 5, j).trim());				
			}
		}
		return null;
	}

	@Override
	public Short getOffsetPercent(SchemaSyntaxWrapper context) {
		//      WITHIN AREA ALMAI101 OFFSET 5 PERCENT FOR 20 PERCENT
		for (var line : context.getLines()) {
			if (line.startsWith(WITHIN_AREA) && line.contains(OFFSET) && line.contains(FOR) && line.trim().endsWith(" PERCENT")) {
				var i = line.indexOf(FOR);
				var j = line.indexOf(" PERCENT", i);
				return Short.valueOf(line.substring(i + 5, j).trim());				
			}
		}
		return null;
	}

	@Override
	public Collection<ProcedureCallTime> getProcedureCallTimes(SchemaSyntaxWrapper context) {
		var procedureNames = new ArrayList<>(getProceduresCalled(context));
		var procedureLines = getProcedureLines(context);
		
		var list = new ArrayList<ProcedureCallTime>();
		for (var k = 0; k < procedureLines.size(); k++) {
			var procedureName = procedureNames.get(k);
			var line = procedureLines.get(k);
			
			var i = line.indexOf(" " + procedureName + " ") + procedureName.length() + 2;
			
			if (line.startsWith(ON_ERROR_DURING, i)) {
				list.add(ProcedureCallTime.ON_ERROR_DURING);
			} else {
				var j = line.indexOf(" ", i);
				String p;
				if (j > -1) {
					p = line.substring(i, j);
				} else {
					p = line.substring(i);
				}
				list.add(ProcedureCallTime.valueOf(p));
			}
		}
		return list;
	}

	@Override
	public Collection<RecordProcedureCallVerb> getProcedureCallVerbs(SchemaSyntaxWrapper context) {
		var procedureNames = new ArrayList<>(getProceduresCalled(context));
		var procedureLines = getProcedureLines(context);		
		
		var list = new ArrayList<RecordProcedureCallVerb>();
		for (var k = 0; k < procedureLines.size(); k++) {
			var procedureName = procedureNames.get(k);
			var line = procedureLines.get(k);
		
			var i = line.indexOf(" " + procedureName + " ") + procedureName.length() + 2;
			int j;
			if (line.startsWith(ON_ERROR_DURING, i)) {
				j = i + 15;
			} else {
				j = line.indexOf(" ", i);
			}			
			if (j > -1) {
				var p = line.substring(j).trim();
				if (p.isEmpty()) {
					list.add(RecordProcedureCallVerb.EVERY_DML_FUNCTION);									
				} else {						
					list.add(RecordProcedureCallVerb.valueOf(p));
				}
			} else {
				list.add(RecordProcedureCallVerb.EVERY_DML_FUNCTION);
			}
		}
		return list;
	}

	@Override
	public Collection<String> getProceduresCalled(SchemaSyntaxWrapper context) {
		var list = new ArrayList<String>();
		for (var line : context.getLines()) {
			if (line.startsWith("         CALL ")) {
				var i = line.indexOf(" ", 14);
				var procedureName = line.substring(14, i).trim();
				list.add(procedureName);
			}
		}
		return list;
	}

	@Override
	public short getRecordId(SchemaSyntaxWrapper context) {
		for (var line : context.getLines()) {
			if (line.startsWith("         RECORD ID IS ")) {
				return Short.parseShort(line.substring(22).trim());
			}
		}		
		return -1;
	}

	@Override
	public String getSymbolicSubareaName(SchemaSyntaxWrapper context) {
		//      WITHIN AREA ALMAI101 SUBAREA AREA1
		for (var line : context.getLines()) {
			if (line.contains("WITHIN AREA ") && line.contains(" SUBAREA ")) {
			    var i = line.indexOf(" SUBAREA ");
				return line.substring(i + 9).trim();
			}
		}
		return null;		
	}
	
	@Override
	public String getSynonymName(SchemaSyntaxWrapper context) { 
		for (var line : context.getLines()) {
			if (line.startsWith("         SHARE STRUCTURE OF RECORD ")) {
				var i = line.indexOf(VERSION);
				return line.substring(35, i);
			}
		}
		for (var line : context.getLines()) {
			if (line.startsWith("*+       USES STRUCTURE OF RECORD ")) {
				var i = line.indexOf(VERSION);
				return line.substring(34, i);
			}
		}
		return getName(context);
	}

	@Override
	public short getSynonymVersion(SchemaSyntaxWrapper context) {		
		for (var line : context.getLines()) {
			if (line.startsWith("         SHARE STRUCTURE OF RECORD ")) {
				var i = line.indexOf(VERSION);
				return Short.parseShort(line.substring(i + 9).trim());
			}
		}
		for (var line : context.getLines()) {
			if (line.startsWith("*+       USES STRUCTURE OF RECORD ")) {
				var i = line.indexOf(VERSION);
				return Short.parseShort(line.substring(i + 9).trim());
			}
		}
		return 1;
	}

	@Override
	public Short getViaDisplacementPageCount(SchemaSyntaxWrapper context) {
		// [...] DISPLACEMENT 5 PAGES
		for (var line : context.getLines()) {
			if (line.contains("DISPLACEMENT ") && line.trim().endsWith(PAGES)) {
			    var i = line.indexOf("DISPLACEMENT ");
			    var j = line.indexOf(PAGES, i);
				return Short.valueOf(line.substring(i + 13, j).trim());
			}
		}
		return null;
	}

	@Override
	public String getViaSetName(SchemaSyntaxWrapper context) {
		for (var line : context.getLines()) {
			if (line.startsWith("         LOCATION MODE IS VIA ")) {				
				return line.substring(30, line.indexOf(" ", 30));
			}
		}
		return null;
	}

	@Override
	public String getViaSymbolicDisplacementName(SchemaSyntaxWrapper context) {
		// [...] DISPLACEMENT USING DISPL1
		for (var line : context.getLines()) {
			if (line.contains("DISPLACEMENT USING ")) {
			    var i = line.indexOf("DISPLACEMENT USING ");
			    return line.substring(i + 19).trim();
			}
		}
		return null;
	}

	@Override
	public VsamLengthType getVsamLengthType(SchemaSyntaxWrapper context) {
		for (var line : context.getLines()) {
			if (line.startsWith("         VSAM TYPE IS ")) {				
				if (line.substring(22).toUpperCase().startsWith("FIXED LENGTH")) {
					return VsamLengthType.FIXED;
				} else if (line.substring(22).toUpperCase().startsWith("VARIABLE LENGTH")) {
					return VsamLengthType.VARIABLE;
				}
			}
		}
		return null;
	}

	@Override
	public boolean isVsamSpanned(SchemaSyntaxWrapper context) {
		for (var line : context.getLines()) {
			if (line.startsWith("         VSAM TYPE IS ")) {				
				return line.trim().toUpperCase().endsWith(" SPANNED");
			}
		}
		return false;
	}	
	
}
