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
 * A representation of the model object '<em><b>Offset Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.OffsetExpression#getOffsetPageCount <em>Offset Page Count</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.OffsetExpression#getOffsetPercent <em>Offset Percent</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.OffsetExpression#getPageCount <em>Page Count</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.OffsetExpression#getPercent <em>Percent</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getOffsetExpression()
 * @model
 * @generated
 */
public interface OffsetExpression extends EObject {
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
