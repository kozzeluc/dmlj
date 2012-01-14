package org.lh.dmlj.schema.editor.command;

import java.text.NumberFormat;

import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.Schema;

public class SetZoomLevelCommand extends Command {
	
	private Schema schema; 
	private double zoomLevel;
	
	private double oldZoomLevel;
	
	public SetZoomLevelCommand(Schema schema, double zoomLevel) {
		super("Zoom to " + NumberFormat.getPercentInstance().format(zoomLevel));
		this.schema = schema;
		this.zoomLevel = zoomLevel;
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