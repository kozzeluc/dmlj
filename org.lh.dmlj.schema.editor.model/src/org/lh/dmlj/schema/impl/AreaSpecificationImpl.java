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
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.OffsetExpression;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SystemOwner;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Area Specification</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.AreaSpecificationImpl#getSymbolicSubareaName <em>Symbolic Subarea Name</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.AreaSpecificationImpl#getArea <em>Area</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.AreaSpecificationImpl#getOffsetExpression <em>Offset Expression</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.AreaSpecificationImpl#getRecord <em>Record</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.AreaSpecificationImpl#getSystemOwner <em>System Owner</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AreaSpecificationImpl extends EObjectImpl implements AreaSpecification {
	/**
	 * The default value of the '{@link #getSymbolicSubareaName() <em>Symbolic Subarea Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSymbolicSubareaName()
	 * @generated
	 * @ordered
	 */
	protected static final String SYMBOLIC_SUBAREA_NAME_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getSymbolicSubareaName() <em>Symbolic Subarea Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSymbolicSubareaName()
	 * @generated
	 * @ordered
	 */
	protected String symbolicSubareaName = SYMBOLIC_SUBAREA_NAME_EDEFAULT;
	/**
	 * The cached value of the '{@link #getOffsetExpression() <em>Offset Expression</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOffsetExpression()
	 * @generated
	 * @ordered
	 */
	protected OffsetExpression offsetExpression;
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
	 * The cached value of the '{@link #getSystemOwner() <em>System Owner</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSystemOwner()
	 * @generated
	 * @ordered
	 */
	protected SystemOwner systemOwner;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AreaSpecificationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.AREA_SPECIFICATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSymbolicSubareaName() {
		return symbolicSubareaName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSymbolicSubareaName(String newSymbolicSubareaName) {
		String oldSymbolicSubareaName = symbolicSubareaName;
		symbolicSubareaName = newSymbolicSubareaName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.AREA_SPECIFICATION__SYMBOLIC_SUBAREA_NAME, oldSymbolicSubareaName, symbolicSubareaName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SchemaArea getArea() {
		if (eContainerFeatureID() != SchemaPackage.AREA_SPECIFICATION__AREA) return null;
		return (SchemaArea)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetArea(SchemaArea newArea, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newArea, SchemaPackage.AREA_SPECIFICATION__AREA, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setArea(SchemaArea newArea) {
		if (newArea != eInternalContainer() || (eContainerFeatureID() != SchemaPackage.AREA_SPECIFICATION__AREA && newArea != null)) {
			if (EcoreUtil.isAncestor(this, newArea))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newArea != null)
				msgs = ((InternalEObject)newArea).eInverseAdd(this, SchemaPackage.SCHEMA_AREA__AREA_SPECIFICATIONS, SchemaArea.class, msgs);
			msgs = basicSetArea(newArea, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.AREA_SPECIFICATION__AREA, newArea, newArea));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OffsetExpression getOffsetExpression() {
		return offsetExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOffsetExpression(OffsetExpression newOffsetExpression, NotificationChain msgs) {
		OffsetExpression oldOffsetExpression = offsetExpression;
		offsetExpression = newOffsetExpression;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.AREA_SPECIFICATION__OFFSET_EXPRESSION, oldOffsetExpression, newOffsetExpression);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOffsetExpression(OffsetExpression newOffsetExpression) {
		if (newOffsetExpression != offsetExpression) {
			NotificationChain msgs = null;
			if (offsetExpression != null)
				msgs = ((InternalEObject)offsetExpression).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.AREA_SPECIFICATION__OFFSET_EXPRESSION, null, msgs);
			if (newOffsetExpression != null)
				msgs = ((InternalEObject)newOffsetExpression).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.AREA_SPECIFICATION__OFFSET_EXPRESSION, null, msgs);
			msgs = basicSetOffsetExpression(newOffsetExpression, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.AREA_SPECIFICATION__OFFSET_EXPRESSION, newOffsetExpression, newOffsetExpression));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.AREA_SPECIFICATION__RECORD, oldRecord, record));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.AREA_SPECIFICATION__RECORD, oldRecord, newRecord);
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
				msgs = ((InternalEObject)record).eInverseRemove(this, SchemaPackage.SCHEMA_RECORD__AREA_SPECIFICATION, SchemaRecord.class, msgs);
			if (newRecord != null)
				msgs = ((InternalEObject)newRecord).eInverseAdd(this, SchemaPackage.SCHEMA_RECORD__AREA_SPECIFICATION, SchemaRecord.class, msgs);
			msgs = basicSetRecord(newRecord, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.AREA_SPECIFICATION__RECORD, newRecord, newRecord));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SystemOwner getSystemOwner() {
		if (systemOwner != null && systemOwner.eIsProxy()) {
			InternalEObject oldSystemOwner = (InternalEObject)systemOwner;
			systemOwner = (SystemOwner)eResolveProxy(oldSystemOwner);
			if (systemOwner != oldSystemOwner) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.AREA_SPECIFICATION__SYSTEM_OWNER, oldSystemOwner, systemOwner));
			}
		}
		return systemOwner;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SystemOwner basicGetSystemOwner() {
		return systemOwner;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSystemOwner(SystemOwner newSystemOwner, NotificationChain msgs) {
		SystemOwner oldSystemOwner = systemOwner;
		systemOwner = newSystemOwner;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.AREA_SPECIFICATION__SYSTEM_OWNER, oldSystemOwner, newSystemOwner);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSystemOwner(SystemOwner newSystemOwner) {
		if (newSystemOwner != systemOwner) {
			NotificationChain msgs = null;
			if (systemOwner != null)
				msgs = ((InternalEObject)systemOwner).eInverseRemove(this, SchemaPackage.SYSTEM_OWNER__AREA_SPECIFICATION, SystemOwner.class, msgs);
			if (newSystemOwner != null)
				msgs = ((InternalEObject)newSystemOwner).eInverseAdd(this, SchemaPackage.SYSTEM_OWNER__AREA_SPECIFICATION, SystemOwner.class, msgs);
			msgs = basicSetSystemOwner(newSystemOwner, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.AREA_SPECIFICATION__SYSTEM_OWNER, newSystemOwner, newSystemOwner));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.AREA_SPECIFICATION__AREA:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetArea((SchemaArea)otherEnd, msgs);
			case SchemaPackage.AREA_SPECIFICATION__RECORD:
				if (record != null)
					msgs = ((InternalEObject)record).eInverseRemove(this, SchemaPackage.SCHEMA_RECORD__AREA_SPECIFICATION, SchemaRecord.class, msgs);
				return basicSetRecord((SchemaRecord)otherEnd, msgs);
			case SchemaPackage.AREA_SPECIFICATION__SYSTEM_OWNER:
				if (systemOwner != null)
					msgs = ((InternalEObject)systemOwner).eInverseRemove(this, SchemaPackage.SYSTEM_OWNER__AREA_SPECIFICATION, SystemOwner.class, msgs);
				return basicSetSystemOwner((SystemOwner)otherEnd, msgs);
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
			case SchemaPackage.AREA_SPECIFICATION__AREA:
				return basicSetArea(null, msgs);
			case SchemaPackage.AREA_SPECIFICATION__OFFSET_EXPRESSION:
				return basicSetOffsetExpression(null, msgs);
			case SchemaPackage.AREA_SPECIFICATION__RECORD:
				return basicSetRecord(null, msgs);
			case SchemaPackage.AREA_SPECIFICATION__SYSTEM_OWNER:
				return basicSetSystemOwner(null, msgs);
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
			case SchemaPackage.AREA_SPECIFICATION__AREA:
				return eInternalContainer().eInverseRemove(this, SchemaPackage.SCHEMA_AREA__AREA_SPECIFICATIONS, SchemaArea.class, msgs);
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
			case SchemaPackage.AREA_SPECIFICATION__SYMBOLIC_SUBAREA_NAME:
				return getSymbolicSubareaName();
			case SchemaPackage.AREA_SPECIFICATION__AREA:
				return getArea();
			case SchemaPackage.AREA_SPECIFICATION__OFFSET_EXPRESSION:
				return getOffsetExpression();
			case SchemaPackage.AREA_SPECIFICATION__RECORD:
				if (resolve) return getRecord();
				return basicGetRecord();
			case SchemaPackage.AREA_SPECIFICATION__SYSTEM_OWNER:
				if (resolve) return getSystemOwner();
				return basicGetSystemOwner();
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
			case SchemaPackage.AREA_SPECIFICATION__SYMBOLIC_SUBAREA_NAME:
				setSymbolicSubareaName((String)newValue);
				return;
			case SchemaPackage.AREA_SPECIFICATION__AREA:
				setArea((SchemaArea)newValue);
				return;
			case SchemaPackage.AREA_SPECIFICATION__OFFSET_EXPRESSION:
				setOffsetExpression((OffsetExpression)newValue);
				return;
			case SchemaPackage.AREA_SPECIFICATION__RECORD:
				setRecord((SchemaRecord)newValue);
				return;
			case SchemaPackage.AREA_SPECIFICATION__SYSTEM_OWNER:
				setSystemOwner((SystemOwner)newValue);
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
			case SchemaPackage.AREA_SPECIFICATION__SYMBOLIC_SUBAREA_NAME:
				setSymbolicSubareaName(SYMBOLIC_SUBAREA_NAME_EDEFAULT);
				return;
			case SchemaPackage.AREA_SPECIFICATION__AREA:
				setArea((SchemaArea)null);
				return;
			case SchemaPackage.AREA_SPECIFICATION__OFFSET_EXPRESSION:
				setOffsetExpression((OffsetExpression)null);
				return;
			case SchemaPackage.AREA_SPECIFICATION__RECORD:
				setRecord((SchemaRecord)null);
				return;
			case SchemaPackage.AREA_SPECIFICATION__SYSTEM_OWNER:
				setSystemOwner((SystemOwner)null);
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
			case SchemaPackage.AREA_SPECIFICATION__SYMBOLIC_SUBAREA_NAME:
				return SYMBOLIC_SUBAREA_NAME_EDEFAULT == null ? symbolicSubareaName != null : !SYMBOLIC_SUBAREA_NAME_EDEFAULT.equals(symbolicSubareaName);
			case SchemaPackage.AREA_SPECIFICATION__AREA:
				return getArea() != null;
			case SchemaPackage.AREA_SPECIFICATION__OFFSET_EXPRESSION:
				return offsetExpression != null;
			case SchemaPackage.AREA_SPECIFICATION__RECORD:
				return record != null;
			case SchemaPackage.AREA_SPECIFICATION__SYSTEM_OWNER:
				return systemOwner != null;
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
		result.append(" (symbolicSubareaName: ");
		result.append(symbolicSubareaName);
		result.append(')');
		return result.toString();
	}

} //AreaSpecificationImpl
