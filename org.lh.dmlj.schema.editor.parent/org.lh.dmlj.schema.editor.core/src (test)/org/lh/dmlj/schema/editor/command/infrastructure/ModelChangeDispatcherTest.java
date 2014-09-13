/**
 * Copyright (C) 2013  Luc Hermans
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
package org.lh.dmlj.schema.editor.command.infrastructure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory.ADD_ITEM;
import static org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory.MOVE_ITEM;
import static org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory.REMOVE_ITEM;
import static org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory.SET_FEATURES;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CompoundCommand;
import org.junit.Test;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.ResizableDiagramNode;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.command.annotation.Features;
import org.lh.dmlj.schema.editor.command.annotation.Item;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.Owner;
import org.lh.dmlj.schema.editor.command.annotation.OwnerType;
import org.lh.dmlj.schema.editor.command.annotation.Reference;

public class ModelChangeDispatcherTest {

	@SuppressWarnings("unchecked")
	private static List<IModelChangeListener> getListeners(ModelChangeDispatcher dispatcher) {		
		try {
			Field field = ModelChangeDispatcher.class.getDeclaredField("listeners");
			field.setAccessible(true);
			return new ArrayList<>(((Collection<IModelChangeListener>) field.get(dispatcher)));
		} catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		
	}	

	@Test
	public void testAddOrRemoveItemNotifier_AddItemCategory() {
	
		// create Command
		AddItemCommand command = new AddItemCommand();
		
		// create mock IModelChangeListener
		IModelChangeListener listener = mock(IModelChangeListener.class);
		
		// create the ModelChangeDispatcher, execute() the add item command and then get the 
		// IModelChangeNotifier 
		ModelChangeDispatcher dispatcher = new ModelChangeDispatcher();		
		command.execute();
		IModelChangeNotifier notifier = 
			dispatcher.getAddOrRemoveItemNotifier(command, ADD_ITEM, CommandStack.POST_EXECUTE);		
		
		// test the notifier, making sure (only) the right method on the listener is called, with 
		// the right arguments --> afterAddItem(...)		
		notifier.doNotify(listener);
		verify(listener, times(1)).afterAddItem(command.owner, command.reference, command.item);		
		verify(listener, never()).afterMoveItem(any(EObject.class), any(EReference.class),
												any(EObject.class), any(EObject.class));
		verify(listener, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
				   								  any(EObject.class));
		verify(listener, never()).afterSetFeatures(any(EObject.class), any(EStructuralFeature[].class));
		
		
		// alter mock CommandStackEvent: POST_UNDO (we do NOT have to undo() the command) and then 
		// get the IModelChangeNotifier		
		notifier = dispatcher.getAddOrRemoveItemNotifier(command, ADD_ITEM, CommandStack.POST_UNDO);
		
		// test the notifier, making sure the right method on the listener is called, with the right
		// arguments --> afterRemoveItem(...)
		notifier.doNotify(listener);
		verify(listener, times(1)).afterAddItem(command.owner, command.reference, command.item);		
		verify(listener, never()).afterMoveItem(any(EObject.class), any(EReference.class),
												any(EObject.class), any(EObject.class));
		verify(listener, times(1)).afterRemoveItem(command.owner, command.reference, command.item);
		verify(listener, never()).afterSetFeatures(any(EObject.class), any(EStructuralFeature[].class));

		// alter mock CommandStackEvent: POST_REDO (we do NOT have to redo() the command) and then 
		// get the IModelChangeNotifier		
		notifier = dispatcher.getAddOrRemoveItemNotifier(command, ADD_ITEM, CommandStack.POST_REDO);
		
		// test the notifier, making sure (only) the right method on the listener is called, with 
		// the right arguments --> afterAddItem(...)		
		notifier.doNotify(listener);
		verify(listener, times(2)).afterAddItem(command.owner, command.reference, command.item);		
		verify(listener, never()).afterMoveItem(any(EObject.class), any(EReference.class),
												any(EObject.class), any(EObject.class));
		verify(listener, times(1)).afterRemoveItem(command.owner, command.reference, command.item);
		verify(listener, never()).afterSetFeatures(any(EObject.class), any(EStructuralFeature[].class));

		// make sure we fail when we try to get a notifier when not after an execute(), undo() or
		// redo()		
		try {
			notifier = 
				dispatcher.getAddOrRemoveItemNotifier(command, ADD_ITEM, CommandStack.PRE_EXECUTE);
			fail("should throw an AssertionFailedException");
		} catch (AssertionFailedException e) {			
			assertEquals("assertion failed: not POST_EXECUTE/POST_UNDO/POST_REDO", e.getMessage());
		}		
		try {
			notifier = 
				dispatcher.getAddOrRemoveItemNotifier(command, ADD_ITEM, CommandStack.PRE_UNDO);
			fail("should throw an AssertionFailedException");
		} catch (AssertionFailedException e) {			
			assertEquals("assertion failed: not POST_EXECUTE/POST_UNDO/POST_REDO", e.getMessage());
		}		
		try {
			notifier = 
				dispatcher.getAddOrRemoveItemNotifier(command, ADD_ITEM, CommandStack.PRE_REDO);
			fail("should throw an AssertionFailedException");
		} catch (AssertionFailedException e) {			
			assertEquals("assertion failed: not POST_EXECUTE/POST_UNDO/POST_REDO", e.getMessage());
		}				
		
	}
	
	@Test
	public void testAddOrRemoveItemNotifier_RemoveItemCategory() {
	
		// create Command
		RemoveItemCommand command = new RemoveItemCommand();
		
		// create mock IModelChangeListener
		IModelChangeListener listener = mock(IModelChangeListener.class);
		
		// create the ModelChangeDispatcher, execute() the add item command and then get the 
		// IModelChangeNotifier 
		ModelChangeDispatcher dispatcher = new ModelChangeDispatcher();		
		command.execute();
		IModelChangeNotifier notifier = 
			dispatcher.getAddOrRemoveItemNotifier(command, REMOVE_ITEM, CommandStack.POST_EXECUTE);		
		
		// test the notifier, making sure (only) the right method on the listener is called, with 
		// the right arguments --> afterRemoveItem(...)		
		notifier.doNotify(listener);
		verify(listener, never()).afterAddItem(any(EObject.class), any(EReference.class), 
					  						   any(EObject.class));
		verify(listener, never()).afterMoveItem(any(EObject.class), any(EReference.class),
												any(EObject.class), any(EObject.class));
		verify(listener, times(1)).afterRemoveItem(command.owner, command.reference, command.item);		
		verify(listener, never()).afterSetFeatures(any(EObject.class), any(EStructuralFeature[].class));

		// alter mock CommandStackEvent: POST_UNDO (we do NOT have to undo() the command) and then 
		// get the IModelChangeNotifier		
		notifier = 
			dispatcher.getAddOrRemoveItemNotifier(command, REMOVE_ITEM, CommandStack.POST_UNDO);
		
		// test the notifier, making sure the right method on the listener is called, with the right
		// arguments --> afterAddItem(...)
		notifier.doNotify(listener);
		verify(listener, times(1)).afterAddItem(command.owner, command.reference, command.item);		
		verify(listener, never()).afterMoveItem(any(EObject.class), any(EReference.class),
												any(EObject.class), any(EObject.class));
		verify(listener, times(1)).afterRemoveItem(command.owner, command.reference, command.item);
		verify(listener, never()).afterSetFeatures(any(EObject.class), any(EStructuralFeature[].class));

		// alter mock CommandStackEvent: POST_REDO (we do NOT have to redo() the command) and then 
		// get the IModelChangeNotifier		
		notifier = 
			dispatcher.getAddOrRemoveItemNotifier(command, REMOVE_ITEM, CommandStack.POST_REDO);
		
		// test the notifier, making sure (only) the right method on the listener is called, with 
		// the right arguments --> afterRemoveItem(...)		
		notifier.doNotify(listener);
		verify(listener, times(1)).afterAddItem(command.owner, command.reference, command.item);		
		verify(listener, never()).afterMoveItem(any(EObject.class), any(EReference.class),
												any(EObject.class), any(EObject.class));
		verify(listener, times(2)).afterRemoveItem(command.owner, command.reference, command.item);
		verify(listener, never()).afterSetFeatures(any(EObject.class), any(EStructuralFeature[].class));

		// make sure we fail when we try to get a notifier when not after an execute(), undo() or
		// redo()		
		try {
			notifier = 
				dispatcher.getAddOrRemoveItemNotifier(command, REMOVE_ITEM, CommandStack.PRE_EXECUTE);
			fail("should throw an AssertionFailedException");
		} catch (AssertionFailedException e) {			
			assertEquals("assertion failed: not POST_EXECUTE/POST_UNDO/POST_REDO", e.getMessage());
		}		
		try {
			notifier = 
				dispatcher.getAddOrRemoveItemNotifier(command, REMOVE_ITEM, CommandStack.PRE_UNDO);
			fail("should throw an AssertionFailedException");
		} catch (AssertionFailedException e) {			
			assertEquals("assertion failed: not POST_EXECUTE/POST_UNDO/POST_REDO", e.getMessage());
		}		
		try {
			notifier = 
				dispatcher.getAddOrRemoveItemNotifier(command, REMOVE_ITEM, CommandStack.PRE_REDO);
			fail("should throw an AssertionFailedException");
		} catch (AssertionFailedException e) {			
			assertEquals("assertion failed: not POST_EXECUTE/POST_UNDO/POST_REDO", e.getMessage());
		}		
		
	}	
	
	@Test
	public void testDispatch_CompoundCommand() {
		
		// create the ModelChangeDispatcher
		ModelChangeDispatcher dispatcher = new ModelChangeDispatcher();	
		
		// create a mock IModelChangeListener and add it to the model change dispatcher's listeners
		IModelChangeListener listener = mock(IModelChangeListener.class);
		dispatcher.addModelChangeListener(listener);
		
		// simulate a PRE_EXECUTE command stack notification using a real CompoundCommand (composed
		// of 2 mock commands) and a mock CommandStackEvent...
		Command command1 = mock(Command.class);
		Command command2 = mock(Command.class);
		CompoundCommand compoundCommand = new CompoundCommand("test compound command");
		compoundCommand.add(command1);
		compoundCommand.add(command2);
		CommandStackEvent event = mock(CommandStackEvent.class);
		when(event.getDetail()).thenReturn(CommandStack.PRE_EXECUTE);	
		when(event.getCommand()).thenReturn(compoundCommand);
		dispatcher.dispatch(event);		
		
		// ...no listener method should have been called
		verify(listener, never()).afterAddItem(any(EObject.class), any(EReference.class), 
				   							   any(EObject.class));		
		verify(listener, never()).afterMoveItem(any(EObject.class), any(EReference.class), 
												anyObject(), any(EObject.class));
		verify(listener, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
				  								  any(EObject.class));
		verify(listener, never()).afterSetFeatures(any(EObject.class), any(EStructuralFeature[].class));		

		// simulate a PRE_REDO command stack notification
		when(event.getDetail()).thenReturn(CommandStack.PRE_REDO);		
		dispatcher.dispatch(event);

		// ...no listener method should have been called
		verify(listener, never()).afterAddItem(any(EObject.class), any(EReference.class), 
											   any(EObject.class));		
		verify(listener, never()).afterMoveItem(any(EObject.class), any(EReference.class), 
												anyObject(), any(EObject.class));
		verify(listener, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
				  								  any(EObject.class));
		verify(listener, never()).afterSetFeatures(any(EObject.class), any(EStructuralFeature[].class));

		// simulate a PRE_UNDO command stack notification
		when(event.getDetail()).thenReturn(CommandStack.PRE_UNDO);		
		dispatcher.dispatch(event);

		// ...no listener method should have been called
		verify(listener, never()).afterAddItem(any(EObject.class), any(EReference.class), 
											   any(EObject.class));		
		verify(listener, never()).afterMoveItem(any(EObject.class), any(EReference.class), 
												anyObject(), any(EObject.class));
		verify(listener, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
				  								  any(EObject.class));
		verify(listener, never()).afterSetFeatures(any(EObject.class), any(EStructuralFeature[].class));

		
		// simulate a POST_EXECUTE command stack notification				
		when(event.getDetail()).thenReturn(CommandStack.POST_EXECUTE);		
		dispatcher.dispatch(event);

		// ...no listener method should have been called (because all commands are missing the
		// @ModelChange annotation)
		verify(listener, never()).afterAddItem(any(EObject.class), any(EReference.class), 
											   any(EObject.class));		
		verify(listener, never()).afterMoveItem(any(EObject.class), any(EReference.class), 
												anyObject(), any(EObject.class));
		verify(listener, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
				  								  any(EObject.class));
		verify(listener, never()).afterSetFeatures(any(EObject.class), any(EStructuralFeature[].class));
		
		
		// simulate a POST_EXECUTE command stack notification using a compound command containing a
		// command annotated with @ModelChange(category=MOVE_ITEM) and a command annotated with
		// @ModelChange(category=SET_ATTRIBUTES)...
		MoveItemCommand moveItemCommand = new MoveItemCommand();
		SetFeaturesCommand setAttributeCommand = new SetFeaturesCommand();		
		compoundCommand = new CompoundCommand("test compound command");
		compoundCommand.add(moveItemCommand);
		compoundCommand.add(setAttributeCommand);
		when(event.getCommand()).thenReturn(compoundCommand);
		when(event.getDetail()).thenReturn(CommandStack.POST_EXECUTE);		
		dispatcher.dispatch(event);

		// ...and verify that the right listener methods have been called (we cannot check the 
		// order in which they were called but this order is not really relevant since the model
		// will contain the changes from BOTH commands on either method call)
		verify(listener, never()).afterAddItem(any(EObject.class), any(EReference.class), 
											   any(EObject.class));		
		verify(listener, times(1)).afterMoveItem(moveItemCommand.oldOwner, moveItemCommand.reference, 
												 moveItemCommand.item, moveItemCommand.newOwner);
		verify(listener, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
				  								  any(EObject.class));
		verify(listener, times(1)).afterSetFeatures(setAttributeCommand.owner, 
													  setAttributeCommand.features);
		
		// simulate a POST_UNDO command stack notification using the same compound command		
		when(event.getDetail()).thenReturn(CommandStack.POST_UNDO);		
		dispatcher.dispatch(event);

		// ...and verify that the right listener methods have been called (we cannot check the 
		// order in which they were called but this order is not really relevant since the model
		// will contain the changes from BOTH commands on either method call)
		verify(listener, never()).afterAddItem(any(EObject.class), any(EReference.class), 
											   any(EObject.class));		
		verify(listener, times(1)).afterMoveItem(moveItemCommand.oldOwner, moveItemCommand.reference, 
												 moveItemCommand.item, moveItemCommand.newOwner);
		verify(listener, times(1)).afterMoveItem(moveItemCommand.newOwner, moveItemCommand.reference, 
				 								 moveItemCommand.item, moveItemCommand.oldOwner);
		verify(listener, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
				  								  any(EObject.class));
		verify(listener, times(2)).afterSetFeatures(setAttributeCommand.owner, 
													  setAttributeCommand.features);
		
		// simulate a POST_REDO command stack notification using the same compound command		
		when(event.getDetail()).thenReturn(CommandStack.POST_REDO);		
		dispatcher.dispatch(event);

		// ...and verify that the right listener methods have been called (we cannot check the 
		// order in which they were called but this order is not really relevant since the model
		// will contain the changes from BOTH commands on either method call)
		verify(listener, never()).afterAddItem(any(EObject.class), any(EReference.class), 
											   any(EObject.class));		
		verify(listener, times(2)).afterMoveItem(moveItemCommand.oldOwner, moveItemCommand.reference, 
												 moveItemCommand.item, moveItemCommand.newOwner);
		verify(listener, times(1)).afterMoveItem(moveItemCommand.newOwner, moveItemCommand.reference, 
				 								 moveItemCommand.item, moveItemCommand.oldOwner);
		verify(listener, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
				  								  any(EObject.class));
		verify(listener, times(3)).afterSetFeatures(setAttributeCommand.owner, 
													  setAttributeCommand.features);		
		
	}
	
	@Test
	public void testDispatch_NestedCompoundCommand() {
		
		// under certain circumstances, GEF will wrap commands in compound commands, which can 
		// result in nested compound commands; if the event command denotes a compound command that
		// is composed of 1 (and only 1) compound command, the commands making up the inner compound 
		// command have to be dispatched 
		
		// create the ModelChangeDispatcher
		ModelChangeDispatcher dispatcher = new ModelChangeDispatcher();	
		
		// create a mock IModelChangeListener and add it to the model change dispatcher's listeners
		IModelChangeListener listener = mock(IModelChangeListener.class);
		dispatcher.addModelChangeListener(listener);
		
		// simulate a PRE_EXECUTE command stack notification using a real CompoundCommand (composed 
		// of another compund command that is composed of 2 mock commands) and a mock 
		// CommandStackEvent...
		Command command1 = mock(Command.class);
		Command command2 = mock(Command.class);
		CompoundCommand innerCompoundCommand = new CompoundCommand("inner compound command");
		innerCompoundCommand.add(command1);
		innerCompoundCommand.add(command2);
		CompoundCommand outerCompoundCommand = new CompoundCommand("outer compound command");
		outerCompoundCommand.add(innerCompoundCommand);
		CommandStackEvent event = mock(CommandStackEvent.class);
		when(event.getDetail()).thenReturn(CommandStack.PRE_EXECUTE);	
		when(event.getCommand()).thenReturn(outerCompoundCommand);
		dispatcher.dispatch(event);		
		
		// ...no listener method should have been called
		verify(listener, never()).afterAddItem(any(EObject.class), any(EReference.class), 
				   							   any(EObject.class));		
		verify(listener, never()).afterMoveItem(any(EObject.class), any(EReference.class), 
												anyObject(), any(EObject.class));
		verify(listener, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
				  								  any(EObject.class));
		verify(listener, never()).afterSetFeatures(any(EObject.class), any(EStructuralFeature[].class));		

		// simulate a PRE_REDO command stack notification
		when(event.getDetail()).thenReturn(CommandStack.PRE_REDO);		
		dispatcher.dispatch(event);

		// ...no listener method should have been called
		verify(listener, never()).afterAddItem(any(EObject.class), any(EReference.class), 
											   any(EObject.class));		
		verify(listener, never()).afterMoveItem(any(EObject.class), any(EReference.class), 
												anyObject(), any(EObject.class));
		verify(listener, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
				  								  any(EObject.class));
		verify(listener, never()).afterSetFeatures(any(EObject.class), any(EStructuralFeature[].class));

		// simulate a PRE_UNDO command stack notification
		when(event.getDetail()).thenReturn(CommandStack.PRE_UNDO);		
		dispatcher.dispatch(event);

		// ...no listener method should have been called
		verify(listener, never()).afterAddItem(any(EObject.class), any(EReference.class), 
											   any(EObject.class));		
		verify(listener, never()).afterMoveItem(any(EObject.class), any(EReference.class), 
												anyObject(), any(EObject.class));
		verify(listener, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
				  								  any(EObject.class));
		verify(listener, never()).afterSetFeatures(any(EObject.class), any(EStructuralFeature[].class));

		
		// simulate a POST_EXECUTE command stack notification				
		when(event.getDetail()).thenReturn(CommandStack.POST_EXECUTE);		
		dispatcher.dispatch(event);

		// ...no listener method should have been called (because all commands are missing the
		// @ModelChange annotation)
		verify(listener, never()).afterAddItem(any(EObject.class), any(EReference.class), 
											   any(EObject.class));		
		verify(listener, never()).afterMoveItem(any(EObject.class), any(EReference.class), 
												anyObject(), any(EObject.class));
		verify(listener, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
				  								  any(EObject.class));
		verify(listener, never()).afterSetFeatures(any(EObject.class), any(EStructuralFeature[].class));
		
		
		// simulate a POST_EXECUTE command stack notification using a compound command containing a
		// command annotated with @ModelChange(category=MOVE_ITEM) and a command annotated with
		// @ModelChange(category=SET_ATTRIBUTES)...
		MoveItemCommand moveItemCommand = new MoveItemCommand();
		SetFeaturesCommand setAttributeCommand = new SetFeaturesCommand();		
		outerCompoundCommand = new CompoundCommand("test compound command");
		outerCompoundCommand.add(moveItemCommand);
		outerCompoundCommand.add(setAttributeCommand);
		when(event.getCommand()).thenReturn(outerCompoundCommand);
		when(event.getDetail()).thenReturn(CommandStack.POST_EXECUTE);		
		dispatcher.dispatch(event);

		// ...and verify that the right listener methods have been called (we cannot check the 
		// order in which they were called but this order is not really relevant since the model
		// will contain the changes from BOTH commands on either method call)
		verify(listener, never()).afterAddItem(any(EObject.class), any(EReference.class), 
											   any(EObject.class));		
		verify(listener, times(1)).afterMoveItem(moveItemCommand.oldOwner, moveItemCommand.reference, 
												 moveItemCommand.item, moveItemCommand.newOwner);
		verify(listener, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
				  								  any(EObject.class));
		verify(listener, times(1)).afterSetFeatures(setAttributeCommand.owner, 
													setAttributeCommand.features);
		
		// simulate a POST_UNDO command stack notification using the same compound command		
		when(event.getDetail()).thenReturn(CommandStack.POST_UNDO);		
		dispatcher.dispatch(event);

		// ...and verify that the right listener methods have been called (we cannot check the 
		// order in which they were called but this order is not really relevant since the model
		// will contain the changes from BOTH commands on either method call)
		verify(listener, never()).afterAddItem(any(EObject.class), any(EReference.class), 
											   any(EObject.class));		
		verify(listener, times(1)).afterMoveItem(moveItemCommand.oldOwner, moveItemCommand.reference, 
												 moveItemCommand.item, moveItemCommand.newOwner);
		verify(listener, times(1)).afterMoveItem(moveItemCommand.newOwner, moveItemCommand.reference, 
				 								 moveItemCommand.item, moveItemCommand.oldOwner);
		verify(listener, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
				  								  any(EObject.class));
		verify(listener, times(2)).afterSetFeatures(setAttributeCommand.owner, 
													setAttributeCommand.features);
		
		// simulate a POST_REDO command stack notification using the same compound command		
		when(event.getDetail()).thenReturn(CommandStack.POST_REDO);		
		dispatcher.dispatch(event);

		// ...and verify that the right listener methods have been called (we cannot check the 
		// order in which they were called but this order is not really relevant since the model
		// will contain the changes from BOTH commands on either method call)
		verify(listener, never()).afterAddItem(any(EObject.class), any(EReference.class), 
											   any(EObject.class));		
		verify(listener, times(2)).afterMoveItem(moveItemCommand.oldOwner, moveItemCommand.reference, 
												 moveItemCommand.item, moveItemCommand.newOwner);
		verify(listener, times(1)).afterMoveItem(moveItemCommand.newOwner, moveItemCommand.reference, 
				 								 moveItemCommand.item, moveItemCommand.oldOwner);
		verify(listener, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
				  								  any(EObject.class));
		verify(listener, times(3)).afterSetFeatures(setAttributeCommand.owner, 
													setAttributeCommand.features);		
		
	}

	@Test
	public void testDispatch_SimpleCommand() {
		
		// create the ModelChangeDispatcher
		ModelChangeDispatcher dispatcher = new ModelChangeDispatcher();									
		
		// create a mock IModelChangeListener and add it to the model change dispatcher's listeners
		IModelChangeListener listener = mock(IModelChangeListener.class);
		dispatcher.addModelChangeListener(listener);
		
		// simulate a PRE_EXECUTE command stack notification using a mock Command and 
		// CommandStackEvent...
		Command command = mock(Command.class);
		CommandStackEvent event = mock(CommandStackEvent.class);
		when(event.getDetail()).thenReturn(CommandStack.PRE_EXECUTE);	
		when(event.getCommand()).thenReturn(command);
		dispatcher.dispatch(event);

		// ...no listener method should have been called
		verify(listener, never()).afterAddItem(any(EObject.class), any(EReference.class), 
				   								   any(EObject.class));		
		verify(listener, never()).afterMoveItem(any(EObject.class), any(EReference.class), 
												anyObject(), any(EObject.class));
		verify(listener, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
				  								  any(EObject.class));
		verify(listener, never()).afterSetFeatures(any(EObject.class), any(EStructuralFeature[].class));

		// simulate a PRE_REDO command stack notification using a mock Command and 
		// CommandStackEvent...				
		when(event.getDetail()).thenReturn(CommandStack.PRE_REDO);		
		dispatcher.dispatch(event);

		// ...no listener method should have been called
		verify(listener, never()).afterAddItem(any(EObject.class), any(EReference.class), 
											   any(EObject.class));		
		verify(listener, never()).afterMoveItem(any(EObject.class), any(EReference.class), 
												anyObject(), any(EObject.class));
		verify(listener, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
				  								  any(EObject.class));
		verify(listener, never()).afterSetFeatures(any(EObject.class), any(EStructuralFeature[].class));

		// simulate a PRE_UNDO command stack notification using a mock Command and 
		// CommandStackEvent...				
		when(event.getDetail()).thenReturn(CommandStack.PRE_UNDO);		
		dispatcher.dispatch(event);

		// ...no listener method should have been called
		verify(listener, never()).afterAddItem(any(EObject.class), any(EReference.class), 
											   any(EObject.class));		
		verify(listener, never()).afterMoveItem(any(EObject.class), any(EReference.class), 
												anyObject(), any(EObject.class));
		verify(listener, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
				  								  any(EObject.class));
		verify(listener, never()).afterSetFeatures(any(EObject.class), any(EStructuralFeature[].class));

		
		// simulate a POST_EXECUTE command stack notification using a mock Command and 
		// CommandStackEvent...				
		when(event.getDetail()).thenReturn(CommandStack.POST_EXECUTE);		
		dispatcher.dispatch(event);

		// ...no listener method should have been called (because the command is missing the
		// @ModelChange annotation)
		verify(listener, never()).afterAddItem(any(EObject.class), any(EReference.class), 
											   any(EObject.class));		
		verify(listener, never()).afterMoveItem(any(EObject.class), any(EReference.class), 
												anyObject(), any(EObject.class));
		verify(listener, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
				  								  any(EObject.class));
		verify(listener, never()).afterSetFeatures(any(EObject.class), any(EStructuralFeature[].class));

		
		// simulate a POST_EXECUTE command stack notification using a command annotated with  
		// @ModelChange(category=SET_ATTRIBUTES)...
		SetFeaturesCommand setAttributeCommand = new SetFeaturesCommand();
		when(event.getCommand()).thenReturn(setAttributeCommand);
		when(event.getDetail()).thenReturn(CommandStack.POST_EXECUTE);		
		dispatcher.dispatch(event);

		// ...and verify that the right listener method has been called
		verify(listener, never()).afterAddItem(any(EObject.class), any(EReference.class), 
											   any(EObject.class));		
		verify(listener, never()).afterMoveItem(any(EObject.class), any(EReference.class), 
												anyObject(), any(EObject.class));
		verify(listener, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
				  								  any(EObject.class));
		verify(listener, times(1)).afterSetFeatures(setAttributeCommand.owner, 
													  setAttributeCommand.features);

		
		// simulate a POST_EXECUTE command stack notification using a command annotated with  
		// @ModelChange(category=ADD_ITEM)...
		AddItemCommand addItemCommand = new AddItemCommand();
		when(event.getCommand()).thenReturn(addItemCommand);
		dispatcher.dispatch(event);

		// ...and verify that the right listener method has been called
		verify(listener, times(1)).afterAddItem(addItemCommand.owner, 
												addItemCommand.reference, 
												addItemCommand.item);		
		verify(listener, never()).afterMoveItem(any(EObject.class), any(EReference.class), 
												anyObject(), any(EObject.class));
		verify(listener, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
				  								  any(EObject.class));
		verify(listener, times(1)).afterSetFeatures(setAttributeCommand.owner, 
													 setAttributeCommand.features);


		// simulate a POST_EXECUTE command stack notification using a command annotated with  
		// @ModelChange(category=REMOVE_ITEM)...
		RemoveItemCommand removeItemCommand = new RemoveItemCommand();
		when(event.getCommand()).thenReturn(removeItemCommand);
		dispatcher.dispatch(event);

		// ...and verify that the right listener method has been called
		verify(listener, times(1)).afterAddItem(addItemCommand.owner, 
												addItemCommand.reference, 
												addItemCommand.item);		
		verify(listener, never()).afterMoveItem(any(EObject.class), any(EReference.class), 
												anyObject(), any(EObject.class));
		verify(listener, times(1)).afterRemoveItem(removeItemCommand.owner, 
												   removeItemCommand.reference, 
												   removeItemCommand.item);
		verify(listener, times(1)).afterSetFeatures(setAttributeCommand.owner, 
				 									  setAttributeCommand.features);

		
		// simulate a POST_EXECUTE command stack notification using a command annotated with  
		// @ModelChange(category=MOVE_ITEM)...
		MoveItemCommand moveItemCommand = new MoveItemCommand();
		when(event.getCommand()).thenReturn(moveItemCommand);
		dispatcher.dispatch(event);

		// ...and verify that the right listener method has been called
		verify(listener, times(1)).afterAddItem(addItemCommand.owner, 
												addItemCommand.reference, 
												addItemCommand.item);		
		verify(listener, times(1)).afterMoveItem(moveItemCommand.oldOwner, 
				 								 moveItemCommand.reference, 
				 								 moveItemCommand.item, 
				 								 moveItemCommand.newOwner);	
		verify(listener, times(1)).afterRemoveItem(removeItemCommand.owner, 
												   removeItemCommand.reference, 
												   removeItemCommand.item);
		verify(listener, times(1)).afterSetFeatures(setAttributeCommand.owner, 
				 									  setAttributeCommand.features);
		

		// simulate a POST_UNDO command stack notification using a command annotated with  
		// @ModelChange(category=SET_ATTRIBUTES)...
		when(event.getCommand()).thenReturn(setAttributeCommand);
		when(event.getDetail()).thenReturn(CommandStack.POST_UNDO);		
		dispatcher.dispatch(event);

		// ...and verify that the right listener method has been called
		verify(listener, times(1)).afterAddItem(addItemCommand.owner, 
												addItemCommand.reference, 
												addItemCommand.item);			
		verify(listener, times(1)).afterMoveItem(moveItemCommand.oldOwner, 
				 								 moveItemCommand.reference, 
				 								 moveItemCommand.item, 
				 								 moveItemCommand.newOwner);	
		verify(listener, times(1)).afterRemoveItem(removeItemCommand.owner, 
				   								   removeItemCommand.reference, 
				   								   removeItemCommand.item);
		verify(listener, times(2)).afterSetFeatures(setAttributeCommand.owner, 
				 									  setAttributeCommand.features);

		
		// simulate a POST_UNDO command stack notification using a command annotated with  
		// @ModelChange(category=ADD_ITEM)...
		when(event.getCommand()).thenReturn(addItemCommand);
		dispatcher.dispatch(event);

		// ...and verify that the right listener method has been called
		verify(listener, times(1)).afterAddItem(addItemCommand.owner, 
												addItemCommand.reference, 
												addItemCommand.item);		
		verify(listener, times(1)).afterMoveItem(moveItemCommand.oldOwner, 
				 								 moveItemCommand.reference, 
				 								 moveItemCommand.item, 
				 								 moveItemCommand.newOwner);	
		verify(listener, times(1)).afterRemoveItem(removeItemCommand.owner, 
				   								   removeItemCommand.reference, 
				   								   removeItemCommand.item);
		verify(listener, times(1)).afterRemoveItem(addItemCommand.owner, 
				   								   addItemCommand.reference, 
				   								   addItemCommand.item);
		verify(listener, times(2)).afterSetFeatures(setAttributeCommand.owner, 
				  									  setAttributeCommand.features);


		// simulate a POST_UNDO command stack notification using a command annotated with  
		// @ModelChange(category=REMOVE_ITEM)...
		when(event.getCommand()).thenReturn(removeItemCommand);
		dispatcher.dispatch(event);

		// ...and verify that the right listener method has been called
		verify(listener, times(1)).afterAddItem(addItemCommand.owner, 
												addItemCommand.reference, 
												addItemCommand.item);		
		verify(listener, times(1)).afterAddItem(removeItemCommand.owner, 
												removeItemCommand.reference, 
												removeItemCommand.item);		
		verify(listener, times(1)).afterMoveItem(moveItemCommand.oldOwner, 
				 								 moveItemCommand.reference, 
				 								 moveItemCommand.item, 
				 								 moveItemCommand.newOwner);	
		verify(listener, times(1)).afterRemoveItem(removeItemCommand.owner, 
												   removeItemCommand.reference, 
												   removeItemCommand.item);
		verify(listener, times(1)).afterRemoveItem(addItemCommand.owner, 
				   								   addItemCommand.reference, 
				   								   addItemCommand.item);
		verify(listener, times(2)).afterSetFeatures(setAttributeCommand.owner, 
				  									  setAttributeCommand.features);

		
		// simulate a POST_UNDO command stack notification using a command annotated with  
		// @ModelChange(category=MOVE_ITEM)...
		when(event.getCommand()).thenReturn(moveItemCommand);
		dispatcher.dispatch(event);

		// ...and verify that the right listener method has been called
		verify(listener, times(1)).afterAddItem(addItemCommand.owner, 
												addItemCommand.reference, 
												addItemCommand.item);		
		verify(listener, times(1)).afterAddItem(removeItemCommand.owner, 
												removeItemCommand.reference, 
												removeItemCommand.item);		
		verify(listener, times(1)).afterMoveItem(moveItemCommand.oldOwner, 
				 								 moveItemCommand.reference, 
				 								 moveItemCommand.item, 
				 								 moveItemCommand.newOwner);
		verify(listener, times(1)).afterMoveItem(moveItemCommand.newOwner, 
				 								 moveItemCommand.reference, 
				 								 moveItemCommand.item, 
				 								 moveItemCommand.oldOwner);
		verify(listener, times(1)).afterRemoveItem(removeItemCommand.owner, 
												   removeItemCommand.reference, 
												   removeItemCommand.item);
		verify(listener, times(1)).afterRemoveItem(addItemCommand.owner, 
				   								   addItemCommand.reference, 
				   								   addItemCommand.item);
		verify(listener, times(2)).afterSetFeatures(setAttributeCommand.owner, 
				  									  setAttributeCommand.features);

		
		// simulate a POST_REDO command stack notification using a command annotated with  
		// @ModelChange(category=SET_ATTRIBUTES)...
		when(event.getCommand()).thenReturn(setAttributeCommand);
		when(event.getDetail()).thenReturn(CommandStack.POST_REDO);		
		dispatcher.dispatch(event);

		// ...and verify that the right listener method has been called	
		verify(listener, times(1)).afterAddItem(addItemCommand.owner, 
												addItemCommand.reference, 
												addItemCommand.item);		
		verify(listener, times(1)).afterAddItem(removeItemCommand.owner, 
											    removeItemCommand.reference, 
											    removeItemCommand.item);		
		verify(listener, times(1)).afterMoveItem(moveItemCommand.oldOwner, 
				 								 moveItemCommand.reference, 
				 								 moveItemCommand.item, 
				 								 moveItemCommand.newOwner);
		verify(listener, times(1)).afterMoveItem(moveItemCommand.newOwner, 
				 								 moveItemCommand.reference, 
				 								 moveItemCommand.item,	 
				 								 moveItemCommand.oldOwner);		
		verify(listener, times(1)).afterRemoveItem(removeItemCommand.owner, 
												   removeItemCommand.reference, 
												   removeItemCommand.item);
		verify(listener, times(1)).afterRemoveItem(addItemCommand.owner, 
				   								   addItemCommand.reference, 
				   								   addItemCommand.item);
		verify(listener, times(3)).afterSetFeatures(setAttributeCommand.owner, 
				  									  setAttributeCommand.features);

		
		// simulate a POST_REDO command stack notification using a command annotated with  
		// @ModelChange(category=ADD_ITEM)...
		when(event.getCommand()).thenReturn(addItemCommand);
		dispatcher.dispatch(event);

		// ...and verify that the right listener method has been called	
		verify(listener, times(2)).afterAddItem(addItemCommand.owner, 
												addItemCommand.reference, 
												addItemCommand.item);		
		verify(listener, times(1)).afterAddItem(removeItemCommand.owner, 
												removeItemCommand.reference, 
												removeItemCommand.item);		
		verify(listener, times(1)).afterMoveItem(moveItemCommand.oldOwner, 
				 								 moveItemCommand.reference, 
				 								 moveItemCommand.item, 
				 								 moveItemCommand.newOwner);
		verify(listener, times(1)).afterMoveItem(moveItemCommand.newOwner, 
				 								 moveItemCommand.reference, 
				 								 moveItemCommand.item,	 
				 								 moveItemCommand.oldOwner);		
		verify(listener, times(1)).afterRemoveItem(removeItemCommand.owner, 
												   removeItemCommand.reference, 
												   removeItemCommand.item);
		verify(listener, times(1)).afterRemoveItem(addItemCommand.owner, 
				   								   addItemCommand.reference, 
				   								   addItemCommand.item);
		verify(listener, times(3)).afterSetFeatures(setAttributeCommand.owner, 
				  									  setAttributeCommand.features);

		
		// simulate a POST_REDO command stack notification using a command annotated with  
		// @ModelChange(category=REMOVE_ITEM)...
		when(event.getCommand()).thenReturn(removeItemCommand);
		dispatcher.dispatch(event);

		// ...and verify that the right listener method has been called
		verify(listener, times(2)).afterAddItem(addItemCommand.owner, 
												addItemCommand.reference, 
												addItemCommand.item);		
		verify(listener, times(1)).afterAddItem(removeItemCommand.owner, 
												removeItemCommand.reference, 
												removeItemCommand.item);		
		verify(listener, times(1)).afterMoveItem(moveItemCommand.oldOwner, 
				 								 moveItemCommand.reference, 
				 								 moveItemCommand.item, 
				 								 moveItemCommand.newOwner);
		verify(listener, times(1)).afterMoveItem(moveItemCommand.newOwner, 
				 								 moveItemCommand.reference, 
				 								 moveItemCommand.item,	 
				 								 moveItemCommand.oldOwner);		
		verify(listener, times(2)).afterRemoveItem(removeItemCommand.owner, 
												   removeItemCommand.reference, 
												   removeItemCommand.item);
		verify(listener, times(1)).afterRemoveItem(addItemCommand.owner, 
				   								   addItemCommand.reference, 
				   								   addItemCommand.item);
		verify(listener, times(3)).afterSetFeatures(setAttributeCommand.owner, 
													  setAttributeCommand.features);

		
		// simulate a POST_REDO command stack notification using a command annotated with  
		// @ModelChange(category=MOVE_ITEM)...
		when(event.getCommand()).thenReturn(moveItemCommand);
		dispatcher.dispatch(event);

		// ...and verify that the right listener method has been called	
		verify(listener, times(2)).afterAddItem(addItemCommand.owner, 
											    addItemCommand.reference, 
											    addItemCommand.item);		
		verify(listener, times(1)).afterAddItem(removeItemCommand.owner, 
												removeItemCommand.reference, 
												removeItemCommand.item);		
		verify(listener, times(2)).afterMoveItem(moveItemCommand.oldOwner, 
				 								 moveItemCommand.reference, 
				 								 moveItemCommand.item, 
				 								 moveItemCommand.newOwner);
		verify(listener, times(1)).afterMoveItem(moveItemCommand.newOwner, 
				 								 moveItemCommand.reference, 
				 								 moveItemCommand.item,	 
				 								 moveItemCommand.oldOwner);			
		verify(listener, times(2)).afterRemoveItem(removeItemCommand.owner, 
												   removeItemCommand.reference, 
												   removeItemCommand.item);
		verify(listener, times(1)).afterRemoveItem(addItemCommand.owner, 
				   								   addItemCommand.reference, 
				   								   addItemCommand.item);
		verify(listener, times(3)).afterSetFeatures(setAttributeCommand.owner, 
				  									  setAttributeCommand.features);


		// unregister the model change listener
		dispatcher.removeModelChangeListener(listener);		
		
	}
	
	@Test
	public void testDispatch_ObsoleteListener() {
		
		// create the ModelChangeDispatcher
		final ModelChangeDispatcher dispatcher = new ModelChangeDispatcher();									
		
		// create 1 mock and 1 real IModelChangeListener and add them both to the model change 
		// dispatcher's listeners - the order in which they are registered matters because listener1
		// has to remove listener2 before that one gets notified
		final IModelChangeListener listener2 = mock(IModelChangeListener.class);
		IModelChangeListener listener1 = new IModelChangeListener() {			
			@Override
			public void afterRemoveItem(EObject owner, EReference reference, Object item) {
				// remove listener2; no more methods should be called on it as of NOW ! 
				dispatcher.removeModelChangeListener(listener2);
				// check that the dispatcher has only 1 registered listener, and that for (only) 
				// the second listener, isObsolete() returns true
				List<IModelChangeListener> listeners = getListeners(dispatcher);				
				assertEquals(1, listeners.size());
				assertSame(this, listeners.get(0)); // 'this' refers to listener1
				assertFalse(dispatcher.isObsolete(this));
				assertTrue(dispatcher.isObsolete(listener2));
			}			
			@Override
			public void afterMoveItem(EObject oldOwner, EReference reference,  Object item, 
									  EObject newOwner) {
				throw new RuntimeException("method should not be called: afterMoveItem");				
			}			
			@Override
			public void afterAddItem(EObject owner, EReference reference, Object item) {
				throw new RuntimeException("method should not be called: afterAddItem");				
			}
			@Override
			public void afterSetFeatures(EObject owner, EStructuralFeature[] attributes) {
				throw new RuntimeException("method should not be called: afterSetFeatures");				
			}
		};
		dispatcher.addModelChangeListener(listener1);
		dispatcher.addModelChangeListener(listener2);
		
		// simulate a POST_EXECUTE command stack notification		
		RemoveItemCommand removeItemCommand = new RemoveItemCommand();
		CommandStackEvent event = mock(CommandStackEvent.class);						
		when(event.getCommand()).thenReturn(removeItemCommand);
		when(event.getDetail()).thenReturn(CommandStack.POST_EXECUTE);		
		dispatcher.dispatch(event);			

		// ...no method should have been called on the second listener because it was made obsolete
		// when notifying the first listener
		verify(listener2, never()).afterMoveItem(any(EObject.class), any(EReference.class), 
				 								 anyObject(), any(EObject.class));		
		verify(listener2, never()).afterAddItem(any(EObject.class), any(EReference.class), 
											    any(EObject.class));		
		verify(listener2, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
				  								   any(EObject.class));
		verify(listener2, never()).afterSetFeatures(any(EObject.class), any(EStructuralFeature[].class));

		// check that the dispatcher has only 1 registered listener, namely our first listener, and 
		// that for that listener, isObsolete() returns false
		List<IModelChangeListener> listeners = getListeners(dispatcher);				
		assertEquals(1, listeners.size());
		assertSame(listener1, listeners.get(0));
		assertFalse(dispatcher.isObsolete(listener1));		
		
		// unregister the model change listener
		dispatcher.removeModelChangeListener(listener1);		
		
	}
	
	@Test
	public void testDispatch_DeferredListener() {
		
		// create the ModelChangeDispatcher
		final ModelChangeDispatcher dispatcher = new ModelChangeDispatcher();									
		
		// create a mock and a real IModelChangeListener and add only the real one to the model 
		// change dispatcher's listeners; when its addItem method is called, it adds the second 
		// listener, which should NOT be notified during the dispatch processing
		final IModelChangeListener listener2 = mock(IModelChangeListener.class);
		IModelChangeListener listener1 = new IModelChangeListener() {			
			@Override
			public void afterAddItem(EObject owner, EReference reference, Object item) {
				// add listener2				
				dispatcher.addModelChangeListener(listener2);
				// check that the dispatcher has 2 registered listeners, neither of them are 
				// obsolete of course, but let's check that as well
				List<IModelChangeListener> listeners = getListeners(dispatcher);				
				assertEquals(2, listeners.size());
				assertSame(this, listeners.get(0)); 		// 'this' refers to listener1
				assertSame(listener2, listeners.get(1));
				assertFalse(dispatcher.isObsolete(this));
				assertFalse(dispatcher.isObsolete(listener2));				
			}			
			@Override
			public void afterRemoveItem(EObject owner, EReference reference, Object item) {
				throw new RuntimeException("method should not be called: afterAddItem");				
			}
			@Override
			public void afterMoveItem(EObject oldOwner, EReference reference,  Object item, 
									  EObject newOwner) {
				throw new RuntimeException("method should not be called: afterMoveItem");				
			}			
			@Override
			public void afterSetFeatures(EObject owner, EStructuralFeature[] attributes) {
				throw new RuntimeException("method should not be called: afterSetFeatures");				
			}
		};
		dispatcher.addModelChangeListener(listener1); // only add the first listener !		
		
		// simulate a POST_EXECUTE command stack notification		
		AddItemCommand removeItemCommand = new AddItemCommand();
		CommandStackEvent event = mock(CommandStackEvent.class);						
		when(event.getCommand()).thenReturn(removeItemCommand);
		when(event.getDetail()).thenReturn(CommandStack.POST_EXECUTE);		
		dispatcher.dispatch(event);			

		// ...no method should have been called on the second listener because it was registered
		// during and not before the dispatch processing
		verify(listener2, never()).afterMoveItem(any(EObject.class), any(EReference.class), 
				 								 anyObject(), any(EObject.class));		
		verify(listener2, never()).afterAddItem(any(EObject.class), any(EReference.class), 
											    any(EObject.class));		
		verify(listener2, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
				  								   any(EObject.class));
		verify(listener2, never()).afterSetFeatures(any(EObject.class), 
													any(EStructuralFeature[].class));

		// check that the dispatcher still has 2 registered listeners and that neither of them is		
		// obsolete
		List<IModelChangeListener> listeners = getListeners(dispatcher);				
		assertEquals(2, listeners.size());
		assertSame(listener1, listeners.get(0));
		assertSame(listener2, listeners.get(1));
		assertFalse(dispatcher.isObsolete(listener1));
		assertFalse(dispatcher.isObsolete(listener2));
		
		// unregister both model change listeners
		dispatcher.removeModelChangeListener(listener1);
		dispatcher.removeModelChangeListener(listener2);
		
	}	
	
	@Test
	public void testMoveItemNotifier() {
		
		// create Command
		MoveItemCommand command = new MoveItemCommand();
		
		// create mock IModelChangeListener
		IModelChangeListener listener = mock(IModelChangeListener.class);
		
		// create the ModelChangeDispatcher, execute() the set attribute command and then get the 
		// IModelChangeNotifier 
		ModelChangeDispatcher dispatcher = new ModelChangeDispatcher();		
		command.execute();
		IModelChangeNotifier notifier = 
				dispatcher.getMoveItemNotifier(command, CommandStack.POST_EXECUTE);
		
		// test the notifier, making sure (only) the right method on the listener is called, with 
		// the right arguments		
		notifier.doNotify(listener);
		verify(listener, never()).afterAddItem(any(EObject.class), any(EReference.class), 
											   any(EObject.class));		
		verify(listener, times(1)).afterMoveItem(command.oldOwner, command.reference, command.item, 
				 								 command.newOwner);
		verify(listener, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
				   								  any(EObject.class));
		verify(listener, never()).afterSetFeatures(any(EObject.class), any(EStructuralFeature[].class));

		// alter mock CommandStackEvent: POST_UNDO (we do NOT have to undo() the command) and then 
		// get the IModelChangeNotifier		
		notifier = dispatcher.getMoveItemNotifier(command, CommandStack.POST_UNDO);
		
		// test the notifier, making sure (only) the right method on the listener is called, with 
		// the right arguments (the old owner has become the new owner and vice versa)	
		notifier.doNotify(listener);
		verify(listener, never()).afterAddItem(any(EObject.class), any(EReference.class), 
											   any(EObject.class));		
		verify(listener, times(1)).afterMoveItem(command.oldOwner, command.reference, command.item, 
				 								 command.newOwner);		
		verify(listener, times(1)).afterMoveItem(command.newOwner, command.reference, command.item, 
				 								 command.oldOwner);
		verify(listener, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
				   								  any(EObject.class));
		verify(listener, never()).afterSetFeatures(any(EObject.class), any(EStructuralFeature[].class));

		// alter mock CommandStackEvent: POST_REDO (we do NOT have to redo() the command) and then 
		// get the IModelChangeNotifier		
		notifier = dispatcher.getMoveItemNotifier(command, CommandStack.POST_REDO);
		
		// test the notifier, making sure (only) the right method on the listener is called, with 
		// the right arguments	
		notifier.doNotify(listener);
		verify(listener, never()).afterAddItem(any(EObject.class), any(EReference.class), 
											   any(EObject.class));		
		verify(listener, times(2)).afterMoveItem(command.oldOwner, command.reference, command.item, 
												 command.newOwner);		
		verify(listener, times(1)).afterMoveItem(command.newOwner, command.reference, command.item, 
				 								 command.oldOwner);	
		verify(listener, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
					  							  any(EObject.class));
		verify(listener, never()).afterSetFeatures(any(EObject.class), any(EStructuralFeature[].class));

		// make sure we fail when we try to get a notifier when not after an execute(), undo() or
		// redo()		
		try {
			notifier = dispatcher.getMoveItemNotifier(command, CommandStack.PRE_EXECUTE);
			fail("should throw an AssertionFailedException");
		} catch (AssertionFailedException e) {			
			assertEquals("assertion failed: not POST_EXECUTE/POST_UNDO/POST_REDO", e.getMessage());
		}		
		try {
			notifier = dispatcher.getMoveItemNotifier(command, CommandStack.PRE_UNDO);
			fail("should throw an AssertionFailedException");
		} catch (AssertionFailedException e) {			
			assertEquals("assertion failed: not POST_EXECUTE/POST_UNDO/POST_REDO", e.getMessage());
		}		
		try {
			notifier = dispatcher.getMoveItemNotifier(command, CommandStack.PRE_REDO);
			fail("should throw an AssertionFailedException");
		} catch (AssertionFailedException e) {			
			assertEquals("assertion failed: not POST_EXECUTE/POST_UNDO/POST_REDO", e.getMessage());
		}
		
	}
	
	@Test
	public void testSetFeaturesNotifier() {
		
		// create Command
		SetFeaturesCommand command = new SetFeaturesCommand();		
		
		// create mock IModelChangeListener
		IModelChangeListener listener = mock(IModelChangeListener.class);
		
		// create the ModelChangeDispatcher, execute() the set attribute command and then get the 
		// IModelChangeNotifier 
		ModelChangeDispatcher dispatcher = new ModelChangeDispatcher();		
		command.execute();
		IModelChangeNotifier notifier = 
			dispatcher.getSetFeaturesNotifier(command, CommandStack.POST_EXECUTE);
		
		// test the notifier, making sure (only) the right method on the listener is called, with 
		// the right arguments		
		notifier.doNotify(listener);
		verify(listener, never()).afterAddItem(any(EObject.class), any(EReference.class), 
											   any(EObject.class));		
		verify(listener, never()).afterMoveItem(any(EObject.class), any(EReference.class),
												any(EObject.class), any(EObject.class));
		verify(listener, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
				   								  any(EObject.class));
		verify(listener, times(1)).afterSetFeatures(command.owner, command.features);
		
		
		// alter mock CommandStackEvent: POST_UNDO (we do NOT have to undo() the command) and then 
		// get the IModelChangeNotifier		
		notifier = dispatcher.getSetFeaturesNotifier(command, CommandStack.POST_UNDO);
		
		// test the notifier, making sure the right method on the listener is called, with the right
		// arguments (the old value has become the new value and vice versa)
		notifier.doNotify(listener);
		verify(listener, never()).afterAddItem(any(EObject.class), any(EReference.class), 
				   							   any(EObject.class));		
		verify(listener, never()).afterMoveItem(any(EObject.class), any(EReference.class),
												any(EObject.class), any(EObject.class));
		verify(listener, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
					  							  any(EObject.class));
		verify(listener, times(2)).afterSetFeatures(command.owner, command.features);
		
		
		// alter mock CommandStackEvent: POST_REDO (we do NOT have to redo() the command) and then 
		// get the IModelChangeNotifier		
		notifier = dispatcher.getSetFeaturesNotifier(command, CommandStack.POST_REDO);
		
		// test the notifier, making sure the right method on the listener is called, with the right
		// arguments (the old value has become the old value again; the same goes for the new value)
		notifier.doNotify(listener);
		verify(listener, never()).afterAddItem(any(EObject.class), any(EReference.class), 
				   							   any(EObject.class));		
		verify(listener, never()).afterMoveItem(any(EObject.class), any(EReference.class),
												any(EObject.class), any(EObject.class));
		verify(listener, never()).afterRemoveItem(any(EObject.class), any(EReference.class), 
				  								  any(EObject.class));
		verify(listener, times(3)).afterSetFeatures(command.owner, command.features);

		
		// make sure we fail when we try to get a notifier when not after an execute(), undo() or
		// redo()		
		try {
			notifier = dispatcher.getSetFeaturesNotifier(command, CommandStack.PRE_EXECUTE);
			fail("should throw an AssertionFailedException");
		} catch (AssertionFailedException e) {			
			assertEquals("assertion failed: not POST_EXECUTE/POST_UNDO/POST_REDO", e.getMessage());
		}		
		try {
			notifier = dispatcher.getSetFeaturesNotifier(command, CommandStack.PRE_UNDO);
			fail("should throw an AssertionFailedException");
		} catch (AssertionFailedException e) {			
			assertEquals("assertion failed: not POST_EXECUTE/POST_UNDO/POST_REDO", e.getMessage());
		}		
		try {
			notifier = dispatcher.getSetFeaturesNotifier(command, CommandStack.PRE_REDO);
			fail("should throw an AssertionFailedException");
		} catch (AssertionFailedException e) {			
			assertEquals("assertion failed: not POST_EXECUTE/POST_UNDO/POST_REDO", e.getMessage());
		}		
		
	}
	
	@Test
	public void testDisposed() {
		
		ModelChangeDispatcher dispatcher = new ModelChangeDispatcher();
		
		dispatcher.dispose();
		
		try {
			dispatcher.addModelChangeListener(null);
			fail("should throw an AssertionFailedException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: model change dispatcher is disposed", e.getMessage());
		}
		
		try {
			dispatcher.dispatch(null);
			fail("should throw an AssertionFailedException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: model change dispatcher is disposed", e.getMessage());
		}
		
		try {
			dispatcher.dispose();
			fail("should throw an AssertionFailedException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: model change dispatcher is already disposed", e.getMessage());
		}
		
		try {
			dispatcher.dispatch(null);
			fail("should throw an AssertionFailedException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: model change dispatcher is disposed", e.getMessage());
		}
		
		assertTrue(dispatcher.isDisposed());
		
		try {
			dispatcher.removeModelChangeListener(null);
			fail("should throw an AssertionFailedException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: model change dispatcher is disposed", e.getMessage());
		}
		
		
	}
	
	@ModelChange(category=ADD_ITEM)
	private static class AddItemCommand extends Command {
		
		@Owner 	   private DiagramData  owner = SchemaFactory.eINSTANCE.createDiagramData();
		@Item  	   private DiagramLabel item = SchemaFactory.eINSTANCE.createDiagramLabel();			   	   
		@Reference private EReference   reference = SchemaPackage.eINSTANCE.getDiagramData_Label();
		
	}
	
	@ModelChange(category=MOVE_ITEM)
	private static class MoveItemCommand extends Command {
		
		@Owner(type=OwnerType.OLD) private SchemaArea		 oldOwner = 
			SchemaFactory.eINSTANCE.createSchemaArea();
		@Owner(type=OwnerType.NEW) private SchemaArea 	     newOwner = 
			SchemaFactory.eINSTANCE.createSchemaArea();
		@Reference 		 		   private EReference 	     reference = 
			SchemaPackage.eINSTANCE.getSchemaArea_AreaSpecifications();	
		@Item			 		   private AreaSpecification item = 
			SchemaFactory.eINSTANCE.createAreaSpecification();		
		
	}

	@ModelChange(category=REMOVE_ITEM)
	private static class RemoveItemCommand extends Command {
		
		@Owner 	   private DiagramData  owner = SchemaFactory.eINSTANCE.createDiagramData();
		@Item  	   private DiagramLabel item = SchemaFactory.eINSTANCE.createDiagramLabel();			   	   
		@Reference private EReference   reference = SchemaPackage.eINSTANCE.getDiagramData_Label();
		
	}	
	
	@ModelChange(category=SET_FEATURES)
	private static class SetFeaturesCommand extends Command {
		@Owner	  private ResizableDiagramNode owner = SchemaFactory.eINSTANCE.createDiagramLabel();
		@Features private EStructuralFeature[] 	features = {
			SchemaPackage.eINSTANCE.getResizableDiagramNode_Width(),
			SchemaPackage.eINSTANCE.getResizableDiagramNode_Height()
		};		
	}
	
}