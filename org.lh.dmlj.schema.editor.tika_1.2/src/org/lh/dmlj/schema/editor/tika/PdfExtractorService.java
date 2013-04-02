package org.lh.dmlj.schema.editor.tika;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.lh.dmlj.schema.editor.service.api.IPdfExtractorService;

public class PdfExtractorService implements IPdfExtractorService {

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
	
	public static final Tika tika = new Tika();	
	
	public PdfExtractorService() {
		super();		
	}
	
	public void extractContent(InputStream in, OutputStream out) throws IOException {		
		
		Reader reader = tika.parse(in);
		BufferedReader bufferedReader = new BufferedReader(reader);
		boolean eyecatchersPassed = false;
		boolean writing = false;
		String next = null;
		String type = null;
		String dotOrColon = null;
		PrintWriter pOut = new PrintWriter(out);
		for (String line = bufferedReader.readLine(); line != null;
			 line = bufferedReader.readLine()) {			
			
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
										
					break;
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
					     line.indexOf("SYSTEM Tables and SYSCA Views") > -1)) {
						
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
		}
		pOut.flush();
		pOut.close();
		bufferedReader.close();
		reader.close();		
		in.close();
		
	}
	
	public String extractTitle(File file) throws IOException {				
		
		// get the title from the metadata; it's important to close
		// the Reader afterwards to avoid out-of-memory exeptions
		InputStream inputStream = new FileInputStream(file);
		Metadata metadata = new Metadata();
		Reader reader = tika.parse(inputStream, metadata);		
		inputStream.close();
		String title = metadata.get("title");
		reader.close(); // we don't do anything with but closing 
		                // it is vital !
		
		// get the release information by parsing a max. of 100 lines
		String release = null;
		reader = tika.parse(file);
		BufferedReader bufferedReader = new BufferedReader(reader);
		boolean lineContainsRelease = false;
		int i = 0;
		for (String line = bufferedReader.readLine(); 
			 release == null && line != null && i < 100;
			 line = bufferedReader.readLine()) {
			
			if (release == null) {
				String p = line.trim();
				if (p.endsWith("Reference Guide")) {
					// the next line will contain the release 
					// information
					lineContainsRelease = true;
				} else if (lineContainsRelease) {
					release = p;
				}		
			}
			i += 1;
		}
		bufferedReader.close();
		reader.close();	
		
		// return the title, combined with the release information if available
		return release != null ? title + " (" + release + ")" : title;		
					
	}	
	
}