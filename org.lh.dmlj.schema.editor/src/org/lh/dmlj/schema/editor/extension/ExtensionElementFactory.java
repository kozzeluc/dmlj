package org.lh.dmlj.schema.editor.extension;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.lh.dmlj.schema.editor.Plugin;

public abstract class ExtensionElementFactory {

	private static <T extends AbstractExtensionElement> Constructor<T> getConstructor(Class<T> _class) {
		try {
			return _class.getConstructor(IConfigurationElement.class);
		} catch (Throwable t) {			
			throw new RuntimeException(t);
		}				
	}
	
	public static <T extends AbstractExtensionElement> List<T> getExtensionElements(String extensionPointId,
																					String rootElementName,
																					String elementName,
																					Class<T> extensionElementClass) {
		
		// we'll need the extension element class' constructor that accepts an
		// IConfigurationElement as its only argument:
		Constructor<T> constructor = getConstructor(extensionElementClass);		
		
		// create the result list
		List<T> list = new ArrayList<>();
		
		// build the list of extension point elements...
		IExtension[] extensions = 
			Platform.getExtensionRegistry()
					.getExtensionPoint(Plugin.PLUGIN_ID, extensionPointId)
					.getExtensions();		
		
		// ... by traversing all defined extensions for the given extension 
		// point, in all installed plug-ins:
		for (IExtension extension : extensions) {
			// for each defined extension, walk the list of elements and process 
			// the (only) one matching the given root element name:
			for (IConfigurationElement rootElement : 
				 extension.getConfigurationElements()) {
				
				if (rootElement.getName().equals(rootElementName)) {
					// root element found; process the elements we're interested 
					// in, 1 by 1...
					for (IConfigurationElement element : 
						 rootElement.getChildren(elementName)) {
						
						// instantiate an extension element of the right type
						// (T) and add it to our list						
						try {
							T extensionElement = 
								constructor.newInstance(element);
							list.add(extensionElement);
						} catch (Throwable t) {
							throw new RuntimeException(t);
						}						
					}					
					break; // we're done as we only expect 1 root element
				}
			}			
		}
		
		return list;
	}
	
	public static <T extends AbstractExtensionElement> List<T> getExtensionElements(IConfigurationElement parentElement,
																				    String elementName,
																				    Class<T> extensionElementClass) {
		
		// we'll need the extension element class' constructor that accepts an
		// IConfigurationElement as its only argument:
		Constructor<T> constructor = getConstructor(extensionElementClass);		
		
		// create the result list
		List<T> list = new ArrayList<>();
		
		for (IConfigurationElement element : 
			 parentElement.getChildren(elementName)) {					
			
			// instantiate an extension element of the right type (T) and add it 
			// to our list						
			try {
				T extensionElement = constructor.newInstance(element);
				list.add(extensionElement);
			} catch (Throwable t) {
				throw new RuntimeException(t);
			}						
		}		
		
		return list;
	}

}