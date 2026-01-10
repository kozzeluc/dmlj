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
import static org.junit.Assert.assertTrue;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.junit.Test;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaFactory;

public class CreateDiagramLabelCommandTest {

	@Test
	public void testWithoutDescription1() {
		
		// create the schema (no description) with its diagram data container
		Schema schema = SchemaFactory.eINSTANCE.createSchema();
		DiagramData diagramData = SchemaFactory.eINSTANCE.createDiagramData();
		diagramData.setSchema(schema);
		// add 2 DiagramLocation objects to the DiagramData so to later make sure the DiagramLabel's
		// diagram location is added after those:
		diagramData.getLocations().add(SchemaFactory.eINSTANCE.createDiagramLocation());
		diagramData.getLocations().add(SchemaFactory.eINSTANCE.createDiagramLocation());
		
		// create the location of the diagram label
		Point location = new Point(10, 20);
		
		// create the size of the diagram label
		Dimension size = new Dimension(30, 40);		
		
		// create the command
		CreateDiagramLabelCommand command = new CreateDiagramLabelCommand(schema, location, size);
		
		// execute the command and check if the diagram label is added
		command.execute();
		DiagramLabel diagramLabel = diagramData.getLabel();
		assertNotNull(diagramLabel);
		
		// check all diagram label attributes
		assertNull(diagramLabel.getDescription());
		assertEquals(30, diagramLabel.getWidth());
		assertEquals(40, diagramLabel.getHeight());
		
		// check the diagram label's diagram location
		DiagramLocation diagramLocation = diagramLabel.getDiagramLocation();
		assertNotNull(diagramLocation);
		assertEquals(10, diagramLocation.getX());
		assertEquals(20, diagramLocation.getY());
		assertEquals("diagram label", diagramLocation.getEyecatcher());
		assertEquals(3, diagramData.getLocations().size());
		assertEquals(2, diagramData.getLocations().indexOf(diagramLocation));
		
		
		// undo the command and check if the diagram label and its diagram location are removed
		command.undo();
		assertNull(diagramData.getLabel());
		assertEquals(2, diagramData.getLocations().size());
		assertEquals(-1, diagramData.getLocations().indexOf(diagramLocation));
		
		
		// redo the command and check if the diagram label is there again, unchanged
		command.redo();
		DiagramLabel diagramLabel2 = diagramData.getLabel();
		assertTrue(diagramLabel2 == diagramLabel);
		assertNull(diagramLabel2.getDescription());
		assertEquals(30, diagramLabel2.getWidth());
		assertEquals(40, diagramLabel2.getHeight());
		DiagramLocation diagramLocation2 = diagramLabel2.getDiagramLocation();
		assertTrue(diagramLocation2 == diagramLocation);
		assertEquals(10, diagramLocation2.getX());
		assertEquals(20, diagramLocation2.getY());
		assertEquals("diagram label", diagramLocation2.getEyecatcher());
		assertEquals(3, diagramData.getLocations().size());
		assertEquals(2, diagramData.getLocations().indexOf(diagramLocation2));
	}
	
	@Test
	public void testWithoutDescription2() {
		
		// create the schema (empty description) with its diagram data container
		Schema schema = SchemaFactory.eINSTANCE.createSchema();
		schema.setDescription("");
		DiagramData diagramData = SchemaFactory.eINSTANCE.createDiagramData();
		diagramData.setSchema(schema);
		
		// create the location
		Point location = new Point(10, 20);
		
		// create the size
		Dimension size = new Dimension(30, 40);		
		
		// create the command
		CreateDiagramLabelCommand command = new CreateDiagramLabelCommand(schema, location, size);
		
		// execute the command and check if the diagram label is added
		command.execute();
		DiagramLabel diagramLabel = diagramData.getLabel();
		assertNotNull(diagramLabel);
		
		// check all diagram label attributes
		assertNull(diagramLabel.getDescription());
		assertEquals(30, diagramLabel.getWidth());
		assertEquals(40, diagramLabel.getHeight());
		
		// check the diagram label's diagram location
		DiagramLocation diagramLocation = diagramLabel.getDiagramLocation();
		assertNotNull(diagramLocation);
		assertEquals(10, diagramLocation.getX());
		assertEquals(20, diagramLocation.getY());
		assertEquals("diagram label", diagramLocation.getEyecatcher());
		
		
		// undo the command and check if the diagram label is removed
		command.undo();
		assertNull(diagramData.getLabel());		
		
		
		// redo the command and check if the diagram label is there again, unchanged
		command.redo();
		DiagramLabel diagramLabel2 = diagramData.getLabel();
		assertTrue(diagramLabel2 == diagramLabel);
		assertNull(diagramLabel2.getDescription());
		assertEquals(30, diagramLabel2.getWidth());
		assertEquals(40, diagramLabel2.getHeight());
		DiagramLocation diagramLocation2 = diagramLabel2.getDiagramLocation();
		assertTrue(diagramLocation2 == diagramLocation);
		assertEquals(10, diagramLocation2.getX());
		assertEquals(20, diagramLocation2.getY());
		assertEquals("diagram label", diagramLocation2.getEyecatcher());		
		
		
	}
	
	@Test
	public void testWithDescription() {
		
		// create the schema (with a description) with its diagram data container
		Schema schema = SchemaFactory.eINSTANCE.createSchema();
		schema.setDescription("abc");
		DiagramData diagramData = SchemaFactory.eINSTANCE.createDiagramData();
		diagramData.setSchema(schema);
		
		// create the location
		Point location = new Point(10, 20);
		
		// create the size
		Dimension size = new Dimension(30, 40);		
		
		// create the command
		CreateDiagramLabelCommand command = new CreateDiagramLabelCommand(schema, location, size);
		
		// execute the command and check if the diagram label is added
		command.execute();
		DiagramLabel diagramLabel = diagramData.getLabel();
		assertNotNull(diagramLabel);
		
		// check all diagram label attributes
		assertEquals("abc", diagramLabel.getDescription());
		assertEquals(30, diagramLabel.getWidth());
		assertEquals(40, diagramLabel.getHeight());
		
		// check the diagram label's diagram location
		DiagramLocation diagramLocation = diagramLabel.getDiagramLocation();
		assertNotNull(diagramLocation);
		assertEquals(10, diagramLocation.getX());
		assertEquals(20, diagramLocation.getY());
		assertEquals("diagram label", diagramLocation.getEyecatcher());
		
		
		// undo the command and check if the diagram label is removed
		command.undo();
		assertNull(diagramData.getLabel());		
		
		
		// redo the command and check if the diagram label is there again, unchanged
		command.redo();
		DiagramLabel diagramLabel2 = diagramData.getLabel();
		assertTrue(diagramLabel2 == diagramLabel);
		assertEquals("abc", diagramLabel2.getDescription());
		assertEquals(30, diagramLabel2.getWidth());
		assertEquals(40, diagramLabel2.getHeight());
		DiagramLocation diagramLocation2 = diagramLabel2.getDiagramLocation();
		assertTrue(diagramLocation2 == diagramLocation);
		assertEquals(10, diagramLocation2.getX());
		assertEquals(20, diagramLocation2.getY());
		assertEquals("diagram label", diagramLocation2.getEyecatcher());				
		
	}	

}
