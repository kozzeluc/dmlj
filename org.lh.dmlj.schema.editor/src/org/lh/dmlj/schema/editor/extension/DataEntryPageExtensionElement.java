package org.lh.dmlj.schema.editor.extension;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.lh.dmlj.schema.editor.importtool.AbstractDataEntryPage;

public class DataEntryPageExtensionElement extends AbstractExtensionElement {

	private AbstractDataEntryPage dataEntryPage;

	public DataEntryPageExtensionElement(IConfigurationElement configElement) {
		super(configElement);
		Assert.isTrue(configElement.getName()
								   .equals(ExtensionPointConstants.ELEMENT_DATA_ENTRY_PAGE), 
					  "wrong IConfigurationElement: " + configElement.getName());		
	}
	
	public AbstractDataEntryPage createDataEntryPage() {
		if (dataEntryPage != null) {
			return dataEntryPage;
		}
		try {
			String propertyName = ExtensionPointConstants.ATTRIBUTE_CLASS;
			Object executableExtension =
				configElement.createExecutableExtension(propertyName);
			dataEntryPage = (AbstractDataEntryPage) executableExtension;			
			return dataEntryPage;
		} catch (CoreException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	public String getImplementingClass() {
		return Util.getAttribute(configElement, 
				  				 ExtensionPointConstants.ATTRIBUTE_CLASS, null);
	}	

	public String getMessage() {
		return Util.getAttribute(configElement, 
								 ExtensionPointConstants.ATTRIBUTE_MESSAGE, 
								 "[no message available]");
	}
	
}