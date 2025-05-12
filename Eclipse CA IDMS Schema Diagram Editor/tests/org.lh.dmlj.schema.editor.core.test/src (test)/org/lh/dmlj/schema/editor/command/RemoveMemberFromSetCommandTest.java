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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.lh.dmlj.schema.editor.testtool.TestTools.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.junit.Before;
import org.junit.Test;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.editor.prefix.Pointer;
import org.lh.dmlj.schema.editor.prefix.PointerType;
import org.lh.dmlj.schema.editor.prefix.Prefix;
import org.lh.dmlj.schema.editor.prefix.PrefixFactory;
import org.lh.dmlj.schema.editor.testtool.ObjectGraph;
import org.lh.dmlj.schema.editor.testtool.TestTools;
import org.lh.dmlj.schema.editor.testtool.Xmi;

public class RemoveMemberFromSetCommandTest {

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
		set = TestTools.getSet(schema, "COVERAGE-CLAIMS");
		assertSame(SetOrder.LAST, set.getOrder());
		memberRoleNonHospClaim = 
			(MemberRole) TestTools.getRole(schema, "NON-HOSP-CLAIM", "COVERAGE-CLAIMS");
		TestTools.makeDirect(memberRoleNonHospClaim.getRecord());		
		TestTools.removeAllBendpoints(memberRoleNonHospClaim);				
		
		objectGraph = TestTools.asObjectGraph(schema);
		xmi = TestTools.asXmi(schema);
		
	}	
	
	@Test
	public void testUnsorted() {	
		
		// make sure that we are sure that we've verified that both the source- and target endpoints 
		// are removed
		DiagramLocation sourceEndpoint = 
			memberRoleNonHospClaim.getConnectionParts().get(0).getSourceEndpointLocation(); 
		assertNotNull(sourceEndpoint);
		DiagramLocation targetEndpoint = 
			memberRoleNonHospClaim.getConnectionParts().get(0).getTargetEndpointLocation(); 
		assertNotNull(targetEndpoint);
		
		Command command = new RemoveMemberFromSetCommand(memberRoleNonHospClaim);
		command.execute();
		ObjectGraph touchedObjectGraph = TestTools.asObjectGraph(schema);
		Xmi touchedXmi = TestTools.asXmi(schema);
		TestTools.assertRecordRemovedFromSet(schema, "COVERAGE-CLAIMS", "NON-HOSP-CLAIM");		

		command.undo();
		checkObjectGraph(objectGraph);
		checkXmi(xmi);
		
		command.redo();
		checkObjectGraph(touchedObjectGraph);
		checkXmi(touchedXmi);
		
	}
	
	@Test
	public void testSorted() {
		
		MemberRole memberRoleHospitalClaim = 
			(MemberRole) TestTools.getRole(schema, "HOSPITAL-CLAIM", "COVERAGE-CLAIMS");		
		MemberRole memberRoleDentalClaim = 
			(MemberRole) TestTools.getRole(schema, "DENTAL-CLAIM", "COVERAGE-CLAIMS");
		
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
		
		Prefix prefix = PrefixFactory.newPrefixForInquiry(recordNonHospClaim);
		assertTrue(prefix.getPointers().isEmpty());		
		
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
	
	@Test
	public void testPointerShifting() {
			
		// make sure record NON-HOSP-CLAIM participates in another set so that we can verify that
		// the prefix is kept in a consistent state
		SchemaRecord recordNonHospClaim = memberRoleNonHospClaim.getRecord();
		SchemaRecord recordHospitalClaim = TestTools.getRecord(schema, "HOSPITAL-CLAIM");
		CreateSetCommand tmpCommand = new CreateSetCommand(recordHospitalClaim, SetMode.CHAINED);
		tmpCommand.setMemberRecord(recordNonHospClaim);
		tmpCommand.execute(); // the new set will be called "NEW-SET-1"
		
		Prefix prefix = PrefixFactory.newPrefixForInquiry(recordNonHospClaim);
		List<Pointer<?>> pointers = prefix.getPointers();
		assertEquals(5, pointers.size());
		assertEquals("COVERAGE-CLAIMS", pointers.get(0).getSetName());
		assertSame(PointerType.MEMBER_NEXT, pointers.get(0).getType());
		assertEquals((short) 1, pointers.get(0).getCurrentPositionInPrefix().shortValue());
		assertEquals("COVERAGE-CLAIMS", pointers.get(1).getSetName());
		assertSame(PointerType.MEMBER_PRIOR, pointers.get(1).getType());
		assertEquals((short) 2, pointers.get(1).getCurrentPositionInPrefix().shortValue());
		assertEquals("NEW-SET-1", pointers.get(2).getSetName());
		assertSame(PointerType.MEMBER_NEXT, pointers.get(2).getType());
		assertEquals((short) 3, pointers.get(2).getCurrentPositionInPrefix().shortValue());
		assertEquals("NEW-SET-1", pointers.get(3).getSetName());
		assertSame(PointerType.MEMBER_PRIOR, pointers.get(3).getType());
		assertEquals((short) 4, pointers.get(3).getCurrentPositionInPrefix().shortValue());
		assertEquals("NEW-SET-1", pointers.get(4).getSetName());
		assertSame(PointerType.MEMBER_OWNER, pointers.get(4).getType());
		assertEquals((short) 5, pointers.get(4).getCurrentPositionInPrefix().shortValue());
		
		Command command = new RemoveMemberFromSetCommand(memberRoleNonHospClaim);
		command.execute();
		prefix = PrefixFactory.newPrefixForInquiry(recordNonHospClaim);
		pointers = prefix.getPointers();
		assertEquals(3, pointers.size());
		assertEquals("NEW-SET-1", pointers.get(0).getSetName());
		assertSame(PointerType.MEMBER_NEXT, pointers.get(0).getType());		
		assertEquals((short) 1, pointers.get(0).getCurrentPositionInPrefix().shortValue());
		assertEquals("NEW-SET-1", pointers.get(1).getSetName());		
		assertSame(PointerType.MEMBER_PRIOR, pointers.get(1).getType());
		assertEquals((short) 2, pointers.get(1).getCurrentPositionInPrefix().shortValue());
		assertEquals("NEW-SET-1", pointers.get(2).getSetName());
		assertSame(PointerType.MEMBER_OWNER, pointers.get(2).getType());
		assertEquals((short) 3, pointers.get(2).getCurrentPositionInPrefix().shortValue());
		
		
		command.undo();
		prefix = PrefixFactory.newPrefixForInquiry(recordNonHospClaim);
		pointers = prefix.getPointers();
		assertEquals(5, pointers.size());
		assertEquals("COVERAGE-CLAIMS", pointers.get(0).getSetName());
		assertSame(PointerType.MEMBER_NEXT, pointers.get(0).getType());
		assertEquals((short) 1, pointers.get(0).getCurrentPositionInPrefix().shortValue());
		assertEquals("COVERAGE-CLAIMS", pointers.get(1).getSetName());
		assertSame(PointerType.MEMBER_PRIOR, pointers.get(1).getType());
		assertEquals((short) 2, pointers.get(1).getCurrentPositionInPrefix().shortValue());
		assertEquals("NEW-SET-1", pointers.get(2).getSetName());
		assertSame(PointerType.MEMBER_NEXT, pointers.get(2).getType());
		assertEquals((short) 3, pointers.get(2).getCurrentPositionInPrefix().shortValue());
		assertEquals("NEW-SET-1", pointers.get(3).getSetName());
		assertSame(PointerType.MEMBER_PRIOR, pointers.get(3).getType());
		assertEquals((short) 4, pointers.get(3).getCurrentPositionInPrefix().shortValue());
		assertEquals("NEW-SET-1", pointers.get(4).getSetName());
		assertSame(PointerType.MEMBER_OWNER, pointers.get(4).getType());
		assertEquals((short) 5, pointers.get(4).getCurrentPositionInPrefix().shortValue());
		
		
		command.redo();
		prefix = PrefixFactory.newPrefixForInquiry(recordNonHospClaim);
		pointers = prefix.getPointers();
		assertEquals(3, pointers.size());
		assertEquals("NEW-SET-1", pointers.get(0).getSetName());
		assertSame(PointerType.MEMBER_NEXT, pointers.get(0).getType());
		assertEquals((short) 1, pointers.get(0).getCurrentPositionInPrefix().shortValue());
		assertEquals("NEW-SET-1", pointers.get(1).getSetName());
		assertSame(PointerType.MEMBER_PRIOR, pointers.get(1).getType());
		assertEquals((short) 2, pointers.get(1).getCurrentPositionInPrefix().shortValue());
		assertEquals("NEW-SET-1", pointers.get(2).getSetName());
		assertSame(PointerType.MEMBER_OWNER, pointers.get(2).getType());
		assertEquals((short) 3, pointers.get(2).getCurrentPositionInPrefix().shortValue());		
		
	}

}
