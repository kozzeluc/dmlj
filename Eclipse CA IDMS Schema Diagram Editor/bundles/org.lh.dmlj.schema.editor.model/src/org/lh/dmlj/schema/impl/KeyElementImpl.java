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

import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SortSequence;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Key Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.KeyElementImpl#isDbkey <em>Dbkey</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.KeyElementImpl#getElement <em>Element</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.KeyElementImpl#getKey <em>Key</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.KeyElementImpl#getSortSequence <em>Sort Sequence</em>}</li>
 * </ul>
 *
 * @generated
 */
public class KeyElementImpl extends EObjectImpl implements KeyElement {
	/**
	 * The default value of the '{@link #isDbkey() <em>Dbkey</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDbkey()
	 * @generated
	 * @ordered
	 */
	protected static final boolean DBKEY_EDEFAULT = false;
	/**
	 * The cached value of the '{@link #getElement() <em>Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getElement()
	 * @generated
	 * @ordered
	 */
	protected Element element;
	/**
	 * The default value of the '{@link #getSortSequence() <em>Sort Sequence</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSortSequence()
	 * @generated
	 * @ordered
	 */
	protected static final SortSequence SORT_SEQUENCE_EDEFAULT = SortSequence.ASCENDING;
	/**
	 * The cached value of the '{@link #getSortSequence() <em>Sort Sequence</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSortSequence()
	 * @generated
	 * @ordered
	 */
	protected SortSequence sortSequence = SORT_SEQUENCE_EDEFAULT;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected KeyElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.KEY_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Element getElement() {
		if (element != null && element.eIsProxy()) {
			InternalEObject oldElement = (InternalEObject)element;
			element = (Element)eResolveProxy(oldElement);
			if (element != oldElement) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.KEY_ELEMENT__ELEMENT, oldElement, element));
			}
		}
		return element;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Element basicGetElement() {
		return element;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetElement(Element newElement, NotificationChain msgs) {
		Element oldElement = element;
		element = newElement;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.KEY_ELEMENT__ELEMENT, oldElement, newElement);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setElement(Element newElement) {
		if (newElement != element) {
			NotificationChain msgs = null;
			if (element != null)
				msgs = ((InternalEObject)element).eInverseRemove(this, SchemaPackage.ELEMENT__KEY_ELEMENTS, Element.class, msgs);
			if (newElement != null)
				msgs = ((InternalEObject)newElement).eInverseAdd(this, SchemaPackage.ELEMENT__KEY_ELEMENTS, Element.class, msgs);
			msgs = basicSetElement(newElement, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.KEY_ELEMENT__ELEMENT, newElement, newElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SortSequence getSortSequence() {
		return sortSequence;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSortSequence(SortSequence newSortSequence) {
		SortSequence oldSortSequence = sortSequence;
		sortSequence = newSortSequence == null ? SORT_SEQUENCE_EDEFAULT : newSortSequence;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.KEY_ELEMENT__SORT_SEQUENCE, oldSortSequence, sortSequence));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Key getKey() {
		if (eContainerFeatureID() != SchemaPackage.KEY_ELEMENT__KEY) return null;
		return (Key)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetKey(Key newKey, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newKey, SchemaPackage.KEY_ELEMENT__KEY, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setKey(Key newKey) {
		if (newKey != eInternalContainer() || (eContainerFeatureID() != SchemaPackage.KEY_ELEMENT__KEY && newKey != null)) {
			if (EcoreUtil.isAncestor(this, newKey))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newKey != null)
				msgs = ((InternalEObject)newKey).eInverseAdd(this, SchemaPackage.KEY__ELEMENTS, Key.class, msgs);
			msgs = basicSetKey(newKey, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.KEY_ELEMENT__KEY, newKey, newKey));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean isDbkey() {
		return getElement() == null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.KEY_ELEMENT__ELEMENT:
				if (element != null)
					msgs = ((InternalEObject)element).eInverseRemove(this, SchemaPackage.ELEMENT__KEY_ELEMENTS, Element.class, msgs);
				return basicSetElement((Element)otherEnd, msgs);
			case SchemaPackage.KEY_ELEMENT__KEY:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetKey((Key)otherEnd, msgs);
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
			case SchemaPackage.KEY_ELEMENT__ELEMENT:
				return basicSetElement(null, msgs);
			case SchemaPackage.KEY_ELEMENT__KEY:
				return basicSetKey(null, msgs);
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
			case SchemaPackage.KEY_ELEMENT__KEY:
				return eInternalContainer().eInverseRemove(this, SchemaPackage.KEY__ELEMENTS, Key.class, msgs);
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
			case SchemaPackage.KEY_ELEMENT__DBKEY:
				return isDbkey();
			case SchemaPackage.KEY_ELEMENT__ELEMENT:
				if (resolve) return getElement();
				return basicGetElement();
			case SchemaPackage.KEY_ELEMENT__KEY:
				return getKey();
			case SchemaPackage.KEY_ELEMENT__SORT_SEQUENCE:
				return getSortSequence();
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
			case SchemaPackage.KEY_ELEMENT__ELEMENT:
				setElement((Element)newValue);
				return;
			case SchemaPackage.KEY_ELEMENT__KEY:
				setKey((Key)newValue);
				return;
			case SchemaPackage.KEY_ELEMENT__SORT_SEQUENCE:
				setSortSequence((SortSequence)newValue);
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
			case SchemaPackage.KEY_ELEMENT__ELEMENT:
				setElement((Element)null);
				return;
			case SchemaPackage.KEY_ELEMENT__KEY:
				setKey((Key)null);
				return;
			case SchemaPackage.KEY_ELEMENT__SORT_SEQUENCE:
				setSortSequence(SORT_SEQUENCE_EDEFAULT);
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
			case SchemaPackage.KEY_ELEMENT__DBKEY:
				return isDbkey() != DBKEY_EDEFAULT;
			case SchemaPackage.KEY_ELEMENT__ELEMENT:
				return element != null;
			case SchemaPackage.KEY_ELEMENT__KEY:
				return getKey() != null;
			case SchemaPackage.KEY_ELEMENT__SORT_SEQUENCE:
				return sortSequence != SORT_SEQUENCE_EDEFAULT;
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
		result.append(" (sortSequence: ");
		result.append(sortSequence);
		result.append(')');
		return result.toString();
	}

} //KeyElementImpl
