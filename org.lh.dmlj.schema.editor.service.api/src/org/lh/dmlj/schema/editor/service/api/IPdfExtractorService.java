package org.lh.dmlj.schema.editor.service.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IPdfExtractorService {

	void extractContent(InputStream in, OutputStream out) throws IOException;
	
	String extractTitle(File file) throws IOException;
	
}