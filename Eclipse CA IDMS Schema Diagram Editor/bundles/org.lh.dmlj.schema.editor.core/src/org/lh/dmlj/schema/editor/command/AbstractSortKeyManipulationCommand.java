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
package org.lh.dmlj.schema.editor.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMode;

public abstract class AbstractSortKeyManipulationCommand extends ModelChangeBasicCommand {
	protected Set set;
	private final StashedDataSlot[] stashedDataSlots = new StashedDataSlot[2];	
	protected ISortKeyDescription[] sortKeyDescriptions; // null if newOrder != SetOrder.SORTED
	
	protected AbstractSortKeyManipulationCommand(Set set) {
		super();
		this.set = set;
		// for sorted sets, the sortKeyDescriptions should be set in time
	}	
	
	protected AbstractSortKeyManipulationCommand(Set set, ISortKeyDescription[] sortKeyDescriptions) {
		super();
		this.set = set; // might be null at this point
		this.sortKeyDescriptions = sortKeyDescriptions;
		if (set != null && sortKeyDescriptions != null) {
			Assert.isTrue(set.getMembers().size() == sortKeyDescriptions.length, 
					"the number of sort key descriptions does NOT match the number of set members: " + set.getName() + 
					" " + Arrays.asList(sortKeyDescriptions));
		}
	}
	
	protected final void prepareSortKey(SchemaRecord schemaRecord, int memberRoleIndex, int slotIndex) {
		// initialize the data slot if needed
		if (stashedDataSlots[slotIndex] == null) {
			stashedDataSlots[slotIndex] = new StashedDataSlot();
		}
		
		var slot = stashedDataSlots[slotIndex];
		while (memberRoleIndex > slot.stashedSortKeys.size()) {
			// provide a dummy stashed sort key so that memberRoleIndex always points to the right stashed sort key
			slot.stashedSortKeys.add(new StashedSortKey());
		}
		
		Assert.isTrue(sortKeyDescriptions[memberRoleIndex].getElementNames().length == sortKeyDescriptions[memberRoleIndex].getSortSequences().length,
				"the number of element names must match the number of sort sequences");		
		
		var preparedSortKey = new StashedSortKey();
			
		preparedSortKey.sortKey = SchemaFactory.eINSTANCE.createKey();
		preparedSortKey.sortKeyIndex = schemaRecord.getKeys().size();
		preparedSortKey.sortKeyElementIndexes = new int[sortKeyDescriptions[memberRoleIndex].getElementNames().length];
		var processed = new ArrayList<String>();
		for (var i = 0; i < sortKeyDescriptions[memberRoleIndex].getElementNames().length; i++) {
			// get the sort element name; an element can be specified only once
			var elementName = sortKeyDescriptions[memberRoleIndex].getElementNames()[i];
			Assert.isTrue(processed.indexOf(elementName) < 0, "already a sort element in record " + schemaRecord.getName() + ": " + elementName);
			if (!elementName.equals(ISortKeyDescription.DBKEY_ELEMENT)) {
				// get the element from the record, make sure it exists
				var element = schemaRecord.getElement(elementName);
				Assert.isNotNull(element, "element not found in record " + schemaRecord.getName() + ": " + elementName);
				// create the sort key without creating the reference to each element
				preparedSortKey.sortKeyElements.add(element);
				preparedSortKey.sortKeyElementIndexes[i] = element.getKeyElements().size();				
			} else {
				// the sortkey denotes the dbkey
				preparedSortKey.sortedByDbkey = true;
			}
			var keyElement = SchemaFactory.eINSTANCE.createKeyElement();
			keyElement.setSortSequence(sortKeyDescriptions[memberRoleIndex].getSortSequences()[i]);
			preparedSortKey.sortKey.getElements().add(keyElement);
			preparedSortKey.sortKeyKeyElements.add(keyElement);
			// remember the element name
			processed.add(elementName);
		}
		Assert.isTrue(set.getMode() == SetMode.INDEXED || !preparedSortKey.sortedByDbkey,
				"only indexed sets can be sorted on dbkey: " + set.getName());
		Assert.isTrue(set.getMode() == SetMode.INDEXED || sortKeyDescriptions[memberRoleIndex].getDuplicatesOption() != DuplicatesOption.BY_DBKEY, 
				"duplicates by dbkey is only allowed for INDEXED sets: " + set.getName());
		Assert.isTrue(!preparedSortKey.sortedByDbkey || sortKeyDescriptions[memberRoleIndex].getDuplicatesOption() != DuplicatesOption.BY_DBKEY,
				"duplicates by dbkey is not allowed for sets sorted on dbkey: " + set.getName());
		preparedSortKey.sortKey.setDuplicatesOption(sortKeyDescriptions[memberRoleIndex].getDuplicatesOption());
		preparedSortKey.sortKey.setNaturalSequence(sortKeyDescriptions[memberRoleIndex].isNaturalSequence());
		if (set.getMode()== SetMode.INDEXED) {
			preparedSortKey.sortKey.setCompressed(sortKeyDescriptions[memberRoleIndex].isCompressed());
		} else {
			preparedSortKey.sortKey.setCompressed(false);
		}
		
		slot.stashedSortKeys.add(preparedSortKey);
	}	
	
	protected final void prepareSortKeys(int slotIndex) {
		Assert.isTrue(stashedDataSlots[slotIndex] == null, "stashed data slot already in use: " + slotIndex);
		for (var i = 0; i < set.getMembers().size(); i++) {
			var memberRole = set.getMembers().get(i);
			prepareSortKey(memberRole.getRecord(), i, slotIndex);
		}
	}	
	
	protected final void removeSortKey(MemberRole memberRole) {
		var sortKey = memberRole.getSortKey();
		for (var keyElement : memberRole.getSortKey().getElements()) {
			keyElement.setElement(null);
		}
		memberRole.setSortKey(null);
		memberRole.getRecord().getKeys().remove(sortKey);
	}

	protected final void removeSortKeys() {
		for (var i = 0; i < set.getMembers().size(); i++) {
			var memberRole = set.getMembers().get(i);
			removeSortKey(memberRole);
		}
	}
	
	protected final void restoreSortKey(int memberRoleIndex, int slotIndex) {
		var slot = stashedDataSlots[slotIndex];
		var memberRole = set.getMembers().get(memberRoleIndex);
		var stashedSortKey = slot.stashedSortKeys.get(memberRoleIndex);
		
		if (!stashedSortKey.sortedByDbkey) {
			for (var i = 0; i < stashedSortKey.sortKeyElements.size(); i++) {
				var element = stashedSortKey.sortKeyElements.get(i);
				var keyElement = stashedSortKey.sortKeyKeyElements.get(i);
				element.getKeyElements().add(stashedSortKey.sortKeyElementIndexes[i], keyElement);
			}
		}
		
		memberRole.setSortKey(stashedSortKey.sortKey);
		memberRole.getRecord().getKeys().add(stashedSortKey.sortKeyIndex, stashedSortKey.sortKey);
	}	
	
	protected final void restoreSortKeys(int slotIndex) {
		Assert.isTrue(stashedDataSlots[slotIndex] != null, "stashed data slot empty: " + slotIndex);
		for (var i = 0; i < set.getMembers().size(); i++) {
			restoreSortKey(i, slotIndex);
		}
	}
	
	private final void stashSortKey(int index, StashedDataSlot slot) {
		var memberRole = set.getMembers().get(index);
		var stashedSortKey = new StashedSortKey();
		
		stashedSortKey.sortKey = memberRole.getSortKey();
		stashedSortKey.sortKeyIndex = memberRole.getRecord().getKeys().indexOf(stashedSortKey.sortKey);
		stashedSortKey.sortKeyElementIndexes = new int[stashedSortKey.sortKey.getElements().size()];
		var i = 0;
		for (var keyElement : stashedSortKey.sortKey.getElements()) {
			if (!keyElement.isDbkey()) {
				var element = keyElement.getElement();
				stashedSortKey.sortKeyElements.add(element);
				stashedSortKey.sortKeyElementIndexes[i++] = element.getKeyElements().indexOf(keyElement);
				stashedSortKey.sortKeyKeyElements.add(keyElement);
			} else {
				stashedSortKey.sortedByDbkey = true;
			}
		}
		
		slot.stashedSortKeys.add(stashedSortKey);
	}	
	
	protected final void stashSortKeys(int slotIndex) {
		Assert.isTrue(stashedDataSlots[slotIndex] == null, "stashed data slot already in use: " + slotIndex);
		stashedDataSlots[slotIndex] = new StashedDataSlot();
		for (var i = 0; i < set.getMembers().size(); i++) {
			stashSortKey(i, stashedDataSlots[slotIndex]);
		}
	}	
	
	private static class StashedDataSlot {
		private List<StashedSortKey> stashedSortKeys = new ArrayList<>(); // 1 per member record
	}
	
	private static class StashedSortKey {		
		private Key 	sortKey;
		private int	sortKeyIndex;
		private List<Element> sortKeyElements = new ArrayList<>();
		private int[] sortKeyElementIndexes;
		private List<KeyElement> sortKeyKeyElements = new ArrayList<>();
		private boolean sortedByDbkey = false;
	}	
	
}
