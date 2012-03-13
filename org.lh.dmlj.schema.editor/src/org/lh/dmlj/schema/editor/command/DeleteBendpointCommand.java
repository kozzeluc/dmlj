package org.lh.dmlj.schema.editor.command;

import org.lh.dmlj.schema.ConnectionPart;

public class DeleteBendpointCommand extends AbstractBendpointCommand {	
	
	public DeleteBendpointCommand(ConnectionPart connectionPart, int index) {
		super(connectionPart, index);		
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