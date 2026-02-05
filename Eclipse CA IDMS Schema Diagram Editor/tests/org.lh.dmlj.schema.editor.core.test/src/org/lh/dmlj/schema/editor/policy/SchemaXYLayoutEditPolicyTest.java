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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Random;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramNode;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.VsamIndex;
import org.lh.dmlj.schema.editor.command.ModelChangeBasicCommand;
import org.lh.dmlj.schema.editor.command.ModelChangeCompoundCommand;
import org.lh.dmlj.schema.editor.command.MoveBendpointCommand;
import org.lh.dmlj.schema.editor.command.MoveDiagramNodeCommand;
import org.lh.dmlj.schema.editor.command.infrastructure.ContextDataKeys;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.common.Pair;
import org.lh.dmlj.schema.editor.testtool.TestTools;

public class SchemaXYLayoutEditPolicyTest {
	private static Schema schema;
	private static record Delta(int x, int y) {
		private static final Random r = new Random();
	
		private static Delta generate() {
			var x = r.nextInt(49) + 1;
			int y;
			do {
				y = r.nextInt(49) + 1;
			} while (y == x);
			return new Delta(x, y);
		}
		
	}
	
	private enum Direction { LEFT, RIGHT, UP, DOWN, LEFT_AND_UP, RIGHT_AND_UP, LEFT_AND_DOWN, RIGHT_AND_DOWN }
	
	private final SchemaXYLayoutEditPolicy policy = new SchemaXYLayoutEditPolicy(schema, false);
	private final SchemaXYLayoutEditPolicy readOnlyPolicy = new SchemaXYLayoutEditPolicy(schema, true);
	private final Delta delta = Delta.generate();
		
	private final Set newSet5 = schema.getSet("NEW-SET-5");
	private final Set newSet6 = schema.getSet("NEW-SET-6");
	private final Set newSet7 = schema.getSet("NEW-SET-7");
	private final Set newSet8 = schema.getSet("NEW-SET-8");
	private final Set newSet9 = schema.getSet("NEW-SET-9");
	private final Set newSet10 = schema.getSet("NEW-SET-10");
	private final Set newSet11 = schema.getSet("NEW-SET-11");
	private final Set newSet12 = schema.getSet("NEW-SET-12");
	private final Set newSet13 = schema.getSet("NEW-SET-13");
	private final Set newSet14 = schema.getSet("NEW-SET-14");
	private final Set newSet15 = schema.getSet("NEW-SET-15");
	private final Set newSet16 = schema.getSet("NEW-SET-16");
	private final Set newSet17 = schema.getSet("NEW-SET-17");
	private final Set newSet19 = schema.getSet("NEW-SET-19");
	private final Set newSet23 = schema.getSet("NEW-SET-23");
	private final Set newSet24 = schema.getSet("NEW-SET-24");
	private final Set newSet25 = schema.getSet("NEW-SET-25");
	
	private final EditPart editPartWithModelOtherThanDiagramNode = mock(EditPart.class);
	private final EditPart newRecord1EditPart = mock(EditPart.class);
	private final EditPart newRecord4EditPart = mock(EditPart.class);
	private final EditPart newRecord5EditPart = mock(EditPart.class);
	private final EditPart newRecord9EditPart = mock(EditPart.class);
	private final EditPart newRecord10EditPart = mock(EditPart.class);
	private final EditPart newRecord11EditPart = mock(EditPart.class);
	private final EditPart connectorA1EditPart = mock(EditPart.class);
	private final EditPart connectorA2EditPart = mock(EditPart.class);
	private final EditPart connectorB1EditPart = mock(EditPart.class);
	private final EditPart connectorB2EditPart = mock(EditPart.class);
	private final EditPart connectorC1EditPart = mock(EditPart.class);
	private final EditPart connectorC2EditPart = mock(EditPart.class);
	private final EditPart connectorD1EditPart = mock(EditPart.class);
	private final EditPart connectorD2EditPart = mock(EditPart.class);
	private final EditPart connectorE1EditPart = mock(EditPart.class);
	private final EditPart connectorE2EditPart = mock(EditPart.class);
	private final EditPart newIndex1EditPart = mock(EditPart.class);
	private final EditPart newIndex1ConnectionLabelEditPart = mock(EditPart.class);
	private final EditPart diagramLabelEditPart = mock(EditPart.class);
	private final EditPart newVsamIndex1EditPart = mock(EditPart.class);
	
	private final ChangeBoundsRequest request = new ChangeBoundsRequest();

	@BeforeClass
	public static void loadSchema() {
		schema = TestTools.getSchema("testdata/MoveRecords.schemadsl");
	}
	
	@Before
	public void setup() {
		when(editPartWithModelOtherThanDiagramNode.getModel()).thenReturn("?");
		when(newRecord1EditPart.getModel()).thenReturn(schema.getRecord("NEW-RECORD-1"));
		when(newRecord4EditPart.getModel()).thenReturn(schema.getRecord("NEW-RECORD-4"));
		when(newRecord5EditPart.getModel()).thenReturn(schema.getRecord("NEW-RECORD-5"));
		when(newRecord9EditPart.getModel()).thenReturn(schema.getRecord("NEW-RECORD-9"));
		when(newRecord10EditPart.getModel()).thenReturn(schema.getRecord("NEW-RECORD-10"));
		when(newRecord11EditPart.getModel()).thenReturn(schema.getRecord("NEW-RECORD-11"));
		when(connectorA1EditPart.getModel()).thenReturn(newSet23.getMembers().get(0).getConnectionParts().get(0).getConnector());
		when(connectorA2EditPart.getModel()).thenReturn(newSet23.getMembers().get(0).getConnectionParts().get(1).getConnector());
		when(connectorB1EditPart.getModel()).thenReturn(newSet24.getMembers().get(0).getConnectionParts().get(0).getConnector());
		when(connectorB2EditPart.getModel()).thenReturn(newSet24.getMembers().get(0).getConnectionParts().get(1).getConnector());
		when(connectorC1EditPart.getModel()).thenReturn(newSet19.getMembers().get(0).getConnectionParts().get(0).getConnector());
		when(connectorC2EditPart.getModel()).thenReturn(newSet19.getMembers().get(0).getConnectionParts().get(1).getConnector());
		when(connectorD1EditPart.getModel()).thenReturn(newSet17.getMembers().get(0).getConnectionParts().get(0).getConnector());
		when(connectorD2EditPart.getModel()).thenReturn(newSet17.getMembers().get(0).getConnectionParts().get(1).getConnector());
		when(connectorE1EditPart.getModel()).thenReturn(newSet25.getMembers().get(0).getConnectionParts().get(0).getConnector());
		when(connectorE2EditPart.getModel()).thenReturn(newSet25.getMembers().get(0).getConnectionParts().get(1).getConnector());
		when(newIndex1EditPart.getModel()).thenReturn(schema.getSet("NEW-INDEX-1").getSystemOwner());
		when(newIndex1ConnectionLabelEditPart.getModel()).thenReturn(schema.getSet("NEW-INDEX-1").getMembers().get(0).getConnectionLabel());
		when(diagramLabelEditPart.getModel()).thenReturn(schema.getDiagramData().getLabel());
		when(newVsamIndex1EditPart.getModel()).thenReturn(schema.getSet("NEW-VSAM-INDEX-1").getVsamIndex());
	}
	
	private <T extends Command> T havePolicyCreateChangeConstraintCommand(EditPart editPart, Direction direction, Class<T> expectedCommandType) {
		var constraint = createConstraint((DiagramNode) editPart.getModel(), direction);
		var command = policy.createChangeConstraintCommand(request, editPart, constraint);
		return expectedCommandType.cast(command);
	}
	
	private Rectangle createConstraint(DiagramNode diagramNode, Direction direction) {
		var deltaPair = toDeltaPair(direction);
		return new Rectangle(diagramNode.getDiagramLocation().getX() + deltaPair.left(),
				diagramNode.getDiagramLocation().getY() + deltaPair.right(), 0, 0);
	}
	
	private void checkMoveDiagramNodeCommand(Command command, EditPart editPart, Direction direction) {
		var moveDiagramNodeCommand = (MoveDiagramNodeCommand) command;
		var diagramNode = (DiagramNode) editPart.getModel();
		var deltaPair = toDeltaPair(direction);
		assertSame(diagramNode, moveDiagramNodeCommand.getDiagramNode());
		assertEquals(diagramNode.getDiagramLocation().getX() + (long) deltaPair.left(), moveDiagramNodeCommand.getX());
		assertEquals(diagramNode.getDiagramLocation().getY() + (long) deltaPair.right(), moveDiagramNodeCommand.getY());
	}
	
	private void checkMoveBendpointCommand(Command command, Set set, Direction direction) {
		checkMoveBendpointCommand(command, set, 0, direction);
	}
	
	private void checkMoveBendpointCommand(Command command, Set set, int connectionPartIndex, Direction direction) {
		var moveBendpointCommand = (MoveBendpointCommand) command;
		var deltaPair = toDeltaPair(direction);
		var connectionPart = set.getMembers().get(0).getConnectionParts().get(connectionPartIndex);
		var index = connectionPart.getBendpointLocations().size() - 1;
		var bendpoint = connectionPart.getBendpointLocations().get(index);
		var expectedLabel = "Move bendpoint with index %s for set %s (connection part index: %d) member %s"
				.formatted(index, set.getName(), connectionPartIndex, set.getMembers().get(0).getRecord().getName());
		assertEquals(expectedLabel, command.getLabel());
		assertSame(bendpoint, moveBendpointCommand.getBendpoint());
		assertEquals(bendpoint.getX() + (long) deltaPair.left(), moveBendpointCommand.getNewX());
		assertEquals(bendpoint.getY() + (long) deltaPair.right(), moveBendpointCommand.getNewY());
		
	}
	
	private Pair<Integer> toDeltaPair(Direction direction) {
		var deltaX = switch (direction) {
			case LEFT, LEFT_AND_UP, LEFT_AND_DOWN -> -delta.x();
			case RIGHT, RIGHT_AND_UP, RIGHT_AND_DOWN -> delta.x();
			case UP, DOWN -> 0;
		};
		var deltaY = switch (direction) {
			case UP, LEFT_AND_UP, RIGHT_AND_UP -> -delta.y();
			case DOWN, LEFT_AND_DOWN, RIGHT_AND_DOWN -> delta.y();
			case LEFT, RIGHT -> 0;
		};
		return new Pair<>(deltaX, deltaY);
	}
	
	private void checkContext(Command command, EditPart editPart, ModelChangeType expectedModelChangeType) {
		if (command instanceof ModelChangeCompoundCommand compoundCommand) {
			var context = compoundCommand.getContext();
			checkContext(context, editPart, expectedModelChangeType);
			var firstCommandWithContext = compoundCommand.getCommands().stream()
					.map(ModelChangeBasicCommand.class::cast)
					.filter(c -> c.getContext() != null)
					.findFirst();
			assertFalse(firstCommandWithContext.isPresent());
		} else if (command instanceof MoveDiagramNodeCommand moveDiagramNodeCommand) {
			var context = moveDiagramNodeCommand.getContext();
			checkContext(context, editPart, expectedModelChangeType);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	private void checkContext(ModelChangeContext context, EditPart editPart, ModelChangeType expectedModelChangeType) {
		assertNotNull(context);
		assertEquals(expectedModelChangeType, context.getModelChangeType());
		
		var expectedContextData = new HashMap<String, String>();
		if (expectedModelChangeType == ModelChangeType.MOVE_RECORD) {
			var schemaRecord = (SchemaRecord) editPart.getModel();
			expectedContextData.put(ContextDataKeys.RECORD_NAME, schemaRecord.getName());
		} else if (expectedModelChangeType == ModelChangeType.MOVE_CONNECTOR) {
			var connector = (Connector) editPart.getModel();
			var connectionPart = connector.getConnectionPart(); 
			var memberRole = connectionPart.getMemberRole();
			expectedContextData.put(ContextDataKeys.SET_NAME, memberRole.getSet().getName());
			expectedContextData.put(ContextDataKeys.RECORD_NAME, memberRole.getRecord().getName());
			expectedContextData.put(ContextDataKeys.CONNECTION_PART_INDEX, String.valueOf(memberRole.getConnectionParts().indexOf(connectionPart)));
		} else if (expectedModelChangeType == ModelChangeType.MOVE_INDEX) {
			var systemOwner = (SystemOwner) editPart.getModel();
			expectedContextData.put(ContextDataKeys.SET_NAME, systemOwner.getSet().getName());
		} else if (expectedModelChangeType == ModelChangeType.MOVE_SET_OR_INDEX_LABEL) {
			var connectionLabel = (ConnectionLabel) editPart.getModel();
			var memberRole = connectionLabel.getMemberRole();
			expectedContextData.put(ContextDataKeys.SET_NAME, memberRole.getSet().getName());
			expectedContextData.put(ContextDataKeys.RECORD_NAME, memberRole.getRecord().getName());
		} else if (expectedModelChangeType == ModelChangeType.MOVE_VSAM_INDEX) {
			var vsamIndex = (VsamIndex) editPart.getModel();
			expectedContextData.put(ContextDataKeys.SET_NAME, vsamIndex.getSet().getName());
		} else if (expectedModelChangeType != ModelChangeType.MOVE_DIAGRAM_LABEL) {
			throw new IllegalArgumentException();
		}
		assertEquals(context.getContextData(), expectedContextData);
	}
	
	@Test
	public void testMoveNewRecord1ToTheLeft() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord1EditPart, Direction.LEFT, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord1EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(9, command.getCommands().size());
				
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord1EditPart, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet10, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet11, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(3), newSet12, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(4), newSet13, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(5), newSet14, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(6), newSet15, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(7), newSet16, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(8), newSet9, Direction.RIGHT);
	}
	
	@Test
	public void testMoveNewRecord1ToTheRight() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord1EditPart, Direction.RIGHT, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord1EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(9, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord1EditPart, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet10, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet11, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(3), newSet12, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(4), newSet13, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(5), newSet14, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(6), newSet15, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(7), newSet16, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(8), newSet9, Direction.LEFT);
	}
	
	@Test
	public void testMoveNewRecord1Up() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord1EditPart, Direction.UP, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord1EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(5, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord1EditPart, Direction.UP);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet5, Direction.DOWN);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet6, Direction.DOWN);
		checkMoveBendpointCommand(command.getCommands().get(3), newSet7, Direction.DOWN);
		checkMoveBendpointCommand(command.getCommands().get(4), newSet8, Direction.DOWN);
	}
	
	@Test
	public void testMoveNewRecord1Down() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord1EditPart, Direction.DOWN, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord1EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(5, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord1EditPart, Direction.DOWN);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet5, Direction.UP);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet6, Direction.UP);
		checkMoveBendpointCommand(command.getCommands().get(3), newSet7, Direction.UP);
		checkMoveBendpointCommand(command.getCommands().get(4), newSet8, Direction.UP);
	}
	
	@Test
	public void testMoveNewRecord1ToTheLeftAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord1EditPart, Direction.LEFT_AND_UP, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord1EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(13, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord1EditPart, Direction.LEFT_AND_UP);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet10, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet11, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(3), newSet12, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(4), newSet13, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(5), newSet14, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(6), newSet15, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(7), newSet16, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(8), newSet5, Direction.DOWN);
		checkMoveBendpointCommand(command.getCommands().get(9), newSet6, Direction.DOWN);
		checkMoveBendpointCommand(command.getCommands().get(10), newSet7, Direction.DOWN);
		checkMoveBendpointCommand(command.getCommands().get(11), newSet8, Direction.DOWN);
		checkMoveBendpointCommand(command.getCommands().get(12), newSet9, Direction.RIGHT);
	}
	
	@Test
	public void testMoveNewRecord1ToTheRightAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord1EditPart, Direction.RIGHT_AND_UP, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord1EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(13, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord1EditPart, Direction.RIGHT_AND_UP);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet10, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet11, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(3), newSet12, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(4), newSet13, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(5), newSet14, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(6), newSet15, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(7), newSet16, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(8), newSet5, Direction.DOWN);
		checkMoveBendpointCommand(command.getCommands().get(9), newSet6, Direction.DOWN);
		checkMoveBendpointCommand(command.getCommands().get(10), newSet7, Direction.DOWN);
		checkMoveBendpointCommand(command.getCommands().get(11), newSet8, Direction.DOWN);
		checkMoveBendpointCommand(command.getCommands().get(12), newSet9, Direction.LEFT);
	}
	
	@Test
	public void testMoveNewRecord1ToTheLeftAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord1EditPart, Direction.LEFT_AND_DOWN, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord1EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(13, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord1EditPart, Direction.LEFT_AND_DOWN);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet10, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet11, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(3), newSet12, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(4), newSet13, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(5), newSet14, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(6), newSet15, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(7), newSet16, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(8), newSet5, Direction.UP);
		checkMoveBendpointCommand(command.getCommands().get(9), newSet6, Direction.UP);
		checkMoveBendpointCommand(command.getCommands().get(10), newSet7, Direction.UP);
		checkMoveBendpointCommand(command.getCommands().get(11), newSet8, Direction.UP);
		checkMoveBendpointCommand(command.getCommands().get(12), newSet9, Direction.RIGHT);
	}
	
	@Test
	public void testMoveNewRecord1ToTheRightAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord1EditPart, Direction.RIGHT_AND_DOWN, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord1EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(13, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord1EditPart, Direction.RIGHT_AND_DOWN);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet10, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet11, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(3), newSet12, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(4), newSet13, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(5), newSet14, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(6), newSet15, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(7), newSet16, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(8), newSet5, Direction.UP);
		checkMoveBendpointCommand(command.getCommands().get(9), newSet6, Direction.UP);
		checkMoveBendpointCommand(command.getCommands().get(10), newSet7, Direction.UP);
		checkMoveBendpointCommand(command.getCommands().get(11), newSet8, Direction.UP);
		checkMoveBendpointCommand(command.getCommands().get(12), newSet9, Direction.LEFT);
	}
	
	@Test
	public void testMoveNewRecord9ToTheLeft() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord9EditPart, Direction.LEFT, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord9EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(3, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord9EditPart, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet10, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet9, Direction.LEFT);
	}
	
	@Test
	public void testMoveNewRecord9ToTheRight() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord9EditPart, Direction.RIGHT, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord9EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(3, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord9EditPart, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet10, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet9, Direction.RIGHT);
	}
	
	@Test
	public void testMoveNewRecord9Up() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord9EditPart, Direction.UP, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord9EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord9EditPart, Direction.UP);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet5, Direction.UP);
	}
	
	@Test
	public void testMoveNewRecord9Down() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord9EditPart, Direction.DOWN, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord9EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord9EditPart, Direction.DOWN);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet5, Direction.DOWN);
	}
	
	@Test
	public void testMoveNewRecord9ToTheLeftAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord9EditPart, Direction.LEFT_AND_UP, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord9EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(4, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord9EditPart, Direction.LEFT_AND_UP);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet10, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet5, Direction.UP);
		checkMoveBendpointCommand(command.getCommands().get(3), newSet9, Direction.LEFT);
	}
	
	@Test
	public void testMoveNewRecord9ToTheRightAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord9EditPart, Direction.RIGHT_AND_UP, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord9EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(4, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord9EditPart, Direction.RIGHT_AND_UP);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet10, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet5, Direction.UP);
		checkMoveBendpointCommand(command.getCommands().get(3), newSet9, Direction.RIGHT);
	}
	
	@Test
	public void testMoveNewRecord9ToTheLeftAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord9EditPart, Direction.LEFT_AND_DOWN, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord9EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(4, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord9EditPart, Direction.LEFT_AND_DOWN);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet10, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet5, Direction.DOWN);
		checkMoveBendpointCommand(command.getCommands().get(3), newSet9, Direction.LEFT);
	}
	
	@Test
	public void testMoveNewRecord9ToTheRightAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord9EditPart, Direction.RIGHT_AND_DOWN, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord9EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(4, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord9EditPart, Direction.RIGHT_AND_DOWN);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet10, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet5, Direction.DOWN);
		checkMoveBendpointCommand(command.getCommands().get(3), newSet9, Direction.RIGHT);
	}
	
	@Test
	public void testMoveNewRecord5ToTheLeft() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord5EditPart, Direction.LEFT, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord5EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(5, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord5EditPart, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet15, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet16, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(3), newSet24, 0, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(4), newSet24, 1, Direction.RIGHT);
	}
	
	@Test
	public void testMoveNewRecord5ToTheRight() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord5EditPart, Direction.RIGHT, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord5EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(5, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord5EditPart, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet15, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet16, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(3), newSet24, 0, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(4), newSet24, 1, Direction.LEFT);
	}
	
	@Test
	public void testMoveNewRecord5Up() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord5EditPart, Direction.UP, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord5EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(3, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord5EditPart, Direction.UP);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet24, 1, Direction.DOWN);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet8, Direction.UP);
	}
	
	@Test
	public void testMoveNewRecord5Down() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord5EditPart, Direction.DOWN, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord5EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(3, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord5EditPart, Direction.DOWN);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet24, 1, Direction.UP);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet8, Direction.DOWN);
	}
	
	@Test
	public void testMoveNewRecord5ToTheLeftAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord5EditPart, Direction.LEFT_AND_UP, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord5EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(6, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord5EditPart, Direction.LEFT_AND_UP);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet15, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet16, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(3), newSet24, 0, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(4), newSet24, 1, Direction.RIGHT_AND_DOWN);
		checkMoveBendpointCommand(command.getCommands().get(5), newSet8, Direction.UP);
	}
	
	@Test
	public void testMoveNewRecord5ToTheRightAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord5EditPart, Direction.RIGHT_AND_UP, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord5EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(6, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord5EditPart, Direction.RIGHT_AND_UP);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet15, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet16, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(3), newSet24, 0, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(4), newSet24, 1, Direction.LEFT_AND_DOWN);
		checkMoveBendpointCommand(command.getCommands().get(5), newSet8, Direction.UP);
	}
	
	@Test
	public void testMoveNewRecord5ToTheLeftAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord5EditPart, Direction.LEFT_AND_DOWN, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord5EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(6, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord5EditPart, Direction.LEFT_AND_DOWN);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet15, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet16, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(3), newSet24, 0, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(4), newSet24, 1, Direction.RIGHT_AND_UP);
		checkMoveBendpointCommand(command.getCommands().get(5), newSet8, Direction.DOWN);
	}
	
	@Test
	public void testMoveNewRecord5ToTheRightAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord5EditPart, Direction.RIGHT_AND_DOWN, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord5EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(6, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord5EditPart, Direction.RIGHT_AND_DOWN);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet15, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet16, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(3), newSet24, 0, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(4), newSet24, 1, Direction.LEFT_AND_UP);
		checkMoveBendpointCommand(command.getCommands().get(5), newSet8, Direction.DOWN);
	}
	
	@Test
	public void testMoveNewRecord4ToTheLeft() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord4EditPart, Direction.LEFT, MoveDiagramNodeCommand.class);
		checkContext(command, newRecord4EditPart, ModelChangeType.MOVE_RECORD);
		checkMoveDiagramNodeCommand(command, newRecord4EditPart, Direction.LEFT);
	}
	
	@Test
	public void testMoveNewRecord4ToTheRight() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord4EditPart, Direction.RIGHT, MoveDiagramNodeCommand.class);
		checkContext(command, newRecord4EditPart, ModelChangeType.MOVE_RECORD);
		checkMoveDiagramNodeCommand(command, newRecord4EditPart, Direction.RIGHT);
	}
	
	@Test
	public void testMoveNewRecord4Up() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord4EditPart, Direction.UP, MoveDiagramNodeCommand.class);
		checkContext(command, newRecord4EditPart, ModelChangeType.MOVE_RECORD);
		checkMoveDiagramNodeCommand(command, newRecord4EditPart, Direction.UP);
	}
	
	@Test
	public void testMoveNewRecord4Down() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord4EditPart, Direction.DOWN, MoveDiagramNodeCommand.class);
		checkContext(command, newRecord4EditPart, ModelChangeType.MOVE_RECORD);
		checkMoveDiagramNodeCommand(command, newRecord4EditPart, Direction.DOWN);
	}
	
	@Test
	public void testMoveNewRecord4ToTheLeftAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord4EditPart, Direction.LEFT_AND_UP, MoveDiagramNodeCommand.class);
		checkContext(command, newRecord4EditPart, ModelChangeType.MOVE_RECORD);
		checkMoveDiagramNodeCommand(command, newRecord4EditPart, Direction.LEFT_AND_UP);
	}
	
	@Test
	public void testMoveNewRecord4ToTheRightAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord4EditPart, Direction.RIGHT_AND_UP, MoveDiagramNodeCommand.class);
		checkContext(command, newRecord4EditPart, ModelChangeType.MOVE_RECORD);
		checkMoveDiagramNodeCommand(command, newRecord4EditPart, Direction.RIGHT_AND_UP);
	}
	
	@Test
	public void testMoveNewRecord4TheLeftAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord4EditPart, Direction.LEFT_AND_DOWN, MoveDiagramNodeCommand.class);
		checkContext(command, newRecord4EditPart, ModelChangeType.MOVE_RECORD);
		checkMoveDiagramNodeCommand(command, newRecord4EditPart, Direction.LEFT_AND_DOWN);
	}
	
	@Test
	public void testMoveNewRecord4ToTheRightDown() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord4EditPart, Direction.RIGHT_AND_DOWN, MoveDiagramNodeCommand.class);
		checkContext(command, newRecord4EditPart, ModelChangeType.MOVE_RECORD);
		checkMoveDiagramNodeCommand(command, newRecord4EditPart, Direction.RIGHT_AND_DOWN);
	}
	
	@Test
	public void testMoveNewRecord10ToTheLeft() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord10EditPart, Direction.LEFT, MoveDiagramNodeCommand.class);
		checkContext(command, newRecord10EditPart, ModelChangeType.MOVE_RECORD);
		checkMoveDiagramNodeCommand(command, newRecord10EditPart, Direction.LEFT);
	}
	
	@Test
	public void testMoveNewRecord10ToTheRight() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord10EditPart, Direction.RIGHT, MoveDiagramNodeCommand.class);
		checkContext(command, newRecord10EditPart, ModelChangeType.MOVE_RECORD);
		checkMoveDiagramNodeCommand(command, newRecord10EditPart, Direction.RIGHT);
	}
	
	@Test
	public void testMoveNewRecord10Up() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord10EditPart, Direction.UP, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord10EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(2, command.getCommands().size());
				
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord10EditPart, Direction.UP);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet23, 1, Direction.UP);
	}
	
	@Test
	public void testMoveNewRecord10Down() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord10EditPart, Direction.DOWN, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord10EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord10EditPart, Direction.DOWN);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet23, 1, Direction.DOWN);
	}
	
	@Test
	public void testMoveNewRecord10ToTheLeftAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord10EditPart, Direction.LEFT_AND_UP, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord10EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord10EditPart, Direction.LEFT_AND_UP);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet23, 1, Direction.UP);
	}
	
	@Test
	public void testMoveNewRecord10ToTheRightAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord10EditPart, Direction.RIGHT_AND_UP, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord10EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord10EditPart, Direction.RIGHT_AND_UP);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet23, 1, Direction.UP);
	}
	
	@Test
	public void testMoveNewRecord10ToTheLeftAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord10EditPart, Direction.LEFT_AND_DOWN, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord10EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord10EditPart, Direction.LEFT_AND_DOWN);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet23, 1, Direction.DOWN);
	}
	
	@Test
	public void testMoveNewRecord10ToTheRightAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord10EditPart, Direction.RIGHT_AND_DOWN, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord10EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord10EditPart, Direction.RIGHT_AND_DOWN);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet23, 1, Direction.DOWN);
	}
	
	@Test
	public void testMoveNewRecord11ToTheLeft() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord11EditPart, Direction.LEFT, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord11EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(3, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord11EditPart, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet23, 1, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet24, 1, Direction.LEFT);
	}
	
	@Test
	public void testMoveNewRecord11ToTheRight() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord11EditPart, Direction.RIGHT, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord11EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(3, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord11EditPart, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet23, 1, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet24, 1, Direction.RIGHT);
	}
	
	@Test
	public void testMoveNewRecord11Up() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord11EditPart, Direction.UP, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord11EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(3, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord11EditPart, Direction.UP);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet23, 0, Direction.DOWN);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet23, 1, Direction.DOWN);
	}
	
	@Test
	public void testMoveNewRecord11Down() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord11EditPart, Direction.DOWN, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord11EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(3, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord11EditPart, Direction.DOWN);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet23, 0, Direction.UP);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet23, 1, Direction.UP);
	}
	
	@Test
	public void testMoveNewRecord11ToTheLeftAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord11EditPart, Direction.LEFT_AND_UP, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord11EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(4, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord11EditPart, Direction.LEFT_AND_UP);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet23, 0, Direction.DOWN);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet23, 1, Direction.RIGHT_AND_DOWN);
		checkMoveBendpointCommand(command.getCommands().get(3), newSet24, 1, Direction.LEFT);
	}
	
	@Test
	public void testMoveNewRecord11ToTheRightAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord11EditPart, Direction.RIGHT_AND_UP, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord11EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(4, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord11EditPart, Direction.RIGHT_AND_UP);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet23, 0, Direction.DOWN);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet23, 1, Direction.LEFT_AND_DOWN);
		checkMoveBendpointCommand(command.getCommands().get(3), newSet24, 1, Direction.RIGHT);
	}
	
	@Test
	public void testMoveNewRecord11ToTheLeftAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord11EditPart, Direction.LEFT_AND_DOWN, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord11EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(4, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord11EditPart, Direction.LEFT_AND_DOWN);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet23, 0, Direction.UP);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet23, 1, Direction.RIGHT_AND_UP);
		checkMoveBendpointCommand(command.getCommands().get(3), newSet24, 1, Direction.LEFT);
	}
	
	@Test
	public void testMoveNewRecord11ToTheRightAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(newRecord11EditPart, Direction.RIGHT_AND_DOWN, ModelChangeCompoundCommand.class);
		checkContext(command, newRecord11EditPart, ModelChangeType.MOVE_RECORD);
		assertEquals(4, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), newRecord11EditPart, Direction.RIGHT_AND_DOWN);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet23, 0, Direction.UP);
		checkMoveBendpointCommand(command.getCommands().get(2), newSet23, 1, Direction.LEFT_AND_UP);
		checkMoveBendpointCommand(command.getCommands().get(3), newSet24, 1, Direction.RIGHT);
	}
	
	@Test
	public void testMoveConnectorA1ToTheLeft() {
		var command = havePolicyCreateChangeConstraintCommand(connectorA1EditPart, Direction.LEFT, MoveDiagramNodeCommand.class);
		checkContext(command, connectorA1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorA1EditPart, Direction.LEFT);
	}
	
	@Test
	public void testMoveConnectorA1ToTheRight() {
		var command = havePolicyCreateChangeConstraintCommand(connectorA1EditPart, Direction.RIGHT, MoveDiagramNodeCommand.class);
		checkContext(command, connectorA1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorA1EditPart, Direction.RIGHT);
	}
	
	@Test
	public void testMoveConnectorA1Up() {
		var command = havePolicyCreateChangeConstraintCommand(connectorA1EditPart, Direction.UP, ModelChangeCompoundCommand.class);
		checkContext(command, connectorA1EditPart, ModelChangeType.MOVE_CONNECTOR);
		assertEquals(2, command.getCommands().size());
				
		checkMoveDiagramNodeCommand(command.getCommands().get(0), connectorA1EditPart, Direction.UP);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet23, 0, Direction.UP);
	}
	
	@Test
	public void testMoveConnectorA1Down() {
		var command = havePolicyCreateChangeConstraintCommand(connectorA1EditPart, Direction.DOWN, ModelChangeCompoundCommand.class);
		checkContext(command, connectorA1EditPart, ModelChangeType.MOVE_CONNECTOR);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), connectorA1EditPart, Direction.DOWN);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet23, 0, Direction.DOWN);
	}
	
	@Test
	public void testMoveConnectorA1ToTheLeftAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(connectorA1EditPart, Direction.LEFT_AND_UP, ModelChangeCompoundCommand.class);
		checkContext(command, connectorA1EditPart, ModelChangeType.MOVE_CONNECTOR);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), connectorA1EditPart, Direction.LEFT_AND_UP);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet23, 0, Direction.UP);
	}
	
	@Test
	public void testMoveConnectorA1ToTheRightAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(connectorA1EditPart, Direction.RIGHT_AND_UP, ModelChangeCompoundCommand.class);
		checkContext(command, connectorA1EditPart, ModelChangeType.MOVE_CONNECTOR);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), connectorA1EditPart, Direction.RIGHT_AND_UP);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet23, 0, Direction.UP);
	}
	
	@Test
	public void testMoveConnectorA1ToTheLeftAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(connectorA1EditPart, Direction.LEFT_AND_DOWN, ModelChangeCompoundCommand.class);
		checkContext(command, connectorA1EditPart, ModelChangeType.MOVE_CONNECTOR);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), connectorA1EditPart, Direction.LEFT_AND_DOWN);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet23, 0, Direction.DOWN);
	}
	
	@Test
	public void testMoveConnectorA1ToTheRightAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(connectorA1EditPart, Direction.RIGHT_AND_DOWN, ModelChangeCompoundCommand.class);
		checkContext(command, connectorA1EditPart, ModelChangeType.MOVE_CONNECTOR);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), connectorA1EditPart, Direction.RIGHT_AND_DOWN);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet23, 0, Direction.DOWN);
	}
	
	@Test
	public void testMoveConnectorA2ToTheLeft() {
		var command = havePolicyCreateChangeConstraintCommand(connectorA2EditPart, Direction.LEFT, ModelChangeCompoundCommand.class);
		checkContext(command, connectorA2EditPart, ModelChangeType.MOVE_CONNECTOR);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), connectorA2EditPart, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet23, 1, Direction.LEFT);
	}
	
	@Test
	public void testMoveConnectorA2ToTheRight() {
		var command = havePolicyCreateChangeConstraintCommand(connectorA2EditPart, Direction.RIGHT, ModelChangeCompoundCommand.class);
		checkContext(command, connectorA2EditPart, ModelChangeType.MOVE_CONNECTOR);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), connectorA2EditPart, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet23, 1, Direction.RIGHT);
	}
	
	@Test
	public void testMoveConnectorA2Up() {
		var command = havePolicyCreateChangeConstraintCommand(connectorA2EditPart, Direction.UP, MoveDiagramNodeCommand.class);
		checkContext(command, connectorA2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorA2EditPart, Direction.UP);
	}
	
	@Test
	public void testMoveConnectorA2Down() {
		var command = havePolicyCreateChangeConstraintCommand(connectorA2EditPart, Direction.DOWN, MoveDiagramNodeCommand.class);
		checkContext(command, connectorA2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorA2EditPart, Direction.DOWN);
	}
	
	@Test
	public void testMoveConnectorA2ToTheLeftAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(connectorA2EditPart, Direction.LEFT_AND_UP, ModelChangeCompoundCommand.class);
		checkContext(command, connectorA2EditPart, ModelChangeType.MOVE_CONNECTOR);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), connectorA2EditPart, Direction.LEFT_AND_UP);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet23, 1, Direction.LEFT);
	}
	
	@Test
	public void testMoveConnectorA2ToTheRightAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(connectorA2EditPart, Direction.RIGHT_AND_UP, ModelChangeCompoundCommand.class);
		checkContext(command, connectorA2EditPart, ModelChangeType.MOVE_CONNECTOR);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), connectorA2EditPart, Direction.RIGHT_AND_UP);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet23, 1, Direction.RIGHT);
	}
	
	@Test
	public void testMoveConnectorA2ToTheLeftAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(connectorA2EditPart, Direction.LEFT_AND_DOWN, ModelChangeCompoundCommand.class);
		checkContext(command, connectorA2EditPart, ModelChangeType.MOVE_CONNECTOR);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), connectorA2EditPart, Direction.LEFT_AND_DOWN);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet23, 1, Direction.LEFT);
	}
	
	@Test
	public void testMoveConnectorA2ToTheRightAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(connectorA2EditPart, Direction.RIGHT_AND_DOWN, ModelChangeCompoundCommand.class);
		checkContext(command, connectorA2EditPart, ModelChangeType.MOVE_CONNECTOR);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), connectorA2EditPart, Direction.RIGHT_AND_DOWN);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet23, 1, Direction.RIGHT);
	}
	
	@Test
	public void testMoveConnectorB1ToTheLeft() {
		var command = havePolicyCreateChangeConstraintCommand(connectorB1EditPart, Direction.LEFT, ModelChangeCompoundCommand.class);
		checkContext(command, connectorB1EditPart, ModelChangeType.MOVE_CONNECTOR);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), connectorB1EditPart, Direction.LEFT);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet24, 0, Direction.LEFT);
	}
	
	@Test
	public void testMoveConnectorB1ToTheRight() {
		var command = havePolicyCreateChangeConstraintCommand(connectorB1EditPart, Direction.RIGHT, ModelChangeCompoundCommand.class);
		checkContext(command, connectorB1EditPart, ModelChangeType.MOVE_CONNECTOR);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), connectorB1EditPart, Direction.RIGHT);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet24, 0, Direction.RIGHT);
	}
	
	@Test
	public void testMoveConnectorB1Up() {
		var command = havePolicyCreateChangeConstraintCommand(connectorB1EditPart, Direction.UP, MoveDiagramNodeCommand.class);
		checkContext(command, connectorB1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorB1EditPart, Direction.UP);
	}
	
	@Test
	public void testMoveConnectorB1Down() {
		var command = havePolicyCreateChangeConstraintCommand(connectorB1EditPart, Direction.DOWN, MoveDiagramNodeCommand.class);
		checkContext(command, connectorB1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorB1EditPart, Direction.DOWN);
	}
	
	@Test
	public void testMoveConnectorB1ToTheLeftAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(connectorB1EditPart, Direction.LEFT_AND_UP, ModelChangeCompoundCommand.class);
		checkContext(command, connectorB1EditPart, ModelChangeType.MOVE_CONNECTOR);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), connectorB1EditPart, Direction.LEFT_AND_UP);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet24, 0, Direction.LEFT);
	}
	
	@Test
	public void testMoveConnectorB1ToTheRightAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(connectorB1EditPart, Direction.RIGHT_AND_UP, ModelChangeCompoundCommand.class);
		checkContext(command, connectorB1EditPart, ModelChangeType.MOVE_CONNECTOR);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), connectorB1EditPart, Direction.RIGHT_AND_UP);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet24, 0, Direction.RIGHT);
	}
	
	@Test
	public void testMoveConnectorB1ToTheLeftAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(connectorB1EditPart, Direction.LEFT_AND_DOWN, ModelChangeCompoundCommand.class);
		checkContext(command, connectorB1EditPart, ModelChangeType.MOVE_CONNECTOR);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), connectorB1EditPart, Direction.LEFT_AND_DOWN);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet24, 0, Direction.LEFT);
	}
	
	@Test
	public void testMoveConnectorB1ToTheRightAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(connectorB1EditPart, Direction.RIGHT_AND_DOWN, ModelChangeCompoundCommand.class);
		checkContext(command, connectorB1EditPart, ModelChangeType.MOVE_CONNECTOR);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), connectorB1EditPart, Direction.RIGHT_AND_DOWN);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet24, 0, Direction.RIGHT);
	}
	
	@Test
	public void testMoveConnectorB2ToTheLeft() {
		var command = havePolicyCreateChangeConstraintCommand(connectorB2EditPart, Direction.LEFT, MoveDiagramNodeCommand.class);
		checkContext(command, connectorB2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorB2EditPart, Direction.LEFT);
	}
	
	@Test
	public void testMoveConnectorB2ToTheRight() {
		var command = havePolicyCreateChangeConstraintCommand(connectorB2EditPart, Direction.RIGHT, MoveDiagramNodeCommand.class);
		checkContext(command, connectorB2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorB2EditPart, Direction.RIGHT);
	}
	
	@Test
	public void testMoveConnectorB2Up() {
		var command = havePolicyCreateChangeConstraintCommand(connectorB2EditPart, Direction.UP, ModelChangeCompoundCommand.class);
		checkContext(command, connectorB2EditPart, ModelChangeType.MOVE_CONNECTOR);
		assertEquals(2, command.getCommands().size());		
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), connectorB2EditPart, Direction.UP);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet24, 1, Direction.UP);
	}
	
	@Test
	public void testMoveConnectorB2Down() {
		var command = havePolicyCreateChangeConstraintCommand(connectorB2EditPart, Direction.DOWN, ModelChangeCompoundCommand.class);
		checkContext(command, connectorB2EditPart, ModelChangeType.MOVE_CONNECTOR);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), connectorB2EditPart, Direction.DOWN);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet24, 1, Direction.DOWN);
	}
	
	@Test
	public void testMoveConnectorB2ToTheLeftAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(connectorB2EditPart, Direction.LEFT_AND_UP, ModelChangeCompoundCommand.class);
		checkContext(command, connectorB2EditPart, ModelChangeType.MOVE_CONNECTOR);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), connectorB2EditPart, Direction.LEFT_AND_UP);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet24, 1, Direction.UP);
	}
	
	@Test
	public void testMoveConnectorB2ToTheRightAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(connectorB2EditPart, Direction.RIGHT_AND_UP, ModelChangeCompoundCommand.class);
		checkContext(command, connectorB2EditPart, ModelChangeType.MOVE_CONNECTOR);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), connectorB2EditPart, Direction.RIGHT_AND_UP);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet24, 1, Direction.UP);
	}
	
	@Test
	public void testMoveConnectorB2ToTheLeftAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(connectorB2EditPart, Direction.LEFT_AND_DOWN, ModelChangeCompoundCommand.class);
		checkContext(command, connectorB2EditPart, ModelChangeType.MOVE_CONNECTOR);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), connectorB2EditPart, Direction.LEFT_AND_DOWN);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet24, 1, Direction.DOWN);
	}
	
	@Test
	public void testMoveConnectorB2ToTheRightAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(connectorB2EditPart, Direction.RIGHT_AND_DOWN, ModelChangeCompoundCommand.class);
		checkContext(command, connectorB2EditPart, ModelChangeType.MOVE_CONNECTOR);
		assertEquals(2, command.getCommands().size());
		
		checkMoveDiagramNodeCommand(command.getCommands().get(0), connectorB2EditPart, Direction.RIGHT_AND_DOWN);
		checkMoveBendpointCommand(command.getCommands().get(1), newSet24, 1, Direction.DOWN);
	}
	
	@Test
	public void testMoveConnectorC1ToTheLeft() {
		var command = havePolicyCreateChangeConstraintCommand(connectorC1EditPart, Direction.LEFT, MoveDiagramNodeCommand.class);
		checkContext(command, connectorC1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorC1EditPart, Direction.LEFT);
	}
	
	@Test
	public void testMoveConnectorC1ToTheRight() {
		var command = havePolicyCreateChangeConstraintCommand(connectorC1EditPart, Direction.RIGHT, MoveDiagramNodeCommand.class);
		checkContext(command, connectorC1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorC1EditPart, Direction.RIGHT);
	}
	
	@Test
	public void testMoveConnectorC1Up() {
		var command = havePolicyCreateChangeConstraintCommand(connectorC1EditPart, Direction.UP, MoveDiagramNodeCommand.class);
		checkContext(command, connectorC1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorC1EditPart, Direction.UP);
	}
	
	@Test
	public void testMoveConnectorC1Down() {
		var command = havePolicyCreateChangeConstraintCommand(connectorC1EditPart, Direction.DOWN, MoveDiagramNodeCommand.class);
		checkContext(command, connectorC1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorC1EditPart, Direction.DOWN);
	}
	
	@Test
	public void testMoveConnectorC1ToTheLeftAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(connectorC1EditPart, Direction.LEFT_AND_UP, MoveDiagramNodeCommand.class);
		checkContext(command, connectorC1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorC1EditPart, Direction.LEFT_AND_UP);
	}
	
	@Test
	public void testMoveConnectorC1ToTheRightAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(connectorC1EditPart, Direction.RIGHT_AND_UP, MoveDiagramNodeCommand.class);
		checkContext(command, connectorC1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorC1EditPart, Direction.RIGHT_AND_UP);
	}
	
	@Test
	public void testMoveConnectorC1ToTheLeftAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(connectorC1EditPart, Direction.LEFT_AND_DOWN, MoveDiagramNodeCommand.class);
		checkContext(command, connectorC1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorC1EditPart, Direction.LEFT_AND_DOWN);
	}
	
	@Test
	public void testMoveConnectorC1ToTheRightAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(connectorC1EditPart, Direction.RIGHT_AND_DOWN, MoveDiagramNodeCommand.class);
		checkContext(command, connectorC1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorC1EditPart, Direction.RIGHT_AND_DOWN);
	}
	
	@Test
	public void testMoveConnectorC2ToTheLeft() {
		var command = havePolicyCreateChangeConstraintCommand(connectorC2EditPart, Direction.LEFT, MoveDiagramNodeCommand.class);
		checkContext(command, connectorC2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorC2EditPart, Direction.LEFT);
	}
	
	@Test
	public void testMoveConnectorC2ToTheRight() {
		var command = havePolicyCreateChangeConstraintCommand(connectorC2EditPart, Direction.RIGHT, MoveDiagramNodeCommand.class);
		checkContext(command, connectorC2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorC2EditPart, Direction.RIGHT);
	}
	
	@Test
	public void testMoveConnectorC2Up() {
		var command = havePolicyCreateChangeConstraintCommand(connectorC2EditPart, Direction.UP, MoveDiagramNodeCommand.class);
		checkContext(command, connectorC2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorC2EditPart, Direction.UP);
	}
	
	@Test
	public void testMoveConnectorC2Down() {
		var command = havePolicyCreateChangeConstraintCommand(connectorC2EditPart, Direction.DOWN, MoveDiagramNodeCommand.class);
		checkContext(command, connectorC2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorC2EditPart, Direction.DOWN);
	}
	
	@Test
	public void testMoveConnectorC2ToTheLeftAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(connectorC2EditPart, Direction.LEFT_AND_UP, MoveDiagramNodeCommand.class);
		checkContext(command, connectorC2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorC2EditPart, Direction.LEFT_AND_UP);
	}
	
	@Test
	public void testMoveConnectorC2ToTheRightAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(connectorC2EditPart, Direction.RIGHT_AND_UP, MoveDiagramNodeCommand.class);
		checkContext(command, connectorC2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorC2EditPart, Direction.RIGHT_AND_UP);
	}
	
	@Test
	public void testMoveConnectorC2ToTheLeftAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(connectorC2EditPart, Direction.LEFT_AND_DOWN, MoveDiagramNodeCommand.class);
		checkContext(command, connectorC2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorC2EditPart, Direction.LEFT_AND_DOWN);
	}
	
	@Test
	public void testMoveConnectorC2ToTheRightAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(connectorC2EditPart, Direction.RIGHT_AND_DOWN, MoveDiagramNodeCommand.class);
		checkContext(command, connectorC2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorC2EditPart, Direction.RIGHT_AND_DOWN);
	}
	
	@Test
	public void testMoveConnectorD1ToTheLeft() {
		var command = havePolicyCreateChangeConstraintCommand(connectorD1EditPart, Direction.LEFT, MoveDiagramNodeCommand.class);
		checkContext(command, connectorD1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorD1EditPart, Direction.LEFT);
	}
	
	@Test
	public void testMoveConnectorD1ToTheRight() {
		var command = havePolicyCreateChangeConstraintCommand(connectorD1EditPart, Direction.RIGHT, MoveDiagramNodeCommand.class);
		checkContext(command, connectorD1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorD1EditPart, Direction.RIGHT);
	}
	
	@Test
	public void testMoveConnectorD1Up() {
		var command = havePolicyCreateChangeConstraintCommand(connectorD1EditPart, Direction.UP, MoveDiagramNodeCommand.class);
		checkContext(command, connectorD1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorD1EditPart, Direction.UP);
	}
	
	@Test
	public void testMoveConnectorD1Down() {
		var command = havePolicyCreateChangeConstraintCommand(connectorD1EditPart, Direction.DOWN, MoveDiagramNodeCommand.class);
		checkContext(command, connectorD1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorD1EditPart, Direction.DOWN);
	}
	
	@Test
	public void testMoveConnectorD1ToTheLeftAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(connectorD1EditPart, Direction.LEFT_AND_UP, MoveDiagramNodeCommand.class);
		checkContext(command, connectorD1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorD1EditPart, Direction.LEFT_AND_UP);
	}
	
	@Test
	public void testMoveConnectorD1ToTheRightAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(connectorD1EditPart, Direction.RIGHT_AND_UP, MoveDiagramNodeCommand.class);
		checkContext(command, connectorD1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorD1EditPart, Direction.RIGHT_AND_UP);
	}
	
	@Test
	public void testMoveConnectorD1ToTheLeftAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(connectorD1EditPart, Direction.LEFT_AND_DOWN, MoveDiagramNodeCommand.class);
		checkContext(command, connectorD1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorD1EditPart, Direction.LEFT_AND_DOWN);
	}
	
	@Test
	public void testMoveConnectorD1ToTheRightAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(connectorD1EditPart, Direction.RIGHT_AND_DOWN, MoveDiagramNodeCommand.class);
		checkContext(command, connectorD1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorD1EditPart, Direction.RIGHT_AND_DOWN);
	}
	
	@Test
	public void testMoveConnectorD2ToTheLeft() {
		var command = havePolicyCreateChangeConstraintCommand(connectorD2EditPart, Direction.LEFT, MoveDiagramNodeCommand.class);
		checkContext(command, connectorD2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorD2EditPart, Direction.LEFT);
	}
	
	@Test
	public void testMoveConnectorD2ToTheRight() {
		var command = havePolicyCreateChangeConstraintCommand(connectorD2EditPart, Direction.RIGHT, MoveDiagramNodeCommand.class);
		checkContext(command, connectorD2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorD2EditPart, Direction.RIGHT);
	}
	
	@Test
	public void testMoveConnectorD2Up() {
		var command = havePolicyCreateChangeConstraintCommand(connectorD2EditPart, Direction.UP, MoveDiagramNodeCommand.class);
		checkContext(command, connectorD2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorD2EditPart, Direction.UP);
	}
	
	@Test
	public void testMoveConnectorD2Down() {
		var command = havePolicyCreateChangeConstraintCommand(connectorD2EditPart, Direction.DOWN, MoveDiagramNodeCommand.class);
		checkContext(command, connectorD2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorD2EditPart, Direction.DOWN);
	}
	
	@Test
	public void testMoveConnectorD2ToTheLeftAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(connectorD2EditPart, Direction.LEFT_AND_UP, MoveDiagramNodeCommand.class);
		checkContext(command, connectorD2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorD2EditPart, Direction.LEFT_AND_UP);
	}
	
	@Test
	public void testMoveConnectorD2ToTheRightAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(connectorD2EditPart, Direction.RIGHT_AND_UP, MoveDiagramNodeCommand.class);
		checkContext(command, connectorD2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorD2EditPart, Direction.RIGHT_AND_UP);
	}
	
	@Test
	public void testMoveConnectorD2ToTheLeftAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(connectorD2EditPart, Direction.LEFT_AND_DOWN, MoveDiagramNodeCommand.class);
		checkContext(command, connectorD2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorD2EditPart, Direction.LEFT_AND_DOWN);
	}
	
	@Test
	public void testMoveConnectorD2ToTheRightAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(connectorD2EditPart, Direction.RIGHT_AND_DOWN, MoveDiagramNodeCommand.class);
		checkContext(command, connectorD2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorD2EditPart, Direction.RIGHT_AND_DOWN);
	}
	
	@Test
	public void testMoveConnectorE1ToTheLeft() {
		var command = havePolicyCreateChangeConstraintCommand(connectorE1EditPart, Direction.LEFT, MoveDiagramNodeCommand.class);
		checkContext(command, connectorE1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorE1EditPart, Direction.LEFT);
	}
	
	@Test
	public void testMoveConnectorE1ToTheRight() {
		var command = havePolicyCreateChangeConstraintCommand(connectorE1EditPart, Direction.RIGHT, MoveDiagramNodeCommand.class);
		checkContext(command, connectorE1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorE1EditPart, Direction.RIGHT);
	}
	
	@Test
	public void testMoveConnectorE1Up() {
		var command = havePolicyCreateChangeConstraintCommand(connectorE1EditPart, Direction.UP, MoveDiagramNodeCommand.class);
		checkContext(command, connectorE1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorE1EditPart, Direction.UP);
	}
	
	@Test
	public void testMoveConnectorE1Down() {
		var command = havePolicyCreateChangeConstraintCommand(connectorE1EditPart, Direction.DOWN, MoveDiagramNodeCommand.class);
		checkContext(command, connectorE1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorE1EditPart, Direction.DOWN);
	}
	
	@Test
	public void testMoveConnectorE1ToTheLeftAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(connectorE1EditPart, Direction.LEFT_AND_UP, MoveDiagramNodeCommand.class);
		checkContext(command, connectorE1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorE1EditPart, Direction.LEFT_AND_UP);
	}
	
	@Test
	public void testMoveConnectorE1ToTheRightAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(connectorE1EditPart, Direction.RIGHT_AND_UP, MoveDiagramNodeCommand.class);
		checkContext(command, connectorE1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorE1EditPart, Direction.RIGHT_AND_UP);
	}
	
	@Test
	public void testMoveConnectorE1ToTheLeftAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(connectorE1EditPart, Direction.LEFT_AND_DOWN, MoveDiagramNodeCommand.class);
		checkContext(command, connectorE1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorE1EditPart, Direction.LEFT_AND_DOWN);
	}
	
	@Test
	public void testMoveConnectorE1ToTheRightAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(connectorE1EditPart, Direction.RIGHT_AND_DOWN, MoveDiagramNodeCommand.class);
		checkContext(command, connectorE1EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorE1EditPart, Direction.RIGHT_AND_DOWN);
	}
	
	@Test
	public void testMoveConnectorE2ToTheLeft() {
		var command = havePolicyCreateChangeConstraintCommand(connectorE2EditPart, Direction.LEFT, MoveDiagramNodeCommand.class);
		checkContext(command, connectorE2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorE2EditPart, Direction.LEFT);
	}
	
	@Test
	public void testMoveConnectorE2ToTheRight() {
		var command = havePolicyCreateChangeConstraintCommand(connectorE2EditPart, Direction.RIGHT, MoveDiagramNodeCommand.class);
		checkContext(command, connectorE2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorE2EditPart, Direction.RIGHT);
	}
	
	@Test
	public void testMoveConnectorE2Up() {
		var command = havePolicyCreateChangeConstraintCommand(connectorE2EditPart, Direction.UP, MoveDiagramNodeCommand.class);
		checkContext(command, connectorE2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorE2EditPart, Direction.UP);
	}
	
	@Test
	public void testMoveConnectorE2Down() {
		var command = havePolicyCreateChangeConstraintCommand(connectorE2EditPart, Direction.DOWN, MoveDiagramNodeCommand.class);
		checkContext(command, connectorE2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorE2EditPart, Direction.DOWN);
	}
	
	@Test
	public void testMoveConnectorE2ToTheLeftAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(connectorE2EditPart, Direction.LEFT_AND_UP, MoveDiagramNodeCommand.class);
		checkContext(command, connectorE2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorE2EditPart, Direction.LEFT_AND_UP);
	}
	
	@Test
	public void testMoveConnectorE2ToTheRightAndUp() {
		var command = havePolicyCreateChangeConstraintCommand(connectorE2EditPart, Direction.RIGHT_AND_UP, MoveDiagramNodeCommand.class);
		checkContext(command, connectorE2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorE2EditPart, Direction.RIGHT_AND_UP);
	}
	
	@Test
	public void testMoveConnectorE2ToTheLeftAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(connectorE2EditPart, Direction.LEFT_AND_DOWN, MoveDiagramNodeCommand.class);
		checkContext(command, connectorE2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorE2EditPart, Direction.LEFT_AND_DOWN);
	}
	
	@Test
	public void testMoveConnectorE2ToTheRightAndDown() {
		var command = havePolicyCreateChangeConstraintCommand(connectorE2EditPart, Direction.RIGHT_AND_DOWN, MoveDiagramNodeCommand.class);
		checkContext(command, connectorE2EditPart, ModelChangeType.MOVE_CONNECTOR);
		checkMoveDiagramNodeCommand(command, connectorE2EditPart, Direction.RIGHT_AND_DOWN);
	}
	
	@Test
	public void testCreateChangeConstraintCommandReturnsNullWhenReadOnly() {
		var command = readOnlyPolicy.createChangeConstraintCommand(request, newRecord1EditPart,
				createConstraint((DiagramNode) newRecord1EditPart.getModel(), Direction.LEFT));
		assertNull(command);
	}
	
	@Test
	public void testCreateChangeConstraintCommandReturnsNullWhenNoDiagramNodeEditPart() {
		var command = policy.createChangeConstraintCommand(request, editPartWithModelOtherThanDiagramNode, null);
		assertNull(command);
	}
	
	@Test
	public void testMoveNewIndex1ToTheLeft() {
		var command = havePolicyCreateChangeConstraintCommand(newIndex1EditPart, Direction.LEFT, MoveDiagramNodeCommand.class);
		checkContext(command, newIndex1EditPart, ModelChangeType.MOVE_INDEX);
		checkMoveDiagramNodeCommand(command, newIndex1EditPart, Direction.LEFT);
	}
	
	@Test
	public void testMoveNewIndex1ConnectionLabelToTheLeft() {
		var command = havePolicyCreateChangeConstraintCommand(newIndex1ConnectionLabelEditPart, Direction.LEFT, MoveDiagramNodeCommand.class);
		checkContext(command, newIndex1ConnectionLabelEditPart, ModelChangeType.MOVE_SET_OR_INDEX_LABEL);
		checkMoveDiagramNodeCommand(command, newIndex1ConnectionLabelEditPart, Direction.LEFT);
	}
	
	@Test
	public void testMoveDiagramLabelToTheLeft() {
		var command = havePolicyCreateChangeConstraintCommand(diagramLabelEditPart, Direction.LEFT, MoveDiagramNodeCommand.class);
		checkContext(command, diagramLabelEditPart, ModelChangeType.MOVE_DIAGRAM_LABEL);
		checkMoveDiagramNodeCommand(command, diagramLabelEditPart, Direction.LEFT);
	}
	
	@Test
	public void testMoveNewVsamIndex1ToTheLeft() {
		var command = havePolicyCreateChangeConstraintCommand(newVsamIndex1EditPart, Direction.LEFT, MoveDiagramNodeCommand.class);
		checkContext(command, newVsamIndex1EditPart, ModelChangeType.MOVE_VSAM_INDEX);
		checkMoveDiagramNodeCommand(command, newVsamIndex1EditPart, Direction.LEFT);
	}
	
}
