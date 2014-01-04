/**
 * Copyright (C) 2013  Luc Hermans
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
import org.eclipse.emf.ecore.EReference;
import org.junit.Test;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.command.annotation.Item;
import org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory;
import org.lh.dmlj.schema.editor.command.annotation.Owner;
import org.lh.dmlj.schema.editor.command.annotation.Reference;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeDispatcher;

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
		
		
		// once execute() has been called, all annotated field values should be in place; make sure
		// the command class itself is annotated with @ModelChange with its type set to 
		// ModelChangeCategory.ADD_ITEM
		ModelChange modelChangeAnnotation = command.getClass().getAnnotation(ModelChange.class);	
		assertNotNull(modelChangeAnnotation);
		assertEquals(ModelChangeCategory.ADD_ITEM, modelChangeAnnotation.category());		
		
		// make sure the owner is set
		DiagramData owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(owner == diagramData);
		
		// make sure the reference is set
		EReference reference = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Reference.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(reference == SchemaPackage.eINSTANCE.getDiagramData_Label());			
		
		// make sure the item is set
		DiagramLabel item = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Item.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(item == diagramLabel);	
		
		
		// undo the command and check if the diagram label and its diagram location are removed
		command.undo();
		assertNull(diagramData.getLabel());
		assertEquals(2, diagramData.getLocations().size());
		assertEquals(-1, diagramData.getLocations().indexOf(diagramLocation));
		
		
		// make sure the owner is still set
		owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(owner == diagramData);
		
		// make sure the reference is still set
		reference = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Reference.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(reference == SchemaPackage.eINSTANCE.getDiagramData_Label());			
		
		// make sure the item is still set
		item = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Item.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(item == diagramLabel);		
		
		
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
		
		
		// make sure the owner is still set
		owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(owner == diagramData);
		
		// make sure the reference is still set
		reference = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Reference.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(reference == SchemaPackage.eINSTANCE.getDiagramData_Label());			
		
		// make sure the item is still set
		item = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Item.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(item == diagramLabel);				
		
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
