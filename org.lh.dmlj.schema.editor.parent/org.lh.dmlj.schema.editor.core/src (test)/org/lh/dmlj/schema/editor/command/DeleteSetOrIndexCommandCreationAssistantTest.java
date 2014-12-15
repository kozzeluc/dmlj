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
import static org.junit.Assert.fail;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.junit.Before;
import org.junit.Test;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.command.annotation.Item;
import org.lh.dmlj.schema.editor.command.annotation.Owner;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeDispatcher;
import org.lh.dmlj.schema.editor.testtool.TestTools;

public class DeleteSetOrIndexCommandCreationAssistantTest {

	private Schema schema;	
	
	private void checkCommand(ModelChangeCompoundCommand compoundCommand, MemberRole memberRole) {		
		checkCommand(compoundCommand, memberRole, 0, compoundCommand.size() - 1);		
	}
	
	private void checkCommand(ModelChangeCompoundCommand compoundCommand, MemberRole memberRole, 
							  int beginIndex, int endIndex) {
		
		for (int i = beginIndex; i <= endIndex; i++) {
			ModelChangeBasicCommand command = 
				(ModelChangeBasicCommand) compoundCommand.getCommands().get(i);			
			checkCommand(command, memberRole);
		}		
	}
	
	private void checkCommand(ModelChangeBasicCommand command, MemberRole memberRole) {
		if (command instanceof DeleteBendpointCommand) {
			assertTrue(!memberRole.getConnectionParts().get(0).getBendpointLocations().isEmpty() ||
					    memberRole.getConnectionParts().size() > 1 &&
					    !memberRole.getConnectionParts().get(1).getBendpointLocations().isEmpty());
			
			ConnectionPart commandConnectionPart = getAnnotatedFieldValue(command, Owner.class);
			assertSame(memberRole, commandConnectionPart.getMemberRole());
			int commandConnectionPartIndex = getFieldValue(command, "connectionPartIndex");
			assertEquals(0, commandConnectionPartIndex);
		} else if (command instanceof DeleteConnectorsCommand) {			
			assertEquals(2, memberRole.getConnectionParts().size());
			MemberRole commandMemberRole = getAnnotatedFieldValue(command, Owner.class);
			assertSame(memberRole, commandMemberRole);
		} else if (command instanceof DeleteSetCommand) {
			assertNull(memberRole.getSet().getSystemOwner());
			Set set = getAnnotatedFieldValue(command, Item.class);
			assertSame(memberRole.getSet(), set);
		} else if (command instanceof DeleteIndexCommand) {
			assertSame(SetMode.INDEXED, memberRole.getSet().getMode());
			assertNotNull(memberRole.getSet().getSystemOwner());
			SystemOwner systemOwner = getFieldValue(command, "systemOwner");  // not annotated
			assertSame(memberRole.getSet().getSystemOwner(), systemOwner);
		} else if (command instanceof MakeRecordDirectCommand) {
			assertSame(LocationMode.VIA, memberRole.getRecord().getLocationMode());
			SchemaRecord record = getAnnotatedFieldValue(command, Owner.class);
			assertSame(memberRole.getRecord(), record);
		} else if (command instanceof RemoveMemberFromSetCommand) {
			MemberRole commandMemberRole = getAnnotatedFieldValue(command, Item.class);
			assertSame(memberRole, commandMemberRole);
		} else {
			fail("unexpected command: " + command.getClass().getSimpleName());
		}
	}

	private <T, U extends Annotation> T getAnnotatedFieldValue(Command command, 
															   Class<U> annotationClass) {
		T value = 
			ModelChangeDispatcher.getAnnotatedFieldValue(command, annotationClass, 
														 ModelChangeDispatcher.Availability.MANDATORY);
		return value;
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getFieldValue(Command command, String fieldName) {
		try {
			Field field = command.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return (T) field.get(command);
		} catch (Throwable t) {
			if (t instanceof NoSuchFieldException) {
				try {
					// go look in the command's direct super class
					Field field = command.getClass().getSuperclass().getDeclaredField(fieldName);
					field.setAccessible(true);
					return (T) field.get(command);
				} catch (Throwable t1) {
					fail(t1.getClass().getSimpleName() + ": " + t.getMessage());			
				}			
			}
			fail(t.getClass().getSimpleName() + ": " + t.getMessage());			
		}
		return null; // not reached because of fail
	}

	@Before
	public void setup() {
		// we'll use EMPSCHM throughout these tests
		schema = TestTools.getEmpschmSchema();
	}
	
	@Test
	public void test_Index_SimpleCommand() {		
		
		// a simple (delete index) command will be returned when the member record isn't stored VIA 
		// the index to be deleted AND when no connectors are present on the line between the index
		// figure and the member record		
		MemberRole memberRole = schema.getSet("JOB-TITLE-NDX").getMembers().get(0);		
		ModelChangeBasicCommand command = 
			(ModelChangeBasicCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);
		assertTrue(command instanceof DeleteIndexCommand);
		checkCommand(command, memberRole);
	
		
		// the same should happen when passing the set and not the member role
		command = 
			(ModelChangeBasicCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole.getSet());
		assertTrue(command instanceof DeleteIndexCommand);
		checkCommand(command, memberRole);
		
	}
	
	@Test
	public void test_Index_CompoundCommandWithDeleteConnectors() {		
		
		// a compound command composed of (only) a delete connectors and delete index command will 
		// be returned when connectors are present on the line between the index figure and the 
		// member record and the member record isn't stored VIA the index
		
		// setup test context: create the connectors first
		MemberRole memberRole = schema.getSet("JOB-TITLE-NDX").getMembers().get(0);
		Command createConnectorCommand = new CreateConnectorCommand(memberRole, new Point(0, 0));
		createConnectorCommand.execute();
		
		// have the compound created and verify		
		ModelChangeCompoundCommand compoundCommand = 
			(ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);
		assertEquals("Delete index", compoundCommand.getLabel());
		assertEquals(2, compoundCommand.getCommands().size());
		assertTrue(compoundCommand.getCommands().get(0) instanceof DeleteConnectorsCommand);
		assertTrue(compoundCommand.getCommands().get(1) instanceof DeleteIndexCommand);
		
		checkCommand(compoundCommand, memberRole);
		
		
		// the same should happen when passing the set and not the member role
		compoundCommand = 
			(ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole.getSet());
		assertEquals("Delete index", compoundCommand.getLabel());
		assertEquals(2, compoundCommand.getCommands().size());
		assertTrue(compoundCommand.getCommands().get(0) instanceof DeleteConnectorsCommand);
		assertTrue(compoundCommand.getCommands().get(1) instanceof DeleteIndexCommand);
		
		checkCommand(compoundCommand, memberRole);		
		
		
	}
	
	@Test
	public void test_Index_CompoundCommandWithDeleteBendpoints_WithoutConnectors() {	
		
		MemberRole memberRole = schema.getSet("JOB-TITLE-NDX").getMembers().get(0);
		assertEquals(1, memberRole.getConnectionParts().size());
		assertTrue(memberRole.getConnectionParts().get(0).getBendpointLocations().isEmpty());
		Command tmpCommand = 
			new CreateBendpointCommand(memberRole.getConnectionParts().get(0), 0, 0, 0);
		tmpCommand.execute();		
		assertEquals(1, memberRole.getConnectionParts().get(0).getBendpointLocations().size());
		
		ModelChangeCompoundCommand cc = 
			(ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);
		
		assertEquals(2, cc.getCommands().size());
		assertTrue(cc.getCommands().get(0) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(1) instanceof DeleteIndexCommand);
		
		checkCommand(cc, memberRole);
		
		
		// the same should happen when passing the set and not the member role
		cc = (ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole.getSet());
		
		assertEquals(2, cc.getCommands().size());
		assertTrue(cc.getCommands().get(0) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(1) instanceof DeleteIndexCommand);
		
		checkCommand(cc, memberRole);		
		
	}

	@Test
	public void test_Index_CompoundCommandWithDeleteBendpoints_WithConnectors() {	
		
		MemberRole memberRole = schema.getSet("JOB-TITLE-NDX").getMembers().get(0);
		assertEquals(1, memberRole.getConnectionParts().size());
		assertTrue(memberRole.getConnectionParts().get(0).getBendpointLocations().isEmpty());
		
		Command tmpCommand1 = new CreateConnectorCommand(memberRole, new Point(0, 0));
		tmpCommand1.execute();
		assertEquals(2, memberRole.getConnectionParts().size());
		Command tmpCommand2 = 
			new CreateBendpointCommand(memberRole.getConnectionParts().get(0), 0, 0, 0);
		tmpCommand2.execute();		
		assertEquals(1, memberRole.getConnectionParts().get(0).getBendpointLocations().size());		
		Command tmpCommand3 = 
			new CreateBendpointCommand(memberRole.getConnectionParts().get(1), 0, 0, 0);
		tmpCommand3.execute();		
		assertEquals(1, memberRole.getConnectionParts().get(1).getBendpointLocations().size());
		
		ModelChangeCompoundCommand cc = 
			(ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);
		
		assertEquals(4, cc.getCommands().size());
		assertTrue(cc.getCommands().get(0) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(1) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(2) instanceof DeleteConnectorsCommand);
		assertTrue(cc.getCommands().get(3) instanceof DeleteIndexCommand);
		
		checkCommand(cc, memberRole);
		
		
		// the same should happen when passing the set and not the member role
		cc = (ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole.getSet());
		
		assertEquals(4, cc.getCommands().size());
		assertTrue(cc.getCommands().get(0) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(1) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(2) instanceof DeleteConnectorsCommand);
		assertTrue(cc.getCommands().get(3) instanceof DeleteIndexCommand);
		
		checkCommand(cc, memberRole);
		
	}

	@Test
	public void test_Index_CompoundCommandWithChangeLocationModeOfMember() {		
		
		// a compound command composed of (only) a make record DIRECT and delete index command will 
		// be returned when connectors are NOT present on the line between the index figure and the 
		// member record and the member record IS stored VIA the index
		
		// setup test context: change the record's location mode to VIA (VIA set: index); we need to
		// do this in 2 steps because we cannot change a CALC record directly to a VIA record but 
		// need to make it DIRECT first
		SchemaRecord memberRecord = schema.getRecord("JOB");
		Command makeRecordDirectCommand = new MakeRecordDirectCommand(memberRecord);
		makeRecordDirectCommand.execute();
		Command makeRecordViaCommand = 
			new MakeRecordViaCommand(memberRecord, "JOB-TITLE-NDX", null, null);
		makeRecordViaCommand.execute();		
		
		// have the compound created and verify		
		MemberRole memberRole = schema.getSet("JOB-TITLE-NDX").getMembers().get(0);
		ModelChangeCompoundCommand compoundCommand = 
			(ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);
		assertEquals("Delete index", compoundCommand.getLabel());
		assertEquals(2, compoundCommand.getCommands().size());
		assertTrue(compoundCommand.getCommands().get(0) instanceof MakeRecordDirectCommand);
		assertTrue(compoundCommand.getCommands().get(1) instanceof DeleteIndexCommand);
		
		checkCommand(compoundCommand, memberRole);
		
		
		// the same should happen when passing the set and not the member role
		compoundCommand = 
			(ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole.getSet());
		assertEquals("Delete index", compoundCommand.getLabel());
		assertEquals(2, compoundCommand.getCommands().size());
		assertTrue(compoundCommand.getCommands().get(0) instanceof MakeRecordDirectCommand);
		assertTrue(compoundCommand.getCommands().get(1) instanceof DeleteIndexCommand);
		
		checkCommand(compoundCommand, memberRole);
		
	}	
	
	@Test
	public void test_Index_CompoundCommandWithEverything() {				
		
		// goal: a compound command composed of delete bendpoints, delete connectors, make record  
		// DIRECT and delete index commands
		
		// setup test context: change the record's location mode to VIA (VIA set: index); we need to
		// do this in 2 steps because we cannot change a CALC record directly to a VIA record but 
		// need to make it DIRECT first - subsequently, add the connectors and bendpoints
		MemberRole memberRole = schema.getSet("JOB-TITLE-NDX").getMembers().get(0);
		SchemaRecord memberRecord = schema.getRecord("JOB");
		Command makeRecordDirectCommand = new MakeRecordDirectCommand(memberRecord);
		makeRecordDirectCommand.execute();
		Command makeRecordViaCommand = 
			new MakeRecordViaCommand(memberRecord, "JOB-TITLE-NDX", null, null);
		makeRecordViaCommand.execute();
		assertSame(LocationMode.VIA, memberRecord.getLocationMode());
		assertSame(memberRole.getSet(), memberRecord.getViaSpecification().getSet());		
		Command createConnectorCommand = new CreateConnectorCommand(memberRole, new Point(0, 0));
		createConnectorCommand.execute();
		assertEquals(2, memberRole.getConnectionParts().size());
		Command createBendpointCommand1 = 
			new CreateBendpointCommand(memberRole.getConnectionParts().get(0), 0, 0, 0);
		createBendpointCommand1.execute();
		Command createBendpointCommand2 = 
			new CreateBendpointCommand(memberRole.getConnectionParts().get(1), 0, 0, 0);
		createBendpointCommand2.execute();
		assertEquals(1, memberRole.getConnectionParts().get(0).getBendpointLocations().size());
		assertEquals(1, memberRole.getConnectionParts().get(1).getBendpointLocations().size());
		
		// have the compound created and verify		
		ModelChangeCompoundCommand compoundCommand = 
			(ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);
		assertEquals("Delete index", compoundCommand.getLabel());
		assertEquals(5, compoundCommand.getCommands().size());
		assertTrue(compoundCommand.getCommands().get(0) instanceof DeleteBendpointCommand);
		assertTrue(compoundCommand.getCommands().get(1) instanceof DeleteBendpointCommand);
		assertTrue(compoundCommand.getCommands().get(2) instanceof DeleteConnectorsCommand);
		assertTrue(compoundCommand.getCommands().get(3) instanceof MakeRecordDirectCommand);
		assertTrue(compoundCommand.getCommands().get(4) instanceof DeleteIndexCommand);
		
		checkCommand(compoundCommand, memberRole);
		
		
		// the same should happen when passing the set and not the member role
		compoundCommand = 
			(ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole.getSet());
		assertEquals("Delete index", compoundCommand.getLabel());
		assertEquals(5, compoundCommand.getCommands().size());
		assertTrue(compoundCommand.getCommands().get(0) instanceof DeleteBendpointCommand);
		assertTrue(compoundCommand.getCommands().get(1) instanceof DeleteBendpointCommand);
		assertTrue(compoundCommand.getCommands().get(2) instanceof DeleteConnectorsCommand);
		assertTrue(compoundCommand.getCommands().get(3) instanceof MakeRecordDirectCommand);
		assertTrue(compoundCommand.getCommands().get(4) instanceof DeleteIndexCommand);
		
		checkCommand(compoundCommand, memberRole);
		
	}
	
	@Test
	public void test_SingleMemberSet_SimpleCommand() {		
		
		MemberRole memberRole = schema.getSet("JOB-EMPOSITION").getMembers().get(0);		
		ModelChangeBasicCommand command = 
			(ModelChangeBasicCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);
		assertTrue(command instanceof DeleteSetCommand);
		checkCommand(command, memberRole);
		
		
		// the same should happen when passing the set and not the member role
		command = 
			(ModelChangeBasicCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole.getSet());
		assertTrue(command instanceof DeleteSetCommand);
		checkCommand(command, memberRole);
		
	}
	
	@Test
	public void test_SingleMemberSet_CompoundCommandWithDeleteConnectors() {				
		
		MemberRole memberRole = schema.getSet("JOB-EMPOSITION").getMembers().get(0);		
		Command tmpCommand = new CreateConnectorCommand(memberRole, new Point(0, 0));
		tmpCommand.execute();
		assertEquals(2, memberRole.getConnectionParts().size());
		
		ModelChangeCompoundCommand cc = 
			(ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);
		assertEquals(2, cc.getCommands().size());
		assertTrue(cc.getCommands().get(0) instanceof DeleteConnectorsCommand);
		assertTrue(cc.getCommands().get(1) instanceof DeleteSetCommand);
		
		checkCommand(cc, memberRole);
		
		
		// the same should happen when passing the set and not the member role
		cc = (ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole.getSet());
		assertEquals(2, cc.getCommands().size());
		assertTrue(cc.getCommands().get(0) instanceof DeleteConnectorsCommand);
		assertTrue(cc.getCommands().get(1) instanceof DeleteSetCommand);
		
		checkCommand(cc, memberRole);
		
	}
	
	@Test
	public void test_SingleMemberSet_CompoundCommandWithDeleteBendpoints_WithoutConnectors() {	
		
		MemberRole memberRole = schema.getSet("JOB-EMPOSITION").getMembers().get(0);		
		assertEquals(1, memberRole.getConnectionParts().size());
		Command tmpCommand = 
			new CreateBendpointCommand(memberRole.getConnectionParts().get(0), 0, 0, 0);
		tmpCommand.execute();
		assertEquals(1, memberRole.getConnectionParts().get(0).getBendpointLocations().size());
		
		ModelChangeCompoundCommand cc = 
			(ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);
		assertEquals(2, cc.getCommands().size());
		assertTrue(cc.getCommands().get(0) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(1) instanceof DeleteSetCommand);
		
		checkCommand(cc, memberRole);
		
		
		// the same should happen when passing the set and not the member role
		cc = (ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole.getSet());
		assertEquals(2, cc.getCommands().size());
		assertTrue(cc.getCommands().get(0) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(1) instanceof DeleteSetCommand);
		
		checkCommand(cc, memberRole);		
		
	}

	@Test
	public void test_SingleMemberSet_CompoundCommandWithDeleteBendpoints_WithConnectors() {	
		
		MemberRole memberRole = schema.getSet("JOB-EMPOSITION").getMembers().get(0);		
		assertEquals(1, memberRole.getConnectionParts().size());
		Command tmpCommand1 =
			new CreateConnectorCommand(memberRole, new Point(0, 0));
		tmpCommand1.execute();
		assertEquals(2, memberRole.getConnectionParts().size());
		Command tmpCommand2 = 
			new CreateBendpointCommand(memberRole.getConnectionParts().get(0), 0, 0, 0);
		tmpCommand2.execute();
		assertEquals(1, memberRole.getConnectionParts().get(0).getBendpointLocations().size());
		Command tmpCommand3 = 
			new CreateBendpointCommand(memberRole.getConnectionParts().get(1), 0, 0, 0);
		tmpCommand3.execute();
		assertEquals(1, memberRole.getConnectionParts().get(1).getBendpointLocations().size());
		
		ModelChangeCompoundCommand cc = 
			(ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);
		assertEquals(4, cc.getCommands().size());
		assertTrue(cc.getCommands().get(0) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(1) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(2) instanceof DeleteConnectorsCommand);
		assertTrue(cc.getCommands().get(3) instanceof DeleteSetCommand);
		
		checkCommand(cc, memberRole);
		
		
		// the same should happen when passing the set and not the member role
		cc = (ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole.getSet());
		assertEquals(4, cc.getCommands().size());
		assertTrue(cc.getCommands().get(0) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(1) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(2) instanceof DeleteConnectorsCommand);
		assertTrue(cc.getCommands().get(3) instanceof DeleteSetCommand);
		
		checkCommand(cc, memberRole);
		
	}

	@Test
	public void test_SingleMemberSet_CompoundCommandWithChangeLocationModeOfMember() {				
		
		MemberRole memberRole = schema.getSet("EMP-COVERAGE").getMembers().get(0);
		assertSame(LocationMode.VIA, memberRole.getRecord().getLocationMode());
		assertSame(memberRole.getSet(), memberRole.getRecord().getViaSpecification().getSet());
		assertEquals(1, memberRole.getConnectionParts().size());
		assertTrue(memberRole.getConnectionParts().get(0).getBendpointLocations().isEmpty());
		
		ModelChangeCompoundCommand cc = 
			(ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);
		assertEquals(2, cc.getCommands().size());
		assertTrue(cc.getCommands().get(0) instanceof MakeRecordDirectCommand);
		assertTrue(cc.getCommands().get(1) instanceof DeleteSetCommand);
		
		checkCommand(cc, memberRole);
		
		
		// the same should happen when passing the set and not the member role
		cc = (ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole.getSet());
		assertEquals(2, cc.getCommands().size());
		assertTrue(cc.getCommands().get(0) instanceof MakeRecordDirectCommand);
		assertTrue(cc.getCommands().get(1) instanceof DeleteSetCommand);
		
		checkCommand(cc, memberRole);		
		
	}	
	
	@Test
	public void test_SingleMemberSet_CompoundCommandWithEverything() {				
		
		MemberRole memberRole = schema.getSet("MANAGES").getMembers().get(0);
		assertSame(LocationMode.VIA, memberRole.getRecord().getLocationMode());
		assertSame(memberRole.getSet(), memberRole.getRecord().getViaSpecification().getSet());
		assertEquals(1, memberRole.getConnectionParts().size());		
		
		Command tmpCommand = new CreateConnectorCommand(memberRole, new Point(0, 0));
		tmpCommand.execute();
		assertEquals(2, memberRole.getConnectionParts().size());
		assertEquals(2, memberRole.getConnectionParts().get(0).getBendpointLocations().size());
		assertEquals(0, memberRole.getConnectionParts().get(1).getBendpointLocations().size());
		
		ModelChangeCompoundCommand cc = 
			(ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);
		assertEquals(5, cc.getCommands().size());
		assertTrue(cc.getCommands().get(0) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(1) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(2) instanceof DeleteConnectorsCommand);		
		assertTrue(cc.getCommands().get(3) instanceof MakeRecordDirectCommand);
		assertTrue(cc.getCommands().get(4) instanceof DeleteSetCommand);
		
		checkCommand(cc, memberRole);
		
		
		// the same should happen when passing the set and not the member role
		cc = (ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole.getSet());
		assertEquals(5, cc.getCommands().size());
		assertTrue(cc.getCommands().get(0) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(1) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(2) instanceof DeleteConnectorsCommand);		
		assertTrue(cc.getCommands().get(3) instanceof MakeRecordDirectCommand);
		assertTrue(cc.getCommands().get(4) instanceof DeleteSetCommand);
		
		checkCommand(cc, memberRole);				
		
	}
	
	@Test
	public void test_MultipleMemberSet_RemoveMember_SimpleCommand() {		
		
		// goal: remove record NON-HOSP-CLAIM as a member from set COVERAGE-CLAIMS; first change the
		// existing schema so that we get a simple command that accomplishes this
		Set set = schema.getSet("COVERAGE-CLAIMS");
		assertEquals(3, set.getMembers().size());
		assertEquals("HOSPITAL-CLAIM", set.getMembers().get(0).getRecord().getName());
		MemberRole memberRole = set.getMembers().get(1);
		assertEquals("NON-HOSP-CLAIM", memberRole.getRecord().getName());
		assertEquals(1, memberRole.getConnectionParts().size());
		assertEquals(2, memberRole.getConnectionParts().get(0).getBendpointLocations().size());
		Command tmpCommand1 = new DeleteBendpointCommand(memberRole.getConnectionParts().get(0), 0);
		tmpCommand1.execute();
		Command tmpCommand2 = new DeleteBendpointCommand(memberRole.getConnectionParts().get(0), 0);
		tmpCommand2.execute();
		assertSame(LocationMode.VIA, memberRole.getRecord().getLocationMode());
		Command tmpCommand3 = new MakeRecordDirectCommand(memberRole.getRecord());
		tmpCommand3.execute();
		assertSame(LocationMode.DIRECT, memberRole.getRecord().getLocationMode());
		assertTrue(memberRole.getConnectionParts().get(0).getBendpointLocations().isEmpty());
		assertEquals("DENTAL-CLAIM", set.getMembers().get(2).getRecord().getName());
		
		ModelChangeBasicCommand command = 
			(ModelChangeBasicCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);
		assertTrue(command instanceof RemoveMemberFromSetCommand);
		
		checkCommand(command, memberRole);
		
	}
	
	@Test
	public void test_MultipleMemberSet_RemoveMember_CompoundCommandWithDeleteConnectors() {				
		
		// goal: remove record NON-HOSP-CLAIM as a member from set COVERAGE-CLAIMS; first change the
		// existing schema so that we get the rightly assembled compound command that accomplishes 
		// this
		Set set = schema.getSet("COVERAGE-CLAIMS");
		assertEquals(3, set.getMembers().size());
		assertEquals("HOSPITAL-CLAIM", set.getMembers().get(0).getRecord().getName());
		MemberRole memberRole = set.getMembers().get(1);
		assertEquals("NON-HOSP-CLAIM", memberRole.getRecord().getName());
		assertEquals(1, memberRole.getConnectionParts().size());
		assertEquals(2, memberRole.getConnectionParts().get(0).getBendpointLocations().size());
		Command tmpCommand1 = new DeleteBendpointCommand(memberRole.getConnectionParts().get(0), 0);
		tmpCommand1.execute();
		Command tmpCommand2 = new DeleteBendpointCommand(memberRole.getConnectionParts().get(0), 0);
		tmpCommand2.execute();
		assertSame(LocationMode.VIA, memberRole.getRecord().getLocationMode());
		Command tmpCommand3 = new MakeRecordDirectCommand(memberRole.getRecord());
		tmpCommand3.execute();
		assertTrue(memberRole.getConnectionParts().get(0).getBendpointLocations().isEmpty());
		assertSame(LocationMode.DIRECT, memberRole.getRecord().getLocationMode());
		Command tmpCommand4 = new CreateConnectorCommand(memberRole, new Point(0, 0));
		tmpCommand4.execute();
		assertEquals(2, memberRole.getConnectionParts().size());
		assertEquals("DENTAL-CLAIM", set.getMembers().get(2).getRecord().getName());
		
		ModelChangeCompoundCommand cc = 
			(ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);
		assertEquals(2, cc.getCommands().size());
		assertTrue(cc.getCommands().get(0) instanceof DeleteConnectorsCommand);
		assertTrue(cc.getCommands().get(1) instanceof RemoveMemberFromSetCommand);
		
		checkCommand(cc, memberRole);
		
	}
	
	@Test
	public void test_MultipleMemberSet_RemoveMember_CompoundCommandWithDeleteBendpoints_WithoutConnectors() {	
		
		// goal: remove record NON-HOSP-CLAIM as a member from set COVERAGE-CLAIMS; first change the
		// existing schema so that we get the rightly assembled compound command that accomplishes 
		// this
		Set set = schema.getSet("COVERAGE-CLAIMS");
		assertEquals(3, set.getMembers().size());
		assertEquals("HOSPITAL-CLAIM", set.getMembers().get(0).getRecord().getName());
		MemberRole memberRole = set.getMembers().get(1);
		assertEquals("NON-HOSP-CLAIM", memberRole.getRecord().getName());
		assertEquals(1, memberRole.getConnectionParts().size());
		assertEquals(2, memberRole.getConnectionParts().get(0).getBendpointLocations().size());
		assertSame(LocationMode.VIA, memberRole.getRecord().getLocationMode());
		Command tmpCommand3 = new MakeRecordDirectCommand(memberRole.getRecord());
		tmpCommand3.execute();
		assertSame(LocationMode.DIRECT, memberRole.getRecord().getLocationMode());				
		Command tmpCommand4 = new CreateConnectorCommand(memberRole, new Point(0, 0));
		tmpCommand4.execute();
		assertEquals(2, memberRole.getConnectionParts().size());
		assertEquals("DENTAL-CLAIM", set.getMembers().get(2).getRecord().getName());
		
		ModelChangeCompoundCommand cc = 
			(ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);
		assertEquals(4, cc.getCommands().size());
		assertTrue(cc.getCommands().get(0) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(1) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(2) instanceof DeleteConnectorsCommand);
		assertTrue(cc.getCommands().get(3) instanceof RemoveMemberFromSetCommand);
		
		checkCommand(cc, memberRole);		
		
	}

	@Test
	public void test_MultipleMemberSet_RemoveMember_CompoundCommandWithDeleteBendpoints_WithConnectors() {	
		
		// goal: remove record NON-HOSP-CLAIM as a member from set COVERAGE-CLAIMS; first change the
		// existing schema so that we get the rightly assembled compound command that accomplishes 
		// this
		Set set = schema.getSet("COVERAGE-CLAIMS");
		assertEquals(3, set.getMembers().size());
		assertEquals("HOSPITAL-CLAIM", set.getMembers().get(0).getRecord().getName());
		MemberRole memberRole = set.getMembers().get(1);
		assertEquals("NON-HOSP-CLAIM", memberRole.getRecord().getName());
		assertEquals(1, memberRole.getConnectionParts().size());
		assertEquals(2, memberRole.getConnectionParts().get(0).getBendpointLocations().size());
		assertSame(LocationMode.VIA, memberRole.getRecord().getLocationMode());
		Command tmpCommand3 = new MakeRecordDirectCommand(memberRole.getRecord());
		tmpCommand3.execute();
		assertSame(LocationMode.DIRECT, memberRole.getRecord().getLocationMode());				
		assertEquals("DENTAL-CLAIM", set.getMembers().get(2).getRecord().getName());
		
		ModelChangeCompoundCommand cc = 
			(ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);
		assertEquals(3, cc.getCommands().size());
		assertTrue(cc.getCommands().get(0) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(1) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(2) instanceof RemoveMemberFromSetCommand);
		
		checkCommand(cc, memberRole);		
		
	}

	@Test
	public void test_MultipleMemberSet_RemoveMember_CompoundCommandWithChangeLocationModeOfMember() {				
		
		// goal: remove record NON-HOSP-CLAIM as a member from set COVERAGE-CLAIMS; first change the
		// existing schema so that we get the rightly assembled compound command that accomplishes 
		// this
		Set set = schema.getSet("COVERAGE-CLAIMS");
		assertEquals(3, set.getMembers().size());
		assertEquals("HOSPITAL-CLAIM", set.getMembers().get(0).getRecord().getName());
		MemberRole memberRole = set.getMembers().get(1);
		assertEquals("NON-HOSP-CLAIM", memberRole.getRecord().getName());
		assertEquals(1, memberRole.getConnectionParts().size());		
		assertEquals(2, memberRole.getConnectionParts().get(0).getBendpointLocations().size());
		Command tmpCommand1 = new DeleteBendpointCommand(memberRole.getConnectionParts().get(0), 0);
		tmpCommand1.execute();
		Command tmpCommand2 = new DeleteBendpointCommand(memberRole.getConnectionParts().get(0), 0);
		tmpCommand2.execute();
		assertTrue(memberRole.getConnectionParts().get(0).getBendpointLocations().isEmpty());
		assertSame(LocationMode.VIA, memberRole.getRecord().getLocationMode());
		assertEquals("DENTAL-CLAIM", set.getMembers().get(2).getRecord().getName());
		
		ModelChangeCompoundCommand cc = 
			(ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);
		assertEquals(2, cc.getCommands().size());
		assertTrue(cc.getCommands().get(0) instanceof MakeRecordDirectCommand);
		assertTrue(cc.getCommands().get(1) instanceof RemoveMemberFromSetCommand);
		
		checkCommand(cc, memberRole);		
		
	}	
	
	@Test
	public void test_MultipleMemberSet_RemoveMember_CompoundCommandWithEverything() {				
		
		// goal: remove record NON-HOSP-CLAIM as a member from set COVERAGE-CLAIMS; first change the
		// existing schema so that we get the rightly assembled compound command that accomplishes 
		// this
		Set set = schema.getSet("COVERAGE-CLAIMS");
		assertEquals(3, set.getMembers().size());
		assertEquals("HOSPITAL-CLAIM", set.getMembers().get(0).getRecord().getName());
		MemberRole memberRole = set.getMembers().get(1);
		assertEquals("NON-HOSP-CLAIM", memberRole.getRecord().getName());
		assertEquals(1, memberRole.getConnectionParts().size());		
		assertEquals(2, memberRole.getConnectionParts().get(0).getBendpointLocations().size());		
		assertSame(LocationMode.VIA, memberRole.getRecord().getLocationMode());
		Command tmpCommand = new CreateConnectorCommand(memberRole, new Point(0, 0));
		tmpCommand.execute();
		assertEquals(2, memberRole.getConnectionParts().size());
		assertEquals("DENTAL-CLAIM", set.getMembers().get(2).getRecord().getName());
		
		ModelChangeCompoundCommand cc = 
			(ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);
		assertEquals(5, cc.getCommands().size());
		assertTrue(cc.getCommands().get(0) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(1) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(2) instanceof DeleteConnectorsCommand);
		assertTrue(cc.getCommands().get(3) instanceof MakeRecordDirectCommand);
		assertTrue(cc.getCommands().get(4) instanceof RemoveMemberFromSetCommand);
		
		checkCommand(cc, memberRole);		
		
	}	
	
	@Test
	public void test_MultipleMemberSet_DeleteSet_CompoundCommandWithDeleteConnectors() {				
		
		Set set = schema.getSet("COVERAGE-CLAIMS");
		assertEquals(3, set.getMembers().size());
		MemberRole memberRole1 = set.getMembers().get(0);
		assertEquals("HOSPITAL-CLAIM", memberRole1.getRecord().getName());
		MemberRole memberRole2 = set.getMembers().get(1);
		assertEquals("NON-HOSP-CLAIM", memberRole2.getRecord().getName());				
		MemberRole memberRole3 = set.getMembers().get(2);
		assertEquals("DENTAL-CLAIM", memberRole3.getRecord().getName());
		
		assertEquals(1, memberRole1.getConnectionParts().size());
		assertEquals(0, memberRole1.getConnectionParts().get(0).getBendpointLocations().size());					
		assertEquals(1, memberRole2.getConnectionParts().size());
		assertEquals(2, memberRole2.getConnectionParts().get(0).getBendpointLocations().size());
		assertEquals(1, memberRole3.getConnectionParts().size());
		assertEquals(2, memberRole3.getConnectionParts().get(0).getBendpointLocations().size());		
		
		Command tmpCommand1 = 
			new DeleteBendpointCommand(memberRole2.getConnectionParts().get(0), 0);
		tmpCommand1.execute();		
		Command tmpCommand2 = 
			new DeleteBendpointCommand(memberRole2.getConnectionParts().get(0), 0);
		tmpCommand2.execute();		
		Command tmpCommand3 = 
			new DeleteBendpointCommand(memberRole3.getConnectionParts().get(0), 0);
		tmpCommand3.execute();		
		Command tmpCommand4 = 
			new DeleteBendpointCommand(memberRole3.getConnectionParts().get(0), 0);
		tmpCommand4.execute();		
		assertEquals(0, memberRole1.getConnectionParts().get(0).getBendpointLocations().size());		
		assertEquals(0, memberRole2.getConnectionParts().get(0).getBendpointLocations().size());		
		assertEquals(0, memberRole3.getConnectionParts().get(0).getBendpointLocations().size());
		
		Command tmpCommand5 = new CreateConnectorCommand(memberRole1, new Point(0, 0));
		tmpCommand5.execute();
		assertEquals(2, memberRole1.getConnectionParts().size());
		assertEquals(0, memberRole1.getConnectionParts().get(0).getBendpointLocations().size());
		assertEquals(0, memberRole1.getConnectionParts().get(1).getBendpointLocations().size());
		
		Command tmpCommand6 = new CreateConnectorCommand(memberRole2, new Point(0, 0));
		tmpCommand6.execute();
		assertEquals(2, memberRole2.getConnectionParts().size());
		assertEquals(0, memberRole2.getConnectionParts().get(0).getBendpointLocations().size());
		assertEquals(0, memberRole2.getConnectionParts().get(1).getBendpointLocations().size());
		
		Command tmpCommand7 = new CreateConnectorCommand(memberRole3, new Point(0, 0));
		tmpCommand7.execute();
		assertEquals(2, memberRole3.getConnectionParts().size());
		assertEquals(0, memberRole3.getConnectionParts().get(0).getBendpointLocations().size());
		assertEquals(0, memberRole3.getConnectionParts().get(1).getBendpointLocations().size());		
		
		assertSame(LocationMode.VIA, memberRole1.getRecord().getLocationMode());
		assertSame(set, memberRole1.getRecord().getViaSpecification().getSet());
		assertSame(LocationMode.VIA, memberRole2.getRecord().getLocationMode());
		assertSame(set, memberRole2.getRecord().getViaSpecification().getSet());
		assertSame(LocationMode.VIA, memberRole3.getRecord().getLocationMode());
		assertSame(set, memberRole3.getRecord().getViaSpecification().getSet());
		
		Command tmpCommand8 = new MakeRecordDirectCommand(memberRole1.getRecord());
		tmpCommand8.execute();
		Command tmpCommand9 = new MakeRecordDirectCommand(memberRole2.getRecord());
		tmpCommand9.execute();
		Command tmpCommand10 = new MakeRecordDirectCommand(memberRole3.getRecord());
		tmpCommand10.execute();
		
		assertSame(LocationMode.DIRECT, memberRole1.getRecord().getLocationMode());		
		assertSame(LocationMode.DIRECT, memberRole2.getRecord().getLocationMode());		
		assertSame(LocationMode.DIRECT, memberRole3.getRecord().getLocationMode());		
		
		ModelChangeCompoundCommand cc = 
			(ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(set);
		assertEquals(6, cc.getCommands().size());
		assertTrue(cc.getCommands().get(0) instanceof DeleteConnectorsCommand);				
		assertTrue(cc.getCommands().get(1) instanceof RemoveMemberFromSetCommand);
		assertTrue(cc.getCommands().get(2) instanceof DeleteConnectorsCommand);
		assertTrue(cc.getCommands().get(3) instanceof RemoveMemberFromSetCommand);
		assertTrue(cc.getCommands().get(4) instanceof DeleteConnectorsCommand);
		assertTrue(cc.getCommands().get(5) instanceof DeleteSetCommand);
		
		checkCommand(cc, memberRole1, 0, 1);
		checkCommand(cc, memberRole2, 2, 3);
		checkCommand(cc, memberRole3, 4, 5);		
		
	}
	
	@Test
	public void test_MultipleMemberSet_DeleteSet_CompoundCommandWithDeleteBendpoints_WithoutConnectors() {	
		
		Set set = schema.getSet("COVERAGE-CLAIMS");
		assertEquals(3, set.getMembers().size());
		MemberRole memberRole1 = set.getMembers().get(0);
		assertEquals("HOSPITAL-CLAIM", memberRole1.getRecord().getName());
		MemberRole memberRole2 = set.getMembers().get(1);
		assertEquals("NON-HOSP-CLAIM", memberRole2.getRecord().getName());				
		MemberRole memberRole3 = set.getMembers().get(2);
		assertEquals("DENTAL-CLAIM", memberRole3.getRecord().getName());
		
		assertEquals(1, memberRole1.getConnectionParts().size());
		assertEquals(0, memberRole1.getConnectionParts().get(0).getBendpointLocations().size());					
		assertEquals(1, memberRole2.getConnectionParts().size());
		assertEquals(2, memberRole2.getConnectionParts().get(0).getBendpointLocations().size());
		assertEquals(1, memberRole3.getConnectionParts().size());
		assertEquals(2, memberRole3.getConnectionParts().get(0).getBendpointLocations().size());		
		
		Command tmpCommand4 = 
			new CreateBendpointCommand(memberRole1.getConnectionParts().get(0), 0, 0, 0);
		tmpCommand4.execute();		
		assertEquals(1, memberRole1.getConnectionParts().get(0).getBendpointLocations().size());		
		assertEquals(2, memberRole2.getConnectionParts().get(0).getBendpointLocations().size());		
		assertEquals(2, memberRole3.getConnectionParts().get(0).getBendpointLocations().size());		
		
		assertSame(LocationMode.VIA, memberRole1.getRecord().getLocationMode());
		assertSame(set, memberRole1.getRecord().getViaSpecification().getSet());
		assertSame(LocationMode.VIA, memberRole2.getRecord().getLocationMode());
		assertSame(set, memberRole2.getRecord().getViaSpecification().getSet());
		assertSame(LocationMode.VIA, memberRole3.getRecord().getLocationMode());
		assertSame(set, memberRole3.getRecord().getViaSpecification().getSet());
		
		Command tmpCommand8 = new MakeRecordDirectCommand(memberRole1.getRecord());
		tmpCommand8.execute();
		Command tmpCommand9 = new MakeRecordDirectCommand(memberRole2.getRecord());
		tmpCommand9.execute();
		Command tmpCommand10 = new MakeRecordDirectCommand(memberRole3.getRecord());
		tmpCommand10.execute();
		
		assertSame(LocationMode.DIRECT, memberRole1.getRecord().getLocationMode());		
		assertSame(LocationMode.DIRECT, memberRole2.getRecord().getLocationMode());		
		assertSame(LocationMode.DIRECT, memberRole3.getRecord().getLocationMode());		
		
		ModelChangeCompoundCommand cc = 
			(ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(set);
		assertEquals(8, cc.getCommands().size());
		assertTrue(cc.getCommands().get(0) instanceof DeleteBendpointCommand);				
		assertTrue(cc.getCommands().get(1) instanceof RemoveMemberFromSetCommand);
		assertTrue(cc.getCommands().get(2) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(3) instanceof DeleteBendpointCommand);				
		assertTrue(cc.getCommands().get(4) instanceof RemoveMemberFromSetCommand);
		assertTrue(cc.getCommands().get(5) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(6) instanceof DeleteBendpointCommand);				
		assertTrue(cc.getCommands().get(7) instanceof DeleteSetCommand);
		
		checkCommand(cc, memberRole1, 0, 1);
		checkCommand(cc, memberRole2, 2, 4);
		checkCommand(cc, memberRole3, 5, 7);		
		
	}

	@Test
	public void test_MultipleMemberSet_DeleteSet_CompoundCommandWithDeleteBendpoints_WithConnectors() {	
		
		Set set = schema.getSet("COVERAGE-CLAIMS");
		assertEquals(3, set.getMembers().size());
		MemberRole memberRole1 = set.getMembers().get(0);
		assertEquals("HOSPITAL-CLAIM", memberRole1.getRecord().getName());
		MemberRole memberRole2 = set.getMembers().get(1);
		assertEquals("NON-HOSP-CLAIM", memberRole2.getRecord().getName());				
		MemberRole memberRole3 = set.getMembers().get(2);
		assertEquals("DENTAL-CLAIM", memberRole3.getRecord().getName());
		
		assertEquals(1, memberRole1.getConnectionParts().size());
		assertEquals(0, memberRole1.getConnectionParts().get(0).getBendpointLocations().size());					
		assertEquals(1, memberRole2.getConnectionParts().size());
		assertEquals(2, memberRole2.getConnectionParts().get(0).getBendpointLocations().size());
		assertEquals(1, memberRole3.getConnectionParts().size());
		assertEquals(2, memberRole3.getConnectionParts().get(0).getBendpointLocations().size());
		
		Command tmpCommand1 = new CreateConnectorCommand(memberRole1, new Point(0, 0));
		tmpCommand1.execute();
		assertEquals(2, memberRole1.getConnectionParts().size());
		assertEquals(0, memberRole1.getConnectionParts().get(0).getBendpointLocations().size());
		assertEquals(0, memberRole1.getConnectionParts().get(1).getBendpointLocations().size());
		
		Command tmpCommand2 = new CreateConnectorCommand(memberRole2, new Point(0, 0));
		tmpCommand2.execute();
		assertEquals(2, memberRole2.getConnectionParts().size());
		assertEquals(2, memberRole2.getConnectionParts().get(0).getBendpointLocations().size());
		assertEquals(0, memberRole2.getConnectionParts().get(1).getBendpointLocations().size());
		
		Command tmpCommand3 = new CreateConnectorCommand(memberRole3, new Point(0, 0));
		tmpCommand3.execute();
		assertEquals(2, memberRole3.getConnectionParts().size());
		assertEquals(0, memberRole3.getConnectionParts().get(0).getBendpointLocations().size());
		assertEquals(2, memberRole3.getConnectionParts().get(1).getBendpointLocations().size());
		
		Command tmpCommand4 = 
			new CreateBendpointCommand(memberRole1.getConnectionParts().get(0), 0, 0, 0);
		tmpCommand4.execute();
		Command tmpCommand5 = 
			new CreateBendpointCommand(memberRole1.getConnectionParts().get(1), 0, 0, 0);
		tmpCommand5.execute();		
		Command tmpCommand6 = 
			new CreateBendpointCommand(memberRole2.getConnectionParts().get(1), 0, 0, 0);
		tmpCommand6.execute();
		Command tmpCommand7 = 
			new CreateBendpointCommand(memberRole3.getConnectionParts().get(0), 0, 0, 0);
		tmpCommand7.execute();
		assertEquals(1, memberRole1.getConnectionParts().get(0).getBendpointLocations().size());
		assertEquals(1, memberRole1.getConnectionParts().get(1).getBendpointLocations().size());
		assertEquals(2, memberRole2.getConnectionParts().get(0).getBendpointLocations().size());
		assertEquals(1, memberRole2.getConnectionParts().get(1).getBendpointLocations().size());
		assertEquals(1, memberRole3.getConnectionParts().get(0).getBendpointLocations().size());
		assertEquals(2, memberRole3.getConnectionParts().get(1).getBendpointLocations().size());
		
		assertSame(LocationMode.VIA, memberRole1.getRecord().getLocationMode());
		assertSame(set, memberRole1.getRecord().getViaSpecification().getSet());
		assertSame(LocationMode.VIA, memberRole2.getRecord().getLocationMode());
		assertSame(set, memberRole2.getRecord().getViaSpecification().getSet());
		assertSame(LocationMode.VIA, memberRole3.getRecord().getLocationMode());
		assertSame(set, memberRole3.getRecord().getViaSpecification().getSet());
		
		Command tmpCommand8 = new MakeRecordDirectCommand(memberRole1.getRecord());
		tmpCommand8.execute();
		Command tmpCommand9 = new MakeRecordDirectCommand(memberRole2.getRecord());
		tmpCommand9.execute();
		Command tmpCommand10 = new MakeRecordDirectCommand(memberRole3.getRecord());
		tmpCommand10.execute();
		
		assertSame(LocationMode.DIRECT, memberRole1.getRecord().getLocationMode());		
		assertSame(LocationMode.DIRECT, memberRole2.getRecord().getLocationMode());		
		assertSame(LocationMode.DIRECT, memberRole3.getRecord().getLocationMode());		
		
		ModelChangeCompoundCommand cc = 
			(ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(set);
		assertEquals(14, cc.getCommands().size());
		assertTrue(cc.getCommands().get(0) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(1) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(2) instanceof DeleteConnectorsCommand);
		assertTrue(cc.getCommands().get(3) instanceof RemoveMemberFromSetCommand);
		assertTrue(cc.getCommands().get(4) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(5) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(6) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(7) instanceof DeleteConnectorsCommand);
		assertTrue(cc.getCommands().get(8) instanceof RemoveMemberFromSetCommand);
		assertTrue(cc.getCommands().get(9) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(10) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(11) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(12) instanceof DeleteConnectorsCommand);
		assertTrue(cc.getCommands().get(13) instanceof DeleteSetCommand);
		
		checkCommand(cc, memberRole1, 0, 3);
		checkCommand(cc, memberRole2, 4, 8);
		checkCommand(cc, memberRole3, 9, 13);
		
	}

	@Test
	public void test_MultipleMemberSet_DeleteSet_CompoundCommandWithChangeLocationModeOfMember() {				
		
		Set set = schema.getSet("COVERAGE-CLAIMS");
		assertEquals(3, set.getMembers().size());
		MemberRole memberRole1 = set.getMembers().get(0);
		assertEquals("HOSPITAL-CLAIM", memberRole1.getRecord().getName());
		MemberRole memberRole2 = set.getMembers().get(1);
		assertEquals("NON-HOSP-CLAIM", memberRole2.getRecord().getName());				
		MemberRole memberRole3 = set.getMembers().get(2);
		assertEquals("DENTAL-CLAIM", memberRole3.getRecord().getName());
		
		assertEquals(1, memberRole1.getConnectionParts().size());
		assertEquals(0, memberRole1.getConnectionParts().get(0).getBendpointLocations().size());					
		assertEquals(1, memberRole2.getConnectionParts().size());
		assertEquals(2, memberRole2.getConnectionParts().get(0).getBendpointLocations().size());
		assertEquals(1, memberRole3.getConnectionParts().size());
		assertEquals(2, memberRole3.getConnectionParts().get(0).getBendpointLocations().size());		
		
		Command tmpCommand1 = 
			new DeleteBendpointCommand(memberRole2.getConnectionParts().get(0), 0);
		tmpCommand1.execute();		
		Command tmpCommand2 = 
			new DeleteBendpointCommand(memberRole2.getConnectionParts().get(0), 0);
		tmpCommand2.execute();		
		Command tmpCommand3 = 
			new DeleteBendpointCommand(memberRole3.getConnectionParts().get(0), 0);
		tmpCommand3.execute();		
		Command tmpCommand4 = 
			new DeleteBendpointCommand(memberRole3.getConnectionParts().get(0), 0);
		tmpCommand4.execute();		
		assertEquals(0, memberRole1.getConnectionParts().get(0).getBendpointLocations().size());		
		assertEquals(0, memberRole2.getConnectionParts().get(0).getBendpointLocations().size());		
		assertEquals(0, memberRole3.getConnectionParts().get(0).getBendpointLocations().size());
		
		assertSame(LocationMode.VIA, memberRole1.getRecord().getLocationMode());
		assertSame(set, memberRole1.getRecord().getViaSpecification().getSet());
		assertSame(LocationMode.VIA, memberRole2.getRecord().getLocationMode());
		assertSame(set, memberRole2.getRecord().getViaSpecification().getSet());
		assertSame(LocationMode.VIA, memberRole3.getRecord().getLocationMode());
		assertSame(set, memberRole3.getRecord().getViaSpecification().getSet());
		
		ModelChangeCompoundCommand cc = 
			(ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(set);
		assertEquals(6, cc.getCommands().size());
		assertTrue(cc.getCommands().get(0) instanceof MakeRecordDirectCommand);				
		assertTrue(cc.getCommands().get(1) instanceof RemoveMemberFromSetCommand);
		assertTrue(cc.getCommands().get(2) instanceof MakeRecordDirectCommand);
		assertTrue(cc.getCommands().get(3) instanceof RemoveMemberFromSetCommand);
		assertTrue(cc.getCommands().get(4) instanceof MakeRecordDirectCommand);
		assertTrue(cc.getCommands().get(5) instanceof DeleteSetCommand);
		
		checkCommand(cc, memberRole1, 0, 1);
		checkCommand(cc, memberRole2, 2, 3);
		checkCommand(cc, memberRole3, 4, 5);		
		
	}	
	
	@Test
	public void test_MultipleMemberSet_DeleteSet_CompoundCommandWithEverything() {				
		
		Set set = schema.getSet("COVERAGE-CLAIMS");
		assertEquals(3, set.getMembers().size());
		MemberRole memberRole1 = set.getMembers().get(0);
		assertEquals("HOSPITAL-CLAIM", memberRole1.getRecord().getName());
		MemberRole memberRole2 = set.getMembers().get(1);
		assertEquals("NON-HOSP-CLAIM", memberRole2.getRecord().getName());				
		MemberRole memberRole3 = set.getMembers().get(2);
		assertEquals("DENTAL-CLAIM", memberRole3.getRecord().getName());
		
		assertEquals(1, memberRole1.getConnectionParts().size());
		assertEquals(0, memberRole1.getConnectionParts().get(0).getBendpointLocations().size());					
		assertEquals(1, memberRole2.getConnectionParts().size());
		assertEquals(2, memberRole2.getConnectionParts().get(0).getBendpointLocations().size());
		assertEquals(1, memberRole3.getConnectionParts().size());
		assertEquals(2, memberRole3.getConnectionParts().get(0).getBendpointLocations().size());
		
		Command tmpCommand1 = new CreateConnectorCommand(memberRole1, new Point(0, 0));
		tmpCommand1.execute();
		assertEquals(2, memberRole1.getConnectionParts().size());
		assertEquals(0, memberRole1.getConnectionParts().get(0).getBendpointLocations().size());
		assertEquals(0, memberRole1.getConnectionParts().get(1).getBendpointLocations().size());
		
		Command tmpCommand2 = new CreateConnectorCommand(memberRole2, new Point(0, 0));
		tmpCommand2.execute();
		assertEquals(2, memberRole2.getConnectionParts().size());
		assertEquals(2, memberRole2.getConnectionParts().get(0).getBendpointLocations().size());
		assertEquals(0, memberRole2.getConnectionParts().get(1).getBendpointLocations().size());
		
		Command tmpCommand3 = new CreateConnectorCommand(memberRole3, new Point(0, 0));
		tmpCommand3.execute();
		assertEquals(2, memberRole3.getConnectionParts().size());
		assertEquals(0, memberRole3.getConnectionParts().get(0).getBendpointLocations().size());
		assertEquals(2, memberRole3.getConnectionParts().get(1).getBendpointLocations().size());
		
		Command tmpCommand4 = 
			new CreateBendpointCommand(memberRole1.getConnectionParts().get(0), 0, 0, 0);
		tmpCommand4.execute();
		Command tmpCommand5 = 
			new CreateBendpointCommand(memberRole1.getConnectionParts().get(1), 0, 0, 0);
		tmpCommand5.execute();		
		Command tmpCommand6 = 
			new CreateBendpointCommand(memberRole2.getConnectionParts().get(1), 0, 0, 0);
		tmpCommand6.execute();
		Command tmpCommand7 = 
			new CreateBendpointCommand(memberRole3.getConnectionParts().get(0), 0, 0, 0);
		tmpCommand7.execute();
		assertEquals(1, memberRole1.getConnectionParts().get(0).getBendpointLocations().size());
		assertEquals(1, memberRole1.getConnectionParts().get(1).getBendpointLocations().size());
		assertEquals(2, memberRole2.getConnectionParts().get(0).getBendpointLocations().size());
		assertEquals(1, memberRole2.getConnectionParts().get(1).getBendpointLocations().size());
		assertEquals(1, memberRole3.getConnectionParts().get(0).getBendpointLocations().size());
		assertEquals(2, memberRole3.getConnectionParts().get(1).getBendpointLocations().size());
		
		assertSame(LocationMode.VIA, memberRole1.getRecord().getLocationMode());
		assertSame(set, memberRole1.getRecord().getViaSpecification().getSet());
		assertSame(LocationMode.VIA, memberRole2.getRecord().getLocationMode());
		assertSame(set, memberRole2.getRecord().getViaSpecification().getSet());
		assertSame(LocationMode.VIA, memberRole3.getRecord().getLocationMode());
		assertSame(set, memberRole3.getRecord().getViaSpecification().getSet());
		
		ModelChangeCompoundCommand cc = 
			(ModelChangeCompoundCommand) DeleteSetOrIndexCommandCreationAssistant.getCommand(set);
		assertEquals(17, cc.getCommands().size());
		assertTrue(cc.getCommands().get(0) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(1) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(2) instanceof DeleteConnectorsCommand);
		assertTrue(cc.getCommands().get(3) instanceof MakeRecordDirectCommand);
		assertTrue(cc.getCommands().get(4) instanceof RemoveMemberFromSetCommand);
		assertTrue(cc.getCommands().get(5) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(6) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(7) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(8) instanceof DeleteConnectorsCommand);
		assertTrue(cc.getCommands().get(9) instanceof MakeRecordDirectCommand);
		assertTrue(cc.getCommands().get(10) instanceof RemoveMemberFromSetCommand);
		assertTrue(cc.getCommands().get(11) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(12) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(13) instanceof DeleteBendpointCommand);
		assertTrue(cc.getCommands().get(14) instanceof DeleteConnectorsCommand);
		assertTrue(cc.getCommands().get(15) instanceof MakeRecordDirectCommand);
		assertTrue(cc.getCommands().get(16) instanceof DeleteSetCommand);
		
		checkCommand(cc, memberRole1, 0, 4);
		checkCommand(cc, memberRole2, 5, 10);
		checkCommand(cc, memberRole3, 11, 16);
		
	}	

}
