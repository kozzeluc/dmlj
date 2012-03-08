/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.lh.dmlj.schema.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>System Owner</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.SystemOwnerImpl#getAreaSpecification <em>Area Specification</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SystemOwnerImpl#getSet <em>Set</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SystemOwnerImpl extends DiagramNodeImpl implements SystemOwner {
	/**
	 * The cached value of the '{@link #getAreaSpecification() <em>Area Specification</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAreaSpecification()
	 * @generated
	 * @ordered
	 */
	protected AreaSpecification areaSpecification;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SystemOwnerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.SYSTEM_OWNER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AreaSpecification getAreaSpecification() {
		if (areaSpecification != null && areaSpecification.eIsProxy()) {
			InternalEObject oldAreaSpecification = (InternalEObject)areaSpecification;
			areaSpecification = (AreaSpecification)eResolveProxy(oldAreaSpecification);
			if (areaSpecification != oldAreaSpecification) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.SYSTEM_OWNER__AREA_SPECIFICATION, oldAreaSpecification, areaSpecification));
			}
		}
		return areaSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AreaSpecification basicGetAreaSpecification() {
		return areaSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAreaSpecification(AreaSpecification newAreaSpecification, NotificationChain msgs) {
		AreaSpecification oldAreaSpecification = areaSpecification;
		areaSpecification = newAreaSpecification;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.SYSTEM_OWNER__AREA_SPECIFICATION, oldAreaSpecification, newAreaSpecification);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAreaSpecification(AreaSpecification newAreaSpecification) {
		if (newAreaSpecification != areaSpecification) {
			NotificationChain msgs = null;
			if (areaSpecification != null)
				msgs = ((InternalEObject)areaSpecification).eInverseRemove(this, SchemaPackage.AREA_SPECIFICATION__SYSTEM_OWNER, AreaSpecification.class, msgs);
			if (newAreaSpecification != null)
				msgs = ((InternalEObject)newAreaSpecification).eInverseAdd(this, SchemaPackage.AREA_SPECIFICATION__SYSTEM_OWNER, AreaSpecification.class, msgs);
			msgs = basicSetAreaSpecification(newAreaSpecification, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SYSTEM_OWNER__AREA_SPECIFICATION, newAreaSpecification, newAreaSpecification));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Set getSet() {
		if (eContainerFeatureID() != SchemaPackage.SYSTEM_OWNER__SET) return null;
		return (Set)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSet(Set newSet, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newSet, SchemaPackage.SYSTEM_OWNER__SET, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSet(Set newSet) {
		if (newSet != eInternalContainer() || (eContainerFeatureID() != SchemaPackage.SYSTEM_OWNER__SET && newSet != null)) {
			if (EcoreUtil.isAncestor(this, newSet))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newSet != null)
				msgs = ((InternalEObject)newSet).eInverseAdd(this, SchemaPackage.SET__SYSTEM_OWNER, Set.class, msgs);
			msgs = basicSetSet(newSet, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SYSTEM_OWNER__SET, newSet, newSet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.SYSTEM_OWNER__AREA_SPECIFICATION:
				if (areaSpecification != null)
					msgs = ((InternalEObject)areaSpecification).eInverseRemove(this, SchemaPackage.AREA_SPECIFICATION__SYSTEM_OWNER, AreaSpecification.class, msgs);
				return basicSetAreaSpecification((AreaSpecification)otherEnd, msgs);
			case SchemaPackage.SYSTEM_OWNER__SET:
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
			case SchemaPackage.SYSTEM_OWNER__AREA_SPECIFICATION:
				return basicSetAreaSpecification(null, msgs);
			case SchemaPackage.SYSTEM_OWNER__SET:
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
			case SchemaPackage.SYSTEM_OWNER__SET:
				return eInternalContainer().eInverseRemove(this, SchemaPackage.SET__SYSTEM_OWNER, Set.class, msgs);
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
			case SchemaPackage.SYSTEM_OWNER__AREA_SPECIFICATION:
				if (resolve) return getAreaSpecification();
				return basicGetAreaSpecification();
			case SchemaPackage.SYSTEM_OWNER__SET:
				return getSet();
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
			case SchemaPackage.SYSTEM_OWNER__AREA_SPECIFICATION:
				setAreaSpecification((AreaSpecification)newValue);
				return;
			case SchemaPackage.SYSTEM_OWNER__SET:
				setSet((Set)newValue);
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
			case SchemaPackage.SYSTEM_OWNER__AREA_SPECIFICATION:
				setAreaSpecification((AreaSpecification)null);
				return;
			case SchemaPackage.SYSTEM_OWNER__SET:
				setSet((Set)null);
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
			case SchemaPackage.SYSTEM_OWNER__AREA_SPECIFICATION:
				return areaSpecification != null;
			case SchemaPackage.SYSTEM_OWNER__SET:
				return getSet() != null;
		}
		return super.eIsSet(featureID);
	}

} //SystemOwnerImpl
