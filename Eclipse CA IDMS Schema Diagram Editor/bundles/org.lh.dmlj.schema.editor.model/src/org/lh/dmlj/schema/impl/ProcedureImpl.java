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
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.lh.dmlj.schema.AreaProcedureCallSpecification;
import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.ProcedureCallSpecification;
import org.lh.dmlj.schema.RecordProcedureCallSpecification;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Procedure</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.ProcedureImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.ProcedureImpl#getSchema <em>Schema</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProcedureImpl extends EObjectImpl implements Procedure {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ProcedureImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.PROCEDURE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.PROCEDURE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Schema getSchema() {
		if (eContainerFeatureID() != SchemaPackage.PROCEDURE__SCHEMA) return null;
		return (Schema)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSchema(Schema newSchema, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newSchema, SchemaPackage.PROCEDURE__SCHEMA, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSchema(Schema newSchema) {
		if (newSchema != eInternalContainer() || (eContainerFeatureID() != SchemaPackage.PROCEDURE__SCHEMA && newSchema != null)) {
			if (EcoreUtil.isAncestor(this, newSchema))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newSchema != null)
				msgs = ((InternalEObject)newSchema).eInverseAdd(this, SchemaPackage.SCHEMA__PROCEDURES, Schema.class, msgs);
			msgs = basicSetSchema(newSchema, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.PROCEDURE__SCHEMA, newSchema, newSchema));
	}

	/**
	 * <!-- begin-user-doc -->
	 *  Unfortunately, we don't have a direct reference from a procedure to all referencing 
	 *  (area and record) procedure call specifications; creating such a reference would break the 
	 *  backward XMI format compatibility, so we provide an operation that traverses all areas and 
	 *  records in order to find all references to the procedure. 
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<ProcedureCallSpecification> getCallSpecifications() {
		EList<ProcedureCallSpecification> callSpecifications = new BasicEList<>();
		for (SchemaArea area : getSchema().getAreas()) {
			for (AreaProcedureCallSpecification callSpecification : area.getProcedures()) {
				if (callSpecification.getProcedure() == this) {
					callSpecifications.add(callSpecification);
				}
			}
		}
		for (SchemaRecord record : getSchema().getRecords()) {
			for (RecordProcedureCallSpecification callSpecification : record.getProcedures()) {
				if (callSpecification.getProcedure() == this) {
					callSpecifications.add(callSpecification);
				}
			}
		}
		return callSpecifications;
	}	

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.PROCEDURE__SCHEMA:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetSchema((Schema)otherEnd, msgs);
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
			case SchemaPackage.PROCEDURE__SCHEMA:
				return basicSetSchema(null, msgs);
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
			case SchemaPackage.PROCEDURE__SCHEMA:
				return eInternalContainer().eInverseRemove(this, SchemaPackage.SCHEMA__PROCEDURES, Schema.class, msgs);
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
			case SchemaPackage.PROCEDURE__NAME:
				return getName();
			case SchemaPackage.PROCEDURE__SCHEMA:
				return getSchema();
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
			case SchemaPackage.PROCEDURE__NAME:
				setName((String)newValue);
				return;
			case SchemaPackage.PROCEDURE__SCHEMA:
				setSchema((Schema)newValue);
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
			case SchemaPackage.PROCEDURE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case SchemaPackage.PROCEDURE__SCHEMA:
				setSchema((Schema)null);
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
			case SchemaPackage.PROCEDURE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case SchemaPackage.PROCEDURE__SCHEMA:
				return getSchema() != null;
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
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //ProcedureImpl
