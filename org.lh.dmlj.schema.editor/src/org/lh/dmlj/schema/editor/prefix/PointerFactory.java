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

import org.lh.dmlj.schema.Role;

public class PointerFactory {	
	
	public static <T extends Role> Pointer<T> newPointer(T role, PointerType type) {
		if (PointerUtil.isPointerTypeValid(role, type)) {
			return new Pointer<T>(role, type);
		} else {
			throw new IllegalArgumentException("no pointer of type " + type + " for " + role);
		}		
	}
	
	public static <T extends Role> PointerToMove<T> newPointerToMove(T role, PointerType type,
		  	   														short newPositionInPrefix) {
		
		Short positionInPrefix = PointerUtil.getPositionInPrefix(role, type);
		if (positionInPrefix == null) {
			throw new IllegalArgumentException("not set: " + type);			
		}
		if (!PointerUtil.isPositionInPrefixValid(newPositionInPrefix)) {
			String message = 
				"positionInPrefixToSet must be a whole integer in the range 1 through 8180: " +
				newPositionInPrefix;
			throw new IllegalArgumentException(message);
		}
		return new PointerToMove<T>(role, type, newPositionInPrefix);
	}	
	
	public static <T extends Role> PointerToSet<T> newPointerToSet(T role, PointerType type,
															  	   short positionInPrefixToSet) {
		
		Short positionInPrefix = PointerUtil.getPositionInPrefix(role, type);
		if (positionInPrefix != null) {
			throw new IllegalArgumentException("already set: " + type);			
		}
		if (!PointerUtil.isPositionInPrefixValid(positionInPrefixToSet)) {
			String message = 
				"positionInPrefixToSet must be a whole integer in the range 1 through 8180: " +
				positionInPrefixToSet;
			throw new IllegalArgumentException(message);
		}
		return new PointerToSet<T>(role, type, positionInPrefixToSet);
	}
	
	public static <T extends Role> PointerToUnset<T> newPointerToUnset(T role, PointerType type) {		
		Short positionInPrefix = PointerUtil.getPositionInPrefix(role, type);
		if (positionInPrefix == null) {
			throw new IllegalArgumentException("not set: " + type);			
		}		
		return new PointerToUnset<T>(role, type);
	}	
	
}
