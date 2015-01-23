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

public class ModelChangeDispatcher implements IModelChangeProvider {
	
	public enum Availability {MANDATORY, OPTIONAL};	
	
	private boolean disposed = false;
	private List<IModelChangeListener> listeners = new ArrayList<>();
	private Schema schema;
	
	// things to carry from a pre-change to a post-change event:
	private Command previousCommand;
	private List<IModelChangeListener> allListeners;
	private Map<Integer, Object> listenerDataMap;	
	
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

	private static String getEventDetailDescription(int eventDetail) {
		if (eventDetail == CommandStack.PRE_EXECUTE) {
			return "PRE_EXECUTE";		
		} if (eventDetail == CommandStack.POST_EXECUTE) {
			return "POST_EXECUTE";
		} else if (eventDetail == CommandStack.PRE_REDO) {
			return "PRE_REDO";
		} else if (eventDetail == CommandStack.POST_REDO) {
			return "POST_REDO";
		} else if (eventDetail == CommandStack.PRE_UNDO) {
			return "PRE_UNDO";	
		} else if (eventDetail == CommandStack.POST_UNDO) {
			return "POST_UNDO";	
		}
		return "<unexpected value>";
	}
	
	public ModelChangeDispatcher() {
		super();
	}
	
	public void addModelChangeListener(IModelChangeListener listener) {
		Assert.isTrue(!isDisposed(), "model change dispatcher is disposed");
		if (!listeners.contains(listener)) {
			// avoid notifying the same listener twice
			listeners.add(listener);
			// write a debug message to the log...
			StringBuilder debugMessage = new StringBuilder();
			debugMessage.append("Added model change dispatch listener:" + "\n");
			debugMessage.append("Listener class: " + listener.getClass().getName() + "\n");			
			if (listener instanceof EditPart) {
				EditPart editPart = (EditPart) listener;
				debugMessage.append("Edit part model: " + (editPart).getModel());
				debugMessage.append("\n");
				if (editPart.getParent() != null) {
					EditPart parent = editPart.getParent();
					debugMessage.append("Parent edit part class: " + parent.getClass().getName() + "\n");
					debugMessage.append("Parent edit part model: " + parent.getModel() + "\n");	
					if (parent.getParent() != null) {
						EditPart grandParent = parent.getParent();
						debugMessage.append("Grand parent edit part class: " + 
											grandParent.getClass().getName() + "\n");
						debugMessage.append("Grand parent edit part model: " + grandParent.getModel() + 
											"\n");				
					} else {
						debugMessage.append("Grand parent edit part: null\n");
					}
				} else {
					debugMessage.append("Parent edit part: null\n");
				}
			}
			debugMessage.append("The listener count is now " + listeners.size());			
			logDebug(debugMessage.toString());			
		}
	}
	
	@SuppressWarnings("unchecked")
	public void dispatch(CommandStackEvent event) {		
		
		Assert.isTrue(!isDisposed(), "model change dispatcher is disposed");
		Class<? extends Command> commandClass = event.getCommand().getClass();
		Assert.isTrue(!(commandClass.getSimpleName().endsWith("ChainedCompoundCommand") &&
				commandClass.getPackage().getName().equals("org.eclipse.gef.commands")), 
					  "don't chain commands together but create a compound command of type " +
					  ModelChangeCompoundCommand.class.getName());
		
		// log every event when in debug mode
		StringBuilder debugMessage = new StringBuilder();
		debugMessage.append("****************************** CommandStackEvent: " +
							"******************************\n");
		debugMessage.append("Command class: " + event.getCommand().getClass().getName() + "\n");
		debugMessage.append("Event detail: " + getEventDetailDescription(event.getDetail()) + " (" + 
	 						event.getDetail() + ")");
		logDebug(debugMessage.toString());
		
		// we need to extract the model change context from the command that was put on the command
		// stack
		ModelChangeContext context;		
		
		// the event's command is possibly a compound command - build a list of commands to process		
		Command eventCommand = event.getCommand();		
		List<Command> commands = new ArrayList<>();
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
				// ignore non model change commands
				if (!(wrappedCompoundCommand instanceof IModelChangeCommand)) {
					return;
				}
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
			} else {
				// ignore non model change commands
				if (!(eventCommand instanceof IModelChangeCommand)) {
					return;
				}
				context = ((IModelChangeCommand) eventCommand).getContext();
			}
			if (isExecuteOrRedo(event.getDetail())) {
				// add the individual commands in the order they are listed in the compound command,
				// listeners should be aware that the model contains the situation after undoing all
				// the commands contained in the compound command				
				commands.addAll(compoundCommandCommands);
			} else {
				// in the case we're undoing a command, add the individual commands in reverse 
				// order, that way we follow the order in which the commands were undone BUT 
				// listeners should be aware that the model contains the situation after undoing all
				// the commands contained in the compound command
				for (int i = compoundCommandCommands.size() - 1; i > -1; i--) {
					commands.add(compoundCommandCommands.get(i));
				}
			}
			// log the commands contained in the compound command
			debugMessage = new StringBuilder();
				debugMessage.append("Compound command classes (in the order in which they will " +
									"be dispatched):\n");
			
			for (int i = 0; i < commands.size(); i++) {
				if (i > 0) {
					debugMessage.append("\n");				
				}
				debugMessage.append("[" + i + "] " + commands.get(i).getClass().getName() + 
									"(label='" + commands.get(i).getLabel() + "')");				
			}
			logDebug(debugMessage.toString());			
		} else {
			// the event command is a basic command; ignore non model change commands
			if (!(eventCommand instanceof IModelChangeCommand)) {
				return;
			} 
			commands.add(eventCommand);
			context = ((IModelChangeCommand) eventCommand).getContext();
		}
		
		// make sure the context's schema is set
		if (context != null) {
			// the schema should be set, but we do NOT enforce it; just copy whatever we have or not
			// to the context
			context.setSchema(getSchema());
		}
		
		// in the case of a pre-change event, (only) invoke the beforeModelChangeListener(context) 
		// method on all current listeners and set aside the listener data; if no context is set, 
		// bypass this step (note that we should avoid this situation but are allowing it to be able 
		// to move gradually to the new dispatching methods				
		if (context != null && event.isPreChangeEvent()) {
			Assert.isTrue(previousCommand == null, "previousCommand is already set");
			Assert.isTrue(allListeners == null, "allListeners is already set");
			Assert.isTrue(listenerDataMap == null, "allListeners is already set");
			// initialize the things we need to carry from the pre-change to the post-change event:						
			previousCommand = eventCommand;
			allListeners = new ArrayList<>(listeners);
			listenerDataMap = new HashMap<>();
			for (int i = 0; i < allListeners.size(); i++) {
				IModelChangeListener listener = allListeners.get(i);
				if (listeners.contains(listener)) {
					// only call the before model change method if the listener is still registered
					ModelChangeContext contextCopy = context.copy();
					contextCopy.setCommandExecutionMode(getCommandExecutionMode(event));
					listener.beforeModelChange(contextCopy);
					Object listenerData = contextCopy.getListenerData();
					if (listenerData != null) {
						listenerDataMap.put(Integer.valueOf(i), listenerData);
					}
				}
			}
		}
		
		// we're done in case of pre-change events
		if (event.isPreChangeEvent()) {
			return;
		}
		
		// in the case of a post-change event, (only) invoke the afterModelChangeListener(context) 
		// method on the same listeners as the corresponding pre-change event (we don't work with 
		// the notion of 'obsolete listeners' here); if no context is set, bypass this step (note 
		// that we should avoid this situation but are allowing it to be able to move gradually to 
		// the new dispatching methods
		if (context != null && event.isPostChangeEvent()) {
			Assert.isTrue(eventCommand == previousCommand, 
						  "eventCommand does NOT match previousCommand");
			Assert.isNotNull(allListeners, "allListeners not set during pre-change event");
			Assert.isNotNull(listenerDataMap, "allListeners not set during pre-change event");
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
					listener.afterModelChange(contextCopy);
				}
			}
			// nullify the things we needed to carry from the pre-change to the post-change event:
			previousCommand = null;
			allListeners = null;
			listenerDataMap = null;
		}

	}
	
	public void dispose() {
		// we provide this method because when a diagram editor is closed, the graphical edit parts
		// will not have the opportunity to remove themselves as a listener because their 
		// removeNotify() method will not be called; calling dispose() on the model change 
		// dispatcher clears all listeners, which is good enough for us
		Assert.isTrue(!isDisposed(), "model change dispatcher is already disposed");
		listeners.clear();
		disposed = true;
		logDebug("Model change dispatcher was disposed; the listener count is now " + 
				 listeners.size());
	}

	public Schema getSchema() {
		return schema;
	}

	public boolean isDisposed() {
		return disposed;
	}
	
	private boolean isExecuteOrRedo(int eventDetail) {
		if (eventDetail == CommandStack.PRE_EXECUTE ||
			eventDetail == CommandStack.PRE_REDO ||
			eventDetail == CommandStack.POST_EXECUTE ||
			eventDetail == CommandStack.POST_REDO) {
			
			return true;
		} else {
			return false;
		}
	}
	
	private void logDebug(String message) {
		// TODO configure debug logging; it's fine for now but we should change it in the future
		Plugin.logDebug(message);
	}

	public void removeModelChangeListener(IModelChangeListener listener) {
		Assert.isTrue(!isDisposed(), "model change dispatcher is disposed");
		listeners.remove(listener);
		// write a debug message to the log...
		StringBuilder debugMessage = new StringBuilder();
		debugMessage.append("Removed model change dispatch listener:" + "\n");
		debugMessage.append("Listener class: " + listener.getClass().getName() + "\n");
		if (listener instanceof EditPart) {
			EditPart editPart = (EditPart) listener;
			debugMessage.append("Edit part model: " + (editPart).getModel());
			debugMessage.append("\n");
			if (editPart.getParent() != null) {
				EditPart parent = editPart.getParent();
				debugMessage.append("Parent edit part class: " + parent.getClass().getName() + "\n");
				debugMessage.append("Parent edit part model: " + parent.getModel() + "\n");
				if (parent.getParent() != null) {
					EditPart grandParent = parent.getParent();
					debugMessage.append("Grand parent edit part class: " + 
										grandParent.getClass().getName() + "\n");
					debugMessage.append("Grand parent edit part model: " + grandParent.getModel() + 
										"\n");				
				} else {
					debugMessage.append("Grand parent edit part: null\n");
				}				
			} else {
				debugMessage.append("Parent edit part: null\n");
			}
		}
		debugMessage.append("The listener count is now " + listeners.size());			
		logDebug(debugMessage.toString());		
	}

	public void setSchema(Schema schema) {
		this.schema = schema;
	}
	
}