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

import java.util.ArrayList;
import java.util.List;

import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.editor.prefix.PointerType;
import org.lh.dmlj.schema.editor.prefix.PrefixFactory;
import org.lh.dmlj.schema.editor.prefix.PrefixForPointerRemoval;
import org.lh.dmlj.schema.editor.prefix.PrefixUtil;

public class RemovableMemberRole extends AbstractRemovableRole<MemberRole> {
	
	private Schema schema;
	
	private SchemaRecord record;
	private int indexOfRoleInRecordsMemberRoles;
	private PrefixForPointerRemoval prefix;
		
	private Set	set;
	private int indexOfRoleInSetsMembers;
	
	private Key sortKey;
	private int	indexOfSortKeyInRecordsKeys;
	private List<KeyElementIndex> indexesOfKeyElementsInElementsKeyElements = new ArrayList<>();
	
	private int indexOfConnectionPartInDiagramDatasConnectionParts;
	private Integer	indexOfSourceEndpointLocationInDiagramDatasLocations;
	private Integer	indexOfTargetEndpointLocationInDiagramDatasLocations;
	
	private int indexOfConnectionLabelInDiagramDatasConnectionLabels;
	private int indexOfConnectionLabelLocationInDiagramDatasLocations;

	public RemovableMemberRole(MemberRole role) {
		super(role);
		rememberSchemaData();
		rememberRecordData();
		rememberSetData();
		rememberMembershipData();
	}

	private void rememberSchemaData() {
		schema = role.getRecord().getSchema();		
	}

	private void rememberRecordData() {
		record = role.getRecord();
		indexOfRoleInRecordsMemberRoles = record.getMemberRoles().indexOf(role);
		PointerType[] definedPointerTypes = PrefixUtil.getDefinedPointerTypes(role);
		prefix = PrefixFactory.newPrefixForPointerRemoval(role, definedPointerTypes);
	}
	
	private void rememberSetData() {
		set = role.getSet();
		indexOfRoleInSetsMembers = set.getMembers().indexOf(role);
	}
	
	private void rememberMembershipData() {
		if (set.getOrder() == SetOrder.SORTED) {
			rememberSortKeyData();
		}		
		rememberConnectionPartData();
		rememberConnectionLabelData();
	}
	
	private void rememberSortKeyData() {
		sortKey = role.getSortKey();
		indexOfSortKeyInRecordsKeys = record.getKeys().indexOf(sortKey);		
		for (KeyElement keyElement : sortKey.getElements()) {
			indexesOfKeyElementsInElementsKeyElements.add(new KeyElementIndex(keyElement));
		}
	}

	private void rememberConnectionPartData() {		
		ConnectionPart connectionPart = role.getConnectionParts().get(0);
		DiagramData diagramData = schema.getDiagramData();
		indexOfConnectionPartInDiagramDatasConnectionParts = 
			diagramData.getConnectionParts().indexOf(connectionPart);		
		DiagramLocation sourceEndpointLocation = connectionPart.getSourceEndpointLocation(); 
		if (sourceEndpointLocation != null) {
			indexOfSourceEndpointLocationInDiagramDatasLocations = 
				Integer.valueOf(diagramData.getLocations().indexOf(sourceEndpointLocation));
		}
		DiagramLocation targetEndpointLocation = connectionPart.getTargetEndpointLocation();
		if (targetEndpointLocation != null) {
			indexOfTargetEndpointLocationInDiagramDatasLocations = 
				Integer.valueOf(diagramData.getLocations().indexOf(targetEndpointLocation));
		} 
	}
	
	private void rememberConnectionLabelData() {		
		ConnectionLabel connectionLabel = role.getConnectionLabel();
		DiagramData diagramData = schema.getDiagramData();
		indexOfConnectionLabelInDiagramDatasConnectionLabels = 
			diagramData.getConnectionLabels().indexOf(role.getConnectionLabel());
		indexOfConnectionLabelLocationInDiagramDatasLocations = 
			diagramData.getLocations().indexOf(connectionLabel.getDiagramLocation());
	}
	
	protected void removeData() {			
		removeRecordData();
		removeSetData();
		removeMembershipData();		
	}
	
	private void removeRecordData() {
		prefix.removePointers();		
		record.getMemberRoles().remove(role);
	}
	
	private void removeSetData() {
		set.getMembers().remove(role);		
	}
	
	private void removeMembershipData() {
		if (set.getOrder() == SetOrder.SORTED) {
			removeSortKey();
		}
		removeConnectionPartData();
		removeConnectionLabelData();
	}	
	
	private void removeSortKey() {		
		for (KeyElement keyElement : role.getSortKey().getElements()) {
			keyElement.setElement(null);
		}		
		role.setSortKey(null);
		record.getKeys().remove(sortKey);
	}

	private void removeConnectionPartData() {
		ConnectionPart connectionPart = role.getConnectionParts().get(0);
		DiagramData diagramData = schema.getDiagramData();
		diagramData.getConnectionParts().remove(connectionPart);
		DiagramLocation sourceEndpointLocation = connectionPart.getSourceEndpointLocation(); 		
		if (sourceEndpointLocation != null) {
			diagramData.getLocations().remove(sourceEndpointLocation);
		}
		DiagramLocation targetEndpointLocation = connectionPart.getTargetEndpointLocation();
		if (targetEndpointLocation != null) {
			diagramData.getLocations().remove(targetEndpointLocation);
		}
	}
	
	private void removeConnectionLabelData() {
		ConnectionLabel connectionLabel = role.getConnectionLabel(); 
		DiagramData diagramData = schema.getDiagramData();
		diagramData.getConnectionLabels().remove(connectionLabel);
		diagramData.getLocations().remove(connectionLabel.getDiagramLocation());	
	}	
	
	protected void restoreData() {	
		restoreRecordData();
		restoreSetData();			
		restoreMembershipData();		
	}
	
	private void restoreRecordData(){
		prefix.reset();	
		record.getMemberRoles().add(indexOfRoleInRecordsMemberRoles, role);
	}	
	
	private void restoreSetData(){
		set.getMembers().add(indexOfRoleInSetsMembers, role);
	}
	
	private void restoreMembershipData(){
		if (set.getOrder() == SetOrder.SORTED) {
			restoreSortKey();
		}		
		restoreConnectionPartData();
		restoreConnectionLabelData();
	}	

	private void restoreSortKey() {						
		for (KeyElement keyElement : sortKey.getElements()) {			
			KeyElementIndex keyElementIndex = 
				KeyElementIndex.find(indexesOfKeyElementsInElementsKeyElements, keyElement);
			keyElementIndex.element.getKeyElements().add(keyElementIndex.value, keyElement);
		}		
		role.setSortKey(sortKey);
		role.getRecord().getKeys().add(indexOfSortKeyInRecordsKeys, sortKey);		
	}	

	private void restoreConnectionPartData() {	
		ConnectionPart connectionPart = role.getConnectionParts().get(0);
		DiagramData diagramData = schema.getDiagramData();
		diagramData.getConnectionParts().add(indexOfConnectionPartInDiagramDatasConnectionParts, 
											 connectionPart);		
		DiagramLocation sourceEndpointLocation = connectionPart.getSourceEndpointLocation(); 
		if (sourceEndpointLocation != null) {
			int i = indexOfSourceEndpointLocationInDiagramDatasLocations.intValue();
			diagramData.getLocations().add(i, sourceEndpointLocation);
		}
		DiagramLocation targetEndpointLocation = connectionPart.getTargetEndpointLocation();
		if (targetEndpointLocation != null) {
			int i = indexOfTargetEndpointLocationInDiagramDatasLocations.intValue(); 
			diagramData.getLocations().add(i, targetEndpointLocation);
		}
	}	
	
	private void restoreConnectionLabelData() {	
		ConnectionLabel connectionLabel = role.getConnectionLabel();
		DiagramData diagramData = schema.getDiagramData();
		diagramData.getConnectionLabels().add(indexOfConnectionLabelInDiagramDatasConnectionLabels, 
											  role.getConnectionLabel());
		diagramData.getLocations().add(indexOfConnectionLabelLocationInDiagramDatasLocations,
									   connectionLabel.getDiagramLocation());
	}
	
	private static class KeyElementIndex {
		
		private Element element;
		private KeyElement keyElement;
		private int value;
		
		private static KeyElementIndex find(List<KeyElementIndex> containingList,
											KeyElement searchItem) {
			
			for (KeyElementIndex keyElementIndex : containingList) {
				if (keyElementIndex.keyElement == searchItem) {
					return keyElementIndex;
				}
			}
			throw new IllegalArgumentException("not found: " + searchItem);
		}
		
		private KeyElementIndex(KeyElement keyElement) {
			super();			
			this.keyElement = keyElement;
			element = keyElement.getElement();
			value = element.getKeyElements().indexOf(keyElement);
		}
		
	}
	
}
