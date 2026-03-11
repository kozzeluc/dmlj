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

import static java.util.Comparator.comparing;
import static org.lh.dmlj.schema.editor.prefix.PointerType.MEMBER_INDEX;
import static org.lh.dmlj.schema.editor.prefix.PointerType.MEMBER_NEXT;
import static org.lh.dmlj.schema.editor.prefix.PointerType.MEMBER_OWNER;
import static org.lh.dmlj.schema.editor.prefix.PointerType.MEMBER_PRIOR;
import static org.lh.dmlj.schema.editor.prefix.PointerType.OWNER_NEXT;
import static org.lh.dmlj.schema.editor.prefix.PointerType.OWNER_PRIOR;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Role;
import org.lh.dmlj.schema.SchemaRecord;

public final class PrefixUtil {

	static List<Pointer> asSortedList(List<Pointer> unsortedPointerList) {
		return unsortedPointerList.stream()
				.sorted(comparing(Pointer::getCurrentPositionInPrefix))
				.toList();
	}

	public static PointerType[] getDefinedPointerTypes(MemberRole role) {
		var pointersToRemoveInMember = new ArrayList<PointerType>();		
		if (role.getNextDbkeyPosition() != null) {
			pointersToRemoveInMember.add(PointerType.MEMBER_NEXT);
		}
		if (role.getPriorDbkeyPosition() != null) {
			pointersToRemoveInMember.add(PointerType.MEMBER_PRIOR);
		}
		if (role.getOwnerDbkeyPosition() != null) {
			pointersToRemoveInMember.add(PointerType.MEMBER_OWNER);
		}
		if (role.getIndexDbkeyPosition() != null) {
			pointersToRemoveInMember.add(PointerType.MEMBER_INDEX);
		}
		return pointersToRemoveInMember.toArray(new PointerType[] {});
	}
	
	public static PointerType[] getDefinedPointerTypes(OwnerRole role) {
		var pointersToRemoveInOwner = new ArrayList<PointerType>();		
		if (role.getNextDbkeyPosition() != 0) {
			pointersToRemoveInOwner.add(PointerType.OWNER_NEXT);
		}
		if (role.getPriorDbkeyPosition() != null) {
			pointersToRemoveInOwner.add(PointerType.OWNER_PRIOR);
		}
		return pointersToRemoveInOwner.toArray(new PointerType[] {});
	}	
	
	static Pointer getPointer(List<Pointer> pointers, PointerDescription desired) {
		return pointers.stream()
				.filter(pointer -> pointer.getSetName().equals(desired.getSetName()))
				.filter(pointer -> pointer.getType() == desired.getPointerType())
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("not found: " + desired.toString()));
	}
	
	public static List<PointerDescription> getPointerDescriptions(SchemaRecord schemaRecord) {
		return PrefixFactory.newPrefixForInquiry(schemaRecord).getPointers().stream()
				.map(pointer -> new PointerDescription(pointer.getSetName(), pointer.getType()))
				.toList();
	}
	
	static List<Pointer> getPointersForRecord(SchemaRecord schemaRecord) {
		var pointers = new ArrayList<Pointer>();
		for (var ownerRole : schemaRecord.getOwnerRoles()) {
			var ownerNext = PointerFactory.newPointer(ownerRole, OWNER_NEXT);
			if (ownerNext.getCurrentPositionInPrefix() != null) {
				// this should always be the case but we do not enforce it
				pointers.add(ownerNext);
			}
			var ownerPrior = PointerFactory.newPointer(ownerRole, OWNER_PRIOR);
			if (ownerPrior.getCurrentPositionInPrefix() != null) {
				pointers.add(ownerPrior);
			}
		}
		for (var memberRole : schemaRecord.getMemberRoles()) {
			var memberNext = PointerFactory.newPointer(memberRole, MEMBER_NEXT);
			if (memberNext.getCurrentPositionInPrefix() != null) {
				pointers.add(memberNext);
			}	
			var memberPrior = PointerFactory.newPointer(memberRole, MEMBER_PRIOR);
			if (memberPrior.getCurrentPositionInPrefix() != null) {
				pointers.add(memberPrior);
			}
			var memberOwner = PointerFactory.newPointer(memberRole, MEMBER_OWNER);
			if (memberOwner.getCurrentPositionInPrefix() != null) {
				pointers.add(memberOwner);
			}
			var memberIndex = PointerFactory.newPointer(memberRole, MEMBER_INDEX);
			if (memberIndex.getCurrentPositionInPrefix() != null) {
				pointers.add(memberIndex);
			}
		}
		return asSortedList(pointers);
	}
	
	static <T extends Role> Short getPositionInPrefix(T role, PointerType type) {
		if (!isPointerTypeValid(role, type)) {
			throw new IllegalArgumentException("no pointer of type " + type + " for " + role);
		}
		if (role instanceof OwnerRole ownerRole) {
			if (type == OWNER_NEXT) {
				var nextDbkeyPosition = ownerRole.getNextDbkeyPosition();
				return nextDbkeyPosition != 0 ? Short.valueOf(nextDbkeyPosition) : null;
			} else {
				return ownerRole.getPriorDbkeyPosition();
			}
		} else {
			var memberRole = (MemberRole) role;
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
	
	static boolean isPointerListConsistent(List<Pointer> pointers) {
		return IntStream.range(0, pointers.size())
				.allMatch(i -> pointers.get(i).getCurrentPositionInPrefix() == i + 1);
	}
	
	static <T extends Role> boolean isPointerTypeValid(T role, PointerType type) {
		return role instanceof OwnerRole && (type == OWNER_NEXT || type == OWNER_PRIOR) || 
			   role instanceof MemberRole && (type == MEMBER_NEXT || type == MEMBER_PRIOR || type == MEMBER_OWNER  || type == MEMBER_INDEX);
	}
	
	static boolean isPositionInPrefixValid(short positionInPrefix) {
		return positionInPrefix > 0 && positionInPrefix < 8181;
	}
	
	public static List<Pointer> reorder(List<Pointer> pointers, List<PointerDescription> desiredOrder) {
		return desiredOrder.stream()
				.map(pointerDescription -> getPointer(pointers, pointerDescription))
				.toList();
	}
	
	static <T extends Role> void setPositionInPrefix(T role, PointerType type, Short newPositionInPrefix) {
		if (!isPointerTypeValid(role, type)) {
			throw new IllegalArgumentException("no pointer of type " + type + " for " + role);
		}		
		if (role instanceof OwnerRole ownerRole) {
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
			var memberRole = (MemberRole) role;
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
	
	private PrefixUtil() {
	}
	
}
