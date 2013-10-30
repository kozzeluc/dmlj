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

import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;
import java.util.PropertyResourceBundle;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;

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
	
	public static ImageDescriptor getImageDescriptor(IConfigurationElement element,
	  		 								  		 String attributeName) {
		
		String imagePath = element.getAttribute(attributeName);
		if (imagePath == null) {
			return null;
		}
		IExtension extension = element.getDeclaringExtension();
		String extendingPluginId = extension.getNamespaceIdentifier();
		ImageDescriptor imageDescriptor = 
			AbstractUIPlugin.imageDescriptorFromPlugin(extendingPluginId,
													   imagePath);
		return imageDescriptor;
	}
	
	public static Properties getResourceAsProperties(IConfigurationElement element,
											  		 String attributeName) {
		
		Properties parameters = new Properties();		
		String path = getAttribute(element, attributeName, "");
		if (!path.equals("")) {
			try {	
				IExtension extension = element.getDeclaringExtension();
				Bundle bundle = 
					Platform.getBundle(extension.getNamespaceIdentifier());
				InputStream in = 
					FileLocator.openStream(bundle, new Path(path), false);
				PropertyResourceBundle prb = new PropertyResourceBundle(in);				
				for (String key : Collections.list(prb.getKeys())) {					
					parameters.put(key, prb.getString(key));
				}
				in.close();					
			} catch (Throwable t) {
				throw new RuntimeException(t);
			}			
		}
		return parameters;
	}	
	
}
