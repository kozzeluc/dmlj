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

import static org.junit.Assert.*;
import static org.lh.dmlj.schema.editor.testtool.TestTools.assertEquals;

import org.eclipse.gef.commands.Command;
import org.junit.Test;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.ViaSpecification;
import org.lh.dmlj.schema.editor.testtool.ObjectGraph;
import org.lh.dmlj.schema.editor.testtool.Syntax;
import org.lh.dmlj.schema.editor.testtool.TestTools;
import org.lh.dmlj.schema.editor.testtool.Xmi;

public class MakeRecordDirectCommandTest {

	@Test
	public void test_CalcRecord() {
		
		// let's make the EMPLOYEE record DIRECT in this test
		Schema schema = TestTools.getEmpschmSchema();
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);
		Syntax syntax = TestTools.asSyntax(schema);
		SchemaRecord record = schema.getRecord("EMPLOYEE");
		assertSame(LocationMode.CALC, record.getLocationMode());
		Key calcKey = record.getCalcKey();
		assertNotNull(calcKey);
		assertNull(record.getViaSpecification());
		int keyCount = record.getKeys().size();
		int calcKeyIndex = record.getKeys().indexOf(calcKey);
		
		// create the command
		Command command = new MakeRecordDirectCommand(record);
		
		
		// execute the command and verify
		command.execute();
		ObjectGraph objectGraph2 = TestTools.asObjectGraph(schema);
		Xmi xmi2 = TestTools.asXmi(schema);	
		Syntax syntax2 = TestTools.asSyntax(schema);
		assertSame(LocationMode.DIRECT, record.getLocationMode());
		assertNull(record.getCalcKey());
		assertNull(record.getViaSpecification());
		assertEquals(keyCount - 1, record.getKeys().size());
		
		
		// undo the command and verify
		command.undo();
		ObjectGraph objectGraph3 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph, objectGraph3);
		Syntax syntax3 = TestTools.asSyntax(schema);
		assertEquals(syntax, syntax3);
		Xmi xmi3 = TestTools.asXmi(schema);	
		assertEquals(xmi, xmi3);
		assertSame(calcKey, record.getCalcKey());
		assertEquals(calcKeyIndex, record.getKeys().indexOf(calcKey));		
		
		
		// redo the command and verify
		command.redo();
		ObjectGraph objectGraph4 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph2, objectGraph4);
		Xmi xmi4 = TestTools.asXmi(schema);
		assertEquals(xmi2, xmi4);
		Syntax syntax4 = TestTools.asSyntax(schema);
		assertEquals(syntax2, syntax4);		
	}
	
	@Test
	public void test_ViaRecord() {
		
		// let's make the COVERAGE record DIRECT in this test
		Schema schema = TestTools.getEmpschmSchema();
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);
		Syntax syntax = TestTools.asSyntax(schema);
		SchemaRecord record = schema.getRecord("COVERAGE");
		assertSame(LocationMode.VIA, record.getLocationMode());
		assertNull(record.getCalcKey());
		ViaSpecification viaSpecification = record.getViaSpecification();
		assertNotNull(viaSpecification);	
		Set set = record.getViaSpecification().getSet();
		int viaSpecificationIndex = set.getViaMembers().indexOf(viaSpecification);
				
		// create the command
		Command command = new MakeRecordDirectCommand(record);
		
		
		// execute the command and verify
		command.execute();
		ObjectGraph objectGraph2 = TestTools.asObjectGraph(schema);
		Xmi xmi2 = TestTools.asXmi(schema);	
		Syntax syntax2 = TestTools.asSyntax(schema);
		assertSame(LocationMode.DIRECT, record.getLocationMode());
		assertNull(record.getCalcKey());
		assertNull(record.getViaSpecification());
		assertEquals(-1, set.getViaMembers().indexOf(viaSpecification));
		
		
		// undo the command and verify
		command.undo();
		ObjectGraph objectGraph3 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph, objectGraph3);
		Syntax syntax3 = TestTools.asSyntax(schema);
		assertEquals(syntax, syntax3);
		Xmi xmi3 = TestTools.asXmi(schema);	
		assertEquals(xmi, xmi3);
		assertSame(viaSpecification, record.getViaSpecification());	
		assertEquals(viaSpecificationIndex, set.getViaMembers().indexOf(viaSpecification));
		
		
		// redo the command and verify
		command.redo();
		ObjectGraph objectGraph4 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph2, objectGraph4);
		Xmi xmi4 = TestTools.asXmi(schema);
		assertEquals(xmi2, xmi4);
		Syntax syntax4 = TestTools.asSyntax(schema);
		assertEquals(syntax2, syntax4);
		
	}	
	
	@Test
	public void test_VsamRecord() {
		fail("not yet implemented");
	}
	
	@Test
	public void test_VsamCalcRecord() {
		fail("not yet implemented");
	}

}
