/**
 * Copyright (C) 2026  Luc Hermans
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

import static java.util.Comparator.comparing;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.DiagramNode;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.VsamIndex;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.command.CreateDiagramLabelCommand;
import org.lh.dmlj.schema.editor.command.CreateRecordCommand;
import org.lh.dmlj.schema.editor.command.ModelChangeCompoundCommand;
import org.lh.dmlj.schema.editor.command.MoveBendpointCommand;
import org.lh.dmlj.schema.editor.command.MoveDiagramNodeCommand;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.figure.DiagramLabelFigure;
import org.lh.dmlj.schema.editor.part.AbstractDiagramNodeEditPart;
import org.lh.dmlj.schema.editor.preference.PreferenceConstants;

public class SchemaXYLayoutEditPolicy extends XYLayoutEditPolicy {
	private Schema schema;
	private boolean readOnlyMode;
	
	public SchemaXYLayoutEditPolicy(Schema schema, boolean readOnlyMode) {
		this.schema = schema;
		this.readOnlyMode = readOnlyMode;
	}
	
	@Override
	protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child, Object constraint) {
		if (readOnlyMode) {
			return null;
		} else if (child.getModel() instanceof DiagramNode diagramNode) {
			// we're dealing with a DiagramNode, it can only be a move request, so create the move command...
			var delta = (Rectangle) constraint;
			var context = new ModelChangeContext(getModelChangeType(diagramNode));
			context.putContextData(diagramNode);
			if (diagramNode instanceof SchemaRecord schemaRecord) {
				return createChangeConstraintCommandForSchemaRecord(context, schemaRecord, delta);
			} else if (diagramNode instanceof Connector connector) {
				return createChangeConstraintCommandForConnector(context, connector, delta);
			} else {
				var command = new MoveDiagramNodeCommand(diagramNode, delta.x, delta.y);
				command.setContext(context);
				return command;
			}
		} else {
			// not a DiagramNode or user is trying to resize, make sure he/she gets the right feedback
			return null;
		}
	}
	
	private ModelChangeType getModelChangeType(DiagramNode diagramNode) {
		if (diagramNode instanceof ConnectionLabel) {
			return ModelChangeType.MOVE_SET_OR_INDEX_LABEL;
		} else if (diagramNode instanceof Connector) {
			return ModelChangeType.MOVE_CONNECTOR;
		} else if (diagramNode instanceof DiagramLabel) {
			return ModelChangeType.MOVE_DIAGRAM_LABEL;
		} else if (diagramNode instanceof SchemaRecord) {
			return ModelChangeType.MOVE_RECORD;
		} else if (diagramNode instanceof SystemOwner) {
			return ModelChangeType.MOVE_INDEX;
		} else if (diagramNode instanceof VsamIndex) {
			return ModelChangeType.MOVE_VSAM_INDEX;
		} else {
			throw new IllegalStateException("Unexpected diagram node: " + diagramNode);
		}
	}
	
	private Command createChangeConstraintCommandForSchemaRecord(ModelChangeContext context, SchemaRecord schemaRecord, Rectangle constraint) {
		var movingSchemaRecord = MovingDiagramNode.fromConstraint(schemaRecord, SchemaRecord.class, constraint);
				
		var moveBendpointCommandsForOwnerRecordNotInvolvingConnectors =
				getMoveBendpointCommandsForOwnerRolesNotInvolvingConnectors(movingSchemaRecord);
		var moveBendpointCommandsForMemberRecordNotInvolvingConnectors =
				getMoveBendpointCommandsForMemberRolesNotInvolvingConnectors(movingSchemaRecord);
		var moveBendpointCommandsForSetsInvolvingConnectors =
				getMoveBendpointCommandsForSetsInvolvingConnectors(movingSchemaRecord);
		
		var moveDiagramNodeCommand = new MoveDiagramNodeCommand(schemaRecord, constraint.x(), constraint.y());
		if (!moveBendpointCommandsForOwnerRecordNotInvolvingConnectors.isEmpty() ||
			!moveBendpointCommandsForMemberRecordNotInvolvingConnectors.isEmpty() ||
			!moveBendpointCommandsForSetsInvolvingConnectors.isEmpty()) {
			
			var compoundCommand = new ModelChangeCompoundCommand();
			compoundCommand.setContext(context);
			compoundCommand.add(moveDiagramNodeCommand);
			Stream.of(moveBendpointCommandsForOwnerRecordNotInvolvingConnectors,
					  moveBendpointCommandsForMemberRecordNotInvolvingConnectors,
					  moveBendpointCommandsForSetsInvolvingConnectors)
					.flatMap(Collection::stream)		
					.sorted(comparing(MoveBendpointCommandWrapper::setName, String.CASE_INSENSITIVE_ORDER)
							.thenComparing(comparing(MoveBendpointCommandWrapper::memberRecordName, String.CASE_INSENSITIVE_ORDER)))
					.map(MoveBendpointCommandWrapper::command)
					.forEach(compoundCommand::add);
			return compoundCommand;
		} else {
			moveDiagramNodeCommand.setContext(context);
			return moveDiagramNodeCommand;
		}
	}
	
	private List<MoveBendpointCommandWrapper> getMoveBendpointCommandsForOwnerRolesNotInvolvingConnectors(MovingDiagramNode<SchemaRecord> movingSchemaRecord) {
		return movingSchemaRecord.diagramNode().getOwnerRoles().stream()
				.map(OwnerRole::getSet)
				.map(Set::getMembers)
				.flatMap(List::stream)
				.filter(m -> m.getConnectionParts().size() == 1)
				.map(m -> m.getConnectionParts().get(0))
				.filter(c -> !c.getBendpointLocations().isEmpty())
				//.map(c -> createMoveBendpointCommand(c, b -> b.getX() - movingSchemaRecord.deltaX(), b -> b.getY() - movingSchemaRecord.deltaY()))
				.map(Bendpoint::lastOf)
				.map(bendpoint -> createMoveBendpointCommand(bendpoint, movingSchemaRecord))
				.flatMap(Optional::stream)
				.toList();
	}
	
	private List<MoveBendpointCommandWrapper> getMoveBendpointCommandsForMemberRolesNotInvolvingConnectors(MovingDiagramNode<SchemaRecord> movingSchemaRecord) {
		return movingSchemaRecord.diagramNode().getMemberRoles().stream()
				.filter(m -> m.getSet().getSystemOwner() == null)
				.filter(m -> m.getConnectionParts().size() == 1)
				.map(m -> m.getConnectionParts().get(0))
				.filter(c -> !c.getBendpointLocations().isEmpty())
				.map(Bendpoint::lastOf)
				.map(bendpoint -> createMoveBendpointCommand(bendpoint, movingSchemaRecord))
				.flatMap(Optional::stream)
				.toList();
	}
	
	private List<MoveBendpointCommandWrapper> getMoveBendpointCommandsForSetsInvolvingConnectors(MovingDiagramNode<SchemaRecord> movingSchemaRecord) {
		var ownerRoleBendpoints = movingSchemaRecord.diagramNode().getOwnerRoles().stream()
				.map(OwnerRole::getSet)
				.map(Set::getMembers)
				.flatMap(List::stream)
				.filter(m -> m.getConnectionParts().size() == 2)
				.map(MemberRole::getConnectionParts)
				.flatMap(List::stream)
				.filter(c -> !c.getBendpointLocations().isEmpty())
				.map(Bendpoint::lastOf)
				.map(bendpoint -> createMoveBendpointCommand(bendpoint, movingSchemaRecord))
				.flatMap(Optional::stream);
				
		var memberRoleBendpoints = movingSchemaRecord.diagramNode().getMemberRoles().stream()
				.filter(m -> m.getConnectionParts().size() == 2)
				.map(MemberRole::getConnectionParts)
				.flatMap(List::stream)
				.filter(c -> !c.getBendpointLocations().isEmpty())
				.map(Bendpoint::lastOf)
				.map(bendpoint -> createMoveBendpointCommand(bendpoint, movingSchemaRecord))
				.flatMap(Optional::stream);
		
		return Stream.concat(ownerRoleBendpoints, memberRoleBendpoints)
				.toList();
	}
	
	private Command createChangeConstraintCommandForConnector(ModelChangeContext context, Connector connector, Rectangle constraint) {
		var moveDiagramNodeCommand = new MoveDiagramNodeCommand(connector, constraint.x(), constraint.y());
		if (!connector.getConnectionPart().getBendpointLocations().isEmpty()) {
			var movingConnector = MovingDiagramNode.fromConstraint(connector, Connector.class, constraint);
			var bendpoint = Bendpoint.lastOf(connector.getConnectionPart());
			
			var moveBendpointCommandWrapper = createMoveBendpointCommand(bendpoint, movingConnector);
			if (moveBendpointCommandWrapper.isPresent()) {
				var compoundCommand = new ModelChangeCompoundCommand();
				compoundCommand.setContext(context);
				compoundCommand.add(moveDiagramNodeCommand);
				compoundCommand.add(moveBendpointCommandWrapper.orElseThrow().command());
				return compoundCommand;
			}			
		}
		moveDiagramNodeCommand.setContext(context);
		return moveDiagramNodeCommand;
	}
	
	private Optional<MoveBendpointCommandWrapper> createMoveBendpointCommand(Bendpoint bendpoint, MovingDiagramNode<?> movingDiagramNode) {
		var target = calculateTargetLocation(bendpoint, movingDiagramNode);
		if (target.getX() != bendpoint.diagramLocation().getX() || target.getY() != bendpoint.diagramLocation().getY()) {
			return Optional.of(new MoveBendpointCommandWrapper(bendpoint.getSetName(), bendpoint.getMemberRecordName(),
					new MoveBendpointCommand(bendpoint.connectionPart(), bendpoint.getIndex(), target.getX(), target.getY())));
		} else {
			return Optional.empty();
		}
	}
	
	private DiagramLocation calculateTargetLocation(Bendpoint bendpoint, MovingDiagramNode<?> movingDiagramNode) {
		var target = SchemaFactory.eINSTANCE.createDiagramLocation();
		target.setX(bendpoint.diagramLocation().getX());
		target.setY(bendpoint.diagramLocation().getY());
		if (movingDiagramNode.diagramNode() instanceof SchemaRecord) {
			if (bendpoint.connectionPart().getConnector() == null) {
				fixCoordinatesForBendpointWithoutConnectorForMovingRecord(target, bendpoint, movingDiagramNode.cast(SchemaRecord.class));
			} else {
				fixCoordinatesForBendpointWithConnectorForMovingRecord(target, bendpoint, movingDiagramNode.cast(SchemaRecord.class));
			}
		} else {
			fixCoordinatesForBendpointForMovingConnector(target, bendpoint, movingDiagramNode.cast(Connector.class));
		}
		return target;
	}
	
	private void fixCoordinatesForBendpointWithoutConnectorForMovingRecord(DiagramLocation target, Bendpoint bendpoint,
			MovingDiagramNode<SchemaRecord> movingSchemaRecord) {
		
		if (Tools.isAtOwnerSideOfSet(movingSchemaRecord.diagramNode(), bendpoint.connectionPart())) {
			compensateTransversally(target, bendpoint, movingSchemaRecord);
		} else {			
			followTransversally(target, bendpoint, movingSchemaRecord);
		}
	}
	
	private void fixCoordinatesForBendpointWithConnectorForMovingRecord(DiagramLocation target, Bendpoint bendpoint,
			MovingDiagramNode<SchemaRecord> movingSchemaRecord) {
		
		if (Tools.isAtOwnerSideOfSet(movingSchemaRecord.diagramNode(), bendpoint.connectionPart())) {
			if (bendpoint.isConnectorAtOwnerSide()) {
				compensateTransversally(target, bendpoint, movingSchemaRecord);
			} else if (bendpoint.isVerticalLineToTargetEndpointLocation() || bendpoint.isHorizontalLineToTargetEndpointLocation()) {
				compensateHorizontally(target, movingSchemaRecord);
				compensateVertically(target, movingSchemaRecord);
			}
		} else if (!bendpoint.isConnectorAtOwnerSide()) {
			followTransversally(target, bendpoint, movingSchemaRecord);
		}
	}
	
	private void fixCoordinatesForBendpointForMovingConnector(DiagramLocation target, Bendpoint bendpoint, MovingDiagramNode<Connector> movingConnector) {
		if (bendpoint.isConnectorAtOwnerSide()) {
			followTransversally(target, bendpoint, movingConnector);
		} else {
			followDirection(target, bendpoint, movingConnector);
		}
	}
	
	private void compensateTransversally(DiagramLocation target, Bendpoint bendpoint, MovingDiagramNode<?> movingDiagramNode) {
		if (bendpoint.isVerticalLineToTargetEndpointLocation()) {
			compensateHorizontally(target, movingDiagramNode);
		} else if (bendpoint.isHorizontalLineToTargetEndpointLocation()) {
			compensateVertically(target, movingDiagramNode);
		}
	}
	
	private void followTransversally(DiagramLocation target, Bendpoint bendpoint, MovingDiagramNode<?> movingDiagramNode) {
		if (bendpoint.isVerticalLineToTargetEndpointLocation()) {
			followHorizontally(target, movingDiagramNode);
		} else if (bendpoint.isHorizontalLineToTargetEndpointLocation()) {
			followVertically(target, movingDiagramNode);
		}
	}
	
	private void followDirection(DiagramLocation target, Bendpoint bendpoint, MovingDiagramNode<?> movingDiagramNode) {
		if (bendpoint.isVerticalLineToTargetEndpointLocation()) {
			followVertically(target, movingDiagramNode);
		} else if (bendpoint.isHorizontalLineToTargetEndpointLocation()) {
			followHorizontally(target, movingDiagramNode);
		}
	}
	
	private void compensateHorizontally(DiagramLocation target, MovingDiagramNode<?> movingDiagramNode) {
		target.setX(target.getX() - movingDiagramNode.deltaX());
	}
	
	private void compensateVertically(DiagramLocation target, MovingDiagramNode<?> movingDiagramNode) {
		target.setY(target.getY() - movingDiagramNode.deltaY());
	}
	
	private void followHorizontally(DiagramLocation target, MovingDiagramNode<?> movingDiagramNode) {
		target.setX(target.getX() + movingDiagramNode.deltaX());
	}
	
	private void followVertically(DiagramLocation target, MovingDiagramNode<?> movingDiagramNode) {
		target.setY(target.getY() + movingDiagramNode.deltaY());
	}
	
	@Override
	protected EditPolicy createChildEditPolicy(EditPart child) {		
		if (child instanceof AbstractDiagramNodeEditPart<?> diagramNodeEditPart) {
			// a diagram node edit part (resizable or not)
			return diagramNodeEditPart.getResizeEditPolicy();
		} else {
			// any other edit part
			return super.createChildEditPolicy(child);
		}
	}
	
	@Override
	protected Command getCreateCommand(CreateRequest request) {
		if (readOnlyMode ||
			request.getNewObjectType() != DiagramLabel.class && request.getNewObjectType() != SchemaRecord.class ||
			request.getNewObjectType() == DiagramLabel.class && schema.getDiagramData().getLabel() != null) {
			
			return null;
		}	
		
		if (request.getNewObjectType() == DiagramLabel.class) {
			var organisation = Plugin.getDefault().getPreferenceStore().getString(PreferenceConstants.DIAGRAMLABEL_ORGANISATION);
			String lastModified = null;
			if (Plugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.DIAGRAMLABEL_SHOW_LAST_MODIFIED)) {
				var pattern = Plugin.getDefault().getPreferenceStore().getString(PreferenceConstants.DIAGRAMLABEL_LAST_MODIFIED_DATE_FORMAT_PATTERN);
				var format = new SimpleDateFormat(pattern);
				lastModified = "Last modified: " + format.format(System.currentTimeMillis()) + " (not saved)";
			}
			var size = DiagramLabelFigure.getInitialSize(organisation, schema.getName(), schema.getVersion(),
					schema.getDescription(), lastModified); 
			var p = new PrecisionPoint(request.getLocation().x, request.getLocation().y); 				
			getHostFigure().translateToRelative(p);
			var context = new ModelChangeContext(ModelChangeType.ADD_DIAGRAM_LABEL);
			var command = new CreateDiagramLabelCommand(schema, p, size);
			command.setContext(context);
			return command;
		} else if (request.getNewObjectType() == SchemaRecord.class) {
			var p = new PrecisionPoint(request.getLocation().x, request.getLocation().y); 				
			getHostFigure().translateToRelative(p);
			var context = new ModelChangeContext(ModelChangeType.ADD_RECORD);
			var command = new CreateRecordCommand(schema, p);
			command.setContext(context);
			return command;
		}
		return null;
	}
	
	private static record MoveBendpointCommandWrapper(String setName, String memberRecordName, MoveBendpointCommand command) {
	}
	
	private static record MovingDiagramNode<T extends DiagramNode>(T diagramNode, Class<T> type, int deltaX, int deltaY) {
		
		private static <T extends DiagramNode> MovingDiagramNode<T> fromConstraint(T diagramNode, Class<T> diagramNodeType, Rectangle constraint) {
			var deltaX = constraint.x - diagramNode.getDiagramLocation().getX();
			var deltaY = constraint.y - diagramNode.getDiagramLocation().getY();
			return new MovingDiagramNode<>(diagramNode, diagramNodeType, deltaX, deltaY);
		}
		
		private <U extends DiagramNode> MovingDiagramNode<U> cast(Class<U> diagramNodeType) {
			return new MovingDiagramNode<>(diagramNodeType.cast(diagramNode), diagramNodeType, deltaX, deltaY);
		}
		
	}
	
}
