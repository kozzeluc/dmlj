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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.lh.dmlj.schema.editor.testtool.TestTools.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.junit.Test;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMembershipOption;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.Usage;
import org.lh.dmlj.schema.editor.prefix.Pointer;
import org.lh.dmlj.schema.editor.prefix.PointerDescription;
import org.lh.dmlj.schema.editor.prefix.PointerType;
import org.lh.dmlj.schema.editor.prefix.PrefixFactory;
import org.lh.dmlj.schema.editor.prefix.PrefixUtil;
import org.lh.dmlj.schema.editor.testtool.ObjectGraph;
import org.lh.dmlj.schema.editor.testtool.TestTools;
import org.lh.dmlj.schema.editor.testtool.Xmi;

public class SwapRecordElementsCommandCreationAssistantTest {
	
	private static List<Element> getSingleElementList() {
		
		Element element = SchemaFactory.eINSTANCE.createElement();
		element.setName("ELEMENT-1");
		element.setLevel((short) 2);
		element.setPicture("X(8)");
		element.setUsage(Usage.DISPLAY);
		
		List<Element> elements = new ArrayList<>();
		elements.add(element);
		
		return elements;
	}
	
	private static List<Element> getSingleElementList(String elementName) {		
		List<Element> elements = getSingleElementList();
		elements.get(0).setName(elementName);		
		return elements;
	}

	private static ISortKeyDescription getSortKeyDescriptionForFirstElement(final SchemaRecord record) {
	
		return new ISortKeyDescription() {
	
			@Override
			public String[] getElementNames() {
				List<String> elementNames = new ArrayList<>();
				elementNames.add(record.getRootElements().get(0).getName());
				return elementNames.toArray(new String[] {});
			}
	
			@Override
			public SortSequence[] getSortSequences() {
				return new SortSequence[] {SortSequence.ASCENDING};
			}
	
			@Override
			public DuplicatesOption getDuplicatesOption() {
				return DuplicatesOption.NOT_ALLOWED;
			}
	
			@Override
			public boolean isCompressed() {
				return false;
			}
	
			@Override
			public boolean isNaturalSequence() {
				return false;
			}
	
		};
	}

	private ISortKeyDescription[] getSortKeyDescriptionsForFirstElements(Set set) {
		ISortKeyDescription[] sortKeyDescriptions = new ISortKeyDescription[set.getMembers().size()];
		for (int i = 0; i < sortKeyDescriptions.length; i++) {
			MemberRole memberRole = set.getMembers().get(i);
			sortKeyDescriptions[i] = getSortKeyDescriptionForFirstElement(memberRole.getRecord());
		}
		return sortKeyDescriptions;
	}

	@Test
	public void testSingleCommand() {
		
		Schema schema = TestTools.getEmpschmSchema();
		SchemaRecord record = schema.getRecord("COVERAGE");
		List<Element> originalRootElements = new ArrayList<>(record.getRootElements());
		assertEquals(4, originalRootElements.size());
		List<Element> newRootElements = getSingleElementList();
		Command command = 
			(Command) SwapRecordElementsCommandCreationAssistant.getCommand(record, newRootElements);
		assertTrue(command instanceof SwapRecordElementsCommand);
		
		// check the swap record elements command
		SwapRecordElementsCommand swapRecordElementsCommand = (SwapRecordElementsCommand) command;
		assertSame(record, swapRecordElementsCommand.record);
		assertEquals(1, swapRecordElementsCommand.newRootElements.size());
		assertSame(newRootElements.get(0), swapRecordElementsCommand.newRootElements.get(0));
		
		// execute(), undo() and redo() the command
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);
		command.execute();
		ObjectGraph objectGraph1 = TestTools.asObjectGraph(schema);
		Xmi xmi1 = TestTools.asXmi(schema);		
		command.undo();
		assertEquals(objectGraph, TestTools.asObjectGraph(schema));
		assertEquals(xmi, TestTools.asXmi(schema));		
		command.redo();
		assertEquals(objectGraph1, TestTools.asObjectGraph(schema));
		assertEquals(xmi1, TestTools.asXmi(schema));
		
		// verify that the command did what it had to do; in this case, only the record's elements
		// should have been replaced since the record has a location mode of VIA and does not 
		// participate as a member in any sorted set
		SchemaRecord coverage = schema.getRecord("COVERAGE");
		assertSame(record, coverage);
		assertEquals(1, coverage.getElements().size());
		assertSame(newRootElements.get(0), coverage.getElements().get(0));
		assertEquals(1, coverage.getRootElements().size());
		assertSame(newRootElements.get(0), coverage.getRootElements().get(0));
		assertSame(LocationMode.VIA, coverage.getLocationMode());
		assertEquals(1, coverage.getOwnerRoles().size());
		Set coverageClaims = coverage.getOwnerRoles().get(0).getSet();
		assertSame(schema.getSet("COVERAGE-CLAIMS"), coverageClaims);
		assertSame(SetOrder.LAST, coverageClaims.getOrder());
		assertEquals(3, coverageClaims.getMembers().size());
		assertEquals(1, coverage.getMemberRoles().size());
		Set empCoverage = coverage.getMemberRoles().get(0).getSet();
		assertSame(schema.getSet("EMP-COVERAGE"), empCoverage);
		assertSame(SetOrder.FIRST, empCoverage.getOrder());
		
	}
	
	@Test
	public void testChangeLocationMode_Calc_KeyNotRetained() {
		
		Schema schema = TestTools.getEmpschmSchema();
		SchemaRecord record = schema.getRecord("DEPARTMENT");
		List<Element> newRootElements = getSingleElementList();
		CompoundCommand command = 
			(CompoundCommand) SwapRecordElementsCommandCreationAssistant.getCommand(record, newRootElements);
		
		// check the assembly of the compound command
		List<?> commands = command.getCommands();
		assertEquals(2, commands.size());
		assertTrue(commands.get(0) instanceof MakeRecordDirectCommand);
		assertTrue(commands.get(1) instanceof SwapRecordElementsCommand);
		
		// check the make record DIRECT command
		MakeRecordDirectCommand makeRecordDirectCommand = (MakeRecordDirectCommand) commands.get(0);
		assertSame(record, makeRecordDirectCommand.record);
		
		// execute(), undo() and redo() the command
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);
		command.execute();
		ObjectGraph objectGraph1 = TestTools.asObjectGraph(schema);
		Xmi xmi1 = TestTools.asXmi(schema);		
		command.undo();
		assertEquals(objectGraph, TestTools.asObjectGraph(schema));
		assertEquals(xmi, TestTools.asXmi(schema));		
		command.redo();
		assertEquals(objectGraph1, TestTools.asObjectGraph(schema));
		assertEquals(xmi1, TestTools.asXmi(schema));
		
		// verify that the command did what it had to do; in this case, the record is no longer 
		// CALC but DIRECT; it still participates in the same (sorted) set as the owner
		SchemaRecord department = schema.getRecord("DEPARTMENT");
		assertSame(record, department);
		assertEquals(1, department.getElements().size());
		assertSame(newRootElements.get(0), department.getElements().get(0));
		assertEquals(1, department.getRootElements().size());
		assertSame(newRootElements.get(0), department.getRootElements().get(0));
		assertSame(LocationMode.DIRECT, department.getLocationMode());
		assertEquals(1, department.getOwnerRoles().size());
		Set deptEmployee = department.getOwnerRoles().get(0).getSet();
		assertSame(schema.getSet("DEPT-EMPLOYEE"), deptEmployee);
		assertSame(SetOrder.SORTED, deptEmployee.getOrder());
		assertEquals(1, deptEmployee.getMembers().size());
		assertSame(schema.getRecord("EMPLOYEE"), deptEmployee.getMembers().get(0).getRecord());
		
	}
	
	@Test
	public void testChangeLocationMode_Calc_KeyRetained() {
		
		Schema schema = TestTools.getEmpschmSchema();
		SchemaRecord record = schema.getRecord("DEPARTMENT");
		List<Element> newRootElements = getSingleElementList("DEPT-ID-0410");
		CompoundCommand command = 
			(CompoundCommand) SwapRecordElementsCommandCreationAssistant.getCommand(record, newRootElements);
		
		// check the assembly of the compound command
		List<?> commands = command.getCommands();
		assertEquals(3, commands.size());
		assertTrue(commands.get(0) instanceof MakeRecordDirectCommand);
		assertTrue(commands.get(1) instanceof SwapRecordElementsCommand);
		assertTrue(commands.get(2) instanceof MakeRecordCalcCommand);
		
		// check the make record CALC command
		MakeRecordCalcCommand makeRecordCalcCommand = (MakeRecordCalcCommand) commands.get(2);
		assertSame(record, makeRecordCalcCommand.record);
		ISupplier<List<Element>> calcKeyElementSupplier = makeRecordCalcCommand.calcKeyElementSupplier;
		assertEquals(1, calcKeyElementSupplier.supply().size());
		assertSame(newRootElements.get(0), calcKeyElementSupplier.supply().get(0));
		assertSame(DuplicatesOption.NOT_ALLOWED, makeRecordCalcCommand.duplicatesOption);
		
		// execute(), undo() and redo() the command
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);
		command.execute();
		ObjectGraph objectGraph1 = TestTools.asObjectGraph(schema);
		Xmi xmi1 = TestTools.asXmi(schema);		
		command.undo();
		assertEquals(objectGraph, TestTools.asObjectGraph(schema));
		assertEquals(xmi, TestTools.asXmi(schema));		
		command.redo();
		assertEquals(objectGraph1, TestTools.asObjectGraph(schema));
		assertEquals(xmi1, TestTools.asXmi(schema));
		
		// verify that the command did what it had to do; in this case, the record is still CALC; it 
		// it still participates in the same (sorted) set as the owner
		SchemaRecord department = schema.getRecord("DEPARTMENT");
		assertSame(record, department);
		assertEquals(1, department.getElements().size());
		assertSame(newRootElements.get(0), department.getElements().get(0));
		assertEquals(1, department.getRootElements().size());
		assertSame(newRootElements.get(0), department.getRootElements().get(0));
		assertSame(LocationMode.CALC, department.getLocationMode());
		assertEquals(1, department.getCalcKey().getElements().size());
		assertSame(newRootElements.get(0), department.getCalcKey().getElements().get(0).getElement());
		assertSame(DuplicatesOption.NOT_ALLOWED, department.getCalcKey().getDuplicatesOption());
		assertEquals(1, department.getOwnerRoles().size());
		Set deptEmployee = department.getOwnerRoles().get(0).getSet();
		assertSame(schema.getSet("DEPT-EMPLOYEE"), deptEmployee);
		assertSame(SetOrder.SORTED, deptEmployee.getOrder());
		assertEquals(1, deptEmployee.getMembers().size());
		assertSame(schema.getRecord("EMPLOYEE"), deptEmployee.getMembers().get(0).getRecord());
	}
	
	@Test
	public void testChangeLocationMode_Via_MultipleMember_KeyNotRetained() {
		
		Schema schema = TestTools.getEmpschmSchema();
		Set set = schema.getSet("COVERAGE-CLAIMS");
		SchemaRecord record = schema.getRecord("HOSPITAL-CLAIM");
		List<Element> newRootElements = getSingleElementList();
		
		// we need to change the order of multiple-member set COVERAGE-CLAIMS to SORTED
		ISortKeyDescription[] sortKeyDescriptions = getSortKeyDescriptionsForFirstElements(set);
		Command assistCommand = new ChangeSetOrderCommand(set, sortKeyDescriptions);
		assistCommand.execute();
		assertSame(SetOrder.SORTED, set.getOrder());
		
		CompoundCommand command = 
			(CompoundCommand) SwapRecordElementsCommandCreationAssistant.getCommand(record, newRootElements);
		
		// check the assembly of the compound commands; there will be a nested compound command that
		// takes care of removing the record from set COVERAGE-CLAIMS because we've made that set
		// SORTED
		List<?> commands = command.getCommands();
		assertEquals(3, commands.size());
		assertTrue(commands.get(0) instanceof MakeRecordDirectCommand);
		assertTrue(commands.get(1) instanceof RemoveMemberFromSetCommand);
		assertTrue(commands.get(2) instanceof SwapRecordElementsCommand);
		
		// check the make record DIRECT command
		MakeRecordDirectCommand makeRecordDirectCommand = (MakeRecordDirectCommand) commands.get(0);
		assertSame(record, makeRecordDirectCommand.record);
		
		// check the remove member from set command
		RemoveMemberFromSetCommand removeMemberFromSetCommand =	
			(RemoveMemberFromSetCommand) commands.get(1);
		assertSame(record.getRole("COVERAGE-CLAIMS"), removeMemberFromSetCommand.memberRole);

		// execute(), undo() and redo() the command
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);
		command.execute();
		ObjectGraph objectGraph1 = TestTools.asObjectGraph(schema);
		Xmi xmi1 = TestTools.asXmi(schema);
		command.undo();
		assertEquals(objectGraph, TestTools.asObjectGraph(schema));
		assertEquals(xmi, TestTools.asXmi(schema));		
		command.redo();
		assertEquals(objectGraph1, TestTools.asObjectGraph(schema));
		assertEquals(xmi1, TestTools.asXmi(schema));
		
		// verify that the command did what it had to do; in this case, the record is DIRECT because
		// it is no longer a member of its VIA set
		SchemaRecord hospitalClaim = schema.getRecord("HOSPITAL-CLAIM");
		assertSame(record, hospitalClaim);
		assertEquals(1, hospitalClaim.getElements().size());
		assertSame(newRootElements.get(0), hospitalClaim.getElements().get(0));
		assertEquals(1, hospitalClaim.getRootElements().size());
		assertSame(newRootElements.get(0), hospitalClaim.getRootElements().get(0));
		assertSame(LocationMode.DIRECT, hospitalClaim.getLocationMode());
		assertEquals(0, hospitalClaim.getOwnerRoles().size());
		assertEquals(0, hospitalClaim.getMemberRoles().size());
	}
	
	@Test
	public void testChangeLocationMode_Via_MultipleMember_KeyRetained() {
		
		Schema schema = TestTools.getEmpschmSchema();
		Set set = schema.getSet("COVERAGE-CLAIMS");
		SchemaRecord record = schema.getRecord("HOSPITAL-CLAIM");
		MemberRole memberRole = (MemberRole) record.getRole("COVERAGE-CLAIMS");
		assertEquals(0, set.getMembers().indexOf(memberRole));
		SetMembershipOption membershipOption = memberRole.getMembershipOption();
		List<Element> newRootElements = getSingleElementList("CLAIM-DATE-0430");
		
		// we need to change the order of multiple-member set COVERAGE-CLAIMS to SORTED
		ISortKeyDescription[] sortKeyDescriptions = getSortKeyDescriptionsForFirstElements(set);
		Command assistCommand = new ChangeSetOrderCommand(set, sortKeyDescriptions);
		assistCommand.execute();
		assertSame(SetOrder.SORTED, set.getOrder());
		
		CompoundCommand command = 
			(CompoundCommand) SwapRecordElementsCommandCreationAssistant.getCommand(record, newRootElements);
		
		// check the assembly of the compound command(s)
		List<?> commands = command.getCommands();
		assertEquals(11, commands.size());
		assertTrue(commands.get(0) instanceof MakeRecordDirectCommand);
		assertTrue(commands.get(1) instanceof RemoveMemberFromSetCommand);
		assertTrue(commands.get(2) instanceof SwapRecordElementsCommand);
		assertTrue(commands.get(3) instanceof AddMemberToSetCommand);
		assertTrue(commands.get(4) instanceof SetObjectAttributeCommand);
		assertTrue(commands.get(5) instanceof MoveEndpointCommand);
		assertTrue(commands.get(6) instanceof MoveEndpointCommand);
		assertTrue(commands.get(7) instanceof ChangeSortKeysCommand);
		assertTrue(commands.get(8) instanceof MakeRecordViaCommand);
		assertTrue(commands.get(9) instanceof ChangePointerOrderCommand);
		assertTrue(commands.get(10) instanceof MoveDiagramNodeCommand);
		
		// check the make record DIRECT command
		MakeRecordDirectCommand makeRecordDirectCommand = (MakeRecordDirectCommand) commands.get(0);
		assertSame(record, makeRecordDirectCommand.record);
		
		// check the remove member from set command
		RemoveMemberFromSetCommand removeMemberFromSetCommand = 
			(RemoveMemberFromSetCommand) commands.get(1);
		assertSame(memberRole, removeMemberFromSetCommand.memberRole);
		
		// check the add member to set command
		AddMemberToSetCommand addMemberToSetCommand = (AddMemberToSetCommand) commands.get(3);
		assertSame(set, addMemberToSetCommand.set);
		assertSame(record, addMemberToSetCommand.memberRecord);
		
		// check the set membership option command
		SetObjectAttributeCommand setMembershipOptionCommand = 
			(SetObjectAttributeCommand) commands.get(4);
		ISupplier<EObject> memberRoleSupplier = setMembershipOptionCommand.eObjectSupplier;
		assertSame(memberRole, memberRoleSupplier.supply());
		assertSame(membershipOption, setMembershipOptionCommand.newValue);
		
		// check the move (source) endpoint command - the command's connection part supplier will 
		// supply the original connection part because we didn't actually remove the record as a 
		// member record from the set and added it again (i.e. we didn't execute the commands)
		MoveEndpointCommand moveSourceEndpointCommand = (MoveEndpointCommand) commands.get(5);
		ISupplier<ConnectionPart> connectionPartSupplier = 
			moveSourceEndpointCommand.connectionPartSupplier;
		assertSame(memberRole.getConnectionParts().get(0), connectionPartSupplier.supply());
		assertEquals(memberRole.getConnectionParts().get(0).getSourceEndpointLocation().getX(),
					 moveSourceEndpointCommand.newX);
		assertEquals(memberRole.getConnectionParts().get(0).getSourceEndpointLocation().getY(),
				 	 moveSourceEndpointCommand.newY);
		assertEquals(true, moveSourceEndpointCommand.source);
		
		// check the move (target) endpoint command - the command's connection part supplier will 
		// supply the original connection part because we didn't actually remove the record as a 
		// member record from the set and added it again (i.e. we didn't execute the commands)
		MoveEndpointCommand moveTargetEndpointCommand = (MoveEndpointCommand) commands.get(6);
		connectionPartSupplier = moveTargetEndpointCommand.connectionPartSupplier;
		assertSame(memberRole.getConnectionParts().get(0), connectionPartSupplier.supply());
		assertEquals(memberRole.getConnectionParts().get(0).getTargetEndpointLocation().getX(),
					 moveTargetEndpointCommand.newX);
		assertEquals(memberRole.getConnectionParts().get(0).getTargetEndpointLocation().getY(),
				 	 moveTargetEndpointCommand.newY);
		assertEquals(false, moveTargetEndpointCommand.source);		
		
		// check the change sort keys command - the command's set supplier will supply the original 
		// set because we didn't actually remove the record as a member record from the set and 
		// added it again (i.e. we didn't execute the commands)
		ChangeSortKeysCommand changeSortKeysCommand = (ChangeSortKeysCommand) commands.get(7);
		ISupplier<Set> setSupplier = changeSortKeysCommand.setSupplier;
		assertSame(set, setSupplier.supply());
		ISortKeyDescription[] sortKeyDescriptions2 = changeSortKeysCommand.sortKeyDescriptions;
		assertEquals(3, sortKeyDescriptions2.length);
		
		assertEquals(1, sortKeyDescriptions2[0].getElementNames().length);
		assertEquals("CLAIM-DATE-0445", sortKeyDescriptions2[0].getElementNames()[0]);
		assertEquals(1, sortKeyDescriptions2[0].getSortSequences().length);
		assertSame(SortSequence.ASCENDING, sortKeyDescriptions2[0].getSortSequences()[0]);
		assertSame(DuplicatesOption.NOT_ALLOWED, sortKeyDescriptions2[0].getDuplicatesOption());
		assertEquals(false, sortKeyDescriptions2[0].isCompressed());
		assertEquals(false, sortKeyDescriptions2[0].isNaturalSequence());
		
		assertEquals(1, sortKeyDescriptions2[1].getElementNames().length);
		assertEquals("CLAIM-DATE-0405", sortKeyDescriptions2[1].getElementNames()[0]);
		assertEquals(1, sortKeyDescriptions2[1].getSortSequences().length);
		assertSame(SortSequence.ASCENDING, sortKeyDescriptions2[1].getSortSequences()[0]);
		assertSame(DuplicatesOption.NOT_ALLOWED, sortKeyDescriptions2[1].getDuplicatesOption());
		assertEquals(false, sortKeyDescriptions2[1].isCompressed());
		assertEquals(false, sortKeyDescriptions2[1].isNaturalSequence());
		
		assertEquals(1, sortKeyDescriptions2[2].getElementNames().length);
		assertEquals("CLAIM-DATE-0430", sortKeyDescriptions2[2].getElementNames()[0]);
		assertEquals(1, sortKeyDescriptions2[2].getSortSequences().length);
		assertSame(SortSequence.ASCENDING, sortKeyDescriptions2[2].getSortSequences()[0]);
		assertSame(DuplicatesOption.NOT_ALLOWED, sortKeyDescriptions2[2].getDuplicatesOption());
		assertEquals(false, sortKeyDescriptions2[2].isCompressed());
		assertEquals(false, sortKeyDescriptions2[2].isNaturalSequence());
		
		// check the make record VIA command
		MakeRecordViaCommand makeRecordViaCommand = (MakeRecordViaCommand) commands.get(8);
		assertSame(record, makeRecordViaCommand.record);
		assertEquals("COVERAGE-CLAIMS", makeRecordViaCommand.viaSetName);
		assertEquals(record.getViaSpecification().getDisplacementPageCount(),
					 makeRecordViaCommand.displacementPageCount);
		assertEquals(record.getViaSpecification().getSymbolicDisplacementName(),
					 makeRecordViaCommand.symbolicDisplacementName);
		
		// check the change pointer order command
		ChangePointerOrderCommand changePointerOrderCommand = 
			(ChangePointerOrderCommand) commands.get(9);
		assertSame(record, changePointerOrderCommand.record);
		List<PointerDescription> asisDescriptions = PrefixUtil.getPointerDescriptions(record);
		List<Pointer<?>> newOrder = changePointerOrderCommand.pointerSupplier.supply();
		assertEquals(asisDescriptions.size(), newOrder.size());
		for (int i = 0; i < asisDescriptions.size(); i++) {
			assertEquals(asisDescriptions.get(i).getSetName(), newOrder.get(i).getSetName());
			assertSame(asisDescriptions.get(i).getPointerType(), newOrder.get(i).getType());
		}
		
		// check the move connection label command (the supplied connection label will be the 
		// current one since we did not yet execute the command and the original items are still 
		// there)
		MoveDiagramNodeCommand moveConnectionLabelCommand = (MoveDiagramNodeCommand) commands.get(10);
		@SuppressWarnings("unchecked")
		ISupplier<ConnectionLabel> connectionLabelSupplier = 
			(ISupplier<ConnectionLabel>) moveConnectionLabelCommand.diagramNodeSupplier;
		assertSame(memberRole.getConnectionLabel(), connectionLabelSupplier.supply());
		assertEquals(memberRole.getConnectionLabel().getDiagramLocation().getX(),
					 moveConnectionLabelCommand.x);
		assertEquals(memberRole.getConnectionLabel().getDiagramLocation().getY(),
				 	 moveConnectionLabelCommand.y);
	
		// execute(), undo() and redo() the command
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);		
		command.execute();
		ObjectGraph objectGraph1 = TestTools.asObjectGraph(schema);
		Xmi xmi1 = TestTools.asXmi(schema);		
		command.undo();
		assertEquals(objectGraph, TestTools.asObjectGraph(schema));
		assertEquals(xmi, TestTools.asXmi(schema));		
		command.redo();
		assertEquals(objectGraph1, TestTools.asObjectGraph(schema));
		assertEquals(xmi1, TestTools.asXmi(schema));

		// verify that the command did what it had to do; in this case, the record is still VIA the
		// same set because the sort key could be retained
		SchemaRecord hospitalClaim = schema.getRecord("HOSPITAL-CLAIM");
		assertSame(record, hospitalClaim);
		assertEquals(1, hospitalClaim.getElements().size());
		assertSame(newRootElements.get(0), hospitalClaim.getElements().get(0));
		assertEquals(1, hospitalClaim.getRootElements().size());
		assertSame(newRootElements.get(0), hospitalClaim.getRootElements().get(0));
		assertSame(LocationMode.VIA, hospitalClaim.getLocationMode());
		Set coverageClaims = schema.getSet("COVERAGE-CLAIMS");
		assertSame(set, coverageClaims);
		assertSame(coverageClaims, hospitalClaim.getViaSpecification().getSet());
		assertEquals(0, hospitalClaim.getOwnerRoles().size());
		assertEquals(1, hospitalClaim.getMemberRoles().size());
		assertSame(coverageClaims, hospitalClaim.getMemberRoles().get(0).getSet());
		assertSame(SetOrder.SORTED, coverageClaims.getOrder());
		Key coverageClaimsKey = hospitalClaim.getMemberRoles().get(0).getSortKey();
		assertEquals(1, coverageClaimsKey.getElements().size());
		assertSame(newRootElements.get(0), coverageClaimsKey.getElements().get(0).getElement());
		assertEquals(3, coverageClaims.getMembers().size());
		assertSame(schema.getRecord("NON-HOSP-CLAIM"), coverageClaims.getMembers().get(0).getRecord());
		assertSame(schema.getRecord("DENTAL-CLAIM"), coverageClaims.getMembers().get(1).getRecord());
		assertSame(hospitalClaim, coverageClaims.getMembers().get(2).getRecord());
	}
	
	@Test
	public void test_MultipleMember_Connectors_KeyNotRetained() {
		Schema schema = TestTools.getSchema("testdata/EMPSCHM version 100b.schema");
		SchemaRecord record = schema.getRecord("DENTAL-CLAIM");
		MemberRole memberRole = (MemberRole) record.getRole("COVERAGE-CLAIMS");
		Set set = memberRole.getSet();
		assertEquals(2, memberRole.getConnectionParts().size());
		
		List<Element> newRootElements = getSingleElementList();
		
		// we need to change the order of multiple-member set COVERAGE-CLAIMS to SORTED
		ISortKeyDescription[] sortKeyDescriptions = getSortKeyDescriptionsForFirstElements(set);
		Command assistCommand = new ChangeSetOrderCommand(set, sortKeyDescriptions);
		assistCommand.execute();
		assertSame(SetOrder.SORTED, set.getOrder());
		
		CompoundCommand command = 
			(CompoundCommand) SwapRecordElementsCommandCreationAssistant.getCommand(record, newRootElements);
		
		// check the assembly of the compound commands; there will be a nested compound command that
		// takes care of removing the record from set COVERAGE-CLAIMS because we've made that set
		// SORTED and the sort key cannot be retained
		List<?> commands = command.getCommands();
		assertEquals(6, commands.size());
		assertTrue(commands.get(0) instanceof DeleteBendpointCommand);
		assertTrue(commands.get(1) instanceof DeleteBendpointCommand);
		assertTrue(commands.get(2) instanceof DeleteConnectorsCommand);
		assertTrue(commands.get(3) instanceof MakeRecordDirectCommand);
		assertTrue(commands.get(4) instanceof RemoveMemberFromSetCommand);
		assertTrue(commands.get(5) instanceof SwapRecordElementsCommand);
		
		// check the first delete bendpoint command
		DeleteBendpointCommand firstDeleteBendpointCommand = (DeleteBendpointCommand) commands.get(0); 
		assertSame(memberRole.getConnectionParts().get(0), firstDeleteBendpointCommand.connectionPart);
		assertEquals(0, firstDeleteBendpointCommand.connectionPartIndex);
		
		// check the second delete bendpoint command
		DeleteBendpointCommand secondDeleteBendpointCommand = (DeleteBendpointCommand) commands.get(1); 
		assertSame(memberRole.getConnectionParts().get(1), secondDeleteBendpointCommand.connectionPart);
		assertEquals(0, secondDeleteBendpointCommand.connectionPartIndex);
			
		// check the delete connectors command
		DeleteConnectorsCommand deleteConnectorsCommand = (DeleteConnectorsCommand) commands.get(2);
		assertSame(memberRole, deleteConnectorsCommand.memberRole);
		
		// check the make record DIRECT command
		MakeRecordDirectCommand makeRecordDirectCommand = (MakeRecordDirectCommand) commands.get(3);
		assertSame(record, makeRecordDirectCommand.record);
		
		// check the remove member from set command
		RemoveMemberFromSetCommand removeMemberFromSetCommand = 
			(RemoveMemberFromSetCommand) commands.get(4);
		assertSame(memberRole, removeMemberFromSetCommand.memberRole);
		
		// execute(), undo() and redo() the command
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);		
		command.execute();
		ObjectGraph objectGraph1 = TestTools.asObjectGraph(schema);
		Xmi xmi1 = TestTools.asXmi(schema);
		command.undo();
		assertEquals(objectGraph, TestTools.asObjectGraph(schema));
		assertEquals(xmi, TestTools.asXmi(schema));
		command.redo();
		assertEquals(objectGraph1, TestTools.asObjectGraph(schema));
		assertEquals(xmi1, TestTools.asXmi(schema));
		
		// verify that the command did what it had to do; in this case, the record is DIRECT because
		// it is no longer a member of its VIA set
		SchemaRecord dentalClaim = schema.getRecord("DENTAL-CLAIM");
		assertSame(record, dentalClaim);
		assertEquals(1, dentalClaim.getElements().size());
		assertSame(newRootElements.get(0), dentalClaim.getElements().get(0));
		assertEquals(1, dentalClaim.getRootElements().size());
		assertSame(newRootElements.get(0), dentalClaim.getRootElements().get(0));
		assertSame(LocationMode.DIRECT, dentalClaim.getLocationMode());
		assertEquals(0, dentalClaim.getOwnerRoles().size());
		assertEquals(0, dentalClaim.getMemberRoles().size());
	}
	
	@Test
	public void test_MultipleMember_Connectors_KeyRetained() {
		Schema schema = TestTools.getSchema("testdata/EMPSCHM version 100b.schema");
		SchemaRecord record = schema.getRecord("DENTAL-CLAIM");
		MemberRole memberRole = (MemberRole) record.getRole("COVERAGE-CLAIMS");
		Set set = memberRole.getSet();
		assertEquals(2, memberRole.getConnectionParts().size());
		SetMembershipOption membershipOption = memberRole.getMembershipOption();		
		List<Element> newRootElements = getSingleElementList("CLAIM-DATE-0405");
		
		// we need to change the order of multiple-member set COVERAGE-CLAIMS to SORTED
		ISortKeyDescription[] sortKeyDescriptions = getSortKeyDescriptionsForFirstElements(set);
		Command assistCommand = new ChangeSetOrderCommand(set, sortKeyDescriptions);
		assistCommand.execute();
		assertSame(SetOrder.SORTED, set.getOrder());
		
		CompoundCommand command = 
			(CompoundCommand) SwapRecordElementsCommandCreationAssistant.getCommand(record, newRootElements);
		
		// check the assembly of the compound commands; there will be a nested compound command that
		// takes care of removing the record from set COVERAGE-CLAIMS because we've made that set
		// SORTED and the sort key cannot be retained
		List<?> commands = command.getCommands();
		assertEquals(17, commands.size());
		assertTrue(commands.get(0) instanceof DeleteBendpointCommand);
		assertTrue(commands.get(1) instanceof DeleteBendpointCommand);
		assertTrue(commands.get(2) instanceof DeleteConnectorsCommand);
		assertTrue(commands.get(3) instanceof MakeRecordDirectCommand);
		assertTrue(commands.get(4) instanceof RemoveMemberFromSetCommand);
		assertTrue(commands.get(5) instanceof SwapRecordElementsCommand);
		assertTrue(commands.get(6) instanceof AddMemberToSetCommand);
		assertTrue(commands.get(7) instanceof SetObjectAttributeCommand);
		assertTrue(commands.get(8) instanceof MoveEndpointCommand);
		assertTrue(commands.get(9) instanceof MoveEndpointCommand);
		assertTrue(commands.get(10) instanceof CreateBendpointCommand);
		assertTrue(commands.get(11) instanceof CreateBendpointCommand);
		assertTrue(commands.get(12) instanceof CreateConnectorCommand);
		assertTrue(commands.get(13) instanceof ChangeSortKeysCommand);
		assertTrue(commands.get(14) instanceof MakeRecordViaCommand);
		assertTrue(commands.get(15) instanceof ChangePointerOrderCommand);
		assertTrue(commands.get(16) instanceof MoveDiagramNodeCommand);
		
		// check the first delete bendpoint command
		DeleteBendpointCommand firstDeleteBendpointCommand = (DeleteBendpointCommand) commands.get(0); 
		assertSame(memberRole.getConnectionParts().get(0), firstDeleteBendpointCommand.connectionPart);
		assertEquals(0, firstDeleteBendpointCommand.connectionPartIndex);
		
		// check the second delete bendpoint command
		DeleteBendpointCommand secondDeleteBendpointCommand = (DeleteBendpointCommand) commands.get(1); 
		assertSame(memberRole.getConnectionParts().get(1), secondDeleteBendpointCommand.connectionPart);
		assertEquals(0, secondDeleteBendpointCommand.connectionPartIndex);
			
		// check the delete connectors command
		DeleteConnectorsCommand deleteConnectorsCommand = (DeleteConnectorsCommand) commands.get(2);
		assertSame(memberRole, deleteConnectorsCommand.memberRole);
		
		// check the make record DIRECT command
		MakeRecordDirectCommand makeRecordDirectCommand = (MakeRecordDirectCommand) commands.get(3);
		assertSame(record, makeRecordDirectCommand.record);
		
		// check the remove member from set command
		RemoveMemberFromSetCommand removeMemberFromSetCommand = 
			(RemoveMemberFromSetCommand) commands.get(4);
		assertSame(memberRole, removeMemberFromSetCommand.memberRole);
		
		// check the add member to set command
		AddMemberToSetCommand addMemberToSetCommand = (AddMemberToSetCommand) commands.get(6);
		assertSame(set, addMemberToSetCommand.set);
		assertSame(record, addMemberToSetCommand.memberRecord);
		
		// check the set membership option command
		SetObjectAttributeCommand setMembershipOptionCommand = 
			(SetObjectAttributeCommand) commands.get(7);
		ISupplier<EObject> eObjectSupplier = setMembershipOptionCommand.eObjectSupplier;
		assertSame(memberRole, eObjectSupplier.supply());
		assertSame(membershipOption, setMembershipOptionCommand.newValue);
		
		// check the move (source) endpoint command - the command's connection part supplier will 
		// supply the original connection part because we didn't actually remove the record as a 
		// member record from the set and added it again (i.e. we didn't execute the commands)
		MoveEndpointCommand moveSourceEndpointCommand = (MoveEndpointCommand) commands.get(8);
		ISupplier<ConnectionPart> connectionPartSupplier = 
			moveSourceEndpointCommand.connectionPartSupplier;
		assertSame(memberRole.getConnectionParts().get(0), connectionPartSupplier.supply());
		assertEquals(memberRole.getConnectionParts().get(0).getSourceEndpointLocation().getX(),
					 moveSourceEndpointCommand.newX);
		assertEquals(memberRole.getConnectionParts().get(0).getSourceEndpointLocation().getY(),
				 	 moveSourceEndpointCommand.newY);
		assertEquals(true, moveSourceEndpointCommand.source);
			
		// check the move (target) endpoint command - the command's connection part supplier will 
		// supply the original FIRST connection part because we didn't actually remove the record as 
		// a member record from the set and added it again (i.e. we didn't execute the commands); if
		// the command was executed, only 1 connection part would exist, so we get the details from
		// the SECOND existing connection part's endpoint
		MoveEndpointCommand moveTargetEndpointCommand = (MoveEndpointCommand) commands.get(9);
		connectionPartSupplier = moveTargetEndpointCommand.connectionPartSupplier;
		assertSame(memberRole.getConnectionParts().get(0), connectionPartSupplier.supply());
		assertEquals(memberRole.getConnectionParts().get(1).getTargetEndpointLocation().getX(),
					 moveTargetEndpointCommand.newX);
		assertEquals(memberRole.getConnectionParts().get(1).getTargetEndpointLocation().getY(),
				 	 moveTargetEndpointCommand.newY);
		assertEquals(false, moveTargetEndpointCommand.source);
		
		// check the first create bendpoint command
		CreateBendpointCommand firstCreateBendpointCommand = (CreateBendpointCommand) commands.get(10);
		connectionPartSupplier = firstCreateBendpointCommand.connectionPartSupplier;
		assertSame(memberRole.getConnectionParts().get(0), connectionPartSupplier.supply());
		assertEquals(0, firstCreateBendpointCommand.connectionPartIndex);
		assertEquals(memberRole.getConnectionParts().get(0).getBendpointLocations().get(0).getX(), 
				    firstCreateBendpointCommand.x);
		assertEquals(memberRole.getConnectionParts().get(0).getBendpointLocations().get(0).getY(), 
				    firstCreateBendpointCommand.y);
		
		// check the second create bendpoint command
		CreateBendpointCommand secondCreateBendpointCommand = (CreateBendpointCommand) commands.get(11);
		connectionPartSupplier = secondCreateBendpointCommand.connectionPartSupplier;
		assertSame(memberRole.getConnectionParts().get(0), connectionPartSupplier.supply());
		assertEquals(1, secondCreateBendpointCommand.connectionPartIndex);
		assertEquals(memberRole.getConnectionParts().get(1).getBendpointLocations().get(0).getX(), 
				     secondCreateBendpointCommand.x);
		assertEquals(memberRole.getConnectionParts().get(1).getBendpointLocations().get(0).getY(), 
				     secondCreateBendpointCommand.y);
		
		// check the create connector command
		CreateConnectorCommand createConnectorCommand = (CreateConnectorCommand) commands.get(12);
		ISupplier<MemberRole> memberRoleSupplier = createConnectorCommand.memberRoleSupplier;
		assertSame(memberRole, memberRoleSupplier.supply());
		assertEquals(memberRole.getConnectionParts().get(0).getConnector().getDiagramLocation().getX(), 
					 createConnectorCommand.location.x);
		assertEquals(memberRole.getConnectionParts().get(0).getConnector().getDiagramLocation().getY(), 
				  	 createConnectorCommand.location.y);
		
		// check the change sort keys command - the command's set supplier will supply the original 
		// set because we didn't actually remove the record as a member record from the set and 
		// added it again (i.e. we didn't execute the commands)
		ChangeSortKeysCommand changeSortKeysCommand = (ChangeSortKeysCommand) commands.get(13);
		ISupplier<Set> setSupplier = changeSortKeysCommand.setSupplier;
		assertSame(set, setSupplier.supply());
		ISortKeyDescription[] sortKeyDescriptions2 = changeSortKeysCommand.sortKeyDescriptions;
		assertEquals(3, sortKeyDescriptions2.length);
		assertEquals(1, sortKeyDescriptions2[0].getElementNames().length);
		assertEquals("CLAIM-DATE-0430", 
					 sortKeyDescriptions2[0].getElementNames()[0]);
		assertEquals(1, sortKeyDescriptions2[0].getSortSequences().length);
		assertSame(SortSequence.ASCENDING, sortKeyDescriptions2[0].getSortSequences()[0]);
		assertSame(DuplicatesOption.NOT_ALLOWED, sortKeyDescriptions2[0].getDuplicatesOption());
		assertEquals(false, sortKeyDescriptions2[0].isCompressed());
		assertEquals(false, sortKeyDescriptions2[0].isNaturalSequence());
		assertEquals(1, sortKeyDescriptions2[1].getElementNames().length);
		assertEquals("CLAIM-DATE-0445", sortKeyDescriptions2[1].getElementNames()[0]);
		assertEquals(1, sortKeyDescriptions2[1].getSortSequences().length);
		assertSame(SortSequence.ASCENDING, sortKeyDescriptions2[1].getSortSequences()[0]);
		assertSame(DuplicatesOption.NOT_ALLOWED, sortKeyDescriptions2[1].getDuplicatesOption());
		assertEquals(false, sortKeyDescriptions2[1].isCompressed());
		assertEquals(false, sortKeyDescriptions2[1].isNaturalSequence());
		assertEquals(1, sortKeyDescriptions2[2].getElementNames().length);
		assertEquals("CLAIM-DATE-0405", sortKeyDescriptions2[2].getElementNames()[0]);
		assertEquals(1, sortKeyDescriptions2[2].getSortSequences().length);
		assertSame(SortSequence.ASCENDING, sortKeyDescriptions2[2].getSortSequences()[0]);
		assertSame(DuplicatesOption.NOT_ALLOWED, sortKeyDescriptions2[2].getDuplicatesOption());
		assertEquals(false, sortKeyDescriptions2[2].isCompressed());
		assertEquals(false, sortKeyDescriptions2[2].isNaturalSequence());		
		
		// check the make record VIA command
		MakeRecordViaCommand makeRecordViaCommand = (MakeRecordViaCommand) commands.get(14);
		assertSame(record, makeRecordViaCommand.record);
		assertEquals("COVERAGE-CLAIMS", makeRecordViaCommand.viaSetName);
		assertEquals(record.getViaSpecification().getDisplacementPageCount(),
					 makeRecordViaCommand.displacementPageCount);
		assertEquals(record.getViaSpecification().getSymbolicDisplacementName(),
					 makeRecordViaCommand.symbolicDisplacementName);
		
		// check the change pointer order command
		ChangePointerOrderCommand changePointerOrderCommand = 
			(ChangePointerOrderCommand) commands.get(15);
		assertSame(record, changePointerOrderCommand.record);
		List<PointerDescription> asisDescriptions = PrefixUtil.getPointerDescriptions(record);
		List<Pointer<?>> newOrder = changePointerOrderCommand.pointerSupplier.supply();
		assertEquals(asisDescriptions.size(), newOrder.size());
		for (int i = 0; i < asisDescriptions.size(); i++) {
			assertEquals(asisDescriptions.get(i).getSetName(), newOrder.get(i).getSetName());
			assertSame(asisDescriptions.get(i).getPointerType(), newOrder.get(i).getType());
		}
		
		// check the move connection label command (the supplied connection label will be the 
		// current one since we did not yet execute the command and the original items are still 
		// there)
		MoveDiagramNodeCommand moveConnectionLabelCommand = (MoveDiagramNodeCommand) commands.get(16);
		@SuppressWarnings("unchecked")
		ISupplier<ConnectionLabel> connectionLabelSupplier = 
			(ISupplier<ConnectionLabel>) moveConnectionLabelCommand.diagramNodeSupplier;
		assertSame(memberRole.getConnectionLabel(), connectionLabelSupplier.supply());
		assertEquals(memberRole.getConnectionLabel().getDiagramLocation().getX(),
					 moveConnectionLabelCommand.x);
		assertEquals(memberRole.getConnectionLabel().getDiagramLocation().getY(),
				 	 moveConnectionLabelCommand.y);
			
		// execute(), undo() and redo() the command
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);	
		command.execute();
		ObjectGraph objectGraph1 = TestTools.asObjectGraph(schema);
		Xmi xmi1 = TestTools.asXmi(schema);
		command.undo();
		assertEquals(objectGraph, TestTools.asObjectGraph(schema));
		assertEquals(xmi, TestTools.asXmi(schema));
		command.redo();
		assertEquals(objectGraph1, TestTools.asObjectGraph(schema));
		assertEquals(xmi1, TestTools.asXmi(schema));
		
		// verify that the command did what it had to do; in this case, the record is still VIA the
		// same set because the sort key could be retained; moreover, the connectors still have to
		// be there
		SchemaRecord dentalClaim = schema.getRecord("DENTAL-CLAIM");
		assertSame(record, dentalClaim);
		assertEquals(1, dentalClaim.getElements().size());
		assertSame(newRootElements.get(0), dentalClaim.getElements().get(0));
		assertEquals(1, dentalClaim.getRootElements().size());
		assertSame(newRootElements.get(0), dentalClaim.getRootElements().get(0));
		assertSame(LocationMode.VIA, dentalClaim.getLocationMode());
		Set coverageClaims = schema.getSet("COVERAGE-CLAIMS");
		assertSame(set, coverageClaims);
		assertSame(coverageClaims, dentalClaim.getViaSpecification().getSet());
		assertEquals(0, dentalClaim.getOwnerRoles().size());
		assertEquals(1, dentalClaim.getMemberRoles().size());
		assertSame(coverageClaims, dentalClaim.getMemberRoles().get(0).getSet());
		assertSame(SetOrder.SORTED, coverageClaims.getOrder());
		Key coverageClaimsKey = dentalClaim.getMemberRoles().get(0).getSortKey();
		assertEquals(1, coverageClaimsKey.getElements().size());
		assertSame(newRootElements.get(0), coverageClaimsKey.getElements().get(0).getElement());
		assertEquals(3, coverageClaims.getMembers().size());
		assertSame(schema.getRecord("HOSPITAL-CLAIM"), coverageClaims.getMembers().get(0).getRecord());
		assertSame(schema.getRecord("NON-HOSP-CLAIM"), coverageClaims.getMembers().get(1).getRecord());
		assertSame(dentalClaim, coverageClaims.getMembers().get(2).getRecord());
		MemberRole dentalClaimMemberRole = coverageClaims.getMembers().get(2);
		assertNotSame(memberRole, dentalClaimMemberRole);
		assertEquals(2, dentalClaimMemberRole.getConnectionParts().size());
	}
	
	@Test
	public void test_CalcKeyNotRetained_SortKeysPartlyRetained() {
		Schema schema = TestTools.getEmpschmSchema();
		SchemaRecord record = schema.getRecord("EMPLOYEE");
		List<Element> newRootElements = getSingleElementList("EMP-LAST-NAME-0415");		
		
		CompoundCommand command = 
			(CompoundCommand) SwapRecordElementsCommandCreationAssistant.getCommand(record, newRootElements);
		
		// check the assembly of the compound command
		List<?> commands = command.getCommands();
		assertEquals(8, commands.size());
		assertTrue(commands.get(0) instanceof MakeRecordDirectCommand);
		assertTrue(commands.get(1) instanceof ChangeSetOrderCommand);
		assertTrue(commands.get(2) instanceof ChangeSetOrderCommand);
		assertTrue(commands.get(3) instanceof ChangeSetOrderCommand);
		assertTrue(commands.get(4) instanceof SwapRecordElementsCommand);
		assertTrue(commands.get(5) instanceof ChangeSetOrderCommand);
		assertTrue(commands.get(6) instanceof ChangeSetOrderCommand);
		assertTrue(commands.get(7) instanceof ChangeSetOrderCommand);
		
		// check the make record DIRECT command
		MakeRecordDirectCommand makeRecordDirectCommand = (MakeRecordDirectCommand) commands.get(0);
		assertSame(record, makeRecordDirectCommand.record);
		
		// check the change set order to LAST command(set DEPT-EMPLOYEE)
		ChangeSetOrderCommand changeSetOrderCommand1 = (ChangeSetOrderCommand) commands.get(1);
		assertSame(schema.getSet("DEPT-EMPLOYEE"), changeSetOrderCommand1.set);
		assertSame(SetOrder.LAST, changeSetOrderCommand1.newOrder);
		
		// check the change set order to LAST command (set EMP-NAME-NDX)
		ChangeSetOrderCommand changeSetOrderCommand2 = (ChangeSetOrderCommand) commands.get(2);
		assertSame(schema.getSet("EMP-NAME-NDX"), changeSetOrderCommand2.set);
		assertSame(SetOrder.LAST, changeSetOrderCommand2.newOrder);
		
		// check the change set order to LAST command (set OFFICE-EMPLOYEE)
		ChangeSetOrderCommand changeSetOrderCommand3 = (ChangeSetOrderCommand) commands.get(3);
		assertSame(schema.getSet("OFFICE-EMPLOYEE"), changeSetOrderCommand3.set);
		assertSame(SetOrder.LAST, changeSetOrderCommand3.newOrder);
		
		// check the change set order to SORTED command (set DEPT-EMPLOYEE)		
		ChangeSetOrderCommand changeSetOrderCommand5 = (ChangeSetOrderCommand) commands.get(5);	
		ISupplier<Set> setSupplier = changeSetOrderCommand5.setSupplier;
		assertSame(schema.getSet("DEPT-EMPLOYEE"), setSupplier.supply());
		assertSame(SetOrder.SORTED, changeSetOrderCommand5.newOrder);
		ISortKeyDescription[] sortKeyDescriptions = changeSetOrderCommand5.sortKeyDescriptions;
		assertEquals(1, sortKeyDescriptions.length);
		assertEquals(1, sortKeyDescriptions[0].getElementNames().length);
		assertEquals("EMP-LAST-NAME-0415", sortKeyDescriptions[0].getElementNames()[0]);
		assertEquals(1, sortKeyDescriptions[0].getSortSequences().length);
		assertSame(SortSequence.ASCENDING, sortKeyDescriptions[0].getSortSequences()[0]);
		assertSame(DuplicatesOption.LAST, sortKeyDescriptions[0].getDuplicatesOption());
		
		// check the change set order to SORTED command (set EMP-NAME-NDX)				
		ChangeSetOrderCommand changeSetOrderCommand6 = (ChangeSetOrderCommand) commands.get(6);
		 setSupplier = changeSetOrderCommand6.setSupplier;
		assertSame(schema.getSet("EMP-NAME-NDX"), setSupplier.supply());
		assertSame(SetOrder.SORTED, changeSetOrderCommand6.newOrder);
		sortKeyDescriptions = changeSetOrderCommand6.sortKeyDescriptions;
		assertEquals(1, sortKeyDescriptions.length);
		assertEquals(1, sortKeyDescriptions[0].getElementNames().length);
		assertEquals("EMP-LAST-NAME-0415", sortKeyDescriptions[0].getElementNames()[0]);
		assertEquals(1, sortKeyDescriptions[0].getSortSequences().length);
		assertSame(SortSequence.ASCENDING, sortKeyDescriptions[0].getSortSequences()[0]);
		assertSame(DuplicatesOption.LAST, sortKeyDescriptions[0].getDuplicatesOption());
				
		// check the change set order to SORTED command (set OFFICE-EMPLOYEE)		
		ChangeSetOrderCommand changeSetOrderCommand7 = (ChangeSetOrderCommand) commands.get(7);
		 setSupplier = changeSetOrderCommand7.setSupplier;
		assertSame(schema.getSet("OFFICE-EMPLOYEE"), setSupplier.supply());
		assertSame(SetOrder.SORTED, changeSetOrderCommand7.newOrder);
		sortKeyDescriptions = changeSetOrderCommand7.sortKeyDescriptions;
		assertEquals(1, sortKeyDescriptions.length);
		assertEquals(1, sortKeyDescriptions[0].getElementNames().length);
		assertEquals("EMP-LAST-NAME-0415", sortKeyDescriptions[0].getElementNames()[0]);
		assertEquals(1, sortKeyDescriptions[0].getSortSequences().length);
		assertSame(SortSequence.ASCENDING, sortKeyDescriptions[0].getSortSequences()[0]);
		assertSame(DuplicatesOption.LAST, sortKeyDescriptions[0].getDuplicatesOption());
		
		// execute(), undo() and redo() the command
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);		
		command.execute();
		ObjectGraph objectGraph1 = TestTools.asObjectGraph(schema);
		Xmi xmi1 = TestTools.asXmi(schema);
		command.undo();
		assertEquals(objectGraph, TestTools.asObjectGraph(schema));
		assertEquals(xmi, TestTools.asXmi(schema));
		command.redo();
		assertEquals(objectGraph1, TestTools.asObjectGraph(schema));
		assertEquals(xmi1, TestTools.asXmi(schema));
		
		// verify that the command did what it had to do: the record should no longer be CALC and
		// the sort keys of the 3 sorted sets in which it participates as a member are only partly
		// retained
		SchemaRecord employee = schema.getRecord("EMPLOYEE");
		assertSame(record, employee);
		assertSame(LocationMode.DIRECT, employee.getLocationMode());
		assertEquals(5, employee.getOwnerRoles().size());
		assertEquals(3, employee.getMemberRoles().size());
		MemberRole deptEmployeeMemberRole = (MemberRole) employee.getRole("DEPT-EMPLOYEE");
		assertSame(schema.getSet("DEPT-EMPLOYEE"), deptEmployeeMemberRole.getSet());
		assertSame(SetOrder.SORTED, deptEmployeeMemberRole.getSet().getOrder());
		assertEquals(1, deptEmployeeMemberRole.getSortKey().getElements().size());
		assertSame(newRootElements.get(0), 
				   deptEmployeeMemberRole.getSortKey().getElements().get(0).getElement());
		assertSame(DuplicatesOption.LAST, 
				   deptEmployeeMemberRole.getSortKey().getDuplicatesOption());
		MemberRole officeEmployeeMemberRole = (MemberRole) employee.getRole("OFFICE-EMPLOYEE");
		assertSame(schema.getSet("OFFICE-EMPLOYEE"), officeEmployeeMemberRole.getSet());
		assertSame(SetOrder.SORTED, officeEmployeeMemberRole.getSet().getOrder());
		assertEquals(1, officeEmployeeMemberRole.getSortKey().getElements().size());
		assertSame(newRootElements.get(0), 
				   officeEmployeeMemberRole.getSortKey().getElements().get(0).getElement());
		assertSame(DuplicatesOption.LAST, 
				   officeEmployeeMemberRole.getSortKey().getDuplicatesOption());
		MemberRole empNameNdxMemberRole = (MemberRole) employee.getRole("EMP-NAME-NDX");
		assertSame(schema.getSet("EMP-NAME-NDX"), empNameNdxMemberRole.getSet());
		assertSame(SetOrder.SORTED, empNameNdxMemberRole.getSet().getOrder());
		assertEquals(1, empNameNdxMemberRole.getSortKey().getElements().size());
		assertSame(newRootElements.get(0), 
				   empNameNdxMemberRole.getSortKey().getElements().get(0).getElement());
		assertSame(DuplicatesOption.LAST, empNameNdxMemberRole.getSortKey().getDuplicatesOption());
	}
	
	@Test
	public void test_CalcKeyRetained_NoSortKeysRetained() {
		Schema schema = TestTools.getEmpschmSchema();
		SchemaRecord record = schema.getRecord("EMPLOYEE");
		List<Element> newRootElements = getSingleElementList("EMP-ID-0415");		
		
		CompoundCommand command = 
			(CompoundCommand) SwapRecordElementsCommandCreationAssistant.getCommand(record, newRootElements);
		
		// check the assembly of the compound command
		List<?> commands = command.getCommands();
		assertEquals(6, commands.size());
		assertTrue(commands.get(0) instanceof MakeRecordDirectCommand);
		assertTrue(commands.get(1) instanceof ChangeSetOrderCommand);
		assertTrue(commands.get(2) instanceof ChangeSetOrderCommand);
		assertTrue(commands.get(3) instanceof ChangeSetOrderCommand);
		assertTrue(commands.get(4) instanceof SwapRecordElementsCommand);
		assertTrue(commands.get(5) instanceof MakeRecordCalcCommand);
		
		// check the make record DIRECT command
		MakeRecordDirectCommand makeRecordDirectCommand = (MakeRecordDirectCommand) commands.get(0);
		assertSame(record, makeRecordDirectCommand.record);
		
		// check the change set order to LAST command(set DEPT-EMPLOYEE)
		ChangeSetOrderCommand changeSetOrderCommand1 = (ChangeSetOrderCommand) commands.get(1);
		assertSame(schema.getSet("DEPT-EMPLOYEE"), changeSetOrderCommand1.set);
		assertSame(SetOrder.LAST, changeSetOrderCommand1.newOrder);
		
		// check the change set order to LAST command(set EMP-NAME-NDX)
		ChangeSetOrderCommand changeSetOrderCommand2 = (ChangeSetOrderCommand) commands.get(2);
		assertSame(schema.getSet("EMP-NAME-NDX"), changeSetOrderCommand2.set);
		assertSame(SetOrder.LAST, changeSetOrderCommand2.newOrder);
		
		// check the change set order to LAST command(set OFFICE-EMPLOYEE)
		ChangeSetOrderCommand changeSetOrderCommand3 = (ChangeSetOrderCommand) commands.get(3);
		assertSame(schema.getSet("OFFICE-EMPLOYEE"), changeSetOrderCommand3.set);
		assertSame(SetOrder.LAST, changeSetOrderCommand3.newOrder);
		
		// check the make record CALC command
		MakeRecordCalcCommand makeRecordCalcCommand = (MakeRecordCalcCommand) commands.get(5);
		assertSame(record, makeRecordCalcCommand.record);
		ISupplier<List<Element>> calcKeyElementSupplier = makeRecordCalcCommand.calcKeyElementSupplier;
		assertEquals(1, calcKeyElementSupplier.supply().size());
		assertSame(newRootElements.get(0), calcKeyElementSupplier.supply().get(0));
		assertSame(DuplicatesOption.NOT_ALLOWED, makeRecordCalcCommand.duplicatesOption);
		
		// execute(), undo() and redo() the command
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);
		command.execute();
		ObjectGraph objectGraph1 = TestTools.asObjectGraph(schema);
		Xmi xmi1 = TestTools.asXmi(schema);
		command.undo();
		assertEquals(objectGraph, TestTools.asObjectGraph(schema));
		assertEquals(xmi, TestTools.asXmi(schema));
		command.redo();
		assertEquals(objectGraph1, TestTools.asObjectGraph(schema));
		assertEquals(xmi1, TestTools.asXmi(schema));
		
		// verify that the command did what it had to do: the record should still be CALC and the
		// 3 sets in which it participates as a member are made LAST and are no longer SORTED
		SchemaRecord employee = schema.getRecord("EMPLOYEE");
		assertSame(record, employee);
		assertSame(LocationMode.CALC, employee.getLocationMode());
		assertSame(newRootElements.get(0), employee.getCalcKey().getElements().get(0).getElement());
		assertSame(DuplicatesOption.NOT_ALLOWED, employee.getCalcKey().getDuplicatesOption());
		assertEquals(5, employee.getOwnerRoles().size());
		assertEquals(3, employee.getMemberRoles().size());
		MemberRole deptEmployeeMemberRole = (MemberRole) employee.getRole("DEPT-EMPLOYEE");
		assertSame(schema.getSet("DEPT-EMPLOYEE"), deptEmployeeMemberRole.getSet());
		assertSame(SetOrder.LAST, deptEmployeeMemberRole.getSet().getOrder());
		MemberRole officeEmployeeMemberRole = (MemberRole) employee.getRole("OFFICE-EMPLOYEE");
		assertSame(schema.getSet("OFFICE-EMPLOYEE"), officeEmployeeMemberRole.getSet());
		assertSame(SetOrder.LAST, officeEmployeeMemberRole.getSet().getOrder());
		MemberRole empNameNdxMemberRole = (MemberRole) employee.getRole("EMP-NAME-NDX");
		assertSame(schema.getSet("EMP-NAME-NDX"), empNameNdxMemberRole.getSet());
		assertSame(SetOrder.LAST, empNameNdxMemberRole.getSet().getOrder());
	}
	
	@Test
	public void test_Calc_And_Multiple_MultipleMembersets_OnlyUnsortedMultipleMemberSetRetained() {
		Schema schema = TestTools.getSchema("testdata/EMPSCHM version 100c.schema");
		SchemaRecord record = schema.getRecord("DENTAL-CLAIM");
		List<Element> newRootElements = getSingleElementList();		
		
		CompoundCommand command = 
			(CompoundCommand) SwapRecordElementsCommandCreationAssistant.getCommand(record, newRootElements);
		
		// check the assembly of the compound command
		List<?> commands = command.getCommands();
		assertEquals(4, commands.size());
		
		assertTrue(commands.get(0) instanceof MakeRecordDirectCommand);
		assertTrue(commands.get(1) instanceof DeleteBendpointCommand);
		assertTrue(commands.get(2) instanceof RemoveMemberFromSetCommand);
		assertTrue(commands.get(3) instanceof SwapRecordElementsCommand);
	
		// check the make record DIRECT command
		MakeRecordDirectCommand makeRecordDirectCommand = (MakeRecordDirectCommand) commands.get(0);
		assertSame(record, makeRecordDirectCommand.record);
		
		// check the delete bendpoint command
		DeleteBendpointCommand deleteBendpointCommand = (DeleteBendpointCommand) commands.get(1);
		assertSame(record.getRole("NEW-SET-1"), deleteBendpointCommand.connectionPart.getMemberRole());
		
		// check the remove member from set command
		RemoveMemberFromSetCommand removeMemberFromSetCommand = 
			(RemoveMemberFromSetCommand) commands.get(2);
		assertSame(record.getRole("NEW-SET-1"), removeMemberFromSetCommand.memberRole);
		
		// execute(), undo() and redo() the command
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);
		command.execute();
		ObjectGraph objectGraph1 = TestTools.asObjectGraph(schema);
		Xmi xmi1 = TestTools.asXmi(schema);
		command.undo();
		assertEquals(objectGraph, TestTools.asObjectGraph(schema));
		assertEquals(xmi, TestTools.asXmi(schema));
		command.redo();
		assertEquals(objectGraph1, TestTools.asObjectGraph(schema));
		assertEquals(xmi1, TestTools.asXmi(schema));
		
		// verify that the command did what it had to do: the record is now DIRECT and it is only
		// part of 1 multiple-member set instead of 2 (because no keys could be retained)
		SchemaRecord dentalClaim = schema.getRecord("DENTAL-CLAIM");
		assertSame(record, dentalClaim);
		assertSame(LocationMode.DIRECT, dentalClaim.getLocationMode());
		assertEquals(0, dentalClaim.getOwnerRoles().size());
		assertEquals(1, dentalClaim.getMemberRoles().size());
		Set coverageClaims = dentalClaim.getMemberRoles().get(0).getSet();
		assertSame(schema.getSet("COVERAGE-CLAIMS"), coverageClaims);
		Set newSet1 = schema.getSet("NEW-SET-1");
		assertFalse(newSet1.isMultipleMember());
		assertSame(schema.getRecord("EXPERTISE"), newSet1.getMembers().get(0).getRecord());
		
	}
	
	@Test
	public void test_Calc_And_Multiple_MultipleMembersets_OnlyCalcRetained() {
		Schema schema = TestTools.getSchema("testdata/EMPSCHM version 100c.schema");
		SchemaRecord record = schema.getRecord("DENTAL-CLAIM");
		List<Element> newRootElements = getSingleElementList("CLAIM-DATE-0405");		
		
		CompoundCommand command = 
			(CompoundCommand) SwapRecordElementsCommandCreationAssistant.getCommand(record, newRootElements);
		
		// check the assembly of the compound command
		List<?> commands = command.getCommands();
		assertEquals(5, commands.size());
		
		assertTrue(commands.get(0) instanceof MakeRecordDirectCommand);
		assertTrue(commands.get(1) instanceof DeleteBendpointCommand);
		assertTrue(commands.get(2) instanceof RemoveMemberFromSetCommand);
		assertTrue(commands.get(3) instanceof SwapRecordElementsCommand);
		assertTrue(commands.get(4) instanceof MakeRecordCalcCommand);
		
		// check the make record DIRECT command
		MakeRecordDirectCommand makeRecordDirectCommand = (MakeRecordDirectCommand) commands.get(0);
		assertSame(record, makeRecordDirectCommand.record);
		
		// check the delete bendpoint command
		DeleteBendpointCommand deleteBendpointCommand = (DeleteBendpointCommand) commands.get(1);
		assertSame(record.getRole("NEW-SET-1"), deleteBendpointCommand.connectionPart.getMemberRole());
		
		// check the remove member from set command
		RemoveMemberFromSetCommand removeMemberFromSetCommand = 
			(RemoveMemberFromSetCommand) commands.get(2);
		assertSame(record.getRole("NEW-SET-1"), removeMemberFromSetCommand.memberRole);
		
		// check the make record CALC command
		MakeRecordCalcCommand makeRecordCalcCommand = (MakeRecordCalcCommand) commands.get(4);
		assertSame(record, makeRecordCalcCommand.record);
		ISupplier<List<Element>> calcKeyElementSupplier = makeRecordCalcCommand.calcKeyElementSupplier;
		assertEquals(1, calcKeyElementSupplier.supply().size());
		assertSame(newRootElements.get(0), calcKeyElementSupplier.supply().get(0));
		assertSame(DuplicatesOption.FIRST, makeRecordCalcCommand.duplicatesOption);
		
		// execute(), undo() and redo() the command
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);
		command.execute();
		ObjectGraph objectGraph1 = TestTools.asObjectGraph(schema);
		Xmi xmi1 = TestTools.asXmi(schema);
		command.undo();
		assertEquals(objectGraph, TestTools.asObjectGraph(schema));
		assertEquals(xmi, TestTools.asXmi(schema));
		command.redo();
		assertEquals(objectGraph1, TestTools.asObjectGraph(schema));
		assertEquals(xmi1, TestTools.asXmi(schema));
		
		// verify that the command did what it had to do: the record is still CALC and it is only
		// part of 1 multiple-member set instead of 2 (because no keys could be retained)
		SchemaRecord dentalClaim = schema.getRecord("DENTAL-CLAIM");
		assertSame(record, dentalClaim);
		assertSame(LocationMode.CALC, dentalClaim.getLocationMode());
		assertEquals(1, dentalClaim.getCalcKey().getElements().size());
		assertSame(newRootElements.get(0), 
				   dentalClaim.getCalcKey().getElements().get(0).getElement());
		assertSame(DuplicatesOption.FIRST, dentalClaim.getCalcKey().getDuplicatesOption());
		assertEquals(0, dentalClaim.getOwnerRoles().size());
		assertEquals(1, dentalClaim.getMemberRoles().size());
		Set coverageClaims = dentalClaim.getMemberRoles().get(0).getSet();
		assertSame(schema.getSet("COVERAGE-CLAIMS"), coverageClaims);
		Set newSet1 = schema.getSet("NEW-SET-1");
		assertFalse(newSet1.isMultipleMember());
		assertSame(schema.getRecord("EXPERTISE"), newSet1.getMembers().get(0).getRecord());
	}
	
	@Test
	public void test_Calc_And_Multiple_MultipleMembersets_AllMultipleMemberSetsRetained() {
		Schema schema = TestTools.getSchema("testdata/EMPSCHM version 100c.schema");
		SchemaRecord record = schema.getRecord("DENTAL-CLAIM");
		List<Element> newRootElements = getSingleElementList("PATIENT-NAME-0405");		
		
		CompoundCommand command = 
			(CompoundCommand) SwapRecordElementsCommandCreationAssistant.getCommand(record, newRootElements);
		
		// check the assembly of the compound command
		List<?> commands = command.getCommands();
		assertEquals(12, commands.size());
		
		assertTrue(commands.get(0) instanceof MakeRecordDirectCommand);
		assertTrue(commands.get(1) instanceof DeleteBendpointCommand);
		assertTrue(commands.get(2) instanceof RemoveMemberFromSetCommand);
		assertTrue(commands.get(3) instanceof SwapRecordElementsCommand);
		assertTrue(commands.get(4) instanceof AddMemberToSetCommand);
		assertTrue(commands.get(5) instanceof SetObjectAttributeCommand);
		assertTrue(commands.get(6) instanceof MoveEndpointCommand);
		assertTrue(commands.get(7) instanceof MoveEndpointCommand);
		assertTrue(commands.get(8) instanceof CreateBendpointCommand);
		assertTrue(commands.get(9) instanceof ChangeSortKeysCommand);
		assertTrue(commands.get(10) instanceof ChangePointerOrderCommand);
		assertTrue(commands.get(11) instanceof MoveDiagramNodeCommand);
		
		// check the make record DIRECT command
		MakeRecordDirectCommand makeRecordDirectCommand = (MakeRecordDirectCommand) commands.get(0);
		assertSame(record, makeRecordDirectCommand.record);
		
		// check the delete bendpoint command
		DeleteBendpointCommand deleteBendpointCommand = (DeleteBendpointCommand) commands.get(1);
		assertSame(record.getRole("NEW-SET-1"), deleteBendpointCommand.connectionPart.getMemberRole());
		
		// check the remove member from set command
		RemoveMemberFromSetCommand removeMemberFromSetCommand = 
			(RemoveMemberFromSetCommand) commands.get(2);
		assertSame(record.getRole("NEW-SET-1"), removeMemberFromSetCommand.memberRole);
		
		// check the add member to set command
		AddMemberToSetCommand addMemberToSetCommand = (AddMemberToSetCommand) commands.get(4);
		assertSame(schema.getSet("NEW-SET-1"), addMemberToSetCommand.set);
		assertSame(record, addMemberToSetCommand.memberRecord);
		
		// check the set membership option command
		SetObjectAttributeCommand setMembershipOptionCommand = 
			(SetObjectAttributeCommand) commands.get(5);
		ISupplier<EObject> memberRoleSupplier = setMembershipOptionCommand.eObjectSupplier;
		MemberRole memberRole = (MemberRole) record.getRole("NEW-SET-1");
		assertSame(memberRole, memberRoleSupplier.supply());
		assertSame(memberRole.getMembershipOption(), setMembershipOptionCommand.newValue);
		
		// check the first move endpoint command
		MoveEndpointCommand moveSourceEndpointCommand = (MoveEndpointCommand) commands.get(6);
		ISupplier<ConnectionPart> connectionPartSupplier = 
			moveSourceEndpointCommand.connectionPartSupplier;
		assertSame(memberRole.getConnectionParts().get(0), connectionPartSupplier.supply());
		assertEquals(memberRole.getConnectionParts().get(0).getSourceEndpointLocation().getX(),
					 moveSourceEndpointCommand.newX);
		assertEquals(memberRole.getConnectionParts().get(0).getSourceEndpointLocation().getY(),
				 	 moveSourceEndpointCommand.newY);
		assertEquals(true, moveSourceEndpointCommand.source);
		
		// check the second move endpoint command
		MoveEndpointCommand moveTargetEndpointCommand = (MoveEndpointCommand) commands.get(7);
		connectionPartSupplier = moveTargetEndpointCommand.connectionPartSupplier;
		assertSame(memberRole.getConnectionParts().get(0), connectionPartSupplier.supply());
		assertEquals(memberRole.getConnectionParts().get(0).getTargetEndpointLocation().getX(),
					 moveTargetEndpointCommand.newX);
		assertEquals(memberRole.getConnectionParts().get(0).getTargetEndpointLocation().getY(),
				 	 moveTargetEndpointCommand.newY);
		assertEquals(false, moveTargetEndpointCommand.source);		
		
		// check the create bendpoint command
		CreateBendpointCommand createBendpointCommand = (CreateBendpointCommand) commands.get(8);
		connectionPartSupplier = createBendpointCommand.connectionPartSupplier;
		assertSame(memberRole.getConnectionParts().get(0), connectionPartSupplier.supply());
		assertEquals(0, createBendpointCommand.connectionPartIndex);
		assertEquals(memberRole.getConnectionParts().get(0).getBendpointLocations().get(0).getX(), 
				    createBendpointCommand.x);
		assertEquals(memberRole.getConnectionParts().get(0).getBendpointLocations().get(0).getY(), 
				    createBendpointCommand.y);
		
		// check the change sort keys command
		ChangeSortKeysCommand changeSortKeysCommand = (ChangeSortKeysCommand) commands.get(9);
		ISupplier<Set> setSupplier = changeSortKeysCommand.setSupplier;
		assertSame(schema.getSet("NEW-SET-1"), setSupplier.supply());
		ISortKeyDescription[] sortKeyDescriptions2 = changeSortKeysCommand.sortKeyDescriptions;
		assertEquals(2, sortKeyDescriptions2.length);
		
		assertEquals(1, sortKeyDescriptions2[0].getElementNames().length);
		assertEquals("SKILL-LEVEL-0425", sortKeyDescriptions2[0].getElementNames()[0]);
		assertEquals(1, sortKeyDescriptions2[0].getSortSequences().length);
		assertSame(SortSequence.ASCENDING, sortKeyDescriptions2[0].getSortSequences()[0]);
		assertSame(DuplicatesOption.NOT_ALLOWED, sortKeyDescriptions2[0].getDuplicatesOption());
		assertEquals(false, sortKeyDescriptions2[0].isCompressed());
		assertEquals(false, sortKeyDescriptions2[0].isNaturalSequence());
		
		assertEquals(1, sortKeyDescriptions2[1].getElementNames().length);
		assertEquals("PATIENT-NAME-0405", sortKeyDescriptions2[1].getElementNames()[0]);
		assertEquals(1, sortKeyDescriptions2[1].getSortSequences().length);
		assertSame(SortSequence.ASCENDING, sortKeyDescriptions2[1].getSortSequences()[0]);
		assertSame(DuplicatesOption.NOT_ALLOWED, sortKeyDescriptions2[1].getDuplicatesOption());
		assertEquals(false, sortKeyDescriptions2[1].isCompressed());
		assertEquals(false, sortKeyDescriptions2[1].isNaturalSequence());
		
		// check the change pointer order command
		ChangePointerOrderCommand changePointerOrderCommand = 
			(ChangePointerOrderCommand) commands.get(10);
		assertSame(record, changePointerOrderCommand.record);
		List<PointerDescription> asisDescriptions = PrefixUtil.getPointerDescriptions(record);
		List<Pointer<?>> newOrder = changePointerOrderCommand.pointerSupplier.supply();
		assertEquals(asisDescriptions.size(), newOrder.size());
		for (int i = 0; i < asisDescriptions.size(); i++) {
			assertEquals(asisDescriptions.get(i).getSetName(), newOrder.get(i).getSetName());
			assertSame(asisDescriptions.get(i).getPointerType(), newOrder.get(i).getType());
		}
		
		// check the move connection label command (the supplied connection label will be the 
		// current one since we did not yet execute the command and the original items are still 
		// there)
		MoveDiagramNodeCommand moveConnectionLabelCommand = (MoveDiagramNodeCommand) commands.get(11);
		@SuppressWarnings("unchecked")
		ISupplier<ConnectionLabel> connectionLabelSupplier = 
			(ISupplier<ConnectionLabel>) moveConnectionLabelCommand.diagramNodeSupplier;
		assertSame(memberRole.getConnectionLabel(), connectionLabelSupplier.supply());
		assertEquals(memberRole.getConnectionLabel().getDiagramLocation().getX(),
					 moveConnectionLabelCommand.x);
		assertEquals(memberRole.getConnectionLabel().getDiagramLocation().getY(),
				 	 moveConnectionLabelCommand.y);
			
		// execute(), undo() and redo() the command
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);
		command.execute();
		ObjectGraph objectGraph1 = TestTools.asObjectGraph(schema);
		Xmi xmi1 = TestTools.asXmi(schema);
		command.undo();
		assertEquals(objectGraph, TestTools.asObjectGraph(schema));
		assertEquals(xmi, TestTools.asXmi(schema));
		command.redo();
		assertEquals(objectGraph1, TestTools.asObjectGraph(schema));
		assertEquals(xmi1, TestTools.asXmi(schema));
		
		// verify that the command did what it had to do: the record is now DIRECT and it is still
		// part of the 2 multiple-member sets (because the sort key of the 1 sorted multiple-member 
		// set could be retained)
		SchemaRecord dentalClaim = schema.getRecord("DENTAL-CLAIM");
		assertSame(record, dentalClaim);
		assertSame(LocationMode.DIRECT, dentalClaim.getLocationMode());
		assertEquals(0, dentalClaim.getOwnerRoles().size());
		assertEquals(2, dentalClaim.getMemberRoles().size());
		Set coverageClaims = dentalClaim.getMemberRoles().get(0).getSet();
		assertSame(schema.getSet("COVERAGE-CLAIMS"), coverageClaims);
		assertTrue(coverageClaims.isMultipleMember());
		Set newSet1 = dentalClaim.getMemberRoles().get(1).getSet();
		assertSame(schema.getSet("NEW-SET-1"), newSet1);
		assertTrue(newSet1.isMultipleMember());
		Key newSet1SortKey= dentalClaim.getMemberRoles().get(1).getSortKey();
		assertEquals(1, newSet1SortKey.getElements().size());
		assertSame(newRootElements.get(0), newSet1SortKey.getElements().get(0).getElement());
	}
	
	@Test
	public void test_Calc_And_Multiple_MultipleMembersets_EverythingRetained() {
		Schema schema = TestTools.getSchema("testdata/EMPSCHM version 100c.schema");
		SchemaRecord record = schema.getRecord("DENTAL-CLAIM");
		List<Element> newRootElements = getSingleElementList("PATIENT-NAME-0405");
		Element secondElement = SchemaFactory.eINSTANCE.createElement();
		secondElement.setName("CLAIM-DATE-0405");
		secondElement.setLevel((short) 2);
		secondElement.setPicture("X(8)");
		secondElement.setUsage(Usage.DISPLAY);
		newRootElements.add(secondElement);
		
		CompoundCommand command = 
			(CompoundCommand) SwapRecordElementsCommandCreationAssistant.getCommand(record, newRootElements);
		
		// check the assembly of the compound command
		List<?> commands = command.getCommands();
		assertEquals(13, commands.size());
		
		assertTrue(commands.get(0) instanceof MakeRecordDirectCommand);
		assertTrue(commands.get(1) instanceof DeleteBendpointCommand);
		assertTrue(commands.get(2) instanceof RemoveMemberFromSetCommand);
		assertTrue(commands.get(3) instanceof SwapRecordElementsCommand);
		assertTrue(commands.get(4) instanceof AddMemberToSetCommand);
		assertTrue(commands.get(5) instanceof SetObjectAttributeCommand);
		assertTrue(commands.get(6) instanceof MoveEndpointCommand);
		assertTrue(commands.get(7) instanceof MoveEndpointCommand);
		assertTrue(commands.get(8) instanceof CreateBendpointCommand);
		assertTrue(commands.get(9) instanceof ChangeSortKeysCommand);
		assertTrue(commands.get(10) instanceof MakeRecordCalcCommand);
		assertTrue(commands.get(11) instanceof ChangePointerOrderCommand);
		assertTrue(commands.get(12) instanceof MoveDiagramNodeCommand);
		
		// check the make record DIRECT command
		MakeRecordDirectCommand makeRecordDirectCommand = (MakeRecordDirectCommand) commands.get(0);
		assertSame(record, makeRecordDirectCommand.record);
		
		// check the delete bendpoint command
		DeleteBendpointCommand deleteBendpointCommand = (DeleteBendpointCommand) commands.get(1);
		assertSame(record.getRole("NEW-SET-1"), deleteBendpointCommand.connectionPart.getMemberRole());
		
		// check the remove member from set command
		RemoveMemberFromSetCommand removeMemberFromSetCommand = 
			(RemoveMemberFromSetCommand) commands.get(2);
		assertSame(record.getRole("NEW-SET-1"), removeMemberFromSetCommand.memberRole);
		
		// check the add member to set command
		AddMemberToSetCommand addMemberToSetCommand = (AddMemberToSetCommand) commands.get(4);
		assertSame(schema.getSet("NEW-SET-1"), addMemberToSetCommand.set);
		assertSame(record, addMemberToSetCommand.memberRecord);
		
		// check the set membership option command
		SetObjectAttributeCommand setMembershipOptionCommand = 
			(SetObjectAttributeCommand) commands.get(5);
		ISupplier<EObject> memberRoleSupplier = setMembershipOptionCommand.eObjectSupplier;
		MemberRole memberRole = (MemberRole) record.getRole("NEW-SET-1");
		assertSame(memberRole, memberRoleSupplier.supply());
		assertSame(memberRole.getMembershipOption(), setMembershipOptionCommand.newValue);
		
		// check the first move endpoint command
		MoveEndpointCommand moveSourceEndpointCommand = (MoveEndpointCommand) commands.get(6);
		ISupplier<ConnectionPart> connectionPartSupplier = 
			moveSourceEndpointCommand.connectionPartSupplier;
		assertSame(memberRole.getConnectionParts().get(0), connectionPartSupplier.supply());
		assertEquals(memberRole.getConnectionParts().get(0).getSourceEndpointLocation().getX(),
					 moveSourceEndpointCommand.newX);
		assertEquals(memberRole.getConnectionParts().get(0).getSourceEndpointLocation().getY(),
				 	 moveSourceEndpointCommand.newY);
		assertEquals(true, moveSourceEndpointCommand.source);
		
		// check the second move endpoint command
		MoveEndpointCommand moveTargetEndpointCommand = (MoveEndpointCommand) commands.get(7);
		connectionPartSupplier = moveTargetEndpointCommand.connectionPartSupplier;
		assertSame(memberRole.getConnectionParts().get(0), connectionPartSupplier.supply());
		assertEquals(memberRole.getConnectionParts().get(0).getTargetEndpointLocation().getX(),
					 moveTargetEndpointCommand.newX);
		assertEquals(memberRole.getConnectionParts().get(0).getTargetEndpointLocation().getY(),
				 	 moveTargetEndpointCommand.newY);
		assertEquals(false, moveTargetEndpointCommand.source);		
		
		// check the create bendpoint command
		CreateBendpointCommand createBendpointCommand = (CreateBendpointCommand) commands.get(8);
		connectionPartSupplier = createBendpointCommand.connectionPartSupplier;
		assertSame(memberRole.getConnectionParts().get(0), connectionPartSupplier.supply());
		assertEquals(0, createBendpointCommand.connectionPartIndex);
		assertEquals(memberRole.getConnectionParts().get(0).getBendpointLocations().get(0).getX(), 
				    createBendpointCommand.x);
		assertEquals(memberRole.getConnectionParts().get(0).getBendpointLocations().get(0).getY(), 
				    createBendpointCommand.y);
		
		// check the change sort keys command
		ChangeSortKeysCommand changeSortKeysCommand = (ChangeSortKeysCommand) commands.get(9);
		ISupplier<Set> setSupplier = changeSortKeysCommand.setSupplier;
		assertSame(schema.getSet("NEW-SET-1"), setSupplier.supply());
		ISortKeyDescription[] sortKeyDescriptions2 = changeSortKeysCommand.sortKeyDescriptions;
		assertEquals(2, sortKeyDescriptions2.length);
		
		assertEquals(1, sortKeyDescriptions2[0].getElementNames().length);
		assertEquals("SKILL-LEVEL-0425", sortKeyDescriptions2[0].getElementNames()[0]);
		assertEquals(1, sortKeyDescriptions2[0].getSortSequences().length);
		assertSame(SortSequence.ASCENDING, sortKeyDescriptions2[0].getSortSequences()[0]);
		assertSame(DuplicatesOption.NOT_ALLOWED, sortKeyDescriptions2[0].getDuplicatesOption());
		assertEquals(false, sortKeyDescriptions2[0].isCompressed());
		assertEquals(false, sortKeyDescriptions2[0].isNaturalSequence());
		
		assertEquals(1, sortKeyDescriptions2[1].getElementNames().length);
		assertEquals("PATIENT-NAME-0405", sortKeyDescriptions2[1].getElementNames()[0]);
		assertEquals(1, sortKeyDescriptions2[1].getSortSequences().length);
		assertSame(SortSequence.ASCENDING, sortKeyDescriptions2[1].getSortSequences()[0]);
		assertSame(DuplicatesOption.NOT_ALLOWED, sortKeyDescriptions2[1].getDuplicatesOption());
		assertEquals(false, sortKeyDescriptions2[1].isCompressed());
		assertEquals(false, sortKeyDescriptions2[1].isNaturalSequence());
		
		// check the make record CALC command
		MakeRecordCalcCommand makeRecordCalcCommand = (MakeRecordCalcCommand) commands.get(10);
		assertSame(record, makeRecordCalcCommand.record);
		ISupplier<List<Element>> calcKeyElementSupplier = makeRecordCalcCommand.calcKeyElementSupplier;
		assertEquals(1, calcKeyElementSupplier.supply().size());
		assertSame(secondElement, calcKeyElementSupplier.supply().get(0));
		assertSame(DuplicatesOption.FIRST, makeRecordCalcCommand.duplicatesOption);
		
		// check the change pointer order command
		ChangePointerOrderCommand changePointerOrderCommand = 
			(ChangePointerOrderCommand) commands.get(11);
		assertSame(record, changePointerOrderCommand.record);
		List<PointerDescription> asisDescriptions = PrefixUtil.getPointerDescriptions(record);
		List<Pointer<?>> newOrder = changePointerOrderCommand.pointerSupplier.supply();
		assertEquals(asisDescriptions.size(), newOrder.size());
		for (int i = 0; i < asisDescriptions.size(); i++) {
			assertEquals(asisDescriptions.get(i).getSetName(), newOrder.get(i).getSetName());
			assertSame(asisDescriptions.get(i).getPointerType(), newOrder.get(i).getType());
		}
		
		// check the move connection label command (the supplied connection label will be the 
		// current one since we did not yet execute the command and the original items are still 
		// there)
		MoveDiagramNodeCommand moveConnectionLabelCommand = (MoveDiagramNodeCommand) commands.get(12);
		@SuppressWarnings("unchecked")
		ISupplier<ConnectionLabel> connectionLabelSupplier = 
			(ISupplier<ConnectionLabel>) moveConnectionLabelCommand.diagramNodeSupplier;
		assertSame(memberRole.getConnectionLabel(), connectionLabelSupplier.supply());
		assertEquals(memberRole.getConnectionLabel().getDiagramLocation().getX(),
					 moveConnectionLabelCommand.x);
		assertEquals(memberRole.getConnectionLabel().getDiagramLocation().getY(),
				 	 moveConnectionLabelCommand.y);
			
		// execute(), undo() and redo() the command
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);
		command.execute();
		ObjectGraph objectGraph1 = TestTools.asObjectGraph(schema);
		Xmi xmi1 = TestTools.asXmi(schema);
		command.undo();
		assertEquals(objectGraph, TestTools.asObjectGraph(schema));
		assertEquals(xmi, TestTools.asXmi(schema));
		command.redo();
		assertEquals(objectGraph1, TestTools.asObjectGraph(schema));
		assertEquals(xmi1, TestTools.asXmi(schema));
		
		// verify that the command did what it had to do: the record is now DIRECT and it is still
		// part of the 2 multiple-member sets (because the sort key of the 1 sorted multiple-member 
		// set could be retained)
		SchemaRecord dentalClaim = schema.getRecord("DENTAL-CLAIM");
		assertSame(record, dentalClaim);
		assertSame(LocationMode.CALC, dentalClaim.getLocationMode());
		assertEquals(1, dentalClaim.getCalcKey().getElements().size());
		assertSame(secondElement, dentalClaim.getCalcKey().getElements().get(0).getElement());
		assertSame(DuplicatesOption.FIRST, dentalClaim.getCalcKey().getDuplicatesOption());
		assertEquals(0, dentalClaim.getOwnerRoles().size());
		assertEquals(2, dentalClaim.getMemberRoles().size());
		Set coverageClaims = dentalClaim.getMemberRoles().get(0).getSet();
		assertSame(schema.getSet("COVERAGE-CLAIMS"), coverageClaims);
		assertTrue(coverageClaims.isMultipleMember());
		Set newSet1 = dentalClaim.getMemberRoles().get(1).getSet();
		assertSame(schema.getSet("NEW-SET-1"), newSet1);
		assertTrue(newSet1.isMultipleMember());
		Key newSet1SortKey= dentalClaim.getMemberRoles().get(1).getSortKey();
		assertEquals(1, newSet1SortKey.getElements().size());
		assertSame(newRootElements.get(0), newSet1SortKey.getElements().get(0).getElement());
	}
	
	@Test
	public void testPointerPositionShiftingAndRetainment_Shift() {
		// whenever a record is removed as a member from a multiple-member set, the prefix of that
		// record will reflect this change, but only if the record is NOT added as a member to that
		// set again - in this test case the record is not added as a member again, so the pointers
		// will shift
		Schema schema = TestTools.getSchema("testdata/EMPSCHM version 100c.schema");
		SchemaRecord record = schema.getRecord("DENTAL-CLAIM");
		List<Element> newRootElements = getSingleElementList();		
		
		List<Pointer<?>> originalPointers = PrefixFactory.newPrefixForInquiry(record).getPointers();
		assertEquals(5, originalPointers.size());
		assertEquals("NEW-SET-1", originalPointers.get(0).getSetName());
		assertEquals(PointerType.MEMBER_NEXT, originalPointers.get(0).getType());
		assertEquals("NEW-SET-1", originalPointers.get(1).getSetName());
		assertEquals(PointerType.MEMBER_PRIOR, originalPointers.get(1).getType());
		assertEquals("NEW-SET-1", originalPointers.get(2).getSetName());
		assertEquals(PointerType.MEMBER_OWNER, originalPointers.get(2).getType());
		assertEquals("COVERAGE-CLAIMS", originalPointers.get(3).getSetName());
		assertEquals(PointerType.MEMBER_NEXT, originalPointers.get(3).getType());
		assertEquals("COVERAGE-CLAIMS", originalPointers.get(4).getSetName());
		assertEquals(PointerType.MEMBER_PRIOR, originalPointers.get(4).getType());
		
		CompoundCommand command = 
			(CompoundCommand) SwapRecordElementsCommandCreationAssistant.getCommand(record, newRootElements);
		
		command.execute();
		
		List<Pointer<?>> newPointers = PrefixFactory.newPrefixForInquiry(record).getPointers();
		assertEquals(2, newPointers.size());
		assertEquals("COVERAGE-CLAIMS", newPointers.get(0).getSetName());
		assertEquals(PointerType.MEMBER_NEXT, newPointers.get(0).getType());
		assertEquals("COVERAGE-CLAIMS", newPointers.get(1).getSetName());
		assertEquals(PointerType.MEMBER_PRIOR, newPointers.get(1).getType());
		
	}
	
	@Test
	public void testPointerPositionShiftingAndRetainment_NoShift() {
		// whenever a record is removed as a member from a multiple-member set, the prefix of that
		// record will reflect this change, but only if the record is NOT added as a member to that
		// set again - in this test case the record is added as a member again, so NO prefix changes
		// should be made at all
		
		Schema schema = TestTools.getSchema("testdata/EMPSCHM version 100c.schema");
		SchemaRecord record = schema.getRecord("DENTAL-CLAIM");
		List<Element> newRootElements = getSingleElementList("PATIENT-NAME-0405");		
		
		List<Pointer<?>> originalPointers = PrefixFactory.newPrefixForInquiry(record).getPointers();
		assertEquals(5, originalPointers.size());
		assertEquals("NEW-SET-1", originalPointers.get(0).getSetName());
		assertEquals(PointerType.MEMBER_NEXT, originalPointers.get(0).getType());
		assertEquals("NEW-SET-1", originalPointers.get(1).getSetName());
		assertEquals(PointerType.MEMBER_PRIOR, originalPointers.get(1).getType());
		assertEquals("NEW-SET-1", originalPointers.get(2).getSetName());
		assertEquals(PointerType.MEMBER_OWNER, originalPointers.get(2).getType());
		assertEquals("COVERAGE-CLAIMS", originalPointers.get(3).getSetName());
		assertEquals(PointerType.MEMBER_NEXT, originalPointers.get(3).getType());
		assertEquals("COVERAGE-CLAIMS", originalPointers.get(4).getSetName());
		assertEquals(PointerType.MEMBER_PRIOR, originalPointers.get(4).getType());
		
		CompoundCommand command = 
			(CompoundCommand) SwapRecordElementsCommandCreationAssistant.getCommand(record, newRootElements);
		
		command.execute();
		
		List<Pointer<?>> newPointers = PrefixFactory.newPrefixForInquiry(record).getPointers();
		assertEquals(5, newPointers.size());
		assertEquals("NEW-SET-1", newPointers.get(0).getSetName());
		assertEquals(PointerType.MEMBER_NEXT, newPointers.get(0).getType());
		assertEquals("NEW-SET-1", newPointers.get(1).getSetName());
		assertEquals(PointerType.MEMBER_PRIOR, newPointers.get(1).getType());
		assertEquals("NEW-SET-1", newPointers.get(2).getSetName());
		assertEquals(PointerType.MEMBER_OWNER, newPointers.get(2).getType());
		assertEquals("COVERAGE-CLAIMS", newPointers.get(3).getSetName());
		assertEquals(PointerType.MEMBER_NEXT, newPointers.get(3).getType());
		assertEquals("COVERAGE-CLAIMS", newPointers.get(4).getSetName());
		assertEquals(PointerType.MEMBER_PRIOR, newPointers.get(4).getType());
	}
	
	@Test
	public void testConnectionLabelLocation_NotRetained() {
		// whenever a record is removed and NOT added again as a member to a multiple-member set, 
		// the location of the connection label should be removed (this test does not really belong
		// here)
		
		Schema schema = TestTools.getSchema("testdata/EMPSCHM version 100c.schema");
		SchemaRecord record = schema.getRecord("DENTAL-CLAIM");
		List<Element> newRootElements = getSingleElementList();
		int originalDiagramLocationCount = schema.getDiagramData().getLocations().size();
		MemberRole memberRole = (MemberRole) record.getRole("NEW-SET-1");
		assertEquals(1, memberRole.getConnectionParts().size());
		// the 4 diagram locations that will be removed:
		assertNotNull(memberRole.getConnectionParts().get(0).getSourceEndpointLocation());
		assertNotNull(memberRole.getConnectionParts().get(0).getTargetEndpointLocation());
		assertEquals(1, memberRole.getConnectionParts().get(0).getBendpointLocations().size());
		assertNotNull(memberRole.getConnectionLabel().getDiagramLocation());
		
		CompoundCommand command = 
			(CompoundCommand) SwapRecordElementsCommandCreationAssistant.getCommand(record, newRootElements);
		
		command.execute();
		
		int newDiagramLocationCount = schema.getDiagramData().getLocations().size();
		assertEquals(originalDiagramLocationCount - 4, newDiagramLocationCount);
		
	}

	@Test
	public void testConnectionLabelLocation_Retained() {
		// whenever a record is removed and added again as a member to a multiple-member set, the
		// location of the connection label should be set to the original diagram location
		
		Schema schema = TestTools.getSchema("testdata/EMPSCHM version 100c.schema");
		SchemaRecord record = schema.getRecord("DENTAL-CLAIM");
		List<Element> newRootElements = getSingleElementList("PATIENT-NAME-0405");
		int originalDiagramLocationCount = schema.getDiagramData().getLocations().size();
		DiagramLocation originalLocation = 
			((MemberRole) record.getRole("NEW-SET-1")).getConnectionLabel().getDiagramLocation();
		
		CompoundCommand command = 
			(CompoundCommand) SwapRecordElementsCommandCreationAssistant.getCommand(record, newRootElements);
		
		command.execute();
		
		int newDiagramLocationCount = schema.getDiagramData().getLocations().size();
		assertEquals(originalDiagramLocationCount, newDiagramLocationCount);
		
		DiagramLocation newLocation = 
			((MemberRole) record.getRole("NEW-SET-1")).getConnectionLabel().getDiagramLocation();
		assertNotSame(newLocation, originalLocation);
		assertEquals(originalLocation.getEyecatcher(), newLocation.getEyecatcher());
		assertEquals(originalLocation.getX(), newLocation.getX());
		assertEquals(originalLocation.getY(), newLocation.getY());
	}
	
}
