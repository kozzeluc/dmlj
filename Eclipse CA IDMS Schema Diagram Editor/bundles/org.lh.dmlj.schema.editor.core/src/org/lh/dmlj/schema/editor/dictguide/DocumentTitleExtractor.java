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
package org.lh.dmlj.schema.editor.dictguide;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Stack;

import org.lh.dmlj.schema.editor.service.api.IPdfExtractorService;

public class DocumentTitleExtractor {
	private static final String TITLE_METADATA_PROPERTY = "title";
	private static final String TITLE_UNKNOWN = "?";
	private static final int MAX_LINES_TO_HANDLE = 100;
	
	private File file;
	private boolean lineContainsRelease = false;
	private int linesHandledForReleaseExtraction = 0;
	private IPdfExtractorService pdfExtractorService;
	private String release;
	
	public DocumentTitleExtractor(IPdfExtractorService pdfExtractorService, File file) {
		super();
		this.pdfExtractorService = pdfExtractorService;
		this.file = file;
	}	
	
	public String extractTitle() {
		Properties metadata = pdfExtractorService.extractMetadata(file);
		if (metadata != null && metadata.containsKey(TITLE_METADATA_PROPERTY)) {
			String title = metadata.getProperty(TITLE_METADATA_PROPERTY);
			return addReleaseToTitle(title);
		} else {
			return extractTitleFromDocumentContent();
		}
	}
	
	private String addReleaseToTitle(String title) {
		// we want to add the release to the title as well; for that, we have to dig into the PDF content, so let's process a max. of 100 lines...
		try (InputStream in = new FileInputStream(file)) {			
			pdfExtractorService.extractContent(in, this::handleContentForReleaseExtraction);
		} catch (IOException e) {
			return title;
		}
				
		if (release == null) {
			return title;
		} else {
			return release != null ? title + " (" + release + ")" : title;
		}
	}

	public boolean handleContentForReleaseExtraction(String line) {
		String trimmedLine = line.trim();
		if (trimmedLine.endsWith("Reference Guide")) {
			// the next line will contain the release information...
			lineContainsRelease = true;
		} else if (lineContainsRelease) {
			release = trimmedLine;
			return false;
		}
		return ++linesHandledForReleaseExtraction < MAX_LINES_TO_HANDLE;
	}
	
	private String extractTitleFromDocumentContent() {
		Stack<String> title = new Stack<>();
		try (InputStream in = new FileInputStream(file)) {
			pdfExtractorService.extractContent(in, line -> {
				String trimmedLine = line.trim();
				if (!trimmedLine.isEmpty()) {
					title.push(trimmedLine);
					return false;
				} else {
					return true;
				}
			});
		} catch (IOException e) {
			return TITLE_UNKNOWN;
		}
		if (!title.isEmpty()) {
			return title.pop();
		} else {
			return TITLE_UNKNOWN;
		}
	}

}
