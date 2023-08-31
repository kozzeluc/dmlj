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
package org.lh.dmlj.schema.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.SchemaPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Connector</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.ConnectorImpl#getConnectionPart <em>Connection Part</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.ConnectorImpl#getLabel <em>Label</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConnectorImpl extends DiagramNodeImpl implements Connector {
	/**
	 * The cached value of the '{@link #getConnectionPart() <em>Connection Part</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConnectionPart()
	 * @generated
	 * @ordered
	 */
	protected ConnectionPart connectionPart;

	/**
	 * The default value of the '{@link #getLabel() <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected static final String LABEL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLabel() <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected String label = LABEL_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ConnectorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.CONNECTOR;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ConnectionPart getConnectionPart() {
		if (connectionPart != null && connectionPart.eIsProxy()) {
			InternalEObject oldConnectionPart = (InternalEObject)connectionPart;
			connectionPart = (ConnectionPart)eResolveProxy(oldConnectionPart);
			if (connectionPart != oldConnectionPart) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.CONNECTOR__CONNECTION_PART, oldConnectionPart, connectionPart));
			}
		}
		return connectionPart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConnectionPart basicGetConnectionPart() {
		return connectionPart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetConnectionPart(ConnectionPart newConnectionPart, NotificationChain msgs) {
		ConnectionPart oldConnectionPart = connectionPart;
		connectionPart = newConnectionPart;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.CONNECTOR__CONNECTION_PART, oldConnectionPart, newConnectionPart);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setConnectionPart(ConnectionPart newConnectionPart) {
		if (newConnectionPart != connectionPart) {
			NotificationChain msgs = null;
			if (connectionPart != null)
				msgs = ((InternalEObject)connectionPart).eInverseRemove(this, SchemaPackage.CONNECTION_PART__CONNECTOR, ConnectionPart.class, msgs);
			if (newConnectionPart != null)
				msgs = ((InternalEObject)newConnectionPart).eInverseAdd(this, SchemaPackage.CONNECTION_PART__CONNECTOR, ConnectionPart.class, msgs);
			msgs = basicSetConnectionPart(newConnectionPart, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.CONNECTOR__CONNECTION_PART, newConnectionPart, newConnectionPart));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getLabel() {
		return label;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLabel(String newLabel) {
		String oldLabel = label;
		label = newLabel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.CONNECTOR__LABEL, oldLabel, label));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.CONNECTOR__CONNECTION_PART:
				if (connectionPart != null)
					msgs = ((InternalEObject)connectionPart).eInverseRemove(this, SchemaPackage.CONNECTION_PART__CONNECTOR, ConnectionPart.class, msgs);
				return basicSetConnectionPart((ConnectionPart)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.CONNECTOR__CONNECTION_PART:
				return basicSetConnectionPart(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchemaPackage.CONNECTOR__CONNECTION_PART:
				if (resolve) return getConnectionPart();
				return basicGetConnectionPart();
			case SchemaPackage.CONNECTOR__LABEL:
				return getLabel();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case SchemaPackage.CONNECTOR__CONNECTION_PART:
				setConnectionPart((ConnectionPart)newValue);
				return;
			case SchemaPackage.CONNECTOR__LABEL:
				setLabel((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case SchemaPackage.CONNECTOR__CONNECTION_PART:
				setConnectionPart((ConnectionPart)null);
				return;
			case SchemaPackage.CONNECTOR__LABEL:
				setLabel(LABEL_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case SchemaPackage.CONNECTOR__CONNECTION_PART:
				return connectionPart != null;
			case SchemaPackage.CONNECTOR__LABEL:
				return LABEL_EDEFAULT == null ? label != null : !LABEL_EDEFAULT.equals(label);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (label: ");
		result.append(label);
		result.append(')');
		return result.toString();
	}

} //ConnectorImpl
