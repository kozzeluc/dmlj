/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.lh.dmlj.schema;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>System Owner</b></em>'.
 * @extends DiagramLocationProvider
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.SystemOwner#getAreaSpecification <em>Area Specification</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.SystemOwner#getSet <em>Set</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getSystemOwner()
 * @model
 * @generated
 */
public interface SystemOwner extends DiagramNode, DiagramLocationProvider {
	/**
	 * Returns the value of the '<em><b>Area Specification</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.AreaSpecification#getSystemOwner <em>System Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Area Specification</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Area Specification</em>' reference.
	 * @see #setAreaSpecification(AreaSpecification)
	 * @see org.lh.dmlj.schema.SchemaPackage#getSystemOwner_AreaSpecification()
	 * @see org.lh.dmlj.schema.AreaSpecification#getSystemOwner
	 * @model opposite="systemOwner"
	 * @generated
	 */
	AreaSpecification getAreaSpecification();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.SystemOwner#getAreaSpecification <em>Area Specification</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Area Specification</em>' reference.
	 * @see #getAreaSpecification()
	 * @generated
	 */
	void setAreaSpecification(AreaSpecification value);

	/**
	 * Returns the value of the '<em><b>Set</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.Set#getSystemOwner <em>System Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Set</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Set</em>' container reference.
	 * @see #setSet(Set)
	 * @see org.lh.dmlj.schema.SchemaPackage#getSystemOwner_Set()
	 * @see org.lh.dmlj.schema.Set#getSystemOwner
	 * @model opposite="systemOwner" required="true" transient="false"
	 * @generated
	 */
	Set getSet();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.SystemOwner#getSet <em>Set</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Set</em>' container reference.
	 * @see #getSet()
	 * @generated
	 */
	void setSet(Set value);

} // SystemOwner
