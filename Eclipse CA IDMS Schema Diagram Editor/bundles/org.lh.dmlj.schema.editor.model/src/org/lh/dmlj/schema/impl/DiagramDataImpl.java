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

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.Ruler;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Diagram Data</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#getConnectionLabels <em>Connection Labels</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#getConnectionParts <em>Connection Parts</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#getConnectors <em>Connectors</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#getHorizontalRuler <em>Horizontal Ruler</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#getLabel <em>Label</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#getLocations <em>Locations</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#getRulers <em>Rulers</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#getSchema <em>Schema</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#isShowGrid <em>Show Grid</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#isShowRulers <em>Show Rulers</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#isSnapToGeometry <em>Snap To Geometry</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#isSnapToGrid <em>Snap To Grid</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#isSnapToGuides <em>Snap To Guides</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#getVerticalRuler <em>Vertical Ruler</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.DiagramDataImpl#getZoomLevel <em>Zoom Level</em>}</li>
 * </ul>
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
	 * The cached value of the '{@link #getLabel() <em>Label</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected DiagramLabel label;

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
	 * The cached value of the '{@link #getRulers() <em>Rulers</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRulers()
	 * @generated
	 * @ordered
	 */
	protected EList<Ruler> rulers;

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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
	public DiagramLabel getLabel() {
		return label;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLabel(DiagramLabel newLabel, NotificationChain msgs) {
		DiagramLabel oldLabel = label;
		label = newLabel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.DIAGRAM_DATA__LABEL, oldLabel, newLabel);
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
	public void setLabel(DiagramLabel newLabel) {
		if (newLabel != label) {
			NotificationChain msgs = null;
			if (label != null)
				msgs = ((InternalEObject)label).eInverseRemove(this, SchemaPackage.DIAGRAM_LABEL__DIAGRAM_DATA, DiagramLabel.class, msgs);
			if (newLabel != null)
				msgs = ((InternalEObject)newLabel).eInverseAdd(this, SchemaPackage.DIAGRAM_LABEL__DIAGRAM_DATA, DiagramLabel.class, msgs);
			msgs = basicSetLabel(newLabel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.DIAGRAM_DATA__LABEL, newLabel, newLabel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public double getZoomLevel() {
		return zoomLevel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
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
	@Override
	public Schema getSchema() {
		if (eContainerFeatureID() != SchemaPackage.DIAGRAM_DATA__SCHEMA) return null;
		return (Schema)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSchema(Schema newSchema, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newSchema, SchemaPackage.DIAGRAM_DATA__SCHEMA, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSchema(Schema newSchema) {
		if (newSchema != eInternalContainer() || (eContainerFeatureID() != SchemaPackage.DIAGRAM_DATA__SCHEMA && newSchema != null)) {
			if (EcoreUtil.isAncestor(this, newSchema))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newSchema != null)
				msgs = ((InternalEObject)newSchema).eInverseAdd(this, SchemaPackage.SCHEMA__DIAGRAM_DATA, Schema.class, msgs);
			msgs = basicSetSchema(newSchema, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.DIAGRAM_DATA__SCHEMA, newSchema, newSchema));
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
			case SchemaPackage.DIAGRAM_DATA__LABEL:
				if (label != null)
					msgs = ((InternalEObject)label).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.DIAGRAM_DATA__LABEL, null, msgs);
				return basicSetLabel((DiagramLabel)otherEnd, msgs);
			case SchemaPackage.DIAGRAM_DATA__RULERS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getRulers()).basicAdd(otherEnd, msgs);
			case SchemaPackage.DIAGRAM_DATA__SCHEMA:
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
	public boolean isShowGrid() {
		return showGrid;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	public boolean isShowRulers() {
		return showRulers;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public boolean isSnapToGeometry() {
		return snapToGeometry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public boolean isSnapToGrid() {
		return snapToGrid;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public boolean isSnapToGuides() {
		return snapToGuides;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
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
	@Override
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
			case SchemaPackage.DIAGRAM_DATA__LABEL:
				return basicSetLabel(null, msgs);
			case SchemaPackage.DIAGRAM_DATA__LOCATIONS:
				return ((InternalEList<?>)getLocations()).basicRemove(otherEnd, msgs);
			case SchemaPackage.DIAGRAM_DATA__RULERS:
				return ((InternalEList<?>)getRulers()).basicRemove(otherEnd, msgs);
			case SchemaPackage.DIAGRAM_DATA__SCHEMA:
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
			case SchemaPackage.DIAGRAM_DATA__SCHEMA:
				return eInternalContainer().eInverseRemove(this, SchemaPackage.SCHEMA__DIAGRAM_DATA, Schema.class, msgs);
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
			case SchemaPackage.DIAGRAM_DATA__CONNECTION_LABELS:
				return getConnectionLabels();
			case SchemaPackage.DIAGRAM_DATA__CONNECTION_PARTS:
				return getConnectionParts();
			case SchemaPackage.DIAGRAM_DATA__CONNECTORS:
				return getConnectors();
			case SchemaPackage.DIAGRAM_DATA__HORIZONTAL_RULER:
				if (resolve) return getHorizontalRuler();
				return basicGetHorizontalRuler();
			case SchemaPackage.DIAGRAM_DATA__LABEL:
				return getLabel();
			case SchemaPackage.DIAGRAM_DATA__LOCATIONS:
				return getLocations();
			case SchemaPackage.DIAGRAM_DATA__RULERS:
				return getRulers();
			case SchemaPackage.DIAGRAM_DATA__SCHEMA:
				return getSchema();
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
			case SchemaPackage.DIAGRAM_DATA__VERTICAL_RULER:
				if (resolve) return getVerticalRuler();
				return basicGetVerticalRuler();
			case SchemaPackage.DIAGRAM_DATA__ZOOM_LEVEL:
				return getZoomLevel();
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
			case SchemaPackage.DIAGRAM_DATA__LABEL:
				setLabel((DiagramLabel)newValue);
				return;
			case SchemaPackage.DIAGRAM_DATA__LOCATIONS:
				getLocations().clear();
				getLocations().addAll((Collection<? extends DiagramLocation>)newValue);
				return;
			case SchemaPackage.DIAGRAM_DATA__RULERS:
				getRulers().clear();
				getRulers().addAll((Collection<? extends Ruler>)newValue);
				return;
			case SchemaPackage.DIAGRAM_DATA__SCHEMA:
				setSchema((Schema)newValue);
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
			case SchemaPackage.DIAGRAM_DATA__VERTICAL_RULER:
				setVerticalRuler((Ruler)newValue);
				return;
			case SchemaPackage.DIAGRAM_DATA__ZOOM_LEVEL:
				setZoomLevel((Double)newValue);
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
			case SchemaPackage.DIAGRAM_DATA__LABEL:
				setLabel((DiagramLabel)null);
				return;
			case SchemaPackage.DIAGRAM_DATA__LOCATIONS:
				getLocations().clear();
				return;
			case SchemaPackage.DIAGRAM_DATA__RULERS:
				getRulers().clear();
				return;
			case SchemaPackage.DIAGRAM_DATA__SCHEMA:
				setSchema((Schema)null);
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
			case SchemaPackage.DIAGRAM_DATA__VERTICAL_RULER:
				setVerticalRuler((Ruler)null);
				return;
			case SchemaPackage.DIAGRAM_DATA__ZOOM_LEVEL:
				setZoomLevel(ZOOM_LEVEL_EDEFAULT);
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
			case SchemaPackage.DIAGRAM_DATA__LABEL:
				return label != null;
			case SchemaPackage.DIAGRAM_DATA__LOCATIONS:
				return locations != null && !locations.isEmpty();
			case SchemaPackage.DIAGRAM_DATA__RULERS:
				return rulers != null && !rulers.isEmpty();
			case SchemaPackage.DIAGRAM_DATA__SCHEMA:
				return getSchema() != null;
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
			case SchemaPackage.DIAGRAM_DATA__VERTICAL_RULER:
				return verticalRuler != null;
			case SchemaPackage.DIAGRAM_DATA__ZOOM_LEVEL:
				return zoomLevel != ZOOM_LEVEL_EDEFAULT;
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
		result.append(", zoomLevel: ");
		result.append(zoomLevel);
		result.append(')');
		return result.toString();
	}

} //DiagramDataImpl
