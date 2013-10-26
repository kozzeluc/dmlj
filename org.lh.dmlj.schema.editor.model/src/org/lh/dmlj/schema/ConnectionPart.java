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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Connection Part</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.ConnectionPart#getConnector <em>Connector</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.ConnectionPart#getBendpointLocations <em>Bendpoint Locations</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.ConnectionPart#getMemberRole <em>Member Role</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.ConnectionPart#getSourceEndpointLocation <em>Source Endpoint Location</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.ConnectionPart#getTargetEndpointLocation <em>Target Endpoint Location</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getConnectionPart()
 * @model
 * @generated
 */
public interface ConnectionPart extends EObject {
	/**
	 * Returns the value of the '<em><b>Connector</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.Connector#getConnectionPart <em>Connection Part</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Connector</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Connector</em>' reference.
	 * @see #setConnector(Connector)
	 * @see org.lh.dmlj.schema.SchemaPackage#getConnectionPart_Connector()
	 * @see org.lh.dmlj.schema.Connector#getConnectionPart
	 * @model opposite="connectionPart"
	 * @generated
	 */
	Connector getConnector();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.ConnectionPart#getConnector <em>Connector</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Connector</em>' reference.
	 * @see #getConnector()
	 * @generated
	 */
	void setConnector(Connector value);

	/**
	 * Returns the value of the '<em><b>Bendpoint Locations</b></em>' reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.DiagramLocation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bendpoint Locations</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bendpoint Locations</em>' reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getConnectionPart_BendpointLocations()
	 * @model
	 * @generated
	 */
	EList<DiagramLocation> getBendpointLocations();

	/**
	 * Returns the value of the '<em><b>Member Role</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.MemberRole#getConnectionParts <em>Connection Parts</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Member Role</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Member Role</em>' reference.
	 * @see #setMemberRole(MemberRole)
	 * @see org.lh.dmlj.schema.SchemaPackage#getConnectionPart_MemberRole()
	 * @see org.lh.dmlj.schema.MemberRole#getConnectionParts
	 * @model opposite="connectionParts" required="true"
	 * @generated
	 */
	MemberRole getMemberRole();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.ConnectionPart#getMemberRole <em>Member Role</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Member Role</em>' reference.
	 * @see #getMemberRole()
	 * @generated
	 */
	void setMemberRole(MemberRole value);

	/**
	 * Returns the value of the '<em><b>Source Endpoint Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source Endpoint Location</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source Endpoint Location</em>' reference.
	 * @see #setSourceEndpointLocation(DiagramLocation)
	 * @see org.lh.dmlj.schema.SchemaPackage#getConnectionPart_SourceEndpointLocation()
	 * @model
	 * @generated
	 */
	DiagramLocation getSourceEndpointLocation();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.ConnectionPart#getSourceEndpointLocation <em>Source Endpoint Location</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source Endpoint Location</em>' reference.
	 * @see #getSourceEndpointLocation()
	 * @generated
	 */
	void setSourceEndpointLocation(DiagramLocation value);

	/**
	 * Returns the value of the '<em><b>Target Endpoint Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target Endpoint Location</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target Endpoint Location</em>' reference.
	 * @see #setTargetEndpointLocation(DiagramLocation)
	 * @see org.lh.dmlj.schema.SchemaPackage#getConnectionPart_TargetEndpointLocation()
	 * @model
	 * @generated
	 */
	DiagramLocation getTargetEndpointLocation();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.ConnectionPart#getTargetEndpointLocation <em>Target Endpoint Location</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target Endpoint Location</em>' reference.
	 * @see #getTargetEndpointLocation()
	 * @generated
	 */
	void setTargetEndpointLocation(DiagramLocation value);

} // ConnectionPart
