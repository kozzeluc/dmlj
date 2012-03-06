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
 * A representation of the model object '<em><b>Diagram Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.DiagramNode#getDiagramLocation <em>Diagram Location</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramNode()
 * @model abstract="true"
 * @generated
 */
public interface DiagramNode extends EObject {
	/**
	 * Returns the value of the '<em><b>Diagram Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Diagram Location</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Diagram Location</em>' reference.
	 * @see #setDiagramLocation(DiagramLocation)
	 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramNode_DiagramLocation()
	 * @model required="true"
	 * @generated
	 */
	DiagramLocation getDiagramLocation();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.DiagramNode#getDiagramLocation <em>Diagram Location</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Diagram Location</em>' reference.
	 * @see #getDiagramLocation()
	 * @generated
	 */
	void setDiagramLocation(DiagramLocation value);

} // DiagramNode
