/**
 * Copyright (C) 2015  Luc Hermans
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program.  If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact information: kozzeluc@gmail.com.
 */
package org.lh.dmlj.schema.editor.command;

import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.DiagramNode;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SystemOwner;

public class MoveDiagramNodeCommand extends ModelChangeBasicCommand {
	
	private DiagramLocation diagramLocation;	
	
	private DiagramNode diagramNode;
	private int 		oldX;
	private int 		oldY;
	protected int 		x;
	protected int 		y;
	
	protected ISupplier<? extends DiagramNode> diagramNodeSupplier;
	
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
	
	public MoveDiagramNodeCommand(ISupplier<? extends DiagramNode> diagramNodeSupplier, int x, int y) {
		super();		
		this.diagramNodeSupplier = diagramNodeSupplier;
		this.x = x;
		this.y = y;		
		setLabel("Move connection label");
	}
	
	@Override
	public void execute() {
		if (diagramNodeSupplier != null) {
			diagramNode = diagramNodeSupplier.supply();
		}
		diagramLocation = diagramNode.getDiagramLocation();
		oldX = diagramLocation.getX();
		oldY = diagramLocation.getY();
		redo();
	}
	
	@Override
	public void redo() {
		diagramLocation.setX(x);
		diagramLocation.setY(y);
	}
	
	@Override
	public void undo() {
		diagramLocation.setX(oldX);
		diagramLocation.setY(oldY);
	}
	
}
