package org.lh.dmlj.schema.editor.service.api;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

public interface IPdfExtractorService {

	void extractContent(InputStream in, IPdfContentConsumer contentConsumer);
	
	Properties extractMetadata(File file);
	
	String getLicensedProductName();
	
	String getLicensedProductVersion();
	
	String getLicenseName();
	
	String getLicenseText();
	
}