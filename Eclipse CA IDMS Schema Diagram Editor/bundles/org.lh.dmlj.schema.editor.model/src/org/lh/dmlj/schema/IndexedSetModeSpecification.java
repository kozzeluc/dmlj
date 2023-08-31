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
 * A representation of the model object '<em><b>Indexed Set Mode Specification</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.IndexedSetModeSpecification#getDisplacementPageCount <em>Displacement Page Count</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.IndexedSetModeSpecification#getKeyCount <em>Key Count</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.IndexedSetModeSpecification#getSet <em>Set</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.IndexedSetModeSpecification#getSymbolicIndexName <em>Symbolic Index Name</em>}</li>
 * </ul>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getIndexedSetModeSpecification()
 * @model
 * @generated
 */
public interface IndexedSetModeSpecification extends EObject {
	/**
	 * Returns the value of the '<em><b>Symbolic Index Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Symbolic Index Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Symbolic Index Name</em>' attribute.
	 * @see #setSymbolicIndexName(String)
	 * @see org.lh.dmlj.schema.SchemaPackage#getIndexedSetModeSpecification_SymbolicIndexName()
	 * @model
	 * @generated
	 */
	String getSymbolicIndexName();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.IndexedSetModeSpecification#getSymbolicIndexName <em>Symbolic Index Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Symbolic Index Name</em>' attribute.
	 * @see #getSymbolicIndexName()
	 * @generated
	 */
	void setSymbolicIndexName(String value);

	/**
	 * Returns the value of the '<em><b>Key Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Key Count</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Key Count</em>' attribute.
	 * @see #setKeyCount(Short)
	 * @see org.lh.dmlj.schema.SchemaPackage#getIndexedSetModeSpecification_KeyCount()
	 * @model
	 * @generated
	 */
	Short getKeyCount();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.IndexedSetModeSpecification#getKeyCount <em>Key Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Key Count</em>' attribute.
	 * @see #getKeyCount()
	 * @generated
	 */
	void setKeyCount(Short value);

	/**
	 * Returns the value of the '<em><b>Set</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.Set#getIndexedSetModeSpecification <em>Indexed Set Mode Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Set</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Set</em>' container reference.
	 * @see #setSet(Set)
	 * @see org.lh.dmlj.schema.SchemaPackage#getIndexedSetModeSpecification_Set()
	 * @see org.lh.dmlj.schema.Set#getIndexedSetModeSpecification
	 * @model opposite="indexedSetModeSpecification" required="true" transient="false"
	 * @generated
	 */
	Set getSet();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.IndexedSetModeSpecification#getSet <em>Set</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Set</em>' container reference.
	 * @see #getSet()
	 * @generated
	 */
	void setSet(Set value);

	/**
	 * Returns the value of the '<em><b>Displacement Page Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Displacement Page Count</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Displacement Page Count</em>' attribute.
	 * @see #setDisplacementPageCount(Short)
	 * @see org.lh.dmlj.schema.SchemaPackage#getIndexedSetModeSpecification_DisplacementPageCount()
	 * @model
	 * @generated
	 */
	Short getDisplacementPageCount();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.IndexedSetModeSpecification#getDisplacementPageCount <em>Displacement Page Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Displacement Page Count</em>' attribute.
	 * @see #getDisplacementPageCount()
	 * @generated
	 */
	void setDisplacementPageCount(Short value);

} // IndexedSetModeSpecification
