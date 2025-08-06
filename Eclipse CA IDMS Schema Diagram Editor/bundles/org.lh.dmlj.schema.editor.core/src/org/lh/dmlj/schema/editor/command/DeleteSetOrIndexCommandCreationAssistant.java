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
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.common.Tools;

/**
 * Deleting a set or index might go further than just deleting the set involved; this class centralizes command
 * creation for deleting sets and indexes.
 */
public class DeleteSetOrIndexCommandCreationAssistant {

	public static IModelChangeCommand getCommand(Set set) {
		return getCommand(set.getMembers());
	}
	
	public static IModelChangeCommand getCommand(MemberRole memberRole) {
		var memberRoles = new ArrayList<MemberRole>();
		memberRoles.add(memberRole);
		return getCommand(memberRoles);
	}
	
	public static IModelChangeCommand getCommand(List<MemberRole> memberRoles) {
		var commands = new ArrayList<IModelChangeCommand>();								
		
		for (var i = 0; i < memberRoles.size(); i++) {
			var memberRole = memberRoles.get(i);
			
			var firstConnectionPart = memberRole.getConnectionParts().get(0);			
			if (!firstConnectionPart.getBendpointLocations().isEmpty()) {	
				firstConnectionPart.getBendpointLocations().stream()
						.map(bendpoint -> new DeleteBendpointCommand(firstConnectionPart, 0))
						.forEach(commands::add);
			}
			
			if (memberRole.getConnectionParts().size() > 1 && !memberRole.getConnectionParts().get(1).getBendpointLocations().isEmpty()) {
				var secondConnectionPart = memberRole.getConnectionParts().get(1);
				secondConnectionPart.getBendpointLocations().stream()
						.map(bendpoint -> new DeleteBendpointCommand(secondConnectionPart, 0))
						.forEach(commands::add);
			}
			
			if (memberRole.getConnectionParts().size() > 1) {
				commands.add(new DeleteConnectorsCommand(memberRole));
			}
			
			var member = memberRole.getRecord();
			if (member.getLocationMode() == LocationMode.VIA && member.getViaSpecification().getSet() == memberRole.getSet()) {
				commands.add(new MakeRecordDirectCommand(member));
			}
			
			addDeleteOrRemoveCommand(memberRoles, i, commands);
		}
		
		return assembleFinalCommand(memberRoles, commands);
	}
	
	private static IModelChangeCommand assembleFinalCommand(List<MemberRole> memberRoles, List<IModelChangeCommand> commands) {
		if (commands.size() > 1) {
			var cc = new ModelChangeCompoundCommand();
			var set = memberRoles.get(0).getSet();
			if (set.getSystemOwner() != null) {
				cc.setLabel("Delete index");
			} else if (set.getVsamIndex() != null) {
				cc.setLabel("Delete VSAM index");
			} else if (memberRoles.size() == 1 && set.getMembers().size() > 1) {
				cc.setLabel("Remove member record type from set " + Tools.removeTrailingUnderscore(set.getName()));			
			} else {
				cc.setLabel("Delete set");
			}
			commands.stream()
					.map(Command.class::cast)
					.forEach(cc::add);
			return cc;
		} else {
			return commands.get(0);
		}
	}
	
	private static void addDeleteOrRemoveCommand(List<MemberRole> memberRoles, int memberRoleIndex, List<IModelChangeCommand> commands) {
		var memberRole = memberRoles.get(memberRoleIndex);
		if (memberRole.getSet().getSystemOwner() != null) {
			// indexed sets are never multiple-member sets and so this will be the last command
			commands.add(new DeleteIndexCommand(memberRole.getSet().getSystemOwner()));				
		} else if (memberRole.getSet().getVsamIndex() != null) {
			// VSAM indexes are never multiple-member sets and so this will be the last command
			commands.add(new DeleteVsamIndexCommand(memberRole.getSet().getVsamIndex()));				
		} else if (memberRole.getSet().getMembers().size() == 1 || memberRoles.size() == memberRole.getSet().getMembers().size() &&
				memberRoleIndex == (memberRoles.size() - 1)) {
			
			// not a multiple-member set or we're processing the last member in a multiple-member set
			commands.add(new DeleteSetCommand(memberRole.getSet()));				
		} else {
			// multiple-member set and not the last member record type				
			commands.add(new RemoveMemberFromSetCommand(memberRole));				
		}		
	}
	
	private DeleteSetOrIndexCommandCreationAssistant() {
	}
	
}
