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
package org.lh.dmlj.schema.editor.dictguide.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class Phase2Extractor {
	private static final String DESCRIPTION_TAG =
		"/* Description */ --------------------------------------------------" + 
		"------------";		
	private static final String DOCUMENT_NAME_TAG =
		"/* Document Name */ ------------------------------------------------" + 
		"------------";		
	@SuppressWarnings("unused")
	private static final String DOCUMENT_ID_TAG =
		"/* Document ID */ --------------------------------------------------" + 
		"------------";		
	private static final String ESTABLISHED_BY_TAG =
		"/* Established by */ -----------------------------------------------" + 
		"------------";	
	private static final String LOCATION_MODE_TAG =
		"/* Location mode */ ------------------------------------------------" + 
		"------------";		
	private static final String MEMBER_OF_TAG =
		"/* Member of */ ----------------------------------------------------" + 
		"------------";
	private static final String OWNER_OF_TAG =
		"/* Owner of */ -----------------------------------------------------" + 
		"------------";
	private static final String RECORD_LENGTH_TAG =
		"/* Record length */ ------------------------------------------------" + 
		"------------";
	private static final String WITHIN_AREA_TAG =
		"/* Within area */ --------------------------------------------------" + 
		"------------";		
	
	private static String extractFieldName(String p) {
		
		// look for the first none-blank character after the level number; the
		// level number is not always there; take care of new line characters
		// preceding the field name as well
		int i = p.charAt(2) == ' ' ? 3 : 0;
		while (p.charAt(i) == ' ' || p.charAt(i) == '\n') {
			i++;
		}		
		
		// build the field name, ignoring any blanks and new line characters;
		// consider hyphens as continuation characters
		StringBuilder q = new StringBuilder();
		while (i < p.length() && 
			   (p.charAt(i) != ' ' && p.charAt(i) != '\n' ||
			    i > 0 && (p.charAt(i) == ' ') && p.charAt(i - 1) == '-' ||
			    i > 0 && (p.charAt(i) == '\n') && p.charAt(i - 1) == '-' ||
			    i > 1 && (p.charAt(i) == ' ') && p.charAt(i - 1) == ' ' &&
			    p.charAt(i - 2) == '-')) {
			
			if (p.charAt(i) != ' ' && p.charAt(i) != '\'') {
				q.append(p.charAt(i));
			}
			i += 1;
		}
		return q.toString();
		
	}
	
	private static String generateFieldDescriptionTag(String p) {
		StringBuilder q = new StringBuilder("/* Field ");
		String fieldName = extractFieldName(p);
		q.append(fieldName);
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
	
	private static boolean isFieldDescriptionBegin(String p, 
												   String currentFieldName) {
		
		// remove leading blanks before evaluating
		StringBuilder q = new StringBuilder(p);
		while (q.length() > 0 && q.charAt(0) == ' ') {
			q.deleteCharAt(0);
		}
		
		// some lines contain only a field level number; most, but not all of
		// them indicate the true begin of a next field - we consider it the
		// beginning of a new field here but will later on decide wheter this
		// decision was correct
		if (p.trim().length() == 2 && isLevelNumber(p.trim())) {
			return true;
		}
		
		if (q.length() > 4 && isLevelNumber(q.substring(0, 2)) && 
			 // there is usually only 1 blank between the level number and field
			 // name
			(q.charAt(2) == ' ' && q.charAt(3) >= 'A' &&	q.charAt(3) <= 'Z' ||
			 // but sometimes there are 2 blanks...
			 q.charAt(2) == ' ' && q.charAt(3) == ' ' && q.charAt(4) >= 'A' &&
			 q.charAt(4) <= 'Z' ||
			 // ...and sometimes 4 --> provide a more generic MO for this
			 q.charAt(2) == ' ' && q.charAt(3) == ' ' && q.charAt(4) == ' ' 
			 && q.charAt(5) == ' ' && q.charAt(6) >= 'A' && q.charAt(7) <= 'Z') &&
			 //
			!q.equals("88 CONDITION-NAME (COBOL level-88") &&
			!q.equals("02 AFTER") &&
			!q.toString().trim().equals("10 TIMES")) {
			
			if (q.indexOf("FILLER") == -1) {
				return !currentFieldName.equals(extractFieldName(q.toString()));
			} else {
				return true;
			}
		}
		
		// for OOAK-012, the fields after EOF-MARKER-012 are missing a level
		// number in (at least) the r16, r17 and release 18 manuals; for
		// CVGDEFS-142 (r17), the CVG-SQLTAB-GROUP1 and CVG-TCPTAB-GROUP1 fields
		// (>= r17) are missing a level number as well  		
		String r = q.toString();
		return r.startsWith("INTERRUPT-COUNT-012") ||
			   r.startsWith("MSGS-LOC-012") ||
			   r.startsWith("USER-OVERRIDE-012") ||
			   r.startsWith("ALT-EOS-012") ||
			   r.startsWith("CVG-SQLTAB-GROUP1") ||
			   r.startsWith("CVG-TCPTAB-GROUP1");
	}
	
	private static boolean isLevelNumber(String p) {
		if (p.length() > 1 && (p.charAt(0) == '+' || p.charAt(0) == '-')) {
			return false;
		}
		try {
			if (p.length() == 2 && p.indexOf(' ') == -1) {
				int level = Integer.parseInt(p);
				if ((level > 1 && level < 50) || level == 66 || level == 77 || 				
					 level == 78 || level == 88) {
					
					return true;
				}			
			}
		} catch (NumberFormatException e) {
		}		
		return false;
	}	
	
	private static String processFieldData(StringBuilder p) {				
		
		// remove leading spaces
		while (p.charAt(0) == ' ') {
			p.deleteCharAt(0);
		}
		
		// can't do anything if less than 4 characters or p doesn't start
		// with the level-number followed by a space; for OOAK-012, the fields 
		// after EOF-MARKER-012 are missing a level number in (at least) the 
		// r16, r17 and release 18 manuals; for CVGDEFS-142 (r17), the 
		// CVG-SQLTAB-GROUP1 and CVG-TCPTAB-GROUP1 fields (>= r17) are missing a 
		// level number as well				
		if ((p.length() < 4 || !isLevelNumber(p.substring(0, 2)) ||
			 p.charAt(2) != ' ') &&
			!(p.toString().startsWith("INTERRUPT-COUNT-012") ||
			  p.toString().startsWith("MSGS-LOC-012") ||
			  p.toString().startsWith("USER-OVERRIDE-012") ||
			  p.toString().startsWith("ALT-EOS-012") ||
			  p.toString().startsWith("CVG-SQLTAB-GROUP1") ||
			  p.toString().startsWith("CVG-TCPTAB-GROUP1"))) {
			
			return p.toString();
		}				
		
		// create a copy of the StringBuilder to hold the result...
		StringBuilder q = new StringBuilder(p.toString());				
		
		// keep only level-number and field-name on first line, keep only 1
		// blank between level number and field name...
		int i = 3;
		while (q.charAt(i) == ' ') {
			i += 1;
		}
		i = q.indexOf(" ", i);
		int j = 3;
		while (q.charAt(j) == '\n') {
			j += 1;
		}
		j = q.indexOf("\n", j);
		if (i == -1 && j == -1) {
			return q.toString();
		}
		if (i != -1 && j != -1) {
			i = Math.min(i,  j);
		} else if (j != -1) {
			i = j;					
		}
		q.setCharAt(i, '\n');
		j = q.charAt(2) == ' ' ? 3 : 4;
		while (j < i) {
			if (q.charAt(j) == ' ' || q.charAt(j) == '\n') {
				q.deleteCharAt(j);
				i -= 1;
			} else {
				j += 1;
			}
		}
		while ((i + 1) < q.length() && 
			   (q.charAt(i + 1) == ' ' || q.charAt(i + 1) == '\n')) {
			
			q.deleteCharAt(i + 1);
		}
		
		// put the picture, usage, occurs and redefines definition on the 
		// second line (replace any unwanted new line characters by spaces)
		if (q.substring(0, 3).equals("88 ")) {
			// condition name - there will not (always) be a third (and 
			// subsequent) line(s)			
			for (j = q.indexOf("COND") + 4; j < q.length(); j++) {
				if (q.charAt(j) == '\n') {					
					q.setCharAt(j, ' ');					
				}
			}
			j = q.indexOf("'");
			if (j > -1) {
				j = q.indexOf("' ", j + 1);
				if (j > -1) {
					q.setCharAt(j + 1, '\n');
				} else {
					j = q.indexOf("  '", j + 1);
					if (j > -1) {
						q.deleteCharAt(j);
					}
				}
			} else {
				j = q.indexOf("VALUE  ");
				if (j > -1) {
					q.deleteCharAt(j + 6);
					j = q.indexOf(" ", j + 6);
					if (j > -1) {
						q.setCharAt(j, '\n');
					}
				} else {				
					j = q.indexOf("VALUE ");
					if (j > -1) {
						j = q.indexOf(" ", j + 6);
						if (j > -1) {
							q.setCharAt(j, '\n');
						}
					}
				}
			}
		} else {
			// any other element
			String[] usage = 
				new String[] {"BIT", 
							  "COMP SYNC", "COMP\nSYNC", "COMP  SYNC",
							  "COMP-1",
							  "COMP-3",
							  "DISPLAY"
							  };			
			int lowest = Integer.MAX_VALUE;
			for (int k = 0; k < usage.length; k++) {
				j = q.indexOf(usage[k] + " ", i);
				if (j == -1) {
					j = q.indexOf(usage[k] + "\n", i);
					if (j == -1) {
						if (q.toString().endsWith(usage[k])) {
							j = q.length() - usage[k].length();
						}
					}
				} else {
					int m = q.indexOf(usage[k] + "\n", i);
					if (m > -1 && m < j) {
						j = m;
					}
				}
				if (j > -1 && j < lowest) {
					lowest = j + usage[k].length() + 1;
				}
			}
			if (lowest < Integer.MAX_VALUE) {				
				int m;
				if (q.indexOf("TIMES ", lowest) > -1) {
					m = q.indexOf("TIMES ") + 6; 
				} else if (q.indexOf("TIMES\n", lowest) > -1) {
					m = q.indexOf("TIMES\n") + 6; 
				} else if (q.toString().endsWith("TIMES")) {
					m = q.length() + 1;
				} else {
					m = lowest;
				}
				if (m < q.length() - 9 && 
					q.substring(m, m + 9).equals("REDEFINES") || 
					m < q.length() - 10 && 
					q.substring(m, m + 10).equals("\nREDEFINES")) {					
					
					// the element has a REDEFINES clause, go catch the 
					// redefined element
					if (q.substring(m, m + 9).equals("REDEFINES")) {
						j = m + 10;
					} else {
						j = m + 11;
					}
					m = j;					
					while (j < q.length() && 
						   (q.charAt(j) != ' ' && q.charAt(j) != '\n' || 
						    q.charAt(j - 1) == '-' || 
						    q.charAt(j - 2) == '-' && q.charAt(j - 1) == ' ' &&
						    (q.charAt(j) == '\n' || q.charAt(j) == ' ') ||
						    q.charAt(j - 3) == '-' && q.charAt(j - 2) == ' ' &&
						    q.charAt(j - 1) == ' ' && 
						    (q.charAt(j) == '\n' || q.charAt(j) == ' ') ||
						    (j + 1) < q.length() && q.charAt(j + 1) == '-' ||
						    j == m && q.charAt(j) == '\n')) {
						
						j += 1;
					}
					// remove any spaces and new line characters between the 
					// start and end of the REDEFINES clause 
					while (m < j) {
						if (q.charAt(m) == ' ' || q.charAt(m) == '\n') {
							q.deleteCharAt(m);
							j -= 1;
						} else {
							m += 1;
						}
					}
				} else if (m < q.length() - 12 && 
						   (q.substring(m, m + 12).equals("DEPENDING ON") ||
							q.substring(m, m + 12).equals("DEPENDING\nON") ||
							q.substring(m, m + 13).equals("\nDEPENDING ON"))) {
					
					// the element has an OCCURS DEPENDING ON clause
					if (q.substring(m, m + 13).equals("\nDEPENDING ON")) {
						j = m + 14;
					} else {
						j = m + 13;
					}
					m = j;
					while (j < q.length() && 
						   q.charAt(j) != ' ' && q.charAt(j) != '\n' ||
						   q.charAt(j - 1) == '-' ||
						   q.charAt(j - 2) == '-' && q.charAt(j - 1) == ' ' &&
						   q.charAt(j) == '\n' ||
						   j == m && q.charAt(j) == '\n') {
						
						j += 1;
					}	
					while (m < j) {
						if (q.charAt(m) == ' ' || q.charAt(m) == '\n') {
							q.deleteCharAt(m);
							j -= 1;
						} else {
							m += 1;
						}
					}
				} else if (m < q.length() - 5 && 
						   q.substring(m, m + 5).equals("VALUE") ||
						   m < q.length() - 6 &&
						   (q.substring(m, m + 6).equals("\nVALUE") ||						   
						    q.substring(m, m + 6).equals(" VALUE"))) {
					
					// the element has a VALUE clause; make sure the VALUE 
					// clause will be on the same line as the PICTURE and USAGE					
					j = q.indexOf("VALUE", m) + 5;
					boolean stringValueOpen = false;
					while (stringValueOpen || j < q.length() && 
						   (q.charAt(j) == ' ' || q.charAt(j) == '\n' || 
						    q.charAt(j) == '\'')) {
						
						if (q.charAt(j) == '\'') {
							stringValueOpen = !stringValueOpen;
						}
						j += 1;						
					}
					j -= 1;					
				} else {
					j = m - 1;
				}
				// insert a new line character after the complete picture and
				// usage description (it might be there already)
				if (j < q.length()) {
					q.setCharAt(j, '\n');
				}
				// remove any new line characters as well as replace any white 
				// space with a single blank in the picture and usage 
				// description while making sure that the variable 'j' keeps 
				// pointing to the new line character that preceeds the field's 
				// description
				for (m = i + 1; m < j; m++) {
					if (q.charAt(m) == '\n') {
						q.setCharAt(m, ' ');						
					}
				}
				while (q.charAt(i + 1) == ' ') {
					q.deleteCharAt(i + 1);
					j--;
				}
				boolean openStringValue = false;	
				m = i + 1;
				while (m < j) {
					if (q.charAt(m) == '\'') {
						openStringValue = !openStringValue;
						m += 1;
					} else if (!openStringValue && (m + 1) < j && 
							   q.charAt(m) == ' ' && q.charAt(m + 1) == ' ') {
						
						q.deleteCharAt(m);
						j--;
					} else {
						m+= 1;
					}
				}
				
				// remove any leading blanks and new line characters from the 
				// field's description
				while ((j + 1) < q.length() && 
					   (q.charAt(j + 1) == '\n' || q.charAt(j + 1) == ' ')) {
					
					q.deleteCharAt(j + 1);
				}
				// in record ATTRNEST-132, the last element in the record is a 
				// FILLER; in the manuals, additional information, that is not
				// related to that FILLER, is supplied, let's go strip that 
				// information from the FILLER information
				if (q.toString().startsWith("02 FILLER\nX(1) DISPLAY") && 
					(j + 1) < q.length() &&
					q.substring(j + 1).startsWith("Sample dictionary structu" +
												  "re for a user-defined " +
												  "relational key")) {
					q.setLength(j);
				}
				// make sure no more than 1 blank line ever exists in the field
				// description
				m = j + 1;
				while (m + 1 < q.length()) {
					if (q.charAt(m) == '\n' && 
						(q.charAt(m + 1) == '\n' || q.charAt(m + 1) == ' ')) {
						
						q.deleteCharAt(m + 1);
					} else {
						m += 1;
					}
				}
			}
		}
		
		// the element description, if applicable, starts on the third line; 
		// we're done
		return q.toString();		
	}
	
	private static void processFile(File file, File outputFolder, 
									String documentTitle) 
		throws IOException {		
		
		// derive the part of the document title that we need to suppress lines
		// containing that and which probably contains "Dictionary Structure 
		// Reference Guide"; ignore the release information between brackets at
		// the end of the title
		int i = documentTitle.lastIndexOf(" (");
		if (i < 0) {
			i = documentTitle.length();
		}
		String documentTitlePart = 
			documentTitle.indexOf("Dictionary") > 0 ?
			documentTitle.substring(documentTitle.indexOf("Dictionary"), i) :
			documentTitle.substring(0, i);		
		
		File outputFile = new File(outputFolder, file.getName());
		PrintWriter out = new PrintWriter(outputFile);
		out.println(generateRecordTag(file.getName()));
		out.println();
		BufferedReader in = new BufferedReader(new FileReader(file)); 
		String p = in.readLine();
		String firstLineTrimmed = p.trim();
		boolean fieldMode = false;
		StringBuilder q = new StringBuilder();
		String currentFieldName = "";
		String currentLevelAndFieldName = "";
		boolean firstSkipped = false;
		boolean secondSkipped = true;			// not applicable for release 18
		boolean descriptionOK = false;
		boolean nakedRecordLengthLine = false;  // release 18 manuals only
		boolean nakedEstablishedByLine = false; // release 18 manuals only
		boolean nakedOwnerOfLine = false; 	    // release 18 manuals only
		boolean nakedMemberOfLine = false; 	    // release 18 manuals only
		boolean nakedLocationMode = false; 	    // release 18 manuals only
		boolean nakedWithinArea = false; 	    // release 18 manuals only
		int descriptionLineCount = 0;
		boolean recordLengthOK = false;
		boolean applyingR19FieldNameSplitOverTwoLinesHack = false;
		boolean withinAreaTagWritten = false;
		while (p != null) {			
			if (!fieldMode) {
				if ("DDLDML".equals(p) && file.getName().startsWith("OOAKEXT-078") && !withinAreaTagWritten) {
					out.println();
					out.println(WITHIN_AREA_TAG);
					out.println(p);
					withinAreaTagWritten = true;
				} else if (!firstSkipped) {
					// first line example :  "4.2 ACCESS-045" (r17)
					firstSkipped = true;
					if (p.charAt(1) == '.' && 
						(p.charAt(3) == ' ' || p.charAt(4) == ' ' ||
						 p.charAt(5) == ' ')) {
						
						// this is probably a manual prior to release 18, so we
						// can (must) skip the second line
						secondSkipped = false;
					}
				} else if (!secondSkipped) {
					// second line example :  "4.2 ACCESS-045" (r17); for 
					// release 18, we're facing the first line of the 
					// description
					secondSkipped = true;					
				} else if (!descriptionOK) {
					descriptionOK = true;
					if (documentTitle != null) {						
						out.println(DOCUMENT_NAME_TAG);
						out.println(documentTitle);
						out.println();
					}					
					out.println(DESCRIPTION_TAG);
					if (p.startsWith("Description: ")) {
						if (p.charAt(13) == ' ' ) {
							out.println(p.substring(14));
						} else {
							out.println(p.substring(13));
						}
					} else {
						out.println(p);						
					}
					descriptionLineCount += 1;
				} else if (p.startsWith("Record length: ")) {					
					if (descriptionLineCount == 0) {
						in.close();
						throw new RuntimeException("logic error: no description " +
												   "written");
					} else if (descriptionLineCount == 1) {
						out.println();						
					}
					out.println(RECORD_LENGTH_TAG);
					out.println(p.substring(15));
					out.println();
					recordLengthOK = true;
				} else if (p.trim().equals("Record length") || p.trim().equals("Record Length")) {
					// release 18 only: the next line will hold the record length
					nakedRecordLengthLine = true;
					recordLengthOK = true;					
				} else if (nakedRecordLengthLine) {
					// release 18 only: we expect this line to contain the naked
					// record length
					if (descriptionLineCount == 0) {
						in.close();
						throw new RuntimeException("logic error: no description written");
					} else if (descriptionLineCount == 1) {
						out.println();						
					}					
					try {
						Integer.parseInt(p.trim());
					} catch (NumberFormatException e) {
						System.out.println(file.getName() + ": expected naked" +
										   " record length here - <" + p + ">");
					}
					out.println(RECORD_LENGTH_TAG);					
					out.println(p.trim());
					out.println();
					nakedRecordLengthLine = false;
				} else if (p.startsWith("Established by: ")) {
					out.println(ESTABLISHED_BY_TAG);
					if (p.charAt(16) == ' ') {
						out.println(p.substring(17));
					} else {
						out.println(p.substring(16));
					}
				} else if (p.trim().equals("Established by")) {
					// release 18 only: the next line will hold the established
					// by data
					nakedEstablishedByLine = true;
				} else if (nakedEstablishedByLine) {
					// release 18 only: we expect this line to contain the naked
					// established by data
					out.println(ESTABLISHED_BY_TAG);
					out.println(p);
					nakedEstablishedByLine = false;
				} else if (p.startsWith("Owner of: ")) {
					out.println();
					out.println(OWNER_OF_TAG);
					if (p.charAt(10) == ' ') {
						out.println(p.substring(11));
					} else {
						out.println(p.substring(10));
					}
				} else if (p.trim().equals("Owner of")) {
					// release 18 only: the next line(s) will hold the owner of
					// data
					nakedOwnerOfLine = true;
				} else if (nakedOwnerOfLine) {
					// release 18 only: we expect this line to be the first line
					// with naked owner of data
					out.println();
					out.println(OWNER_OF_TAG);
					out.println(p);
					nakedOwnerOfLine = false;
				} else if (p.startsWith("Member of: ")) {
					out.println();
					out.println(MEMBER_OF_TAG);
					if (p.charAt(11) == ' ') {
						out.println(p.substring(12));
					} else {
						out.println(p.substring(11));
					}
				} else if (p.trim().equals("Member of")) {
					// release 18 only: the next line(s) will hold the member of
					// data
					nakedMemberOfLine = true;
				} else if (nakedMemberOfLine) {
					// release 18 only: we expect this line to be the first line
					// with naked member of data
					out.println();
					out.println(MEMBER_OF_TAG);
					out.println(p);
					nakedMemberOfLine = false;
				} else if (p.startsWith("Location mode: ") ||	// <= r17
						   p.startsWith("Location mode ")&&		// >= release 18
						   !p.trim().equals("Location mode")) {	// >= release 18
					
					out.println();
					out.println(LOCATION_MODE_TAG);	
					// in at least 1 case (LOADHDR-156, r16), the Within area 
					// specification will be on the same line as the location 
					// mode, so deal with that situation...
					i = p.indexOf("-Within area: ");
					if (i < 0) {
						// no within area information on same line
						if (p.charAt(13) == ':') {
							// pre release 18 manuals
							if (p.charAt(15) == ' ') {
								out.println(p.substring(16));
							} else {
								out.println(p.substring(15));
							}
						} else {
							// release 18 manuals
							out.println(p.substring(14));
						}
					} else {
						// within area information is on same line
						if (p.charAt(15) == ' ') {
							out.println(p.substring(16, i));
						} else {
							out.println(p.substring(15, i));
						}
						out.println();
						out.println(WITHIN_AREA_TAG);
						out.println(p.substring(i + 14));
						withinAreaTagWritten = true;
					}
				} else if (p.trim().equals("Location mode")) {	// >= release 18
					// release 18 only: the next line(s) will hold the location
					// mode data
					nakedLocationMode = true;
				} else if (nakedLocationMode) {
					// release 18 only: we expect this line to be a line with
					// naked location mode data; if it's just a new line 
					// character that we get, ignore it
					if (p.length() > 1 || p.charAt(0) != '\n') {
						out.println();
						out.println(LOCATION_MODE_TAG);
						out.println(p);
						nakedLocationMode = false;
					}
				} else if (p.startsWith("Within area: ") ||		// <= r17
						   p.startsWith("Within area ") &&		// >= release 18
						   !p.trim().equals("Within area")) {	// >= release 18
					
					out.println();
					out.println(WITHIN_AREA_TAG);
					if (p.charAt(11) == ':') {
						// pre release 18 manuals
						out.println(p.substring(13));
					} else {
						// release 18 manuals
						out.println(p.substring(12));
					}
					withinAreaTagWritten = true;
				} else if (p.trim().equals("Within area")) {	// >= release 18
					// release 18 only: the next line(s) will hold the within
					// area data
					nakedWithinArea = true;
				} else if (nakedWithinArea) {
					// release 18 only: we expect this line to be a line with
					// naked within area data; if it's just a new line 
					// character that we get, ignore it
					if (p.length() > 1 || p.charAt(0) != '\n') {
						out.println();
						out.println(WITHIN_AREA_TAG);
						out.println(p);
						withinAreaTagWritten = true;
						nakedWithinArea = false;
					}
				} else if (p.startsWith("Field Picture Description")) {
					fieldMode = true;
				} else {
					if (!recordLengthOK) {
						descriptionLineCount += 1;
					}					
					if (!firstLineTrimmed.equals(p.trim()) && 
						!p.trim().endsWith(documentTitlePart) &&
						!p.startsWith(documentTitle) &&
						!p.startsWith(documentTitlePart) &&
						p.indexOf("Record and Element Descriptions") < 0) {
						
						if (!p.trim().equals("")) {
							//suppress blank lines
							out.println(p);
						}
						if (p.trim().endsWith(".") ||
							p.trim().endsWith(".\"")) {
							
							out.println();
						}
					}
				}
			} else {
				if (applyingR19FieldNameSplitOverTwoLinesHack) {
					currentFieldName = currentFieldName + p;
					currentLevelAndFieldName = currentLevelAndFieldName + p;
					out.println(generateFieldDescriptionTag(currentFieldName));
					if (q.length() == 0) {
						q.append(currentLevelAndFieldName);
					} else {
						q.append(p);
					}
					applyingR19FieldNameSplitOverTwoLinesHack = false;
				} else if (!firstLineTrimmed.equals(p.trim()) && 
					!p.trim().endsWith(documentTitlePart) &&
					!p.startsWith("Field Picture Description") &&
					!p.startsWith("Chapter ") &&
					!p.endsWith(". Record and Element Descriptions") &&
					!p.startsWith(documentTitle) &&
					!p.startsWith(documentTitlePart)) {
						
					boolean fieldDescriptionBegin = 
						isFieldDescriptionBegin(p, currentFieldName);
					if (fieldDescriptionBegin) {
						if (p.trim().length() == 2 && isLevelNumber(p.trim())) {
							// the line only contains a level number; concatenate
							// the next line, which should really be there...
							String r = in.readLine();
							if (r != null) {
								p = p + " " + r;
							}
						}
						// because we may have concatenated 2 lines, the field 
						// description begin is not necessary a true field
						// description begin, so make sure we're only flushing
						// the current field when the field is complete
						if (!(p.trim().startsWith("88 ") &&
							 p.indexOf("CONDITION-NAME") > -1  ||
							 p.trim().startsWith("02") &&
							 p.trim().endsWith("AFTER"))) {
																					
							writeFieldData(out, q);						
							q = new StringBuilder();
							out.println();
														
							if (applyR19FieldNameSplitOverTwoLinesHack(p)) {
								applyingR19FieldNameSplitOverTwoLinesHack = true;
							} else {	
								out.println(generateFieldDescriptionTag(p));
							}
							currentFieldName = extractFieldName(p);
							if (p.charAt(2) == ' ') {
								// level number present
								currentLevelAndFieldName = p.substring(0, 3) + currentFieldName;
							} else {
								// no level number present
								currentLevelAndFieldName = currentFieldName;							
							}
						}
					}	
					if (p.length() > 3 && p.charAt(0) == '0' && 
						p.charAt(2) == ' ' && p.indexOf("-  ") > -1 &&
						p.indexOf(" - ") == -1) {								
						
						// strip double blanks after hyphens in the field name
						q.append(p.replaceAll("-  ", "-"));
					} else if (p.length() > 3 && p.charAt(0) == '0' && p.charAt(2) == ' ' && p.contains("- ") && !p.contains(" - ") && !p.contains(" -- ")) {
						// strip blanks after hyphens in the field name
						q.append(p.replaceAll("- ", "-"));
					} else {
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
								if (i == -1) {
									i = p.indexOf(" continued ? ");
								}
								if (i == -1) {
									i = p.indexOf(" continued ");
								}
								if (i != -1) {
									q.append('\n');
									q.append(p.substring(i + 11));
								} else {
									System.out.println("ignored line for field " + currentFieldName + ": <" + p + ">");
								}
							}
						} else {
							if (q.length() > 0) {
								q.append('\n');
							}
							if (!applyingR19FieldNameSplitOverTwoLinesHack) {
								q.append(p);
							}
						}
					}
				}
			}
			p = in.readLine();
		}		
		writeFieldData(out, q);
		in.close();
		out.flush();
		out.close();		
	}
	
	private static boolean applyR19FieldNameSplitOverTwoLinesHack(String line) {
		return line.equals("03 CVG-OLQ-MAX-REP-") || line.equals("03 CVG-OLQ-CONTINUATION-") || 
			   line.equals("02 CVG-PREDEF-RUNUNIT-") || line.equals("04 CVG-TCPTAB-MAXPTASK-") || 
			   line.equals("02 STAT-MEM-AVG- CLUSTER-");
	}
	
	public static void performTask(File inputFolder, File outputFolder,
								   String documentTitle) 
		throws IOException {
		
		File[] file = inputFolder.listFiles();
		for (int i = 0; i < file.length; i++) {
			processFile(file[i], outputFolder, documentTitle);
		}
	}
	
	private static void removeTrailingChars(StringBuilder p) {
		while (p.length() > 0 &&
			   (p.charAt(p.length() - 1) == ' ' ||
			    p.charAt(p.length() - 1) == '\n')) {
				
			p.setLength(p.length() - 1);
		}
	}

	private static void writeFieldData(PrintWriter out, StringBuilder p) {		
		removeTrailingChars(p);
		if (p.length() > 0) {
			out.println(processFieldData(p));
		}
	}
}
