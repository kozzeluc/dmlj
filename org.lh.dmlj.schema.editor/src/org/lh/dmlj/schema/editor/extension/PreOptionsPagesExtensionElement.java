package org.lh.dmlj.schema.editor.extension;

import static org.lh.dmlj.schema.editor.extension.ExtensionPointConstants.ELEMENT_DATA_ENTRY_PAGE;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;

public class PreOptionsPagesExtensionElement extends AbstractExtensionElement {
	
	private List<DataEntryPageExtensionElement> dataEntryPageExtensionElements;	

	public PreOptionsPagesExtensionElement(IConfigurationElement configElement) {
		super(configElement);		
	}
	
	public List<DataEntryPageExtensionElement> getDataEntryPageExtensionElements() {		
		if (dataEntryPageExtensionElements == null) {
			dataEntryPageExtensionElements = new ArrayList<>();
			List<DataEntryPageExtensionElement> pages =
				ExtensionElementFactory.getExtensionElements(configElement, 
															 ELEMENT_DATA_ENTRY_PAGE, 
															 DataEntryPageExtensionElement.class);		
			dataEntryPageExtensionElements.addAll(pages);
		}
		return dataEntryPageExtensionElements;
	}

}