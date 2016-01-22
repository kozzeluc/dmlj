/**
 * Copyright (C) 2013  Luc Hermans
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program.  If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact information: kozzeluc@gmail.com.
 */
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
