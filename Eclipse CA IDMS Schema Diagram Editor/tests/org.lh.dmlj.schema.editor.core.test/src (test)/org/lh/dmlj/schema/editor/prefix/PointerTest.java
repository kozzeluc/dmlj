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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;

public class PointerTest extends AbstractPointerOrPrefixRelatedTestCase {
	
	@Test
	public void testOwnerNextPointerThatIsSet() {
		
		OwnerRole role = mockOwnerRoleWithNoPointersSet("REC1", "SET1");
		when(role.getNextDbkeyPosition()).thenReturn((short) 13);
		
		Pointer<OwnerRole> pointer = PointerFactory.newPointer(role, OWNER_NEXT);
		assertSame(role, pointer.role);
		assertSame(OWNER_NEXT, pointer.getType());
		assertEquals("REC1", pointer.getRecordName());
		assertEquals("SET1", pointer.getSetName());
		assertTrue(pointer.isOwnerDefined());
		assertFalse(pointer.isMemberDefined());
		
		Short positionInPrefix = pointer.getCurrentPositionInPrefix();
		assertNotNull(positionInPrefix);
		assertEquals((short) 13, positionInPrefix.shortValue());
		
		assertSame(role, pointer.getOwnerRole());
		
		try {
			pointer.getMemberRole();
			fail("should throw an UnsupportedOperationException because the role is an owner role");
		} catch (UnsupportedOperationException e) {
			assertEquals("not a member type pointer", e.getMessage());
		}
		
		verify(role, times(1)).getRecord();
		verify(role.getRecord(), times(1)).getName();
		verify(role, times(1)).getSet();
		verify(role.getSet(), times(1)).getName();
		verify(role, times(1)).getNextDbkeyPosition();
		verify(role, never()).getPriorDbkeyPosition();
		
	}
	
	@Test
	public void testOwnerNextPointerThatIsNotSet() {
		
		OwnerRole role = mockOwnerRoleWithNoPointersSet("REC1", "SET1");		
		
		Pointer<OwnerRole> pointer = PointerFactory.newPointer(role, OWNER_NEXT);
		assertSame(role, pointer.role);
		assertSame(OWNER_NEXT, pointer.getType());
		assertEquals("REC1", pointer.getRecordName());
		assertEquals("SET1", pointer.getSetName());
		assertTrue(pointer.isOwnerDefined());
		assertFalse(pointer.isMemberDefined());
		
		Short positionInPrefix = pointer.getCurrentPositionInPrefix();
		assertNull(positionInPrefix);		
		
		assertSame(role, pointer.getOwnerRole());
		
		try {
			pointer.getMemberRole();
			fail("should throw an UnsupportedOperationException because the role is an owner role");
		} catch (UnsupportedOperationException e) {
			assertEquals("not a member type pointer", e.getMessage());
		}
		
		verify(role, times(1)).getRecord();
		verify(role.getRecord(), times(1)).getName();
		verify(role, times(1)).getSet();
		verify(role.getSet(), times(1)).getName();
		verify(role, times(1)).getNextDbkeyPosition();
		verify(role, never()).getPriorDbkeyPosition();
		
	}	
	
	@Test
	public void testOwnerPriorPointerThatIsSet() {
		
		OwnerRole role = mockOwnerRoleWithNoPointersSet("REC1", "SET1");
		Short positionInPrefix = Short.valueOf((short) 17);
		when(role.getPriorDbkeyPosition()).thenReturn(positionInPrefix);
		
		Pointer<OwnerRole> pointer = PointerFactory.newPointer(role, OWNER_PRIOR);
		assertSame(role, pointer.role);
		assertSame(OWNER_PRIOR, pointer.getType());
		assertEquals("REC1", pointer.getRecordName());
		assertEquals("SET1", pointer.getSetName());
		assertTrue(pointer.isOwnerDefined());
		assertFalse(pointer.isMemberDefined());
		
		assertSame(positionInPrefix, pointer.getCurrentPositionInPrefix());
		
		assertSame(role, pointer.getOwnerRole());
		
		try {
			pointer.getMemberRole();
			fail("should throw an UnsupportedOperationException because the role is an owner role");
		} catch (UnsupportedOperationException e) {
			assertEquals("not a member type pointer", e.getMessage());
		}
		
		verify(role, times(1)).getRecord();
		verify(role.getRecord(), times(1)).getName();
		verify(role, times(1)).getSet();
		verify(role.getSet(), times(1)).getName();
		verify(role, never()).getNextDbkeyPosition();
		verify(role, times(1)).getPriorDbkeyPosition();
		
	}	
	
	@Test
	public void testOwnerPriorPointerThatIsNotSet() {
		
		OwnerRole role = mockOwnerRoleWithNoPointersSet("REC1", "SET1");		
		
		Pointer<OwnerRole> pointer = PointerFactory.newPointer(role, OWNER_PRIOR);
		assertSame(role, pointer.role);
		assertSame(OWNER_PRIOR, pointer.getType());
		assertEquals("REC1", pointer.getRecordName());
		assertEquals("SET1", pointer.getSetName());
		assertTrue(pointer.isOwnerDefined());
		assertFalse(pointer.isMemberDefined());
		
		assertNull(pointer.getCurrentPositionInPrefix());
		
		assertSame(role, pointer.getOwnerRole());
		
		try {
			pointer.getMemberRole();
			fail("should throw an UnsupportedOperationException because the role is an owner role");
		} catch (UnsupportedOperationException e) {
			assertEquals("not a member type pointer", e.getMessage());
		}
		
		verify(role, times(1)).getRecord();
		verify(role.getRecord(), times(1)).getName();
		verify(role, times(1)).getSet();
		verify(role.getSet(), times(1)).getName();
		verify(role, never()).getNextDbkeyPosition();
		verify(role, times(1)).getPriorDbkeyPosition();
		
	}

	@Test
	public void testMemberNextPointerThatIsSet() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("REC1", "SET1");
		Short positionInPrefix = Short.valueOf((short) 11);
		when(role.getNextDbkeyPosition()).thenReturn(positionInPrefix);
		
		Pointer<MemberRole> pointer = PointerFactory.newPointer(role, MEMBER_NEXT);
		assertSame(role, pointer.role);
		assertSame(MEMBER_NEXT, pointer.getType());
		assertEquals("REC1", pointer.getRecordName());
		assertEquals("SET1", pointer.getSetName());
		assertFalse(pointer.isOwnerDefined());
		assertTrue(pointer.isMemberDefined());
		
		assertSame(positionInPrefix, pointer.getCurrentPositionInPrefix());
		
		try {
			pointer.getOwnerRole();
			fail("should throw an UnsupportedOperationException because the role is a member role");
		} catch (UnsupportedOperationException e) {
			assertEquals("not an owner type pointer", e.getMessage());
		}
		
		assertSame(role, pointer.getMemberRole());		
		
		verify(role, times(1)).getRecord();
		verify(role.getRecord(), times(1)).getName();
		verify(role, times(1)).getSet();
		verify(role.getSet(), times(1)).getName();
		verify(role, times(1)).getNextDbkeyPosition();
		verify(role, never()).getPriorDbkeyPosition();
		verify(role, never()).getOwnerDbkeyPosition();
		verify(role, never()).getIndexDbkeyPosition();
		
	}
	
	@Test
	public void testMemberNextPointerThatIsNotSet() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("REC1", "SET1");		
		
		Pointer<MemberRole> pointer = PointerFactory.newPointer(role, MEMBER_NEXT);
		assertSame(role, pointer.role);
		assertSame(MEMBER_NEXT, pointer.getType());
		assertEquals("REC1", pointer.getRecordName());
		assertEquals("SET1", pointer.getSetName());
		assertFalse(pointer.isOwnerDefined());
		assertTrue(pointer.isMemberDefined());
		
		assertNull(pointer.getCurrentPositionInPrefix());
		
		try {
			pointer.getOwnerRole();
			fail("should throw an UnsupportedOperationException because the role is a member role");
		} catch (UnsupportedOperationException e) {
			assertEquals("not an owner type pointer", e.getMessage());
		}
		
		assertSame(role, pointer.getMemberRole());		
		
		verify(role, times(1)).getRecord();
		verify(role.getRecord(), times(1)).getName();
		verify(role, times(1)).getSet();
		verify(role.getSet(), times(1)).getName();
		verify(role, times(1)).getNextDbkeyPosition();
		verify(role, never()).getPriorDbkeyPosition();
		verify(role, never()).getOwnerDbkeyPosition();
		verify(role, never()).getIndexDbkeyPosition();
		
	}
	
	@Test
	public void testMemberPriorPointerThatIsSet() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("REC1", "SET1");
		Short positionInPrefix = Short.valueOf((short) 7);
		when(role.getPriorDbkeyPosition()).thenReturn(positionInPrefix);
		
		Pointer<MemberRole> pointer = PointerFactory.newPointer(role, MEMBER_PRIOR);
		assertSame(role, pointer.role);
		assertSame(MEMBER_PRIOR, pointer.getType());
		assertEquals("REC1", pointer.getRecordName());
		assertEquals("SET1", pointer.getSetName());
		assertFalse(pointer.isOwnerDefined());
		assertTrue(pointer.isMemberDefined());
		
		assertSame(positionInPrefix, pointer.getCurrentPositionInPrefix());
		
		try {
			pointer.getOwnerRole();
			fail("should throw an UnsupportedOperationException because the role is a member role");
		} catch (UnsupportedOperationException e) {
			assertEquals("not an owner type pointer", e.getMessage());
		}
		
		assertSame(role, pointer.getMemberRole());		
		
		verify(role, times(1)).getRecord();
		verify(role.getRecord(), times(1)).getName();
		verify(role, times(1)).getSet();
		verify(role.getSet(), times(1)).getName();
		verify(role, never()).getNextDbkeyPosition();
		verify(role, times(1)).getPriorDbkeyPosition();
		verify(role, never()).getOwnerDbkeyPosition();
		verify(role, never()).getIndexDbkeyPosition();
		
	}
	
	@Test
	public void testMemberPriorPointerThatIsNotSet() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("REC1", "SET1");		
		
		Pointer<MemberRole> pointer = PointerFactory.newPointer(role, MEMBER_PRIOR);
		assertSame(role, pointer.role);
		assertSame(MEMBER_PRIOR, pointer.getType());
		assertEquals("REC1", pointer.getRecordName());
		assertEquals("SET1", pointer.getSetName());
		assertFalse(pointer.isOwnerDefined());
		assertTrue(pointer.isMemberDefined());
		
		assertNull(pointer.getCurrentPositionInPrefix());
		
		try {
			pointer.getOwnerRole();
			fail("should throw an UnsupportedOperationException because the role is a member role");
		} catch (UnsupportedOperationException e) {
			assertEquals("not an owner type pointer", e.getMessage());
		}
		
		assertSame(role, pointer.getMemberRole());		
		
		verify(role, times(1)).getRecord();
		verify(role.getRecord(), times(1)).getName();
		verify(role, times(1)).getSet();
		verify(role.getSet(), times(1)).getName();
		verify(role, never()).getNextDbkeyPosition();
		verify(role, times(1)).getPriorDbkeyPosition();
		verify(role, never()).getOwnerDbkeyPosition();
		verify(role, never()).getIndexDbkeyPosition();
		
	}
	
	@Test
	public void testMemberOwnerPointerThatIsSet() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("REC1", "SET1");
		Short positionInPrefix = Short.valueOf((short) 3);
		when(role.getOwnerDbkeyPosition()).thenReturn(positionInPrefix);
		
		Pointer<MemberRole> pointer = PointerFactory.newPointer(role, MEMBER_OWNER);
		assertSame(role, pointer.role);
		assertSame(MEMBER_OWNER, pointer.getType());
		assertEquals("REC1", pointer.getRecordName());
		assertEquals("SET1", pointer.getSetName());
		assertFalse(pointer.isOwnerDefined());
		assertTrue(pointer.isMemberDefined());
		
		assertSame(positionInPrefix, pointer.getCurrentPositionInPrefix());
		
		try {
			pointer.getOwnerRole();
			fail("should throw an UnsupportedOperationException because the role is a member role");
		} catch (UnsupportedOperationException e) {
			assertEquals("not an owner type pointer", e.getMessage());
		}
		
		assertSame(role, pointer.getMemberRole());		
		
		verify(role, times(1)).getRecord();
		verify(role.getRecord(), times(1)).getName();
		verify(role, times(1)).getSet();
		verify(role.getSet(), times(1)).getName();
		verify(role, never()).getNextDbkeyPosition();
		verify(role, never()).getPriorDbkeyPosition();
		verify(role, times(1)).getOwnerDbkeyPosition();
		verify(role, never()).getIndexDbkeyPosition();
		
	}
	
	@Test
	public void testMemberOwnerPointerThatIsNotSet() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("REC1", "SET1");		
		
		Pointer<MemberRole> pointer = PointerFactory.newPointer(role, MEMBER_OWNER);
		assertSame(role, pointer.role);
		assertSame(MEMBER_OWNER, pointer.getType());
		assertEquals("REC1", pointer.getRecordName());
		assertEquals("SET1", pointer.getSetName());
		assertFalse(pointer.isOwnerDefined());
		assertTrue(pointer.isMemberDefined());
		
		assertNull(pointer.getCurrentPositionInPrefix());
		
		try {
			pointer.getOwnerRole();
			fail("should throw an UnsupportedOperationException because the role is a member role");
		} catch (UnsupportedOperationException e) {
			assertEquals("not an owner type pointer", e.getMessage());
		}
		
		assertSame(role, pointer.getMemberRole());		
		
		verify(role, times(1)).getRecord();
		verify(role.getRecord(), times(1)).getName();
		verify(role, times(1)).getSet();
		verify(role.getSet(), times(1)).getName();
		verify(role, never()).getNextDbkeyPosition();
		verify(role, never()).getPriorDbkeyPosition();
		verify(role, times(1)).getOwnerDbkeyPosition();
		verify(role, never()).getIndexDbkeyPosition();
		
	}
	
	@Test
	public void testMemberIndexPointerThatIsSet() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("REC1", "SET1");
		Short positionInPrefix = Short.valueOf((short) 5);
		when(role.getIndexDbkeyPosition()).thenReturn(positionInPrefix);
		
		Pointer<MemberRole> pointer = PointerFactory.newPointer(role, MEMBER_INDEX);
		assertSame(role, pointer.role);
		assertSame(MEMBER_INDEX, pointer.getType());
		assertEquals("REC1", pointer.getRecordName());
		assertEquals("SET1", pointer.getSetName());
		assertFalse(pointer.isOwnerDefined());
		assertTrue(pointer.isMemberDefined());
		
		assertSame(positionInPrefix, pointer.getCurrentPositionInPrefix());
		
		try {
			pointer.getOwnerRole();
			fail("should throw an UnsupportedOperationException because the role is a member role");
		} catch (UnsupportedOperationException e) {
			assertEquals("not an owner type pointer", e.getMessage());
		}
		
		assertSame(role, pointer.getMemberRole());		
		
		verify(role, times(1)).getRecord();
		verify(role.getRecord(), times(1)).getName();
		verify(role, times(1)).getSet();
		verify(role.getSet(), times(1)).getName();
		verify(role, never()).getNextDbkeyPosition();
		verify(role, never()).getPriorDbkeyPosition();
		verify(role, never()).getOwnerDbkeyPosition();
		verify(role, times(1)).getIndexDbkeyPosition();
		
	}
	
	@Test
	public void testMemberIndexPointerThatIsNotSet() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("REC1", "SET1");		
		
		Pointer<MemberRole> pointer = PointerFactory.newPointer(role, MEMBER_INDEX);
		assertSame(role, pointer.role);
		assertSame(MEMBER_INDEX, pointer.getType());
		assertEquals("REC1", pointer.getRecordName());
		assertEquals("SET1", pointer.getSetName());
		assertFalse(pointer.isOwnerDefined());
		assertTrue(pointer.isMemberDefined());
		
		assertNull(pointer.getCurrentPositionInPrefix());
		
		try {
			pointer.getOwnerRole();
			fail("should throw an UnsupportedOperationException because the role is a member role");
		} catch (UnsupportedOperationException e) {
			assertEquals("not an owner type pointer", e.getMessage());
		}
		
		assertSame(role, pointer.getMemberRole());		
		
		verify(role, times(1)).getRecord();
		verify(role.getRecord(), times(1)).getName();
		verify(role, times(1)).getSet();
		verify(role.getSet(), times(1)).getName();
		verify(role, never()).getNextDbkeyPosition();
		verify(role, never()).getPriorDbkeyPosition();
		verify(role, never()).getOwnerDbkeyPosition();
		verify(role, times(1)).getIndexDbkeyPosition();
		
	}	

}
