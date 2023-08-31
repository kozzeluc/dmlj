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
 * A representation of the literals of the enumeration '<em><b>Set Order</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.lh.dmlj.schema.SchemaPackage#getSetOrder()
 * @model
 * @generated
 */
public enum SetOrder implements Enumerator {
	/**
	 * The '<em><b>FIRST</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #FIRST_VALUE
	 * @generated
	 * @ordered
	 */
	FIRST(0, "FIRST", "FIRST"),

	/**
	 * The '<em><b>LAST</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #LAST_VALUE
	 * @generated
	 * @ordered
	 */
	LAST(1, "LAST", "LAST"),

	/**
	 * The '<em><b>NEXT</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #NEXT_VALUE
	 * @generated
	 * @ordered
	 */
	NEXT(2, "NEXT", "NEXT"),

	/**
	 * The '<em><b>PRIOR</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PRIOR_VALUE
	 * @generated
	 * @ordered
	 */
	PRIOR(3, "PRIOR", "PRIOR"),

	/**
	 * The '<em><b>SORTED</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SORTED_VALUE
	 * @generated
	 * @ordered
	 */
	SORTED(4, "SORTED", "SORTED");

	/**
	 * The '<em><b>FIRST</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>FIRST</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #FIRST
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int FIRST_VALUE = 0;

	/**
	 * The '<em><b>LAST</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>LAST</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #LAST
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int LAST_VALUE = 1;

	/**
	 * The '<em><b>NEXT</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>NEXT</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #NEXT
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int NEXT_VALUE = 2;

	/**
	 * The '<em><b>PRIOR</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>PRIOR</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #PRIOR
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int PRIOR_VALUE = 3;

	/**
	 * The '<em><b>SORTED</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>SORTED</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SORTED
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int SORTED_VALUE = 4;

	/**
	 * An array of all the '<em><b>Set Order</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final SetOrder[] VALUES_ARRAY =
		new SetOrder[] {
			FIRST,
			LAST,
			NEXT,
			PRIOR,
			SORTED,
		};

	/**
	 * A public read-only list of all the '<em><b>Set Order</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<SetOrder> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Set Order</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static SetOrder get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			SetOrder result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Set Order</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static SetOrder getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			SetOrder result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Set Order</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static SetOrder get(int value) {
		switch (value) {
			case FIRST_VALUE: return FIRST;
			case LAST_VALUE: return LAST;
			case NEXT_VALUE: return NEXT;
			case PRIOR_VALUE: return PRIOR;
			case SORTED_VALUE: return SORTED;
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
	private SetOrder(int value, String name, String literal) {
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
	
} //SetOrder
