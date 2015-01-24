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
package org.lh.dmlj.schema.editor.policy;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.editor.anchor.ReconnectEndpointAnchor;
import org.lh.dmlj.schema.editor.command.AddMemberToSetCommand;
import org.lh.dmlj.schema.editor.command.CreateSetCommand;
import org.lh.dmlj.schema.editor.command.IModelChangeCommand;
import org.lh.dmlj.schema.editor.command.MoveEndpointCommand;
import org.lh.dmlj.schema.editor.command.infrastructure.IContextDataKeys;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.figure.RecordFigure;
import org.lh.dmlj.schema.editor.palette.IChainedSetPlaceHolder;
import org.lh.dmlj.schema.editor.palette.IIndexedSetPlaceHolder;
import org.lh.dmlj.schema.editor.part.SetEditPart;

/**
 * An edit policy that enables moving both connection end points and creating new sets or adding a
 * member record type to an existing set, making it a multiple-member set if not already.
 */
public class RecordGraphicalNodeEditPolicy extends GraphicalNodeEditPolicy {

	private RecordFigure   figure;
	private SchemaRecord   record;
	private EditPartViewer viewer;	
	
	public RecordGraphicalNodeEditPolicy(SchemaRecord record,
										 RecordFigure figure,
										 EditPartViewer viewer) {	
		
		super();		
		this.record = record;
		this.figure = figure;
		this.viewer = viewer;
	}
	
	@Override
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		// called when the user wants to add a chained or indexed set and is hoovering above a  
		// candidate member record type OR when he wants to add a member record type to an existing
		// set, making it a multiple-member set if not so already
		if (request.getStartCommand() instanceof CreateSetCommand) {
			// creation of new set in progress
			CreateSetCommand command = (CreateSetCommand) request.getStartCommand();
			if (record != command.getOwner()) {
				// the owner and member have to be different record types
				command.setMemberRecord(record);
				return command;
			}			
		} else if (request.getStartCommand() instanceof AddMemberToSetCommand) {
			// adding a member record type to an existing set 
			AddMemberToSetCommand command = (AddMemberToSetCommand)request.getStartCommand();
			Set set = command.getSet();
			if (set.getOwner().getRecord() == record) {
				// the record already participates in the given set as the owner record type
				return null;
			}
			for (MemberRole memberRole : set.getMembers()) {
				if (memberRole.getRecord() == record) {
					// the record already participates in the given set as a member record type
					return null;
				}
			}
			// the record is a valid candidate for participating in the set as an additional member
			// record type, provided the set is either not sorted, OR sorted AND at least 1 element
			// suitable for the sort key is defined in the record
			if (set.getOrder() != SetOrder.SORTED || 
				Tools.getDefaultSortKeyElement(record) != null) {
				
				// supply the member record type to the command since we couldn't do this at command
				// construction time
				command.setMemberRecord(record);
				// create the model change context and pass it to the command
				ModelChangeContext context = new ModelChangeContext(ModelChangeType.ADD_MEMBER_TO_SET);
				context.getContextData().put(IContextDataKeys.SET_NAME, set.getName());
				context.getContextData().put(IContextDataKeys.RECORD_NAME, record.getName());
				command.setContext(context);
				return command;
			}
		}
		return null;
	}

	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		// we should only instantiate a set creation command when we're adding either a chained or
		// indexed set
		if (request.getNewObjectType() != IChainedSetPlaceHolder.class &&
			request.getNewObjectType() != IIndexedSetPlaceHolder.class) {
			
			return null;
		}
		// if we get here, we're called when the user wants to add a chained or indexed set and is 
		// hoovering above a candidate owner record type
		if (!(request.getStartCommand() instanceof CreateSetCommand) ||
			((CreateSetCommand) request.getStartCommand()).getOwner() != record) {
			
			// avoid creating a new command over and over again for the same owner record
			SetMode mode = null;
			if (request.getNewObjectType() == IChainedSetPlaceHolder.class) {
				mode = SetMode.CHAINED;
			} else if (request.getNewObjectType() == IIndexedSetPlaceHolder.class) {
				mode = SetMode.INDEXED;
			} 
			Assert.isNotNull(mode, "cannot determine set mode");	
			ModelChangeContext context = new ModelChangeContext(ModelChangeType.ADD_USER_OWNED_SET);
			CreateSetCommand command = new CreateSetCommand(record, mode);
			command.setContext(context);
			request.setStartCommand(command);
		}
		return request.getStartCommand();
	}

	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		// do not allow to change the owner of the set; only the start
		// location can be changed; we currently don't support split
		// set connections (i.e. a set with 2 connection parts each with 
		// a connector attached)...
		if (!(request.getConnectionEditPart() instanceof SetEditPart)) {
			return null;
		}
		ConnectionPart connectionPart = 
			(ConnectionPart) request.getConnectionEditPart().getModel();
		List<ConnectionPart> connectionParts = 
			connectionPart.getMemberRole().getConnectionParts();
		if (connectionParts.size() > 1 && 
			connectionPart == connectionParts.get(1)) {
				
			// cannot set the source endpoint location on a connector 
			return null;
		}
		OwnerRole ownerRole = 
			connectionPart.getMemberRole().getSet().getOwner();
		if (ownerRole != null && ownerRole.getRecord() == record) {	
			Point reference;
			if (connectionPart.getBendpointLocations().isEmpty()) {
				DiagramLocation targetConnectionPoint = 
					connectionPart.getTargetEndpointLocation();
				if (targetConnectionPoint != null) {							
					reference = 
						new PrecisionPoint(targetConnectionPoint.getX(), 
										   targetConnectionPoint.getY());
				} else {
					SchemaRecord record =
						connectionPart.getMemberRole().getRecord();
					GraphicalEditPart editPart = 
						(GraphicalEditPart) viewer.getEditPartRegistry()
												  .get(record);
					reference = 
						editPart.getFigure().getBounds().getCenter();
					editPart.getFigure().translateToAbsolute(reference);
				}
			} else {
				int i = 
					connectionPart.getBendpointLocations().size() - 1;
				DiagramLocation lastBendpoint = 
					connectionPart.getBendpointLocations().get(i);
				reference = new PrecisionPoint(lastBendpoint.getX(), 
											   lastBendpoint.getY());
			}
			double zoomLevel = connectionPart.getMemberRole()
											 .getSet()
											 .getSchema()
											 .getDiagramData()
											 .getZoomLevel();
			Point location = 
				ReconnectEndpointAnchor.getRelativeLocation((RecordFigure)figure, 
														    request.getLocation(), 
														    zoomLevel);
			ModelChangeContext context = new ModelChangeContext(ModelChangeType.MOVE_ENDPOINT);
			context.putContextData(connectionPart);
			IModelChangeCommand command = 
				new MoveEndpointCommand(connectionPart, location.x, location.y, true);
			command.setContext(context);
			return (Command) command;
		} else {
			return null;
		}
	}

	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		// do not allow to change the member of the set; only the end
		// location can be changed
		if (!(request.getConnectionEditPart() instanceof SetEditPart)) {
			return null;
		}
		ConnectionPart connectionPart = 
			(ConnectionPart) request.getConnectionEditPart().getModel();
		List<ConnectionPart> connectionParts = 
			connectionPart.getMemberRole().getConnectionParts();
		if (connectionParts.size() > 1 && 
			connectionPart == connectionParts.get(0)) {
			
			// cannot set the target endpoint location on a connector 
			return null;
		}
		if (record == connectionPart.getMemberRole().getRecord()) {					
			double zoomLevel = connectionPart.getMemberRole()
											 .getSet()
											 .getSchema()
											 .getDiagramData()
											 .getZoomLevel();
			Point location = 
				ReconnectEndpointAnchor.getRelativeLocation((RecordFigure)figure, 
															request.getLocation(), 
															zoomLevel);
			ModelChangeContext context = new ModelChangeContext(ModelChangeType.MOVE_ENDPOINT);
			context.putContextData(connectionPart);
			IModelChangeCommand command = 
				new MoveEndpointCommand(connectionPart, location.x, location.y, false);
			command.setContext(context);
			return (Command) command;
		} else {
			return null;
		}
	}
	
	@Override
	protected Connection createDummyConnection(Request request) {
		if (!(request instanceof CreateConnectionRequest) ||
			((CreateConnectionRequest) request).getNewObjectType() != IChainedSetPlaceHolder.class &&
			((CreateConnectionRequest) request).getNewObjectType() != IIndexedSetPlaceHolder.class) {
			
			return super.createDummyConnection(request);
		}
		// we're adding a chained or (user-owned) indexed set; make sure the line that is shown
		// while looking for the member record type is fitted with an arrow at its target end
		PolylineConnection connection = new PolylineConnection();
		PolylineDecoration decoration = new PolylineDecoration();
		decoration.setTemplate(PolylineDecoration.TRIANGLE_TIP);
		connection.setTargetDecoration(decoration);
		return connection;
	}	
		
}
