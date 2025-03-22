/**
 * Copyright (C) 2015  Luc Hermans
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
package org.lh.dmlj.schema;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Location Mode</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.lh.dmlj.schema.SchemaPackage#getLocationMode()
 * @model
 * @generated
 */
public enum LocationMode implements Enumerator {
	/**
	 * The '<em><b>CALC</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CALC_VALUE
	 * @generated
	 * @ordered
	 */
	CALC(0, "CALC", "CALC"),

	/**
	 * The '<em><b>DIRECT</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DIRECT_VALUE
	 * @generated
	 * @ordered
	 */
	DIRECT(1, "DIRECT", "DIRECT"),

	/**
	 * The '<em><b>VIA</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #VIA_VALUE
	 * @generated
	 * @ordered
	 */
	VIA(2, "VIA", "VIA"), /**
	 * The '<em><b>VSAM</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #VSAM_VALUE
	 * @generated
	 * @ordered
	 */
	VSAM(3, "VSAM", "VSAM"), /**
	 * The '<em><b>VSAM CALC</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #VSAM_CALC_VALUE
	 * @generated
	 * @ordered
	 */
	VSAM_CALC(4, "VSAM_CALC", "VSAM_CALC");

	/**
	 * The '<em><b>CALC</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>CALC</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CALC
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int CALC_VALUE = 0;

	/**
	 * The '<em><b>DIRECT</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>DIRECT</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DIRECT
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int DIRECT_VALUE = 1;

	/**
	 * The '<em><b>VIA</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>VIA</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #VIA
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int VIA_VALUE = 2;

	/**
	 * The '<em><b>VSAM</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>VSAM</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #VSAM
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int VSAM_VALUE = 3;

	/**
	 * The '<em><b>VSAM CALC</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>VSAM CALC</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #VSAM_CALC
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int VSAM_CALC_VALUE = 4;

	/**
	 * An array of all the '<em><b>Location Mode</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final LocationMode[] VALUES_ARRAY =
		new LocationMode[] {
			CALC,
			DIRECT,
			VIA,
			VSAM,
			VSAM_CALC,
		};

	/**
	 * A public read-only list of all the '<em><b>Location Mode</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<LocationMode> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Location Mode</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static LocationMode get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			LocationMode result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Location Mode</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static LocationMode getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			LocationMode result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Location Mode</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static LocationMode get(int value) {
		switch (value) {
			case CALC_VALUE: return CALC;
			case DIRECT_VALUE: return DIRECT;
			case VIA_VALUE: return VIA;
			case VSAM_VALUE: return VSAM;
			case VSAM_CALC_VALUE: return VSAM_CALC;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private LocationMode(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getValue() {
	  return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
	  return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getLiteral() {
	  return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string representation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}
	
} //LocationMode
