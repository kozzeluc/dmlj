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

public class DeleteDiagramLabelCommandTest {

	@Test
	public void test() {
		
		// create the schema with its diagram data container and a couple of diagram locations
		Schema schema = SchemaFactory.eINSTANCE.createSchema();
		DiagramData diagramData = SchemaFactory.eINSTANCE.createDiagramData();
		diagramData.setSchema(schema);		
		diagramData.getLocations().add(SchemaFactory.eINSTANCE.createDiagramLocation());
		diagramData.getLocations().add(SchemaFactory.eINSTANCE.createDiagramLocation());
		diagramData.getLocations().add(SchemaFactory.eINSTANCE.createDiagramLocation());
		
		// the diagram location of the diagram label is in the middle of the locations container;
		// create the diagram label
		DiagramLocation diagramLocation = diagramData.getLocations().get(1);
		DiagramLabel diagramLabel = SchemaFactory.eINSTANCE.createDiagramLabel();
		diagramLabel.setDescription("test");
		diagramLabel.setDiagramData(diagramData);
		diagramLabel.setDiagramLocation(diagramLocation);
		
		// create the command
		DeleteDiagramLabelCommand command = new DeleteDiagramLabelCommand(schema);
		
		// execute the command and check if the diagram label is deleted and its diagram location
		// removed from the diagram locations container
		command.execute();
		assertNull(diagramData.getLabel());
		assertEquals(2, diagramData.getLocations().size());
		assertEquals(-1, diagramData.getLocations().indexOf(diagramLocation));
		
		
		// once execute() has been called, all annotated field values should be in place; make sure
		// the command class itself is annotated with @ModelChange with its type set to 
		// ModelChangeCategory.ADD_ITEM
		ModelChange modelChangeAnnotation = command.getClass().getAnnotation(ModelChange.class);	
		assertNotNull(modelChangeAnnotation);
		assertEquals(ModelChangeCategory.REMOVE_ITEM, modelChangeAnnotation.category());		
		
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
		
		
		// undo the command and check if the diagram label and its diagram location are restored
		// make sure the diagram label's diagram location is inserted again at the original index in
		// the diagram location container
		command.undo();
		DiagramLabel diagramLabel2 = diagramData.getLabel(); 
		assertTrue(diagramLabel2 == diagramLabel);
		assertEquals(3, diagramData.getLocations().size());
		DiagramLocation diagramLocation2 = diagramLabel.getDiagramLocation();
		assertTrue(diagramLocation2 == diagramLocation);
		assertEquals(1, diagramData.getLocations().indexOf(diagramLocation2));
		
		
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
		
		
		// redo the command and check if the diagram label and its location are removed again
		command.redo();
		assertNull(diagramData.getLabel());
		assertEquals(2, diagramData.getLocations().size());
		assertEquals(-1, diagramData.getLocations().indexOf(diagramLocation2));
		
		
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

}
