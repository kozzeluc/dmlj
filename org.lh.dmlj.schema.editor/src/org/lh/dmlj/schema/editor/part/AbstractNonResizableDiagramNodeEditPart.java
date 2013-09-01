package org.lh.dmlj.schema.editor.part;

import org.eclipse.gef.EditPolicy;
import org.lh.dmlj.schema.DiagramNode;
import org.lh.dmlj.schema.editor.policy.ModifiedNonResizableEditPolicy;

/**
 * A superclass for all non-resizable diagram nodes; although the ResizableDiagramNode type can be
 * provided as the type parameter (T); the diagram node will NOT be resizable.
 * @author Luc Hermans
 *
 * @param <T> the model type
 */
public abstract class AbstractNonResizableDiagramNodeEditPart<T extends DiagramNode> 
	extends AbstractDiagramNodeEditPart<T> {
	
	protected AbstractNonResizableDiagramNodeEditPart(T diagramNode) {
		super(diagramNode);
	}
	
	@Override
	public EditPolicy getResizeEditPolicy() {
		return new ModifiedNonResizableEditPolicy();
	}
	
}