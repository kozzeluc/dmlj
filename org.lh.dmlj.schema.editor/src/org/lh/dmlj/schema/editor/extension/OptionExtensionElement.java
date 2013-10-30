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
