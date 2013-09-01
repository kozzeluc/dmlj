package org.lh.dmlj.schema.editor.command;

import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.DiagramNode;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SystemOwner;

public class MoveDiagramNodeCommand extends Command {
	
	private DiagramNode diagramNode;
	private int 		oldX;
	private int 		oldY;
	private int 		x;
	private int 		y;
	
	@SuppressWarnings("unused")
	private MoveDiagramNodeCommand() {
		super();
	}
	
	public MoveDiagramNodeCommand(DiagramNode diagramNode, int x, int y) {
		super();		
		this.diagramNode = diagramNode;
		this.x = x;
		this.y = y;
		if (diagramNode instanceof SchemaRecord) {
			setLabel("Move record " + ((SchemaRecord)diagramNode).getName());
		} else if (diagramNode instanceof SystemOwner) {
			setLabel("Move index " + 
					 ((SystemOwner)diagramNode).getSet().getName());
		} else if (diagramNode instanceof ConnectionLabel) {
			setLabel("Move connection label for set " + 
					 ((ConnectionLabel)diagramNode).getMemberRole()
					 							   .getSet()
					 							   .getName());
		} else if (diagramNode instanceof Connector) {
			setLabel("Move connector for set " + 
					 ((Connector)diagramNode).getConnectionPart()
					 				     	 .getMemberRole()
					 				     	 .getSet()
					 					  	 .getName());
		} else if (diagramNode instanceof DiagramLabel) {
			setLabel("Move diagram label");
		} else {
			setLabel("Move");
		}
	}
	
	@Override
	public void execute() {
		oldX = diagramNode.getDiagramLocation().getX();
		oldY = diagramNode.getDiagramLocation().getY();
		diagramNode.getDiagramLocation().setX(x);
		diagramNode.getDiagramLocation().setY(y);
	}
	
	@Override
	public void undo() {
		diagramNode.getDiagramLocation().setX(oldX);
		diagramNode.getDiagramLocation().setY(oldY);
	}
	
}