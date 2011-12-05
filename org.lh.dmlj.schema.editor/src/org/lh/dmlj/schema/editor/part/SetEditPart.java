package org.lh.dmlj.schema.editor.part;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Set;

public class SetEditPart extends AbstractConnectionEditPart {

	public SetEditPart(MemberRole memberRole) {
		super();
		setModel(memberRole);
	}
	
	@Override
	protected void createEditPolicies() {
		
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
						  new ConnectionEndpointEditPolicy());
		
		BendpointEditPolicy bendpointPolicy = new BendpointEditPolicy() {
			
			@Override
			protected Command getCreateBendpointCommand(BendpointRequest request) {		
				System.out.println(request.getIndex() + " " + 
								   request.getLocation());
				return null;
			}

			@Override
			protected Command getDeleteBendpointCommand(BendpointRequest request) {
				return null;
			}

			@Override
			protected Command getMoveBendpointCommand(BendpointRequest request) {
				return null;
			}
			
		};
		installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, bendpointPolicy);
	}
	
	@Override
	protected IFigure createFigure() {
		
		PolylineConnection connection = new PolylineConnection();
		
		BendpointConnectionRouter router = new BendpointConnectionRouter();
		connection.setConnectionRouter(router);
		
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