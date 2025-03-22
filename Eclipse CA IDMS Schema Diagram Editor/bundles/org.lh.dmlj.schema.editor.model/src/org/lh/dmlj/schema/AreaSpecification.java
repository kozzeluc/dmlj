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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Area Specification</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.AreaSpecification#getSymbolicSubareaName <em>Symbolic Subarea Name</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.AreaSpecification#getArea <em>Area</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.AreaSpecification#getOffsetExpression <em>Offset Expression</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.AreaSpecification#getRecord <em>Record</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.AreaSpecification#getSystemOwner <em>System Owner</em>}</li>
 * </ul>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getAreaSpecification()
 * @model
 * @generated
 */
public interface AreaSpecification extends EObject {
	/**
	 * Returns the value of the '<em><b>Symbolic Subarea Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Symbolic Subarea Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Symbolic Subarea Name</em>' attribute.
	 * @see #setSymbolicSubareaName(String)
	 * @see org.lh.dmlj.schema.SchemaPackage#getAreaSpecification_SymbolicSubareaName()
	 * @model
	 * @generated
	 */
	String getSymbolicSubareaName();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.AreaSpecification#getSymbolicSubareaName <em>Symbolic Subarea Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Symbolic Subarea Name</em>' attribute.
	 * @see #getSymbolicSubareaName()
	 * @generated
	 */
	void setSymbolicSubareaName(String value);

	/**
	 * Returns the value of the '<em><b>Area</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.SchemaArea#getAreaSpecifications <em>Area Specifications</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Area</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Area</em>' container reference.
	 * @see #setArea(SchemaArea)
	 * @see org.lh.dmlj.schema.SchemaPackage#getAreaSpecification_Area()
	 * @see org.lh.dmlj.schema.SchemaArea#getAreaSpecifications
	 * @model opposite="areaSpecifications" required="true" transient="false"
	 * @generated
	 */
	SchemaArea getArea();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.AreaSpecification#getArea <em>Area</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Area</em>' container reference.
	 * @see #getArea()
	 * @generated
	 */
	void setArea(SchemaArea value);

	/**
	 * Returns the value of the '<em><b>Offset Expression</b></em>' containment reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.OffsetExpression#getAreaSpecification <em>Area Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Offset Expression</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Offset Expression</em>' containment reference.
	 * @see #setOffsetExpression(OffsetExpression)
	 * @see org.lh.dmlj.schema.SchemaPackage#getAreaSpecification_OffsetExpression()
	 * @see org.lh.dmlj.schema.OffsetExpression#getAreaSpecification
	 * @model opposite="areaSpecification" containment="true"
	 * @generated
	 */
	OffsetExpression getOffsetExpression();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.AreaSpecification#getOffsetExpression <em>Offset Expression</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Offset Expression</em>' containment reference.
	 * @see #getOffsetExpression()
	 * @generated
	 */
	void setOffsetExpression(OffsetExpression value);

	/**
	 * Returns the value of the '<em><b>Record</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.SchemaRecord#getAreaSpecification <em>Area Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Record</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Record</em>' reference.
	 * @see #setRecord(SchemaRecord)
	 * @see org.lh.dmlj.schema.SchemaPackage#getAreaSpecification_Record()
	 * @see org.lh.dmlj.schema.SchemaRecord#getAreaSpecification
	 * @model opposite="areaSpecification" required="true"
	 * @generated
	 */
	SchemaRecord getRecord();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.AreaSpecification#getRecord <em>Record</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Record</em>' reference.
	 * @see #getRecord()
	 * @generated
	 */
	void setRecord(SchemaRecord value);

	/**
	 * Returns the value of the '<em><b>System Owner</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.SystemOwner#getAreaSpecification <em>Area Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>System Owner</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>System Owner</em>' reference.
	 * @see #setSystemOwner(SystemOwner)
	 * @see org.lh.dmlj.schema.SchemaPackage#getAreaSpecification_SystemOwner()
	 * @see org.lh.dmlj.schema.SystemOwner#getAreaSpecification
	 * @model opposite="areaSpecification" required="true"
	 * @generated
	 */
	SystemOwner getSystemOwner();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.AreaSpecification#getSystemOwner <em>System Owner</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>System Owner</em>' reference.
	 * @see #getSystemOwner()
	 * @generated
	 */
	void setSystemOwner(SystemOwner value);

} // AreaSpecification
