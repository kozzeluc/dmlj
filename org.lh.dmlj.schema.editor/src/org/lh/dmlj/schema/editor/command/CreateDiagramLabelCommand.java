package org.lh.dmlj.schema.editor.command;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaFactory;

public class CreateDiagramLabelCommand extends Command {
	
	private DiagramLabel diagramLabel;
	private short		 height;
	private Point  		 location;
	private Schema 		 schema;
	private short 		 width;
	
	public CreateDiagramLabelCommand(Schema schema, Point location, Dimension size) {
		super("Add diagram label");
		this.schema = schema;
		this.location = location;
		this.width = (short) size.width;
		this.height = (short) size.height;
	}
	
	@Override
	public void execute() {
		
		Assert.isTrue(schema.getDiagramData().getLabel() == null);
		
		DiagramLocation diagramLocation = SchemaFactory.eINSTANCE.createDiagramLocation();
		diagramLocation.setX(location.x);
		diagramLocation.setY(location.y);
		diagramLocation.setEyecatcher("diagram label");
		schema.getDiagramData().getLocations().add(diagramLocation);
		
		diagramLabel = SchemaFactory.eINSTANCE.createDiagramLabel();
		diagramLabel.setDiagramLocation(diagramLocation);
		if (schema.getDescription() != null && ! schema.getDescription().trim().equals("")) {
			diagramLabel.setDescription(schema.getDescription());
		}
		diagramLabel.setWidth(width);
		diagramLabel.setHeight(height);
		diagramLabel.setDiagramData(schema.getDiagramData());
		
	}
	
	@Override
	public void redo() {
		
		Assert.isTrue(schema.getDiagramData().getLabel() == null);
		
		schema.getDiagramData().getLocations().add(diagramLabel.getDiagramLocation());
		
		diagramLabel.setDiagramData(schema.getDiagramData());
		
	}
	
	@Override
	public void undo() {
		
		Assert.isTrue(schema.getDiagramData().getLabel() != null);
		
		diagramLabel.setDiagramData(null);
		
		schema.getDiagramData().getLocations().remove(diagramLabel.getDiagramLocation());
	}
	
}