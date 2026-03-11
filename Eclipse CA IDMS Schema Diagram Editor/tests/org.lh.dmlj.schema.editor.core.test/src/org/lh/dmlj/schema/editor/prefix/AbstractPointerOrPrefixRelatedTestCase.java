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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;

public class AbstractPointerOrPrefixRelatedTestCase {
	
	protected MemberRole mockMemberRoleWithNoPointersSet(String recordName, String setName) {
		
		SchemaRecord record = mock(SchemaRecord.class);
		when(record.getName()).thenReturn(recordName);
		
		Set set = mock(Set.class);
		when(set.getName()).thenReturn(setName);
		
		MemberRole role = mock(MemberRole.class);
		when(role.getRecord()).thenReturn(record);
		when(role.getSet()).thenReturn(set);
		when(role.toString()).thenReturn("MemberRole: record=" + recordName + ", set=" + setName);
		
		when(role.getNextDbkeyPosition()).thenReturn(null);
		when(role.getPriorDbkeyPosition()).thenReturn(null);
		when(role.getOwnerDbkeyPosition()).thenReturn(null);
		when(role.getIndexDbkeyPosition()).thenReturn(null);
		
		return role;
		
	}	

	protected OwnerRole mockOwnerRoleWithNoPointersSet(String recordName, String setName) {
		
		SchemaRecord record = mock(SchemaRecord.class);
		when(record.getName()).thenReturn(recordName);
		
		Set set = mock(Set.class);
		when(set.getName()).thenReturn(setName);
		
		OwnerRole role = mock(OwnerRole.class);
		when(role.getRecord()).thenReturn(record);
		when(role.getSet()).thenReturn(set);
		when(role.toString()).thenReturn("OwnerRole: record=" + recordName + ", set=" + setName);
		
		when(role.getNextDbkeyPosition()).thenReturn((short) 0);
		when(role.getPriorDbkeyPosition()).thenReturn(null);
		
		return role;
		
	}	

}
