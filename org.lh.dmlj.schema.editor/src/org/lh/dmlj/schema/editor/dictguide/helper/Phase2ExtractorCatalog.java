package org.lh.dmlj.schema.editor.dictguide.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public abstract class Phase2ExtractorCatalog {
	private static final String 	  	 UPPER_CASE_LETTERS = 
		"ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String 	  	 DIGITS = "0123456789";
	private static final String 	  	 DESCRIPTION_TAG =
		"/* Description */ --------------------------------------------------" + 
		"------------";		
	private static final String 	  	 DOCUMENT_NAME_TAG =
		"/* Document Name */ ------------------------------------------------" + 
		"------------";		
	@SuppressWarnings("unused")
	private static final String 	  	 DOCUMENT_ID_TAG =
		"/* Document ID */ --------------------------------------------------" + 
		"------------";		
	private static final String 	  	 LOCATION_MODE_TAG =
		"/* Location mode */ ------------------------------------------------" + 
		"------------";		
	private static final String 	  	 MEMBER_OF_TAG =
		"/* Member of */ ----------------------------------------------------" + 
		"------------";
	private static final String 	  	 OWNER_OF_TAG =
		"/* Owner of */ -----------------------------------------------------" + 
		"------------";
	private static final String 	  	 RECORD_LENGTH_TAG =
		"/* Record length */ ------------------------------------------------" + 
		"------------";
	private static final String 	  	 WITHIN_AREA_TAG =
		"/* Within area */ --------------------------------------------------" + 
		"------------";	
	
	private static Map<String, Integer>  dataLengths = 
		new HashMap<String, Integer>();
	private static Map<String, String> 	 locationModes = 
		new HashMap<String, String>();
	private static Map<String, String[]> ownerOf = 
		new HashMap<String, String[]>();
	private static Map<String, String[]> memberOf = 
		new HashMap<String, String[]>();
	private static Map<String, String>	 typeInfo =
		new HashMap<String, String>();	
	
	static {
		dataLengths.put("AM-1024", 60);
		dataLengths.put("AMDEP-1025", 80);
		dataLengths.put("COLUMN-1028", 164);
		dataLengths.put("CONSTKEY-1030", 144);
		dataLengths.put("CONSTRAINT-1029", 504);
		dataLengths.put("INDEX-1041", 364);
		dataLengths.put("INDEXKEY-1042", 128);
		dataLengths.put("ORDERKEY-1044", 112);
		dataLengths.put("SCHEMA-1045", 164);
		dataLengths.put("SECTION-1046", 552);
		dataLengths.put("SYNTAX-1049", 120);
		dataLengths.put("TABLE-1050", 256);
		dataLengths.put("VIEWDEP-1051", 80);
		dataLengths.put("RESGROUPAUTH-1055", 192);  	// doc not available
		dataLengths.put("RESOURCE-1052", 216);	   		// doc not available
		dataLengths.put("RESOURCEAUTH-1054", 160);  	// doc not available
		dataLengths.put("RESOURCEGROUP-1053", 156); 	// doc not available		
		//
		locationModes.put("AM-1024", "CALC using NAME-1024, VERSION-1024");
		locationModes.put("AMDEP-1025", "VIA set AM-AMDEP");
		locationModes.put("COLUMN-1028", "VIA set TABLE-COLNAME");
		locationModes.put("CONSTKEY-1030", "VIA set CONSTRAINT-KEY");
		locationModes.put("CONSTRAINT-1029", "VIA set REFERENCE-TABLE");
		locationModes.put("INDEX-1041", "VIA set TABLE-INDEX");
		locationModes.put("INDEXKEY-1042", "VIA set INDEX-INDEXKEY");
		locationModes.put("ORDERKEY-1044", "VIA set CONSTRAINT-ORDER");
		locationModes.put("SCHEMA-1045", "CALC using NAME-1045");
		locationModes.put("SECTION-1046", "VIA set TABLE-SECTION");
		locationModes.put("SYNTAX-1049", "VIA set TABLE-SYNTAX");
		locationModes.put("TABLE-1050", "CALC using NAME-1050, SCHEMA-1050");
		locationModes.put("VIEWDEP-1051", "VIA Set VIEW-DEPENDENT");
		locationModes.put("RESGROUPAUTH-1055", 			// doc not available
						  "CALC using AUTHID-1055");	
		locationModes.put("RESOURCE-1052", 				// doc not available
						  "CALC using RESOURCETYPE-1052, " +
						  "RESOURCENAME-1052");	   		
		locationModes.put("RESOURCEAUTH-1054",			// doc not available 
						  "CALC using AUTHID-1054");  	
		locationModes.put("RESOURCEGROUP-1053", 		// doc not available
					      "CALC using RESOURCETYPE-1053, " +
					      "RESOURCENAME-1053"); 		
		//
		ownerOf.put("AM-1024", new String[] {"AM-AMDEP"});
		ownerOf.put("AMDEP-1025", new String[] {});
		ownerOf.put("COLUMN-1028", new String[] {});
		ownerOf.put("CONSTKEY-1030", new String[] {});
		ownerOf.put("CONSTRAINT-1029", 
					new String[] {"CONSTRAINT-KEY", "CONSTRAINT-ORDER"});
		ownerOf.put("INDEX-1041", new String[] {"INDEX-INDEXKEY"});
		ownerOf.put("INDEXKEY-1042", new String[] {});
		ownerOf.put("ORDERKEY-1044", new String[] {});
		ownerOf.put("SCHEMA-1045", 
					new String[] {"SCHEMA-CONSTRAINT", "SCHEMA-TABLE"});
		ownerOf.put("SECTION-1046", new String[] {});
		ownerOf.put("SYNTAX-1049", new String[] {});
		ownerOf.put("TABLE-1050", 
					new String[] {"REFERENCED-TABLE", "REFERENCING-TABLE",
								  "TABLE-COLNAME", "TABLE-COLNUM", 
								  "TABLE-INDEX", "TABLE-SECTION", 
								  "TABLE-SYNTAX", "VIEW-REFERENCED", 
								  "VIEW-DEPENDENT"});
		ownerOf.put("VIEWDEP-1051", new String[] {});
		ownerOf.put("RESGROUPAUTH-1055",				// doc not available 
					new String[] {});	
		ownerOf.put("RESOURCE-1052",					// doc not available 
					new String[] {"RESOURCE-AUTH"});	
		ownerOf.put("RESOURCEAUTH-1054",				// doc not available 
					new String[] {});  	
		ownerOf.put("RESOURCEGROUP-1053", 				// doc not available
					new String[] {"RESGROUP-AUTH", "RESGROUP-RES"});
		//
		memberOf.put("AM-1024", new String[] {});
		memberOf.put("AMDEP-1025", new String[] {"AM-AMDEP"});
		memberOf.put("COLUMN-1028", 
					 new String[] {"TABLE-COLNAME", "TABLE-COLNUM"});
		memberOf.put("CONSTKEY-1030", new String[] {"CONSTRAINT-KEY"});
		memberOf.put("CONSTRAINT-1029", 
					 new String[] {"IX-CONSTRAINT", "SCHEMA-CONSTRAINT",
								   "REFERENCED-TABLE", "REFERENCING-TABLE"});
		memberOf.put("INDEX-1041", 
					 new String[] {"AREA-INDEX", "IX-INDEX", "TABLE-INDEX"});
		memberOf.put("INDEXKEY-1042", new String[] {"INDEX-INDEXKEY"});
		memberOf.put("ORDERKEY-1044", new String[] {"CONSTRAINT-ORDER"});
		memberOf.put("SCHEMA-1045", new String[] {"IX-SCHEMA"});
		memberOf.put("SECTION-1046", new String[] {"TABLE-SECTION"});
		memberOf.put("SYNTAX-1049", new String[] {"TABLE-SYNTAX"});
		memberOf.put("TABLE-1050", 
					 new String[] {"AREA-TABLE", "IX-TABLE", "SCHEMA-TABLE"});
		memberOf.put("VIEWDEP-1051", 
					 new String[] {"VIEW-DEPENDENT", "VIEW-REFERENCED"});
		memberOf.put("RESGROUPAUTH-1055",				// doc not available 
					 new String[] {"RESGROUP-AUTH"});	
		memberOf.put("RESOURCE-1052",					// doc not available 
					new String[] {"IX-RESOURCE", "RESGROUP-RES"});	
		memberOf.put("RESOURCEAUTH-1054",				// doc not available 
					new String[] {"RESOURCE-AUTH"});  	
		memberOf.put("RESOURCEGROUP-1053", 				// doc not available
					new String[] {"IX-RESGROUP"}); 
		//
		typeInfo.put("COLUMN-1028.DEFAULT", "CHAR(1) NOT NULL");
		typeInfo.put("INDEX-1041.AVGDMEMCLUSCNT", "REAL NOT NULL");
		typeInfo.put("INDEXKEY-1042.COLUMN", "CHAR(32) NOT NULL");
	}
	
	private static String extractFieldName(String p) {		
		return p.substring(0, p.indexOf(' '));		
	}	
	
	private static String generateFieldDescriptionTag(String p, 
														String suffix) {
		StringBuilder q = new StringBuilder("/* Field ");
		String fieldName = extractFieldName(p);
		if (!fieldName.equals("FILLER")) {
			q.append(fieldName + suffix);
		} else {
			q.append("FILLER");
		}
		q.append(" */ ");
		while (q.length() < 80) {
			q.append("-");
		}
		return q.toString(); 
	}
	
	private static String generateRecordTag(String fileName) {
		StringBuilder p = new StringBuilder("/* ");
		p.append(fileName.substring(0, fileName.length() - 4));
		p.append(" */ ");
		while (p.length() < 80) {
			p.append("-");
		}
		return p.toString();
	}
	
	private static boolean isFieldDescriptionBegin(String p) {
		/*if (p.length() < 4) {
			return false;
		}*/
		int i = p.indexOf(" ");
		if (i != -1) {								
			String q = p.substring(0, i);
			for (int j = 0; j < q.length(); j++) {
				String r = q.substring(j, j + 1);
				if (!(UPPER_CASE_LETTERS.indexOf(r) != -1 ||					
					  j > 0 && DIGITS.indexOf(r) != -1)) {
									
					return false;
				}
			}
			String r = p.trim();
			if (!q.equals("INTEGER") && 
				!q.equals("TIMESTAMP") &&
				!q.equals("SMALLINT") && !q.equals("REAL") && 
				!r.equals("CREATE SCHEMA) that") &&
				!r.equals("AREA column are blank") &&
				!r.equals("SQL schema was created,") &&
				!r.equals("F (function), this is the") &&
				!r.equals("WITH CHECK OPTION") &&
				!r.equals("CHECK OPTION indicator:") &&
				!r.equals("NUMCOLUMNS entries).") &&
				!r.equals("NUMSORTCOLS entries):") &&
				!r.equals("NUMSORTCOLS entries).") &&
				!r.equals("MAXMEMPAGES (below)") &&
				!r.equals("MAXMEMPAGES are not") &
				!r.equals("MAXSR8PAGES (below).") &&
				!r.equals("NUMROWS column value in") &&
				!r.equals("NUMPAGES column value in") &&
				!r.equals("MAXMEMPAGES (below).") &&
				!r.equals("AVGMEMPAGES (above)") &&
				!r.equals("AVGSR8PAGES is more than") &&
				!r.equals("NUMROWS in the") &&
				!r.equals("LONGESTSR8 SR8s when") &&
				!r.equals("LONGESTMEM indexed") &&
				!r.equals("NUMPAGES column value") &&
				!r.equals("NUMROWS column value") &&
				!r.equals("CALC index when statistics") &&
				!r.equals("AVGSR8PAGES is more") &&
				!r.equals("NUMLONGKEYS (above).") &&
				!r.equals("CREATE SCHEMA)") &&
				!r.equals("CA IDMS Presspack") &&
				!r.equals("NOT NULL") &&
				!r.equals("INT") &&
				!r.equals("TAMP") &&
				!r.startsWith("CALC index when statistics were") &&
				!r.equals("ER") &&
				!r.startsWith("SR8 records") &&
				!r.equals("AVGMEMPAGES (above) are not") &&
				!r.equals("LONGESTMEM indexed rows") &&
				!r.equals("AVGMEMPAGES (above).") &&
				!r.equals("AVGSR8PAGES is more than 20,") &&
				!r.equals("TYPE is N).") &&
				!r.equals("SQL schema table") &&
				!r.equals("OPTION indicator:") &&
				!r.startsWith("IDMS Presspack") &&
				!r.equals("CHECK OPTION") &&
				!r.startsWith("CHAR3") ||
				r.equals("TIMESTAMP Table timestamp, used for")) {
				
				return true;
			}
		}
		return false;
	}
	
	private static boolean isLineTableName(String line) {
		if (!(line.startsWith("E.") && line.indexOf("SYSTEM.") > -1 ||	// <= r17
			  line.startsWith("SYSTEM.") && !line.endsWith("."))) {		// >= 18.0
			
			return false;
		}
		int i = line.trim().indexOf("SYSTEM.");
		int j = line.trim().indexOf(" ", i);
		return j == -1;
	}	
	
	private static String processFieldData(String recordName, StringBuilder p) {
		
		StringBuilder q = new StringBuilder();
		int i = p.indexOf(" ") + 1; 		// offset of Column description
		//
		String column = p.substring(0, i).trim();
		q.append(p.substring(0, i).trim()); // Column name
		q.append('\n');
		//
		int j = p.length() - 1; 
		while (j >= 0 && 
			   !p.substring(j).startsWith("BINARY(") &&	
			   !p.substring(j).startsWith("BINAR\nY(") &&	
			   !p.substring(j).startsWith("CHAR(") &&
			   !p.substring(j).startsWith("INTEGER") &&
			   !p.substring(j).startsWith("INTEG\nER") &&
			   !p.substring(j).startsWith("REAL") &&	
			   !p.substring(j).startsWith("SMALLINT") &&
			   !p.substring(j).startsWith("SMALL\nINT") &&
			   !p.substring(j).startsWith("TIMESTAMP") &&
			   !p.substring(j).startsWith("TIMES\nTAMP") &&
			   !p.substring(j).startsWith("CHAR3")) {
			
			j--;
		}
		if (j > -1) {
			// remove new line characters within or following the column type
			if (p.substring(j).startsWith("BINARY(\n")) {
				p.deleteCharAt(j + 7);
			} else if (p.substring(j).startsWith("BINAR\nY(")) {
				p.deleteCharAt(j + 5);
			} else if (p.substring(j).startsWith("CHAR(\n")) {
				p.deleteCharAt(j + 5);
			} else if (p.substring(j).startsWith("INTEG\nER")) {
				p.deleteCharAt(j + 5);
			} else if (p.substring(j).startsWith("SMALL\nINT")) {
				p.deleteCharAt(j + 5);
			} else if (p.substring(j).startsWith("TIMES\nTAMP")) {
				p.deleteCharAt(j + 5);
			}
		}
		
		if (j != -1) {
			
			StringBuilder r = new StringBuilder(p.substring(j));
			
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
			
			// process the column description; remove all double blanks and
			// double new line characters as well as trailing new line 
			// characters and leading blanks and new line characters from it
			r = new StringBuilder(p.substring(i, j));
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
				if (r.charAt(k) == ' ' && r.charAt(k + 1) == ' ' ||
					r.charAt(k) == '\n' && r.charAt(k + 1) == '\n') {
						
					r.deleteCharAt(k + 1);
				} else {
					k += 1;
				}
			}
			while (r.length() > 0 && 
				   (r.charAt(0) == ' ' || r.charAt(0) == '\n')) {
				
				r.deleteCharAt(0);
			}
			q.append(r.toString()); 
		} else if (typeInfo.containsKey(recordName + "." + column)) {
			q.append(typeInfo.get(recordName + "." + column));		
			q.append('\n');
			q.append(p.substring(i));
		} else {
			q.append("N/A");		
			q.append('\n');
			q.append(p.substring(i));
		}
		//
		return q.toString();
	}
	
	private static void writeFieldData(String recordName, PrintWriter out, 
									   StringBuilder p) {		
		if (p.length() > 0) {
			out.println(processFieldData(recordName, p));
		}
	}
	
	private static void writeSetData(PrintWriter out, String[] set) {
		int written = 0;
		for (int i = 0; i < set.length; i++) {
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
	
	private static void processFile(File file, File outputFolder, 
									String documentTitle) 
		throws IOException {
		
		// derive the part of the document title that we need to suppress lines
		// containing that; ignore the release information between brackets at
		// the end of the title
		int i = documentTitle.lastIndexOf(" (");
		if (i < 0) {
			i = documentTitle.length();
		}
		String documentTitlePart = documentTitle.substring(0, i);
		
		String recordName = 
			file.getName().substring(0, file.getName().length() - 4);
		String suffix = recordName.substring(recordName.length() - 5);
		PrintWriter out = 
			new PrintWriter(new File(outputFolder, file.getName()));
		out.println(generateRecordTag(file.getName()));
		out.println();
		BufferedReader in = new BufferedReader(new FileReader(file));
		String p = in.readLine();
		String firstLineTrimmed = p.trim();
		boolean fieldMode = false;
		StringBuilder q = new StringBuilder();
		String currentFieldName = "";
		String currentLevelAndFieldName = "";
		boolean ignore = false;
		boolean descriptionOK = false;
		boolean nakedDescription = false; // release 18 manuals only
		String lastDescriptionLine = null;
		while (p != null) {
			if (!fieldMode) {
				if (p.startsWith("Description: ")) {
					if (documentTitle != null) {						
						out.println(DOCUMENT_NAME_TAG);
						out.println(documentTitle);
						out.println();						
					}
					out.println(DESCRIPTION_TAG);					
					String r;
					if (p.charAt(13) == ' ') {
						r = p.substring(14);						
					} else {
						r = p.substring(13);						
					}
					out.println(r);					
					descriptionOK = true;
					lastDescriptionLine = r; 
				} else if (p.trim().equals("Description")) {
					// release 18 only: the next line(s) will contain the 
					// description
					nakedDescription = true;
				} else if (nakedDescription) {
					// release 18 only: we expect this line to be the first line
					// that contains the naked description
					if (documentTitle != null) {						
						out.println(DOCUMENT_NAME_TAG);
						out.println(documentTitle);
						out.println();						
					}
					out.println(DESCRIPTION_TAG);
					out.println(p);
					nakedDescription = false;
					descriptionOK = true;
					lastDescriptionLine = p;
				} else if (p.startsWith("Column name Column description Data")) {						
					if (lastDescriptionLine != null && 
						!lastDescriptionLine.trim().equals("")) {
												
						out.println();
					}
					out.println(RECORD_LENGTH_TAG);
					if (dataLengths.containsKey(recordName)) {
						out.println(String.valueOf(dataLengths.get(recordName)));
					} else {
						out.println("N/A");
					}
					out.println();				
					if (ownerOf.containsKey(recordName) &&
						ownerOf.get(recordName).length > 0) {
						
						out.println(OWNER_OF_TAG);
						writeSetData(out, ownerOf.get(recordName));
						out.println();						
					}					
					if (memberOf.containsKey(recordName) &&
						memberOf.get(recordName).length > 0) {
							
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
					ignore = !p.trim().endsWith("cation");
				} else {	
					if (!firstLineTrimmed.equals(p.trim()) && 
						!p.endsWith(documentTitlePart) && 
						!p.startsWith("Appendix ") &&
						!p.endsWith(". SYSTEM Tables and SYSCA Views") &&
						!p.trim().endsWith("SQL Reference Guide") &&
						(!p.trim().equals("") || descriptionOK) &&
						!ignore) {
											
						out.println(p);
						lastDescriptionLine = p;
												
					} else if (ignore) {
						ignore = !p.trim().endsWith("cation");						
					}
				}
			} else {
				if (!firstLineTrimmed.equals(p.trim()) && 
					!p.endsWith(documentTitlePart) &&
					!p.startsWith("Column name Column description Data") &&
					!p.startsWith("Appendix ") &&
					!p.startsWith("Chapter 15: XML") && 	// the 18.5 manual has a wrong footer
					!p.endsWith(". SYSTEM Tables and SYSCA Views") && 
					!p.trim().equals("SYSCA Objects") &&
					!p.trim().equals("SYSCA Views") &&
					!ignore) {
						
					boolean fieldDescriptionBegin = isFieldDescriptionBegin(p);
					if (fieldDescriptionBegin) {
						writeFieldData(recordName, out, q);
						q = new StringBuilder();
						out.println();
						out.println(generateFieldDescriptionTag(p, suffix));
						currentFieldName = extractFieldName(p);
						currentLevelAndFieldName = 
							p.substring(0, 3) + currentFieldName;
					}					
					if (!currentFieldName.equals("") && 
						!fieldDescriptionBegin && 
						p.startsWith(currentLevelAndFieldName) &&
						p.indexOf("FILLER") == -1 ||
						p.equals("continued")) {
						
						if (p.equals("continued") ||
							p.equals(currentLevelAndFieldName)) {
							
							// ignore line; continuation indication only
						} else {
							i = p.indexOf(" continued ¦ ");
							if (i != -1) {
								q.append('\n');
								q.append(p.substring(i + 11));
							} else {
								System.out.println("ignored line for " + 
												   "field " +
											       currentFieldName + 
											       ": <" + p + ">");
							}
						}
					} else if (!isLineTableName(p) && !p.trim().equals("") &&
							   p.indexOf("SQL Reference Guide") == -1) {
						
						if (q.length() > 0) {
							q.append('\n');
						}
						q.append(p);
					}
				} else {
					if (p.startsWith("Column name Column description Data")) {
						ignore = !p.trim().endsWith("cation");						
					} else if (ignore) {					
						ignore = !p.trim().endsWith("cation");
					}	
				}
			}
			p = in.readLine();
		}
		writeFieldData(recordName, out, q);
		in.close();
		out.flush();
		out.close();		
	}

	public static void performTask(File inputFolder, File outputFolder,
								   String documentTitle)
		throws IOException {
		
		File[] file = inputFolder.listFiles();
		for (int i = 0; i < file.length; i++) {
			processFile(file[i], outputFolder, documentTitle);
		}
	}
}