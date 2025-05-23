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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Guide</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.Guide#getPosition <em>Position</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Guide#getRuler <em>Ruler</em>}</li>
 * </ul>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getGuide()
 * @model
 * @generated
 */
public interface Guide extends EObject {
	/**
	 * Returns the value of the '<em><b>Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Position</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Position</em>' attribute.
	 * @see #setPosition(int)
	 * @see org.lh.dmlj.schema.SchemaPackage#getGuide_Position()
	 * @model
	 * @generated
	 */
	int getPosition();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Guide#getPosition <em>Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Position</em>' attribute.
	 * @see #getPosition()
	 * @generated
	 */
	void setPosition(int value);

	/**
	 * Returns the value of the '<em><b>Ruler</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.Ruler#getGuides <em>Guides</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ruler</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ruler</em>' container reference.
	 * @see #setRuler(Ruler)
	 * @see org.lh.dmlj.schema.SchemaPackage#getGuide_Ruler()
	 * @see org.lh.dmlj.schema.Ruler#getGuides
	 * @model opposite="guides" transient="false"
	 * @generated
	 */
	Ruler getRuler();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Guide#getRuler <em>Ruler</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ruler</em>' container reference.
	 * @see #getRuler()
	 * @generated
	 */
	void setRuler(Ruler value);

} // Guide
