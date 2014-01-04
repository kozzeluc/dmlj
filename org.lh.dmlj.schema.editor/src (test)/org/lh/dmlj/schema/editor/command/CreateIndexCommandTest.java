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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.lh.dmlj.schema.editor.testtool.TestTools.asObjectGraph;
import static org.lh.dmlj.schema.editor.testtool.TestTools.asXmi;
import static org.lh.dmlj.schema.editor.testtool.TestTools.assertEquals;
import static org.lh.dmlj.schema.editor.testtool.TestTools.getEmpschmSchema;

import org.eclipse.emf.ecore.EReference;
import org.junit.Test;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.LabelAlignment;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMembershipOption;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.command.annotation.Item;
import org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory;
import org.lh.dmlj.schema.editor.command.annotation.Owner;
import org.lh.dmlj.schema.editor.command.annotation.Reference;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeDispatcher;
import org.lh.dmlj.schema.editor.figure.IndexFigure;
import org.lh.dmlj.schema.editor.testtool.ObjectGraph;
import org.lh.dmlj.schema.editor.testtool.Xmi;

public class CreateIndexCommandTest {

	@Test
	public void test() {
		
		// get the EMPSCHM version 1 schema from the testdata folder; locate the EMPLOYEE record
		Schema schema = getEmpschmSchema();
		Xmi xmi = asXmi(schema);
		ObjectGraph objectGraph = asObjectGraph(schema);
		SchemaRecord record = schema.getRecord("EMPLOYEE");
		int originalAreaCount = schema.getAreas().size();
		int originalSetCount = schema.getSets().size();
		int originalDiagramLocationsCount = schema.getDiagramData().getLocations().size();
		
		// create the command
		CreateIndexCommand command = new CreateIndexCommand(record);
		
		// execute the command and check that the index is created, in all of its facets...
		command.execute();
		Xmi xmi1 = asXmi(schema);
		ObjectGraph objectGraph1 = asObjectGraph(schema);
		
		// check for a new system-owned indexed set
		assertEquals(originalSetCount + 1, schema.getSets().size());
		Set set = schema.getSets().get(originalSetCount); // gets last set in the list
		assertEquals("NEW-INDEX-1", set.getName());
		assertEquals(SetMode.INDEXED, set.getMode());
		assertEquals(SetOrder.LAST, set.getOrder());
		assertNull(set.getOwner());
		assertEquals(1, set.getMembers().size());	
		assertEquals(0, set.getViaMembers().size());
		
		// check the set's system owner
		SystemOwner systemOwner = set.getSystemOwner();
		assertNotNull(systemOwner);		
		
		// check the set's system owner area specification
		AreaSpecification areaSpecification = systemOwner.getAreaSpecification();
		assertNotNull(areaSpecification);
		assertNull(areaSpecification.getOffsetExpression());
		assertNull(areaSpecification.getSymbolicSubareaName());
		
		// check the (new) set's system owner area
		assertEquals(originalAreaCount + 1, schema.getAreas().size());
		SchemaArea area = areaSpecification.getArea();
		assertNotNull(area);
		assertEquals("NEW-INDEX-1-AREA", area.getName());
		assertEquals(1, area.getAreaSpecifications().size());
		assertEquals(1, area.getIndexes().size());
		assertTrue(area.getIndexes().get(0) == systemOwner);
		assertEquals(0, area.getProcedures().size());
		assertEquals(0, area.getRecords().size());
		
		// check the set's member
		MemberRole memberRole = set.getMembers().get(0);
		assertTrue(memberRole.getRecord() == record);
		assertEquals(SetMembershipOption.MANDATORY_AUTOMATIC, memberRole.getMembershipOption());
		assertNull(memberRole.getIndexDbkeyPosition());
		assertNull(memberRole.getNextDbkeyPosition());
		assertNull(memberRole.getPriorDbkeyPosition());
		assertNull(memberRole.getOwnerDbkeyPosition());
		assertNull(memberRole.getSortKey());
		
		// check the connection part
		assertEquals(1, memberRole.getConnectionParts().size());
		ConnectionPart connectionPart = memberRole.getConnectionParts().get(0);		
		assertEquals(0, connectionPart.getBendpointLocations().size());
		assertNull(connectionPart.getConnector());
		assertNull(connectionPart.getSourceEndpointLocation());
		assertNull(connectionPart.getTargetEndpointLocation());
		
		// check the connection label
		ConnectionLabel connectionLabel = memberRole.getConnectionLabel();
		assertNotNull(connectionLabel);
		assertEquals(LabelAlignment.LEFT, connectionLabel.getAlignment());
		
		// check the diagram location created for the set's system owner
		assertEquals(originalDiagramLocationsCount + 2, 
				 	 schema.getDiagramData().getLocations().size());
		DiagramLocation diagramLocation1 = systemOwner.getDiagramLocation();
		assertNotNull(diagramLocation1);
		assertEquals(originalDiagramLocationsCount, 
					 schema.getDiagramData().getLocations().indexOf(diagramLocation1));
		assertEquals("system owner NEW-INDEX-1", diagramLocation1.getEyecatcher());
		assertEquals(record.getDiagramLocation().getX() - IndexFigure.UNSCALED_WIDTH, 
				     diagramLocation1.getX());
		assertEquals(record.getDiagramLocation().getY() - 2 * IndexFigure.UNSCALED_HEIGHT, 
					 diagramLocation1.getY());
		
		// check the diagram location created for the set's connection label
		DiagramLocation diagramLocation2 = connectionLabel.getDiagramLocation();
		assertNotNull(diagramLocation2);
		assertEquals(originalDiagramLocationsCount + 1, 
				 	 schema.getDiagramData().getLocations().indexOf(diagramLocation2));
		assertEquals("set label NEW-INDEX-1 (EMPLOYEE)", diagramLocation2.getEyecatcher());
		assertEquals(systemOwner.getDiagramLocation().getX() + IndexFigure.UNSCALED_WIDTH + 5, 
					 diagramLocation2.getX());
		assertEquals(systemOwner.getDiagramLocation().getY(), diagramLocation2.getY());	
		
		
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
		assertTrue(owner == schema);
		
		// make sure the reference is set
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
		assertTrue(item == set);
		
		
		// undo the command and check if the system-owned indexed set is removed (as a matter of 
		// fact, check if the schema exactly matches the state before creating the index)
		command.undo();	
		Xmi xmi2 = asXmi(schema);
		assertEquals(xmi, xmi2);
		ObjectGraph objectGraph2 = asObjectGraph(schema);
		assertEquals(objectGraph, objectGraph2);
		
		
		// make sure the owner is still set
		owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(owner == schema);
		
		// make sure the reference is still set
		reference = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Reference.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(reference == SchemaPackage.eINSTANCE.getSchema_Sets());			
		
		// make sure the item is still set
		item = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Item.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(item == set);
		
		
		// redo the command and check that the index is created again, in all of its facets (as a 
		// matter of fact, check if the schema exactly matches the state after creating the index
		// when the command was first executed)
		command.redo();	
		Xmi xmi3 = asXmi(schema);
		assertEquals(xmi1, xmi3);
		ObjectGraph objectGraph3 = asObjectGraph(schema);
		assertEquals(objectGraph1, objectGraph3);		
		
		
		// make sure the owner is still set
		owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(owner == schema);
		
		// make sure the reference is still set
		reference = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Reference.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(reference == SchemaPackage.eINSTANCE.getSchema_Sets());			
		
		// make sure the item is still set
		item = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Item.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(item == set);		
		
	}

}
