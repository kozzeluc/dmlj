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
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.lh.dmlj.schema.editor.dictionary.tools.Plugin;

public abstract class JarHelper {
	
	private static String MANIFEST_HEADER_COMMENT_START = 
		"Comment: All of the following OSGi headers were added using Eclipse plug-in\n " + 
		Plugin.getDefault().getBundle().getSymbolicName() + " version " + 
		Plugin.getDefault().getBundle().getVersion() + ":\n";
	private static byte[] NEW_LINE = "\n".getBytes();
	
	public static void copyAndAddOSGiHeadersToManifest(File src, File dest) throws IOException {
		Properties existingManifestHeaders = JarHelper.getManifestHeaders(src);
		Properties extraManifestHeaders = new Properties();
		if (!existingManifestHeaders.containsKey("Bundle-ManifestVersion")) {
			extraManifestHeaders.put("0Bundle-ManifestVersion", "2");
		}
		if (!existingManifestHeaders.containsKey("Bundle-Name")) {
			extraManifestHeaders.put("1Bundle-Name", 
								 	 existingManifestHeaders.getProperty("Implementation-Title"));
		}
		if (!existingManifestHeaders.containsKey("Bundle-SymbolicName")) {
			extraManifestHeaders.put("2Bundle-SymbolicName", "com.ca.idms.jdbc.driver");
		}
		if (!existingManifestHeaders.containsKey("Bundle-Version")) {
			extraManifestHeaders.put("3Bundle-Version", 
								 	 existingManifestHeaders.getProperty("Implementation-Version"));
		}
		if (!existingManifestHeaders.containsKey("Bundle-Vendor")) {
			extraManifestHeaders.put("4Bundle-Vendor", 
								 	 existingManifestHeaders.getProperty("Implementation-Vendor"));
		}
		if (!existingManifestHeaders.containsKey("Export-Package")) {
			extraManifestHeaders.put("5Export-Package", 
								 	 "ca.idms.dsi,\n ca.idms.io,\n ca.idms.jdbc,\n ca.idms.net,\n " +
								     "ca.idms.proxy,\n ca.idms.qcli,\n ca.idms.util,\n ca.idms.xa,\n " +
								 	 "com.ca.idms.hibernate,\n com.ca.idms.was");
		}
		if (!existingManifestHeaders.containsKey("Bundle-RequiredExecutionEnvironment")) {
			extraManifestHeaders.put("6Bundle-RequiredExecutionEnvironment", "JavaSE-1.7");		
		}
		copyJarWithExtraManifestHeaders(src, dest, extraManifestHeaders);		
	}	

	private static void copyJarWithExtraManifestHeaders(File src, File dest, 
													   Properties extraManifestHeaders) 
		throws IOException {
		
		ZipFile oldJarFile = new ZipFile(src);
		ZipOutputStream newZos = new ZipOutputStream(new FileOutputStream(dest));
		for (Enumeration<? extends ZipEntry> oldEntries = oldJarFile.entries(); 
			 oldEntries.hasMoreElements(); ) {
			
			ZipEntry oldEntry = oldEntries.nextElement();			
			if (!oldEntry.isDirectory()) {
				ZipEntry newEntry = new ZipEntry(oldEntry.getName());
				newZos.putNextEntry(newEntry);
				InputStream is = oldJarFile.getInputStream(oldEntry);
				if (oldEntry.getName().equals("META-INF/MANIFEST.MF") && 
					!extraManifestHeaders.isEmpty()) {
					
					BufferedReader in = new BufferedReader(new InputStreamReader(is));
					for (String line = in.readLine(); line != null && !line.trim().isEmpty();
						 line = in.readLine()) {
						
						newZos.write(line.getBytes());
						newZos.write(NEW_LINE);
					}
					in.close();				
					newZos.write(MANIFEST_HEADER_COMMENT_START.getBytes());
					List<String> sequencedHeaderNames = 
						new ArrayList<>(extraManifestHeaders.stringPropertyNames());
					Collections.sort(sequencedHeaderNames);
					for (String sequencedHeaderName : sequencedHeaderNames) {
						String headerName = sequencedHeaderName.substring(1);
						String headerValue = extraManifestHeaders.getProperty(sequencedHeaderName);
						String line = headerName + ": " + headerValue;
						newZos.write(line.getBytes());
						newZos.write(NEW_LINE);
					}
				} else {
					copyStream(is, newZos);
					newEntry.setTime(oldEntry.getTime());
				}
				is.close();	
				newZos.closeEntry();
			}
		}
		
		newZos.flush();
		newZos.close();
		oldJarFile.close();
	}
	
	private static void copyStream(InputStream is, OutputStream os) {
	     final int buffer_size = 1024;
	     try {
	         byte[] bytes = new byte[buffer_size];
	         for(; ; ) {
	           int count = is.read(bytes, 0, buffer_size);
	           if (count == -1) {
	               break;
	           }
	           os.write(bytes, 0, count);
	         }
	     } catch (Throwable t) {
	    	 throw new RuntimeException(t);
	     }
	 }

	public static Properties getManifestHeaders(File aJarFile) throws IOException {
		ZipFile zipFile = new ZipFile(aJarFile);
		Properties properties = new Properties();		
		try {			
			ZipEntry zipEntry = zipFile.getEntry("META-INF/MANIFEST.MF");
			InputStream is = zipFile.getInputStream(zipEntry);
			properties.load(is);
			is.close();
		} catch (Throwable t) {
		} 
		zipFile.close();
		return properties;		
	}
	
}
