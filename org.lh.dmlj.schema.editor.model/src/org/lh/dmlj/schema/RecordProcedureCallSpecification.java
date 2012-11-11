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
 * A representation of the model object '<em><b>Record Procedure Call Specification</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.RecordProcedureCallSpecification#getCallTime <em>Call Time</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.RecordProcedureCallSpecification#getProcedure <em>Procedure</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.RecordProcedureCallSpecification#getRecord <em>Record</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.RecordProcedureCallSpecification#getVerb <em>Verb</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getRecordProcedureCallSpecification()
 * @model
 * @generated
 */
public interface RecordProcedureCallSpecification extends EObject {
	/**
	 * Returns the value of the '<em><b>Procedure</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Procedure</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Procedure</em>' reference.
	 * @see #setProcedure(Procedure)
	 * @see org.lh.dmlj.schema.SchemaPackage#getRecordProcedureCallSpecification_Procedure()
	 * @model required="true"
	 * @generated
	 */
	Procedure getProcedure();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.RecordProcedureCallSpecification#getProcedure <em>Procedure</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Procedure</em>' reference.
	 * @see #getProcedure()
	 * @generated
	 */
	void setProcedure(Procedure value);

	/**
	 * Returns the value of the '<em><b>Record</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.SchemaRecord#getProcedures <em>Procedures</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Record</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Record</em>' container reference.
	 * @see #setRecord(SchemaRecord)
	 * @see org.lh.dmlj.schema.SchemaPackage#getRecordProcedureCallSpecification_Record()
	 * @see org.lh.dmlj.schema.SchemaRecord#getProcedures
	 * @model opposite="procedures" required="true" transient="false"
	 * @generated
	 */
	SchemaRecord getRecord();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.RecordProcedureCallSpecification#getRecord <em>Record</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Record</em>' container reference.
	 * @see #getRecord()
	 * @generated
	 */
	void setRecord(SchemaRecord value);

	/**
	 * Returns the value of the '<em><b>Call Time</b></em>' attribute.
	 * The literals are from the enumeration {@link org.lh.dmlj.schema.ProcedureCallTime}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Call Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Call Time</em>' attribute.
	 * @see org.lh.dmlj.schema.ProcedureCallTime
	 * @see #setCallTime(ProcedureCallTime)
	 * @see org.lh.dmlj.schema.SchemaPackage#getRecordProcedureCallSpecification_CallTime()
	 * @model
	 * @generated
	 */
	ProcedureCallTime getCallTime();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.RecordProcedureCallSpecification#getCallTime <em>Call Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Call Time</em>' attribute.
	 * @see org.lh.dmlj.schema.ProcedureCallTime
	 * @see #getCallTime()
	 * @generated
	 */
	void setCallTime(ProcedureCallTime value);

	/**
	 * Returns the value of the '<em><b>Verb</b></em>' attribute.
	 * The literals are from the enumeration {@link org.lh.dmlj.schema.RecordProcedureCallVerb}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Verb</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Verb</em>' attribute.
	 * @see org.lh.dmlj.schema.RecordProcedureCallVerb
	 * @see #setVerb(RecordProcedureCallVerb)
	 * @see org.lh.dmlj.schema.SchemaPackage#getRecordProcedureCallSpecification_Verb()
	 * @model
	 * @generated
	 */
	RecordProcedureCallVerb getVerb();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.RecordProcedureCallSpecification#getVerb <em>Verb</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Verb</em>' attribute.
	 * @see org.lh.dmlj.schema.RecordProcedureCallVerb
	 * @see #getVerb()
	 * @generated
	 */
	void setVerb(RecordProcedureCallVerb value);

} // RecordProcedureCallSpecification
