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
 * A representation of the model object '<em><b>Diagram Data</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.DiagramData#getConnectionLabels <em>Connection Labels</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.DiagramData#getConnectionParts <em>Connection Parts</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.DiagramData#getConnectors <em>Connectors</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.DiagramData#getHorizontalRuler <em>Horizontal Ruler</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.DiagramData#getLabel <em>Label</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.DiagramData#getLocations <em>Locations</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.DiagramData#getRulers <em>Rulers</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.DiagramData#getSchema <em>Schema</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.DiagramData#isShowGrid <em>Show Grid</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.DiagramData#isShowRulers <em>Show Rulers</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.DiagramData#isSnapToGeometry <em>Snap To Geometry</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.DiagramData#isSnapToGrid <em>Snap To Grid</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.DiagramData#isSnapToGuides <em>Snap To Guides</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.DiagramData#getVerticalRuler <em>Vertical Ruler</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.DiagramData#getZoomLevel <em>Zoom Level</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramData()
 * @model
 * @generated
 */
public interface DiagramData extends EObject {
	/**
	 * Returns the value of the '<em><b>Connection Labels</b></em>' containment reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.ConnectionLabel}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Connection Labels</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Connection Labels</em>' containment reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramData_ConnectionLabels()
	 * @model containment="true"
	 * @generated
	 */
	EList<ConnectionLabel> getConnectionLabels();

	/**
	 * Returns the value of the '<em><b>Connection Parts</b></em>' containment reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.ConnectionPart}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Connection Parts</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Connection Parts</em>' containment reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramData_ConnectionParts()
	 * @model containment="true"
	 * @generated
	 */
	EList<ConnectionPart> getConnectionParts();

	/**
	 * Returns the value of the '<em><b>Connectors</b></em>' containment reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.Connector}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Connectors</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Connectors</em>' containment reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramData_Connectors()
	 * @model containment="true"
	 * @generated
	 */
	EList<Connector> getConnectors();

	/**
	 * Returns the value of the '<em><b>Horizontal Ruler</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Horizontal Ruler</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Horizontal Ruler</em>' reference.
	 * @see #setHorizontalRuler(Ruler)
	 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramData_HorizontalRuler()
	 * @model required="true"
	 * @generated
	 */
	Ruler getHorizontalRuler();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.DiagramData#getHorizontalRuler <em>Horizontal Ruler</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Horizontal Ruler</em>' reference.
	 * @see #getHorizontalRuler()
	 * @generated
	 */
	void setHorizontalRuler(Ruler value);

	/**
	 * Returns the value of the '<em><b>Label</b></em>' containment reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.DiagramLabel#getDiagramData <em>Diagram Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Label</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Label</em>' containment reference.
	 * @see #setLabel(DiagramLabel)
	 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramData_Label()
	 * @see org.lh.dmlj.schema.DiagramLabel#getDiagramData
	 * @model opposite="diagramData" containment="true"
	 * @generated
	 */
	DiagramLabel getLabel();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.DiagramData#getLabel <em>Label</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Label</em>' containment reference.
	 * @see #getLabel()
	 * @generated
	 */
	void setLabel(DiagramLabel value);

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
	 * Returns the value of the '<em><b>Rulers</b></em>' containment reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.Ruler}.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.Ruler#getDiagramData <em>Diagram Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rulers</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rulers</em>' containment reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramData_Rulers()
	 * @see org.lh.dmlj.schema.Ruler#getDiagramData
	 * @model opposite="diagramData" containment="true" lower="2" upper="2"
	 * @generated
	 */
	EList<Ruler> getRulers();

	/**
	 * Returns the value of the '<em><b>Schema</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.Schema#getDiagramData <em>Diagram Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Schema</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Schema</em>' container reference.
	 * @see #setSchema(Schema)
	 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramData_Schema()
	 * @see org.lh.dmlj.schema.Schema#getDiagramData
	 * @model opposite="diagramData" required="true" transient="false"
	 * @generated
	 */
	Schema getSchema();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.DiagramData#getSchema <em>Schema</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Schema</em>' container reference.
	 * @see #getSchema()
	 * @generated
	 */
	void setSchema(Schema value);

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

	/**
	 * Returns the value of the '<em><b>Show Rulers</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Show Rulers</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Show Rulers</em>' attribute.
	 * @see #setShowRulers(boolean)
	 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramData_ShowRulers()
	 * @model
	 * @generated
	 */
	boolean isShowRulers();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.DiagramData#isShowRulers <em>Show Rulers</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Show Rulers</em>' attribute.
	 * @see #isShowRulers()
	 * @generated
	 */
	void setShowRulers(boolean value);

	/**
	 * Returns the value of the '<em><b>Snap To Geometry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Snap To Geometry</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Snap To Geometry</em>' attribute.
	 * @see #setSnapToGeometry(boolean)
	 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramData_SnapToGeometry()
	 * @model
	 * @generated
	 */
	boolean isSnapToGeometry();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.DiagramData#isSnapToGeometry <em>Snap To Geometry</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Snap To Geometry</em>' attribute.
	 * @see #isSnapToGeometry()
	 * @generated
	 */
	void setSnapToGeometry(boolean value);

	/**
	 * Returns the value of the '<em><b>Snap To Grid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Snap To Grid</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Snap To Grid</em>' attribute.
	 * @see #setSnapToGrid(boolean)
	 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramData_SnapToGrid()
	 * @model
	 * @generated
	 */
	boolean isSnapToGrid();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.DiagramData#isSnapToGrid <em>Snap To Grid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Snap To Grid</em>' attribute.
	 * @see #isSnapToGrid()
	 * @generated
	 */
	void setSnapToGrid(boolean value);

	/**
	 * Returns the value of the '<em><b>Snap To Guides</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Snap To Guides</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Snap To Guides</em>' attribute.
	 * @see #setSnapToGuides(boolean)
	 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramData_SnapToGuides()
	 * @model
	 * @generated
	 */
	boolean isSnapToGuides();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.DiagramData#isSnapToGuides <em>Snap To Guides</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Snap To Guides</em>' attribute.
	 * @see #isSnapToGuides()
	 * @generated
	 */
	void setSnapToGuides(boolean value);

	/**
	 * Returns the value of the '<em><b>Vertical Ruler</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vertical Ruler</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vertical Ruler</em>' reference.
	 * @see #setVerticalRuler(Ruler)
	 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramData_VerticalRuler()
	 * @model required="true"
	 * @generated
	 */
	Ruler getVerticalRuler();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.DiagramData#getVerticalRuler <em>Vertical Ruler</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vertical Ruler</em>' reference.
	 * @see #getVerticalRuler()
	 * @generated
	 */
	void setVerticalRuler(Ruler value);

} // DiagramData
