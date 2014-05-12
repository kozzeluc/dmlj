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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.junit.Test;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.editor.command.helper.RemovableMemberRole.DiagramLocationIndex;
import org.mockito.ArgumentCaptor;

public class RemovableMemberRoleTest {
	
	private static void testRemovableMemberRoleForDiagramLocationTesting(
		int connectionLabelLocationIndex,
		int sourceEndpointLocationIndex,
		int targetEndpointLocationIndex) {
		
		Schema schema = mock(Schema.class);
		
		DiagramData diagramData = mock(DiagramData.class);
		when(schema.getDiagramData()).thenReturn(diagramData);
		
		@SuppressWarnings("unchecked")
		EList<ConnectionLabel> diagramDataConnectionLabels = mock(EList.class);
		when(diagramData.getConnectionLabels()).thenReturn(diagramDataConnectionLabels);
		
		@SuppressWarnings("unchecked")
		EList<DiagramLocation> diagramDataLocations = mock(EList.class);
		when(diagramData.getLocations()).thenReturn(diagramDataLocations);
		
		@SuppressWarnings("unchecked")
		EList<ConnectionPart> diagramDataConnectionParts = mock(EList.class);
		when(diagramData.getConnectionParts()).thenReturn(diagramDataConnectionParts);
		
		SchemaRecord record = mock(SchemaRecord.class);
		when(record.getSchema()).thenReturn(schema);
		
		Set impactedSet = mock(Set.class);
		when(impactedSet.getSchema()).thenReturn(schema);
		when(impactedSet.getOrder()).thenReturn(SetOrder.LAST);		
		
		MemberRole roleToRemove = mock(MemberRole.class);
		when(roleToRemove.getRecord()).thenReturn(record);
		when(roleToRemove.getSet()).thenReturn(impactedSet);
		when(roleToRemove.getNextDbkeyPosition()).thenReturn(Short.valueOf((short) 1));
		when(roleToRemove.getPriorDbkeyPosition()).thenReturn(null);
		when(roleToRemove.getOwnerDbkeyPosition()).thenReturn(null);
		when(roleToRemove.getIndexDbkeyPosition()).thenReturn(null);		
		
		List<OwnerRole> recordOwnerRoleList = new ArrayList<>();
		
		@SuppressWarnings("unchecked")
		EList<OwnerRole> recordOwnerRoles = mock(EList.class);
		when(recordOwnerRoles.iterator()).thenReturn(recordOwnerRoleList.iterator());		
		when(recordOwnerRoles.indexOf(anyObject())).thenThrow(new RuntimeException("not to be called"));
		when(record.getOwnerRoles()).thenReturn(recordOwnerRoles);
		
		List<MemberRole> recordMemberRoleList = new ArrayList<>();
		recordMemberRoleList.add(roleToRemove);		
		
		@SuppressWarnings("unchecked")
		EList<MemberRole> recordMemberRoles = mock(EList.class);
		when(recordMemberRoles.iterator()).thenReturn(recordMemberRoleList.iterator());				
		when(recordMemberRoles.indexOf(roleToRemove)).thenReturn(0);
		when(record.getMemberRoles()).thenReturn(recordMemberRoles);
		
		@SuppressWarnings("unchecked")
		EList<MemberRole> setMemberRoles = mock(EList.class);
		when(setMemberRoles.indexOf(roleToRemove)).thenReturn(0);
		when(impactedSet.getMembers()).thenReturn(setMemberRoles);
		
		when(roleToRemove.getSortKey()).thenReturn(null);
		
		ConnectionLabel connectionLabel = mock(ConnectionLabel.class);
		when(roleToRemove.getConnectionLabel()).thenReturn(connectionLabel);
		when(diagramDataConnectionLabels.indexOf(connectionLabel)).thenReturn(0);		
		
		ConnectionPart connectionPart = mock(ConnectionPart.class);
		@SuppressWarnings("unchecked")
		EList<ConnectionPart> roleConnectionParts = mock(EList.class);
		when(roleConnectionParts.get(0)).thenReturn(connectionPart);
		when(roleToRemove.getConnectionParts()).thenReturn(roleConnectionParts);
		when(diagramDataConnectionParts.indexOf(connectionPart)).thenReturn(0);
		
		DiagramLocation connectionLabelLocation = mock(DiagramLocation.class);
		when(connectionLabel.getDiagramLocation()).thenReturn(connectionLabelLocation);
		when(diagramDataLocations.indexOf(connectionLabelLocation)).thenReturn(connectionLabelLocationIndex);
		
		DiagramLocation sourceEndpointLocation = mock(DiagramLocation.class);
		if (sourceEndpointLocationIndex > -1) {
			when(connectionPart.getSourceEndpointLocation()).thenReturn(sourceEndpointLocation);
			when(diagramDataLocations.indexOf(sourceEndpointLocation)).thenReturn(sourceEndpointLocationIndex);
		}
		
		DiagramLocation targetEndpointLocation = mock(DiagramLocation.class);
		if (targetEndpointLocationIndex > -1) {
			when(connectionPart.getTargetEndpointLocation()).thenReturn(targetEndpointLocation);
			when(diagramDataLocations.indexOf(targetEndpointLocation)).thenReturn(targetEndpointLocationIndex);
		}
				
		RemovableMemberRole removableMemberRole = new RemovableMemberRole(roleToRemove);
		
		removableMemberRole.remove();		
		removableMemberRole.restore();	
		
		List<DiagramLocationIndex> diagramLocationIndexes = new ArrayList<>();
		diagramLocationIndexes.add(new DiagramLocationIndex(connectionLabelLocation, 
														connectionLabelLocationIndex));
		if (sourceEndpointLocationIndex > -1) {
			diagramLocationIndexes.add(new DiagramLocationIndex(sourceEndpointLocation, 
															sourceEndpointLocationIndex));
		}
		if (targetEndpointLocationIndex > -1) {
			diagramLocationIndexes.add(new DiagramLocationIndex(targetEndpointLocation, 
															targetEndpointLocationIndex));
		}
		Collections.sort(diagramLocationIndexes);
		
		int transformedConnectionLabelLocationIndex = -1;
		int transformedSourceEndpointLocationIndex = -1;
		int transformedTargetEndpointLocationIndex = -1;
		for (int i = 0; i < diagramLocationIndexes.size(); i++) {
			DiagramLocationIndex index = diagramLocationIndexes.get(i);
			if (index.diagramLocation == connectionLabelLocation) {
				transformedConnectionLabelLocationIndex = i;
			} else if (index.diagramLocation == sourceEndpointLocation) {
				transformedSourceEndpointLocationIndex = i;
			} else {
				transformedTargetEndpointLocationIndex = i;
			}
		}		
		
		// it is important that all diagram locations are restored back to their original values;
		// the order in which these values are restored matters
		ArgumentCaptor<Integer> indexCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<DiagramLocation> elementCaptor = ArgumentCaptor.forClass(DiagramLocation.class);	
		verify(diagramDataLocations, times(diagramLocationIndexes.size())).add(indexCaptor.capture(), 
																			   elementCaptor.capture());
		List<Integer> indexes = indexCaptor.getAllValues();				
		
		assertEquals(Integer.valueOf(connectionLabelLocationIndex), 
					 indexes.get(transformedConnectionLabelLocationIndex));
		if (sourceEndpointLocationIndex > -1) {
			assertEquals(Integer.valueOf(sourceEndpointLocationIndex), 
					 	 indexes.get(transformedSourceEndpointLocationIndex));
		}
		if (targetEndpointLocationIndex > -1) {
			assertEquals(Integer.valueOf(targetEndpointLocationIndex), 
					 	 indexes.get(transformedTargetEndpointLocationIndex));
		}
		List<DiagramLocation> diagramLocationArguments = elementCaptor.getAllValues();		
		assertSame(connectionLabelLocation, 
				   diagramLocationArguments.get(transformedConnectionLabelLocationIndex));
		if (sourceEndpointLocationIndex > -1) {
			assertSame(sourceEndpointLocation, 
					   diagramLocationArguments.get(transformedSourceEndpointLocationIndex));
		}
		if (targetEndpointLocationIndex > -1) {
			assertSame(targetEndpointLocation, 
					   diagramLocationArguments.get(transformedTargetEndpointLocationIndex));		
		}
		
	}

	@Test
	public void testUnsortedChainedSet() {
		
		Schema schema = mock(Schema.class);
		
		DiagramData diagramData = mock(DiagramData.class);
		when(schema.getDiagramData()).thenReturn(diagramData);
		
		@SuppressWarnings("unchecked")
		EList<ConnectionLabel> diagramDataConnectionLabels = mock(EList.class);
		when(diagramData.getConnectionLabels()).thenReturn(diagramDataConnectionLabels);
		
		@SuppressWarnings("unchecked")
		EList<DiagramLocation> diagramDataLocations = mock(EList.class);
		when(diagramData.getLocations()).thenReturn(diagramDataLocations);
		
		@SuppressWarnings("unchecked")
		EList<ConnectionPart> diagramDataConnectionParts = mock(EList.class);
		when(diagramData.getConnectionParts()).thenReturn(diagramDataConnectionParts);
		
		SchemaRecord record = mock(SchemaRecord.class);
		when(record.getSchema()).thenReturn(schema);
		
		Set impactedSet = mock(Set.class);
		when(impactedSet.getSchema()).thenReturn(schema);
		when(impactedSet.getOrder()).thenReturn(SetOrder.LAST);
		Set firstSetToKeep = mock(Set.class);
		Set secondSetToKeep = mock(Set.class);
		Set thirdSetToKeep = mock(Set.class);
		Set fourthSetToKeep = mock(Set.class);
		
		MemberRole firstRoleToKeep = mock(MemberRole.class);
		when(firstRoleToKeep.getRecord()).thenReturn(record);
		when(firstRoleToKeep.getSet()).thenReturn(firstSetToKeep);
		when(firstRoleToKeep.getNextDbkeyPosition()).thenReturn(Short.valueOf((short) 1));
		when(firstRoleToKeep.getPriorDbkeyPosition()).thenReturn(Short.valueOf((short) 2));
		when(firstRoleToKeep.getOwnerDbkeyPosition()).thenReturn(Short.valueOf((short) 3));
		when(firstRoleToKeep.getIndexDbkeyPosition()).thenReturn(null);		
		
		OwnerRole secondRoleToKeep = mock(OwnerRole.class);
		when(secondRoleToKeep.getRecord()).thenReturn(record);
		when(secondRoleToKeep.getSet()).thenReturn(secondSetToKeep);
		when(secondRoleToKeep.getNextDbkeyPosition()).thenReturn(Short.valueOf((short) 4));
		when(secondRoleToKeep.getPriorDbkeyPosition()).thenReturn(Short.valueOf((short) 5));
		
		MemberRole roleToRemove = mock(MemberRole.class);
		when(roleToRemove.getRecord()).thenReturn(record);
		when(roleToRemove.getSet()).thenReturn(impactedSet);
		when(roleToRemove.getNextDbkeyPosition()).thenReturn(Short.valueOf((short) 6));
		when(roleToRemove.getPriorDbkeyPosition()).thenReturn(Short.valueOf((short) 7));
		when(roleToRemove.getOwnerDbkeyPosition()).thenReturn(Short.valueOf((short) 8));
		when(roleToRemove.getIndexDbkeyPosition()).thenReturn(null);
		
		OwnerRole thirdRoleToKeep = mock(OwnerRole.class);
		when(thirdRoleToKeep.getRecord()).thenReturn(record);
		when(thirdRoleToKeep.getSet()).thenReturn(thirdSetToKeep);
		when(thirdRoleToKeep.getNextDbkeyPosition()).thenReturn(Short.valueOf((short) 9));
		when(thirdRoleToKeep.getPriorDbkeyPosition()).thenReturn(Short.valueOf((short) 10));
		
		MemberRole fourthRoleToKeep = mock(MemberRole.class);
		when(fourthRoleToKeep.getRecord()).thenReturn(record);
		when(fourthRoleToKeep.getSet()).thenReturn(fourthSetToKeep);
		when(fourthRoleToKeep.getNextDbkeyPosition()).thenReturn(Short.valueOf((short) 11));
		when(fourthRoleToKeep.getPriorDbkeyPosition()).thenReturn(Short.valueOf((short) 12));
		when(fourthRoleToKeep.getOwnerDbkeyPosition()).thenReturn(Short.valueOf((short) 13));
		when(fourthRoleToKeep.getIndexDbkeyPosition()).thenReturn(null);
		
		List<OwnerRole> recordOwnerRoleList = new ArrayList<>();
		recordOwnerRoleList.add(secondRoleToKeep);	
		recordOwnerRoleList.add(thirdRoleToKeep);		
		
		@SuppressWarnings("unchecked")
		EList<OwnerRole> recordOwnerRoles = mock(EList.class);
		when(recordOwnerRoles.iterator()).thenReturn(recordOwnerRoleList.iterator());		
		when(recordOwnerRoles.indexOf(anyObject())).thenThrow(new RuntimeException("not to be called"));
		when(record.getOwnerRoles()).thenReturn(recordOwnerRoles);
		
		List<MemberRole> recordMemberRoleList = new ArrayList<>();
		recordMemberRoleList.add(firstRoleToKeep);
		recordMemberRoleList.add(roleToRemove);
		recordMemberRoleList.add(fourthRoleToKeep);
		
		@SuppressWarnings("unchecked")
		EList<MemberRole> recordMemberRoles = mock(EList.class);
		when(recordMemberRoles.iterator()).thenReturn(recordMemberRoleList.iterator());		
		when(recordMemberRoles.indexOf(firstRoleToKeep)).thenThrow(new RuntimeException("not to be called"));
		when(recordMemberRoles.indexOf(roleToRemove)).thenReturn(1);
		when(recordMemberRoles.indexOf(fourthRoleToKeep)).thenThrow(new RuntimeException("not to be called"));
		when(record.getMemberRoles()).thenReturn(recordMemberRoles);
		
		@SuppressWarnings("unchecked")
		EList<MemberRole> setMemberRoles = mock(EList.class);
		when(setMemberRoles.indexOf(roleToRemove)).thenReturn(13);
		when(impactedSet.getMembers()).thenReturn(setMemberRoles);
		
		when(roleToRemove.getSortKey()).thenReturn(null);
		
		ConnectionLabel connectionLabel = mock(ConnectionLabel.class);
		when(roleToRemove.getConnectionLabel()).thenReturn(connectionLabel);
		when(diagramDataConnectionLabels.indexOf(connectionLabel)).thenReturn(7);

		DiagramLocation connectionLabelLocation = mock(DiagramLocation.class);
		when(connectionLabel.getDiagramLocation()).thenReturn(connectionLabelLocation);
		when(diagramDataLocations.indexOf(connectionLabelLocation)).thenReturn(5);
		
		ConnectionPart connectionPart = mock(ConnectionPart.class);
		@SuppressWarnings("unchecked")
		EList<ConnectionPart> roleConnectionParts = mock(EList.class);
		when(roleConnectionParts.get(0)).thenReturn(connectionPart);
		when(roleToRemove.getConnectionParts()).thenReturn(roleConnectionParts);
		when(diagramDataConnectionParts.indexOf(connectionPart)).thenReturn(3);
		
		DiagramLocation sourceEndpointLocation = mock(DiagramLocation.class);
		when(connectionPart.getSourceEndpointLocation()).thenReturn(sourceEndpointLocation);
		when(diagramDataLocations.indexOf(sourceEndpointLocation)).thenReturn(27);
		
		DiagramLocation targetEndpointLocation = mock(DiagramLocation.class);
		when(connectionPart.getTargetEndpointLocation()).thenReturn(targetEndpointLocation);
		when(diagramDataLocations.indexOf(targetEndpointLocation)).thenReturn(29);
		
		RemovableMemberRole removableMemberRole = new RemovableMemberRole(roleToRemove);		
		
		verify(recordOwnerRoles, times(1)).iterator();			
		verify(recordMemberRoles, times(1)).iterator();
		
		removableMemberRole.remove();
		
		verify(recordOwnerRoles, times(1)).iterator();			
		verify(recordMemberRoles, times(1)).iterator();
		
		verify(firstRoleToKeep, never()).setNextDbkeyPosition(any(Short.class));
		verify(firstRoleToKeep, never()).setPriorDbkeyPosition(any(Short.class));
		verify(firstRoleToKeep, never()).setOwnerDbkeyPosition(any(Short.class));
		verify(firstRoleToKeep, never()).setIndexDbkeyPosition(any(Short.class));
		
		verify(secondRoleToKeep, never()).setNextDbkeyPosition(any(Short.class));
		verify(secondRoleToKeep, never()).setPriorDbkeyPosition(any(Short.class));		
		
		verify(roleToRemove, times(1)).setNextDbkeyPosition(null);
		verify(roleToRemove, times(1)).setNextDbkeyPosition(any(Short.class));
		verify(roleToRemove, times(1)).setPriorDbkeyPosition(null);
		verify(roleToRemove, times(1)).setPriorDbkeyPosition(any(Short.class));		
		verify(roleToRemove, times(1)).setOwnerDbkeyPosition(null);
		verify(roleToRemove, times(1)).setOwnerDbkeyPosition(any(Short.class));
		verify(roleToRemove, never()).setIndexDbkeyPosition(any(Short.class));
		
		verify(thirdRoleToKeep, times(1)).setNextDbkeyPosition(Short.valueOf((short) 6));
		verify(thirdRoleToKeep, times(1)).setNextDbkeyPosition(any(Short.class));
		verify(thirdRoleToKeep, times(1)).setPriorDbkeyPosition(Short.valueOf((short) 7));
		verify(thirdRoleToKeep, times(1)).setPriorDbkeyPosition(any(Short.class));
		
		verify(fourthRoleToKeep, times(1)).setNextDbkeyPosition(Short.valueOf((short) 8));
		verify(fourthRoleToKeep, times(1)).setNextDbkeyPosition(any(Short.class));
		verify(fourthRoleToKeep, times(1)).setPriorDbkeyPosition(Short.valueOf((short) 9));
		verify(fourthRoleToKeep, times(1)).setPriorDbkeyPosition(any(Short.class));		
		verify(fourthRoleToKeep, times(1)).setOwnerDbkeyPosition(Short.valueOf((short) 10));
		verify(fourthRoleToKeep, times(1)).setOwnerDbkeyPosition(any(Short.class));
		verify(fourthRoleToKeep, never()).setIndexDbkeyPosition(any(Short.class));		
		
		verify(recordOwnerRoles, never()).remove(any(OwnerRole.class));
		verify(recordMemberRoles, times(1)).remove(roleToRemove);
		verify(recordMemberRoles, times(1)).remove(any(MemberRole.class));
		
		verify(firstSetToKeep, never()).setOwner(any(OwnerRole.class));
		verify(firstSetToKeep, never()).getMembers();
		
		verify(secondSetToKeep, never()).setOwner(any(OwnerRole.class));
		verify(secondSetToKeep, never()).getMembers();
		
		verify(impactedSet, never()).setOwner(any(OwnerRole.class));
		verify(setMemberRoles, times(1)).remove(roleToRemove);
		verify(setMemberRoles, times(1)).remove(any(MemberRole.class));
		
		verify(thirdSetToKeep, never()).setOwner(any(OwnerRole.class));
		verify(thirdSetToKeep, never()).getMembers();
		
		verify(fourthSetToKeep, never()).setOwner(any(OwnerRole.class));
		verify(fourthSetToKeep, never()).getMembers();
		
		verify(roleToRemove, never()).getSortKey();
		verify(roleToRemove, never()).setSortKey(any(Key.class));
		verify(record, never()).getKeys();
		
		verify(diagramDataConnectionParts, times(1)).remove(connectionPart);
		verify(diagramDataConnectionParts, times(1)).remove(any(ConnectionPart.class));
		verify(diagramDataLocations, times(1)).remove(sourceEndpointLocation);
		verify(diagramDataLocations, times(1)).remove(targetEndpointLocation);
		
		verify(diagramDataConnectionLabels, times(1)).remove(connectionLabel);
		verify(diagramDataConnectionLabels, times(1)).remove(any(ConnectionLabel.class));
		verify(diagramDataLocations, times(1)).remove(connectionLabelLocation);
		
		verify(diagramDataLocations, times(3)).remove(any(DiagramLocation.class));
		
		
		try {
			removableMemberRole.remove();
			fail("should throw an UnsupportedOperationException because already removed");
		} catch (UnsupportedOperationException e) {
			assertEquals("already removed", e.getMessage());
		}
		
		
		removableMemberRole.restore();
		
		verify(recordOwnerRoles, times(1)).iterator();			
		verify(recordMemberRoles, times(1)).iterator();
		
		verify(firstRoleToKeep, never()).setNextDbkeyPosition(any(Short.class));
		verify(firstRoleToKeep, never()).setPriorDbkeyPosition(any(Short.class));
		verify(firstRoleToKeep, never()).setOwnerDbkeyPosition(any(Short.class));
		verify(firstRoleToKeep, never()).setIndexDbkeyPosition(any(Short.class));
		
		verify(secondRoleToKeep, never()).setNextDbkeyPosition(any(Short.class));
		verify(secondRoleToKeep, never()).setPriorDbkeyPosition(any(Short.class));		
		
		verify(roleToRemove, times(1)).setNextDbkeyPosition(null);
		verify(roleToRemove, times(1)).setNextDbkeyPosition(Short.valueOf((short) 6));
		verify(roleToRemove, times(2)).setNextDbkeyPosition(any(Short.class));
		verify(roleToRemove, times(1)).setPriorDbkeyPosition(null);
		verify(roleToRemove, times(1)).setPriorDbkeyPosition(Short.valueOf((short) 7));
		verify(roleToRemove, times(2)).setPriorDbkeyPosition(any(Short.class));		
		verify(roleToRemove, times(1)).setOwnerDbkeyPosition(null);
		verify(roleToRemove, times(1)).setOwnerDbkeyPosition(Short.valueOf((short) 8));
		verify(roleToRemove, times(2)).setOwnerDbkeyPosition(any(Short.class));
		verify(roleToRemove, never()).setIndexDbkeyPosition(any(Short.class));
		
		verify(thirdRoleToKeep, times(1)).setNextDbkeyPosition(Short.valueOf((short) 6));
		verify(thirdRoleToKeep, times(1)).setNextDbkeyPosition(Short.valueOf((short) 9));
		verify(thirdRoleToKeep, times(2)).setNextDbkeyPosition(any(Short.class));
		verify(thirdRoleToKeep, times(1)).setPriorDbkeyPosition(Short.valueOf((short) 7));
		verify(thirdRoleToKeep, times(1)).setPriorDbkeyPosition(Short.valueOf((short) 10));
		verify(thirdRoleToKeep, times(2)).setPriorDbkeyPosition(any(Short.class));
		
		verify(fourthRoleToKeep, times(1)).setNextDbkeyPosition(Short.valueOf((short) 8));
		verify(fourthRoleToKeep, times(1)).setNextDbkeyPosition(Short.valueOf((short) 11));
		verify(fourthRoleToKeep, times(2)).setNextDbkeyPosition(any(Short.class));
		verify(fourthRoleToKeep, times(1)).setPriorDbkeyPosition(Short.valueOf((short) 9));
		verify(fourthRoleToKeep, times(1)).setPriorDbkeyPosition(Short.valueOf((short) 12));
		verify(fourthRoleToKeep, times(2)).setPriorDbkeyPosition(any(Short.class));		
		verify(fourthRoleToKeep, times(1)).setOwnerDbkeyPosition(Short.valueOf((short) 10));
		verify(fourthRoleToKeep, times(1)).setOwnerDbkeyPosition(Short.valueOf((short) 13));
		verify(fourthRoleToKeep, times(2)).setOwnerDbkeyPosition(any(Short.class));
		verify(fourthRoleToKeep, never()).setIndexDbkeyPosition(any(Short.class));
		
		verify(recordMemberRoles, times(1)).add(1, roleToRemove);
		verify(recordMemberRoles, times(1)).add(any(int.class), any(MemberRole.class));
		
		verify(setMemberRoles, times(1)).add(13, roleToRemove);
		verify(setMemberRoles, times(1)).add(any(int.class), any(MemberRole.class));
		
		verify(roleToRemove, never()).setSortKey(any(Key.class));
		verify(record, never()).getKeys();
		
		verify(diagramDataConnectionParts, times(1)).add(3, connectionPart);
		verify(diagramDataConnectionParts, times(1)).add(any(int.class), any(ConnectionPart.class));
		verify(diagramDataLocations, times(1)).add(27, sourceEndpointLocation);		
		verify(diagramDataLocations, times(1)).add(29, targetEndpointLocation);
		
		verify(diagramDataConnectionLabels, times(1)).add(7, connectionLabel);
		verify(diagramDataConnectionLabels, times(1)).add(any(int.class), any(ConnectionLabel.class));
		
		verify(diagramDataLocations, times(1)).add(5, connectionLabelLocation);
		
		verify(diagramDataLocations, times(3)).add(any(int.class), any(DiagramLocation.class));
		
		
		removableMemberRole.remove();
		
		
		removableMemberRole.restore();
		
	}

	@Test
	public void testSortedIndexedSet() {
		
		Schema schema = mock(Schema.class);
		
		DiagramData diagramData = mock(DiagramData.class);
		when(schema.getDiagramData()).thenReturn(diagramData);
		
		@SuppressWarnings("unchecked")
		EList<ConnectionLabel> diagramDataConnectionLabels = mock(EList.class);
		when(diagramData.getConnectionLabels()).thenReturn(diagramDataConnectionLabels);
		
		@SuppressWarnings("unchecked")
		EList<DiagramLocation> diagramDataLocations = mock(EList.class);
		when(diagramData.getLocations()).thenReturn(diagramDataLocations);
		
		@SuppressWarnings("unchecked")
		EList<ConnectionPart> diagramDataConnectionParts = mock(EList.class);
		when(diagramData.getConnectionParts()).thenReturn(diagramDataConnectionParts);		
		
		SchemaRecord record = mock(SchemaRecord.class);
		when(record.getSchema()).thenReturn(schema);
		
		Set impactedSet = mock(Set.class);
		when(impactedSet.getSchema()).thenReturn(schema);
		when(impactedSet.getOrder()).thenReturn(SetOrder.SORTED);
		
		MemberRole roleToRemove = mock(MemberRole.class);
		when(roleToRemove.getRecord()).thenReturn(record);
		when(roleToRemove.getSet()).thenReturn(impactedSet);
		when(roleToRemove.getNextDbkeyPosition()).thenReturn(null);
		when(roleToRemove.getPriorDbkeyPosition()).thenReturn(null);
		when(roleToRemove.getOwnerDbkeyPosition()).thenReturn(Short.valueOf((short) 1));
		when(roleToRemove.getIndexDbkeyPosition()).thenReturn(Short.valueOf((short) 2));	
		
		List<OwnerRole> recordOwnerRoleList = new ArrayList<>();
		
		@SuppressWarnings("unchecked")
		EList<OwnerRole> recordOwnerRoles = mock(EList.class);
		when(recordOwnerRoles.iterator()).thenReturn(recordOwnerRoleList.iterator());		
		when(recordOwnerRoles.indexOf(anyObject())).thenThrow(new RuntimeException("not to be called"));
		when(record.getOwnerRoles()).thenReturn(recordOwnerRoles);
		
		List<MemberRole> recordMemberRoleList = new ArrayList<>();
		recordMemberRoleList.add(roleToRemove);		
		
		@SuppressWarnings("unchecked")
		EList<MemberRole> recordMemberRoles = mock(EList.class);
		when(recordMemberRoles.iterator()).thenReturn(recordMemberRoleList.iterator());		
		when(recordMemberRoles.indexOf(roleToRemove)).thenReturn(0);
		when(record.getMemberRoles()).thenReturn(recordMemberRoles);
		
		@SuppressWarnings("unchecked")
		EList<MemberRole> setMemberRoles = mock(EList.class);
		when(setMemberRoles.indexOf(roleToRemove)).thenReturn(0);
		when(impactedSet.getMembers()).thenReturn(setMemberRoles);
		
		Key sortKey = mock(Key.class);
		when(roleToRemove.getSortKey()).thenReturn(sortKey);
		
		Element firstElement = mock(Element.class);
		KeyElement firstKeyElement= mock(KeyElement.class);
		when(firstKeyElement.getElement()).thenReturn(firstElement);
		@SuppressWarnings("unchecked")
		EList<KeyElement> firstElementsKeyElements = mock(EList.class);
		when(firstElement.getKeyElements()).thenReturn(firstElementsKeyElements);
		when(firstElementsKeyElements.indexOf(firstKeyElement)).thenReturn(13);
		
		Element secondElement = mock(Element.class);
		KeyElement secondKeyElement= mock(KeyElement.class);
		when(secondKeyElement.getElement()).thenReturn(secondElement);
		@SuppressWarnings("unchecked")
		EList<KeyElement> secondElementsKeyElements = mock(EList.class);
		when(secondElement.getKeyElements()).thenReturn(secondElementsKeyElements);
		when(secondElementsKeyElements.indexOf(secondKeyElement)).thenReturn(17);
		
		List<KeyElement> keyElementList = new ArrayList<>();
		keyElementList.add(firstKeyElement);
		keyElementList.add(secondKeyElement);
		
		@SuppressWarnings("unchecked")
		EList<KeyElement> keyElements = mock(EList.class);
		when(keyElements.size()).thenReturn(2);
		when(keyElements.iterator()).thenReturn(keyElementList.iterator());
		when(sortKey.getElements()).thenReturn(keyElements);		
		
		when(roleToRemove.getSortKey()).thenReturn(sortKey);
		
		@SuppressWarnings("unchecked")
		EList<Key> recordSortKeys = mock(EList.class);
		when(record.getKeys()).thenReturn(recordSortKeys);
		when(recordSortKeys.indexOf(sortKey)).thenReturn(5);
		
		ConnectionLabel connectionLabel = mock(ConnectionLabel.class);
		when(roleToRemove.getConnectionLabel()).thenReturn(connectionLabel);
		when(diagramDataConnectionLabels.indexOf(connectionLabel)).thenReturn(0);

		DiagramLocation connectionLabelLocation = mock(DiagramLocation.class);
		when(connectionLabel.getDiagramLocation()).thenReturn(connectionLabelLocation);
		when(diagramDataLocations.indexOf(connectionLabelLocation)).thenReturn(0);
		
		ConnectionPart connectionPart = mock(ConnectionPart.class);
		@SuppressWarnings("unchecked")
		EList<ConnectionPart> roleConnectionParts = mock(EList.class);
		when(roleConnectionParts.get(0)).thenReturn(connectionPart);
		when(roleToRemove.getConnectionParts()).thenReturn(roleConnectionParts);
		when(diagramDataConnectionParts.indexOf(connectionPart)).thenReturn(0);
		
		DiagramLocation sourceEndpointLocation = mock(DiagramLocation.class);
		when(connectionPart.getSourceEndpointLocation()).thenReturn(sourceEndpointLocation);
		when(diagramDataLocations.indexOf(sourceEndpointLocation)).thenReturn(1);
		
		DiagramLocation targetEndpointLocation = mock(DiagramLocation.class);
		when(connectionPart.getTargetEndpointLocation()).thenReturn(targetEndpointLocation);
		when(diagramDataLocations.indexOf(targetEndpointLocation)).thenReturn(2);
		
		
		RemovableMemberRole removableMemberRole = new RemovableMemberRole(roleToRemove);
		
		verify(recordOwnerRoles, times(1)).iterator();			
		verify(recordMemberRoles, times(1)).iterator();				
		verify(keyElements, times(1)).iterator(); // the iterator will be consumed, install a new one:
		when(keyElements.iterator()).thenReturn(keyElementList.iterator());
		
		
		removableMemberRole.remove();
		
		verify(recordOwnerRoles, times(1)).iterator();			
		verify(recordMemberRoles, times(1)).iterator();				
		verify(keyElements, times(2)).iterator(); // the iterator will be consumed, install a new one:
		when(keyElements.iterator()).thenReturn(keyElementList.iterator());
				
		verify(roleToRemove, never()).setNextDbkeyPosition(any(Short.class));		
		verify(roleToRemove, never()).setPriorDbkeyPosition(any(Short.class));		
		verify(roleToRemove, times(1)).setOwnerDbkeyPosition(null);
		verify(roleToRemove, times(1)).setOwnerDbkeyPosition(any(Short.class));
		verify(roleToRemove, times(1)).setIndexDbkeyPosition(null);
		verify(roleToRemove, times(1)).setIndexDbkeyPosition(any(Short.class));
		
		verify(recordOwnerRoles, never()).remove(any(OwnerRole.class));
		verify(recordMemberRoles, times(1)).remove(roleToRemove);
		verify(recordMemberRoles, times(1)).remove(any(MemberRole.class));
		
		verify(impactedSet, never()).setOwner(any(OwnerRole.class));
		verify(setMemberRoles, times(1)).remove(roleToRemove);
		verify(setMemberRoles, times(1)).remove(any(MemberRole.class));		
		
		verify(diagramDataConnectionParts, times(1)).remove(connectionPart);
		verify(diagramDataConnectionParts, times(1)).remove(any(ConnectionPart.class));
		verify(diagramDataLocations, times(1)).remove(sourceEndpointLocation);
		verify(diagramDataLocations, times(1)).remove(targetEndpointLocation);
		
		verify(diagramDataConnectionLabels, times(1)).remove(connectionLabel);
		verify(diagramDataConnectionLabels, times(1)).remove(any(ConnectionLabel.class));
		verify(diagramDataLocations, times(1)).remove(connectionLabelLocation);
		
		verify(diagramDataLocations, times(3)).remove(any(DiagramLocation.class));
		
		verify(firstKeyElement, times(1)).setElement(null);
		verify(firstKeyElement, times(1)).setElement(any(Element.class));
		
		verify(secondKeyElement, times(1)).setElement(null);
		verify(secondKeyElement, times(1)).setElement(any(Element.class));
		
		verify(roleToRemove, times(1)).setSortKey(null);
		verify(roleToRemove, times(1)).setSortKey(any(Key.class));
		
		verify(recordSortKeys, times(1)).remove(sortKey);
		verify(recordSortKeys, times(1)).remove(any(Key.class));
		
				
		try {
			removableMemberRole.remove();
			fail("should throw an UnsupportedOperationException because already removed");
		} catch (UnsupportedOperationException e) {
			assertEquals("already removed", e.getMessage());
		}
		
		verify(recordOwnerRoles, times(1)).iterator();			
		verify(recordMemberRoles, times(1)).iterator();				
		verify(keyElements, times(2)).iterator(); // the iterator will be consumed, install a new one:
		when(keyElements.iterator()).thenReturn(keyElementList.iterator());				
		
		
		removableMemberRole.restore();
		
		verify(recordOwnerRoles, times(1)).iterator();			
		verify(recordMemberRoles, times(1)).iterator();				
		verify(keyElements, times(3)).iterator(); // the iterator will be consumed, install a new one:
		when(keyElements.iterator()).thenReturn(keyElementList.iterator());	
		
		verify(roleToRemove, never()).setNextDbkeyPosition(any(Short.class));		
		verify(roleToRemove, never()).setPriorDbkeyPosition(any(Short.class));		
		verify(roleToRemove, times(1)).setOwnerDbkeyPosition(null);
		verify(roleToRemove, times(1)).setOwnerDbkeyPosition(Short.valueOf((short) 1));
		verify(roleToRemove, times(2)).setOwnerDbkeyPosition(any(Short.class));
		verify(roleToRemove, times(1)).setIndexDbkeyPosition(null);
		verify(roleToRemove, times(1)).setIndexDbkeyPosition(Short.valueOf((short) 2));
		verify(roleToRemove, times(2)).setIndexDbkeyPosition(any(Short.class));		
		
		verify(recordMemberRoles, times(1)).add(0, roleToRemove);
		verify(recordMemberRoles, times(1)).add(any(int.class), any(MemberRole.class));
		
		verify(setMemberRoles, times(1)).add(0, roleToRemove);
		verify(setMemberRoles, times(1)).add(any(int.class), any(MemberRole.class));
		
		verify(diagramDataConnectionParts, times(1)).add(0, connectionPart);
		verify(diagramDataConnectionParts, times(1)).add(any(int.class), any(ConnectionPart.class));
		verify(diagramDataLocations, times(1)).add(1, sourceEndpointLocation);		
		verify(diagramDataLocations, times(1)).add(2, targetEndpointLocation);
		
		verify(diagramDataConnectionLabels, times(1)).add(0, connectionLabel);
		verify(diagramDataConnectionLabels, times(1)).add(any(int.class), any(ConnectionLabel.class));
		
		verify(diagramDataLocations, times(1)).add(0, connectionLabelLocation);
		
		verify(diagramDataLocations, times(3)).add(any(int.class), any(DiagramLocation.class));		
		
		verify(firstElementsKeyElements, times(1)).add(13, firstKeyElement);
		verify(firstElementsKeyElements, times(1)).add(any(int.class), any(KeyElement.class));
		
		verify(secondElementsKeyElements, times(1)).add(17, secondKeyElement);
		verify(secondElementsKeyElements, times(1)).add(any(int.class), any(KeyElement.class));
		
		verify(roleToRemove, times(1)).setSortKey(null);
		verify(roleToRemove, times(1)).setSortKey(sortKey);	
		verify(roleToRemove, times(2)).setSortKey(any(Key.class));
		
		verify(recordSortKeys, times(1)).add(5, sortKey);
		verify(recordSortKeys, times(1)).add(any(int.class), any(Key.class));
		
		try {
			removableMemberRole.restore();
			fail("should throw an UnsupportedOperationException because not removed");
		} catch (UnsupportedOperationException e) {
			assertEquals("not removed", e.getMessage());
		}
		
		
		removableMemberRole.remove();
		
		
		removableMemberRole.restore();		
		
	}
	
	@Test
	public void testRestoreDiagramLocation_SequenceCheckAfterRestore() {		
		
		testRemovableMemberRoleForDiagramLocationTesting(0, 1, 2);
		testRemovableMemberRoleForDiagramLocationTesting(2, 0, 1);
		testRemovableMemberRoleForDiagramLocationTesting(1, 2, 0);
		testRemovableMemberRoleForDiagramLocationTesting(0, 2, 1);
		testRemovableMemberRoleForDiagramLocationTesting(1, 0, 2);
		testRemovableMemberRoleForDiagramLocationTesting(2, 1, 0);
	
		testRemovableMemberRoleForDiagramLocationTesting(0, -1, 1);
		testRemovableMemberRoleForDiagramLocationTesting(1, -1, 0);
		
		testRemovableMemberRoleForDiagramLocationTesting(0, 1, -1);
		testRemovableMemberRoleForDiagramLocationTesting(1, 0, -1);
		
		testRemovableMemberRoleForDiagramLocationTesting(0, -1, -1);
		
	}
	
}
