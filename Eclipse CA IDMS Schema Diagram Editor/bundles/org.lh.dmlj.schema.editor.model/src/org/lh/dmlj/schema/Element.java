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
 * A representation of the model object '<em><b>Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.Element#getBaseName <em>Base Name</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Element#getChildren <em>Children</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Element#getKeyElements <em>Key Elements</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Element#getLength <em>Length</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Element#getLevel <em>Level</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Element#getName <em>Name</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Element#isNullable <em>Nullable</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Element#getOccursSpecification <em>Occurs Specification</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Element#getOffset <em>Offset</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Element#getParent <em>Parent</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Element#getPicture <em>Picture</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Element#getRecord <em>Record</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Element#getRedefines <em>Redefines</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Element#getSyntaxLength <em>Syntax Length</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Element#getSyntaxPosition <em>Syntax Position</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Element#getUsage <em>Usage</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Element#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getElement()
 * @model
 * @generated
 */
public interface Element extends EObject {
	/**
	 * Returns the value of the '<em><b>Base Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Name</em>' attribute.
	 * @see #setBaseName(String)
	 * @see org.lh.dmlj.schema.SchemaPackage#getElement_BaseName()
	 * @model
	 * @generated
	 */
	String getBaseName();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Element#getBaseName <em>Base Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Name</em>' attribute.
	 * @see #getBaseName()
	 * @generated
	 */
	void setBaseName(String value);

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
	 * @see org.lh.dmlj.schema.SchemaPackage#getElement_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Element#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Level</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Level</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Level</em>' attribute.
	 * @see #setLevel(short)
	 * @see org.lh.dmlj.schema.SchemaPackage#getElement_Level()
	 * @model
	 * @generated
	 */
	short getLevel();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Element#getLevel <em>Level</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Level</em>' attribute.
	 * @see #getLevel()
	 * @generated
	 */
	void setLevel(short value);

	/**
	 * Returns the value of the '<em><b>Usage</b></em>' attribute.
	 * The literals are from the enumeration {@link org.lh.dmlj.schema.Usage}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Usage</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Usage</em>' attribute.
	 * @see org.lh.dmlj.schema.Usage
	 * @see #setUsage(Usage)
	 * @see org.lh.dmlj.schema.SchemaPackage#getElement_Usage()
	 * @model
	 * @generated
	 */
	Usage getUsage();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Element#getUsage <em>Usage</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Usage</em>' attribute.
	 * @see org.lh.dmlj.schema.Usage
	 * @see #getUsage()
	 * @generated
	 */
	void setUsage(Usage value);

	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(String)
	 * @see org.lh.dmlj.schema.SchemaPackage#getElement_Value()
	 * @model
	 * @generated
	 */
	String getValue();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Element#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(String value);

	/**
	 * Returns the value of the '<em><b>Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Offset</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Offset</em>' attribute.
	 * @see org.lh.dmlj.schema.SchemaPackage#getElement_Offset()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	short getOffset();

	/**
	 * Returns the value of the '<em><b>Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Length</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Length</em>' attribute.
	 * @see org.lh.dmlj.schema.SchemaPackage#getElement_Length()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	short getLength();

	/**
	 * Returns the value of the '<em><b>Picture</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Picture</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Picture</em>' attribute.
	 * @see #setPicture(String)
	 * @see org.lh.dmlj.schema.SchemaPackage#getElement_Picture()
	 * @model
	 * @generated
	 */
	String getPicture();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Element#getPicture <em>Picture</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Picture</em>' attribute.
	 * @see #getPicture()
	 * @generated
	 */
	void setPicture(String value);

	/**
	 * Returns the value of the '<em><b>Nullable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Nullable</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Nullable</em>' attribute.
	 * @see #setNullable(boolean)
	 * @see org.lh.dmlj.schema.SchemaPackage#getElement_Nullable()
	 * @model
	 * @generated
	 */
	boolean isNullable();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Element#isNullable <em>Nullable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Nullable</em>' attribute.
	 * @see #isNullable()
	 * @generated
	 */
	void setNullable(boolean value);

	/**
	 * Returns the value of the '<em><b>Record</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.SchemaRecord#getElements <em>Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Record</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Record</em>' container reference.
	 * @see #setRecord(SchemaRecord)
	 * @see org.lh.dmlj.schema.SchemaPackage#getElement_Record()
	 * @see org.lh.dmlj.schema.SchemaRecord#getElements
	 * @model opposite="elements" transient="false"
	 * @generated
	 */
	SchemaRecord getRecord();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Element#getRecord <em>Record</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Record</em>' container reference.
	 * @see #getRecord()
	 * @generated
	 */
	void setRecord(SchemaRecord value);

	/**
	 * Returns the value of the '<em><b>Key Elements</b></em>' reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.KeyElement}.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.KeyElement#getElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Key Elements</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Key Elements</em>' reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getElement_KeyElements()
	 * @see org.lh.dmlj.schema.KeyElement#getElement
	 * @model opposite="element"
	 * @generated
	 */
	EList<KeyElement> getKeyElements();

	/**
	 * Returns the value of the '<em><b>Children</b></em>' reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.Element}.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.Element#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children</em>' reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getElement_Children()
	 * @see org.lh.dmlj.schema.Element#getParent
	 * @model opposite="parent"
	 * @generated
	 */
	EList<Element> getChildren();

	/**
	 * Returns the value of the '<em><b>Redefines</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Redefines</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Redefines</em>' reference.
	 * @see #setRedefines(Element)
	 * @see org.lh.dmlj.schema.SchemaPackage#getElement_Redefines()
	 * @model
	 * @generated
	 */
	Element getRedefines();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Element#getRedefines <em>Redefines</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Redefines</em>' reference.
	 * @see #getRedefines()
	 * @generated
	 */
	void setRedefines(Element value);

	/**
	 * Returns the value of the '<em><b>Syntax Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Syntax Length</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Syntax Length</em>' attribute.
	 * @see org.lh.dmlj.schema.SchemaPackage#getElement_SyntaxLength()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	short getSyntaxLength();

	/**
	 * Returns the value of the '<em><b>Syntax Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Syntax Position</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Syntax Position</em>' attribute.
	 * @see org.lh.dmlj.schema.SchemaPackage#getElement_SyntaxPosition()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	short getSyntaxPosition();

	/**
	 * Returns the value of the '<em><b>Occurs Specification</b></em>' containment reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.OccursSpecification#getElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Occurs Specification</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Occurs Specification</em>' containment reference.
	 * @see #setOccursSpecification(OccursSpecification)
	 * @see org.lh.dmlj.schema.SchemaPackage#getElement_OccursSpecification()
	 * @see org.lh.dmlj.schema.OccursSpecification#getElement
	 * @model opposite="element" containment="true"
	 * @generated
	 */
	OccursSpecification getOccursSpecification();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Element#getOccursSpecification <em>Occurs Specification</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Occurs Specification</em>' containment reference.
	 * @see #getOccursSpecification()
	 * @generated
	 */
	void setOccursSpecification(OccursSpecification value);

	/**
	 * Returns the value of the '<em><b>Parent</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.Element#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parent</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent</em>' reference.
	 * @see #setParent(Element)
	 * @see org.lh.dmlj.schema.SchemaPackage#getElement_Parent()
	 * @see org.lh.dmlj.schema.Element#getChildren
	 * @model opposite="children"
	 * @generated
	 */
	Element getParent();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Element#getParent <em>Parent</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent</em>' reference.
	 * @see #getParent()
	 * @generated
	 */
	void setParent(Element value);

} // Element
