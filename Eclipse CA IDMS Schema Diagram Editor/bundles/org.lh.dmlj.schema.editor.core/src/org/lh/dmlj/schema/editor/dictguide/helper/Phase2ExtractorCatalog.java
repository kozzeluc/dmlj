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
import java.util.HashMap;
import java.util.Map;

import org.lh.dmlj.schema.editor.Plugin;

public final class Phase2ExtractorCatalog {
	private static final String CATION = "cation";
	private static final String COLUMN_NAME_COLUMN_DESCRIPTION_DATA = "Column name Column description Data";
	private static final String SYSTEM = "SYSTEM.";
	private static final String FILLER = "FILLER";
	private static final String RESOURCEGROUP_1053 = "RESOURCEGROUP-1053";
	private static final String RESOURCEAUTH_1054 = "RESOURCEAUTH-1054";
	private static final String RESOURCE_1052 = "RESOURCE-1052";
	private static final String RESGROUPAUTH_1055 = "RESGROUPAUTH-1055";
	private static final String VIEWDEP_1051 = "VIEWDEP-1051";
	private static final String TABLE_1050 = "TABLE-1050";
	private static final String SYNTAX_1049 = "SYNTAX-1049";
	private static final String SECTION_1046 = "SECTION-1046";
	private static final String SCHEMA_1045 = "SCHEMA-1045";
	private static final String ORDERKEY_1044 = "ORDERKEY-1044";
	private static final String INDEXKEY_1042 = "INDEXKEY-1042";
	private static final String INDEX_1041 = "INDEX-1041";
	private static final String CONSTRAINT_1029 = "CONSTRAINT-1029";
	private static final String CONSTKEY_1030 = "CONSTKEY-1030";
	private static final String COLUMN_1028 = "COLUMN-1028";
	private static final String AMDEP_1025 = "AMDEP-1025";
	private static final String AM_1024 = "AM-1024";
	private static final String UPPER_CASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String DIGITS = "0123456789";
	private static final String DESCRIPTION_TAG = "/* Description */ --------------------------------------------------------------";		
	private static final String DOCUMENT_NAME_TAG = "/* Document Name */ ------------------------------------------------------------";
	private static final String LOCATION_MODE_TAG = "/* Location mode */ ------------------------------------------------------------";		
	private static final String MEMBER_OF_TAG = "/* Member of */ ----------------------------------------------------------------";
	private static final String OWNER_OF_TAG = "/* Owner of */ -----------------------------------------------------------------";
	private static final String RECORD_LENGTH_TAG = "/* Record length */ ------------------------------------------------------------";
	private static final String WITHIN_AREA_TAG = "/* Within area */ --------------------------------------------------------------";
	
	private static Map<String, Integer> dataLengths = new HashMap<>();
	private static Map<String, String> locationModes = new HashMap<>();
	private static Map<String, String[]> ownerOf = new HashMap<>();
	private static Map<String, String[]> memberOf = new HashMap<>();
	private static Map<String, String> typeInfo = new HashMap<>();	
	
	static {
		dataLengths.put(AM_1024, 60);
		dataLengths.put(AMDEP_1025, 80);
		dataLengths.put(COLUMN_1028, 164);
		dataLengths.put(CONSTKEY_1030, 144);
		dataLengths.put(CONSTRAINT_1029, 504);
		dataLengths.put(INDEX_1041, 364);
		dataLengths.put(INDEXKEY_1042, 128);
		dataLengths.put(ORDERKEY_1044, 112);
		dataLengths.put(SCHEMA_1045, 164);
		dataLengths.put(SECTION_1046, 552);
		dataLengths.put(SYNTAX_1049, 120);
		dataLengths.put(TABLE_1050, 256);
		dataLengths.put(VIEWDEP_1051, 80);
		dataLengths.put(RESGROUPAUTH_1055, 192);  	// doc not available
		dataLengths.put(RESOURCE_1052, 216);			// doc not available
		dataLengths.put(RESOURCEAUTH_1054, 160);  	// doc not available
		dataLengths.put(RESOURCEGROUP_1053, 156); 	// doc not available		
		
		locationModes.put(AM_1024, "CALC using NAME-1024, VERSION-1024");
		locationModes.put(AMDEP_1025, "VIA set AM-AMDEP");
		locationModes.put(COLUMN_1028, "VIA set TABLE-COLNAME");
		locationModes.put(CONSTKEY_1030, "VIA set CONSTRAINT-KEY");
		locationModes.put(CONSTRAINT_1029, "VIA set REFERENCE-TABLE");
		locationModes.put(INDEX_1041, "VIA set TABLE-INDEX");
		locationModes.put(INDEXKEY_1042, "VIA set INDEX-INDEXKEY");
		locationModes.put(ORDERKEY_1044, "VIA set CONSTRAINT-ORDER");
		locationModes.put(SCHEMA_1045, "CALC using NAME-1045");
		locationModes.put(SECTION_1046, "VIA set TABLE-SECTION");
		locationModes.put(SYNTAX_1049, "VIA set TABLE-SYNTAX");
		locationModes.put(TABLE_1050, "CALC using NAME-1050, SCHEMA-1050");
		locationModes.put(VIEWDEP_1051, "VIA Set VIEW-DEPENDENT");
		locationModes.put(RESGROUPAUTH_1055, "CALC using AUTHID-1055");							// doc not available
		locationModes.put(RESOURCE_1052, "CALC using RESOURCETYPE-1052, RESOURCENAME-1052");		// doc not available
		locationModes.put(RESOURCEAUTH_1054, "CALC using AUTHID-1054");  							// doc not available
		locationModes.put(RESOURCEGROUP_1053, "CALC using RESOURCETYPE-1053, RESOURCENAME-1053");	// doc not available
		
		ownerOf.put(AM_1024, new String[] {"AM-AMDEP"});
		ownerOf.put(AMDEP_1025, new String[] {});
		ownerOf.put(COLUMN_1028, new String[] {});
		ownerOf.put(CONSTKEY_1030, new String[] {});
		ownerOf.put(CONSTRAINT_1029, new String[] {"CONSTRAINT-KEY", "CONSTRAINT-ORDER"});
		ownerOf.put(INDEX_1041, new String[] {"INDEX-INDEXKEY"});
		ownerOf.put(INDEXKEY_1042, new String[] {});
		ownerOf.put(ORDERKEY_1044, new String[] {});
		ownerOf.put(SCHEMA_1045, new String[] {"SCHEMA-CONSTRAINT", "SCHEMA-TABLE"});
		ownerOf.put(SECTION_1046, new String[] {});
		ownerOf.put(SYNTAX_1049, new String[] {});
		ownerOf.put(TABLE_1050, new String[] {"REFERENCED-TABLE", "REFERENCING-TABLE", "TABLE-COLNAME", "TABLE-COLNUM", 
				"TABLE-INDEX", "TABLE-SECTION", "TABLE-SYNTAX", "VIEW-REFERENCED", "VIEW-DEPENDENT"});
		ownerOf.put(VIEWDEP_1051, new String[] {});
		ownerOf.put(RESGROUPAUTH_1055,	new String[] {});									// doc not available
		ownerOf.put(RESOURCE_1052,	new String[] {"RESOURCE-AUTH"});							// doc not available
		ownerOf.put(RESOURCEAUTH_1054,	new String[] {});  									// doc not available
		ownerOf.put(RESOURCEGROUP_1053, new String[] {"RESGROUP-AUTH", "RESGROUP-RES"});		// doc not available
		
		memberOf.put(AM_1024, new String[] {});
		memberOf.put(AMDEP_1025, new String[] {"AM-AMDEP"});
		memberOf.put(COLUMN_1028, new String[] {"TABLE-COLNAME", "TABLE-COLNUM"});
		memberOf.put(CONSTKEY_1030, new String[] {"CONSTRAINT-KEY"});
		memberOf.put(CONSTRAINT_1029, new String[] {"IX-CONSTRAINT", "SCHEMA-CONSTRAINT", "REFERENCED-TABLE", "REFERENCING-TABLE"});
		memberOf.put(INDEX_1041, new String[] {"AREA-INDEX", "IX-INDEX", "TABLE-INDEX"});
		memberOf.put(INDEXKEY_1042, new String[] {"INDEX-INDEXKEY"});
		memberOf.put(ORDERKEY_1044, new String[] {"CONSTRAINT-ORDER"});
		memberOf.put(SCHEMA_1045, new String[] {"IX-SCHEMA"});
		memberOf.put(SECTION_1046, new String[] {"TABLE-SECTION"});
		memberOf.put(SYNTAX_1049, new String[] {"TABLE-SYNTAX"});
		memberOf.put(TABLE_1050, new String[] {"AREA-TABLE", "IX-TABLE", "SCHEMA-TABLE"});
		memberOf.put(VIEWDEP_1051, new String[] {"VIEW-DEPENDENT", "VIEW-REFERENCED"});
		memberOf.put(RESGROUPAUTH_1055, new String[] {"RESGROUP-AUTH"});				// doc not available
		memberOf.put(RESOURCE_1052, new String[] {"IX-RESOURCE", "RESGROUP-RES"});	// doc not available	
		memberOf.put(RESOURCEAUTH_1054, new String[] {"RESOURCE-AUTH"});  			// doc not available
		memberOf.put(RESOURCEGROUP_1053, new String[] {"IX-RESGROUP"}); 				// doc not available
		
		typeInfo.put("COLUMN-1028.DEFAULT", "CHAR(1) NOT NULL");
		typeInfo.put("INDEX-1041.AVGDMEMCLUSCNT", "REAL NOT NULL");
		typeInfo.put("INDEXKEY-1042.COLUMN", "CHAR(32) NOT NULL");
	}
	
	private static String extractFieldName(String line) {		
		return line.substring(0, line.indexOf(' '));		
	}	
	
	private static String generateFieldDescriptionTag(String line, String suffix) {
		var tag = new StringBuilder("/* Field ");
		var fieldName = extractFieldName(line);
		if (!fieldName.equals(FILLER)) {
			tag.append(fieldName + suffix);
		} else {
			tag.append(FILLER);
		}
		tag.append(" */ ");
		while (tag.length() < 80) {
			tag.append("-");
		}
		return tag.toString();
	}
	
	private static String generateRecordTag(String fileName) {
		var tag = new StringBuilder("/* ");
		tag.append(fileName.substring(0, fileName.length() - 4));
		tag.append(" */ ");
		while (tag.length() < 80) {
			tag.append("-");
		}
		return tag.toString();
	}
	
	private static boolean isFieldDescriptionBegin(String line) {
		var i = line.indexOf(" ");
		if (i != -1) {								
			var type = line.substring(0, i);
			for (var j = 0; j < type.length(); j++) {
				var r = type.substring(j, j + 1);
				if (!(UPPER_CASE_LETTERS.indexOf(r) != -1 ||	j > 0 && DIGITS.indexOf(r) != -1)) {
					return false;
				}
			}
			var trimmedLine = line.trim();
			if (!type.equals("INTEGER") && 
				!type.equals("TIMESTAMP") &&
				!type.equals("SMALLINT") && !type.equals("REAL") && 
				!trimmedLine.equals("CREATE SCHEMA) that") &&
				!trimmedLine.equals("AREA column are blank") &&
				!trimmedLine.equals("SQL schema was created,") &&
				!trimmedLine.equals("F (function), this is the") &&
				!trimmedLine.equals("WITH CHECK OPTION") &&
				!trimmedLine.equals("CHECK OPTION indicator:") &&
				!trimmedLine.equals("NUMCOLUMNS entries).") &&
				!trimmedLine.equals("NUMSORTCOLS entries):") &&
				!trimmedLine.equals("NUMSORTCOLS entries).") &&
				!trimmedLine.equals("MAXMEMPAGES (below)") &&
				!trimmedLine.equals("MAXMEMPAGES are not") &&
				!trimmedLine.equals("MAXSR8PAGES (below).") &&
				!trimmedLine.equals("NUMROWS column value in") &&
				!trimmedLine.equals("NUMPAGES column value in") &&
				!trimmedLine.equals("MAXMEMPAGES (below).") &&
				!trimmedLine.equals("AVGMEMPAGES (above)") &&
				!trimmedLine.equals("AVGSR8PAGES is more than") &&
				!trimmedLine.equals("NUMROWS in the") &&
				!trimmedLine.equals("LONGESTSR8 SR8s when") &&
				!trimmedLine.equals("LONGESTMEM indexed") &&
				!trimmedLine.equals("NUMPAGES column value") &&
				!trimmedLine.equals("NUMROWS column value") &&
				!trimmedLine.equals("CALC index when statistics") &&
				!trimmedLine.equals("AVGSR8PAGES is more") &&
				!trimmedLine.equals("NUMLONGKEYS (above).") &&
				!trimmedLine.equals("CREATE SCHEMA)") &&
				!trimmedLine.equals("CA IDMS Presspack") &&
				!trimmedLine.equals("NOT NULL") &&
				!trimmedLine.equals("INT") &&
				!trimmedLine.equals("TAMP") &&
				!trimmedLine.startsWith("CALC index when statistics were") &&
				!trimmedLine.equals("ER") &&
				!trimmedLine.startsWith("SR8 records") &&
				!trimmedLine.equals("AVGMEMPAGES (above) are not") &&
				!trimmedLine.equals("LONGESTMEM indexed rows") &&
				!trimmedLine.equals("AVGMEMPAGES (above).") &&
				!trimmedLine.equals("AVGSR8PAGES is more than 20,") &&
				!trimmedLine.equals("TYPE is N).") &&
				!trimmedLine.equals("SQL schema table") &&
				!trimmedLine.equals("OPTION indicator:") &&
				!trimmedLine.startsWith("IDMS Presspack") &&
				!trimmedLine.equals("CHECK OPTION") &&
				!trimmedLine.startsWith("CHAR3") &&
				!trimmedLine.contains(" -- ") &&
				!trimmedLine.startsWith("CHAR (1)") &&
				!trimmedLine.contains(" (above) ") ||
				trimmedLine.equals("TIMESTAMP Table timestamp, used for")) {
				
				return true;
			}
		}
		return false;
	}
	
	private static boolean isLineTableName(String line) {
		if (!(line.startsWith("E.") && line.indexOf(SYSTEM) > -1 ||	// <= r17
			  line.startsWith(SYSTEM) && !line.endsWith("."))) {		// >= 18.0
			
			return false;
		}
		var i = line.trim().indexOf(SYSTEM);
		var j = line.trim().indexOf(" ", i);
		return j == -1;
	}	
	
	private static String processFieldData(String recordName, StringBuilder fieldData) {
		var q = new StringBuilder();
		var i = fieldData.indexOf(" ") + 1; 			// offset of Column description
		
		var column = fieldData.substring(0, i).trim();
		q.append(fieldData.substring(0, i).trim()); 	// Column name
		q.append('\n');
		
		final var typeDescriptorIndex = calculateTypeDescriptorIndex(fieldData.toString());
		removeNewLineCharactersWithinOrFollowingColumType(fieldData, typeDescriptorIndex);
		
		if (typeDescriptorIndex != -1) {
			var r = new StringBuilder(fieldData.substring(typeDescriptorIndex));
			
			int k = r.indexOf(")NOT NULL");
			if (k != -1) {
				r.insert(k + 1, ' ');
			} 
			
			k = 0;
			while (k < r.length()) {
				if (r.charAt(k) == '\n') {
					r.deleteCharAt(k);
				} else {
					k += 1;
				}
			}
			q.append(r.toString());		
			
			q.append('\n');
			
			// process the column description; remove all double blanks and double new line characters as well as
			// trailing new line characters and leading blanks and new line characters from it
			r = new StringBuilder(fieldData.substring(i, typeDescriptorIndex));
			k = r.length() - 1;
			while (k >= 0) {
				if (r.charAt(k) != ' ' && r.charAt(k) != '\n') {
					break;
				}
				r.deleteCharAt(k);
				k -= 1;
			}
			k = 0;
			while (k + 1 < r.length()) {
				if (r.charAt(k) == ' ' && r.charAt(k + 1) == ' ' || r.charAt(k) == '\n' && r.charAt(k + 1) == '\n') {
					r.deleteCharAt(k + 1);
				} else {
					k += 1;
				}
			}
			while (!r.isEmpty() && (r.charAt(0) == ' ' || r.charAt(0) == '\n')) {
				r.deleteCharAt(0);
			}
			q.append(r.toString()); 
		} else if (typeInfo.containsKey(recordName + "." + column)) {
			q.append(typeInfo.get(recordName + "." + column));		
			q.append('\n');
			q.append(fieldData.substring(i));
		} else {
			q.append("N/A");		
			q.append('\n');
			q.append(fieldData.substring(i));
		}
		
		return q.toString();
	}
	
	private static int calculateTypeDescriptorIndex(String fieldData) {
		var typeDescriptorIndex = fieldData.length() - 1; 
		while (typeDescriptorIndex >= 0 && 
			   !fieldData.startsWith("BINARY(", typeDescriptorIndex) &&	
			   !fieldData.startsWith("BINAR\nY(", typeDescriptorIndex) &&
			   !fieldData.startsWith("CHAR(", typeDescriptorIndex) &&
			   !fieldData.startsWith("CHAR (", typeDescriptorIndex) &&
			   !fieldData.startsWith("INTEGER", typeDescriptorIndex) &&
			   !fieldData.startsWith("INTEG\nER", typeDescriptorIndex) &&
			   !fieldData.startsWith("REAL", typeDescriptorIndex) &&
			   !fieldData.startsWith("SMALLINT", typeDescriptorIndex) &&
			   !fieldData.startsWith("SMALL\nINT", typeDescriptorIndex) &&
			   !fieldData.startsWith("TIMESTAMP", typeDescriptorIndex) &&
			   !fieldData.startsWith("TIMES\nTAMP", typeDescriptorIndex) &&
			   !fieldData.startsWith("CHAR3", typeDescriptorIndex)) {
			
			typeDescriptorIndex--;
		}
		return typeDescriptorIndex;
	}
	
	private static void removeNewLineCharactersWithinOrFollowingColumType(StringBuilder fieldData, int typeDescriptorIndex) {
		if (typeDescriptorIndex > -1) {
			// remove new line characters within or following the column type
			if (fieldData.substring(typeDescriptorIndex).startsWith("BINARY(\n")) {
				fieldData.deleteCharAt(typeDescriptorIndex + 7);
			} else if (fieldData.substring(typeDescriptorIndex).startsWith("BINAR\nY(")) {
				fieldData.deleteCharAt(typeDescriptorIndex + 5);
			} else if (fieldData.substring(typeDescriptorIndex).startsWith("CHAR(\n")) {
				fieldData.deleteCharAt(typeDescriptorIndex + 5);
			} else if (fieldData.substring(typeDescriptorIndex).startsWith("INTEG\nER")) {
				fieldData.deleteCharAt(typeDescriptorIndex + 5);
			} else if (fieldData.substring(typeDescriptorIndex).startsWith("SMALL\nINT")) {
				fieldData.deleteCharAt(typeDescriptorIndex + 5);
			} else if (fieldData.substring(typeDescriptorIndex).startsWith("TIMES\nTAMP")) {
				fieldData.deleteCharAt(typeDescriptorIndex + 5);
			}
		}
	}
	
	private static void writeFieldData(String recordName, PrintWriter out, StringBuilder fieldData) {		
		if (!fieldData.isEmpty()) {
			out.println(processFieldData(recordName, fieldData));
		}
	}
	
	private static void writeSetData(PrintWriter out, String[] set) {
		var written = 0;
		for (var i = 0; i < set.length; i++) {
			if (i > 0) {
				out.print(", ");
				written += 2;
			}
			if (written + set[i].length() > 80) {
				out.println();
				written = 0;
			}
			out.print(set[i]);
			written += set[i].length();
		}
		out.println();
	}	
		
	private static void processFile(File file, File outputFolder, String documentTitle) throws IOException {
		// derive the part of the document title that we need to suppress lines containing that; ignore the
		// release information between brackets at the end of the title
		var i = documentTitle.lastIndexOf(" (");
		if (i < 0) {
			i = documentTitle.length();
		}
		var documentTitlePart = documentTitle.substring(0, i);
		
		var recordName = file.getName().substring(0, file.getName().length() - 4);
		var suffix = recordName.substring(recordName.length() - 5);
		try (var out = new PrintWriter(new File(outputFolder, file.getName())); var in = new BufferedReader(new FileReader(file))) {
			out.println(generateRecordTag(file.getName()));
			out.println();
			var line = in.readLine();
			var firstLineTrimmed = line.trim();
			var fieldMode = false;
			var fieldData = new StringBuilder();
			var currentFieldName = "";
			var currentLevelAndFieldName = "";
			var ignore = false;
			var descriptionOK = false;
			var nakedDescription = false; // release 18 manuals only
			String lastDescriptionLine = null;
			while (line != null) {
				if (!fieldMode) {
					if (line.startsWith("Description: ")) {
						if (documentTitle != null) {
							out.println(DOCUMENT_NAME_TAG);
							out.println(documentTitle);
							out.println();
						}
						out.println(DESCRIPTION_TAG);
						String r;
						if (line.charAt(13) == ' ') {
							r = line.substring(14);
						} else {
							r = line.substring(13);
						}
						out.println(r);
						descriptionOK = true;
						lastDescriptionLine = r;
					} else if (line.trim().equals("Description")) {
						// release 18 only: the next line(s) will contain the description
						nakedDescription = true;
					} else if (nakedDescription) {
						// release 18 only: we expect this line to be the first line that contains the naked description
						if (documentTitle != null) {
							out.println(DOCUMENT_NAME_TAG);
							out.println(documentTitle);
							out.println();
						}
						out.println(DESCRIPTION_TAG);
						out.println(line);
						nakedDescription = false;
						descriptionOK = true;
						lastDescriptionLine = line;
					} else if (line.startsWith(COLUMN_NAME_COLUMN_DESCRIPTION_DATA)) {
						if (lastDescriptionLine != null && !lastDescriptionLine.isBlank()) {
							out.println();
						}
						out.println(RECORD_LENGTH_TAG);
						if (dataLengths.containsKey(recordName)) {
							out.println(String.valueOf(dataLengths.get(recordName)));
						} else {
							out.println("N/A");
						}
						out.println();
						if (ownerOf.containsKey(recordName) && ownerOf.get(recordName).length > 0) {
							out.println(OWNER_OF_TAG);
							writeSetData(out, ownerOf.get(recordName));
							out.println();
						}
						if (memberOf.containsKey(recordName) && memberOf.get(recordName).length > 0) {
							out.println(MEMBER_OF_TAG);
							writeSetData(out, memberOf.get(recordName));
							out.println();
						}
						out.println(LOCATION_MODE_TAG);
						if (locationModes.containsKey(recordName)) {
							out.println(String.valueOf(locationModes.get(recordName)));
						} else {
							out.println("N/A");
						}
						out.println();
						out.println(WITHIN_AREA_TAG);
						out.println("DDLCAT");
						fieldMode = true;
						ignore = !line.trim().endsWith(CATION);
					} else {	
						if (!firstLineTrimmed.equals(line.trim()) && !line.endsWith(documentTitlePart) && 
							!line.startsWith("Appendix ") && !line.endsWith(". SYSTEM Tables and SYSCA Views") &&
							!line.trim().endsWith("SQL Reference Guide") && (!line.trim().equals("") || descriptionOK) &&
							!ignore) {
							
							out.println(line);
							lastDescriptionLine = line;
						} else if (ignore) {
							ignore = !line.trim().endsWith(CATION);
						}
					}
				} else {
					if (!firstLineTrimmed.equals(line.trim()) && !line.endsWith(documentTitlePart) &&
						!line.startsWith(COLUMN_NAME_COLUMN_DESCRIPTION_DATA) && !line.startsWith("Appendix ") &&
						!line.startsWith("Chapter 15: XML") && 	// the 18.5 manual has a wrong footer
						!line.endsWith(". SYSTEM Tables and SYSCA Views") && !line.trim().equals("SYSCA Objects") &&
						!line.trim().equals("SYSCA Views") && !ignore) {
							
						var fieldDescriptionBegin = isFieldDescriptionBegin(line);
						if (fieldDescriptionBegin) {
							writeFieldData(recordName, out, fieldData);
							fieldData = new StringBuilder();
							out.println();
							out.println(generateFieldDescriptionTag(line, suffix));
							currentFieldName = extractFieldName(line);
							currentLevelAndFieldName = line.substring(0, 3) + currentFieldName;
						}					
						if (!currentFieldName.equals("") && !fieldDescriptionBegin && line.startsWith(currentLevelAndFieldName) &&
							line.indexOf(FILLER) == -1 || line.equals("continued")) {
							
							if (line.equals("continued") || line.equals(currentLevelAndFieldName)) {
								// ignore line; continuation indication only
							} else {
								i = line.indexOf(" continued ¦ ");
								if (i != -1) {
									fieldData.append('\n');
									fieldData.append(line.substring(i + 11));
								} else {
									Plugin.getDefault().getLog().warn("ignored line for field " + currentFieldName + ": <" + line + ">");
								}
							}
						} else if (!isLineTableName(line) && !line.trim().equals("") && line.indexOf("SQL Reference Guide") == -1) {
							if (!fieldData.isEmpty()) {
								fieldData.append('\n');
							}
							if (!trimmedLineIsNumber(line)) {
								fieldData.append(line);
							}
						}
					} else {
						if (line.startsWith(COLUMN_NAME_COLUMN_DESCRIPTION_DATA) || ignore) {
							ignore = !line.trim().endsWith(CATION);
						}
					}
				}
				line = in.readLine();
			}
			writeFieldData(recordName, out, fieldData);
			out.flush();
		}
	}
	
	private static boolean trimmedLineIsNumber(String untrimmedLine) {
		try {
			Integer.parseInt(untrimmedLine.trim());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static void performTask(File inputFolder, File outputFolder, String documentTitle) throws IOException {
		var files = inputFolder.listFiles();
		for (int i = 0; i < files.length; i++) {
			processFile(files[i], outputFolder, documentTitle);
		}
	}
	
	private Phase2ExtractorCatalog() {		
	}
	
}
