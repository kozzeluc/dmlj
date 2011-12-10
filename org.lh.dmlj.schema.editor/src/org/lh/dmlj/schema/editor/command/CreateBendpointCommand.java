package org.lh.dmlj.schema.editor.command;

import org.lh.dmlj.schema.MemberRole;

public class CreateBendpointCommand extends AbstractBendpointCommand {	
	
	public CreateBendpointCommand(MemberRole memberRole, int index, int x, int y) {
		super(memberRole, index, x, y);		
	}
	
	@Override
	public void execute() {
		insertBendpoint(index, x, y);			
	}
	
	@Override
	public void undo() {
		removeBendpoint(index);
	}
}