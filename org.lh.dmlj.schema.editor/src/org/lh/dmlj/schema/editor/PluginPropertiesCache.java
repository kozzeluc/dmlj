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
package org.lh.dmlj.schema.editor;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Plugin;

public abstract class PluginPropertiesCache {

	private static final String PLUGIN_PROPERTIES = "plugin.properties";
	
	private static Map<String, PropertyResourceBundle> map = new HashMap<>();

	public static String get(Plugin plugin, String key) {		
		String bundleSymbolicName = plugin.getBundle().getSymbolicName(); 
		if (!map.containsKey(bundleSymbolicName)) {
			// load the plugin.properties file from the given plug-in:
			try {
				InputStream in = 
					FileLocator.openStream(plugin.getBundle(), new Path(PLUGIN_PROPERTIES), false);
				PropertyResourceBundle propertyResourceBundle = new PropertyResourceBundle(in);
				in.close();
				map.put(bundleSymbolicName, propertyResourceBundle);				
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		PropertyResourceBundle propertyResourceBundle = map.get(bundleSymbolicName);
		try {
			return propertyResourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return null;
		}
	}
	
}
