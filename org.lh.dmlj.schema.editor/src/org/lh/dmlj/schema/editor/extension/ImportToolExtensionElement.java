package org.lh.dmlj.schema.editor.extension;

import static org.lh.dmlj.schema.editor.extension.ExtensionPointConstants.ELEMENT_DATA_ENTRY_PAGE;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.lh.dmlj.schema.editor.importtool.ISchemaImportTool;

public class ImportToolExtensionElement extends AbstractExtensionElement {

	private List<DataEntryPageExtensionElement> dataEntryPageExtensionElements;
	private Properties					  		parameters;
	private ISchemaImportTool 			  		schemaImportTool;	
	
	public ImportToolExtensionElement(IConfigurationElement configElement) {	
		super(configElement);
		Assert.isTrue(configElement.getName()
								   .equals(ExtensionPointConstants.ELEMENT_IMPORT_TOOL), 
					  "wrong IConfigurationElement: " + configElement.getName());		
	}	

	public String getImplementingClass() {
		return Util.getAttribute(configElement, 
				  ExtensionPointConstants.ATTRIBUTE_CLASS, null);
	}

	public List<DataEntryPageExtensionElement> getDataEntryPageExtensionElements() {
		if (dataEntryPageExtensionElements == null) {
			// create the list...
			dataEntryPageExtensionElements = new ArrayList<>();
			// ...and collect the data entry pages:			
			List<DataEntryPageExtensionElement> pages =
				ExtensionElementFactory.getExtensionElements(configElement, 
															 ELEMENT_DATA_ENTRY_PAGE, 
															 DataEntryPageExtensionElement.class);
			dataEntryPageExtensionElements.addAll(pages);
						
		}
		return dataEntryPageExtensionElements;
	}
	
	public Properties getParameters() {
		if (parameters == null) {		
			parameters = 
				Util.getResourceAsProperties(configElement, 
											 ExtensionPointConstants.ATTRIBUTE_PARAMETERS);
		}
		return parameters;
	}	

	public ISchemaImportTool getSchemaImportTool() {
		if (schemaImportTool != null) {
			return schemaImportTool;
		}
		try {
			String propertyName = ExtensionPointConstants.ATTRIBUTE_CLASS;
			Object executableExtension =
				configElement.createExecutableExtension(propertyName);
			schemaImportTool = (ISchemaImportTool) executableExtension;
			return schemaImportTool;
		} catch (CoreException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	public String getSource() {
		return Util.getAttribute(configElement, 
			      				 ExtensionPointConstants.ATTRIBUTE_SOURCE, null);
	}
	
}