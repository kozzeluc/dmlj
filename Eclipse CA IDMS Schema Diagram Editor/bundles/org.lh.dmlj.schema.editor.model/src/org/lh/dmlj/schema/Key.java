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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Key</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.Key#isCalcKey <em>Calc Key</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Key#isCompressed <em>Compressed</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Key#getDuplicatesOption <em>Duplicates Option</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Key#getElements <em>Elements</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Key#getElementSummary <em>Element Summary</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Key#getLength <em>Length</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Key#getMemberRole <em>Member Role</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Key#isNaturalSequence <em>Natural Sequence</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Key#getRecord <em>Record</em>}</li>
 * </ul>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getKey()
 * @model
 * @generated
 */
public interface Key extends EObject {
	/**
	 * Returns the value of the '<em><b>Calc Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Calc Key</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Calc Key</em>' attribute.
	 * @see org.lh.dmlj.schema.SchemaPackage#getKey_CalcKey()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	boolean isCalcKey();

	/**
	 * Returns the value of the '<em><b>Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Length</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Length</em>' attribute.
	 * @see org.lh.dmlj.schema.SchemaPackage#getKey_Length()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	short getLength();

	/**
	 * Returns the value of the '<em><b>Duplicates Option</b></em>' attribute.
	 * The literals are from the enumeration {@link org.lh.dmlj.schema.DuplicatesOption}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Duplicates Option</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Duplicates Option</em>' attribute.
	 * @see org.lh.dmlj.schema.DuplicatesOption
	 * @see #setDuplicatesOption(DuplicatesOption)
	 * @see org.lh.dmlj.schema.SchemaPackage#getKey_DuplicatesOption()
	 * @model
	 * @generated
	 */
	DuplicatesOption getDuplicatesOption();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Key#getDuplicatesOption <em>Duplicates Option</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Duplicates Option</em>' attribute.
	 * @see org.lh.dmlj.schema.DuplicatesOption
	 * @see #getDuplicatesOption()
	 * @generated
	 */
	void setDuplicatesOption(DuplicatesOption value);

	/**
	 * Returns the value of the '<em><b>Compressed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Compressed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Compressed</em>' attribute.
	 * @see #setCompressed(boolean)
	 * @see org.lh.dmlj.schema.SchemaPackage#getKey_Compressed()
	 * @model
	 * @generated
	 */
	boolean isCompressed();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Key#isCompressed <em>Compressed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Compressed</em>' attribute.
	 * @see #isCompressed()
	 * @generated
	 */
	void setCompressed(boolean value);

	/**
	 * Returns the value of the '<em><b>Natural Sequence</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Natural Sequence</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Natural Sequence</em>' attribute.
	 * @see #setNaturalSequence(boolean)
	 * @see org.lh.dmlj.schema.SchemaPackage#getKey_NaturalSequence()
	 * @model
	 * @generated
	 */
	boolean isNaturalSequence();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Key#isNaturalSequence <em>Natural Sequence</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Natural Sequence</em>' attribute.
	 * @see #isNaturalSequence()
	 * @generated
	 */
	void setNaturalSequence(boolean value);

	/**
	 * Returns the value of the '<em><b>Elements</b></em>' containment reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.KeyElement}.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.KeyElement#getKey <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Elements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Elements</em>' containment reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getKey_Elements()
	 * @see org.lh.dmlj.schema.KeyElement#getKey
	 * @model opposite="key" containment="true" required="true"
	 * @generated
	 */
	EList<KeyElement> getElements();

	/**
	 * Returns the value of the '<em><b>Element Summary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Element Summary</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Element Summary</em>' attribute.
	 * @see org.lh.dmlj.schema.SchemaPackage#getKey_ElementSummary()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	String getElementSummary();

	/**
	 * Returns the value of the '<em><b>Member Role</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.MemberRole#getSortKey <em>Sort Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Member Role</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Member Role</em>' reference.
	 * @see #setMemberRole(MemberRole)
	 * @see org.lh.dmlj.schema.SchemaPackage#getKey_MemberRole()
	 * @see org.lh.dmlj.schema.MemberRole#getSortKey
	 * @model opposite="sortKey"
	 * @generated
	 */
	MemberRole getMemberRole();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Key#getMemberRole <em>Member Role</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Member Role</em>' reference.
	 * @see #getMemberRole()
	 * @generated
	 */
	void setMemberRole(MemberRole value);

	/**
	 * Returns the value of the '<em><b>Record</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.SchemaRecord#getKeys <em>Keys</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Record</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Record</em>' container reference.
	 * @see #setRecord(SchemaRecord)
	 * @see org.lh.dmlj.schema.SchemaPackage#getKey_Record()
	 * @see org.lh.dmlj.schema.SchemaRecord#getKeys
	 * @model opposite="keys" transient="false"
	 * @generated
	 */
	SchemaRecord getRecord();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Key#getRecord <em>Record</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Record</em>' container reference.
	 * @see #getRecord()
	 * @generated
	 */
	void setRecord(SchemaRecord value);

} // Key
