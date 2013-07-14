package org.lh.dmlj.schema.editor.tika;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.lh.dmlj.schema.editor.service.api.IPdfContentConsumer;
import org.lh.dmlj.schema.editor.service.api.IPdfExtractorService;

public class PdfExtractorService implements IPdfExtractorService {
	
	public static final Tika tika = new Tika();	
	
	public PdfExtractorService() {
		super();		
	}
	
	@Override
	public void extractContent(InputStream in, IPdfContentConsumer contentConsumer) {		
		try {
			Reader reader = tika.parse(in);
			BufferedReader bufferedReader = new BufferedReader(reader);
			boolean proceed = true;
			for (String line = bufferedReader.readLine(); line != null && proceed;
				 line = bufferedReader.readLine()) {			
				
				proceed = contentConsumer.handleContent(line);
			}
			bufferedReader.close();
			reader.close();		
			in.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Properties extractMetadata(File file) {
		
		Metadata metadata = new Metadata();
		try {
			InputStream inputStream = new FileInputStream(file);
			// it's important to close the following Reader afterwards to avoid out-of-memory 
			// exeptions			
			Reader reader = tika.parse(inputStream, metadata);		
			inputStream.close();
			reader.close(); // we don't do anything with but closing it is vital !
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		Properties properties = new Properties();
		for (String name : metadata.names()) {
			properties.put(name, metadata.get(name));
		}
		
		return properties;
		
	}
	
	@Override
	public String getLicensedProductName() {
		return "Apache Tika";
	}
	
	@Override
	public String getLicensedProductVersion() {
		return "1.2";
	}

	@Override
	public String getLicenseName() {
		return "Apache License Version 2.0";
	}

	@Override
	public String getLicenseText() {
		StringBuilder p = new StringBuilder();
		try {
			InputStream inStream = 
				PdfExtractorService.class.getClassLoader().getResourceAsStream("META-INF/LICENSE");
			BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				if (p.length() > 0) {
					p.append("\n");
				}
				p.append(line);
			}
			in.close();
			inStream.close();
		} catch (IOException e) {
			p.append("\nIOException: " + e.getMessage());
		}
		return p.toString();
	}
	
}