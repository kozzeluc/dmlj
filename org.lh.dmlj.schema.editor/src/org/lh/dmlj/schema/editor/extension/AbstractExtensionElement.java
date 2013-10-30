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

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;

public abstract class AbstractExtensionElement {

	protected final IConfigurationElement configElement;
	protected final IExtension 			  extension;	
	
	public AbstractExtensionElement(IConfigurationElement configElement) {
		super();		
		this.configElement = configElement;
		this.extension = configElement.getDeclaringExtension();
	}

	public final IConfigurationElement getConfigurationElement() {
		return configElement;
	}
	
	public final String getDescription() {
		return Util.getAttribute(configElement, 
				  				 ExtensionPointConstants.ATTRIBUTE_DESCRIPTION, 
				  				 ""); // not in every extension element
	}

	public final String getId() {
		return Util.getAttribute(configElement, 
				   				 ExtensionPointConstants.ATTRIBUTE_ID, 
				   				 ""); // not in every extension element
	}

	public final String getName() {
		return Util.getAttribute(configElement, 
				 				 ExtensionPointConstants.ATTRIBUTE_NAME, 
				 				 ""); // not in every extension element
	}

	public final String getPluginId() {
		return extension.getNamespaceIdentifier();
	}

}
