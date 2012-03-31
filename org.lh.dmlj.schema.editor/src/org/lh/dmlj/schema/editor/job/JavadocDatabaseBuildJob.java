package org.lh.dmlj.schema.editor.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.lh.dmlj.schema.editor.Plugin;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexManager;

public class JavadocDatabaseBuildJob extends Job {
	
	private GraphDatabaseService neo4jDb;
	private ZipFile 			 zipFile;
	
	private static BufferedReader createBufferedReader(ZipFile file, 
													   ZipEntry entry) {		
		if (entry != null) {
			InputStream is;
			try {
				is = file.getInputStream(entry);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			InputStreamReader isr = new InputStreamReader(is);
			return new BufferedReader(isr);
		}
		return null;
	}	
	
	private static String extractFromJavadoc(List<String> lines, String key) {
		int i = 0;
		while (i < lines.size() && 
			   !lines.get(i).startsWith("/* " + key + " */")) {
			i++;
		}
		if (i >= lines.size()) {
			return null;
		}
		StringBuilder p = new StringBuilder();
		while (++i < lines.size() && !lines.get(i).startsWith("/*")) {
			if (p.length() > 0) {
				p.append("\n");
			}
			p.append(lines.get(i));
		}
		return p.toString();		
	}	
	
	public JavadocDatabaseBuildJob(ZipFile zipFile, 
								   GraphDatabaseService neo4jDb) {
		super("Build IDMSNTWK V1 Info cache");
		this.zipFile = zipFile;
		this.neo4jDb = neo4jDb;
	}	
	
	private String getAdjustedDescription(String description) {		
		
		if (description.startsWith("USAGE DISPLAY")) {
			return description.substring(13);
		}
		
		if (description.startsWith("VALUE")) {
			int i = description.indexOf("'");
			int j = description.indexOf("'", i + 1);
			if (i> -1 && j > -1) {
				return description.substring(j + 1);				
			}			
		}
		
		return description;
	}
	
	private String getAdjustedPictureAndUsage(String pictureAndUsage,
											  String description) {
		String adjustedDescription = getAdjustedDescription(description);
		if (description.equals(adjustedDescription)) {
			return pictureAndUsage;
		}
		String p = description.substring(0, description.length() - 
										 adjustedDescription.length());
		if (p.startsWith("VALUE'")) {
			p = "VALUE '" + p.substring(6);
		}
		return pictureAndUsage + " " + p;		
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		
		try {
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				storeJavadocData(zipFile, entry);
			}			
			return new Status(IStatus.OK, Plugin.PLUGIN_ID, IStatus.OK, 
		  	  		  "Job ended normally.", null);
		} catch (Throwable t) {
			return new Status(IStatus.ERROR, Plugin.PLUGIN_ID, IStatus.ERROR, 
		  	  		  		  "Job ended abnormally.", t);
		}				
	}
	
	private void storeJavadocData(ZipFile zipFile, ZipEntry entry) 
			throws IOException {
			
			// cache the entire entry in a List<String>; this is easier to deal 
			// with...
			List<String> lines = new ArrayList<>();
			BufferedReader in = createBufferedReader(zipFile, entry);
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				lines.add(line);
			}
			in.close();
			
			// if the list is empty, don't bother with the entry...
			if (lines.isEmpty()) {
				return;
			}
			
			// the first line contains the record name (which should be the same as
			// the one contained in the entry name; if we cannot extract the record
			// name, ignore the entry...
			int i = lines.get(0).indexOf("/* ");
			int j = lines.get(0).indexOf(" */");
			if (i < 0 || j < 0 || j < i) {
				return;
			}
			String recordName = lines.get(0).substring(i + 3, j).trim();
			
			// extract the description attribute value; if it's missing, ignore the 
			// entry...
			String description = extractFromJavadoc(lines, "Description");
			if (description == null) {
				return;
			}

			// extract the documentId attribute value...
			String documentId = extractFromJavadoc(lines, "Document ID");
			
			// extract the documentName attribute value...
			String documentName = extractFromJavadoc(lines, "Document Name");
			
			// extract the recordLength attribute value...
			String recordLength = extractFromJavadoc(lines, "Record length");
			
			// extract the establishedBy attribute value...
			String establishedBy = extractFromJavadoc(lines, "Established by");
			
			// extract the ownerOf attribute value...
			String ownerOf = extractFromJavadoc(lines, "Owner of");
			
			// extract the memberOf attribute value...
			String memberOf = extractFromJavadoc(lines, "Member of");
			
			// extract the locationMode attribute value...
			String locationMode = extractFromJavadoc(lines, "Location mode");
			
			// extract the withinArea attribute value...
			String withinArea = extractFromJavadoc(lines, "Within area");
			
			// create the node for the record and set its recordName, description,
			// documentId and documentName attributes...
			Transaction tx = neo4jDb.beginTx();
			Node recordNode = neo4jDb.createNode();
			recordNode.setProperty("recordName", recordName);		
			recordNode.setProperty("description", description);
			if (documentId != null) {
				recordNode.setProperty("documentId", documentId);
			}
			if (documentName != null) {
				recordNode.setProperty("documentName", documentName);
			}
			if (recordLength != null) {
				recordNode.setProperty("recordLength", recordLength);
			}
			if (establishedBy != null) {
				recordNode.setProperty("establishedBy", establishedBy);
			}
			if (ownerOf != null) {
				recordNode.setProperty("ownerOf", ownerOf);
			}
			if (memberOf != null) {
				recordNode.setProperty("memberOf", memberOf);
			}
			if (locationMode != null) {
				recordNode.setProperty("locationMode", locationMode);
			}
			if (withinArea != null) {
				recordNode.setProperty("withinArea", withinArea);
			}
			
			// index all record nodes
			IndexManager indexManager = neo4jDb.index(); 
			Index<Node> recordsIndex = indexManager.forNodes("RECORDS");
			recordsIndex.add(recordNode, "recordName", recordName);		
			
			// process the record's elements...
			List<List<String>> elementLists = new ArrayList<>();
			List<String> elementLines = new ArrayList<String>();
			boolean processing = false;
			for (String line : lines) {
				if (!processing && line.startsWith("/* Field ") &&
					line.indexOf(" */ ") > -1) {
					
					processing = true;
				}
				if (processing) {
					if (line.startsWith("/* Field ") &&
						line.indexOf(" */ ") > -1) {
						
						if (!elementLines.isEmpty()) {
							elementLists.add(elementLines);
							elementLines = new ArrayList<String>();							
						}						
					}
					elementLines.add(line);
				}
			}
			if (!elementLines.isEmpty()) {
				elementLists.add(elementLines);
			}
			int seqNo = 0;
			for (List<String> elementLines2 : elementLists) {
				// we have a list that contains everything for a single element;
				// the first line contains the elementName				
				i = elementLines2.get(0).indexOf(" */");
				String elementName = elementLines2.get(0).substring(9, i);
				
				// the second line contains the level number and element name
				String levelAndElementName = null;
				if (elementLines2.size() > 1) {
					levelAndElementName = elementLines2.get(1).trim();
				}
				
				// the third line contains the picture, if any, and usage
				String pictureAndUsage = null;
				if (elementLines2.size() > 2) {
					pictureAndUsage = elementLines2.get(2).trim();
				}
				
				// the description is contained on line 4 and subsequent lines
				description = null;
				if (elementLines2.size() > 3) {
					StringBuilder p = new StringBuilder();				
					for (i = 3; i < elementLines2.size(); i++) {
						if (p.length() > 0) {
							p.append(" ");
						}
						p.append(elementLines2.get(i));
					}
					description = p.toString();
				}
				
				// create the elementNode
				Node elementNode = neo4jDb.createNode();
				elementNode.setProperty("seqNo", Integer.valueOf(++seqNo));
				elementNode.setProperty("elementName", elementName);		
				if (levelAndElementName != null) {
					elementNode.setProperty("levelAndElementName", 
											levelAndElementName);
				}
				if (pictureAndUsage != null) {
					if (description != null) {
						// only a few elements need some tweaking...
						pictureAndUsage = 
							getAdjustedPictureAndUsage(pictureAndUsage,
													   description);
					}
					elementNode.setProperty("pictureAndUsage", pictureAndUsage);
				}				
				if (description != null && !description.trim().equals("")) {
					// only a few elements need some tweaking; mind you that we
					// might need to adjust the description once more...
					description = getAdjustedDescription(description);					
					elementNode.setProperty("description", description);
				}
				if (pictureAndUsage != null) {
					elementNode.setProperty("pictureAndUsage", pictureAndUsage);
				}
				// in some cases some more tweaking has to be done..
				if (pictureAndUsage != null && 
				    pictureAndUsage.indexOf("TIMES ") > -1 && 
					pictureAndUsage.indexOf("TIMES DEPENDING ON") == -1) {									
					
					i = pictureAndUsage.indexOf("TIMES ");
					if (!pictureAndUsage.substring(i + 6).trim().equals("")) {					
						if (description == null || 
							description.trim().equals("")) {
							
							description = pictureAndUsage.substring(i + 6);
						} else {
							description = pictureAndUsage.substring(i + 6) + 
										  " " + description;
						}
						pictureAndUsage = pictureAndUsage.substring(0, i + 5);
						elementNode.setProperty("pictureAndUsage", 
												pictureAndUsage);
						elementNode.setProperty("description", description);						
					}
					
				}
				
				// create a relationship to the record node
				elementNode.createRelationshipTo(recordNode, 
				  		  						 Plugin.RelTypes.RECORD_ELEMENT);
				
				// if we ever want direct access to an element node, index all 
				// element nodes, except FILLER elements
				/*if (!elementName.equals("FILLER")) {
					Index<Node> elementsIndex = 
						indexManager.forNodes("ELEMENTS");
					elementsIndex.add(elementNode, "elementName", elementName);
				}*/
			}
			
			// commit and finish the database transaction...
			tx.success();
			tx.finish();
		}	

}