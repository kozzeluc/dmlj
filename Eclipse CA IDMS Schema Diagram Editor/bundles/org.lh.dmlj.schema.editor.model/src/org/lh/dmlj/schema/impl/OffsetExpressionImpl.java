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
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.OffsetExpression;
import org.lh.dmlj.schema.SchemaPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Offset Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.OffsetExpressionImpl#getAreaSpecification <em>Area Specification</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.OffsetExpressionImpl#getOffsetPageCount <em>Offset Page Count</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.OffsetExpressionImpl#getOffsetPercent <em>Offset Percent</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.OffsetExpressionImpl#getPageCount <em>Page Count</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.OffsetExpressionImpl#getPercent <em>Percent</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OffsetExpressionImpl extends EObjectImpl implements OffsetExpression {
	/**
	 * The default value of the '{@link #getOffsetPageCount() <em>Offset Page Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOffsetPageCount()
	 * @generated
	 * @ordered
	 */
	protected static final Integer OFFSET_PAGE_COUNT_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getOffsetPageCount() <em>Offset Page Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOffsetPageCount()
	 * @generated
	 * @ordered
	 */
	protected Integer offsetPageCount = OFFSET_PAGE_COUNT_EDEFAULT;
	/**
	 * The default value of the '{@link #getOffsetPercent() <em>Offset Percent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOffsetPercent()
	 * @generated
	 * @ordered
	 */
	protected static final Short OFFSET_PERCENT_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getOffsetPercent() <em>Offset Percent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOffsetPercent()
	 * @generated
	 * @ordered
	 */
	protected Short offsetPercent = OFFSET_PERCENT_EDEFAULT;
	/**
	 * The default value of the '{@link #getPageCount() <em>Page Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPageCount()
	 * @generated
	 * @ordered
	 */
	protected static final Integer PAGE_COUNT_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getPageCount() <em>Page Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPageCount()
	 * @generated
	 * @ordered
	 */
	protected Integer pageCount = PAGE_COUNT_EDEFAULT;
	/**
	 * The default value of the '{@link #getPercent() <em>Percent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPercent()
	 * @generated
	 * @ordered
	 */
	protected static final Short PERCENT_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getPercent() <em>Percent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPercent()
	 * @generated
	 * @ordered
	 */
	protected Short percent = PERCENT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OffsetExpressionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.OFFSET_EXPRESSION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AreaSpecification getAreaSpecification() {
		if (eContainerFeatureID() != SchemaPackage.OFFSET_EXPRESSION__AREA_SPECIFICATION) return null;
		return (AreaSpecification)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAreaSpecification(AreaSpecification newAreaSpecification, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newAreaSpecification, SchemaPackage.OFFSET_EXPRESSION__AREA_SPECIFICATION, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAreaSpecification(AreaSpecification newAreaSpecification) {
		if (newAreaSpecification != eInternalContainer() || (eContainerFeatureID() != SchemaPackage.OFFSET_EXPRESSION__AREA_SPECIFICATION && newAreaSpecification != null)) {
			if (EcoreUtil.isAncestor(this, newAreaSpecification))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newAreaSpecification != null)
				msgs = ((InternalEObject)newAreaSpecification).eInverseAdd(this, SchemaPackage.AREA_SPECIFICATION__OFFSET_EXPRESSION, AreaSpecification.class, msgs);
			msgs = basicSetAreaSpecification(newAreaSpecification, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.OFFSET_EXPRESSION__AREA_SPECIFICATION, newAreaSpecification, newAreaSpecification));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Integer getOffsetPageCount() {
		return offsetPageCount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOffsetPageCount(Integer newOffsetPageCount) {
		Integer oldOffsetPageCount = offsetPageCount;
		offsetPageCount = newOffsetPageCount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.OFFSET_EXPRESSION__OFFSET_PAGE_COUNT, oldOffsetPageCount, offsetPageCount));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Short getOffsetPercent() {
		return offsetPercent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOffsetPercent(Short newOffsetPercent) {
		Short oldOffsetPercent = offsetPercent;
		offsetPercent = newOffsetPercent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.OFFSET_EXPRESSION__OFFSET_PERCENT, oldOffsetPercent, offsetPercent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Integer getPageCount() {
		return pageCount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPageCount(Integer newPageCount) {
		Integer oldPageCount = pageCount;
		pageCount = newPageCount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.OFFSET_EXPRESSION__PAGE_COUNT, oldPageCount, pageCount));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Short getPercent() {
		return percent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPercent(Short newPercent) {
		Short oldPercent = percent;
		percent = newPercent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.OFFSET_EXPRESSION__PERCENT, oldPercent, percent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.OFFSET_EXPRESSION__AREA_SPECIFICATION:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetAreaSpecification((AreaSpecification)otherEnd, msgs);
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
			case SchemaPackage.OFFSET_EXPRESSION__AREA_SPECIFICATION:
				return basicSetAreaSpecification(null, msgs);
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
			case SchemaPackage.OFFSET_EXPRESSION__AREA_SPECIFICATION:
				return eInternalContainer().eInverseRemove(this, SchemaPackage.AREA_SPECIFICATION__OFFSET_EXPRESSION, AreaSpecification.class, msgs);
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
			case SchemaPackage.OFFSET_EXPRESSION__AREA_SPECIFICATION:
				return getAreaSpecification();
			case SchemaPackage.OFFSET_EXPRESSION__OFFSET_PAGE_COUNT:
				return getOffsetPageCount();
			case SchemaPackage.OFFSET_EXPRESSION__OFFSET_PERCENT:
				return getOffsetPercent();
			case SchemaPackage.OFFSET_EXPRESSION__PAGE_COUNT:
				return getPageCount();
			case SchemaPackage.OFFSET_EXPRESSION__PERCENT:
				return getPercent();
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
			case SchemaPackage.OFFSET_EXPRESSION__AREA_SPECIFICATION:
				setAreaSpecification((AreaSpecification)newValue);
				return;
			case SchemaPackage.OFFSET_EXPRESSION__OFFSET_PAGE_COUNT:
				setOffsetPageCount((Integer)newValue);
				return;
			case SchemaPackage.OFFSET_EXPRESSION__OFFSET_PERCENT:
				setOffsetPercent((Short)newValue);
				return;
			case SchemaPackage.OFFSET_EXPRESSION__PAGE_COUNT:
				setPageCount((Integer)newValue);
				return;
			case SchemaPackage.OFFSET_EXPRESSION__PERCENT:
				setPercent((Short)newValue);
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
			case SchemaPackage.OFFSET_EXPRESSION__AREA_SPECIFICATION:
				setAreaSpecification((AreaSpecification)null);
				return;
			case SchemaPackage.OFFSET_EXPRESSION__OFFSET_PAGE_COUNT:
				setOffsetPageCount(OFFSET_PAGE_COUNT_EDEFAULT);
				return;
			case SchemaPackage.OFFSET_EXPRESSION__OFFSET_PERCENT:
				setOffsetPercent(OFFSET_PERCENT_EDEFAULT);
				return;
			case SchemaPackage.OFFSET_EXPRESSION__PAGE_COUNT:
				setPageCount(PAGE_COUNT_EDEFAULT);
				return;
			case SchemaPackage.OFFSET_EXPRESSION__PERCENT:
				setPercent(PERCENT_EDEFAULT);
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
			case SchemaPackage.OFFSET_EXPRESSION__AREA_SPECIFICATION:
				return getAreaSpecification() != null;
			case SchemaPackage.OFFSET_EXPRESSION__OFFSET_PAGE_COUNT:
				return OFFSET_PAGE_COUNT_EDEFAULT == null ? offsetPageCount != null : !OFFSET_PAGE_COUNT_EDEFAULT.equals(offsetPageCount);
			case SchemaPackage.OFFSET_EXPRESSION__OFFSET_PERCENT:
				return OFFSET_PERCENT_EDEFAULT == null ? offsetPercent != null : !OFFSET_PERCENT_EDEFAULT.equals(offsetPercent);
			case SchemaPackage.OFFSET_EXPRESSION__PAGE_COUNT:
				return PAGE_COUNT_EDEFAULT == null ? pageCount != null : !PAGE_COUNT_EDEFAULT.equals(pageCount);
			case SchemaPackage.OFFSET_EXPRESSION__PERCENT:
				return PERCENT_EDEFAULT == null ? percent != null : !PERCENT_EDEFAULT.equals(percent);
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
		result.append(" (offsetPageCount: ");
		result.append(offsetPageCount);
		result.append(", offsetPercent: ");
		result.append(offsetPercent);
		result.append(", pageCount: ");
		result.append(pageCount);
		result.append(", percent: ");
		result.append(percent);
		result.append(')');
		return result.toString();
	}

} //OffsetExpressionImpl
