/**
 * Copyright (C) 2016  Luc Hermans
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
package org.lh.dmlj.schema.editor.wizard._import.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.IndexElement;
import org.lh.dmlj.schema.OccursSpecification;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.editor.common.NamingConventions;
import org.lh.dmlj.schema.editor.common.ValidationResult;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;
import org.lh.dmlj.schema.editor.importtool.IElementDataCollector;
import org.lh.dmlj.schema.editor.importtool.elements.IRecordElementsDataCollectorRegistry;
import org.lh.dmlj.schema.editor.importtool.elements.IRecordElementsImportTool;

public final class RecordElementsImportToolProxy {
	
	// optional parameters configured in the import tool's defining plug-in
	private Properties importToolParameters;
	
	// the import tool
	private IRecordElementsImportTool tool;
	
	// the ROOT element list that we are building
	private ArrayList<Element> rootElements = new ArrayList<>();
	
	// a list with ALL elements that we keep to easily find elements
	private ArrayList<Element> allElements = new ArrayList<>();
	
	// our data collector registry
	IRecordElementsDataCollectorRegistry dataCollectorRegistry = 
		new RecordElementsDataCollectorRegistry();

	// an indicator to track whether the import tool's dispose() method was called
	private boolean importToolIsDisposed = false;
	
	private boolean toolInitialized = false;

	private static String toUppercaseWithValidation(String name) {
		if (name == null) {
			throw new RuntimeException("ELEMENT NAME is null");
		}
		String nameConvertedToUppercase = name.trim().toUpperCase();	
		ValidationResult validationResult = 
			NamingConventions.validate(nameConvertedToUppercase, NamingConventions.Type.ELEMENT_NAME);
		if (validationResult.getStatus() != ValidationResult.Status.OK) {
			String message = "invalid ELEMENT NAME: " + nameConvertedToUppercase + " (" + 
							 validationResult.getMessage() + ")";
			throw new RuntimeException(message);
		}
		return nameConvertedToUppercase;
	}	
	
	public RecordElementsImportToolProxy(IRecordElementsImportTool tool,
								 		 Properties importToolParameters) {
		super();		
		this.tool = tool;
		this.importToolParameters = importToolParameters;		
	}
	
	private Element createBasicElement(Element parent, String name, String baseName) {		
		
		// validate the element names and convert them to upper case
		String elementName = toUppercaseWithValidation(name);
		String baseElementName = toUppercaseWithValidation(baseName);
			
		// make sure the element does not yet exist unless it's a FILLER
		if (!elementName.equals("FILLER")) {
			Element duplicate = findElement(elementName);
			if (duplicate != null) {
				throw new RuntimeException("duplicate element name: " + elementName);
			}
		}
			
		Element element = SchemaFactory.eINSTANCE.createElement();
		element.setName(name);
		element.setBaseName(baseElementName); // null or different from name		
		allElements.add(element);
		
		if (parent == null) {
			rootElements.add(element);
		} else {
			parent.getChildren().add(element);
		}
		
		return element;
	}		

	public void disposeImportTool() {
		if (isImportToolDisposed()) {
			throw new IllegalStateException("import tool is already disposed");
		}
		tool.dispose();
		importToolIsDisposed = true;
	}

	private Element findElement(String name) {
		for (Element element : allElements) {
			if (element.getName().equalsIgnoreCase(name)) {
				return element;
			}
		}
		return null;
	}

	private void handleElement(Element parent, Object elementContext) {
		
		// get the data collector
		@SuppressWarnings("unchecked")
		IElementDataCollector<Object> dataCollector =
			(IElementDataCollector<Object>) dataCollectorRegistry.getDataCollector(elementContext.getClass());
		
		// get the element name
		String elementName = dataCollector.getName(elementContext);
		
		// get the base element name
		String baseName = dataCollector.getBaseName(elementContext);		
		
		// create the element and have it added to either the list of root elements or to its 
		// parent's list of children
		Element element = createBasicElement(parent, elementName, baseName);	
		
		// set some of the element's attributes
		element.setLevel(dataCollector.getLevel(elementContext));		
		element.setUsage(dataCollector.getUsage(elementContext));						
		element.setPicture(dataCollector.getPicture(elementContext));		
		element.setNullable(dataCollector.getIsNullable(elementContext));
		
		// deal with the REDEFINES clause, if specified
		String redefinedElementName = dataCollector.getRedefinedElementName(elementContext);
		if (redefinedElementName != null) {			
			Element redefinedElement = findElement(redefinedElementName);
			if (redefinedElement == null) {
				String message = 
					"logic error: element " + element.getName() + " redefines " + 
					redefinedElementName + ", but " + redefinedElementName + " was not found";
				throw new RuntimeException(message);
			} else if (redefinedElement.getLevel() != element.getLevel()) {
				String message = 
					"logic error: element " + element.getName() + " redefines " + 
					redefinedElementName + ", but " + element.getName() + "'s level number (" + 
					element.getLevel() + ") does not match that of " + redefinedElementName + " (" + 
					redefinedElement.getLevel() + ")";
				throw new RuntimeException(message);
			}
			element.setRedefines(redefinedElement);
		}
		
		// set the occurs specification if applicable
		short occurrenceCount = dataCollector.getOccurrenceCount(elementContext);
		if (occurrenceCount > 1) {
			
			OccursSpecification occursSpecification = 
				SchemaFactory.eINSTANCE.createOccursSpecification();
			element.setOccursSpecification(occursSpecification);
			
			occursSpecification.setCount(occurrenceCount);
			String dependsOnElementName = 
				dataCollector.getDependsOnElementName(elementContext);
			if (dependsOnElementName != null) {
				Element dependsOnElement = findElement(dependsOnElementName);
				if (dependsOnElement == null) {
					String message = 
						"logic error: element " + element.getName() + 
						"'s occurs-depending-on-element, " + dependsOnElementName + 
			   			", was not found in the record";
					throw new RuntimeException(message);
				}
				occursSpecification.setDependingOn(dependsOnElement);
			}
			
			List<String> indexElementBaseNames = new ArrayList<>();
				indexElementBaseNames.addAll(dataCollector.getIndexElementBaseNames(elementContext));
			if (!indexElementBaseNames.isEmpty()) {
				List<String> indexElementNames = new ArrayList<>();
				indexElementNames.addAll(dataCollector.getIndexElementNames(elementContext));
				if (indexElementBaseNames.size() != indexElementNames.size()) {
					throw new RuntimeException("");
				}
				for (int i = 0; i < indexElementBaseNames.size(); i++) {
					IndexElement indexElement = SchemaFactory.eINSTANCE.createIndexElement();
					occursSpecification.getIndexElements().add(indexElement);
					indexElement.setBaseName(indexElementBaseNames.get(i));
					indexElement.setName(indexElementNames.get(i));					
				}
			}
			
		}
		
		// set the element value, if any
		String value = dataCollector.getValue(elementContext);
		if (value != null) {
			element.setValue(value);
		}
		
		// deal with the element's subordinate elements, if any
		for (Object childElementContext : tool.getSubordinateElementContexts(elementContext)) {		
			handleElement(element, childElementContext);			
		}				
		
	}
	
	public List<Element> invokeImportTool(IDataEntryContext dataEntryContext) {
		
		rootElements.clear();
		allElements.clear();
		
		if (!toolInitialized) {
			tool.init(importToolParameters, dataCollectorRegistry);
			toolInitialized = true;
		}
		
		tool.setContext(dataEntryContext);
		
		Collection<?> elementContexts = tool.getRootElementContexts();
		for (Object elementContext : elementContexts) {					
			handleElement(null, elementContext);									
		}
		
		return rootElements;		
	}
	
	public boolean isImportToolDisposed() {
		return importToolIsDisposed;
	}

}
