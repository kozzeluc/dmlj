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

import org.junit.Test;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.SchemaFactory;

public class ResizeDiagramNodeCommandTest {

	@Test
	public void test() {
		
		// create the owner
		DiagramLabel diagramLabel = SchemaFactory.eINSTANCE.createDiagramLabel();
		diagramLabel.setWidth((short) 1);
		diagramLabel.setHeight((short) 2);
		
		// create the command
		ResizeDiagramNodeCommand command = 
			new ResizeDiagramNodeCommand(diagramLabel, (short) 10, (short) 20);
		
		// execute the command and check if the attribute value is set
		command.execute();
		assertEquals(10, diagramLabel.getWidth());
		assertEquals(20, diagramLabel.getHeight());
		
		
		// undo the command and check if the attribute value is set to its old value again
		command.undo();
		assertEquals(1, diagramLabel.getWidth());
		assertEquals(2, diagramLabel.getHeight());
		
		
		// redo the command and check if the attribute value is set to its new value again
		command.redo();
		assertEquals(10, diagramLabel.getWidth());
		assertEquals(20, diagramLabel.getHeight());
	}

}
