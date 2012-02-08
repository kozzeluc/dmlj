/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.lh.dmlj.schema.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.lh.dmlj.schema.IndexedSetModeSpecification;
import org.lh.dmlj.schema.SchemaPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Indexed Set Mode Specification</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.IndexedSetModeSpecificationImpl#getDisplacementPageCount <em>Displacement Page Count</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.IndexedSetModeSpecificationImpl#getKeyCount <em>Key Count</em>}</li>
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
