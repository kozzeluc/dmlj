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
package org.lh.dmlj.schema.editor.dictionary.tools.importtool.collector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Namesyn_083;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sdes_044;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sdr_042;

public class DictionaryElementDataCollectorGetValuesTest {
	
	private static Namesyn_083 createNamesyn_083(Sdes_044... sdes_044s) {
		Namesyn_083 namesyn_083 = new Namesyn_083();
		Sdr_042 sdr_042 = new Sdr_042();
		namesyn_083.setSdr_042(sdr_042);
		sdr_042.getSdes_044s().addAll(Arrays.asList(sdes_044s));
		return namesyn_083;
	}
	
	private static Sdes_044 createSdes_044(String val1_044, String val2_044) {
		Sdes_044 sdes_044 = mock(Sdes_044.class);		
		when(sdes_044.getCmtId_044()).thenReturn(-3);
		when(sdes_044.getVal1_044()).thenReturn(val1_044);
		when(sdes_044.getVal2_044()).thenReturn(val2_044);
		return sdes_044;
	}
	
	private static Sdes_044 createIrrelevantSdes_044(int cmdId_044) {
		Sdes_044 sdes_044 = mock(Sdes_044.class);		
		when(sdes_044.getCmtId_044()).thenReturn(cmdId_044);
		return sdes_044;
	}

	@Test
	public void noValueDefinition() {
		Namesyn_083 namesyn_083 = createNamesyn_083();
		
		List<String> values = new DictionaryElementDataCollector().getValues(namesyn_083);
		assertTrue("no values expected when no SDES-044 records available", values.isEmpty());
	}
	
	@Test
	public void singleValueDefinition() {		
		Sdes_044 sdes_044 = createSdes_044("'xyz'", "");
		Namesyn_083 namesyn_083 = createNamesyn_083(sdes_044);
		
		List<String> values = new DictionaryElementDataCollector().getValues(namesyn_083);		
		assertEquals(Arrays.asList("'xyz'"), values);
	}
	
	@Test
	public void singleRangeValueDefinition() {
		Sdes_044 sdes_044 = createSdes_044("'abc'", "'xyz'");
		Namesyn_083 namesyn_083 = createNamesyn_083(sdes_044);
		
		List<String> values = new DictionaryElementDataCollector().getValues(namesyn_083);		
		assertEquals(Arrays.asList("'abc' THRU 'xyz'"), values);
	}
	
	@Test
	public void multipleValueDefinitions() {
		Sdes_044 sdes_044_1 = createSdes_044("'abc'", "");
		Sdes_044 sdes_044_2 = createSdes_044("'def'", "");
		Sdes_044 sdes_044_3 = createSdes_044("'ghi'", "");
		Namesyn_083 namesyn_083 = createNamesyn_083(sdes_044_1, sdes_044_2, sdes_044_3);
		
		List<String> values = new DictionaryElementDataCollector().getValues(namesyn_083);		
		assertEquals(Arrays.asList("'abc'","'def'","'ghi'"), values);
	}
	
	@Test
	public void multipleRangeValueDefinitions() {
		Sdes_044 sdes_044_1 = createSdes_044("'abc'", "'def'");
		Sdes_044 sdes_044_2 = createSdes_044("'ghi'", "'jkl'");
		Sdes_044 sdes_044_3 = createSdes_044("'mno'", "'pqr'");
		Namesyn_083 namesyn_083 = createNamesyn_083(sdes_044_1, sdes_044_2, sdes_044_3);
		
		List<String> values = new DictionaryElementDataCollector().getValues(namesyn_083);		
		assertEquals(Arrays.asList("'abc' THRU 'def'","'ghi' THRU 'jkl'","'mno' THRU 'pqr'"), values);
	}
	
	@Test
	public void multipleMixedValueDefinitions() {
		Sdes_044 sdes_044_1 = createSdes_044("'abc'", "");
		Sdes_044 sdes_044_2 = createSdes_044("'ghi'", "'jkl'");
		Sdes_044 sdes_044_3 = createSdes_044("'mno'", "'pqr'");
		Namesyn_083 namesyn_083 = createNamesyn_083(sdes_044_1, sdes_044_2, sdes_044_3);
		
		List<String> values = new DictionaryElementDataCollector().getValues(namesyn_083);		
		assertEquals(Arrays.asList("'abc'","'ghi' THRU 'jkl'","'mno' THRU 'pqr'"), values);
	}
	
	@Test
	public void notAllElementCommentRecordsAreRelevantForValues() {
		Sdes_044 sdes_044_comments = createIrrelevantSdes_044(-1);
		Sdes_044 sdes_044_definition = createIrrelevantSdes_044(-2);
		Sdes_044 sdes_044_olq_header = createIrrelevantSdes_044(-4);
		Sdes_044 sdes_044_culprit_header = createIrrelevantSdes_044(-5);
		Sdes_044 sdes_044_edit_tables = createIrrelevantSdes_044(-8);
		Sdes_044 sdes_044_code_tables = createIrrelevantSdes_044(-9);
		Sdes_044 sdes_044_occurs_depending_on = createIrrelevantSdes_044(-10);
		Sdes_044 sdes_indexed_by = createIrrelevantSdes_044(-11);
		Sdes_044 sdes_index_key = createIrrelevantSdes_044(-12);
		Sdes_044 sdes_asf_header_and_display_sequence_number = createIrrelevantSdes_044(-24);
		Namesyn_083 namesyn_083 = createNamesyn_083(sdes_044_comments, sdes_044_definition, sdes_044_olq_header, 
				sdes_044_culprit_header, sdes_044_edit_tables, sdes_044_code_tables, sdes_044_occurs_depending_on, 
				sdes_indexed_by, sdes_index_key, sdes_asf_header_and_display_sequence_number);
		
		List<String> values = new DictionaryElementDataCollector().getValues(namesyn_083);
		assertTrue("no values expected when only irrelevant SDES-044 records available", values.isEmpty());
	}
	
}
