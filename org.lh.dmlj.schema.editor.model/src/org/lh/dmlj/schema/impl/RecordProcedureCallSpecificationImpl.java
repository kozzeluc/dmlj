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

import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.ProcedureCallTime;
import org.lh.dmlj.schema.RecordProcedureCallSpecification;
import org.lh.dmlj.schema.RecordProcedureCallVerb;
import org.lh.dmlj.schema.SchemaPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Record Procedure Call Specification</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.RecordProcedureCallSpecificationImpl#getCallTime <em>Call Time</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.RecordProcedureCallSpecificationImpl#getVerb <em>Verb</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.RecordProcedureCallSpecificationImpl#getProcedure <em>Procedure</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RecordProcedureCallSpecificationImpl extends EObjectImpl implements RecordProcedureCallSpecification {
	/**
	 * The default value of the '{@link #getCallTime() <em>Call Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCallTime()
	 * @generated
	 * @ordered
	 */
	protected static final ProcedureCallTime CALL_TIME_EDEFAULT = ProcedureCallTime.BEFORE;
	/**
	 * The cached value of the '{@link #getCallTime() <em>Call Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCallTime()
	 * @generated
	 * @ordered
	 */
	protected ProcedureCallTime callTime = CALL_TIME_EDEFAULT;
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
	 * The cached value of the '{@link #getProcedure() <em>Procedure</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProcedure()
	 * @generated
	 * @ordered
	 */
	protected Procedure procedure;

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
	public Procedure getProcedure() {
		if (procedure != null && procedure.eIsProxy()) {
			InternalEObject oldProcedure = (InternalEObject)procedure;
			procedure = (Procedure)eResolveProxy(oldProcedure);
			if (procedure != oldProcedure) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__PROCEDURE, oldProcedure, procedure));
			}
		}
		return procedure;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Procedure basicGetProcedure() {
		return procedure;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProcedure(Procedure newProcedure) {
		Procedure oldProcedure = procedure;
		procedure = newProcedure;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__PROCEDURE, oldProcedure, procedure));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProcedureCallTime getCallTime() {
		return callTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCallTime(ProcedureCallTime newCallTime) {
		ProcedureCallTime oldCallTime = callTime;
		callTime = newCallTime == null ? CALL_TIME_EDEFAULT : newCallTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__CALL_TIME, oldCallTime, callTime));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RecordProcedureCallVerb getVerb() {
		return verb;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__CALL_TIME:
				return getCallTime();
			case SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__VERB:
				return getVerb();
			case SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__PROCEDURE:
				if (resolve) return getProcedure();
				return basicGetProcedure();
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
			case SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__CALL_TIME:
				setCallTime((ProcedureCallTime)newValue);
				return;
			case SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__VERB:
				setVerb((RecordProcedureCallVerb)newValue);
				return;
			case SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__PROCEDURE:
				setProcedure((Procedure)newValue);
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
			case SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__CALL_TIME:
				setCallTime(CALL_TIME_EDEFAULT);
				return;
			case SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__VERB:
				setVerb(VERB_EDEFAULT);
				return;
			case SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__PROCEDURE:
				setProcedure((Procedure)null);
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
			case SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__CALL_TIME:
				return callTime != CALL_TIME_EDEFAULT;
			case SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__VERB:
				return verb != VERB_EDEFAULT;
			case SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__PROCEDURE:
				return procedure != null;
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
		result.append(" (callTime: ");
		result.append(callTime);
		result.append(", verb: ");
		result.append(verb);
		result.append(')');
		return result.toString();
	}

} //RecordProcedureCallSpecificationImpl
