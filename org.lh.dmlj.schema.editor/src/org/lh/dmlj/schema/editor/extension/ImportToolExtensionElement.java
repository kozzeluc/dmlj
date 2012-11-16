package org.lh.dmlj.schema.editor.extension;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.lh.dmlj.schema.editor.importtool.ISchemaImportTool;

public class ImportToolExtensionElement extends AbstractExtensionElement {

	private String 						  implementingClass;
	private List<DataEntryPageExtensionElement> pageDescriptors = new ArrayList<>();
	private Properties					  parameters;
	private ISchemaImportTool 			  schemaImportTool;
	private String 						  source;
	
	public ImportToolExtensionElement(IExtension extension,
									  IConfigurationElement configElement) {
		
		super(extension, configElement);
		Assert.isTrue(configElement.getName()
								   .equals(ExtensionPointConstants.ELEMENT_IMPORT_TOOL), 
					  "wrong IConfigurationElement: " + configElement.getName());		
		source = 
			Util.getAttribute(configElement, 
						      ExtensionPointConstants.ATTRIBUTE_SOURCE, null);
		implementingClass = 
			Util.getAttribute(configElement, 
							  ExtensionPointConstants.ATTRIBUTE_CLASS, null);		
	}	

	public String getImplementingClass() {
		return implementingClass;
	}

	public List<DataEntryPageExtensionElement> getPageDescriptors() {
		return pageDescriptors;
	}
	
	public Properties getParameters() {
		if (parameters == null) {		
			parameters = 
				Util.getResourceAsProperties(extension, configElement, 
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
		return source;
	}

	@Override
	public String toString() {
		StringBuilder p = new StringBuilder();
		p.append(ExtensionPointConstants.ELEMENT_IMPORT_TOOL);
		p.append(":  (defining plug-in:" + getPluginId() + ") ");
		p.append(ExtensionPointConstants.ATTRIBUTE_ID + "=" + getId());
		p.append(" ");
		p.append(ExtensionPointConstants.ATTRIBUTE_NAME + "=" + getName());
		p.append(" ");
		p.append(ExtensionPointConstants.ATTRIBUTE_SOURCE + "=" + source);
		p.append(" ");
		p.append(ExtensionPointConstants.ATTRIBUTE_CLASS + "=" + 
				 implementingClass);
		p.append(" ");
		p.append(ExtensionPointConstants.ATTRIBUTE_DESCRIPTION + "=" + 
				 getDescription());		
		for (DataEntryPageExtensionElement pageDescriptor : pageDescriptors) {
			p.append("\n ");
			p.append(pageDescriptor.toString());
		}
		return p.toString();
	}	
	
}