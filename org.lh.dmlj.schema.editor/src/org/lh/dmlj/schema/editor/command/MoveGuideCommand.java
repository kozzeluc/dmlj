package org.lh.dmlj.schema.editor.command;

import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.Guide;

public class MoveGuideCommand extends Command {

	private Guide guide; 
	private int   positionDelta;
	
	public MoveGuideCommand(Guide guide, int positionDelta) {
		super("Move Guide");
		this.guide = guide;
		this.positionDelta = positionDelta;
	}		
	
	@Override
	public void execute() {		
		guide.setPosition(guide.getPosition() + positionDelta);		
	}
	
	@Override
	public void undo() {		
		guide.setPosition(guide.getPosition() - positionDelta);		
	}	
	
}