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

import org.lh.dmlj.schema.Role;

final class PointerFactory {	
	
	static <T extends Role> Pointer newPointer(T role, PointerType type) {
		if (PrefixUtil.isPointerTypeValid(role, type)) {
			return new Pointer(role, type);
		} else {
			throw new IllegalArgumentException("no pointer of type " + type + " for " + role);
		}		
	}
	
	static PointerToMove newPointerToMove(Role role, PointerType type, short newPositionInPrefix) {
		var positionInPrefix = PrefixUtil.getPositionInPrefix(role, type);
		if (positionInPrefix == null) {
			throw new IllegalArgumentException("not set: " + type);			
		}
		if (!PrefixUtil.isPositionInPrefixValid(newPositionInPrefix)) {
			var message = "positionInPrefixToSet must be a whole integer in the range 1 through 8180: " + newPositionInPrefix;
			throw new IllegalArgumentException(message);
		}
		return new PointerToMove(role, type, newPositionInPrefix);
	}	
	
	static PointerToSet newPointerToSet(Role role, PointerType type, short positionInPrefixToSet) {
		var positionInPrefix = PrefixUtil.getPositionInPrefix(role, type);
		if (positionInPrefix != null) {
			throw new IllegalArgumentException("already set: " + type);			
		}
		if (!PrefixUtil.isPositionInPrefixValid(positionInPrefixToSet)) {
			var message = "positionInPrefixToSet must be a whole integer in the range 1 through 8180: " + positionInPrefixToSet;
			throw new IllegalArgumentException(message);
		}
		return new PointerToSet(role, type, positionInPrefixToSet);
	}
	
	static PointerToUnset newPointerToUnset(Role role, PointerType type) {		
		var positionInPrefix = PrefixUtil.getPositionInPrefix(role, type);
		if (positionInPrefix == null) {
			throw new IllegalArgumentException("not set: " + type);			
		}		
		return new PointerToUnset(role, type);
	}
	
	private PointerFactory() {
	}
	
}
