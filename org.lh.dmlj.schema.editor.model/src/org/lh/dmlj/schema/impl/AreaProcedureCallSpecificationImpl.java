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
import org.lh.dmlj.schema.AreaProcedureCallFunction;
import org.lh.dmlj.schema.AreaProcedureCallSpecification;
import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.ProcedureCallTime;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Area Procedure Call Specification</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.AreaProcedureCallSpecificationImpl#getArea <em>Area</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.AreaProcedureCallSpecificationImpl#getCallTime <em>Call Time</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.AreaProcedureCallSpecificationImpl#getFunction <em>Function</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.AreaProcedureCallSpecificationImpl#getProcedure <em>Procedure</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AreaProcedureCallSpecificationImpl extends EObjectImpl implements AreaProcedureCallSpecification {
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
	 * The default value of the '{@link #getFunction() <em>Function</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFunction()
	 * @generated
	 * @ordered
	 */
	protected static final AreaProcedureCallFunction FUNCTION_EDEFAULT = AreaProcedureCallFunction.EVERY_DML_FUNCTION;
	/**
	 * The cached value of the '{@link #getFunction() <em>Function</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFunction()
	 * @generated
	 * @ordered
	 */
	protected AreaProcedureCallFunction function = FUNCTION_EDEFAULT;
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
	protected AreaProcedureCallSpecificationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.AREA_PROCEDURE_CALL_SPECIFICATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SchemaArea getArea() {
		if (eContainerFeatureID() != SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION__AREA) return null;
		return (SchemaArea)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetArea(SchemaArea newArea, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newArea, SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION__AREA, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setArea(SchemaArea newArea) {
		if (newArea != eInternalContainer() || (eContainerFeatureID() != SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION__AREA && newArea != null)) {
			if (EcoreUtil.isAncestor(this, newArea))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newArea != null)
				msgs = ((InternalEObject)newArea).eInverseAdd(this, SchemaPackage.SCHEMA_AREA__PROCEDURES, SchemaArea.class, msgs);
			msgs = basicSetArea(newArea, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION__AREA, newArea, newArea));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION__PROCEDURE, oldProcedure, procedure));
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
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION__PROCEDURE, oldProcedure, procedure));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION__AREA:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetArea((SchemaArea)otherEnd, msgs);
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
			case SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION__AREA:
				return basicSetArea(null, msgs);
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
			case SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION__AREA:
				return eInternalContainer().eInverseRemove(this, SchemaPackage.SCHEMA_AREA__PROCEDURES, SchemaArea.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
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
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION__CALL_TIME, oldCallTime, callTime));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AreaProcedureCallFunction getFunction() {
		return function;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFunction(AreaProcedureCallFunction newFunction) {
		AreaProcedureCallFunction oldFunction = function;
		function = newFunction == null ? FUNCTION_EDEFAULT : newFunction;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION__FUNCTION, oldFunction, function));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION__AREA:
				return getArea();
			case SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION__CALL_TIME:
				return getCallTime();
			case SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION__FUNCTION:
				return getFunction();
			case SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION__PROCEDURE:
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
			case SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION__AREA:
				setArea((SchemaArea)newValue);
				return;
			case SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION__CALL_TIME:
				setCallTime((ProcedureCallTime)newValue);
				return;
			case SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION__FUNCTION:
				setFunction((AreaProcedureCallFunction)newValue);
				return;
			case SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION__PROCEDURE:
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
			case SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION__AREA:
				setArea((SchemaArea)null);
				return;
			case SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION__CALL_TIME:
				setCallTime(CALL_TIME_EDEFAULT);
				return;
			case SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION__FUNCTION:
				setFunction(FUNCTION_EDEFAULT);
				return;
			case SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION__PROCEDURE:
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
			case SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION__AREA:
				return getArea() != null;
			case SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION__CALL_TIME:
				return callTime != CALL_TIME_EDEFAULT;
			case SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION__FUNCTION:
				return function != FUNCTION_EDEFAULT;
			case SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION__PROCEDURE:
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
		result.append(", function: ");
		result.append(function);
		result.append(')');
		return result.toString();
	}

} //AreaProcedureCallSpecificationImpl
