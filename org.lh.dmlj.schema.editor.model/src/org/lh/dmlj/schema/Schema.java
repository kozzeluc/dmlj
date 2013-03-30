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
 * A representation of the model object '<em><b>Schema</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.Schema#getAreas <em>Areas</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Schema#getComments <em>Comments</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Schema#getDescription <em>Description</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Schema#getDiagramData <em>Diagram Data</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Schema#getMemoDate <em>Memo Date</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Schema#getName <em>Name</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Schema#getProcedures <em>Procedures</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Schema#getRecords <em>Records</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Schema#getSets <em>Sets</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.Schema#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getSchema()
 * @model
 * @generated
 */
public interface Schema extends EObject {
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
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchema_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Schema#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Version</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Version</em>' attribute.
	 * @see #setVersion(short)
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchema_Version()
	 * @model
	 * @generated
	 */
	short getVersion();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Schema#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version</em>' attribute.
	 * @see #getVersion()
	 * @generated
	 */
	void setVersion(short value);

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
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchema_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Schema#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Memo Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Memo Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Memo Date</em>' attribute.
	 * @see #setMemoDate(String)
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchema_MemoDate()
	 * @model
	 * @generated
	 */
	String getMemoDate();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Schema#getMemoDate <em>Memo Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Memo Date</em>' attribute.
	 * @see #getMemoDate()
	 * @generated
	 */
	void setMemoDate(String value);

	/**
	 * Returns the value of the '<em><b>Areas</b></em>' containment reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.SchemaArea}.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.SchemaArea#getSchema <em>Schema</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Areas</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Areas</em>' containment reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchema_Areas()
	 * @see org.lh.dmlj.schema.SchemaArea#getSchema
	 * @model opposite="schema" containment="true"
	 * @generated
	 */
	EList<SchemaArea> getAreas();

	/**
	 * Returns the value of the '<em><b>Comments</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Comments</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Comments</em>' attribute list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchema_Comments()
	 * @model derived="true"
	 * @generated
	 */
	EList<String> getComments();

	/**
	 * Returns the value of the '<em><b>Records</b></em>' containment reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.SchemaRecord}.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.SchemaRecord#getSchema <em>Schema</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Records</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Records</em>' containment reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchema_Records()
	 * @see org.lh.dmlj.schema.SchemaRecord#getSchema
	 * @model opposite="schema" containment="true"
	 * @generated
	 */
	EList<SchemaRecord> getRecords();

	/**
	 * Returns the value of the '<em><b>Sets</b></em>' containment reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.Set}.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.Set#getSchema <em>Schema</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sets</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sets</em>' containment reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchema_Sets()
	 * @see org.lh.dmlj.schema.Set#getSchema
	 * @model opposite="schema" containment="true"
	 * @generated
	 */
	EList<Set> getSets();

	/**
	 * Returns the value of the '<em><b>Diagram Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Diagram Data</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Diagram Data</em>' containment reference.
	 * @see #setDiagramData(DiagramData)
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchema_DiagramData()
	 * @model containment="true" required="true"
	 * @generated
	 */
	DiagramData getDiagramData();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.Schema#getDiagramData <em>Diagram Data</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Diagram Data</em>' containment reference.
	 * @see #getDiagramData()
	 * @generated
	 */
	void setDiagramData(DiagramData value);

	/**
	 * Returns the value of the '<em><b>Procedures</b></em>' containment reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.Procedure}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Procedures</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Procedures</em>' containment reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchema_Procedures()
	 * @model containment="true"
	 * @generated
	 */
	EList<Procedure> getProcedures();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model nameRequired="true"
	 * @generated
	 */
	Procedure getProcedure(String name);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model nameRequired="true"
	 * @generated
	 */
	SchemaArea getArea(String name);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model nameRequired="true"
	 * @generated
	 */
	SchemaRecord getRecord(String name);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model areaNameRequired="true" recordIdRequired="true"
	 * @generated
	 */
	SchemaRecord getRecord(String areaName, short recordId);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model nameRequired="true"
	 * @generated
	 */
	Set getSet(String name);

} // Schema
