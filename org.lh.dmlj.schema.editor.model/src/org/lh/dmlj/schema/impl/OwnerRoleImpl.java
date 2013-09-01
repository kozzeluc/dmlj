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

import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Owner Role</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.OwnerRoleImpl#getNextDbkeyPosition <em>Next Dbkey Position</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.OwnerRoleImpl#getPriorDbkeyPosition <em>Prior Dbkey Position</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.OwnerRoleImpl#getRecord <em>Record</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.OwnerRoleImpl#getSet <em>Set</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OwnerRoleImpl extends RoleImpl implements OwnerRole {
	/**
	 * The default value of the '{@link #getNextDbkeyPosition() <em>Next Dbkey Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNextDbkeyPosition()
	 * @generated
	 * @ordered
	 */
	protected static final short NEXT_DBKEY_POSITION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getNextDbkeyPosition() <em>Next Dbkey Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNextDbkeyPosition()
	 * @generated
	 * @ordered
	 */
	protected short nextDbkeyPosition = NEXT_DBKEY_POSITION_EDEFAULT;

	/**
	 * The default value of the '{@link #getPriorDbkeyPosition() <em>Prior Dbkey Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriorDbkeyPosition()
	 * @generated
	 * @ordered
	 */
	protected static final Short PRIOR_DBKEY_POSITION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPriorDbkeyPosition() <em>Prior Dbkey Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriorDbkeyPosition()
	 * @generated
	 * @ordered
	 */
	protected Short priorDbkeyPosition = PRIOR_DBKEY_POSITION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getRecord() <em>Record</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRecord()
	 * @generated
	 * @ordered
	 */
	protected SchemaRecord record;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OwnerRoleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.OWNER_ROLE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public short getNextDbkeyPosition() {
		return nextDbkeyPosition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNextDbkeyPosition(short newNextDbkeyPosition) {
		short oldNextDbkeyPosition = nextDbkeyPosition;
		nextDbkeyPosition = newNextDbkeyPosition;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.OWNER_ROLE__NEXT_DBKEY_POSITION, oldNextDbkeyPosition, nextDbkeyPosition));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Short getPriorDbkeyPosition() {
		return priorDbkeyPosition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPriorDbkeyPosition(Short newPriorDbkeyPosition) {
		Short oldPriorDbkeyPosition = priorDbkeyPosition;
		priorDbkeyPosition = newPriorDbkeyPosition;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.OWNER_ROLE__PRIOR_DBKEY_POSITION, oldPriorDbkeyPosition, priorDbkeyPosition));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SchemaRecord getRecord() {
		if (record != null && record.eIsProxy()) {
			InternalEObject oldRecord = (InternalEObject)record;
			record = (SchemaRecord)eResolveProxy(oldRecord);
			if (record != oldRecord) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.OWNER_ROLE__RECORD, oldRecord, record));
			}
		}
		return record;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SchemaRecord basicGetRecord() {
		return record;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRecord(SchemaRecord newRecord, NotificationChain msgs) {
		SchemaRecord oldRecord = record;
		record = newRecord;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.OWNER_ROLE__RECORD, oldRecord, newRecord);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRecord(SchemaRecord newRecord) {
		if (newRecord != record) {
			NotificationChain msgs = null;
			if (record != null)
				msgs = ((InternalEObject)record).eInverseRemove(this, SchemaPackage.SCHEMA_RECORD__OWNER_ROLES, SchemaRecord.class, msgs);
			if (newRecord != null)
				msgs = ((InternalEObject)newRecord).eInverseAdd(this, SchemaPackage.SCHEMA_RECORD__OWNER_ROLES, SchemaRecord.class, msgs);
			msgs = basicSetRecord(newRecord, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.OWNER_ROLE__RECORD, newRecord, newRecord));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Set getSet() {
		if (eContainerFeatureID() != SchemaPackage.OWNER_ROLE__SET) return null;
		return (Set)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSet(Set newSet, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newSet, SchemaPackage.OWNER_ROLE__SET, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSet(Set newSet) {
		if (newSet != eInternalContainer() || (eContainerFeatureID() != SchemaPackage.OWNER_ROLE__SET && newSet != null)) {
			if (EcoreUtil.isAncestor(this, newSet))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newSet != null)
				msgs = ((InternalEObject)newSet).eInverseAdd(this, SchemaPackage.SET__OWNER, Set.class, msgs);
			msgs = basicSetSet(newSet, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.OWNER_ROLE__SET, newSet, newSet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.OWNER_ROLE__RECORD:
				if (record != null)
					msgs = ((InternalEObject)record).eInverseRemove(this, SchemaPackage.SCHEMA_RECORD__OWNER_ROLES, SchemaRecord.class, msgs);
				return basicSetRecord((SchemaRecord)otherEnd, msgs);
			case SchemaPackage.OWNER_ROLE__SET:
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
			case SchemaPackage.OWNER_ROLE__RECORD:
				return basicSetRecord(null, msgs);
			case SchemaPackage.OWNER_ROLE__SET:
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
			case SchemaPackage.OWNER_ROLE__SET:
				return eInternalContainer().eInverseRemove(this, SchemaPackage.SET__OWNER, Set.class, msgs);
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
			case SchemaPackage.OWNER_ROLE__NEXT_DBKEY_POSITION:
				return getNextDbkeyPosition();
			case SchemaPackage.OWNER_ROLE__PRIOR_DBKEY_POSITION:
				return getPriorDbkeyPosition();
			case SchemaPackage.OWNER_ROLE__RECORD:
				if (resolve) return getRecord();
				return basicGetRecord();
			case SchemaPackage.OWNER_ROLE__SET:
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
			case SchemaPackage.OWNER_ROLE__NEXT_DBKEY_POSITION:
				setNextDbkeyPosition((Short)newValue);
				return;
			case SchemaPackage.OWNER_ROLE__PRIOR_DBKEY_POSITION:
				setPriorDbkeyPosition((Short)newValue);
				return;
			case SchemaPackage.OWNER_ROLE__RECORD:
				setRecord((SchemaRecord)newValue);
				return;
			case SchemaPackage.OWNER_ROLE__SET:
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
			case SchemaPackage.OWNER_ROLE__NEXT_DBKEY_POSITION:
				setNextDbkeyPosition(NEXT_DBKEY_POSITION_EDEFAULT);
				return;
			case SchemaPackage.OWNER_ROLE__PRIOR_DBKEY_POSITION:
				setPriorDbkeyPosition(PRIOR_DBKEY_POSITION_EDEFAULT);
				return;
			case SchemaPackage.OWNER_ROLE__RECORD:
				setRecord((SchemaRecord)null);
				return;
			case SchemaPackage.OWNER_ROLE__SET:
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
			case SchemaPackage.OWNER_ROLE__NEXT_DBKEY_POSITION:
				return nextDbkeyPosition != NEXT_DBKEY_POSITION_EDEFAULT;
			case SchemaPackage.OWNER_ROLE__PRIOR_DBKEY_POSITION:
				return PRIOR_DBKEY_POSITION_EDEFAULT == null ? priorDbkeyPosition != null : !PRIOR_DBKEY_POSITION_EDEFAULT.equals(priorDbkeyPosition);
			case SchemaPackage.OWNER_ROLE__RECORD:
				return record != null;
			case SchemaPackage.OWNER_ROLE__SET:
				return getSet() != null;
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
		result.append(" (nextDbkeyPosition: ");
		result.append(nextDbkeyPosition);
		result.append(", priorDbkeyPosition: ");
		result.append(priorDbkeyPosition);
		result.append(')');
		return result.toString();
	}

} //OwnerRoleImpl
