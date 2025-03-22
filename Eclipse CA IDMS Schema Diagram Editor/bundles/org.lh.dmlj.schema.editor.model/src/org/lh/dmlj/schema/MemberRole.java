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


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Member Role</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.MemberRole#getIndexDbkeyPosition <em>Index Dbkey Position</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.MemberRole#getMembershipOption <em>Membership Option</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.MemberRole#getNextDbkeyPosition <em>Next Dbkey Position</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.MemberRole#getPriorDbkeyPosition <em>Prior Dbkey Position</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.MemberRole#getOwnerDbkeyPosition <em>Owner Dbkey Position</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.MemberRole#getRecord <em>Record</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.MemberRole#getSet <em>Set</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.MemberRole#getSortKey <em>Sort Key</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.MemberRole#getConnectionParts <em>Connection Parts</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.MemberRole#getConnectionLabel <em>Connection Label</em>}</li>
 * </ul>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getMemberRole()
 * @model
 * @generated
 */
public interface MemberRole extends Role {
	/**
	 * Returns the value of the '<em><b>Index Dbkey Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Index Dbkey Position</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Index Dbkey Position</em>' attribute.
	 * @see #setIndexDbkeyPosition(Short)
	 * @see org.lh.dmlj.schema.SchemaPackage#getMemberRole_IndexDbkeyPosition()
	 * @model
	 * @generated
	 */
	Short getIndexDbkeyPosition();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.MemberRole#getIndexDbkeyPosition <em>Index Dbkey Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Index Dbkey Position</em>' attribute.
	 * @see #getIndexDbkeyPosition()
	 * @generated
	 */
	void setIndexDbkeyPosition(Short value);

	/**
	 * Returns the value of the '<em><b>Membership Option</b></em>' attribute.
	 * The literals are from the enumeration {@link org.lh.dmlj.schema.SetMembershipOption}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Membership Option</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Membership Option</em>' attribute.
	 * @see org.lh.dmlj.schema.SetMembershipOption
	 * @see #setMembershipOption(SetMembershipOption)
	 * @see org.lh.dmlj.schema.SchemaPackage#getMemberRole_MembershipOption()
	 * @model
	 * @generated
	 */
	SetMembershipOption getMembershipOption();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.MemberRole#getMembershipOption <em>Membership Option</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Membership Option</em>' attribute.
	 * @see org.lh.dmlj.schema.SetMembershipOption
	 * @see #getMembershipOption()
	 * @generated
	 */
	void setMembershipOption(SetMembershipOption value);

	/**
	 * Returns the value of the '<em><b>Next Dbkey Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Next Dbkey Position</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Next Dbkey Position</em>' attribute.
	 * @see #setNextDbkeyPosition(Short)
	 * @see org.lh.dmlj.schema.SchemaPackage#getMemberRole_NextDbkeyPosition()
	 * @model
	 * @generated
	 */
	Short getNextDbkeyPosition();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.MemberRole#getNextDbkeyPosition <em>Next Dbkey Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Next Dbkey Position</em>' attribute.
	 * @see #getNextDbkeyPosition()
	 * @generated
	 */
	void setNextDbkeyPosition(Short value);

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
	 * @see org.lh.dmlj.schema.SchemaPackage#getMemberRole_PriorDbkeyPosition()
	 * @model
	 * @generated
	 */
	Short getPriorDbkeyPosition();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.MemberRole#getPriorDbkeyPosition <em>Prior Dbkey Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Prior Dbkey Position</em>' attribute.
	 * @see #getPriorDbkeyPosition()
	 * @generated
	 */
	void setPriorDbkeyPosition(Short value);

	/**
	 * Returns the value of the '<em><b>Owner Dbkey Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Owner Dbkey Position</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owner Dbkey Position</em>' attribute.
	 * @see #setOwnerDbkeyPosition(Short)
	 * @see org.lh.dmlj.schema.SchemaPackage#getMemberRole_OwnerDbkeyPosition()
	 * @model
	 * @generated
	 */
	Short getOwnerDbkeyPosition();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.MemberRole#getOwnerDbkeyPosition <em>Owner Dbkey Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owner Dbkey Position</em>' attribute.
	 * @see #getOwnerDbkeyPosition()
	 * @generated
	 */
	void setOwnerDbkeyPosition(Short value);

	/**
	 * Returns the value of the '<em><b>Record</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.SchemaRecord#getMemberRoles <em>Member Roles</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Record</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Record</em>' reference.
	 * @see #setRecord(SchemaRecord)
	 * @see org.lh.dmlj.schema.SchemaPackage#getMemberRole_Record()
	 * @see org.lh.dmlj.schema.SchemaRecord#getMemberRoles
	 * @model opposite="memberRoles" required="true"
	 * @generated
	 */
	SchemaRecord getRecord();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.MemberRole#getRecord <em>Record</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Record</em>' reference.
	 * @see #getRecord()
	 * @generated
	 */
	void setRecord(SchemaRecord value);

	/**
	 * Returns the value of the '<em><b>Set</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.Set#getMembers <em>Members</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Set</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Set</em>' container reference.
	 * @see #setSet(Set)
	 * @see org.lh.dmlj.schema.SchemaPackage#getMemberRole_Set()
	 * @see org.lh.dmlj.schema.Set#getMembers
	 * @model opposite="members" required="true" transient="false"
	 * @generated
	 */
	Set getSet();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.MemberRole#getSet <em>Set</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Set</em>' container reference.
	 * @see #getSet()
	 * @generated
	 */
	void setSet(Set value);

	/**
	 * Returns the value of the '<em><b>Sort Key</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.Key#getMemberRole <em>Member Role</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sort Key</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sort Key</em>' reference.
	 * @see #setSortKey(Key)
	 * @see org.lh.dmlj.schema.SchemaPackage#getMemberRole_SortKey()
	 * @see org.lh.dmlj.schema.Key#getMemberRole
	 * @model opposite="memberRole"
	 * @generated
	 */
	Key getSortKey();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.MemberRole#getSortKey <em>Sort Key</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sort Key</em>' reference.
	 * @see #getSortKey()
	 * @generated
	 */
	void setSortKey(Key value);

	/**
	 * Returns the value of the '<em><b>Connection Parts</b></em>' reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.ConnectionPart}.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.ConnectionPart#getMemberRole <em>Member Role</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Connection Parts</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Connection Parts</em>' reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getMemberRole_ConnectionParts()
	 * @see org.lh.dmlj.schema.ConnectionPart#getMemberRole
	 * @model opposite="memberRole" required="true" upper="2"
	 * @generated
	 */
	EList<ConnectionPart> getConnectionParts();

	/**
	 * Returns the value of the '<em><b>Connection Label</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.ConnectionLabel#getMemberRole <em>Member Role</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Connection Label</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Connection Label</em>' reference.
	 * @see #setConnectionLabel(ConnectionLabel)
	 * @see org.lh.dmlj.schema.SchemaPackage#getMemberRole_ConnectionLabel()
	 * @see org.lh.dmlj.schema.ConnectionLabel#getMemberRole
	 * @model opposite="memberRole" required="true"
	 * @generated
	 */
	ConnectionLabel getConnectionLabel();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.MemberRole#getConnectionLabel <em>Connection Label</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Connection Label</em>' reference.
	 * @see #getConnectionLabel()
	 * @generated
	 */
	void setConnectionLabel(ConnectionLabel value);

} // MemberRole
