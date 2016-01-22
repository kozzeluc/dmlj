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

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.SchemaPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Diagram Location</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramLocationImpl#getEyecatcher <em>Eyecatcher</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramLocationImpl#getX <em>X</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramLocationImpl#getY <em>Y</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DiagramLocationImpl extends EObjectImpl implements DiagramLocation {
	/**
	 * The default value of the '{@link #getEyecatcher() <em>Eyecatcher</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEyecatcher()
	 * @generated
	 * @ordered
	 */
	protected static final String EYECATCHER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEyecatcher() <em>Eyecatcher</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEyecatcher()
	 * @generated
	 * @ordered
	 */
	protected String eyecatcher = EYECATCHER_EDEFAULT;

	/**
	 * The default value of the '{@link #getX() <em>X</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getX()
	 * @generated
	 * @ordered
	 */
	protected static final int X_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getX() <em>X</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getX()
	 * @generated
	 * @ordered
	 */
	protected int x = X_EDEFAULT;

	/**
	 * The default value of the '{@link #getY() <em>Y</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getY()
	 * @generated
	 * @ordered
	 */
	protected static final int Y_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getY() <em>Y</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getY()
	 * @generated
	 * @ordered
	 */
	protected int y = Y_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DiagramLocationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.DIAGRAM_LOCATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getX() {
		return x;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setX(int newX) {
		int oldX = x;
		x = newX;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.DIAGRAM_LOCATION__X, oldX, x));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getY() {
		return y;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setY(int newY) {
		int oldY = y;
		y = newY;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.DIAGRAM_LOCATION__Y, oldY, y));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getEyecatcher() {
		return eyecatcher;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEyecatcher(String newEyecatcher) {
		String oldEyecatcher = eyecatcher;
		eyecatcher = newEyecatcher;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.DIAGRAM_LOCATION__EYECATCHER, oldEyecatcher, eyecatcher));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchemaPackage.DIAGRAM_LOCATION__EYECATCHER:
				return getEyecatcher();
			case SchemaPackage.DIAGRAM_LOCATION__X:
				return getX();
			case SchemaPackage.DIAGRAM_LOCATION__Y:
				return getY();
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
			case SchemaPackage.DIAGRAM_LOCATION__EYECATCHER:
				setEyecatcher((String)newValue);
				return;
			case SchemaPackage.DIAGRAM_LOCATION__X:
				setX((Integer)newValue);
				return;
			case SchemaPackage.DIAGRAM_LOCATION__Y:
				setY((Integer)newValue);
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
			case SchemaPackage.DIAGRAM_LOCATION__EYECATCHER:
				setEyecatcher(EYECATCHER_EDEFAULT);
				return;
			case SchemaPackage.DIAGRAM_LOCATION__X:
				setX(X_EDEFAULT);
				return;
			case SchemaPackage.DIAGRAM_LOCATION__Y:
				setY(Y_EDEFAULT);
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
			case SchemaPackage.DIAGRAM_LOCATION__EYECATCHER:
				return EYECATCHER_EDEFAULT == null ? eyecatcher != null : !EYECATCHER_EDEFAULT.equals(eyecatcher);
			case SchemaPackage.DIAGRAM_LOCATION__X:
				return x != X_EDEFAULT;
			case SchemaPackage.DIAGRAM_LOCATION__Y:
				return y != Y_EDEFAULT;
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

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (eyecatcher: ");
		result.append(eyecatcher);
		result.append(", x: ");
		result.append(x);
		result.append(", y: ");
		result.append(y);
		result.append(')');
		return result.toString();
	}

} //DiagramLocationImpl
