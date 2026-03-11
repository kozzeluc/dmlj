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

import org.eclipse.core.runtime.AssertionFailedException;
import org.junit.Test;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.ViaSpecification;
import org.lh.dmlj.schema.editor.testtool.ObjectGraph;
import org.lh.dmlj.schema.editor.testtool.TestTools;

public class DeleteRecordCommandTest {

	@Test
	public void testViaRecord() {
		
		Schema schema = SchemaFactory.eINSTANCE.createSchema();
		
		SchemaArea area = SchemaFactory.eINSTANCE.createSchemaArea();
		area.setSchema(schema);
		
		AreaSpecification areaSpecification = SchemaFactory.eINSTANCE.createAreaSpecification();
		area.getAreaSpecifications().add(SchemaFactory.eINSTANCE.createAreaSpecification());
		area.getAreaSpecifications().add(areaSpecification);
		area.getAreaSpecifications().add(SchemaFactory.eINSTANCE.createAreaSpecification());
		
		DiagramData diagramData = SchemaFactory.eINSTANCE.createDiagramData();
		diagramData.setSchema(schema);
		DiagramLocation diagramLocation = SchemaFactory.eINSTANCE.createDiagramLocation();
		diagramData.getLocations().add(SchemaFactory.eINSTANCE.createDiagramLocation());
		diagramData.getLocations().add(diagramLocation);
		diagramData.getLocations().add(SchemaFactory.eINSTANCE.createDiagramLocation());
		
		Set set = SchemaFactory.eINSTANCE.createSet();
		
		ViaSpecification viaSpecification = SchemaFactory.eINSTANCE.createViaSpecification();
		set.getViaMembers().add(SchemaFactory.eINSTANCE.createViaSpecification());
		set.getViaMembers().add(viaSpecification);
		set.getViaMembers().add(SchemaFactory.eINSTANCE.createViaSpecification());
		
		SchemaRecord record = SchemaFactory.eINSTANCE.createSchemaRecord();
		record.setLocationMode(LocationMode.VIA);
		record.setViaSpecification(viaSpecification);
		record.setAreaSpecification(areaSpecification);
		record.setDiagramLocation(diagramLocation);
		schema.getRecords().add(SchemaFactory.eINSTANCE.createSchemaRecord());
		schema.getRecords().add(record);
		schema.getRecords().add(SchemaFactory.eINSTANCE.createSchemaRecord());
		
		ObjectGraph graph1 = TestTools.asObjectGraph(schema);
		
		DeleteRecordCommand command = new DeleteRecordCommand(record);
		
		command.execute();
		ObjectGraph graph2 = TestTools.asObjectGraph(schema);
		assertEquals(1, schema.getAreas().size());
		assertSame(area, schema.getAreas().get(0));
		assertEquals(2, area.getAreaSpecifications().size());
		assertEquals(-1, area.getAreaSpecifications().indexOf(areaSpecification));
		assertEquals(2, schema.getRecords().size());
		assertEquals(-1, schema.getRecords().indexOf(record));
		assertEquals(2, schema.getDiagramData().getLocations().size());
		assertEquals(-1, schema.getDiagramData().getLocations().indexOf(diagramLocation));
		assertEquals(2, set.getViaMembers().size());
		assertEquals(-1, set.getViaMembers().indexOf(viaSpecification));
		
		command.undo();
		ObjectGraph graph3 = TestTools.asObjectGraph(schema);
		TestTools.assertEquals(graph1, graph3);
		
		command.redo();
		ObjectGraph graph4 = TestTools.asObjectGraph(schema);
		TestTools.assertEquals(graph2, graph4);
		
	}
	
	@Test
	public void testAsserts() {
		
		Schema schema = SchemaFactory.eINSTANCE.createSchema();
		
		SchemaArea area = SchemaFactory.eINSTANCE.createSchemaArea();
		area.setSchema(schema);
		
		AreaSpecification areaSpecification = SchemaFactory.eINSTANCE.createAreaSpecification();
		area.getAreaSpecifications().add(SchemaFactory.eINSTANCE.createAreaSpecification());
		area.getAreaSpecifications().add(areaSpecification);
		area.getAreaSpecifications().add(SchemaFactory.eINSTANCE.createAreaSpecification());
		
		DiagramData diagramData = SchemaFactory.eINSTANCE.createDiagramData();
		diagramData.setSchema(schema);
		DiagramLocation diagramLocation = SchemaFactory.eINSTANCE.createDiagramLocation();
		diagramData.getLocations().add(SchemaFactory.eINSTANCE.createDiagramLocation());
		diagramData.getLocations().add(diagramLocation);
		diagramData.getLocations().add(SchemaFactory.eINSTANCE.createDiagramLocation());
		
		Set set = SchemaFactory.eINSTANCE.createSet();
		
		ViaSpecification viaSpecification = SchemaFactory.eINSTANCE.createViaSpecification();
		set.getViaMembers().add(SchemaFactory.eINSTANCE.createViaSpecification());
		set.getViaMembers().add(viaSpecification);
		set.getViaMembers().add(SchemaFactory.eINSTANCE.createViaSpecification());
		
		SchemaRecord record = SchemaFactory.eINSTANCE.createSchemaRecord();
		record.setName("RECORD1");
		record.setLocationMode(LocationMode.VIA);
		record.setViaSpecification(viaSpecification);
		record.setAreaSpecification(areaSpecification);
		record.setDiagramLocation(diagramLocation);
		schema.getRecords().add(SchemaFactory.eINSTANCE.createSchemaRecord());
		schema.getRecords().add(record);
		schema.getRecords().add(SchemaFactory.eINSTANCE.createSchemaRecord());

		DeleteRecordCommand command = new DeleteRecordCommand(record);
		
		record.getOwnerRoles().add(SchemaFactory.eINSTANCE.createOwnerRole());
		try {
			command.execute();
			fail("assert should have failed but didn't");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: Record is an owner of at least 1 set: RECORD1", 
						 e.getMessage());
		}
		
		record.getOwnerRoles().clear();
		record.getMemberRoles().add(SchemaFactory.eINSTANCE.createMemberRole());
		try {
			command.execute();
			fail("assert should have failed but didn't");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: Record is a member of at least 1 set: RECORD1", 
						 e.getMessage());
		}
		
		record.getMemberRoles().clear();
		record.getProcedures().add(SchemaFactory.eINSTANCE.createRecordProcedureCallSpecification());
		try {
			command.execute();
			fail("assert should have failed but didn't");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: Record references at least 1 procedure: RECORD1", 
						 e.getMessage());
		}
		
		record.getProcedures().clear();
		try {
			command.undo();
			fail("assert should have failed but didn't");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: Record is already referenced by a schema: RECORD1", 
						 e.getMessage());
		}
		
	}

}
