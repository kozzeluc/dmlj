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
import static org.junit.Assert.assertNull;
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
import static org.mockito.Matchers.any;

import org.junit.Test;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Role;

public class PointerUtilTest extends AbstractPointerOrPrefixRelatedTestCase {
	
	private void assertWrongPointerTypeWhenGettingPositionInPrefix(Role role, PointerType type) {
		try {
			PointerUtil.getPositionInPrefix(role, type);
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("no pointer of type " + type + " for " + role, e.getMessage());
		}
		
	}
	
	private void assertWrongPointerTypeWhenSettingPositionInPrefix(Role role, PointerType type) {
		try {
			PointerUtil.setPositionInPrefix(role, type, (short) 5);
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("no pointer of type " + type + " for " + role, e.getMessage());
		}
		
	}	
	
	@Test
	public void testIsPointerTypeValid() {
	
		assertTrue(PointerUtil.isPointerTypeValid(mockOwnerRoleWithNoPointersSet("A", "B"), 
												  OWNER_NEXT));		
		assertTrue(PointerUtil.isPointerTypeValid(mockOwnerRoleWithNoPointersSet("A", "B"), 
				  								  OWNER_PRIOR));
		
		assertTrue(PointerUtil.isPointerTypeValid(mockMemberRoleWithNoPointersSet("A", "B"), 
				  								  MEMBER_NEXT));		
		assertTrue(PointerUtil.isPointerTypeValid(mockMemberRoleWithNoPointersSet("A", "B"), 
				  								  MEMBER_PRIOR));
		assertTrue(PointerUtil.isPointerTypeValid(mockMemberRoleWithNoPointersSet("A", "B"), 
				  								  MEMBER_OWNER));
		assertTrue(PointerUtil.isPointerTypeValid(mockMemberRoleWithNoPointersSet("A", "B"), 
				  								  MEMBER_INDEX));		
		
		assertFalse(PointerUtil.isPointerTypeValid(mockOwnerRoleWithNoPointersSet("A", "B"), 
												   MEMBER_NEXT));		
		assertFalse(PointerUtil.isPointerTypeValid(mockOwnerRoleWithNoPointersSet("A", "B"), 
												   MEMBER_PRIOR));
		assertFalse(PointerUtil.isPointerTypeValid(mockOwnerRoleWithNoPointersSet("A", "B"), 
												   MEMBER_OWNER));
		assertFalse(PointerUtil.isPointerTypeValid(mockOwnerRoleWithNoPointersSet("A", "B"), 
												   MEMBER_INDEX));
		assertFalse(PointerUtil.isPointerTypeValid(mockMemberRoleWithNoPointersSet("A", "B"), 
												   OWNER_NEXT));
		assertFalse(PointerUtil.isPointerTypeValid(mockMemberRoleWithNoPointersSet("A", "B"), 
												   OWNER_PRIOR));		
		
	}
	
	@Test
	public void testIsPositionInPrefixValid() {
		for (short positionInPrefix = Short.MIN_VALUE; positionInPrefix < Short.MAX_VALUE; 
			 positionInPrefix++) {
			
			if (positionInPrefix > 0 && positionInPrefix <= 8180) {
				assertTrue(PointerUtil.isPositionInPrefixValid(positionInPrefix));
			} else {
				assertFalse(PointerUtil.isPositionInPrefixValid(positionInPrefix));
			}
		}
		// take care of Short.MAX_VALUE, which we skipped to avoid an endless loop
		assertFalse(PointerUtil.isPositionInPrefixValid(Short.MAX_VALUE));
	}
	
	@Test
	public void testGetPositionInPrefix_WrongPointerTypes() {
		
		assertWrongPointerTypeWhenGettingPositionInPrefix(mockOwnerRoleWithNoPointersSet("A", "B"), 
														  MEMBER_NEXT);		
		assertWrongPointerTypeWhenGettingPositionInPrefix(mockOwnerRoleWithNoPointersSet("A", "B"), 
														  MEMBER_PRIOR);
		assertWrongPointerTypeWhenGettingPositionInPrefix(mockOwnerRoleWithNoPointersSet("A", "B"), 
														  MEMBER_OWNER);
		assertWrongPointerTypeWhenGettingPositionInPrefix(mockOwnerRoleWithNoPointersSet("A", "B"), 
														  MEMBER_INDEX);
		assertWrongPointerTypeWhenGettingPositionInPrefix(mockMemberRoleWithNoPointersSet("A", "B"), 
														  OWNER_NEXT);
		assertWrongPointerTypeWhenGettingPositionInPrefix(mockMemberRoleWithNoPointersSet("A", "B"), 
														  OWNER_PRIOR);		
		
	}
	
	@Test
	public void testGetPositionInPrefix_OwnerNext() {
		
		OwnerRole role = mockOwnerRoleWithNoPointersSet("A", "B");
		when(role.getNextDbkeyPosition()).thenReturn((short) 3);
		assertEquals(Short.valueOf((short) 3), PointerUtil.getPositionInPrefix(role, OWNER_NEXT));
		
		verify(role, times(1)).getNextDbkeyPosition();
		verify(role, never()).getPriorDbkeyPosition();
		
		
		role = mockOwnerRoleWithNoPointersSet("A", "B");		
		assertNull(PointerUtil.getPositionInPrefix(role, OWNER_NEXT));
		
		verify(role, times(1)).getNextDbkeyPosition();
		verify(role, never()).getPriorDbkeyPosition();		
		
	}
	
	@Test
	public void testGetPositionInPrefix_OwnerPrior() {
		
		OwnerRole role = mockOwnerRoleWithNoPointersSet("A", "B");
		when(role.getPriorDbkeyPosition()).thenReturn((short) 5);
		assertEquals(Short.valueOf((short) 5), PointerUtil.getPositionInPrefix(role, OWNER_PRIOR));
		
		verify(role, never()).getNextDbkeyPosition();
		verify(role, times(1)).getPriorDbkeyPosition();
		
		
		role = mockOwnerRoleWithNoPointersSet("A", "B");		
		assertNull(PointerUtil.getPositionInPrefix(role, OWNER_PRIOR));
		
		verify(role, never()).getNextDbkeyPosition();
		verify(role, times(1)).getPriorDbkeyPosition();	
		
	}
	
	@Test
	public void testGetPositionInPrefix_MemberNext() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
		when(role.getNextDbkeyPosition()).thenReturn((short) 9);
		assertEquals(Short.valueOf((short) 9), PointerUtil.getPositionInPrefix(role, MEMBER_NEXT));
		
		verify(role, times(1)).getNextDbkeyPosition();
		verify(role, never()).getPriorDbkeyPosition();
		verify(role, never()).getOwnerDbkeyPosition();	
		verify(role, never()).getIndexDbkeyPosition();	
		
		
		role = mockMemberRoleWithNoPointersSet("A", "B");		
		assertNull(PointerUtil.getPositionInPrefix(role, MEMBER_NEXT));
		
		verify(role, times(1)).getNextDbkeyPosition();
		verify(role, never()).getPriorDbkeyPosition();
		verify(role, never()).getOwnerDbkeyPosition();	
		verify(role, never()).getIndexDbkeyPosition();	
		
	}
	
	@Test
	public void testGetPositionInPrefix_MemberPrior() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
		when(role.getPriorDbkeyPosition()).thenReturn((short) 2);
		assertEquals(Short.valueOf((short) 2), PointerUtil.getPositionInPrefix(role, MEMBER_PRIOR));
		
		verify(role, never()).getNextDbkeyPosition();
		verify(role, times(1)).getPriorDbkeyPosition();
		verify(role, never()).getOwnerDbkeyPosition();	
		verify(role, never()).getIndexDbkeyPosition();	
		
		
		role = mockMemberRoleWithNoPointersSet("A", "B");		
		assertNull(PointerUtil.getPositionInPrefix(role, MEMBER_PRIOR));
		
		verify(role, never()).getNextDbkeyPosition();
		verify(role, times(1)).getPriorDbkeyPosition();
		verify(role, never()).getOwnerDbkeyPosition();	
		verify(role, never()).getIndexDbkeyPosition();	
		
	}
	
	@Test
	public void testGetPositionInPrefix_MemberOwner() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
		when(role.getOwnerDbkeyPosition()).thenReturn((short) 1);
		assertEquals(Short.valueOf((short) 1), PointerUtil.getPositionInPrefix(role, MEMBER_OWNER));
		
		verify(role, never()).getNextDbkeyPosition();
		verify(role, never()).getPriorDbkeyPosition();
		verify(role, times(1)).getOwnerDbkeyPosition();	
		verify(role, never()).getIndexDbkeyPosition();	
		
		
		role = mockMemberRoleWithNoPointersSet("A", "B");		
		assertNull(PointerUtil.getPositionInPrefix(role, MEMBER_OWNER));
		
		verify(role, never()).getNextDbkeyPosition();
		verify(role, never()).getPriorDbkeyPosition();
		verify(role, times(1)).getOwnerDbkeyPosition();	
		verify(role, never()).getIndexDbkeyPosition();	
		
	}
	
	@Test
	public void testGetPositionInPrefix_MemberIndex() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
		when(role.getIndexDbkeyPosition()).thenReturn((short) 1);
		assertEquals(Short.valueOf((short) 1), PointerUtil.getPositionInPrefix(role, MEMBER_INDEX));
		
		verify(role, never()).getNextDbkeyPosition();
		verify(role, never()).getPriorDbkeyPosition();
		verify(role, never()).getOwnerDbkeyPosition();	
		verify(role, times(1)).getIndexDbkeyPosition();	
		
		
		role = mockMemberRoleWithNoPointersSet("A", "B");		
		assertNull(PointerUtil.getPositionInPrefix(role, MEMBER_INDEX));
		
		verify(role, never()).getNextDbkeyPosition();
		verify(role, never()).getPriorDbkeyPosition();
		verify(role, never()).getOwnerDbkeyPosition();	
		verify(role, times(1)).getIndexDbkeyPosition();	
		
	}	
	
	@Test
	public void testSetPositionInPrefix_WrongPointerTypes() {
		
		assertWrongPointerTypeWhenSettingPositionInPrefix(mockOwnerRoleWithNoPointersSet("A", "B"), 
														  MEMBER_NEXT);		
		assertWrongPointerTypeWhenSettingPositionInPrefix(mockOwnerRoleWithNoPointersSet("A", "B"), 
														  MEMBER_PRIOR);
		assertWrongPointerTypeWhenSettingPositionInPrefix(mockOwnerRoleWithNoPointersSet("A", "B"), 
														  MEMBER_OWNER);
		assertWrongPointerTypeWhenSettingPositionInPrefix(mockOwnerRoleWithNoPointersSet("A", "B"), 
														  MEMBER_INDEX);
		assertWrongPointerTypeWhenSettingPositionInPrefix(mockMemberRoleWithNoPointersSet("A", "B"), 
														  OWNER_NEXT);
		assertWrongPointerTypeWhenSettingPositionInPrefix(mockMemberRoleWithNoPointersSet("A", "B"), 
														  OWNER_PRIOR);		
		
	}
	
	@Test
	public void testSetPositionInPrefix_OwnerNext() {
		
		OwnerRole role = mockOwnerRoleWithNoPointersSet("A", "B");
		
		PointerUtil.setPositionInPrefix(role, OWNER_NEXT, (short) 7);		
		
		verify(role, times(1)).setNextDbkeyPosition((short) 7);
		verify(role, times(1)).setNextDbkeyPosition(any(Short.class));
		verify(role, never()).setPriorDbkeyPosition(any(Short.class));
		
		
		role = mockOwnerRoleWithNoPointersSet("A", "B");		
		when(role.getNextDbkeyPosition()).thenReturn((short) 7);
		
		PointerUtil.setPositionInPrefix(role, OWNER_NEXT, null);		
		
		verify(role, times(1)).setNextDbkeyPosition((short) 0);
		verify(role, times(1)).setNextDbkeyPosition(any(Short.class));
		verify(role, never()).setPriorDbkeyPosition(any(Short.class));	
		
	}
	
	@Test
	public void testSetPositionInPrefix_OwnerPrior() {
		
		OwnerRole role = mockOwnerRoleWithNoPointersSet("A", "B");
		
		Short newPositionInPrefix = Short.valueOf((short) 7);
		PointerUtil.setPositionInPrefix(role, OWNER_PRIOR, newPositionInPrefix);		
		
		verify(role, never()).setNextDbkeyPosition(any(Short.class));
		verify(role, times(1)).setPriorDbkeyPosition(newPositionInPrefix);
		verify(role, times(1)).setPriorDbkeyPosition(any(Short.class));		
		
		
		role = mockOwnerRoleWithNoPointersSet("A", "B");		
		when(role.getNextDbkeyPosition()).thenReturn((short) 7);
		
		PointerUtil.setPositionInPrefix(role, OWNER_PRIOR, null);		
		
		verify(role, never()).setNextDbkeyPosition(any(Short.class));
		verify(role, times(1)).setPriorDbkeyPosition(null);
		verify(role, times(1)).setPriorDbkeyPosition(any(Short.class));			
		
	}
	
	@Test
	public void testSetPositionInPrefix_MemberNext() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
		
		Short newPositionInPrefix = Short.valueOf((short) 7);
		PointerUtil.setPositionInPrefix(role, MEMBER_NEXT, newPositionInPrefix);		
		
		verify(role, times(1)).setNextDbkeyPosition(newPositionInPrefix);
		verify(role, times(1)).setNextDbkeyPosition(any(Short.class));
		verify(role, never()).setPriorDbkeyPosition(any(Short.class));
		verify(role, never()).setOwnerDbkeyPosition(any(Short.class));
		verify(role, never()).setIndexDbkeyPosition(any(Short.class));
		
		
		role = mockMemberRoleWithNoPointersSet("A", "B");		
		when(role.getNextDbkeyPosition()).thenReturn((short) 7);
		
		PointerUtil.setPositionInPrefix(role, MEMBER_NEXT, null);		
		
		verify(role, times(1)).setNextDbkeyPosition(null);
		verify(role, times(1)).setNextDbkeyPosition(any(Short.class));
		verify(role, never()).setPriorDbkeyPosition(any(Short.class));
		verify(role, never()).setOwnerDbkeyPosition(any(Short.class));
		verify(role, never()).setIndexDbkeyPosition(any(Short.class));	
		
	}
	
	@Test
	public void testSetPositionInPrefix_MemberPrior() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
		
		Short newPositionInPrefix = Short.valueOf((short) 7);
		PointerUtil.setPositionInPrefix(role, MEMBER_PRIOR, newPositionInPrefix);		
		
		verify(role, never()).setNextDbkeyPosition(any(Short.class));
		verify(role, times(1)).setPriorDbkeyPosition(newPositionInPrefix);
		verify(role, times(1)).setPriorDbkeyPosition(any(Short.class));
		verify(role, never()).setOwnerDbkeyPosition(any(Short.class));
		verify(role, never()).setIndexDbkeyPosition(any(Short.class));
		
		
		role = mockMemberRoleWithNoPointersSet("A", "B");		
		when(role.getPriorDbkeyPosition()).thenReturn((short) 7);
		
		PointerUtil.setPositionInPrefix(role, MEMBER_PRIOR, null);		
		
		verify(role, never()).setNextDbkeyPosition(any(Short.class));
		verify(role, times(1)).setPriorDbkeyPosition(null);
		verify(role, times(1)).setPriorDbkeyPosition(any(Short.class));
		verify(role, never()).setOwnerDbkeyPosition(any(Short.class));
		verify(role, never()).setIndexDbkeyPosition(any(Short.class));	
		
	}
	
	@Test
	public void testSetPositionInPrefix_MemberOwner() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
		
		Short newPositionInPrefix = Short.valueOf((short) 7);
		PointerUtil.setPositionInPrefix(role, MEMBER_OWNER, newPositionInPrefix);		
		
		verify(role, never()).setNextDbkeyPosition(any(Short.class));
		verify(role, never()).setPriorDbkeyPosition(any(Short.class));
		verify(role, times(1)).setOwnerDbkeyPosition(newPositionInPrefix);
		verify(role, times(1)).setOwnerDbkeyPosition(any(Short.class));
		verify(role, never()).setIndexDbkeyPosition(any(Short.class));
		
		
		role = mockMemberRoleWithNoPointersSet("A", "B");		
		when(role.getOwnerDbkeyPosition()).thenReturn((short) 7);
		
		PointerUtil.setPositionInPrefix(role, MEMBER_OWNER, null);		
		
		verify(role, never()).setNextDbkeyPosition(any(Short.class));
		verify(role, never()).setPriorDbkeyPosition(any(Short.class));
		verify(role, times(1)).setOwnerDbkeyPosition(null);
		verify(role, times(1)).setOwnerDbkeyPosition(any(Short.class));
		verify(role, never()).setIndexDbkeyPosition(any(Short.class));	
		
	}
	
	@Test
	public void testSetPositionInPrefix_MemberIndex() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
		
		Short newPositionInPrefix = Short.valueOf((short) 7);
		PointerUtil.setPositionInPrefix(role, MEMBER_INDEX, newPositionInPrefix);		
		
		verify(role, never()).setNextDbkeyPosition(any(Short.class));
		verify(role, never()).setPriorDbkeyPosition(any(Short.class));		
		verify(role, never()).setOwnerDbkeyPosition(any(Short.class));
		verify(role, times(1)).setIndexDbkeyPosition(newPositionInPrefix);
		verify(role, times(1)).setIndexDbkeyPosition(any(Short.class));
		
		
		role = mockMemberRoleWithNoPointersSet("A", "B");		
		when(role.getOwnerDbkeyPosition()).thenReturn((short) 7);
		
		PointerUtil.setPositionInPrefix(role, MEMBER_INDEX, null);		
		
		verify(role, never()).setNextDbkeyPosition(any(Short.class));
		verify(role, never()).setPriorDbkeyPosition(any(Short.class));
		verify(role, never()).setOwnerDbkeyPosition(any(Short.class));
		verify(role, times(1)).setIndexDbkeyPosition(null);
		verify(role, times(1)).setIndexDbkeyPosition(any(Short.class));	
		
	}	
	
}
