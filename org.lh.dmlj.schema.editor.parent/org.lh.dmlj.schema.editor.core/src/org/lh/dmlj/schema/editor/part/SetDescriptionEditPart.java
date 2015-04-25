/**
 * Copyright (C) 2015  Luc Hermans
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
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaArea;
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
	public void afterModelChange(ModelChangeContext context) {		
		if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY &&			 
			context.isPropertySet(SchemaPackage.eINSTANCE.getSet_Name()) &&
			context.getCommandExecutionMode() == CommandExecutionMode.UNDO &&
			context.appliesTo(getModel().getMemberRole().getSet())) {
			
			// the set name change was undone; note that the context data will ALWAYS contain the
			// ORIGINAL set name (i.e. the one that was changed during the execute/redo); that's why 
			// we compare the set name in the context data to the actual value to determine if we 
			// need to refresh the visuals
			refreshVisuals();			
		} else if (context.getModelChangeType() == ModelChangeType.CHANGE_AREA_SPECIFICATION &&
				   getModel().getMemberRole().getSet().getSystemOwner() != null &&
				   context.appliesTo(getModel().getMemberRole().getSet())) {
			
			// the system owner's area specification has changed
			refreshVisuals();
		} else if (context.getModelChangeType() == ModelChangeType.CHANGE_SET_ORDER &&
				   context.appliesTo(getModel().getMemberRole().getSet())) {
			
			// the set order has changed
			refreshVisuals();
		} else if (context.getModelChangeType() == ModelChangeType.CHANGE_SORTKEYS &&
				   context.appliesTo(getModel().getMemberRole().getSet())) {
			
			// the sort key has changed
			refreshVisuals();
		} else if (context.getModelChangeType() == ModelChangeType.ADD_OR_REMOVE_SET_POINTERS &&
				   (context.appliesTo(getModel().getMemberRole()) ||
				    context.appliesTo(getModel().getMemberRole().getSet()))) {			
			
			// pointers were added or removed (note that the context can apply to either the member
			// role OR set)
			refreshVisuals();			
		} else if ((context.getModelChangeType() == ModelChangeType.MOVE_SET_OR_INDEX_LABEL ||
				    context.getModelChangeType() == ModelChangeType.MOVE_GROUP_OF_DIAGRAM_NODES) &&
				   context.appliesTo(getModel().getMemberRole())) {
	
			// the connection label was moved
			refreshVisuals();			
			refreshConnections();			
		} else if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY &&
				   context.isPropertySet(SchemaPackage.eINSTANCE.getMemberRole_MembershipOption()) &&
				   context.appliesTo(getModel().getMemberRole())) {
			
			// the membership option has changed
			refreshVisuals();
		} else if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY &&			 
				   context.isPropertySet(SchemaPackage.eINSTANCE.getSchemaArea_Name()) &&
				   getModel().getMemberRole().getSet().getSystemOwner() != null &&
				   context.appliesTo(getModel().getMemberRole()
						   					   .getSet()
						   					   .getSystemOwner()
						   					   .getAreaSpecification()
						   					   .getArea())) {
					
			// the system owner's containing area name change was undone
			refreshVisuals();
		} else if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY) {
			// the set name or the system owner's containing area name has changed (execute/redo)
			Boolean needToRefreshVisuals = (Boolean) context.getListenerData();
			if (needToRefreshVisuals != null && needToRefreshVisuals.equals(Boolean.TRUE)) {
				refreshVisuals();
			}		
		} else if (context.getModelChangeType() == ModelChangeType.CHANGE_AREA_SPECIFICATION) {
			// the containing area name was possibly renamed
			Boolean needToRefreshVisuals = (Boolean) context.getListenerData();
			if (needToRefreshVisuals != null && needToRefreshVisuals.equals(Boolean.TRUE)) {
				refreshVisuals();
			}
		}
	}
	
	@Override
	public void beforeModelChange(ModelChangeContext context) {
		SystemOwner systemOwner = getModel().getMemberRole().getSet().getSystemOwner();
		if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY &&			 
			context.isPropertySet(SchemaPackage.eINSTANCE.getSet_Name()) &&
			context.getCommandExecutionMode() != CommandExecutionMode.UNDO &&
			context.appliesTo(getModel().getMemberRole().getSet())) {
			
			// the set name is changing (execute/redo); put a boolean in the listener data, which we 
			// will pick up again when processing the after model change event
			context.setListenerData(Boolean.TRUE);
		} else if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY &&			 
				   context.isPropertySet(SchemaPackage.eINSTANCE.getSchemaArea_Name()) &&
				   systemOwner != null &&
				   context.getCommandExecutionMode() != CommandExecutionMode.UNDO &&
				   context.appliesTo(systemOwner.getAreaSpecification().getArea())) {
				
			// the system owner's containing area name is changing (execute/redo); put a boolean in 
			// the listener data, which we will pick up again when processing the after model change 
			// event
			context.setListenerData(Boolean.TRUE);			
		} else if (context.getModelChangeType() == ModelChangeType.CHANGE_AREA_SPECIFICATION &&
				   getModel().getMemberRole().getSet().getSystemOwner() != null &&
				   !context.appliesTo(getModel().getMemberRole().getSet())) {
			
			// it is possible that, together with changing an area specification, the area is 
			// being renamed as well; the record or system owned indexed set whose area 
			// specification is changed will be refreshed already, but we need to make sure that the 
			// new area name replaces the old one EVERYWHERE (we might unnecessarily do a refresh,  
			// but that's better than missing an area rename)
			SchemaArea modelArea = 
				getModel().getMemberRole().getSet().getSystemOwner().getAreaSpecification().getArea();
			if (context.getContextData().containsKey(IContextDataKeys.RECORD_NAME)) {
				String recordName = context.getContextData().get(IContextDataKeys.RECORD_NAME);
				SchemaRecord record = 
					getModel().getMemberRole().getSet().getSchema().getRecord(recordName);
				if (record.getAreaSpecification().getArea() == modelArea) {
					context.setListenerData(Boolean.TRUE);
				}
			} else if (context.getContextData().containsKey(IContextDataKeys.SET_NAME)) {
				String anotherSetName = context.getContextData().get(IContextDataKeys.SET_NAME);
				Set anotherSet = 
					getModel().getMemberRole().getSet().getSchema().getSet(anotherSetName);
				if (anotherSet.getSystemOwner().getAreaSpecification().getArea() == modelArea) {
					context.setListenerData(Boolean.TRUE);
				}
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
