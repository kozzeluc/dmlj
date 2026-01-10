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

import org.eclipse.emf.ecore.EAttribute;
import org.junit.Test;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaPackage;

public class SetShortAttributeCommandTest {

	@Test
	public void test() {
		
		// create the owner and set the initial attribute value
		Schema owner = SchemaFactory.eINSTANCE.createSchema();
		owner.setVersion((short) 1);
		
		// the attribute to be set
		EAttribute attribute = SchemaPackage.eINSTANCE.getSchema_Version();
		
		// create the command
		SetShortAttributeCommand command = 
			new SetShortAttributeCommand(owner, attribute, (short) 2, "test");
		
		// execute the command and check if the attribute value is set
		command.execute();
		assertEquals(2, owner.getVersion());			
		
		
		// undo the command and check if the attribute value is set to its old value again
		command.undo();
		assertEquals(1, owner.getVersion());
		
		
		// redo the command and check if the attribute value is set to its new value again
		command.redo();
		assertEquals(2, owner.getVersion());
	}

}
