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


import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.DiagramNode;
import org.lh.dmlj.schema.SchemaPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Diagram Node</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramNodeImpl#getDiagramLocation <em>Diagram Location</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class DiagramNodeImpl extends EObjectImpl implements DiagramNode {
	/**
	 * The cached value of the '{@link #getDiagramLocation() <em>Diagram Location</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDiagramLocation()
	 * @generated
	 * @ordered
	 */
	protected DiagramLocation diagramLocation;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DiagramNodeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.DIAGRAM_NODE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramLocation getDiagramLocation() {
		if (diagramLocation != null && diagramLocation.eIsProxy()) {
			InternalEObject oldDiagramLocation = (InternalEObject)diagramLocation;
			diagramLocation = (DiagramLocation)eResolveProxy(oldDiagramLocation);
			if (diagramLocation != oldDiagramLocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.DIAGRAM_NODE__DIAGRAM_LOCATION, oldDiagramLocation, diagramLocation));
			}
		}
		return diagramLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramLocation basicGetDiagramLocation() {
		return diagramLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDiagramLocation(DiagramLocation newDiagramLocation) {
		DiagramLocation oldDiagramLocation = diagramLocation;
		diagramLocation = newDiagramLocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.DIAGRAM_NODE__DIAGRAM_LOCATION, oldDiagramLocation, diagramLocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchemaPackage.DIAGRAM_NODE__DIAGRAM_LOCATION:
				if (resolve) return getDiagramLocation();
				return basicGetDiagramLocation();
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
			case SchemaPackage.DIAGRAM_NODE__DIAGRAM_LOCATION:
				setDiagramLocation((DiagramLocation)newValue);
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
			case SchemaPackage.DIAGRAM_NODE__DIAGRAM_LOCATION:
				setDiagramLocation((DiagramLocation)null);
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
			case SchemaPackage.DIAGRAM_NODE__DIAGRAM_LOCATION:
				return diagramLocation != null;
		}
		return super.eIsSet(featureID);
	}

} //DiagramNodeImpl
