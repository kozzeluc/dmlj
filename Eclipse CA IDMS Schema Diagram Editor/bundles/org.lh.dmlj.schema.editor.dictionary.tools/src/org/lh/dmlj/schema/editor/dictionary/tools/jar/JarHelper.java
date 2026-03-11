/**
 * Copyright (C) 2025  Luc Hermans
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
package org.lh.dmlj.schema.editor.dictionary.tools.jar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.lh.dmlj.schema.editor.dictionary.tools.Plugin;

public final class JarHelper {
	private static final String MANIFEST_HEADER_COMMENT_START = 
		"Comment: All of the following OSGi headers were added using Eclipse plug-in\n " + 
		Plugin.getDefault().getBundle().getSymbolicName() + " version " + 
		Plugin.getDefault().getBundle().getVersion() + ":\n";
	private static final byte[] NEW_LINE = "\n".getBytes();
	
	public static void copyAndAddOSGiHeadersToManifest(File src, File dest) throws IOException {
		var existingManifestHeaders = JarHelper.getManifestHeaders(src);
		var extraManifestHeaders = new Properties();
		if (!existingManifestHeaders.containsKey("Bundle-ManifestVersion")) {
			extraManifestHeaders.put("0Bundle-ManifestVersion", "2");
		}
		if (!existingManifestHeaders.containsKey("Bundle-Name")) {
			extraManifestHeaders.put("1Bundle-Name", existingManifestHeaders.getProperty("Implementation-Title"));
		}
		if (!existingManifestHeaders.containsKey("Bundle-SymbolicName")) {
			extraManifestHeaders.put("2Bundle-SymbolicName", "com.ca.idms.jdbc.driver");
		}
		if (!existingManifestHeaders.containsKey("Bundle-Version")) {
			extraManifestHeaders.put("3Bundle-Version", existingManifestHeaders.getProperty("Implementation-Version"));
		}
		if (!existingManifestHeaders.containsKey("Bundle-Vendor")) {
			extraManifestHeaders.put("4Bundle-Vendor", existingManifestHeaders.getProperty("Implementation-Vendor"));
		}
		if (!existingManifestHeaders.containsKey("Export-Package")) {
			extraManifestHeaders.put("5Export-Package",
					"""
					ca.idms.dsi,
					 ca.idms.io,
					 ca.idms.jdbc,
					 ca.idms.net,
					 ca.idms.proxy,
					 ca.idms.qcli,
					 ca.idms.util,
					 ca.idms.xa,
					 com.ca.idms.hibernate,
					 com.ca.idms.was\
					""");
		}
		if (!existingManifestHeaders.containsKey("Bundle-RequiredExecutionEnvironment")) {
			extraManifestHeaders.put("6Bundle-RequiredExecutionEnvironment", "JavaSE-17");
		}
		copyJarWithExtraManifestHeaders(src, dest, extraManifestHeaders);		
	}	

	private static void copyJarWithExtraManifestHeaders(File src, File dest, Properties extraManifestHeaders) throws IOException {
		try (var oldJarFile = new ZipFile(src); var newZos = new ZipOutputStream(new FileOutputStream(dest))) {
			for (var oldEntries = oldJarFile.entries(); oldEntries.hasMoreElements(); ) {
				var oldEntry = oldEntries.nextElement();
				if (!oldEntry.isDirectory()) {
					createNewEntry(oldJarFile, oldEntry, newZos, extraManifestHeaders);
				}
			}
		}
	}
	
	private static void createNewEntry(ZipFile oldJarFile, ZipEntry oldEntry, ZipOutputStream newZos, Properties extraManifestHeaders) throws IOException {
		var newEntry = new ZipEntry(oldEntry.getName());
		newZos.putNextEntry(newEntry);
		try (var is = oldJarFile.getInputStream(oldEntry)) {
			if (oldEntry.getName().equals("META-INF/MANIFEST.MF") && !extraManifestHeaders.isEmpty()) {
				try (var in = new BufferedReader(new InputStreamReader(is))) {
					for (var line = in.readLine(); line != null && !line.trim().isEmpty(); line = in.readLine()) {
						newZos.write(line.getBytes());
						newZos.write(NEW_LINE);
					}
				}
				newZos.write(MANIFEST_HEADER_COMMENT_START.getBytes());
				var sequencedHeaderNames = new ArrayList<>(extraManifestHeaders.stringPropertyNames());
				Collections.sort(sequencedHeaderNames);
				for (var sequencedHeaderName : sequencedHeaderNames) {
					var headerName = sequencedHeaderName.substring(1);
					var headerValue = extraManifestHeaders.getProperty(sequencedHeaderName);
					var line = headerName + ": " + headerValue;
					newZos.write(line.getBytes());
					newZos.write(NEW_LINE);
				}
			} else {
				copyStream(is, newZos);
				newEntry.setTime(oldEntry.getTime());
			}
		}
		newZos.closeEntry();
	}
	
	private static void copyStream(InputStream is, OutputStream os) {
	     var bufferSize = 1024;
	     try {
	         var bytes = new byte[bufferSize];
	         for(; ; ) {
				 var count = is.read(bytes, 0, bufferSize);
				 if (count == -1) {
					 break;
				 }
				 os.write(bytes, 0, count);
	         }
	     } catch (Exception e) {
	    	 	throw new IllegalStateException(e);
	     }
	 }

	public static Properties getManifestHeaders(File aJarFile) throws IOException {
		var properties = new Properties();
		try (var zipFile = new ZipFile(aJarFile)) {
			var zipEntry = zipFile.getEntry("META-INF/MANIFEST.MF");
			try (var is = zipFile.getInputStream(zipEntry)) {
				properties.load(is);
			}			
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		return properties;
	}
	
	private JarHelper() {
	}
	
}
