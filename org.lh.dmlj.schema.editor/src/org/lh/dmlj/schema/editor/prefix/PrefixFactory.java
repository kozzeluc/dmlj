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

import java.util.List;

import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Role;
import org.lh.dmlj.schema.SchemaRecord;

public class PrefixFactory {

	public static Prefix newPrefixForInquiry(SchemaRecord record) {		
		List<Pointer<?>> pointers = PrefixUtil.getPointersForRecord(record);
		if (!PrefixUtil.isPointerListConsistent(pointers)) {
			throw new IllegalArgumentException("record prefix invalid: " + record);
		}
		return new Prefix(record, pointers);		
	}

	public static PrefixForPointerAppendage newPrefixForPointerAppendage(Role role, 
																	 	    PointerType... pointersToAppend) {
		
		SchemaRecord record;
		if (role instanceof OwnerRole) {
			record = ((OwnerRole) role).getRecord();
		} else {
			record = ((MemberRole) role).getRecord();
		}
		List<Pointer<?>> pointers = PrefixUtil.getPointersForRecord(record);
		if (!PrefixUtil.isPointerListConsistent(pointers)) {
			throw new IllegalArgumentException("record prefix invalid: " + record);
		}
		short positionInPrefixToSet = (short) (pointers.size() + 1);
		for (PointerType pointerType : pointersToAppend) {
			PointerToSet<Role> pointerToAppend = 
				PointerFactory.newPointerToSet(role, pointerType, positionInPrefixToSet++);
			pointers.add(pointerToAppend);
		}		
		return new PrefixForPointerAppendage(record, pointers);
	}
	
	public static PrefixForPointerRemoval newPrefixForPointerRemoval(Role role, 
																   	 PointerType... pointersToRemove) {

		SchemaRecord record;
		if (role instanceof OwnerRole) {
			record = ((OwnerRole) role).getRecord();
		} else {
			record = ((MemberRole) role).getRecord();
		}
		List<Pointer<?>> pointers = PrefixUtil.getPointersForRecord(record);
		if (!PrefixUtil.isPointerListConsistent(pointers)) {
			throw new IllegalArgumentException("record prefix invalid: " + record);
		}
		short decrementForPointersToMove = (short) 0;
		for (int i = 0; i < pointers.size(); i++) {
			Pointer<?> originalPointer = pointers.get(i);
			if (originalPointer.getRole() == role) {
				for (PointerType pointerType : pointersToRemove) {
					if (originalPointer.getType() == pointerType) {
						PointerToUnset<?> replacementPointer = 
							PointerFactory.newPointerToUnset(role, pointerType);
						pointers.set(i, replacementPointer);
						decrementForPointersToMove += 1;
					}
				}
			} else if (decrementForPointersToMove > 0) {
				short newPositionInPrefixToSet = 
					(short) (originalPointer.getCurrentPositionInPrefix().shortValue() - 
							 decrementForPointersToMove);
				PointerToMove<?> replacementPointer = 
					PointerFactory.newPointerToMove(originalPointer.getRole(), 
													originalPointer.getType(), 
													newPositionInPrefixToSet);
				pointers.set(i, replacementPointer);
			}
		}		
		return new PrefixForPointerRemoval(record, pointers);
	}
	
	public static PrefixForPointerReordering newPrefixForPointerReordering(SchemaRecord record, 
	   																	   List<Pointer<?>> newPointerOrder) {

		List<Pointer<?>> pointers = PrefixUtil.getPointersForRecord(record);
		if (!PrefixUtil.isPointerListConsistent(pointers)) {
			throw new IllegalArgumentException("record prefix invalid: " + record);
		}
		
		if (newPointerOrder.size() != pointers.size()) {
			throw new IllegalArgumentException("newPointerOrder.size() mismatch: " +
											   newPointerOrder.size() + " (expected: " + 
											   pointers.size() + ")");
		}
		
		List<Pointer<?>> newPointerOrderSorted = PrefixUtil.asSortedList(newPointerOrder);
				
		for (int i = 0; i < pointers.size(); i++) {
			
			Pointer<?> originalPointer = pointers.get(i);
			Pointer<?> newOrderPointer = newPointerOrderSorted.get(i);
			
			if (originalPointer.getRole() != newOrderPointer.getRole()) {				
				String message = "newPointerOrder content mismatch (role): " + i;
				throw new IllegalArgumentException(message);
			}
			if (originalPointer.getType() != newOrderPointer.getType()) {				
				String message = "newPointerOrder content mismatch (type): " + i;
				throw new IllegalArgumentException(message);
			}
			if (originalPointer.getCurrentPositionInPrefix() != 
													 newOrderPointer.getCurrentPositionInPrefix()) {
				
				String message = 
					"newPointerOrder content mismatch (current position in prefix): " + i;
				throw new IllegalArgumentException(message);
			}
			
			short newPositionInPrefix = (short) (newPointerOrder.indexOf(newOrderPointer) + 1);
			
			if (newPositionInPrefix != originalPointer.getCurrentPositionInPrefix()) {
				PointerToMove<?> replacementPointer = 
					PointerFactory.newPointerToMove(originalPointer.getRole(), 
													originalPointer.getType(), 
													newPositionInPrefix);
				pointers.set(i, replacementPointer);
			}
			
		}
		
		return new PrefixForPointerReordering(record, pointers);
	}	
	
}
