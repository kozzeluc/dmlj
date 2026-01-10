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
package org.lh.dmlj.schema.editor.dictguide;

import java.io.OutputStream;
import java.io.PrintWriter;

import org.lh.dmlj.schema.editor.service.api.IPdfContentConsumer;

public class PdfContentConsumer implements IPdfContentConsumer {
	private static final String CHAPTER = "Chapter";
	private static final String APPENDIX = "Appendix";
	private static final String CHAR_MINUS3 = new String(new char[] { (char) ((byte) -3) });
	private static final String EYECATCHER_1_PRE_R17 = "This chapter provides information about the records and sets";
	private static final String EYECATCHER_1_POST_R16 = "This guide describes the dictionary database records";
	private static final String EYECATCHER_1_POST_R185 = "The records in this section are presented alphabetically by record name";
	private static final String EYECATCHER_2_PRE_R17 = "You can submit SQL statements to Advantage CA-IDMS";
	private static final String EYECATCHER_2_POST_R16 = "Most of the definitions and entities used in this guide are intuitively understood";
	private static final String EYECATCHER_2_POST_R185 = "This topic contains information about coding consideration for CA IDMS SQL:";
	private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";	

	private enum BookPartType { CHAPTER, APPENDIX, NONE }
	
	private final PrintWriter pOut;
	
	private String dotOrColon = null;
	private boolean eyecatchersPassed = false;
	private String next = null;
	private BookPartType bookPartType = null;
	private String bookPartTypeAsText = null;
	private boolean writing = false;
	
	public PdfContentConsumer(OutputStream out) {
		pOut = new PrintWriter(out);
	}
	
	public void finish() {
		pOut.flush();
		pOut.close();
	}
	
	@Override
	public boolean handleContent(String line) {
		if (!eyecatchersPassed) {
			checkEyeCatchersPassed(line);
		} else if (writing) {
			if (line.trim().equals("Bookshelf") || line.trim().equals("CA IDMS Dictionary Structure Reference Guide")) {
				return false;
			}			
			if (bookPartType != BookPartType.NONE && (line.startsWith(bookPartTypeAsText + " " + next + dotOrColon) ||
				line.startsWith(" " + bookPartTypeAsText + " " + next + dotOrColon)) ||
				bookPartType == BookPartType.NONE  && line.trim().equals(next)) {
									
				return false;
			}
			writeContentWhenLineNotEmpty(line);
		} else if ((line.startsWith(CHAPTER) || line.startsWith(APPENDIX)) && (line.contains("Record and Element Descriptions") ||
				line.contains("SYSTEM Tables and SYSCA")) || line.contains("Each description includes the following information:") ||
				line.equals("SYSTEM Tables and SYSCA Views")) {
					
			writeChapterAndAppendexContent(line);			
		}
		return true;
	}
	
	private void checkEyeCatchersPassed(String line) {
		if (line.contains(EYECATCHER_1_PRE_R17) || line.contains(EYECATCHER_1_POST_R16) || line.contains(EYECATCHER_1_POST_R185) ||
			line.contains(EYECATCHER_2_PRE_R17) || line.contains(EYECATCHER_2_POST_R16) || line.contains(EYECATCHER_2_POST_R185)) {
			
			eyecatchersPassed = true;
		}
	}
	
	private void writeContentWhenLineNotEmpty(String line) {
		if (!line.equals("")) {										
			if (line.length() > 1 && line.charAt(0) == ' ' && (byte) line.charAt(1) == -96) {					
				pOut.println("¦" + line.substring(2).replaceAll(CHAR_MINUS3, " "));
			} else if (line.startsWith(" ")) {
				pOut.println(line.trim().replaceAll(CHAR_MINUS3, " "));
			} else if ((byte) line.charAt(0) == -96) {						
				pOut.println("¦" + line.substring(1).replaceAll(CHAR_MINUS3, " "));						
			} else if ((byte) line.charAt(0) == -3 && (byte) line.charAt(1) == -3) {					
				pOut.println(">>" + line.substring(2).replaceAll(CHAR_MINUS3, " "));
			} else {
				writeContent(line);
			}
		}
	}
	
	private void writeContent(String line) {
		var p = new StringBuilder();
		for (var c : line.toCharArray()) {
			if ((byte) c == (byte) -96) {
				p.append("¦");
			} else {
				p.append(c);
			}
		}
		pOut.println(p.toString().replaceAll(CHAR_MINUS3, " "));
	}
	
	private void writeChapterAndAppendexContent(String line) {
		if (line.startsWith(CHAPTER)) {
			bookPartType = BookPartType.CHAPTER;
			bookPartTypeAsText = CHAPTER;
		} else if (line.startsWith(APPENDIX)) {
			bookPartType = BookPartType.APPENDIX;
			bookPartTypeAsText = APPENDIX;
		} else {
			bookPartType = BookPartType.NONE;
		}
		pOut.println(line.replaceAll(CHAR_MINUS3, " "));
		writing = true;
		var i = line.indexOf(" ");
		var j = line.indexOf(".");
		if (j > -1) {
			dotOrColon = ".";
		} else {
			j = line.indexOf(":");
			if (j == -1 && bookPartType != BookPartType.NONE) {
				throw new IllegalStateException("logic error; expecting 'Chapter/Appendix n:' here: " + line);
			}
			dotOrColon = ":";
		}
		var p = "";
		if (bookPartType != BookPartType.NONE) {
			p = line.substring(i + 1, j);
		}
		if (bookPartType == BookPartType.CHAPTER) {
			i = Integer.parseInt(p) + 1;
			next = String.valueOf(i);
		} else if (bookPartType == BookPartType.APPENDIX) {
			i = LETTERS.indexOf(p) + 1;
			next = LETTERS.substring(i, i + 1);					
		} else if (line.contains("SYSTEM Tables and SYSCA Views")) {
			next = "SYSCA Views";
		} else {
			next = "Table of Contents";
		}
	}

}
