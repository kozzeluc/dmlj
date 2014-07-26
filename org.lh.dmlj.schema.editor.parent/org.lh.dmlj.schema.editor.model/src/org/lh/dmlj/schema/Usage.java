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
package org.lh.dmlj.schema;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Usage</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.lh.dmlj.schema.SchemaPackage#getUsage()
 * @model
 * @generated
 */
public enum Usage implements Enumerator {
	/**
	 * The '<em><b>DISPLAY</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DISPLAY_VALUE
	 * @generated
	 * @ordered
	 */
	DISPLAY(0, "DISPLAY", "DISPLAY"),

	/**
	 * The '<em><b>COMPUTATIONAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #COMPUTATIONAL_VALUE
	 * @generated
	 * @ordered
	 */
	COMPUTATIONAL(1, "COMPUTATIONAL", "COMPUTATIONAL"),

	/**
	 * The '<em><b>COMPUTATIONAL 1</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #COMPUTATIONAL_1_VALUE
	 * @generated
	 * @ordered
	 */
	COMPUTATIONAL_1(2, "COMPUTATIONAL_1", "COMPUTATIONAL_1"),

	/**
	 * The '<em><b>COMPUTATIONAL 2</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #COMPUTATIONAL_2_VALUE
	 * @generated
	 * @ordered
	 */
	COMPUTATIONAL_2(3, "COMPUTATIONAL_2", "COMPUTATIONAL_2"),

	/**
	 * The '<em><b>COMPUTATIONAL 3</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #COMPUTATIONAL_3_VALUE
	 * @generated
	 * @ordered
	 */
	COMPUTATIONAL_3(4, "COMPUTATIONAL_3", "COMPUTATIONAL_3"),

	/**
	 * The '<em><b>BIT</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #BIT_VALUE
	 * @generated
	 * @ordered
	 */
	BIT(5, "BIT", "BIT"),

	/**
	 * The '<em><b>POINTER</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #POINTER_VALUE
	 * @generated
	 * @ordered
	 */
	POINTER(6, "POINTER", "POINTER"),

	/**
	 * The '<em><b>DISPLAY 1</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DISPLAY_1_VALUE
	 * @generated
	 * @ordered
	 */
	DISPLAY_1(7, "DISPLAY_1", "DISPLAY_1"),

	/**
	 * The '<em><b>CONDITION NAME</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CONDITION_NAME_VALUE
	 * @generated
	 * @ordered
	 */
	CONDITION_NAME(88, "CONDITION_NAME", "CONDITION_NAME");

	/**
	 * The '<em><b>DISPLAY</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>DISPLAY</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DISPLAY
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int DISPLAY_VALUE = 0;

	/**
	 * The '<em><b>COMPUTATIONAL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>COMPUTATIONAL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #COMPUTATIONAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int COMPUTATIONAL_VALUE = 1;

	/**
	 * The '<em><b>COMPUTATIONAL 1</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>COMPUTATIONAL 1</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #COMPUTATIONAL_1
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int COMPUTATIONAL_1_VALUE = 2;

	/**
	 * The '<em><b>COMPUTATIONAL 2</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>COMPUTATIONAL 2</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #COMPUTATIONAL_2
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int COMPUTATIONAL_2_VALUE = 3;

	/**
	 * The '<em><b>COMPUTATIONAL 3</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>COMPUTATIONAL 3</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #COMPUTATIONAL_3
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int COMPUTATIONAL_3_VALUE = 4;

	/**
	 * The '<em><b>BIT</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>BIT</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BIT
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int BIT_VALUE = 5;

	/**
	 * The '<em><b>POINTER</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>POINTER</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #POINTER
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int POINTER_VALUE = 6;

	/**
	 * The '<em><b>DISPLAY 1</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>DISPLAY 1</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DISPLAY_1
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int DISPLAY_1_VALUE = 7;

	/**
	 * The '<em><b>CONDITION NAME</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>CONDITION NAME</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CONDITION_NAME
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int CONDITION_NAME_VALUE = 88;

	/**
	 * An array of all the '<em><b>Usage</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final Usage[] VALUES_ARRAY =
		new Usage[] {
			DISPLAY,
			COMPUTATIONAL,
			COMPUTATIONAL_1,
			COMPUTATIONAL_2,
			COMPUTATIONAL_3,
			BIT,
			POINTER,
			DISPLAY_1,
			CONDITION_NAME,
		};

	/**
	 * A public read-only list of all the '<em><b>Usage</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<Usage> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Usage</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Usage get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			Usage result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Usage</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Usage getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			Usage result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Usage</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Usage get(int value) {
		switch (value) {
			case DISPLAY_VALUE: return DISPLAY;
			case COMPUTATIONAL_VALUE: return COMPUTATIONAL;
			case COMPUTATIONAL_1_VALUE: return COMPUTATIONAL_1;
			case COMPUTATIONAL_2_VALUE: return COMPUTATIONAL_2;
			case COMPUTATIONAL_3_VALUE: return COMPUTATIONAL_3;
			case BIT_VALUE: return BIT;
			case POINTER_VALUE: return POINTER;
			case DISPLAY_1_VALUE: return DISPLAY_1;
			case CONDITION_NAME_VALUE: return CONDITION_NAME;
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
	private Usage(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getValue() {
	  return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
	  return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
	
} //Usage
