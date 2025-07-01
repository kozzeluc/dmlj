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
import static org.lh.dmlj.schema.editor.prefix.PointerType.OWNER_NEXT;
import static org.lh.dmlj.schema.editor.prefix.PointerType.OWNER_PRIOR;
import static org.lh.dmlj.schema.editor.prefix.PointerType.MEMBER_NEXT;
import static org.lh.dmlj.schema.editor.prefix.PointerType.MEMBER_PRIOR;
import static org.lh.dmlj.schema.editor.prefix.PointerType.MEMBER_OWNER;
import static org.lh.dmlj.schema.editor.prefix.PointerType.MEMBER_INDEX;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;

public class PointerToSetTest extends AbstractPointerOrPrefixRelatedTestCase {

	@Test
	public void testOwnerNext() {
		OwnerRole role = mockOwnerRoleWithNoPointersSet("R1", "S1");		
		
		PointerToSet<OwnerRole> pointer = 
			PointerFactory.newPointerToSet(role, OWNER_NEXT, (short) 5);
		
		assertEquals((short) 5, pointer.getPositionInPrefixToSet());
		
		pointer.set();
		verify(role, times(1)).setNextDbkeyPosition((short) 5);		
		
		pointer.unset();
		verify(role, times(1)).setNextDbkeyPosition((short) 0);
	}
	
	@Test
	public void testOwnerPrior() {
		OwnerRole role = mockOwnerRoleWithNoPointersSet("R1", "S1");		
		
		Short newPositionInPrefix = Short.valueOf((short) 3);
		PointerToSet<OwnerRole> pointer = 
			PointerFactory.newPointerToSet(role, OWNER_PRIOR, newPositionInPrefix.shortValue());
		
		assertEquals(newPositionInPrefix.shortValue(), pointer.getPositionInPrefixToSet());
		
		pointer.set();
		verify(role, times(1)).setPriorDbkeyPosition(newPositionInPrefix);
		
		pointer.unset();
		verify(role, times(1)).setPriorDbkeyPosition(null);
	}
	
	@Test
	public void testMemberNext() {
		MemberRole role = mockMemberRoleWithNoPointersSet("R1", "S1");		
		
		Short newPositionInPrefix = Short.valueOf((short) 13);
		PointerToSet<MemberRole> pointer = 
			PointerFactory.newPointerToSet(role, MEMBER_NEXT, newPositionInPrefix.shortValue());
		
		assertEquals(newPositionInPrefix.shortValue(), pointer.getPositionInPrefixToSet());
		
		pointer.set();
		verify(role, times(1)).setNextDbkeyPosition(newPositionInPrefix);		
		
		pointer.unset();
		verify(role, times(1)).setNextDbkeyPosition(null);
	}
	
	@Test
	public void testMemberPrior() {
		MemberRole role = mockMemberRoleWithNoPointersSet("R1", "S1");		
		
		Short newPositionInPrefix = Short.valueOf((short) 13);
		PointerToSet<MemberRole> pointer = 
			PointerFactory.newPointerToSet(role, MEMBER_PRIOR, newPositionInPrefix.shortValue());
		
		assertEquals(newPositionInPrefix.shortValue(), pointer.getPositionInPrefixToSet());
		
		pointer.set();
		verify(role, times(1)).setPriorDbkeyPosition(newPositionInPrefix);		
		
		pointer.unset();
		verify(role, times(1)).setPriorDbkeyPosition(null);
	}	
	
	@Test
	public void testMemberOwner() {
		MemberRole role = mockMemberRoleWithNoPointersSet("R1", "S1");		
		
		Short newPositionInPrefix = Short.valueOf((short) 17);
		PointerToSet<MemberRole> pointer = 
			PointerFactory.newPointerToSet(role, MEMBER_OWNER, newPositionInPrefix.shortValue());
		
		assertEquals(newPositionInPrefix.shortValue(), pointer.getPositionInPrefixToSet());
		
		pointer.set();
		verify(role, times(1)).setOwnerDbkeyPosition(newPositionInPrefix);		
		
		pointer.unset();
		verify(role, times(1)).setOwnerDbkeyPosition(null);
	}
	
	@Test
	public void testMemberIndex() {
		MemberRole role = mockMemberRoleWithNoPointersSet("R1", "S1");		
		
		Short newPositionInPrefix = Short.valueOf((short) 17);
		PointerToSet<MemberRole> pointer = 
			PointerFactory.newPointerToSet(role, MEMBER_INDEX, newPositionInPrefix.shortValue());
		
		assertEquals(newPositionInPrefix.shortValue(), pointer.getPositionInPrefixToSet());
		
		pointer.set();
		verify(role, times(1)).setIndexDbkeyPosition(newPositionInPrefix);		
		
		pointer.unset();
		verify(role, times(1)).setIndexDbkeyPosition(null);
	}

}
