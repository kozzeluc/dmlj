package org.lh.dmlj.schema.editor.command;

import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.Guide;
import org.lh.dmlj.schema.Ruler;

public class DeleteGuideCommand extends Command {

	private Guide guide;
	private Ruler ruler;
	
	public DeleteGuideCommand(Ruler ruler, Guide guide) {
		super("Delete Guide");
		this.ruler = ruler;
		this.guide = guide;
	}		
	
	@Override
	public void execute() {		
		ruler.getGuides().remove(guide);				
	}
	
	@Override
	public void undo() {		
		ruler.getGuides().add(guide);		
	}	
	
}