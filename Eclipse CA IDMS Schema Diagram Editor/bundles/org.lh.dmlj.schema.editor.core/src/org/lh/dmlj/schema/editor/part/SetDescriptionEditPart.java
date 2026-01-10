/**
 * Copyright (C) 2025  Luc Hermans
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
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.swt.graphics.Color;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.command.infrastructure.CommandExecutionMode;
import org.lh.dmlj.schema.editor.command.infrastructure.ContextDataKeys;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.figure.ConnectorFigure;
import org.lh.dmlj.schema.editor.figure.SetDescriptionFigure;
import org.lh.dmlj.schema.editor.palette.IMultipleMemberSetPlaceHolder;
import org.lh.dmlj.schema.editor.policy.SetDescriptionComponentEditPolicy;
import org.lh.dmlj.schema.editor.policy.SetDescriptionGraphicalNodeEditPolicy;

public class SetDescriptionEditPart extends AbstractNonResizableDiagramNodeEditPart<ConnectionLabel>  {
	private Color foregroundColorToRestore;
	
	public SetDescriptionEditPart(ConnectionLabel connectionLabel, IModelChangeProvider modelChangeProvider) {
		super(connectionLabel, modelChangeProvider);		
	}
	
	@Override
	public void afterModelChange(ModelChangeContext context) {
		if (needToRefreshVisuals(context)) {
			refreshVisuals();
		}
		if (isConnectionLabelMoved(context)) {
			refreshConnections(); // note: this doesn't do anything for the moment
		}
	}
	
	private boolean needToRefreshVisuals(ModelChangeContext context) {
		return isUndoOfSetNameChange(context) || isSystemOwnerAreaSpecificationChanged(context) || isSetOrderChanged(context) ||
			   isSortKeyChanged(context) || arePointersAddedOrRemoved(context) || isConnectionLabelMoved(context) ||
			   isMembershipOptionChanged(context) || isSetNameOrSystemOwnerAreaNameChange(context) || isSetNameOrSystemOwnerAreaNameChange(context) ||
			   isUndoOfSstemOwnerAreaNameChange(context) || isAreaNameRenamed(context) || isMemberRecordStructureChanged(context);
	}
	
	private boolean isUndoOfSetNameChange(ModelChangeContext context) {
		// when a set name change is undone, the context data will ALWAYS contain the ORIGINAL set name (i.e. the
		// one that was changed during the execute/redo); that's why we compare the set name in the context data
		// to the actual value to determine if we need to refresh the visuals
		return context.getCommandExecutionMode() == CommandExecutionMode.UNDO && context.getModelChangeType() == ModelChangeType.SET_PROPERTY &&
			   context.isPropertySet(SchemaPackage.eINSTANCE.getSet_Name()) && context.appliesTo(getModel().getMemberRole().getSet());
	}
	
	private boolean isSystemOwnerAreaSpecificationChanged(ModelChangeContext context) {
		return context.getModelChangeType() == ModelChangeType.CHANGE_AREA_SPECIFICATION && getModel().getMemberRole().getSet().getSystemOwner() != null &&
			   context.appliesTo(getModel().getMemberRole().getSet());
	}
	
	private boolean isSetOrderChanged(ModelChangeContext context) {
		return context.getModelChangeType() == ModelChangeType.CHANGE_SET_ORDER && context.appliesTo(getModel().getMemberRole().getSet());
	}
	
	private boolean isSortKeyChanged(ModelChangeContext context) {
		return context.getModelChangeType() == ModelChangeType.CHANGE_SORTKEYS && context.appliesTo(getModel().getMemberRole().getSet());
	}
	
	private boolean arePointersAddedOrRemoved(ModelChangeContext context) {
		// note that when pointers were added or removed, that the context can apply to either the member role OR the set
		return context.getModelChangeType() == ModelChangeType.ADD_OR_REMOVE_SET_POINTERS &&
			   (context.appliesTo(getModel().getMemberRole()) || context.appliesTo(getModel().getMemberRole().getSet()));
	}
	
	private boolean isConnectionLabelMoved(ModelChangeContext context) {
		return (context.getModelChangeType() == ModelChangeType.MOVE_SET_OR_INDEX_LABEL || context.getModelChangeType() == ModelChangeType.MOVE_GROUP_OF_DIAGRAM_NODES) &&
				context.appliesTo(getModel().getMemberRole());
	}
	
	private boolean isMembershipOptionChanged(ModelChangeContext context) {
		return context.getModelChangeType() == ModelChangeType.SET_PROPERTY && context.isPropertySet(SchemaPackage.eINSTANCE.getMemberRole_MembershipOption()) &&
			   context.appliesTo(getModel().getMemberRole());
	}
	
	private boolean isSetNameOrSystemOwnerAreaNameChange(ModelChangeContext context) {
		return context.getModelChangeType() == ModelChangeType.SET_PROPERTY && Boolean.TRUE.equals(context.getListenerData());
	}
	
	private boolean isUndoOfSstemOwnerAreaNameChange(ModelChangeContext context) {
		return context.getModelChangeType() == ModelChangeType.SET_PROPERTY && context.isPropertySet(SchemaPackage.eINSTANCE.getSchemaArea_Name()) &&
			   getModel().getMemberRole().getSet().getSystemOwner() != null &&
			   context.appliesTo(getModel().getMemberRole().getSet().getSystemOwner().getAreaSpecification().getArea());		
	}
	
	private boolean isAreaNameRenamed(ModelChangeContext context) {
		return context.getModelChangeType() == ModelChangeType.CHANGE_AREA_SPECIFICATION && Boolean.TRUE.equals(context.getListenerData());
	}
	
	private boolean isMemberRecordStructureChanged(ModelChangeContext context) {
		return context.getModelChangeType() == ModelChangeType.SWAP_RECORD_ELEMENTS && context.appliesTo(getModel().getMemberRole().getRecord());
	}
	
	@Override
	public void beforeModelChange(ModelChangeContext context) {
		var systemOwner = getModel().getMemberRole().getSet().getSystemOwner();
		if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY && context.isPropertySet(SchemaPackage.eINSTANCE.getSet_Name()) &&
			context.getCommandExecutionMode() != CommandExecutionMode.UNDO && context.appliesTo(getModel().getMemberRole().getSet())) {
			
			// the set name is changing (execute/redo); put a boolean in the listener data, which we will pick up
			// again when processing the after model change event
			context.setListenerData(Boolean.TRUE);
		} else if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY && context.isPropertySet(SchemaPackage.eINSTANCE.getSchemaArea_Name()) && systemOwner != null &&
				   context.getCommandExecutionMode() != CommandExecutionMode.UNDO && context.appliesTo(systemOwner.getAreaSpecification().getArea())) {
				
			// the system owner's containing area name is changing (execute/redo); put a boolean in the listener
			// data, which we will pick up again when processing the after model change event
			context.setListenerData(Boolean.TRUE);			
		} else if (context.getModelChangeType() == ModelChangeType.CHANGE_AREA_SPECIFICATION && getModel().getMemberRole().getSet().getSystemOwner() != null &&
				   !context.appliesTo(getModel().getMemberRole().getSet())) {
			
			// it is possible that, together with changing an area specification, the area is being renamed as
			// well; the record or system owned indexed set whose area specification is changed will be refreshed
			// already, but we need to make sure that the new area name replaces the old one EVERYWHERE (we might
			// unnecessarily do a refresh, but that's better than missing an area rename)
			var modelArea = getModel().getMemberRole().getSet().getSystemOwner().getAreaSpecification().getArea();
			if (context.getContextData().containsKey(ContextDataKeys.RECORD_NAME)) {
				var recordName = context.getContextData().get(ContextDataKeys.RECORD_NAME);
				var schemaRecord = getModel().getMemberRole().getSet().getSchema().getRecord(recordName);
				if (schemaRecord.getAreaSpecification().getArea() == modelArea) {
					context.setListenerData(Boolean.TRUE);
				}
			} else if (context.getContextData().containsKey(ContextDataKeys.SET_NAME)) {
				var anotherSetName = context.getContextData().get(ContextDataKeys.SET_NAME);
				var anotherSet = getModel().getMemberRole().getSet().getSchema().getSet(anotherSetName);
				if (anotherSet.getSystemOwner().getAreaSpecification().getArea() == modelArea) {
					context.setListenerData(Boolean.TRUE);
				}
			}
		}
	}
	
	@Override
	protected void createEditPolicies() {
		if (isReadOnlyMode()) {
			return;
		}
		
		// make sure we can delete a set by pressing the delete key on the line represented by this edit part:
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new SetDescriptionComponentEditPolicy());
		
		// make sure we can add member record types to existing sets - the palette tool to accomplish this can be
		// used in conjunction with either this edit part, the connection part (set) edit part(s) or the connector
		// edit parts (if present); ALL logic to handle this is handled with this type of edit part, so the other
		// edit parts should set this edit part as the target for those create connection requests
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new SetDescriptionGraphicalNodeEditPolicy(getModel()));
	}

	@Override
	protected IFigure createFigure() {
		return new SetDescriptionFigure();
	}
	
	@Override
	public void eraseSourceFeedback(Request request) {
		if (request instanceof ChangeBoundsRequest) {
			// Change the line color of the connection parts and connectors to which this label belongs back to black.
			for (var connectionPart : getModel().getMemberRole().getConnectionParts()) {
				var setEditPart = (SetEditPart) getViewer().getEditPartRegistry().get(connectionPart);
				var connection = (PolylineConnection) setEditPart.getFigure();
				connection.setLineWidth(1);
				connection.setForegroundColor(foregroundColorToRestore);
				
				var connector = connectionPart.getConnector();
				if (connector != null) {
					var connectorEditPart = (ConnectorEditPart) getViewer().getEditPartRegistry().get(connector);
					((ConnectorFigure) connectorEditPart.getFigure()).setLineWidth(1);
					connectorEditPart.getFigure().setForegroundColor(foregroundColorToRestore);
				}
			}
		}
		super.eraseSourceFeedback(request);
	}
	
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		// only manipulate the source connection anchor when adding a member record type to an existing set...
		if (!(request instanceof CreateConnectionRequest createConnectionRequest) || createConnectionRequest.getNewObjectType() != IMultipleMemberSetPlaceHolder.class) {
			return super.getSourceConnectionAnchor(request);
		}		
		// ...we want the line that is drawn, to start at the owner of the set:
		var ownerRecord = getModel().getMemberRole().getSet().getOwner().getRecord();
		var ownerRecordEditPart = (RecordEditPart) getViewer().getEditPartRegistry().get(ownerRecord);
		Assert.isNotNull(ownerRecordEditPart, "no edit part for record " + ownerRecord.getName());
		return ownerRecordEditPart.getSourceConnectionAnchor(request);
	}
	
	@Override
	protected void setFigureData() {
		var memberRole = getModel().getMemberRole();
		
		var figure = (SetDescriptionFigure) getFigure();
		
		// we need to manipulate the set name in the case of some dictionary sets (DDLCATLOD area, which has the
		// same structure as DDLDCLOD)...
		var adjustedSetName = Tools.removeTrailingUnderscore(memberRole.getSet().getName());
		figure.setName(adjustedSetName);
		
		figure.setPointers(Tools.getPointers(memberRole));
		figure.setMembershipOption(Tools.getMembershipOption(memberRole));
		figure.setOrder(memberRole.getSet().getOrder().toString());
		
		figure.setSortKeys(Tools.getSortKeys(memberRole));		
		
		figure.setSystemOwnerArea(Tools.getSystemOwnerArea(memberRole));
	}

	@Override
	public void showSourceFeedback(Request request) {		
		if (request instanceof ChangeBoundsRequest) {
			// Change the line color of the connection parts and connectors to which this label belongs to red so
			// that the user can see to which connection parts the label belongs.
			for (var connectionPart : getModel().getMemberRole().getConnectionParts()) {
				var setEditPart = (SetEditPart) getViewer().getEditPartRegistry().get(connectionPart);
				var connection = (PolylineConnection) setEditPart.getFigure();
				connection.setLineWidth(2);
				if (foregroundColorToRestore == null) {
					foregroundColorToRestore = connection.getForegroundColor();
				}
				connection.setForegroundColor(ColorConstants.red);
				
				var connector = connectionPart.getConnector();
				if (connector != null) {
					var connectorEditPart = (ConnectorEditPart) getViewer().getEditPartRegistry().get(connector);
					((ConnectorFigure) connectorEditPart.getFigure()).setLineWidth(2);
					connectorEditPart.getFigure().setForegroundColor(ColorConstants.red);
				}
			}
		}
		super.showSourceFeedback(request);
	}

}
