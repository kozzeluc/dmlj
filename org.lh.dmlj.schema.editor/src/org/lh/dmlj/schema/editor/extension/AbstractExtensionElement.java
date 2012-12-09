package org.lh.dmlj.schema.editor.extension;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;

public abstract class AbstractExtensionElement {

	protected final IConfigurationElement configElement;
	protected final IExtension 			  extension;	
	
	public AbstractExtensionElement(IConfigurationElement configElement) {
		super();		
		this.configElement = configElement;
		this.extension = configElement.getDeclaringExtension();
	}

	public final IConfigurationElement getConfigurationElement() {
		return configElement;
	}
	
	public final String getDescription() {
		return Util.getAttribute(configElement, 
				  				 ExtensionPointConstants.ATTRIBUTE_DESCRIPTION, 
				  				 ""); // not in every extension element
	}

	public final String getId() {
		return Util.getAttribute(configElement, 
				   				 ExtensionPointConstants.ATTRIBUTE_ID, 
				   				 ""); // not in every extension element
	}

	public final String getName() {
		return Util.getAttribute(configElement, 
				 				 ExtensionPointConstants.ATTRIBUTE_NAME, 
				 				 ""); // not in every extension element
	}

	public final String getPluginId() {
		return extension.getNamespaceIdentifier();
	}

}