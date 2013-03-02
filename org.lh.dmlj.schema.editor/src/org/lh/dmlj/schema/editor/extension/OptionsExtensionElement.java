package org.lh.dmlj.schema.editor.extension;

import static org.lh.dmlj.schema.editor.extension.ExtensionPointConstants.ELEMENT_OPTION;
import static org.lh.dmlj.schema.editor.extension.ExtensionPointConstants.ELEMENT_OPTION_GROUP;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;

public class OptionsExtensionElement extends AbstractExtensionElement {

	private List<OptionExtensionElement> 	  optionExtensionElements;
	private List<OptionGroupExtensionElement> optionGroupExtensionElements;
	
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
	
	public List<OptionGroupExtensionElement> getOptionGroupExtensionElements() {		
		if (optionGroupExtensionElements == null) {
			optionGroupExtensionElements = new ArrayList<>();
			List<OptionGroupExtensionElement> optionGroups =
				ExtensionElementFactory.getExtensionElements(configElement, 
															 ELEMENT_OPTION_GROUP, 
															 OptionGroupExtensionElement.class);		
			optionGroupExtensionElements.addAll(optionGroups);
		}
		return optionGroupExtensionElements;
	}	

}