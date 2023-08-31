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
 * A representation of the model object '<em><b>Offset Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.OffsetExpression#getAreaSpecification <em>Area Specification</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.OffsetExpression#getOffsetPageCount <em>Offset Page Count</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.OffsetExpression#getOffsetPercent <em>Offset Percent</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.OffsetExpression#getPageCount <em>Page Count</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.OffsetExpression#getPercent <em>Percent</em>}</li>
 * </ul>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getOffsetExpression()
 * @model
 * @generated
 */
public interface OffsetExpression extends EObject {
	/**
	 * Returns the value of the '<em><b>Area Specification</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.AreaSpecification#getOffsetExpression <em>Offset Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Area Specification</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Area Specification</em>' container reference.
	 * @see #setAreaSpecification(AreaSpecification)
	 * @see org.lh.dmlj.schema.SchemaPackage#getOffsetExpression_AreaSpecification()
	 * @see org.lh.dmlj.schema.AreaSpecification#getOffsetExpression
	 * @model opposite="offsetExpression" required="true" transient="false"
	 * @generated
	 */
	AreaSpecification getAreaSpecification();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.OffsetExpression#getAreaSpecification <em>Area Specification</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Area Specification</em>' container reference.
	 * @see #getAreaSpecification()
	 * @generated
	 */
	void setAreaSpecification(AreaSpecification value);

	/**
	 * Returns the value of the '<em><b>Offset Page Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Offset Page Count</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Offset Page Count</em>' attribute.
	 * @see #setOffsetPageCount(Integer)
	 * @see org.lh.dmlj.schema.SchemaPackage#getOffsetExpression_OffsetPageCount()
	 * @model
	 * @generated
	 */
	Integer getOffsetPageCount();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.OffsetExpression#getOffsetPageCount <em>Offset Page Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Offset Page Count</em>' attribute.
	 * @see #getOffsetPageCount()
	 * @generated
	 */
	void setOffsetPageCount(Integer value);

	/**
	 * Returns the value of the '<em><b>Offset Percent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Offset Percent</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Offset Percent</em>' attribute.
	 * @see #setOffsetPercent(Short)
	 * @see org.lh.dmlj.schema.SchemaPackage#getOffsetExpression_OffsetPercent()
	 * @model
	 * @generated
	 */
	Short getOffsetPercent();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.OffsetExpression#getOffsetPercent <em>Offset Percent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Offset Percent</em>' attribute.
	 * @see #getOffsetPercent()
	 * @generated
	 */
	void setOffsetPercent(Short value);

	/**
	 * Returns the value of the '<em><b>Page Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Page Count</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Page Count</em>' attribute.
	 * @see #setPageCount(Integer)
	 * @see org.lh.dmlj.schema.SchemaPackage#getOffsetExpression_PageCount()
	 * @model
	 * @generated
	 */
	Integer getPageCount();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.OffsetExpression#getPageCount <em>Page Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Page Count</em>' attribute.
	 * @see #getPageCount()
	 * @generated
	 */
	void setPageCount(Integer value);

	/**
	 * Returns the value of the '<em><b>Percent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Percent</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Percent</em>' attribute.
	 * @see #setPercent(Short)
	 * @see org.lh.dmlj.schema.SchemaPackage#getOffsetExpression_Percent()
	 * @model
	 * @generated
	 */
	Short getPercent();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.OffsetExpression#getPercent <em>Percent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Percent</em>' attribute.
	 * @see #getPercent()
	 * @generated
	 */
	void setPercent(Short value);

} // OffsetExpression
