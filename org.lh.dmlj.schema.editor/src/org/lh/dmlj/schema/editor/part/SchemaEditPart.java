package org.lh.dmlj.schema.editor.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.lh.dmlj.schema.DiagramElement;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.editor.command.MoveDiagramElementCommand;
import org.lh.dmlj.schema.editor.policy.ModifiedNonResizableEditPolicy;

public class SchemaEditPart extends AbstractGraphicalEditPart {

	@SuppressWarnings("unused")
	private SchemaEditPart() {
		super(); // disabled constructor
	}
	
	public SchemaEditPart(Schema schema) {
		super();
		setModel(schema);
	}
	
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new XYLayoutEditPolicy() {
			@Override
			protected Command createChangeConstraintCommand(ChangeBoundsRequest request, 
															EditPart child,
															Object constraint) {
				
				if (child.getModel() instanceof DiagramElement) {
					// we're dealing with a DiagramElement, it can only be a
					// move request, so create the move command...
					Rectangle box = (Rectangle)constraint;
					return new MoveDiagramElementCommand((DiagramElement)child.getModel(), 
														 box.x, box.y);
				} else {
					// not a DiagramElement or user is trying to resize, make
					// sure he/she gets the right feedback
					return null;
				}
			}
			@Override
			protected EditPolicy createChildEditPolicy(EditPart child) {
				if (child instanceof RecordEditPart ||
					child instanceof IndexEditPart ||
					child instanceof SetDescriptionEditPart) {
					
					return new ModifiedNonResizableEditPolicy();
				}
				return super.createChildEditPolicy(child);
			}
			@Override
			protected Command getCreateCommand(CreateRequest request) {
				return null;
			}
			
		});
		
	}

	@Override
	protected IFigure createFigure() {
		Figure figure = new FreeformLayer();
		figure.setBorder(new MarginBorder(3));
		figure.setLayoutManager(new FreeformLayout());
		return figure;
	}
	
	public Schema getModel() {
		return (Schema) super.getModel();
	}
	
	@Override
	protected List<?> getModelChildren() {
		List<Object> allObjects = new ArrayList<>();
		
		// records
		allObjects.addAll(getModel().getRecords());
		
		// set descriptions
		for (Set set : getModel().getSets()) {
			for (MemberRole memberRole : set.getMembers()) {
				allObjects.add(memberRole.getDiagramLabel());
			}
		}
		
		// indexes
		for (Set set : getModel().getSets()) {
			if (set.getMode() == SetMode.INDEXED && 
				set.getSystemOwner() != null) {
				
				allObjects.add(set.getSystemOwner());
			}
		}
		
		return allObjects;
	}
	
}