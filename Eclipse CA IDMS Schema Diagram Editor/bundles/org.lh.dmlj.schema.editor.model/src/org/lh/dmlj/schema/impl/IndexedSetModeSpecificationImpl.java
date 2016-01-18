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
import org.lh.dmlj.schema.IndexedSetModeSpecification;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.Set;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Indexed Set Mode Specification</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.IndexedSetModeSpecificationImpl#getDisplacementPageCount <em>Displacement Page Count</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.IndexedSetModeSpecificationImpl#getKeyCount <em>Key Count</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.IndexedSetModeSpecificationImpl#getSet <em>Set</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.IndexedSetModeSpecificationImpl#getSymbolicIndexName <em>Symbolic Index Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IndexedSetModeSpecificationImpl extends EObjectImpl implements IndexedSetModeSpecification {
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
	 * The default value of the '{@link #getKeyCount() <em>Key Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKeyCount()
	 * @generated
	 * @ordered
	 */
	protected static final Short KEY_COUNT_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getKeyCount() <em>Key Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKeyCount()
	 * @generated
	 * @ordered
	 */
	protected Short keyCount = KEY_COUNT_EDEFAULT;
	/**
	 * The default value of the '{@link #getSymbolicIndexName() <em>Symbolic Index Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSymbolicIndexName()
	 * @generated
	 * @ordered
	 */
	protected static final String SYMBOLIC_INDEX_NAME_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getSymbolicIndexName() <em>Symbolic Index Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSymbolicIndexName()
	 * @generated
	 * @ordered
	 */
	protected String symbolicIndexName = SYMBOLIC_INDEX_NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IndexedSetModeSpecificationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.INDEXED_SET_MODE_SPECIFICATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSymbolicIndexName() {
		return symbolicIndexName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSymbolicIndexName(String newSymbolicIndexName) {
		String oldSymbolicIndexName = symbolicIndexName;
		symbolicIndexName = newSymbolicIndexName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__SYMBOLIC_INDEX_NAME, oldSymbolicIndexName, symbolicIndexName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__SET:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
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
			case SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__SET:
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
			case SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__SET:
				return eInternalContainer().eInverseRemove(this, SchemaPackage.SET__INDEXED_SET_MODE_SPECIFICATION, Set.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Short getKeyCount() {
		return keyCount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKeyCount(Short newKeyCount) {
		Short oldKeyCount = keyCount;
		keyCount = newKeyCount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__KEY_COUNT, oldKeyCount, keyCount));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Set getSet() {
		if (eContainerFeatureID() != SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__SET) return null;
		return (Set)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSet(Set newSet, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newSet, SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__SET, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSet(Set newSet) {
		if (newSet != eInternalContainer() || (eContainerFeatureID() != SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__SET && newSet != null)) {
			if (EcoreUtil.isAncestor(this, newSet))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newSet != null)
				msgs = ((InternalEObject)newSet).eInverseAdd(this, SchemaPackage.SET__INDEXED_SET_MODE_SPECIFICATION, Set.class, msgs);
			msgs = basicSetSet(newSet, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__SET, newSet, newSet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Short getDisplacementPageCount() {
		return displacementPageCount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDisplacementPageCount(Short newDisplacementPageCount) {
		Short oldDisplacementPageCount = displacementPageCount;
		displacementPageCount = newDisplacementPageCount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__DISPLACEMENT_PAGE_COUNT, oldDisplacementPageCount, displacementPageCount));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__DISPLACEMENT_PAGE_COUNT:
				return getDisplacementPageCount();
			case SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__KEY_COUNT:
				return getKeyCount();
			case SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__SET:
				return getSet();
			case SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__SYMBOLIC_INDEX_NAME:
				return getSymbolicIndexName();
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
			case SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__DISPLACEMENT_PAGE_COUNT:
				setDisplacementPageCount((Short)newValue);
				return;
			case SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__KEY_COUNT:
				setKeyCount((Short)newValue);
				return;
			case SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__SET:
				setSet((Set)newValue);
				return;
			case SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__SYMBOLIC_INDEX_NAME:
				setSymbolicIndexName((String)newValue);
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
			case SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__DISPLACEMENT_PAGE_COUNT:
				setDisplacementPageCount(DISPLACEMENT_PAGE_COUNT_EDEFAULT);
				return;
			case SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__KEY_COUNT:
				setKeyCount(KEY_COUNT_EDEFAULT);
				return;
			case SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__SET:
				setSet((Set)null);
				return;
			case SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__SYMBOLIC_INDEX_NAME:
				setSymbolicIndexName(SYMBOLIC_INDEX_NAME_EDEFAULT);
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
			case SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__DISPLACEMENT_PAGE_COUNT:
				return DISPLACEMENT_PAGE_COUNT_EDEFAULT == null ? displacementPageCount != null : !DISPLACEMENT_PAGE_COUNT_EDEFAULT.equals(displacementPageCount);
			case SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__KEY_COUNT:
				return KEY_COUNT_EDEFAULT == null ? keyCount != null : !KEY_COUNT_EDEFAULT.equals(keyCount);
			case SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__SET:
				return getSet() != null;
			case SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__SYMBOLIC_INDEX_NAME:
				return SYMBOLIC_INDEX_NAME_EDEFAULT == null ? symbolicIndexName != null : !SYMBOLIC_INDEX_NAME_EDEFAULT.equals(symbolicIndexName);
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
		result.append(" (displacementPageCount: ");
		result.append(displacementPageCount);
		result.append(", keyCount: ");
		result.append(keyCount);
		result.append(", symbolicIndexName: ");
		result.append(symbolicIndexName);
		result.append(')');
		return result.toString();
	}

} //IndexedSetModeSpecificationImpl
