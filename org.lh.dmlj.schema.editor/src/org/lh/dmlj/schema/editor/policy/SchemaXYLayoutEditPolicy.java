package org.lh.dmlj.schema.editor.policy;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.DiagramNode;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.command.CreateDiagramLabelCommand;
import org.lh.dmlj.schema.editor.command.MoveDiagramNodeCommand;
import org.lh.dmlj.schema.editor.figure.DiagramLabelFigure;
import org.lh.dmlj.schema.editor.part.AbstractDiagramNodeEditPart;
import org.lh.dmlj.schema.editor.preference.PreferenceConstants;

public class SchemaXYLayoutEditPolicy extends XYLayoutEditPolicy {

	private Schema schema;
	
	public SchemaXYLayoutEditPolicy(Schema schema) {
		super();
		this.schema = schema;
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
		if (child instanceof AbstractDiagramNodeEditPart<?>) {
			// a diagram node edit part (resizable or not)
			return ((AbstractDiagramNodeEditPart<?>) child).getResizeEditPolicy();
		} else {
			// any other edit part
			return super.createChildEditPolicy(child);
		}
	}
	
	@Override
	protected Command getCreateCommand(CreateRequest request) {
		
		if (request.getNewObjectType() != DiagramLabel.class ||
			schema.getDiagramData().getLabel() != null) {
			
			return null;
		}	
		
		String organisation = 
			Plugin.getDefault().getPreferenceStore().getString(PreferenceConstants.ORGANISATION);
		Dimension size = 
			DiagramLabelFigure.getInitialSize(organisation, schema.getName(), schema.getVersion(), 
											  schema.getDescription()); 
		
		return new CreateDiagramLabelCommand(schema, request.getLocation(), size);
	}	
	
}