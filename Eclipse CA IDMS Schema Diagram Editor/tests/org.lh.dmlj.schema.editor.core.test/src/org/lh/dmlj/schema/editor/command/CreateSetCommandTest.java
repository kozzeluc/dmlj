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

import org.junit.Before;
import org.junit.Test;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.IndexedSetModeSpecification;
import org.lh.dmlj.schema.LabelAlignment;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMembershipOption;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.editor.figure.RecordFigure;
import org.lh.dmlj.schema.editor.testtool.ObjectGraph;
import org.lh.dmlj.schema.editor.testtool.TestTools;
import org.lh.dmlj.schema.editor.testtool.Xmi;

public class CreateSetCommandTest {

	private ObjectGraph objectGraph;
	private Schema 		schema;
	private Xmi 		xmi;

	private void checkObjectGraph(ObjectGraph expected) {
		ObjectGraph actual = TestTools.asObjectGraph(schema);
		assertEquals(expected, actual);		
	}
	
	private void checkXmi(Xmi expected) {
		Xmi actual = TestTools.asXmi(schema);
		assertEquals(expected, actual);		
	}
	
	@Before
	public void setup() {
		// we'll use EMPSCHM throughout these tests
		schema = TestTools.getEmpschmSchema();
		objectGraph = TestTools.asObjectGraph(schema);
		xmi = TestTools.asXmi(schema);
	}
	
	@Test
	public void testChainedSet() {
		
		int originalSetCount = schema.getSets().size();
		
		SchemaRecord recordEmployee = schema.getRecord("EMPLOYEE");
		SchemaRecord recordInsurancePlan = schema.getRecord("INSURANCE-PLAN");
		
		CreateSetCommand command = new CreateSetCommand(recordEmployee, SetMode.CHAINED);
		command.setMemberRecord(recordInsurancePlan);
		
		
		command.execute();
		ObjectGraph touchedObjectGraph = TestTools.asObjectGraph(schema);
		Xmi touchedXmi = TestTools.asXmi(schema);
		
		assertEquals(originalSetCount + 1, schema.getSets().size());
		Set set = schema.getSet("NEW-SET-1");
		assertNotNull(set);
		assertEquals(originalSetCount, schema.getSets().indexOf(set));
		assertEquals("NEW-SET-1", set.getName());
		assertSame(SetMode.CHAINED, set.getMode());
		assertNull(set.getIndexedSetModeSpecification());
		assertNull(set.getSystemOwner());
		assertEquals(1, set.getMembers().size());
		assertEquals(0, set.getViaMembers().size());
		
		OwnerRole ownerRole = set.getOwner();
		assertSame(recordEmployee, ownerRole.getRecord());
		assertEquals(17, ownerRole.getNextDbkeyPosition());
		assertEquals(18, ownerRole.getPriorDbkeyPosition().shortValue());
		
		MemberRole memberRole = set.getMembers().get(0);
		assertSame(recordInsurancePlan, memberRole.getRecord());
		assertSame(SetMembershipOption.MANDATORY_AUTOMATIC, memberRole.getMembershipOption());
		assertEquals(1, memberRole.getNextDbkeyPosition().shortValue());
		assertEquals(2, memberRole.getPriorDbkeyPosition().shortValue());
		assertEquals(3, memberRole.getOwnerDbkeyPosition().shortValue());
		assertNull(memberRole.getIndexDbkeyPosition());
		assertNull(memberRole.getSortKey());
		assertEquals(1, memberRole.getConnectionParts().size());
		
		ConnectionPart connectionPart = memberRole.getConnectionParts().get(0);
		assertEquals(0, connectionPart.getBendpointLocations().size());
		assertNull(connectionPart.getConnector());
		assertNull(connectionPart.getSourceEndpointLocation());
		assertNull(connectionPart.getTargetEndpointLocation());
		assertEquals(schema.getDiagramData().getConnectionParts().size() - 1, 
		 	 	 	 schema.getDiagramData().getConnectionParts().indexOf(connectionPart));	
				
		ConnectionLabel connectionLabel = memberRole.getConnectionLabel();		
		assertNotNull(connectionLabel);
		assertSame(LabelAlignment.LEFT, connectionLabel.getAlignment());
		assertEquals(schema.getDiagramData().getConnectionLabels().size() - 1, 
			 	 	 schema.getDiagramData().getConnectionLabels().indexOf(connectionLabel));	
	
		
		DiagramLocation diagramLocation = connectionLabel.getDiagramLocation();
		assertNotNull(diagramLocation);
		assertEquals(schema.getDiagramData().getLocations().size() - 1,
					 schema.getDiagramData().getLocations().indexOf(diagramLocation));
		assertEquals("set label NEW-SET-1 (INSURANCE-PLAN)", diagramLocation.getEyecatcher());
		assertEquals(recordEmployee.getDiagramLocation().getX() + RecordFigure.UNSCALED_WIDTH + 5, 
					 diagramLocation.getX());
		assertEquals(recordEmployee.getDiagramLocation().getY(), diagramLocation.getY());
		
		
		command.undo();
		checkObjectGraph(objectGraph);
		checkXmi(xmi);
		
		
		command.redo();
		checkObjectGraph(touchedObjectGraph);
		checkXmi(touchedXmi);
		
	}
	
	@Test
	public void testIndexedSet() {
		
		int originalSetCount = schema.getSets().size();
		
		SchemaRecord recordEmployee = schema.getRecord("EMPLOYEE");
		SchemaRecord recordInsurancePlan = schema.getRecord("INSURANCE-PLAN");
		
		CreateSetCommand command = new CreateSetCommand(recordEmployee, SetMode.INDEXED);
		command.setMemberRecord(recordInsurancePlan);
		
		
		command.execute();
		ObjectGraph touchedObjectGraph = TestTools.asObjectGraph(schema);
		Xmi touchedXmi = TestTools.asXmi(schema);
		
		assertEquals(originalSetCount + 1, schema.getSets().size());
		Set set = schema.getSet("NEW-SET-1");
		assertNotNull(set);
		assertEquals(originalSetCount, schema.getSets().indexOf(set));
		assertEquals("NEW-SET-1", set.getName());
		assertSame(SetMode.INDEXED, set.getMode());
		assertNull(set.getSystemOwner());
		assertEquals(1, set.getMembers().size());
		assertEquals(0, set.getViaMembers().size());
		
		IndexedSetModeSpecification indexedSetModeSpecification = 
			set.getIndexedSetModeSpecification();
		assertNotNull(indexedSetModeSpecification);
		assertNull(indexedSetModeSpecification.getKeyCount());
		assertEquals("NEW-SET-1", indexedSetModeSpecification.getSymbolicIndexName());
		assertNull(indexedSetModeSpecification.getDisplacementPageCount());
		
		OwnerRole ownerRole = set.getOwner();
		assertSame(recordEmployee, ownerRole.getRecord());
		assertEquals(17, ownerRole.getNextDbkeyPosition());
		assertEquals(18, ownerRole.getPriorDbkeyPosition().shortValue());
		
		MemberRole memberRole = set.getMembers().get(0);
		assertSame(recordInsurancePlan, memberRole.getRecord());
		assertSame(SetMembershipOption.MANDATORY_AUTOMATIC, memberRole.getMembershipOption());
		assertNull(memberRole.getNextDbkeyPosition());
		assertNull(memberRole.getPriorDbkeyPosition());		
		assertEquals(1, memberRole.getIndexDbkeyPosition().shortValue());
		assertEquals(2, memberRole.getOwnerDbkeyPosition().shortValue());
		assertNull(memberRole.getSortKey());
		assertEquals(1, memberRole.getConnectionParts().size());
		
		ConnectionPart connectionPart = memberRole.getConnectionParts().get(0);
		assertEquals(0, connectionPart.getBendpointLocations().size());
		assertNull(connectionPart.getConnector());
		assertNull(connectionPart.getSourceEndpointLocation());
		assertNull(connectionPart.getTargetEndpointLocation());
		assertEquals(schema.getDiagramData().getConnectionParts().size() - 1, 
	 	 	 	 	 schema.getDiagramData().getConnectionParts().indexOf(connectionPart));
		
		ConnectionLabel connectionLabel = memberRole.getConnectionLabel();
		assertNotNull(connectionLabel);
		assertSame(LabelAlignment.LEFT, connectionLabel.getAlignment());
		assertEquals(schema.getDiagramData().getConnectionLabels().size() - 1, 
		 	 	 	 schema.getDiagramData().getConnectionLabels().indexOf(connectionLabel));
		
		DiagramLocation diagramLocation = connectionLabel.getDiagramLocation();
		assertNotNull(diagramLocation);
		assertEquals(schema.getDiagramData().getLocations().size() - 1,
					 schema.getDiagramData().getLocations().indexOf(diagramLocation));
		assertEquals("set label NEW-SET-1 (INSURANCE-PLAN)", diagramLocation.getEyecatcher());
		assertEquals(recordEmployee.getDiagramLocation().getX() + RecordFigure.UNSCALED_WIDTH + 5, 
					 diagramLocation.getX());
		assertEquals(recordEmployee.getDiagramLocation().getY(), diagramLocation.getY());
		
		
		command.undo();
		checkObjectGraph(objectGraph);
		checkXmi(xmi);
		
		
		command.redo();
		checkObjectGraph(touchedObjectGraph);
		checkXmi(touchedXmi);
		
	}	

}
