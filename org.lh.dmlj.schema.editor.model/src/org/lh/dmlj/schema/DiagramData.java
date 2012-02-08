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
 * A representation of the model object '<em><b>Diagram Data</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.DiagramData#getLocations <em>Locations</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.DiagramData#getZoomLevel <em>Zoom Level</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.DiagramData#isShowGrid <em>Show Grid</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramData()
 * @model
 * @generated
 */
public interface DiagramData extends EObject {
	/**
	 * Returns the value of the '<em><b>Locations</b></em>' containment reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.DiagramLocation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Locations</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Locations</em>' containment reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramData_Locations()
	 * @model containment="true"
	 * @generated
	 */
	EList<DiagramLocation> getLocations();

	/**
	 * Returns the value of the '<em><b>Zoom Level</b></em>' attribute.
	 * The default value is <code>"1.0d"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Zoom Level</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Zoom Level</em>' attribute.
	 * @see #setZoomLevel(double)
	 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramData_ZoomLevel()
	 * @model default="1.0d"
	 * @generated
	 */
	double getZoomLevel();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.DiagramData#getZoomLevel <em>Zoom Level</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Zoom Level</em>' attribute.
	 * @see #getZoomLevel()
	 * @generated
	 */
	void setZoomLevel(double value);

	/**
	 * Returns the value of the '<em><b>Show Grid</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Show Grid</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Show Grid</em>' attribute.
	 * @see #setShowGrid(boolean)
	 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramData_ShowGrid()
	 * @model default="false"
	 * @generated
	 */
	boolean isShowGrid();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.DiagramData#isShowGrid <em>Show Grid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Show Grid</em>' attribute.
	 * @see #isShowGrid()
	 * @generated
	 */
	void setShowGrid(boolean value);

} // DiagramData
