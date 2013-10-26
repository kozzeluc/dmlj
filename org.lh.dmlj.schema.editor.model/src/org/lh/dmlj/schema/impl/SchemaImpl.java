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

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Schema</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaImpl#getAreas <em>Areas</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaImpl#getComments <em>Comments</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaImpl#getDiagramData <em>Diagram Data</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaImpl#getMemoDate <em>Memo Date</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaImpl#getProcedures <em>Procedures</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaImpl#getRecords <em>Records</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaImpl#getSets <em>Sets</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaImpl#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SchemaImpl extends EObjectImpl implements Schema {
	/**
	 * The cached value of the '{@link #getAreas() <em>Areas</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAreas()
	 * @generated
	 * @ordered
	 */
	protected EList<SchemaArea> areas;
	/**
	 * The cached value of the '{@link #getComments() <em>Comments</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComments()
	 * @generated
	 * @ordered
	 */
	protected EList<String> comments;
	/**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;
	/**
	 * The cached value of the '{@link #getDiagramData() <em>Diagram Data</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDiagramData()
	 * @generated
	 * @ordered
	 */
	protected DiagramData diagramData;
	/**
	 * The default value of the '{@link #getMemoDate() <em>Memo Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMemoDate()
	 * @generated
	 * @ordered
	 */
	protected static final String MEMO_DATE_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getMemoDate() <em>Memo Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMemoDate()
	 * @generated
	 * @ordered
	 */
	protected String memoDate = MEMO_DATE_EDEFAULT;
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
	protected EList<Procedure> procedures;
	/**
	 * The cached value of the '{@link #getRecords() <em>Records</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRecords()
	 * @generated
	 * @ordered
	 */
	protected EList<SchemaRecord> records;
	/**
	 * The cached value of the '{@link #getSets() <em>Sets</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSets()
	 * @generated
	 * @ordered
	 */
	protected EList<Set> sets;
	/**
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected static final short VERSION_EDEFAULT = 0;
	/**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected short version = VERSION_EDEFAULT;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SchemaImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.SCHEMA;
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
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public short getVersion() {
		return version;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVersion(short newVersion) {
		short oldVersion = version;
		version = newVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA__VERSION, oldVersion, version));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getMemoDate() {
		return memoDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMemoDate(String newMemoDate) {
		String oldMemoDate = memoDate;
		memoDate = newMemoDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA__MEMO_DATE, oldMemoDate, memoDate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SchemaArea> getAreas() {
		if (areas == null) {
			areas = new EObjectContainmentWithInverseEList<SchemaArea>(SchemaArea.class, this, SchemaPackage.SCHEMA__AREAS, SchemaPackage.SCHEMA_AREA__SCHEMA);
		}
		return areas;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getComments() {
		if (comments == null) {
			comments = new EDataTypeUniqueEList<String>(String.class, this, SchemaPackage.SCHEMA__COMMENTS);
		}
		return comments;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SchemaRecord> getRecords() {
		if (records == null) {
			records = new EObjectContainmentWithInverseEList<SchemaRecord>(SchemaRecord.class, this, SchemaPackage.SCHEMA__RECORDS, SchemaPackage.SCHEMA_RECORD__SCHEMA);
		}
		return records;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Set> getSets() {
		if (sets == null) {
			sets = new EObjectContainmentWithInverseEList<Set>(Set.class, this, SchemaPackage.SCHEMA__SETS, SchemaPackage.SET__SCHEMA);
		}
		return sets;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramData getDiagramData() {
		return diagramData;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDiagramData(DiagramData newDiagramData, NotificationChain msgs) {
		DiagramData oldDiagramData = diagramData;
		diagramData = newDiagramData;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA__DIAGRAM_DATA, oldDiagramData, newDiagramData);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDiagramData(DiagramData newDiagramData) {
		if (newDiagramData != diagramData) {
			NotificationChain msgs = null;
			if (diagramData != null)
				msgs = ((InternalEObject)diagramData).eInverseRemove(this, SchemaPackage.DIAGRAM_DATA__SCHEMA, DiagramData.class, msgs);
			if (newDiagramData != null)
				msgs = ((InternalEObject)newDiagramData).eInverseAdd(this, SchemaPackage.DIAGRAM_DATA__SCHEMA, DiagramData.class, msgs);
			msgs = basicSetDiagramData(newDiagramData, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA__DIAGRAM_DATA, newDiagramData, newDiagramData));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Procedure> getProcedures() {
		if (procedures == null) {
			procedures = new EObjectContainmentEList<Procedure>(Procedure.class, this, SchemaPackage.SCHEMA__PROCEDURES);
		}
		return procedures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Procedure getProcedure(String name) {
		for (Procedure procedure : getProcedures()) {
			if (procedure.getName().equals(name)) {
				return procedure;
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public SchemaArea getArea(String name) {
		for (SchemaArea area : getAreas()) {
			if (area.getName().equals(name)) {
				return area;
			}
		}
		return null;
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
	public SchemaRecord getRecord(String areaName, short recordId) {
		SchemaArea area = getArea(areaName);
		if (area != null) {
			return area.getRecord(recordId);
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Set getSet(String name) {
		for (Set set : getSets()) {
			if (set.getName().equals(name)) {
				return set;
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
			case SchemaPackage.SCHEMA__AREAS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getAreas()).basicAdd(otherEnd, msgs);
			case SchemaPackage.SCHEMA__DIAGRAM_DATA:
				if (diagramData != null)
					msgs = ((InternalEObject)diagramData).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.SCHEMA__DIAGRAM_DATA, null, msgs);
				return basicSetDiagramData((DiagramData)otherEnd, msgs);
			case SchemaPackage.SCHEMA__RECORDS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getRecords()).basicAdd(otherEnd, msgs);
			case SchemaPackage.SCHEMA__SETS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getSets()).basicAdd(otherEnd, msgs);
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
			case SchemaPackage.SCHEMA__AREAS:
				return ((InternalEList<?>)getAreas()).basicRemove(otherEnd, msgs);
			case SchemaPackage.SCHEMA__DIAGRAM_DATA:
				return basicSetDiagramData(null, msgs);
			case SchemaPackage.SCHEMA__PROCEDURES:
				return ((InternalEList<?>)getProcedures()).basicRemove(otherEnd, msgs);
			case SchemaPackage.SCHEMA__RECORDS:
				return ((InternalEList<?>)getRecords()).basicRemove(otherEnd, msgs);
			case SchemaPackage.SCHEMA__SETS:
				return ((InternalEList<?>)getSets()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchemaPackage.SCHEMA__AREAS:
				return getAreas();
			case SchemaPackage.SCHEMA__COMMENTS:
				return getComments();
			case SchemaPackage.SCHEMA__DESCRIPTION:
				return getDescription();
			case SchemaPackage.SCHEMA__DIAGRAM_DATA:
				return getDiagramData();
			case SchemaPackage.SCHEMA__MEMO_DATE:
				return getMemoDate();
			case SchemaPackage.SCHEMA__NAME:
				return getName();
			case SchemaPackage.SCHEMA__PROCEDURES:
				return getProcedures();
			case SchemaPackage.SCHEMA__RECORDS:
				return getRecords();
			case SchemaPackage.SCHEMA__SETS:
				return getSets();
			case SchemaPackage.SCHEMA__VERSION:
				return getVersion();
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
			case SchemaPackage.SCHEMA__AREAS:
				getAreas().clear();
				getAreas().addAll((Collection<? extends SchemaArea>)newValue);
				return;
			case SchemaPackage.SCHEMA__COMMENTS:
				getComments().clear();
				getComments().addAll((Collection<? extends String>)newValue);
				return;
			case SchemaPackage.SCHEMA__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case SchemaPackage.SCHEMA__DIAGRAM_DATA:
				setDiagramData((DiagramData)newValue);
				return;
			case SchemaPackage.SCHEMA__MEMO_DATE:
				setMemoDate((String)newValue);
				return;
			case SchemaPackage.SCHEMA__NAME:
				setName((String)newValue);
				return;
			case SchemaPackage.SCHEMA__PROCEDURES:
				getProcedures().clear();
				getProcedures().addAll((Collection<? extends Procedure>)newValue);
				return;
			case SchemaPackage.SCHEMA__RECORDS:
				getRecords().clear();
				getRecords().addAll((Collection<? extends SchemaRecord>)newValue);
				return;
			case SchemaPackage.SCHEMA__SETS:
				getSets().clear();
				getSets().addAll((Collection<? extends Set>)newValue);
				return;
			case SchemaPackage.SCHEMA__VERSION:
				setVersion((Short)newValue);
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
			case SchemaPackage.SCHEMA__AREAS:
				getAreas().clear();
				return;
			case SchemaPackage.SCHEMA__COMMENTS:
				getComments().clear();
				return;
			case SchemaPackage.SCHEMA__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case SchemaPackage.SCHEMA__DIAGRAM_DATA:
				setDiagramData((DiagramData)null);
				return;
			case SchemaPackage.SCHEMA__MEMO_DATE:
				setMemoDate(MEMO_DATE_EDEFAULT);
				return;
			case SchemaPackage.SCHEMA__NAME:
				setName(NAME_EDEFAULT);
				return;
			case SchemaPackage.SCHEMA__PROCEDURES:
				getProcedures().clear();
				return;
			case SchemaPackage.SCHEMA__RECORDS:
				getRecords().clear();
				return;
			case SchemaPackage.SCHEMA__SETS:
				getSets().clear();
				return;
			case SchemaPackage.SCHEMA__VERSION:
				setVersion(VERSION_EDEFAULT);
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
			case SchemaPackage.SCHEMA__AREAS:
				return areas != null && !areas.isEmpty();
			case SchemaPackage.SCHEMA__COMMENTS:
				return comments != null && !comments.isEmpty();
			case SchemaPackage.SCHEMA__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case SchemaPackage.SCHEMA__DIAGRAM_DATA:
				return diagramData != null;
			case SchemaPackage.SCHEMA__MEMO_DATE:
				return MEMO_DATE_EDEFAULT == null ? memoDate != null : !MEMO_DATE_EDEFAULT.equals(memoDate);
			case SchemaPackage.SCHEMA__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case SchemaPackage.SCHEMA__PROCEDURES:
				return procedures != null && !procedures.isEmpty();
			case SchemaPackage.SCHEMA__RECORDS:
				return records != null && !records.isEmpty();
			case SchemaPackage.SCHEMA__SETS:
				return sets != null && !sets.isEmpty();
			case SchemaPackage.SCHEMA__VERSION:
				return version != VERSION_EDEFAULT;
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
		result.append(" (comments: ");
		result.append(comments);
		result.append(", description: ");
		result.append(description);
		result.append(", memoDate: ");
		result.append(memoDate);
		result.append(", name: ");
		result.append(name);
		result.append(", version: ");
		result.append(version);
		result.append(')');
		return result.toString();
	}

} //SchemaImpl
