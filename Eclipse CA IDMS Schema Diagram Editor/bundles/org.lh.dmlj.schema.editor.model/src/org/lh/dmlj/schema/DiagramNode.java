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
package org.lh.dmlj.schema;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diagram Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.DiagramNode#getDiagramLocation <em>Diagram Location</em>}</li>
 * </ul>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramNode()
 * @model abstract="true"
 * @generated
 */
public interface DiagramNode extends EObject {
	/**
	 * Returns the value of the '<em><b>Diagram Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Diagram Location</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Diagram Location</em>' reference.
	 * @see #setDiagramLocation(DiagramLocation)
	 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramNode_DiagramLocation()
	 * @model required="true"
	 * @generated
	 */
	DiagramLocation getDiagramLocation();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.DiagramNode#getDiagramLocation <em>Diagram Location</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Diagram Location</em>' reference.
	 * @see #getDiagramLocation()
	 * @generated
	 */
	void setDiagramLocation(DiagramLocation value);

} // DiagramNode
