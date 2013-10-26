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
 * A representation of the literals of the enumeration '<em><b>Set Membership Option</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.lh.dmlj.schema.SchemaPackage#getSetMembershipOption()
 * @model
 * @generated
 */
public enum SetMembershipOption implements Enumerator {
	/**
	 * The '<em><b>MANDATORY AUTOMATIC</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MANDATORY_AUTOMATIC_VALUE
	 * @generated
	 * @ordered
	 */
	MANDATORY_AUTOMATIC(0, "MANDATORY_AUTOMATIC", "MANDATORY_AUTOMATIC"),

	/**
	 * The '<em><b>MANDATORY MANUAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MANDATORY_MANUAL_VALUE
	 * @generated
	 * @ordered
	 */
	MANDATORY_MANUAL(1, "MANDATORY_MANUAL", "MANDATORY_MANUAL"),

	/**
	 * The '<em><b>OPTIONAL AUTOMATIC</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #OPTIONAL_AUTOMATIC_VALUE
	 * @generated
	 * @ordered
	 */
	OPTIONAL_AUTOMATIC(2, "OPTIONAL_AUTOMATIC", "OPTIONAL_AUTOMATIC"),

	/**
	 * The '<em><b>OPTIONAL MANUAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #OPTIONAL_MANUAL_VALUE
	 * @generated
	 * @ordered
	 */
	OPTIONAL_MANUAL(3, "OPTIONAL_MANUAL", "OPTIONAL_MANUAL");

	/**
	 * The '<em><b>MANDATORY AUTOMATIC</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MANDATORY AUTOMATIC</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MANDATORY_AUTOMATIC
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int MANDATORY_AUTOMATIC_VALUE = 0;

	/**
	 * The '<em><b>MANDATORY MANUAL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MANDATORY MANUAL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MANDATORY_MANUAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int MANDATORY_MANUAL_VALUE = 1;

	/**
	 * The '<em><b>OPTIONAL AUTOMATIC</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>OPTIONAL AUTOMATIC</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #OPTIONAL_AUTOMATIC
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int OPTIONAL_AUTOMATIC_VALUE = 2;

	/**
	 * The '<em><b>OPTIONAL MANUAL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>OPTIONAL MANUAL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #OPTIONAL_MANUAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int OPTIONAL_MANUAL_VALUE = 3;

	/**
	 * An array of all the '<em><b>Set Membership Option</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final SetMembershipOption[] VALUES_ARRAY =
		new SetMembershipOption[] {
			MANDATORY_AUTOMATIC,
			MANDATORY_MANUAL,
			OPTIONAL_AUTOMATIC,
			OPTIONAL_MANUAL,
		};

	/**
	 * A public read-only list of all the '<em><b>Set Membership Option</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<SetMembershipOption> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Set Membership Option</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SetMembershipOption get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			SetMembershipOption result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Set Membership Option</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SetMembershipOption getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			SetMembershipOption result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Set Membership Option</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SetMembershipOption get(int value) {
		switch (value) {
			case MANDATORY_AUTOMATIC_VALUE: return MANDATORY_AUTOMATIC;
			case MANDATORY_MANUAL_VALUE: return MANDATORY_MANUAL;
			case OPTIONAL_AUTOMATIC_VALUE: return OPTIONAL_AUTOMATIC;
			case OPTIONAL_MANUAL_VALUE: return OPTIONAL_MANUAL;
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
	private SetMembershipOption(int value, String name, String literal) {
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
	
} //SetMembershipOption
