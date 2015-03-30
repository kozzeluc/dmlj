/**
 * Copyright (C) 2013  Luc Hermans
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
package org.lh.dmlj.schema.editor.policy;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.DiagramNode;
import org.lh.dmlj.schema.ResizableDiagramNode;
import org.lh.dmlj.schema.editor.command.IModelChangeCommand;
import org.lh.dmlj.schema.editor.command.ResizeDiagramNodeCommand;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.part.AbstractResizableDiagramNodeEditPart;

public class ModifiedResizableEditPolicy extends ResizableEditPolicy {

	private static ModelChangeType getModelChangeType(DiagramNode diagramNode) {
		if (diagramNode instanceof DiagramLabel) {
			return ModelChangeType.RESIZE_DIAGRAM_LABEL;
		} else {
			throw new IllegalStateException("Unexpected diagram node: " + diagramNode);
		}
	}
	
	private static void putModelChangeContext(ModelChangeContext context,
											  ResizableDiagramNode diagramNode) {
		
		// since the diagram label is the only diagram node type we can resize, there is no need to
		// put any context data at the moment
	}

	public ModifiedResizableEditPolicy() {
		super();
	}
	
	@Override
	protected Command getResizeCommand(ChangeBoundsRequest request) {
		
		// make sure the resize request is for just 1 (resizable diagram node) edit part
		List<?> editParts = request.getEditParts(); 
		Assert.isTrue(editParts.size() == 1, "only 1 resizable diagram node expected");
		Assert.isTrue(editParts.get(0) instanceof AbstractResizableDiagramNodeEditPart, 
					  "expected a resizable diagram node");		
		
		// get the resizable diagram node
		AbstractResizableDiagramNodeEditPart<?> editPart = 
			(AbstractResizableDiagramNodeEditPart<?>) editParts.get(0); 
		ResizableDiagramNode diagramNode = editPart.getModel();
		
		// calculate the new (unscaled) width and height
		Dimension delta = request.getSizeDelta();		
		double zoomLevel = editPart.getZoomLevel();
		short newWidth = (short) (diagramNode.getWidth() + delta.width / zoomLevel);
		short newHeight = (short) (diagramNode.getHeight() + delta.height / zoomLevel);
		
		ModelChangeContext context = new ModelChangeContext(getModelChangeType(diagramNode));
		putModelChangeContext(context, diagramNode);
		IModelChangeCommand command = 
			new ResizeDiagramNodeCommand(diagramNode, newWidth, newHeight);
		command.setContext(context);
		return (Command) command;
	}
	
}
