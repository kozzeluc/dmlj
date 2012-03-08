/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.lh.dmlj.schema;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ruler</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.Ruler#getGuides <em>Guides</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Ruler#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getRuler()
 * @model
 * @generated
 */
public interface Ruler extends EObject {
	/**
	 * Returns the value of the '<em><b>Guides</b></em>' containment reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.Guide}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Guides</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Guides</em>' containment reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getRuler_Guides()
	 * @model containment="true"
	 * @generated
	 */
	EList<Guide> getGuides();

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The literals are from the enumeration {@link org.lh.dmlj.schema.RulerType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see org.lh.dmlj.schema.RulerType
	 * @see #setType(RulerType)
	 * @see org.lh.dmlj.schema.SchemaPackage#getRuler_Type()
	 * @model
	 * @generated
	 */
	RulerType getType();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Ruler#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see org.lh.dmlj.schema.RulerType
	 * @see #getType()
	 * @generated
	 */
	void setType(RulerType value);

} // Ruler
