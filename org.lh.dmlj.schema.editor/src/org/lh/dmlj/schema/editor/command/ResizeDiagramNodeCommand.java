package org.lh.dmlj.schema.editor.command;

import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.ResizableDiagramNode;

public class ResizeDiagramNodeCommand extends Command {
	
	private ResizableDiagramNode diagramNode;
	private short 				 oldWidth;
	private short 				 oldHeight;
	private short 				 width;
	private short 				 height;
	
	@SuppressWarnings("unused")
	private ResizeDiagramNodeCommand() {
		super();
	}
	
	public ResizeDiagramNodeCommand(ResizableDiagramNode diagramNode, short width, short height) {
		super();
		this.diagramNode = diagramNode;
		this.width = width;
		this.height = height;
		if (diagramNode instanceof DiagramLabel) {
			setLabel("Resize diagram label");
		} else {
			setLabel("Resize");
		}
	}
	
	@Override
	public void execute() {
		oldWidth = diagramNode.getWidth();
		oldHeight = diagramNode.getHeight();
		diagramNode.setWidth(width);
		diagramNode.setHeight(height);
	}
	
	@Override
	public void undo() {
		diagramNode.setWidth(oldWidth);
		diagramNode.setHeight(oldHeight);
	}
	
}