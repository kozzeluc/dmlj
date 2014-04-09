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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.junit.Before;
import org.junit.Test;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.testtool.TestTools;

public class DeleteSetOrIndexCommandCreationAssistantTest {

	private Schema schema;	
	
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
		Command command = DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);
		assertTrue(command instanceof DeleteIndexCommand);		
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
		Command command = DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);
		assertTrue(command instanceof CompoundCommand);	
		CompoundCommand compoundCommand = (CompoundCommand) command;
		assertEquals("Delete index", compoundCommand.getLabel());
		assertEquals(2, compoundCommand.getCommands().size());
		assertTrue(compoundCommand.getCommands().get(0) instanceof DeleteConnectorsCommand);
		assertTrue(compoundCommand.getCommands().get(1) instanceof DeleteIndexCommand);
		
	}
	
	@Test
	public void test_Index_CompoundCommandWithDeleteBendpoints_WithoutConnectors() {	
		fail("not yet implemented");
	}

	@Test
	public void test_Index_CompoundCommandWithDeleteBendpoints_WithConnectors() {	
		fail("not yet implemented");
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
		Command command = DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);
		assertTrue(command instanceof CompoundCommand);	
		CompoundCommand compoundCommand = (CompoundCommand) command;
		assertEquals("Delete index", compoundCommand.getLabel());
		assertEquals(2, compoundCommand.getCommands().size());
		assertTrue(compoundCommand.getCommands().get(0) instanceof MakeRecordDirectCommand);
		assertTrue(compoundCommand.getCommands().get(1) instanceof DeleteIndexCommand);
		
	}	
	
	@Test
	public void test_Index_CompoundCommandWithEverything() {		
		
		fail("test case incomplete: make sure bendpoints are removed as well");
		
		// a compound command composed of a delete connectors, make record DIRECT and delete index 
		// command will be returned when connectors are present on the line between the index figure  
		// and the member record and the member record is stored VIA the index
		
		// setup test context: change the record's location mode to VIA (VIA set: index); we need to
		// do this in 2 steps because we cannot change a CALC record directly to a VIA record but 
		// need to make it DIRECT first - subsequently, add the connectors
		SchemaRecord memberRecord = schema.getRecord("JOB");
		Command makeRecordDirectCommand = new MakeRecordDirectCommand(memberRecord);
		makeRecordDirectCommand.execute();
		Command makeRecordViaCommand = 
			new MakeRecordViaCommand(memberRecord, "JOB-TITLE-NDX", null, null);
		makeRecordViaCommand.execute();
		MemberRole memberRole = schema.getSet("JOB-TITLE-NDX").getMembers().get(0);
		Command createConnectorCommand = new CreateConnectorCommand(memberRole, new Point(0, 0));
		createConnectorCommand.execute();		
		
		// have the compound created and verify		
		Command command = DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);
		assertTrue(command instanceof CompoundCommand);	
		CompoundCommand compoundCommand = (CompoundCommand) command;
		assertEquals("Delete index", compoundCommand.getLabel());
		assertEquals(3, compoundCommand.getCommands().size());
		assertTrue(compoundCommand.getCommands().get(0) instanceof DeleteConnectorsCommand);
		assertTrue(compoundCommand.getCommands().get(1) instanceof MakeRecordDirectCommand);
		assertTrue(compoundCommand.getCommands().get(2) instanceof DeleteIndexCommand);
		
	}
	
	@Test
	public void test_SingleMemberSet_SimpleCommand() {		
		fail("not yet implemented");		
	}
	
	@Test
	public void test_SingleMemberSet_CompoundCommandWithDeleteConnectors() {				
		fail("not yet implemented");			
	}
	
	@Test
	public void test_SingleMemberSet_CompoundCommandWithDeleteBendpoints_WithoutConnectors() {	
		fail("not yet implemented");
	}

	@Test
	public void test_SingleMemberSet_CompoundCommandWithDeleteBendpoints_WithConnectors() {	
		fail("not yet implemented");
	}

	@Test
	public void test_SingleMemberSet_CompoundCommandWithChangeLocationModeOfMember() {				
		fail("not yet implemented");		
	}	
	
	@Test
	public void test_SingleMemberSet_CompoundCommandWithEverything() {				
		fail("not yet implemented");
	}
	
	@Test
	public void test_MultipleMemberSet_RemoveMember_SimpleCommand() {		
		fail("not yet implemented");		
	}
	
	@Test
	public void test_MultipleMemberSet_RemoveMember_CompoundCommandWithDeleteConnectors() {				
		fail("not yet implemented");			
	}
	
	@Test
	public void test_MultipleMemberSet_RemoveMember_CompoundCommandWithDeleteBendpoints_WithoutConnectors() {	
		fail("not yet implemented");
	}

	@Test
	public void test_MultipleMemberSet_RemoveMember_CompoundCommandWithDeleteBendpoints_WithConnectors() {	
		fail("not yet implemented");
	}

	@Test
	public void test_MultipleMemberSet_RemoveMember_CompoundCommandWithChangeLocationModeOfMember() {				
		fail("not yet implemented");		
	}	
	
	@Test
	public void test_MultipleMemberSet_RemoveMember_CompoundCommandWithEverything() {				
		fail("not yet implemented");
	}
	
	@Test
	public void test_MultipleMemberSet_DeleteSet_SimpleCommand() {		
		fail("not yet implemented");		
	}
	
	@Test
	public void test_MultipleMemberSet_DeleteSet_CompoundCommandWithDeleteConnectors() {				
		fail("not yet implemented");			
	}
	
	@Test
	public void test_MultipleMemberSet_DeleteSet_CompoundCommandWithDeleteBendpoints_WithoutConnectors() {	
		fail("not yet implemented");
	}

	@Test
	public void test_MultipleMemberSet_DeleteSet_CompoundCommandWithDeleteBendpoints_WithConnectors() {	
		fail("not yet implemented");
	}

	@Test
	public void test_MultipleMemberSet_DeleteSet_CompoundCommandWithChangeLocationModeOfMember() {				
		fail("not yet implemented");		
	}	
	
	@Test
	public void test_MultipleMemberSet_DeleteSet_CompoundCommandWithEverything() {				
		fail("not yet implemented");
	}	

}
