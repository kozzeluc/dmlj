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
 * A representation of the literals of the enumeration '<em><b>Area Procedure Call Function</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.lh.dmlj.schema.SchemaPackage#getAreaProcedureCallFunction()
 * @model
 * @generated
 */
public enum AreaProcedureCallFunction implements Enumerator {
	/**
	 * The '<em><b>EVERY DML FUNCTION</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #EVERY_DML_FUNCTION_VALUE
	 * @generated
	 * @ordered
	 */
	EVERY_DML_FUNCTION(-1, "EVERY_DML_FUNCTION", "EVERY_DML_FUNCTION"), /**
	 * The '<em><b>READY EXCLUSIVE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #READY_EXCLUSIVE_VALUE
	 * @generated
	 * @ordered
	 */
	READY_EXCLUSIVE(0, "READY_EXCLUSIVE", "READY_EXCLUSIVE"),

	/**
	 * The '<em><b>READY EXCLUSIVE UPDATE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #READY_EXCLUSIVE_UPDATE_VALUE
	 * @generated
	 * @ordered
	 */
	READY_EXCLUSIVE_UPDATE(1, "READY_EXCLUSIVE_UPDATE", "READY_EXCLUSIVE_UPDATE"),

	/**
	 * The '<em><b>READY EXCLUSIVE RETRIEVAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #READY_EXCLUSIVE_RETRIEVAL_VALUE
	 * @generated
	 * @ordered
	 */
	READY_EXCLUSIVE_RETRIEVAL(2, "READY_EXCLUSIVE_RETRIEVAL", "READY_EXCLUSIVE_RETRIEVAL"),

	/**
	 * The '<em><b>READY PROTECTED</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #READY_PROTECTED_VALUE
	 * @generated
	 * @ordered
	 */
	READY_PROTECTED(3, "READY_PROTECTED", "READY_PROTECTED"),

	/**
	 * The '<em><b>READY PROTECTED UPDATE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #READY_PROTECTED_UPDATE_VALUE
	 * @generated
	 * @ordered
	 */
	READY_PROTECTED_UPDATE(4, "READY_PROTECTED_UPDATE", "READY_PROTECTED_UPDATE"),

	/**
	 * The '<em><b>READY PROTECTED RETRIEVAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #READY_PROTECTED_RETRIEVAL_VALUE
	 * @generated
	 * @ordered
	 */
	READY_PROTECTED_RETRIEVAL(5, "READY_PROTECTED_RETRIEVAL", "READY_PROTECTED_RETRIEVAL"),

	/**
	 * The '<em><b>READY SHARED</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #READY_SHARED_VALUE
	 * @generated
	 * @ordered
	 */
	READY_SHARED(6, "READY_SHARED", "READY_SHARED"),

	/**
	 * The '<em><b>READY SHARED UPDATE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #READY_SHARED_UPDATE_VALUE
	 * @generated
	 * @ordered
	 */
	READY_SHARED_UPDATE(7, "READY_SHARED_UPDATE", "READY_SHARED_UPDATE"),

	/**
	 * The '<em><b>READY SHARED RETRIEVAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #READY_SHARED_RETRIEVAL_VALUE
	 * @generated
	 * @ordered
	 */
	READY_SHARED_RETRIEVAL(8, "READY_SHARED_RETRIEVAL", "READY_SHARED_RETRIEVAL"),

	/**
	 * The '<em><b>READY UPDATE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #READY_UPDATE_VALUE
	 * @generated
	 * @ordered
	 */
	READY_UPDATE(9, "READY_UPDATE", "READY_UPDATE"),

	/**
	 * The '<em><b>READY RETRIEVAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #READY_RETRIEVAL_VALUE
	 * @generated
	 * @ordered
	 */
	READY_RETRIEVAL(10, "READY_RETRIEVAL", "READY_RETRIEVAL"),

	/**
	 * The '<em><b>FINISH</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #FINISH_VALUE
	 * @generated
	 * @ordered
	 */
	FINISH(11, "FINISH", "FINISH"),

	/**
	 * The '<em><b>COMMIT</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #COMMIT_VALUE
	 * @generated
	 * @ordered
	 */
	COMMIT(12, "COMMIT", "COMMIT"),

	/**
	 * The '<em><b>ROLLBACK</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ROLLBACK_VALUE
	 * @generated
	 * @ordered
	 */
	ROLLBACK(13, "ROLLBACK", "ROLLBACK");

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
	 * The '<em><b>READY EXCLUSIVE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>READY EXCLUSIVE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #READY_EXCLUSIVE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int READY_EXCLUSIVE_VALUE = 0;

	/**
	 * The '<em><b>READY EXCLUSIVE UPDATE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>READY EXCLUSIVE UPDATE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #READY_EXCLUSIVE_UPDATE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int READY_EXCLUSIVE_UPDATE_VALUE = 1;

	/**
	 * The '<em><b>READY EXCLUSIVE RETRIEVAL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>READY EXCLUSIVE RETRIEVAL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #READY_EXCLUSIVE_RETRIEVAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int READY_EXCLUSIVE_RETRIEVAL_VALUE = 2;

	/**
	 * The '<em><b>READY PROTECTED</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>READY PROTECTED</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #READY_PROTECTED
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int READY_PROTECTED_VALUE = 3;

	/**
	 * The '<em><b>READY PROTECTED UPDATE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>READY PROTECTED UPDATE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #READY_PROTECTED_UPDATE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int READY_PROTECTED_UPDATE_VALUE = 4;

	/**
	 * The '<em><b>READY PROTECTED RETRIEVAL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>READY PROTECTED RETRIEVAL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #READY_PROTECTED_RETRIEVAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int READY_PROTECTED_RETRIEVAL_VALUE = 5;

	/**
	 * The '<em><b>READY SHARED</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>READY SHARED</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #READY_SHARED
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int READY_SHARED_VALUE = 6;

	/**
	 * The '<em><b>READY SHARED UPDATE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>READY SHARED UPDATE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #READY_SHARED_UPDATE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int READY_SHARED_UPDATE_VALUE = 7;

	/**
	 * The '<em><b>READY SHARED RETRIEVAL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>READY SHARED RETRIEVAL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #READY_SHARED_RETRIEVAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int READY_SHARED_RETRIEVAL_VALUE = 8;

	/**
	 * The '<em><b>READY UPDATE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>READY UPDATE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #READY_UPDATE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int READY_UPDATE_VALUE = 9;

	/**
	 * The '<em><b>READY RETRIEVAL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>READY RETRIEVAL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #READY_RETRIEVAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int READY_RETRIEVAL_VALUE = 10;

	/**
	 * The '<em><b>FINISH</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>FINISH</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #FINISH
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int FINISH_VALUE = 11;

	/**
	 * The '<em><b>COMMIT</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>COMMIT</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #COMMIT
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int COMMIT_VALUE = 12;

	/**
	 * The '<em><b>ROLLBACK</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ROLLBACK</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ROLLBACK
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int ROLLBACK_VALUE = 13;

	/**
	 * An array of all the '<em><b>Area Procedure Call Function</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final AreaProcedureCallFunction[] VALUES_ARRAY =
		new AreaProcedureCallFunction[] {
			EVERY_DML_FUNCTION,
			READY_EXCLUSIVE,
			READY_EXCLUSIVE_UPDATE,
			READY_EXCLUSIVE_RETRIEVAL,
			READY_PROTECTED,
			READY_PROTECTED_UPDATE,
			READY_PROTECTED_RETRIEVAL,
			READY_SHARED,
			READY_SHARED_UPDATE,
			READY_SHARED_RETRIEVAL,
			READY_UPDATE,
			READY_RETRIEVAL,
			FINISH,
			COMMIT,
			ROLLBACK,
		};

	/**
	 * A public read-only list of all the '<em><b>Area Procedure Call Function</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<AreaProcedureCallFunction> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Area Procedure Call Function</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static AreaProcedureCallFunction get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			AreaProcedureCallFunction result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Area Procedure Call Function</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static AreaProcedureCallFunction getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			AreaProcedureCallFunction result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Area Procedure Call Function</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static AreaProcedureCallFunction get(int value) {
		switch (value) {
			case EVERY_DML_FUNCTION_VALUE: return EVERY_DML_FUNCTION;
			case READY_EXCLUSIVE_VALUE: return READY_EXCLUSIVE;
			case READY_EXCLUSIVE_UPDATE_VALUE: return READY_EXCLUSIVE_UPDATE;
			case READY_EXCLUSIVE_RETRIEVAL_VALUE: return READY_EXCLUSIVE_RETRIEVAL;
			case READY_PROTECTED_VALUE: return READY_PROTECTED;
			case READY_PROTECTED_UPDATE_VALUE: return READY_PROTECTED_UPDATE;
			case READY_PROTECTED_RETRIEVAL_VALUE: return READY_PROTECTED_RETRIEVAL;
			case READY_SHARED_VALUE: return READY_SHARED;
			case READY_SHARED_UPDATE_VALUE: return READY_SHARED_UPDATE;
			case READY_SHARED_RETRIEVAL_VALUE: return READY_SHARED_RETRIEVAL;
			case READY_UPDATE_VALUE: return READY_UPDATE;
			case READY_RETRIEVAL_VALUE: return READY_RETRIEVAL;
			case FINISH_VALUE: return FINISH;
			case COMMIT_VALUE: return COMMIT;
			case ROLLBACK_VALUE: return ROLLBACK;
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
	private AreaProcedureCallFunction(int value, String name, String literal) {
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
	
} //AreaProcedureCallFunction
