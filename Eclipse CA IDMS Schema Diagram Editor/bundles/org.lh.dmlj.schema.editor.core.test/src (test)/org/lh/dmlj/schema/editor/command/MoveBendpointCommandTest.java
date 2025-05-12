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

import org.eclipse.gef.commands.Command;
import org.junit.Test;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.testtool.TestTools;

public class MoveBendpointCommandTest {

	@Test
	public void test() {
		
		// get the test schema and locate set DEPT-EMPLOYEE; this set has 1 connection part (i.e.
		// no connectors split the line) and the connection part has 2 bendpoints; we will be  
		// moving the first bendpoint 
		Schema schema = TestTools.getSchema("testdata/BendpointsAndConnectors.schema");
		Set set = schema.getSet("DEPT-EMPLOYEE");
		assertEquals(1, set.getMembers().get(0).getConnectionParts().size());
		ConnectionPart originalConnectionPart = 
			set.getMembers().get(0).getConnectionParts().get(0);
		assertEquals(2, originalConnectionPart.getBendpointLocations().size());
		DiagramLocation originalBendpoint1 = originalConnectionPart.getBendpointLocations().get(0);
		assertEquals(66, originalBendpoint1.getX());
		assertEquals(114, originalBendpoint1.getY());
		DiagramLocation originalBendpoint2 = originalConnectionPart.getBendpointLocations().get(1);		
		
		
		
		// create the command
		Command command = new MoveBendpointCommand(originalConnectionPart, 0, 20, 30);
		
		
		// execute the command and verify
		command.execute();
		assertEquals(1, set.getMembers().get(0).getConnectionParts().size());
		assertEquals(2, originalConnectionPart.getBendpointLocations().size());
		DiagramLocation newBendpoint1a = originalConnectionPart.getBendpointLocations().get(0);
		assertSame(originalBendpoint1, newBendpoint1a);
		assertEquals(20, newBendpoint1a.getX());
		assertEquals(30, newBendpoint1a.getY());
		assertSame(originalBendpoint2, originalConnectionPart.getBendpointLocations().get(1));
		assertEquals("bendpoint [0] set DEPT-EMPLOYEE (EMPLOYEE)", newBendpoint1a.getEyecatcher());
		assertEquals("bendpoint [1] set DEPT-EMPLOYEE (EMPLOYEE)", 
			 	 	 originalBendpoint2.getEyecatcher());
	
		
		// undo the command and verify
		command.undo();
		assertEquals(1, set.getMembers().get(0).getConnectionParts().size());
		assertEquals(2, originalConnectionPart.getBendpointLocations().size());
		DiagramLocation newBendpoint1b = originalConnectionPart.getBendpointLocations().get(0);
		assertSame(originalBendpoint1, newBendpoint1b);
		assertEquals(66, newBendpoint1b.getX());
		assertEquals(114, newBendpoint1b.getY());
		assertEquals("bendpoint [0] set DEPT-EMPLOYEE (EMPLOYEE)", newBendpoint1b.getEyecatcher());		
		assertSame(originalBendpoint2, originalConnectionPart.getBendpointLocations().get(1));
		assertEquals("bendpoint [1] set DEPT-EMPLOYEE (EMPLOYEE)", 
				 	 originalBendpoint2.getEyecatcher());	
		
		
		// redo the command and verify
		command.redo();
		assertEquals(1, set.getMembers().get(0).getConnectionParts().size());
		assertEquals(2, originalConnectionPart.getBendpointLocations().size());
		DiagramLocation newBendpoint1c = originalConnectionPart.getBendpointLocations().get(0);
		assertSame(originalBendpoint1, newBendpoint1c);
		assertEquals(20, newBendpoint1c.getX());
		assertEquals(30, newBendpoint1c.getY());
		assertSame(originalBendpoint2, originalConnectionPart.getBendpointLocations().get(1));
		assertEquals("bendpoint [1] set DEPT-EMPLOYEE (EMPLOYEE)", 
				 	 originalBendpoint2.getEyecatcher());
	}

}
