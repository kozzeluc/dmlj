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