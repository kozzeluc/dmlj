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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.lh.dmlj.schema.editor.testtool.TestTools.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.gef.commands.Command;
import org.junit.Before;
import org.junit.Test;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.editor.command.annotation.Item;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory;
import org.lh.dmlj.schema.editor.command.annotation.Owner;
import org.lh.dmlj.schema.editor.command.annotation.Reference;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeDispatcher;
import org.lh.dmlj.schema.editor.testtool.ObjectGraph;
import org.lh.dmlj.schema.editor.testtool.TestTools;
import org.lh.dmlj.schema.editor.testtool.Xmi;

public class RemoveMemberFromSetCommandTest {

	private MemberRole  memberRoleDentalClaim;
	private MemberRole  memberRoleHospitalClaim;
	private MemberRole  memberRoleNonHospClaim;
	private ObjectGraph objectGraph;
	private Schema 		schema;
	private Set 		set; // COVERAGE-CLAIMS: the only available multiple-member set
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
		
		// we'll use EMPSCHM's COVERAGE-CLAIMS throughout these tests :  the 
		// RemoveMemberFromSetCommand only operates against multiple-member sets - we need to 
		// manipulate each member record type and change its location mode to direct because we
		// cannot remove a record from its VIA set
		schema = TestTools.getEmpschmSchema();
		set = schema.getSet("COVERAGE-CLAIMS");
		assertSame(SetOrder.LAST, set.getOrder());
		memberRoleHospitalClaim = set.getMembers().get(0);
		assertEquals("HOSPITAL-CLAIM", memberRoleHospitalClaim.getRecord().getName());
		memberRoleNonHospClaim = set.getMembers().get(1);
		assertEquals("NON-HOSP-CLAIM", memberRoleNonHospClaim.getRecord().getName());
		memberRoleDentalClaim = set.getMembers().get(2);
		assertEquals("DENTAL-CLAIM", memberRoleDentalClaim.getRecord().getName());
		Command tmpCommand1 = new MakeRecordDirectCommand(memberRoleHospitalClaim.getRecord());
		tmpCommand1.execute();
		Command tmpCommand2 = new MakeRecordDirectCommand(memberRoleNonHospClaim.getRecord());
		tmpCommand2.execute();
		Command tmpCommand3 = new MakeRecordDirectCommand(memberRoleDentalClaim.getRecord());
		tmpCommand3.execute();
		assertSame(LocationMode.DIRECT, memberRoleHospitalClaim.getRecord().getLocationMode());
		assertSame(LocationMode.DIRECT, memberRoleNonHospClaim.getRecord().getLocationMode());
		assertSame(LocationMode.DIRECT, memberRoleDentalClaim.getRecord().getLocationMode());
		assertTrue(memberRoleHospitalClaim.getConnectionParts().get(0).getBendpointLocations().isEmpty());
		assertEquals(2, memberRoleNonHospClaim.getConnectionParts().get(0).getBendpointLocations().size());
		Command tmpCommand4 = 
			new DeleteBendpointCommand(memberRoleNonHospClaim.getConnectionParts().get(0), 0);
		tmpCommand4.execute();
		Command tmpCommand5 = 
			new DeleteBendpointCommand(memberRoleNonHospClaim.getConnectionParts().get(0), 0);
		tmpCommand5.execute();
		assertTrue(memberRoleNonHospClaim.getConnectionParts().get(0).getBendpointLocations().isEmpty());
		assertEquals(2, memberRoleDentalClaim.getConnectionParts().get(0).getBendpointLocations().size());
		Command tmpCommand6 = 
			new DeleteBendpointCommand(memberRoleDentalClaim.getConnectionParts().get(0), 0);
		tmpCommand6.execute();
		Command tmpCommand7 = 
			new DeleteBendpointCommand(memberRoleDentalClaim.getConnectionParts().get(0), 0);
		tmpCommand7.execute();
		assertEquals(0, memberRoleDentalClaim.getConnectionParts().get(0).getBendpointLocations().size());
		assertTrue(memberRoleDentalClaim.getConnectionParts().get(0).getBendpointLocations().isEmpty());
		
		objectGraph = TestTools.asObjectGraph(schema);
		xmi = TestTools.asXmi(schema);
		
	}	
	
	@Test
	public void testAnnotations() {
						
		Command command = new RemoveMemberFromSetCommand(memberRoleHospitalClaim);
		command.execute();
		
		// once execute() has been called, all annotated field values should be in place; make sure
		// the command class itself is annotated with @ModelChange with its type set to 
		// ModelChangeCategory.REMOVE_ITEM
		ModelChange modelChangeAnnotation = command.getClass().getAnnotation(ModelChange.class);	
		assertNotNull(modelChangeAnnotation);
		assertEquals(ModelChangeCategory.REMOVE_ITEM, modelChangeAnnotation.category());
		
		// make sure the owner is set
		Set owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(set, owner);	
		
		// make sure the members reference is set
		EReference reference = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Reference.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(reference == SchemaPackage.eINSTANCE.getSet_Members());
		
		// make sure the item is set
		MemberRole item = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Item.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(memberRoleHospitalClaim, item);
		
		
		command.undo();
		
		// make sure the owner is still set
		owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(set, owner);	
		
		// make sure the members reference is still set
		reference = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Reference.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(reference == SchemaPackage.eINSTANCE.getSet_Members());
		
		// make sure the item is still set
		item = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Item.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(memberRoleHospitalClaim, item);		
		
		
		command.redo();
		
		// make sure the owner is still set
		owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(set, owner);	
		
		// make sure the members reference is still set
		reference = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Reference.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertTrue(reference == SchemaPackage.eINSTANCE.getSet_Members());
		
		// make sure the item is still set
		item = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Item.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(memberRoleHospitalClaim, item);		
		
	}
	
	@Test
	public void testUnsorted() {
		
		Command command = new RemoveMemberFromSetCommand(memberRoleNonHospClaim);
		command.execute();
		ObjectGraph touchedObjectGraph = TestTools.asObjectGraph(schema);
		Xmi touchedXmi = TestTools.asXmi(schema);
		
		assertEquals(2, set.getMembers().size());
		assertEquals(0, set.getMembers().indexOf(memberRoleHospitalClaim));
		assertEquals(1, set.getMembers().indexOf(memberRoleDentalClaim));
		assertEquals(-1, schema.getDiagramData()
							   .getLocations()
							   .indexOf(memberRoleNonHospClaim.getConnectionLabel()
									   						  .getDiagramLocation()));
		assertEquals(-1, schema.getDiagramData()
				   			   .getConnectionLabels()
				   			   .indexOf(memberRoleNonHospClaim.getConnectionLabel()));
		assertEquals(-1, schema.getDiagramData()
				   			   .getConnectionParts()
				   			   .indexOf(memberRoleNonHospClaim.getConnectionParts().get(0)));

		command.undo();
		checkObjectGraph(objectGraph);
		checkXmi(xmi);
		
		command.redo();
		checkObjectGraph(touchedObjectGraph);
		checkXmi(touchedXmi);
		
	}
	
	@Test
	public void testSorted() {
		
		// make the set sorted first...
		ISortKeyDescription[] sortKeyDescriptions = new ISortKeyDescription[3]; 
		
		sortKeyDescriptions[0] = mock(ISortKeyDescription.class);
		when(sortKeyDescriptions[0].getElementNames()).thenReturn(new String[] {"CLAIM-DATE-0430"});
		when(sortKeyDescriptions[0].getSortSequences()).thenReturn(new SortSequence[] {SortSequence.DESCENDING});
		when(sortKeyDescriptions[0].getDuplicatesOption()).thenReturn(DuplicatesOption.LAST);
		when(sortKeyDescriptions[0].isNaturalSequence()).thenReturn(true);
		when(sortKeyDescriptions[0].isCompressed()).thenReturn(false);		
		
		sortKeyDescriptions[1] = mock(ISortKeyDescription.class);
		when(sortKeyDescriptions[1].getElementNames()).thenReturn(new String[] {"CLAIM-DATE-0445"});
		when(sortKeyDescriptions[1].getSortSequences()).thenReturn(new SortSequence[] {SortSequence.ASCENDING});
		when(sortKeyDescriptions[1].getDuplicatesOption()).thenReturn(DuplicatesOption.FIRST);
		when(sortKeyDescriptions[1].isNaturalSequence()).thenReturn(false);
		when(sortKeyDescriptions[1].isCompressed()).thenReturn(false);
		
		sortKeyDescriptions[2] = mock(ISortKeyDescription.class);
		when(sortKeyDescriptions[2].getElementNames()).thenReturn(new String[] {"CLAIM-DATE-0405"});
		when(sortKeyDescriptions[2].getSortSequences()).thenReturn(new SortSequence[] {SortSequence.ASCENDING});
		when(sortKeyDescriptions[2].getDuplicatesOption()).thenReturn(DuplicatesOption.NOT_ALLOWED);
		when(sortKeyDescriptions[2].isNaturalSequence()).thenReturn(false);
		when(sortKeyDescriptions[2].isCompressed()).thenReturn(false);		
		
		Command tmpCommand = new ChangeSetOrderCommand(set, sortKeyDescriptions);
		tmpCommand.execute();
		assertSame(SetOrder.SORTED, set.getOrder());
		objectGraph = TestTools.asObjectGraph(schema);
		xmi = TestTools.asXmi(schema);
		
		// additional data we need to perform key related checks:
		SchemaRecord recordNonHospClaim = memberRoleNonHospClaim.getRecord();
		Key sortKey = memberRoleNonHospClaim.getSortKey();
		assertNotNull(sortKey);
		assertEquals(0, recordNonHospClaim.getKeys().indexOf(sortKey));		
		assertEquals(1, sortKey.getElements().size());
		KeyElement keyElement = sortKey.getElements().get(0);
		Element element = keyElement.getElement();
		assertEquals(1, element.getKeyElements().size());
		
		Command command = new RemoveMemberFromSetCommand(memberRoleNonHospClaim);
		command.execute();
		ObjectGraph touchedObjectGraph = TestTools.asObjectGraph(schema);
		Xmi touchedXmi = TestTools.asXmi(schema);
		
		assertEquals(2, set.getMembers().size());
		assertEquals(0, set.getMembers().indexOf(memberRoleHospitalClaim));
		assertEquals(1, set.getMembers().indexOf(memberRoleDentalClaim));
		assertEquals(-1, schema.getDiagramData()
							   .getLocations()
							   .indexOf(memberRoleNonHospClaim.getConnectionLabel()
									   						  .getDiagramLocation()));
		assertEquals(-1, schema.getDiagramData()
				   			   .getConnectionLabels()
				   			   .indexOf(memberRoleNonHospClaim.getConnectionLabel()));
		assertEquals(-1, schema.getDiagramData()
				   			   .getConnectionParts()
				   			   .indexOf(memberRoleNonHospClaim.getConnectionParts().get(0)));
		
		// regarding the sort key: it should be removed from the element involved AND from the 
		// member record as well
		assertEquals(-1, recordNonHospClaim.getKeys().indexOf(sortKey));
		assertTrue(element.getKeyElements().isEmpty());		
		

		command.undo();
		checkObjectGraph(objectGraph);
		checkXmi(xmi);
		
		command.redo();
		checkObjectGraph(touchedObjectGraph);
		checkXmi(touchedXmi);		
		
	}

}
