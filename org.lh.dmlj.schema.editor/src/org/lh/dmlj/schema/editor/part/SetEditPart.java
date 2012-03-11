package org.lh.dmlj.schema.editor.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.DiagramNode;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.command.CreateConnectorCommand;
import org.lh.dmlj.schema.editor.policy.SetBendpointEditPolicy;

/** 
 * A ConnectionPart represents a line, possibly bended, representing the set.  
 * A set will contain a maximum of 2 ConnectionPart instances; if only 1 
 * ConnectionPart is present, the line is drawn directly between the figures 
 * representing the owner and (a) member of the set.  If 2 ConnectionParts 
 * are present for a set, it means that 1 line is drawn between the owner figure 
 * and the first connector (figure) and 1 line between the second connector 
 * (figure) and the member record (figure).<br><br>
 * 
 * SetEditPart instances currently represent a direct line between the
 * owner and member figures and each MemberRole is considered to contain only 1
 * ConnectionPart instance (in other words, we currently don't support 
 * connectors).
 */
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
	
	public SetEditPart(ConnectionPart connectionPart) {
		super();
		setModel(connectionPart);
	}
	
	@Override
	public final void activate() {
		super.activate();
		// add a model change listener for the ConnectionPart, the MemberRole to
		// which it belongs and the Set; we ignore the other ConnectionPart
		// belonging to the MemberRole, if any...
		getModel().eAdapters().add(changeListener);			 
		getModel().getMemberRole().eAdapters().add(changeListener); 	
		getModel().getMemberRole().getSet().eAdapters().add(changeListener);
	}	
	
	@Override
	protected void createEditPolicies() {
		
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new XYLayoutEditPolicy() {

			@Override
			protected Command getCreateCommand(CreateRequest request) {
				if (request.getNewObjectType() != Connector.class ||
					getModel().getMemberRole().getConnectionParts().size() > 1) {
					
					return null;
				}				
				return new CreateConnectorCommand(getModel().getMemberRole(),
						   						  request.getLocation());
			}
			
		});	
		
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
		
		MemberRole memberRole = getModel().getMemberRole();
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
		
		// add a tooltip containing the set's name; this is helpful if the
        // connection endpoints are not easy to locate...
        String adjustedSetName;
        if (set.getName().endsWith("_")) {
            StringBuilder p = new StringBuilder(set.getName());
            p.setLength(p.length() - 1);
            adjustedSetName = p.toString();
        } else {
            adjustedSetName = set.getName();
        }
        Label tooltip = new Label(adjustedSetName);
        connection.setToolTip(tooltip);
		
		return connection;		
	}
	
	@Override
	public final void deactivate() {
		// remove the model change listeners...		 	
		getModel().getMemberRole().getSet().eAdapters().remove(changeListener);
		getModel().getMemberRole().eAdapters().remove(changeListener);
		getModel().eAdapters().remove(changeListener);			 
		super.deactivate();
	}
	
	public ConnectionPart getModel() {
		return (ConnectionPart) super.getModel();
	}	
	
	/**
	 * @return the location, in absolute coordinates and as stored in the model
	 *         (i.e. unscaled), for the RecordFigure or IndexFigure that acts as 
	 *         the owner of this set
	 */
	private PrecisionPoint getOwnerFigureLocation() {		
		DiagramNode owner;
		if (getModel().getMemberRole().getSet().getSystemOwner() != null) {
			// system owned set
			owner = getModel().getMemberRole().getSet().getSystemOwner();			 
		} else {
			// user owned set
			owner = getModel().getMemberRole().getSet().getOwner().getRecord();
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
		// no connectors are currently involved
		for (DiagramLocation location : getModel().getBendpointLocations()) {
			
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