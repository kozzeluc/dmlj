package org.lh.dmlj.schema.editor.testtool;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

public class ObjectGraph {

	private List<String> lines = new ArrayList<>();
		
	private static List<String> getLines(EObject object, List<EObject> objects) {		
		
		List<String> lines = new ArrayList<>();
		lines.add(getObjectIdentifier(object));
		
		List<EClass> classes = new ArrayList<>(); 
		EClass aClass = object.eClass();
		while (aClass != null) {			
			classes.add(0, aClass);
			EList<EClass> superTypes = aClass.getESuperTypes();
			aClass = null;
			for (EClass superClass : superTypes) {
				if (!superClass.isInterface()) {
					aClass = superClass;
					break;
				}
			}
		}		
		
		for (EClass _class : classes) {			
						
			for (EAttribute attribute : _class.getEAllAttributes()) {
				Object value = object.eGet(attribute);
				lines.add("+ " + attribute.getName() + ": " + getObjectIdentifier(value));				
			}
			for (EReference reference : _class.getEAllReferences()) {
				Object value = object.eGet(reference);				
				if (value != null) {
					if (reference.isMany()) {
						@SuppressWarnings("unchecked")
						EList<? extends EObject> list = (EList<? extends EObject>) value;
						for (int i = 0; i < list.size(); i++) {
							EObject referencedObject = list.get(i);
							lines.add("+ " + reference.getName() + "[" + i + "]: " + 
									  getObjectIdentifier(referencedObject));				
							if (!objects.contains(referencedObject)) {
								objects.add(referencedObject);
							}
						}
					} else {
						lines.add("+ " + reference.getName() + ": " + getObjectIdentifier(value));				
						if (!objects.contains(value)) {					
							objects.add((EObject) value);
						}
					}
				} else {
					lines.add("+ " + reference.getName() + ": " + getObjectIdentifier(null));
				}
			}			 			
			
		}
		
		return lines;
	}	
	
	private static String getObjectIdentifier(Object object) {
		if (object == null) {
			return "[null]";
		} else if (object.getClass().getPackage().getName().equals("java.lang")) {				
			return object.getClass().getSimpleName() + " [" + String.valueOf(object) + "]";				
		} else {			
			return object.getClass().getSimpleName() + " [" + System.identityHashCode(object) + "]";			
		}
	}

	ObjectGraph(EObject root) {
		super();
		List<EObject> objects = new ArrayList<>();
		objects.add(root);		
		for (int i = 0; i < objects.size(); i++) { // the list of children grows along the way
			lines.addAll(getLines(objects.get(i), objects));
		}		
	}

	List<String> getLines() {
		return lines;
	}
	
	public String toString() {
		StringBuilder p = new StringBuilder();
		for (String line : lines) {
			if (p.length() > 0) {
				p.append("\n");
			}
			p.append(line);
		}
		return p.toString();
	}
	
}
