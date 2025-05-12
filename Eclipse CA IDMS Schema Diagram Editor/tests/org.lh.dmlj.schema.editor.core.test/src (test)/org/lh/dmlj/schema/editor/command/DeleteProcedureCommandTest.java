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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.emf.common.util.EList;
import org.junit.Test;
import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.ProcedureCallSpecification;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaFactory;

public class DeleteProcedureCommandTest {

	@Test
	public void test() {
		Schema schema = SchemaFactory.eINSTANCE.createSchema();
		for (int i = 1; i <= 5; i++) {
			Procedure procedure = SchemaFactory.eINSTANCE.createProcedure();			
			procedure.setName("PROC000" + i);
			schema.getProcedures().add(procedure);
		}
		
		Procedure removedProcedure = schema.getProcedures().get(2);
		DeleteProcedureCommand command = new DeleteProcedureCommand(removedProcedure);
		
		command.execute();
		assertEquals(4, schema.getProcedures().size());
		assertEquals("PROC0001", schema.getProcedures().get(0).getName());
		assertEquals("PROC0002", schema.getProcedures().get(1).getName());
		assertNull(schema.getProcedure("PROC0003"));
		assertEquals("PROC0004", schema.getProcedures().get(2).getName());
		assertEquals("PROC0005", schema.getProcedures().get(3).getName());
		
		command.undo();
		assertEquals(5, schema.getProcedures().size());
		assertEquals("PROC0001", schema.getProcedures().get(0).getName());
		assertEquals("PROC0002", schema.getProcedures().get(1).getName());
		assertSame(removedProcedure, schema.getProcedures().get(2));
		assertEquals("PROC0004", schema.getProcedures().get(3).getName());
		assertEquals("PROC0005", schema.getProcedures().get(4).getName());
		
		command.redo();
		assertEquals(4, schema.getProcedures().size());
		assertEquals("PROC0001", schema.getProcedures().get(0).getName());
		assertEquals("PROC0002", schema.getProcedures().get(1).getName());
		assertNull(schema.getProcedure("PROC0003"));
		assertEquals("PROC0004", schema.getProcedures().get(2).getName());
		assertEquals("PROC0005", schema.getProcedures().get(3).getName());
	}
	
	@Test
	public void testAsserts() {
		Schema schema = mock(Schema.class);
		@SuppressWarnings("unchecked")
		EList<Procedure> procedures = mock(EList.class);
		when(schema.getProcedures()).thenReturn(procedures);
		Procedure procedure = mock(Procedure.class);
		when(procedure.getSchema()).thenReturn(schema);
		when(procedure.getName()).thenReturn("PROC0001");
		when(procedures.indexOf(procedure)).thenReturn(3);
		@SuppressWarnings("unchecked")
		EList<ProcedureCallSpecification> callSpecs = mock(EList.class);
		when(procedure.getCallSpecifications()).thenReturn(callSpecs);
		when(callSpecs.isEmpty()).thenReturn(false);
		
		DeleteProcedureCommand command = new DeleteProcedureCommand(procedure);
		try {
			command.execute();
			fail("assertion should fail");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: Cannot remove procedure because it is still " +
						 "referenced by at least 1 area and/or record: PROC0001", e.getMessage());
		}
		try {
			command.redo();
			fail("assertion should fail");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: Cannot remove procedure because it is still " +
						 "referenced by at least 1 area and/or record: PROC0001", e.getMessage());
		}
		
		when(schema.getProcedure("PROC0001")).thenReturn(mock(Procedure.class));
		try {
			command.undo();
			fail("assertion should fail");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: Cannot add procedure because a procedure with that " +
						 "name is already referenced by the schema: PROC0001", e.getMessage());
		}
	}

}
