package org.lh.dmlj.schema.editor.part;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Set;

public class SetEditPart extends AbstractConnectionEditPart {

	public SetEditPart(MemberRole memberRole) {
		super();
		setModel(memberRole);
	}
	
	@Override
	protected void createEditPolicies() {
		ConnectionEndpointEditPolicy selectionPolicy =
			new ConnectionEndpointEditPolicy();
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, selectionPolicy);
	}
	
	@Override
	protected IFigure createFigure() {
		
		PolylineConnection connection = new PolylineConnection();
		
		MemberRole memberRole = getModel();
		Set set = memberRole.getSet();
		if (set.getOwner() != null) { 
			// user owned set (chained or indexed), make sure an arrow is drawn
			// at the target end...
			PolylineDecoration decoration = new PolylineDecoration();
			decoration.setTemplate(PolylineDecoration.TRIANGLE_TIP);
			connection.setTargetDecoration(decoration);
		}
		
		connection.setLineWidth(1);				
		
		return connection;		
	}	
	
	public MemberRole getModel() {
		return (MemberRole) super.getModel();
	}			
	
}