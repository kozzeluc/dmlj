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

import static java.util.stream.Collectors.groupingBy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public final class Phase1Extractor {
	private static final String SOH = "\u0001";
	private static final boolean AUTOMERGE = true;
	
	public static void performTask(Reader reader, File outputFolder, File pdf1ExtractFile) throws IOException {
		if (chapterMarkerFound(pdf1ExtractFile)) {
			performTaskForLegacyManuals(reader, outputFolder);
		} else {
			reader.close();
			performTaskForRecentManuals(pdf1ExtractFile, outputFolder);
		}
	}

	private static boolean chapterMarkerFound(File pdf1ExtractFile) throws IOException {
		try (var fr = new FileReader(pdf1ExtractFile); var in = new BufferedReader(fr);) {
			for (var line = in.readLine(); line != null; line = in.readLine()) {
				if (line.startsWith("Chapter ")) {
					return true;
				}
			}
		}
		return false;
	}
	
	private static void performTaskForLegacyManuals(Reader reader, File outputFolder) throws IOException {
		String currentRecordName = null; // only used for pre release 18 manuals
		String chapterNo = null;
		// release 18 manuals are different beasts...
		var typeRelease18 = false;
		var allRecordNamesCollectedForTypeRelease18 = false;
		var recordNameForTypeRelease18 = new ArrayList<String>();
		var markersForTypeRelease18 = new ArrayList<String>();
		var typeRelease18Index = 0;
		var markerForRelease18Passed = false;
		
		PrintWriter out = null;
		
		var in = new BufferedReader(reader);
		var line = in.readLine();
		while (line != null) {
			var skip = false;
			if (typeRelease18Index < markersForTypeRelease18.size() && line.contains(markersForTypeRelease18.get(typeRelease18Index))) {
				markerForRelease18Passed = true;
				skip = true;
			}
			if (chapterNo == null && line.startsWith("Chapter ")) {
				chapterNo = line.substring(8, 9);
				typeRelease18 = line.contains(": ");
			} else if (chapterNo != null && !typeRelease18 && line.startsWith(chapterNo + ".") && !line.contains(". . .") &&
					!line.contains("About This Chapter") && !line.contains("Overview")) {
				
				// this is not a release 18 manual
				var recordName = line.substring(line.indexOf(" ")).trim();
				if (currentRecordName == null || !currentRecordName.equals(recordName)) {	
					currentRecordName = recordName;
					if (out != null) {
						out.flush();
						out.close();
					}					
					out = new PrintWriter(new File(outputFolder, recordName + ".txt"));
				}
			} else if (chapterNo != null && typeRelease18 && line.contains(" (see page") && !allRecordNamesCollectedForTypeRelease18) {
				// release 18 type manual and we are collecting the record names
				if (line.startsWith("Records (see page ")) {
					// for the given release 18 manual, we now have collected the names of all record names
					allRecordNamesCollectedForTypeRelease18 = true;					
				} else {
					// get the record name and add it to the list
					var i = line.indexOf(" (see page ");
					var recordName = line.substring(0, i);
					recordNameForTypeRelease18.add(recordName);
					// we also need the page number to compose the marker
					var j = line.indexOf(")", i);
					var pageNumber = Integer.parseInt(line.substring(i + 11, j));
					var marker = pageNumber % 2 == 0 ? pageNumber + "  Dictionary Structure Reference Guide" :
							"Record and Element Descriptions  " + pageNumber;
					markersForTypeRelease18.add(marker);
				}
			} else if (typeRelease18Index < recordNameForTypeRelease18.size() &&
					line.trim().equals(recordNameForTypeRelease18.get(typeRelease18Index)) && markerForRelease18Passed) {
				// start of a fresh record description for release 18
				if (out != null) {
					out.flush();
					out.close();
				}					
				var recordName = recordNameForTypeRelease18.get(typeRelease18Index);
				out = new PrintWriter(new File(outputFolder, recordName + ".txt"));
				typeRelease18Index += 1;
				markerForRelease18Passed = false;
				skip = false;
			} else if (typeRelease18Index < recordNameForTypeRelease18.size() &&
					line.trim().equals(recordNameForTypeRelease18.get(typeRelease18Index)) && !markerForRelease18Passed) {
				
				// false start of a fresh record description for release 18 - suppress the line
				skip = true;
			}
			if (!skip && out != null) {
				if (AUTOMERGE) {
					var processed = false;
					if (line.length() == 2) {
						try {
							var i = Integer.parseInt(line);
							if ((i > 1 && i < 50) || i == 66 || i == 77 || i == 78 || i == 88) {
								out.print(line);
								if (!line.endsWith(" ")) {
									out.print(" ");
								}
								line = in.readLine();
								if (line == null) {									
									out.println();
									processed = true;
								}
							}
						} catch (NumberFormatException e) {
							out.println(line);
							processed = true;
						}
					}
					if (!processed && line.endsWith("-") && !line.startsWith("X'02' and X'0A') — ")) {
						if (line.contains("“")) {
							line = line.replace('“', '"');
						}
						if (line.contains("”")) {
							line = line.replace('”', '"');
						}
						if (line.contains(SOH)) {
							line = line.replace(SOH, ">");
						}
						out.print(line);
						line = in.readLine();
						if (line != null) {
							if (line.contains("“")) {
								line = line.replace('“', '"');
							}
							if (line.contains("”")) {
								line = line.replace('”', '"');
							}
							if (line.contains(SOH)) {
								line = line.replace(SOH, ">");
							}
							out.println(line);
						} else {
							out.println();
						}
					} else if (!processed) {
						if (line.contains("“")) {
							line = line.replace('“', '"');
						}
						if (line.contains("”")) {
							line = line.replace('”', '"');
						}
						if (line.contains(SOH)) {
							line = line.replace(SOH, ">");
						}
						out.println(line);
					}
				} else {
					if (line.contains("“")) {
						line = line.replace('“', '"');
					}
					if (line.contains("”")) {
						line = line.replace('”', '"');
					}
					if (line.contains(SOH)) {
						line = line.replace(SOH, ">");
					}					
					out.println(line);
				}
			}
			line = in.readLine();
		}
		if (out != null) {
			out.flush();
			out.close();
		}		
		in.close();
	}

	private static void performTaskForRecentManuals(File pdf1ExtractFile, File outputFolder) throws IOException {
		var recordNames = getRecordNames(pdf1ExtractFile);
		var recordNameIndex = 0;
		String recordNameForFlushing = null;
		var linesToFlush = new ArrayList<String>();
		try (var fr = new FileReader(pdf1ExtractFile); var in = new BufferedReader(fr);) {
			for (var line = in.readLine(); line != null; line = in.readLine()) {
				if (recordNameIndex < recordNames.size() && line.trim().equals(recordNames.get(recordNameIndex))) {
					flush(recordNameForFlushing, linesToFlush, outputFolder);
					linesToFlush.add(line);
					recordNameForFlushing = recordNames.get(recordNameIndex);
					recordNameIndex += 1;
				} else if (recordNameForFlushing != null && !line.isBlank()) {
					if (isTitleLineThatNeedsBeSkipped(line)) {
						linesToFlush.remove(linesToFlush.size() - 1);
					} else if (line.startsWith("03 CMT-INFO-050 continued ")) {
						linesToFlush.add(line.substring(26));
					} else {
						linesToFlush.add(line);
					}
				}
			}
		}
		flush(recordNameForFlushing, linesToFlush, outputFolder);
	}
	
	private static void flush(String recordName, List<String> linesToFlush, File outputFolder) throws IOException {
		if (recordName != null) {
			var out = new PrintWriter(new File(outputFolder, recordName + ".txt"));
			linesToFlush.stream().forEach(out::println);
			out.flush();
			out.close();
		}
		linesToFlush.clear();
	}

	private static List<String> getRecordNames(File pdf1ExtractFile) throws IOException {
		var candidateRecordNames = new LinkedHashSet<String>();
		try (var fr = new FileReader(pdf1ExtractFile); var in = new BufferedReader(fr);) {
			for (var line = in.readLine(); line != null; line = in.readLine()) {
				if (isRecordName(line.trim())) {
					candidateRecordNames.add(line.trim());
				}
			}
		}
		var toRetain = candidateRecordNames.stream()
				.collect(groupingBy(candidate -> candidate.substring(candidate.indexOf("-") + 1)))
				.values().stream()
				.map(list -> list.get(0))
				.toList();
		candidateRecordNames.retainAll(toRetain);
		return new ArrayList<>(candidateRecordNames);
	}

	private static boolean isRecordName(String line) {
		var i = line.lastIndexOf("-");
		if (i < 0) {
			return false;
		}
		
		var prefix = line.substring(0, i).toCharArray();
		if (prefix.length == 0) {
			return false;
		}
		var hyphenCountInPrefix = 0;
		for (i = 0; i < prefix.length; i++) {
			if (prefix[i] == '-') {
				hyphenCountInPrefix += 1;
			} else if (!Character.isLetter(prefix[i])) {
				return false;
			}
		}
		if  (hyphenCountInPrefix > 1) {
			return false;
		}
		
		var suffix = line.substring(i + 1);
		if (suffix.length() < 3 || suffix.length() > 4) {
			return false;
		}
		try {
			Integer.parseInt(suffix);
		} catch (NumberFormatException e) {
			return false;
		}
		
		return true;
	}
	
	private static boolean isTitleLineThatNeedsBeSkipped(String line) {
		return line.trim().equals("CA IDMS Dictionary Structure Reference");
	}
	
	private Phase1Extractor() {
	}
	
}
