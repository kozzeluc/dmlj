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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.eclipse.draw2d.geometry.Point;
import org.junit.Test;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Usage;
import org.lh.dmlj.schema.editor.testtool.ObjectGraph;
import org.lh.dmlj.schema.editor.testtool.TestTools;
import org.lh.dmlj.schema.editor.testtool.Xmi;

public class CreateRecordCommandTest {
	
	private static Schema createEmptySchema() {
		Schema schema = SchemaFactory.eINSTANCE.createSchema();
		DiagramData diagramData = SchemaFactory.eINSTANCE.createDiagramData();
		schema.setDiagramData(diagramData);
		return schema;
	}

	@Test
	public void testEmptySchema() {
		
		Schema schema = createEmptySchema();
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);
		assertEquals(0, schema.getAreas().size());
		assertEquals(0, schema.getRecords().size());
		assertEquals(0, schema.getDiagramData().getLocations().size());
		
		CreateRecordCommand command = new CreateRecordCommand(schema, new Point(3, 5));				
		command.execute();	
		ObjectGraph objectGraph2 = TestTools.asObjectGraph(schema);
		Xmi xmi2 = TestTools.asXmi(schema);
		assertEquals(1, schema.getAreas().size());
		assertEquals(1, schema.getRecords().size());
		assertEquals(1, schema.getDiagramData().getLocations().size());
		SchemaArea area = schema.getAreas().get(0);
		assertEquals("RECORD-1-AREA", area.getName());
		assertEquals(0, area.getIndexes().size());
		assertEquals(1, area.getAreaSpecifications().size());
		assertEquals(0, area.getProcedures().size());
		assertSame(schema, area.getSchema());
		AreaSpecification areaSpecification = area.getAreaSpecifications().get(0);
		assertSame(area, areaSpecification.getArea());
		assertNull(areaSpecification.getOffsetExpression());
		assertNotNull(areaSpecification.getRecord());
		assertNull(areaSpecification.getSymbolicSubareaName());
		assertNull(areaSpecification.getSystemOwner());
		SchemaRecord record = schema.getRecords().get(0);
		assertEquals("NEW-RECORD-1", record.getName());
		assertEquals(record.getName(), record.getBaseName());
		assertEquals(1,record.getBaseVersion());
		assertEquals(record.getName(), record.getSynonymName());
		assertEquals(1,record.getSynonymVersion());
		assertNull(record.getCalcKey());
		assertNull(record.getViaSpecification());
		assertEquals(0, record.getKeys().size());
		assertNull(record.getMinimumFragmentLength());
		assertNull(record.getMinimumRootLength());
		assertEquals(0, record.getOwnerRoles().size());
		assertEquals(0, record.getMemberRoles().size());
		assertSame(areaSpecification, record.getAreaSpecification());
		assertEquals(1, record.getElements().size());
		assertEquals(1, record.getRootElements().size());
		assertSame(record.getElements().get(0), record.getRootElements().get(0));
		Element element = record.getElements().get(0);
		assertEquals(2, element.getLevel());
		assertEquals("ELEMENT-1", element.getName());
		assertEquals("ELEMENT-1", element.getBaseName());
		assertEquals("X(8)", element.getPicture());
		assertSame(Usage.DISPLAY, element.getUsage());
		assertEquals(0, element.getChildren().size());
		assertNull(element.getParent());
		assertEquals(0, element.getKeyElements().size());
		assertNull(element.getOccursSpecification());
		assertNull(element.getRedefines());
		assertNull(element.getValue());
		assertSame(record, element.getRecord());
		DiagramLocation diagramLocation = record.getDiagramLocation();
		assertEquals(3, diagramLocation.getX());
		assertEquals(5, diagramLocation.getY());
		assertEquals("record NEW-RECORD-1", diagramLocation.getEyecatcher());
		
		command.undo();
		ObjectGraph objectGraph3 = TestTools.asObjectGraph(schema);
		Xmi xmi3 = TestTools.asXmi(schema);
		TestTools.assertEquals(objectGraph, objectGraph3);
		TestTools.assertEquals(xmi, xmi3);
		
		command.redo();
		ObjectGraph objectGraph4 = TestTools.asObjectGraph(schema);
		Xmi xmi4 = TestTools.asXmi(schema);
		TestTools.assertEquals(objectGraph2, objectGraph4);
		TestTools.assertEquals(xmi2, xmi4);
		
	}
	
	@Test
	public void testNonEmptySchema() {
		
		Schema schema = TestTools.getEmpschmSchema();
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);
		int originalRecordCount = schema.getRecords().size();
		int originalAreaCount = schema.getAreas().size();
		SchemaArea empDemoRegion = schema.getArea("EMP-DEMO-REGION");
		assertNotNull(empDemoRegion);
		assertEquals(5, empDemoRegion.getAreaSpecifications().size());
		
		CreateRecordCommand command = new CreateRecordCommand(schema, new Point(3, 5));				
		command.execute();	
		ObjectGraph objectGraph2 = TestTools.asObjectGraph(schema);
		Xmi xmi2 = TestTools.asXmi(schema);
		assertEquals(originalAreaCount, schema.getAreas().size());
		assertEquals(originalRecordCount + 1, schema.getRecords().size());
 		assertEquals(6, empDemoRegion.getAreaSpecifications().size());
 		SchemaRecord newRecord = schema.getRecord("NEW-RECORD-1");
 		assertNotNull(newRecord);
 		assertEquals(461, newRecord.getId());
 		assertEquals(originalRecordCount, schema.getRecords().indexOf(newRecord));
 		assertSame(empDemoRegion, newRecord.getAreaSpecification().getArea());
 		assertEquals(5, empDemoRegion.getAreaSpecifications()
 									 .indexOf(newRecord.getAreaSpecification()));
		
		command.undo();
		ObjectGraph objectGraph3 = TestTools.asObjectGraph(schema);
		Xmi xmi3 = TestTools.asXmi(schema);
		TestTools.assertEquals(objectGraph, objectGraph3);
		TestTools.assertEquals(xmi, xmi3);
		
		command.redo();
		ObjectGraph objectGraph4 = TestTools.asObjectGraph(schema);
		Xmi xmi4 = TestTools.asXmi(schema);
		TestTools.assertEquals(objectGraph2, objectGraph4);
		TestTools.assertEquals(xmi2, xmi4);
		
		// create a second new record
		command = new CreateRecordCommand(schema, new Point(34, 6));				
		command.execute();
		
		assertEquals(originalAreaCount, schema.getAreas().size());
		assertEquals(originalRecordCount + 2, schema.getRecords().size());
 		assertEquals(7, empDemoRegion.getAreaSpecifications().size());
 		SchemaRecord newRecord2 = schema.getRecord("NEW-RECORD-2");
 		assertNotNull(newRecord2);
 		assertEquals(462, newRecord2.getId());
 		assertEquals(originalRecordCount + 1, schema.getRecords().indexOf(newRecord2));
 		assertSame(empDemoRegion, newRecord2.getAreaSpecification().getArea());
 		assertEquals(6, empDemoRegion.getAreaSpecifications()
 									 .indexOf(newRecord2.getAreaSpecification()));
		
	}
	
}
