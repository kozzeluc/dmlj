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
 * A representation of the model object '<em><b>Area Procedure Call Specification</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.AreaProcedureCallSpecification#getArea <em>Area</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.AreaProcedureCallSpecification#getCallTime <em>Call Time</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.AreaProcedureCallSpecification#getFunction <em>Function</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.AreaProcedureCallSpecification#getProcedure <em>Procedure</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getAreaProcedureCallSpecification()
 * @model
 * @generated
 */
public interface AreaProcedureCallSpecification extends EObject {
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
	 * Returns the value of the '<em><b>Procedure</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Procedure</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Procedure</em>' reference.
	 * @see #setProcedure(Procedure)
	 * @see org.lh.dmlj.schema.SchemaPackage#getAreaProcedureCallSpecification_Procedure()
	 * @model required="true"
	 * @generated
	 */
	Procedure getProcedure();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.AreaProcedureCallSpecification#getProcedure <em>Procedure</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Procedure</em>' reference.
	 * @see #getProcedure()
	 * @generated
	 */
	void setProcedure(Procedure value);

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
	 * @see org.lh.dmlj.schema.SchemaPackage#getAreaProcedureCallSpecification_CallTime()
	 * @model
	 * @generated
	 */
	ProcedureCallTime getCallTime();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.AreaProcedureCallSpecification#getCallTime <em>Call Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Call Time</em>' attribute.
	 * @see org.lh.dmlj.schema.ProcedureCallTime
	 * @see #getCallTime()
	 * @generated
	 */
	void setCallTime(ProcedureCallTime value);

	/**
	 * Returns the value of the '<em><b>Function</b></em>' attribute.
	 * The default value is <code>""</code>.
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
	 * @model default=""
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
