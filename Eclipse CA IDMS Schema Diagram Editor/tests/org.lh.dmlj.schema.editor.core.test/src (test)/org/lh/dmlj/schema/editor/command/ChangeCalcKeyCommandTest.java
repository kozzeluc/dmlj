/**
 * Copyright (C) 2015  Luc Hermans
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
package org.lh.dmlj.schema.editor.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.lh.dmlj.schema.editor.testtool.TestTools.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.testtool.ObjectGraph;
import org.lh.dmlj.schema.editor.testtool.Syntax;
import org.lh.dmlj.schema.editor.testtool.TestTools;
import org.lh.dmlj.schema.editor.testtool.Xmi;

public class ChangeCalcKeyCommandTest {

	@Test
	public void test() {
		
		// get the EMPSCHM version 1 schema from the testdata folder; locate the EMPLOYEE record
		Schema schema = TestTools.getEmpschmSchema();
		SchemaRecord record = schema.getRecord("EMPLOYEE");
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);
		Syntax syntax = TestTools.asSyntax(schema);
		Key calcKey = record.getCalcKey();		
		assertNull(calcKey.getMemberRole());
		assertEquals("EMP-ID-0415", calcKey.getElementSummary());
		
		// we will change the record's CALC key to contain both EMP-NAME-0415 and EMP-ID-0415 (in
		// that sequence) with duplicates first
		Element empName = record.getElement("EMP-NAME-0415");
		assertNotNull(empName);
		Element empId = record.getElement("EMP-ID-0415");
		assertNotNull(empId);
		List<Element> calcKeyElements = new ArrayList<>();
		calcKeyElements.add(empName);
		calcKeyElements.add(empId);
		
		// create the command 
		ChangeCalcKeyCommand command = 
			new ChangeCalcKeyCommand(record, calcKeyElements, DuplicatesOption.FIRST);
		
		
		// execute the command and check the CALC key
		command.execute();
		ObjectGraph objectGraph2 = TestTools.asObjectGraph(schema);
		Xmi xmi2 = TestTools.asXmi(schema);
		Syntax syntax2 = TestTools.asSyntax(schema);
		Key calcKey2 = record.getCalcKey();
		assertNotNull(calcKey2);
		assertNotSame(calcKey, calcKey2);
		assertEquals(2, calcKey2.getElements().size());
		assertSame(empName, calcKey2.getElements().get(0).getElement());
		assertSame(empId, calcKey2.getElements().get(1).getElement());
		assertSame(DuplicatesOption.FIRST, calcKey2.getDuplicatesOption());		
		assertNull(calcKey2.getMemberRole());	
		assertEquals("EMP-NAME-0415, EMP-ID-0415", calcKey2.getElementSummary());		
		
		
		// undo the command and check the CALC key
		command.undo();
		ObjectGraph objectGraph3 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph, objectGraph3);
		Xmi xmi3 = TestTools.asXmi(schema);
		assertEquals(xmi, xmi3);
		Syntax syntax3 = TestTools.asSyntax(schema);
		assertEquals(syntax, syntax3);		
		assertSame(calcKey, record.getCalcKey());		
		assertEquals(1, calcKey.getElements().size());
		assertSame(empId, calcKey.getElements().get(0).getElement());
		assertSame(DuplicatesOption.NOT_ALLOWED, calcKey.getDuplicatesOption());
		assertNull(calcKey.getMemberRole());
		assertEquals("EMP-ID-0415", calcKey.getElementSummary());
		
		
		// redo the command and check the CALC key
		command.redo();
		ObjectGraph objectGraph4 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph2, objectGraph4);
		Xmi xmi4 = TestTools.asXmi(schema);
		assertEquals(xmi2, xmi4);
		Syntax syntax4 = TestTools.asSyntax(schema);
		assertEquals(syntax2, syntax4);				
		assertSame(calcKey2, record.getCalcKey());
		assertEquals(2, calcKey2.getElements().size());
		assertSame(empName, calcKey2.getElements().get(0).getElement());
		assertSame(empId, calcKey2.getElements().get(1).getElement());
		assertSame(DuplicatesOption.FIRST, calcKey2.getDuplicatesOption());
		assertNull(calcKey2.getMemberRole());
		assertEquals("EMP-NAME-0415, EMP-ID-0415", calcKey2.getElementSummary());		
	}

}
