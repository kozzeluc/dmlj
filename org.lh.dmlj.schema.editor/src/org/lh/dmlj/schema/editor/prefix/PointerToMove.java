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

/**
 * Represents a pointer in a record's prefix whose position in the prefix is to be changed to
 * another position.
 */
public class PointerToMove<T extends Role> extends Pointer<T> {

	private Short newPositionInPrefix;	
	private Short oldPositionInPrefix;
	
	protected PointerToMove(T role, PointerType type, short newPositionInPrefix) {
		super(role, type);
		this.newPositionInPrefix = Short.valueOf(newPositionInPrefix);
		oldPositionInPrefix = PointerUtil.getPositionInPrefix(role, type);
	}
	
	public short getNewPositionInPrefix() {
		return newPositionInPrefix.shortValue();
	}
	
	public Short getOldPositionInPrefix() {
		return oldPositionInPrefix;
	}	
	
	public void move() {
		PointerUtil.setPositionInPrefix(role, type, newPositionInPrefix);
	}
	
	public void moveBack() {
		PointerUtil.setPositionInPrefix(role, type, oldPositionInPrefix);
	}
	
}
