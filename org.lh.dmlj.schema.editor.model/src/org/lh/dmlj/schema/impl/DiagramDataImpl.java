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
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.Ruler;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.Unit;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Diagram Data</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#getConnectionLabels <em>Connection Labels</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#getConnectionParts <em>Connection Parts</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#getConnectors <em>Connectors</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#getHorizontalRuler <em>Horizontal Ruler</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#getLocations <em>Locations</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#isShowGrid <em>Show Grid</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#isShowRulers <em>Show Rulers</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#isSnapToGeometry <em>Snap To Geometry</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#isSnapToGrid <em>Snap To Grid</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#isSnapToGuides <em>Snap To Guides</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#getUnit <em>Unit</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#getVerticalRuler <em>Vertical Ruler</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#getZoomLevel <em>Zoom Level</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#getRulers <em>Rulers</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DiagramDataImpl extends EObjectImpl implements DiagramData {
	/**
	 * The cached value of the '{@link #getConnectionLabels() <em>Connection Labels</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConnectionLabels()
	 * @generated
	 * @ordered
	 */
	protected EList<ConnectionLabel> connectionLabels;

	/**
	 * The cached value of the '{@link #getConnectionParts() <em>Connection Parts</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConnectionParts()
	 * @generated
	 * @ordered
	 */
	protected EList<ConnectionPart> connectionParts;

	/**
	 * The cached value of the '{@link #getConnectors() <em>Connectors</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConnectors()
	 * @generated
	 * @ordered
	 */
	protected EList<Connector> connectors;

	/**
	 * The cached value of the '{@link #getHorizontalRuler() <em>Horizontal Ruler</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHorizontalRuler()
	 * @generated
	 * @ordered
	 */
	protected Ruler horizontalRuler;

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
	 * The default value of the '{@link #isShowRulers() <em>Show Rulers</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isShowRulers()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SHOW_RULERS_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isShowRulers() <em>Show Rulers</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isShowRulers()
	 * @generated
	 * @ordered
	 */
	protected boolean showRulers = SHOW_RULERS_EDEFAULT;

	/**
	 * The default value of the '{@link #isSnapToGeometry() <em>Snap To Geometry</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSnapToGeometry()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SNAP_TO_GEOMETRY_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isSnapToGeometry() <em>Snap To Geometry</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSnapToGeometry()
	 * @generated
	 * @ordered
	 */
	protected boolean snapToGeometry = SNAP_TO_GEOMETRY_EDEFAULT;

	/**
	 * The default value of the '{@link #isSnapToGrid() <em>Snap To Grid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSnapToGrid()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SNAP_TO_GRID_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isSnapToGrid() <em>Snap To Grid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSnapToGrid()
	 * @generated
	 * @ordered
	 */
	protected boolean snapToGrid = SNAP_TO_GRID_EDEFAULT;

	/**
	 * The default value of the '{@link #isSnapToGuides() <em>Snap To Guides</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSnapToGuides()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SNAP_TO_GUIDES_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isSnapToGuides() <em>Snap To Guides</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSnapToGuides()
	 * @generated
	 * @ordered
	 */
	protected boolean snapToGuides = SNAP_TO_GUIDES_EDEFAULT;

	/**
	 * The default value of the '{@link #getUnit() <em>Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnit()
	 * @generated
	 * @ordered
	 */
	protected static final Unit UNIT_EDEFAULT = Unit.CENTIMETERS;

	/**
	 * The cached value of the '{@link #getUnit() <em>Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnit()
	 * @generated
	 * @ordered
	 */
	protected Unit unit = UNIT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getVerticalRuler() <em>Vertical Ruler</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVerticalRuler()
	 * @generated
	 * @ordered
	 */
	protected Ruler verticalRuler;

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
	 * The cached value of the '{@link #getRulers() <em>Rulers</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRulers()
	 * @generated
	 * @ordered
	 */
	protected EList<Ruler> rulers;

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
	public EList<ConnectionLabel> getConnectionLabels() {
		if (connectionLabels == null) {
			connectionLabels = new EObjectContainmentEList<ConnectionLabel>(ConnectionLabel.class, this, SchemaPackage.DIAGRAM_DATA__CONNECTION_LABELS);
		}
		return connectionLabels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ConnectionPart> getConnectionParts() {
		if (connectionParts == null) {
			connectionParts = new EObjectContainmentEList<ConnectionPart>(ConnectionPart.class, this, SchemaPackage.DIAGRAM_DATA__CONNECTION_PARTS);
		}
		return connectionParts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Connector> getConnectors() {
		if (connectors == null) {
			connectors = new EObjectContainmentEList<Connector>(Connector.class, this, SchemaPackage.DIAGRAM_DATA__CONNECTORS);
		}
		return connectors;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Ruler getHorizontalRuler() {
		if (horizontalRuler != null && horizontalRuler.eIsProxy()) {
			InternalEObject oldHorizontalRuler = (InternalEObject)horizontalRuler;
			horizontalRuler = (Ruler)eResolveProxy(oldHorizontalRuler);
			if (horizontalRuler != oldHorizontalRuler) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.DIAGRAM_DATA__HORIZONTAL_RULER, oldHorizontalRuler, horizontalRuler));
			}
		}
		return horizontalRuler;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Ruler basicGetHorizontalRuler() {
		return horizontalRuler;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHorizontalRuler(Ruler newHorizontalRuler) {
		Ruler oldHorizontalRuler = horizontalRuler;
		horizontalRuler = newHorizontalRuler;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.DIAGRAM_DATA__HORIZONTAL_RULER, oldHorizontalRuler, horizontalRuler));
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
	public EList<Ruler> getRulers() {
		if (rulers == null) {
			rulers = new EObjectContainmentWithInverseEList<Ruler>(Ruler.class, this, SchemaPackage.DIAGRAM_DATA__RULERS, SchemaPackage.RULER__DIAGRAM_DATA);
		}
		return rulers;
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
			case SchemaPackage.DIAGRAM_DATA__RULERS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getRulers()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
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
	public boolean isShowRulers() {
		return showRulers;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setShowRulers(boolean newShowRulers) {
		boolean oldShowRulers = showRulers;
		showRulers = newShowRulers;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.DIAGRAM_DATA__SHOW_RULERS, oldShowRulers, showRulers));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSnapToGeometry() {
		return snapToGeometry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSnapToGeometry(boolean newSnapToGeometry) {
		boolean oldSnapToGeometry = snapToGeometry;
		snapToGeometry = newSnapToGeometry;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.DIAGRAM_DATA__SNAP_TO_GEOMETRY, oldSnapToGeometry, snapToGeometry));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSnapToGrid() {
		return snapToGrid;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSnapToGrid(boolean newSnapToGrid) {
		boolean oldSnapToGrid = snapToGrid;
		snapToGrid = newSnapToGrid;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.DIAGRAM_DATA__SNAP_TO_GRID, oldSnapToGrid, snapToGrid));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSnapToGuides() {
		return snapToGuides;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSnapToGuides(boolean newSnapToGuides) {
		boolean oldSnapToGuides = snapToGuides;
		snapToGuides = newSnapToGuides;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.DIAGRAM_DATA__SNAP_TO_GUIDES, oldSnapToGuides, snapToGuides));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Unit getUnit() {
		return unit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUnit(Unit newUnit) {
		Unit oldUnit = unit;
		unit = newUnit == null ? UNIT_EDEFAULT : newUnit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.DIAGRAM_DATA__UNIT, oldUnit, unit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Ruler getVerticalRuler() {
		if (verticalRuler != null && verticalRuler.eIsProxy()) {
			InternalEObject oldVerticalRuler = (InternalEObject)verticalRuler;
			verticalRuler = (Ruler)eResolveProxy(oldVerticalRuler);
			if (verticalRuler != oldVerticalRuler) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.DIAGRAM_DATA__VERTICAL_RULER, oldVerticalRuler, verticalRuler));
			}
		}
		return verticalRuler;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Ruler basicGetVerticalRuler() {
		return verticalRuler;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVerticalRuler(Ruler newVerticalRuler) {
		Ruler oldVerticalRuler = verticalRuler;
		verticalRuler = newVerticalRuler;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.DIAGRAM_DATA__VERTICAL_RULER, oldVerticalRuler, verticalRuler));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.DIAGRAM_DATA__CONNECTION_LABELS:
				return ((InternalEList<?>)getConnectionLabels()).basicRemove(otherEnd, msgs);
			case SchemaPackage.DIAGRAM_DATA__CONNECTION_PARTS:
				return ((InternalEList<?>)getConnectionParts()).basicRemove(otherEnd, msgs);
			case SchemaPackage.DIAGRAM_DATA__CONNECTORS:
				return ((InternalEList<?>)getConnectors()).basicRemove(otherEnd, msgs);
			case SchemaPackage.DIAGRAM_DATA__LOCATIONS:
				return ((InternalEList<?>)getLocations()).basicRemove(otherEnd, msgs);
			case SchemaPackage.DIAGRAM_DATA__RULERS:
				return ((InternalEList<?>)getRulers()).basicRemove(otherEnd, msgs);
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
			case SchemaPackage.DIAGRAM_DATA__CONNECTION_LABELS:
				return getConnectionLabels();
			case SchemaPackage.DIAGRAM_DATA__CONNECTION_PARTS:
				return getConnectionParts();
			case SchemaPackage.DIAGRAM_DATA__CONNECTORS:
				return getConnectors();
			case SchemaPackage.DIAGRAM_DATA__HORIZONTAL_RULER:
				if (resolve) return getHorizontalRuler();
				return basicGetHorizontalRuler();
			case SchemaPackage.DIAGRAM_DATA__LOCATIONS:
				return getLocations();
			case SchemaPackage.DIAGRAM_DATA__SHOW_GRID:
				return isShowGrid();
			case SchemaPackage.DIAGRAM_DATA__SHOW_RULERS:
				return isShowRulers();
			case SchemaPackage.DIAGRAM_DATA__SNAP_TO_GEOMETRY:
				return isSnapToGeometry();
			case SchemaPackage.DIAGRAM_DATA__SNAP_TO_GRID:
				return isSnapToGrid();
			case SchemaPackage.DIAGRAM_DATA__SNAP_TO_GUIDES:
				return isSnapToGuides();
			case SchemaPackage.DIAGRAM_DATA__UNIT:
				return getUnit();
			case SchemaPackage.DIAGRAM_DATA__VERTICAL_RULER:
				if (resolve) return getVerticalRuler();
				return basicGetVerticalRuler();
			case SchemaPackage.DIAGRAM_DATA__ZOOM_LEVEL:
				return getZoomLevel();
			case SchemaPackage.DIAGRAM_DATA__RULERS:
				return getRulers();
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
			case SchemaPackage.DIAGRAM_DATA__CONNECTION_LABELS:
				getConnectionLabels().clear();
				getConnectionLabels().addAll((Collection<? extends ConnectionLabel>)newValue);
				return;
			case SchemaPackage.DIAGRAM_DATA__CONNECTION_PARTS:
				getConnectionParts().clear();
				getConnectionParts().addAll((Collection<? extends ConnectionPart>)newValue);
				return;
			case SchemaPackage.DIAGRAM_DATA__CONNECTORS:
				getConnectors().clear();
				getConnectors().addAll((Collection<? extends Connector>)newValue);
				return;
			case SchemaPackage.DIAGRAM_DATA__HORIZONTAL_RULER:
				setHorizontalRuler((Ruler)newValue);
				return;
			case SchemaPackage.DIAGRAM_DATA__LOCATIONS:
				getLocations().clear();
				getLocations().addAll((Collection<? extends DiagramLocation>)newValue);
				return;
			case SchemaPackage.DIAGRAM_DATA__SHOW_GRID:
				setShowGrid((Boolean)newValue);
				return;
			case SchemaPackage.DIAGRAM_DATA__SHOW_RULERS:
				setShowRulers((Boolean)newValue);
				return;
			case SchemaPackage.DIAGRAM_DATA__SNAP_TO_GEOMETRY:
				setSnapToGeometry((Boolean)newValue);
				return;
			case SchemaPackage.DIAGRAM_DATA__SNAP_TO_GRID:
				setSnapToGrid((Boolean)newValue);
				return;
			case SchemaPackage.DIAGRAM_DATA__SNAP_TO_GUIDES:
				setSnapToGuides((Boolean)newValue);
				return;
			case SchemaPackage.DIAGRAM_DATA__UNIT:
				setUnit((Unit)newValue);
				return;
			case SchemaPackage.DIAGRAM_DATA__VERTICAL_RULER:
				setVerticalRuler((Ruler)newValue);
				return;
			case SchemaPackage.DIAGRAM_DATA__ZOOM_LEVEL:
				setZoomLevel((Double)newValue);
				return;
			case SchemaPackage.DIAGRAM_DATA__RULERS:
				getRulers().clear();
				getRulers().addAll((Collection<? extends Ruler>)newValue);
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
			case SchemaPackage.DIAGRAM_DATA__CONNECTION_LABELS:
				getConnectionLabels().clear();
				return;
			case SchemaPackage.DIAGRAM_DATA__CONNECTION_PARTS:
				getConnectionParts().clear();
				return;
			case SchemaPackage.DIAGRAM_DATA__CONNECTORS:
				getConnectors().clear();
				return;
			case SchemaPackage.DIAGRAM_DATA__HORIZONTAL_RULER:
				setHorizontalRuler((Ruler)null);
				return;
			case SchemaPackage.DIAGRAM_DATA__LOCATIONS:
				getLocations().clear();
				return;
			case SchemaPackage.DIAGRAM_DATA__SHOW_GRID:
				setShowGrid(SHOW_GRID_EDEFAULT);
				return;
			case SchemaPackage.DIAGRAM_DATA__SHOW_RULERS:
				setShowRulers(SHOW_RULERS_EDEFAULT);
				return;
			case SchemaPackage.DIAGRAM_DATA__SNAP_TO_GEOMETRY:
				setSnapToGeometry(SNAP_TO_GEOMETRY_EDEFAULT);
				return;
			case SchemaPackage.DIAGRAM_DATA__SNAP_TO_GRID:
				setSnapToGrid(SNAP_TO_GRID_EDEFAULT);
				return;
			case SchemaPackage.DIAGRAM_DATA__SNAP_TO_GUIDES:
				setSnapToGuides(SNAP_TO_GUIDES_EDEFAULT);
				return;
			case SchemaPackage.DIAGRAM_DATA__UNIT:
				setUnit(UNIT_EDEFAULT);
				return;
			case SchemaPackage.DIAGRAM_DATA__VERTICAL_RULER:
				setVerticalRuler((Ruler)null);
				return;
			case SchemaPackage.DIAGRAM_DATA__ZOOM_LEVEL:
				setZoomLevel(ZOOM_LEVEL_EDEFAULT);
				return;
			case SchemaPackage.DIAGRAM_DATA__RULERS:
				getRulers().clear();
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
			case SchemaPackage.DIAGRAM_DATA__CONNECTION_LABELS:
				return connectionLabels != null && !connectionLabels.isEmpty();
			case SchemaPackage.DIAGRAM_DATA__CONNECTION_PARTS:
				return connectionParts != null && !connectionParts.isEmpty();
			case SchemaPackage.DIAGRAM_DATA__CONNECTORS:
				return connectors != null && !connectors.isEmpty();
			case SchemaPackage.DIAGRAM_DATA__HORIZONTAL_RULER:
				return horizontalRuler != null;
			case SchemaPackage.DIAGRAM_DATA__LOCATIONS:
				return locations != null && !locations.isEmpty();
			case SchemaPackage.DIAGRAM_DATA__SHOW_GRID:
				return showGrid != SHOW_GRID_EDEFAULT;
			case SchemaPackage.DIAGRAM_DATA__SHOW_RULERS:
				return showRulers != SHOW_RULERS_EDEFAULT;
			case SchemaPackage.DIAGRAM_DATA__SNAP_TO_GEOMETRY:
				return snapToGeometry != SNAP_TO_GEOMETRY_EDEFAULT;
			case SchemaPackage.DIAGRAM_DATA__SNAP_TO_GRID:
				return snapToGrid != SNAP_TO_GRID_EDEFAULT;
			case SchemaPackage.DIAGRAM_DATA__SNAP_TO_GUIDES:
				return snapToGuides != SNAP_TO_GUIDES_EDEFAULT;
			case SchemaPackage.DIAGRAM_DATA__UNIT:
				return unit != UNIT_EDEFAULT;
			case SchemaPackage.DIAGRAM_DATA__VERTICAL_RULER:
				return verticalRuler != null;
			case SchemaPackage.DIAGRAM_DATA__ZOOM_LEVEL:
				return zoomLevel != ZOOM_LEVEL_EDEFAULT;
			case SchemaPackage.DIAGRAM_DATA__RULERS:
				return rulers != null && !rulers.isEmpty();
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
		result.append(" (showGrid: ");
		result.append(showGrid);
		result.append(", showRulers: ");
		result.append(showRulers);
		result.append(", snapToGeometry: ");
		result.append(snapToGeometry);
		result.append(", snapToGrid: ");
		result.append(snapToGrid);
		result.append(", snapToGuides: ");
		result.append(snapToGuides);
		result.append(", unit: ");
		result.append(unit);
		result.append(", zoomLevel: ");
		result.append(zoomLevel);
		result.append(')');
		return result.toString();
	}

} //DiagramDataImpl
