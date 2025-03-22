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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.ProcedureCallSpecification;
import org.lh.dmlj.schema.ProcedureCallTime;
import org.lh.dmlj.schema.SchemaPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Procedure Call Specification</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.ProcedureCallSpecificationImpl#getCallTime <em>Call Time</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.ProcedureCallSpecificationImpl#getProcedure <em>Procedure</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class ProcedureCallSpecificationImpl extends EObjectImpl implements ProcedureCallSpecification {
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
	protected ProcedureCallSpecificationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.PROCEDURE_CALL_SPECIFICATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ProcedureCallTime getCallTime() {
		return callTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCallTime(ProcedureCallTime newCallTime) {
		ProcedureCallTime oldCallTime = callTime;
		callTime = newCallTime == null ? CALL_TIME_EDEFAULT : newCallTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.PROCEDURE_CALL_SPECIFICATION__CALL_TIME, oldCallTime, callTime));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Procedure getProcedure() {
		if (procedure != null && procedure.eIsProxy()) {
			InternalEObject oldProcedure = (InternalEObject)procedure;
			procedure = (Procedure)eResolveProxy(oldProcedure);
			if (procedure != oldProcedure) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.PROCEDURE_CALL_SPECIFICATION__PROCEDURE, oldProcedure, procedure));
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
	@Override
	public void setProcedure(Procedure newProcedure) {
		Procedure oldProcedure = procedure;
		procedure = newProcedure;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.PROCEDURE_CALL_SPECIFICATION__PROCEDURE, oldProcedure, procedure));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchemaPackage.PROCEDURE_CALL_SPECIFICATION__CALL_TIME:
				return getCallTime();
			case SchemaPackage.PROCEDURE_CALL_SPECIFICATION__PROCEDURE:
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
			case SchemaPackage.PROCEDURE_CALL_SPECIFICATION__CALL_TIME:
				setCallTime((ProcedureCallTime)newValue);
				return;
			case SchemaPackage.PROCEDURE_CALL_SPECIFICATION__PROCEDURE:
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
			case SchemaPackage.PROCEDURE_CALL_SPECIFICATION__CALL_TIME:
				setCallTime(CALL_TIME_EDEFAULT);
				return;
			case SchemaPackage.PROCEDURE_CALL_SPECIFICATION__PROCEDURE:
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
			case SchemaPackage.PROCEDURE_CALL_SPECIFICATION__CALL_TIME:
				return callTime != CALL_TIME_EDEFAULT;
			case SchemaPackage.PROCEDURE_CALL_SPECIFICATION__PROCEDURE:
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

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (callTime: ");
		result.append(callTime);
		result.append(')');
		return result.toString();
	}

} //ProcedureCallSpecificationImpl
