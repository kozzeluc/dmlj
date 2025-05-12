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

public class PointerToMoveTest extends AbstractPointerOrPrefixRelatedTestCase {

	@Test
	public void testOwnerNext() {
		
		OwnerRole role = mockOwnerRoleWithNoPointersSet("R1", "S1");
		short oldPositionInPrefix = (short) 7;
		when(role.getNextDbkeyPosition()).thenReturn(oldPositionInPrefix);		
		
		short newPositionInPrefix = (short) 5;
		PointerToMove<OwnerRole> pointer = 
			PointerFactory.newPointerToMove(role, OWNER_NEXT, newPositionInPrefix);
		
		assertEquals(oldPositionInPrefix, pointer.getOldPositionInPrefix().shortValue());
		assertEquals(newPositionInPrefix, pointer.getNewPositionInPrefix());
		
		pointer.move();
		verify(role, times(1)).setNextDbkeyPosition(newPositionInPrefix);		
		
		pointer.moveBack();
		verify(role, times(1)).setNextDbkeyPosition(oldPositionInPrefix);
		
		verify(role, times(2)).setNextDbkeyPosition(any(Short.class));
		
	}
	
	@Test
	public void testOwnerPrior() {
		
		OwnerRole role = mockOwnerRoleWithNoPointersSet("R1", "S1");
		Short oldPositionInPrefix = Short.valueOf((short) 14);
		when(role.getPriorDbkeyPosition()).thenReturn(oldPositionInPrefix);		
		
		short newPositionInPrefix = (short) 7;
		PointerToMove<OwnerRole> pointer = 
			PointerFactory.newPointerToMove(role, OWNER_PRIOR, newPositionInPrefix);
		
		assertSame(oldPositionInPrefix, pointer.getOldPositionInPrefix());
		assertEquals(newPositionInPrefix, pointer.getNewPositionInPrefix());
		
		pointer.move();
		verify(role, times(1)).setPriorDbkeyPosition(newPositionInPrefix);		
		
		pointer.moveBack();
		verify(role, times(1)).setPriorDbkeyPosition(oldPositionInPrefix);
		
		verify(role, times(2)).setPriorDbkeyPosition(any(Short.class));
		
	}
	
	@Test
	public void testMemberNext() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("R1", "S1");
		Short oldPositionInPrefix = Short.valueOf((short) 3);
		when(role.getNextDbkeyPosition()).thenReturn(oldPositionInPrefix);		
		
		short newPositionInPrefix = (short) 2;
		PointerToMove<MemberRole> pointer = 
			PointerFactory.newPointerToMove(role, MEMBER_NEXT, newPositionInPrefix);
		
		assertSame(oldPositionInPrefix, pointer.getOldPositionInPrefix());
		assertEquals(newPositionInPrefix, pointer.getNewPositionInPrefix());
		
		pointer.move();
		verify(role, times(1)).setNextDbkeyPosition(newPositionInPrefix);		
		
		pointer.moveBack();
		verify(role, times(1)).setNextDbkeyPosition(oldPositionInPrefix);
		
		verify(role, times(2)).setNextDbkeyPosition(any(Short.class));
		
	}
	
	@Test
	public void testMemberPrior() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("R1", "S1");
		Short oldPositionInPrefix = Short.valueOf((short) 5);
		when(role.getPriorDbkeyPosition()).thenReturn(oldPositionInPrefix);		
		
		short newPositionInPrefix = (short) 1;
		PointerToMove<MemberRole> pointer = 
			PointerFactory.newPointerToMove(role, MEMBER_PRIOR, newPositionInPrefix);
		
		assertSame(oldPositionInPrefix, pointer.getOldPositionInPrefix());
		assertEquals(newPositionInPrefix, pointer.getNewPositionInPrefix());
		
		pointer.move();
		verify(role, times(1)).setPriorDbkeyPosition(newPositionInPrefix);		
		
		pointer.moveBack();
		verify(role, times(1)).setPriorDbkeyPosition(oldPositionInPrefix);
		
		verify(role, times(2)).setPriorDbkeyPosition(any(Short.class));
		
	}
	
	@Test
	public void testMemberOwner() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("R1", "S1");
		Short oldPositionInPrefix = Short.valueOf((short) 7);
		when(role.getOwnerDbkeyPosition()).thenReturn(oldPositionInPrefix);		
		
		short newPositionInPrefix = (short) 4;
		PointerToMove<MemberRole> pointer = 
			PointerFactory.newPointerToMove(role, MEMBER_OWNER, newPositionInPrefix);
		
		assertSame(oldPositionInPrefix, pointer.getOldPositionInPrefix());
		assertEquals(newPositionInPrefix, pointer.getNewPositionInPrefix());
		
		pointer.move();
		verify(role, times(1)).setOwnerDbkeyPosition(newPositionInPrefix);		
		
		pointer.moveBack();
		verify(role, times(1)).setOwnerDbkeyPosition(oldPositionInPrefix);
		
		verify(role, times(2)).setOwnerDbkeyPosition(any(Short.class));
		
	}
	
	@Test
	public void testMemberIndex() {
		
		MemberRole role = mockMemberRoleWithNoPointersSet("R1", "S1");
		Short oldPositionInPrefix = Short.valueOf((short) 9);
		when(role.getIndexDbkeyPosition()).thenReturn(oldPositionInPrefix);		
		
		short newPositionInPrefix = (short) 6;
		PointerToMove<MemberRole> pointer = 
			PointerFactory.newPointerToMove(role, MEMBER_INDEX, newPositionInPrefix);
		
		assertSame(oldPositionInPrefix, pointer.getOldPositionInPrefix());
		assertEquals(newPositionInPrefix, pointer.getNewPositionInPrefix());
		
		pointer.move();
		verify(role, times(1)).setIndexDbkeyPosition(newPositionInPrefix);		
		
		pointer.moveBack();
		verify(role, times(1)).setIndexDbkeyPosition(oldPositionInPrefix);
		
		verify(role, times(2)).setIndexDbkeyPosition(any(Short.class));
		
	}	

}
