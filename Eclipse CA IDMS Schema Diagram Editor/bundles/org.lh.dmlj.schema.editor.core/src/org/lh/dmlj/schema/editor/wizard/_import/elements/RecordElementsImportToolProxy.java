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
package org.lh.dmlj.schema.editor.wizard._import.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.editor.common.ElementValueTransformer;
import org.lh.dmlj.schema.editor.common.NamingConventions;
import org.lh.dmlj.schema.editor.common.ValidationResult;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;
import org.lh.dmlj.schema.editor.importtool.IElementDataCollector;
import org.lh.dmlj.schema.editor.importtool.elements.IRecordElementsDataCollectorRegistry;
import org.lh.dmlj.schema.editor.importtool.elements.IRecordElementsImportTool;

public final class RecordElementsImportToolProxy {
	private final IRecordElementsImportTool tool;
	private final Properties toolParameters; // optional parameters configured in the import tool's defining plug-in	
		
	private ArrayList<Element> rootElements = new ArrayList<>();
	private ArrayList<Element> allElements = new ArrayList<>(); // a list with ALL elements that we keep to easily find elements
	private IRecordElementsDataCollectorRegistry dataCollectorRegistry = new RecordElementsDataCollectorRegistry();
	private boolean toolInitialized = false;
	private boolean toolDisposed = false;

	private static String toUppercaseWithValidation(String name) {
		if (name == null) {
			throw new IllegalStateException("ELEMENT NAME is null");
		}
		var nameConvertedToUppercase = name.trim().toUpperCase();	
		var validationResult = NamingConventions.validate(nameConvertedToUppercase, NamingConventions.Type.ELEMENT_NAME);
		if (validationResult.getStatus() != ValidationResult.Status.OK) {
			var message = "invalid ELEMENT NAME: " + nameConvertedToUppercase + " (" + validationResult.getMessage() + ")";
			throw new IllegalStateException(message);
		}
		return nameConvertedToUppercase;
	}
	
	public RecordElementsImportToolProxy(IRecordElementsImportTool tool, Properties toolParameters) {
		this.tool = tool;
		this.toolParameters = toolParameters;		
	}
	
	private Element createBasicElement(Element parent, String name, String baseName) {
		var elementName = toUppercaseWithValidation(name);
		var baseElementName = toUppercaseWithValidation(baseName);
			
		// make sure the element does not yet exist unless it's a FILLER
		if (!elementName.equals("FILLER")) {
			var duplicate = findElement(elementName);
			if (duplicate != null) {
				throw new IllegalStateException("duplicate element name: " + elementName);
			}
		}
			
		var element = SchemaFactory.eINSTANCE.createElement();
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
		toolDisposed = true;
	}

	private Element findElement(String name) {
		return allElements.stream()
				.filter(e -> e.getName().equalsIgnoreCase(name))
				.findFirst()
				.orElse(null);
	}

	private void handleElement(Element parent, Object elementContext) {
		@SuppressWarnings("unchecked")
		var dataCollector = (IElementDataCollector<Object>) dataCollectorRegistry.getDataCollector(elementContext.getClass());
				
		var elementName = dataCollector.getName(elementContext);
		var baseName = dataCollector.getBaseName(elementContext);
		
		// create the element and have it added to either the list of root elements or to its parent's list of
		// children and set some of the element's attributes
		var element = createBasicElement(parent, elementName, baseName);
		element.setLevel(dataCollector.getLevel(elementContext));		
		element.setUsage(dataCollector.getUsage(elementContext));						
		element.setPicture(dataCollector.getPicture(elementContext));		
		element.setNullable(dataCollector.getIsNullable(elementContext));
		
		// deal with the REDEFINES clause, if specified
		var redefinedElementName = dataCollector.getRedefinedElementName(elementContext);
		if (redefinedElementName != null) {			
			var redefinedElement = findElement(redefinedElementName);
			if (redefinedElement == null) {
				var message = "logic error: element " + element.getName() + " redefines " + redefinedElementName +
						", but " + redefinedElementName + " was not found";
				throw new IllegalStateException(message);
			} else if (redefinedElement.getLevel() != element.getLevel()) {
				var message = "logic error: element " + element.getName() + " redefines " + redefinedElementName +
						", but " + element.getName() + "'s level number (" + element.getLevel() + 
						") does not match that of " + redefinedElementName + " (" + redefinedElement.getLevel() + ")";
				throw new IllegalStateException(message);
			}
			element.setRedefines(redefinedElement);
		}
		
		// set the occurs specification if applicable
		var occurrenceCount = dataCollector.getOccurrenceCount(elementContext);
		if (occurrenceCount > 1) {
			var occursSpecification = SchemaFactory.eINSTANCE.createOccursSpecification();
			element.setOccursSpecification(occursSpecification);
			
			occursSpecification.setCount(occurrenceCount);
			var dependsOnElementName = dataCollector.getDependsOnElementName(elementContext);
			if (dependsOnElementName != null) {
				var dependsOnElement = findElement(dependsOnElementName);
				if (dependsOnElement == null) {
					var message = "logic error: element " + element.getName() + "'s occurs-depending-on-element, " +
							dependsOnElementName + ", was not found in the record";
					throw new IllegalStateException(message);
				}
				occursSpecification.setDependingOn(dependsOnElement);
			}
			
			var indexElementBaseNames = new ArrayList<String>();
			indexElementBaseNames.addAll(dataCollector.getIndexElementBaseNames(elementContext));
			if (!indexElementBaseNames.isEmpty()) {
				var indexElementNames = new ArrayList<String>();
				indexElementNames.addAll(dataCollector.getIndexElementNames(elementContext));
				if (indexElementBaseNames.size() != indexElementNames.size()) {
					throw new IllegalStateException("");
				}
				for (var i = 0; i < indexElementBaseNames.size(); i++) {
					var indexElement = SchemaFactory.eINSTANCE.createIndexElement();
					occursSpecification.getIndexElements().add(indexElement);
					indexElement.setBaseName(indexElementBaseNames.get(i));
					indexElement.setName(indexElementNames.get(i));					
				}
			}
		}
		
		// set the element value, if any
		var values = dataCollector.getValues(elementContext);
		element.setValue(ElementValueTransformer.toValueString(values));
		
		// deal with the element's subordinate elements, if any
		for (var childElementContext : tool.getSubordinateElementContexts(elementContext)) {		
			handleElement(element, childElementContext);			
		}
	}
	
	public List<Element> invokeImportTool(IDataEntryContext dataEntryContext) {
		rootElements.clear();
		allElements.clear();
		
		if (!toolInitialized) {
			tool.init(toolParameters, dataCollectorRegistry);
			toolInitialized = true;
		}
		
		tool.setContext(dataEntryContext);
		
		var elementContexts = tool.getRootElementContexts().elements();
		for (var elementContext : elementContexts) {					
			handleElement(null, elementContext);									
		}
		
		return rootElements;		
	}
	
	public boolean isImportToolDisposed() {
		return toolDisposed;
	}

}
