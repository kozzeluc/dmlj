package org.lh.dmlj.schema.editor.importtool.impl;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

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
				String p = line.substring(21).trim();
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
		int i = 0;
		while (!context.getLines()
					   .get(i)
					   .startsWith("         LOCATION MODE IS CALC USING ( ")) {
			
			i += 1;
		}
		while (i < context.getLines().size()) {
			if (context.getLines().get(i).trim().equals(")")) {
				// we've processed all elements; no relevant data on this line
				break;
			}
			int j;
			if (context.getLines()
					   .get(i)
					   .startsWith("         LOCATION MODE IS CALC USING ( ")) {
				
				// first line
				j = 39;
			} else {
				j = 15;
			}
			StringBuilder p = 
				new StringBuilder(context.getLines().get(i).substring(j).trim());
			if (p.toString().endsWith(" )")) {
				p.setLength(p.length() - 2);
			}
			StringTokenizer tokenizer = new StringTokenizer(p.toString());
			while (tokenizer.hasMoreTokens()) {
				list.add(tokenizer.nextToken());
			}
			if (context.getLines().get(i).trim().endsWith(" )")) {
				// we've processed all elements
				break;
			} 
			i += 1;
		} 		
		return list;
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
		//          WITHIN AREA INS-DEMO-REGION OFFSET 5 PAGES FOR 45 PAGES
		for (String line : context.getLines()) {
			if (line.startsWith("         WITHIN AREA ") &&
				line.indexOf(" OFFSET ") > -1 &&
				line.indexOf(" PAGES FOR ") > -1) {
				
				int i = line.indexOf(" OFFSET ");
				int j = line.indexOf(" PAGES FOR ");
				return Integer.valueOf(line.substring(i + 8, j).trim());				
			}
		}
		return null;
	}

	@Override
	public Short getOffsetOffsetPercent(SchemaSyntaxWrapper context) {
		//      WITHIN AREA ALMAI101 OFFSET 5 PERCENT FOR 20 PERCENT
		for (String line : context.getLines()) {
			if (line.startsWith("         WITHIN AREA ") &&
				line.indexOf(" OFFSET ") > -1 &&
				line.indexOf(" PERCENT FOR ") > -1) {
				
				int i = line.indexOf(" OFFSET ");
				int j = line.indexOf(" PERCENT FOR ", i);
				return Short.valueOf(line.substring(i + 8, j).trim());				
			}
		}
		return null;
	}

	@Override
	public Integer getOffsetPageCount(SchemaSyntaxWrapper context) {
		//          WITHIN AREA INS-DEMO-REGION OFFSET 5 PAGES FOR 45 PAGES
		for (String line : context.getLines()) {
			if (line.startsWith("         WITHIN AREA ") &&
				line.indexOf(" OFFSET ") > -1 &&
				line.indexOf(" FOR ") > -1 &&
				line.trim().endsWith(" PAGES")) {
				
				int i = line.indexOf(" FOR ");
				int j = line.indexOf(" PAGES", i);
				return Integer.valueOf(line.substring(i + 5, j).trim());				
			}
		}
		return null;
	}

	@Override
	public Short getOffsetPercent(SchemaSyntaxWrapper context) {
		//      WITHIN AREA ALMAI101 OFFSET 5 PERCENT FOR 20 PERCENT
		for (String line : context.getLines()) {
			if (line.startsWith("         WITHIN AREA ") &&
				line.indexOf(" OFFSET ") > -1 &&
				line.indexOf(" FOR ") > -1 &&
				line.trim().endsWith(" PERCENT")) {
				
				int i = line.indexOf(" FOR ");
				int j = line.indexOf(" PERCENT", i);
				return Short.valueOf(line.substring(i + 5, j).trim());				
			}
		}
		return null;
	}

	@Override
	public Collection<ProcedureCallTime> getProcedureCallTimes(SchemaSyntaxWrapper context, 
															   String procedureName) {
		List<ProcedureCallTime> list = new ArrayList<>();
		
		for (String line : getProcedureLines(context, procedureName)) {
			int i = line.indexOf(" " + procedureName + " ") + 
					procedureName.length() + 2;
			
			if (line.substring(i).startsWith("ON ERROR DURING")) {
				list.add(ProcedureCallTime.ON_ERROR_DURING);
			} else {
				int j = line.indexOf(" ", i);
				String p;
				if (j > -1) {
					p = line.substring(i, j);
				} else {
					p = line.substring(i);
				}
				ProcedureCallTime procedureCallTime = 
					ProcedureCallTime.valueOf(p);
				list.add(procedureCallTime);
			}
		}
		
		return list;
		
	}

	@Override
	public Collection<RecordProcedureCallVerb> getProcedureCallVerbs(SchemaSyntaxWrapper context, 
																	 String procedureName) {
		
		List<RecordProcedureCallVerb> list = new ArrayList<>();
		
		for (String line : getProcedureLines(context, procedureName)) {
			int i = line.indexOf(" " + procedureName + " ") + 
					procedureName.length() + 2;
			int j;
			if (line.substring(i).startsWith("ON ERROR DURING")) {
				j = i + 15;
			} else {
				j = line.indexOf(" ", i);
			}			
			if (j > -1) {
				String p = line.substring(j).trim();
				if (p.equals("")) {
					list.add(RecordProcedureCallVerb.EVERY_DML_FUNCTION);									
				} else {										
					RecordProcedureCallVerb recordProcedureCallVerb =
						RecordProcedureCallVerb.valueOf(p);
					list.add(recordProcedureCallVerb);					
				}
			} else {
				list.add(RecordProcedureCallVerb.EVERY_DML_FUNCTION);
			}			
		}
		
		return list;
		
	}

	@Override
	public Collection<String> getProceduresCalled(SchemaSyntaxWrapper context) {
		List<String> list = new ArrayList<>();
		for (String line : context.getLines()) {
			if (line.startsWith("         CALL ")) {
				int i = line.indexOf(" ", 14);
				String procedureName = line.substring(14, i).trim();
				if (!list.contains(procedureName)) {
					list.add(procedureName);
				}
			}
		}
		return list;
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
		//      WITHIN AREA ALMAI101 SUBAREA AREA1
		for (String line : context.getLines()) {
			if (line.indexOf("WITHIN AREA ") > -1 &&
			    line.indexOf(" SUBAREA ") > -1) {
			    	
			    int i = line.indexOf(" SUBAREA ");
				return line.substring(i + 9).trim();
			}
		}
		return null;		
	}

	@Override
	public Short getViaDisplacementPageCount(SchemaSyntaxWrapper context) {
		// [...] DISPLACEMENT 5 PAGES
		for (String line : context.getLines()) {
			if (line.indexOf("DISPLACEMENT ") > -1 &&
			    line.trim().endsWith(" PAGES")) {
			    	
			    int i = line.indexOf("DISPLACEMENT ");
			    int j = line.indexOf(" PAGES", i);
				return Short.valueOf(line.substring(i + 13, j).trim());
			}
		}
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
	public String getViaSymbolicDisplacementName(SchemaSyntaxWrapper context) {
		// [...] DISPLACEMENT USING DISPL1
		for (String line : context.getLines()) {
			if (line.indexOf("DISPLACEMENT USING ") > -1) {
			    int i = line.indexOf("DISPLACEMENT USING ");
			    return line.substring(i + 19).trim();
			}
		}
		return null;
	}	

}