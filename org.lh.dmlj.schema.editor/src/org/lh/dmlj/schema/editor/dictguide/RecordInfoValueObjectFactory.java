package org.lh.dmlj.schema.editor.dictguide;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lh.dmlj.schema.editor.property.ElementInfoValueObject;
import org.lh.dmlj.schema.editor.property.RecordInfoValueObject;

public class RecordInfoValueObjectFactory {

	public static RecordInfoValueObjectFactory INSTANCE = 
		new RecordInfoValueObjectFactory();
	
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
	
	private static String getAdjustedDescription(String description) {		
		
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
	
	private static String getAdjustedPictureAndUsage(String pictureAndUsage,
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

	private RecordInfoValueObjectFactory() {
		super();
	}
	
	public RecordInfoValueObject createRecordInfoValueObject(BufferedReader in) 
		throws IOException {
		
		// cache the entire entry in a List<String>; this is easier to deal 
		// with...
		List<String> lines = new ArrayList<>();
		for (String line = in.readLine(); line != null; line = in.readLine()) {
			lines.add(line);
		}		
		
		// if the list is empty, don't bother with the entry...
		if (lines.isEmpty()) {
			return null;
		}
		
		// the first line contains the record name (which should be the same as
		// the one contained in the entry name; if we cannot extract the record
		// name, ignore the entry...
		int i = lines.get(0).indexOf("/* ");
		int j = lines.get(0).indexOf(" */");
		if (i < 0 || j < 0 || j < i) {
			return null;
		}
		String recordName = lines.get(0).substring(i + 3, j).trim();
		
		// extract the description attribute value; if it's missing, ignore the 
		// entry...
		String description = extractFromJavadoc(lines, "Description");
		if (description == null) {
			return null;
		}		
		
		// extract the documentName attribute value (document title)...
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
		
		// create the RecordInfoValueObject and set it's attributes
		RecordInfoValueObject recordInfoValueObject = 
			new RecordInfoValueObject();
		recordInfoValueObject.setRecordName(recordName);		
		recordInfoValueObject.setDescription(description);
		recordInfoValueObject.setDocumentId(null); // obsolete
		if (documentName != null) {
			// we should normally always have a document title available
			recordInfoValueObject.setDocumentName(documentName);
		}
		if (recordLength != null) {
			recordInfoValueObject.setRecordLength(recordLength);
		}
		if (establishedBy != null) {
			recordInfoValueObject.setEstablishedBy(establishedBy);
		}
		if (ownerOf != null) {
			recordInfoValueObject.setOwnerOf(ownerOf);
		}
		if (memberOf != null) {
			recordInfoValueObject.setMemberOf(memberOf);
		}
		if (locationMode != null) {
			recordInfoValueObject.setLocationMode(locationMode);
		}
		if (withinArea != null) {
			recordInfoValueObject.setWithinArea(withinArea);
		}
		
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
			
			// create the ElementInfoValueObject and add it to the
			// RecordInfoValueObject's list of ElementInfoValueObject children 
			ElementInfoValueObject elementInfoValueObject =
				new ElementInfoValueObject(seqNo);			
			elementInfoValueObject.setElementName(elementName);		
			if (levelAndElementName != null) {
				elementInfoValueObject.setLevelAndElementName(levelAndElementName);
			}
			if (pictureAndUsage != null) {
				if (description != null) {
					// only a few elements need some tweaking...
					pictureAndUsage = 
						getAdjustedPictureAndUsage(pictureAndUsage, description);
				}
				elementInfoValueObject.setPictureAndUsage(pictureAndUsage);
			}				
			if (description != null && !description.trim().equals("")) {
				// only a few elements need some tweaking; mind you that we
				// might need to adjust the description once more...
				description = getAdjustedDescription(description);					
				elementInfoValueObject.setDescription(description);
			}
			if (pictureAndUsage != null) {
				elementInfoValueObject.setPictureAndUsage(pictureAndUsage);
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
					elementInfoValueObject.setPictureAndUsage(pictureAndUsage);
					elementInfoValueObject.setDescription(description);						
				}
				
			}			
			recordInfoValueObject.getElementInfoValueObjects()
							     .add(elementInfoValueObject);
						
		}
		
		return recordInfoValueObject;
		
	}	
	
}