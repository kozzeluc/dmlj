/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.lh.dmlj.schema.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.LabelAlignment;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Connection Label</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.ConnectionLabelImpl#getAlignment <em>Alignment</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.ConnectionLabelImpl#getLocation <em>Location</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.ConnectionLabelImpl#getMemberRole <em>Member Role</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ConnectionLabelImpl extends DiagramNodeImpl implements ConnectionLabel {
	/**
	 * The default value of the '{@link #getAlignment() <em>Alignment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAlignment()
	 * @generated
	 * @ordered
	 */
	protected static final LabelAlignment ALIGNMENT_EDEFAULT = LabelAlignment.LEFT;

	/**
	 * The cached value of the '{@link #getAlignment() <em>Alignment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAlignment()
	 * @generated
	 * @ordered
	 */
	protected LabelAlignment alignment = ALIGNMENT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getLocation() <em>Location</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocation()
	 * @generated
	 * @ordered
	 */
	protected DiagramLocation location;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ConnectionLabelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.CONNECTION_LABEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LabelAlignment getAlignment() {
		return alignment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAlignment(LabelAlignment newAlignment) {
		LabelAlignment oldAlignment = alignment;
		alignment = newAlignment == null ? ALIGNMENT_EDEFAULT : newAlignment;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.CONNECTION_LABEL__ALIGNMENT, oldAlignment, alignment));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramLocation getLocation() {
		if (location != null && location.eIsProxy()) {
			InternalEObject oldLocation = (InternalEObject)location;
			location = (DiagramLocation)eResolveProxy(oldLocation);
			if (location != oldLocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.CONNECTION_LABEL__LOCATION, oldLocation, location));
			}
		}
		return location;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramLocation basicGetLocation() {
		return location;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLocation(DiagramLocation newLocation) {
		DiagramLocation oldLocation = location;
		location = newLocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.CONNECTION_LABEL__LOCATION, oldLocation, location));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.CONNECTION_LABEL__MEMBER_ROLE, oldMemberRole, memberRole));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.CONNECTION_LABEL__MEMBER_ROLE, oldMemberRole, newMemberRole);
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
				msgs = ((InternalEObject)memberRole).eInverseRemove(this, SchemaPackage.MEMBER_ROLE__CONNECTION_LABEL, MemberRole.class, msgs);
			if (newMemberRole != null)
				msgs = ((InternalEObject)newMemberRole).eInverseAdd(this, SchemaPackage.MEMBER_ROLE__CONNECTION_LABEL, MemberRole.class, msgs);
			msgs = basicSetMemberRole(newMemberRole, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.CONNECTION_LABEL__MEMBER_ROLE, newMemberRole, newMemberRole));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.CONNECTION_LABEL__MEMBER_ROLE:
				if (memberRole != null)
					msgs = ((InternalEObject)memberRole).eInverseRemove(this, SchemaPackage.MEMBER_ROLE__CONNECTION_LABEL, MemberRole.class, msgs);
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
			case SchemaPackage.CONNECTION_LABEL__MEMBER_ROLE:
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
			case SchemaPackage.CONNECTION_LABEL__ALIGNMENT:
				return getAlignment();
			case SchemaPackage.CONNECTION_LABEL__LOCATION:
				if (resolve) return getLocation();
				return basicGetLocation();
			case SchemaPackage.CONNECTION_LABEL__MEMBER_ROLE:
				if (resolve) return getMemberRole();
				return basicGetMemberRole();
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
			case SchemaPackage.CONNECTION_LABEL__ALIGNMENT:
				setAlignment((LabelAlignment)newValue);
				return;
			case SchemaPackage.CONNECTION_LABEL__LOCATION:
				setLocation((DiagramLocation)newValue);
				return;
			case SchemaPackage.CONNECTION_LABEL__MEMBER_ROLE:
				setMemberRole((MemberRole)newValue);
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
			case SchemaPackage.CONNECTION_LABEL__ALIGNMENT:
				setAlignment(ALIGNMENT_EDEFAULT);
				return;
			case SchemaPackage.CONNECTION_LABEL__LOCATION:
				setLocation((DiagramLocation)null);
				return;
			case SchemaPackage.CONNECTION_LABEL__MEMBER_ROLE:
				setMemberRole((MemberRole)null);
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
			case SchemaPackage.CONNECTION_LABEL__ALIGNMENT:
				return alignment != ALIGNMENT_EDEFAULT;
			case SchemaPackage.CONNECTION_LABEL__LOCATION:
				return location != null;
			case SchemaPackage.CONNECTION_LABEL__MEMBER_ROLE:
				return memberRole != null;
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
		result.append(" (alignment: ");
		result.append(alignment);
		result.append(')');
		return result.toString();
	}

} //ConnectionLabelImpl
