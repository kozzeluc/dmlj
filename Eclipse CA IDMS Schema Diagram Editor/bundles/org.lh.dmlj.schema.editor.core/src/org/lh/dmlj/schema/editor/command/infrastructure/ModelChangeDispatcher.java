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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CompoundCommand;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.command.IModelChangeCommand;
import org.lh.dmlj.schema.editor.command.ModelChangeBasicCommand;
import org.lh.dmlj.schema.editor.command.ModelChangeCompoundCommand;
import org.lh.dmlj.schema.editor.command.MoveDiagramNodeCommand;

public class ModelChangeDispatcher implements IModelChangeProvider {
	
	public enum Availability {MANDATORY, OPTIONAL};	
	
	private boolean disposed = false;
	private List<IModelChangeListener> listeners = new ArrayList<>();
	private Schema schema;
	
	// things to carry from a pre-change to a post-change event:
	private Command previousCommand;
	private List<IModelChangeListener> allListeners;
	private Map<Integer, Object> listenerDataMap;	
	
	private static List<MoveDiagramNodeCommand> convertToMoveDiagramNodeCommands(List<Command> commands) {
		List<MoveDiagramNodeCommand> moveDiagramNodeCommands = new ArrayList<>();
		for (Command command : commands) {
			moveDiagramNodeCommands.add((MoveDiagramNodeCommand) command);
		}
		return moveDiagramNodeCommands;
	}

	protected static void createModelChangeContextHierarchy(ModelChangeContext parentContext, 
								    				 	    List<MoveDiagramNodeCommand> childCommands) {
		
		for (MoveDiagramNodeCommand command : childCommands) {
			ModelChangeContext childContext = command.getContext();
			parentContext.getChildren().add(childContext);
			childContext.setParent(parentContext);
		}
	}

	private static CommandExecutionMode getCommandExecutionMode(CommandStackEvent event) {
		if (event.getDetail() == CommandStack.PRE_EXECUTE || 
			event.getDetail() == CommandStack.POST_EXECUTE) {
			
			return CommandExecutionMode.EXECUTE;
		} else if (event.getDetail() == CommandStack.PRE_UNDO ||
				   event.getDetail() == CommandStack.POST_UNDO) {
			
			return CommandExecutionMode.UNDO;
		} else if (event.getDetail() == CommandStack.PRE_REDO ||
				   event.getDetail() == CommandStack.POST_REDO) {
			
			return CommandExecutionMode.REDO;
		}
		throw new IllegalArgumentException("cannot derive command execution mode: " + 
										   event.getDetail()); 
	}

	protected static boolean isMoveGroupOfDiagramNodesCompoundCommand(List<Command> commands) {
		for (Object command : commands) {
			if (!(command instanceof MoveDiagramNodeCommand)) {
				return false;
			}
		}
		return true;
	}

	public ModelChangeDispatcher() {
		super();
	}
	
	public void addModelChangeListener(IModelChangeListener listener) {
		Assert.isTrue(!isDisposed(), "model change dispatcher is disposed");
		if (!listeners.contains(listener)) {
			// avoid notifying the same listener twice
			listeners.add(listener);			
		}
	}
	
	public void removeModelChangeListener(IModelChangeListener listener) {
		Assert.isTrue(!isDisposed(), "model change dispatcher is disposed");
		listeners.remove(listener);		
	}

	public void setSchema(Schema schema) {
		this.schema = schema;
	}

	public void dispatch(CommandStackEvent event) {			
		performPreDispatchChecks(event);		
		if (event.isPreChangeEvent()) {
			dispatchPreChangeEvent(event);
		} else {
			dispatchPostChangeEvent(event);
		}	
	}

	private void performPreDispatchChecks(CommandStackEvent event) {
		Assert.isTrue(!isDisposed(), "model change dispatcher is disposed");
		Class<? extends Command> commandClass = event.getCommand().getClass();
		Assert.isTrue(!(commandClass.getSimpleName().endsWith("ChainedCompoundCommand") &&
					    commandClass.getPackage().getName().equals("org.eclipse.gef.commands")), 
					  "don't chain commands together but create a compound command of type " +
					  ModelChangeCompoundCommand.class.getName());		
	}

	private void dispatchPreChangeEvent(CommandStackEvent event) {
		// invoke the beforeModelChangeListener(context) method on all current listeners and set 
		// aside the listener data; if no context is set, bypass this step (note that we should 
		// avoid this situation but are allowing it to be able to move gradually to the new 
		// dispatching methods								
		Assert.isTrue(previousCommand == null, "previousCommand is already set");
		Assert.isTrue(allListeners == null, "allListeners is already set");
		Assert.isTrue(listenerDataMap == null, "allListeners is already set");
		// get the context from the event:
		ModelChangeContext context = locateModelChangeContext(event);
		// initialize the things we need to carry from the pre-change to the post-change event:						
		previousCommand = event.getCommand();
		allListeners = new ArrayList<>(listeners);
		listenerDataMap = new HashMap<>();
		for (int i = 0; i < allListeners.size(); i++) {
			IModelChangeListener listener = allListeners.get(i);
			if (listeners.contains(listener)) {
				// only call the before model change method if the listener is still registered
				ModelChangeContext contextCopy = context.copy();
				contextCopy.setCommandExecutionMode(getCommandExecutionMode(event));
				try {
					listener.beforeModelChange(contextCopy);
				} catch (Throwable t) {
					StringBuilder message = new StringBuilder(); 
					message.append("Exception thrown while dispatching pre-change event");
					message.append("\n         Listener: " + listener.getClass().getName());
					if (listener instanceof EditPart) {
						EditPart editPart = (EditPart) listener;
						message.append("\n         Model:    " + editPart.getModel().toString());
					}
					Plugin.logError(message.toString(), t);
				}
				Object listenerData = contextCopy.getListenerData();
				if (listenerData != null) {
					listenerDataMap.put(Integer.valueOf(i), listenerData);
				}
			}
		}
	}

	private void dispatchPostChangeEvent(CommandStackEvent event) {
		// invoke the afterModelChangeListener(context) method on the same listeners as the 
		// corresponding pre-change event (we don't work with the notion of 'obsolete listeners' 
		// here)		
		Assert.isTrue(event.getCommand() == previousCommand, 
					  "eventCommand does NOT match previousCommand");
		Assert.isNotNull(allListeners, "allListeners not set during pre-change event");
		Assert.isNotNull(listenerDataMap, "allListeners not set during pre-change event");
		// get the context from the event:
		ModelChangeContext context = locateModelChangeContext(event);
		for (int i = 0; i < allListeners.size(); i++) {
			IModelChangeListener listener = allListeners.get(i);
			if (listeners.contains(listener)) {
				// only call the after model change method if the listener is still registered
				ModelChangeContext contextCopy = context.copy();
				contextCopy.setCommandExecutionMode(getCommandExecutionMode(event));
				Object listenerData = listenerDataMap.get(Integer.valueOf(i));
				if (listenerData != null) {
					contextCopy.setListenerData(listenerData);
				}
				try {
					listener.afterModelChange(contextCopy);
				} catch (Throwable t) {
					StringBuilder message = new StringBuilder(); 
					message.append("Exception thrown while dispatching post-change event");
					message.append("\n         Listener: " + listener.getClass().getName());
					if (listener instanceof EditPart) {
						EditPart editPart = (EditPart) listener;
						message.append("\n         Model:    " + editPart.getModel().toString());
					}
					Plugin.logError(message.toString(), t);
				}
			}
		}
		// nullify the things we needed to carry from the pre-change to the post-change event:
		previousCommand = null;
		allListeners = null;
		listenerDataMap = null;
	}

	@SuppressWarnings("unchecked")
	protected ModelChangeContext locateModelChangeContext(CommandStackEvent event) {
		// we need to extract the model change context from the command that was put on the command
		// stack
		ModelChangeContext context;		
		
		// the event's command is possibly a compound command		
		Command eventCommand = event.getCommand();		
		if (eventCommand instanceof CompoundCommand) {
			// the event command is a compound command
			CompoundCommand compoundCommand = (CompoundCommand) eventCommand;
			List<Command> compoundCommandCommands = compoundCommand.getCommands();			
			if (compoundCommandCommands.size() == 1 &&
				compoundCommandCommands.get(0) instanceof ModelChangeCompoundCommand) {
				
				// in some cases, compound commands that we create are wrapped themselves in a
				// compound command, so make sure we can handle this situation
				ModelChangeCompoundCommand wrappedCompoundCommand = 
					(ModelChangeCompoundCommand) compoundCommandCommands.get(0);
				Assert.isTrue(wrappedCompoundCommand instanceof IModelChangeCommand, 
							  "not an IModelChangeCommand: " + wrappedCompoundCommand);
				compoundCommandCommands = wrappedCompoundCommand.getCommands();
				context = wrappedCompoundCommand.getContext();
			} else if (compoundCommandCommands.size() == 1 &&
					   compoundCommandCommands.get(0) instanceof ModelChangeBasicCommand) {
				
				// in some cases, basic commands that we create are wrapped themselves in a compound 
				// command, so make sure we can handle this situation
				ModelChangeBasicCommand wrappedBasicCommand = 
					(ModelChangeBasicCommand) compoundCommandCommands.get(0);
				compoundCommandCommands = new ArrayList<>();
				compoundCommandCommands.add(wrappedBasicCommand);
				context = wrappedBasicCommand.getContext();
			} else if (compoundCommandCommands.size() > 1 &&
					   isMoveGroupOfDiagramNodesCompoundCommand(compoundCommand.getCommands())) {
				
				// 2 or more diagram nodes are being moved; we need to create the (global) model 
				// change context ourselves
				context = new ModelChangeContext(ModelChangeType.MOVE_GROUP_OF_DIAGRAM_NODES);
				List<MoveDiagramNodeCommand> moveDiagramNodeCommands = 
					convertToMoveDiagramNodeCommands(compoundCommand.getCommands());
				createModelChangeContextHierarchy(context, moveDiagramNodeCommands);
			} else {
				Assert.isTrue(eventCommand instanceof IModelChangeCommand, 
						  	  "not an IModelChangeCommand: " + eventCommand);
				context = ((IModelChangeCommand) eventCommand).getContext();
			}			
		} else {
			// the event command is a basic command
			Assert.isTrue(eventCommand instanceof IModelChangeCommand, 
				  	  	  "not an IModelChangeCommand: " + eventCommand);
			context = ((IModelChangeCommand) eventCommand).getContext();
		}
		
		// make sure the context's schema is set
		if (context != null) {
			// the schema should be set, but we do NOT enforce it; just copy whatever we have or not
			// to the context
			context.setSchema(schema);
		}
		
		// the context is now complete
		return context;
	}

	public void dispose() {
		// we provide this method because when a diagram editor is closed, the graphical edit parts
		// will not have the opportunity to remove themselves as a listener because their 
		// removeNotify() method will not be called; calling dispose() on the model change 
		// dispatcher clears all listeners, which is good enough for us
		Assert.isTrue(!isDisposed(), "model change dispatcher is already disposed");
		listeners.clear();
		disposed = true;
	}

	protected boolean isDisposed() {
		return disposed;
	}
	
}