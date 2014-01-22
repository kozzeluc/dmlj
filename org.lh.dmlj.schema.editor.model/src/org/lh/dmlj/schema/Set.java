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
 * A representation of the model object '<em><b>Set</b></em>'. 
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.Set#getIndexedSetModeSpecification <em>Indexed Set Mode Specification</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Set#getMembers <em>Members</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Set#getMode <em>Mode</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Set#getName <em>Name</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Set#getOrder <em>Order</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Set#getOwner <em>Owner</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Set#getSchema <em>Schema</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Set#getSystemOwner <em>System Owner</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Set#getViaMembers <em>Via Members</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getSet()
 * @model
 * @generated
 */
public interface Set extends INodeTextProvider<Set> {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.lh.dmlj.schema.SchemaPackage#getSet_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Set#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Mode</b></em>' attribute.
	 * The literals are from the enumeration {@link org.lh.dmlj.schema.SetMode}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mode</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mode</em>' attribute.
	 * @see org.lh.dmlj.schema.SetMode
	 * @see #setMode(SetMode)
	 * @see org.lh.dmlj.schema.SchemaPackage#getSet_Mode()
	 * @model
	 * @generated
	 */
	SetMode getMode();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Set#getMode <em>Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mode</em>' attribute.
	 * @see org.lh.dmlj.schema.SetMode
	 * @see #getMode()
	 * @generated
	 */
	void setMode(SetMode value);

	/**
	 * Returns the value of the '<em><b>Order</b></em>' attribute.
	 * The literals are from the enumeration {@link org.lh.dmlj.schema.SetOrder}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Order</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Order</em>' attribute.
	 * @see org.lh.dmlj.schema.SetOrder
	 * @see #setOrder(SetOrder)
	 * @see org.lh.dmlj.schema.SchemaPackage#getSet_Order()
	 * @model
	 * @generated
	 */
	SetOrder getOrder();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Set#getOrder <em>Order</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Order</em>' attribute.
	 * @see org.lh.dmlj.schema.SetOrder
	 * @see #getOrder()
	 * @generated
	 */
	void setOrder(SetOrder value);

	/**
	 * Returns the value of the '<em><b>Schema</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.Schema#getSets <em>Sets</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Schema</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Schema</em>' container reference.
	 * @see #setSchema(Schema)
	 * @see org.lh.dmlj.schema.SchemaPackage#getSet_Schema()
	 * @see org.lh.dmlj.schema.Schema#getSets
	 * @model opposite="sets" required="true" transient="false"
	 * @generated
	 */
	Schema getSchema();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Set#getSchema <em>Schema</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Schema</em>' container reference.
	 * @see #getSchema()
	 * @generated
	 */
	void setSchema(Schema value);

	/**
	 * Returns the value of the '<em><b>Owner</b></em>' containment reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.OwnerRole#getSet <em>Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Owner</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owner</em>' containment reference.
	 * @see #setOwner(OwnerRole)
	 * @see org.lh.dmlj.schema.SchemaPackage#getSet_Owner()
	 * @see org.lh.dmlj.schema.OwnerRole#getSet
	 * @model opposite="set" containment="true"
	 * @generated
	 */
	OwnerRole getOwner();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Set#getOwner <em>Owner</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owner</em>' containment reference.
	 * @see #getOwner()
	 * @generated
	 */
	void setOwner(OwnerRole value);

	/**
	 * Returns the value of the '<em><b>System Owner</b></em>' containment reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.SystemOwner#getSet <em>Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>System Owner</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>System Owner</em>' containment reference.
	 * @see #setSystemOwner(SystemOwner)
	 * @see org.lh.dmlj.schema.SchemaPackage#getSet_SystemOwner()
	 * @see org.lh.dmlj.schema.SystemOwner#getSet
	 * @model opposite="set" containment="true"
	 * @generated
	 */
	SystemOwner getSystemOwner();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Set#getSystemOwner <em>System Owner</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>System Owner</em>' containment reference.
	 * @see #getSystemOwner()
	 * @generated
	 */
	void setSystemOwner(SystemOwner value);

	/**
	 * Returns the value of the '<em><b>Members</b></em>' containment reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.MemberRole}.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.MemberRole#getSet <em>Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Members</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Members</em>' containment reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getSet_Members()
	 * @see org.lh.dmlj.schema.MemberRole#getSet
	 * @model opposite="set" containment="true" required="true"
	 * @generated
	 */
	EList<MemberRole> getMembers();

	/**
	 * Returns the value of the '<em><b>Via Members</b></em>' reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.ViaSpecification}.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.ViaSpecification#getSet <em>Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Via Members</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Via Members</em>' reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getSet_ViaMembers()
	 * @see org.lh.dmlj.schema.ViaSpecification#getSet
	 * @model opposite="set"
	 * @generated
	 */
	EList<ViaSpecification> getViaMembers();

	/**
	 * Returns the value of the '<em><b>Indexed Set Mode Specification</b></em>' containment reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.IndexedSetModeSpecification#getSet <em>Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Indexed Set Mode Specification</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Indexed Set Mode Specification</em>' containment reference.
	 * @see #setIndexedSetModeSpecification(IndexedSetModeSpecification)
	 * @see org.lh.dmlj.schema.SchemaPackage#getSet_IndexedSetModeSpecification()
	 * @see org.lh.dmlj.schema.IndexedSetModeSpecification#getSet
	 * @model opposite="set" containment="true"
	 * @generated
	 */
	IndexedSetModeSpecification getIndexedSetModeSpecification();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Set#getIndexedSetModeSpecification <em>Indexed Set Mode Specification</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Indexed Set Mode Specification</em>' containment reference.
	 * @see #getIndexedSetModeSpecification()
	 * @generated
	 */
	void setIndexedSetModeSpecification(IndexedSetModeSpecification value);

} // Set
