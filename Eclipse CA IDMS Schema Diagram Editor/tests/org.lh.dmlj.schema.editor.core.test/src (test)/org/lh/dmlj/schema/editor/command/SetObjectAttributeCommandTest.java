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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaPackage;

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
	
		
		// undo the command and check if the attribute value is set to its old value again
		command.undo();
		assertEquals("ABC", owner.getName());	
		
		
		// redo the command and check if the attribute value is set to its new value again
		command.redo();
		assertEquals("DEF", owner.getName());
	}
	
	@Test
	public void testAlternativeConstructor() {
		// use the alternative constructor (accepting as its arguments 'eObjectSupplier' 
		// (IEObjectSupplier), 'attribute' (EAttribute), newValue (Object) and attributeLabel 
		// (String) when the owner EObject instance (see the other constructor) is NOT yet known at 
		// command construction time
		final Schema schema = SchemaFactory.eINSTANCE.createSchema();
		ISupplier<EObject> eObjectSupplier = new ISupplier<EObject>() {			
			@Override
			public EObject supply() {
				return schema;
			}
		};
		SetObjectAttributeCommand command = 
			new SetObjectAttributeCommand(eObjectSupplier, SchemaPackage.eINSTANCE.getSchema_Name(), 
										  "EMPSCHM", "test");
		command.execute();
		assertSame(schema, command.owner);
		command.undo();
		command.redo();
	}

}
