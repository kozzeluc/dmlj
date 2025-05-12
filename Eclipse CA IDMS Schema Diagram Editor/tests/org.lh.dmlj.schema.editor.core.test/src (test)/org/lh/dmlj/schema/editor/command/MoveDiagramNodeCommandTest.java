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
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.DiagramNode;
import org.lh.dmlj.schema.SchemaFactory;

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
		
		
		// undo the command and check the diagram node's location
		command.undo();
		assertEquals(1, diagramLocation.getX());
		assertEquals(2, diagramLocation.getY());		
		
		
		// redo the command and check the diagram node's location
		command.redo();
		assertEquals(15, diagramLocation.getX());
		assertEquals(25, diagramLocation.getY());
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
		
		
		// undo the command and check the diagram node's location
		command.undo();
		assertEquals(1, diagramLocation.getX());
		assertEquals(2, diagramLocation.getY());
		
		
		// redo the command and check the diagram node's location
		command.redo();
		assertEquals(15, diagramLocation.getX());
		assertEquals(25, diagramLocation.getY());
	}

}
