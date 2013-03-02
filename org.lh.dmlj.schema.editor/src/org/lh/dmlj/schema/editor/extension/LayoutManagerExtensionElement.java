package org.lh.dmlj.schema.editor.extension;

import static org.lh.dmlj.schema.editor.extension.ExtensionPointConstants.ELEMENT_VALID_FOR;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.resource.ImageDescriptor;
import org.lh.dmlj.schema.editor.importtool.AbstractRecordLayoutManager;

public class LayoutManagerExtensionElement extends AbstractExtensionElement {

	private ImageDescriptor          	   imageDescriptor;
	private AbstractRecordLayoutManager    layoutManager;
	private Properties				 	   parameters;
	private List<ValidForExtensionElement> validForExtensionElements;
	
	public LayoutManagerExtensionElement(IConfigurationElement configElement) {
		
		super(configElement);
		Assert.isTrue(configElement.getName()
								   .equals(ExtensionPointConstants.ELEMENT_LAYOUT_MANAGER), 
					  "wrong IConfigurationElement: " + configElement.getName());
	}	

	public Properties getConfiguredParameters() {
		if (parameters == null) {		
			parameters = 
				Util.getResourceAsProperties(configElement, 
											 ExtensionPointConstants.ATTRIBUTE_PARAMETERS);
		}
		return parameters;
	}

	public ImageDescriptor getImageDescriptor() {
		if (imageDescriptor == null) {
			imageDescriptor = 
				Util.getImageDescriptor(configElement, 
									    ExtensionPointConstants.ATTRIBUTE_IMAGE);
		}
		return imageDescriptor;
	}	
	
	public String getImplementingClass() {
		return Util.getAttribute(configElement, 
				  				 ExtensionPointConstants.ATTRIBUTE_CLASS, null);
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

	public List<ValidForExtensionElement> getValidForExtensionElements() {
		// get the schemas to which the layout manager applies;
		// if nothing is configured, then the layout manager is
		// valid for ALL schemas
		if (validForExtensionElements == null) {
			// create the list...
			validForExtensionElements = new ArrayList<>();
			// ...and collect the valid for elements:			
			List<ValidForExtensionElement> validFors =
				ExtensionElementFactory.getExtensionElements(configElement, 
															 ELEMENT_VALID_FOR, 
															 ValidForExtensionElement.class);
			validForExtensionElements.addAll(validFors);			
		}
		return validForExtensionElements;
	}

	public boolean isPromptForPropertiesFile() {
		String promptForPropertiesFile = 
			Util.getAttribute(configElement, 
		  			 		  ExtensionPointConstants.ATTRIBUTE_PROMPT_FOR_PROPERTIES_FILE, 
		  			 		  "false");
		return Boolean.valueOf(promptForPropertiesFile);	
	}
	
	public boolean isValidFor(String schemaName, short schemaVersion) {
		if (getValidForExtensionElements().isEmpty()) {
			return true;
		}
		for (ValidForExtensionElement validFor : validForExtensionElements) {
			if (validFor.isValidFor(schemaName, schemaVersion)) {
				return true;
			}
		}
		return false;
	}	
	
}