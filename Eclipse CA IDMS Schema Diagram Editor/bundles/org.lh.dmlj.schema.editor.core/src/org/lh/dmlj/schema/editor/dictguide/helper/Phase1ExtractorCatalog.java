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
package org.lh.dmlj.schema.editor.dictguide.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class Phase1ExtractorCatalog {
	private static final String SYSTEM = "SYSTEM.";
	private static final String SOH = "\u0001";
	private static Map<String, String> map = new HashMap<>();
	private static boolean autoMerge = true;	
	
	static {
		map.put("SYSTEM.AM", "-1024");
		map.put("SYSTEM.AMDEP", "-1025");
		map.put("SYSTEM.COLUMN", "-1028");
		map.put("SYSTEM.CONSTKEY", "-1030");
		map.put("SYSTEM.CONSTRAINT", "-1029");
		map.put("SYSTEM.INDEX", "-1041");
		map.put("SYSTEM.INDEXKEY", "-1042");
		map.put("SYSTEM.ORDERKEY", "-1044");
		map.put("SYSTEM.SCHEMA", "-1045");
		map.put("SYSTEM.SECTION", "-1046");
		map.put("SYSTEM.SYNTAX", "-1049");
		map.put("SYSTEM.TABLE", "-1050");
		map.put("SYSTEM.VIEWDEP", "-1051");
		map.put("SYSTEM.RESGROUPAUTH", "-1055");  	// documentation not available
		map.put("SYSTEM.RESOURCE", "-1052");	  		// documentation not available
		map.put("SYSTEM.RESOURCEAUTH", "-1054");  	// documentation not available
		map.put("SYSTEM.RESOURCEGROUP", "-1053"); 	// documentation not available
	}
	
	private static boolean isLineTableName(String line) {
		if (!(line.startsWith("E.") && line.contains(SYSTEM) ||		// <= r17
			  line.startsWith(SYSTEM) && !line.endsWith("."))) {		// >= 18.0
			
			return false;
		}
		var i = line.trim().indexOf(SYSTEM);
		var j = line.trim().indexOf(" ", i);
		return j == -1;
	}
	
	public static void performTask(File file, File outputFolder) throws IOException {
		// we need to know if we're dealing with a release 18.0 manual or not and, if so, record the line number
		// where each table's description really starts; let's read the file for the first time...
		var release18TableStarts = new ArrayList<Integer>();
		try (var in = new BufferedReader(new FileReader(file))) {	
			var tableNameLineNumber = -1;
			var lineNumber = 1;
			for (var line = in.readLine(); line != null; line = in.readLine()) {
				if (isLineTableName(line)) {
					tableNameLineNumber = lineNumber;				
				} else if (line.trim().equals("Description")) {
					release18TableStarts.add(Integer.valueOf(tableNameLineNumber));
				}
				lineNumber += 1;
			}
		}
		
		PrintWriter out = null;
		String currentTableName = null;
		try (var in = new BufferedReader(new FileReader(file))) {
			var p = in.readLine();
			var lineNumber = 1;
			var release18TableStartsIndex = 0;
			while (p != null) {
				if ((p.trim().endsWith("SYSCA Objects") || p.trim().endsWith("SYSCA Views") && !p.trim().startsWith("Appendix E")) &&
					release18TableStarts.isEmpty() || p.trim().startsWith("SYSCA views are views defined on a subset of SYSTEM tables") &&
					!release18TableStarts.isEmpty() && release18TableStartsIndex >= release18TableStarts.size()) {
					
					// we've processed all relevant information
					break;
				}
				// for release 18.0, encountering a table name does not always mean that we're done with the previous
				// one, since a table description doesn't start on a fresh page any longer and the table name at the
				// top of each page indicates which table's description starts on that page (or which table's
				// description continues if no new table description starts on that page)
				if (isLineTableName(p) && release18TableStarts.isEmpty() || release18TableStartsIndex < release18TableStarts.size() &&
					lineNumber == release18TableStarts.get(release18TableStartsIndex).intValue()) {
					
					if (!release18TableStarts.isEmpty()) {
						release18TableStartsIndex += 1;
					}
					
					var i = p.indexOf(SYSTEM);
					var j = p.trim().indexOf(" ", i);
					if (j == -1) {
						var tableName = p.substring(i).trim();
						if (currentTableName == null || !currentTableName.equals(tableName)) {
							if (out != null) {
								out.flush();
								out.close();						
							}
							if (map.containsKey(tableName) ) {
								currentTableName = tableName;
								var fileName = tableName.substring(7) + map.get(tableName) + ".txt";
								out = new PrintWriter(new File(outputFolder, fileName));							
							} else {
								currentTableName = null;
								out = null;
							}
						}
					}
				}			
				if (out != null) {
					if (autoMerge) {
						var processed = false;
						if (p.length() == 2) {
							try {
								var i = Integer.parseInt(p);
								if ((i > 1 && i < 50) || i == 66 || i == 77 || i == 78 || i == 88) {
									out.print(p);
									if (!p.endsWith(" ")) {
										out.print(" ");
									}
									p = in.readLine();
									lineNumber += 1;
									if (p == null) {									
										out.println();
										processed = true;
									}
								}
							} catch (NumberFormatException e) {
								out.println(p);
								processed = true;
							}
						}
						if (!processed && p.endsWith("-") && !p.startsWith("X'02' and X'0A') — ")) {
							if (p.indexOf("“") != -1) {
								p = p.replace('“', '"');
							}
							if (p.indexOf("”") != -1) {
								p = p.replace('”', '"');
							}
							if (p.indexOf(SOH) != -1) {
								p = p.replace(SOH, ">");
							}
							out.print(p);
							p = in.readLine();
							lineNumber += 1;
							if (p != null) {
								if (p.indexOf("“") != -1) {
									p = p.replace('“', '"');
								}
								if (p.indexOf("”") != -1) {
									p = p.replace('”', '"');
								}
								if (p.indexOf(SOH) != -1) {
									p = p.replace(SOH, ">");
								}
								out.println(p);
							} else {
								out.println();
							}
						} else if (!processed) {
							if (p.indexOf("“") != -1) {
								p = p.replace('“', '"');
							}
							if (p.indexOf("”") != -1) {
								p = p.replace('”', '"');
							}
							if (p.indexOf(SOH) != -1) {
								p = p.replace(SOH, ">");
							}
							out.println(p);
						}
					} else {
						if (p.indexOf("“") != -1) {
							p = p.replace('“', '"');
						}
						if (p.indexOf("”") != -1) {
							p = p.replace('”', '"');
						}
						if (p.indexOf(SOH) != -1) {
							p = p.replace(SOH, ">");
						}
						out.println(p);
					}
				}
				p = in.readLine();
				lineNumber += 1;
			}
			if (out != null) {
				out.flush();
				out.close();
			}		
		}
	}
	
	private Phase1ExtractorCatalog() {
	}
	
}
