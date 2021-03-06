/**
 * Copyright (C) 2016  Luc Hermans
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program.  If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact information: kozzeluc@gmail.com.
 */
package org.lh.dmlj.schema.editor.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.DiagramNode;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.command.infrastructure.CommandExecutionMode;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeListener;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.palette.IMultipleMemberSetPlaceHolder;
import org.lh.dmlj.schema.editor.policy.RemoveMemberFromSetEditPolicy;
import org.lh.dmlj.schema.editor.policy.SetBendpointEditPolicy;
import org.lh.dmlj.schema.editor.policy.SetXYLayoutEditPolicy;

/** 
 * A ConnectionPart represents a line, possibly bended, representing the set.  
 * A set will contain a maximum of 2 ConnectionPart instances; if only 1 
 * ConnectionPart is present, the line is drawn directly between the figures 
 * representing the owner and (a) member of the set.  If 2 ConnectionParts 
 * are present for a set, it means that 1 line is drawn between the owner figure 
 * and the first connector (figure) and 1 line between the second connector 
 * (figure) and the member record (figure).<br><br> 
 */
public class SetEditPart 
	extends AbstractConnectionEditPart implements IModelChangeListener {

	private MemberRole 			 memberRole;	
	private IModelChangeProvider modelChangeProvider; // null means we're in read-only mode
	private boolean tooltipSet;
	
	/**
	 * Checks whether the feature of interest is set in a grouped model change.
	 * @param setFeatures the features set in the model change
	 * @param featureOfInterest the feature of interest
	 * @return true if the feature of interest is contained in the list of set features, false if 
	 * 		   not
	 */
	protected static final boolean isFeatureSet(EStructuralFeature[] setFeatures, 
								   		  		EStructuralFeature featureOfInterest) {
		
		for (EStructuralFeature feature : setFeatures) {
			if (feature == featureOfInterest) {
				return true;
			}
		}
		return false;
	}	
	
	public SetEditPart(ConnectionPart connectionPart, IModelChangeProvider modelChangeProvider) {
		super();
		setModel(connectionPart);
		this.modelChangeProvider = modelChangeProvider;
		// keep track of the MemberRole because if connectors are involved and
		// deleted, the reference to that MemberRole will be nullified in the
		// ConnectionPart
		this.memberRole = connectionPart.getMemberRole();
	}
	
	@Override
	public void addNotify() {
		super.addNotify();
		if (!isReadOnlyMode()) {
			modelChangeProvider.addModelChangeListener(this);
		}
	}

	@Override
	public void afterModelChange(ModelChangeContext context) {
		if (context.getModelChangeType() == ModelChangeType.ADD_BENDPOINT &&
			context.appliesTo(getModel())) {
					
			// a bendpoint was added to the connection part
			refreshVisuals();
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_BENDPOINT &&
				   context.appliesTo(getModel())) {
						
			// a bendpoint was removed from the connection part
			refreshVisuals();
		} else if (context.getModelChangeType() == ModelChangeType.MOVE_BENDPOINT &&
				   context.appliesTo(getModel())) {
			
			// a bendpoint was moved on the connection part
			refreshVisuals();
		} else if (context.getModelChangeType() == ModelChangeType.MOVE_ENDPOINT &&
				   context.appliesTo(getModel())) {
			
			// an endpoint was moved on the connection part
			refreshVisuals();
		} else if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY && 
				   context.isPropertySet(SchemaPackage.eINSTANCE.getSet_Name()) &&
				   (context.getCommandExecutionMode() != CommandExecutionMode.UNDO &&
				    Boolean.TRUE.equals(context.getListenerData())) ||
				    context.getCommandExecutionMode() == CommandExecutionMode.UNDO &&
				    context.appliesTo(getModel().getMemberRole().getSet())) {
			
			// the set name has changed (execute/undo/redo)
			refreshTooltip();						
		}
	}

	@Override
	public void beforeModelChange(ModelChangeContext context) {
		if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY && 
			context.isPropertySet(SchemaPackage.eINSTANCE.getSet_Name()) &&
			context.getCommandExecutionMode() != CommandExecutionMode.UNDO &&
			context.appliesTo(getModel().getMemberRole().getSet())) {
					
			// the model set's name is changing (execute/redo); put Boolean.TRUE in the context's 
			// listener's data so that we can respond to this when processing the after model change 
			// event
			context.setListenerData(Boolean.TRUE);
		}
	}
	
	@Override
	protected void createEditPolicies() {
		
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
						  new ConnectionEndpointEditPolicy());		
		
		if (isReadOnlyMode()) {
			return;
		}
		
		PolylineConnection connection = (PolylineConnection) getFigure();
		installEditPolicy(EditPolicy.LAYOUT_ROLE, 
						  new SetXYLayoutEditPolicy(getModel(), connection));
		
		// make sure we can remove a member record type from a set (or remove the set altogether if
		// it's not a multiple-memberset) by pressing the delete key on the line represented by this
		// edit part:
		installEditPolicy(EditPolicy.COMPONENT_ROLE, 
						  new RemoveMemberFromSetEditPolicy(getMemberRole(), true));			
		
		refreshBendpointEditPolicy();		
		
	}
	
	@Override
	protected IFigure createFigure() {
		
		PolylineConnection connection = new PolylineConnection();
		
		BendpointConnectionRouter router = new BendpointConnectionRouter();
		connection.setConnectionRouter(router);
		
		connection.setLineWidth(1);
		
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
		
		return connection;		
	}
	
	public MemberRole getMemberRole() {
		return memberRole;
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
		if (memberRole.getSet().getSystemOwner() != null) {
			// system owned set
			owner = memberRole.getSet().getSystemOwner();			 
		} else {
			// user owned set
			owner = memberRole.getSet().getOwner().getRecord();
		}
		DiagramLocation location = owner.getDiagramLocation();
		return new PrecisionPoint(location.getX(), location.getY());
	}

	@Override
	public EditPart getTargetEditPart(Request request) {
		
		// we need to do something special ONLY in the case when adding a member record type to a
		// (possibly already multiple-member) set...
		if (!(request instanceof CreateConnectionRequest) ||
			((CreateConnectionRequest) request).getNewObjectType() != IMultipleMemberSetPlaceHolder.class) {
							
			return super.getTargetEditPart(request);
		}
		
		// ... if the user has already selected a set, just have the default behaviour proceed
		CreateConnectionRequest cRequest = (CreateConnectionRequest) request;
		if (cRequest.getSourceEditPart() != null) {			
			return super.getTargetEditPart(request);			
		}
		
		// ...we want the line that is drawn, to start at the owner of the set, so 'redirect' to the
		// connection label edit part and have that take care of everything
		ConnectionLabel connectionLabel = memberRole.getConnectionLabel();
		SetDescriptionEditPart setDescriptionEditPart = 
			(SetDescriptionEditPart) getViewer().getEditPartRegistry().get(connectionLabel);
		Assert.isNotNull(setDescriptionEditPart, "no edit part for set description: " + 
						 memberRole.getSet().getName() + " (" + memberRole.getRecord(). getName() +
						 ")");
		return setDescriptionEditPart;
		
	}
	
	protected boolean isReadOnlyMode() {
		return modelChangeProvider == null;
	}
	
	private void refreshBendpointEditPolicy() {
		if (!isReadOnlyMode()) {
			installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new SetBendpointEditPolicy(this));
		}
	}
	
	private void refreshTooltip() {
		// set/refresh the tooltip, containing the set's name; this is helpful if the connection  
		// endpoints are not easy to locate...
        String adjustedSetName = 
        	Tools.removeTrailingUnderscore(getModel().getMemberRole().getSet().getName());
        Label tooltip = new Label(adjustedSetName);
        getConnectionFigure().setToolTip(tooltip);
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
		
		if (!tooltipSet) {
			// set the tooltip for the first time (it will be set again when the set name changes)
			refreshTooltip();
			tooltipSet = true;
		}
	}

	@Override
	public void removeNotify() {
		// note: this method is NOT invoked when the editor is closed (i.e. when the viewer is 		
		//disposed)
		if (!isReadOnlyMode()) {
			modelChangeProvider.removeModelChangeListener(this);
		}
		super.removeNotify();
	}	
	
}
