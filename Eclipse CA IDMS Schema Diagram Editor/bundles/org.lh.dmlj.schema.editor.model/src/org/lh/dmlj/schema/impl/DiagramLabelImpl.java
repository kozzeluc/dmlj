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

import org.eclipse.emf.ecore.util.EcoreUtil;

import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.INodeTextProvider;
import org.lh.dmlj.schema.SchemaPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Diagram Label</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramLabelImpl#getNodeText <em>Node Text</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramLabelImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramLabelImpl#getDiagramData <em>Diagram Data</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DiagramLabelImpl extends ResizableDiagramNodeImpl implements DiagramLabel {
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DiagramLabelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.DIAGRAM_LABEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getNodeText() {
		return "Diagram label";
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.DIAGRAM_LABEL__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DiagramData getDiagramData() {
		if (eContainerFeatureID() != SchemaPackage.DIAGRAM_LABEL__DIAGRAM_DATA) return null;
		return (DiagramData)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDiagramData(DiagramData newDiagramData, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newDiagramData, SchemaPackage.DIAGRAM_LABEL__DIAGRAM_DATA, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDiagramData(DiagramData newDiagramData) {
		if (newDiagramData != eInternalContainer() || (eContainerFeatureID() != SchemaPackage.DIAGRAM_LABEL__DIAGRAM_DATA && newDiagramData != null)) {
			if (EcoreUtil.isAncestor(this, newDiagramData))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newDiagramData != null)
				msgs = ((InternalEObject)newDiagramData).eInverseAdd(this, SchemaPackage.DIAGRAM_DATA__LABEL, DiagramData.class, msgs);
			msgs = basicSetDiagramData(newDiagramData, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.DIAGRAM_LABEL__DIAGRAM_DATA, newDiagramData, newDiagramData));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.DIAGRAM_LABEL__DIAGRAM_DATA:
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
			case SchemaPackage.DIAGRAM_LABEL__DIAGRAM_DATA:
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
			case SchemaPackage.DIAGRAM_LABEL__DIAGRAM_DATA:
				return eInternalContainer().eInverseRemove(this, SchemaPackage.DIAGRAM_DATA__LABEL, DiagramData.class, msgs);
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
			case SchemaPackage.DIAGRAM_LABEL__NODE_TEXT:
				return getNodeText();
			case SchemaPackage.DIAGRAM_LABEL__DESCRIPTION:
				return getDescription();
			case SchemaPackage.DIAGRAM_LABEL__DIAGRAM_DATA:
				return getDiagramData();
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
			case SchemaPackage.DIAGRAM_LABEL__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case SchemaPackage.DIAGRAM_LABEL__DIAGRAM_DATA:
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
			case SchemaPackage.DIAGRAM_LABEL__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case SchemaPackage.DIAGRAM_LABEL__DIAGRAM_DATA:
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
			case SchemaPackage.DIAGRAM_LABEL__NODE_TEXT:
				return NODE_TEXT_EDEFAULT == null ? getNodeText() != null : !NODE_TEXT_EDEFAULT.equals(getNodeText());
			case SchemaPackage.DIAGRAM_LABEL__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case SchemaPackage.DIAGRAM_LABEL__DIAGRAM_DATA:
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
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == INodeTextProvider.class) {
			switch (derivedFeatureID) {
				case SchemaPackage.DIAGRAM_LABEL__NODE_TEXT: return SchemaPackage.INODE_TEXT_PROVIDER__NODE_TEXT;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == INodeTextProvider.class) {
			switch (baseFeatureID) {
				case SchemaPackage.INODE_TEXT_PROVIDER__NODE_TEXT: return SchemaPackage.DIAGRAM_LABEL__NODE_TEXT;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
		result.append(" (description: ");
		result.append(description);
		result.append(')');
		return result.toString();
	}

	@Override
	public int compareTo(DiagramLabel diagramLabel) {
		return 0; // there really should be only 1 diagram label in a diagram
	}

} //DiagramLabelImpl
