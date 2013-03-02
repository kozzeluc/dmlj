/**
 */
package org.lh.dmlj.schema;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Index Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.IndexElement#getBaseName <em>Base Name</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.IndexElement#getName <em>Name</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.IndexElement#getOccursSpecification <em>Occurs Specification</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getIndexElement()
 * @model
 * @generated
 */
public interface IndexElement extends EObject {
	/**
	 * Returns the value of the '<em><b>Base Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Name</em>' attribute.
	 * @see #setBaseName(String)
	 * @see org.lh.dmlj.schema.SchemaPackage#getIndexElement_BaseName()
	 * @model
	 * @generated
	 */
	String getBaseName();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.IndexElement#getBaseName <em>Base Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Name</em>' attribute.
	 * @see #getBaseName()
	 * @generated
	 */
	void setBaseName(String value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.lh.dmlj.schema.SchemaPackage#getIndexElement_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.IndexElement#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Occurs Specification</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.OccursSpecification#getIndexElements <em>Index Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Occurs Specification</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Occurs Specification</em>' container reference.
	 * @see #setOccursSpecification(OccursSpecification)
	 * @see org.lh.dmlj.schema.SchemaPackage#getIndexElement_OccursSpecification()
	 * @see org.lh.dmlj.schema.OccursSpecification#getIndexElements
	 * @model opposite="indexElements" required="true" transient="false"
	 * @generated
	 */
	OccursSpecification getOccursSpecification();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.IndexElement#getOccursSpecification <em>Occurs Specification</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Occurs Specification</em>' container reference.
	 * @see #getOccursSpecification()
	 * @generated
	 */
	void setOccursSpecification(OccursSpecification value);

} // IndexElement
