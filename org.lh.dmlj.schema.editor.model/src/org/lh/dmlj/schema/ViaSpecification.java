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
 * A representation of the model object '<em><b>Via Specification</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.ViaSpecification#getDisplacementPageCount <em>Displacement Page Count</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.ViaSpecification#getRecord <em>Record</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.ViaSpecification#getSet <em>Set</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.ViaSpecification#getSymbolicDisplacementName <em>Symbolic Displacement Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getViaSpecification()
 * @model
 * @generated
 */
public interface ViaSpecification extends EObject {
	/**
	 * Returns the value of the '<em><b>Symbolic Displacement Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Symbolic Displacement Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Symbolic Displacement Name</em>' attribute.
	 * @see #setSymbolicDisplacementName(String)
	 * @see org.lh.dmlj.schema.SchemaPackage#getViaSpecification_SymbolicDisplacementName()
	 * @model
	 * @generated
	 */
	String getSymbolicDisplacementName();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.ViaSpecification#getSymbolicDisplacementName <em>Symbolic Displacement Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Symbolic Displacement Name</em>' attribute.
	 * @see #getSymbolicDisplacementName()
	 * @generated
	 */
	void setSymbolicDisplacementName(String value);

	/**
	 * Returns the value of the '<em><b>Set</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.Set#getViaMembers <em>Via Members</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Set</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Set</em>' reference.
	 * @see #setSet(Set)
	 * @see org.lh.dmlj.schema.SchemaPackage#getViaSpecification_Set()
	 * @see org.lh.dmlj.schema.Set#getViaMembers
	 * @model opposite="viaMembers" required="true"
	 * @generated
	 */
	Set getSet();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.ViaSpecification#getSet <em>Set</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Set</em>' reference.
	 * @see #getSet()
	 * @generated
	 */
	void setSet(Set value);

	/**
	 * Returns the value of the '<em><b>Record</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.SchemaRecord#getViaSpecification <em>Via Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Record</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Record</em>' container reference.
	 * @see #setRecord(SchemaRecord)
	 * @see org.lh.dmlj.schema.SchemaPackage#getViaSpecification_Record()
	 * @see org.lh.dmlj.schema.SchemaRecord#getViaSpecification
	 * @model opposite="viaSpecification" required="true" transient="false"
	 * @generated
	 */
	SchemaRecord getRecord();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.ViaSpecification#getRecord <em>Record</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Record</em>' container reference.
	 * @see #getRecord()
	 * @generated
	 */
	void setRecord(SchemaRecord value);

	/**
	 * Returns the value of the '<em><b>Displacement Page Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Displacement Page Count</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Displacement Page Count</em>' attribute.
	 * @see #setDisplacementPageCount(Short)
	 * @see org.lh.dmlj.schema.SchemaPackage#getViaSpecification_DisplacementPageCount()
	 * @model
	 * @generated
	 */
	Short getDisplacementPageCount();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.ViaSpecification#getDisplacementPageCount <em>Displacement Page Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Displacement Page Count</em>' attribute.
	 * @see #getDisplacementPageCount()
	 * @generated
	 */
	void setDisplacementPageCount(Short value);

} // ViaSpecification
