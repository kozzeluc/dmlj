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

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.lh.dmlj.schema.RecordProcedureCallSpecification;
import org.lh.dmlj.schema.RecordProcedureCallVerb;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Record Procedure Call Specification</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.RecordProcedureCallSpecificationImpl#getRecord <em>Record</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.RecordProcedureCallSpecificationImpl#getVerb <em>Verb</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RecordProcedureCallSpecificationImpl extends ProcedureCallSpecificationImpl implements RecordProcedureCallSpecification {
	/**
	 * The default value of the '{@link #getVerb() <em>Verb</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVerb()
	 * @generated
	 * @ordered
	 */
	protected static final RecordProcedureCallVerb VERB_EDEFAULT = RecordProcedureCallVerb.EVERY_DML_FUNCTION;
	/**
	 * The cached value of the '{@link #getVerb() <em>Verb</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVerb()
	 * @generated
	 * @ordered
	 */
	protected RecordProcedureCallVerb verb = VERB_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RecordProcedureCallSpecificationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.RECORD_PROCEDURE_CALL_SPECIFICATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SchemaRecord getRecord() {
		if (eContainerFeatureID() != SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__RECORD) return null;
		return (SchemaRecord)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRecord(SchemaRecord newRecord, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newRecord, SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__RECORD, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRecord(SchemaRecord newRecord) {
		if (newRecord != eInternalContainer() || (eContainerFeatureID() != SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__RECORD && newRecord != null)) {
			if (EcoreUtil.isAncestor(this, newRecord))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newRecord != null)
				msgs = ((InternalEObject)newRecord).eInverseAdd(this, SchemaPackage.SCHEMA_RECORD__PROCEDURES, SchemaRecord.class, msgs);
			msgs = basicSetRecord(newRecord, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__RECORD, newRecord, newRecord));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public RecordProcedureCallVerb getVerb() {
		return verb;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVerb(RecordProcedureCallVerb newVerb) {
		RecordProcedureCallVerb oldVerb = verb;
		verb = newVerb == null ? VERB_EDEFAULT : newVerb;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__VERB, oldVerb, verb));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__RECORD:
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
			case SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__RECORD:
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
			case SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__RECORD:
				return eInternalContainer().eInverseRemove(this, SchemaPackage.SCHEMA_RECORD__PROCEDURES, SchemaRecord.class, msgs);
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
			case SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__RECORD:
				return getRecord();
			case SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__VERB:
				return getVerb();
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
			case SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__RECORD:
				setRecord((SchemaRecord)newValue);
				return;
			case SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__VERB:
				setVerb((RecordProcedureCallVerb)newValue);
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
			case SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__RECORD:
				setRecord((SchemaRecord)null);
				return;
			case SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__VERB:
				setVerb(VERB_EDEFAULT);
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
			case SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__RECORD:
				return getRecord() != null;
			case SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__VERB:
				return verb != VERB_EDEFAULT;
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
		result.append(" (verb: ");
		result.append(verb);
		result.append(')');
		return result.toString();
	}

} //RecordProcedureCallSpecificationImpl
