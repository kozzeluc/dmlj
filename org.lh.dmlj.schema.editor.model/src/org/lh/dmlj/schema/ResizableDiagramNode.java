/**
 */
package org.lh.dmlj.schema;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Resizable Diagram Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.ResizableDiagramNode#getHeight <em>Height</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.ResizableDiagramNode#getWidth <em>Width</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getResizableDiagramNode()
 * @model abstract="true"
 * @generated
 */
public interface ResizableDiagramNode extends DiagramNode {
	/**
	 * Returns the value of the '<em><b>Height</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Height</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Height</em>' attribute.
	 * @see #setHeight(short)
	 * @see org.lh.dmlj.schema.SchemaPackage#getResizableDiagramNode_Height()
	 * @model
	 * @generated
	 */
	short getHeight();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.ResizableDiagramNode#getHeight <em>Height</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Height</em>' attribute.
	 * @see #getHeight()
	 * @generated
	 */
	void setHeight(short value);

	/**
	 * Returns the value of the '<em><b>Width</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Width</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Width</em>' attribute.
	 * @see #setWidth(short)
	 * @see org.lh.dmlj.schema.SchemaPackage#getResizableDiagramNode_Width()
	 * @model
	 * @generated
	 */
	short getWidth();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.ResizableDiagramNode#getWidth <em>Width</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Width</em>' attribute.
	 * @see #getWidth()
	 * @generated
	 */
	void setWidth(short value);

} // ResizableDiagramNode
