package org.lh.dmlj.schema.editor.command;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.Schema;

public class DeleteDiagramLabelCommand extends Command {
	
	private DiagramLabel diagramLabel;
	private Schema 		 schema;	
	
	public DeleteDiagramLabelCommand(Schema schema) {
		super("Delete diagram label");
		this.schema = schema;
	}
	
	@Override
	public void execute() {
		
		diagramLabel = schema.getDiagramData().getLabel();
		Assert.isTrue(diagramLabel != null);
		
		diagramLabel.setDiagramData(null);
		
		schema.getDiagramData().getLocations().remove(diagramLabel.getDiagramLocation());
		
	}

	@Override
	public void undo() {
		
		Assert.isTrue(schema.getDiagramData().getLabel() == null);
	
		schema.getDiagramData().getLocations().add(diagramLabel.getDiagramLocation());
		
		diagramLabel.setDiagramData(schema.getDiagramData());
		
	}
	
	@Override
	public void redo() {
		
		Assert.isTrue(schema.getDiagramData().getLabel() != null);
		
		diagramLabel.setDiagramData(null);
		
		schema.getDiagramData().getLocations().remove(diagramLabel.getDiagramLocation());
		
	}
	
}