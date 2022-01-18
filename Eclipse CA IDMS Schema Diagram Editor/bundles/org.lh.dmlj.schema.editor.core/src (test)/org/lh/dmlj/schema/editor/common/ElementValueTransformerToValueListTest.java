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
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ElementValueTransformerToValueListTest {
	
	@Test
	public void emptyListReturnedForNullValue() {
		List<String> values = ElementValueTransformer.toValueList(null);
		assertTrue("empty list expected when value is null", values.isEmpty());
	}
	
	@Test
	public void emptyListReturnedForEmptyValue() {
		List<String> values = ElementValueTransformer.toValueList("");
		assertTrue("empty list expected when value is null", values.isEmpty());
	}
	
	@Test
	public void emptyListReturnedForBlankValue() {
		List<String> values = ElementValueTransformer.toValueList(" ");
		assertTrue("empty list expected when value is null", values.isEmpty());
	}
	
	@Test
	public void noDelimiterNeededForSingleValue() {
		List<String> valueList = ElementValueTransformer.toValueList("'abc'");
		assertEquals(Arrays.asList(new String[] { "'abc'" }), valueList);
	}
	
	@Test
	public void delimiterAllowedForSingleValue() {
		List<String> valueList = ElementValueTransformer.toValueList("list:,:'abc'");
		assertEquals(Arrays.asList(new String[] { "'abc'" }), valueList);
	}
	
	@Test
	public void delimiterIsMissingSecondColon() {
		List<String> valueList = ElementValueTransformer.toValueList("list:,'abc'");
		assertEquals(Arrays.asList(new String[] { "list:,'abc'" }), valueList);
	}
	
	@Test
	public void singleCommaDelimiterPrefixForMultipleValuesNotContainingComma() {
		List<String> values = ElementValueTransformer.toValueList("list:,:'abc','def'");
		assertEquals(Arrays.asList(new String[] { "'abc'", "'def'" }), values);
	}
	
	@Test
	public void doubleCommaDelimiterPrefixForMultipleValuesContainingSingleComma() {
		List<String> values = ElementValueTransformer.toValueList("list:,,:'abc',,','");
		assertEquals(Arrays.asList(new String[] { "'abc'", "','" }), values);
	}
	
	@Test
	public void tripleCommaDelimiterPrefixForMultipleValuesContainingDoubleComma() {
		List<String> values = ElementValueTransformer.toValueList("list:,,,:'abc',,,',,'");
		assertEquals(Arrays.asList(new String[] { "'abc'", "',,'" }), values);
	}

}
