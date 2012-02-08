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
 * A representation of the model object '<em><b>Diagram Location</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.DiagramLocation#getX <em>X</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.DiagramLocation#getY <em>Y</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.DiagramLocation#getEyecatcher <em>Eyecatcher</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramLocation()
 * @model
 * @generated
 */
public interface DiagramLocation extends EObject {
	/**
	 * Returns the value of the '<em><b>X</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>X</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>X</em>' attribute.
	 * @see #setX(int)
	 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramLocation_X()
	 * @model
	 * @generated
	 */
	int getX();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.DiagramLocation#getX <em>X</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>X</em>' attribute.
	 * @see #getX()
	 * @generated
	 */
	void setX(int value);

	/**
	 * Returns the value of the '<em><b>Y</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Y</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Y</em>' attribute.
	 * @see #setY(int)
	 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramLocation_Y()
	 * @model
	 * @generated
	 */
	int getY();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.DiagramLocation#getY <em>Y</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Y</em>' attribute.
	 * @see #getY()
	 * @generated
	 */
	void setY(int value);

	/**
	 * Returns the value of the '<em><b>Eyecatcher</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Eyecatcher</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Eyecatcher</em>' attribute.
	 * @see #setEyecatcher(String)
	 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramLocation_Eyecatcher()
	 * @model
	 * @generated
	 */
	String getEyecatcher();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.DiagramLocation#getEyecatcher <em>Eyecatcher</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Eyecatcher</em>' attribute.
	 * @see #getEyecatcher()
	 * @generated
	 */
	void setEyecatcher(String value);

} // DiagramLocation
