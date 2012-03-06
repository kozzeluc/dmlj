/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.lh.dmlj.schema.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.lh.dmlj.schema.AreaProcedureCallSpecification;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SystemOwner;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Area</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaAreaImpl#getAreaSpecifications <em>Area Specifications</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaAreaImpl#getIndexes <em>Indexes</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaAreaImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaAreaImpl#getProcedures <em>Procedures</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaAreaImpl#getRecords <em>Records</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaAreaImpl#getSchema <em>Schema</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SchemaAreaImpl extends EObjectImpl implements SchemaArea {
	/**
	 * The cached value of the '{@link #getAreaSpecifications() <em>Area Specifications</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAreaSpecifications()
	 * @generated
	 * @ordered
	 */
	protected EList<AreaSpecification> areaSpecifications;
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
	 * The cached value of the '{@link #getProcedures() <em>Procedures</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProcedures()
	 * @generated
	 * @ordered
	 */
	protected EList<AreaProcedureCallSpecification> procedures;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SchemaAreaImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.SCHEMA_AREA;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA_AREA__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Schema getSchema() {
		if (eContainerFeatureID() != SchemaPackage.SCHEMA_AREA__SCHEMA) return null;
		return (Schema)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSchema(Schema newSchema, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newSchema, SchemaPackage.SCHEMA_AREA__SCHEMA, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSchema(Schema newSchema) {
		if (newSchema != eInternalContainer() || (eContainerFeatureID() != SchemaPackage.SCHEMA_AREA__SCHEMA && newSchema != null)) {
			if (EcoreUtil.isAncestor(this, newSchema))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newSchema != null)
				msgs = ((InternalEObject)newSchema).eInverseAdd(this, SchemaPackage.SCHEMA__AREAS, Schema.class, msgs);
			msgs = basicSetSchema(newSchema, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA_AREA__SCHEMA, newSchema, newSchema));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<SystemOwner> getIndexes() {
		EStructuralFeature eFeature = 
			SchemaPackage.Literals.SCHEMA_AREA__INDEXES;		
		Collection<SystemOwner> result = new ArrayList<SystemOwner>();
		for (AreaSpecification areaSpecification : getAreaSpecifications()) {
			SystemOwner systemOwner = areaSpecification.getSystemOwner();
			if (systemOwner != null) {
				result.add(systemOwner);
			}
		}
		return new EcoreEList.UnmodifiableEList<SystemOwner>(this, eFeature, 
															 result.size(), 
															 result.toArray());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AreaProcedureCallSpecification> getProcedures() {
		if (procedures == null) {
			procedures = new EObjectContainmentEList<AreaProcedureCallSpecification>(AreaProcedureCallSpecification.class, this, SchemaPackage.SCHEMA_AREA__PROCEDURES);
		}
		return procedures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AreaSpecification> getAreaSpecifications() {
		if (areaSpecifications == null) {
			areaSpecifications = new EObjectContainmentWithInverseEList<AreaSpecification>(AreaSpecification.class, this, SchemaPackage.SCHEMA_AREA__AREA_SPECIFICATIONS, SchemaPackage.AREA_SPECIFICATION__AREA);
		}
		return areaSpecifications;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<SchemaRecord> getRecords() {
		EStructuralFeature eFeature = 
			SchemaPackage.Literals.SCHEMA_AREA__RECORDS;		
		Collection<SchemaRecord> result = new ArrayList<SchemaRecord>();
		for (AreaSpecification areaSpecification : getAreaSpecifications()) {
			SchemaRecord record = areaSpecification.getRecord();
			if (record != null) {
				result.add(record);
			}
		}
		return new EcoreEList.UnmodifiableEList<SchemaRecord>(this, 
															  eFeature, 
															  result.size(), 
															  result.toArray());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public SchemaRecord getRecord(String name) {
		for (SchemaRecord record : getRecords()) {
			if (record.getName().equals(name)) {
				return record;
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public SchemaRecord getRecord(short recordId) {
		for (SchemaRecord record : getRecords()) {
			if (record.getId() == recordId) {
				return record;
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.SCHEMA_AREA__AREA_SPECIFICATIONS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getAreaSpecifications()).basicAdd(otherEnd, msgs);
			case SchemaPackage.SCHEMA_AREA__SCHEMA:
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
			case SchemaPackage.SCHEMA_AREA__AREA_SPECIFICATIONS:
				return ((InternalEList<?>)getAreaSpecifications()).basicRemove(otherEnd, msgs);
			case SchemaPackage.SCHEMA_AREA__PROCEDURES:
				return ((InternalEList<?>)getProcedures()).basicRemove(otherEnd, msgs);
			case SchemaPackage.SCHEMA_AREA__SCHEMA:
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
			case SchemaPackage.SCHEMA_AREA__SCHEMA:
				return eInternalContainer().eInverseRemove(this, SchemaPackage.SCHEMA__AREAS, Schema.class, msgs);
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
			case SchemaPackage.SCHEMA_AREA__AREA_SPECIFICATIONS:
				return getAreaSpecifications();
			case SchemaPackage.SCHEMA_AREA__INDEXES:
				return getIndexes();
			case SchemaPackage.SCHEMA_AREA__NAME:
				return getName();
			case SchemaPackage.SCHEMA_AREA__PROCEDURES:
				return getProcedures();
			case SchemaPackage.SCHEMA_AREA__RECORDS:
				return getRecords();
			case SchemaPackage.SCHEMA_AREA__SCHEMA:
				return getSchema();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case SchemaPackage.SCHEMA_AREA__AREA_SPECIFICATIONS:
				getAreaSpecifications().clear();
				getAreaSpecifications().addAll((Collection<? extends AreaSpecification>)newValue);
				return;
			case SchemaPackage.SCHEMA_AREA__NAME:
				setName((String)newValue);
				return;
			case SchemaPackage.SCHEMA_AREA__PROCEDURES:
				getProcedures().clear();
				getProcedures().addAll((Collection<? extends AreaProcedureCallSpecification>)newValue);
				return;
			case SchemaPackage.SCHEMA_AREA__SCHEMA:
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
			case SchemaPackage.SCHEMA_AREA__AREA_SPECIFICATIONS:
				getAreaSpecifications().clear();
				return;
			case SchemaPackage.SCHEMA_AREA__NAME:
				setName(NAME_EDEFAULT);
				return;
			case SchemaPackage.SCHEMA_AREA__PROCEDURES:
				getProcedures().clear();
				return;
			case SchemaPackage.SCHEMA_AREA__SCHEMA:
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
			case SchemaPackage.SCHEMA_AREA__AREA_SPECIFICATIONS:
				return areaSpecifications != null && !areaSpecifications.isEmpty();
			case SchemaPackage.SCHEMA_AREA__INDEXES:
				return !getIndexes().isEmpty();
			case SchemaPackage.SCHEMA_AREA__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case SchemaPackage.SCHEMA_AREA__PROCEDURES:
				return procedures != null && !procedures.isEmpty();
			case SchemaPackage.SCHEMA_AREA__RECORDS:
				return !getRecords().isEmpty();
			case SchemaPackage.SCHEMA_AREA__SCHEMA:
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

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //SchemaAreaImpl
