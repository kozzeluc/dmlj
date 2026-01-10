/**
 * Copyright (C) 2025  Luc Hermans
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
package org.lh.dmlj.schema.editor.common;

import java.util.stream.IntStream;

public final class NamingConventions {
	public enum Type { ELEMENT_NAME, LOGICAL_AREA_NAME, PHYSICAL_AREA_NAME, PRIMARY_RECORD_NAME, PROCEDURE_NAME,
		RECORD_ID, RECORD_NAME, SCHEMA_NAME, SET_NAME, SYMBOLIC_DISPLACEMENT, SYMBOLIC_INDEX_NAME, VERSION_SPECIFICATION }
	
	private static final String FIRST_CHARACTER_MUST_BE_ETC = "first character must be A through Z (alphabetic), #, $, or @";
	private static final String AT_SIGN = "@";
	private static final String DIGITS = "0123456789";
	private static final String DOLLAR_SIGN = "$";
	private static final String HYPHEN = "-";
	private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String POUND_SIGN = "#";
	private static final String UNDERSCORE = "_";
		
	static boolean checkValidCharacters(String name, String validCharacters) {
		return IntStream.range(0, name.length())
				.mapToObj(i -> name.substring(i, i + 1))
				.allMatch(validCharacters::contains);
	}
	
	public static ValidationResult validate(String value, Type type) {
		return switch (type) {
			case ELEMENT_NAME -> validateElementName(value);
			case LOGICAL_AREA_NAME -> validateLogicalAreaName(value);
			case PHYSICAL_AREA_NAME -> validatePhysicalDdlEntityName(value);
			case RECORD_NAME -> validateRecordName(value);
			case PRIMARY_RECORD_NAME -> validatePrimaryRecordName(value);
			case PROCEDURE_NAME ->  validateProcedureName(value);
			case SCHEMA_NAME -> validateSchemaName(value);
			case SET_NAME -> validateSetName(value);
			case SYMBOLIC_DISPLACEMENT, SYMBOLIC_INDEX_NAME -> validatePhysicalDdlEntityName(value);
			default -> throw new IllegalArgumentException("type not supported: " + type);
		};
	}
	
	public static ValidationResult validate(short value, Type type) {
		return switch (type) {
			case RECORD_ID -> validateRecordId(value);
			case VERSION_SPECIFICATION -> validateVersionSpecification(value);
			default -> throw new IllegalArgumentException("type not supported: " + type);
		};
	}
	
	private static ValidationResult validateElementName(String name) {
		// name must be a 1- to 32-character name.							*** ONLY CHECK ***		
		if (name == null || name.isEmpty() || name.length() > 32) {				
			var message = "must be a 1- to 32-character value";
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}
		
		// SUPPRESSED CHECKS:
		// - The first character must be A through Z (alphabetic), #, $, or @ (international symbols).
		// - The remaining characters can be alphabetic or international symbols, 0 through 9, or the hyphen
		//   (except as the last character or following another hyphen).
		
		// name is valid
		return new ValidationResult(ValidationResult.Status.OK);
	}	
	
	private static ValidationResult validateLogicalAreaName(String name) {
		// The number of characters is limited to 16, check that first
		if (name == null || name.isEmpty() || name.length() > 16) {
			var message = "number of characters is limited to 16";
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}
		
		// the rest of the validation work is the same as for physical DDL entities
		return validatePhysicalDdlEntityName(name);
	}

	/**
	 * Validates a physical DDL entity name.
	 * @param name the name to be validated
	 * @return the validation result
	 */
	private static ValidationResult validatePhysicalDdlEntityName(String name) {
		// The number of characters is limited to 18 (we don't support DBNAME, DBTABLE, DMCL and SEGMENT).
		if (name == null || name.isEmpty() || name.length() > 18) {
			var message = "number of characters is limited to 18";
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}		
		
		// Valid Characters: A physical DDL entity name consists of a combination of:
		// - Upper case letters (A through Z)
		// - Digits (0 through 9)
		// - At sign (@)
		// - Dollar sign ($)
		// - Pound sign (#)
		// - Hyphen (-) or underscore (_), but not both		
		if (!checkValidCharacters(name, LETTERS + DIGITS + AT_SIGN + DOLLAR_SIGN + POUND_SIGN + HYPHEN + UNDERSCORE)) {
			var message = "valid characters are upper case letters, digits, '@', '$', '#', '-' and '_'";			
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}
		if (name.contains(HYPHEN) && name.contains(UNDERSCORE)) {
			var message = "contains both '-' and '_'";
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}
		
		// The first character of an identifier must be a letter, @, $, or #.
		var firstChar = name.substring(0, 1).toUpperCase();
		var validFirstChars = LETTERS + AT_SIGN + DOLLAR_SIGN + POUND_SIGN; 
		if (!validFirstChars.contains(firstChar)) {
			var message = "first character must be a letter, @, $, or #";
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}
		
		// name is valid
		return new ValidationResult(ValidationResult.Status.OK);
		
	}
	
	private static ValidationResult validateProcedureName(String name) {
		// name must be a 1- to 8-character name.		
		if (name == null || name.isEmpty() || name.length() > 8) {				
			var message = "must be a 1- to 8-character value";
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}
		
		// The first character must be A through Z (alphabetic), #, $, or @ (international symbols).
		var firstChar = name.substring(0, 1).toUpperCase();
		var validFirstChars = LETTERS + POUND_SIGN + DOLLAR_SIGN + AT_SIGN;
		if (!validFirstChars.contains(firstChar)) {
			var message = FIRST_CHARACTER_MUST_BE_ETC;
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}
		
		// The remaining characters can be alphabetic or international symbols, or 0 through 9.
		var validNextChars = LETTERS + DIGITS + POUND_SIGN + DOLLAR_SIGN + AT_SIGN;
		for (var i = 1; i < name.length(); i++) {
			var remainingChar = name.substring(i, i + 1).toUpperCase();
			if (!validNextChars.contains(remainingChar)) {
				var message = "remaining characters can only be alphabetic or international symbols (#, $, or @) or 0 through 9";
				return new ValidationResult(ValidationResult.Status.ERROR, message);
			}
		}		
		
		// name is valid
		return new ValidationResult(ValidationResult.Status.OK);
	}	
	
	private static ValidationResult validateRecordId(short recordId) {
		// recordId must be an unsigned integer in the range 10 through 9999. 
		if (recordId < 10 || recordId > 9999) {
			var message = "must be an unsigned integer in the range 10 through 9999";	
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}		
		
		// recordId is valid
		return new ValidationResult(ValidationResult.Status.OK);
	}
	
	private static ValidationResult validateVersionSpecification(short versionSpecification) {
		// versionSpecification must be an unsigned integer in the range 1 through 9999. 
		if (versionSpecification < 1 || versionSpecification > 9999) {
			var message = "must be an unsigned integer in the range 1 through 9999";	
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}		
		
		// versionSpecification is valid
		return new ValidationResult(ValidationResult.Status.OK);
	}
	
	private static ValidationResult validatePrimaryRecordName(String name) {
		// name must be a 1- through 32-character alphanumeric value.
		if (name == null || name.isEmpty() || name.length() > 32) {				
			var message = "must be a 1- through 32-character alphanumeric value";
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}
		
		// the following restrictions are assumed but might be not be completely correct:
		
		// The first character must be A through Z (alphabetic), #, $, or @ (international symbols).
		var firstChar = name.substring(0, 1).toUpperCase();
		var validFirstChars = LETTERS + POUND_SIGN + DOLLAR_SIGN + AT_SIGN;
		if (!validFirstChars.contains(firstChar)) {
			var message = FIRST_CHARACTER_MUST_BE_ETC;
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}
		
		// The remaining characters can be alphabetic or international symbols, 0 through 9, or the hyphen
		// (except as the last character or following another hyphen).
		var validNextChars = LETTERS + DIGITS + POUND_SIGN + DOLLAR_SIGN + AT_SIGN + HYPHEN;
		for (var i = 1; i < name.length(); i++) {
			var remainingChar = name.substring(i, i + 1).toUpperCase();
			if (!validNextChars.contains(remainingChar)) {
				String message = "remaining characters can only be alphabetic or international symbols (#, $, or @), 0 through 9, or the hyphen";
				return new ValidationResult(ValidationResult.Status.ERROR, message);
			}
		}
		if (name.endsWith("-") || name.contains("--")) {			
			var message = "the hyphen can not be the last character or follow another hyphen";
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}
		
		// name is valid
		return new ValidationResult(ValidationResult.Status.OK);
	}	

	private static ValidationResult validateRecordName(String name) {
		// name must be a 1- to 18-character name.  (the normal limit is 16 characters, but some catalog tables
		// contain more than 16 characters (e.g. RESOURCEGROUP-1053)
		if (name == null || name.isEmpty() || name.length() > 18) {				
			var message = "must be a 1- to 18-character value";
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}
		
		// The first character must be A through Z (alphabetic), #, $, or @ (international symbols).
		var firstChar = name.substring(0, 1).toUpperCase();
		var validFirstChars = LETTERS + POUND_SIGN + DOLLAR_SIGN + AT_SIGN;
		if (!validFirstChars.contains(firstChar)) {
			var message = FIRST_CHARACTER_MUST_BE_ETC;
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}
		
		// The remaining characters can be alphabetic or international symbols, 0 through 9, or the hyphen
		// (except as the last character or following another hyphen).
		var validNextChars = LETTERS + DIGITS + POUND_SIGN + DOLLAR_SIGN + AT_SIGN + HYPHEN;
		for (var i = 1; i < name.length(); i++) {
			var remainingChar = name.substring(i, i + 1).toUpperCase();
			if (!validNextChars.contains(remainingChar)) {
				var message = "remaining characters can only be alphabetic or international symbols (#, $, or @), 0 through 9, or the hyphen";
				return new ValidationResult(ValidationResult.Status.ERROR, message);
			}
		}
		if (name.endsWith("-") || name.contains("--")) {			
			var message = "the hyphen can not be the last character or follow another hyphen";
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}
		
		// name is valid
		return new ValidationResult(ValidationResult.Status.OK);
	}

	private static ValidationResult validateSchemaName(String name) {	
		// name must be a 1- to 8-character value		
		if (name == null || name.isEmpty() || name.length() > 8) {
			var message = "must be a 1- to 8-character value";
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}
		
		// name is valid
		return new ValidationResult(ValidationResult.Status.OK);
	}

	private static ValidationResult validateSetName(String name) {
		// name must be a 1- to 18-character name.  (the normal limit is 16 characters, but some catalog
		// constraints contain more than 16 characters (e.g. REFERENCING-TABLE))		
		if (name == null || name.isEmpty() || name.length() > 18) {				
			var message = "must be a 1- to 18-character value";
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}
		
		// name is valid
		return new ValidationResult(ValidationResult.Status.OK);
	}
	
	private NamingConventions() {
	}
	
}
