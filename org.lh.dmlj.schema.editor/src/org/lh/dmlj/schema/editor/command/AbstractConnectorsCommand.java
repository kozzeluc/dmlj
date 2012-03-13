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