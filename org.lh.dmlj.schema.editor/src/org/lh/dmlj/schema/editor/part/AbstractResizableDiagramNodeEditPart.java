package org.lh.dmlj.schema.editor.part;

import org.eclipse.gef.EditPolicy;
import org.lh.dmlj.schema.ResizableDiagramNode;
import org.lh.dmlj.schema.editor.policy.ModifiedResizableEditPolicy;

/**
 * A superclass for all resizable diagram nodes.
 * @author Luc Hermans
 *
 * @param <T> the model type
 */
public abstract class AbstractResizableDiagramNodeEditPart<T extends ResizableDiagramNode> 
	extends AbstractDiagramNodeEditPart<T> {
	
	protected AbstractResizableDiagramNodeEditPart(T diagramNode) {
		super(diagramNode);
	}
	
	@Override
	public EditPolicy getResizeEditPolicy() {
		return new ModifiedResizableEditPolicy();
	}
	
	public abstract double getZoomLevel();
	
}