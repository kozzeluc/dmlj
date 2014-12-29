/**
 * Copyright (C) 2014  Luc Hermans
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

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.command.infrastructure.CommandExecutionMode;
import org.lh.dmlj.schema.editor.command.infrastructure.IContextDataKeys;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.figure.ConnectorFigure;
import org.lh.dmlj.schema.editor.figure.SetDescriptionFigure;
import org.lh.dmlj.schema.editor.palette.IMultipleMemberSetPlaceHolder;
import org.lh.dmlj.schema.editor.policy.SetDescriptionComponentEditPolicy;
import org.lh.dmlj.schema.editor.policy.SetDescriptionGraphicalNodeEditPolicy;

public class SetDescriptionEditPart 
    extends AbstractNonResizableDiagramNodeEditPart<ConnectionLabel>  {

	private SetDescriptionEditPart() {
		super(null, null); // disabled constructor
	}
	
	public SetDescriptionEditPart(ConnectionLabel connectionLabel, 
								  IModelChangeProvider modelChangeProvider) {
		super(connectionLabel, modelChangeProvider);		
	}
	
	@Override
	public void afterAddItem(EObject owner, EReference reference, Object item) {
	}

	@Override
	public void afterModelChange(ModelChangeContext context) {		
		SystemOwner systemOwner = getModel().getMemberRole().getSet().getSystemOwner();
		if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY &&			 
			context.isFeatureSet(SchemaPackage.eINSTANCE.getSet_Name()) &&
			context.getCommandExecutionMode() == CommandExecutionMode.UNDO) {
			
			String setName = context.getContextData().get(IContextDataKeys.SET_NAME);
			if (setName.equals(getModel().getMemberRole().getSet().getName())) {
				// the set name change was undone; note that the context data will ALWAYS contain
				// the ORIGINAL set name (i.e. the one that was changed during the execute/redo); 
				// that's why we compare the set name in the context data to the actual value to
				// determine if we need to refresh the visuals
				refreshVisuals();
			}
		} else if (context.getModelChangeType() == ModelChangeType.CHANGE_AREA_SPECIFICATION) {
			String setName = context.getContextData().get(IContextDataKeys.SET_NAME);
			if (setName != null &&
				setName.equals(getModel().getMemberRole().getSet().getName())) {
				
				refreshVisuals();
			}
		} else if (context.getModelChangeType() == ModelChangeType.CHANGE_SET_ORDER) {
			String setName = context.getContextData().get(IContextDataKeys.SET_NAME);
			if (setName.equals(getModel().getMemberRole().getSet().getName())) {
				refreshVisuals();
			}
		} else if (context.getModelChangeType() == ModelChangeType.CHANGE_SORTKEYS) {
			String setName = context.getContextData().get(IContextDataKeys.SET_NAME);
			if (setName.equals(getModel().getMemberRole().getSet().getName())) {
				refreshVisuals();
			}
		} else if (context.getModelChangeType() == ModelChangeType.ADD_OR_REMOVE_SET_POINTERS) {			
			String setName = context.getContextData().get(IContextDataKeys.SET_NAME);
			String recordName =	// when null, the set pointer change applies to ALL set members 
				context.getContextData().get(IContextDataKeys.RECORD_NAME);
			if (setName.equals(getModel().getMemberRole().getSet().getName()) &&
				(recordName == null || 
				 recordName.equals(getModel().getMemberRole().getRecord().getName()))) {
				
				refreshVisuals();
			}			
		} else if (context.getModelChangeType() == ModelChangeType.MOVE_SET_OR_INDEX_LABEL) {
			String setName = context.getContextData().get(IContextDataKeys.SET_NAME);
			String recordName = context.getContextData().get(IContextDataKeys.RECORD_NAME);
			if (setName.equals(getModel().getMemberRole().getSet().getName()) &&
				recordName.equals(getModel().getMemberRole().getRecord().getName())) {
				
				refreshVisuals();			
				refreshConnections();
			}
		} else if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY &&
				   context.isFeatureSet(SchemaPackage.eINSTANCE.getMemberRole_MembershipOption())) {
			
			String setName = context.getContextData().get(IContextDataKeys.SET_NAME);
			String recordName = context.getContextData().get(IContextDataKeys.RECORD_NAME);
			if (setName.equals(getModel().getMemberRole().getSet().getName()) &&
				recordName.equals(getModel().getMemberRole().getRecord().getName())) {
				
				// the membership option has changed
				refreshVisuals();
			}
		} else if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY &&			 
				   context.isFeatureSet(SchemaPackage.eINSTANCE.getSchemaArea_Name()) &&
				   systemOwner != null &&
				   context.getCommandExecutionMode() == CommandExecutionMode.UNDO) {
					
			String areaName = context.getContextData().get(IContextDataKeys.AREA_NAME);
			if (areaName.equals(systemOwner.getAreaSpecification().getArea().getName())) {
				// the system owner's containing area name change was undone
				refreshVisuals();
			}
		} else if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY) {
			Boolean needToRefreshVisuals = (Boolean) context.getListenerData();
			if (needToRefreshVisuals != null && needToRefreshVisuals.equals(Boolean.TRUE)) {
				refreshVisuals();
			}		
		}
	}
	
	@Override
	public void afterMoveItem(EObject oldOwner, EReference reference, Object item, EObject newOwner) {
	}

	@Override
	public void afterRemoveItem(EObject owner, EReference reference, Object item) {		
	}	
	
	@Override
	public void afterSetFeatures(EObject owner, EStructuralFeature[] features) {		
	}
	
	@Override
	public void beforeModelChange(ModelChangeContext context) {
		SystemOwner systemOwner = getModel().getMemberRole().getSet().getSystemOwner();
		if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY &&			 
			context.isFeatureSet(SchemaPackage.eINSTANCE.getSet_Name()) &&
			context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {
			
			String setName = context.getContextData().get(IContextDataKeys.SET_NAME);
			if (setName.equals(getModel().getMemberRole().getSet().getName())) {
				// the set name is changing (execute/redo); put a boolean in the listener data, 
				// which we will pick up again when processing the after model change event
				context.setListenerData(Boolean.TRUE);
			}
		} else if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY &&			 
				   context.isFeatureSet(SchemaPackage.eINSTANCE.getSchemaArea_Name()) &&
				   systemOwner != null &&
				   context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {
				
			String areaName = context.getContextData().get(IContextDataKeys.AREA_NAME);
			if (areaName.equals(systemOwner.getAreaSpecification().getArea().getName())) {
				// the system owner's containing area name is changing; put a boolean in the 
				// listener data, which we will pick up again when processing the after model change 
				// event
				context.setListenerData(Boolean.TRUE);
			}			
		}
	}
	
	@Override
	protected void createEditPolicies() {
		
		// make sure we can delete a set by pressing the delete key on the line represented by this
		// edit part:
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new SetDescriptionComponentEditPolicy());
		
		// make sure we can add member record types to existing sets - the palette tool to 
		// accomplish this can be used in conjunction with either this edit part, the connection
		// part (set) edit part(s) or the connector edit parts (if present); ALL logic to handle 
		// this is handled with this type of edit part, so the other edit parts should set this edit
		// part as the target for those create connection requests
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, 
				  		  new SetDescriptionGraphicalNodeEditPolicy(getModel()));		
		
	}

	@Override
	protected IFigure createFigure() {
		Figure figure = new SetDescriptionFigure();	
		
		// add a tooltip containing the set's name...
        String adjustedSetName;
        Set set = getModel().getMemberRole().getSet();
        if (set.getName().endsWith("_")) {
            StringBuilder p = new StringBuilder(set.getName());
            p.setLength(p.length() - 1);
            adjustedSetName = p.toString();
        } else {
            adjustedSetName = set.getName();
        }
        Label tooltip = new Label(adjustedSetName);
        figure.setToolTip(tooltip);
        
        return figure;
	}
	
	@Override
	public void eraseSourceFeedback(Request request) {
		if (request instanceof ChangeBoundsRequest) {
			// Change the line color of the connection parts and connectors to 
			// which this label belongs back to black.
			for (ConnectionPart connectionPart : 
				 getModel().getMemberRole().getConnectionParts()) {
				
				SetEditPart setEditPart = 
					(SetEditPart) getViewer().getEditPartRegistry()
											 .get(connectionPart);
				PolylineConnection connection = 
					(PolylineConnection) setEditPart.getFigure();
				connection.setLineWidth(1);
				connection.setForegroundColor(ColorConstants.black);
				
				Connector connector = connectionPart.getConnector();
				if (connector != null) {
					ConnectorEditPart connectorEditPart = 
						(ConnectorEditPart) getViewer().getEditPartRegistry()
													   .get(connector);
					((ConnectorFigure)connectorEditPart.getFigure()).setLineWidth(1);
					connectorEditPart.getFigure()
								     .setForegroundColor(ColorConstants.black);
				}
			}
		}
		super.eraseSourceFeedback(request);
	}
	
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		// only manipulate the source connection anchor when adding a member record type to an
		// existing set...
		if (!(request instanceof CreateConnectionRequest) ||
			((CreateConnectionRequest) request).getNewObjectType() != IMultipleMemberSetPlaceHolder.class) {
						
			return super.getSourceConnectionAnchor(request);
		}		
		// ...we want the line that is drawn, to start at the owner of the set:
		SchemaRecord ownerRecord = getModel().getMemberRole().getSet().getOwner().getRecord();
		RecordEditPart ownerRecordEditPart = 
			(RecordEditPart) getViewer().getEditPartRegistry().get(ownerRecord);
		Assert.isNotNull(ownerRecordEditPart, "no edit part for record " + ownerRecord.getName());
		return ownerRecordEditPart.getSourceConnectionAnchor(request);
	}
	
	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return super.getTargetConnectionAnchor(request);
	}
	
	@Override
	protected void setFigureData() {
		
		MemberRole memberRole = getModel().getMemberRole();
		
		SetDescriptionFigure figure = (SetDescriptionFigure) getFigure();
		
		// we need to manipulate the set name in the case of some dictionary
		// sets (DDLCATLOD area, which has the same structure as DDLDCLOD)...
		StringBuilder setName = 
			new StringBuilder(memberRole.getSet().getName());
		if (setName.toString().endsWith("_")) {
			setName.setLength(setName.length() - 1);
		}
		figure.setName(setName.toString());
		
		figure.setPointers(Tools.getPointers(memberRole));
		figure.setMembershipOption(Tools.getMembershipOption(memberRole));
		figure.setOrder(memberRole.getSet().getOrder().toString());
		
		figure.setSortKeys(Tools.getSortKeys(memberRole));		
		
		figure.setSystemOwnerArea(Tools.getSystemOwnerArea(memberRole));
		
	}

	@Override
	public void showSourceFeedback(Request request) {		
		if (request instanceof ChangeBoundsRequest) {
			// Change the line color of the connection parts and connectors to 
			// which this label belongs to red so that the user can see to which 
			// connection parts the label belongs.
			for (ConnectionPart connectionPart : 
				 getModel().getMemberRole().getConnectionParts()) {
				
				SetEditPart setEditPart = 
					(SetEditPart) getViewer().getEditPartRegistry()
											 .get(connectionPart);
				PolylineConnection connection = 
					(PolylineConnection) setEditPart.getFigure();
				connection.setLineWidth(2);
				connection.setForegroundColor(ColorConstants.red);
				
				Connector connector = connectionPart.getConnector();
				if (connector != null) {
					ConnectorEditPart connectorEditPart = 
						(ConnectorEditPart) getViewer().getEditPartRegistry()
													   .get(connector);
					((ConnectorFigure)connectorEditPart.getFigure()).setLineWidth(2);
					connectorEditPart.getFigure()
								     .setForegroundColor(ColorConstants.red);
				}
			}
		}
		super.showSourceFeedback(request);
	}

}
