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
package org.lh.dmlj.schema.editor.command.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.junit.Test;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;

public class RemovableOwnerRoleTest {

	@Test
	public void test() {

		SchemaRecord record = mock(SchemaRecord.class);
		
		Set setToRemove = mock(Set.class);
		Set firstSetToKeep = mock(Set.class);
		Set secondSetToKeep = mock(Set.class);
		Set thirdSetToKeep = mock(Set.class);
		
		OwnerRole firstRoleToKeep = mock(OwnerRole.class);
		when(firstRoleToKeep.getRecord()).thenReturn(record);
		when(firstRoleToKeep.getSet()).thenReturn(firstSetToKeep);
		when(firstRoleToKeep.getNextDbkeyPosition()).thenReturn(Short.valueOf((short) 1));
		when(firstRoleToKeep.getPriorDbkeyPosition()).thenReturn(Short.valueOf((short) 2));		
		
		OwnerRole roleToRemove = mock(OwnerRole.class);
		when(roleToRemove.getRecord()).thenReturn(record);
		when(roleToRemove.getSet()).thenReturn(setToRemove);
		when(roleToRemove.getNextDbkeyPosition()).thenReturn((short) 3);
		when(roleToRemove.getPriorDbkeyPosition()).thenReturn(Short.valueOf((short) 4));
		
		OwnerRole secondRoleToKeep = mock(OwnerRole.class);
		when(secondRoleToKeep.getRecord()).thenReturn(record);
		when(secondRoleToKeep.getSet()).thenReturn(secondSetToKeep);
		when(secondRoleToKeep.getNextDbkeyPosition()).thenReturn(Short.valueOf((short) 5));
		when(secondRoleToKeep.getPriorDbkeyPosition()).thenReturn(Short.valueOf((short) 6));
		
		MemberRole thirdRoleToKeep = mock(MemberRole.class);
		when(thirdRoleToKeep.getRecord()).thenReturn(record);
		when(thirdRoleToKeep.getSet()).thenReturn(thirdSetToKeep);
		when(thirdRoleToKeep.getNextDbkeyPosition()).thenReturn(Short.valueOf((short) 7));
		when(thirdRoleToKeep.getPriorDbkeyPosition()).thenReturn(Short.valueOf((short) 8));
		when(thirdRoleToKeep.getOwnerDbkeyPosition()).thenReturn(Short.valueOf((short) 9));
		when(thirdRoleToKeep.getIndexDbkeyPosition()).thenReturn(null);		
		
		List<OwnerRole> ownerRoleList = new ArrayList<>();
		ownerRoleList.add(firstRoleToKeep);	
		ownerRoleList.add(roleToRemove);
		ownerRoleList.add(secondRoleToKeep);
		
		@SuppressWarnings("unchecked")
		EList<OwnerRole> ownerRoles = mock(EList.class);
		when(ownerRoles.iterator()).thenReturn(ownerRoleList.iterator());		
		when(ownerRoles.indexOf(firstRoleToKeep)).thenThrow(new RuntimeException("not to be called"));
		when(ownerRoles.indexOf(roleToRemove)).thenReturn(1);
		when(ownerRoles.indexOf(secondRoleToKeep)).thenThrow(new RuntimeException("not to be called"));
		when(record.getOwnerRoles()).thenReturn(ownerRoles);
		
		List<MemberRole> memberRoleList = new ArrayList<>();
		memberRoleList.add(thirdRoleToKeep);		
		
		@SuppressWarnings("unchecked")
		EList<MemberRole> memberRoles = mock(EList.class);
		when(memberRoles.iterator()).thenReturn(memberRoleList.iterator());		
		when(memberRoles.indexOf(anyObject())).thenThrow(new RuntimeException("not to be called"));
		when(record.getMemberRoles()).thenReturn(memberRoles);				
		
		RemovableOwnerRole removableOwnerRole = new RemovableOwnerRole(roleToRemove);
		
		
		removableOwnerRole.remove();
		
		verify(firstRoleToKeep, never()).setNextDbkeyPosition(any(short.class));		
		verify(firstRoleToKeep, never()).setPriorDbkeyPosition(any(Short.class));				
				
		verify(roleToRemove, times(1)).setNextDbkeyPosition((short) 0);
		verify(roleToRemove, times(1)).setNextDbkeyPosition(any(short.class));
		verify(roleToRemove, times(1)).setPriorDbkeyPosition(null);
		verify(roleToRemove, times(1)).setPriorDbkeyPosition(any(Short.class));
		
		verify(secondRoleToKeep, times(1)).setNextDbkeyPosition((short) 3);
		verify(secondRoleToKeep, times(1)).setNextDbkeyPosition(any(short.class));
		verify(secondRoleToKeep, times(1)).setPriorDbkeyPosition(Short.valueOf((short) 4));
		verify(secondRoleToKeep, times(1)).setPriorDbkeyPosition(any(Short.class));		
		
		verify(thirdRoleToKeep, times(1)).setNextDbkeyPosition((short) 5);
		verify(thirdRoleToKeep, times(1)).setNextDbkeyPosition(any(short.class));
		verify(thirdRoleToKeep, times(1)).setPriorDbkeyPosition(Short.valueOf((short) 6));
		verify(thirdRoleToKeep, times(1)).setPriorDbkeyPosition(any(Short.class));		
		verify(thirdRoleToKeep, times(1)).setOwnerDbkeyPosition(Short.valueOf((short) 7));
		verify(thirdRoleToKeep, times(1)).setOwnerDbkeyPosition(any(Short.class));
		verify(thirdRoleToKeep, never()).setIndexDbkeyPosition(any(Short.class));		
		
		verify(ownerRoles, times(1)).remove(roleToRemove);
		verify(ownerRoles, times(1)).remove(any(OwnerRole.class));
		
		verify(firstSetToKeep, never()).setOwner(any(OwnerRole.class));
		verify(firstSetToKeep, never()).getMembers();
		
		verify(setToRemove, times(1)).setOwner(null);
		verify(setToRemove, times(1)).setOwner(any(OwnerRole.class));
		verify(setToRemove, never()).getMembers();
		
		verify(secondSetToKeep, never()).setOwner(any(OwnerRole.class));
		verify(secondSetToKeep, never()).getMembers();
		
		verify(thirdSetToKeep, never()).setOwner(any(OwnerRole.class));
		verify(thirdSetToKeep, never()).getMembers();
		
		
		try {
			removableOwnerRole.remove();
			fail("should throw an UnsupportedOperationException because already removed");
		} catch (UnsupportedOperationException e) {
			assertEquals("already removed", e.getMessage());
		}
		
		
		removableOwnerRole.restore();
		
		verify(firstRoleToKeep, never()).setNextDbkeyPosition(any(short.class));		
		verify(firstRoleToKeep, never()).setPriorDbkeyPosition(any(Short.class));
		verify(firstSetToKeep, never()).setOwner(any(OwnerRole.class));
		verify(firstSetToKeep, never()).getMembers();
		
		verify(roleToRemove, times(1)).setNextDbkeyPosition((short) 0); 
		verify(roleToRemove, times(1)).setNextDbkeyPosition((short) 3); 
		verify(roleToRemove, times(2)).setNextDbkeyPosition(any(short.class));
		verify(roleToRemove, times(1)).setPriorDbkeyPosition(null); 	
		verify(roleToRemove, times(1)).setPriorDbkeyPosition(Short.valueOf((short) 4));
		verify(roleToRemove, times(2)).setPriorDbkeyPosition(any(Short.class));
		
		verify(secondRoleToKeep, times(1)).setNextDbkeyPosition(Short.valueOf((short) 3));
		verify(secondRoleToKeep, times(1)).setNextDbkeyPosition(Short.valueOf((short) 5));	
		verify(secondRoleToKeep, times(2)).setNextDbkeyPosition(any(Short.class));	
		verify(secondRoleToKeep, times(1)).setPriorDbkeyPosition(Short.valueOf((short) 4));
		verify(secondRoleToKeep, times(1)).setPriorDbkeyPosition(Short.valueOf((short) 6));
		verify(secondRoleToKeep, times(2)).setPriorDbkeyPosition(any(Short.class));	
		
		verify(thirdRoleToKeep, times(1)).setNextDbkeyPosition((short) 5);
		verify(thirdRoleToKeep, times(1)).setNextDbkeyPosition((short) 7);
		verify(thirdRoleToKeep, times(2)).setNextDbkeyPosition(any(short.class));
		verify(thirdRoleToKeep, times(1)).setPriorDbkeyPosition(Short.valueOf((short) 6));
		verify(thirdRoleToKeep, times(1)).setPriorDbkeyPosition(Short.valueOf((short) 8));
		verify(thirdRoleToKeep, times(2)).setPriorDbkeyPosition(any(Short.class));		
		verify(thirdRoleToKeep, times(1)).setOwnerDbkeyPosition(Short.valueOf((short) 7));
		verify(thirdRoleToKeep, times(1)).setOwnerDbkeyPosition(Short.valueOf((short) 9));
		verify(thirdRoleToKeep, times(2)).setOwnerDbkeyPosition(any(Short.class));
		verify(thirdRoleToKeep, never()).setIndexDbkeyPosition(any(Short.class));				
		
		verify(ownerRoles, times(1)).add(1, roleToRemove);
		verify(ownerRoles, times(1)).add(any(int.class), any(OwnerRole.class));
		
		verify(setToRemove, times(1)).setOwner(null);
		verify(setToRemove, times(1)).setOwner(roleToRemove);
		verify(setToRemove, times(2)).setOwner(any(OwnerRole.class));
		verify(firstSetToKeep, never()).getMembers();
		
		verify(secondSetToKeep, never()).setOwner(any(OwnerRole.class));
		verify(secondSetToKeep, never()).getMembers();
		
		verify(thirdSetToKeep, never()).setOwner(any(OwnerRole.class));
		verify(thirdSetToKeep, never()).getMembers();
		
		
		try {
			removableOwnerRole.restore();
			fail("should throw an UnsupportedOperationException because not removed");
		} catch (UnsupportedOperationException e) {
			assertEquals("not removed", e.getMessage());
		}
		
		
		removableOwnerRole.remove();
		
		
		removableOwnerRole.restore();
		
	}

}
