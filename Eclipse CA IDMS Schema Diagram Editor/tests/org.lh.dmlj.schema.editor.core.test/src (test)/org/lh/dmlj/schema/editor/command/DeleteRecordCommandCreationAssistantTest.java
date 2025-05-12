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

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.junit.Test;
import org.lh.dmlj.schema.AreaProcedureCallSpecification;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.RecordProcedureCallSpecification;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.testtool.TestTools;

public class DeleteRecordCommandCreationAssistantTest {
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCanDeleteRecord() {
		
		// a record that does not participate in any set: true
		SchemaRecord record = mock(SchemaRecord.class);
		EList<OwnerRole> ownerRoles = mock(EList.class);
		EList<MemberRole> memberRoles = mock(EList.class);
		when(ownerRoles.isEmpty()).thenReturn(true);
		when(memberRoles.isEmpty()).thenReturn(true);
		when(record.getOwnerRoles()).thenReturn(ownerRoles);
		when(record.getMemberRoles()).thenReturn(memberRoles);
		assertTrue(DeleteRecordCommandCreationAssistant.canDeleteRecord(record));
		
		// a record that does participate as an owner in at least 1 set and not as a member in any
		// set: false
		record = mock(SchemaRecord.class);
		ownerRoles = mock(EList.class);
		memberRoles = mock(EList.class);
		when(ownerRoles.isEmpty()).thenReturn(false);
		when(memberRoles.isEmpty()).thenReturn(true);
		when(record.getOwnerRoles()).thenReturn(ownerRoles);
		when(record.getMemberRoles()).thenReturn(memberRoles);
		assertFalse(DeleteRecordCommandCreationAssistant.canDeleteRecord(record));
		
		// a record that does participate as a member in at least 1 set and not as an owner in any
		// set: false
		record = mock(SchemaRecord.class);
		ownerRoles = mock(EList.class);
		memberRoles = mock(EList.class);
		when(ownerRoles.isEmpty()).thenReturn(true);
		when(memberRoles.isEmpty()).thenReturn(false);
		when(record.getOwnerRoles()).thenReturn(ownerRoles);
		when(record.getMemberRoles()).thenReturn(memberRoles);
		assertFalse(DeleteRecordCommandCreationAssistant.canDeleteRecord(record));
		
		// a record that does participate as an owner in at least 1 set and as a member in at least 
		// 1 set: false
		record = mock(SchemaRecord.class);
		ownerRoles = mock(EList.class);
		memberRoles = mock(EList.class);
		when(ownerRoles.isEmpty()).thenReturn(false);
		when(memberRoles.isEmpty()).thenReturn(false);
		when(record.getOwnerRoles()).thenReturn(ownerRoles);
		when(record.getMemberRoles()).thenReturn(memberRoles);
		assertFalse(DeleteRecordCommandCreationAssistant.canDeleteRecord(record));
	}
	
	@Test
	public void testIsProcedureObsolete() {
		
		// we need a record and its area
		SchemaRecord record = mock(SchemaRecord.class);
		SchemaArea area = mock(SchemaArea.class);
		AreaSpecification areaSpecification = mock(AreaSpecification.class);
		when(record.getAreaSpecification()).thenReturn(areaSpecification);
		when(areaSpecification.getArea()).thenReturn(area);
		
		// procedure not called for any area or record: true
		Procedure procedure = TestTools.createProcedure();		
		assertTrue(DeleteRecordCommandCreationAssistant.isProcedureObsolete(procedure, record, false));
		
		// procedure only called for the record's area and the area is NOT obsolete: false
		procedure = TestTools.createProcedure();
		TestTools.addProcedureCallSpecification(procedure, area);
		assertFalse(DeleteRecordCommandCreationAssistant.isProcedureObsolete(procedure, record, false));
		
		// procedure only called for the record's area and the area IS obsolete: true
		assertTrue(DeleteRecordCommandCreationAssistant.isProcedureObsolete(procedure, record, true));
		
		// procedure only called for the record's area AND another area: false (whether the record's  
		// area is obsolete or not)
		procedure = TestTools.createProcedure();				
		TestTools.addProcedureCallSpecification(procedure, area);
		TestTools.addProcedureCallSpecification(procedure, mock(SchemaArea.class));
		assertFalse(DeleteRecordCommandCreationAssistant.isProcedureObsolete(procedure, record, false));
		assertFalse(DeleteRecordCommandCreationAssistant.isProcedureObsolete(procedure, record, true));
		
		// procedure only called for the record: true (whether the record's area is obsolete or not)
		procedure = TestTools.createProcedure();	
		TestTools.addProcedureCallSpecification(procedure, record);
		assertTrue(DeleteRecordCommandCreationAssistant.isProcedureObsolete(procedure, record, false));
		assertTrue(DeleteRecordCommandCreationAssistant.isProcedureObsolete(procedure, record, true));
		
		// procedure only called for the record AND another record: false (whether the record's area 
		// is obsolete or not)
		procedure = TestTools.createProcedure();	
		TestTools.addProcedureCallSpecification(procedure, record);
		TestTools.addProcedureCallSpecification(procedure, mock(SchemaRecord.class));
		assertFalse(DeleteRecordCommandCreationAssistant.isProcedureObsolete(procedure, record, false));
		assertFalse(DeleteRecordCommandCreationAssistant.isProcedureObsolete(procedure, record, true));
		
		// procedure only called for the record AND an area other than the record's area: false 
		// (whether the record's area is obsolete or not)
		procedure = TestTools.createProcedure();	
		TestTools.addProcedureCallSpecification(procedure, record);
		TestTools.addProcedureCallSpecification(procedure, mock(SchemaArea.class));
		assertFalse(DeleteRecordCommandCreationAssistant.isProcedureObsolete(procedure, record, false));
		assertFalse(DeleteRecordCommandCreationAssistant.isProcedureObsolete(procedure, record, true));
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetCommand() {
		
		// we need a record and its area (no procedures for now)...
		SchemaRecord record = mock(SchemaRecord.class);
		SchemaArea area = mock(SchemaArea.class);
		
		EList<AreaProcedureCallSpecification> areaProcedures = new BasicEList<>();
		when(area.getProcedures()).thenReturn(areaProcedures);
		
		EList<RecordProcedureCallSpecification> recordProcedures = new BasicEList<>();
		when(record.getProcedures()).thenReturn(recordProcedures);
				
		AreaSpecification areaSpecification = mock(AreaSpecification.class);
		when(record.getAreaSpecification()).thenReturn(areaSpecification);
		when(areaSpecification.getArea()).thenReturn(area);
		
		EList<SchemaRecord> areaRecords = new BasicEList<>();
		areaRecords.add(record);
		when(area.getRecords()).thenReturn(areaRecords);
	
		
		// the record participates as an owner in a set: null 
		EList<OwnerRole> ownerRoles = mock(EList.class);
		EList<MemberRole> memberRoles = mock(EList.class);
		when(ownerRoles.isEmpty()).thenReturn(false);
		when(memberRoles.isEmpty()).thenReturn(true);
		when(record.getOwnerRoles()).thenReturn(ownerRoles);
		when(record.getMemberRoles()).thenReturn(memberRoles);
		assertNull(DeleteRecordCommandCreationAssistant.getCommand(record));
		
		// the record participates as a member in a set: null 
		when(ownerRoles.isEmpty()).thenReturn(true);
		when(memberRoles.isEmpty()).thenReturn(false);
		when(record.getOwnerRoles()).thenReturn(ownerRoles);
		when(record.getMemberRoles()).thenReturn(memberRoles);
		assertNull(DeleteRecordCommandCreationAssistant.getCommand(record));
		
		// the record participates as an owner and as a member in a set: null 
		when(ownerRoles.isEmpty()).thenReturn(false);
		when(memberRoles.isEmpty()).thenReturn(false);
		when(record.getOwnerRoles()).thenReturn(ownerRoles);
		when(record.getMemberRoles()).thenReturn(memberRoles);
		assertNull(DeleteRecordCommandCreationAssistant.getCommand(record));
		
		// no procedures to be deleted, nor is the record's area obsolete: a simple command
		when(ownerRoles.isEmpty()).thenReturn(true);
		when(memberRoles.isEmpty()).thenReturn(true);
		areaRecords.add(mock(SchemaRecord.class));
		DeleteRecordCommand deleteRecordCommand = 
			(DeleteRecordCommand) DeleteRecordCommandCreationAssistant.getCommand(record);
		assertNotNull(deleteRecordCommand);
		assertSame(record, deleteRecordCommand.record);
		
		// no procedures to be deleted, but the record's area is obsolete: a compound command to
		// delete both the record and its area
		areaRecords.remove(1);
		ModelChangeCompoundCommand compoundCommand = 
			(ModelChangeCompoundCommand) DeleteRecordCommandCreationAssistant.getCommand(record);
		assertEquals(2, compoundCommand.getCommands().size());
		assertTrue(compoundCommand.getCommands().get(0) instanceof DeleteRecordCommand);
		assertTrue(compoundCommand.getCommands().get(1) instanceof DeleteAreaCommand);
		deleteRecordCommand = (DeleteRecordCommand) compoundCommand.getCommands().get(0);
		assertSame(record, deleteRecordCommand.record);
		DeleteAreaCommand deleteAreaCommand = 
			(DeleteAreaCommand) compoundCommand.getCommands().get(1);
		assertSame(area, deleteAreaCommand.area);
		
		// 2 procedures to delete as well: a compound command to delete the record, area and both
		// procedures (the delete procedure commands are sorted by procedure name)
		Procedure areaProcedure = TestTools.createProcedure("A");
		Procedure recordProcedure = TestTools.createProcedure("R");
		TestTools.addProcedureCallSpecification(areaProcedure, area);
		TestTools.addProcedureCallSpecification(recordProcedure, record);
		compoundCommand = 
			(ModelChangeCompoundCommand) DeleteRecordCommandCreationAssistant.getCommand(record);
		assertEquals(6, compoundCommand.getCommands().size());
		assertTrue(compoundCommand.getCommands().get(0) instanceof RemoveRecordProcedureCallSpecificationCommand);
		assertTrue(compoundCommand.getCommands().get(1) instanceof DeleteRecordCommand);
		assertTrue(compoundCommand.getCommands().get(2) instanceof RemoveAreaProcedureCallSpecificationCommand);
		assertTrue(compoundCommand.getCommands().get(3) instanceof DeleteAreaCommand);
		assertTrue(compoundCommand.getCommands().get(4) instanceof DeleteProcedureCommand);
		assertTrue(compoundCommand.getCommands().get(5) instanceof DeleteProcedureCommand);
		RemoveRecordProcedureCallSpecificationCommand removeRecordProcedureCallSpecificationCommand =
				(RemoveRecordProcedureCallSpecificationCommand) compoundCommand.getCommands().get(0);
		assertSame(recordProcedure.getCallSpecifications().get(0), 
				   removeRecordProcedureCallSpecificationCommand.callSpec);
		deleteRecordCommand = (DeleteRecordCommand) compoundCommand.getCommands().get(1);
		assertSame(record, deleteRecordCommand.record);
		RemoveAreaProcedureCallSpecificationCommand removeAreaProcedureCallSpecificationCommand =
				(RemoveAreaProcedureCallSpecificationCommand) compoundCommand.getCommands().get(2);
		assertSame(areaProcedure.getCallSpecifications().get(0), 
				   removeAreaProcedureCallSpecificationCommand.callSpec);
		deleteAreaCommand = (DeleteAreaCommand) compoundCommand.getCommands().get(3);
		assertSame(area, deleteAreaCommand.area);
		DeleteProcedureCommand deleteProcedureCommand = 
			(DeleteProcedureCommand) compoundCommand.getCommands().get(4);
		assertSame(areaProcedure, deleteProcedureCommand.procedure);
		deleteProcedureCommand = (DeleteProcedureCommand) compoundCommand.getCommands().get(5);
		assertSame(recordProcedure, deleteProcedureCommand.procedure);
	}

}
