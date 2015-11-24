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
 * A representation of the model object '<em><b>Vsam Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.VsamType#getLengthType <em>Length Type</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.VsamType#getRecord <em>Record</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.VsamType#isSpanned <em>Spanned</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getVsamType()
 * @model
 * @generated
 */
public interface VsamType extends EObject {
	/**
	 * Returns the value of the '<em><b>Length Type</b></em>' attribute.
	 * The literals are from the enumeration {@link org.lh.dmlj.schema.VsamLengthType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Length Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Length Type</em>' attribute.
	 * @see org.lh.dmlj.schema.VsamLengthType
	 * @see #setLengthType(VsamLengthType)
	 * @see org.lh.dmlj.schema.SchemaPackage#getVsamType_LengthType()
	 * @model
	 * @generated
	 */
	VsamLengthType getLengthType();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.VsamType#getLengthType <em>Length Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Length Type</em>' attribute.
	 * @see org.lh.dmlj.schema.VsamLengthType
	 * @see #getLengthType()
	 * @generated
	 */
	void setLengthType(VsamLengthType value);

	/**
	 * Returns the value of the '<em><b>Record</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.SchemaRecord#getVsamType <em>Vsam Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Record</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Record</em>' container reference.
	 * @see #setRecord(SchemaRecord)
	 * @see org.lh.dmlj.schema.SchemaPackage#getVsamType_Record()
	 * @see org.lh.dmlj.schema.SchemaRecord#getVsamType
	 * @model opposite="vsamType" required="true" transient="false"
	 * @generated
	 */
	SchemaRecord getRecord();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.VsamType#getRecord <em>Record</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Record</em>' container reference.
	 * @see #getRecord()
	 * @generated
	 */
	void setRecord(SchemaRecord value);

	/**
	 * Returns the value of the '<em><b>Spanned</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Spanned</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Spanned</em>' attribute.
	 * @see #setSpanned(boolean)
	 * @see org.lh.dmlj.schema.SchemaPackage#getVsamType_Spanned()
	 * @model default="false"
	 * @generated
	 */
	boolean isSpanned();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.VsamType#isSpanned <em>Spanned</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spanned</em>' attribute.
	 * @see #isSpanned()
	 * @generated
	 */
	void setSpanned(boolean value);

} // VsamType
