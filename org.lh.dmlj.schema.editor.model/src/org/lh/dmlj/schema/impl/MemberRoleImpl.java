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
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMembershipOption;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Member Role</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.MemberRoleImpl#getDiagramBendpoints <em>Diagram Bendpoints</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.MemberRoleImpl#getDiagramLabelLocation <em>Diagram Label Location</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.MemberRoleImpl#getDiagramSourceAnchor <em>Diagram Source Anchor</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.MemberRoleImpl#getDiagramTargetAnchor <em>Diagram Target Anchor</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.MemberRoleImpl#getIndexDbkeyPosition <em>Index Dbkey Position</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.MemberRoleImpl#getMembershipOption <em>Membership Option</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.MemberRoleImpl#getNextDbkeyPosition <em>Next Dbkey Position</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.MemberRoleImpl#getPriorDbkeyPosition <em>Prior Dbkey Position</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.MemberRoleImpl#getOwnerDbkeyPosition <em>Owner Dbkey Position</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.MemberRoleImpl#getRecord <em>Record</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.MemberRoleImpl#getSet <em>Set</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.MemberRoleImpl#getSortKey <em>Sort Key</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MemberRoleImpl extends RoleImpl implements MemberRole {
	/**
	 * The cached value of the '{@link #getDiagramBendpoints() <em>Diagram Bendpoints</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDiagramBendpoints()
	 * @generated
	 * @ordered
	 */
	protected EList<DiagramLocation> diagramBendpoints;

	/**
	 * The cached value of the '{@link #getDiagramLabelLocation() <em>Diagram Label Location</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDiagramLabelLocation()
	 * @generated
	 * @ordered
	 */
	protected DiagramLocation diagramLabelLocation;

	/**
	 * The cached value of the '{@link #getDiagramSourceAnchor() <em>Diagram Source Anchor</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDiagramSourceAnchor()
	 * @generated
	 * @ordered
	 */
	protected DiagramLocation diagramSourceAnchor;

	/**
	 * The cached value of the '{@link #getDiagramTargetAnchor() <em>Diagram Target Anchor</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDiagramTargetAnchor()
	 * @generated
	 * @ordered
	 */
	protected DiagramLocation diagramTargetAnchor;

	/**
	 * The default value of the '{@link #getIndexDbkeyPosition() <em>Index Dbkey Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndexDbkeyPosition()
	 * @generated
	 * @ordered
	 */
	protected static final Short INDEX_DBKEY_POSITION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIndexDbkeyPosition() <em>Index Dbkey Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndexDbkeyPosition()
	 * @generated
	 * @ordered
	 */
	protected Short indexDbkeyPosition = INDEX_DBKEY_POSITION_EDEFAULT;

	/**
	 * The default value of the '{@link #getMembershipOption() <em>Membership Option</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMembershipOption()
	 * @generated
	 * @ordered
	 */
	protected static final SetMembershipOption MEMBERSHIP_OPTION_EDEFAULT = SetMembershipOption.MANDATORY_AUTOMATIC;

	/**
	 * The cached value of the '{@link #getMembershipOption() <em>Membership Option</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMembershipOption()
	 * @generated
	 * @ordered
	 */
	protected SetMembershipOption membershipOption = MEMBERSHIP_OPTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getNextDbkeyPosition() <em>Next Dbkey Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNextDbkeyPosition()
	 * @generated
	 * @ordered
	 */
	protected static final Short NEXT_DBKEY_POSITION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNextDbkeyPosition() <em>Next Dbkey Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNextDbkeyPosition()
	 * @generated
	 * @ordered
	 */
	protected Short nextDbkeyPosition = NEXT_DBKEY_POSITION_EDEFAULT;

	/**
	 * The default value of the '{@link #getPriorDbkeyPosition() <em>Prior Dbkey Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriorDbkeyPosition()
	 * @generated
	 * @ordered
	 */
	protected static final Short PRIOR_DBKEY_POSITION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPriorDbkeyPosition() <em>Prior Dbkey Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriorDbkeyPosition()
	 * @generated
	 * @ordered
	 */
	protected Short priorDbkeyPosition = PRIOR_DBKEY_POSITION_EDEFAULT;

	/**
	 * The default value of the '{@link #getOwnerDbkeyPosition() <em>Owner Dbkey Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnerDbkeyPosition()
	 * @generated
	 * @ordered
	 */
	protected static final Short OWNER_DBKEY_POSITION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOwnerDbkeyPosition() <em>Owner Dbkey Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnerDbkeyPosition()
	 * @generated
	 * @ordered
	 */
	protected Short ownerDbkeyPosition = OWNER_DBKEY_POSITION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getRecord() <em>Record</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRecord()
	 * @generated
	 * @ordered
	 */
	protected SchemaRecord record;

	/**
	 * The cached value of the '{@link #getSortKey() <em>Sort Key</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSortKey()
	 * @generated
	 * @ordered
	 */
	protected Key sortKey;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MemberRoleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.MEMBER_ROLE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Short getIndexDbkeyPosition() {
		return indexDbkeyPosition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIndexDbkeyPosition(Short newIndexDbkeyPosition) {
		Short oldIndexDbkeyPosition = indexDbkeyPosition;
		indexDbkeyPosition = newIndexDbkeyPosition;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.MEMBER_ROLE__INDEX_DBKEY_POSITION, oldIndexDbkeyPosition, indexDbkeyPosition));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SetMembershipOption getMembershipOption() {
		return membershipOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMembershipOption(SetMembershipOption newMembershipOption) {
		SetMembershipOption oldMembershipOption = membershipOption;
		membershipOption = newMembershipOption == null ? MEMBERSHIP_OPTION_EDEFAULT : newMembershipOption;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.MEMBER_ROLE__MEMBERSHIP_OPTION, oldMembershipOption, membershipOption));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Short getNextDbkeyPosition() {
		return nextDbkeyPosition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNextDbkeyPosition(Short newNextDbkeyPosition) {
		Short oldNextDbkeyPosition = nextDbkeyPosition;
		nextDbkeyPosition = newNextDbkeyPosition;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.MEMBER_ROLE__NEXT_DBKEY_POSITION, oldNextDbkeyPosition, nextDbkeyPosition));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Short getPriorDbkeyPosition() {
		return priorDbkeyPosition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPriorDbkeyPosition(Short newPriorDbkeyPosition) {
		Short oldPriorDbkeyPosition = priorDbkeyPosition;
		priorDbkeyPosition = newPriorDbkeyPosition;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.MEMBER_ROLE__PRIOR_DBKEY_POSITION, oldPriorDbkeyPosition, priorDbkeyPosition));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Short getOwnerDbkeyPosition() {
		return ownerDbkeyPosition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOwnerDbkeyPosition(Short newOwnerDbkeyPosition) {
		Short oldOwnerDbkeyPosition = ownerDbkeyPosition;
		ownerDbkeyPosition = newOwnerDbkeyPosition;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.MEMBER_ROLE__OWNER_DBKEY_POSITION, oldOwnerDbkeyPosition, ownerDbkeyPosition));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SchemaRecord getRecord() {
		if (record != null && record.eIsProxy()) {
			InternalEObject oldRecord = (InternalEObject)record;
			record = (SchemaRecord)eResolveProxy(oldRecord);
			if (record != oldRecord) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.MEMBER_ROLE__RECORD, oldRecord, record));
			}
		}
		return record;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SchemaRecord basicGetRecord() {
		return record;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRecord(SchemaRecord newRecord, NotificationChain msgs) {
		SchemaRecord oldRecord = record;
		record = newRecord;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.MEMBER_ROLE__RECORD, oldRecord, newRecord);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRecord(SchemaRecord newRecord) {
		if (newRecord != record) {
			NotificationChain msgs = null;
			if (record != null)
				msgs = ((InternalEObject)record).eInverseRemove(this, SchemaPackage.SCHEMA_RECORD__MEMBER_ROLES, SchemaRecord.class, msgs);
			if (newRecord != null)
				msgs = ((InternalEObject)newRecord).eInverseAdd(this, SchemaPackage.SCHEMA_RECORD__MEMBER_ROLES, SchemaRecord.class, msgs);
			msgs = basicSetRecord(newRecord, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.MEMBER_ROLE__RECORD, newRecord, newRecord));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Set getSet() {
		if (eContainerFeatureID() != SchemaPackage.MEMBER_ROLE__SET) return null;
		return (Set)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSet(Set newSet, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newSet, SchemaPackage.MEMBER_ROLE__SET, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSet(Set newSet) {
		if (newSet != eInternalContainer() || (eContainerFeatureID() != SchemaPackage.MEMBER_ROLE__SET && newSet != null)) {
			if (EcoreUtil.isAncestor(this, newSet))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newSet != null)
				msgs = ((InternalEObject)newSet).eInverseAdd(this, SchemaPackage.SET__MEMBERS, Set.class, msgs);
			msgs = basicSetSet(newSet, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.MEMBER_ROLE__SET, newSet, newSet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Key getSortKey() {
		if (sortKey != null && sortKey.eIsProxy()) {
			InternalEObject oldSortKey = (InternalEObject)sortKey;
			sortKey = (Key)eResolveProxy(oldSortKey);
			if (sortKey != oldSortKey) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.MEMBER_ROLE__SORT_KEY, oldSortKey, sortKey));
			}
		}
		return sortKey;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Key basicGetSortKey() {
		return sortKey;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSortKey(Key newSortKey, NotificationChain msgs) {
		Key oldSortKey = sortKey;
		sortKey = newSortKey;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.MEMBER_ROLE__SORT_KEY, oldSortKey, newSortKey);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSortKey(Key newSortKey) {
		if (newSortKey != sortKey) {
			NotificationChain msgs = null;
			if (sortKey != null)
				msgs = ((InternalEObject)sortKey).eInverseRemove(this, SchemaPackage.KEY__MEMBER_ROLE, Key.class, msgs);
			if (newSortKey != null)
				msgs = ((InternalEObject)newSortKey).eInverseAdd(this, SchemaPackage.KEY__MEMBER_ROLE, Key.class, msgs);
			msgs = basicSetSortKey(newSortKey, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.MEMBER_ROLE__SORT_KEY, newSortKey, newSortKey));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramLocation getDiagramTargetAnchor() {
		if (diagramTargetAnchor != null && diagramTargetAnchor.eIsProxy()) {
			InternalEObject oldDiagramTargetAnchor = (InternalEObject)diagramTargetAnchor;
			diagramTargetAnchor = (DiagramLocation)eResolveProxy(oldDiagramTargetAnchor);
			if (diagramTargetAnchor != oldDiagramTargetAnchor) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.MEMBER_ROLE__DIAGRAM_TARGET_ANCHOR, oldDiagramTargetAnchor, diagramTargetAnchor));
			}
		}
		return diagramTargetAnchor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramLocation basicGetDiagramTargetAnchor() {
		return diagramTargetAnchor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDiagramTargetAnchor(DiagramLocation newDiagramTargetAnchor) {
		DiagramLocation oldDiagramTargetAnchor = diagramTargetAnchor;
		diagramTargetAnchor = newDiagramTargetAnchor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.MEMBER_ROLE__DIAGRAM_TARGET_ANCHOR, oldDiagramTargetAnchor, diagramTargetAnchor));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramLocation getDiagramSourceAnchor() {
		if (diagramSourceAnchor != null && diagramSourceAnchor.eIsProxy()) {
			InternalEObject oldDiagramSourceAnchor = (InternalEObject)diagramSourceAnchor;
			diagramSourceAnchor = (DiagramLocation)eResolveProxy(oldDiagramSourceAnchor);
			if (diagramSourceAnchor != oldDiagramSourceAnchor) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.MEMBER_ROLE__DIAGRAM_SOURCE_ANCHOR, oldDiagramSourceAnchor, diagramSourceAnchor));
			}
		}
		return diagramSourceAnchor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramLocation basicGetDiagramSourceAnchor() {
		return diagramSourceAnchor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDiagramSourceAnchor(DiagramLocation newDiagramSourceAnchor) {
		DiagramLocation oldDiagramSourceAnchor = diagramSourceAnchor;
		diagramSourceAnchor = newDiagramSourceAnchor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.MEMBER_ROLE__DIAGRAM_SOURCE_ANCHOR, oldDiagramSourceAnchor, diagramSourceAnchor));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<DiagramLocation> getDiagramBendpoints() {
		if (diagramBendpoints == null) {
			diagramBendpoints = new EObjectResolvingEList<DiagramLocation>(DiagramLocation.class, this, SchemaPackage.MEMBER_ROLE__DIAGRAM_BENDPOINTS);
		}
		return diagramBendpoints;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramLocation getDiagramLabelLocation() {
		if (diagramLabelLocation != null && diagramLabelLocation.eIsProxy()) {
			InternalEObject oldDiagramLabelLocation = (InternalEObject)diagramLabelLocation;
			diagramLabelLocation = (DiagramLocation)eResolveProxy(oldDiagramLabelLocation);
			if (diagramLabelLocation != oldDiagramLabelLocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.MEMBER_ROLE__DIAGRAM_LABEL_LOCATION, oldDiagramLabelLocation, diagramLabelLocation));
			}
		}
		return diagramLabelLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramLocation basicGetDiagramLabelLocation() {
		return diagramLabelLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDiagramLabelLocation(DiagramLocation newDiagramLabelLocation) {
		DiagramLocation oldDiagramLabelLocation = diagramLabelLocation;
		diagramLabelLocation = newDiagramLabelLocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.MEMBER_ROLE__DIAGRAM_LABEL_LOCATION, oldDiagramLabelLocation, diagramLabelLocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.MEMBER_ROLE__RECORD:
				if (record != null)
					msgs = ((InternalEObject)record).eInverseRemove(this, SchemaPackage.SCHEMA_RECORD__MEMBER_ROLES, SchemaRecord.class, msgs);
				return basicSetRecord((SchemaRecord)otherEnd, msgs);
			case SchemaPackage.MEMBER_ROLE__SET:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetSet((Set)otherEnd, msgs);
			case SchemaPackage.MEMBER_ROLE__SORT_KEY:
				if (sortKey != null)
					msgs = ((InternalEObject)sortKey).eInverseRemove(this, SchemaPackage.KEY__MEMBER_ROLE, Key.class, msgs);
				return basicSetSortKey((Key)otherEnd, msgs);
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
			case SchemaPackage.MEMBER_ROLE__RECORD:
				return basicSetRecord(null, msgs);
			case SchemaPackage.MEMBER_ROLE__SET:
				return basicSetSet(null, msgs);
			case SchemaPackage.MEMBER_ROLE__SORT_KEY:
				return basicSetSortKey(null, msgs);
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
			case SchemaPackage.MEMBER_ROLE__SET:
				return eInternalContainer().eInverseRemove(this, SchemaPackage.SET__MEMBERS, Set.class, msgs);
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
			case SchemaPackage.MEMBER_ROLE__DIAGRAM_BENDPOINTS:
				return getDiagramBendpoints();
			case SchemaPackage.MEMBER_ROLE__DIAGRAM_LABEL_LOCATION:
				if (resolve) return getDiagramLabelLocation();
				return basicGetDiagramLabelLocation();
			case SchemaPackage.MEMBER_ROLE__DIAGRAM_SOURCE_ANCHOR:
				if (resolve) return getDiagramSourceAnchor();
				return basicGetDiagramSourceAnchor();
			case SchemaPackage.MEMBER_ROLE__DIAGRAM_TARGET_ANCHOR:
				if (resolve) return getDiagramTargetAnchor();
				return basicGetDiagramTargetAnchor();
			case SchemaPackage.MEMBER_ROLE__INDEX_DBKEY_POSITION:
				return getIndexDbkeyPosition();
			case SchemaPackage.MEMBER_ROLE__MEMBERSHIP_OPTION:
				return getMembershipOption();
			case SchemaPackage.MEMBER_ROLE__NEXT_DBKEY_POSITION:
				return getNextDbkeyPosition();
			case SchemaPackage.MEMBER_ROLE__PRIOR_DBKEY_POSITION:
				return getPriorDbkeyPosition();
			case SchemaPackage.MEMBER_ROLE__OWNER_DBKEY_POSITION:
				return getOwnerDbkeyPosition();
			case SchemaPackage.MEMBER_ROLE__RECORD:
				if (resolve) return getRecord();
				return basicGetRecord();
			case SchemaPackage.MEMBER_ROLE__SET:
				return getSet();
			case SchemaPackage.MEMBER_ROLE__SORT_KEY:
				if (resolve) return getSortKey();
				return basicGetSortKey();
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
			case SchemaPackage.MEMBER_ROLE__DIAGRAM_BENDPOINTS:
				getDiagramBendpoints().clear();
				getDiagramBendpoints().addAll((Collection<? extends DiagramLocation>)newValue);
				return;
			case SchemaPackage.MEMBER_ROLE__DIAGRAM_LABEL_LOCATION:
				setDiagramLabelLocation((DiagramLocation)newValue);
				return;
			case SchemaPackage.MEMBER_ROLE__DIAGRAM_SOURCE_ANCHOR:
				setDiagramSourceAnchor((DiagramLocation)newValue);
				return;
			case SchemaPackage.MEMBER_ROLE__DIAGRAM_TARGET_ANCHOR:
				setDiagramTargetAnchor((DiagramLocation)newValue);
				return;
			case SchemaPackage.MEMBER_ROLE__INDEX_DBKEY_POSITION:
				setIndexDbkeyPosition((Short)newValue);
				return;
			case SchemaPackage.MEMBER_ROLE__MEMBERSHIP_OPTION:
				setMembershipOption((SetMembershipOption)newValue);
				return;
			case SchemaPackage.MEMBER_ROLE__NEXT_DBKEY_POSITION:
				setNextDbkeyPosition((Short)newValue);
				return;
			case SchemaPackage.MEMBER_ROLE__PRIOR_DBKEY_POSITION:
				setPriorDbkeyPosition((Short)newValue);
				return;
			case SchemaPackage.MEMBER_ROLE__OWNER_DBKEY_POSITION:
				setOwnerDbkeyPosition((Short)newValue);
				return;
			case SchemaPackage.MEMBER_ROLE__RECORD:
				setRecord((SchemaRecord)newValue);
				return;
			case SchemaPackage.MEMBER_ROLE__SET:
				setSet((Set)newValue);
				return;
			case SchemaPackage.MEMBER_ROLE__SORT_KEY:
				setSortKey((Key)newValue);
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
			case SchemaPackage.MEMBER_ROLE__DIAGRAM_BENDPOINTS:
				getDiagramBendpoints().clear();
				return;
			case SchemaPackage.MEMBER_ROLE__DIAGRAM_LABEL_LOCATION:
				setDiagramLabelLocation((DiagramLocation)null);
				return;
			case SchemaPackage.MEMBER_ROLE__DIAGRAM_SOURCE_ANCHOR:
				setDiagramSourceAnchor((DiagramLocation)null);
				return;
			case SchemaPackage.MEMBER_ROLE__DIAGRAM_TARGET_ANCHOR:
				setDiagramTargetAnchor((DiagramLocation)null);
				return;
			case SchemaPackage.MEMBER_ROLE__INDEX_DBKEY_POSITION:
				setIndexDbkeyPosition(INDEX_DBKEY_POSITION_EDEFAULT);
				return;
			case SchemaPackage.MEMBER_ROLE__MEMBERSHIP_OPTION:
				setMembershipOption(MEMBERSHIP_OPTION_EDEFAULT);
				return;
			case SchemaPackage.MEMBER_ROLE__NEXT_DBKEY_POSITION:
				setNextDbkeyPosition(NEXT_DBKEY_POSITION_EDEFAULT);
				return;
			case SchemaPackage.MEMBER_ROLE__PRIOR_DBKEY_POSITION:
				setPriorDbkeyPosition(PRIOR_DBKEY_POSITION_EDEFAULT);
				return;
			case SchemaPackage.MEMBER_ROLE__OWNER_DBKEY_POSITION:
				setOwnerDbkeyPosition(OWNER_DBKEY_POSITION_EDEFAULT);
				return;
			case SchemaPackage.MEMBER_ROLE__RECORD:
				setRecord((SchemaRecord)null);
				return;
			case SchemaPackage.MEMBER_ROLE__SET:
				setSet((Set)null);
				return;
			case SchemaPackage.MEMBER_ROLE__SORT_KEY:
				setSortKey((Key)null);
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
			case SchemaPackage.MEMBER_ROLE__DIAGRAM_BENDPOINTS:
				return diagramBendpoints != null && !diagramBendpoints.isEmpty();
			case SchemaPackage.MEMBER_ROLE__DIAGRAM_LABEL_LOCATION:
				return diagramLabelLocation != null;
			case SchemaPackage.MEMBER_ROLE__DIAGRAM_SOURCE_ANCHOR:
				return diagramSourceAnchor != null;
			case SchemaPackage.MEMBER_ROLE__DIAGRAM_TARGET_ANCHOR:
				return diagramTargetAnchor != null;
			case SchemaPackage.MEMBER_ROLE__INDEX_DBKEY_POSITION:
				return INDEX_DBKEY_POSITION_EDEFAULT == null ? indexDbkeyPosition != null : !INDEX_DBKEY_POSITION_EDEFAULT.equals(indexDbkeyPosition);
			case SchemaPackage.MEMBER_ROLE__MEMBERSHIP_OPTION:
				return membershipOption != MEMBERSHIP_OPTION_EDEFAULT;
			case SchemaPackage.MEMBER_ROLE__NEXT_DBKEY_POSITION:
				return NEXT_DBKEY_POSITION_EDEFAULT == null ? nextDbkeyPosition != null : !NEXT_DBKEY_POSITION_EDEFAULT.equals(nextDbkeyPosition);
			case SchemaPackage.MEMBER_ROLE__PRIOR_DBKEY_POSITION:
				return PRIOR_DBKEY_POSITION_EDEFAULT == null ? priorDbkeyPosition != null : !PRIOR_DBKEY_POSITION_EDEFAULT.equals(priorDbkeyPosition);
			case SchemaPackage.MEMBER_ROLE__OWNER_DBKEY_POSITION:
				return OWNER_DBKEY_POSITION_EDEFAULT == null ? ownerDbkeyPosition != null : !OWNER_DBKEY_POSITION_EDEFAULT.equals(ownerDbkeyPosition);
			case SchemaPackage.MEMBER_ROLE__RECORD:
				return record != null;
			case SchemaPackage.MEMBER_ROLE__SET:
				return getSet() != null;
			case SchemaPackage.MEMBER_ROLE__SORT_KEY:
				return sortKey != null;
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
		result.append(" (indexDbkeyPosition: ");
		result.append(indexDbkeyPosition);
		result.append(", membershipOption: ");
		result.append(membershipOption);
		result.append(", nextDbkeyPosition: ");
		result.append(nextDbkeyPosition);
		result.append(", priorDbkeyPosition: ");
		result.append(priorDbkeyPosition);
		result.append(", ownerDbkeyPosition: ");
		result.append(ownerDbkeyPosition);
		result.append(')');
		return result.toString();
	}

} //MemberRoleImpl
