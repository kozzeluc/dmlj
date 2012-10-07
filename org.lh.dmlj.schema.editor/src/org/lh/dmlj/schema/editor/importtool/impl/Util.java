package org.lh.dmlj.schema.editor.importtool.impl;

import org.eclipse.core.runtime.IConfigurationElement;

public abstract class Util {

	public static String getAttribute(IConfigurationElement configElement,
			   						  String name, String defaultValue) {

		String value = configElement.getAttribute(name);
		if (value != null) {
			return value;
		}
		if (defaultValue != null) {
			return defaultValue;
		}
		throw new IllegalArgumentException(configElement.getName() + 
										   " element with missing " + name + 
										   " attribute");
	}	
	
}