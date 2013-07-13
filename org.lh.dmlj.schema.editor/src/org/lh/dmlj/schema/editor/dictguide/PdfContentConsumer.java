package org.lh.dmlj.schema.editor.dictguide;

import java.io.OutputStream;
import java.io.PrintWriter;

import org.lh.dmlj.schema.editor.service.api.IPdfContentConsumer;

public class PdfContentConsumer implements IPdfContentConsumer {

	private static final String CHAR_MINUS3 = 
		new String(new char[] {(char) ((byte) -3)});
	private static final String EYECATCHER_1_PRE_R17 = 
		"This chapter provides information about the records and sets";
	private static final String EYECATCHER_1_POST_R16 = 
		"This guide describes the dictionary database records";
	private static final String EYECATCHER_2_PRE_R17 = 
		"You can submit SQL statements to Advantage CA-IDMS";
	private static final String EYECATCHER_2_POST_R16 = 
		"Most of the definitions and entities used in this guide are " +
		"intuitively understood";
	private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";		
	
	private String 				dotOrColon = null;
	private boolean 			eyecatchersPassed = false;
	private String 				next = null;
	private PrintWriter 		pOut;
	private String 				type = null;
	private boolean 			writing = false;
	
	public PdfContentConsumer(OutputStream out) {
		super();
		pOut = new PrintWriter(out);
	}
	
	public void finish() {
		pOut.flush();
		pOut.close();
	}
	
	@Override
	public boolean handleContent(String line) {
		if (!eyecatchersPassed) {
			if (line.indexOf(EYECATCHER_1_PRE_R17) > -1 ||
				line.indexOf(EYECATCHER_1_POST_R16) > -1 ||
				line.indexOf(EYECATCHER_2_PRE_R17) > -1 ||
				line.indexOf(EYECATCHER_2_POST_R16) > -1) {
				
				eyecatchersPassed = true;
			}
		} else if (writing) {				
			
			if (line.startsWith(type + " " + next + dotOrColon) ||
				line.startsWith(" " + type + " " + next + dotOrColon)) {
									
				return false;
			}
			if (!line.equals("")) {										
				if (line.length() > 1 && line.charAt(0) == ' ' &&
				    (byte) line.charAt(1) == -96) {
					
					pOut.println("¦" + 
								line.substring(2).replaceAll(CHAR_MINUS3, " "));
				} else if (line.startsWith(" ")) {
					pOut.println(line.trim().replaceAll(CHAR_MINUS3, " "));
				} else if ((byte) line.charAt(0) == -96) {						
					pOut.println("¦" + 
								line.substring(1).replaceAll(CHAR_MINUS3, " "));						
				} else if ((byte) line.charAt(0) == -3 &&
						   (byte) line.charAt(1) == -3) {
					
					pOut.println(">>" + 
								line.substring(2).replaceAll(CHAR_MINUS3, " "));											
				} else {
					StringBuilder p = new StringBuilder();
					for (char c : line.toCharArray()) {
						if ((byte) c == (byte) -96) {
							p.append("¦");
						} else {
							p.append(c);
						}
					}
					pOut.println(p.toString().replaceAll(CHAR_MINUS3, " "));
				}
			}								
		} else if ((line.startsWith("Chapter") || 
				    line.startsWith("Appendix")) && 
				    (line.indexOf("Record and Element Descriptions") > -1 || 
//				     line.indexOf("SYSTEM Tables and SYSCA Views") > -1)) {	// OK for 18.0.00
					 line.indexOf("SYSTEM Tables and SYSCA") > -1)) {		// OK for 18.5.00
					
			if (line.startsWith("Chapter")) {
				type = "Chapter";
			} else {
				type = "Appendix";
			}
			pOut.println(line.replaceAll(CHAR_MINUS3, " "));
			writing = true;
			int i = line.indexOf(" ");
			int j = line.indexOf(".");
			if (j > -1) {
				dotOrColon = ".";		// before IDMS 18.0
			} else {
				j = line.indexOf(":"); 	// as of IDMS 18.0
				if (j == -1) {
					throw new Error("logic error; expecting 'Chapter/" +
									"Appendix n:' here: " + line);
				}
				dotOrColon = ":";
			}
			String p = line.substring(i + 1, j);
			if (type.equals("Chapter")) {
				i = Integer.valueOf(p).intValue() + 1;
				next = String.valueOf(i);
			} else {
				i = LETTERS.indexOf(p) + 1;
				next = LETTERS.substring(i, i + 1);					
			}
		}
		return true;
	}

}