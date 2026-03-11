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
package org.lh.dmlj.schema.editor.dictguide;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lh.dmlj.schema.editor.property.ElementInfoValueObject;
import org.lh.dmlj.schema.editor.property.RecordInfoValueObject;

public final class RecordInfoValueObjectFactory {
	private static final RecordInfoValueObjectFactory instance = new RecordInfoValueObjectFactory();
	
	public static RecordInfoValueObjectFactory getInstance() {
		return instance;
	}
	
	private static String extractFromJavadoc(List<String> lines, String key) {
		var i = 0;
		while (i < lines.size() && !lines.get(i).startsWith("/* " + key + " */")) {
			i++;
		}
		if (i >= lines.size()) {
			return null;
		}
		var p = new StringBuilder();
		while (++i < lines.size() && !lines.get(i).startsWith("/*")) {
			if (!p.isEmpty()) {
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
			var i = description.indexOf("'");
			var j = description.indexOf("'", i + 1);
			if (i > -1 && j > -1) {
				return description.substring(j + 1);				
			}			
		}
		
		return description;
	}
	
	private static String getAdjustedPictureAndUsage(String pictureAndUsage, String description) {
		var adjustedDescription = getAdjustedDescription(description);
		if (description.equals(adjustedDescription)) {
			return pictureAndUsage;
		}
		var p = description.substring(0, description.length() - adjustedDescription.length());
		if (p.startsWith("VALUE'")) {
			p = "VALUE '" + p.substring(6);
		}
		return pictureAndUsage + " " + p;		
	}	

	private RecordInfoValueObjectFactory() {
	}
	
	public RecordInfoValueObject createRecordInfoValueObject(BufferedReader in) throws IOException {
		// cache the entire entry in a List<String>; this is easier to deal with...
		var lines = new ArrayList<String>();
		for (var line = in.readLine(); line != null; line = in.readLine()) {
			lines.add(line);
		}		
		
		// if the list is empty, don't bother with the entry...
		if (lines.isEmpty()) {
			return null;
		}
		
		// the first line contains the record name (which should be the same as the one contained in the entry
		// name; if we cannot extract the record name, ignore the entry...
		var i = lines.get(0).indexOf("/* ");
		var j = lines.get(0).indexOf(" */");
		if (i < 0 || j < 0 || j < i) {
			return null;
		}
		var recordName = lines.get(0).substring(i + 3, j).trim();
		
		return createRecordInfoValueObject(lines, recordName);
	}
	
	private RecordInfoValueObject createRecordInfoValueObject(List<String> lines, String recordName) {
		var recordInfoValueObject = new RecordInfoValueObject();
		var description = extractFromJavadoc(lines, "Description");
		if (description == null) {
			return null;
		}
		setRecordData(recordInfoValueObject, lines, recordName, description);
		setElementData(recordInfoValueObject, lines);
		return recordInfoValueObject;		
	}
	
	private void setRecordData(RecordInfoValueObject recordInfoValueObject, List<String> lines, String recordName, String description) {
		var documentName = extractFromJavadoc(lines, "Document Name");
		var recordLength = extractFromJavadoc(lines, "Record length");
		var establishedBy = extractFromJavadoc(lines, "Established by");
		var ownerOf = extractFromJavadoc(lines, "Owner of");
		var memberOf = extractFromJavadoc(lines, "Member of");
		var locationMode = extractFromJavadoc(lines, "Location mode");
		var withinArea = extractFromJavadoc(lines, "Within area");
		
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
	}
	
	private void setElementData(RecordInfoValueObject recordInfoValueObject, List<String> lines) {
		createElementLists(lines).stream()
				.map(this::createInfoValueObject)
				.forEach(recordInfoValueObject.getElementInfoValueObjects()::add);
	}
	
	private List<List<String>> createElementLists(List<String> lines) {
		var elementLists = new ArrayList<List<String>>();
		var elementLines = new ArrayList<String>();
		var processing = false;
		for (var line : lines) {
			if (!processing && line.startsWith("/* Field ") && line.indexOf(" */ ") > -1) {
				processing = true;
			}
			if (processing) {
				if (line.startsWith("/* Field ") && line.indexOf(" */ ") > -1 && !elementLines.isEmpty()) {
					elementLists.add(elementLines);
					elementLines = new ArrayList<String>();							
				}				
				elementLines.add(line);
			}
		}
		if (!elementLines.isEmpty()) {
			elementLists.add(elementLines);
		}
		return elementLists;
	}
	
	private ElementInfoValueObject createInfoValueObject(List<String> elementLines) {
		var elementName = elementLines.get(0).substring(9, elementLines.get(0).indexOf(" */"));
		var levelAndElementName = elementLines.size() > 1 ? elementLines.get(1).trim() : null;
		var pictureAndUsage = elementLines.size() > 2 ? elementLines.get(2).trim() : null;
		var description = extractDescription(elementLines);
				
		var elementInfoValueObject = new ElementInfoValueObject(0);			
		elementInfoValueObject.setElementName(elementName);		
		if (levelAndElementName != null) {
			elementInfoValueObject.setLevelAndElementName(levelAndElementName);
		}
		if (pictureAndUsage != null) {
			if (description != null) {
				// only a few elements need some tweaking...
				pictureAndUsage = getAdjustedPictureAndUsage(pictureAndUsage, description);
			}
			elementInfoValueObject.setPictureAndUsage(pictureAndUsage);
		}				
		if (description != null && !description.trim().equals("")) {
			// only a few elements need some tweaking; mind you that we might need to adjust the description once more...
			description = getAdjustedDescription(description);					
			elementInfoValueObject.setDescription(description);
		}
		if (pictureAndUsage != null) {
			elementInfoValueObject.setPictureAndUsage(pictureAndUsage);
		}
		tweakPictureAndUsageAndDescriptionIfNeeded(pictureAndUsage, description, elementInfoValueObject);			
		return elementInfoValueObject;		
	}
	
	private String extractDescription(List<String> elementLines) {
		if (elementLines.size() > 3) {
			var p = new StringBuilder();				
			for (var i = 3; i < elementLines.size(); i++) {
				if (!p.isEmpty()) {
					p.append(" ");
				}
				p.append(elementLines.get(i));
			}
			return p.toString();
		} else {
			return null;
		}
	}
	
	private void tweakPictureAndUsageAndDescriptionIfNeeded(String pictureAndUsage, String description, ElementInfoValueObject target) {
		if (pictureAndUsage != null && pictureAndUsage.contains("TIMES ") && !pictureAndUsage.contains("TIMES DEPENDING ON")) {
			var i = pictureAndUsage.indexOf("TIMES ");
			if (!pictureAndUsage.substring(i + 6).isBlank()) {
				target.setPictureAndUsage(pictureAndUsage.substring(0, i + 5));
				if (description == null || description.isBlank()) {
					target.setDescription(target.getPictureAndUsage().substring(i + 6));
				} else {
					target.setDescription(target.getPictureAndUsage().substring(i + 6) + " " + description);
				}
			}
			
		}
	}
	
}
