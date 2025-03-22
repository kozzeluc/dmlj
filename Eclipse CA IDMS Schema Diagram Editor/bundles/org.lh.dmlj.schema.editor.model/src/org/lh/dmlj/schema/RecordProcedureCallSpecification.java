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


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Record Procedure Call Specification</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.RecordProcedureCallSpecification#getRecord <em>Record</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.RecordProcedureCallSpecification#getVerb <em>Verb</em>}</li>
 * </ul>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getRecordProcedureCallSpecification()
 * @model
 * @generated
 */
public interface RecordProcedureCallSpecification extends ProcedureCallSpecification {
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
