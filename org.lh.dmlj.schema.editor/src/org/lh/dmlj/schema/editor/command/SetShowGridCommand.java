package org.lh.dmlj.schema.editor.command;

import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.Schema;

@Deprecated
public class SetShowGridCommand extends Command {
	
	private Schema  schema; 
	private boolean showGrid;
	
	private boolean oldShowGrid;
	
	public SetShowGridCommand(Schema schema, boolean zoomLevel) {
		super(zoomLevel ? "Show Grid" : "Hide Grid");
		this.schema = schema;
		this.showGrid = zoomLevel;
	}	
	
	@Override
	public void execute() {
		oldShowGrid = schema.getDiagramData().isShowGrid();
		schema.getDiagramData().setShowGrid(showGrid);
	}	

	@Override
	public void undo() {
		schema.getDiagramData().setShowGrid(oldShowGrid);
	}
	
}