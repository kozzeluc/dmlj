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
package org.lh.dmlj.schema.editor.command.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.editor.prefix.PrefixFactory;
import org.lh.dmlj.schema.editor.prefix.PrefixForPointerRemoval;
import org.lh.dmlj.schema.editor.prefix.PrefixUtil;

public class RemovableMemberRole extends AbstractRemovableRole<MemberRole> {
	private Schema schema;
	
	private SchemaRecord schemaRecord;
	private int indexOfRoleInRecordsMemberRoles;
	private PrefixForPointerRemoval prefix;
		
	private Set	set;
	private int indexOfRoleInSetsMembers;
	
	private Key sortKey;
	private int	indexOfSortKeyInRecordsKeys;
	private List<KeyElementIndex> indexesOfKeyElementsInElementsKeyElements = new ArrayList<>();
	
	private int indexOfConnectionPartInDiagramDatasConnectionParts;
		
	private int indexOfConnectionLabelInDiagramDatasConnectionLabels;	
	
	private List<DiagramLocationIndex> indexesInDiagramDatasLocations = new ArrayList<>();
	
	private List<DiagramLocation> additionalObsoleteLocations;

	public RemovableMemberRole(MemberRole role) {
		this(role, new ArrayList<>());
	}
	
	public RemovableMemberRole(MemberRole role, List<DiagramLocation> additionalObsoleteLocations) {
		super(role);
		rememberSchemaData();
		rememberRecordData();
		rememberSetData();
		rememberMembershipData();
		rememberAdditionalObsoleteLocations(additionalObsoleteLocations);
		sortIndexesInDiagramDatasLocations();
	}	

	private void rememberSchemaData() {
		schema = role.getRecord().getSchema();		
	}

	private void rememberRecordData() {
		schemaRecord = role.getRecord();
		indexOfRoleInRecordsMemberRoles = schemaRecord.getMemberRoles().indexOf(role);
		var definedPointerTypes = PrefixUtil.getDefinedPointerTypes(role);
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
		rememberDiagramLocationData();
	}
	
	private void rememberSortKeyData() {
		sortKey = role.getSortKey();
		indexOfSortKeyInRecordsKeys = schemaRecord.getKeys().indexOf(sortKey);		
		for (var keyElement : sortKey.getElements()) {
			indexesOfKeyElementsInElementsKeyElements.add(new KeyElementIndex(keyElement));
		}
	}

	private void rememberConnectionPartData() {		
		var connectionPart = role.getConnectionParts().get(0);
		var diagramData = schema.getDiagramData();
		indexOfConnectionPartInDiagramDatasConnectionParts = diagramData.getConnectionParts().indexOf(connectionPart);
	}
	
	private void rememberConnectionLabelData() {		
		var connectionLabel = role.getConnectionLabel();
		var diagramData = schema.getDiagramData();
		indexOfConnectionLabelInDiagramDatasConnectionLabels = diagramData.getConnectionLabels().indexOf(connectionLabel);		
	}
	
	private void rememberDiagramLocationData() {
		var diagramData = schema.getDiagramData();
		
		var connectionPart = role.getConnectionParts().get(0);
		var sourceEndpointLocation = connectionPart.getSourceEndpointLocation(); 
		if (sourceEndpointLocation != null) {
			var sourceEndpointLocationIndex = 
					new DiagramLocationIndex(sourceEndpointLocation, diagramData.getLocations().indexOf(sourceEndpointLocation));
			indexesInDiagramDatasLocations.add(sourceEndpointLocationIndex);
		}
		var targetEndpointLocation = connectionPart.getTargetEndpointLocation();
		if (targetEndpointLocation != null) {
			var targetEndpointLocationIndex = 
					new DiagramLocationIndex(targetEndpointLocation,diagramData.getLocations().indexOf(targetEndpointLocation));
			indexesInDiagramDatasLocations.add(targetEndpointLocationIndex);
		}
		
		var connectionLabel = role.getConnectionLabel();		
		var connectionLabelIndex = new DiagramLocationIndex(connectionLabel.getDiagramLocation(),
				diagramData.getLocations().indexOf(connectionLabel.getDiagramLocation()));
		indexesInDiagramDatasLocations.add(connectionLabelIndex);	
	}
	
	private void rememberAdditionalObsoleteLocations(List<DiagramLocation> additionalObsoleteLocations) {
		this.additionalObsoleteLocations = additionalObsoleteLocations;
		
		var diagramData = schema.getDiagramData();
		
		for (var obsoleteLocation : additionalObsoleteLocations) {
			var companyingLocationIndex = 
					new DiagramLocationIndex(obsoleteLocation, diagramData.getLocations().indexOf(obsoleteLocation));
			indexesInDiagramDatasLocations.add(companyingLocationIndex);
		}
		
	}
	
	private void sortIndexesInDiagramDatasLocations() {
		Collections.sort(indexesInDiagramDatasLocations);
	}
	
	protected void removeData() {			
		removeRecordData();
		removeSetData();
		removeMembershipData();	
		removeAdditionalObsoleteLocations();
	}

	private void removeRecordData() {
		prefix.removePointers();		
		schemaRecord.getMemberRoles().remove(role);
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
		for (var keyElement : role.getSortKey().getElements()) {
			keyElement.setElement(null);
		}		
		role.setSortKey(null);
		schemaRecord.getKeys().remove(sortKey);
	}

	private void removeConnectionPartData() {
		var connectionPart = role.getConnectionParts().get(0);
		var diagramData = schema.getDiagramData();
		diagramData.getConnectionParts().remove(connectionPart);
		var sourceEndpointLocation = connectionPart.getSourceEndpointLocation(); 		
		if (sourceEndpointLocation != null) {
			diagramData.getLocations().remove(sourceEndpointLocation);
		}
		var targetEndpointLocation = connectionPart.getTargetEndpointLocation();
		if (targetEndpointLocation != null) {
			diagramData.getLocations().remove(targetEndpointLocation);
		}
	}
	
	private void removeConnectionLabelData() {
		var connectionLabel = role.getConnectionLabel(); 
		var diagramData = schema.getDiagramData();
		diagramData.getConnectionLabels().remove(connectionLabel);
		diagramData.getLocations().remove(connectionLabel.getDiagramLocation());	
	}	
	
	private void removeAdditionalObsoleteLocations() {		
		var diagramData = schema.getDiagramData();
		for (var obsoleteLocation : additionalObsoleteLocations) {
			diagramData.getLocations().remove(obsoleteLocation);
		}
	}
	
	protected void restoreData() {	
		restoreRecordData();
		restoreSetData();			
		restoreMembershipDataAndAdditionalObsoleteLocations();		
	}
	
	private void restoreRecordData(){
		prefix.reset();	
		schemaRecord.getMemberRoles().add(indexOfRoleInRecordsMemberRoles, role);
	}	
	
	private void restoreSetData(){
		set.getMembers().add(indexOfRoleInSetsMembers, role);
	}
	
	private void restoreMembershipDataAndAdditionalObsoleteLocations(){
		if (set.getOrder() == SetOrder.SORTED) {
			restoreSortKey();
		}		
		restoreConnectionPartData();
		restoreConnectionLabelData();
		restoreDiagramLocationData();
	}	

	private void restoreSortKey() {						
		for (var keyElement : sortKey.getElements()) {			
			var keyElementIndex = KeyElementIndex.find(indexesOfKeyElementsInElementsKeyElements, keyElement);
			keyElementIndex.element.getKeyElements().add(keyElementIndex.value, keyElement);
		}		
		role.setSortKey(sortKey);
		role.getRecord().getKeys().add(indexOfSortKeyInRecordsKeys, sortKey);		
	}	

	private void restoreConnectionPartData() {	
		var connectionPart = role.getConnectionParts().get(0);
		var diagramData = schema.getDiagramData();
		diagramData.getConnectionParts().add(indexOfConnectionPartInDiagramDatasConnectionParts, connectionPart);		
	}	
	
	private void restoreConnectionLabelData() {	
		var connectionLabel = role.getConnectionLabel();
		var diagramData = schema.getDiagramData();
		diagramData.getConnectionLabels().add(indexOfConnectionLabelInDiagramDatasConnectionLabels, 
											  connectionLabel);		
	}
	
	private void restoreDiagramLocationData() {		
		var diagramData = schema.getDiagramData();
		for (var diagramLocationIndex :  indexesInDiagramDatasLocations) {
			diagramData.getLocations().add(diagramLocationIndex.value, diagramLocationIndex.diagramLocation);
		}
	}	
	
	private static class KeyElementIndex {
		private Element element;
		private KeyElement keyElement;
		private int value;
		
		private static KeyElementIndex find(List<KeyElementIndex> containingList, KeyElement searchItem) {
			for (var keyElementIndex : containingList) {
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
	
	static class DiagramLocationIndex implements Comparable<DiagramLocationIndex> {
		DiagramLocation diagramLocation;
		int value;
		
		DiagramLocationIndex(DiagramLocation diagramLocation, int value) {
			super();
			this.diagramLocation = diagramLocation;
			this.value = value;
		}

		@Override
		public int compareTo(DiagramLocationIndex other) {			
			return value - other.value;
		}
		
		@Override
		public boolean equals(Object obj) {
			return super.equals(obj);
		}
		
		@Override
		public int hashCode() {
			return super.hashCode();
		}
				
	}
	
}
