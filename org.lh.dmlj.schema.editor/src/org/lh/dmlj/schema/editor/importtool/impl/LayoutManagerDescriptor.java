package org.lh.dmlj.schema.editor.importtool.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.lh.dmlj.schema.editor.importtool.AbstractRecordLayoutManager;

public class LayoutManagerDescriptor {

	private IConfigurationElement 	 	configElement;
	private String 					 	description;
	private String 					 	id;
	private ImageDescriptor          	imageDescriptor;
	private String 					 	implementingClass;
	private AbstractRecordLayoutManager layoutManager;
	private String 				     	name;	
	private String 					 	pluginId;
	private Properties				 	parameters;
	private List<ValidForDescriptor> 	validForDescriptors = new ArrayList<>();
	
	public LayoutManagerDescriptor(IExtension extension,
								   IConfigurationElement configElement) {
		super();
		Assert.isTrue(configElement.getName()
								   .equals(ExtensionPointConstants.ELEMENT_LAYOUT_MANAGER), 
					  "wrong IConfigurationElement: " + configElement.getName());
		this.configElement = configElement;
		pluginId = extension.getNamespaceIdentifier();
		id = Util.getAttribute(configElement, 
							   ExtensionPointConstants.ATTRIBUTE_ID, null);
		name = Util.getAttribute(configElement, 
								 ExtensionPointConstants.ATTRIBUTE_NAME, null);
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

	public ImageDescriptor getImageDescriptor() {
		if (imageDescriptor != null) {
			return imageDescriptor;
		}
		String imagePath = 
			configElement.getAttribute(ExtensionPointConstants.ATTRIBUTE_IMAGE);
		if (imagePath == null) {
			return null;
		}
		IExtension extension = configElement.getDeclaringExtension();
		String extendingPluginId = extension.getNamespaceIdentifier();
		imageDescriptor = 
			AbstractUIPlugin.imageDescriptorFromPlugin(extendingPluginId,
													   imagePath);
		return imageDescriptor;
	}	
	
	public String getImplementingClass() {
		return implementingClass;
	}

	public AbstractRecordLayoutManager getLayoutManager() {
		if (layoutManager != null) {
			return layoutManager;
		}
		try {
			String propertyName = ExtensionPointConstants.ATTRIBUTE_CLASS;
			Object executableExtension =
				configElement.createExecutableExtension(propertyName);
			layoutManager = (AbstractRecordLayoutManager) executableExtension;
			return layoutManager;
		} catch (CoreException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	public String getName() {
		return name;
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
				ClassLoader cl = getLayoutManager().getClass().getClassLoader(); 		
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
	
	public List<ValidForDescriptor> getValidForDescriptors() {
		return validForDescriptors;
	}

	public void setDescription(String description) {
		this.description = description;
	}	

	@Override
	public String toString() {
		StringBuilder p = new StringBuilder();
		p.append(ExtensionPointConstants.ELEMENT_LAYOUT_MANAGER);
		p.append(":  (defining plug-in:" + pluginId + ") ");
		p.append(ExtensionPointConstants.ATTRIBUTE_ID + "=" + id);
		p.append(" ");
		p.append(ExtensionPointConstants.ATTRIBUTE_NAME + "=" + name);
		p.append(" ");		
		p.append(ExtensionPointConstants.ATTRIBUTE_CLASS + "=" + 
				 implementingClass);
		p.append(" ");
		p.append(ExtensionPointConstants.ATTRIBUTE_DESCRIPTION + "=" + 
				 description);		
		return p.toString();
	}

	public boolean isValidFor(String schemaName, short schemaVersion) {
		if (validForDescriptors.isEmpty()) {
			return true;
		}
		for (ValidForDescriptor validFor : validForDescriptors) {
			if (validFor.isValidFor(schemaName, schemaVersion)) {
				return true;
			}
		}
		return false;
	}	
	
}