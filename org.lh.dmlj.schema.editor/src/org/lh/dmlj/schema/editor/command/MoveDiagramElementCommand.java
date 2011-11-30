package org.lh.dmlj.schema.editor.command;

import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.DiagramElement;

public class MoveDiagramElementCommand extends Command {
	
	private DiagramElement 	diagramElement;
	private int 			oldX;
	private int 			oldY;
	private int 			x;
	private int 			y;
	
	@SuppressWarnings("unused")
	private MoveDiagramElementCommand() {
		super();
	}
	
	public MoveDiagramElementCommand(DiagramElement diagramElement, int x, 
									 int y) {
		super();
		this.diagramElement = diagramElement;
		this.x = x;
		this.y = y;		
	}
	
	@Override
	public void execute() {
		oldX = diagramElement.getX();
		oldY = diagramElement.getY();
		diagramElement.setX(x);
		diagramElement.setY(y);
	}
	
	@Override
	public void undo() {
		diagramElement.setX(oldX);
		diagramElement.setY(oldY);
	}
	
}