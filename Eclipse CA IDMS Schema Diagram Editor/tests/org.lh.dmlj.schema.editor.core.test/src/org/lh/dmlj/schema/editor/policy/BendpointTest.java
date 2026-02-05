/**
 * Copyright (C) 2026  Luc Hermans
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
package org.lh.dmlj.schema.editor.policy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.editor.testtool.TestTools;

public class BendpointTest {
	private static Schema schema;
	
	private ConnectionPart inboundLineToConnectorA1;
	private ConnectionPart outboundLineFromConnectorA2;
	private Bendpoint bendpointForConnectorA1;
	private Bendpoint bendpointForConnectorA2;
	
	private ConnectionPart inboundLineToConnectorB1;
	private ConnectionPart outboundLineFromConnectorB2;
	private Bendpoint bendpointForConnectorB1;
	private Bendpoint bendpointForConnectorB2;
	
	private ConnectionPart newIndex1ConnectionPart;
	
	@BeforeClass
	public static void loadSchema() {
		schema = TestTools.getSchema("testdata/MoveRecords.schemadsl");
	}
	
	private static DiagramLocation getLastBendpointLocation(ConnectionPart connectionPart) {
		if (!connectionPart.getBendpointLocations().isEmpty()) {
			return connectionPart.getBendpointLocations().get(connectionPart.getBendpointLocations().size() - 1);
		} else {
			return null;
		}
	}
	
	@Before
	public void setup() {
		var newSet23 = schema.getSet("NEW-SET-23");
		inboundLineToConnectorA1 = newSet23.getMembers().get(0).getConnectionParts().get(0);
		bendpointForConnectorA1 = Bendpoint.lastOf(inboundLineToConnectorA1);
		outboundLineFromConnectorA2 = newSet23.getMembers().get(0).getConnectionParts().get(1);
		bendpointForConnectorA2 = Bendpoint.lastOf(outboundLineFromConnectorA2);
		
		var newSet24 = schema.getSet("NEW-SET-24");
		inboundLineToConnectorB1 = newSet24.getMembers().get(0).getConnectionParts().get(0);
		bendpointForConnectorB1 = Bendpoint.lastOf(inboundLineToConnectorB1);
		outboundLineFromConnectorB2 = newSet24.getMembers().get(0).getConnectionParts().get(1);
		bendpointForConnectorB2 = Bendpoint.lastOf(outboundLineFromConnectorB2);
		
		var newIndex1 = schema.getSet("NEW-INDEX-1");
		newIndex1ConnectionPart = newIndex1.getMembers().get(0).getConnectionParts().get(0);
	}
	
	@Test
	public void bendpointForConnectorA1sCorrectlyConstructed() {
		assertSame(inboundLineToConnectorA1, bendpointForConnectorA1.connectionPart());
		assertSame(getLastBendpointLocation(inboundLineToConnectorA1), bendpointForConnectorA1.diagramLocation());
	}
	
	@Test
	public void bendpointForConnectorA2IsCorrectlyConstructed() {
		assertSame(outboundLineFromConnectorA2, bendpointForConnectorA2.connectionPart());
		assertSame(getLastBendpointLocation(outboundLineFromConnectorA2), bendpointForConnectorA2.diagramLocation());
	}
	
	@Test
	public void bendpointForConnectorB1sCorrectlyConstructed() {
		assertSame(inboundLineToConnectorB1, bendpointForConnectorB1.connectionPart());
		assertSame(getLastBendpointLocation(inboundLineToConnectorB1), bendpointForConnectorB1.diagramLocation());
	}
	
	@Test
	public void bendpointForConnectorB2IsCorrectlyConstructed() {
		assertSame(outboundLineFromConnectorB2, bendpointForConnectorB2.connectionPart());
		assertSame(getLastBendpointLocation(outboundLineFromConnectorB2), bendpointForConnectorB2.diagramLocation());
	}
	
	@Test
	public void lastSegmentOfInboundLineToConnectorA1IsHorizontal() {
		assertTrue(bendpointForConnectorA1.isHorizontalLineToTargetEndpointLocation());
	}
	
	@Test
	public void lastSegmentOfInboundLineToConnectorA1IsNotVertical() {
		assertFalse(bendpointForConnectorA1.isVerticalLineToTargetEndpointLocation());
	}
	
	@Test
	public void lastSegmentOfoutboundLineFromConnectorA2IsHorizontal() {
		assertTrue(bendpointForConnectorA2.isHorizontalLineToTargetEndpointLocation());
	}
	
	@Test
	public void lastSegmentOfoutboundLineFromConnectorA2IsNotVertical() {
		assertFalse(bendpointForConnectorA2.isVerticalLineToTargetEndpointLocation());
	}
	
	@Test
	public void lastSegmentOfInboundLineToConnectorB1IsVertical() {
		assertTrue(bendpointForConnectorB1.isVerticalLineToTargetEndpointLocation());
	}
	
	@Test
	public void lastSegmentOfInboundLineToConnectorB1IsNotHorizontal() {
		assertFalse(bendpointForConnectorB1.isHorizontalLineToTargetEndpointLocation());
	}
	
	@Test
	public void lastSegmentOfInboundLineToConnectorB2IsVertical() {
		assertTrue(bendpointForConnectorB2.isVerticalLineToTargetEndpointLocation());
	}
	
	@Test
	public void lastSegmentOfInboundLineToConnectorB2IsNotHorizontal() {
		assertFalse(bendpointForConnectorB2.isHorizontalLineToTargetEndpointLocation());
	}
	
	@Test
	public void anIllegalArgumentExceptionIsThrownWhenNotUserOwnedSet() {
		var exception = assertThrows(IllegalArgumentException.class, () -> Bendpoint.lastOf(newIndex1ConnectionPart) );
		assertEquals("not a connectionPart for a user owned set: NEW-INDEX-1", exception.getMessage());
	}
	
	@Test
	public void theSetNameCanBeObtained() {
		assertEquals("NEW-SET-24", bendpointForConnectorB1.getSetName());
	}
	
	@Test
	public void theMemberRecordNameCanBeObtained() {
		assertEquals("NEW-RECORD-11", bendpointForConnectorB1.getMemberRecordName());
	}
	
	@Test
	public void theBendpointIndexCanBeObtained() {
		assertEquals(0, bendpointForConnectorB1.getIndex());
	}

}
