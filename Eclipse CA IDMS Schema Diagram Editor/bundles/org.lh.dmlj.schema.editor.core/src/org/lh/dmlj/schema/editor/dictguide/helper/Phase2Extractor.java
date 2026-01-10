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

import org.lh.dmlj.schema.editor.Plugin;

public final class Phase2Extractor {
	private static final String DESCRIPTION_TAG = "/* Description */ --------------------------------------------------------------";
	private static final String DOCUMENT_NAME_TAG = "/* Document Name */ ------------------------------------------------------------";
	private static final String ESTABLISHED_BY_TAG = "/* Established by */ -----------------------------------------------------------";	
	private static final String LOCATION_MODE_TAG = "/* Location mode */ ------------------------------------------------------------";
	private static final String MEMBER_OF_TAG = "/* Member of */ ----------------------------------------------------------------";
	private static final String OWNER_OF_TAG = "/* Owner of */ -----------------------------------------------------------------";
	private static final String RECORD_LENGTH_TAG = "/* Record length */ ------------------------------------------------------------";
	private static final String WITHIN_AREA_TAG = "/* Within area */ --------------------------------------------------------------";
	
	private static String extractFieldName(String p) {
		// look for the first none-blank character after the level number; the level number is not always
		// there; take care of new line characters preceding the field name as well
		var i = p.charAt(2) == ' ' ? 3 : 0;
		while (p.charAt(i) == ' ' || p.charAt(i) == '\n') {
			i++;
		}		
		
		// build the field name, ignoring any blanks and new line characters; consider hyphens as continuation
		// characters
		var q = new StringBuilder();
		while (i < p.length() && (p.charAt(i) != ' ' && p.charAt(i) != '\n' ||
			   i > 0 && (p.charAt(i) == ' ') && p.charAt(i - 1) == '-' ||
			   i > 0 && (p.charAt(i) == '\n') && p.charAt(i - 1) == '-' ||
			   i > 1 && (p.charAt(i) == ' ') && p.charAt(i - 1) == ' ' && p.charAt(i - 2) == '-')) {
			
			if (p.charAt(i) != ' ' && p.charAt(i) != '\'') {
				q.append(p.charAt(i));
			}
			i += 1;
		}
		return q.toString();
	}
	
	private static String generateFieldDescriptionTag(String p) {
		var q = new StringBuilder("/* Field ");
		var fieldName = extractFieldName(p);
		q.append(fieldName);
		q.append(" */ ");
		while (q.length() < 80) {
			q.append("-");
		}
		return q.toString(); 
	}
	
	private static String generateRecordTag(String fileName) {
		var p = new StringBuilder("/* ");
		p.append(fileName.substring(0, fileName.length() - 4));
		p.append(" */ ");
		while (p.length() < 80) {
			p.append("-");
		}
		return p.toString();
	}
	
	private static boolean isFieldDescriptionBegin(String p, String currentFieldName) {
		// remove leading blanks before evaluating
		var q = new StringBuilder(p);
		while (!q.isEmpty() && q.charAt(0) == ' ') {
			q.deleteCharAt(0);
		}
		
		// some lines contain only a field level number; most, but not all of them indicate the true begin of a
		// next field - we consider it the beginning of a new field here but will later on decide wheter this
		// decision was correct
		if (p.trim().length() == 2 && isLevelNumber(p.trim())) {
			return true;
		}
		
		if (q.length() > 4 && isLevelNumber(q.substring(0, 2)) && 
			 // there is usually only 1 blank between the level number and field name
			(q.charAt(2) == ' ' && q.charAt(3) >= 'A' &&	q.charAt(3) <= 'Z' ||
			 // but sometimes there are 2 blanks...
			 q.charAt(2) == ' ' && q.charAt(3) == ' ' && q.charAt(4) >= 'A' && q.charAt(4) <= 'Z' ||
			 // ...and sometimes 4 --> provide a more generic MO for this
			 q.charAt(2) == ' ' && q.charAt(3) == ' ' && q.charAt(4) == ' ' && q.charAt(5) == ' ' && q.charAt(6) >= 'A' && q.charAt(7) <= 'Z') &&
			!q.toString().equals("88 CONDITION-NAME (COBOL level-88") && !q.toString().equals("02 AFTER") && !q.toString().trim().equals("10 TIMES")) {
			
			if (q.indexOf("FILLER") == -1) {
				return !currentFieldName.equals(extractFieldName(q.toString()));
			} else {
				return true;
			}
		}
		
		// for OOAK-012, the fields after EOF-MARKER-012 are missing a level number in (at least) the r16, r17
		// and release 18 manuals; for CVGDEFS-142 (r17), the CVG-SQLTAB-GROUP1 and CVG-TCPTAB-GROUP1 fields
		// (>= r17) are missing a level number as well
		String r = q.toString();
		return r.startsWith("INTERRUPT-COUNT-012") || r.startsWith("MSGS-LOC-012") || r.startsWith("USER-OVERRIDE-012") ||
				r.startsWith("ALT-EOS-012") || r.startsWith("CVG-SQLTAB-GROUP1") || r.startsWith("CVG-TCPTAB-GROUP1");
	}
	
	private static boolean isLevelNumber(String p) {
		if (p.length() > 1 && (p.charAt(0) == '+' || p.charAt(0) == '-')) {
			return false;
		}
		try {
			if (p.length() == 2 && p.indexOf(' ') == -1) {
				var level = Integer.parseInt(p);
				if ((level > 1 && level < 50) || level == 66 || level == 77 || level == 78 || level == 88) {
					return true;
				}			
			}
		} catch (NumberFormatException e) {
			Plugin.getDefault().getLog().warn("NumberFormatException" + e.getMessage());
		}		
		return false;
	}	
	
	private static String processFieldData(StringBuilder p) {			
		// remove leading spaces
		while (p.charAt(0) == ' ') {
			p.deleteCharAt(0);
		}
		
		// can't do anything if less than 4 characters or p doesn't start with the level-number followed by a
		// space; for OOAK-012, the fields after EOF-MARKER-012 are missing a level number in (at least) the r16,
		// r17 and release 18 manuals; for CVGDEFS-142 (r17), the CVG-SQLTAB-GROUP1 and CVG-TCPTAB-GROUP1 fields
		// (>= r17) are missing a level number as well
		if ((p.length() < 4 || !isLevelNumber(p.substring(0, 2)) || p.charAt(2) != ' ') &&
			!(p.toString().startsWith("INTERRUPT-COUNT-012") || p.toString().startsWith("MSGS-LOC-012") ||
			  p.toString().startsWith("USER-OVERRIDE-012") || p.toString().startsWith("ALT-EOS-012") ||
			  p.toString().startsWith("CVG-SQLTAB-GROUP1") || p.toString().startsWith("CVG-TCPTAB-GROUP1"))) {
			
			return p.toString();
		}				
				
		var result = new StringBuilder(p.toString());				
		
		// keep only level-number and field-name on first line, keep only 1 blank between level number and field name...
		var i = keepOnlyLevelNumberAndFieldBameOnFirstLine(result);
		if (i == Integer.MIN_VALUE) {
			return result.toString();
		}
		replaceUnwantedNewLineCharactersBySpaces(result, i);
		
		// the element description, if applicable, starts on the third line; we're done
		return result.toString();		
	}
	
	private static int keepOnlyLevelNumberAndFieldBameOnFirstLine(StringBuilder result) {
		// keep only level-number and field-name on first line, keep only 1 blank between level number and field name...
		var i = 3;
		while (result.charAt(i) == ' ') {
			i += 1;
		}
		i = result.indexOf(" ", i);
		var j = 3;
		while (result.charAt(j) == '\n') {
			j += 1;
		}
		j = result.indexOf("\n", j);
		if (i == -1 && j == -1) {
			return Integer.MIN_VALUE;
		}
		if (i != -1 && j != -1) {
			i = Math.min(i,  j);
		} else if (j != -1) {
			i = j;					
		}
		result.setCharAt(i, '\n');
		j = result.charAt(2) == ' ' ? 3 : 4;
		while (j < i) {
			if (result.charAt(j) == ' ' || result.charAt(j) == '\n') {
				result.deleteCharAt(j);
				i -= 1;
			} else {
				j += 1;
			}
		}
		while ((i + 1) < result.length() && (result.charAt(i + 1) == ' ' || result.charAt(i + 1) == '\n')) {
			result.deleteCharAt(i + 1);
		}
		return i;
	}
	
	private static void replaceUnwantedNewLineCharactersBySpaces(StringBuilder result, int i) {
		int j;
		if (result.substring(0, 3).equals("88 ")) {
			// condition name - there will not (always) be a third (and subsequent) line(s)			
			for (j = result.indexOf("COND") + 4; j < result.length(); j++) {
				if (result.charAt(j) == '\n') {					
					result.setCharAt(j, ' ');					
				}
			}
			j = result.indexOf("'");
			if (j > -1) {
				j = result.indexOf("' ", j + 1);
				if (j > -1) {
					result.setCharAt(j + 1, '\n');
				} else {
					j = result.indexOf("  '", j + 1);
					if (j > -1) {
						result.deleteCharAt(j);
					}
				}
			} else {
				j = result.indexOf("VALUE  ");
				if (j > -1) {
					result.deleteCharAt(j + 6);
					j = result.indexOf(" ", j + 6);
					if (j > -1) {
						result.setCharAt(j, '\n');
					}
				} else {				
					j = result.indexOf("VALUE ");
					if (j > -1) {
						j = result.indexOf(" ", j + 6);
						if (j > -1) {
							result.setCharAt(j, '\n');
						}
					}
				}
			}
		} else {
			// any other element
			var usage = new String[] { "BIT", "COMP SYNC", "COMP\nSYNC", "COMP  SYNC", "COMP-1", "COMP-3", "DISPLAY" };			
			var lowest = Integer.MAX_VALUE;
			for (var k = 0; k < usage.length; k++) {
				j = result.indexOf(usage[k] + " ", i);
				if (j == -1) {
					j = result.indexOf(usage[k] + "\n", i);
					if (j == -1 && result.toString().endsWith(usage[k])) {
						j = result.length() - usage[k].length();
					}
				} else {
					var m = result.indexOf(usage[k] + "\n", i);
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
				if (result.indexOf("TIMES ", lowest) > -1) {
					m = result.indexOf("TIMES ") + 6; 
				} else if (result.indexOf("TIMES\n", lowest) > -1) {
					m = result.indexOf("TIMES\n") + 6; 
				} else if (result.toString().endsWith("TIMES")) {
					m = result.length() + 1;
				} else {
					m = lowest;
				}
				if (m < result.length() - 9 && result.substring(m, m + 9).equals("REDEFINES") || m < result.length() - 10 && result.substring(m, m + 10).equals("\nREDEFINES")) {
					// the element has a REDEFINES clause, go catch the redefined element
					if (result.substring(m, m + 9).equals("REDEFINES")) {
						j = m + 10;
					} else {
						j = m + 11;
					}
					m = j;					
					while (j < result.length() && 
						   (result.charAt(j) != ' ' && result.charAt(j) != '\n' || result.charAt(j - 1) == '-' || 
						    result.charAt(j - 2) == '-' && result.charAt(j - 1) == ' ' && (result.charAt(j) == '\n' || result.charAt(j) == ' ') ||
						    result.charAt(j - 3) == '-' && result.charAt(j - 2) == ' ' && result.charAt(j - 1) == ' ' && (result.charAt(j) == '\n' || result.charAt(j) == ' ') ||
						    (j + 1) < result.length() && result.charAt(j + 1) == '-' || j == m && result.charAt(j) == '\n')) {
						
						j += 1;
					}
					// remove any spaces and new line characters between the start and end of the REDEFINES clause 
					while (m < j) {
						if (result.charAt(m) == ' ' || result.charAt(m) == '\n') {
							result.deleteCharAt(m);
							j -= 1;
						} else {
							m += 1;
						}
					}
				} else if (m < result.length() - 12 && (result.substring(m, m + 12).equals("DEPENDING ON") || result.substring(m, m + 12).equals("DEPENDING\nON") || result.substring(m, m + 13).equals("\nDEPENDING ON"))) {
					// the element has an OCCURS DEPENDING ON clause
					if (result.substring(m, m + 13).equals("\nDEPENDING ON")) {
						j = m + 14;
					} else {
						j = m + 13;
					}
					m = j;
					while (j < result.length() && result.charAt(j) != ' ' && result.charAt(j) != '\n' || result.charAt(j - 1) == '-' ||
						   result.charAt(j - 2) == '-' && result.charAt(j - 1) == ' ' && result.charAt(j) == '\n' || j == m && result.charAt(j) == '\n') {
						
						j += 1;
					}	
					while (m < j) {
						if (result.charAt(m) == ' ' || result.charAt(m) == '\n') {
							result.deleteCharAt(m);
							j -= 1;
						} else {
							m += 1;
						}
					}
				} else if (m < result.length() - 5 && result.substring(m, m + 5).equals("VALUE") || m < result.length() - 6 &&
						   (result.substring(m, m + 6).equals("\nVALUE") || result.substring(m, m + 6).equals(" VALUE"))) {
					
					// the element has a VALUE clause; make sure the VALUE clause will be on the same line as the PICTURE and USAGE					
					j = result.indexOf("VALUE", m) + 5;
					var stringValueOpen = false;
					while (stringValueOpen || j < result.length() && (result.charAt(j) == ' ' || result.charAt(j) == '\n' || result.charAt(j) == '\'')) {
						if (result.charAt(j) == '\'') {
							stringValueOpen = !stringValueOpen;
						}
						j += 1;						
					}
					j -= 1;					
				} else {
					j = m - 1;
				}
				// insert a new line character after the complete picture and usage description (it might be there already)
				if (j < result.length()) {
					result.setCharAt(j, '\n');
				}
				// remove any new line characters as well as replace any white space with a single blank in the
				// picture and usage description while making sure that the variable 'j' keeps pointing to the
				// new line character that preceeds the field's description
				for (m = i + 1; m < j; m++) {
					if (result.charAt(m) == '\n') {
						result.setCharAt(m, ' ');						
					}
				}
				while (result.charAt(i + 1) == ' ') {
					result.deleteCharAt(i + 1);
					j--;
				}
				var openStringValue = false;
				m = i + 1;
				while (m < j) {
					if (result.charAt(m) == '\'') {
						openStringValue = !openStringValue;
						m += 1;
					} else if (!openStringValue && (m + 1) < j && result.charAt(m) == ' ' && result.charAt(m + 1) == ' ') {
						result.deleteCharAt(m);
						j--;
					} else {
						m+= 1;
					}
				}
				
				// remove any leading blanks and new line characters from the field's description
				while ((j + 1) < result.length() && (result.charAt(j + 1) == '\n' || result.charAt(j + 1) == ' ')) {
					result.deleteCharAt(j + 1);
				}
				// in record ATTRNEST-132, the last element in the record is a FILLER; in the manuals, additional
				// information, that is not related to that FILLER, is supplied, let's go strip that information
				// from the FILLER information
				if (result.toString().startsWith("02 FILLER\nX(1) DISPLAY") && (j + 1) < result.length() &&
					result.substring(j + 1).startsWith("Sample dictionary structure for a user-defined relational key")) {
					
					result.setLength(j);
				}
				// make sure no more than 1 blank line ever exists in the field description
				m = j + 1;
				while (m + 1 < result.length()) {
					if (result.charAt(m) == '\n' && (result.charAt(m + 1) == '\n' || result.charAt(m + 1) == ' ')) {
						result.deleteCharAt(m + 1);
					} else {
						m += 1;
					}
				}
			}
		}
	}
	
	private static void processFile(File file, File outputFolder, String documentTitle) throws IOException {
		// derive the part of the document title that we need to suppress lines containing that and which
		// probably contains "Dictionary Structure Reference Guide"; ignore the release information between
		// brackets at the end of the title
		var i = documentTitle.lastIndexOf(" (");
		if (i < 0) {
			i = documentTitle.length();
		}
		var documentTitlePart = documentTitle.indexOf("Dictionary") > 0 ? documentTitle.substring(documentTitle.indexOf("Dictionary"), i) :
				documentTitle.substring(0, i);		
		
		var outputFile = new File(outputFolder, file.getName());
		try (var in = new BufferedReader(new FileReader(file)); var out = new PrintWriter(outputFile)) {
			out.println(generateRecordTag(file.getName()));
			out.println();
			var line = in.readLine();
			var firstLineTrimmed = line.trim();
			var fieldMode = false;
			var q = new StringBuilder();
			var currentFieldName = "";
			var currentLevelAndFieldName = "";
			var firstSkipped = false;
			var secondSkipped = true;			// not applicable for release 18
			var descriptionOK = false;
			var nakedRecordLengthLine = false;	// release 18 manuals only
			var nakedEstablishedByLine = false;	// release 18 manuals only
			var nakedOwnerOfLine = false; 	    // release 18 manuals only
			var nakedMemberOfLine = false; 	    // release 18 manuals only
			var nakedLocationMode = false; 	    // release 18 manuals only
			var nakedWithinArea = false; 	    // release 18 manuals only
			var descriptionLineCount = 0;
			var recordLengthOK = false;
			var applyingR19FieldNameSplitOverTwoLinesHack = false;
			var withinAreaTagWritten = false;
			while (line != null) {			
				if (!fieldMode) {
					if ("DDLDML".equals(line) && file.getName().startsWith("OOAKEXT-078") && !withinAreaTagWritten) {
						out.println();
						out.println(WITHIN_AREA_TAG);
						out.println(line);
						withinAreaTagWritten = true;
					} else if (!firstSkipped) {
						// first line example :  "4.2 ACCESS-045" (r17)
						firstSkipped = true;
						if (line.charAt(1) == '.' && (line.charAt(3) == ' ' || line.charAt(4) == ' ' || line.charAt(5) == ' ')) {
							// this is probably a manual prior to release 18, so we can (must) skip the second line
							secondSkipped = false;
						}
					} else if (!secondSkipped) {
						// second line example :  "4.2 ACCESS-045" (r17); for release 18, we're facing the first line
						// of the description
						secondSkipped = true;					
					} else if (!descriptionOK) {
						descriptionOK = true;
						if (documentTitle != null) {						
							out.println(DOCUMENT_NAME_TAG);
							out.println(documentTitle);
							out.println();
						}					
						out.println(DESCRIPTION_TAG);
						if (line.startsWith("Description: ")) {
							if (line.charAt(13) == ' ' ) {
								out.println(line.substring(14));
							} else {
								out.println(line.substring(13));
							}
						} else {
							out.println(line);						
						}
						descriptionLineCount += 1;
					} else if (line.startsWith("Record length: ")) {					
						if (descriptionLineCount == 0) {
							throw new IllegalStateException("logic error: no description written");
						} else if (descriptionLineCount == 1) {
							out.println();						
						}
						out.println(RECORD_LENGTH_TAG);
						out.println(line.substring(15));
						out.println();
						recordLengthOK = true;
					} else if (line.trim().equals("Record length") || line.trim().equals("Record Length")) {
						// release 18 only: the next line will hold the record length
						nakedRecordLengthLine = true;
						recordLengthOK = true;					
					} else if (nakedRecordLengthLine) {
						// release 18 only: we expect this line to contain the naked record length
						if (descriptionLineCount == 0) {
							throw new IllegalStateException("logic error: no description written");
						} else if (descriptionLineCount == 1) {
							out.println();						
						}					
						try {
							Integer.parseInt(line.trim());
						} catch (NumberFormatException e) {
							Plugin.getDefault().getLog().warn(file.getName() + ": expected naked record length here - <" + line + ">");
						}
						out.println(RECORD_LENGTH_TAG);					
						out.println(line.trim());
						out.println();
						nakedRecordLengthLine = false;
					} else if (line.startsWith("Established by: ")) {
						out.println(ESTABLISHED_BY_TAG);
						if (line.charAt(16) == ' ') {
							out.println(line.substring(17));
						} else {
							out.println(line.substring(16));
						}
					} else if (line.trim().equals("Established by")) {
						// release 18 only: the next line will hold the established by data
						nakedEstablishedByLine = true;
					} else if (nakedEstablishedByLine) {
						// release 18 only: we expect this line to contain the naked established by data
						out.println(ESTABLISHED_BY_TAG);
						out.println(line);
						nakedEstablishedByLine = false;
					} else if (line.startsWith("Owner of: ")) {
						out.println();
						out.println(OWNER_OF_TAG);
						if (line.charAt(10) == ' ') {
							out.println(line.substring(11));
						} else {
							out.println(line.substring(10));
						}
					} else if (line.trim().equals("Owner of")) {
						// release 18 only: the next line(s) will hold the owner of data
						nakedOwnerOfLine = true;
					} else if (nakedOwnerOfLine) {
						// release 18 only: we expect this line to be the first line with naked owner of data
						out.println();
						out.println(OWNER_OF_TAG);
						out.println(line);
						nakedOwnerOfLine = false;
					} else if (line.startsWith("Member of: ")) {
						out.println();
						out.println(MEMBER_OF_TAG);
						if (line.charAt(11) == ' ') {
							out.println(line.substring(12));
						} else {
							out.println(line.substring(11));
						}
					} else if (line.trim().equals("Member of")) {
						// release 18 only: the next line(s) will hold the member of data
						nakedMemberOfLine = true;
					} else if (nakedMemberOfLine) {
						// release 18 only: we expect this line to be the first line with naked member of data
						out.println();
						out.println(MEMBER_OF_TAG);
						out.println(line);
						nakedMemberOfLine = false;
					} else if (line.startsWith("Location mode: ") ||	// <= r17
							   line.startsWith("Location mode ")&&		// >= release 18
							   !line.trim().equals("Location mode")) {	// >= release 18
						
						out.println();
						out.println(LOCATION_MODE_TAG);	
						// in at least 1 case (LOADHDR-156, r16), the Within area specification will be on the same
						// line as the location mode, so deal with that situation...
						i = line.indexOf("-Within area: ");
						if (i < 0) {
							// no within area information on same line
							if (line.charAt(13) == ':') {
								// pre release 18 manuals
								if (line.charAt(15) == ' ') {
									out.println(line.substring(16));
								} else {
									out.println(line.substring(15));
								}
							} else {
								// release 18 manuals
								out.println(line.substring(14));
							}
						} else {
							// within area information is on same line
							if (line.charAt(15) == ' ') {
								out.println(line.substring(16, i));
							} else {
								out.println(line.substring(15, i));
							}
							out.println();
							out.println(WITHIN_AREA_TAG);
							out.println(line.substring(i + 14));
							withinAreaTagWritten = true;
						}
					} else if (line.trim().equals("Location mode")) {	// >= release 18
						// release 18 only: the next line(s) will hold the location mode data
						nakedLocationMode = true;
					} else if (nakedLocationMode) {
						// release 18 only: we expect this line to be a line with naked location mode data; if it's
						// just a new line character that we get, ignore it
						if (line.length() > 1 || line.charAt(0) != '\n') {
							out.println();
							out.println(LOCATION_MODE_TAG);
							out.println(line);
							nakedLocationMode = false;
						}
					} else if (line.startsWith("Within area: ") ||		// <= r17
							   line.startsWith("Within area ") &&		// >= release 18
							   !line.trim().equals("Within area")) {	// >= release 18
						
						out.println();
						out.println(WITHIN_AREA_TAG);
						if (line.charAt(11) == ':') {
							// pre release 18 manuals
							out.println(line.substring(13));
						} else {
							// release 18 manuals
							out.println(line.substring(12));
						}
						withinAreaTagWritten = true;
					} else if (line.trim().equals("Within area")) {	// >= release 18
						// release 18 only: the next line(s) will hold the within area data
						nakedWithinArea = true;
					} else if (nakedWithinArea) {
						// release 18 only: we expect this line to be a line with naked within area data; if it's
						// just a new line character that we get, ignore it
						if (line.length() > 1 || line.charAt(0) != '\n') {
							out.println();
							out.println(WITHIN_AREA_TAG);
							out.println(line);
							withinAreaTagWritten = true;
							nakedWithinArea = false;
						}
					} else if (line.startsWith("Field Picture Description")) {
						fieldMode = true;
					} else {
						if (!recordLengthOK) {
							descriptionLineCount += 1;
						}					
						if (!firstLineTrimmed.equals(line.trim()) && !line.trim().endsWith(documentTitlePart) &&
							!line.startsWith(documentTitle) && !line.startsWith(documentTitlePart) &&
							!line.contains("Record and Element Descriptions")) {
							
							if (!line.trim().equals("")) {
								//suppress blank lines
								out.println(line);
							}
							if (line.trim().endsWith(".") ||
								line.trim().endsWith(".\"")) {
								
								out.println();
							}
						}
					}
				} else {
					if (applyingR19FieldNameSplitOverTwoLinesHack) {
						var tmpCurrentFieldName = new StringBuilder(currentFieldName);
						tmpCurrentFieldName.append(line);
						currentFieldName = tmpCurrentFieldName.toString();
						currentLevelAndFieldName = currentLevelAndFieldName.concat(line);
						out.println(generateFieldDescriptionTag(currentFieldName));
						if (q.isEmpty()) {
							q.append(currentLevelAndFieldName);
						} else {
							q.append(line);
						}
						applyingR19FieldNameSplitOverTwoLinesHack = false;
					} else if (!firstLineTrimmed.equals(line.trim()) && !line.trim().endsWith(documentTitlePart) &&
							   !line.startsWith("Field Picture Description") && !line.startsWith("Chapter ") &&
							   !line.endsWith(". Record and Element Descriptions") && !line.startsWith(documentTitle) &&
							   !line.startsWith(documentTitlePart)) {
							
						var fieldDescriptionBegin = isFieldDescriptionBegin(line, currentFieldName);
						if (fieldDescriptionBegin) {
							if (line.trim().length() == 2 && isLevelNumber(line.trim())) {
								// the line only contains a level number; concatenate the next line, which should
								// really be there...
								var nextLine = in.readLine();
								if (nextLine != null) {
									line = line.concat(" ").concat(nextLine);
								}
							}
							// because we may have concatenated 2 lines, the field description begin is not necessary
							// a true field description begin, so make sure we're only flushing the current field
							// when the field is complete
							if (!(line.trim().startsWith("88 ") && line.indexOf("CONDITION-NAME") > -1  || line.trim().startsWith("02") && line.trim().endsWith("AFTER"))) {
								writeFieldData(out, q);						
								q = new StringBuilder();
								out.println();
															
								if (applyR19FieldNameSplitOverTwoLinesHack(line)) {
									applyingR19FieldNameSplitOverTwoLinesHack = true;
								} else {	
									out.println(generateFieldDescriptionTag(line));
								}
								currentFieldName = extractFieldName(line);
								if (line.charAt(2) == ' ') {
									// level number present
									currentLevelAndFieldName = line.substring(0, 3).concat(currentFieldName);
								} else {
									// no level number present
									currentLevelAndFieldName = currentFieldName;							
								}
							}
						}	
						if (line.length() > 3 && line.charAt(0) == '0' && line.charAt(2) == ' ' && line.indexOf("-  ") > -1 && !line.contains(" - ")) {
							// strip double blanks after hyphens in the field name
							q.append(line.replace("-  ", "-"));
						} else if (line.length() > 3 && line.charAt(0) == '0' && line.charAt(2) == ' ' && line.contains("- ") && !line.contains(" - ") && !line.contains(" -- ")) {
							// strip blanks after hyphens in the field name
							q.append(line.replace("- ", "-"));
						} else {
							if (!currentFieldName.equals("") && !fieldDescriptionBegin && line.startsWith(currentLevelAndFieldName) && line.indexOf("FILLER") == -1 ||
								line.equals("continued")) {
								
								if (line.equals("continued") || line.equals(currentLevelAndFieldName)) {
									// ignore line; continuation indication only
								} else {
									i = line.indexOf(" continued ¦ ");
									if (i == -1) {
										i = line.indexOf(" continued ? ");
									}
									if (i == -1) {
										i = line.indexOf(" continued ");
									}
									if (i != -1) {
										q.append('\n');
										q.append(line.substring(i + 11));
									} else {
										Plugin.getDefault().getLog().warn("ignored line for field " + currentFieldName + ": <" + line + ">");
									}
								}
							} else {
								if (!q.isEmpty()) {
									q.append('\n');
								}
								if (!applyingR19FieldNameSplitOverTwoLinesHack) {
									q.append(line);
								}
							}
						}
					}
				}
				line = in.readLine();
			}		
			writeFieldData(out, q);
			out.flush();	
		}		
	}
	
	private static boolean applyR19FieldNameSplitOverTwoLinesHack(String line) {
		return line.equals("03 CVG-OLQ-MAX-REP-") || line.equals("03 CVG-OLQ-CONTINUATION-") || 
			   line.equals("02 CVG-PREDEF-RUNUNIT-") || line.equals("04 CVG-TCPTAB-MAXPTASK-") || 
			   line.equals("02 STAT-MEM-AVG- CLUSTER-");
	}
	
	public static void performTask(File inputFolder, File outputFolder, String documentTitle) throws IOException {	
		var file = inputFolder.listFiles();
		for (int i = 0; i < file.length; i++) {
			processFile(file[i], outputFolder, documentTitle);
		}
	}
	
	private static void removeTrailingChars(StringBuilder p) {
		while (!p.isEmpty() && (p.charAt(p.length() - 1) == ' ' || p.charAt(p.length() - 1) == '\n')) {
			p.setLength(p.length() - 1);
		}
	}

	private static void writeFieldData(PrintWriter out, StringBuilder p) {
		removeTrailingChars(p);
		if (!p.isEmpty()) {
			out.println(processFieldData(p));
		}
	}
	
	private Phase2Extractor() {
	}
	
}
