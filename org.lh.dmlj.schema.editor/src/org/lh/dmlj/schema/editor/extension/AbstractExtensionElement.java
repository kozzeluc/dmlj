package org.lh.dmlj.schema.editor.extension;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;

public abstract class AbstractExtensionElement {

	protected IConfigurationElement configElement;
	protected IExtension 			extension;	
	
	public AbstractExtensionElement(IExtension extension,
									IConfigurationElement configElement) {
		super();
		this.extension = extension;
		this.configElement = configElement;
	}

	public final String getDescription() {
		return Util.getAttribute(configElement, 
				  				 ExtensionPointConstants.ATTRIBUTE_DESCRIPTION, 
				  				 null);
	}

	public final String getId() {
		return Util.getAttribute(configElement, 
				   				 ExtensionPointConstants.ATTRIBUTE_ID, null);
	}

	public final String getName() {
		return Util.getAttribute(configElement, 
				 				 ExtensionPointConstants.ATTRIBUTE_NAME, null);
	}

	public final String getPluginId() {
		return extension.getNamespaceIdentifier();
	}

}