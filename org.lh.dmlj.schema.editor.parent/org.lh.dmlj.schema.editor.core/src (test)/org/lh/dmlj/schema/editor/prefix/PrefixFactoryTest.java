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
package org.lh.dmlj.schema.editor.prefix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.lh.dmlj.schema.editor.prefix.PointerType.MEMBER_INDEX;
import static org.lh.dmlj.schema.editor.prefix.PointerType.MEMBER_NEXT;
import static org.lh.dmlj.schema.editor.prefix.PointerType.MEMBER_OWNER;
import static org.lh.dmlj.schema.editor.prefix.PointerType.MEMBER_PRIOR;
import static org.lh.dmlj.schema.editor.prefix.PointerType.OWNER_NEXT;
import static org.lh.dmlj.schema.editor.prefix.PointerType.OWNER_PRIOR;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.junit.Test;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Role;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.testtool.TestTools;

public class PrefixFactoryTest extends AbstractPointerOrPrefixRelatedTestCase {

	private SchemaRecord mockRecordWithInconsistentPointerList(String recordName) {
		
		OwnerRole ownerRole = mockOwnerRoleWithNoPointersSet(recordName, "test set");
		when(ownerRole.getNextDbkeyPosition()).thenReturn((short) 13);
		
		EList<OwnerRole> ownerRoles = new BasicEList<OwnerRole>();
		ownerRoles.add(ownerRole);
				
		SchemaRecord record = mock(SchemaRecord.class);
		when(record.getOwnerRoles()).thenReturn(ownerRoles);
		when(record.getMemberRoles()).thenReturn(new BasicEList<MemberRole>());
		when(record.toString()).thenReturn(recordName);
		
		when(ownerRole.getRecord()).thenReturn(record);
		
		return record;
		
	}
	
	@Test
	public void testPrefixForInquiry() {
		
		Schema schema = TestTools.getEmpschmSchema();
		SchemaRecord recordEmployee = schema.getRecord("EMPLOYEE");
		
		Prefix prefix = PrefixFactory.newPrefixForInquiry(recordEmployee);		
		assertNotNull(prefix);
		
		assertSame(recordEmployee, prefix.getRecord());
		
		List<Pointer<?>> pointers = prefix.getPointers();
		assertNotNull(pointers);
		assertEquals(16, pointers.size());
		
		assertEquals("EMPLOYEE", pointers.get(0).getRecordName());
		assertEquals("DEPT-EMPLOYEE", pointers.get(0).getSetName());
		assertSame(MEMBER_NEXT, pointers.get(0).getType());
		assertFalse(pointers.get(0) instanceof PointerToMove);
		assertFalse(pointers.get(0) instanceof PointerToSet);
		assertFalse(pointers.get(0) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(1).getRecordName());
		assertEquals("DEPT-EMPLOYEE", pointers.get(1).getSetName());
		assertSame(MEMBER_PRIOR, pointers.get(1).getType());
		assertFalse(pointers.get(1) instanceof PointerToMove);
		assertFalse(pointers.get(1) instanceof PointerToSet);
		assertFalse(pointers.get(1) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(2).getRecordName());
		assertEquals("DEPT-EMPLOYEE", pointers.get(2).getSetName());
		assertSame(MEMBER_OWNER, pointers.get(2).getType());
		assertFalse(pointers.get(2) instanceof PointerToMove);
		assertFalse(pointers.get(2) instanceof PointerToSet);
		assertFalse(pointers.get(2) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(3).getRecordName());
		assertEquals("EMP-NAME-NDX", pointers.get(3).getSetName());
		assertSame(MEMBER_INDEX, pointers.get(3).getType());
		assertFalse(pointers.get(3) instanceof PointerToMove);
		assertFalse(pointers.get(3) instanceof PointerToSet);
		assertFalse(pointers.get(3) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(4).getRecordName());
		assertEquals("OFFICE-EMPLOYEE", pointers.get(4).getSetName());
		assertSame(MEMBER_INDEX, pointers.get(4).getType());
		assertFalse(pointers.get(4) instanceof PointerToMove);
		assertFalse(pointers.get(4) instanceof PointerToSet);
		assertFalse(pointers.get(4) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(5).getRecordName());
		assertEquals("OFFICE-EMPLOYEE", pointers.get(5).getSetName());
		assertSame(MEMBER_OWNER, pointers.get(5).getType());
		assertFalse(pointers.get(5) instanceof PointerToMove);
		assertFalse(pointers.get(5) instanceof PointerToSet);
		assertFalse(pointers.get(5) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(6).getRecordName());
		assertEquals("EMP-COVERAGE", pointers.get(6).getSetName());
		assertSame(OWNER_NEXT, pointers.get(6).getType());
		assertFalse(pointers.get(6) instanceof PointerToMove);
		assertFalse(pointers.get(6) instanceof PointerToSet);
		assertFalse(pointers.get(6) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(7).getRecordName());
		assertEquals("EMP-COVERAGE", pointers.get(7).getSetName());
		assertSame(OWNER_PRIOR, pointers.get(7).getType());
		assertFalse(pointers.get(7) instanceof PointerToMove);
		assertFalse(pointers.get(7) instanceof PointerToSet);
		assertFalse(pointers.get(7) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(8).getRecordName());
		assertEquals("EMP-EMPOSITION", pointers.get(8).getSetName());
		assertSame(OWNER_NEXT, pointers.get(8).getType());
		assertFalse(pointers.get(8) instanceof PointerToMove);
		assertFalse(pointers.get(8) instanceof PointerToSet);
		assertFalse(pointers.get(8) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(9).getRecordName());
		assertEquals("EMP-EMPOSITION", pointers.get(9).getSetName());
		assertSame(OWNER_PRIOR, pointers.get(9).getType());
		assertFalse(pointers.get(9) instanceof PointerToMove);
		assertFalse(pointers.get(9) instanceof PointerToSet);
		assertFalse(pointers.get(9) instanceof PointerToUnset);

		assertEquals("EMPLOYEE", pointers.get(10).getRecordName());
		assertEquals("EMP-EXPERTISE", pointers.get(10).getSetName());
		assertSame(OWNER_NEXT, pointers.get(10).getType());
		assertFalse(pointers.get(10) instanceof PointerToMove);
		assertFalse(pointers.get(10) instanceof PointerToSet);
		assertFalse(pointers.get(10) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(11).getRecordName());
		assertEquals("EMP-EXPERTISE", pointers.get(11).getSetName());
		assertSame(OWNER_PRIOR, pointers.get(11).getType());
		assertFalse(pointers.get(11) instanceof PointerToMove);
		assertFalse(pointers.get(11) instanceof PointerToSet);
		assertFalse(pointers.get(11) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(12).getRecordName());
		assertEquals("MANAGES", pointers.get(12).getSetName());
		assertSame(OWNER_NEXT, pointers.get(12).getType());
		assertFalse(pointers.get(12) instanceof PointerToMove);
		assertFalse(pointers.get(12) instanceof PointerToSet);
		assertFalse(pointers.get(12) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(13).getRecordName());
		assertEquals("MANAGES", pointers.get(13).getSetName());
		assertSame(OWNER_PRIOR, pointers.get(13).getType());
		assertFalse(pointers.get(13) instanceof PointerToMove);
		assertFalse(pointers.get(13) instanceof PointerToSet);
		assertFalse(pointers.get(13) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(14).getRecordName());
		assertEquals("REPORTS-TO", pointers.get(14).getSetName());
		assertSame(OWNER_NEXT, pointers.get(14).getType());
		assertFalse(pointers.get(14) instanceof PointerToMove);
		assertFalse(pointers.get(14) instanceof PointerToSet);
		assertFalse(pointers.get(14) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(15).getRecordName());
		assertEquals("REPORTS-TO", pointers.get(15).getSetName());
		assertSame(OWNER_PRIOR, pointers.get(15).getType());
		assertFalse(pointers.get(15) instanceof PointerToMove);
		assertFalse(pointers.get(15) instanceof PointerToSet);
		assertFalse(pointers.get(15) instanceof PointerToUnset);
		
	}
	
	@Test
	public void testPrefixForInquiry_Inconsistent() {				
		SchemaRecord record = mockRecordWithInconsistentPointerList("TESTRECORD");
		try {
			PrefixFactory.newPrefixForInquiry(record);
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("record prefix invalid: " + record.toString(), e.getMessage());
		}		
	}
	
	@Test
	public void testPrefixForPointerAppendage() {	
		
		Schema schema = TestTools.getEmpschmSchema();
		SchemaRecord recordEmployee = schema.getRecord("EMPLOYEE");	
		
		MemberRole newMemberRole = mockMemberRoleWithNoPointersSet("EMPLOYEE", "NEW-SET-1");
		when(newMemberRole.getRecord()).thenReturn(recordEmployee);
		
		PrefixForPointerAppendage prefix = 
			PrefixFactory.newPrefixForPointerAppendage(newMemberRole, 
												   		  MEMBER_NEXT, MEMBER_PRIOR, MEMBER_OWNER);		
		assertNotNull(prefix);
		
		assertSame(recordEmployee, prefix.getRecord());		
		
		List<Pointer<?>> pointers = prefix.getPointers();
		assertEquals(19, pointers.size());
		
		assertEquals("EMPLOYEE", pointers.get(0).getRecordName());
		assertEquals("DEPT-EMPLOYEE", pointers.get(0).getSetName());
		assertSame(MEMBER_NEXT, pointers.get(0).getType());
		assertFalse(pointers.get(0) instanceof PointerToMove);
		assertFalse(pointers.get(0) instanceof PointerToSet);
		assertFalse(pointers.get(0) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(1).getRecordName());
		assertEquals("DEPT-EMPLOYEE", pointers.get(1).getSetName());
		assertSame(MEMBER_PRIOR, pointers.get(1).getType());
		assertFalse(pointers.get(1) instanceof PointerToMove);
		assertFalse(pointers.get(1) instanceof PointerToSet);
		assertFalse(pointers.get(1) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(2).getRecordName());
		assertEquals("DEPT-EMPLOYEE", pointers.get(2).getSetName());
		assertSame(MEMBER_OWNER, pointers.get(2).getType());
		assertFalse(pointers.get(2) instanceof PointerToMove);
		assertFalse(pointers.get(2) instanceof PointerToSet);
		assertFalse(pointers.get(2) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(3).getRecordName());
		assertEquals("EMP-NAME-NDX", pointers.get(3).getSetName());
		assertSame(MEMBER_INDEX, pointers.get(3).getType());
		assertFalse(pointers.get(3) instanceof PointerToMove);
		assertFalse(pointers.get(3) instanceof PointerToSet);
		assertFalse(pointers.get(3) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(4).getRecordName());
		assertEquals("OFFICE-EMPLOYEE", pointers.get(4).getSetName());
		assertSame(MEMBER_INDEX, pointers.get(4).getType());
		assertFalse(pointers.get(4) instanceof PointerToMove);
		assertFalse(pointers.get(4) instanceof PointerToSet);
		assertFalse(pointers.get(4) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(5).getRecordName());
		assertEquals("OFFICE-EMPLOYEE", pointers.get(5).getSetName());
		assertSame(MEMBER_OWNER, pointers.get(5).getType());
		assertFalse(pointers.get(5) instanceof PointerToMove);
		assertFalse(pointers.get(5) instanceof PointerToSet);
		assertFalse(pointers.get(5) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(6).getRecordName());
		assertEquals("EMP-COVERAGE", pointers.get(6).getSetName());
		assertSame(OWNER_NEXT, pointers.get(6).getType());
		assertFalse(pointers.get(6) instanceof PointerToMove);
		assertFalse(pointers.get(6) instanceof PointerToSet);
		assertFalse(pointers.get(6) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(7).getRecordName());
		assertEquals("EMP-COVERAGE", pointers.get(7).getSetName());
		assertSame(OWNER_PRIOR, pointers.get(7).getType());
		assertFalse(pointers.get(7) instanceof PointerToMove);
		assertFalse(pointers.get(7) instanceof PointerToSet);
		assertFalse(pointers.get(7) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(8).getRecordName());
		assertEquals("EMP-EMPOSITION", pointers.get(8).getSetName());
		assertSame(OWNER_NEXT, pointers.get(8).getType());
		assertFalse(pointers.get(8) instanceof PointerToMove);
		assertFalse(pointers.get(8) instanceof PointerToSet);
		assertFalse(pointers.get(8) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(9).getRecordName());
		assertEquals("EMP-EMPOSITION", pointers.get(9).getSetName());
		assertSame(OWNER_PRIOR, pointers.get(9).getType());
		assertFalse(pointers.get(9) instanceof PointerToMove);
		assertFalse(pointers.get(9) instanceof PointerToSet);
		assertFalse(pointers.get(9) instanceof PointerToUnset);

		assertEquals("EMPLOYEE", pointers.get(10).getRecordName());
		assertEquals("EMP-EXPERTISE", pointers.get(10).getSetName());
		assertSame(OWNER_NEXT, pointers.get(10).getType());
		assertFalse(pointers.get(10) instanceof PointerToMove);
		assertFalse(pointers.get(10) instanceof PointerToSet);
		assertFalse(pointers.get(10) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(11).getRecordName());
		assertEquals("EMP-EXPERTISE", pointers.get(11).getSetName());
		assertSame(OWNER_PRIOR, pointers.get(11).getType());
		assertFalse(pointers.get(11) instanceof PointerToMove);
		assertFalse(pointers.get(11) instanceof PointerToSet);
		assertFalse(pointers.get(11) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(12).getRecordName());
		assertEquals("MANAGES", pointers.get(12).getSetName());
		assertSame(OWNER_NEXT, pointers.get(12).getType());
		assertFalse(pointers.get(12) instanceof PointerToMove);
		assertFalse(pointers.get(12) instanceof PointerToSet);
		assertFalse(pointers.get(12) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(13).getRecordName());
		assertEquals("MANAGES", pointers.get(13).getSetName());
		assertSame(OWNER_PRIOR, pointers.get(13).getType());
		assertFalse(pointers.get(13) instanceof PointerToMove);
		assertFalse(pointers.get(13) instanceof PointerToSet);
		assertFalse(pointers.get(13) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(14).getRecordName());
		assertEquals("REPORTS-TO", pointers.get(14).getSetName());
		assertSame(OWNER_NEXT, pointers.get(14).getType());
		assertFalse(pointers.get(14) instanceof PointerToMove);
		assertFalse(pointers.get(14) instanceof PointerToSet);
		assertFalse(pointers.get(14) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(15).getRecordName());
		assertEquals("REPORTS-TO", pointers.get(15).getSetName());
		assertSame(OWNER_PRIOR, pointers.get(15).getType());
		assertFalse(pointers.get(15) instanceof PointerToMove);
		assertFalse(pointers.get(15) instanceof PointerToSet);
		assertFalse(pointers.get(15) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(16).getRecordName());
		assertEquals("NEW-SET-1", pointers.get(16).getSetName());
		assertSame(MEMBER_NEXT, pointers.get(16).getType());
		assertTrue(pointers.get(16) instanceof PointerToSet);
		PointerToSet<?> pointer = (PointerToSet<?>) pointers.get(16); 
		assertNull(pointer.getCurrentPositionInPrefix());
		assertEquals((short) 17, pointer.getPositionInPrefixToSet());
		assertSame(newMemberRole, pointer.getRole());
		
		assertEquals("EMPLOYEE", pointers.get(17).getRecordName());
		assertEquals("NEW-SET-1", pointers.get(17).getSetName());
		assertSame(MEMBER_PRIOR, pointers.get(17).getType());
		assertTrue(pointers.get(17) instanceof PointerToSet);
		pointer = (PointerToSet<?>) pointers.get(17); 
		assertNull(pointer.getCurrentPositionInPrefix());
		assertEquals((short) 18, pointer.getPositionInPrefixToSet());
		assertSame(newMemberRole, pointer.getRole());
		
		assertEquals("EMPLOYEE", pointers.get(18).getRecordName());
		assertEquals("NEW-SET-1", pointers.get(18).getSetName());
		assertSame(MEMBER_OWNER, pointers.get(18).getType());
		assertTrue(pointers.get(18) instanceof PointerToSet);
		pointer = (PointerToSet<?>) pointers.get(18); 
		assertNull(pointer.getCurrentPositionInPrefix());
		assertEquals((short) 19, pointer.getPositionInPrefixToSet());
		assertSame(newMemberRole, pointer.getRole());
		
	}
	
	@Test
	public void testPrefixForAppendage_Inconsistent() {	
		SchemaRecord record = mockRecordWithInconsistentPointerList("TESTRECORD");
		try {
			PrefixFactory.newPrefixForPointerAppendage(record.getOwnerRoles().get(0), OWNER_PRIOR);
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("record prefix invalid: " + record.toString(), e.getMessage());
		}
	}	
	
	@Test
	public void testPrefixForPointerRemoval() {
		
		Schema schema = TestTools.getEmpschmSchema();
		SchemaRecord recordEmployee = schema.getRecord("EMPLOYEE");	
		
		OwnerRole ownerRole = recordEmployee.getOwnerRoles().get(0);
		assertEquals("EMP-COVERAGE", ownerRole.getSet().getName());
		
		PrefixForPointerRemoval prefix = 
			PrefixFactory.newPrefixForPointerRemoval(ownerRole, OWNER_NEXT, OWNER_PRIOR);		
		assertNotNull(prefix);
		
		assertSame(recordEmployee, prefix.getRecord());		
		
		List<Pointer<?>> pointers = prefix.getPointers();
		assertEquals(16, pointers.size());
		
		assertEquals("EMPLOYEE", pointers.get(0).getRecordName());
		assertEquals("DEPT-EMPLOYEE", pointers.get(0).getSetName());
		assertSame(MEMBER_NEXT, pointers.get(0).getType());
		assertFalse(pointers.get(0) instanceof PointerToSet);
		assertFalse(pointers.get(0) instanceof PointerToUnset);
		assertFalse(pointers.get(0) instanceof PointerToMove);
		
		assertEquals("EMPLOYEE", pointers.get(1).getRecordName());
		assertEquals("DEPT-EMPLOYEE", pointers.get(1).getSetName());
		assertSame(MEMBER_PRIOR, pointers.get(1).getType());
		assertFalse(pointers.get(1) instanceof PointerToSet);
		assertFalse(pointers.get(1) instanceof PointerToUnset);
		assertFalse(pointers.get(1) instanceof PointerToMove);
		
		assertEquals("EMPLOYEE", pointers.get(2).getRecordName());
		assertEquals("DEPT-EMPLOYEE", pointers.get(2).getSetName());
		assertSame(MEMBER_OWNER, pointers.get(2).getType());
		assertFalse(pointers.get(2) instanceof PointerToSet);		
		assertFalse(pointers.get(2) instanceof PointerToUnset);
		assertFalse(pointers.get(2) instanceof PointerToMove);
		
		assertEquals("EMPLOYEE", pointers.get(3).getRecordName());
		assertEquals("EMP-NAME-NDX", pointers.get(3).getSetName());
		assertSame(MEMBER_INDEX, pointers.get(3).getType());
		assertFalse(pointers.get(3) instanceof PointerToSet);		
		assertFalse(pointers.get(3) instanceof PointerToUnset);
		assertFalse(pointers.get(3) instanceof PointerToMove);
		
		assertEquals("EMPLOYEE", pointers.get(4).getRecordName());
		assertEquals("OFFICE-EMPLOYEE", pointers.get(4).getSetName());
		assertSame(MEMBER_INDEX, pointers.get(4).getType());
		assertFalse(pointers.get(4) instanceof PointerToSet);
		assertFalse(pointers.get(4) instanceof PointerToUnset);
		assertFalse(pointers.get(4) instanceof PointerToMove);
		
		assertEquals("EMPLOYEE", pointers.get(5).getRecordName());
		assertEquals("OFFICE-EMPLOYEE", pointers.get(5).getSetName());
		assertSame(MEMBER_OWNER, pointers.get(5).getType());
		assertFalse(pointers.get(5) instanceof PointerToSet);		
		assertFalse(pointers.get(5) instanceof PointerToUnset);	
		assertFalse(pointers.get(5) instanceof PointerToMove);
		
		assertEquals("EMPLOYEE", pointers.get(6).getRecordName());
		assertEquals("EMP-COVERAGE", pointers.get(6).getSetName());
		assertSame(OWNER_NEXT, pointers.get(6).getType());
		assertTrue(pointers.get(6) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(7).getRecordName());
		assertEquals("EMP-COVERAGE", pointers.get(7).getSetName());
		assertSame(OWNER_PRIOR, pointers.get(7).getType());
		assertTrue(pointers.get(7) instanceof PointerToUnset);		
		
		assertEquals("EMPLOYEE", pointers.get(8).getRecordName());
		assertEquals("EMP-EMPOSITION", pointers.get(8).getSetName());
		assertSame(OWNER_NEXT, pointers.get(8).getType());
		assertTrue(pointers.get(8) instanceof PointerToMove);
		PointerToMove<?> pointer = (PointerToMove<?>) pointers.get(8);
		assertEquals((short) 9, pointer.getOldPositionInPrefix().shortValue());
		assertEquals((short) 7, pointer.getNewPositionInPrefix());
		
		assertEquals("EMPLOYEE", pointers.get(9).getRecordName());
		assertEquals("EMP-EMPOSITION", pointers.get(9).getSetName());
		assertSame(OWNER_PRIOR, pointers.get(9).getType());
		assertTrue(pointers.get(9) instanceof PointerToMove);
		pointer = (PointerToMove<?>) pointers.get(9);
		assertEquals((short) 10, pointer.getOldPositionInPrefix().shortValue());
		assertEquals((short) 8, pointer.getNewPositionInPrefix());

		assertEquals("EMPLOYEE", pointers.get(10).getRecordName());
		assertEquals("EMP-EXPERTISE", pointers.get(10).getSetName());
		assertSame(OWNER_NEXT, pointers.get(10).getType());
		assertTrue(pointers.get(10) instanceof PointerToMove);
		pointer = (PointerToMove<?>) pointers.get(10);
		assertEquals((short) 11, pointer.getOldPositionInPrefix().shortValue());
		assertEquals((short) 9, pointer.getNewPositionInPrefix());
		
		assertEquals("EMPLOYEE", pointers.get(11).getRecordName());
		assertEquals("EMP-EXPERTISE", pointers.get(11).getSetName());
		assertSame(OWNER_PRIOR, pointers.get(11).getType());
		assertTrue(pointers.get(11) instanceof PointerToMove);
		pointer = (PointerToMove<?>) pointers.get(11);
		assertEquals((short) 12, pointer.getOldPositionInPrefix().shortValue());
		assertEquals((short) 10, pointer.getNewPositionInPrefix());
		
		assertEquals("EMPLOYEE", pointers.get(12).getRecordName());
		assertEquals("MANAGES", pointers.get(12).getSetName());
		assertSame(OWNER_NEXT, pointers.get(12).getType());
		assertTrue(pointers.get(12) instanceof PointerToMove);
		pointer = (PointerToMove<?>) pointers.get(12);
		assertEquals((short) 13, pointer.getOldPositionInPrefix().shortValue());
		assertEquals((short) 11, pointer.getNewPositionInPrefix());
		
		assertEquals("EMPLOYEE", pointers.get(13).getRecordName());
		assertEquals("MANAGES", pointers.get(13).getSetName());
		assertSame(OWNER_PRIOR, pointers.get(13).getType());
		assertTrue(pointers.get(13) instanceof PointerToMove);
		pointer = (PointerToMove<?>) pointers.get(13);
		assertEquals((short) 14, pointer.getOldPositionInPrefix().shortValue());
		assertEquals((short) 12, pointer.getNewPositionInPrefix());
		
		assertEquals("EMPLOYEE", pointers.get(14).getRecordName());
		assertEquals("REPORTS-TO", pointers.get(14).getSetName());
		assertSame(OWNER_NEXT, pointers.get(14).getType());
		assertTrue(pointers.get(14) instanceof PointerToMove);
		pointer = (PointerToMove<?>) pointers.get(14);
		assertEquals((short) 15, pointer.getOldPositionInPrefix().shortValue());
		assertEquals((short) 13, pointer.getNewPositionInPrefix());
		
		assertEquals("EMPLOYEE", pointers.get(15).getRecordName());
		assertEquals("REPORTS-TO", pointers.get(15).getSetName());
		assertSame(OWNER_PRIOR, pointers.get(15).getType());
		assertTrue(pointers.get(15) instanceof PointerToMove);		
		pointer = (PointerToMove<?>) pointers.get(15);
		assertEquals((short) 16, pointer.getOldPositionInPrefix().shortValue());
		assertEquals((short) 14, pointer.getNewPositionInPrefix());
		
	}
	
	@Test
	public void testPrefixForPointerRemoval_Inconsistent() {	
		SchemaRecord record = mockRecordWithInconsistentPointerList("TESTRECORD");
		try {
			PrefixFactory.newPrefixForPointerRemoval(record.getOwnerRoles().get(0), OWNER_PRIOR);
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("record prefix invalid: " + record.toString(), e.getMessage());
		}
	}
	
	@Test
	public void testPrefixForPointerReordering() {
		
		Schema schema = TestTools.getEmpschmSchema();
		SchemaRecord recordEmployee = schema.getRecord("EMPLOYEE");			
		
		// swap the first and last pointer in the EMPLOYEE record type's prefix
		List<Pointer<?>> newPointerOrder = PrefixUtil.getPointersForRecord(recordEmployee);
		assertEquals(16, newPointerOrder.size());
		Pointer<?> firstPointer = newPointerOrder.get(0);
		Pointer<?> lastPointer = newPointerOrder.get(15);
		newPointerOrder.set(0, lastPointer);
		newPointerOrder.set(15, firstPointer);		
		
		PrefixForPointerReordering prefix = 
			PrefixFactory.newPrefixForPointerReordering(recordEmployee, newPointerOrder);		
		assertNotNull(prefix);
		
		assertSame(recordEmployee, prefix.getRecord());		
		
		List<Pointer<?>> pointers = prefix.getPointers();
		assertEquals(16, pointers.size());
		
		assertEquals("EMPLOYEE", pointers.get(0).getRecordName());
		assertEquals("DEPT-EMPLOYEE", pointers.get(0).getSetName());
		assertSame(MEMBER_NEXT, pointers.get(0).getType());
		assertTrue(pointers.get(0) instanceof PointerToMove);
		PointerToMove<?> pointer = (PointerToMove<?>) pointers.get(0);
		assertEquals((short) 1, pointer.getOldPositionInPrefix().shortValue());
		assertEquals((short) 16, pointer.getNewPositionInPrefix());
		
		assertEquals("EMPLOYEE", pointers.get(1).getRecordName());
		assertEquals("DEPT-EMPLOYEE", pointers.get(1).getSetName());
		assertSame(MEMBER_PRIOR, pointers.get(1).getType());
		assertFalse(pointers.get(1) instanceof PointerToMove);
		assertFalse(pointers.get(1) instanceof PointerToUnset);
		assertFalse(pointers.get(1) instanceof PointerToMove);
		
		assertEquals("EMPLOYEE", pointers.get(2).getRecordName());
		assertEquals("DEPT-EMPLOYEE", pointers.get(2).getSetName());
		assertSame(MEMBER_OWNER, pointers.get(2).getType());
		assertFalse(pointers.get(2) instanceof PointerToMove);
		assertFalse(pointers.get(2) instanceof PointerToUnset);
		assertFalse(pointers.get(2) instanceof PointerToMove);
		
		assertEquals("EMPLOYEE", pointers.get(3).getRecordName());
		assertEquals("EMP-NAME-NDX", pointers.get(3).getSetName());
		assertSame(MEMBER_INDEX, pointers.get(3).getType());
		assertFalse(pointers.get(3) instanceof PointerToMove);
		assertFalse(pointers.get(3) instanceof PointerToUnset);
		assertFalse(pointers.get(3) instanceof PointerToMove);
		
		assertEquals("EMPLOYEE", pointers.get(4).getRecordName());
		assertEquals("OFFICE-EMPLOYEE", pointers.get(4).getSetName());
		assertSame(MEMBER_INDEX, pointers.get(4).getType());
		assertFalse(pointers.get(4) instanceof PointerToMove);
		assertFalse(pointers.get(4) instanceof PointerToUnset);
		assertFalse(pointers.get(4) instanceof PointerToMove);
		
		assertEquals("EMPLOYEE", pointers.get(5).getRecordName());
		assertEquals("OFFICE-EMPLOYEE", pointers.get(5).getSetName());
		assertSame(MEMBER_OWNER, pointers.get(5).getType());
		assertFalse(pointers.get(5) instanceof PointerToMove);
		assertFalse(pointers.get(5) instanceof PointerToUnset);	
		assertFalse(pointers.get(5) instanceof PointerToMove);
		
		assertEquals("EMPLOYEE", pointers.get(6).getRecordName());
		assertEquals("EMP-COVERAGE", pointers.get(6).getSetName());
		assertSame(OWNER_NEXT, pointers.get(6).getType());
		assertFalse(pointers.get(6) instanceof PointerToMove);
		assertFalse(pointers.get(6) instanceof PointerToSet);
		assertFalse(pointers.get(6) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(7).getRecordName());
		assertEquals("EMP-COVERAGE", pointers.get(7).getSetName());
		assertSame(OWNER_PRIOR, pointers.get(7).getType());
		assertFalse(pointers.get(7) instanceof PointerToMove);
		assertFalse(pointers.get(7) instanceof PointerToSet);
		assertFalse(pointers.get(7) instanceof PointerToUnset);	
		
		assertEquals("EMPLOYEE", pointers.get(8).getRecordName());
		assertEquals("EMP-EMPOSITION", pointers.get(8).getSetName());
		assertSame(OWNER_NEXT, pointers.get(8).getType());
		assertFalse(pointers.get(8) instanceof PointerToMove);
		assertFalse(pointers.get(8) instanceof PointerToSet);
		assertFalse(pointers.get(8) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(9).getRecordName());
		assertEquals("EMP-EMPOSITION", pointers.get(9).getSetName());
		assertSame(OWNER_PRIOR, pointers.get(9).getType());
		assertFalse(pointers.get(9) instanceof PointerToMove);
		assertFalse(pointers.get(9) instanceof PointerToSet);
		assertFalse(pointers.get(9) instanceof PointerToUnset);

		assertEquals("EMPLOYEE", pointers.get(10).getRecordName());
		assertEquals("EMP-EXPERTISE", pointers.get(10).getSetName());
		assertSame(OWNER_NEXT, pointers.get(10).getType());
		assertFalse(pointers.get(10) instanceof PointerToMove);
		assertFalse(pointers.get(10) instanceof PointerToSet);
		assertFalse(pointers.get(10) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(11).getRecordName());
		assertEquals("EMP-EXPERTISE", pointers.get(11).getSetName());
		assertSame(OWNER_PRIOR, pointers.get(11).getType());
		assertFalse(pointers.get(11) instanceof PointerToMove);
		assertFalse(pointers.get(11) instanceof PointerToSet);
		assertFalse(pointers.get(11) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(12).getRecordName());
		assertEquals("MANAGES", pointers.get(12).getSetName());
		assertSame(OWNER_NEXT, pointers.get(12).getType());
		assertFalse(pointers.get(12) instanceof PointerToMove);
		assertFalse(pointers.get(12) instanceof PointerToSet);
		assertFalse(pointers.get(12) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(13).getRecordName());
		assertEquals("MANAGES", pointers.get(13).getSetName());
		assertSame(OWNER_PRIOR, pointers.get(13).getType());
		assertFalse(pointers.get(13) instanceof PointerToMove);
		assertFalse(pointers.get(13) instanceof PointerToSet);
		assertFalse(pointers.get(13) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(14).getRecordName());
		assertEquals("REPORTS-TO", pointers.get(14).getSetName());
		assertSame(OWNER_NEXT, pointers.get(14).getType());
		assertFalse(pointers.get(14) instanceof PointerToMove);
		assertFalse(pointers.get(14) instanceof PointerToSet);
		assertFalse(pointers.get(14) instanceof PointerToUnset);
		
		assertEquals("EMPLOYEE", pointers.get(15).getRecordName());
		assertEquals("REPORTS-TO", pointers.get(15).getSetName());
		assertSame(OWNER_PRIOR, pointers.get(15).getType());
		assertTrue(pointers.get(15) instanceof PointerToMove);
		pointer = (PointerToMove<?>) pointers.get(15);
		assertEquals((short) 16, pointer.getOldPositionInPrefix().shortValue());
		assertEquals((short) 1, pointer.getNewPositionInPrefix());
		
	}
	
	@Test
	public void testPrefixForPointerReordering_Inconsistent() {	
		SchemaRecord record = mockRecordWithInconsistentPointerList("TESTRECORD");
		try {
			PrefixFactory.newPrefixForPointerReordering(record, new BasicEList<Pointer<?>>());
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("record prefix invalid: " + record.toString(), e.getMessage());
		}
	}
	
	@Test
	public void testPrefixForPointerReordering_NewPointerOrderSizeMismatch() {	
		Schema schema = TestTools.getEmpschmSchema();
		SchemaRecord record = schema.getRecord("EMPLOYEE");	
		try {
			PrefixFactory.newPrefixForPointerReordering(record, new BasicEList<Pointer<?>>());
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("newPointerOrder.size() mismatch: 0 (expected: 16)", e.getMessage());
		}
	}
	
	@Test
	public void testPrefixForPointerReordering_NewPointerOrderContentMismatch_Role() {	
		Schema schema = TestTools.getEmpschmSchema();
		SchemaRecord record = schema.getRecord("EMPLOYEE");	
		EList<Pointer<?>> newPointerOrder = new BasicEList<Pointer<?>>();
		for (int i = 0; i < 16; i++) {
			Pointer<?> pointer = mock(Pointer.class);
			when(pointer.getCurrentPositionInPrefix()).thenReturn(Short.valueOf((short) (i + 1)));
			newPointerOrder.add(pointer);
		}
		try {
			PrefixFactory.newPrefixForPointerReordering(record, newPointerOrder);
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("newPointerOrder content mismatch (role): 0", e.getMessage());
		}
	}	
	
	@Test
	public void testPrefixForPointerReordering_NewPointerOrderContentMismatch_Type() {	
		Schema schema = TestTools.getEmpschmSchema();
		SchemaRecord record = schema.getRecord("EMPLOYEE");
		List<Pointer<?>> pointers = PrefixUtil.getPointersForRecord(record);
		EList<Pointer<?>> newPointerOrder = new BasicEList<Pointer<?>>();
		for (int i = 0; i < 16; i++) {
			@SuppressWarnings("unchecked")
			Pointer<Role> pointer = mock(Pointer.class);
			when(pointer.getCurrentPositionInPrefix()).thenReturn(pointers.get(i).getCurrentPositionInPrefix());
			when(pointer.getRole()).thenReturn(pointers.get(i).getRole());
			if (i == 5) {
				when(pointer.getType()).thenReturn(pointers.get(4).getType());
			} else {
				when(pointer.getType()).thenReturn(pointers.get(i).getType());
			}
			newPointerOrder.add(pointer);
		}
		try {
			PrefixFactory.newPrefixForPointerReordering(record, newPointerOrder);
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("newPointerOrder content mismatch (type): 5", e.getMessage());
		}
	}	
	
	@Test
	public void testPrefixForPointerReordering_NewPointerOrderContentMismatch_CurrentPositionInPRefix() {	
		Schema schema = TestTools.getEmpschmSchema();
		SchemaRecord record = schema.getRecord("EMPLOYEE");
		List<Pointer<?>> pointers = PrefixUtil.getPointersForRecord(record);
		EList<Pointer<?>> newPointerOrder = new BasicEList<Pointer<?>>();
		for (int i = 0; i < 16; i++) {
			@SuppressWarnings("unchecked")
			Pointer<Role> pointer = mock(Pointer.class);
			if (i == 15) {
				when(pointer.getCurrentPositionInPrefix()).thenReturn((short) 8180);					
			} else {
				when(pointer.getCurrentPositionInPrefix()).thenReturn(pointers.get(i).getCurrentPositionInPrefix());			
			}
			when(pointer.getRole()).thenReturn(pointers.get(i).getRole());
			when(pointer.getType()).thenReturn(pointers.get(i).getType());
			newPointerOrder.add(pointer);
		}
		try {
			PrefixFactory.newPrefixForPointerReordering(record, newPointerOrder);
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("newPointerOrder content mismatch (current position in prefix): 15", 
						 e.getMessage());
		}
	}	

}
