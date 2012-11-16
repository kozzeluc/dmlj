package org.lh.dmlj.schema.editor.extension;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.lh.dmlj.schema.editor.importtool.AbstractDataEntryPage;

public class DataEntryPageExtensionElement {

	private IConfigurationElement configElement;
	private AbstractDataEntryPage dataEntryPage;
	private String 				  id;
	private String 				  implementingClass;
	private String 				  name;
	private String 				  message;		

	public DataEntryPageExtensionElement(IConfigurationElement configElement) {
		super();
		Assert.isTrue(configElement.getName()
								   .equals(ExtensionPointConstants.ELEMENT_DATA_ENTRY_PAGE), 
					  "wrong IConfigurationElement: " + configElement.getName());
		this.configElement = configElement;
		id = Util.getAttribute(configElement, 
							   ExtensionPointConstants.ATTRIBUTE_ID, null);
		name = Util.getAttribute(configElement, 
								 ExtensionPointConstants.ATTRIBUTE_NAME, null);
		implementingClass = 
			Util.getAttribute(configElement, 
							  ExtensionPointConstants.ATTRIBUTE_CLASS, null);
		message = Util.getAttribute(configElement, 
									ExtensionPointConstants.ATTRIBUTE_MESSAGE, 
									"[no message available]");
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

	public String getId() {
		return id;
	}

	public String getImplementingClass() {
		return implementingClass;
	}

	public String getName() {
		return name;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		StringBuilder p = new StringBuilder();
		p.append(ExtensionPointConstants.ELEMENT_DATA_ENTRY_PAGE);
		p.append(": ");
		p.append(ExtensionPointConstants.ATTRIBUTE_ID + "=" + id);
		p.append(" ");
		p.append(ExtensionPointConstants.ATTRIBUTE_NAME + "=" + name);
		p.append(" "); 
		p.append(ExtensionPointConstants.ATTRIBUTE_MESSAGE + "=" + message);
		p.append(" ");
		p.append(ExtensionPointConstants.ATTRIBUTE_CLASS + "=" + 
				 implementingClass);		
		return p.toString();
	}	
	
}