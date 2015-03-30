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
import static org.lh.dmlj.schema.editor.testtool.TestTools.assertEquals;

import org.eclipse.gef.commands.Command;
import org.junit.Test;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.testtool.ObjectGraph;
import org.lh.dmlj.schema.editor.testtool.TestTools;

public class DeleteBendpointCommandTest {

	@Test
	public void test_ConnectorsPresent_FirstConnectionPart() {
		
		// get the test schema and locate set OFFICE-EMPLOYEE; this set has 2 connection parts (i.e.
		// connectors split the line) and each connection part has 2 bendpoints; we will be removing
		// the first bendpoint of the first connection part
		Schema schema = TestTools.getSchema("testdata/BendpointsAndConnectors.schema");
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Set set = schema.getSet("OFFICE-EMPLOYEE");
		assertEquals(2, set.getMembers().get(0).getConnectionParts().size());
		ConnectionPart originalConnectionPart1 = 
			set.getMembers().get(0).getConnectionParts().get(0);
		ConnectionPart originalConnectionPart2 = 
			set.getMembers().get(0).getConnectionParts().get(1);
		assertEquals(2, originalConnectionPart1.getBendpointLocations().size());
		DiagramLocation originalBendpoint1 = originalConnectionPart1.getBendpointLocations().get(0);
		DiagramLocation originalBendpoint2 = originalConnectionPart1.getBendpointLocations().get(1);
		assertEquals(2, originalConnectionPart2.getBendpointLocations().size());
		DiagramLocation originalBendpoint3 = originalConnectionPart2.getBendpointLocations().get(0);
		DiagramLocation originalBendpoint4 = originalConnectionPart2.getBendpointLocations().get(1);
		
		// create the command
		Command command = new DeleteBendpointCommand(originalConnectionPart1, 0);
		
		
		// execute the command and verify
		command.execute();
		ObjectGraph objectGraph2 = TestTools.asObjectGraph(schema);
		assertEquals(2, set.getMembers().get(0).getConnectionParts().size());
		assertEquals(1, originalConnectionPart1.getBendpointLocations().size());
		assertEquals("bendpoint [0] set OFFICE-EMPLOYEE (EMPLOYEE)", 
					 originalBendpoint1.getEyecatcher());
		assertSame(originalBendpoint2, originalConnectionPart1.getBendpointLocations().get(0));		
		assertEquals(2, originalConnectionPart2.getBendpointLocations().size());
		assertSame(originalBendpoint3, originalConnectionPart2.getBendpointLocations().get(0));
		assertEquals("bendpoint [1] set OFFICE-EMPLOYEE (EMPLOYEE)", 
					 originalBendpoint3.getEyecatcher());
		assertSame(originalBendpoint4, originalConnectionPart2.getBendpointLocations().get(1));
		assertEquals("bendpoint [2] set OFFICE-EMPLOYEE (EMPLOYEE)", 
				 	 originalBendpoint4.getEyecatcher());	
	
		
		// undo the command and verify
		command.undo();
		ObjectGraph objectGraph3 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph, objectGraph3);
		assertEquals(2, set.getMembers().get(0).getConnectionParts().size());
		assertEquals(2, originalConnectionPart1.getBendpointLocations().size());
		assertSame(originalBendpoint1, originalConnectionPart1.getBendpointLocations().get(0));
		assertEquals("bendpoint [0] set OFFICE-EMPLOYEE (EMPLOYEE)", 
					 originalBendpoint1.getEyecatcher());		
		assertSame(originalBendpoint2, originalConnectionPart1.getBendpointLocations().get(1));
		assertEquals("bendpoint [1] set OFFICE-EMPLOYEE (EMPLOYEE)", 
				 	 originalBendpoint2.getEyecatcher());	
		assertEquals(2, originalConnectionPart2.getBendpointLocations().size());
		assertSame(originalBendpoint3, originalConnectionPart2.getBendpointLocations().get(0));
		assertEquals("bendpoint [2] set OFFICE-EMPLOYEE (EMPLOYEE)", 
					 originalBendpoint3.getEyecatcher());
		assertSame(originalBendpoint4, originalConnectionPart2.getBendpointLocations().get(1));
		assertEquals("bendpoint [3] set OFFICE-EMPLOYEE (EMPLOYEE)", 
				 	 originalBendpoint4.getEyecatcher());
		
		
		// redo the command and verify
		command.redo();
		ObjectGraph objectGraph4 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph2, objectGraph4);
		assertEquals(2, set.getMembers().get(0).getConnectionParts().size());
		assertEquals(1, originalConnectionPart1.getBendpointLocations().size());
		assertSame(originalBendpoint2, originalConnectionPart1.getBendpointLocations().get(0));
		assertEquals("bendpoint [0] set OFFICE-EMPLOYEE (EMPLOYEE)", 
				 	 originalBendpoint2.getEyecatcher());
		assertEquals(2, originalConnectionPart2.getBendpointLocations().size());
		assertSame(originalBendpoint3, originalConnectionPart2.getBendpointLocations().get(0));
		assertEquals("bendpoint [1] set OFFICE-EMPLOYEE (EMPLOYEE)", 
					 originalBendpoint3.getEyecatcher());
		assertSame(originalBendpoint4, originalConnectionPart2.getBendpointLocations().get(1));
		assertEquals("bendpoint [2] set OFFICE-EMPLOYEE (EMPLOYEE)", 
				 	 originalBendpoint4.getEyecatcher());		
	}
	
	@Test
	public void test_ConnectorsPresent_SecondConnectionPart() {
		
		// get the test schema and locate set OFFICE-EMPLOYEE; this set has 2 connection parts (i.e.
		// connectors split the line) and each connection part has 2 bendpoints; we will be removing
		// the first bendpoint of the second connection part
		Schema schema = TestTools.getSchema("testdata/BendpointsAndConnectors.schema");
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);		
		Set set = schema.getSet("OFFICE-EMPLOYEE");
		assertEquals(2, set.getMembers().get(0).getConnectionParts().size());
		ConnectionPart originalConnectionPart1 = 
			set.getMembers().get(0).getConnectionParts().get(0);
		ConnectionPart originalConnectionPart2 = 
			set.getMembers().get(0).getConnectionParts().get(1);
		assertEquals(2, originalConnectionPart1.getBendpointLocations().size());
		DiagramLocation originalBendpoint1 = originalConnectionPart1.getBendpointLocations().get(0);
		DiagramLocation originalBendpoint2 = originalConnectionPart1.getBendpointLocations().get(1);
		assertEquals(2, originalConnectionPart2.getBendpointLocations().size());
		DiagramLocation originalBendpoint3 = originalConnectionPart2.getBendpointLocations().get(0);
		DiagramLocation originalBendpoint4 = originalConnectionPart2.getBendpointLocations().get(1);
		
		// create the command
		Command command = new DeleteBendpointCommand(originalConnectionPart2, 0);
		
		
		// execute the command and verify
		command.execute();
		ObjectGraph objectGraph2 = TestTools.asObjectGraph(schema);		
		assertEquals(2, set.getMembers().get(0).getConnectionParts().size());
		assertEquals(2, originalConnectionPart1.getBendpointLocations().size());
		assertSame(originalBendpoint1, originalConnectionPart1.getBendpointLocations().get(0));
		assertEquals("bendpoint [0] set OFFICE-EMPLOYEE (EMPLOYEE)", 
					 originalBendpoint1.getEyecatcher());
		assertSame(originalBendpoint2, originalConnectionPart1.getBendpointLocations().get(1));
		assertEquals("bendpoint [1] set OFFICE-EMPLOYEE (EMPLOYEE)", 
				 	 originalBendpoint2.getEyecatcher());
		assertEquals(1, originalConnectionPart2.getBendpointLocations().size());
		assertSame(originalBendpoint4, originalConnectionPart2.getBendpointLocations().get(0));
		assertEquals("bendpoint [2] set OFFICE-EMPLOYEE (EMPLOYEE)", 
				 	 originalBendpoint4.getEyecatcher());
	
		
		// undo the command and verify
		command.undo();
		ObjectGraph objectGraph3 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph, objectGraph3);
		assertEquals(2, set.getMembers().get(0).getConnectionParts().size());
		assertEquals(2, originalConnectionPart1.getBendpointLocations().size());
		assertSame(originalBendpoint1, originalConnectionPart1.getBendpointLocations().get(0));
		assertEquals("bendpoint [0] set OFFICE-EMPLOYEE (EMPLOYEE)", 
					 originalBendpoint1.getEyecatcher());		
		assertSame(originalBendpoint2, originalConnectionPart1.getBendpointLocations().get(1));
		assertEquals("bendpoint [1] set OFFICE-EMPLOYEE (EMPLOYEE)", 
				 	 originalBendpoint2.getEyecatcher());	
		assertEquals(2, originalConnectionPart2.getBendpointLocations().size());
		assertSame(originalBendpoint3, originalConnectionPart2.getBendpointLocations().get(0));
		assertEquals("bendpoint [2] set OFFICE-EMPLOYEE (EMPLOYEE)", 
					 originalBendpoint3.getEyecatcher());
		assertSame(originalBendpoint4, originalConnectionPart2.getBendpointLocations().get(1));
		assertEquals("bendpoint [3] set OFFICE-EMPLOYEE (EMPLOYEE)", 
				 	 originalBendpoint4.getEyecatcher());
		
		
		// redo the command and verify
		command.redo();
		ObjectGraph objectGraph4 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph2, objectGraph4);
		assertEquals(2, set.getMembers().get(0).getConnectionParts().size());
		assertEquals(2, originalConnectionPart1.getBendpointLocations().size());
		assertSame(originalBendpoint1, originalConnectionPart1.getBendpointLocations().get(0));
		assertEquals("bendpoint [0] set OFFICE-EMPLOYEE (EMPLOYEE)", 
					 originalBendpoint1.getEyecatcher());
		assertSame(originalBendpoint2, originalConnectionPart1.getBendpointLocations().get(1));
		assertEquals("bendpoint [1] set OFFICE-EMPLOYEE (EMPLOYEE)", 
				 	 originalBendpoint2.getEyecatcher());
		assertEquals(1, originalConnectionPart2.getBendpointLocations().size());
		assertSame(originalBendpoint4, originalConnectionPart2.getBendpointLocations().get(0));
		assertEquals("bendpoint [2] set OFFICE-EMPLOYEE (EMPLOYEE)", 
				 	 originalBendpoint4.getEyecatcher());
		
	}
	
	@Test
	public void test_NoConnectorsPresent() {
		
		// get the test schema and locate set DEPT-EMPLOYEE; this set has 1 connection part (i.e.
		// no connectors split the line) and the connection part has 2 bendpoints; we will be  
		// removing the first bendpoint 
		Schema schema = TestTools.getSchema("testdata/BendpointsAndConnectors.schema");
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);		
		Set set = schema.getSet("DEPT-EMPLOYEE");
		assertEquals(1, set.getMembers().get(0).getConnectionParts().size());
		ConnectionPart originalConnectionPart = 
			set.getMembers().get(0).getConnectionParts().get(0);
		assertEquals(2, originalConnectionPart.getBendpointLocations().size());
		DiagramLocation originalBendpoint1 = originalConnectionPart.getBendpointLocations().get(0);
		DiagramLocation originalBendpoint2 = originalConnectionPart.getBendpointLocations().get(1);		
		
		// create the command
		Command command = new DeleteBendpointCommand(originalConnectionPart, 0);
		
		
		// execute the command and verify
		command.execute();
		ObjectGraph objectGraph2 = TestTools.asObjectGraph(schema);		
		assertEquals(1, set.getMembers().get(0).getConnectionParts().size());
		assertEquals(1, originalConnectionPart.getBendpointLocations().size());
		assertSame(originalBendpoint2, originalConnectionPart.getBendpointLocations().get(0));
		assertEquals("bendpoint [0] set DEPT-EMPLOYEE (EMPLOYEE)", 
				 	 originalBendpoint2.getEyecatcher());
	
		
		// undo the command and verify
		command.undo();
		ObjectGraph objectGraph3 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph, objectGraph3);
		assertEquals(1, set.getMembers().get(0).getConnectionParts().size());
		assertEquals(2, originalConnectionPart.getBendpointLocations().size());
		assertSame(originalBendpoint1, originalConnectionPart.getBendpointLocations().get(0));
		assertEquals("bendpoint [0] set DEPT-EMPLOYEE (EMPLOYEE)", 
					 originalBendpoint1.getEyecatcher());		
		assertSame(originalBendpoint2, originalConnectionPart.getBendpointLocations().get(1));
		assertEquals("bendpoint [1] set DEPT-EMPLOYEE (EMPLOYEE)", 
				 	 originalBendpoint2.getEyecatcher());		
		
		
		// redo the command and verify
		command.redo();
		ObjectGraph objectGraph4 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph2, objectGraph4);
		assertEquals(1, set.getMembers().get(0).getConnectionParts().size());
		assertEquals(1, originalConnectionPart.getBendpointLocations().size());
		assertSame(originalBendpoint2, originalConnectionPart.getBendpointLocations().get(0));
		assertEquals("bendpoint [0] set DEPT-EMPLOYEE (EMPLOYEE)", 
				 	 originalBendpoint2.getEyecatcher());		
		
	}	

}
