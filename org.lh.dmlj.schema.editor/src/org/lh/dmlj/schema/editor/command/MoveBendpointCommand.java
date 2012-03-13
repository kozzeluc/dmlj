package org.lh.dmlj.schema.editor.command;

import org.lh.dmlj.schema.ConnectionPart;

public class MoveBendpointCommand extends AbstractBendpointCommand {	
	
	public MoveBendpointCommand(ConnectionPart connectionPart, int index, int x, 
								int y) {
		super(connectionPart, index, x, y);		
	}
	
	@Override
	public void execute() {
		removeBendpoint(index);	// will set oldX and oldY
		insertBendpoint(index, x, y);
	}
	
	@Override
	public void undo() {
		// saveguard oldX and oldY because they will be changed by the call to
		// removeBendpoint(index)...
		int oldXa = oldX;
		int oldYa = oldY;
		removeBendpoint(index);	// will set oldX and oldY to x and y
		insertBendpoint(index, oldXa, oldYa);
	}
	
}