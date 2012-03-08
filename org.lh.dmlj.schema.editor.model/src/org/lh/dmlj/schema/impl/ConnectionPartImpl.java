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

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Connection Part</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.ConnectionPartImpl#getConnector <em>Connector</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.ConnectionPartImpl#getBendpointLocations <em>Bendpoint Locations</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.ConnectionPartImpl#getMemberRole <em>Member Role</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.ConnectionPartImpl#getSourceEndpointLocation <em>Source Endpoint Location</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.ConnectionPartImpl#getTargetEndpointLocation <em>Target Endpoint Location</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ConnectionPartImpl extends EObjectImpl implements ConnectionPart {
	/**
	 * The cached value of the '{@link #getConnector() <em>Connector</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConnector()
	 * @generated
	 * @ordered
	 */
	protected Connector connector;

	/**
	 * The cached value of the '{@link #getBendpointLocations() <em>Bendpoint Locations</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBendpointLocations()
	 * @generated
	 * @ordered
	 */
	protected EList<DiagramLocation> bendpointLocations;

	/**
	 * The cached value of the '{@link #getMemberRole() <em>Member Role</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMemberRole()
	 * @generated
	 * @ordered
	 */
	protected MemberRole memberRole;

	/**
	 * The cached value of the '{@link #getSourceEndpointLocation() <em>Source Endpoint Location</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSourceEndpointLocation()
	 * @generated
	 * @ordered
	 */
	protected DiagramLocation sourceEndpointLocation;

	/**
	 * The cached value of the '{@link #getTargetEndpointLocation() <em>Target Endpoint Location</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetEndpointLocation()
	 * @generated
	 * @ordered
	 */
	protected DiagramLocation targetEndpointLocation;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ConnectionPartImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.CONNECTION_PART;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Connector getConnector() {
		if (connector != null && connector.eIsProxy()) {
			InternalEObject oldConnector = (InternalEObject)connector;
			connector = (Connector)eResolveProxy(oldConnector);
			if (connector != oldConnector) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.CONNECTION_PART__CONNECTOR, oldConnector, connector));
			}
		}
		return connector;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Connector basicGetConnector() {
		return connector;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetConnector(Connector newConnector, NotificationChain msgs) {
		Connector oldConnector = connector;
		connector = newConnector;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.CONNECTION_PART__CONNECTOR, oldConnector, newConnector);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConnector(Connector newConnector) {
		if (newConnector != connector) {
			NotificationChain msgs = null;
			if (connector != null)
				msgs = ((InternalEObject)connector).eInverseRemove(this, SchemaPackage.CONNECTOR__CONNECTION_PART, Connector.class, msgs);
			if (newConnector != null)
				msgs = ((InternalEObject)newConnector).eInverseAdd(this, SchemaPackage.CONNECTOR__CONNECTION_PART, Connector.class, msgs);
			msgs = basicSetConnector(newConnector, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.CONNECTION_PART__CONNECTOR, newConnector, newConnector));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<DiagramLocation> getBendpointLocations() {
		if (bendpointLocations == null) {
			bendpointLocations = new EObjectResolvingEList<DiagramLocation>(DiagramLocation.class, this, SchemaPackage.CONNECTION_PART__BENDPOINT_LOCATIONS);
		}
		return bendpointLocations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MemberRole getMemberRole() {
		if (memberRole != null && memberRole.eIsProxy()) {
			InternalEObject oldMemberRole = (InternalEObject)memberRole;
			memberRole = (MemberRole)eResolveProxy(oldMemberRole);
			if (memberRole != oldMemberRole) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.CONNECTION_PART__MEMBER_ROLE, oldMemberRole, memberRole));
			}
		}
		return memberRole;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MemberRole basicGetMemberRole() {
		return memberRole;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMemberRole(MemberRole newMemberRole, NotificationChain msgs) {
		MemberRole oldMemberRole = memberRole;
		memberRole = newMemberRole;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.CONNECTION_PART__MEMBER_ROLE, oldMemberRole, newMemberRole);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMemberRole(MemberRole newMemberRole) {
		if (newMemberRole != memberRole) {
			NotificationChain msgs = null;
			if (memberRole != null)
				msgs = ((InternalEObject)memberRole).eInverseRemove(this, SchemaPackage.MEMBER_ROLE__CONNECTION_PARTS, MemberRole.class, msgs);
			if (newMemberRole != null)
				msgs = ((InternalEObject)newMemberRole).eInverseAdd(this, SchemaPackage.MEMBER_ROLE__CONNECTION_PARTS, MemberRole.class, msgs);
			msgs = basicSetMemberRole(newMemberRole, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.CONNECTION_PART__MEMBER_ROLE, newMemberRole, newMemberRole));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramLocation getSourceEndpointLocation() {
		if (sourceEndpointLocation != null && sourceEndpointLocation.eIsProxy()) {
			InternalEObject oldSourceEndpointLocation = (InternalEObject)sourceEndpointLocation;
			sourceEndpointLocation = (DiagramLocation)eResolveProxy(oldSourceEndpointLocation);
			if (sourceEndpointLocation != oldSourceEndpointLocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.CONNECTION_PART__SOURCE_ENDPOINT_LOCATION, oldSourceEndpointLocation, sourceEndpointLocation));
			}
		}
		return sourceEndpointLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramLocation basicGetSourceEndpointLocation() {
		return sourceEndpointLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSourceEndpointLocation(DiagramLocation newSourceEndpointLocation) {
		DiagramLocation oldSourceEndpointLocation = sourceEndpointLocation;
		sourceEndpointLocation = newSourceEndpointLocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.CONNECTION_PART__SOURCE_ENDPOINT_LOCATION, oldSourceEndpointLocation, sourceEndpointLocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramLocation getTargetEndpointLocation() {
		if (targetEndpointLocation != null && targetEndpointLocation.eIsProxy()) {
			InternalEObject oldTargetEndpointLocation = (InternalEObject)targetEndpointLocation;
			targetEndpointLocation = (DiagramLocation)eResolveProxy(oldTargetEndpointLocation);
			if (targetEndpointLocation != oldTargetEndpointLocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.CONNECTION_PART__TARGET_ENDPOINT_LOCATION, oldTargetEndpointLocation, targetEndpointLocation));
			}
		}
		return targetEndpointLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramLocation basicGetTargetEndpointLocation() {
		return targetEndpointLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTargetEndpointLocation(DiagramLocation newTargetEndpointLocation) {
		DiagramLocation oldTargetEndpointLocation = targetEndpointLocation;
		targetEndpointLocation = newTargetEndpointLocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.CONNECTION_PART__TARGET_ENDPOINT_LOCATION, oldTargetEndpointLocation, targetEndpointLocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.CONNECTION_PART__CONNECTOR:
				if (connector != null)
					msgs = ((InternalEObject)connector).eInverseRemove(this, SchemaPackage.CONNECTOR__CONNECTION_PART, Connector.class, msgs);
				return basicSetConnector((Connector)otherEnd, msgs);
			case SchemaPackage.CONNECTION_PART__MEMBER_ROLE:
				if (memberRole != null)
					msgs = ((InternalEObject)memberRole).eInverseRemove(this, SchemaPackage.MEMBER_ROLE__CONNECTION_PARTS, MemberRole.class, msgs);
				return basicSetMemberRole((MemberRole)otherEnd, msgs);
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
			case SchemaPackage.CONNECTION_PART__CONNECTOR:
				return basicSetConnector(null, msgs);
			case SchemaPackage.CONNECTION_PART__MEMBER_ROLE:
				return basicSetMemberRole(null, msgs);
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
			case SchemaPackage.CONNECTION_PART__CONNECTOR:
				if (resolve) return getConnector();
				return basicGetConnector();
			case SchemaPackage.CONNECTION_PART__BENDPOINT_LOCATIONS:
				return getBendpointLocations();
			case SchemaPackage.CONNECTION_PART__MEMBER_ROLE:
				if (resolve) return getMemberRole();
				return basicGetMemberRole();
			case SchemaPackage.CONNECTION_PART__SOURCE_ENDPOINT_LOCATION:
				if (resolve) return getSourceEndpointLocation();
				return basicGetSourceEndpointLocation();
			case SchemaPackage.CONNECTION_PART__TARGET_ENDPOINT_LOCATION:
				if (resolve) return getTargetEndpointLocation();
				return basicGetTargetEndpointLocation();
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
			case SchemaPackage.CONNECTION_PART__CONNECTOR:
				setConnector((Connector)newValue);
				return;
			case SchemaPackage.CONNECTION_PART__BENDPOINT_LOCATIONS:
				getBendpointLocations().clear();
				getBendpointLocations().addAll((Collection<? extends DiagramLocation>)newValue);
				return;
			case SchemaPackage.CONNECTION_PART__MEMBER_ROLE:
				setMemberRole((MemberRole)newValue);
				return;
			case SchemaPackage.CONNECTION_PART__SOURCE_ENDPOINT_LOCATION:
				setSourceEndpointLocation((DiagramLocation)newValue);
				return;
			case SchemaPackage.CONNECTION_PART__TARGET_ENDPOINT_LOCATION:
				setTargetEndpointLocation((DiagramLocation)newValue);
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
			case SchemaPackage.CONNECTION_PART__CONNECTOR:
				setConnector((Connector)null);
				return;
			case SchemaPackage.CONNECTION_PART__BENDPOINT_LOCATIONS:
				getBendpointLocations().clear();
				return;
			case SchemaPackage.CONNECTION_PART__MEMBER_ROLE:
				setMemberRole((MemberRole)null);
				return;
			case SchemaPackage.CONNECTION_PART__SOURCE_ENDPOINT_LOCATION:
				setSourceEndpointLocation((DiagramLocation)null);
				return;
			case SchemaPackage.CONNECTION_PART__TARGET_ENDPOINT_LOCATION:
				setTargetEndpointLocation((DiagramLocation)null);
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
			case SchemaPackage.CONNECTION_PART__CONNECTOR:
				return connector != null;
			case SchemaPackage.CONNECTION_PART__BENDPOINT_LOCATIONS:
				return bendpointLocations != null && !bendpointLocations.isEmpty();
			case SchemaPackage.CONNECTION_PART__MEMBER_ROLE:
				return memberRole != null;
			case SchemaPackage.CONNECTION_PART__SOURCE_ENDPOINT_LOCATION:
				return sourceEndpointLocation != null;
			case SchemaPackage.CONNECTION_PART__TARGET_ENDPOINT_LOCATION:
				return targetEndpointLocation != null;
		}
		return super.eIsSet(featureID);
	}

} //ConnectionPartImpl
