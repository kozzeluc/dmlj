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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.emf.common.util.EList;
import org.junit.Test;
import org.lh.dmlj.schema.AreaProcedureCallSpecification;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SystemOwner;

public class DeleteAreaCommandTest {

	@Test
	public void test() {
		
		Schema schema = SchemaFactory.eINSTANCE.createSchema();	
		schema.getAreas().add(SchemaFactory.eINSTANCE.createSchemaArea());
		schema.getAreas().add(SchemaFactory.eINSTANCE.createSchemaArea()); // <-- we will delete this area
		schema.getAreas().add(SchemaFactory.eINSTANCE.createSchemaArea());
		List<SchemaArea> allAreas = new ArrayList<>(schema.getAreas());
		
		DeleteAreaCommand command = new DeleteAreaCommand(allAreas.get(1));
		
		command.execute();
		assertEquals(2, schema.getAreas().size());
		assertSame(allAreas.get(0), schema.getAreas().get(0));
		assertTrue(schema.getAreas().indexOf(allAreas.get(1)) < 0);
		assertSame(allAreas.get(2), schema.getAreas().get(1));
		assertNull(allAreas.get(1).getSchema());
		
		command.undo();
		assertEquals(3, schema.getAreas().size());
		assertSame(allAreas.get(0), schema.getAreas().get(0));
		assertSame(allAreas.get(1), schema.getAreas().get(1));
		assertSame(allAreas.get(2), schema.getAreas().get(2));
		assertSame(schema, allAreas.get(1).getSchema());
		
		command.redo();
		assertEquals(2, schema.getAreas().size());
		assertSame(allAreas.get(0), schema.getAreas().get(0));
		assertTrue(schema.getAreas().indexOf(allAreas.get(1)) < 0);
		assertSame(allAreas.get(2), schema.getAreas().get(1));
		assertNull(allAreas.get(1).getSchema());
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testAsserts() {
		
		Schema schema = mock(Schema.class);
		
		SchemaArea area = mock(SchemaArea.class);
		when(area.getSchema()).thenReturn(schema);
		when(area.getName()).thenReturn("MOCKAREA");
		
		EList<SchemaArea> areas = mock(EList.class);		
		when(schema.getAreas()).thenReturn(areas);
		when(areas.indexOf(area)).thenReturn(0);
		
		EList<AreaSpecification> areaSpecifications = mock(EList.class);
		when(area.getAreaSpecifications()).thenReturn(areaSpecifications);
		when(areaSpecifications.isEmpty()).thenReturn(false);
		
		EList<SchemaRecord> records = mock(EList.class);
		when(area.getRecords()).thenReturn(records);
		when(records.isEmpty()).thenReturn(false);
		
		EList<SystemOwner> systemOwners = mock(EList.class);
		when(area.getIndexes()).thenReturn(systemOwners);
		when(records.isEmpty()).thenReturn(false);
		
		EList<AreaProcedureCallSpecification> procedures = mock(EList.class);
		when(area.getProcedures()).thenReturn(procedures);
		when(procedures.isEmpty()).thenReturn(false);
		
		DeleteAreaCommand command = new DeleteAreaCommand(area);
		
		try {
			command.execute();
			fail("assert should have failed but didn't");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: Area is referenced by at least 1 record or system " +
						 "owner: MOCKAREA", e.getMessage());
		}
		
		when(areaSpecifications.isEmpty()).thenReturn(true);		
		
		try {
			command.execute();
			fail("assert should have failed but didn't");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: Area references at least 1 record: MOCKAREA", 
						 e.getMessage());
		}
		
		when(records.isEmpty()).thenReturn(true);
		
		try {
			command.execute();
			fail("assert should have failed but didn't");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: Area references at least 1 system owner: MOCKAREA", 
						 e.getMessage());
		}
		
		when(systemOwners.isEmpty()).thenReturn(true);
		
		try {
			command.execute();
			fail("assert should have failed but didn't");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: Area references at least 1 procedure: MOCKAREA", 
						 e.getMessage());
		}
		
		try {
			command.undo();
			fail("assert should have failed but didn't");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: Area is already referenced by a schema: MOCKAREA", 
						 e.getMessage());
		}
	}

}
