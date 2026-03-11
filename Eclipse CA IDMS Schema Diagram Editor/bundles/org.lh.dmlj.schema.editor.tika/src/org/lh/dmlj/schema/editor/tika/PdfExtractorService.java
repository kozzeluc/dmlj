/**
 * Copyright (C) 2026  Luc Hermans
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
package org.lh.dmlj.schema.editor.tika;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.lh.dmlj.schema.editor.service.api.IPdfContentConsumer;
import org.lh.dmlj.schema.editor.service.api.IPdfExtractorService;

public class PdfExtractorService implements IPdfExtractorService {
	public static final Tika tika = new Tika();
	
	@Override
	public void extractContent(InputStream in, IPdfContentConsumer contentConsumer) {		
		try (var reader = tika.parse(in); var bufferedReader = new BufferedReader(reader)) {
			var proceed = true;
			// New in Tika 1.3: text from bookmarks is now extracted (TIKA-1035) 
			// see: http://issues.apache.org/jira/browse/TIKA-1035).
			// The IPdfContentConsumer has to be aware of this; it is only of relevance for the 'Dictionary
			// Structure Reference Guides' because it is the very last chapter in each of these books that interests us...
			for (var line = bufferedReader.readLine(); line != null && proceed; line = bufferedReader.readLine()) {				
				proceed = contentConsumer.handleContent(line);
			}
			in.close();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public Properties extractMetadata(File file) {
		var metadata = new Metadata();
		try (var inputStream = new FileInputStream(file); var reader = tika.parse(inputStream, metadata)) {
			// nothing to do here
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		var properties = new Properties();
		for (var name : metadata.names()) {
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
		return "2.3.0";
	}

	@Override
	public String getLicenseName() {
		return "Apache License Version 2.0";
	}

	@Override
	public String getLicenseText() {
		var p = new StringBuilder();
		try (var inStream = PdfExtractorService.class.getClassLoader().getResourceAsStream("license/LICENSE");
			 var in = new BufferedReader(new InputStreamReader(inStream))) {
			
			for (var line = in.readLine(); line != null; line = in.readLine()) {
				if (!p.isEmpty()) {
					p.append("\n");
				}
				p.append(line);
			}
		} catch (IOException e) {
			p.append("\nIOException: " + e.getMessage());
		}
		return p.toString();
	}	
	
}
