package org.lh.dmlj.schema.editor.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.DiagramNode;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.policy.SetBendpointEditPolicy;

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
		getModel().getSet().eAdapters().add(changeListener);	
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
		
		connection.setLineWidth(1);
		connection.setForegroundColor(ColorConstants.black);
		
		MemberRole memberRole = getModel();
		Set set = memberRole.getSet();
		if (set.getOwner() != null) { 
			// user owned set (chained or indexed), make sure an arrow is drawn
			// at the target end...
			PolylineDecoration decoration = new PolylineDecoration();
			decoration.setTemplate(PolylineDecoration.TRIANGLE_TIP);
			connection.setTargetDecoration(decoration);
		} else {
			connection.setLineStyle(Graphics.LINE_DASH);
		}
		
		return connection;		
	}
	
	@Override
	public final void deactivate() {
		getModel().getSet().eAdapters().remove(changeListener);
		getModel().eAdapters().remove(changeListener);		
		super.deactivate();
	}
	
	public MemberRole getModel() {
		return (MemberRole) super.getModel();
	}	
	
	/**
	 * @return the location, in absolute coordinates and as stored in the model
	 *         (i.e. unscaled), for the RecordFigure or IndexFigure that acts as 
	 *         the owner of this set
	 */
	private PrecisionPoint getOwnerFigureLocation() {		
		DiagramNode owner;
		if (getModel().getSet().getSystemOwner() != null) {
			// system owned set
			owner = getModel().getSet().getSystemOwner();			 
		} else {
			// user owned set
			owner = getModel().getSet().getOwner().getRecord();
		}
		DiagramLocation location = owner.getDiagramLocation();
		return new PrecisionPoint(location.getX(), location.getY());
	}

	private void refreshBendpointEditPolicy() {
		installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, 
						  new SetBendpointEditPolicy(this));
	}
	
	@Override
	protected void refreshVisuals() {		
		
		List<Bendpoint> bendpoints = new ArrayList<>();
		for (DiagramLocation location : getModel().getDiagramBendpoints()) {
			
			// the model stores the (unscaled) bendpoint location relative to 
			// the owner figure (which is either a record or index figure); we 
			// need to convert this to an absolute (unscaled) location...
			PrecisionPoint ownerLocation = getOwnerFigureLocation();			
			PrecisionPoint p = 
				new PrecisionPoint(ownerLocation.preciseX() + location.getX(), 
								   ownerLocation.preciseY() + location.getY());			
			
            AbsoluteBendpoint bendpoint = new AbsoluteBendpoint(p.x, p.y);
			bendpoints.add(bendpoint);
		}
		getConnectionFigure().setRoutingConstraint(bendpoints);
		
		refreshBendpointEditPolicy();
	}
	
}