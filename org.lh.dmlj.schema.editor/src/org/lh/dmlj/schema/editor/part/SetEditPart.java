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
import org.eclipse.draw2d.geometry.PrecisionPoint;
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
import org.lh.dmlj.schema.DiagramNode;
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
	
	/**
	 * @return the location, in absolute coordinates and as stored in the model, 
	 * 	       for the RecordFigure or IndexFigure that acts as the owner of 
	 * this set
	 */
	private Point getOwnerFigureLocation() {		
		DiagramNode owner;
		if (getModel().getSet().getSystemOwner() != null) {
			// system owned set
			owner = getModel().getSet().getSystemOwner();			 
		} else {
			// user owned set
			owner = getModel().getSet().getOwner().getRecord();
		}
		DiagramLocation location = owner.getDiagramLocation();
		Point p = new PrecisionPoint(location.getX(), location.getY());
		return p;
	}

	private void refreshBendpointEditPolicy() {
		BendpointEditPolicy bendpointPolicy = new BendpointEditPolicy() {
			
			@Override
			protected Command getCreateBendpointCommand(BendpointRequest request) {						
				Point p = request.getLocation().getCopy(); 
				Point ownerLocation = getOwnerFigureLocation();
                p.x -= ownerLocation.x; 
                p.y -= ownerLocation.y;
                getConnection().translateToRelative(p);
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
				Point ownerLocation = getOwnerFigureLocation();
				p.x -= ownerLocation.x; 
                p.y -= ownerLocation.y;
                getConnection().translateToRelative(p);
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
			
			// the model stores the bendpoint location relative to the owner
			// figure (which is either a record or index figure); we need to
			// convert this to an absolute location first...
			Point ownerLocation = getOwnerFigureLocation();
			Point p = new PrecisionPoint(ownerLocation.x + location.getX(), 
								   		 ownerLocation.y + location.getY());	
			
            AbsoluteBendpoint bendpoint = new AbsoluteBendpoint(p.x, p.y);
			bendpoints.add(bendpoint);
		}
		getConnectionFigure().setRoutingConstraint(bendpoints);
		
		refreshBendpointEditPolicy();
	}
	
}