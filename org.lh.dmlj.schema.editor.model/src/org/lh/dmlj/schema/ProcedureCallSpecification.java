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
 * A representation of the model object '<em><b>Procedure Call Specification</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.ProcedureCallSpecification#getCallTime <em>Call Time</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.ProcedureCallSpecification#getProcedure <em>Procedure</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getProcedureCallSpecification()
 * @model abstract="true"
 * @generated
 */
public interface ProcedureCallSpecification extends EObject {
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
	 * @see org.lh.dmlj.schema.SchemaPackage#getProcedureCallSpecification_CallTime()
	 * @model
	 * @generated
	 */
	ProcedureCallTime getCallTime();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.ProcedureCallSpecification#getCallTime <em>Call Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Call Time</em>' attribute.
	 * @see org.lh.dmlj.schema.ProcedureCallTime
	 * @see #getCallTime()
	 * @generated
	 */
	void setCallTime(ProcedureCallTime value);

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
	 * @see org.lh.dmlj.schema.SchemaPackage#getProcedureCallSpecification_Procedure()
	 * @model required="true"
	 * @generated
	 */
	Procedure getProcedure();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.ProcedureCallSpecification#getProcedure <em>Procedure</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Procedure</em>' reference.
	 * @see #getProcedure()
	 * @generated
	 */
	void setProcedure(Procedure value);

} // ProcedureCallSpecification
