package org.lh.dmlj.schema.editor.extension;

import static org.lh.dmlj.schema.editor.extension.ExtensionPointConstants.ELEMENT_OPTION;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;

public class OptionsExtensionElement extends AbstractExtensionElement {

	private List<OptionExtensionElement> optionExtensionElements;
	
	public OptionsExtensionElement(IConfigurationElement configElement) {
		super(configElement);		
	}
	
	public List<OptionExtensionElement> getOptionExtensionElements() {		
		if (optionExtensionElements == null) {
			optionExtensionElements = new ArrayList<>();
			List<OptionExtensionElement> options =
				ExtensionElementFactory.getExtensionElements(configElement, 
															 ELEMENT_OPTION, 
															 OptionExtensionElement.class);		
			optionExtensionElements.addAll(options);
		}
		return optionExtensionElements;
	}	

}