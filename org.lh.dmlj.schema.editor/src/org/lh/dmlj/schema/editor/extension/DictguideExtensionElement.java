package org.lh.dmlj.schema.editor.extension;

import java.io.File;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IConfigurationElement;

public class DictguideExtensionElement extends AbstractExtensionElement {
	
	private File pdf;

	public DictguideExtensionElement(IConfigurationElement configElement) {
		super(configElement);
		Assert.isTrue(configElement.getName()
				   .equals(ExtensionPointConstants.ELEMENT_DICTGUIDE), 
				   		   "wrong IConfigurationElement: " + configElement.getName());
	}
	
	public File getPdf() {
		if (pdf == null) {
			pdf = Util.getResourceAsFile(configElement, 
										 ExtensionPointConstants.ATTRIBUTE_PDF, 
										 "pdf");
		}
		return pdf;
	}
	
	public String getRelease() {
		String release = 
			Util.getAttribute(configElement, 
			 				  ExtensionPointConstants.ATTRIBUTE_RELEASE, "");
		return release.equals("") ? null : release;
	}

}