package org.lh.dmlj.schema.editor.common;




public abstract class NamingConventions {
		
	private static final String AT_SIGN = "@";
	private static final String DIGITS = "0123456789";
	private static final String DOLLAR_SIGN = "$";
	private static final String HYPHEN = "-";
	private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String POUND_SIGN = "#";
	private static final String UNDERSCORE = "_";

	public static enum Type {LOGICAL_AREA_NAME, PHYSICAL_AREA_NAME, RECORD_NAME, 
						     SCHEMA_NAME, SET_NAME, SYMBOLIC_DISPLACEMENT};	
		
	static boolean checkValidCharacters(String name, 
												String validCharacters) {
		for (int i = 0; i < name.length(); i++) {
			if (validCharacters.indexOf(name.substring(i, i + 1)) < 0) {
				return false;
			}
		}
		return true;
	}
	
	public static ValidationResult validate(String name, Type type) {
		if (type == Type.LOGICAL_AREA_NAME) {
			return validateLogicalAreaName(name);
		} else if (type == Type.PHYSICAL_AREA_NAME) {
			return validatePhysicalDdlEntityName(name, type);
		} else if (type == Type.RECORD_NAME) {
			return validateRecordName(name);
		} else if (type == Type.SCHEMA_NAME) {
			return validateSchemaName(name);
		} else if (type == Type.SET_NAME) {
			return validateSetName(name);
		} else if (type == Type.SYMBOLIC_DISPLACEMENT) {
			return validatePhysicalDdlEntityName(name, type);
		} else {
			throw new IllegalArgumentException("type not supported: " + type);
		}
	}
	
	private static ValidationResult validateLogicalAreaName(String name) {
		
		// The number of characters is limited to 16, check that first
		if (name == null || name.length() < 1 || name.length() > 16) {
			String message = "number of characters is limited to 16";
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}
		
		// the rest of the validation work is the same as for physical DDL 
		// entities
		return validatePhysicalDdlEntityName(name, Type.PHYSICAL_AREA_NAME);
		
	}

	/**
	 * Validates a physical DDL entity name.
	 * @param name the name to be validated
	 * @param type the type of name
	 * @return the validation result
	 */
	static ValidationResult validatePhysicalDdlEntityName(String name, 
														  Type type) {
		
		// The number of characters is limited to 18 (we don't support DBNAME, 
		// DBTABLE, DMCL and SEGMENT).
		if (name == null || name.length() < 1 || name.length() > 18) {
			String message = "number of characters is limited to 18";
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}		
		
		// Valid Characters: A physical DDL entity name consists of a 
		// combination of:
		// - Upper case letters (A through Z)
		// - Digits (0 through 9)
		// - At sign (@)
		// - Dollar sign ($)
		// - Pound sign (#)
		// - Hyphen (-) or underscore (_), but not both		
		if (!checkValidCharacters(name, LETTERS + DIGITS + AT_SIGN + 
								  DOLLAR_SIGN + POUND_SIGN + HYPHEN + 
								  UNDERSCORE)) {
			String message = "valid characters are upper case letters, " +
					 		 "digits, '@', '$', '#', '-' and '_'";			
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}
		if (name.indexOf(HYPHEN) > -1 && name.indexOf(UNDERSCORE) > -1) {
			String message = "contains both '-' and '_'";
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}
		
		// The first character of an identifier must be a letter, @, $, or #.
		String firstChar = name.substring(0, 1).toUpperCase();
		String validFirstChars = LETTERS + AT_SIGN + DOLLAR_SIGN + POUND_SIGN; 
		if (validFirstChars.indexOf(firstChar) == -1) {
			String message = "first character must be a letter, @, $, or #";
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}
		
		// name is valid
		return new ValidationResult(ValidationResult.Status.OK);
		
	}
	
	private static ValidationResult validateRecordName(String name) {
		
		// name must be a 1- to 16-character name.		
		if (name == null || name.length() < 1 || name.length() > 16) {				
			String message = "must be a 1- to 16-character value";
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}
		
		// The first character must be A through Z (alphabetic), #, $, or @ 
		// (international symbols).
		String firstChar = name.substring(0, 1).toUpperCase();
		String validFirstChars = LETTERS + POUND_SIGN + DOLLAR_SIGN + AT_SIGN;
		if (validFirstChars.indexOf(firstChar) == -1) {
			String message = "first character must be A through Z " +
							 "(alphabetic), #, $, or @";
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}
		
		// The remaining characters can be alphabetic or international symbols,  
		// 0 through 9, or the hyphen (except as the last character or following 
		// another hyphen).
		String validNextChars = 
			LETTERS + DIGITS + POUND_SIGN + DOLLAR_SIGN + AT_SIGN + HYPHEN;
		for (int i = 1; i < name.length(); i++) {
			String remainingChar = name.substring(i, i + 1).toUpperCase();
			if (validNextChars.indexOf(remainingChar) == -1) {
				String message = 
					"remaining characters can only be alphabetic or " +
					"international symbols (#, $, or @), 0 through 9, or " +
					"the hyphen";
				return new ValidationResult(ValidationResult.Status.ERROR, 
											message);
			}
		}
		if (name.endsWith("-") || name.indexOf("--") > -1) {			
			String message = "the hyphen can not be the last character " +
							 "or follow another hyphen";
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}
		
		// name is valid
		return new ValidationResult(ValidationResult.Status.OK);
	}

	static ValidationResult validateSchemaName(String name) {		
		
		// name must be a 1- to 8-character value		
		if (name == null || name.length() < 1 || name.length() > 8) {
			String message = "must be a 1- to 8-character value";
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}
		
		// name is valid
		return new ValidationResult(ValidationResult.Status.OK);
	}

	private static ValidationResult validateSetName(String name) {
		
		// name must be a 1- to 16-character name.		
		if (name == null || name.length() < 1 || name.length() > 16) {				
			String message = "must be a 1- to 16-character value";
			return new ValidationResult(ValidationResult.Status.ERROR, message);
		}
		
		// name is valid
		return new ValidationResult(ValidationResult.Status.OK);
	}
	
}