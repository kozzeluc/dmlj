/**
 * Copyright (C) 2013  Luc Hermans
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
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public abstract class Phase1Extractor {		
	
	private static final boolean autoMerge = true;
	
	public static void performTask(Reader reader, File outputFolder) 
		throws IOException {
		
		PrintWriter out = null;
		String currentRecordName = null; // only used for pre release 18 manuals
		BufferedReader in = new BufferedReader(reader);
		String p = in.readLine();
		String chapterNo = null;
		// release 18 manuals are different beasts...
		boolean typeRelease18 = false;
		boolean allRecordNamesCollectedForTypeRelease18 = false;
		List<String> recordNameForTypeRelease18 = new ArrayList<>();
		List<String> markersForTypeRelease18 = new ArrayList<>();
		int typeRelease18Index = 0;
		boolean markerForRelease18Passed = false;
		while (p != null) {
			boolean skip = false;
			if (typeRelease18Index < markersForTypeRelease18.size() &&
				p.indexOf(markersForTypeRelease18.get(typeRelease18Index)) > -1) {
				
				markerForRelease18Passed = true;
				skip = true;
			}
			if (chapterNo == null && p.startsWith("Chapter ")) {
				chapterNo = p.substring(8, 9);
				typeRelease18 = p.indexOf(": ") > -1;
			} else if (chapterNo != null &&
					   !typeRelease18 &&
				       p.startsWith(chapterNo + ".") && 
				       p.indexOf(". . .") == -1 &&
				       p.indexOf("About This Chapter") == -1 &&
				       p.indexOf("Overview") == -1) {
				
				// this is not a release 18 manual
				String recordName = p.substring(p.indexOf(" ")).trim();
				if (currentRecordName == null || 
					!currentRecordName.equals(recordName)) {
				
					currentRecordName = recordName;
					if (out != null) {
						out.flush();
						out.close();
					}					
					out = new PrintWriter(new File(outputFolder, 
												   recordName + ".txt"));
				}
			} else if (chapterNo != null &&
					   typeRelease18 &&
					   p.indexOf(" (see page") > -1 &&
					   !allRecordNamesCollectedForTypeRelease18) {
				
				// release 18 type manual and we are collecting the record names
				if (p.startsWith("Records (see page ")) {
					// for the given release 18 manual, we now have collected
					// the names of all record names
					allRecordNamesCollectedForTypeRelease18 = true;					
				} else {
					// get the record name and add it to the list
					int i = p.indexOf(" (see page ");
					String recordName = p.substring(0, i);
					recordNameForTypeRelease18.add(recordName);
					// we also need the page number to compose the marker
					int j = p.indexOf(")", i);
					int pageNumber = 
						Integer.valueOf(p.substring(i + 11, j)).intValue();
					String marker = 
						pageNumber % 2 == 0 ?
						pageNumber + "  Dictionary Structure Reference Guide" : 
						"Record and Element Descriptions  " + pageNumber;
					markersForTypeRelease18.add(marker);
					
				}
			} else if (typeRelease18Index < recordNameForTypeRelease18.size() &&
					   p.trim().equals(recordNameForTypeRelease18.get(typeRelease18Index)) &&
					   markerForRelease18Passed) {
				
				// start of a fresh record description for release 18
				if (out != null) {
					out.flush();
					out.close();
				}					
				String recordName = 
					recordNameForTypeRelease18.get(typeRelease18Index);
				out = new PrintWriter(new File(outputFolder, 
											   recordName + ".txt"));
				typeRelease18Index += 1;
				markerForRelease18Passed = false;
				skip = false;
			} else if (typeRelease18Index < recordNameForTypeRelease18.size() &&
					   p.trim().equals(recordNameForTypeRelease18.get(typeRelease18Index)) &&
					   !markerForRelease18Passed) {
				
				// false start of a fresh record description for release 18; 
				// suppress the line
				skip = true;
			}
			if (!skip && out != null) {
				if (autoMerge) {
					boolean processed = false;
					if (p.length() == 2) {
						try {
							int i = Integer.parseInt(p);
							if ((i > 1 && i < 50) || i == 66 || i == 77 || 
								i == 78 || i == 88) {
								
								out.print(p);
								if (!p.endsWith(" ")) {
									out.print(" ");
								}
								p = in.readLine();
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
					if (!processed && p.endsWith("-") && 
						!p.startsWith("X'02' and X'0A') — ")) {
						
						if (p.indexOf("“") != -1) {
							p = p.replace('“', '"');
						}
						if (p.indexOf("”") != -1) {
							p = p.replace('”', '"');
						}
						if (p.indexOf("") != -1) {
							p = p.replace('', '>');
						}
						out.print(p);
						p = in.readLine();
						if (p != null) {
							if (p.indexOf("“") != -1) {
								p = p.replace('“', '"');
							}
							if (p.indexOf("”") != -1) {
								p = p.replace('”', '"');
							}
							if (p.indexOf("") != -1) {
								p = p.replace('', '>');
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
						if (p.indexOf("") != -1) {
							p = p.replace('', '>');
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
					if (p.indexOf("") != -1) {
						p = p.replace('', '>');
					}					
					out.println(p);
				}
			}
			p = in.readLine();
		}
		if (out != null) {
			out.flush();
			out.close();
		}		
		in.close();
	}
}
