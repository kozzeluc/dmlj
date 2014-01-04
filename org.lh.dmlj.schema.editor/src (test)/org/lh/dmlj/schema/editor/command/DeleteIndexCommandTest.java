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
import org.lh.dmlj.schema.AreaProcedureCallFunction;
import org.lh.dmlj.schema.AreaProcedureCallSpecification;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.ProcedureCallTime;
import org.lh.dmlj.schema.RecordProcedureCallSpecification;
import org.lh.dmlj.schema.RecordProcedureCallVerb;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.command.annotation.Item;
import org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory;
import org.lh.dmlj.schema.editor.command.annotation.Owner;
import org.lh.dmlj.schema.editor.command.annotation.Reference;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeDispatcher;
import org.lh.dmlj.schema.editor.testtool.ObjectGraph;
import org.lh.dmlj.schema.editor.testtool.TestTools;
import org.lh.dmlj.schema.editor.testtool.Xmi;

public class DeleteIndexCommandTest {

	@Test
	public void test_NoObsoleteArea() {
		
		// get the EMPSCHM version 1 schema from the testdata folder; locate the EMP-NAME-NDX system
		// owner
		Schema schema = getEmpschmSchema();
		Xmi xmi = asXmi(schema);		
		ObjectGraph objectGraph = asObjectGraph(schema);
		SystemOwner systemOwner = schema.getSet("EMP-NAME-NDX").getSystemOwner();
		assertNotNull(systemOwner);
		int originalAreaCount = schema.getAreas().size();
		int originalSetCount = schema.getSets().size();
		int originalDiagramLocationsCount = schema.getDiagramData().getLocations().size();
		int originalAreaSpecificationsCount = 
			schema.getArea("EMP-DEMO-REGION").getAreaSpecifications().size();
		
		// create the command
		DeleteIndexCommand command = new DeleteIndexCommand(systemOwner);
		
		// execute the command and check that the index is removed...
		command.execute();
		Xmi xmi1 = asXmi(schema);
		ObjectGraph objectGraph1 = asObjectGraph(schema);
		assertEquals(originalAreaCount, schema.getAreas().size());
		assertEquals(originalSetCount - 1, schema.getSets().size());
		assertEquals(originalDiagramLocationsCount - 2, 
					 schema.getDiagramData().getLocations().size());
		assertNull(schema.getSet("EMP-NAME-NDX"));
		assertEquals(originalAreaSpecificationsCount - 1, 
					 schema.getArea("EMP-DEMO-REGION").getAreaSpecifications().size());
		
		
		// once execute() has been called, all annotated field values should be in place; make sure
		// the command class itself is annotated with @ModelChange with its type set to 
		// ModelChangeCategory.REMOVE_ITEM
		ModelChange modelChangeAnnotation = command.getClass().getAnnotation(ModelChange.class);	
		assertNotNull(modelChangeAnnotation);
		assertEquals(ModelChangeCategory.REMOVE_ITEM, modelChangeAnnotation.category());		
		
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
		assertTrue(item == systemOwner.getSet());
		
		
		// undo the command and check if the system-owned indexed set is restored (as a matter of 
		// fact, check if the schema exactly matches the state before removing the index)
		command.undo();			
		Xmi xmi2 = TestTools.asXmi(schema);		
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
		assertTrue(item == systemOwner.getSet());
		

		// redo the command and check that the index is removed again, in all of its facets (as a 
		// matter of fact, check if the schema exactly matches the state before removing the index)
		command.redo();	
		Xmi xmi3 = TestTools.asXmi(schema);
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
		assertTrue(item == systemOwner.getSet());		
		
	}
	
	@Test
	public void test_ObsoleteArea() {
		
		// get the EMPSCHM version 1 schema from the testdata folder; create a new area, add 4 area
		// procedures (2 of which will become obsolete), locate the EMP-NAME-NDX system owner and 
		// move it to the new area - the only thing the area contains is the EMP-NAME-NDX index, so 
		// we expect it to be removed together with the index (and 2 procedures)
		Schema schema = getEmpschmSchema();
		//
		SchemaArea obsoleteArea = SchemaFactory.eINSTANCE.createSchemaArea();
		obsoleteArea.setName("OBSOLETE-AREA");
		obsoleteArea.setSchema(schema);
		// OBSPROC1 is called only for the area to become obsolete; this makes OBSPROC2 obsolete as
		// well
		Procedure obsoleteProcedure1 = SchemaFactory.eINSTANCE.createProcedure();
		obsoleteProcedure1.setName("OBSPROC1");
		schema.getProcedures().add(obsoleteProcedure1);
		//
		AreaProcedureCallSpecification obsoleteAreaProcedureCallSpecification1 = 
			SchemaFactory.eINSTANCE.createAreaProcedureCallSpecification();
		obsoleteAreaProcedureCallSpecification1.setArea(obsoleteArea);
		obsoleteAreaProcedureCallSpecification1.setProcedure(obsoleteProcedure1);
		obsoleteAreaProcedureCallSpecification1.setCallTime(ProcedureCallTime.BEFORE);
		obsoleteAreaProcedureCallSpecification1.setFunction(AreaProcedureCallFunction.FINISH);		
		// OBSPROC2 is called only for the area to become obsolete; this makes OBSPROC2 obsolete as
		// well
		Procedure obsoleteProcedure2 = SchemaFactory.eINSTANCE.createProcedure();
		obsoleteProcedure2.setName("OBSPROC2");
		schema.getProcedures().add(obsoleteProcedure2);
		// 
		AreaProcedureCallSpecification obsoleteAreaProcedureCallSpecification2 = 
			SchemaFactory.eINSTANCE.createAreaProcedureCallSpecification();
		obsoleteAreaProcedureCallSpecification2.setArea(obsoleteArea);
		obsoleteAreaProcedureCallSpecification2.setProcedure(obsoleteProcedure2);
		obsoleteAreaProcedureCallSpecification2.setCallTime(ProcedureCallTime.AFTER);
		obsoleteAreaProcedureCallSpecification2.setFunction(AreaProcedureCallFunction.READY_EXCLUSIVE);
		// PROC3 is called for both the area to become obsolete AND record EMPLOYEE; the fact that
		// it is called for the EMPLOYEE record prevents the procedure from being removed
		Procedure procedure3 = SchemaFactory.eINSTANCE.createProcedure();
		procedure3.setName("PROC3");
		schema.getProcedures().add(procedure3);
		//
		AreaProcedureCallSpecification areaProcedureCallSpecification3 = 
			SchemaFactory.eINSTANCE.createAreaProcedureCallSpecification();
		areaProcedureCallSpecification3.setArea(obsoleteArea);
		areaProcedureCallSpecification3.setProcedure(procedure3);
		areaProcedureCallSpecification3.setCallTime(ProcedureCallTime.AFTER);
		areaProcedureCallSpecification3.setFunction(AreaProcedureCallFunction.READY_EXCLUSIVE);
		//
		RecordProcedureCallSpecification recordProcedureCallSpecification3 = 
			SchemaFactory.eINSTANCE.createRecordProcedureCallSpecification();
		recordProcedureCallSpecification3.setRecord(schema.getRecord("EMPLOYEE"));
		recordProcedureCallSpecification3.setProcedure(procedure3);
		recordProcedureCallSpecification3.setCallTime(ProcedureCallTime.AFTER);
		recordProcedureCallSpecification3.setVerb(RecordProcedureCallVerb.ERASE);
		// PROC4 is called for both the area to become obsolete AND area EMP-DEMO-REGION; the fact 
		// that it is called for the EMP-DEMO-REGION area prevents the procedure from being removed
		Procedure procedure4 = SchemaFactory.eINSTANCE.createProcedure();
		procedure4.setName("PROC4");
		schema.getProcedures().add(procedure4);
		//
		AreaProcedureCallSpecification areaProcedureCallSpecification4a = 
			SchemaFactory.eINSTANCE.createAreaProcedureCallSpecification();
		areaProcedureCallSpecification4a.setArea(obsoleteArea);
		areaProcedureCallSpecification4a.setProcedure(procedure4);
		areaProcedureCallSpecification4a.setCallTime(ProcedureCallTime.AFTER);
		areaProcedureCallSpecification4a.setFunction(AreaProcedureCallFunction.READY_EXCLUSIVE);
		//
		AreaProcedureCallSpecification areaProcedureCallSpecification4b = 
			SchemaFactory.eINSTANCE.createAreaProcedureCallSpecification();
		areaProcedureCallSpecification4b.setArea(schema.getArea("EMP-DEMO-REGION"));
		areaProcedureCallSpecification4b.setProcedure(procedure4);
		areaProcedureCallSpecification4b.setCallTime(ProcedureCallTime.BEFORE);
		areaProcedureCallSpecification4b.setFunction(AreaProcedureCallFunction.READY_PROTECTED);		
		// MOVE the EMP-NAME-NDX index to the area to become obsolete
		Set set = schema.getSet("EMP-NAME-NDX");
		SystemOwner systemOwner = set.getSystemOwner();
		AreaSpecification areaSpecification = systemOwner.getAreaSpecification();
		areaSpecification.setArea(obsoleteArea);
		//
		int originalAreaCount = schema.getAreas().size();
		int originalSetCount = schema.getSets().size();
		int originalDiagramLocationsCount = schema.getDiagramData().getLocations().size();
		int originalProcedureCount = schema.getProcedures().size();
		//
		Xmi xmi = asXmi(schema);		
		ObjectGraph objectGraph = asObjectGraph(schema);
				
		// create the command
		DeleteIndexCommand command = new DeleteIndexCommand(systemOwner);
		
		// execute the command and check that the index, as well as the obsolete area and procedures  
		// are removed...		
		command.execute();
		Xmi xmi1 = asXmi(schema);
		ObjectGraph objectGraph1 = asObjectGraph(schema);
		assertEquals(originalAreaCount - 1, schema.getAreas().size());
		assertEquals(originalSetCount - 1, schema.getSets().size());
		assertEquals(originalDiagramLocationsCount - 2, 
					 schema.getDiagramData().getLocations().size());
		assertEquals(originalProcedureCount - 2, schema.getProcedures().size());
		assertNull(schema.getSet("EMP-NAME-NDX"));
		assertNull(schema.getArea("OBSOLETE-AREA"));
		assertNull(schema.getProcedure("OBSPROC1"));
		assertNull(schema.getProcedure("OBSPROC2"));
		
		
		// undo the command and check if the system-owned indexed set is restored (as a matter of 
		// fact, check if the schema exactly matches the state before removing the index)
		command.undo();			
		Xmi xmi2 = TestTools.asXmi(schema);		
		assertEquals(xmi, xmi2);		
		ObjectGraph objectGraph2 = asObjectGraph(schema);
		assertEquals(objectGraph, objectGraph2);		
		
		
		// redo the command and check that the index is removed again, in all of its facets (as a 
		// matter of fact, check if the schema exactly matches the state before removing the index)
		command.redo();	
		Xmi xmi3 = TestTools.asXmi(schema);
		assertEquals(xmi1, xmi3);
		ObjectGraph objectGraph3 = asObjectGraph(schema);
		assertEquals(objectGraph1, objectGraph3);		
		
	}

}
