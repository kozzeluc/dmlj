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
 * A representation of the literals of the enumeration '<em><b>Record Procedure Call Verb</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.lh.dmlj.schema.SchemaPackage#getRecordProcedureCallVerb()
 * @model
 * @generated
 */
public enum RecordProcedureCallVerb implements Enumerator {
	/**
	 * The '<em><b>EVERY DML FUNCTION</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #EVERY_DML_FUNCTION_VALUE
	 * @generated
	 * @ordered
	 */
	EVERY_DML_FUNCTION(-1, "EVERY_DML_FUNCTION", "EVERY_DML_FUNCTION"), /**
	 * The '<em><b>CONNECT</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CONNECT_VALUE
	 * @generated
	 * @ordered
	 */
	CONNECT(0, "CONNECT", "CONNECT"),

	/**
	 * The '<em><b>DISCONNECT</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DISCONNECT_VALUE
	 * @generated
	 * @ordered
	 */
	DISCONNECT(1, "DISCONNECT", "DISCONNECT"),

	/**
	 * The '<em><b>ERASE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ERASE_VALUE
	 * @generated
	 * @ordered
	 */
	ERASE(2, "ERASE", "ERASE"),

	/**
	 * The '<em><b>FIND</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #FIND_VALUE
	 * @generated
	 * @ordered
	 */
	FIND(3, "FIND", "FIND"),

	/**
	 * The '<em><b>GET</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #GET_VALUE
	 * @generated
	 * @ordered
	 */
	GET(4, "GET", "GET"),

	/**
	 * The '<em><b>MODIFY</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MODIFY_VALUE
	 * @generated
	 * @ordered
	 */
	MODIFY(5, "MODIFY", "MODIFY"),

	/**
	 * The '<em><b>STORE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #STORE_VALUE
	 * @generated
	 * @ordered
	 */
	STORE(6, "STORE", "STORE");

	/**
	 * The '<em><b>EVERY DML FUNCTION</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>EVERY DML FUNCTION</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #EVERY_DML_FUNCTION
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int EVERY_DML_FUNCTION_VALUE = -1;

	/**
	 * The '<em><b>CONNECT</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>CONNECT</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CONNECT
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int CONNECT_VALUE = 0;

	/**
	 * The '<em><b>DISCONNECT</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>DISCONNECT</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DISCONNECT
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int DISCONNECT_VALUE = 1;

	/**
	 * The '<em><b>ERASE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ERASE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ERASE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int ERASE_VALUE = 2;

	/**
	 * The '<em><b>FIND</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>FIND</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #FIND
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int FIND_VALUE = 3;

	/**
	 * The '<em><b>GET</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>GET</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #GET
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int GET_VALUE = 4;

	/**
	 * The '<em><b>MODIFY</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MODIFY</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MODIFY
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int MODIFY_VALUE = 5;

	/**
	 * The '<em><b>STORE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>STORE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #STORE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int STORE_VALUE = 6;

	/**
	 * An array of all the '<em><b>Record Procedure Call Verb</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final RecordProcedureCallVerb[] VALUES_ARRAY =
		new RecordProcedureCallVerb[] {
			EVERY_DML_FUNCTION,
			CONNECT,
			DISCONNECT,
			ERASE,
			FIND,
			GET,
			MODIFY,
			STORE,
		};

	/**
	 * A public read-only list of all the '<em><b>Record Procedure Call Verb</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<RecordProcedureCallVerb> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Record Procedure Call Verb</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static RecordProcedureCallVerb get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			RecordProcedureCallVerb result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Record Procedure Call Verb</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static RecordProcedureCallVerb getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			RecordProcedureCallVerb result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Record Procedure Call Verb</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static RecordProcedureCallVerb get(int value) {
		switch (value) {
			case EVERY_DML_FUNCTION_VALUE: return EVERY_DML_FUNCTION;
			case CONNECT_VALUE: return CONNECT;
			case DISCONNECT_VALUE: return DISCONNECT;
			case ERASE_VALUE: return ERASE;
			case FIND_VALUE: return FIND;
			case GET_VALUE: return GET;
			case MODIFY_VALUE: return MODIFY;
			case STORE_VALUE: return STORE;
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
	private RecordProcedureCallVerb(int value, String name, String literal) {
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
	
} //RecordProcedureCallVerb
