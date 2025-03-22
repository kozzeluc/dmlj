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
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.ViaSpecification;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Via Specification</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.ViaSpecificationImpl#getDisplacementPageCount <em>Displacement Page Count</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.ViaSpecificationImpl#getRecord <em>Record</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.ViaSpecificationImpl#getSet <em>Set</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.ViaSpecificationImpl#getSymbolicDisplacementName <em>Symbolic Displacement Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ViaSpecificationImpl extends EObjectImpl implements ViaSpecification {
	/**
	 * The default value of the '{@link #getDisplacementPageCount() <em>Displacement Page Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDisplacementPageCount()
	 * @generated
	 * @ordered
	 */
	protected static final Short DISPLACEMENT_PAGE_COUNT_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getDisplacementPageCount() <em>Displacement Page Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDisplacementPageCount()
	 * @generated
	 * @ordered
	 */
	protected Short displacementPageCount = DISPLACEMENT_PAGE_COUNT_EDEFAULT;
	/**
	 * The cached value of the '{@link #getSet() <em>Set</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSet()
	 * @generated
	 * @ordered
	 */
	protected Set set;
	/**
	 * The default value of the '{@link #getSymbolicDisplacementName() <em>Symbolic Displacement Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSymbolicDisplacementName()
	 * @generated
	 * @ordered
	 */
	protected static final String SYMBOLIC_DISPLACEMENT_NAME_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getSymbolicDisplacementName() <em>Symbolic Displacement Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSymbolicDisplacementName()
	 * @generated
	 * @ordered
	 */
	protected String symbolicDisplacementName = SYMBOLIC_DISPLACEMENT_NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ViaSpecificationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.VIA_SPECIFICATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getSymbolicDisplacementName() {
		return symbolicDisplacementName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSymbolicDisplacementName(String newSymbolicDisplacementName) {
		String oldSymbolicDisplacementName = symbolicDisplacementName;
		symbolicDisplacementName = newSymbolicDisplacementName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.VIA_SPECIFICATION__SYMBOLIC_DISPLACEMENT_NAME, oldSymbolicDisplacementName, symbolicDisplacementName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Set getSet() {
		if (set != null && set.eIsProxy()) {
			InternalEObject oldSet = (InternalEObject)set;
			set = (Set)eResolveProxy(oldSet);
			if (set != oldSet) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.VIA_SPECIFICATION__SET, oldSet, set));
			}
		}
		return set;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Set basicGetSet() {
		return set;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSet(Set newSet, NotificationChain msgs) {
		Set oldSet = set;
		set = newSet;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.VIA_SPECIFICATION__SET, oldSet, newSet);
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
	public void setSet(Set newSet) {
		if (newSet != set) {
			NotificationChain msgs = null;
			if (set != null)
				msgs = ((InternalEObject)set).eInverseRemove(this, SchemaPackage.SET__VIA_MEMBERS, Set.class, msgs);
			if (newSet != null)
				msgs = ((InternalEObject)newSet).eInverseAdd(this, SchemaPackage.SET__VIA_MEMBERS, Set.class, msgs);
			msgs = basicSetSet(newSet, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.VIA_SPECIFICATION__SET, newSet, newSet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SchemaRecord getRecord() {
		if (eContainerFeatureID() != SchemaPackage.VIA_SPECIFICATION__RECORD) return null;
		return (SchemaRecord)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRecord(SchemaRecord newRecord, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newRecord, SchemaPackage.VIA_SPECIFICATION__RECORD, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRecord(SchemaRecord newRecord) {
		if (newRecord != eInternalContainer() || (eContainerFeatureID() != SchemaPackage.VIA_SPECIFICATION__RECORD && newRecord != null)) {
			if (EcoreUtil.isAncestor(this, newRecord))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newRecord != null)
				msgs = ((InternalEObject)newRecord).eInverseAdd(this, SchemaPackage.SCHEMA_RECORD__VIA_SPECIFICATION, SchemaRecord.class, msgs);
			msgs = basicSetRecord(newRecord, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.VIA_SPECIFICATION__RECORD, newRecord, newRecord));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.VIA_SPECIFICATION__RECORD:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetRecord((SchemaRecord)otherEnd, msgs);
			case SchemaPackage.VIA_SPECIFICATION__SET:
				if (set != null)
					msgs = ((InternalEObject)set).eInverseRemove(this, SchemaPackage.SET__VIA_MEMBERS, Set.class, msgs);
				return basicSetSet((Set)otherEnd, msgs);
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
			case SchemaPackage.VIA_SPECIFICATION__RECORD:
				return basicSetRecord(null, msgs);
			case SchemaPackage.VIA_SPECIFICATION__SET:
				return basicSetSet(null, msgs);
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
			case SchemaPackage.VIA_SPECIFICATION__RECORD:
				return eInternalContainer().eInverseRemove(this, SchemaPackage.SCHEMA_RECORD__VIA_SPECIFICATION, SchemaRecord.class, msgs);
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
			case SchemaPackage.VIA_SPECIFICATION__DISPLACEMENT_PAGE_COUNT:
				return getDisplacementPageCount();
			case SchemaPackage.VIA_SPECIFICATION__RECORD:
				return getRecord();
			case SchemaPackage.VIA_SPECIFICATION__SET:
				if (resolve) return getSet();
				return basicGetSet();
			case SchemaPackage.VIA_SPECIFICATION__SYMBOLIC_DISPLACEMENT_NAME:
				return getSymbolicDisplacementName();
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
			case SchemaPackage.VIA_SPECIFICATION__DISPLACEMENT_PAGE_COUNT:
				setDisplacementPageCount((Short)newValue);
				return;
			case SchemaPackage.VIA_SPECIFICATION__RECORD:
				setRecord((SchemaRecord)newValue);
				return;
			case SchemaPackage.VIA_SPECIFICATION__SET:
				setSet((Set)newValue);
				return;
			case SchemaPackage.VIA_SPECIFICATION__SYMBOLIC_DISPLACEMENT_NAME:
				setSymbolicDisplacementName((String)newValue);
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
			case SchemaPackage.VIA_SPECIFICATION__DISPLACEMENT_PAGE_COUNT:
				setDisplacementPageCount(DISPLACEMENT_PAGE_COUNT_EDEFAULT);
				return;
			case SchemaPackage.VIA_SPECIFICATION__RECORD:
				setRecord((SchemaRecord)null);
				return;
			case SchemaPackage.VIA_SPECIFICATION__SET:
				setSet((Set)null);
				return;
			case SchemaPackage.VIA_SPECIFICATION__SYMBOLIC_DISPLACEMENT_NAME:
				setSymbolicDisplacementName(SYMBOLIC_DISPLACEMENT_NAME_EDEFAULT);
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
			case SchemaPackage.VIA_SPECIFICATION__DISPLACEMENT_PAGE_COUNT:
				return DISPLACEMENT_PAGE_COUNT_EDEFAULT == null ? displacementPageCount != null : !DISPLACEMENT_PAGE_COUNT_EDEFAULT.equals(displacementPageCount);
			case SchemaPackage.VIA_SPECIFICATION__RECORD:
				return getRecord() != null;
			case SchemaPackage.VIA_SPECIFICATION__SET:
				return set != null;
			case SchemaPackage.VIA_SPECIFICATION__SYMBOLIC_DISPLACEMENT_NAME:
				return SYMBOLIC_DISPLACEMENT_NAME_EDEFAULT == null ? symbolicDisplacementName != null : !SYMBOLIC_DISPLACEMENT_NAME_EDEFAULT.equals(symbolicDisplacementName);
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
		result.append(" (displacementPageCount: ");
		result.append(displacementPageCount);
		result.append(", symbolicDisplacementName: ");
		result.append(symbolicDisplacementName);
		result.append(')');
		return result.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Short getDisplacementPageCount() {
		return displacementPageCount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDisplacementPageCount(Short newDisplacementPageCount) {
		Short oldDisplacementPageCount = displacementPageCount;
		displacementPageCount = newDisplacementPageCount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.VIA_SPECIFICATION__DISPLACEMENT_PAGE_COUNT, oldDisplacementPageCount, displacementPageCount));
	}

} //ViaSpecificationImpl
