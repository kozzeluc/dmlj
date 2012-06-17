package org.lh.dmlj.schema.editor.command;

import java.text.NumberFormat;

import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.Schema;

public class SetZoomLevelCommand extends Command {
	
	private boolean canUndo = true;
	
	private Schema schema; 
	private double zoomLevel;
	
	private double oldZoomLevel;
	
	public SetZoomLevelCommand(Schema schema, double zoomLevel) {
		this(schema, zoomLevel, true);
	}
	
	public SetZoomLevelCommand(Schema schema, double zoomLevel, boolean canUndo) {
		super("Zoom to " + NumberFormat.getPercentInstance().format(zoomLevel));
		this.schema = schema;
		this.zoomLevel = zoomLevel;
		this.canUndo = canUndo;
	}	
	
	@Override
	public boolean canUndo() {
		return canUndo;
	}
	
	@Override
	public void execute() {
		oldZoomLevel = schema.getDiagramData().getZoomLevel();
		schema.getDiagramData().setZoomLevel(zoomLevel);
	}	

	@Override
	public void undo() {
		schema.getDiagramData().setZoomLevel(oldZoomLevel);
	}
	
}