package org.lh.dmlj.schema.editor.importtool.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.lh.dmlj.schema.editor.importtool.ISchemaImportTool;

public class ImportToolDescriptor {

	private IConfigurationElement 		  configElement;
	private String 						  description;
	private String 						  id;
	private String 						  implementingClass;
	private String 						  name;	
	private List<DataEntryPageDescriptor> pageDescriptors = 
		new ArrayList<>();
	private Properties					  parameters;
	private String 						  pluginId;
	private ISchemaImportTool 			  schemaImportTool;
	private String 						  source;
	
	public ImportToolDescriptor(IExtension extension,
								IConfigurationElement configElement) {
		super();
		Assert.isTrue(configElement.getName()
								   .equals(ExtensionPointConstants.ELEMENT_IMPORT_TOOL), 
					  "wrong IConfigurationElement: " + configElement.getName());
		this.configElement = configElement;
		pluginId = extension.getNamespaceIdentifier();
		id = Util.getAttribute(configElement, 
							   ExtensionPointConstants.ATTRIBUTE_ID, null);
		name = Util.getAttribute(configElement, 
								 ExtensionPointConstants.ATTRIBUTE_NAME, null);
		source = 
			Util.getAttribute(configElement, 
						      ExtensionPointConstants.ATTRIBUTE_SOURCE, null);
		implementingClass = 
			Util.getAttribute(configElement, 
							  ExtensionPointConstants.ATTRIBUTE_CLASS, null);
		description = 
			Util.getAttribute(configElement, 
							  ExtensionPointConstants.ATTRIBUTE_DESCRIPTION, 
							  null);
	}
	
	public String getDescription() {
		return description;
	}

	public String getId() {
		return id;
	}

	public String getImplementingClass() {
		return implementingClass;
	}

	public String getName() {
		return name;
	}	
	
	public List<DataEntryPageDescriptor> getPageDescriptors() {
		return pageDescriptors;
	}
	
	public Properties getParameters() {
		if (parameters != null) {
			return parameters;
		}
		parameters = new Properties();
		String propertyName = ExtensionPointConstants.ATTRIBUTE_PARAMETERS;
		String path = Util.getAttribute(configElement, propertyName, "");
		if (!path.equals("")) {
			try {
				ClassLoader cl = 
					getSchemaImportTool().getClass().getClassLoader(); 		
				InputStream in = cl.getResourceAsStream(path);
				if (in != null) {									
					parameters.load(in);
					in.close();
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}			
		}
		return parameters;
	}	

	public String getPluginId() {
		return pluginId;
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

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		StringBuilder p = new StringBuilder();
		p.append(ExtensionPointConstants.ELEMENT_IMPORT_TOOL);
		p.append(":  (defining plug-in:" + pluginId + ") ");
		p.append(ExtensionPointConstants.ATTRIBUTE_ID + "=" + id);
		p.append(" ");
		p.append(ExtensionPointConstants.ATTRIBUTE_NAME + "=" + name);
		p.append(" ");
		p.append(ExtensionPointConstants.ATTRIBUTE_SOURCE + "=" + source);
		p.append(" ");
		p.append(ExtensionPointConstants.ATTRIBUTE_CLASS + "=" + 
				 implementingClass);
		p.append(" ");
		p.append(ExtensionPointConstants.ATTRIBUTE_DESCRIPTION + "=" + 
				 description);		
		for (DataEntryPageDescriptor pageDescriptor : pageDescriptors) {
			p.append("\n ");
			p.append(pageDescriptor.toString());
		}
		return p.toString();
	}	
	
}