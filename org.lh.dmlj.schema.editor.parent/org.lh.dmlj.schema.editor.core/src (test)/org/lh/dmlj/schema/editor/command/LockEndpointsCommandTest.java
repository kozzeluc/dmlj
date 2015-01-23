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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.lh.dmlj.schema.editor.testtool.TestTools.assertEquals;

import org.eclipse.draw2d.geometry.Point;
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

public class LockEndpointsCommandTest {

	@Test
	public void test_VerifyAnnotationsOnly() {
		
		// use the regular EMPSCHM schema
		Schema schema = TestTools.getEmpschmSchema();
		Set set = schema.getSet("OFFICE-EMPLOYEE");
		MemberRole memberRole = set.getMembers().get(0);
		
		// create the command
		Command command = new LockEndpointsCommand(memberRole, null, null);
		
		// execute the command
		command.execute();

		
		// undo the command
		command.undo();
		
		
		// redo the command
		command.redo();		
	}
	
	@Test
	public void test_BothEndpointsAlreadyPresent() {
		
		// use the regular EMPSCHM schema
		Schema schema = TestTools.getEmpschmSchema();
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);
		Set set = schema.getSet("OFFICE-EMPLOYEE");
		MemberRole memberRole = set.getMembers().get(0);
		assertEquals(1, memberRole.getConnectionParts().size());
		DiagramLocation sourceEndpoint =
			memberRole.getConnectionParts().get(0).getSourceEndpointLocation();
		assertNotNull(sourceEndpoint);
		assertEquals(65, sourceEndpoint.getX());
		assertEquals(53, sourceEndpoint.getY());
		DiagramLocation targetEndpoint =
			memberRole.getConnectionParts().get(0).getTargetEndpointLocation();
		assertNotNull(targetEndpoint);
		assertEquals(95, targetEndpoint.getX());
		assertEquals(0, targetEndpoint.getY());
		
		// create the command
		Command command = new LockEndpointsCommand(memberRole, new Point(1, 2), new Point(3, 4));
		
		
		// execute the command and verify (nothing should have happened)
		command.execute();
		ObjectGraph objectGraph2 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph, objectGraph2);
		Xmi xmi2 = TestTools.asXmi(schema);
		assertEquals(xmi, xmi2);
		assertSame(sourceEndpoint, 
				   memberRole.getConnectionParts().get(0).getSourceEndpointLocation());
		assertEquals(65, sourceEndpoint.getX());
		assertEquals(53, sourceEndpoint.getY());
		assertSame(targetEndpoint, 
				   memberRole.getConnectionParts().get(0).getTargetEndpointLocation());
		assertEquals(95, targetEndpoint.getX());
		assertEquals(0, targetEndpoint.getY());
		
		
		// undo the command and verify
		command.undo();
		ObjectGraph objectGraph3 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph, objectGraph3);
		Xmi xmi3 = TestTools.asXmi(schema);
		assertEquals(xmi, xmi3);
		
		
		// redo the command and verify
		command.redo();
		ObjectGraph objectGraph4 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph, objectGraph4);
		Xmi xmi4 = TestTools.asXmi(schema);
		assertEquals(xmi, xmi4);
		
	}
	
	@Test
	public void test_SourceEndpointAlreadyPresent() {
		
		// use the regular EMPSCHM schema
		Schema schema = TestTools.getEmpschmSchema();
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);
		Set set = schema.getSet("EMP-COVERAGE");
		assertEquals(1, set.getMembers().size());
		MemberRole memberRole = set.getMembers().get(0);
		assertEquals(1, memberRole.getConnectionParts().size());
		ConnectionPart connectionPart = set.getMembers().get(0).getConnectionParts().get(0);
		DiagramLocation sourceEndpoint = connectionPart.getSourceEndpointLocation();
		assertNotNull(sourceEndpoint);	
		assertEquals(65, sourceEndpoint.getX());
		assertEquals(53, sourceEndpoint.getY());	
		int sourceEndpointIndex = schema.getDiagramData().getLocations().indexOf(sourceEndpoint);
		assertNull(connectionPart.getTargetEndpointLocation());
		int originalDiagramLocationCount = schema.getDiagramData().getLocations().size();
		
		// create the command	
		Command command = new LockEndpointsCommand(memberRole, new Point(1, 2), new Point(3, 4));
		
		// execute the command; only the target endpoint should change
		command.execute();
		ObjectGraph objectGraph2 = TestTools.asObjectGraph(schema);
		Xmi xmi2 = TestTools.asXmi(schema);
		assertSame(sourceEndpoint, connectionPart.getSourceEndpointLocation());
		assertEquals(65, sourceEndpoint.getX());
		assertEquals(53, sourceEndpoint.getY());		
		assertEquals(sourceEndpointIndex, 
					 schema.getDiagramData().getLocations().indexOf(sourceEndpoint));
		DiagramLocation targetEndpoint = connectionPart.getTargetEndpointLocation();
		assertNotNull(targetEndpoint);
		assertEquals(3, targetEndpoint.getX());
		assertEquals(4, targetEndpoint.getY());
		assertEquals("set EMP-COVERAGE member endpoint (COVERAGE)", targetEndpoint.getEyecatcher());
		assertEquals(originalDiagramLocationCount + 1, 
					 schema.getDiagramData().getLocations().size());
		assertEquals(originalDiagramLocationCount, 
					 schema.getDiagramData().getLocations().indexOf(targetEndpoint));
		
		// undo the command and verify
		command.undo();
		ObjectGraph objectGraph3 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph, objectGraph3);
		Xmi xmi3 = TestTools.asXmi(schema);
		assertEquals(xmi, xmi3);
		assertNull(connectionPart.getTargetEndpointLocation());
		assertEquals(originalDiagramLocationCount, schema.getDiagramData().getLocations().size());
		
		
		// redo the command and verify
		command.redo();
		ObjectGraph objectGraph4 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph2, objectGraph4);
		Xmi xmi4 = TestTools.asXmi(schema);
		assertEquals(xmi2, xmi4);
		assertSame(targetEndpoint, connectionPart.getTargetEndpointLocation());
		
	}
	
	@Test
	public void test_TargetEndpointAlreadyPresent() {	
		
		// use the IDMSNTWK schema for this test
		Schema schema = TestTools.getIdmsntwkSchema();
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);
		Set set = schema.getSet("SSR-SSOR");
		assertEquals(1, set.getMembers().size());
		MemberRole memberRole = set.getMembers().get(0);
		assertEquals(1, memberRole.getConnectionParts().size());
		ConnectionPart connectionPart = set.getMembers().get(0).getConnectionParts().get(0);		
		assertNull(connectionPart.getSourceEndpointLocation());	
		DiagramLocation targetEndpoint = connectionPart.getTargetEndpointLocation();
		assertNotNull(targetEndpoint);
		assertEquals(65, targetEndpoint.getX());
		assertEquals(0, targetEndpoint.getY());		
		int targetEndpointIndex = schema.getDiagramData().getLocations().indexOf(targetEndpoint);
		int originalDiagramLocationCount = schema.getDiagramData().getLocations().size();		
		
		// create the command	
		Command command = new LockEndpointsCommand(memberRole, new Point(1, 2), new Point(3, 4));
		
		
		// execute the command; only the target endpoint should change
		command.execute();
		ObjectGraph objectGraph2 = TestTools.asObjectGraph(schema);
		Xmi xmi2 = TestTools.asXmi(schema);
		DiagramLocation sourceEndpoint = connectionPart.getSourceEndpointLocation();
		assertNotNull(sourceEndpoint);		
		assertEquals(1, sourceEndpoint.getX());
		assertEquals(2, sourceEndpoint.getY());
		assertEquals("set SSR-SSOR member endpoint (SSOR-034)", targetEndpoint.getEyecatcher());		
		assertSame(targetEndpoint, connectionPart.getTargetEndpointLocation());
		assertEquals(65, targetEndpoint.getX());
		assertEquals(0, targetEndpoint.getY());		
		assertEquals(originalDiagramLocationCount + 1, 
					 schema.getDiagramData().getLocations().size());
		assertEquals(originalDiagramLocationCount, 
					 schema.getDiagramData().getLocations().indexOf(sourceEndpoint));
		assertEquals(targetEndpointIndex, 
			     	 schema.getDiagramData().getLocations().indexOf(targetEndpoint));
		
		// undo the command and verify
		command.undo();
		ObjectGraph objectGraph3 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph, objectGraph3);
		Xmi xmi3 = TestTools.asXmi(schema);
		assertEquals(xmi, xmi3);
		assertNull(connectionPart.getSourceEndpointLocation());
		assertEquals(originalDiagramLocationCount, schema.getDiagramData().getLocations().size());
		
		
		// redo the command and verify
		command.redo();
		ObjectGraph objectGraph4 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph2, objectGraph4);
		Xmi xmi4 = TestTools.asXmi(schema);
		assertEquals(xmi2, xmi4);
		assertSame(sourceEndpoint, connectionPart.getSourceEndpointLocation());
				
	}
	
	@Test
	public void test_CreateBothEndpoints() {
		
		// use the regular EMPSCHM schema
		Schema schema = TestTools.getEmpschmSchema();
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);
		Set set = schema.getSet("EMP-EMPOSITION");
		assertEquals(1, set.getMembers().size());
		MemberRole memberRole = set.getMembers().get(0);
		assertEquals(1, memberRole.getConnectionParts().size());
		ConnectionPart connectionPart = set.getMembers().get(0).getConnectionParts().get(0);		
		assertNull(connectionPart.getSourceEndpointLocation());			
		assertNull(connectionPart.getTargetEndpointLocation());		
		int originalDiagramLocationCount = schema.getDiagramData().getLocations().size();
		
		// create the command	
		Command command = new LockEndpointsCommand(memberRole, new Point(1, 2), new Point(3, 4));
		
		
		// execute the command; only the target endpoint should change
		command.execute();
		ObjectGraph objectGraph2 = TestTools.asObjectGraph(schema);
		Xmi xmi2 = TestTools.asXmi(schema);
		DiagramLocation sourceEndpoint = connectionPart.getSourceEndpointLocation();
		assertNotNull(sourceEndpoint);		
		assertEquals(1, sourceEndpoint.getX());
		assertEquals(2, sourceEndpoint.getY());
		assertEquals("set EMP-EMPOSITION owner endpoint (EMPOSITION)", 
					 sourceEndpoint.getEyecatcher());
		DiagramLocation targetEndpoint = connectionPart.getTargetEndpointLocation();
		assertNotNull(targetEndpoint);
		assertEquals(3, targetEndpoint.getX());
		assertEquals(4, targetEndpoint.getY());		
		assertEquals("set EMP-EMPOSITION member endpoint (EMPOSITION)", 
					 targetEndpoint.getEyecatcher());
		assertEquals(originalDiagramLocationCount + 2, 
					 schema.getDiagramData().getLocations().size());
		assertEquals(originalDiagramLocationCount, 
					 schema.getDiagramData().getLocations().indexOf(sourceEndpoint));
		assertEquals(originalDiagramLocationCount + 1, 
			     	 schema.getDiagramData().getLocations().indexOf(targetEndpoint));
		
		// undo the command and verify
		command.undo();
		ObjectGraph objectGraph3 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph, objectGraph3);
		Xmi xmi3 = TestTools.asXmi(schema);
		assertEquals(xmi, xmi3);
		assertNull(connectionPart.getSourceEndpointLocation());
		assertNull(connectionPart.getTargetEndpointLocation());
		assertEquals(originalDiagramLocationCount, schema.getDiagramData().getLocations().size());
		
		
		// redo the command and verify
		command.redo();
		ObjectGraph objectGraph4 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph2, objectGraph4);
		Xmi xmi4 = TestTools.asXmi(schema);
		assertEquals(xmi2, xmi4);
		assertSame(sourceEndpoint, connectionPart.getSourceEndpointLocation());	
		assertSame(targetEndpoint, connectionPart.getTargetEndpointLocation());
		
	}
	
	@Test
	public void test_CreateNoEndpoint() {
		
		// use the regular EMPSCHM schema
		Schema schema = TestTools.getEmpschmSchema();
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);
		Set set = schema.getSet("EMP-EMPOSITION");
		assertEquals(1, set.getMembers().size());
		MemberRole memberRole = set.getMembers().get(0);
		assertEquals(1, memberRole.getConnectionParts().size());
		ConnectionPart connectionPart = set.getMembers().get(0).getConnectionParts().get(0);		
		assertNull(connectionPart.getSourceEndpointLocation());			
		assertNull(connectionPart.getTargetEndpointLocation());		
		int originalDiagramLocationCount = schema.getDiagramData().getLocations().size();
		
		// create the command - no endpoint will be created
		Command command = new LockEndpointsCommand(memberRole, null, null);
		
		
		// execute the command; only the target endpoint should change
		command.execute();
		ObjectGraph objectGraph2 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph, objectGraph2);
		Xmi xmi2 = TestTools.asXmi(schema);
		assertEquals(xmi, xmi2);		
		assertNull(connectionPart.getSourceEndpointLocation());
		assertNull(connectionPart.getTargetEndpointLocation());
		
		// undo the command and verify
		command.undo();
		ObjectGraph objectGraph3 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph, objectGraph3);
		Xmi xmi3 = TestTools.asXmi(schema);
		assertEquals(xmi, xmi3);
		assertNull(connectionPart.getSourceEndpointLocation());
		assertNull(connectionPart.getTargetEndpointLocation());
		assertEquals(originalDiagramLocationCount, schema.getDiagramData().getLocations().size());
		
		
		// redo the command and verify
		command.redo();
		ObjectGraph objectGraph4 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph2, objectGraph4);
		Xmi xmi4 = TestTools.asXmi(schema);
		assertEquals(xmi2, xmi4);
		assertNull(connectionPart.getSourceEndpointLocation());
		assertNull(connectionPart.getTargetEndpointLocation());
		
	}	

}
