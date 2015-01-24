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

import org.eclipse.gef.commands.Command;
import org.junit.Test;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.testtool.ObjectGraph;
import org.lh.dmlj.schema.editor.testtool.TestTools;
import org.lh.dmlj.schema.editor.testtool.Xmi;

public class DeleteConnectorCommandTest {

	@Test
	public void test() {
		
		// get the EMPSCHM schema dedicated to bendpoint and connector tests and locate set 
		// OFFICE-EMPLOYEE; we will delete the connectors which are after the 2nd of 4 bendpoints  
		Schema schema = TestTools.getSchema("testdata/BendpointsAndConnectors.schema");
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);		
		Xmi xmi = TestTools.asXmi(schema);
		Set set = schema.getSet("OFFICE-EMPLOYEE");
		MemberRole memberRole = set.getMembers().get(0);
		assertEquals(2, memberRole.getConnectionParts().size());
		ConnectionPart connectionPart1 = memberRole.getConnectionParts().get(0);
		assertEquals(2, connectionPart1.getBendpointLocations().size());
		DiagramLocation bendpoint1 = connectionPart1.getBendpointLocations().get(0);
		DiagramLocation bendpoint2 = connectionPart1.getBendpointLocations().get(1);
		ConnectionPart connectionPart2 = memberRole.getConnectionParts().get(1);
		assertEquals(2, connectionPart2.getBendpointLocations().size());
		DiagramLocation bendpoint3 = connectionPart2.getBendpointLocations().get(0);
		DiagramLocation bendpoint4 = connectionPart2.getBendpointLocations().get(1);
		Connector connector1 = connectionPart1.getConnector();
		Connector connector2 = connectionPart2.getConnector();		
		DiagramLocation sourceEndpoint = connectionPart1.getSourceEndpointLocation();
		assertNotNull(sourceEndpoint);
		DiagramLocation targetEndpoint = connectionPart2.getTargetEndpointLocation();
		assertNotNull(targetEndpoint);
		int originalConnectionPartCount = schema.getDiagramData().getConnectionParts().size();
		int originalConnectorCount = schema.getDiagramData().getConnectors().size();
		
		// create the command
		Command command = new DeleteConnectorsCommand(memberRole);
		
		
		// execute the command and verify
		command.execute();
		ObjectGraph objectGraph2 = TestTools.asObjectGraph(schema);
		Xmi xmi2 = TestTools.asXmi(schema);
		assertEquals(1, memberRole.getConnectionParts().size());
		assertEquals(4, connectionPart1.getBendpointLocations().size());
		assertSame(bendpoint1, connectionPart1.getBendpointLocations().get(0));
		assertSame(bendpoint2, connectionPart1.getBendpointLocations().get(1));
		assertSame(bendpoint3, connectionPart1.getBendpointLocations().get(2));
		assertSame(bendpoint4, connectionPart1.getBendpointLocations().get(3));
		assertNull(connectionPart1.getConnector());
		assertSame(sourceEndpoint, connectionPart1.getSourceEndpointLocation());
		assertSame(targetEndpoint, connectionPart1.getTargetEndpointLocation());
		assertEquals(originalConnectionPartCount - 1, 
					 schema.getDiagramData().getConnectionParts().size());
		assertEquals(originalConnectorCount - 2, schema.getDiagramData().getConnectors().size());		
		
		
		// undo the command and verify
		command.undo();
		ObjectGraph objectGraph3 = TestTools.asObjectGraph(schema);		
		assertEquals(objectGraph, objectGraph3);
		Xmi xmi3 = TestTools.asXmi(schema);
		assertEquals(xmi, xmi3);
		assertSame(connector1, connectionPart1.getConnector());
		assertSame(connector2, connectionPart2.getConnector());		
		
		
		// redo the command and verify
		command.redo();
		ObjectGraph objectGraph4 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph2, objectGraph4);
		Xmi xmi4 = TestTools.asXmi(schema);
		assertEquals(xmi2, xmi4);		
		
	}

}
