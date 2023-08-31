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


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Owner Role</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.OwnerRole#getNextDbkeyPosition <em>Next Dbkey Position</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.OwnerRole#getPriorDbkeyPosition <em>Prior Dbkey Position</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.OwnerRole#getRecord <em>Record</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.OwnerRole#getSet <em>Set</em>}</li>
 * </ul>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getOwnerRole()
 * @model
 * @generated
 */
public interface OwnerRole extends Role {
	/**
	 * Returns the value of the '<em><b>Next Dbkey Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Next Dbkey Position</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Next Dbkey Position</em>' attribute.
	 * @see #setNextDbkeyPosition(short)
	 * @see org.lh.dmlj.schema.SchemaPackage#getOwnerRole_NextDbkeyPosition()
	 * @model
	 * @generated
	 */
	short getNextDbkeyPosition();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.OwnerRole#getNextDbkeyPosition <em>Next Dbkey Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Next Dbkey Position</em>' attribute.
	 * @see #getNextDbkeyPosition()
	 * @generated
	 */
	void setNextDbkeyPosition(short value);

	/**
	 * Returns the value of the '<em><b>Prior Dbkey Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Prior Dbkey Position</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Prior Dbkey Position</em>' attribute.
	 * @see #setPriorDbkeyPosition(Short)
	 * @see org.lh.dmlj.schema.SchemaPackage#getOwnerRole_PriorDbkeyPosition()
	 * @model
	 * @generated
	 */
	Short getPriorDbkeyPosition();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.OwnerRole#getPriorDbkeyPosition <em>Prior Dbkey Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Prior Dbkey Position</em>' attribute.
	 * @see #getPriorDbkeyPosition()
	 * @generated
	 */
	void setPriorDbkeyPosition(Short value);

	/**
	 * Returns the value of the '<em><b>Record</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.SchemaRecord#getOwnerRoles <em>Owner Roles</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Record</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Record</em>' reference.
	 * @see #setRecord(SchemaRecord)
	 * @see org.lh.dmlj.schema.SchemaPackage#getOwnerRole_Record()
	 * @see org.lh.dmlj.schema.SchemaRecord#getOwnerRoles
	 * @model opposite="ownerRoles" required="true"
	 * @generated
	 */
	SchemaRecord getRecord();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.OwnerRole#getRecord <em>Record</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Record</em>' reference.
	 * @see #getRecord()
	 * @generated
	 */
	void setRecord(SchemaRecord value);

	/**
	 * Returns the value of the '<em><b>Set</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.Set#getOwner <em>Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Set</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Set</em>' container reference.
	 * @see #setSet(Set)
	 * @see org.lh.dmlj.schema.SchemaPackage#getOwnerRole_Set()
	 * @see org.lh.dmlj.schema.Set#getOwner
	 * @model opposite="owner" required="true" transient="false"
	 * @generated
	 */
	Set getSet();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.OwnerRole#getSet <em>Set</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Set</em>' container reference.
	 * @see #getSet()
	 * @generated
	 */
	void setSet(Set value);

} // OwnerRole
