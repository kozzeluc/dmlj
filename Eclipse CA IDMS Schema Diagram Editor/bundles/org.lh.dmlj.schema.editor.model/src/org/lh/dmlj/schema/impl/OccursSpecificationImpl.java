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
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.IndexElement;
import org.lh.dmlj.schema.OccursSpecification;
import org.lh.dmlj.schema.SchemaPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Occurs Specification</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.OccursSpecificationImpl#getCount <em>Count</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.OccursSpecificationImpl#getDependingOn <em>Depending On</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.OccursSpecificationImpl#getElement <em>Element</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.OccursSpecificationImpl#getIndexElements <em>Index Elements</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OccursSpecificationImpl extends EObjectImpl implements OccursSpecification {
	/**
	 * The default value of the '{@link #getCount() <em>Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCount()
	 * @generated
	 * @ordered
	 */
	protected static final short COUNT_EDEFAULT = 0;
	/**
	 * The cached value of the '{@link #getCount() <em>Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCount()
	 * @generated
	 * @ordered
	 */
	protected short count = COUNT_EDEFAULT;
	/**
	 * The cached value of the '{@link #getDependingOn() <em>Depending On</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDependingOn()
	 * @generated
	 * @ordered
	 */
	protected Element dependingOn;

	/**
	 * The cached value of the '{@link #getIndexElements() <em>Index Elements</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndexElements()
	 * @generated
	 * @ordered
	 */
	protected EList<IndexElement> indexElements;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OccursSpecificationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.OCCURS_SPECIFICATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public short getCount() {
		return count;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCount(short newCount) {
		short oldCount = count;
		count = newCount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.OCCURS_SPECIFICATION__COUNT, oldCount, count));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Element getDependingOn() {
		if (dependingOn != null && dependingOn.eIsProxy()) {
			InternalEObject oldDependingOn = (InternalEObject)dependingOn;
			dependingOn = (Element)eResolveProxy(oldDependingOn);
			if (dependingOn != oldDependingOn) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.OCCURS_SPECIFICATION__DEPENDING_ON, oldDependingOn, dependingOn));
			}
		}
		return dependingOn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Element basicGetDependingOn() {
		return dependingOn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDependingOn(Element newDependingOn) {
		Element oldDependingOn = dependingOn;
		dependingOn = newDependingOn;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.OCCURS_SPECIFICATION__DEPENDING_ON, oldDependingOn, dependingOn));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Element getElement() {
		if (eContainerFeatureID() != SchemaPackage.OCCURS_SPECIFICATION__ELEMENT) return null;
		return (Element)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetElement(Element newElement, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newElement, SchemaPackage.OCCURS_SPECIFICATION__ELEMENT, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setElement(Element newElement) {
		if (newElement != eInternalContainer() || (eContainerFeatureID() != SchemaPackage.OCCURS_SPECIFICATION__ELEMENT && newElement != null)) {
			if (EcoreUtil.isAncestor(this, newElement))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newElement != null)
				msgs = ((InternalEObject)newElement).eInverseAdd(this, SchemaPackage.ELEMENT__OCCURS_SPECIFICATION, Element.class, msgs);
			msgs = basicSetElement(newElement, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.OCCURS_SPECIFICATION__ELEMENT, newElement, newElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<IndexElement> getIndexElements() {
		if (indexElements == null) {
			indexElements = new EObjectContainmentWithInverseEList<IndexElement>(IndexElement.class, this, SchemaPackage.OCCURS_SPECIFICATION__INDEX_ELEMENTS, SchemaPackage.INDEX_ELEMENT__OCCURS_SPECIFICATION);
		}
		return indexElements;
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
			case SchemaPackage.OCCURS_SPECIFICATION__ELEMENT:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetElement((Element)otherEnd, msgs);
			case SchemaPackage.OCCURS_SPECIFICATION__INDEX_ELEMENTS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getIndexElements()).basicAdd(otherEnd, msgs);
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
			case SchemaPackage.OCCURS_SPECIFICATION__ELEMENT:
				return basicSetElement(null, msgs);
			case SchemaPackage.OCCURS_SPECIFICATION__INDEX_ELEMENTS:
				return ((InternalEList<?>)getIndexElements()).basicRemove(otherEnd, msgs);
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
			case SchemaPackage.OCCURS_SPECIFICATION__ELEMENT:
				return eInternalContainer().eInverseRemove(this, SchemaPackage.ELEMENT__OCCURS_SPECIFICATION, Element.class, msgs);
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
			case SchemaPackage.OCCURS_SPECIFICATION__COUNT:
				return getCount();
			case SchemaPackage.OCCURS_SPECIFICATION__DEPENDING_ON:
				if (resolve) return getDependingOn();
				return basicGetDependingOn();
			case SchemaPackage.OCCURS_SPECIFICATION__ELEMENT:
				return getElement();
			case SchemaPackage.OCCURS_SPECIFICATION__INDEX_ELEMENTS:
				return getIndexElements();
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
			case SchemaPackage.OCCURS_SPECIFICATION__COUNT:
				setCount((Short)newValue);
				return;
			case SchemaPackage.OCCURS_SPECIFICATION__DEPENDING_ON:
				setDependingOn((Element)newValue);
				return;
			case SchemaPackage.OCCURS_SPECIFICATION__ELEMENT:
				setElement((Element)newValue);
				return;
			case SchemaPackage.OCCURS_SPECIFICATION__INDEX_ELEMENTS:
				getIndexElements().clear();
				getIndexElements().addAll((Collection<? extends IndexElement>)newValue);
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
			case SchemaPackage.OCCURS_SPECIFICATION__COUNT:
				setCount(COUNT_EDEFAULT);
				return;
			case SchemaPackage.OCCURS_SPECIFICATION__DEPENDING_ON:
				setDependingOn((Element)null);
				return;
			case SchemaPackage.OCCURS_SPECIFICATION__ELEMENT:
				setElement((Element)null);
				return;
			case SchemaPackage.OCCURS_SPECIFICATION__INDEX_ELEMENTS:
				getIndexElements().clear();
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
			case SchemaPackage.OCCURS_SPECIFICATION__COUNT:
				return count != COUNT_EDEFAULT;
			case SchemaPackage.OCCURS_SPECIFICATION__DEPENDING_ON:
				return dependingOn != null;
			case SchemaPackage.OCCURS_SPECIFICATION__ELEMENT:
				return getElement() != null;
			case SchemaPackage.OCCURS_SPECIFICATION__INDEX_ELEMENTS:
				return indexElements != null && !indexElements.isEmpty();
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
		result.append(" (count: ");
		result.append(count);
		result.append(')');
		return result.toString();
	}

} //OccursSpecificationImpl
