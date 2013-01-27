package org.lh.dmlj.schema.editor.extension;

import static org.lh.dmlj.schema.editor.extension.ExtensionPointConstants.ELEMENT_OPTIONS;
import static org.lh.dmlj.schema.editor.extension.ExtensionPointConstants.ELEMENT_POST_OPTIONS_PAGES;
import static org.lh.dmlj.schema.editor.extension.ExtensionPointConstants.ELEMENT_PRE_OPTIONS_PAGES;

import java.util.Properties;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.lh.dmlj.schema.editor.importtool.ISchemaImportTool;

public class ImportToolExtensionElement extends AbstractExtensionElement {

	private OptionsExtensionElement		     optionsExtensionElement;
	private Properties					  	 parameters;
	private PostOptionsPagesExtensionElement postOptionsPagesExtensionElement;
	private PreOptionsPagesExtensionElement  preOptionsPagesExtensionElement;
	private ISchemaImportTool 			  	 schemaImportTool;	
	
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

	public OptionsExtensionElement getOptionsExtensionElement() {
		if (optionsExtensionElement == null) {
			optionsExtensionElement =
				ExtensionElementFactory.getExtensionElements(configElement, 
															 ELEMENT_OPTIONS, 
															 OptionsExtensionElement.class)
									   .get(0);			
		}
		return optionsExtensionElement;
	}
	
	public PostOptionsPagesExtensionElement getPostOptionsDataEntryPageExtensionElement() {
		if (postOptionsPagesExtensionElement == null) {			
			postOptionsPagesExtensionElement =
				ExtensionElementFactory.getExtensionElements(configElement, 
															 ELEMENT_POST_OPTIONS_PAGES, 
															 PostOptionsPagesExtensionElement.class)
									   .get(0);
		}
		return postOptionsPagesExtensionElement;
	}	
	
	public PreOptionsPagesExtensionElement getPreOptionsDataEntryPageExtensionElement() {
		if (preOptionsPagesExtensionElement == null) {			
			preOptionsPagesExtensionElement =
				ExtensionElementFactory.getExtensionElements(configElement, 
															 ELEMENT_PRE_OPTIONS_PAGES, 
															 PreOptionsPagesExtensionElement.class)
									   .get(0);
		}
		return preOptionsPagesExtensionElement;
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