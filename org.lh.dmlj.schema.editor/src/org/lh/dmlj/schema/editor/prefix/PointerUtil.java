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

import static org.lh.dmlj.schema.editor.prefix.PointerType.MEMBER_INDEX;
import static org.lh.dmlj.schema.editor.prefix.PointerType.MEMBER_NEXT;
import static org.lh.dmlj.schema.editor.prefix.PointerType.MEMBER_OWNER;
import static org.lh.dmlj.schema.editor.prefix.PointerType.MEMBER_PRIOR;
import static org.lh.dmlj.schema.editor.prefix.PointerType.OWNER_NEXT;
import static org.lh.dmlj.schema.editor.prefix.PointerType.OWNER_PRIOR;

import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Role;

public class PointerUtil {

	public static <T extends Role> Short getPositionInPrefix(T role, PointerType type) {
		
		if (!isPointerTypeValid(role, type)) {
			throw new IllegalArgumentException("no pointer of type " + type + " for " + role);
		}
		
		if (role instanceof OwnerRole) {
			OwnerRole ownerRole = (OwnerRole) role;
			if (type == OWNER_NEXT) {
				short nextDbkeyPosition = ownerRole.getNextDbkeyPosition();
				return nextDbkeyPosition != 0 ? Short.valueOf(nextDbkeyPosition) : null;
			} else {
				return ownerRole.getPriorDbkeyPosition();
			}
		} else {
			MemberRole memberRole = (MemberRole) role;
			if (type == MEMBER_NEXT) {
				return memberRole.getNextDbkeyPosition();
			} else if (type == MEMBER_PRIOR) {
				return memberRole.getPriorDbkeyPosition();
			} else if (type == MEMBER_OWNER) {
				return memberRole.getOwnerDbkeyPosition();
			} else {
				return memberRole.getIndexDbkeyPosition();
			}
		}
		
	}	
	
	public static <T extends Role> boolean isPointerTypeValid(T role, PointerType type) {
		return role instanceof OwnerRole && (type == OWNER_NEXT || type == OWNER_PRIOR) ||
			   role instanceof MemberRole && (type == MEMBER_NEXT || type == MEMBER_PRIOR  || 
										      type == MEMBER_OWNER  || type == MEMBER_INDEX);		
	}
	
	public static boolean isPositionInPrefixValid(short positionInPrefix) {
		return positionInPrefix > 0 && positionInPrefix < 8181;
	}
	
	public static <T extends Role> void setPositionInPrefix(T role, PointerType type,
															Short newPositionInPrefix) {
	
		if (!isPointerTypeValid(role, type)) {
			throw new IllegalArgumentException("no pointer of type " + type + " for " + role);
		}	
		
		if (role instanceof OwnerRole) {
			OwnerRole ownerRole = (OwnerRole) role;
			if (type == OWNER_NEXT) {				
				if (newPositionInPrefix != null) {
					ownerRole.setNextDbkeyPosition(newPositionInPrefix.shortValue());
				} else {
					ownerRole.setNextDbkeyPosition((short) 0);
				}
			} else {
				ownerRole.setPriorDbkeyPosition(newPositionInPrefix);
			}
		} else {
			MemberRole memberRole = (MemberRole) role;
			if (type == MEMBER_NEXT) {
				memberRole.setNextDbkeyPosition(newPositionInPrefix);
			} else if (type == MEMBER_PRIOR) {
				memberRole.setPriorDbkeyPosition(newPositionInPrefix);
			} else if (type == MEMBER_OWNER) {
				memberRole.setOwnerDbkeyPosition(newPositionInPrefix);
			} else {
				memberRole.setIndexDbkeyPosition(newPositionInPrefix);
			}
		}		
		
	}
	
}
