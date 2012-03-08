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
 *   <li>{@link org.lh.dmlj.schema.Connector#getConnection <em>Connection</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Connector#getLabel <em>Label</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Connector#getLocation <em>Location</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getConnector()
 * @model
 * @generated
 */
public interface Connector extends DiagramNode {
	/**
	 * Returns the value of the '<em><b>Connection</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.Connection#getConnector <em>Connector</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Connection</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Connection</em>' reference.
	 * @see #setConnection(Connection)
	 * @see org.lh.dmlj.schema.SchemaPackage#getConnector_Connection()
	 * @see org.lh.dmlj.schema.Connection#getConnector
	 * @model opposite="connector" required="true"
	 * @generated
	 */
	Connection getConnection();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Connector#getConnection <em>Connection</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Connection</em>' reference.
	 * @see #getConnection()
	 * @generated
	 */
	void setConnection(Connection value);

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

	/**
	 * Returns the value of the '<em><b>Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Location</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Location</em>' reference.
	 * @see #setLocation(DiagramLocation)
	 * @see org.lh.dmlj.schema.SchemaPackage#getConnector_Location()
	 * @model required="true"
	 * @generated
	 */
	DiagramLocation getLocation();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Connector#getLocation <em>Location</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Location</em>' reference.
	 * @see #getLocation()
	 * @generated
	 */
	void setLocation(DiagramLocation value);

} // Connector
