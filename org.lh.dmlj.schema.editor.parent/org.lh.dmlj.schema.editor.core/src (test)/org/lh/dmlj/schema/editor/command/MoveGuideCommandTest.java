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
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.junit.Test;
import org.lh.dmlj.schema.Guide;
import org.lh.dmlj.schema.Ruler;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.command.annotation.Features;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory;
import org.lh.dmlj.schema.editor.command.annotation.Owner;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeDispatcher;

public class MoveGuideCommandTest {

	@Test
	public void testIncrementPosition() {
		
		// create a ruler and add a guide to it
		Ruler ruler = SchemaFactory.eINSTANCE.createRuler();
		Guide guide = SchemaFactory.eINSTANCE.createGuide();
		ruler.getGuides().add(guide);
		guide.setPosition(1);		
		
		// the guide should have its position attribute incremented by 5
		int delta = 5;
		
		// create the command
		MoveGuideCommand command = new MoveGuideCommand(guide, delta);
		
		// execute the command and check the guide's position
		command.execute();
		assertEquals(6, guide.getPosition());
		
		
		// once execute() has been called, all annotated field values should be in place; make sure
		// the command class itself is annotated with @ModelChange with its type set to 
		// ModelChangeCategory.SET_FEATURES
		ModelChange modelChangeAnnotation = command.getClass().getAnnotation(ModelChange.class);	
		assertNotNull(modelChangeAnnotation);
		assertEquals(ModelChangeCategory.SET_FEATURES, modelChangeAnnotation.category());		
		
		// make sure the owner is set
		Guide owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(owner == guide);
		
		// make sure the attribute is set
		EStructuralFeature[] attributes = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Features.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertEquals(1, attributes.length);
		assertTrue(attributes[0] == SchemaPackage.eINSTANCE.getGuide_Position());
		
		
		// undo the command and check if the guide position is reverted
		command.undo();
		assertEquals(1, guide.getPosition());	
		
		// make sure the owner is still set
		owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(owner == guide);
		
		// make sure the attribute is set
		attributes = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Features.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertEquals(1, attributes.length);
		assertTrue(attributes[0] == SchemaPackage.eINSTANCE.getGuide_Position());
		
		
		// redo the command and and check the guide's position
		command.redo();
		assertEquals(6, guide.getPosition());	
		
		// make sure the owner is still set
		owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(owner == guide);
		
		// make sure the attribute is set
		attributes = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Features.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertEquals(1, attributes.length);
		assertTrue(attributes[0] == SchemaPackage.eINSTANCE.getGuide_Position());		
		
	}
	
	@Test
	public void testDecrementPosition() {
		
		// create a ruler and add a guide to it
		Ruler ruler = SchemaFactory.eINSTANCE.createRuler();
		Guide guide = SchemaFactory.eINSTANCE.createGuide();
		ruler.getGuides().add(guide);
		guide.setPosition(6);		
		
		// the guide should have its position attribute incremented by 5
		int delta = -5;
		
		// create the command
		MoveGuideCommand command = new MoveGuideCommand(guide, delta);
		
		// execute the command and check the guide's position
		command.execute();
		assertEquals(1, guide.getPosition());
		
		
		// undo the command and check if the guide position is reverted
		command.undo();
		assertEquals(6, guide.getPosition());	
				
		
		// redo the command and and check the guide's position
		command.redo();
		assertEquals(1, guide.getPosition());	
		
	}	

}
