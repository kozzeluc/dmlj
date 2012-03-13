package org.lh.dmlj.schema.editor.policy;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.lh.dmlj.schema.DiagramNode;
import org.lh.dmlj.schema.editor.command.MoveDiagramNodeCommand;
import org.lh.dmlj.schema.editor.part.ConnectorEditPart;
import org.lh.dmlj.schema.editor.part.IndexEditPart;
import org.lh.dmlj.schema.editor.part.RecordEditPart;
import org.lh.dmlj.schema.editor.part.SetDescriptionEditPart;

public class MoveDiagramNodeEditPolicy extends XYLayoutEditPolicy {

	public MoveDiagramNodeEditPolicy() {
		super();
	}
	
	@Override
	protected Command createChangeConstraintCommand(ChangeBoundsRequest request, 
													EditPart child,
													Object constraint) {
		
		if (child.getModel() instanceof DiagramNode) {
			// we're dealing with a DiagramNode, it can only be a
			// move request, so create the move command...
			Rectangle box = (Rectangle)constraint;
			DiagramNode locationProvider = (DiagramNode) child.getModel();
			return new MoveDiagramNodeCommand(locationProvider, box.x, box.y);
		} else {
			// not a DiagramNode or user is trying to resize, make
			// sure he/she gets the right feedback
			return null;
		}
	}
	
	@Override
	protected EditPolicy createChildEditPolicy(EditPart child) {		
		if (child instanceof RecordEditPart ||
			child instanceof IndexEditPart ||
			child instanceof SetDescriptionEditPart ||
			child instanceof ConnectorEditPart) {
			
			return new ModifiedNonResizableEditPolicy();
		}
		return super.createChildEditPolicy(child);
	}
	
	@Override
	protected Command getCreateCommand(CreateRequest request) {
		return null;
	}	
	
}