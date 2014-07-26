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
 * A representation of the model object '<em><b>Area Procedure Call Specification</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.AreaProcedureCallSpecification#getArea <em>Area</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.AreaProcedureCallSpecification#getFunction <em>Function</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getAreaProcedureCallSpecification()
 * @model
 * @generated
 */
public interface AreaProcedureCallSpecification extends ProcedureCallSpecification {
	/**
	 * Returns the value of the '<em><b>Area</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.SchemaArea#getProcedures <em>Procedures</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Area</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Area</em>' container reference.
	 * @see #setArea(SchemaArea)
	 * @see org.lh.dmlj.schema.SchemaPackage#getAreaProcedureCallSpecification_Area()
	 * @see org.lh.dmlj.schema.SchemaArea#getProcedures
	 * @model opposite="procedures" required="true" transient="false"
	 * @generated
	 */
	SchemaArea getArea();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.AreaProcedureCallSpecification#getArea <em>Area</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Area</em>' container reference.
	 * @see #getArea()
	 * @generated
	 */
	void setArea(SchemaArea value);

	/**
	 * Returns the value of the '<em><b>Function</b></em>' attribute.
	 * The literals are from the enumeration {@link org.lh.dmlj.schema.AreaProcedureCallFunction}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Function</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Function</em>' attribute.
	 * @see org.lh.dmlj.schema.AreaProcedureCallFunction
	 * @see #setFunction(AreaProcedureCallFunction)
	 * @see org.lh.dmlj.schema.SchemaPackage#getAreaProcedureCallSpecification_Function()
	 * @model
	 * @generated
	 */
	AreaProcedureCallFunction getFunction();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.AreaProcedureCallSpecification#getFunction <em>Function</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Function</em>' attribute.
	 * @see org.lh.dmlj.schema.AreaProcedureCallFunction
	 * @see #getFunction()
	 * @generated
	 */
	void setFunction(AreaProcedureCallFunction value);

} // AreaProcedureCallSpecification
