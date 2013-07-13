package org.lh.dmlj.schema.editor.dictguide;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.lh.dmlj.schema.editor.service.api.IPdfContentConsumer;
import org.lh.dmlj.schema.editor.service.api.IPdfExtractorService;

public class DocumentTitleExtractor implements IPdfContentConsumer {

	private static final int	 MAX_LINES_TO_HANDLE = 100;
	
	private File   				 file;
	private boolean 			 lineContainsRelease = false;
	private int    				 linesHandled = 0;
	private IPdfExtractorService pdfExtractorService;
	private String 				 release;
	
	public DocumentTitleExtractor(IPdfExtractorService pdfExtractorService, File file) {
		super();
		this.pdfExtractorService = pdfExtractorService;
		this.file = file;
	}	
	
	public String extractTitle() {
		
		String title = "?";
		
		// try to get the title from the PDF metadata
		Properties metadata = pdfExtractorService.extractMetadata(file);
		if (metadata != null && metadata.containsKey("title")) {
			title = metadata.getProperty("title");
		}
		
		// we want to add the release to the title as well; for that, we have to dig into the PDF 
		// content, so let's process a max. of 100 lines...
		try {
			InputStream in = new FileInputStream(file);
			pdfExtractorService.extractContent(in, this);
			in.close();
		} catch (IOException e) {
			return title;
		}
		
		// just return the title (if we found one, that is) if no release could be detected
		if (release == null) {
			return title;
		}
		
		// return the title, combined with the release information
		return release != null ? title + " (" + release + ")" : title;
		
	}

	@Override
	public boolean handleContent(String line) {
		String p = line.trim();
		if (p.endsWith("Reference Guide")) {
			// the next line will contain the release information...
			lineContainsRelease = true;
		} else if (lineContainsRelease) {
			release = p;
			return false;
		}
		return ++linesHandled < MAX_LINES_TO_HANDLE;
	}

}