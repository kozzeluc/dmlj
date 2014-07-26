/**
 * Copyright (C) 2013  Luc Hermans
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.lh.dmlj.schema.editor.testtool.TestTools.assertEquals;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gef.commands.Command;
import org.junit.Test;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.command.annotation.Item;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory;
import org.lh.dmlj.schema.editor.command.annotation.Owner;
import org.lh.dmlj.schema.editor.command.annotation.Reference;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeDispatcher;
import org.lh.dmlj.schema.editor.testtool.ObjectGraph;
import org.lh.dmlj.schema.editor.testtool.TestTools;

public class CreateConnectorCommandTest {

	@Test
	public void testLineFragmentsCollection() {
		
		// we'll use the EMPSCHM schema for our first tests 
		Schema schema = TestTools.getEmpschmSchema();		
		
		
		// EMP-EXPERTISE has no source nor a target endpoint and thus no bendpoints neither
		MemberRole memberRole = schema.getSet("EMP-EXPERTISE").getMembers().get(0);
		assertNull(memberRole.getConnectionParts().get(0).getSourceEndpointLocation());
		assertNull(memberRole.getConnectionParts().get(0).getTargetEndpointLocation());
		
		// create a command (the location argument constructor is irrelevant at this point)
		CreateConnectorCommand command = new CreateConnectorCommand(memberRole, new Point(0, 0));
		
		// a list containing the coordinates for 1 line will be returned
		List<Point[]> lineCoordinates = command.getAbsoluteLineCoordinates();
		assertNotNull(lineCoordinates);
		assertEquals(1, lineCoordinates.size());
		Point[] line = lineCoordinates.get(0);
		Point source = line[0];
		Point target = line[1];
		DiagramLocation ownerLocation = 
			memberRole.getSet().getOwner().getRecord().getDiagramLocation();
		DiagramLocation memberLocation = memberRole.getRecord().getDiagramLocation();
		assertEquals(ownerLocation.getX() + 65, source.x);
		assertEquals(ownerLocation.getY() + 26, source.y);
		assertEquals(memberLocation.getX() + 65, target.x);
		assertEquals(memberLocation.getY() + 26, target.y);
				
		
		// EMP-NAME-NDX has no source nor a target endpoint and thus no bendpoints neither
		memberRole = schema.getSet("EMP-NAME-NDX").getMembers().get(0);
		assertNull(memberRole.getConnectionParts().get(0).getSourceEndpointLocation());
		assertNull(memberRole.getConnectionParts().get(0).getTargetEndpointLocation());
		
		// create a command (the location argument constructor is irrelevant at this point)
		command = new CreateConnectorCommand(memberRole, new Point(0, 0));
		
		// a list containing the coordinates for 1 line will be returned
		lineCoordinates = command.getAbsoluteLineCoordinates();
		assertNotNull(lineCoordinates);
		assertEquals(1, lineCoordinates.size());
		line = lineCoordinates.get(0);
		source = line[0];
		target = line[1];
		ownerLocation = memberRole.getSet().getSystemOwner().getDiagramLocation();
		memberLocation = memberRole.getRecord().getDiagramLocation();
		assertEquals(ownerLocation.getX() + 11, source.x);
		assertEquals(ownerLocation.getY() + 22, source.y);
		assertEquals(memberLocation.getX() + 65, target.x);
		assertEquals(memberLocation.getY() + 26, target.y);		
		
		
		// we'll now use the IDMSNTWK schema for a couple of further tests
		schema = TestTools.getIdmsntwkSchema();		
		
		
		// MAP-MAPCMT has a source endpoint but no target endpoint (indexes will NEVER have a source 
		// endpoint so we don't have to test that situation)
		memberRole = schema.getSet("MAP-MAPCMT").getMembers().get(0);
		assertNotNull(memberRole.getConnectionParts().get(0).getSourceEndpointLocation());
		assertNull(memberRole.getConnectionParts().get(0).getTargetEndpointLocation());
		
		// create a command (the location argument constructor is irrelevant at this point)
		command = new CreateConnectorCommand(memberRole, new Point(0, 0));
		
		// a list containing the coordinates for 1 line will be returned
		lineCoordinates = command.getAbsoluteLineCoordinates();
		assertNotNull(lineCoordinates);
		assertEquals(1, lineCoordinates.size());
		line = lineCoordinates.get(0);
		source = line[0];
		target = line[1];
		ownerLocation = memberRole.getSet().getOwner().getRecord().getDiagramLocation();
		memberLocation = memberRole.getRecord().getDiagramLocation();
		assertEquals(ownerLocation.getX() +
					 memberRole.getConnectionParts().get(0).getSourceEndpointLocation().getX(), 
					 source.x);
		assertEquals(ownerLocation.getY() +
					 memberRole.getConnectionParts().get(0).getSourceEndpointLocation().getY(), 
					 source.y);
		assertEquals(memberLocation.getX() + 65, target.x);
		assertEquals(memberLocation.getY() + 26, target.y);	
		
		
		// SSR-SSOR has a target endpoint but no source endpoint 
		memberRole = schema.getSet("SSR-SSOR").getMembers().get(0);
		assertNull(memberRole.getConnectionParts().get(0).getSourceEndpointLocation());
		assertNotNull(memberRole.getConnectionParts().get(0).getTargetEndpointLocation());
		
		// create a command (the location argument constructor is irrelevant at this point)
		command = new CreateConnectorCommand(memberRole, new Point(0, 0));
		
		// a list containing the coordinates for 1 line will be returned
		lineCoordinates = command.getAbsoluteLineCoordinates();
		assertNotNull(lineCoordinates);
		assertEquals(1, lineCoordinates.size());
		line = lineCoordinates.get(0);
		source = line[0];
		target = line[1];
		ownerLocation = memberRole.getSet().getOwner().getRecord().getDiagramLocation();
		memberLocation = memberRole.getRecord().getDiagramLocation();
		assertEquals(ownerLocation.getX() + 65, source.x);
		assertEquals(ownerLocation.getY() + 26, source.y);
		assertEquals(memberLocation.getX() +
				 	 memberRole.getConnectionParts().get(0).getTargetEndpointLocation().getX(), 
				 	 target.x);
		assertEquals(memberLocation.getY() +
			 	 	 memberRole.getConnectionParts().get(0).getTargetEndpointLocation().getY(), 
			 	 	 target.y);			
	
		
		// IX-AREA has a target endpoint but no source endpoint
		memberRole = schema.getSet("IX-AREA").getMembers().get(0);
		assertNull(memberRole.getConnectionParts().get(0).getSourceEndpointLocation());
		assertNotNull(memberRole.getConnectionParts().get(0).getTargetEndpointLocation());
		
		// create a command (the location argument constructor is irrelevant at this point)
		command = new CreateConnectorCommand(memberRole, new Point(0, 0));
		
		// a list containing the coordinates for 1 line will be returned
		lineCoordinates = command.getAbsoluteLineCoordinates();
		assertNotNull(lineCoordinates);
		assertEquals(1, lineCoordinates.size());
		line = lineCoordinates.get(0);
		source = line[0];
		target = line[1];
		ownerLocation = memberRole.getSet().getSystemOwner().getDiagramLocation();
		memberLocation = memberRole.getRecord().getDiagramLocation();
		assertEquals(ownerLocation.getX() + 11, source.x);
		assertEquals(ownerLocation.getY() + 22, source.y);
		assertEquals(memberLocation.getX() +
				 	 memberRole.getConnectionParts().get(0).getTargetEndpointLocation().getX(), 
				 	 target.x);
		assertEquals(memberLocation.getY() +
			 	 	 memberRole.getConnectionParts().get(0).getTargetEndpointLocation().getY(), 
			 	 	 target.y);	
		
		// we'll use the EMPSCHM schema again for the remainder of the tests 
		schema = TestTools.getEmpschmSchema();
		
		
		// tests with set COVERAGE-CLAIMS (1 line for the HOSPITAL-CLAIM member; no bendpoints)
		memberRole = schema.getSet("COVERAGE-CLAIMS").getMembers().get(0);
		assertEquals("HOSPITAL-CLAIM", memberRole.getRecord().getName());
		assertEquals(1, memberRole.getConnectionParts().size());
		assertNotNull(memberRole.getConnectionParts().get(0).getSourceEndpointLocation());
		assertNotNull(memberRole.getConnectionParts().get(0).getTargetEndpointLocation());
		assertEquals(0, memberRole.getConnectionParts().get(0).getBendpointLocations().size());
		command = new CreateConnectorCommand(memberRole, new Point(0, 0));
		lineCoordinates = command.getAbsoluteLineCoordinates();
		assertNotNull(lineCoordinates);
		assertEquals(1, lineCoordinates.size());
		line = lineCoordinates.get(0);
		source = line[0];
		target = line[1];
		ownerLocation = memberRole.getSet().getOwner().getRecord().getDiagramLocation();
		memberLocation = memberRole.getRecord().getDiagramLocation();
		assertEquals(ownerLocation.getX() + 
					 memberRole.getConnectionParts().get(0).getSourceEndpointLocation().getX(), 
					 source.x);
		assertEquals(ownerLocation.getY() + 
					 memberRole.getConnectionParts().get(0).getSourceEndpointLocation().getY(), 
					 source.y);
		assertEquals(memberLocation.getX() +
				 	 memberRole.getConnectionParts().get(0).getTargetEndpointLocation().getX(), 
				 	 target.x);
		assertEquals(memberLocation.getY() +
			 	 	 memberRole.getConnectionParts().get(0).getTargetEndpointLocation().getY(), 
			 	 	 target.y);		
		
		// tests with set OFFICE-EMPLOYEE (3 lines; 2 bendpoints):
		// 464,72 - 464,190
		// 464,190 - 380,190
		// 380,190 - 380,247
		// the source endpoint is relative to the owner, all bendpoints are relative to the owner as
		// well, whereas the target endpoint is relative to the member record; that's why the target 
		// endpoint needs to be converted to be also relative to the owner
		memberRole = schema.getSet("OFFICE-EMPLOYEE").getMembers().get(0);
		assertEquals(1, memberRole.getConnectionParts().size());
		assertEquals(2, memberRole.getConnectionParts().get(0).getBendpointLocations().size());
		command = new CreateConnectorCommand(memberRole, new Point(0, 0));
		lineCoordinates = command.getAbsoluteLineCoordinates();
		assertNotNull(lineCoordinates);
		assertEquals(3, lineCoordinates.size());
		// first line:
		assertEquals(2, lineCoordinates.get(0).length); 
		assertEquals(464, lineCoordinates.get(0)[0].x);	// source endpoint x
		assertEquals(72, lineCoordinates.get(0)[0].y);	// source endpoint y
		assertEquals(464, lineCoordinates.get(0)[1].x);	// bendpoint[0] x
		assertEquals(190, lineCoordinates.get(0)[1].y);	// bendpoint[0] y
		// second line:
		assertEquals(2, lineCoordinates.get(1).length);		
		assertEquals(464, lineCoordinates.get(1)[0].x);	// bendpoint[0] x
		assertEquals(190, lineCoordinates.get(1)[0].y);	// bendpoint[0] y
		assertEquals(380, lineCoordinates.get(1)[1].x);	// bendpoint[1] x
		assertEquals(190, lineCoordinates.get(1)[1].y);	// bendpoint[1] y
		// third line:
		assertEquals(2, lineCoordinates.get(2).length);
		assertEquals(380, lineCoordinates.get(2)[0].x);	// bendpoint[1] x
		assertEquals(190, lineCoordinates.get(2)[0].y);	// bendpoint[1] y
		assertEquals(380, lineCoordinates.get(2)[1].x);	// target endpoint x
		assertEquals(247, lineCoordinates.get(2)[1].y);	// target endpoint y
		
		
	}
	
	@Test
	public void testConnectorLayoutDetermination() {		
		
		// a perfectly horizontal line; left to right
		Point[] lineFragment = new Point[] {new Point(100, 50), new Point(200, 50)};
		CreateConnectorCommand.ConnectorLayout layout = 
			CreateConnectorCommand.getConnectorLayout(lineFragment);
		assertSame(CreateConnectorCommand.ConnectorLayout.HORIZONTAL, layout);
		
		// a perfectly horizontal line; right to left
		lineFragment = new Point[] {new Point(200, 50), new Point(100, 50)};
		layout = CreateConnectorCommand.getConnectorLayout(lineFragment);
		assertSame(CreateConnectorCommand.ConnectorLayout.HORIZONTAL, layout);
		
		
		// a perfectly vertical line; top-down
		lineFragment = new Point[] {new Point(50, 100), new Point(50, 200)};
		layout = CreateConnectorCommand.getConnectorLayout(lineFragment);
		assertSame(CreateConnectorCommand.ConnectorLayout.VERTICAL, layout);
		
		// a perfectly vertical line; bottom-up
		lineFragment = new Point[] {new Point(50, 200), new Point(50, 100)};
		layout = CreateConnectorCommand.getConnectorLayout(lineFragment);
		assertSame(CreateConnectorCommand.ConnectorLayout.VERTICAL, layout);
		
		
		// a diagonal line; top left to bottom right
		lineFragment = new Point[] {new Point(0, 0), new Point(100, 100)};
		layout = CreateConnectorCommand.getConnectorLayout(lineFragment);
		assertSame(CreateConnectorCommand.ConnectorLayout.HORIZONTAL, layout);
		
		// a diagonal line; top right to bottom left
		lineFragment = new Point[] {new Point(100, 0), new Point(0, 100)};
		layout = CreateConnectorCommand.getConnectorLayout(lineFragment);
		assertSame(CreateConnectorCommand.ConnectorLayout.HORIZONTAL, layout);
		
		// a diagonal line; bottom left to top right 
		lineFragment = new Point[] {new Point(0, 100), new Point(100, 0)};
		layout = CreateConnectorCommand.getConnectorLayout(lineFragment);
		assertSame(CreateConnectorCommand.ConnectorLayout.HORIZONTAL, layout);		
		
		// a diagonal line; bottom right to top left 
		lineFragment = new Point[] {new Point(100, 100), new Point(0, 0)};
		layout = CreateConnectorCommand.getConnectorLayout(lineFragment);
		assertSame(CreateConnectorCommand.ConnectorLayout.HORIZONTAL, layout);
		

		// a diagonal line; top left to bottom right
		lineFragment = new Point[] {new Point(0, 0), new Point(100, 99)};
		layout = CreateConnectorCommand.getConnectorLayout(lineFragment);
		assertSame(CreateConnectorCommand.ConnectorLayout.HORIZONTAL, layout);
		
		// a diagonal line; top right to bottom left
		lineFragment = new Point[] {new Point(100, 0), new Point(0, 99)};
		layout = CreateConnectorCommand.getConnectorLayout(lineFragment);
		assertSame(CreateConnectorCommand.ConnectorLayout.HORIZONTAL, layout);
		
		// a diagonal line; bottom left to top right 
		lineFragment = new Point[] {new Point(0, 99), new Point(100, 0)};
		layout = CreateConnectorCommand.getConnectorLayout(lineFragment);
		assertSame(CreateConnectorCommand.ConnectorLayout.HORIZONTAL, layout);		
		
		// a diagonal line; bottom right to top left 
		lineFragment = new Point[] {new Point(100, 99), new Point(0, 0)};
		layout = CreateConnectorCommand.getConnectorLayout(lineFragment);
		assertSame(CreateConnectorCommand.ConnectorLayout.HORIZONTAL, layout);
		
		
		// a diagonal line; top left to bottom right
		lineFragment = new Point[] {new Point(0, 0), new Point(100, 101)};
		layout = CreateConnectorCommand.getConnectorLayout(lineFragment);
		assertSame(CreateConnectorCommand.ConnectorLayout.VERTICAL, layout);
		
		// a diagonal line; top right to bottom left
		lineFragment = new Point[] {new Point(100, 0), new Point(0, 101)};
		layout = CreateConnectorCommand.getConnectorLayout(lineFragment);
		assertSame(CreateConnectorCommand.ConnectorLayout.VERTICAL, layout);
		
		// a diagonal line; bottom left to top right 
		lineFragment = new Point[] {new Point(0, 101), new Point(100, 0)};
		layout = CreateConnectorCommand.getConnectorLayout(lineFragment);
		assertSame(CreateConnectorCommand.ConnectorLayout.VERTICAL, layout);		
		
		// a diagonal line; bottom right to top left 
		lineFragment = new Point[] {new Point(100, 101), new Point(0, 0)};
		layout = CreateConnectorCommand.getConnectorLayout(lineFragment);
		assertSame(CreateConnectorCommand.ConnectorLayout.VERTICAL, layout);		
		
	}
	
	@Test
	public void testInsertionIndexCalculation() {

		// we'll use the EMPSCHM schema for the rest of the tests 
		Schema schema = TestTools.getEmpschmSchema();
		
		// tests with set EMP-COVERAGE (1 line; no bendpoints)
		MemberRole memberRole = schema.getSet("EMP-COVERAGE").getMembers().get(0);
		assertEquals(1, memberRole.getConnectionParts().size());
		assertEquals(0, memberRole.getConnectionParts().get(0).getBendpointLocations().size());
		CreateConnectorCommand command = new CreateConnectorCommand(memberRole, new Point(0, 0));		
		assertEquals(-1, command.getInsertionIndex());
		
		// tests with set OFFICE-EMPLOYEE (3 lines; 2 bendpoints):
		// 464,72 - 464,190
		// 464,190 - 380,190
		// 380,190 - 380,247
		// there is a -2/+2 margin
		memberRole = schema.getSet("OFFICE-EMPLOYEE").getMembers().get(0);
		assertEquals(1, memberRole.getConnectionParts().size());
		assertEquals(2, memberRole.getConnectionParts().get(0).getBendpointLocations().size());
		// traverse every point of the first (vertical) line fragment, with a horizontal margin of 
		// -2/+2, except for the last 3 points at the bottom (y=188/189/190):
		for (int y = 72; y < 188; y++) {			
			command = new CreateConnectorCommand(memberRole, new Point(462, y));
			assertEquals("x=462, y=" + y, -1, command.getInsertionIndex());
			command = new CreateConnectorCommand(memberRole, new Point(463, y));
			assertEquals("x=463, y=" + y, -1, command.getInsertionIndex());
			command = new CreateConnectorCommand(memberRole, new Point(464, y));
			assertEquals("x=464, y=" + y, -1, command.getInsertionIndex());
			command = new CreateConnectorCommand(memberRole, new Point(465, y));
			assertEquals("x=465, y=" + y, -1, command.getInsertionIndex());
			command = new CreateConnectorCommand(memberRole, new Point(466, y));
			assertEquals("x=466, y=" + y, -1, command.getInsertionIndex());
		}
		// now check the bottom 3 points; y=188 is the top most of them; the outmost on the left and
		// right will be calculated to belong to the second line fragment, whereas all three others 
		// belong to the first
		command = new CreateConnectorCommand(memberRole, new Point(462, 188));
		assertEquals(0, command.getInsertionIndex());
		command = new CreateConnectorCommand(memberRole, new Point(463, 188));
		assertEquals(-1, command.getInsertionIndex());
		command = new CreateConnectorCommand(memberRole, new Point(464, 188));
		assertEquals(-1, command.getInsertionIndex());
		command = new CreateConnectorCommand(memberRole, new Point(465, 188));
		assertEquals(-1, command.getInsertionIndex());
		command = new CreateConnectorCommand(memberRole, new Point(466, 188));
		assertEquals(0, command.getInsertionIndex());
		// continue checking the bottom 3 points; y=189 is the middle of them; only the middle point
		// will be calculated to belong to the first line segment, all others belong to the second
		// line fragment
		command = new CreateConnectorCommand(memberRole, new Point(462, 189));
		assertEquals(0, command.getInsertionIndex());
		command = new CreateConnectorCommand(memberRole, new Point(463, 189));
		assertEquals(0, command.getInsertionIndex());
		command = new CreateConnectorCommand(memberRole, new Point(464, 189));
		assertEquals(-1, command.getInsertionIndex());
		command = new CreateConnectorCommand(memberRole, new Point(465, 189));
		assertEquals(0, command.getInsertionIndex());
		command = new CreateConnectorCommand(memberRole, new Point(466, 189));
		assertEquals(0, command.getInsertionIndex());		
		// finalize checking the bottom 3 points; y=190 is the last of them; only the middle point
		// will be calculated to belong to the first line segment, all others belong to the second
		// line fragment
		command = new CreateConnectorCommand(memberRole, new Point(462, 190));
		assertEquals(0, command.getInsertionIndex());
		command = new CreateConnectorCommand(memberRole, new Point(463, 190));
		assertEquals(0, command.getInsertionIndex());
		command = new CreateConnectorCommand(memberRole, new Point(464, 190));
		assertEquals(-1, command.getInsertionIndex());
		command = new CreateConnectorCommand(memberRole, new Point(465, 190));
		assertEquals(0, command.getInsertionIndex());
		command = new CreateConnectorCommand(memberRole, new Point(466, 190));
		assertEquals(0, command.getInsertionIndex());
		
		// ==> dropping connectors directly on a bendpoint (with a margin of -2/+2) makes 
		//     calculating the connector locations a bit unpredictable
		
		// traverse every point of the second (horizontal) line fragment, with a vertical margin of 
		// -2/+2, except for the first and last 3 of them (we've tested the last 3 of them before, 
		// we will be verifying the first 3 next up)
		for (int x = 383; x < 462; x++) {
			command = new CreateConnectorCommand(memberRole, new Point(x, 188));
			assertEquals("x=" + x + ", y=188", 0, command.getInsertionIndex());
			command = new CreateConnectorCommand(memberRole, new Point(x, 189));
			assertEquals("x=" + x + ", y=189", 0, command.getInsertionIndex());
			command = new CreateConnectorCommand(memberRole, new Point(x, 190));
			assertEquals("x=" + x + ", y=190", 0, command.getInsertionIndex());
			command = new CreateConnectorCommand(memberRole, new Point(x, 191));
			assertEquals("x=" + x + ", y=191", 0, command.getInsertionIndex());
			command = new CreateConnectorCommand(memberRole, new Point(x, 192));
			assertEquals("x=" + x + ", y=192", 0, command.getInsertionIndex());
		}
		// now check the first 3 points; first up is the first (x=380); only the middle (the one 
		// that is right on the line) belongs to the second line fragment, the ones above and below
		// belong to the third line
		command = new CreateConnectorCommand(memberRole, new Point(380, 188));
		assertEquals(1, command.getInsertionIndex());
		command = new CreateConnectorCommand(memberRole, new Point(380, 189));
		assertEquals(1, command.getInsertionIndex());
		command = new CreateConnectorCommand(memberRole, new Point(380, 190));
		assertEquals(0, command.getInsertionIndex());
		command = new CreateConnectorCommand(memberRole, new Point(380, 191));
		assertEquals(1, command.getInsertionIndex());
		command = new CreateConnectorCommand(memberRole, new Point(380, 192));
		assertEquals(1, command.getInsertionIndex());
		// continue checking the first 3 points; next up is the second (x=381); only the middle (the  
		// onethat is right on the line) belongs to the second line fragment, the ones above and 
		// below belong to the third line
		command = new CreateConnectorCommand(memberRole, new Point(381, 188));
		assertEquals(1, command.getInsertionIndex());
		command = new CreateConnectorCommand(memberRole, new Point(381, 189));
		assertEquals(1, command.getInsertionIndex());
		command = new CreateConnectorCommand(memberRole, new Point(381, 190));
		assertEquals(0, command.getInsertionIndex());
		command = new CreateConnectorCommand(memberRole, new Point(381, 191));
		assertEquals(1, command.getInsertionIndex());
		command = new CreateConnectorCommand(memberRole, new Point(381, 192));
		assertEquals(1, command.getInsertionIndex());
		// finalize checking the first 3 points; last up is the third (x=382); the outmost on the 
		// top and bottom will be calculated to belong to the third line fragment, whereas all three  
		// others belong to the second
		command = new CreateConnectorCommand(memberRole, new Point(382, 188));
		assertEquals(1, command.getInsertionIndex());
		command = new CreateConnectorCommand(memberRole, new Point(382, 189));
		assertEquals(0, command.getInsertionIndex());
		command = new CreateConnectorCommand(memberRole, new Point(382, 190));
		assertEquals(0, command.getInsertionIndex());
		command = new CreateConnectorCommand(memberRole, new Point(382, 191));
		assertEquals(0, command.getInsertionIndex());
		command = new CreateConnectorCommand(memberRole, new Point(382, 192));
		assertEquals(1, command.getInsertionIndex());		
		
		
		// traverse every point of the third (vertical) line fragment, with a horizontal margin of 
		// -2/+2, except for the first 2 points at the top (y=190/191/192; we've tested the first 3 
		// of them before)
		for (int y = 193; y <= 247; y++) {
			command = new CreateConnectorCommand(memberRole, new Point(378, y));
			assertEquals("x=378, y=" + y, 1, command.getInsertionIndex());
			command = new CreateConnectorCommand(memberRole, new Point(379, y));
			assertEquals("x=379, y=" + y, 1, command.getInsertionIndex());
			command = new CreateConnectorCommand(memberRole, new Point(380, y));
			assertEquals("x=390, y=" + y, 1, command.getInsertionIndex());
			command = new CreateConnectorCommand(memberRole, new Point(381, y));
			assertEquals("x=381, y=" + y, 1, command.getInsertionIndex());
			command = new CreateConnectorCommand(memberRole, new Point(382, y));
			assertEquals("x=382, y=" + y, 1, command.getInsertionIndex());
		}
		
	}
	
	@Test
	public void testPositionSwitchDecision() {
		
		// a perfectly horizontal line that runs from left to right: no position switch
		Point[] lineFragment = new Point[] {new Point(50, 100), new Point(100, 100)};
		boolean positionSwitch = 
			CreateConnectorCommand.isPositionSwitch(lineFragment,  
													CreateConnectorCommand.ConnectorLayout.HORIZONTAL);
		assertFalse(positionSwitch);
		
		// a perfectly horizontal line that runs from right to left: position switch
		lineFragment = new Point[] {new Point(100, 100), new Point(50, 100)};
		positionSwitch = 
			CreateConnectorCommand.isPositionSwitch(lineFragment,  
													CreateConnectorCommand.ConnectorLayout.HORIZONTAL);
		assertTrue(positionSwitch);
		
		// a perfectly vertical line that runs top down: no position switch
		lineFragment = new Point[] {new Point(100, 100), new Point(100, 200)};
		positionSwitch = 
			CreateConnectorCommand.isPositionSwitch(lineFragment,  
													CreateConnectorCommand.ConnectorLayout.VERTICAL);
		assertFalse(positionSwitch);
		
		// a perfectly vertical line that runs bottom up: position switch
		lineFragment = new Point[] {new Point(100, 200), new Point(100, 100)};
		positionSwitch = 
			CreateConnectorCommand.isPositionSwitch(lineFragment,  
													CreateConnectorCommand.ConnectorLayout.VERTICAL);
		assertTrue(positionSwitch);
		
	}
	
	@Test
	public void testFirstConnectorLocationCalculation_HorizontalLineFragment() {
		
		// in the middle of a left-to-right straight line fragment
		Point[] lineFragment = new Point[] {new Point(100, 50), new Point(200, 50)}; 
		CreateConnectorCommand.ConnectorLayout layout = 
			CreateConnectorCommand.ConnectorLayout.HORIZONTAL;
		Point mouseClickLocation = new Point(150, 50);
		Point firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(130, firstConnectorLocation.x);
		assertEquals(40, firstConnectorLocation.y);
		
		// in the middle of an almost straight left-to-right line fragment (1)
		lineFragment = new Point[] {new Point(100, 49), new Point(200, 50)}; 
		layout = CreateConnectorCommand.ConnectorLayout.HORIZONTAL;
		mouseClickLocation = new Point(150, 50);
		firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(130, firstConnectorLocation.x);
		assertEquals(39, firstConnectorLocation.y);		// within margin so use line fragment begin
		
		// in the middle of an almost straight left-to-right line fragment (2)
		lineFragment = new Point[] {new Point(100, 48), new Point(200, 50)}; 
		layout = CreateConnectorCommand.ConnectorLayout.HORIZONTAL;
		mouseClickLocation = new Point(150, 50);
		firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(130, firstConnectorLocation.x);
		assertEquals(38, firstConnectorLocation.y);		// within margin so use line fragment begin	
		
		// in the middle of an almost straight left-to-right line fragment (3)
		lineFragment = new Point[] {new Point(100, 47), new Point(200, 50)}; 
		layout = CreateConnectorCommand.ConnectorLayout.HORIZONTAL;
		mouseClickLocation = new Point(150, 50);
		firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(130, firstConnectorLocation.x);
		assertEquals(40, firstConnectorLocation.y);  	// not within margin so use mouse click
		
		// in the middle of an almost straight left-to-right line fragment (4)
		lineFragment = new Point[] {new Point(100, 51), new Point(200, 50)}; 
		layout = CreateConnectorCommand.ConnectorLayout.HORIZONTAL;
		mouseClickLocation = new Point(150, 50);
		firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(130, firstConnectorLocation.x);
		assertEquals(41, firstConnectorLocation.y);		// within margin so use line fragment begin
		
		// in the middle of an almost straight left-to-right line fragment (5)
		lineFragment = new Point[] {new Point(100, 52), new Point(200, 50)}; 
		layout = CreateConnectorCommand.ConnectorLayout.HORIZONTAL;
		mouseClickLocation = new Point(150, 50);
		firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(130, firstConnectorLocation.x);
		assertEquals(42, firstConnectorLocation.y);		// within margin so use line fragment begin	
		
		// in the middle of an almost straight left-to-right line fragment (6)
		lineFragment = new Point[] {new Point(100, 53), new Point(200, 50)}; 
		layout = CreateConnectorCommand.ConnectorLayout.HORIZONTAL;
		mouseClickLocation = new Point(150, 50);
		firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(130, firstConnectorLocation.x);
		assertEquals(40, firstConnectorLocation.y);  	// not within margin so use mouse click
		
		
		// in the middle of a straight right-to-left line fragment
		lineFragment = new Point[] {new Point(200, 50), new Point(100, 50)}; 
		layout = CreateConnectorCommand.ConnectorLayout.HORIZONTAL;
		mouseClickLocation = new Point(150, 50);
		firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(130, firstConnectorLocation.x);
		assertEquals(40, firstConnectorLocation.y);
		
		// in the middle of an almost straight right-to-left line fragment (1)
		lineFragment = new Point[] {new Point(200, 49), new Point(100, 50)}; 
		layout = CreateConnectorCommand.ConnectorLayout.HORIZONTAL;
		mouseClickLocation = new Point(150, 50);
		firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(130, firstConnectorLocation.x);
		assertEquals(39, firstConnectorLocation.y);		// within margin so use line fragment begin
		
		// in the middle of an almost straight right-to-left line fragment (2)
		lineFragment = new Point[] {new Point(200, 48), new Point(100, 50)}; 
		layout = CreateConnectorCommand.ConnectorLayout.HORIZONTAL;
		mouseClickLocation = new Point(150, 50);
		firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(130, firstConnectorLocation.x);
		assertEquals(38, firstConnectorLocation.y);		// within margin so use line fragment begin	
		
		// in the middle of an almost straight right-to-left line fragment (3)
		lineFragment = new Point[] {new Point(200, 47), new Point(100, 50)}; 
		layout = CreateConnectorCommand.ConnectorLayout.HORIZONTAL;
		mouseClickLocation = new Point(150, 50);
		firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(130, firstConnectorLocation.x);
		assertEquals(40, firstConnectorLocation.y);  	// not within margin so use mouse click
		
		// in the middle of an almost straight right-to-left line fragment (4)
		lineFragment = new Point[] {new Point(200, 51), new Point(100, 50)}; 
		layout = CreateConnectorCommand.ConnectorLayout.HORIZONTAL;
		mouseClickLocation = new Point(150, 50);
		firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(130, firstConnectorLocation.x);
		assertEquals(41, firstConnectorLocation.y);		// within margin so use line fragment begin
		
		// in the middle of an almost straight right-to-left line fragment (5)
		lineFragment = new Point[] {new Point(200, 52), new Point(100, 50)}; 
		layout = CreateConnectorCommand.ConnectorLayout.HORIZONTAL;
		mouseClickLocation = new Point(150, 50);
		firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(130, firstConnectorLocation.x);
		assertEquals(42, firstConnectorLocation.y);		// within margin so use line fragment begin	
		
		// in the middle of an almost straight right-to-left line fragment (6)
		lineFragment = new Point[] {new Point(200, 53), new Point(100, 50)}; 
		layout = CreateConnectorCommand.ConnectorLayout.HORIZONTAL;
		mouseClickLocation = new Point(150, 50);
		firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(130, firstConnectorLocation.x);
		assertEquals(40, firstConnectorLocation.y);  	// not within margin so use mouse click				
		
	}
	
	@Test
	public void testFirstConnectorLocationCalculation_VerticalLineFragment() {
		
		// in the middle of a straight top-down line fragment
		Point[] lineFragment = new Point[] {new Point(100, 50), new Point(100, 150)}; 
		CreateConnectorCommand.ConnectorLayout layout = 
			CreateConnectorCommand.ConnectorLayout.VERTICAL;
		Point mouseClickLocation = new Point(100, 100);
		Point firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(90, firstConnectorLocation.x);
		assertEquals(80, firstConnectorLocation.y);
		
		// in the middle of an almost straight top-down line fragment (1)
		lineFragment = new Point[] {new Point(99, 50), new Point(100, 150)}; 
		layout = CreateConnectorCommand.ConnectorLayout.VERTICAL;
		mouseClickLocation = new Point(100, 100);
		firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(89, firstConnectorLocation.x);		// within margin so use line fragment begin
		assertEquals(80, firstConnectorLocation.y);		
		
		// in the middle of an almost straight top-down top-down line fragment (2)
		lineFragment = new Point[] {new Point(98, 50), new Point(100, 150)}; 
		layout = CreateConnectorCommand.ConnectorLayout.VERTICAL;
		mouseClickLocation = new Point(100, 100);
		firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(88, firstConnectorLocation.x);		// within margin so use line fragment begin
		assertEquals(80, firstConnectorLocation.y);			
		
		// in the middle of an almost straight top-down line fragment (3)
		lineFragment = new Point[] {new Point(97, 50), new Point(100, 150)}; 
		layout = CreateConnectorCommand.ConnectorLayout.VERTICAL;
		mouseClickLocation = new Point(100, 100);
		firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(90, firstConnectorLocation.x);		// not within margin so use mouse click
		assertEquals(80, firstConnectorLocation.y);  	
		
		// in the middle of an almost straight top-down line fragment (4)
		lineFragment = new Point[] {new Point(101, 50), new Point(100, 150)}; 
		layout = CreateConnectorCommand.ConnectorLayout.VERTICAL;
		mouseClickLocation = new Point(100, 100);
		firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(91, firstConnectorLocation.x);		// within margin so use line fragment begin
		assertEquals(80, firstConnectorLocation.y);		
		
		// in the middle of an almost straight top-down line fragment (5)
		lineFragment = new Point[] {new Point(102, 50), new Point(100, 150)}; 
		layout = CreateConnectorCommand.ConnectorLayout.VERTICAL;
		mouseClickLocation = new Point(100, 100);
		firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(92, firstConnectorLocation.x);		// within margin so use line fragment begin
		assertEquals(80, firstConnectorLocation.y);			
		
		// in the middle of an almost straight top-down line fragment (6)
		lineFragment = new Point[] {new Point(103, 50), new Point(100, 150)}; 
		layout = CreateConnectorCommand.ConnectorLayout.VERTICAL;
		mouseClickLocation = new Point(100, 100);
		firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(90, firstConnectorLocation.x);		// not within margin so use mouse click
		assertEquals(80, firstConnectorLocation.y);  	
		
		
		// in the middle of a straight bottom-up line fragment
		lineFragment = new Point[] {new Point(100, 150), new Point(100, 50)}; 
		layout = CreateConnectorCommand.ConnectorLayout.VERTICAL;
		mouseClickLocation = new Point(100, 100);
		firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(90, firstConnectorLocation.x);
		assertEquals(80, firstConnectorLocation.y);
		
		// in the middle of an almost straight bottom-up line fragment (1)
		lineFragment = new Point[] {new Point(99, 150), new Point(100, 50)}; 
		layout = CreateConnectorCommand.ConnectorLayout.VERTICAL;
		mouseClickLocation = new Point(100, 100);
		firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(89, firstConnectorLocation.x);		// within margin so use line fragment begin
		assertEquals(80, firstConnectorLocation.y);		
		
		// in the middle of an almost straight bottom-up line fragment (2)
		lineFragment = new Point[] {new Point(98, 150), new Point(100, 50)}; 
		layout = CreateConnectorCommand.ConnectorLayout.VERTICAL;
		mouseClickLocation = new Point(100, 100);
		firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(88, firstConnectorLocation.x);		// within margin so use line fragment begin
		assertEquals(80, firstConnectorLocation.y);			
		
		// in the middle of an almost straight bottom-up line fragment (3)
		lineFragment = new Point[] {new Point(97, 150), new Point(100, 50)}; 
		layout = CreateConnectorCommand.ConnectorLayout.VERTICAL;
		mouseClickLocation = new Point(100, 100);
		firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(90, firstConnectorLocation.x);		// not within margin so use mouse click
		assertEquals(80, firstConnectorLocation.y);  	
		
		// in the middle of an almost straight bottom-up line fragment (4)
		lineFragment = new Point[] {new Point(101, 150), new Point(100, 50)}; 
		layout = CreateConnectorCommand.ConnectorLayout.VERTICAL;
		mouseClickLocation = new Point(100, 100);
		firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(91, firstConnectorLocation.x);		// within margin so use line fragment begin
		assertEquals(80, firstConnectorLocation.y);		
		
		// in the middle of an almost straight bottom-up line fragment (5)
		lineFragment = new Point[] {new Point(102, 150), new Point(100, 50)}; 
		layout = CreateConnectorCommand.ConnectorLayout.VERTICAL;
		mouseClickLocation = new Point(100, 100);
		firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(92, firstConnectorLocation.x);		// within margin so use line fragment begin	
		assertEquals(80, firstConnectorLocation.y);		
		
		// in the middle of an almost straight bottom-up line fragment (6)
		lineFragment = new Point[] {new Point(103, 150), new Point(100, 50)}; 
		layout = CreateConnectorCommand.ConnectorLayout.VERTICAL;
		mouseClickLocation = new Point(100, 100);
		firstConnectorLocation = 
			CreateConnectorCommand.getFirstConnectorLocation(lineFragment, layout, mouseClickLocation);
		assertEquals(90, firstConnectorLocation.x);		// not within margin so use mouse click
		assertEquals(80, firstConnectorLocation.y);  					
		
	}	
	
	@Test
	public void testSecondConnectorLocationCalculation() {
		
		Point firstConnectorLocation = new Point(100, 50);
		
		Point secondConnectorLocation = 
			CreateConnectorCommand.getSecondConnectorLocation(firstConnectorLocation, 
													  		  CreateConnectorCommand.ConnectorLayout
													  								.HORIZONTAL);
		assertEquals(120, secondConnectorLocation.x);
		assertEquals(50, secondConnectorLocation.y);
		
		secondConnectorLocation = 
			CreateConnectorCommand.getSecondConnectorLocation(firstConnectorLocation, 
													  		  CreateConnectorCommand.ConnectorLayout
													  								.VERTICAL);
		assertEquals(100, secondConnectorLocation.x);
		assertEquals(70, secondConnectorLocation.y);		
		
	}	
	
	@Test
	public void testCreateConnectorsOnFirstOfThreeLineFragments() {
		
		// get the EMPSCHM schema and locate set OFFICE-EMPLOYEE; we will be adding connectors for 
		// this set: it contains 2 bendpoints and we'll add the connectors in the middle of the 
		// first line fragment, i.e. we'll simulate a mouse click in the middle of the vertical line
		// between the owner record and the first bendpoint; after the connectors are added, the  
		// newly created (second) connection part will contain both bendpoints - the connectors will 
		// be located one on top of the other because the line fragment is vertical
		Schema schema = TestTools.getEmpschmSchema();
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);		
		Set set = schema.getSet("OFFICE-EMPLOYEE");
		SchemaRecord office = set.getOwner().getRecord();
		assertEquals(399, office.getDiagramLocation().getX());
		assertEquals(19, office.getDiagramLocation().getY());
		MemberRole memberRole = set.getMembers().get(0);
		SchemaRecord employee = memberRole.getRecord(); 
		assertEquals(285, employee.getDiagramLocation().getX());
		assertEquals(247, employee.getDiagramLocation().getY());
		assertEquals(1, memberRole.getConnectionParts().size());		
		ConnectionPart originalConnectionPart =	memberRole.getConnectionParts().get(0);		
		assertEquals(2, originalConnectionPart.getBendpointLocations().size());
		DiagramLocation originalBendpoint1 = originalConnectionPart.getBendpointLocations().get(0);
		assertEquals(65, originalBendpoint1.getX());		// relative to owner record
		assertEquals(171, originalBendpoint1.getY());		// relative to owner record
		DiagramLocation originalBendpoint2 = originalConnectionPart.getBendpointLocations().get(1);
		assertEquals(-19, originalBendpoint2.getX());		// relative to owner record
		assertEquals(171, originalBendpoint2.getY());		// relative to owner record
		DiagramLocation originalSourceEndpoint = originalConnectionPart.getSourceEndpointLocation();
		assertNotNull(originalSourceEndpoint);
		assertEquals(65, originalSourceEndpoint.getX());	// relative to owner record
		assertEquals(53, originalSourceEndpoint.getY());	// relative to owner record
		DiagramLocation originalTargetEndpoint = originalConnectionPart.getTargetEndpointLocation();
		assertNotNull(originalTargetEndpoint);
		assertEquals(95, originalTargetEndpoint.getX());	// relative to member record
		assertEquals(0, originalTargetEndpoint.getY());		// relative to owner record
		
		
		// create the command; the coordinates of the first line fragment are 464,72 - 464,190
		Point location = new Point(464, 146);
		Command command = new CreateConnectorCommand(memberRole, location);
		
		
		// execute the command and verify
		command.execute();
		ObjectGraph objectGraph2 = TestTools.asObjectGraph(schema);
		assertEquals(2, memberRole.getConnectionParts().size());
		// check the first connection part (except for its connector):
		assertSame(originalConnectionPart, memberRole.getConnectionParts().get(0));
		assertSame(originalSourceEndpoint, originalConnectionPart.getSourceEndpointLocation());
		assertNull(originalConnectionPart.getTargetEndpointLocation());
		assertEquals(0, originalConnectionPart.getBendpointLocations().size());
		// check the second connection part (except for its connector):
		ConnectionPart newConnectionPart = memberRole.getConnectionParts().get(1);
		assertNotNull(newConnectionPart);
		assertNull(newConnectionPart.getSourceEndpointLocation());
		assertSame(originalTargetEndpoint, newConnectionPart.getTargetEndpointLocation());
		assertEquals(2, newConnectionPart.getBendpointLocations().size());
		assertSame(originalBendpoint1, newConnectionPart.getBendpointLocations().get(0));
		assertSame(originalBendpoint2, newConnectionPart.getBendpointLocations().get(1));
		// check the first connector:
		Connector connector1 = originalConnectionPart.getConnector();
		assertNotNull(connector1);
		assertNotNull(connector1.getDiagramLocation());
		assertEquals(454, connector1.getDiagramLocation().getX());
		assertEquals(126, connector1.getDiagramLocation().getY());
		assertEquals("set connector[0] OFFICE-EMPLOYEE (EMPLOYEE)", 
					 connector1.getDiagramLocation().getEyecatcher());
		assertNull(connector1.getLabel());
		// check the second connector:
		Connector connector2 = newConnectionPart.getConnector();
		assertNotNull(connector2);
		assertNotNull(connector2.getDiagramLocation());
		assertEquals(454, connector2.getDiagramLocation().getX());
		assertEquals(146, connector2.getDiagramLocation().getY());
		assertEquals("set connector[1] OFFICE-EMPLOYEE (EMPLOYEE)", 
					 connector2.getDiagramLocation().getEyecatcher());
		assertNull(connector2.getLabel());
		
		
		// once execute() has been called, all annotated field values should be in place; make sure
		// the command class itself is annotated with @ModelChange with its type set to 
		// ModelChangeCategory.ADD_ITEM
		ModelChange modelChangeAnnotation = command.getClass().getAnnotation(ModelChange.class);	
		assertNotNull(modelChangeAnnotation);
		assertEquals(ModelChangeCategory.ADD_ITEM, modelChangeAnnotation.category());		
		
		// make sure the owner is set
		MemberRole owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(memberRole, owner);
		
		// make sure the reference is set
		EReference reference = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Reference.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(reference == SchemaPackage.eINSTANCE.getMemberRole_ConnectionParts());			
		
		// make sure the item is set
		ConnectionPart item = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Item.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(newConnectionPart, item);
		
		
		// undo the command and verify
		command.undo();
		ObjectGraph objectGraph3 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph, objectGraph3);
		assertEquals(1, memberRole.getConnectionParts().size());
		// check the connection part:
		assertSame(originalConnectionPart, memberRole.getConnectionParts().get(0));
		assertSame(originalSourceEndpoint, originalConnectionPart.getSourceEndpointLocation());
		assertSame(originalTargetEndpoint, originalConnectionPart.getTargetEndpointLocation());
		assertEquals(2, originalConnectionPart.getBendpointLocations().size());
		assertSame(originalBendpoint1, originalConnectionPart.getBendpointLocations().get(0));
		assertSame(originalBendpoint2, originalConnectionPart.getBendpointLocations().get(1));
		
		
		// make sure the owner is still set
		owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(memberRole, owner);
		
		// make sure the reference is still set
		reference = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Reference.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(reference == SchemaPackage.eINSTANCE.getMemberRole_ConnectionParts());			
		
		// make sure the item is still set
		item = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Item.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(newConnectionPart, item);		
		
		
		// redo the command and verify
		command.redo();
		ObjectGraph objectGraph4 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph2, objectGraph4);
		assertEquals(2, memberRole.getConnectionParts().size());
		// check the first connection part (except for its connector):
		assertSame(originalConnectionPart, memberRole.getConnectionParts().get(0));
		assertSame(originalSourceEndpoint, originalConnectionPart.getSourceEndpointLocation());
		assertNull(originalConnectionPart.getTargetEndpointLocation());
		assertEquals(0, originalConnectionPart.getBendpointLocations().size());
		// check the second connection part (except for its connector):		
		assertSame(newConnectionPart, memberRole.getConnectionParts().get(1));
		assertNull(newConnectionPart.getSourceEndpointLocation());
		assertSame(originalTargetEndpoint, newConnectionPart.getTargetEndpointLocation());
		assertEquals(2, newConnectionPart.getBendpointLocations().size());
		assertSame(originalBendpoint1, newConnectionPart.getBendpointLocations().get(0));
		assertSame(originalBendpoint2, newConnectionPart.getBendpointLocations().get(1));
		// check the first connector:		
		assertSame(connector1, originalConnectionPart.getConnector());
		assertNotNull(connector1.getDiagramLocation());
		assertEquals(454, connector1.getDiagramLocation().getX());
		assertEquals(126, connector1.getDiagramLocation().getY());
		assertEquals("set connector[0] OFFICE-EMPLOYEE (EMPLOYEE)", 
					 connector1.getDiagramLocation().getEyecatcher());
		assertNull(connector1.getLabel());
		// check the second connector:
		assertSame(connector2, newConnectionPart.getConnector());
		assertNotNull(connector2.getDiagramLocation());
		assertEquals(454, connector2.getDiagramLocation().getX());
		assertEquals(146, connector2.getDiagramLocation().getY());
		assertEquals("set connector[1] OFFICE-EMPLOYEE (EMPLOYEE)", 
					 connector2.getDiagramLocation().getEyecatcher());
		assertNull(connector2.getLabel());
		
		
		// make sure the owner is still set
		owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(memberRole, owner);
		
		// make sure the reference is still set
		reference = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Reference.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(reference == SchemaPackage.eINSTANCE.getMemberRole_ConnectionParts());			
		
		// make sure the item is still set
		item = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Item.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(newConnectionPart, item);		
				
	}

	@Test
	public void testCreateConnectorsOnSecondOfThreeLineFragments() {
		
		// get the EMPSCHM schema and locate set OFFICE-EMPLOYEE; we will be adding connectors for 
		// this set: it contains 2 bendpoints and we'll add the connectors in the middle of the 
		// second line fragment, i.e. we'll simulate a mouse click in the middle of the horizontal 
		// line between both bendpoints; after the connectors are added, each connection part will 
		// will contain 1 bendpoint - the connectors will be located next to each other because the 
		// line fragment is horizontal
		Schema schema = TestTools.getEmpschmSchema();
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);		
		Set set = schema.getSet("OFFICE-EMPLOYEE");
		MemberRole memberRole = set.getMembers().get(0);
		ConnectionPart originalConnectionPart =	memberRole.getConnectionParts().get(0);		
		DiagramLocation originalBendpoint1 = originalConnectionPart.getBendpointLocations().get(0);
		DiagramLocation originalBendpoint2 = originalConnectionPart.getBendpointLocations().get(1);
		DiagramLocation originalSourceEndpoint = originalConnectionPart.getSourceEndpointLocation();
		DiagramLocation originalTargetEndpoint = originalConnectionPart.getTargetEndpointLocation();
		
		
		// create the command; the coordinates of the second line fragment are 464,190 - 380,190
		// (the line goes from the right to the left, so the connector locations will be switched)
		Point location = new Point(422, 190);
		Command command = new CreateConnectorCommand(memberRole, location);
		
		
		// execute the command and verify
		command.execute();
		ObjectGraph objectGraph2 = TestTools.asObjectGraph(schema);
		assertEquals(2, memberRole.getConnectionParts().size());
		// check the first connection part (except for its connector):
		assertSame(originalConnectionPart, memberRole.getConnectionParts().get(0));
		assertSame(originalSourceEndpoint, originalConnectionPart.getSourceEndpointLocation());
		assertNull(originalConnectionPart.getTargetEndpointLocation());
		assertEquals(1, originalConnectionPart.getBendpointLocations().size());
		assertSame(originalBendpoint1, originalConnectionPart.getBendpointLocations().get(0));
		// check the second connection part (except for its connector):
		ConnectionPart newConnectionPart = memberRole.getConnectionParts().get(1);
		assertNotNull(newConnectionPart);
		assertNull(newConnectionPart.getSourceEndpointLocation());
		assertSame(originalTargetEndpoint, newConnectionPart.getTargetEndpointLocation());		
		assertEquals(1, newConnectionPart.getBendpointLocations().size());		
		assertSame(originalBendpoint2, newConnectionPart.getBendpointLocations().get(0));
		// check the first connector:
		Connector connector1 = originalConnectionPart.getConnector();
		assertNotNull(connector1);
		assertNotNull(connector1.getDiagramLocation());
		assertEquals(422, connector1.getDiagramLocation().getX());
		assertEquals(180, connector1.getDiagramLocation().getY());
		assertEquals("set connector[0] OFFICE-EMPLOYEE (EMPLOYEE)", 
					 connector1.getDiagramLocation().getEyecatcher());
		assertNull(connector1.getLabel());
		// check the second connector:
		Connector connector2 = newConnectionPart.getConnector();
		assertNotNull(connector2);
		assertNotNull(connector2.getDiagramLocation());
		assertEquals(402, connector2.getDiagramLocation().getX());
		assertEquals(180, connector2.getDiagramLocation().getY());
		assertEquals("set connector[1] OFFICE-EMPLOYEE (EMPLOYEE)", 
					 connector2.getDiagramLocation().getEyecatcher());
		assertNull(connector2.getLabel());
		
		
		// undo the command and verify
		command.undo();
		ObjectGraph objectGraph3 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph, objectGraph3);
		assertEquals(1, memberRole.getConnectionParts().size());
		// check the connection part:
		assertSame(originalConnectionPart, memberRole.getConnectionParts().get(0));
		assertSame(originalSourceEndpoint, originalConnectionPart.getSourceEndpointLocation());
		assertSame(originalTargetEndpoint, originalConnectionPart.getTargetEndpointLocation());
		assertEquals(2, originalConnectionPart.getBendpointLocations().size());
		assertSame(originalBendpoint1, originalConnectionPart.getBendpointLocations().get(0));
		assertSame(originalBendpoint2, originalConnectionPart.getBendpointLocations().get(1));
		
		
		// redo the command and verify
		command.redo();
		ObjectGraph objectGraph4 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph2, objectGraph4);
		assertEquals(2, memberRole.getConnectionParts().size());
		// check the first connection part (except for its connector):
		assertSame(originalConnectionPart, memberRole.getConnectionParts().get(0));
		assertSame(originalSourceEndpoint, originalConnectionPart.getSourceEndpointLocation());
		assertNull(originalConnectionPart.getTargetEndpointLocation());
		assertSame(originalBendpoint1, originalConnectionPart.getBendpointLocations().get(0));
		assertEquals(1, originalConnectionPart.getBendpointLocations().size());
		// check the second connection part (except for its connector):		
		assertSame(newConnectionPart, memberRole.getConnectionParts().get(1));
		assertNull(newConnectionPart.getSourceEndpointLocation());
		assertSame(originalTargetEndpoint, newConnectionPart.getTargetEndpointLocation());		
		assertSame(originalTargetEndpoint, newConnectionPart.getTargetEndpointLocation());
		assertEquals(1, newConnectionPart.getBendpointLocations().size());
		assertSame(originalBendpoint2, newConnectionPart.getBendpointLocations().get(0));
		// check the first connector:		
		assertSame(connector1, originalConnectionPart.getConnector());
		assertNotNull(connector1.getDiagramLocation());
		assertEquals(422, connector1.getDiagramLocation().getX());
		assertEquals(180, connector1.getDiagramLocation().getY());
		assertEquals("set connector[0] OFFICE-EMPLOYEE (EMPLOYEE)", 
					 connector1.getDiagramLocation().getEyecatcher());
		assertNull(connector1.getLabel());
		// check the second connector:
		assertSame(connector2, newConnectionPart.getConnector());
		assertNotNull(connector2.getDiagramLocation());
		assertEquals(402, connector2.getDiagramLocation().getX());
		assertEquals(180, connector2.getDiagramLocation().getY());
		assertEquals("set connector[1] OFFICE-EMPLOYEE (EMPLOYEE)", 
					 connector2.getDiagramLocation().getEyecatcher());
		assertNull(connector2.getLabel());
				
	}

	@Test
	public void testCreateConnectorsOnLastOfThreeLineFragments() {
		
		// get the EMPSCHM schema and locate set OFFICE-EMPLOYEE; we will be adding connectors for 
		// this set: it contains 2 bendpoints and we'll add the connectors in the middle of the 
		// last line fragment, i.e. we'll simulate a mouse click in the middle of the vertical line
		// between the last bendpoint and the member record; after the connectors are added, the  
		// newly created (second) connection part will contain NO bendpoints - the connectors will 
		// be located one on top of the other because the line fragment is vertical
		Schema schema = TestTools.getEmpschmSchema();
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);		
		Set set = schema.getSet("OFFICE-EMPLOYEE");
		MemberRole memberRole = set.getMembers().get(0);
		ConnectionPart originalConnectionPart =	memberRole.getConnectionParts().get(0);		
		DiagramLocation originalBendpoint1 = originalConnectionPart.getBendpointLocations().get(0);
		DiagramLocation originalBendpoint2 = originalConnectionPart.getBendpointLocations().get(1);
		assertEquals(-19, originalBendpoint2.getX());		// relative to owner record
		assertEquals(171, originalBendpoint2.getY());		// relative to owner record
		DiagramLocation originalSourceEndpoint = originalConnectionPart.getSourceEndpointLocation();
		DiagramLocation originalTargetEndpoint = originalConnectionPart.getTargetEndpointLocation();
		
		
		// create the command; the coordinates of the last line fragment are 380,190 - 380,247
		Point location = new Point(380, 218);
		Command command = new CreateConnectorCommand(memberRole, location);
		
		
		// execute the command and verify
		command.execute();
		ObjectGraph objectGraph2 = TestTools.asObjectGraph(schema);
		assertEquals(2, memberRole.getConnectionParts().size());
		// check the first connection part (except for its connector):
		assertSame(originalConnectionPart, memberRole.getConnectionParts().get(0));
		assertSame(originalSourceEndpoint, originalConnectionPart.getSourceEndpointLocation());
		assertNull(originalConnectionPart.getTargetEndpointLocation());
		assertEquals(2, originalConnectionPart.getBendpointLocations().size());
		assertSame(originalBendpoint1, originalConnectionPart.getBendpointLocations().get(0));
		assertSame(originalBendpoint2, originalConnectionPart.getBendpointLocations().get(1));
		// check the second connection part (except for its connector):
		ConnectionPart newConnectionPart = memberRole.getConnectionParts().get(1);
		assertNotNull(newConnectionPart);
		assertNull(newConnectionPart.getSourceEndpointLocation());
		assertSame(originalTargetEndpoint, newConnectionPart.getTargetEndpointLocation());
		assertEquals(0, newConnectionPart.getBendpointLocations().size());
		// check the first connector:
		Connector connector1 = originalConnectionPart.getConnector();
		assertNotNull(connector1);
		assertNotNull(connector1.getDiagramLocation());
		assertEquals(370, connector1.getDiagramLocation().getX());
		assertEquals(198, connector1.getDiagramLocation().getY());
		assertEquals("set connector[0] OFFICE-EMPLOYEE (EMPLOYEE)", 
					 connector1.getDiagramLocation().getEyecatcher());
		assertNull(connector1.getLabel());
		// check the second connector:
		Connector connector2 = newConnectionPart.getConnector();
		assertNotNull(connector2);
		assertNotNull(connector2.getDiagramLocation());
		assertEquals(370, connector2.getDiagramLocation().getX());
		assertEquals(218, connector2.getDiagramLocation().getY());
		assertEquals("set connector[1] OFFICE-EMPLOYEE (EMPLOYEE)", 
					 connector2.getDiagramLocation().getEyecatcher());
		assertNull(connector2.getLabel());		
		
		
		// undo the command and verify
		command.undo();
		ObjectGraph objectGraph3 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph, objectGraph3);
		assertEquals(1, memberRole.getConnectionParts().size());
		// check the connection part:
		assertSame(originalConnectionPart, memberRole.getConnectionParts().get(0));
		assertSame(originalSourceEndpoint, originalConnectionPart.getSourceEndpointLocation());
		assertSame(originalTargetEndpoint, originalConnectionPart.getTargetEndpointLocation());
		assertEquals(2, originalConnectionPart.getBendpointLocations().size());
		assertSame(originalBendpoint1, originalConnectionPart.getBendpointLocations().get(0));
		assertSame(originalBendpoint2, originalConnectionPart.getBendpointLocations().get(1));		
		
		
		// redo the command and verify
		command.redo();
		ObjectGraph objectGraph4 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph2, objectGraph4);
		assertEquals(2, memberRole.getConnectionParts().size());
		// check the first connection part (except for its connector):
		assertSame(originalConnectionPart, memberRole.getConnectionParts().get(0));
		assertSame(originalSourceEndpoint, originalConnectionPart.getSourceEndpointLocation());
		assertNull(originalConnectionPart.getTargetEndpointLocation());
		assertEquals(2, originalConnectionPart.getBendpointLocations().size());
		assertSame(originalBendpoint1, originalConnectionPart.getBendpointLocations().get(0));
		assertSame(originalBendpoint2, originalConnectionPart.getBendpointLocations().get(1));		
		// check the second connection part (except for its connector):		
		assertSame(newConnectionPart, memberRole.getConnectionParts().get(1));
		assertNull(newConnectionPart.getSourceEndpointLocation());
		assertSame(originalTargetEndpoint, newConnectionPart.getTargetEndpointLocation());
		assertEquals(0, newConnectionPart.getBendpointLocations().size());
		// check the first connector:		
		assertSame(connector1, originalConnectionPart.getConnector());
		assertNotNull(connector1.getDiagramLocation());
		assertEquals(370, connector1.getDiagramLocation().getX());
		assertEquals(198, connector1.getDiagramLocation().getY());
		assertEquals("set connector[0] OFFICE-EMPLOYEE (EMPLOYEE)", 
					 connector1.getDiagramLocation().getEyecatcher());
		assertNull(connector1.getLabel());
		// check the second connector:
		assertSame(connector2, newConnectionPart.getConnector());
		assertNotNull(connector2.getDiagramLocation());
		assertEquals(370, connector2.getDiagramLocation().getX());
		assertEquals(218, connector2.getDiagramLocation().getY());
		assertEquals("set connector[1] OFFICE-EMPLOYEE (EMPLOYEE)", 
					 connector2.getDiagramLocation().getEyecatcher());
		assertNull(connector2.getLabel());
				
	}	

}
