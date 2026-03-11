/**
 * Copyright (C) 2025  Luc Hermans
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

import java.util.Collections;
import java.util.Properties;
import java.util.PropertyResourceBundle;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public final class Util {
	
    public static String getAttribute(IConfigurationElement configElement, String name, String defaultValue) {
		var value = configElement.getAttribute(name);
		if (value != null) {
			return value;
		}
		if (defaultValue != null) {
			return defaultValue;
		}
		throw new IllegalArgumentException(configElement.getName() + " element with missing " + name + " attribute");
	}
	
	public static ImageDescriptor getImageDescriptor(IConfigurationElement element, String attributeName) {
		var imagePath = element.getAttribute(attributeName);
		if (imagePath == null) {
			return null;
		}
		var extension = element.getDeclaringExtension();
		var extendingPluginId = extension.getNamespaceIdentifier();
		return AbstractUIPlugin.imageDescriptorFromPlugin(extendingPluginId, imagePath);
	}
	
	public static Properties getResourceAsProperties(IConfigurationElement element, String attributeName) {
		var parameters = new Properties();		
		var path = getAttribute(element, attributeName, "");
		if (!path.isEmpty()) {
			try {	
				var extension = element.getDeclaringExtension();
				var bundle = Platform.getBundle(extension.getNamespaceIdentifier());
				var in = FileLocator.openStream(bundle, new Path(path), false);
				var prb = new PropertyResourceBundle(in);				
				for (var key : Collections.list(prb.getKeys())) {					
					parameters.put(key, prb.getString(key));
				}
				in.close();					
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}			
		}
		return parameters;
	}
	
	private Util() {		
	}
	
}
