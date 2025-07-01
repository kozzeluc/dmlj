/**
 * Copyright (C) 2025  Luc Hermans
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyShort;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Role;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.testtool.TestTools;

public class PrefixUtilTest extends AbstractPointerOrPrefixRelatedTestCase {
	
	private void assertWrongPointerTypeWhenGettingPositionInPrefix(Role role, PointerType type) {
		try {
			PrefixUtil.getPositionInPrefix(role, type);
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("no pointer of type " + type + " for " + role, e.getMessage());
		}
		
	}
	
	private void assertWrongPointerTypeWhenSettingPositionInPrefix(Role role, PointerType type) {
		try {
			PrefixUtil.setPositionInPrefix(role, type, (short) 5);
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("no pointer of type " + type + " for " + role, e.getMessage());
		}
		
	}	
	
	@Test
	public void testIsPointerTypeValid() {
	
		assertTrue(PrefixUtil.isPointerTypeValid(mockOwnerRoleWithNoPointersSet("A", "B"), 
												  OWNER_NEXT));		
		assertTrue(PrefixUtil.isPointerTypeValid(mockOwnerRoleWithNoPointersSet("A", "B"), 
				  								  OWNER_PRIOR));
		
		assertTrue(PrefixUtil.isPointerTypeValid(mockMemberRoleWithNoPointersSet("A", "B"), 
				  								  MEMBER_NEXT));		
		assertTrue(PrefixUtil.isPointerTypeValid(mockMemberRoleWithNoPointersSet("A", "B"), 
				  								  MEMBER_PRIOR));
		assertTrue(PrefixUtil.isPointerTypeValid(mockMemberRoleWithNoPointersSet("A", "B"), 
				  								  MEMBER_OWNER));
		assertTrue(PrefixUtil.isPointerTypeValid(mockMemberRoleWithNoPointersSet("A", "B"), 
				  								  MEMBER_INDEX));		
		
		assertFalse(PrefixUtil.isPointerTypeValid(mockOwnerRoleWithNoPointersSet("A", "B"), 
												   MEMBER_NEXT));		
		assertFalse(PrefixUtil.isPointerTypeValid(mockOwnerRoleWithNoPointersSet("A", "B"), 
												   MEMBER_PRIOR));
		assertFalse(PrefixUtil.isPointerTypeValid(mockOwnerRoleWithNoPointersSet("A", "B"), 
												   MEMBER_OWNER));
		assertFalse(PrefixUtil.isPointerTypeValid(mockOwnerRoleWithNoPointersSet("A", "B"), 
												   MEMBER_INDEX));
		assertFalse(PrefixUtil.isPointerTypeValid(mockMemberRoleWithNoPointersSet("A", "B"), 
												   OWNER_NEXT));
		assertFalse(PrefixUtil.isPointerTypeValid(mockMemberRoleWithNoPointersSet("A", "B"), 
												   OWNER_PRIOR));		
		
	}
	
	@Test
	public void testIsPositionInPrefixValid() {
		for (short positionInPrefix = Short.MIN_VALUE; positionInPrefix < Short.MAX_VALUE; 
			 positionInPrefix++) {
			
			if (positionInPrefix > 0 && positionInPrefix <= 8180) {
				assertTrue(PrefixUtil.isPositionInPrefixValid(positionInPrefix));
			} else {
				assertFalse(PrefixUtil.isPositionInPrefixValid(positionInPrefix));
			}
		}
		// take care of Short.MAX_VALUE, which we skipped to avoid an endless loop
		assertFalse(PrefixUtil.isPositionInPrefixValid(Short.MAX_VALUE));
	}
	
	@Test
	public void testGetPointer() {
		
		Schema schema = TestTools.getEmpschmSchema();
		SchemaRecord record = schema.getRecord("HOSPITAL-CLAIM");
		List<Pointer<?>> pointers = PrefixFactory.newPrefixForInquiry(record).getPointers();
		
		// existing pointer
		PointerDescription pointerDescription = 
			new PointerDescription("COVERAGE-CLAIMS", PointerType.MEMBER_NEXT);
		Pointer<?> pointer = PrefixUtil.getPointer(pointers, pointerDescription);
		assertNotNull(pointer);
		assertEquals("COVERAGE-CLAIMS", pointer.getSetName());
		assertSame(PointerType.MEMBER_NEXT, pointer.getType());
		
		// try a pointer that isn't there
		pointerDescription = new PointerDescription("COVERAGE-CLAIMS", PointerType.MEMBER_OWNER);
		try {
			PrefixUtil.getPointer(pointers, pointerDescription);
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("not found: COVERAGE-CLAIMS, MEMBER_OWNER", e.getMessage());
		}
	}
	
	@Test
	public void testGetPointerDescriptions() {
		
		Schema schema = TestTools.getEmpschmSchema();
		SchemaRecord record = schema.getRecord("HOSPITAL-CLAIM");
		
		List<PointerDescription> pointerDescriptions = PrefixUtil.getPointerDescriptions(record);
		assertNotNull(pointerDescriptions);
		assertEquals(2, pointerDescriptions.size());
		assertEquals("COVERAGE-CLAIMS", pointerDescriptions.get(0).getSetName());
		assertSame(PointerType.MEMBER_NEXT,pointerDescriptions.get(0).getPointerType());
		assertEquals("COVERAGE-CLAIMS", pointerDescriptions.get(1).getSetName());
		assertSame(PointerType.MEMBER_PRIOR,pointerDescriptions.get(1).getPointerType());		
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
		assertEquals(Short.valueOf((short) 3), PrefixUtil.getPositionInPrefix(role, OWNER_NEXT));
		
		verify(role, times(1)).getNextDbkeyPosition();
		verify(role, never()).getPriorDbkeyPosition();
		
		
		role = mockOwnerRoleWithNoPointersSet("A", "B");		
		assertNull(PrefixUtil.getPositionInPrefix(role, OWNER_NEXT));
		
		verify(role, times(1)).getNextDbkeyPosition();
		verify(role, never()).getPriorDbkeyPosition();		
		
	}
	
	@Test
	public void testGetPositionInPrefix_OwnerPrior() {
		
		OwnerRole role = mockOwnerRoleWithNoPointersSet("A", "B");
		when(role.getPriorDbkeyPosition()).thenReturn((short) 5);
		assertEquals(Short.valueOf((short) 5), PrefixUtil.getPositionInPrefix(role, OWNER_PRIOR));
		
		verify(role, never()).getNextDbkeyPosition();
		verify(role, times(1)).getPriorDbkeyPosition();
		
		
		role = mockOwnerRoleWithNoPointersSet("A", "B");		
		assertNull(PrefixUtil.getPositionInPrefix(role, OWNER_PRIOR));
		
		verify(role, never()).getNextDbkeyPosition();
		verify(role, times(1)).getPriorDbkeyPosition();	
		
	}
	
	@Test
	public void testGetPositionInPrefix_MemberNext() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
		when(role.getNextDbkeyPosition()).thenReturn((short) 9);
		assertEquals(Short.valueOf((short) 9), PrefixUtil.getPositionInPrefix(role, MEMBER_NEXT));
		
		verify(role, times(1)).getNextDbkeyPosition();
		verify(role, never()).getPriorDbkeyPosition();
		verify(role, never()).getOwnerDbkeyPosition();	
		verify(role, never()).getIndexDbkeyPosition();	
		
		
		role = mockMemberRoleWithNoPointersSet("A", "B");		
		assertNull(PrefixUtil.getPositionInPrefix(role, MEMBER_NEXT));
		
		verify(role, times(1)).getNextDbkeyPosition();
		verify(role, never()).getPriorDbkeyPosition();
		verify(role, never()).getOwnerDbkeyPosition();	
		verify(role, never()).getIndexDbkeyPosition();	
		
	}
	
	@Test
	public void testGetPositionInPrefix_MemberPrior() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
		when(role.getPriorDbkeyPosition()).thenReturn((short) 2);
		assertEquals(Short.valueOf((short) 2), PrefixUtil.getPositionInPrefix(role, MEMBER_PRIOR));
		
		verify(role, never()).getNextDbkeyPosition();
		verify(role, times(1)).getPriorDbkeyPosition();
		verify(role, never()).getOwnerDbkeyPosition();	
		verify(role, never()).getIndexDbkeyPosition();	
		
		
		role = mockMemberRoleWithNoPointersSet("A", "B");		
		assertNull(PrefixUtil.getPositionInPrefix(role, MEMBER_PRIOR));
		
		verify(role, never()).getNextDbkeyPosition();
		verify(role, times(1)).getPriorDbkeyPosition();
		verify(role, never()).getOwnerDbkeyPosition();	
		verify(role, never()).getIndexDbkeyPosition();	
		
	}
	
	@Test
	public void testGetPositionInPrefix_MemberOwner() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
		when(role.getOwnerDbkeyPosition()).thenReturn((short) 1);
		assertEquals(Short.valueOf((short) 1), PrefixUtil.getPositionInPrefix(role, MEMBER_OWNER));
		
		verify(role, never()).getNextDbkeyPosition();
		verify(role, never()).getPriorDbkeyPosition();
		verify(role, times(1)).getOwnerDbkeyPosition();	
		verify(role, never()).getIndexDbkeyPosition();	
		
		
		role = mockMemberRoleWithNoPointersSet("A", "B");		
		assertNull(PrefixUtil.getPositionInPrefix(role, MEMBER_OWNER));
		
		verify(role, never()).getNextDbkeyPosition();
		verify(role, never()).getPriorDbkeyPosition();
		verify(role, times(1)).getOwnerDbkeyPosition();	
		verify(role, never()).getIndexDbkeyPosition();	
		
	}
	
	@Test
	public void testGetPositionInPrefix_MemberIndex() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
		when(role.getIndexDbkeyPosition()).thenReturn((short) 1);
		assertEquals(Short.valueOf((short) 1), PrefixUtil.getPositionInPrefix(role, MEMBER_INDEX));
		
		verify(role, never()).getNextDbkeyPosition();
		verify(role, never()).getPriorDbkeyPosition();
		verify(role, never()).getOwnerDbkeyPosition();	
		verify(role, times(1)).getIndexDbkeyPosition();	
		
		
		role = mockMemberRoleWithNoPointersSet("A", "B");		
		assertNull(PrefixUtil.getPositionInPrefix(role, MEMBER_INDEX));
		
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
		
		PrefixUtil.setPositionInPrefix(role, OWNER_NEXT, (short) 7);		
		
		verify(role, times(1)).setNextDbkeyPosition((short) 7);
		verify(role, times(1)).setNextDbkeyPosition(any(Short.class));
		verify(role, never()).setPriorDbkeyPosition(any(Short.class));
		
		
		role = mockOwnerRoleWithNoPointersSet("A", "B");		
		when(role.getNextDbkeyPosition()).thenReturn((short) 7);
		
		PrefixUtil.setPositionInPrefix(role, OWNER_NEXT, null);		
		
		verify(role, times(1)).setNextDbkeyPosition((short) 0);
		verify(role, times(1)).setNextDbkeyPosition(any(Short.class));
		verify(role, never()).setPriorDbkeyPosition(any(Short.class));	
		
	}
	
	@Test
	public void testSetPositionInPrefix_OwnerPrior() {
		OwnerRole role = mockOwnerRoleWithNoPointersSet("A", "B");
		
		Short newPositionInPrefix = Short.valueOf((short) 7);
		PrefixUtil.setPositionInPrefix(role, OWNER_PRIOR, newPositionInPrefix);		
		
		verify(role, never()).setNextDbkeyPosition(anyShort());
		verify(role, times(1)).setPriorDbkeyPosition(newPositionInPrefix);
		verify(role, times(1)).setPriorDbkeyPosition(anyShort());		
		
		
		role = mockOwnerRoleWithNoPointersSet("A", "B");		
		when(role.getNextDbkeyPosition()).thenReturn((short) 7);
		
		PrefixUtil.setPositionInPrefix(role, OWNER_PRIOR, null);		
		
		verify(role, never()).setNextDbkeyPosition(anyShort());
		verify(role, times(1)).setPriorDbkeyPosition(null);
		verify(role, never()).setPriorDbkeyPosition(anyShort());
	}
	
	@Test
	public void testSetPositionInPrefix_MemberNext() {
		MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
		
		Short newPositionInPrefix = Short.valueOf((short) 7);
		PrefixUtil.setPositionInPrefix(role, MEMBER_NEXT, newPositionInPrefix);		
		
		verify(role, times(1)).setNextDbkeyPosition(newPositionInPrefix);
		verify(role, times(1)).setNextDbkeyPosition(anyShort());
		verify(role, never()).setPriorDbkeyPosition(anyShort());
		verify(role, never()).setOwnerDbkeyPosition(anyShort());
		verify(role, never()).setIndexDbkeyPosition(anyShort());
		
		
		role = mockMemberRoleWithNoPointersSet("A", "B");		
		when(role.getNextDbkeyPosition()).thenReturn((short) 7);
		
		PrefixUtil.setPositionInPrefix(role, MEMBER_NEXT, null);		
		
		verify(role, times(1)).setNextDbkeyPosition(null);
		verify(role, never()).setNextDbkeyPosition(anyShort());
		verify(role, never()).setPriorDbkeyPosition(anyShort());
		verify(role, never()).setOwnerDbkeyPosition(anyShort());
		verify(role, never()).setIndexDbkeyPosition(anyShort());
	}
	
	@Test
	public void testSetPositionInPrefix_MemberPrior() {
		MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
		
		Short newPositionInPrefix = Short.valueOf((short) 7);
		PrefixUtil.setPositionInPrefix(role, MEMBER_PRIOR, newPositionInPrefix);		
		
		verify(role, never()).setNextDbkeyPosition(anyShort());
		verify(role, times(1)).setPriorDbkeyPosition(newPositionInPrefix);
		verify(role, times(1)).setPriorDbkeyPosition(anyShort());
		verify(role, never()).setOwnerDbkeyPosition(anyShort());
		verify(role, never()).setIndexDbkeyPosition(anyShort());
		
		
		role = mockMemberRoleWithNoPointersSet("A", "B");		
		when(role.getPriorDbkeyPosition()).thenReturn((short) 7);
		
		PrefixUtil.setPositionInPrefix(role, MEMBER_PRIOR, null);		
		
		verify(role, never()).setNextDbkeyPosition(anyShort());
		verify(role, times(1)).setPriorDbkeyPosition(null);
		verify(role, never()).setPriorDbkeyPosition(anyShort());
		verify(role, never()).setOwnerDbkeyPosition(anyShort());
		verify(role, never()).setIndexDbkeyPosition(anyShort());
	}
	
	@Test
	public void testSetPositionInPrefix_MemberOwner() {
		MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
		
		Short newPositionInPrefix = Short.valueOf((short) 7);
		PrefixUtil.setPositionInPrefix(role, MEMBER_OWNER, newPositionInPrefix);		
		
		verify(role, never()).setNextDbkeyPosition(anyShort());
		verify(role, never()).setPriorDbkeyPosition(anyShort());
		verify(role, times(1)).setOwnerDbkeyPosition(newPositionInPrefix);
		verify(role, times(1)).setOwnerDbkeyPosition(anyShort());
		verify(role, never()).setIndexDbkeyPosition(anyShort());
		
		
		role = mockMemberRoleWithNoPointersSet("A", "B");		
		when(role.getOwnerDbkeyPosition()).thenReturn((short) 7);
		
		PrefixUtil.setPositionInPrefix(role, MEMBER_OWNER, null);		
		
		verify(role, never()).setNextDbkeyPosition(anyShort());
		verify(role, never()).setPriorDbkeyPosition(anyShort());
		verify(role, times(1)).setOwnerDbkeyPosition(null);
		verify(role, never()).setOwnerDbkeyPosition(anyShort());
		verify(role, never()).setIndexDbkeyPosition(anyShort());
	}
	
	@Test
	public void testSetPositionInPrefix_MemberIndex() {
		MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
		
		Short newPositionInPrefix = Short.valueOf((short) 7);
		PrefixUtil.setPositionInPrefix(role, MEMBER_INDEX, newPositionInPrefix);		
		
		verify(role, never()).setNextDbkeyPosition(anyShort());
		verify(role, never()).setPriorDbkeyPosition(anyShort());		
		verify(role, never()).setOwnerDbkeyPosition(anyShort());
		verify(role, times(1)).setIndexDbkeyPosition(newPositionInPrefix);
		verify(role, times(1)).setIndexDbkeyPosition(anyShort());
		
		
		role = mockMemberRoleWithNoPointersSet("A", "B");		
		when(role.getOwnerDbkeyPosition()).thenReturn((short) 7);
		
		PrefixUtil.setPositionInPrefix(role, MEMBER_INDEX, null);		
		
		verify(role, never()).setNextDbkeyPosition(anyShort());
		verify(role, never()).setPriorDbkeyPosition(anyShort());
		verify(role, never()).setOwnerDbkeyPosition(anyShort());
		verify(role, times(1)).setIndexDbkeyPosition(null);
		verify(role, never()).setIndexDbkeyPosition(anyShort());
	}
	
	@Test
	public void testPointerSorting() {
		
		List<Pointer<?>> unsortedList = new ArrayList<>();
		
		Pointer<?> pointer1 = mock(Pointer.class);
		when(pointer1.getCurrentPositionInPrefix()).thenReturn((short) 2);
		unsortedList.add(pointer1);
		
		Pointer<?> pointer2 = mock(Pointer.class);
		when(pointer2.getCurrentPositionInPrefix()).thenReturn((short) 1);
		unsortedList.add(pointer2);
		
		List<Pointer<?>> sortedList = PrefixUtil.asSortedList(unsortedList);
		assertNotNull(sortedList);
		assertEquals(2, sortedList.size());
		assertSame(pointer2, sortedList.get(0));
		assertSame(pointer1, sortedList.get(1));
		
	}
	
	@Test
	public void testPointerListAssemblage() {
		
		Schema schema = TestTools.getEmpschmSchema();
		SchemaRecord recordEmployee = schema.getRecord("EMPLOYEE");		
		List<Pointer<?>> pointers = PrefixUtil.getPointersForRecord(recordEmployee);
		assertNotNull(pointers);
		assertEquals(16, pointers.size());
		
		assertEquals("EMPLOYEE", pointers.get(0).getRecordName());
		assertEquals("DEPT-EMPLOYEE", pointers.get(0).getSetName());
		assertSame(MEMBER_NEXT, pointers.get(0).getType());
		
		assertEquals("EMPLOYEE", pointers.get(1).getRecordName());
		assertEquals("DEPT-EMPLOYEE", pointers.get(1).getSetName());
		assertSame(MEMBER_PRIOR, pointers.get(1).getType());
		
		assertEquals("EMPLOYEE", pointers.get(2).getRecordName());
		assertEquals("DEPT-EMPLOYEE", pointers.get(2).getSetName());
		assertSame(MEMBER_OWNER, pointers.get(2).getType());
		
		assertEquals("EMPLOYEE", pointers.get(3).getRecordName());
		assertEquals("EMP-NAME-NDX", pointers.get(3).getSetName());
		assertSame(MEMBER_INDEX, pointers.get(3).getType());
		
		assertEquals("EMPLOYEE", pointers.get(4).getRecordName());
		assertEquals("OFFICE-EMPLOYEE", pointers.get(4).getSetName());
		assertSame(MEMBER_INDEX, pointers.get(4).getType());
		
		assertEquals("EMPLOYEE", pointers.get(5).getRecordName());
		assertEquals("OFFICE-EMPLOYEE", pointers.get(5).getSetName());
		assertSame(MEMBER_OWNER, pointers.get(5).getType());
		
		assertEquals("EMPLOYEE", pointers.get(6).getRecordName());
		assertEquals("EMP-COVERAGE", pointers.get(6).getSetName());
		assertSame(OWNER_NEXT, pointers.get(6).getType());
		
		assertEquals("EMPLOYEE", pointers.get(7).getRecordName());
		assertEquals("EMP-COVERAGE", pointers.get(7).getSetName());
		assertSame(OWNER_PRIOR, pointers.get(7).getType());
		
		assertEquals("EMPLOYEE", pointers.get(8).getRecordName());
		assertEquals("EMP-EMPOSITION", pointers.get(8).getSetName());
		assertSame(OWNER_NEXT, pointers.get(8).getType());		
		
		assertEquals("EMPLOYEE", pointers.get(9).getRecordName());
		assertEquals("EMP-EMPOSITION", pointers.get(9).getSetName());
		assertSame(OWNER_PRIOR, pointers.get(9).getType());

		assertEquals("EMPLOYEE", pointers.get(10).getRecordName());
		assertEquals("EMP-EXPERTISE", pointers.get(10).getSetName());
		assertSame(OWNER_NEXT, pointers.get(10).getType());		
		
		assertEquals("EMPLOYEE", pointers.get(11).getRecordName());
		assertEquals("EMP-EXPERTISE", pointers.get(11).getSetName());
		assertSame(OWNER_PRIOR, pointers.get(11).getType());	
		
		assertEquals("EMPLOYEE", pointers.get(12).getRecordName());
		assertEquals("MANAGES", pointers.get(12).getSetName());
		assertSame(OWNER_NEXT, pointers.get(12).getType());		
		
		assertEquals("EMPLOYEE", pointers.get(13).getRecordName());
		assertEquals("MANAGES", pointers.get(13).getSetName());
		assertSame(OWNER_PRIOR, pointers.get(13).getType());
		
		assertEquals("EMPLOYEE", pointers.get(14).getRecordName());
		assertEquals("REPORTS-TO", pointers.get(14).getSetName());
		assertSame(OWNER_NEXT, pointers.get(14).getType());		
		
		assertEquals("EMPLOYEE", pointers.get(15).getRecordName());
		assertEquals("REPORTS-TO", pointers.get(15).getSetName());
		assertSame(OWNER_PRIOR, pointers.get(15).getType());		
		
	}
	
	@Test
	public void testIsPointerListConsistent() {
		
		List<Pointer<?>> unsortedList = new ArrayList<>();		
		Pointer<?> pointer1 = mock(Pointer.class);
		when(pointer1.getCurrentPositionInPrefix()).thenReturn((short) 2);
		unsortedList.add(pointer1);		
		Pointer<?> pointer2 = mock(Pointer.class);
		when(pointer2.getCurrentPositionInPrefix()).thenReturn((short) 1);
		unsortedList.add(pointer2);		
		List<Pointer<?>> sortedList = PrefixUtil.asSortedList(unsortedList);
				
		assertTrue(PrefixUtil.isPointerListConsistent(sortedList));
		assertFalse(PrefixUtil.isPointerListConsistent(unsortedList));
		
	}
	
	@Test
	public void testGetDefinedPointerTypesForOwnerRole() {

		OwnerRole role = mockOwnerRoleWithNoPointersSet("A", "B");
		
		PointerType[] definedPointerTypes = PrefixUtil.getDefinedPointerTypes(role);
		assertEquals(0, definedPointerTypes.length);
		
		when(role.getNextDbkeyPosition()).thenReturn((short) 5);
		definedPointerTypes = PrefixUtil.getDefinedPointerTypes(role);
		assertEquals(1, definedPointerTypes.length);
		assertSame(OWNER_NEXT, definedPointerTypes[0]);
		
		when(role.getNextDbkeyPosition()).thenReturn((short) 0);
		when(role.getPriorDbkeyPosition()).thenReturn(Short.valueOf((short) 7));
		definedPointerTypes = PrefixUtil.getDefinedPointerTypes(role);
		assertEquals(1, definedPointerTypes.length);
		assertSame(OWNER_PRIOR, definedPointerTypes[0]);
		
		when(role.getNextDbkeyPosition()).thenReturn((short) 5);
		when(role.getPriorDbkeyPosition()).thenReturn(Short.valueOf((short) 7));
		definedPointerTypes = PrefixUtil.getDefinedPointerTypes(role);
		assertEquals(2, definedPointerTypes.length);
		assertSame(OWNER_NEXT, definedPointerTypes[0]);
		assertSame(OWNER_PRIOR, definedPointerTypes[1]);
		
	}
	
	@Test
	public void testGetDefinedPointerTypesForMemberRole() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("A", "B");
		
		PointerType[] definedPointerTypes = PrefixUtil.getDefinedPointerTypes(role);
		assertEquals(0, definedPointerTypes.length);
		
		when(role.getNextDbkeyPosition()).thenReturn(Short.valueOf((short) 5));
		definedPointerTypes = PrefixUtil.getDefinedPointerTypes(role);
		assertEquals(1, definedPointerTypes.length);
		assertSame(MEMBER_NEXT, definedPointerTypes[0]);
		
		when(role.getNextDbkeyPosition()).thenReturn(null);
		when(role.getPriorDbkeyPosition()).thenReturn(Short.valueOf((short) 7));
		definedPointerTypes = PrefixUtil.getDefinedPointerTypes(role);
		assertEquals(1, definedPointerTypes.length);
		assertSame(MEMBER_PRIOR, definedPointerTypes[0]);
		
		when(role.getPriorDbkeyPosition()).thenReturn(null);
		when(role.getOwnerDbkeyPosition()).thenReturn(Short.valueOf((short) 9));
		definedPointerTypes = PrefixUtil.getDefinedPointerTypes(role);
		assertEquals(1, definedPointerTypes.length);
		assertSame(MEMBER_OWNER, definedPointerTypes[0]);
		
		when(role.getOwnerDbkeyPosition()).thenReturn(null);
		when(role.getIndexDbkeyPosition()).thenReturn(Short.valueOf((short) 9));
		definedPointerTypes = PrefixUtil.getDefinedPointerTypes(role);
		assertEquals(1, definedPointerTypes.length);
		assertSame(MEMBER_INDEX, definedPointerTypes[0]);
		
		when(role.getNextDbkeyPosition()).thenReturn(Short.valueOf((short) 5));
		when(role.getPriorDbkeyPosition()).thenReturn(Short.valueOf((short) 6));
		when(role.getOwnerDbkeyPosition()).thenReturn(null);
		when(role.getIndexDbkeyPosition()).thenReturn(null);
		definedPointerTypes = PrefixUtil.getDefinedPointerTypes(role);
		assertEquals(2, definedPointerTypes.length);
		assertSame(MEMBER_NEXT, definedPointerTypes[0]);		
		assertSame(MEMBER_PRIOR, definedPointerTypes[1]);
		
		when(role.getNextDbkeyPosition()).thenReturn(Short.valueOf((short) 5));
		when(role.getPriorDbkeyPosition()).thenReturn(null);
		when(role.getOwnerDbkeyPosition()).thenReturn(Short.valueOf((short) 6));
		when(role.getIndexDbkeyPosition()).thenReturn(null);
		definedPointerTypes = PrefixUtil.getDefinedPointerTypes(role);
		assertEquals(2, definedPointerTypes.length);
		assertSame(MEMBER_NEXT, definedPointerTypes[0]);		
		assertSame(MEMBER_OWNER, definedPointerTypes[1]);		
		
		when(role.getNextDbkeyPosition()).thenReturn(Short.valueOf((short) 5));
		when(role.getPriorDbkeyPosition()).thenReturn(Short.valueOf((short) 6));
		when(role.getOwnerDbkeyPosition()).thenReturn(Short.valueOf((short) 7));
		when(role.getIndexDbkeyPosition()).thenReturn(null);
		definedPointerTypes = PrefixUtil.getDefinedPointerTypes(role);
		assertEquals(3, definedPointerTypes.length);
		assertSame(MEMBER_NEXT, definedPointerTypes[0]);		
		assertSame(MEMBER_PRIOR, definedPointerTypes[1]);		
		assertSame(MEMBER_OWNER, definedPointerTypes[2]);
		
		when(role.getNextDbkeyPosition()).thenReturn(null);
		when(role.getPriorDbkeyPosition()).thenReturn(null);
		when(role.getOwnerDbkeyPosition()).thenReturn(Short.valueOf((short) 6));
		when(role.getIndexDbkeyPosition()).thenReturn(Short.valueOf((short) 7));
		definedPointerTypes = PrefixUtil.getDefinedPointerTypes(role);
		assertEquals(2, definedPointerTypes.length);
		assertSame(MEMBER_OWNER, definedPointerTypes[0]);		
		assertSame(MEMBER_INDEX, definedPointerTypes[1]);		
		
	}
	
	@Test
	public void testReorder() {
		
		Schema schema = TestTools.getEmpschmSchema();
		SchemaRecord record = schema.getRecord("HOSPITAL-CLAIM");
		
		List<Pointer<?>> pointers = PrefixFactory.newPrefixForInquiry(record).getPointers();
		assertEquals(2, pointers.size());
		assertEquals("COVERAGE-CLAIMS", pointers.get(0).getSetName());
		assertSame(PointerType.MEMBER_NEXT, pointers.get(0).getType());
		assertEquals("COVERAGE-CLAIMS", pointers.get(1).getSetName());
		assertSame(PointerType.MEMBER_PRIOR, pointers.get(1).getType());
		
		List<PointerDescription> desiredOrder = new ArrayList<>();
		desiredOrder.add(new PointerDescription("COVERAGE-CLAIMS", PointerType.MEMBER_PRIOR));
		desiredOrder.add(new PointerDescription("COVERAGE-CLAIMS", PointerType.MEMBER_NEXT));
		
		PrefixUtil.reorder(pointers, desiredOrder);
		
		assertEquals(2, pointers.size());
		assertEquals("COVERAGE-CLAIMS", pointers.get(0).getSetName());
		assertSame(PointerType.MEMBER_PRIOR, pointers.get(0).getType());
		assertEquals("COVERAGE-CLAIMS", pointers.get(1).getSetName());
		assertSame(PointerType.MEMBER_NEXT, pointers.get(1).getType());
	}
}
