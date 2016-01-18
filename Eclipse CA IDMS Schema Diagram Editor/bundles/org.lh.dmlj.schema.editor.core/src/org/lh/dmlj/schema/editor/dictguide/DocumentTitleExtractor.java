/**
 * Copyright (C) 2013  Luc Hermans
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
