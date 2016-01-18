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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.lh.dmlj.schema.editor.testtool.TestTools.assertEquals;

import org.eclipse.gef.commands.Command;
import org.junit.Test;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.testtool.ObjectGraph;
import org.lh.dmlj.schema.editor.testtool.TestTools;
import org.lh.dmlj.schema.editor.testtool.Xmi;

public class MoveEndpointCommandTest {

	@Test
	public void test_CreateSourceEndpoint() {
		
		// We'll perform our test with the regular EMPSCHM version 100 schema; example of a set with
		// neither source or target endpoint: EMP-EMPOSITION.  Example of a set with both source and 
		// target endpoints: DEPT-EMPLOYEE.
		Schema schema = TestTools.getEmpschmSchema();		
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);
		Set set = schema.getSet("EMP-EMPOSITION");
		assertEquals(1, set.getMembers().size());
		MemberRole memberRole = set.getMembers().get(0);
		assertEquals(1, memberRole.getConnectionParts().size());
		ConnectionPart connectionPart = memberRole.getConnectionParts().get(0);
		assertNull(connectionPart.getSourceEndpointLocation());
		assertNull(connectionPart.getTargetEndpointLocation());
		int originalLocationsCount = schema.getDiagramData().getLocations().size();
		
		// create the command (the coordinates don't have to be meaningful)
		Command command = new MoveEndpointCommand(connectionPart, 10, 20, true);
		
		
		// execute the command and verify
		command.execute();
		ObjectGraph objectGraph2 = TestTools.asObjectGraph(schema);
		Xmi xmi2 = TestTools.asXmi(schema);
		DiagramLocation sourceEndpoint = connectionPart.getSourceEndpointLocation();
		assertNotNull(sourceEndpoint);
		assertEquals(10, sourceEndpoint.getX());
		assertEquals(20, sourceEndpoint.getY());
		assertEquals("set EMP-EMPOSITION owner endpoint (EMPOSITION)", 
					 sourceEndpoint.getEyecatcher());
		assertNull(connectionPart.getTargetEndpointLocation());
		assertEquals(originalLocationsCount + 1, schema.getDiagramData().getLocations().size());
		assertEquals(originalLocationsCount, 
					 schema.getDiagramData().getLocations().indexOf(sourceEndpoint));
		
		
		// undo the command and verify
		command.undo();
		ObjectGraph objectGraph3 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph, objectGraph3);
		Xmi xmi3 = TestTools.asXmi(schema);
		assertEquals(xmi, xmi3);		
		
		
		// redo the command and verify
		command.redo();
		ObjectGraph objectGraph4 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph2, objectGraph4);
		Xmi xmi4 = TestTools.asXmi(schema);
		assertEquals(xmi2, xmi4);		
	}
	
	@Test
	public void test_CreateTargetEndpoint() {
		
		// We'll perform our test with the regular EMPSCHM version 100 schema; example of a set with
		// neither source or target endpoint: EMP-EMPOSITION.  Example of a set with both source and 
		// target endpoints: DEPT-EMPLOYEE.
		Schema schema = TestTools.getEmpschmSchema();		
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);
		Set set = schema.getSet("EMP-EMPOSITION");
		assertEquals(1, set.getMembers().size());
		MemberRole memberRole = set.getMembers().get(0);
		assertEquals(1, memberRole.getConnectionParts().size());
		ConnectionPart connectionPart = memberRole.getConnectionParts().get(0);
		assertNull(connectionPart.getSourceEndpointLocation());
		assertNull(connectionPart.getTargetEndpointLocation());
		int originalLocationsCount = schema.getDiagramData().getLocations().size();
		
		// create the command (the coordinates don't have to be meaningful)
		Command command = new MoveEndpointCommand(connectionPart, 10, 20, false);
		
		
		// execute the command and verify
		command.execute();
		ObjectGraph objectGraph2 = TestTools.asObjectGraph(schema);
		Xmi xmi2 = TestTools.asXmi(schema);
		assertNull(connectionPart.getSourceEndpointLocation());		
		DiagramLocation targetEndpoint = connectionPart.getTargetEndpointLocation();
		assertNotNull(targetEndpoint);
		assertEquals(10, targetEndpoint.getX());
		assertEquals(20, targetEndpoint.getY());
		assertEquals("set EMP-EMPOSITION member endpoint (EMPOSITION)", 
					 targetEndpoint.getEyecatcher());
		assertEquals(originalLocationsCount + 1, schema.getDiagramData().getLocations().size());
		assertEquals(originalLocationsCount, 
					 schema.getDiagramData().getLocations().indexOf(targetEndpoint));		
		
		
		// undo the command and verify
		command.undo();
		ObjectGraph objectGraph3 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph, objectGraph3);
		Xmi xmi3 = TestTools.asXmi(schema);
		assertEquals(xmi, xmi3);
		
		
		// redo the command and verify
		command.redo();
		ObjectGraph objectGraph4 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph2, objectGraph4);
		Xmi xmi4 = TestTools.asXmi(schema);
		assertEquals(xmi2, xmi4);	
	}
	
	@Test
	public void test_MoveSourceEndpoint() {
		
		// We'll perform our test with the regular EMPSCHM version 100 schema; example of a set with
		// neither source or target endpoint: EMP-EMPOSITION.  Example of a set with both source and 
		// target endpoints: DEPT-EMPLOYEE.
		Schema schema = TestTools.getEmpschmSchema();		
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);
		Set set = schema.getSet("DEPT-EMPLOYEE");
		assertEquals(1, set.getMembers().size());
		MemberRole memberRole = set.getMembers().get(0);
		assertEquals(1, memberRole.getConnectionParts().size());
		ConnectionPart connectionPart = memberRole.getConnectionParts().get(0);
		DiagramLocation sourceEndpoint = connectionPart.getSourceEndpointLocation();
		assertNotNull(sourceEndpoint);
		assertNotEquals(10, sourceEndpoint.getX());
		assertNotEquals(20, sourceEndpoint.getY());
		DiagramLocation targetEndpoint = connectionPart.getTargetEndpointLocation();
		assertNotNull(targetEndpoint);
		int originalLocationsCount = schema.getDiagramData().getLocations().size();
		
		// create the command (the coordinates don't have to be meaningful)
		Command command = new MoveEndpointCommand(connectionPart, 10, 20, true);
		
		
		// execute the command and verify
		command.execute();
		ObjectGraph objectGraph2 = TestTools.asObjectGraph(schema);
		Xmi xmi2 = TestTools.asXmi(schema);
		DiagramLocation sourceEndpoint2 = connectionPart.getSourceEndpointLocation();
		assertNotSame(sourceEndpoint, sourceEndpoint2);
		assertEquals(10, sourceEndpoint2.getX());
		assertEquals(20, sourceEndpoint2.getY());
		assertEquals("set DEPT-EMPLOYEE owner endpoint (EMPLOYEE)", 
					 sourceEndpoint.getEyecatcher());
		assertSame(targetEndpoint, connectionPart.getTargetEndpointLocation());
		assertEquals("set DEPT-EMPLOYEE member endpoint (EMPLOYEE)", 
			 	 	 targetEndpoint.getEyecatcher());
		assertEquals(originalLocationsCount, schema.getDiagramData().getLocations().size());
		assertEquals(-1, schema.getDiagramData().getLocations().indexOf(sourceEndpoint));
		assertEquals(originalLocationsCount - 1, 
					 schema.getDiagramData().getLocations().indexOf(sourceEndpoint2));
		
		
		// undo the command and verify
		command.undo();
		ObjectGraph objectGraph3 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph, objectGraph3);
		Xmi xmi3 = TestTools.asXmi(schema);
		assertEquals(xmi, xmi3);		
		
		
		// redo the command and verify
		command.redo();
		ObjectGraph objectGraph4 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph2, objectGraph4);
		Xmi xmi4 = TestTools.asXmi(schema);
		assertEquals(xmi2, xmi4);
		
	}

	@Test
	public void test_MoveTargetEndpoint() {
		
		// We'll perform our test with the regular EMPSCHM version 100 schema; example of a set with
		// neither source or target endpoint: EMP-EMPOSITION.  Example of a set with both source and 
		// target endpoints: DEPT-EMPLOYEE.
		Schema schema = TestTools.getEmpschmSchema();		
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);
		Set set = schema.getSet("DEPT-EMPLOYEE");
		assertEquals(1, set.getMembers().size());
		MemberRole memberRole = set.getMembers().get(0);
		assertEquals(1, memberRole.getConnectionParts().size());
		ConnectionPart connectionPart = memberRole.getConnectionParts().get(0);
		DiagramLocation sourceEndpoint = connectionPart.getSourceEndpointLocation();
		assertNotNull(sourceEndpoint);
		assertNotEquals(10, sourceEndpoint.getX());
		assertNotEquals(20, sourceEndpoint.getY());
		DiagramLocation targetEndpoint = connectionPart.getTargetEndpointLocation();
		assertNotNull(targetEndpoint);
		int originalLocationsCount = schema.getDiagramData().getLocations().size();
		
		// create the command (the coordinates don't have to be meaningful)
		Command command = new MoveEndpointCommand(connectionPart, 10, 20, false);
		
		
		// execute the command and verify
		command.execute();
		ObjectGraph objectGraph2 = TestTools.asObjectGraph(schema);
		Xmi xmi2 = TestTools.asXmi(schema);
		DiagramLocation sourceEndpoint2 = connectionPart.getSourceEndpointLocation();
		assertSame(sourceEndpoint, sourceEndpoint2);		
		assertEquals("set DEPT-EMPLOYEE owner endpoint (EMPLOYEE)", 
					 sourceEndpoint.getEyecatcher());
		DiagramLocation targetEndpoint2 = connectionPart.getTargetEndpointLocation();
		assertNotNull(targetEndpoint2);
		assertNotSame(targetEndpoint, targetEndpoint2);
		assertEquals(10, targetEndpoint2.getX());
		assertEquals(20, targetEndpoint2.getY());
		assertEquals("set DEPT-EMPLOYEE member endpoint (EMPLOYEE)", 
				 	 targetEndpoint2.getEyecatcher());
		assertEquals(originalLocationsCount, schema.getDiagramData().getLocations().size());
		assertEquals(-1, schema.getDiagramData().getLocations().indexOf(targetEndpoint));
		assertEquals(originalLocationsCount - 1, 
					 schema.getDiagramData().getLocations().indexOf(targetEndpoint2));
		
		
		// undo the command and verify
		command.undo();
		ObjectGraph objectGraph3 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph, objectGraph3);
		Xmi xmi3 = TestTools.asXmi(schema);
		assertEquals(xmi, xmi3);		
		
		
		// redo the command and verify
		command.redo();
		ObjectGraph objectGraph4 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph2, objectGraph4);
		Xmi xmi4 = TestTools.asXmi(schema);
		assertEquals(xmi2, xmi4);
		
	}
	
	@Test
	public void testAlternativeConstructor() {
		// use the alternative constructor (accepting as its arguments 'connectionPartProvider' 
		// (IConnectionPartProvider), newX (int), newY (int) and 'source' (boolean) when the 
		// connection part instance (see the other constructor) is NOT yet known at command 
		// construction time
		Schema schema = TestTools.getSchema("testdata/BendpointsAndConnectors.schema");
		Set set = schema.getSet("DEPT-EMPLOYEE");
		final ConnectionPart connectionPart = set.getMembers().get(0).getConnectionParts().get(0);
		assertNotNull(connectionPart);
		ISupplier<ConnectionPart> connectionPartSupplier = new ISupplier<ConnectionPart>() {
			@Override
			public ConnectionPart supply() {
				return connectionPart;
			}		
		};
		MoveEndpointCommand command = new MoveEndpointCommand(connectionPartSupplier, 0, 0, true);
		command.execute();
		assertSame(connectionPart, command.connectionPart);
		command.undo();
		command.redo();
	}

}
