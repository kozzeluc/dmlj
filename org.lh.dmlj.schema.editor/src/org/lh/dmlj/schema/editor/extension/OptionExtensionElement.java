package org.lh.dmlj.schema.editor.extension;

import org.eclipse.core.runtime.IConfigurationElement;

public class OptionExtensionElement extends AbstractExtensionElement {

	public OptionExtensionElement(IConfigurationElement configElement) {
		super(configElement);		
	}
	
	public String getCheckButtonLabel() {
		return Util.getAttribute(configElement, 
 				 				 ExtensionPointConstants.ATTRIBUTE_CHECK_BUTTON_LABEL, null);
	}
	
	public String getGroup() {
		return Util.getAttribute(configElement, 
 				 				 ExtensionPointConstants.ATTRIBUTE_GROUP, "");
	}	
	
	public boolean getIdmsntwkOnly() {
		String p = Util.getAttribute(configElement, 
 				 				     ExtensionPointConstants.ATTRIBUTE_IDMSNTWK_ONLY, null);
		return Boolean.valueOf(p).booleanValue();
	}
	
	public boolean getInitialValue() {
		String p = Util.getAttribute(configElement, 
 				 				     ExtensionPointConstants.ATTRIBUTE_INITIAL_VALUE, null);
		return Boolean.valueOf(p).booleanValue();
	}
	
	public String getMutuallyExclusiveWith() {
		return Util.getAttribute(configElement, 
 				 				 ExtensionPointConstants.ATTRIBUTE_MUTUALLY_EXCLUSIVE_WITH, "");
	}
	
	@Override
	public int hashCode() {		
		return getName().hashCode();
	}

}