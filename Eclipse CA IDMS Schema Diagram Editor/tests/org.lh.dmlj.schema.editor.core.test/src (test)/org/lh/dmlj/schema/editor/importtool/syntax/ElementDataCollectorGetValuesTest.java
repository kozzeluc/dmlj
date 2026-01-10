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
package org.lh.dmlj.schema.editor.importtool.syntax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ElementDataCollectorGetValuesTest {
	
	private static SchemaSyntaxWrapper createContext(String... lines) {
		SchemaSyntaxWrapper context = new SchemaSyntaxWrapper();
		for (String line : lines) {
			context.getLines().add(line);
		}
		return context;
	}
	
	@Test
	public void noValuesWhenNoSyntax() {
		SchemaSyntaxWrapper context = createContext();
		
		List<String> values = new ElementDataCollector().getValues(context);
		assertTrue("no values expected when no VALUE clause available", values.isEmpty());
	}

	@Test
	public void noValueDefinition() {
		SchemaSyntaxWrapper context = createContext(
			"*+       03 SELECTION-YEAR-0400",
			"*+           PICTURE IS  9(4)",
			"*+           USAGE IS DISPLAY",
			"*+           ELEMENT LENGTH IS 4",
			"*+           POSITION IS 1",
			"*+           ELEMENT NAME SYNONYM FOR LANGUAGE ASSEMBLER IS COVSELYR",
			"*+           ELEMENT NAME SYNONYM FOR LANGUAGE FORTRAN IS CVSLYR",
			"*+           ."				
		);
		
		List<String> values = new ElementDataCollector().getValues(context);
		assertTrue("no values expected when no VALUE clause available", values.isEmpty());
	}
	
	@Test
	public void singleValueDefinition() {		
		SchemaSyntaxWrapper context = createContext(
			"*+                   88 MASTER-0400",
			"*+                       USAGE IS CONDITION-NAME",
			"*+                       POSITION IS 17",
			"*+                       VALUE IS ( 'M' )",
			"*+                       ELEMENT NAME SYNONYM FOR LANGUAGE ASSEMBLER IS",
			"*+                             COVMASTR",
			"*+                       ELEMENT NAME SYNONYM FOR LANGUAGE FORTRAN IS",
			"*+                             CVMSTR",
			"*+                       ."				
		);
		
		List<String> values = new ElementDataCollector().getValues(context);		
		assertEquals(Arrays.asList("'M'"), values);
	}
	
	@Test
	public void singleRangeValueDefinition() {
		SchemaSyntaxWrapper context = createContext(
			"*+                   88 PROJECT-0460",
			"*+                       USAGE IS CONDITION-NAME",
			"*+                       POSITION IS 1",
			"*+                       VALUE IS ( 'P1' THRU 'P9' )",
			"*+                       ELEMENT NAME SYNONYM FOR LANGUAGE FORTRAN IS",
			"*+                             PROJCT",
			"*+                       ."									
		);
		
		List<String> values = new ElementDataCollector().getValues(context);		
		assertEquals(Arrays.asList("'P1' THRU 'P9'"), values);
	}
	
	@Test
	public void multipleValueDefinitions() {
		SchemaSyntaxWrapper context = createContext(
			"*+                   88 ELX",
			"*+                       USAGE IS CONDITION-NAME",
			"*+                       POSITION IS 105",
			"*+                       VALUE IS ( 'abc'",
			"*+                                  'def'",
			"*+                                  'ghi' )",
			"*+                       ."									
		);
		
		List<String> values = new ElementDataCollector().getValues(context);		
		assertEquals(Arrays.asList("'abc'","'def'","'ghi'"), values);
	}
	
	@Test
	public void multipleRangeValueDefinitions() {
		SchemaSyntaxWrapper context = createContext(
			"*+                   88 ELX",
			"*+                       USAGE IS CONDITION-NAME",
			"*+                       POSITION IS 105",
			"*+                       VALUE IS ( 'abc' THRU 'def'",
			"*+                                  'ghi' THRU 'jkl'",
			"*+                                  'mno' THRU 'pqr' )",
			"*+                       ."									
		);
		
		List<String> values = new ElementDataCollector().getValues(context);		
		assertEquals(Arrays.asList("'abc' THRU 'def'","'ghi' THRU 'jkl'","'mno' THRU 'pqr'"), values);
	}
	
	@Test
	public void multipleMixedValueDefinitions() {
		SchemaSyntaxWrapper context = createContext(
			"*+                   88 ELX",
			"*+                       USAGE IS CONDITION-NAME",
			"*+                       POSITION IS 105",
			"*+                       VALUE IS ( 'abc'",
			"*+                                  'ghi' THRU 'jkl'",
			"*+                                  'mno' THRU 'pqr' )",
			"*+                       ."									
		);
		
		List<String> values = new ElementDataCollector().getValues(context);		
		assertEquals(Arrays.asList("'abc'","'ghi' THRU 'jkl'","'mno' THRU 'pqr'"), values);
	}
	
}
