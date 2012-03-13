package org.lh.dmlj.schema.editor.command;

import org.lh.dmlj.schema.ConnectionPart;

public class CreateBendpointCommand extends AbstractBendpointCommand {	
	
	public CreateBendpointCommand(ConnectionPart connectionPart, int index, 
								  int x, int y) {
		super(connectionPart, index, x, y);		
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