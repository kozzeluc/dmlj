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
