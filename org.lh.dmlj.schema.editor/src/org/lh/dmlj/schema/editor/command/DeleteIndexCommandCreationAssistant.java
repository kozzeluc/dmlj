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
package org.lh.dmlj.schema.editor.command;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaRecord;

/**
 * Deleting an index might go further than just deleting the indexed set involved; this class
 * centralizes command creation for deleting indexes.
 */
public abstract class DeleteIndexCommandCreationAssistant {

	public static Command getCommand(MemberRole memberRole) {
		
		// we might need to delete the connectors or change the member record type's location mode 
		// to DIRECT first, so create a chained command when needed
		List<Command> commands = new ArrayList<>();								
		
		if (memberRole.getConnectionParts().size() > 1) {
			commands.add(new DeleteConnectorsCommand(memberRole));
		}
		
		SchemaRecord member = memberRole.getRecord();
		if (member.getLocationMode() == LocationMode.VIA &&
			member.getViaSpecification().getSet() == memberRole.getSet()) {
			
			commands.add(new MakeRecordDirectCommand(member));
		}
		
		commands.add(new DeleteIndexCommand(memberRole.getSet().getSystemOwner()));
		
		if (commands.size() > 1) {
			CompoundCommand cc = new CompoundCommand("Delete index");
			for (Command command : commands) {
				cc.add(command);
			}
			return cc;
		} else {
			return commands.get(0);
		}		
		
	}
	
}
