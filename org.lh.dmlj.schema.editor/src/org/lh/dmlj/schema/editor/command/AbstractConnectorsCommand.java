/**
 * Copyright (C) 2013  Luc Hermans
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

import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.MemberRole;

public abstract class AbstractConnectorsCommand extends Command {
	
	protected MemberRole memberRole;
	
	protected AbstractConnectorsCommand(String label, MemberRole memberRole) {
		super(label);
		this.memberRole = memberRole;
	}
	
	protected void fixEyecatchers() {
		int index = 0;
		for (ConnectionPart connectionPart : memberRole.getConnectionParts()) {
			for (DiagramLocation bendpoint : 
				 connectionPart.getBendpointLocations()) {
				
				bendpoint.setEyecatcher(generateEyecatcher(index++));
			}
		}
	}

	private String generateEyecatcher(int index) {
		return "bendpoint [" + index + "] set " + 
			   memberRole.getSet().getName() + " (" +
			   memberRole.getRecord().getName() + ")";		
	}
	
}
