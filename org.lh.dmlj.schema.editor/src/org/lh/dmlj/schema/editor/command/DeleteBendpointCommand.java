package org.lh.dmlj.schema.editor.command;

import org.lh.dmlj.schema.MemberRole;

public class DeleteBendpointCommand extends AbstractBendpointCommand {	
	
	public DeleteBendpointCommand(MemberRole memberRole, int index) {
		super(memberRole, index);		
	}
	
	@Override
	public void execute() {
		removeBendpoint(index); // will set oldX and oldY	
	}
	
	@Override
	public void undo() {
		insertBendpoint(index, oldX, oldY);
	}
}