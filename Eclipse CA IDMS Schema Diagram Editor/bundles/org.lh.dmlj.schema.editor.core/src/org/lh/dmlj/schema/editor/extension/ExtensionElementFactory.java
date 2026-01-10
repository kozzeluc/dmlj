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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.lh.dmlj.schema.editor.Plugin;

public final class ExtensionElementFactory {

	private static <T extends AbstractExtensionElement> Constructor<T> getConstructor(Class<T> type) {
		try {
			return type.getConstructor(IConfigurationElement.class);
		} catch (Exception t) {			
			throw new IllegalStateException(t);
		}				
	}
	
	public static <T extends AbstractExtensionElement> List<T> getExtensionElements(String extensionPointId,
			String rootElementName, String elementName, Class<T> extensionElementClass) {
				
		var constructor = getConstructor(extensionElementClass);
		var list = new ArrayList<T>();
						
		var extensions = Platform.getExtensionRegistry().getExtensionPoint(Plugin.PLUGIN_ID, extensionPointId).getExtensions();
		for (var extension : extensions) {
			for (var rootElement : extension.getConfigurationElements()) {				
				if (rootElement.getName().equals(rootElementName)) {
					for (var element : rootElement.getChildren(elementName)) {
						try {
							T extensionElement = constructor.newInstance(element);
							list.add(extensionElement);
						} catch (Exception e) {
							throw new IllegalStateException(e);
						}						
					}					
					break; // we're done as we only expect 1 root element
				}
			}			
		}
		
		return list;
	}
	
	public static <T extends AbstractExtensionElement> List<T> getExtensionElements(IConfigurationElement parentElement,
			String elementName, Class<T> extensionElementClass) {
				
		var constructor = getConstructor(extensionElementClass);		
		var list = new ArrayList<T>();
		
		for (var element : parentElement.getChildren(elementName)) {
			try {
				var extensionElement = constructor.newInstance(element);
				list.add(extensionElement);
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}						
		}
		return list;
	}
	
	private ExtensionElementFactory() {
	}

}
