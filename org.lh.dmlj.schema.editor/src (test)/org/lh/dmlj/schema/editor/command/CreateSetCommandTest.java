/**
 * Copyright (C) 2014  Luc Hermans
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
import static org.junit.Assert.assertTrue;
import static org.lh.dmlj.schema.editor.testtool.TestTools.assertEquals;

import org.eclipse.emf.ecore.EReference;
import org.junit.Before;
import org.junit.Test;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.LabelAlignment;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMembershipOption;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.editor.command.annotation.Item;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory;
import org.lh.dmlj.schema.editor.command.annotation.Owner;
import org.lh.dmlj.schema.editor.command.annotation.Reference;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeDispatcher;
import org.lh.dmlj.schema.editor.figure.RecordFigure;
import org.lh.dmlj.schema.editor.testtool.ObjectGraph;
import org.lh.dmlj.schema.editor.testtool.TestTools;

public class CreateSetCommandTest {

	private ObjectGraph objectGraph;
	private Schema 		schema;	

	private void checkObjectGraph(ObjectGraph expected) {
		ObjectGraph actual = TestTools.asObjectGraph(schema);
		assertEquals(expected, actual);		
	}
	
	@Before
	public void setup() {
		// we'll use EMPSCHM throughout these tests
		schema = TestTools.getEmpschmSchema();
		objectGraph = TestTools.asObjectGraph(schema);
	}
	
	@Test
	public void testAnnotations() {
		
		SchemaRecord recordEmployee = schema.getRecord("EMPLOYEE");
		SchemaRecord recordInsurancePlan = schema.getRecord("INSURANCE-PLAN");
		
		CreateSetCommand command = new CreateSetCommand(recordEmployee, SetMode.CHAINED);
		command.setMemberRecord(recordInsurancePlan);		
		
		command.execute();		
		
		// once execute() has been called, all annotated field values should be in place; make sure
		// the command class itself is annotated with @ModelChange with its type set to 
		// ModelChangeCategory.ADD_ITEM
		ModelChange modelChangeAnnotation = command.getClass().getAnnotation(ModelChange.class);	
		assertNotNull(modelChangeAnnotation);
		assertEquals(ModelChangeCategory.ADD_ITEM, modelChangeAnnotation.category());
		
		// make sure the owner is set
		Schema owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(schema, owner);	
		
		// make sure the sets reference is set
		EReference reference = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Reference.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(reference == SchemaPackage.eINSTANCE.getSchema_Sets());
		
		// make sure the item is set
		Set item = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Item.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertNotNull(item);
		
		
		command.undo();
		
		// make sure the owner is still set
		owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(schema, owner);		
		
		// make sure the sets reference is still set
		reference = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Reference.class, 
			ModelChangeDispatcher.Availability.MANDATORY);		
		assertTrue(reference == SchemaPackage.eINSTANCE.getSchema_Sets());
		
		// make sure the item is still set
		Set item2 = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Item.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(item, item2);		
		
		
		command.redo();
		
		// make sure the owner is still set
		owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(schema, owner);		
		
		// make sure the sets reference is still set
		reference = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Reference.class, 
			ModelChangeDispatcher.Availability.MANDATORY);		
		assertTrue(reference == SchemaPackage.eINSTANCE.getSchema_Sets());
		
		// make sure the item is still set
		item2 = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Item.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(item, item2);		
				
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
		assertEquals(0, memberRole.getConnectionParts().get(0).getBendpointLocations().size());
		assertNull(memberRole.getConnectionParts().get(0).getConnector());
		assertNull(memberRole.getConnectionParts().get(0).getSourceEndpointLocation());
		assertNull(memberRole.getConnectionParts().get(0).getTargetEndpointLocation());
		assertNotNull(memberRole.getConnectionLabel());
		
		ConnectionLabel connectionLabel = memberRole.getConnectionLabel();
		assertNotNull(connectionLabel);
		assertSame(LabelAlignment.LEFT, connectionLabel.getAlignment());
		
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
		
		
		command.redo();
		checkObjectGraph(touchedObjectGraph);
		
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
		
		assertEquals(originalSetCount + 1, schema.getSets().size());
		Set set = schema.getSet("NEW-SET-1");
		assertNotNull(set);
		assertEquals(originalSetCount, schema.getSets().indexOf(set));
		assertEquals("NEW-SET-1", set.getName());
		assertSame(SetMode.INDEXED, set.getMode());
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
		assertNull(memberRole.getNextDbkeyPosition());
		assertNull(memberRole.getPriorDbkeyPosition());		
		assertEquals(1, memberRole.getIndexDbkeyPosition().shortValue());
		assertEquals(2, memberRole.getOwnerDbkeyPosition().shortValue());
		assertNull(memberRole.getSortKey());
		assertEquals(1, memberRole.getConnectionParts().size());
		assertEquals(0, memberRole.getConnectionParts().get(0).getBendpointLocations().size());
		assertNull(memberRole.getConnectionParts().get(0).getConnector());
		assertNull(memberRole.getConnectionParts().get(0).getSourceEndpointLocation());
		assertNull(memberRole.getConnectionParts().get(0).getTargetEndpointLocation());
		assertNotNull(memberRole.getConnectionLabel());
		
		ConnectionLabel connectionLabel = memberRole.getConnectionLabel();
		assertNotNull(connectionLabel);
		assertSame(LabelAlignment.LEFT, connectionLabel.getAlignment());
		
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
		
		
		command.redo();
		checkObjectGraph(touchedObjectGraph);
		
	}	

}
