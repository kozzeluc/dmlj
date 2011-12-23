package org.lh.dmlj.schema.editor.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.command.CreateBendpointCommand;
import org.lh.dmlj.schema.editor.command.DeleteBendpointCommand;
import org.lh.dmlj.schema.editor.command.MoveBendpointCommand;

public class SetEditPart extends AbstractConnectionEditPart {

	private Adapter changeListener = new Adapter() {
		
		@Override
		public Notifier getTarget() {
			return null;
		}

		@Override
		public boolean isAdapterForType(Object type) {
			return false;
		}
		
		@Override
		public void notifyChanged(Notification notification) {
			refreshVisuals();
		}
		
		@Override
		public void setTarget(Notifier newTarget) {		
		}
		
	};
	
	public SetEditPart(MemberRole memberRole) {
		super();
		setModel(memberRole);
	}
	
	@Override
	public final void activate() {
		super.activate();
		getModel().eAdapters().add(changeListener);		
	}	
	
	@Override
	protected void createEditPolicies() {
		
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
						  new ConnectionEndpointEditPolicy());
		
		refreshBendpointEditPolicy();		
		
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
	
	@Override
	public final void deactivate() {
		getModel().eAdapters().remove(changeListener);		
		super.deactivate();
	}
	
	public MemberRole getModel() {
		return (MemberRole) super.getModel();
	}

	private void refreshBendpointEditPolicy() {
		BendpointEditPolicy bendpointPolicy = new BendpointEditPolicy() {
			
			@Override
			protected Command getCreateBendpointCommand(BendpointRequest request) {						
				Point p = request.getLocation().getCopy();             
                getConnection().translateToRelative(p); // explanation why needed ?
				CreateBendpointCommand command = 
					new CreateBendpointCommand(getModel(), request.getIndex(), 
											   p.x, p.y);
				return command;
			}

			@Override
			protected Command getDeleteBendpointCommand(BendpointRequest request) {
				DeleteBendpointCommand command = 
					new DeleteBendpointCommand(getModel(), request.getIndex());
				return command;
			}

			@Override
			protected Command getMoveBendpointCommand(BendpointRequest request) {
				Point p = request.getLocation().getCopy();             
                getConnection().translateToRelative(p); // explanation why needed ?
				MoveBendpointCommand command = 
					new MoveBendpointCommand(getModel(), request.getIndex(), 
											 p.x, p.y);
				return command;
			}
			
		};
		installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, bendpointPolicy);
	}
	
	@Override
	protected void refreshVisuals() {
		
		List<Bendpoint> bendpoints = new ArrayList<>();
		for (DiagramLocation location : getModel().getDiagramBendpoints()) {
			AbsoluteBendpoint bendpoint = 
				new AbsoluteBendpoint(location.getX(), location.getY());
			bendpoints.add(bendpoint);
		}
		getConnectionFigure().setRoutingConstraint(bendpoints);
		
		refreshBendpointEditPolicy();
	}
	
}