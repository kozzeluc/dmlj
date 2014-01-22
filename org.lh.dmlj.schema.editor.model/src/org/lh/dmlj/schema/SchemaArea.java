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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Area</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.SchemaArea#getAreaSpecifications <em>Area Specifications</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.SchemaArea#getIndexes <em>Indexes</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.SchemaArea#getName <em>Name</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.SchemaArea#getProcedures <em>Procedures</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.SchemaArea#getRecords <em>Records</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.SchemaArea#getSchema <em>Schema</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaArea()
 * @model
 * @generated
 */
public interface SchemaArea extends INodeTextProvider<SchemaArea> {
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
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaArea_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.SchemaArea#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Schema</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.Schema#getAreas <em>Areas</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Schema</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Schema</em>' container reference.
	 * @see #setSchema(Schema)
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaArea_Schema()
	 * @see org.lh.dmlj.schema.Schema#getAreas
	 * @model opposite="areas" required="true" transient="false"
	 * @generated
	 */
	Schema getSchema();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.SchemaArea#getSchema <em>Schema</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Schema</em>' container reference.
	 * @see #getSchema()
	 * @generated
	 */
	void setSchema(Schema value);

	/**
	 * Returns the value of the '<em><b>Indexes</b></em>' reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.SystemOwner}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Indexes</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Indexes</em>' reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaArea_Indexes()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	EList<SystemOwner> getIndexes();

	/**
	 * Returns the value of the '<em><b>Procedures</b></em>' containment reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.AreaProcedureCallSpecification}.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.AreaProcedureCallSpecification#getArea <em>Area</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Procedures</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Procedures</em>' containment reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaArea_Procedures()
	 * @see org.lh.dmlj.schema.AreaProcedureCallSpecification#getArea
	 * @model opposite="area" containment="true"
	 * @generated
	 */
	EList<AreaProcedureCallSpecification> getProcedures();

	/**
	 * Returns the value of the '<em><b>Area Specifications</b></em>' containment reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.AreaSpecification}.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.AreaSpecification#getArea <em>Area</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Area Specifications</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Area Specifications</em>' containment reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaArea_AreaSpecifications()
	 * @see org.lh.dmlj.schema.AreaSpecification#getArea
	 * @model opposite="area" containment="true"
	 * @generated
	 */
	EList<AreaSpecification> getAreaSpecifications();

	/**
	 * Returns the value of the '<em><b>Records</b></em>' reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.SchemaRecord}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Records</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Records</em>' reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaArea_Records()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	EList<SchemaRecord> getRecords();

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
	 * @model recordIdRequired="true"
	 * @generated
	 */
	SchemaRecord getRecord(short recordId);

} // SchemaArea
