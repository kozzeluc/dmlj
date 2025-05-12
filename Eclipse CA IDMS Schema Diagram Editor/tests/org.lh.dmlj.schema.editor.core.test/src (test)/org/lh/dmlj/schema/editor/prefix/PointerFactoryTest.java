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

import static org.junit.Assert.*;
import static org.lh.dmlj.schema.editor.prefix.PointerType.MEMBER_INDEX;
import static org.lh.dmlj.schema.editor.prefix.PointerType.MEMBER_NEXT;
import static org.lh.dmlj.schema.editor.prefix.PointerType.MEMBER_OWNER;
import static org.lh.dmlj.schema.editor.prefix.PointerType.MEMBER_PRIOR;
import static org.lh.dmlj.schema.editor.prefix.PointerType.OWNER_NEXT;
import static org.lh.dmlj.schema.editor.prefix.PointerType.OWNER_PRIOR;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Role;

public class PointerFactoryTest extends AbstractPointerOrPrefixRelatedTestCase {

	private void assertWrongPointerTypeWithNewPointer(Role role, PointerType type) {
		try {
			PointerFactory.newPointer(role, type);
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("no pointer of type " + type + " for " + role, e.getMessage());
		}
		
	}
	
	private void assertWrongPointerTypeWithNewPointerToMove(Role role, PointerType type) {
		try {
			PointerFactory.newPointerToMove(role, type, (short) 5);
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("no pointer of type " + type + " for " + role, e.getMessage());
		}
		
	}	
	
	private void assertWrongPointerTypeWithNewPointerToSet(Role role, PointerType type) {
		try {
			PointerFactory.newPointerToSet(role, type, (short) 5);
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("no pointer of type " + type + " for " + role, e.getMessage());
		}
		
	}
	
	private void assertWrongPointerTypeWithNewPointerToUnset(Role role, PointerType type) {
		try {
			PointerFactory.newPointerToUnset(role, type);
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("no pointer of type " + type + " for " + role, e.getMessage());
		}
		
	}	
	
	@Test
	public void testWrongPointers() {		
		
		// make sure invalid pointer types always throw an IllegalArgumentException when creating a
		// pointer of any kind
		
		// regular pointers
		assertWrongPointerTypeWithNewPointer(mockOwnerRoleWithNoPointersSet("A", "B"), MEMBER_NEXT);		
		assertWrongPointerTypeWithNewPointer(mockOwnerRoleWithNoPointersSet("A", "B"), MEMBER_PRIOR);
		assertWrongPointerTypeWithNewPointer(mockOwnerRoleWithNoPointersSet("A", "B"), MEMBER_OWNER);
		assertWrongPointerTypeWithNewPointer(mockOwnerRoleWithNoPointersSet("A", "B"), MEMBER_INDEX);
		assertWrongPointerTypeWithNewPointer(mockMemberRoleWithNoPointersSet("A", "B"), OWNER_NEXT);
		assertWrongPointerTypeWithNewPointer(mockMemberRoleWithNoPointersSet("A", "B"), OWNER_PRIOR);
		
		// pointers to set
		assertWrongPointerTypeWithNewPointerToSet(mockOwnerRoleWithNoPointersSet("A", "B"), 
												  MEMBER_NEXT);		
		assertWrongPointerTypeWithNewPointerToSet(mockOwnerRoleWithNoPointersSet("A", "B"), 
												  MEMBER_PRIOR);
		assertWrongPointerTypeWithNewPointerToSet(mockOwnerRoleWithNoPointersSet("A", "B"), 
												  MEMBER_OWNER);
		assertWrongPointerTypeWithNewPointerToSet(mockOwnerRoleWithNoPointersSet("A", "B"), 
												  MEMBER_INDEX);
		assertWrongPointerTypeWithNewPointerToSet(mockMemberRoleWithNoPointersSet("A", "B"), 
												  OWNER_NEXT);
		assertWrongPointerTypeWithNewPointerToSet(mockMemberRoleWithNoPointersSet("A", "B"), 
												  OWNER_PRIOR);
		
		// pointers to unset
		assertWrongPointerTypeWithNewPointerToUnset(mockOwnerRoleWithNoPointersSet("A", "B"), 
												   MEMBER_NEXT);		
		assertWrongPointerTypeWithNewPointerToUnset(mockOwnerRoleWithNoPointersSet("A", "B"), 
												   MEMBER_PRIOR);
		assertWrongPointerTypeWithNewPointerToUnset(mockOwnerRoleWithNoPointersSet("A", "B"), 
												   MEMBER_OWNER);
		assertWrongPointerTypeWithNewPointerToUnset(mockOwnerRoleWithNoPointersSet("A", "B"), 
												   MEMBER_INDEX);
		assertWrongPointerTypeWithNewPointerToUnset(mockMemberRoleWithNoPointersSet("A", "B"), 
												   OWNER_NEXT);
		assertWrongPointerTypeWithNewPointerToUnset(mockMemberRoleWithNoPointersSet("A", "B"), 
												   OWNER_PRIOR);
		
		// pointers to move
		assertWrongPointerTypeWithNewPointerToMove(mockOwnerRoleWithNoPointersSet("A", "B"), 
												   MEMBER_NEXT);		
		assertWrongPointerTypeWithNewPointerToMove(mockOwnerRoleWithNoPointersSet("A", "B"), 
												   MEMBER_PRIOR);
		assertWrongPointerTypeWithNewPointerToMove(mockOwnerRoleWithNoPointersSet("A", "B"), 
												   MEMBER_OWNER);
		assertWrongPointerTypeWithNewPointerToMove(mockOwnerRoleWithNoPointersSet("A", "B"), 
												   MEMBER_INDEX);
		assertWrongPointerTypeWithNewPointerToMove(mockMemberRoleWithNoPointersSet("A", "B"), 
												   OWNER_NEXT);
		assertWrongPointerTypeWithNewPointerToMove(mockMemberRoleWithNoPointersSet("A", "B"), 
												   OWNER_PRIOR);		
		
	}
	
	@Test
	public void testNewPointerForOwnerRole() {
		OwnerRole role = mockOwnerRoleWithNoPointersSet("A", "B");
		Pointer<OwnerRole> pointer = PointerFactory.newPointer(role, OWNER_NEXT);
		assertNotNull(pointer);
		assertSame(role, pointer.getRole());
		assertSame(OWNER_NEXT, pointer.getType());
	}
	
	@Test
	public void testNewPointerForMemberRole() {
		MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
		Pointer<MemberRole> pointer = PointerFactory.newPointer(role, MEMBER_NEXT);
		assertNotNull(pointer);
		assertSame(role, pointer.getRole());
		assertSame(MEMBER_NEXT, pointer.getType());
	}
	
	@Test
	public void testPointerToSetThatIsAlreadySet() {		
		try {
			OwnerRole role = mockOwnerRoleWithNoPointersSet("A", "B");
			when(role.getNextDbkeyPosition()).thenReturn((short) 3);
			PointerFactory.newPointerToSet(role, OWNER_NEXT, (short) 7);
			fail("should throw an IllegalArgumentException because the pointer is already set");
		} catch (IllegalArgumentException e) {
			assertEquals("already set: " + OWNER_NEXT, e.getMessage());
		}
		try {
			OwnerRole role = mockOwnerRoleWithNoPointersSet("A", "B");
			when(role.getPriorDbkeyPosition()).thenReturn(Short.valueOf((short) 5));
			PointerFactory.newPointerToSet(role, OWNER_PRIOR, (short) 7);
			fail("should throw an IllegalArgumentException because the pointer is already set");
		} catch (IllegalArgumentException e) {
			assertEquals("already set: " + OWNER_PRIOR, e.getMessage());
		}
		try {
			MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
			when(role.getNextDbkeyPosition()).thenReturn(Short.valueOf((short) 5));
			PointerFactory.newPointerToSet(role, MEMBER_NEXT, (short) 7);
			fail("should throw an IllegalArgumentException because the pointer is already set");
		} catch (IllegalArgumentException e) {
			assertEquals("already set: " + MEMBER_NEXT, e.getMessage());
		}
		try {
			MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
			when(role.getPriorDbkeyPosition()).thenReturn(Short.valueOf((short) 5));
			PointerFactory.newPointerToSet(role, MEMBER_PRIOR, (short) 7);
			fail("should throw an IllegalArgumentException because the pointer is already set");
		} catch (IllegalArgumentException e) {
			assertEquals("already set: " + MEMBER_PRIOR, e.getMessage());
		}
		try {
			MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
			when(role.getOwnerDbkeyPosition()).thenReturn(Short.valueOf((short) 5));
			PointerFactory.newPointerToSet(role, MEMBER_OWNER, (short) 7);
			fail("should throw an IllegalArgumentException because the pointer is already set");
		} catch (IllegalArgumentException e) {
			assertEquals("already set: " + MEMBER_OWNER, e.getMessage());
		}
		try {
			MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
			when(role.getIndexDbkeyPosition()).thenReturn(Short.valueOf((short) 5));
			PointerFactory.newPointerToSet(role, MEMBER_INDEX, (short) 7);
			fail("should throw an IllegalArgumentException because the pointer is already set");
		} catch (IllegalArgumentException e) {
			assertEquals("already set: " + MEMBER_INDEX, e.getMessage());
		}
	}

	@Test
	public void testNewPointerToSetForOwnerRole() {
		OwnerRole role = mockOwnerRoleWithNoPointersSet("A", "B");
		PointerToSet<OwnerRole> pointer = 
			PointerFactory.newPointerToSet(role, OWNER_NEXT, (short) 13);
		assertNotNull(pointer);
		assertSame(role, pointer.getRole());
		assertSame(OWNER_NEXT, pointer.getType());
		assertEquals((short) 13, pointer.getPositionInPrefixToSet());
		assertNull(pointer.getCurrentPositionInPrefix());
	}
	
	@Test
	public void testNewPointerToSetForMemberRole() {
		MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
		PointerToSet<MemberRole> pointer = 
			PointerFactory.newPointerToSet(role, MEMBER_NEXT, Short.valueOf((short) 17));
		assertNotNull(pointer);
		assertSame(role, pointer.getRole());
		assertSame(MEMBER_NEXT, pointer.getType());
		assertSame(Short.valueOf((short) 17), pointer.getPositionInPrefixToSet());
		assertNull(pointer.getCurrentPositionInPrefix());
	}
	
	@Test
	public void testNewPointerToSetInvalidArgument() {
		
		try {
			PointerFactory.newPointerToSet(mockOwnerRoleWithNoPointersSet("A", "B"), OWNER_NEXT, 
										   (short) 0);
			fail("should throw an IllegalArgumentException because the new prefix position is invalid");
		} catch (IllegalArgumentException e) {
			assertEquals("positionInPrefixToSet must be a whole integer in the range 1 through 8180: 0", 
						 e.getMessage());
		}
		
		try {
			PointerFactory.newPointerToSet(mockOwnerRoleWithNoPointersSet("A", "B"), OWNER_NEXT, 
										   (short) 8181);
			fail("should throw an IllegalArgumentException because the new prefix position is invalid");
		} catch (IllegalArgumentException e) {
			assertEquals("positionInPrefixToSet must be a whole integer in the range 1 through 8180: 8181", 
						 e.getMessage());
		}		
		
	}
	
	@Test
	public void testPointerToUnsetThatIsNotSet() {
		try {
			OwnerRole role = mockOwnerRoleWithNoPointersSet("A", "B");
			PointerFactory.newPointerToUnset(role, OWNER_NEXT);
			fail("should throw an IllegalArgumentException because the pointer is already set");
		} catch (IllegalArgumentException e) {
			assertEquals("not set: " + OWNER_NEXT, e.getMessage());
		}
		try {
			OwnerRole role = mockOwnerRoleWithNoPointersSet("A", "B");
			PointerFactory.newPointerToUnset(role, OWNER_PRIOR);
			fail("should throw an IllegalArgumentException because the pointer is already set");
		} catch (IllegalArgumentException e) {
			assertEquals("not set: " + OWNER_PRIOR, e.getMessage());
		}
		try {
			MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
			PointerFactory.newPointerToUnset(role, MEMBER_NEXT);
			fail("should throw an IllegalArgumentException because the pointer is already set");
		} catch (IllegalArgumentException e) {
			assertEquals("not set: " + MEMBER_NEXT, e.getMessage());
		}
		try {
			MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
			PointerFactory.newPointerToUnset(role, MEMBER_PRIOR);
			fail("should throw an IllegalArgumentException because the pointer is already set");
		} catch (IllegalArgumentException e) {
			assertEquals("not set: " + MEMBER_PRIOR, e.getMessage());
		}
		try {
			MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
			PointerFactory.newPointerToUnset(role, MEMBER_OWNER);
			fail("should throw an IllegalArgumentException because the pointer is already set");
		} catch (IllegalArgumentException e) {
			assertEquals("not set: " + MEMBER_OWNER, e.getMessage());
		}
		try {
			MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
			PointerFactory.newPointerToUnset(role, MEMBER_INDEX);
			fail("should throw an IllegalArgumentException because the pointer is already set");
		} catch (IllegalArgumentException e) {
			assertEquals("not set: " + MEMBER_INDEX, e.getMessage());
		}
	}
	
	@Test
	public void testNewPointerToUnsetForOwnerRole() {
		OwnerRole role = mockOwnerRoleWithNoPointersSet("A", "B");
		Short positionInPrefix = Short.valueOf((short) 21);
		when(role.getPriorDbkeyPosition()).thenReturn(positionInPrefix);
		PointerToUnset<OwnerRole> pointer = PointerFactory.newPointerToUnset(role, OWNER_PRIOR);
		assertNotNull(pointer);
		assertSame(role, pointer.getRole());
		assertSame(OWNER_PRIOR, pointer.getType());
		assertSame(positionInPrefix, pointer.getCurrentPositionInPrefix());		
	}
	
	@Test
	public void testNewPointerToUnsetForMemberRole() {
		MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
		Short positionInPrefix = Short.valueOf((short) 19);
		when(role.getPriorDbkeyPosition()).thenReturn(positionInPrefix);
		PointerToUnset<MemberRole> pointer = PointerFactory.newPointerToUnset(role, MEMBER_PRIOR);
		assertNotNull(pointer);
		assertSame(role, pointer.getRole());
		assertSame(MEMBER_PRIOR, pointer.getType());
		assertSame(positionInPrefix, pointer.getCurrentPositionInPrefix());		
	}
	
	@Test
	public void testPointerToMoveThatIsNotSet() {		
		try {
			OwnerRole role = mockOwnerRoleWithNoPointersSet("A", "B");
			PointerFactory.newPointerToMove(role, OWNER_NEXT, (short) 7);
			fail("should throw an IllegalArgumentException because the pointer is already set");
		} catch (IllegalArgumentException e) {
			assertEquals("not set: " + OWNER_NEXT, e.getMessage());
		}
		try {
			OwnerRole role = mockOwnerRoleWithNoPointersSet("A", "B");
			PointerFactory.newPointerToMove(role, OWNER_PRIOR, (short) 7);
			fail("should throw an IllegalArgumentException because the pointer is already set");
		} catch (IllegalArgumentException e) {
			assertEquals("not set: " + OWNER_PRIOR, e.getMessage());
		}
		try {
			MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
			PointerFactory.newPointerToMove(role, MEMBER_NEXT, (short) 7);
			fail("should throw an IllegalArgumentException because the pointer is already set");
		} catch (IllegalArgumentException e) {
			assertEquals("not set: " + MEMBER_NEXT, e.getMessage());
		}
		try {
			MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
			PointerFactory.newPointerToMove(role, MEMBER_PRIOR, (short) 7);
			fail("should throw an IllegalArgumentException because the pointer is already set");
		} catch (IllegalArgumentException e) {
			assertEquals("not set: " + MEMBER_PRIOR, e.getMessage());
		}
		try {
			MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
			PointerFactory.newPointerToMove(role, MEMBER_OWNER, (short) 7);
			fail("should throw an IllegalArgumentException because the pointer is already set");
		} catch (IllegalArgumentException e) {
			assertEquals("not set: " + MEMBER_OWNER, e.getMessage());
		}
		try {
			MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
			PointerFactory.newPointerToMove(role, MEMBER_INDEX, (short) 7);
			fail("should throw an IllegalArgumentException because the pointer is already set");
		} catch (IllegalArgumentException e) {
			assertEquals("not set: " + MEMBER_INDEX, e.getMessage());
		}
	}
	
	@Test
	public void testNewPointerToMoveForOwnerRole() {
		OwnerRole role = mockOwnerRoleWithNoPointersSet("A", "B");
		when(role.getNextDbkeyPosition()).thenReturn((short) 7);
		PointerToMove<OwnerRole> pointer = 
			PointerFactory.newPointerToMove(role, OWNER_NEXT, (short) 13);
		assertNotNull(pointer);
		assertSame(role, pointer.getRole());
		assertSame(OWNER_NEXT, pointer.getType());
		assertEquals((short) 13, pointer.getNewPositionInPrefix());
		assertEquals((short) 7, pointer.getCurrentPositionInPrefix().shortValue());
	}
	
	@Test
	public void testNewPointerToMoveForMemberRole() {
		MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
		when(role.getNextDbkeyPosition()).thenReturn(Short.valueOf((short) 7));
		PointerToMove<MemberRole> pointer = 
			PointerFactory.newPointerToMove(role, MEMBER_NEXT, (short) 17);
		assertNotNull(pointer);
		assertSame(role, pointer.getRole());
		assertSame(MEMBER_NEXT, pointer.getType());
		assertEquals((short) 17, pointer.getNewPositionInPrefix());
		assertSame(Short.valueOf((short) 7), pointer.getCurrentPositionInPrefix());
	}
	
	@Test
	public void testNewPointerToMoveInvalidArgument() {
		
		try {
			OwnerRole role = mockOwnerRoleWithNoPointersSet("A", "B");
			when(role.getNextDbkeyPosition()).thenReturn((short) 1);
			PointerFactory.newPointerToMove(role, OWNER_NEXT, (short) 0);
			fail("should throw an IllegalArgumentException because the new prefix position is invalid");
		} catch (IllegalArgumentException e) {
			assertEquals("positionInPrefixToSet must be a whole integer in the range 1 through 8180: 0", 
						 e.getMessage());
		}
		
		try {
			OwnerRole role = mockOwnerRoleWithNoPointersSet("A", "B");
			when(role.getNextDbkeyPosition()).thenReturn((short) 1);
			PointerFactory.newPointerToMove(role, OWNER_NEXT, (short) 8181);
			fail("should throw an IllegalArgumentException because the new prefix position is invalid");
		} catch (IllegalArgumentException e) {
			assertEquals("positionInPrefixToSet must be a whole integer in the range 1 through 8180: 8181", 
						 e.getMessage());
		}		
		
	}

}
