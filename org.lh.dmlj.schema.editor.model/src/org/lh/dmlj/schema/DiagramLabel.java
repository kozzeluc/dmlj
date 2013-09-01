/**
 */
package org.lh.dmlj.schema;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diagram Label</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.DiagramLabel#getDescription <em>Description</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.DiagramLabel#getDiagramData <em>Diagram Data</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramLabel()
 * @model
 * @generated
 */
public interface DiagramLabel extends ResizableDiagramNode {
	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramLabel_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.DiagramLabel#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Diagram Data</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.DiagramData#getLabel <em>Label</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Diagram Data</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Diagram Data</em>' container reference.
	 * @see #setDiagramData(DiagramData)
	 * @see org.lh.dmlj.schema.SchemaPackage#getDiagramLabel_DiagramData()
	 * @see org.lh.dmlj.schema.DiagramData#getLabel
	 * @model opposite="label" required="true" transient="false"
	 * @generated
	 */
	DiagramData getDiagramData();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.DiagramLabel#getDiagramData <em>Diagram Data</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Diagram Data</em>' container reference.
	 * @see #getDiagramData()
	 * @generated
	 */
	void setDiagramData(DiagramData value);

} // DiagramLabel
