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

import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.ReconnectRequest;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.StorageMode;
import org.lh.dmlj.schema.editor.anchor.ReconnectEndpointAnchor;
import org.lh.dmlj.schema.editor.anchor.RecordSourceAnchor;
import org.lh.dmlj.schema.editor.anchor.RecordTargetAnchor;
import org.lh.dmlj.schema.editor.command.infrastructure.CommandExecutionMode;
import org.lh.dmlj.schema.editor.command.infrastructure.ContextDataKeys;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.figure.RecordFigure;
import org.lh.dmlj.schema.editor.policy.RecordComponentEditPolicy;
import org.lh.dmlj.schema.editor.policy.RecordGraphicalNodeEditPolicy;
import org.lh.dmlj.schema.editor.policy.RecordXYLayoutEditPolicy;

public class RecordEditPart extends AbstractNonResizableDiagramNodeEditPart<SchemaRecord>  {
	
	public RecordEditPart(SchemaRecord schemaRecord, IModelChangeProvider modelChangeProvider) {
		super(schemaRecord, modelChangeProvider);		
	}
	
	@Override
	public void afterModelChange(ModelChangeContext context) {
		if (context.appliesTo(getModel())) {
			afterModelChangeWhenContextAppliesToModel(context);
		} else if (context.appliesTo(getModel().getAreaSpecification().getArea()) && context.getModelChangeType() == ModelChangeType.SET_PROPERTY &&
				   context.isPropertySet(SchemaPackage.eINSTANCE.getSchemaArea_Name()) && context.getCommandExecutionMode() == CommandExecutionMode.UNDO) {
								
			// the record's containing area name change was undone
			refreshVisuals();
		} else if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY) {
			// the record name or containing area name has changed (execute/redo)
			var needToRefreshVisuals = (Boolean) context.getListenerData();
			if (needToRefreshVisuals != null && needToRefreshVisuals.equals(Boolean.TRUE)) {
				refreshVisuals();
			}		
		} else if (context.getModelChangeType() == ModelChangeType.CHANGE_AREA_SPECIFICATION) {
			// the containing area name was possibly renamed
			var needToRefreshVisuals = (Boolean) context.getListenerData();
			if (needToRefreshVisuals != null && needToRefreshVisuals.equals(Boolean.TRUE)) {
				refreshVisuals();
			}
		}
	}
	
	private void afterModelChangeWhenContextAppliesToModel(ModelChangeContext context) {
		if (context.getModelChangeType() == ModelChangeType.CHANGE_AREA_SPECIFICATION ||
			context.getModelChangeType() == ModelChangeType.CHANGE_CALCKEY ||
			context.getModelChangeType() == ModelChangeType.CHANGE_LOCATION_MODE ||
			context.getModelChangeType() == ModelChangeType.CHANGE_VIA_SPECIFICATION ||
			context.getModelChangeType() == ModelChangeType.SET_PROPERTY && 
			(context.isPropertySet(SchemaPackage.eINSTANCE.getSchemaRecord_Name()) && context.getCommandExecutionMode() == CommandExecutionMode.UNDO ||
			 context.isPropertySet(SchemaPackage.eINSTANCE.getSchemaRecord_Id()) ||
			 context.isPropertySet(SchemaPackage.eINSTANCE.getSchemaRecord_StorageMode()))) {
			
			refreshVisuals();
		} else if (context.getModelChangeType() == ModelChangeType.MOVE_RECORD ||
				   context.getModelChangeType() == ModelChangeType.MOVE_GROUP_OF_DIAGRAM_NODES) {
			
			refreshVisuals();			
			refreshConnections();
		}
	}
	
	@Override
	public void beforeModelChange(ModelChangeContext context) {
		if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY && context.isPropertySet(SchemaPackage.eINSTANCE.getSchemaRecord_Name()) &&
			context.getCommandExecutionMode() != CommandExecutionMode.UNDO && context.appliesTo(getModel())) {
							
			// the record name is changing (execute/redo); put a boolean in the listener data, which we will pick
			// up again when processing the after model change event
			context.setListenerData(Boolean.TRUE);
		} else if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY && context.isPropertySet(SchemaPackage.eINSTANCE.getSchemaArea_Name()) &&
				   context.getCommandExecutionMode() != CommandExecutionMode.UNDO && context.appliesTo(getModel().getAreaSpecification().getArea())) {
			
			// the record's containing area name is changing (execute/redo); put a boolean in the listener data,
			// which we will pick up again when processing the after model change event
			context.setListenerData(Boolean.TRUE);
		} else if (context.getModelChangeType() == ModelChangeType.CHANGE_AREA_SPECIFICATION && !context.appliesTo(getModel())) {
			// it is possible that, together with changing an area specification, the area is being renamed as
			// well; the record or system owned indexed set whose area specification is changed will be refreshed
			// already, but we need to make sure that the new area name replaces the old one EVERYWHERE (we might
			// unnecessarily do a refresh, but that's better than missing an area rename)
			var modelArea = getModel().getAreaSpecification().getArea();
			if (context.getContextData().containsKey(ContextDataKeys.RECORD_NAME)) {
				var anotherRecordName = context.getContextData().get(ContextDataKeys.RECORD_NAME);
				var anotherRecord = getModel().getSchema().getRecord(anotherRecordName);
				if (anotherRecord.getAreaSpecification().getArea() == modelArea) {
					context.setListenerData(Boolean.TRUE);
				}
			} else if (context.getContextData().containsKey(ContextDataKeys.SET_NAME)) {
				var setName = context.getContextData().get(ContextDataKeys.SET_NAME);
				var set = getModel().getSchema().getSet(setName);
				if (set.getSystemOwner().getAreaSpecification().getArea() == modelArea) {
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
		
		var figure = (RecordFigure) getFigure();
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new RecordGraphicalNodeEditPolicy(getModel(), figure, getViewer()));
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new RecordXYLayoutEditPolicy(getModel()));
		// the next edit policy allows for the deletion of a record
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new RecordComponentEditPolicy());
	}
	
	@Override
	protected IFigure createFigure() {
		return new RecordFigure();
	}
	
	@Override
	protected List<ConnectionPart> getModelSourceConnections() {
		return getModel().getOwnerRoles().stream()
				.flatMap(ownerRole -> ownerRole.getSet().getMembers().stream())
				.map(memberRole -> memberRole.getConnectionParts().get(0))
				.toList();
	}
	
	@Override
	protected List<ConnectionPart> getModelTargetConnections() {
		return getModel().getMemberRoles().stream()
				.map(memberRole -> memberRole.getConnectionParts().get(memberRole.getConnectionParts().size() - 1))
				.toList();
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return new RecordSourceAnchor((RecordFigure) getFigure(), (ConnectionPart) connection.getModel());
	}
	
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return getSourceOrTargetConnectionAnchor(request);	
	}
	
	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return new RecordTargetAnchor((RecordFigure) getFigure(), (ConnectionPart) connection.getModel());
	}
	
	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return getSourceOrTargetConnectionAnchor(request);	
	}
	
	private ConnectionAnchor getSourceOrTargetConnectionAnchor(Request request) {
		if (!(request instanceof ReconnectRequest)) {
			return super.getSourceConnectionAnchor(request);
		}
		var rRequest = (ReconnectRequest) request;		
		var connectionPart = (ConnectionPart)rRequest.getConnectionEditPart().getModel();
		return new ReconnectEndpointAnchor((RecordFigure) getFigure(), rRequest.getLocation(), connectionPart);	
	}
	
	@Override
	protected void refreshConnections() {
		getModel().getOwnerRoles().stream()
				.flatMap(ownerRole -> ownerRole.getSet().getMembers().stream())
				.flatMap(memberRole -> memberRole.getConnectionParts().stream())
				.map(connectionPart -> getViewer().getEditPartRegistry().get(connectionPart))
				.map(EditPart.class::cast)
				.forEach(EditPart::refresh);
	}

	@Override
	protected void setFigureData() {
		var schemaRecord = getModel();
		var figure = (RecordFigure) getFigure();
		// we need to manipulate the record name in the case of some dictionary records (DDLCATLOD area, which
		// has the same structure as DDLDCLOD)...
		var adjustedRecordName = Tools.removeTrailingUnderscore(schemaRecord.getName());
		figure.setRecordName(adjustedRecordName);
		figure.setRecordId(schemaRecord.getId());
		figure.setStorageMode(Tools.getStorageMode(schemaRecord.getStorageMode()));
		int dataLength = schemaRecord.getDataLength();
		if (schemaRecord.getStorageMode() != StorageMode.FIXED) {
			dataLength -= 4; 
		}
		figure.setRecordLength(dataLength);
		figure.setLocationMode(schemaRecord.getLocationMode().toString().replace("_", " "));
		if (schemaRecord.getLocationMode() == LocationMode.CALC || schemaRecord.getLocationMode() == LocationMode.VSAM_CALC) {
			var calcKey = Tools.getCalcKey(schemaRecord.getCalcKey());
			figure.setLocationModeDetails(calcKey);
			String duplicatesOption = Tools.getDuplicatesOption(schemaRecord.getCalcKey());
			figure.setDuplicatesOption(duplicatesOption);
		} else if (schemaRecord.getLocationMode() == LocationMode.VIA) {
			String setName;
			// the via specification will be null if we are changing the VIA set
			if (schemaRecord.getViaSpecification() != null) {
				if (schemaRecord.getViaSpecification().getSet().getName().endsWith("_")) {
					// we need to manipulate the via set name in the case of some dictionary records (DDLCATLOD
					// area, which has the same structure as DDLDCLOD)...
					setName = schemaRecord.getViaSpecification().getSet().getName()
							.substring(0, schemaRecord.getViaSpecification().getSet().getName().length() - 1);
				} else {
					setName = schemaRecord.getViaSpecification().getSet().getName();
				}
			} else {
				setName = "?";
			}
			figure.setLocationModeDetails(setName);
			figure.setDuplicatesOption("");
		} else {
			figure.setLocationModeDetails("");
			figure.setDuplicatesOption("");
		}
		figure.setAreaName(schemaRecord.getAreaSpecification().getArea().getName());
	}		
	
}
