/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.lh.dmlj.schema.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.OccursSpecification;
import org.lh.dmlj.schema.SchemaPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Occurs Specification</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.OccursSpecificationImpl#getCount <em>Count</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.OccursSpecificationImpl#getDependingOn <em>Depending On</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OccursSpecificationImpl extends EObjectImpl implements OccursSpecification {
	/**
	 * The default value of the '{@link #getCount() <em>Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCount()
	 * @generated
	 * @ordered
	 */
	protected static final short COUNT_EDEFAULT = 0;
	/**
	 * The cached value of the '{@link #getCount() <em>Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCount()
	 * @generated
	 * @ordered
	 */
	protected short count = COUNT_EDEFAULT;
	/**
	 * The cached value of the '{@link #getDependingOn() <em>Depending On</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDependingOn()
	 * @generated
	 * @ordered
	 */
	protected Element dependingOn;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OccursSpecificationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.OCCURS_SPECIFICATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public short getCount() {
		return count;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCount(short newCount) {
		short oldCount = count;
		count = newCount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.OCCURS_SPECIFICATION__COUNT, oldCount, count));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Element getDependingOn() {
		if (dependingOn != null && dependingOn.eIsProxy()) {
			InternalEObject oldDependingOn = (InternalEObject)dependingOn;
			dependingOn = (Element)eResolveProxy(oldDependingOn);
			if (dependingOn != oldDependingOn) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.OCCURS_SPECIFICATION__DEPENDING_ON, oldDependingOn, dependingOn));
			}
		}
		return dependingOn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Element basicGetDependingOn() {
		return dependingOn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDependingOn(Element newDependingOn) {
		Element oldDependingOn = dependingOn;
		dependingOn = newDependingOn;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.OCCURS_SPECIFICATION__DEPENDING_ON, oldDependingOn, dependingOn));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchemaPackage.OCCURS_SPECIFICATION__COUNT:
				return getCount();
			case SchemaPackage.OCCURS_SPECIFICATION__DEPENDING_ON:
				if (resolve) return getDependingOn();
				return basicGetDependingOn();
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
			case SchemaPackage.OCCURS_SPECIFICATION__COUNT:
				setCount((Short)newValue);
				return;
			case SchemaPackage.OCCURS_SPECIFICATION__DEPENDING_ON:
				setDependingOn((Element)newValue);
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
			case SchemaPackage.OCCURS_SPECIFICATION__COUNT:
				setCount(COUNT_EDEFAULT);
				return;
			case SchemaPackage.OCCURS_SPECIFICATION__DEPENDING_ON:
				setDependingOn((Element)null);
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
			case SchemaPackage.OCCURS_SPECIFICATION__COUNT:
				return count != COUNT_EDEFAULT;
			case SchemaPackage.OCCURS_SPECIFICATION__DEPENDING_ON:
				return dependingOn != null;
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
		result.append(" (count: ");
		result.append(count);
		result.append(')');
		return result.toString();
	}

} //OccursSpecificationImpl