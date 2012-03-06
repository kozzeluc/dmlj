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
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Key</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.KeyImpl#isCompressed <em>Compressed</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.KeyImpl#getDuplicatesOption <em>Duplicates Option</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.KeyImpl#getElements <em>Elements</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.KeyImpl#getLength <em>Length</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.KeyImpl#getMemberRole <em>Member Role</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.KeyImpl#isNaturalSequence <em>Natural Sequence</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.KeyImpl#getRecord <em>Record</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class KeyImpl extends EObjectImpl implements Key {
	/**
	 * The default value of the '{@link #isCompressed() <em>Compressed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCompressed()
	 * @generated
	 * @ordered
	 */
	protected static final boolean COMPRESSED_EDEFAULT = false;
	/**
	 * The cached value of the '{@link #isCompressed() <em>Compressed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCompressed()
	 * @generated
	 * @ordered
	 */
	protected boolean compressed = COMPRESSED_EDEFAULT;
	/**
	 * The default value of the '{@link #getDuplicatesOption() <em>Duplicates Option</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDuplicatesOption()
	 * @generated
	 * @ordered
	 */
	protected static final DuplicatesOption DUPLICATES_OPTION_EDEFAULT = DuplicatesOption.FIRST;
	/**
	 * The cached value of the '{@link #getDuplicatesOption() <em>Duplicates Option</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDuplicatesOption()
	 * @generated
	 * @ordered
	 */
	protected DuplicatesOption duplicatesOption = DUPLICATES_OPTION_EDEFAULT;
	/**
	 * The cached value of the '{@link #getElements() <em>Elements</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getElements()
	 * @generated
	 * @ordered
	 */
	protected EList<KeyElement> elements;
	/**
	 * The default value of the '{@link #getLength() <em>Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLength()
	 * @generated
	 * @ordered
	 */
	protected static final short LENGTH_EDEFAULT = 0;
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
	 * The default value of the '{@link #isNaturalSequence() <em>Natural Sequence</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isNaturalSequence()
	 * @generated
	 * @ordered
	 */
	protected static final boolean NATURAL_SEQUENCE_EDEFAULT = false;
	/**
	 * The cached value of the '{@link #isNaturalSequence() <em>Natural Sequence</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isNaturalSequence()
	 * @generated
	 * @ordered
	 */
	protected boolean naturalSequence = NATURAL_SEQUENCE_EDEFAULT;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected KeyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.KEY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public short getLength() {
		short length = 0;
		// traverse all key elements to calculate the key's length...
		for (KeyElement keyElement : getElements()) {
			if (!keyElement.isDbkey()) {
				// add the element's length to the key length...
				length += keyElement.getElement().getLength();
			} else {
				// add 4, the length of a dbkey, to the key length...
				length += 4;
			}
		}
		//
		return length;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DuplicatesOption getDuplicatesOption() {
		return duplicatesOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDuplicatesOption(DuplicatesOption newDuplicatesOption) {
		DuplicatesOption oldDuplicatesOption = duplicatesOption;
		duplicatesOption = newDuplicatesOption == null ? DUPLICATES_OPTION_EDEFAULT : newDuplicatesOption;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.KEY__DUPLICATES_OPTION, oldDuplicatesOption, duplicatesOption));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isCompressed() {
		return compressed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCompressed(boolean newCompressed) {
		boolean oldCompressed = compressed;
		compressed = newCompressed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.KEY__COMPRESSED, oldCompressed, compressed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isNaturalSequence() {
		return naturalSequence;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNaturalSequence(boolean newNaturalSequence) {
		boolean oldNaturalSequence = naturalSequence;
		naturalSequence = newNaturalSequence;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.KEY__NATURAL_SEQUENCE, oldNaturalSequence, naturalSequence));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<KeyElement> getElements() {
		if (elements == null) {
			elements = new EObjectContainmentWithInverseEList<KeyElement>(KeyElement.class, this, SchemaPackage.KEY__ELEMENTS, SchemaPackage.KEY_ELEMENT__KEY);
		}
		return elements;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.KEY__MEMBER_ROLE, oldMemberRole, memberRole));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.KEY__MEMBER_ROLE, oldMemberRole, newMemberRole);
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
				msgs = ((InternalEObject)memberRole).eInverseRemove(this, SchemaPackage.MEMBER_ROLE__SORT_KEY, MemberRole.class, msgs);
			if (newMemberRole != null)
				msgs = ((InternalEObject)newMemberRole).eInverseAdd(this, SchemaPackage.MEMBER_ROLE__SORT_KEY, MemberRole.class, msgs);
			msgs = basicSetMemberRole(newMemberRole, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.KEY__MEMBER_ROLE, newMemberRole, newMemberRole));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SchemaRecord getRecord() {
		if (eContainerFeatureID() != SchemaPackage.KEY__RECORD) return null;
		return (SchemaRecord)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRecord(SchemaRecord newRecord, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newRecord, SchemaPackage.KEY__RECORD, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRecord(SchemaRecord newRecord) {
		if (newRecord != eInternalContainer() || (eContainerFeatureID() != SchemaPackage.KEY__RECORD && newRecord != null)) {
			if (EcoreUtil.isAncestor(this, newRecord))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newRecord != null)
				msgs = ((InternalEObject)newRecord).eInverseAdd(this, SchemaPackage.SCHEMA_RECORD__KEYS, SchemaRecord.class, msgs);
			msgs = basicSetRecord(newRecord, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.KEY__RECORD, newRecord, newRecord));
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
			case SchemaPackage.KEY__ELEMENTS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getElements()).basicAdd(otherEnd, msgs);
			case SchemaPackage.KEY__MEMBER_ROLE:
				if (memberRole != null)
					msgs = ((InternalEObject)memberRole).eInverseRemove(this, SchemaPackage.MEMBER_ROLE__SORT_KEY, MemberRole.class, msgs);
				return basicSetMemberRole((MemberRole)otherEnd, msgs);
			case SchemaPackage.KEY__RECORD:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetRecord((SchemaRecord)otherEnd, msgs);
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
			case SchemaPackage.KEY__ELEMENTS:
				return ((InternalEList<?>)getElements()).basicRemove(otherEnd, msgs);
			case SchemaPackage.KEY__MEMBER_ROLE:
				return basicSetMemberRole(null, msgs);
			case SchemaPackage.KEY__RECORD:
				return basicSetRecord(null, msgs);
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
			case SchemaPackage.KEY__RECORD:
				return eInternalContainer().eInverseRemove(this, SchemaPackage.SCHEMA_RECORD__KEYS, SchemaRecord.class, msgs);
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
			case SchemaPackage.KEY__COMPRESSED:
				return isCompressed();
			case SchemaPackage.KEY__DUPLICATES_OPTION:
				return getDuplicatesOption();
			case SchemaPackage.KEY__ELEMENTS:
				return getElements();
			case SchemaPackage.KEY__LENGTH:
				return getLength();
			case SchemaPackage.KEY__MEMBER_ROLE:
				if (resolve) return getMemberRole();
				return basicGetMemberRole();
			case SchemaPackage.KEY__NATURAL_SEQUENCE:
				return isNaturalSequence();
			case SchemaPackage.KEY__RECORD:
				return getRecord();
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
			case SchemaPackage.KEY__COMPRESSED:
				setCompressed((Boolean)newValue);
				return;
			case SchemaPackage.KEY__DUPLICATES_OPTION:
				setDuplicatesOption((DuplicatesOption)newValue);
				return;
			case SchemaPackage.KEY__ELEMENTS:
				getElements().clear();
				getElements().addAll((Collection<? extends KeyElement>)newValue);
				return;
			case SchemaPackage.KEY__MEMBER_ROLE:
				setMemberRole((MemberRole)newValue);
				return;
			case SchemaPackage.KEY__NATURAL_SEQUENCE:
				setNaturalSequence((Boolean)newValue);
				return;
			case SchemaPackage.KEY__RECORD:
				setRecord((SchemaRecord)newValue);
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
			case SchemaPackage.KEY__COMPRESSED:
				setCompressed(COMPRESSED_EDEFAULT);
				return;
			case SchemaPackage.KEY__DUPLICATES_OPTION:
				setDuplicatesOption(DUPLICATES_OPTION_EDEFAULT);
				return;
			case SchemaPackage.KEY__ELEMENTS:
				getElements().clear();
				return;
			case SchemaPackage.KEY__MEMBER_ROLE:
				setMemberRole((MemberRole)null);
				return;
			case SchemaPackage.KEY__NATURAL_SEQUENCE:
				setNaturalSequence(NATURAL_SEQUENCE_EDEFAULT);
				return;
			case SchemaPackage.KEY__RECORD:
				setRecord((SchemaRecord)null);
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
			case SchemaPackage.KEY__COMPRESSED:
				return compressed != COMPRESSED_EDEFAULT;
			case SchemaPackage.KEY__DUPLICATES_OPTION:
				return duplicatesOption != DUPLICATES_OPTION_EDEFAULT;
			case SchemaPackage.KEY__ELEMENTS:
				return elements != null && !elements.isEmpty();
			case SchemaPackage.KEY__LENGTH:
				return getLength() != LENGTH_EDEFAULT;
			case SchemaPackage.KEY__MEMBER_ROLE:
				return memberRole != null;
			case SchemaPackage.KEY__NATURAL_SEQUENCE:
				return naturalSequence != NATURAL_SEQUENCE_EDEFAULT;
			case SchemaPackage.KEY__RECORD:
				return getRecord() != null;
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
		result.append(" (compressed: ");
		result.append(compressed);
		result.append(", duplicatesOption: ");
		result.append(duplicatesOption);
		result.append(", naturalSequence: ");
		result.append(naturalSequence);
		result.append(')');
		return result.toString();
	}

} //KeyImpl
