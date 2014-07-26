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
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import org.lh.dmlj.schema.IndexedSetModeSpecification;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.ViaSpecification;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Set</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.SetImpl#getNodeText <em>Node Text</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SetImpl#getIndexedSetModeSpecification <em>Indexed Set Mode Specification</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SetImpl#getMembers <em>Members</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SetImpl#getMode <em>Mode</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SetImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SetImpl#getOrder <em>Order</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SetImpl#getOwner <em>Owner</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SetImpl#getSchema <em>Schema</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SetImpl#getSystemOwner <em>System Owner</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SetImpl#getViaMembers <em>Via Members</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SetImpl extends EObjectImpl implements Set {
	/**
	 * The default value of the '{@link #getNodeText() <em>Node Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNodeText()
	 * @generated
	 * @ordered
	 */
	protected static final String NODE_TEXT_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getIndexedSetModeSpecification() <em>Indexed Set Mode Specification</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndexedSetModeSpecification()
	 * @generated
	 * @ordered
	 */
	protected IndexedSetModeSpecification indexedSetModeSpecification;
	/**
	 * The cached value of the '{@link #getMembers() <em>Members</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMembers()
	 * @generated
	 * @ordered
	 */
	protected EList<MemberRole> members;
	/**
	 * The default value of the '{@link #getMode() <em>Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMode()
	 * @generated
	 * @ordered
	 */
	protected static final SetMode MODE_EDEFAULT = SetMode.CHAINED;
	/**
	 * The cached value of the '{@link #getMode() <em>Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMode()
	 * @generated
	 * @ordered
	 */
	protected SetMode mode = MODE_EDEFAULT;
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
	 * The default value of the '{@link #getOrder() <em>Order</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOrder()
	 * @generated
	 * @ordered
	 */
	protected static final SetOrder ORDER_EDEFAULT = SetOrder.FIRST;
	/**
	 * The cached value of the '{@link #getOrder() <em>Order</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOrder()
	 * @generated
	 * @ordered
	 */
	protected SetOrder order = ORDER_EDEFAULT;
	/**
	 * The cached value of the '{@link #getOwner() <em>Owner</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwner()
	 * @generated
	 * @ordered
	 */
	protected OwnerRole owner;
	/**
	 * The cached value of the '{@link #getSystemOwner() <em>System Owner</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSystemOwner()
	 * @generated
	 * @ordered
	 */
	protected SystemOwner systemOwner;
	/**
	 * The cached value of the '{@link #getViaMembers() <em>Via Members</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getViaMembers()
	 * @generated
	 * @ordered
	 */
	protected EList<ViaSpecification> viaMembers;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.SET;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getNodeText() {
		return getName();
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
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SET__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SetMode getMode() {
		return mode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMode(SetMode newMode) {
		SetMode oldMode = mode;
		mode = newMode == null ? MODE_EDEFAULT : newMode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SET__MODE, oldMode, mode));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SetOrder getOrder() {
		return order;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOrder(SetOrder newOrder) {
		SetOrder oldOrder = order;
		order = newOrder == null ? ORDER_EDEFAULT : newOrder;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SET__ORDER, oldOrder, order));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Schema getSchema() {
		if (eContainerFeatureID() != SchemaPackage.SET__SCHEMA) return null;
		return (Schema)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSchema(Schema newSchema, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newSchema, SchemaPackage.SET__SCHEMA, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSchema(Schema newSchema) {
		if (newSchema != eInternalContainer() || (eContainerFeatureID() != SchemaPackage.SET__SCHEMA && newSchema != null)) {
			if (EcoreUtil.isAncestor(this, newSchema))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newSchema != null)
				msgs = ((InternalEObject)newSchema).eInverseAdd(this, SchemaPackage.SCHEMA__SETS, Schema.class, msgs);
			msgs = basicSetSchema(newSchema, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SET__SCHEMA, newSchema, newSchema));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OwnerRole getOwner() {
		return owner;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOwner(OwnerRole newOwner, NotificationChain msgs) {
		OwnerRole oldOwner = owner;
		owner = newOwner;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.SET__OWNER, oldOwner, newOwner);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOwner(OwnerRole newOwner) {
		if (newOwner != owner) {
			NotificationChain msgs = null;
			if (owner != null)
				msgs = ((InternalEObject)owner).eInverseRemove(this, SchemaPackage.OWNER_ROLE__SET, OwnerRole.class, msgs);
			if (newOwner != null)
				msgs = ((InternalEObject)newOwner).eInverseAdd(this, SchemaPackage.OWNER_ROLE__SET, OwnerRole.class, msgs);
			msgs = basicSetOwner(newOwner, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SET__OWNER, newOwner, newOwner));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SystemOwner getSystemOwner() {
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.SET__SYSTEM_OWNER, oldSystemOwner, newSystemOwner);
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
				msgs = ((InternalEObject)systemOwner).eInverseRemove(this, SchemaPackage.SYSTEM_OWNER__SET, SystemOwner.class, msgs);
			if (newSystemOwner != null)
				msgs = ((InternalEObject)newSystemOwner).eInverseAdd(this, SchemaPackage.SYSTEM_OWNER__SET, SystemOwner.class, msgs);
			msgs = basicSetSystemOwner(newSystemOwner, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SET__SYSTEM_OWNER, newSystemOwner, newSystemOwner));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MemberRole> getMembers() {
		if (members == null) {
			members = new EObjectContainmentWithInverseEList<MemberRole>(MemberRole.class, this, SchemaPackage.SET__MEMBERS, SchemaPackage.MEMBER_ROLE__SET);
		}
		return members;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ViaSpecification> getViaMembers() {
		if (viaMembers == null) {
			viaMembers = new EObjectWithInverseResolvingEList<ViaSpecification>(ViaSpecification.class, this, SchemaPackage.SET__VIA_MEMBERS, SchemaPackage.VIA_SPECIFICATION__SET);
		}
		return viaMembers;
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
			case SchemaPackage.SET__INDEXED_SET_MODE_SPECIFICATION:
				if (indexedSetModeSpecification != null)
					msgs = ((InternalEObject)indexedSetModeSpecification).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.SET__INDEXED_SET_MODE_SPECIFICATION, null, msgs);
				return basicSetIndexedSetModeSpecification((IndexedSetModeSpecification)otherEnd, msgs);
			case SchemaPackage.SET__MEMBERS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getMembers()).basicAdd(otherEnd, msgs);
			case SchemaPackage.SET__OWNER:
				if (owner != null)
					msgs = ((InternalEObject)owner).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.SET__OWNER, null, msgs);
				return basicSetOwner((OwnerRole)otherEnd, msgs);
			case SchemaPackage.SET__SCHEMA:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetSchema((Schema)otherEnd, msgs);
			case SchemaPackage.SET__SYSTEM_OWNER:
				if (systemOwner != null)
					msgs = ((InternalEObject)systemOwner).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.SET__SYSTEM_OWNER, null, msgs);
				return basicSetSystemOwner((SystemOwner)otherEnd, msgs);
			case SchemaPackage.SET__VIA_MEMBERS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getViaMembers()).basicAdd(otherEnd, msgs);
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
			case SchemaPackage.SET__INDEXED_SET_MODE_SPECIFICATION:
				return basicSetIndexedSetModeSpecification(null, msgs);
			case SchemaPackage.SET__MEMBERS:
				return ((InternalEList<?>)getMembers()).basicRemove(otherEnd, msgs);
			case SchemaPackage.SET__OWNER:
				return basicSetOwner(null, msgs);
			case SchemaPackage.SET__SCHEMA:
				return basicSetSchema(null, msgs);
			case SchemaPackage.SET__SYSTEM_OWNER:
				return basicSetSystemOwner(null, msgs);
			case SchemaPackage.SET__VIA_MEMBERS:
				return ((InternalEList<?>)getViaMembers()).basicRemove(otherEnd, msgs);
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
			case SchemaPackage.SET__SCHEMA:
				return eInternalContainer().eInverseRemove(this, SchemaPackage.SCHEMA__SETS, Schema.class, msgs);
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
			case SchemaPackage.SET__NODE_TEXT:
				return getNodeText();
			case SchemaPackage.SET__INDEXED_SET_MODE_SPECIFICATION:
				return getIndexedSetModeSpecification();
			case SchemaPackage.SET__MEMBERS:
				return getMembers();
			case SchemaPackage.SET__MODE:
				return getMode();
			case SchemaPackage.SET__NAME:
				return getName();
			case SchemaPackage.SET__ORDER:
				return getOrder();
			case SchemaPackage.SET__OWNER:
				return getOwner();
			case SchemaPackage.SET__SCHEMA:
				return getSchema();
			case SchemaPackage.SET__SYSTEM_OWNER:
				return getSystemOwner();
			case SchemaPackage.SET__VIA_MEMBERS:
				return getViaMembers();
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
			case SchemaPackage.SET__INDEXED_SET_MODE_SPECIFICATION:
				setIndexedSetModeSpecification((IndexedSetModeSpecification)newValue);
				return;
			case SchemaPackage.SET__MEMBERS:
				getMembers().clear();
				getMembers().addAll((Collection<? extends MemberRole>)newValue);
				return;
			case SchemaPackage.SET__MODE:
				setMode((SetMode)newValue);
				return;
			case SchemaPackage.SET__NAME:
				setName((String)newValue);
				return;
			case SchemaPackage.SET__ORDER:
				setOrder((SetOrder)newValue);
				return;
			case SchemaPackage.SET__OWNER:
				setOwner((OwnerRole)newValue);
				return;
			case SchemaPackage.SET__SCHEMA:
				setSchema((Schema)newValue);
				return;
			case SchemaPackage.SET__SYSTEM_OWNER:
				setSystemOwner((SystemOwner)newValue);
				return;
			case SchemaPackage.SET__VIA_MEMBERS:
				getViaMembers().clear();
				getViaMembers().addAll((Collection<? extends ViaSpecification>)newValue);
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
			case SchemaPackage.SET__INDEXED_SET_MODE_SPECIFICATION:
				setIndexedSetModeSpecification((IndexedSetModeSpecification)null);
				return;
			case SchemaPackage.SET__MEMBERS:
				getMembers().clear();
				return;
			case SchemaPackage.SET__MODE:
				setMode(MODE_EDEFAULT);
				return;
			case SchemaPackage.SET__NAME:
				setName(NAME_EDEFAULT);
				return;
			case SchemaPackage.SET__ORDER:
				setOrder(ORDER_EDEFAULT);
				return;
			case SchemaPackage.SET__OWNER:
				setOwner((OwnerRole)null);
				return;
			case SchemaPackage.SET__SCHEMA:
				setSchema((Schema)null);
				return;
			case SchemaPackage.SET__SYSTEM_OWNER:
				setSystemOwner((SystemOwner)null);
				return;
			case SchemaPackage.SET__VIA_MEMBERS:
				getViaMembers().clear();
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
			case SchemaPackage.SET__NODE_TEXT:
				return NODE_TEXT_EDEFAULT == null ? getNodeText() != null : !NODE_TEXT_EDEFAULT.equals(getNodeText());
			case SchemaPackage.SET__INDEXED_SET_MODE_SPECIFICATION:
				return indexedSetModeSpecification != null;
			case SchemaPackage.SET__MEMBERS:
				return members != null && !members.isEmpty();
			case SchemaPackage.SET__MODE:
				return mode != MODE_EDEFAULT;
			case SchemaPackage.SET__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case SchemaPackage.SET__ORDER:
				return order != ORDER_EDEFAULT;
			case SchemaPackage.SET__OWNER:
				return owner != null;
			case SchemaPackage.SET__SCHEMA:
				return getSchema() != null;
			case SchemaPackage.SET__SYSTEM_OWNER:
				return systemOwner != null;
			case SchemaPackage.SET__VIA_MEMBERS:
				return viaMembers != null && !viaMembers.isEmpty();
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
		result.append(" (mode: ");
		result.append(mode);
		result.append(", name: ");
		result.append(name);
		result.append(", order: ");
		result.append(order);
		result.append(')');
		return result.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IndexedSetModeSpecification getIndexedSetModeSpecification() {
		return indexedSetModeSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetIndexedSetModeSpecification(IndexedSetModeSpecification newIndexedSetModeSpecification, NotificationChain msgs) {
		IndexedSetModeSpecification oldIndexedSetModeSpecification = indexedSetModeSpecification;
		indexedSetModeSpecification = newIndexedSetModeSpecification;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.SET__INDEXED_SET_MODE_SPECIFICATION, oldIndexedSetModeSpecification, newIndexedSetModeSpecification);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIndexedSetModeSpecification(IndexedSetModeSpecification newIndexedSetModeSpecification) {
		if (newIndexedSetModeSpecification != indexedSetModeSpecification) {
			NotificationChain msgs = null;
			if (indexedSetModeSpecification != null)
				msgs = ((InternalEObject)indexedSetModeSpecification).eInverseRemove(this, SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__SET, IndexedSetModeSpecification.class, msgs);
			if (newIndexedSetModeSpecification != null)
				msgs = ((InternalEObject)newIndexedSetModeSpecification).eInverseAdd(this, SchemaPackage.INDEXED_SET_MODE_SPECIFICATION__SET, IndexedSetModeSpecification.class, msgs);
			msgs = basicSetIndexedSetModeSpecification(newIndexedSetModeSpecification, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SET__INDEXED_SET_MODE_SPECIFICATION, newIndexedSetModeSpecification, newIndexedSetModeSpecification));
	}

	@Override
	public int compareTo(Set other) {
		return getName().compareTo(other.getName());
	}

} //SetImpl
