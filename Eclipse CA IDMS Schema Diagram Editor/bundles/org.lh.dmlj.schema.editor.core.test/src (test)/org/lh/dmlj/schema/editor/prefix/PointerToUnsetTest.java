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
import static org.lh.dmlj.schema.editor.prefix.PointerType.OWNER_NEXT;
import static org.lh.dmlj.schema.editor.prefix.PointerType.OWNER_PRIOR;
import static org.lh.dmlj.schema.editor.prefix.PointerType.MEMBER_NEXT;
import static org.lh.dmlj.schema.editor.prefix.PointerType.MEMBER_PRIOR;
import static org.lh.dmlj.schema.editor.prefix.PointerType.MEMBER_OWNER;
import static org.lh.dmlj.schema.editor.prefix.PointerType.MEMBER_INDEX;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;

public class PointerToUnsetTest extends AbstractPointerOrPrefixRelatedTestCase {

	@Test
	public void testOwnerNext() {
		
		OwnerRole role = mockOwnerRoleWithNoPointersSet("R1", "S1");
		short positionInPrefix = (short) 5;
		when(role.getNextDbkeyPosition()).thenReturn(positionInPrefix);
		
		PointerToUnset<OwnerRole> pointer = PointerFactory.newPointerToUnset(role, OWNER_NEXT);		
		assertEquals(positionInPrefix, pointer.getCurrentPositionInPrefix().shortValue());
		
		pointer.unset();
		verify(role, times(1)).setNextDbkeyPosition((short) 0);		
		
		pointer.reset();
		verify(role, times(1)).setNextDbkeyPosition(positionInPrefix);
		
		verify(role, times(2)).setNextDbkeyPosition(any(Short.class));
		
	}
	
	@Test
	public void testOwnerPrior() {
		
		OwnerRole role = mockOwnerRoleWithNoPointersSet("R1", "S1");
		Short positionInPrefix = Short.valueOf((short) 17);
		when(role.getPriorDbkeyPosition()).thenReturn(positionInPrefix);
		
		PointerToUnset<OwnerRole> pointer = PointerFactory.newPointerToUnset(role, OWNER_PRIOR);		
		assertSame(positionInPrefix, pointer.getCurrentPositionInPrefix());
		
		pointer.unset();
		verify(role, times(1)).setPriorDbkeyPosition(null);		
		
		pointer.reset();
		verify(role, times(1)).setPriorDbkeyPosition(positionInPrefix);
		
		verify(role, times(2)).setPriorDbkeyPosition(any(Short.class));
		
	}	
	
	@Test
	public void testMemberNext() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("R1", "S1");
		Short positionInPrefix = Short.valueOf((short) 12);
		when(role.getNextDbkeyPosition()).thenReturn(positionInPrefix);
		
		PointerToUnset<MemberRole> pointer = PointerFactory.newPointerToUnset(role, MEMBER_NEXT);		
		assertSame(positionInPrefix, pointer.getCurrentPositionInPrefix());
		
		pointer.unset();
		verify(role, times(1)).setNextDbkeyPosition(null);		
		
		pointer.reset();
		verify(role, times(1)).setNextDbkeyPosition(positionInPrefix);
		
		verify(role, times(2)).setNextDbkeyPosition(any(Short.class));
		
	}
	
	@Test
	public void testMemberPrior() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("R1", "S1");
		Short positionInPrefix = Short.valueOf((short) 1);
		when(role.getPriorDbkeyPosition()).thenReturn(positionInPrefix);
		
		PointerToUnset<MemberRole> pointer = PointerFactory.newPointerToUnset(role, MEMBER_PRIOR);		
		assertSame(positionInPrefix, pointer.getCurrentPositionInPrefix());
		
		pointer.unset();
		verify(role, times(1)).setPriorDbkeyPosition(null);		
		
		pointer.reset();
		verify(role, times(1)).setPriorDbkeyPosition(positionInPrefix);
		
		verify(role, times(2)).setPriorDbkeyPosition(any(Short.class));
		
	}
	
	@Test
	public void testMemberOwner() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("R1", "S1");
		Short positionInPrefix = Short.valueOf((short) 5);
		when(role.getOwnerDbkeyPosition()).thenReturn(positionInPrefix);
		
		PointerToUnset<MemberRole> pointer = PointerFactory.newPointerToUnset(role, MEMBER_OWNER);		
		assertSame(positionInPrefix, pointer.getCurrentPositionInPrefix());
		
		pointer.unset();
		verify(role, times(1)).setOwnerDbkeyPosition(null);		
		
		pointer.reset();
		verify(role, times(1)).setOwnerDbkeyPosition(positionInPrefix);
		
		verify(role, times(2)).setOwnerDbkeyPosition(any(Short.class));
		
	}
	
	@Test
	public void testMemberIndex() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("R1", "S1");
		Short positionInPrefix = Short.valueOf((short) 5);
		when(role.getIndexDbkeyPosition()).thenReturn(positionInPrefix);
		
		PointerToUnset<MemberRole> pointer = PointerFactory.newPointerToUnset(role, MEMBER_INDEX);		
		assertSame(positionInPrefix, pointer.getCurrentPositionInPrefix());
		
		pointer.unset();
		verify(role, times(1)).setIndexDbkeyPosition(null);		
		
		pointer.reset();
		verify(role, times(1)).setIndexDbkeyPosition(positionInPrefix);
		
		verify(role, times(2)).setIndexDbkeyPosition(any(Short.class));
		
	}	

}
