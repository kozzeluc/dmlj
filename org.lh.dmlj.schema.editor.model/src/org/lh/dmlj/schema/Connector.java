/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.lh.dmlj.schema;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Connector</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.Connector#getConnectionPart <em>Connection Part</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Connector#getLabel <em>Label</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getConnector()
 * @model
 * @generated
 */
public interface Connector extends DiagramNode {
	/**
	 * Returns the value of the '<em><b>Connection Part</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.ConnectionPart#getConnector <em>Connector</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Connection Part</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Connection Part</em>' reference.
	 * @see #setConnectionPart(ConnectionPart)
	 * @see org.lh.dmlj.schema.SchemaPackage#getConnector_ConnectionPart()
	 * @see org.lh.dmlj.schema.ConnectionPart#getConnector
	 * @model opposite="connector" required="true"
	 * @generated
	 */
	ConnectionPart getConnectionPart();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Connector#getConnectionPart <em>Connection Part</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Connection Part</em>' reference.
	 * @see #getConnectionPart()
	 * @generated
	 */
	void setConnectionPart(ConnectionPart value);

	/**
	 * Returns the value of the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Label</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Label</em>' attribute.
	 * @see #setLabel(String)
	 * @see org.lh.dmlj.schema.SchemaPackage#getConnector_Label()
	 * @model
	 * @generated
	 */
	String getLabel();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Connector#getLabel <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Label</em>' attribute.
	 * @see #getLabel()
	 * @generated
	 */
	void setLabel(String value);

} // Connector
