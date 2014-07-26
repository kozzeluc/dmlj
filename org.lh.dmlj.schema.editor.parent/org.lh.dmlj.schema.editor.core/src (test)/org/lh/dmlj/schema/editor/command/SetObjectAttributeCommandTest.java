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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.junit.Test;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.command.annotation.Features;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory;
import org.lh.dmlj.schema.editor.command.annotation.Owner;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeDispatcher;

public class SetObjectAttributeCommandTest {

	@Test
	public void test() {
		
		// create the owner and set the initial attribute value
		Schema owner = SchemaFactory.eINSTANCE.createSchema();
		owner.setName("ABC");
		
		// the attribute to be set
		EAttribute attribute = SchemaPackage.eINSTANCE.getSchema_Name();
		
		// create the command
		SetObjectAttributeCommand command = 
			new SetObjectAttributeCommand(owner, attribute, "DEF", "test");
		
		// execute the command and check if the attribute value is set
		command.execute();
		assertEquals("DEF", owner.getName());
		
		
		// once execute() has been called, all annotated field values should be in place; make sure
		// the command class itself is annotated with @ModelChange with its type set to 
		// ModelChangeCategory.SET_FEATURES
		ModelChange modelChangeAnnotation = command.getClass().getAnnotation(ModelChange.class);	
		assertNotNull(modelChangeAnnotation);
		assertEquals(ModelChangeCategory.SET_FEATURES, 
							modelChangeAnnotation.category());
		
		// make sure the owner is set
		Schema owner2 = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(owner2 == owner);		
		
		// make sure the attribute is set
		EStructuralFeature[] attributes = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Features.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertEquals(1, attributes.length);
		assertTrue(attributes[0] == attribute);				
		
		
		// undo the command and check if the attribute value is set to its old value again
		command.undo();
		assertEquals("ABC", owner.getName());
		
		
		// make sure the owner is still set
		owner2 = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(owner2 == owner);		
		
		// make sure the attribute is still set
		attributes = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Features.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertEquals(1, attributes.length);
		assertTrue(attributes[0] == attribute);		
		
		
		// redo the command and check if the attribute value is set to its new value again
		command.redo();
		assertEquals("DEF", owner.getName());
		
		
		// make sure the owner is still set
		owner2 = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(owner2 == owner);		
		
		// make sure the attribute is still set
		attributes = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Features.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertEquals(1, attributes.length);
		assertTrue(attributes[0] == attribute);
		
	}

}
