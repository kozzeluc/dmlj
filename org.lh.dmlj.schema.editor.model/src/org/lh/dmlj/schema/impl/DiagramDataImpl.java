/**
 * <copyright>
 * </copyright>
 *
 * $Id$
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

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.SchemaPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Diagram Data</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#getLocations <em>Locations</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#getZoomLevel <em>Zoom Level</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#isShowGrid <em>Show Grid</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DiagramDataImpl extends EObjectImpl implements DiagramData {
	/**
	 * The cached value of the '{@link #getLocations() <em>Locations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocations()
	 * @generated
	 * @ordered
	 */
	protected EList<DiagramLocation> locations;

	/**
	 * The default value of the '{@link #getZoomLevel() <em>Zoom Level</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getZoomLevel()
	 * @generated
	 * @ordered
	 */
	protected static final double ZOOM_LEVEL_EDEFAULT = 1.0;
	/**
	 * The cached value of the '{@link #getZoomLevel() <em>Zoom Level</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getZoomLevel()
	 * @generated
	 * @ordered
	 */
	protected double zoomLevel = ZOOM_LEVEL_EDEFAULT;

	/**
	 * The default value of the '{@link #isShowGrid() <em>Show Grid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isShowGrid()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SHOW_GRID_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isShowGrid() <em>Show Grid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isShowGrid()
	 * @generated
	 * @ordered
	 */
	protected boolean showGrid = SHOW_GRID_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DiagramDataImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.DIAGRAM_DATA;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<DiagramLocation> getLocations() {
		if (locations == null) {
			locations = new EObjectContainmentEList<DiagramLocation>(DiagramLocation.class, this, SchemaPackage.DIAGRAM_DATA__LOCATIONS);
		}
		return locations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getZoomLevel() {
		return zoomLevel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setZoomLevel(double newZoomLevel) {
		double oldZoomLevel = zoomLevel;
		zoomLevel = newZoomLevel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.DIAGRAM_DATA__ZOOM_LEVEL, oldZoomLevel, zoomLevel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isShowGrid() {
		return showGrid;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setShowGrid(boolean newShowGrid) {
		boolean oldShowGrid = showGrid;
		showGrid = newShowGrid;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.DIAGRAM_DATA__SHOW_GRID, oldShowGrid, showGrid));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.DIAGRAM_DATA__LOCATIONS:
				return ((InternalEList<?>)getLocations()).basicRemove(otherEnd, msgs);
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
			case SchemaPackage.DIAGRAM_DATA__LOCATIONS:
				return getLocations();
			case SchemaPackage.DIAGRAM_DATA__ZOOM_LEVEL:
				return getZoomLevel();
			case SchemaPackage.DIAGRAM_DATA__SHOW_GRID:
				return isShowGrid();
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
			case SchemaPackage.DIAGRAM_DATA__LOCATIONS:
				getLocations().clear();
				getLocations().addAll((Collection<? extends DiagramLocation>)newValue);
				return;
			case SchemaPackage.DIAGRAM_DATA__ZOOM_LEVEL:
				setZoomLevel((Double)newValue);
				return;
			case SchemaPackage.DIAGRAM_DATA__SHOW_GRID:
				setShowGrid((Boolean)newValue);
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
			case SchemaPackage.DIAGRAM_DATA__LOCATIONS:
				getLocations().clear();
				return;
			case SchemaPackage.DIAGRAM_DATA__ZOOM_LEVEL:
				setZoomLevel(ZOOM_LEVEL_EDEFAULT);
				return;
			case SchemaPackage.DIAGRAM_DATA__SHOW_GRID:
				setShowGrid(SHOW_GRID_EDEFAULT);
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
			case SchemaPackage.DIAGRAM_DATA__LOCATIONS:
				return locations != null && !locations.isEmpty();
			case SchemaPackage.DIAGRAM_DATA__ZOOM_LEVEL:
				return zoomLevel != ZOOM_LEVEL_EDEFAULT;
			case SchemaPackage.DIAGRAM_DATA__SHOW_GRID:
				return showGrid != SHOW_GRID_EDEFAULT;
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
		result.append(" (zoomLevel: ");
		result.append(zoomLevel);
		result.append(", showGrid: ");
		result.append(showGrid);
		result.append(')');
		return result.toString();
	}

} //DiagramDataImpl
