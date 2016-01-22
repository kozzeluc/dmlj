/**
 * Copyright (C) 2014  Luc Hermans
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
package org.lh.dmlj.schema.editor.extension;

import static org.lh.dmlj.schema.editor.extension.ExtensionPointConstants.ELEMENT_DATA_ENTRY_PAGE;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.lh.dmlj.schema.editor.importtool.elements.IRecordElementsImportTool;

public class RecordElementsImportToolExtensionElement extends AbstractExtensionElement {

	private Properties parameters;
	private List<DataEntryPageExtensionElement> dataEntryPageExtensionElements;	
	private IRecordElementsImportTool recordElementsImportTool;	
	
	public RecordElementsImportToolExtensionElement(IConfigurationElement configElement) {	
		super(configElement);
		Assert.isTrue(configElement.getName()
								   .equals(ExtensionPointConstants.ELEMENT_IMPORT_TOOL), 
					  "wrong IConfigurationElement: " + configElement.getName());		
	}	

	public List<DataEntryPageExtensionElement> getDataEntryPageExtensionElements() {		
		if (dataEntryPageExtensionElements == null) {
			dataEntryPageExtensionElements = new ArrayList<>();
			List<DataEntryPageExtensionElement> pages =
				ExtensionElementFactory.getExtensionElements(configElement, 
															 ELEMENT_DATA_ENTRY_PAGE, 
															 DataEntryPageExtensionElement.class);		
			dataEntryPageExtensionElements.addAll(pages);
		}
		return dataEntryPageExtensionElements;
	}

	public String getImplementingClass() {
		return Util.getAttribute(configElement, 
				  				 ExtensionPointConstants.ATTRIBUTE_CLASS, null);
	}	
	
	public Properties getParameters() {
		if (parameters == null) {		
			parameters = 
				Util.getResourceAsProperties(configElement, 
											 ExtensionPointConstants.ATTRIBUTE_PARAMETERS);
		}
		return parameters;
	}	

	public IRecordElementsImportTool getRecordElementsImportTool() {
		if (recordElementsImportTool != null) {
			return recordElementsImportTool;
		}
		try {
			String propertyName = ExtensionPointConstants.ATTRIBUTE_CLASS;
			Object executableExtension = configElement.createExecutableExtension(propertyName);
			recordElementsImportTool = (IRecordElementsImportTool) executableExtension;
			return recordElementsImportTool;
		} catch (CoreException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	public String getSource() {
		return Util.getAttribute(configElement, ExtensionPointConstants.ATTRIBUTE_SOURCE, null);
	}
	
}
