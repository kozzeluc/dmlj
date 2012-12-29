package org.lh.dmlj.schema.editor.dictguide.helper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.apache.tika.metadata.Metadata;


public class PdfTitleExtractor {

	public static void main(String[] args) throws Throwable {			
		String title = performTask(new File(args[0]));
		System.out.println("title=" + title);
	}
	
	public static String performTask(File file) throws IOException {		
			
		// get the title from the metadata; it's important to close
		// the Reader afterwards to avoid out-of-memory exeptions
		InputStream inputStream = new FileInputStream(file);
		Metadata metadata = new Metadata();
		Reader reader = PdfContentExtractor.tika.parse(inputStream, metadata);		
		inputStream.close();
		String title = metadata.get("title");
		reader.close(); // we don't do anything with but closing 
		                // it is vital !
		
		// get the release information by parsing a max. of 100 lines
		String release = null;
		reader = PdfContentExtractor.tika.parse(file);
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