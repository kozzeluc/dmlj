package org.lh.dmlj.schema.editor.property.filter;

import org.eclipse.emf.ecore.EAttribute;

/**
 * A filter that filters the items in a combo created for editing enum 
 * attributes.
 */
public interface IEnumFilter<T extends Enum<?>> {
	
	/**
	 * Indicates whether the given element has to be listed in the combo 
	 * created for editing the enum attribute.
	 * @param attribute the EAttribute for which a combo is created
	 * @param element the enum element to filter
	 * @return true if the element has to be listed in the combo, false if not
	 */
	boolean include(EAttribute attribute, T element);
}