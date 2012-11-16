package org.lh.dmlj.schema.editor.extension;

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

public class LayoutManagerExtensionElement extends AbstractExtensionElement {

	private ImageDescriptor          	imageDescriptor;
	private String 					 	implementingClass;
	private AbstractRecordLayoutManager layoutManager;
	private Properties				 	parameters;
	private List<ValidForExtensionElement> 	validForDescriptors = new ArrayList<>();
	
	public LayoutManagerExtensionElement(IExtension extension,
								   		 IConfigurationElement configElement) {
		
		super(extension, configElement);
		Assert.isTrue(configElement.getName()
								   .equals(ExtensionPointConstants.ELEMENT_LAYOUT_MANAGER), 
					  "wrong IConfigurationElement: " + configElement.getName());
		implementingClass = 
			Util.getAttribute(configElement, 
							  ExtensionPointConstants.ATTRIBUTE_CLASS, null);		
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

	public Properties getParameters() {
		if (parameters == null) {		
			parameters = 
				Util.getResourceAsProperties(extension, configElement, 
											 ExtensionPointConstants.ATTRIBUTE_PARAMETERS);
		}
		return parameters;
	}
	
	public List<ValidForExtensionElement> getValidForDescriptors() {
		return validForDescriptors;
	}

	@Override
	public String toString() {
		StringBuilder p = new StringBuilder();
		p.append(ExtensionPointConstants.ELEMENT_LAYOUT_MANAGER);
		p.append(":  (defining plug-in:" + getPluginId() + ") ");
		p.append(ExtensionPointConstants.ATTRIBUTE_ID + "=" + getId());
		p.append(" ");
		p.append(ExtensionPointConstants.ATTRIBUTE_NAME + "=" + getName());
		p.append(" ");		
		p.append(ExtensionPointConstants.ATTRIBUTE_CLASS + "=" + 
				 implementingClass);
		p.append(" ");
		p.append(ExtensionPointConstants.ATTRIBUTE_DESCRIPTION + "=" + 
				 getDescription());		
		return p.toString();
	}

	public boolean isValidFor(String schemaName, short schemaVersion) {
		if (validForDescriptors.isEmpty()) {
			return true;
		}
		for (ValidForExtensionElement validFor : validForDescriptors) {
			if (validFor.isValidFor(schemaName, schemaVersion)) {
				return true;
			}
		}
		return false;
	}	
	
}