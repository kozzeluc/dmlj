/**
 * Copyright (C) 2015  Luc Hermans
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

import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.Guide;
import org.lh.dmlj.schema.Ruler;
import org.lh.dmlj.schema.RulerType;
import org.lh.dmlj.schema.SchemaPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Ruler</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.RulerImpl#getGuides <em>Guides</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.RulerImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.RulerImpl#getDiagramData <em>Diagram Data</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RulerImpl extends EObjectImpl implements Ruler {
	/**
	 * The cached value of the '{@link #getGuides() <em>Guides</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGuides()
	 * @generated
	 * @ordered
	 */
	protected EList<Guide> guides;

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final RulerType TYPE_EDEFAULT = RulerType.HORIZONTAL;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected RulerType type = TYPE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RulerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.RULER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Guide> getGuides() {
		if (guides == null) {
			guides = new EObjectContainmentWithInverseEList<Guide>(Guide.class, this, SchemaPackage.RULER__GUIDES, SchemaPackage.GUIDE__RULER);
		}
		return guides;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public RulerType getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setType(RulerType newType) {
		RulerType oldType = type;
		type = newType == null ? TYPE_EDEFAULT : newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.RULER__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DiagramData getDiagramData() {
		if (eContainerFeatureID() != SchemaPackage.RULER__DIAGRAM_DATA) return null;
		return (DiagramData)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDiagramData(DiagramData newDiagramData, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newDiagramData, SchemaPackage.RULER__DIAGRAM_DATA, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDiagramData(DiagramData newDiagramData) {
		if (newDiagramData != eInternalContainer() || (eContainerFeatureID() != SchemaPackage.RULER__DIAGRAM_DATA && newDiagramData != null)) {
			if (EcoreUtil.isAncestor(this, newDiagramData))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newDiagramData != null)
				msgs = ((InternalEObject)newDiagramData).eInverseAdd(this, SchemaPackage.DIAGRAM_DATA__RULERS, DiagramData.class, msgs);
			msgs = basicSetDiagramData(newDiagramData, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.RULER__DIAGRAM_DATA, newDiagramData, newDiagramData));
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
			case SchemaPackage.RULER__GUIDES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getGuides()).basicAdd(otherEnd, msgs);
			case SchemaPackage.RULER__DIAGRAM_DATA:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetDiagramData((DiagramData)otherEnd, msgs);
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
			case SchemaPackage.RULER__GUIDES:
				return ((InternalEList<?>)getGuides()).basicRemove(otherEnd, msgs);
			case SchemaPackage.RULER__DIAGRAM_DATA:
				return basicSetDiagramData(null, msgs);
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
			case SchemaPackage.RULER__DIAGRAM_DATA:
				return eInternalContainer().eInverseRemove(this, SchemaPackage.DIAGRAM_DATA__RULERS, DiagramData.class, msgs);
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
			case SchemaPackage.RULER__GUIDES:
				return getGuides();
			case SchemaPackage.RULER__TYPE:
				return getType();
			case SchemaPackage.RULER__DIAGRAM_DATA:
				return getDiagramData();
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
			case SchemaPackage.RULER__GUIDES:
				getGuides().clear();
				getGuides().addAll((Collection<? extends Guide>)newValue);
				return;
			case SchemaPackage.RULER__TYPE:
				setType((RulerType)newValue);
				return;
			case SchemaPackage.RULER__DIAGRAM_DATA:
				setDiagramData((DiagramData)newValue);
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
			case SchemaPackage.RULER__GUIDES:
				getGuides().clear();
				return;
			case SchemaPackage.RULER__TYPE:
				setType(TYPE_EDEFAULT);
				return;
			case SchemaPackage.RULER__DIAGRAM_DATA:
				setDiagramData((DiagramData)null);
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
			case SchemaPackage.RULER__GUIDES:
				return guides != null && !guides.isEmpty();
			case SchemaPackage.RULER__TYPE:
				return type != TYPE_EDEFAULT;
			case SchemaPackage.RULER__DIAGRAM_DATA:
				return getDiagramData() != null;
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
		result.append(" (type: ");
		result.append(type);
		result.append(')');
		return result.toString();
	}

} //RulerImpl
