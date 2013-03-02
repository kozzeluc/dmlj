package org.lh.dmlj.schema.editor.extension;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IConfigurationElement;

public class OptionGroupExtensionElement extends AbstractExtensionElement {

	public OptionGroupExtensionElement(IConfigurationElement configElement) {
		super(configElement);
		Assert.isTrue(configElement.getName()
								   .equals(ExtensionPointConstants.ELEMENT_OPTION_GROUP), 
					  "wrong IConfigurationElement: " + configElement.getName());
	}
	
	public String getLabel() {
		return Util.getAttribute(configElement, 
 				 				 ExtensionPointConstants.ATTRIBUTE_LABEL, null);
	}	

}