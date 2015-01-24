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
package org.lh.dmlj.schema.editor.command.infrastructure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.junit.Test;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.editor.command.ModelChangeBasicCommand;
import org.lh.dmlj.schema.editor.command.ModelChangeCompoundCommand;

public class ModelChangeDispatcherTest {	
	
	@Test
	public void testDispatch_CompoundCommand() {	
		
		// create a mock IModelChangeListener and add it to the model change dispatcher's listeners
		IModelChangeListener listener = mock(IModelChangeListener.class);
		ModelChangeDispatcher dispatcher = new ModelChangeDispatcher();		
		dispatcher.addModelChangeListener(listener);
		
		// create a mock ModelChangeContext
		ModelChangeContext context = new ModelChangeContext(ModelChangeType.SWAP_RECORD_ELEMENTS);
		
		// simulate a PRE_EXECUTE command stack notification using a real CompoundCommand (composed
		// of 2 mock commands), a mock CommandStackEvent and a real ModelChangeContext...
		ModelChangeBasicCommand command1 = mock(ModelChangeBasicCommand.class);
		ModelChangeBasicCommand command2 = mock(ModelChangeBasicCommand.class);
		ModelChangeCompoundCommand compoundCommand = 
			new ModelChangeCompoundCommand("test compound command");
		compoundCommand.add(command1);
		compoundCommand.add(command2);
		compoundCommand.setContext(context);
		
		CommandStackEvent event = mock(CommandStackEvent.class);
		when(event.getDetail()).thenReturn(CommandStack.PRE_EXECUTE);	
		when(event.getCommand()).thenReturn(compoundCommand);
		dispatcher = new ModelChangeDispatcher();
		dispatcher.addModelChangeListener(listener);
		dispatcher.dispatch(event);		
		
		// the following listener method should have been called once; mind that the context passed
		// will NOT be the same instance as the original one (but we don't check the context here)
		verify(listener, times(1)).beforeModelChange(any(ModelChangeContext.class));	
		
		// ...no other listener method should have been called
		verify(listener, never()).afterModelChange(any(ModelChangeContext.class));			

		// simulate a PRE_REDO command stack notification
		when(event.getDetail()).thenReturn(CommandStack.PRE_REDO);		
		dispatcher = new ModelChangeDispatcher();
		dispatcher.addModelChangeListener(listener);
		dispatcher.dispatch(event);

		// the following listener method should have been called once again; mind that the context 
		// passed will NOT be the same instance as the original one (but we don't check the context 
		// here)
		verify(listener, times(2)).beforeModelChange(any(ModelChangeContext.class));
		
		// ...no other listener method should have been called
		verify(listener, never()).afterModelChange(any(ModelChangeContext.class));	
		

		// simulate a PRE_UNDO command stack notification
		when(event.getDetail()).thenReturn(CommandStack.PRE_UNDO);
		dispatcher = new ModelChangeDispatcher();
		dispatcher.addModelChangeListener(listener);
		dispatcher.dispatch(event);

		// the following listener method should have been called once again; mind that the context 
		// passed will NOT be the same instance as the original one (but we don't check the context 
		// here)
		verify(listener, times(3)).beforeModelChange(any(ModelChangeContext.class));
		
		// ...no other listener method should have been called
		verify(listener, never()).afterModelChange(any(ModelChangeContext.class));	
		
		
		// prepare for the next test; we need a new dispatcher and make sure it is first past a 
		// pre-change event
		dispatcher = new ModelChangeDispatcher();
		dispatcher.addModelChangeListener(listener);
		when(event.getDetail()).thenReturn(CommandStack.PRE_EXECUTE);
		dispatcher.dispatch(event);
		
		// simulate a POST_EXECUTE command stack notification				
		when(event.getDetail()).thenReturn(CommandStack.POST_EXECUTE);		
		dispatcher.dispatch(event);
		verify(listener, times(4)).beforeModelChange(any(ModelChangeContext.class));

		// the following listener method should have been called once; mind that the context passed
		// will NOT be the same instance as the original one (but we don't check the context here)
		verify(listener, times(1)).afterModelChange(any(ModelChangeContext.class));	
		
		// ...none of the following listener methods should have been called (because it either only
		// applies to pre-change events or the commands are missing the @ModelChange annotation)
		verify(listener, times(4)).beforeModelChange(any(ModelChangeContext.class));
		
				
		// prepare for the next test; we need a new dispatcher and make sure it is first passed a 
		// pre-change event
		ModelChangeBasicCommand moveItemCommand = mock(ModelChangeBasicCommand.class);
		ModelChangeBasicCommand setAttributeCommand = mock(ModelChangeBasicCommand.class);		
		compoundCommand = new ModelChangeCompoundCommand("test compound command");
		compoundCommand.add(moveItemCommand);
		compoundCommand.add(setAttributeCommand);
		compoundCommand.setContext(context);
		dispatcher = new ModelChangeDispatcher();
		dispatcher.addModelChangeListener(listener);
		when(event.getCommand()).thenReturn(compoundCommand);
		when(event.getDetail()).thenReturn(CommandStack.PRE_EXECUTE);
		dispatcher.dispatch(event);
		verify(listener, times(5)).beforeModelChange(any(ModelChangeContext.class));
		
		// simulate a POST_EXECUTE command stack notification using a compound command containing a
		// command annotated with @ModelChange(category=MOVE_ITEM) and a command annotated with
		// @ModelChange(category=SET_ATTRIBUTES)...
		when(event.getCommand()).thenReturn(compoundCommand);
		when(event.getDetail()).thenReturn(CommandStack.POST_EXECUTE);		
		dispatcher.dispatch(event);
		
		// the following listener method should have been called once again; mind that the context 
		// passed will NOT be the same instance as the original one (but we don't check the context 
		// here)
		verify(listener, times(2)).afterModelChange(any(ModelChangeContext.class));

		// ...and verify that the right listener methods have been called (we cannot check the 
		// order in which they were called but this order is not really relevant since the model
		// will contain the changes from BOTH commands on either method call)
		verify(listener, times(5)).beforeModelChange(any(ModelChangeContext.class));
		
		
		// prepare for the next test; we need a new dispatcher and make sure it is first past a 
		// pre-change event
		dispatcher = new ModelChangeDispatcher();
		dispatcher.addModelChangeListener(listener);
		when(event.getDetail()).thenReturn(CommandStack.PRE_UNDO);
		dispatcher.dispatch(event);
		verify(listener, times(6)).beforeModelChange(any(ModelChangeContext.class));
		
		// simulate a POST_UNDO command stack notification using the same compound command		
		when(event.getDetail()).thenReturn(CommandStack.POST_UNDO);		
		dispatcher.dispatch(event);
		
		// the following listener method should have been called once again; mind that the context 
		// passed will NOT be the same instance as the original one (but we don't check the context 
		// here)
		verify(listener, times(3)).afterModelChange(any(ModelChangeContext.class));

		// ...and verify that the right listener methods have been called (we cannot check the 
		// order in which they were called but this order is not really relevant since the model
		// will contain the changes from BOTH commands on either method call)
		verify(listener, times(6)).beforeModelChange(any(ModelChangeContext.class));
		
		
		// prepare for the next test; we need a new dispatcher and make sure it is first past a 
		// pre-change event
		dispatcher = new ModelChangeDispatcher();
		dispatcher.addModelChangeListener(listener);
		when(event.getDetail()).thenReturn(CommandStack.PRE_REDO);
		dispatcher.dispatch(event);
		verify(listener, times(7)).beforeModelChange(any(ModelChangeContext.class));
		
		// simulate a POST_REDO command stack notification using the same compound command		
		when(event.getDetail()).thenReturn(CommandStack.POST_REDO);				
		dispatcher.dispatch(event);

		// the following listener method should have been called once again; mind that the context 
		// passed will NOT be the same instance as the original one (but we don't check the context 
		// here)
		verify(listener, times(4)).afterModelChange(any(ModelChangeContext.class));

		// ...and verify that the right listener methods have been called (we cannot check the 
		// order in which they were called but this order is not really relevant since the model
		// will contain the changes from BOTH commands on either method call)
		verify(listener, times(7)).beforeModelChange(any(ModelChangeContext.class));		
		
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
	
	@Test
	public void testContextPassingAndSchemaHandling() {
		
		final Schema schema = mock(Schema.class);
		
		final ModelChangeContext context = 
			new ModelChangeContext(ModelChangeType.SWAP_RECORD_ELEMENTS);
		// don't set the schema because our model change dispatcher should do this for us
		context.getContextData().put("a", "b");
		
		final CommandExecutionMode[] expectedCommandExecutionMode = new CommandExecutionMode[1];
		
		final ModelChangeContext[] savedListenerContext1 = new ModelChangeContext[1];
		final boolean[] listener1MethodCallsOK = {false, false};
		IModelChangeListener listener1 = new IModelChangeListener() {		
			public void beforeModelChange(ModelChangeContext listenerContext1) {
								
				assertNotSame(context, listenerContext1);
				assertSame(schema, listenerContext1.getSchema());
				
				assertSame(expectedCommandExecutionMode[0], 
						   listenerContext1.getCommandExecutionMode());
				assertNotSame(context.getContextData(), listenerContext1.getContextData());
				assertEquals(1, listenerContext1.getContextData().size());
				assertEquals("b", context.getContextData().get("a"));
				assertNull(listenerContext1.getListenerData());
				
				listenerContext1.setListenerData("listener1 data");
				
				listener1MethodCallsOK[0] = true;
			}			
			public void afterModelChange(ModelChangeContext listenerContext2) {
				
				assertNotSame(context, listenerContext2);
				assertNotSame(savedListenerContext1[0], listenerContext2);
				assertSame(schema, listenerContext2.getSchema());
				
				assertSame(expectedCommandExecutionMode[0], 
						   listenerContext2.getCommandExecutionMode());
				assertNotSame(context.getContextData(), listenerContext2.getContextData());
				assertEquals(1, listenerContext2.getContextData().size());
				assertEquals("b", context.getContextData().get("a"));
				assertEquals("listener1 data", listenerContext2.getListenerData());
				
				listener1MethodCallsOK[1] = true;
			}			
		};
		
		final ModelChangeContext[] savedListenerContext2 = new ModelChangeContext[1];
		final boolean[] listener2MethodCallsOK = {false, false};
		IModelChangeListener listener2 = new IModelChangeListener() {		
			public void beforeModelChange(ModelChangeContext listenerContext1) {
				
				assertNotSame(context, listenerContext1);
				assertSame(schema, listenerContext1.getSchema());
				
				assertSame(expectedCommandExecutionMode[0], 
						   listenerContext1.getCommandExecutionMode());
				assertNotSame(context.getContextData(), listenerContext1.getContextData());
				assertSame(expectedCommandExecutionMode[0], 
						   listenerContext1.getCommandExecutionMode());
				assertEquals("b", context.getContextData().get("a"));
				assertNull(listenerContext1.getListenerData());
				
				listenerContext1.setListenerData("listener2 data");
				
				listener2MethodCallsOK[0] = true;
			}			
			public void afterModelChange(ModelChangeContext listenerContext2) {
				
				assertNotSame(context, listenerContext2);
				assertNotSame(savedListenerContext2[0], listenerContext2);
				assertSame(schema, listenerContext2.getSchema());
				
				assertSame(expectedCommandExecutionMode[0], 
						   listenerContext2.getCommandExecutionMode());
				assertNotSame(context.getContextData(), listenerContext2.getContextData());
				assertSame(expectedCommandExecutionMode[0], 
						   listenerContext2.getCommandExecutionMode());
				assertEquals("b", context.getContextData().get("a"));
				assertEquals("listener2 data", listenerContext2.getListenerData());
				
				listener2MethodCallsOK[1] = true;
			}			
		};		
		
		ModelChangeDispatcher dispatcher = new ModelChangeDispatcher();	
		dispatcher.setSchema(schema);
		dispatcher.addModelChangeListener(listener1);
		dispatcher.addModelChangeListener(listener2);
		
		ModelChangeBasicCommand command = new ModelChangeBasicCommand("test command");
		command.setContext(context);
				
		// before model change (EXECUTE)...
		expectedCommandExecutionMode[0] = CommandExecutionMode.EXECUTE;
		CommandStackEvent event = mock(CommandStackEvent.class);
		when(event.getDetail()).thenReturn(CommandStack.PRE_EXECUTE);	
		when(event.getCommand()).thenReturn(command);
		dispatcher.dispatch(event);
		
		assertEquals(true, listener1MethodCallsOK[0]);
		assertEquals(false, listener1MethodCallsOK[1]);
		assertEquals(true, listener2MethodCallsOK[0]);
		assertEquals(false, listener2MethodCallsOK[1]);
		
		// after model change (EXECUTE)...
		listener1MethodCallsOK[0] = false;
		listener2MethodCallsOK[0] = false;
		expectedCommandExecutionMode[0] = CommandExecutionMode.EXECUTE;
		event = mock(CommandStackEvent.class);
		when(event.getDetail()).thenReturn(CommandStack.POST_EXECUTE);	
		when(event.getCommand()).thenReturn(command);
		dispatcher.dispatch(event);
		
		assertEquals(false, listener1MethodCallsOK[0]);
		assertEquals(true, listener1MethodCallsOK[1]);
		assertEquals(false, listener2MethodCallsOK[0]);
		assertEquals(true, listener2MethodCallsOK[1]);
		
		
		// before model change (UNDO)...
		listener1MethodCallsOK[1] = false;
		listener2MethodCallsOK[1] = false;	
		expectedCommandExecutionMode[0] = CommandExecutionMode.UNDO;
		event = mock(CommandStackEvent.class);
		when(event.getDetail()).thenReturn(CommandStack.PRE_UNDO);	
		when(event.getCommand()).thenReturn(command);
		dispatcher.dispatch(event);
		
		assertEquals(true, listener1MethodCallsOK[0]);
		assertEquals(false, listener1MethodCallsOK[1]);
		assertEquals(true, listener2MethodCallsOK[0]);
		assertEquals(false, listener2MethodCallsOK[1]);
		
		// after model change (UNDO)...
		listener1MethodCallsOK[0] = false;
		listener2MethodCallsOK[0] = false;	
		expectedCommandExecutionMode[0] = CommandExecutionMode.UNDO;
		event = mock(CommandStackEvent.class);
		when(event.getDetail()).thenReturn(CommandStack.POST_UNDO);	
		when(event.getCommand()).thenReturn(command);
		dispatcher.dispatch(event);
		
		assertEquals(false, listener1MethodCallsOK[0]);
		assertEquals(true, listener1MethodCallsOK[1]);
		assertEquals(false, listener2MethodCallsOK[0]);
		assertEquals(true, listener2MethodCallsOK[1]);
		
		
		// before model change (REDO)...
		listener1MethodCallsOK[1] = false;
		listener2MethodCallsOK[1] = false;		
		expectedCommandExecutionMode[0] = CommandExecutionMode.REDO;
		event = mock(CommandStackEvent.class);
		when(event.getDetail()).thenReturn(CommandStack.PRE_REDO);	
		when(event.getCommand()).thenReturn(command);
		dispatcher.dispatch(event);
		
		assertEquals(true, listener1MethodCallsOK[0]);
		assertEquals(false, listener1MethodCallsOK[1]);
		assertEquals(true, listener2MethodCallsOK[0]);
		assertEquals(false, listener2MethodCallsOK[1]);
		
		// after model change (REDO)...
		listener1MethodCallsOK[0] = false;
		listener2MethodCallsOK[0] = false;		
		expectedCommandExecutionMode[0] = CommandExecutionMode.REDO;
		event = mock(CommandStackEvent.class);
		when(event.getDetail()).thenReturn(CommandStack.POST_REDO);	
		when(event.getCommand()).thenReturn(command);
		dispatcher.dispatch(event);
		
		assertEquals(false, listener1MethodCallsOK[0]);
		assertEquals(true, listener1MethodCallsOK[1]);
		assertEquals(false, listener2MethodCallsOK[0]);
		assertEquals(true, listener2MethodCallsOK[1]);
		
	}
	
	@Test
	public void testObsoleteListener_AfterModelChange() {
		
		final ModelChangeDispatcher dispatcher = new ModelChangeDispatcher();
		dispatcher.setSchema(mock(Schema.class));
		
		final boolean[] methodsInvoked = {false}; // beforeModelChange
		IModelChangeListener listener = new IModelChangeListener() {			
			@Override
			public void beforeModelChange(ModelChangeContext context) {	
				methodsInvoked[0] = true;
			}			
			@Override
			public void afterModelChange(ModelChangeContext context) {
				fail("method shouldn't be called");
			}			
		};
		dispatcher.addModelChangeListener(listener);
		
		ModelChangeContext context = new ModelChangeContext(ModelChangeType.SWAP_RECORD_ELEMENTS);
		ModelChangeBasicCommand command = new ModelChangeBasicCommand("test command");
		command.setContext(context);
		CommandStackEvent event = mock(CommandStackEvent.class);
		when(event.getCommand()).thenReturn(command);
		
		// dispatch the pre model change event
		when(event.getDetail()).thenReturn(CommandStack.PRE_EXECUTE);	
		dispatcher.dispatch(event);
		
		assertTrue(methodsInvoked[0]);
		methodsInvoked[0] = false;
		
		// remove the listener between the before- and after model change callbacks so that it will
		// be marked as obsolete during the after model change event callback
		dispatcher.removeModelChangeListener(listener);
		
		// dispatch the after model change event
		when(event.getDetail()).thenReturn(CommandStack.POST_EXECUTE);
		dispatcher.dispatch(event);
		
		assertFalse(methodsInvoked[0]);
		
	}
	
	@Test
	public void testObsoleteListener_BeforeModelChange() {
		
		final ModelChangeDispatcher dispatcher = new ModelChangeDispatcher();
		dispatcher.setSchema(mock(Schema.class));
		
		final boolean[] methodsInvoked = {false}; // beforeModelChange listener2
		final IModelChangeListener listener1 = new IModelChangeListener() {			
			@Override
			public void beforeModelChange(ModelChangeContext context) {	
				fail("method shouldn't be called");
			}			
			@Override
			public void afterModelChange(ModelChangeContext context) {
				fail("method shouldn't be called");
			}			
		};
		IModelChangeListener listener2 = new IModelChangeListener() {			
			@Override
			public void beforeModelChange(ModelChangeContext context) {	
				dispatcher.removeModelChangeListener(listener1);
				methodsInvoked[0] = true;
			}			
			@Override
			public void afterModelChange(ModelChangeContext context) {
				fail("method shouldn't be called");
			}			
		};
		// listener2 should receive its callbacks before listener1 so that it can unregister listener1
		dispatcher.addModelChangeListener(listener2);
		dispatcher.addModelChangeListener(listener1);		
		
		ModelChangeContext context = new ModelChangeContext(ModelChangeType.SWAP_RECORD_ELEMENTS);
		ModelChangeBasicCommand command = new ModelChangeBasicCommand("test command");
		command.setContext(context);
		CommandStackEvent event = mock(CommandStackEvent.class);
		when(event.getCommand()).thenReturn(command);
		
		// dispatch the pre model change event
		when(event.getDetail()).thenReturn(CommandStack.PRE_EXECUTE);	
		dispatcher.dispatch(event);
		
		assertTrue(methodsInvoked[0]);
		
	}
	
}
