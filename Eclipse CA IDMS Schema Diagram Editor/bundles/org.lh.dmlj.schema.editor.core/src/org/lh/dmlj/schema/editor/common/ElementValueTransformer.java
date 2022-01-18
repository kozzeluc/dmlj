/**
 * Copyright (C) 2021  Luc Hermans
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ElementValueTransformer {
	private static final String COLON = ":";
	private static final String COMMA = ",";
	private static final String INITIAL_DELIMITER_PART = "list" + COLON;
	private static final String INITIAL_DELIMITER_PART_PLUS_COMMA = INITIAL_DELIMITER_PART + COMMA;


	private static String calculateDelimiterChars(List<String> values) {
		StringBuilder commas = new StringBuilder(COMMA);
		while (values.stream().anyMatch(value -> value.contains(commas.toString()))) {
			commas.append(COMMA);
		}
		return commas.toString();
	}
	
	private static String extractDelimiterChars(String value) {
		if (value.startsWith(INITIAL_DELIMITER_PART_PLUS_COMMA)) {
			int secondColonOffset = value.indexOf(COLON, 5);
			if (secondColonOffset > -1) {
				return value.substring(5, secondColonOffset);
			}
		}	
		return null;
	}
		
	public static String toValueString(List<String> values) {
		if (values != null && !values.isEmpty()) {
			if (values.size() == 1) {
				return values.get(0);
			} else {
				String delimiterChars = calculateDelimiterChars(values);
				String delimiterPrefix = INITIAL_DELIMITER_PART + delimiterChars + COLON;
				return delimiterPrefix + values.stream().collect(Collectors.joining(delimiterChars));
			}
		} else {
			return null;
		}
	}
	

	public static List<String> toValueList(String value) {
		if  (value != null && !value.trim().isEmpty()) {
			String delimiterChars = extractDelimiterChars(value);
			if (delimiterChars == null) {
				return Arrays.asList(value);
			} else {
				String delimiterPrefix = INITIAL_DELIMITER_PART + delimiterChars + COLON;
				return Arrays.asList(value.substring(delimiterPrefix.length()).split(delimiterChars));
			}
		} else {
			return Collections.emptyList();
		}
	}

}
