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
 * A representation of the model object '<em><b>Connection Label</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.ConnectionLabel#getAlignment <em>Alignment</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.ConnectionLabel#getMemberRole <em>Member Role</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getConnectionLabel()
 * @model
 * @generated
 */
public interface ConnectionLabel extends DiagramNode {
	/**
	 * Returns the value of the '<em><b>Alignment</b></em>' attribute.
	 * The literals are from the enumeration {@link org.lh.dmlj.schema.LabelAlignment}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Alignment</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Alignment</em>' attribute.
	 * @see org.lh.dmlj.schema.LabelAlignment
	 * @see #setAlignment(LabelAlignment)
	 * @see org.lh.dmlj.schema.SchemaPackage#getConnectionLabel_Alignment()
	 * @model
	 * @generated
	 */
	LabelAlignment getAlignment();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.ConnectionLabel#getAlignment <em>Alignment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Alignment</em>' attribute.
	 * @see org.lh.dmlj.schema.LabelAlignment
	 * @see #getAlignment()
	 * @generated
	 */
	void setAlignment(LabelAlignment value);

	/**
	 * Returns the value of the '<em><b>Member Role</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.MemberRole#getConnectionLabel <em>Connection Label</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Member Role</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Member Role</em>' reference.
	 * @see #setMemberRole(MemberRole)
	 * @see org.lh.dmlj.schema.SchemaPackage#getConnectionLabel_MemberRole()
	 * @see org.lh.dmlj.schema.MemberRole#getConnectionLabel
	 * @model opposite="connectionLabel" required="true"
	 * @generated
	 */
	MemberRole getMemberRole();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.ConnectionLabel#getMemberRole <em>Member Role</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Member Role</em>' reference.
	 * @see #getMemberRole()
	 * @generated
	 */
	void setMemberRole(MemberRole value);

} // ConnectionLabel
