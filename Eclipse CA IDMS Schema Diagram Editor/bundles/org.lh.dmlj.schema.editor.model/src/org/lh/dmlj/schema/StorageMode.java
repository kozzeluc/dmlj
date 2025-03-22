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
 * A representation of the literals of the enumeration '<em><b>Storage Mode</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.lh.dmlj.schema.SchemaPackage#getStorageMode()
 * @model
 * @generated
 */
public enum StorageMode implements Enumerator {
	/**
	 * The '<em><b>FIXED</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #FIXED_VALUE
	 * @generated
	 * @ordered
	 */
	FIXED(0, "FIXED", "FIXED"),

	/**
	 * The '<em><b>FIXED COMPRESSED</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #FIXED_COMPRESSED_VALUE
	 * @generated
	 * @ordered
	 */
	FIXED_COMPRESSED(1, "FIXED_COMPRESSED", "FIXED_COMPRESSED"),

	/**
	 * The '<em><b>VARIABLE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #VARIABLE_VALUE
	 * @generated
	 * @ordered
	 */
	VARIABLE(2, "VARIABLE", "VARIABLE"),

	/**
	 * The '<em><b>VARIABLE COMPRESSED</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #VARIABLE_COMPRESSED_VALUE
	 * @generated
	 * @ordered
	 */
	VARIABLE_COMPRESSED(3, "VARIABLE_COMPRESSED", "VARIABLE_COMPRESSED");

	/**
	 * The '<em><b>FIXED</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>FIXED</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #FIXED
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int FIXED_VALUE = 0;

	/**
	 * The '<em><b>FIXED COMPRESSED</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>FIXED COMPRESSED</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #FIXED_COMPRESSED
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int FIXED_COMPRESSED_VALUE = 1;

	/**
	 * The '<em><b>VARIABLE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>VARIABLE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #VARIABLE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int VARIABLE_VALUE = 2;

	/**
	 * The '<em><b>VARIABLE COMPRESSED</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>VARIABLE COMPRESSED</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #VARIABLE_COMPRESSED
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int VARIABLE_COMPRESSED_VALUE = 3;

	/**
	 * An array of all the '<em><b>Storage Mode</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final StorageMode[] VALUES_ARRAY =
		new StorageMode[] {
			FIXED,
			FIXED_COMPRESSED,
			VARIABLE,
			VARIABLE_COMPRESSED,
		};

	/**
	 * A public read-only list of all the '<em><b>Storage Mode</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<StorageMode> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Storage Mode</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static StorageMode get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			StorageMode result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Storage Mode</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static StorageMode getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			StorageMode result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Storage Mode</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static StorageMode get(int value) {
		switch (value) {
			case FIXED_VALUE: return FIXED;
			case FIXED_COMPRESSED_VALUE: return FIXED_COMPRESSED;
			case VARIABLE_VALUE: return VARIABLE;
			case VARIABLE_COMPRESSED_VALUE: return VARIABLE_COMPRESSED;
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
	private StorageMode(int value, String name, String literal) {
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
	
} //StorageMode
