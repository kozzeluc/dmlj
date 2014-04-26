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

import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Role;

/**
 * Represents a pointer in a record's prefix.  The prefix position does not have to be set, i.e. it
 * can be zero (in the case of the owner next pointer type) or null (all other pointer types).
 */
public class Pointer<T extends Role> {
	
	protected T   		  role;
	protected PointerType type;
	
	protected Pointer() {
		throw new UnsupportedOperationException("disabled constructor");
	}
	
	protected Pointer(T role, PointerType type) {
		super();	
		this.role = role;
		this.type = type;		
	}
	
	protected MemberRole getMemberRole() {
		if (!isMemberDefined()) {
			throw new UnsupportedOperationException("not a member type pointer");
		}
		return (MemberRole) role;
	}
	
	protected OwnerRole getOwnerRole() {
		if (!isOwnerDefined()) {
			throw new UnsupportedOperationException("not an owner type pointer");
		}
		return (OwnerRole) role;
	}
	
	public Short getPositionInPrefix() {
		return PointerUtil.getPositionInPrefix(role, type);
	}
	
	public String getRecordName() {
		if (isOwnerDefined()) {			
			return getOwnerRole().getRecord().getName();
		} else {
			return getMemberRole().getRecord().getName();
		}
	}
	
	public T getRole() {
		return role;
	}
	
	public String getSetName() {
		if (isOwnerDefined()) {
			return getOwnerRole().getSet().getName();
		} else {
			return getMemberRole().getSet().getName();
		}
	}
	
	public PointerType getType() {
		return type;
	}
	
	public boolean isMemberDefined() {
		return role instanceof MemberRole;
	}

	public boolean isOwnerDefined() {
		return role instanceof OwnerRole;
	}	
	
}
