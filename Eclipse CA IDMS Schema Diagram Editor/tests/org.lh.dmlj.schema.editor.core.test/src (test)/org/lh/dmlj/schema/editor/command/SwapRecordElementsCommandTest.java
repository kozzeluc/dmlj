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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.lh.dmlj.schema.editor.testtool.TestTools.asObjectGraph;
import static org.lh.dmlj.schema.editor.testtool.TestTools.asSyntax;
import static org.lh.dmlj.schema.editor.testtool.TestTools.asXmi;
import static org.lh.dmlj.schema.editor.testtool.TestTools.assertEquals;
import static org.lh.dmlj.schema.editor.testtool.TestTools.getEmpschmSchema;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.testtool.ObjectGraph;
import org.lh.dmlj.schema.editor.testtool.Syntax;
import org.lh.dmlj.schema.editor.testtool.Xmi;

public class SwapRecordElementsCommandTest {

	@Test
	public void testNoElementsError() {
		try {
			Schema schema = getEmpschmSchema();
			SwapRecordElementsCommand command = 
				new SwapRecordElementsCommand(schema.getRecord("EMPLOYEE"), new ArrayList<Element>());		
			command.execute();
			fail("should throw a RuntimeException");
		} catch (RuntimeException e) {
			assertEquals("record should contain at least 1 element: EMPLOYEE", e.getMessage());
		}
	}
	
	@Test
	public void testContainsKeysError() {
		try {
			Schema schema = getEmpschmSchema();
			SwapRecordElementsCommand command = 
				new SwapRecordElementsCommand(schema.getRecord("EMPLOYEE"), 
											  schema.getRecord("COVERAGE").getRootElements());		
			command.execute();
			fail("should throw a RuntimeException");
		} catch (RuntimeException e) {
			assertEquals("record should NOT contain any keys: EMPLOYEE", e.getMessage());
		}
	}	
	
	@Test
	public void testSwap() {
		
		Schema schema = getEmpschmSchema();
		ObjectGraph objectGraph = asObjectGraph(schema);
		Xmi xmi = asXmi(schema);
		Syntax syntax = asSyntax(schema);
		
		SchemaRecord coverage = schema.getRecord("COVERAGE");
				
		List<Element> newRootElements = new ArrayList<>();
		Element root = SchemaFactory.eINSTANCE.createElement();
		Element child = SchemaFactory.eINSTANCE.createElement();
		root.getChildren().add(child);
		newRootElements.add(root);
		
		SwapRecordElementsCommand command = new SwapRecordElementsCommand(coverage, newRootElements);		
		
		command.execute();
		ObjectGraph objectGraph2 = asObjectGraph(schema);
		Xmi xmi2 = asXmi(schema);
		Syntax syntax2 = asSyntax(schema);
		assertEquals(1, coverage.getRootElements().size());
		assertSame(root, coverage.getRootElements().get(0));
		assertEquals(2, coverage.getElements().size());
		assertSame(root, coverage.getElements().get(0));
		assertSame(child, coverage.getElements().get(1));
		
		
		command.undo();
		assertEquals(objectGraph, asObjectGraph(schema));
		assertEquals(xmi, asXmi(schema));
		assertEquals(syntax, asSyntax(schema));
		
		
		command.redo();
		assertEquals(objectGraph2, asObjectGraph(schema));
		assertEquals(xmi2, asXmi(schema));
		assertEquals(syntax2, asSyntax(schema));
		
	}

}
