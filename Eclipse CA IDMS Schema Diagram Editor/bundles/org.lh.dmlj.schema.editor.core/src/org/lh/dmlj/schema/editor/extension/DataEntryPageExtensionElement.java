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
