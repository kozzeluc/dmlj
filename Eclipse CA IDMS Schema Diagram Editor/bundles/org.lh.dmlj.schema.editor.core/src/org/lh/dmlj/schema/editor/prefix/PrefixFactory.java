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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Role;
import org.lh.dmlj.schema.SchemaRecord;

public final class PrefixFactory {

	public static Prefix newPrefixForInquiry(SchemaRecord schemaRecord) {		
		var pointers = PrefixUtil.getPointersForRecord(schemaRecord);
		if (!PrefixUtil.isPointerListConsistent(pointers)) {
			throw new IllegalArgumentException("record prefix invalid: " + schemaRecord);
		}
		return new Prefix(schemaRecord, pointers);		
	}

	public static PrefixForPointerAppendage newPrefixForPointerAppendage(Role role, PointerType... pointersToAppend) {
		SchemaRecord schemaRecord;
		if (role instanceof OwnerRole ownerRole) {
			schemaRecord = ownerRole.getRecord();
		} else {
			schemaRecord = ((MemberRole) role).getRecord();
		}
		var pointers = new ArrayList<>(PrefixUtil.getPointersForRecord(schemaRecord));
		if (!PrefixUtil.isPointerListConsistent(pointers)) {
			throw new IllegalArgumentException("record prefix invalid: " + schemaRecord);
		}
		var positionInPrefixToSet = (short) (pointers.size() + 1);
		for (var pointerType : pointersToAppend) {
			var pointerToAppend = PointerFactory.newPointerToSet(role, pointerType, positionInPrefixToSet++);
			pointers.add(pointerToAppend);
		}		
		return new PrefixForPointerAppendage(schemaRecord, pointers);
	}
	
	public static PrefixForPointerRemoval newPrefixForPointerRemoval(Role role, PointerType... pointersToRemove) {
		SchemaRecord schemaRecord;
		if (role instanceof OwnerRole ownerRole) {
			schemaRecord = ownerRole.getRecord();
		} else {
			schemaRecord = ((MemberRole) role).getRecord();
		}
		var pointers = new ArrayList<>(PrefixUtil.getPointersForRecord(schemaRecord));
		if (!PrefixUtil.isPointerListConsistent(pointers)) {
			throw new IllegalArgumentException("record prefix invalid: " + schemaRecord);
		}
		var decrementForPointersToMove = (short) 0;
		for (var i = 0; i < pointers.size(); i++) {
			var originalPointer = pointers.get(i);
			if (originalPointer.getRole() == role) {
				for (var pointerType : pointersToRemove) {
					if (originalPointer.getType() == pointerType) {
						var replacementPointer = PointerFactory.newPointerToUnset(role, pointerType);
						pointers.set(i, replacementPointer);
						decrementForPointersToMove += 1;
					}
				}
			} else if (decrementForPointersToMove > 0) {
				var newPositionInPrefixToSet = (short) (originalPointer.getCurrentPositionInPrefix().shortValue() - decrementForPointersToMove);
				var replacementPointer = PointerFactory.newPointerToMove(originalPointer.getRole(), originalPointer.getType(),
						newPositionInPrefixToSet);
				pointers.set(i, replacementPointer);
			}
		}		
		return new PrefixForPointerRemoval(schemaRecord, pointers);
	}
	
	public static PrefixForPointerReordering newPrefixForPointerReordering(SchemaRecord schemaRecord, List<Pointer> newPointerOrder) {
		var pointers = new ArrayList<>(PrefixUtil.getPointersForRecord(schemaRecord));
		if (!PrefixUtil.isPointerListConsistent(pointers)) {
			throw new IllegalArgumentException("record prefix invalid: " + schemaRecord);
		}
		if (newPointerOrder.size() != pointers.size()) {
			throw new IllegalArgumentException("newPointerOrder.size() mismatch: " + newPointerOrder.size() + 
					" (expected: " + pointers.size() + ")");
		}
		var newPointerOrderSorted = PrefixUtil.asSortedList(newPointerOrder);
		for (var i = 0; i < pointers.size(); i++) {
			var originalPointer = pointers.get(i);
			var newOrderPointer = newPointerOrderSorted.get(i);
			if (originalPointer.getRole() != newOrderPointer.getRole()) {				
				throw new IllegalArgumentException("newPointerOrder content mismatch (role): " + i);
			}
			if (originalPointer.getType() != newOrderPointer.getType()) {				
				throw new IllegalArgumentException("newPointerOrder content mismatch (type): " + i);
			}
			if (!Objects.equals(originalPointer.getCurrentPositionInPrefix(), newOrderPointer.getCurrentPositionInPrefix())) {
				throw new IllegalArgumentException("newPointerOrder content mismatch (current position in prefix): " + i);
			}
			var newPositionInPrefix = (short) (newPointerOrder.indexOf(newOrderPointer) + 1);
			if (newPositionInPrefix != originalPointer.getCurrentPositionInPrefix()) {
				var replacementPointer = PointerFactory.newPointerToMove(originalPointer.getRole(), originalPointer.getType(), newPositionInPrefix);
				pointers.set(i, replacementPointer);
			}
		}
		return new PrefixForPointerReordering(schemaRecord, pointers);
	}
	
	private PrefixFactory() {
	}
	
}
