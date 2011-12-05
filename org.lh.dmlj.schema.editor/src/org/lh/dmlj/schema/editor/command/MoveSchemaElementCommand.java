package org.lh.dmlj.schema.editor.command;

import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.DiagramLocationProvider;

public class MoveSchemaElementCommand extends Command {
	
	private DiagramLocationProvider locationProvider;
	private int 					oldX;
	private int 					oldY;
	private int 					x;
	private int 					y;
	
	@SuppressWarnings("unused")
	private MoveSchemaElementCommand() {
		super();
	}
	
	public MoveSchemaElementCommand(DiagramLocationProvider locationProvider, 
									int x, int y) {
		super();
		this.locationProvider = locationProvider;
		this.x = x;
		this.y = y;		
	}
	
	@Override
	public void execute() {
		oldX = locationProvider.getDiagramLocation().getX();
		oldY = locationProvider.getDiagramLocation().getY();
		locationProvider.getDiagramLocation().setX(x);
		locationProvider.getDiagramLocation().setY(y);
	}
	
	@Override
	public void undo() {
		locationProvider.getDiagramLocation().setX(oldX);
		locationProvider.getDiagramLocation().setY(oldY);
	}
	
}