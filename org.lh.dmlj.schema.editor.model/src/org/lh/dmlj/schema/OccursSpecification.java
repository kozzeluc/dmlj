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
 * A representation of the model object '<em><b>Occurs Specification</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.OccursSpecification#getCount <em>Count</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.OccursSpecification#getDependingOn <em>Depending On</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.OccursSpecification#getElement <em>Element</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getOccursSpecification()
 * @model
 * @generated
 */
public interface OccursSpecification extends EObject {
	/**
	 * Returns the value of the '<em><b>Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Count</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Count</em>' attribute.
	 * @see #setCount(short)
	 * @see org.lh.dmlj.schema.SchemaPackage#getOccursSpecification_Count()
	 * @model
	 * @generated
	 */
	short getCount();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.OccursSpecification#getCount <em>Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Count</em>' attribute.
	 * @see #getCount()
	 * @generated
	 */
	void setCount(short value);

	/**
	 * Returns the value of the '<em><b>Depending On</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Depending On</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Depending On</em>' reference.
	 * @see #setDependingOn(Element)
	 * @see org.lh.dmlj.schema.SchemaPackage#getOccursSpecification_DependingOn()
	 * @model
	 * @generated
	 */
	Element getDependingOn();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.OccursSpecification#getDependingOn <em>Depending On</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Depending On</em>' reference.
	 * @see #getDependingOn()
	 * @generated
	 */
	void setDependingOn(Element value);

	/**
	 * Returns the value of the '<em><b>Element</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.Element#getOccursSpecification <em>Occurs Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Element</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Element</em>' container reference.
	 * @see #setElement(Element)
	 * @see org.lh.dmlj.schema.SchemaPackage#getOccursSpecification_Element()
	 * @see org.lh.dmlj.schema.Element#getOccursSpecification
	 * @model opposite="occursSpecification" required="true" transient="false"
	 * @generated
	 */
	Element getElement();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.OccursSpecification#getElement <em>Element</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Element</em>' container reference.
	 * @see #getElement()
	 * @generated
	 */
	void setElement(Element value);

} // OccursSpecification
