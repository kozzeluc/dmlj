/**
 * Copyright (C) 2015  Luc Hermans
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

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ruler</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.Ruler#getGuides <em>Guides</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Ruler#getType <em>Type</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Ruler#getDiagramData <em>Diagram Data</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getRuler()
 * @model
 * @generated
 */
public interface Ruler extends EObject {
	/**
	 * Returns the value of the '<em><b>Guides</b></em>' containment reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.Guide}.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.Guide#getRuler <em>Ruler</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Guides</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Guides</em>' containment reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getRuler_Guides()
	 * @see org.lh.dmlj.schema.Guide#getRuler
	 * @model opposite="ruler" containment="true"
	 * @generated
	 */
	EList<Guide> getGuides();

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The literals are from the enumeration {@link org.lh.dmlj.schema.RulerType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see org.lh.dmlj.schema.RulerType
	 * @see #setType(RulerType)
	 * @see org.lh.dmlj.schema.SchemaPackage#getRuler_Type()
	 * @model
	 * @generated
	 */
	RulerType getType();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Ruler#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see org.lh.dmlj.schema.RulerType
	 * @see #getType()
	 * @generated
	 */
	void setType(RulerType value);

	/**
	 * Returns the value of the '<em><b>Diagram Data</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.DiagramData#getRulers <em>Rulers</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Diagram Data</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Diagram Data</em>' container reference.
	 * @see #setDiagramData(DiagramData)
	 * @see org.lh.dmlj.schema.SchemaPackage#getRuler_DiagramData()
	 * @see org.lh.dmlj.schema.DiagramData#getRulers
	 * @model opposite="rulers" required="true" transient="false"
	 * @generated
	 */
	DiagramData getDiagramData();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Ruler#getDiagramData <em>Diagram Data</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Diagram Data</em>' container reference.
	 * @see #getDiagramData()
	 * @generated
	 */
	void setDiagramData(DiagramData value);

} // Ruler
