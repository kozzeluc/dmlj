package org.lh.dmlj.schema.editor.command;

import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.Guide;
import org.lh.dmlj.schema.Ruler;
import org.lh.dmlj.schema.SchemaFactory;

public class CreateGuideCommand extends Command {

	private Guide guide; 	
	private int   position;
	private Ruler ruler;
	
	public CreateGuideCommand(Ruler ruler, int position) {
		super("Add Guide");
		this.ruler = ruler;
		this.position = position;
	}		
		
	@Override
	public void execute() {		
		guide = SchemaFactory.eINSTANCE.createGuide();
		guide.setPosition(position);
		ruler.getGuides().add(guide);							
	}
	
	@Override
	public void undo() {		
		ruler.getGuides().remove(guide);					
	}	
	
}