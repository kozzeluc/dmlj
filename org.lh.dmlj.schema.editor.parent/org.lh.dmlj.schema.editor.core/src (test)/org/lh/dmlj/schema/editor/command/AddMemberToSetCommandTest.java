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
import static org.junit.Assert.fail;
import static org.lh.dmlj.schema.editor.testtool.TestTools.assertEquals;

import org.eclipse.core.runtime.AssertionFailedException;
import org.junit.Before;
import org.junit.Test;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.LabelAlignment;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMembershipOption;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.editor.figure.RecordFigure;
import org.lh.dmlj.schema.editor.prefix.PointerType;
import org.lh.dmlj.schema.editor.testtool.ObjectGraph;
import org.lh.dmlj.schema.editor.testtool.TestTools;
import org.lh.dmlj.schema.editor.testtool.Xmi;

public class AddMemberToSetCommandTest {

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
	public void testIndexedSetNotSupported() {
		Set set = schema.getSet("OFFICE-EMPLOYEE");
		assertSame(SetMode.INDEXED, set.getMode());		
		try {
			new AddMemberToSetCommand(set);
			fail("should throw an AssertionFailedException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: set is NOT chained", e.getMessage());
		}
	}
	
	@Test
	public void testUnsortedChainedSet() {
		
		Set set = schema.getSet("JOB-EMPOSITION");
		assertEquals(1, set.getMembers().size());
		SchemaRecord jobRecord = schema.getRecord("JOB");
		SchemaRecord employeeRecord = schema.getRecord("EMPLOYEE");
		
		AddMemberToSetCommand command = new AddMemberToSetCommand(set);
		command.setMemberRecord(employeeRecord);
		
		
		command.execute();
		ObjectGraph touchedObjectGraph = TestTools.asObjectGraph(schema);
		Xmi touchedXmi = TestTools.asXmi(schema);
		
		assertEquals(2, set.getMembers().size());
		MemberRole newMemberRole = set.getMembers().get(1);
		assertSame(set, newMemberRole.getSet());
		assertSame(employeeRecord, newMemberRole.getRecord());
		assertEquals(1, newMemberRole.getConnectionParts().size());		
		assertEquals(Short.valueOf((short) 17), (Short) newMemberRole.getNextDbkeyPosition());
		assertEquals(Short.valueOf((short) 18), (Short) newMemberRole.getPriorDbkeyPosition());
		assertEquals(Short.valueOf((short) 19), (Short) newMemberRole.getOwnerDbkeyPosition());
		assertNull(newMemberRole.getIndexDbkeyPosition());
		assertSame(SetMembershipOption.MANDATORY_AUTOMATIC, newMemberRole.getMembershipOption());
		
		ConnectionPart connectionPart = newMemberRole.getConnectionParts().get(0);
		assertNotNull(connectionPart);
		assertEquals(0, connectionPart.getBendpointLocations().size());
		assertNull(connectionPart.getConnector());
		assertNull(connectionPart.getSourceEndpointLocation());
		assertNull(connectionPart.getTargetEndpointLocation());
		assertEquals(schema.getDiagramData().getConnectionParts().size() - 1, 
					 schema.getDiagramData().getConnectionParts().indexOf(connectionPart));
		
		ConnectionLabel connectionLabel = newMemberRole.getConnectionLabel();
		assertNotNull(connectionLabel);
		assertSame(LabelAlignment.LEFT, connectionLabel.getAlignment());
		assertEquals(schema.getDiagramData().getConnectionLabels().size() - 1, 
				 	 schema.getDiagramData().getConnectionLabels().indexOf(connectionLabel));
		
		DiagramLocation diagramLocation = connectionLabel.getDiagramLocation();
		assertNotNull(diagramLocation);
		assertEquals(schema.getDiagramData().getLocations().size() - 1,
				 	 schema.getDiagramData().getLocations().indexOf(diagramLocation));
		assertEquals("set label JOB-EMPOSITION (EMPLOYEE)", diagramLocation.getEyecatcher());
		assertEquals(jobRecord.getDiagramLocation().getX() + RecordFigure.UNSCALED_WIDTH + 5, 
					 diagramLocation.getX());
		assertEquals(jobRecord.getDiagramLocation().getY(), diagramLocation.getY());
		
		
		command.undo();
		checkObjectGraph(objectGraph);
		checkXmi(xmi);
		
		
		command.redo();
		checkObjectGraph(touchedObjectGraph);
		checkXmi(touchedXmi);
		
	}
	
	@Test
	public void testUnsortedChainedSet_ExplicitPointerTypes() {
		
		Set set = schema.getSet("JOB-EMPOSITION");
		assertEquals(1, set.getMembers().size());
		SchemaRecord jobRecord = schema.getRecord("JOB");
		SchemaRecord employeeRecord = schema.getRecord("EMPLOYEE");
		
		AddMemberToSetCommand command = 
			new AddMemberToSetCommand(set, new PointerType[] {PointerType.MEMBER_NEXT, 
															  PointerType.MEMBER_PRIOR});
		command.setMemberRecord(employeeRecord);
		
		
		command.execute();
		ObjectGraph touchedObjectGraph = TestTools.asObjectGraph(schema);
		Xmi touchedXmi = TestTools.asXmi(schema);
		
		assertEquals(2, set.getMembers().size());
		MemberRole newMemberRole = set.getMembers().get(1);
		assertSame(set, newMemberRole.getSet());
		assertSame(employeeRecord, newMemberRole.getRecord());
		assertEquals(1, newMemberRole.getConnectionParts().size());		
		assertEquals(Short.valueOf((short) 17), (Short) newMemberRole.getNextDbkeyPosition());
		assertEquals(Short.valueOf((short) 18), (Short) newMemberRole.getPriorDbkeyPosition());
		assertNull(newMemberRole.getOwnerDbkeyPosition());
		assertNull(newMemberRole.getIndexDbkeyPosition());
		assertSame(SetMembershipOption.MANDATORY_AUTOMATIC, newMemberRole.getMembershipOption());
		
		ConnectionPart connectionPart = newMemberRole.getConnectionParts().get(0);
		assertNotNull(connectionPart);
		assertEquals(0, connectionPart.getBendpointLocations().size());
		assertNull(connectionPart.getConnector());
		assertNull(connectionPart.getSourceEndpointLocation());
		assertNull(connectionPart.getTargetEndpointLocation());
		assertEquals(schema.getDiagramData().getConnectionParts().size() - 1, 
					 schema.getDiagramData().getConnectionParts().indexOf(connectionPart));
		
		ConnectionLabel connectionLabel = newMemberRole.getConnectionLabel();
		assertNotNull(connectionLabel);
		assertSame(LabelAlignment.LEFT, connectionLabel.getAlignment());
		assertEquals(schema.getDiagramData().getConnectionLabels().size() - 1, 
				 	 schema.getDiagramData().getConnectionLabels().indexOf(connectionLabel));
		
		DiagramLocation diagramLocation = connectionLabel.getDiagramLocation();
		assertNotNull(diagramLocation);
		assertEquals(schema.getDiagramData().getLocations().size() - 1,
				 	 schema.getDiagramData().getLocations().indexOf(diagramLocation));
		assertEquals("set label JOB-EMPOSITION (EMPLOYEE)", diagramLocation.getEyecatcher());
		assertEquals(jobRecord.getDiagramLocation().getX() + RecordFigure.UNSCALED_WIDTH + 5, 
					 diagramLocation.getX());
		assertEquals(jobRecord.getDiagramLocation().getY(), diagramLocation.getY());
		
		
		command.undo();
		checkObjectGraph(objectGraph);
		checkXmi(xmi);
		
		
		command.redo();
		checkObjectGraph(touchedObjectGraph);
		checkXmi(touchedXmi);
		
	}

	@Test
	public void testSortedChainedSet() {
		
		Set set = schema.getSet("DEPT-EMPLOYEE");
		assertSame(SetOrder.SORTED, set.getOrder());
		assertEquals(1, set.getMembers().size());
		SchemaRecord departmentRecord = schema.getRecord("DEPARTMENT");
		SchemaRecord jobRecord = schema.getRecord("JOB");		
		
		AddMemberToSetCommand command = new AddMemberToSetCommand(set);
		command.setMemberRecord(jobRecord);
		
		
		command.execute();
		ObjectGraph touchedObjectGraph = TestTools.asObjectGraph(schema);
		Xmi touchedXmi = TestTools.asXmi(schema);
		
		assertEquals(2, set.getMembers().size());
		MemberRole newMemberRole = set.getMembers().get(1);
		assertSame(set, newMemberRole.getSet());
		assertSame(jobRecord, newMemberRole.getRecord());
		assertEquals(1, newMemberRole.getConnectionParts().size());
		assertEquals(Short.valueOf((short) 4), (Short) newMemberRole.getNextDbkeyPosition());
		assertEquals(Short.valueOf((short) 5), (Short) newMemberRole.getPriorDbkeyPosition());
		assertEquals(Short.valueOf((short) 6), (Short) newMemberRole.getOwnerDbkeyPosition());
		assertNull(newMemberRole.getIndexDbkeyPosition());
		assertSame(SetMembershipOption.MANDATORY_AUTOMATIC, newMemberRole.getMembershipOption());		
		
		ConnectionPart connectionPart = newMemberRole.getConnectionParts().get(0);
		assertNotNull(connectionPart);
		assertEquals(0, connectionPart.getBendpointLocations().size());
		assertNull(connectionPart.getConnector());
		assertNull(connectionPart.getSourceEndpointLocation());
		assertNull(connectionPart.getTargetEndpointLocation());
		assertEquals(schema.getDiagramData().getConnectionParts().size() - 1, 
				 	 schema.getDiagramData().getConnectionParts().indexOf(connectionPart));
		
		ConnectionLabel connectionLabel = newMemberRole.getConnectionLabel();
		assertNotNull(connectionLabel);
		assertSame(LabelAlignment.LEFT, connectionLabel.getAlignment());
		assertEquals(schema.getDiagramData().getConnectionLabels().size() - 1, 
			 	 	 schema.getDiagramData().getConnectionLabels().indexOf(connectionLabel));	
	
		DiagramLocation diagramLocation = connectionLabel.getDiagramLocation();
		assertNotNull(diagramLocation);
		assertEquals(schema.getDiagramData().getLocations().size() - 1,
				 	 schema.getDiagramData().getLocations().indexOf(diagramLocation));
		assertEquals("set label DEPT-EMPLOYEE (JOB)", diagramLocation.getEyecatcher());
		assertEquals(departmentRecord.getDiagramLocation().getX() + RecordFigure.UNSCALED_WIDTH + 5, 
					 diagramLocation.getX());
		assertEquals(departmentRecord.getDiagramLocation().getY(), diagramLocation.getY());	
		
		Key sortKey = newMemberRole.getSortKey();
		assertNotNull(sortKey);
		assertEquals(jobRecord.getKeys().size() - 1, jobRecord.getKeys().indexOf(sortKey));
		assertSame(DuplicatesOption.NOT_ALLOWED, sortKey.getDuplicatesOption());
		assertEquals(false, sortKey.isCalcKey());
		assertEquals(false, sortKey.isCompressed());
		assertEquals(false, sortKey.isNaturalSequence());
		assertEquals(1, sortKey.getElements().size());
		assertEquals("JOB-ID-0440", sortKey.getElements().get(0).getElement().getName());
		assertSame(SortSequence.ASCENDING, sortKey.getElements().get(0).getSortSequence());
		assertEquals(false, sortKey.getElements().get(0).isDbkey());		
		
		
		command.undo();
		checkObjectGraph(objectGraph);
		checkXmi(xmi);
		
		
		command.redo();
		checkObjectGraph(touchedObjectGraph);
		checkXmi(touchedXmi);	
	}
	
	@Test
	public void testPointerTypes() {
		
		Set set = schema.getSet("COVERAGE-CLAIMS");
		
		// invalid pointer type specifications
		try {
			new AddMemberToSetCommand(set, new PointerType[] {});
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("NEXT pointer is mandatory", e.getMessage());
		}
		try {
			new AddMemberToSetCommand(set, new PointerType[] {PointerType.MEMBER_NEXT,
															  PointerType.OWNER_NEXT});
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("invalid pointer type: OWNER_NEXT", e.getMessage());
		}
		try {
			new AddMemberToSetCommand(set, new PointerType[] {PointerType.MEMBER_NEXT,
															  PointerType.OWNER_PRIOR});
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("invalid pointer type: OWNER_PRIOR", e.getMessage());
		}
		try {
			new AddMemberToSetCommand(set, new PointerType[] {PointerType.MEMBER_NEXT,
															  PointerType.MEMBER_INDEX});
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("invalid pointer type: MEMBER_INDEX", e.getMessage());
		}
		
		// ALL valid pointer specifiations (the order in which they are specified does NOT matter)
		new AddMemberToSetCommand(set, new PointerType[] {PointerType.MEMBER_NEXT});
		new AddMemberToSetCommand(set, new PointerType[] {PointerType.MEMBER_NEXT,
				  										  PointerType.MEMBER_PRIOR});
		new AddMemberToSetCommand(set, new PointerType[] {PointerType.MEMBER_NEXT,
				  										  PointerType.MEMBER_OWNER});
		new AddMemberToSetCommand(set, new PointerType[] {PointerType.MEMBER_NEXT,
				  										  PointerType.MEMBER_PRIOR,
				  										  PointerType.MEMBER_OWNER});
	}

}
