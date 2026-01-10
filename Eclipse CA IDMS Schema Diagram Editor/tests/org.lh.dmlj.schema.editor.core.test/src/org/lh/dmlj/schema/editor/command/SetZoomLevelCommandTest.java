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
import static org.junit.Assert.fail;

import org.junit.Test;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.SchemaFactory;

public class SetZoomLevelCommandTest {

	@Test
	public void test_CanUndo() {
		
		// create the owner and set the initial attribute value		
		DiagramData diagramData = SchemaFactory.eINSTANCE.createDiagramData();
		diagramData.setSchema(SchemaFactory.eINSTANCE.createSchema());
		diagramData.setZoomLevel(1.0);		
		
		// create the command
		SetZoomLevelCommand command = new SetZoomLevelCommand(diagramData.getSchema(), 2.0);
		
		// execute the command and check if the attribute value is set
		command.execute();
		assertEquals(2.0, diagramData.getZoomLevel(), 0.0);	
		
		
		// undo the command and check if the attribute value is set to its old value again
		command.undo();
		assertEquals(1.0, diagramData.getZoomLevel(), 0.0);
		
		
		// redo the command and check if the attribute value is set to its new value again
		command.redo();
		assertEquals(2.0, diagramData.getZoomLevel(), 0.0);		
	}
	
	@Test
	public void test_CannotUndo() {
		
		// create the owner and set the initial attribute value		
		DiagramData diagramData = SchemaFactory.eINSTANCE.createDiagramData();
		diagramData.setSchema(SchemaFactory.eINSTANCE.createSchema());
		diagramData.setZoomLevel(1.0);		
		
		// create the command
		SetZoomLevelCommand command = new SetZoomLevelCommand(diagramData.getSchema(), 2.0, false);
		
		// execute the command and check if the attribute value is set
		command.execute();
		assertEquals(2.0, diagramData.getZoomLevel(), 0.0);
				
		
		// try to undo the command
		try {
			command.undo();
			fail("should throw a RuntimeException");
		} catch (RuntimeException e) {
			assertEquals("cannot undo", e.getMessage());
		}
		
		
		// try to redo the command
		try {
			command.redo();
			fail("should throw a RuntimeException");
		} catch (RuntimeException e) {
			assertEquals("cannot redo (canUndo == false)", e.getMessage());
		}		
		
	}	

}
