/**
 * Copyright (C) 2014  Luc Hermans
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
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.junit.Test;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.DiagramNode;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.command.annotation.Features;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory;
import org.lh.dmlj.schema.editor.command.annotation.Owner;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeDispatcher;

public class MoveDiagramNodeCommandTest {

	@Test
	public void test() {
		
		// create a diagramNode and set its location
		DiagramNode diagramNode = SchemaFactory.eINSTANCE.createDiagramLabel();
		DiagramLocation diagramLocation = SchemaFactory.eINSTANCE.createDiagramLocation();
		diagramLocation.setEyecatcher("test");
		diagramLocation.setX(1);
		diagramLocation.setY(2);
		diagramNode.setDiagramLocation(diagramLocation);
		
		// create the command
		MoveDiagramNodeCommand command = new MoveDiagramNodeCommand(diagramNode, 15, 25);
		
		// execute the command and check the diagram node's location
		command.execute();
		assertEquals(15, diagramLocation.getX());
		assertEquals(25, diagramLocation.getY());
		
		
		// once execute() has been called, all annotated field values should be in place; make sure
		// the command class itself is annotated with @ModelChange with its type set to 
		// ModelChangeCategory.SET_FEATURES
		ModelChange modelChangeAnnotation = command.getClass().getAnnotation(ModelChange.class);	
		assertNotNull(modelChangeAnnotation);
		assertEquals(ModelChangeCategory.SET_FEATURES, modelChangeAnnotation.category());		
		
		// make sure the owner is set
		DiagramLocation owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(owner == diagramLocation);
		
		// make sure the attributes are set
		EStructuralFeature[] attributes = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Features.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertEquals(2, attributes.length);
		assertTrue(attributes[0] == SchemaPackage.eINSTANCE.getDiagramLocation_X());
		assertTrue(attributes[1] == SchemaPackage.eINSTANCE.getDiagramLocation_Y());
		
		
		// undo the command and check the diagram node's location
		command.undo();
		assertEquals(1, diagramLocation.getX());
		assertEquals(2, diagramLocation.getY());
		
		
		// make sure the owner is still set
		owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(owner == diagramLocation);
		
		// make sure the attributes are still set
		attributes = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Features.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertEquals(2, attributes.length);
		assertTrue(attributes[0] == SchemaPackage.eINSTANCE.getDiagramLocation_X());
		assertTrue(attributes[1] == SchemaPackage.eINSTANCE.getDiagramLocation_Y());		
		
		
		// redo the command and check the diagram node's location
		command.redo();
		assertEquals(15, diagramLocation.getX());
		assertEquals(25, diagramLocation.getY());
		
		
		// make sure the owner is still set
		owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(owner == diagramLocation);
		
		// make sure the attributes are still set
		attributes = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Features.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertEquals(2, attributes.length);
		assertTrue(attributes[0] == SchemaPackage.eINSTANCE.getDiagramLocation_X());
		assertTrue(attributes[1] == SchemaPackage.eINSTANCE.getDiagramLocation_Y());		
		
	}
	@Test
	public void testWithSupplier() {
		
		// create a diagramNode and set its location
		final DiagramLabel diagramLabel = SchemaFactory.eINSTANCE.createDiagramLabel();
		DiagramLocation diagramLocation = SchemaFactory.eINSTANCE.createDiagramLocation();
		diagramLocation.setEyecatcher("test");
		diagramLocation.setX(1);
		diagramLocation.setY(2);
		diagramLabel.setDiagramLocation(diagramLocation);
		
		// create the diagram node supplier
		ISupplier<DiagramLabel> diagramLabelSupplier = new ISupplier<DiagramLabel>() {
			@Override
			public DiagramLabel supply() {
				return diagramLabel;
			}		
		};
		
		// create the command
		MoveDiagramNodeCommand command = new MoveDiagramNodeCommand(diagramLabelSupplier, 15, 25);
		
		// execute the command and check the diagram node's location
		command.execute();
		assertEquals(15, diagramLocation.getX());
		assertEquals(25, diagramLocation.getY());
		
		
		// once execute() has been called, all annotated field values should be in place; make sure
		// the command class itself is annotated with @ModelChange with its type set to 
		// ModelChangeCategory.SET_FEATURES
		ModelChange modelChangeAnnotation = command.getClass().getAnnotation(ModelChange.class);	
		assertNotNull(modelChangeAnnotation);
		assertEquals(ModelChangeCategory.SET_FEATURES, modelChangeAnnotation.category());		
		
		// make sure the owner is set
		DiagramLocation owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(owner == diagramLocation);
		
		// make sure the attributes are set
		EStructuralFeature[] attributes = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Features.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertEquals(2, attributes.length);
		assertTrue(attributes[0] == SchemaPackage.eINSTANCE.getDiagramLocation_X());
		assertTrue(attributes[1] == SchemaPackage.eINSTANCE.getDiagramLocation_Y());
		
		
		// undo the command and check the diagram node's location
		command.undo();
		assertEquals(1, diagramLocation.getX());
		assertEquals(2, diagramLocation.getY());
		
		
		// make sure the owner is still set
		owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(owner == diagramLocation);
		
		// make sure the attributes are still set
		attributes = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Features.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertEquals(2, attributes.length);
		assertTrue(attributes[0] == SchemaPackage.eINSTANCE.getDiagramLocation_X());
		assertTrue(attributes[1] == SchemaPackage.eINSTANCE.getDiagramLocation_Y());		
		
		
		// redo the command and check the diagram node's location
		command.redo();
		assertEquals(15, diagramLocation.getX());
		assertEquals(25, diagramLocation.getY());
		
		
		// make sure the owner is still set
		owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(owner == diagramLocation);
		
		// make sure the attributes are still set
		attributes = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Features.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertEquals(2, attributes.length);
		assertTrue(attributes[0] == SchemaPackage.eINSTANCE.getDiagramLocation_X());
		assertTrue(attributes[1] == SchemaPackage.eINSTANCE.getDiagramLocation_Y());		
		
	}

}
