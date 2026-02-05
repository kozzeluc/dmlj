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

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.junit.Before;
import org.junit.Test;
import org.lh.dmlj.schema.editor.command.CreateIndexCommand;
import org.lh.dmlj.schema.editor.command.ModelChangeCompoundCommand;
import org.lh.dmlj.schema.editor.command.MoveBendpointCommand;
import org.lh.dmlj.schema.editor.command.MoveDiagramNodeCommand;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.part.ConnectorEditPart;
import org.lh.dmlj.schema.editor.part.IndexEditPart;
import org.lh.dmlj.schema.editor.part.RecordEditPart;

public class ModifiedNonResizableEditPolicyTest {
	private final ModifiedNonResizableEditPolicy policy = new ModifiedNonResizableEditPolicy();
	private final CreateRequest createRequest = new CreateRequest();
	private final ChangeBoundsRequest changeBoundsRequestWithSingleEditPart = new ChangeBoundsRequest();
	private final ChangeBoundsRequest changeBoundsRequestWithMultipleEditParts = new ChangeBoundsRequest();
	private final CreateIndexCommand createIndexCommand = mock(CreateIndexCommand.class);
	private final MoveDiagramNodeCommand moveSchemaRecordCommand = new MoveDiagramNodeCommand(() -> null, -1, -1);
	private final MoveBendpointCommand moveBendpointCommand = mock(MoveBendpointCommand.class);
	private final ModelChangeCompoundCommand compoundCommandWithSingleCommand = new ModelChangeCompoundCommand();
	private final ModelChangeCompoundCommand compoundCommandWithTwoCommandsButFirstNotMoveDiagramNodeCommand = new ModelChangeCompoundCommand();
	private final ModelChangeCompoundCommand compoundCommandWithTwoCommands = new ModelChangeCompoundCommand();
	private final RecordEditPart recordEditPart = mock(RecordEditPart.class);
	private final ConnectorEditPart connectorEditPart = mock(ConnectorEditPart.class);
	private final IndexEditPart indexEditPart = mock(IndexEditPart.class);
	private final ModelChangeContext context = mock(ModelChangeContext.class);

	@Before
	public void setup() {
		changeBoundsRequestWithSingleEditPart.setEditParts(recordEditPart);
		changeBoundsRequestWithMultipleEditParts.setEditParts(List.of(recordEditPart, indexEditPart, connectorEditPart));
		compoundCommandWithSingleCommand.add(moveSchemaRecordCommand);
		compoundCommandWithTwoCommandsButFirstNotMoveDiagramNodeCommand.add(moveBendpointCommand);
		compoundCommandWithTwoCommandsButFirstNotMoveDiagramNodeCommand.add(moveBendpointCommand);
		compoundCommandWithTwoCommands.add(moveSchemaRecordCommand);
		compoundCommandWithTwoCommands.add(moveBendpointCommand);
		compoundCommandWithTwoCommands.setContext(context);
	}
	
	@Test
	public void originalCommandReturnedWhenNotChangeBoundsRequest() {
		var command = policy.fixCommandIfNeeded(createRequest, createIndexCommand, recordEditPart);
		assertSame(createIndexCommand, command); 
	}
	
	@Test
	public void originalCommandReturnedWhenRequestHasOnlyOnePart() {
		var command = policy.fixCommandIfNeeded(changeBoundsRequestWithSingleEditPart, moveSchemaRecordCommand, recordEditPart);
		assertSame(moveSchemaRecordCommand, command); 
	}
	
	@Test
	public void originalCommandReturnedWhenHostOtherThanSchemaRecordAndConnector() {
		var command = policy.fixCommandIfNeeded(changeBoundsRequestWithMultipleEditParts, moveSchemaRecordCommand, indexEditPart);
		assertSame(moveSchemaRecordCommand, command); 
	}
	
	@Test
	public void originalCommandReturnedWhenCommandNotCompoundCommand() {
		var command = policy.fixCommandIfNeeded(changeBoundsRequestWithMultipleEditParts, moveSchemaRecordCommand, recordEditPart);
		assertSame(moveSchemaRecordCommand, command); 
	}
	
	@Test
	public void originalCommandReturnedWhenCompoundCommandContainsSingleCommand() {
		var command = policy.fixCommandIfNeeded(changeBoundsRequestWithMultipleEditParts, compoundCommandWithSingleCommand, recordEditPart);
		assertSame(compoundCommandWithSingleCommand, command); 
	}
	
	@Test
	public void originalCommandReturnedWhenCompoundCommandsFirstCommandNotMoveDiagramNodeCommand() {
		var command = policy.fixCommandIfNeeded(changeBoundsRequestWithMultipleEditParts, compoundCommandWithTwoCommandsButFirstNotMoveDiagramNodeCommand, recordEditPart);
		assertSame(compoundCommandWithTwoCommandsButFirstNotMoveDiagramNodeCommand, command);
	}
	
	@Test
	public void compoundCommandsFirstCommandReturnedForEditPartHostingRecordWhenMovingRecord() {
		var command = policy.fixCommandIfNeeded(changeBoundsRequestWithMultipleEditParts, compoundCommandWithTwoCommands, recordEditPart);
		assertSame(moveSchemaRecordCommand, command);
		assertSame(context, moveSchemaRecordCommand.getContext());
	}
	
	@Test
	public void compoundCommandsFirstCommandReturnedForEditPartHostingConnectorWhenMovingRecord() {
		var command = policy.fixCommandIfNeeded(changeBoundsRequestWithMultipleEditParts, compoundCommandWithTwoCommands, connectorEditPart);
		assertSame(moveSchemaRecordCommand, command);
		assertSame(context, moveSchemaRecordCommand.getContext());
	}
	
}
