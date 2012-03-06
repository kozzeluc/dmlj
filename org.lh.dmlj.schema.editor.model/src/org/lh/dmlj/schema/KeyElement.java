/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.lh.dmlj.schema;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Key Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.KeyElement#isDbkey <em>Dbkey</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.KeyElement#getElement <em>Element</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.KeyElement#getKey <em>Key</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.KeyElement#getSortSequence <em>Sort Sequence</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getKeyElement()
 * @model
 * @generated
 */
public interface KeyElement extends EObject {
	/**
	 * Returns the value of the '<em><b>Element</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.Element#getKeyElements <em>Key Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Element</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Element</em>' reference.
	 * @see #setElement(Element)
	 * @see org.lh.dmlj.schema.SchemaPackage#getKeyElement_Element()
	 * @see org.lh.dmlj.schema.Element#getKeyElements
	 * @model opposite="keyElements" required="true"
	 * @generated
	 */
	Element getElement();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.KeyElement#getElement <em>Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Element</em>' reference.
	 * @see #getElement()
	 * @generated
	 */
	void setElement(Element value);

	/**
	 * Returns the value of the '<em><b>Sort Sequence</b></em>' attribute.
	 * The literals are from the enumeration {@link org.lh.dmlj.schema.SortSequence}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sort Sequence</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sort Sequence</em>' attribute.
	 * @see org.lh.dmlj.schema.SortSequence
	 * @see #setSortSequence(SortSequence)
	 * @see org.lh.dmlj.schema.SchemaPackage#getKeyElement_SortSequence()
	 * @model
	 * @generated
	 */
	SortSequence getSortSequence();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.KeyElement#getSortSequence <em>Sort Sequence</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sort Sequence</em>' attribute.
	 * @see org.lh.dmlj.schema.SortSequence
	 * @see #getSortSequence()
	 * @generated
	 */
	void setSortSequence(SortSequence value);

	/**
	 * Returns the value of the '<em><b>Key</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.Key#getElements <em>Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Key</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Key</em>' container reference.
	 * @see #setKey(Key)
	 * @see org.lh.dmlj.schema.SchemaPackage#getKeyElement_Key()
	 * @see org.lh.dmlj.schema.Key#getElements
	 * @model opposite="elements" required="true" transient="false"
	 * @generated
	 */
	Key getKey();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.KeyElement#getKey <em>Key</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Key</em>' container reference.
	 * @see #getKey()
	 * @generated
	 */
	void setKey(Key value);

	/**
	 * Returns the value of the '<em><b>Dbkey</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dbkey</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dbkey</em>' attribute.
	 * @see org.lh.dmlj.schema.SchemaPackage#getKeyElement_Dbkey()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	boolean isDbkey();

} // KeyElement
