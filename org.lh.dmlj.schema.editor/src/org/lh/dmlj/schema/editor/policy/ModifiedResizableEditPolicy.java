package org.lh.dmlj.schema.editor.policy;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.lh.dmlj.schema.ResizableDiagramNode;
import org.lh.dmlj.schema.editor.command.ResizeDiagramNodeCommand;
import org.lh.dmlj.schema.editor.part.AbstractResizableDiagramNodeEditPart;

public class ModifiedResizableEditPolicy extends ResizableEditPolicy {

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
		
		return new ResizeDiagramNodeCommand(diagramNode, newWidth, newHeight);
	}
	
}