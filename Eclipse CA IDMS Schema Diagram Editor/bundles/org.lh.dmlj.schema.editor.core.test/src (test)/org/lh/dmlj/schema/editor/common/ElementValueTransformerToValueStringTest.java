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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class ElementValueTransformerToValueStringTest {
	
	@Test
	public void nullReturnedForNullList() {
		String valueString = ElementValueTransformer.toValueString(null);
		assertNull("null expected when valueList is null", valueString);
	}
	
	@Test
	public void nullReturnedForEmptyList() {
		String valueString = ElementValueTransformer.toValueString(Collections.emptyList());
		assertNull("null expected when valueList is null", valueString);
	}
	
	@Test
	public void noDelimiterPrefixForSingleValue() {
		String valueString = ElementValueTransformer.toValueString(Arrays.asList(new String[] { "'abc'" }));
		assertEquals("'abc'", valueString);
	}
	
	@Test
	public void singleCommaDelimiterPrefixForMultipleValuesNotContainingComma() {
		String valueString = ElementValueTransformer.toValueString(Arrays.asList(new String[] { "'abc'", "'def'" }));
		assertEquals("list:,:'abc','def'", valueString);
	}
	
	@Test
	public void doubleCommaDelimiterPrefixForMultipleValuesContainingSingleComma() {
		String valueString = ElementValueTransformer.toValueString(Arrays.asList(new String[] { "'abc'", "','" }));
		assertEquals("list:,,:'abc',,','", valueString);
	}
	
	@Test
	public void tripleCommaDelimiterPrefixForMultipleValuesContainingDoubleComma() {
		String valueString = ElementValueTransformer.toValueString(Arrays.asList(new String[] { "'abc'", "',,'" }));
		assertEquals("list:,,,:'abc',,,',,'", valueString);
	}

}
