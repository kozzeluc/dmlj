/**
 * Copyright (C) 2015  Luc Hermans
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
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EcoreUtil;

import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.VsamLengthType;
import org.lh.dmlj.schema.VsamType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vsam Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.VsamTypeImpl#getLengthType <em>Length Type</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.VsamTypeImpl#getRecord <em>Record</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.VsamTypeImpl#isSpanned <em>Spanned</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VsamTypeImpl extends EObjectImpl implements VsamType {
	/**
	 * The default value of the '{@link #getLengthType() <em>Length Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLengthType()
	 * @generated
	 * @ordered
	 */
	protected static final VsamLengthType LENGTH_TYPE_EDEFAULT = VsamLengthType.FIXED;

	/**
	 * The cached value of the '{@link #getLengthType() <em>Length Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLengthType()
	 * @generated
	 * @ordered
	 */
	protected VsamLengthType lengthType = LENGTH_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #isSpanned() <em>Spanned</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSpanned()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SPANNED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isSpanned() <em>Spanned</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSpanned()
	 * @generated
	 * @ordered
	 */
	protected boolean spanned = SPANNED_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VsamTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.VSAM_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VsamLengthType getLengthType() {
		return lengthType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLengthType(VsamLengthType newLengthType) {
		VsamLengthType oldLengthType = lengthType;
		lengthType = newLengthType == null ? LENGTH_TYPE_EDEFAULT : newLengthType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.VSAM_TYPE__LENGTH_TYPE, oldLengthType, lengthType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SchemaRecord getRecord() {
		if (eContainerFeatureID() != SchemaPackage.VSAM_TYPE__RECORD) return null;
		return (SchemaRecord)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRecord(SchemaRecord newRecord, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newRecord, SchemaPackage.VSAM_TYPE__RECORD, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRecord(SchemaRecord newRecord) {
		if (newRecord != eInternalContainer() || (eContainerFeatureID() != SchemaPackage.VSAM_TYPE__RECORD && newRecord != null)) {
			if (EcoreUtil.isAncestor(this, newRecord))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newRecord != null)
				msgs = ((InternalEObject)newRecord).eInverseAdd(this, SchemaPackage.SCHEMA_RECORD__VSAM_TYPE, SchemaRecord.class, msgs);
			msgs = basicSetRecord(newRecord, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.VSAM_TYPE__RECORD, newRecord, newRecord));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSpanned() {
		return spanned;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSpanned(boolean newSpanned) {
		boolean oldSpanned = spanned;
		spanned = newSpanned;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.VSAM_TYPE__SPANNED, oldSpanned, spanned));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.VSAM_TYPE__RECORD:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetRecord((SchemaRecord)otherEnd, msgs);
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
			case SchemaPackage.VSAM_TYPE__RECORD:
				return basicSetRecord(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case SchemaPackage.VSAM_TYPE__RECORD:
				return eInternalContainer().eInverseRemove(this, SchemaPackage.SCHEMA_RECORD__VSAM_TYPE, SchemaRecord.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchemaPackage.VSAM_TYPE__LENGTH_TYPE:
				return getLengthType();
			case SchemaPackage.VSAM_TYPE__RECORD:
				return getRecord();
			case SchemaPackage.VSAM_TYPE__SPANNED:
				return isSpanned();
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
			case SchemaPackage.VSAM_TYPE__LENGTH_TYPE:
				setLengthType((VsamLengthType)newValue);
				return;
			case SchemaPackage.VSAM_TYPE__RECORD:
				setRecord((SchemaRecord)newValue);
				return;
			case SchemaPackage.VSAM_TYPE__SPANNED:
				setSpanned((Boolean)newValue);
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
			case SchemaPackage.VSAM_TYPE__LENGTH_TYPE:
				setLengthType(LENGTH_TYPE_EDEFAULT);
				return;
			case SchemaPackage.VSAM_TYPE__RECORD:
				setRecord((SchemaRecord)null);
				return;
			case SchemaPackage.VSAM_TYPE__SPANNED:
				setSpanned(SPANNED_EDEFAULT);
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
			case SchemaPackage.VSAM_TYPE__LENGTH_TYPE:
				return lengthType != LENGTH_TYPE_EDEFAULT;
			case SchemaPackage.VSAM_TYPE__RECORD:
				return getRecord() != null;
			case SchemaPackage.VSAM_TYPE__SPANNED:
				return spanned != SPANNED_EDEFAULT;
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
		result.append(" (lengthType: ");
		result.append(lengthType);
		result.append(", spanned: ");
		result.append(spanned);
		result.append(')');
		return result.toString();
	}

} //VsamTypeImpl
