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
package org.lh.dmlj.schema.editor.command.infrastructure;

import java.util.HashMap;
import java.util.Map;

import org.lh.dmlj.schema.Schema;

public class ModelChangeContext {

	private CommandExecutionMode commandExecutionMode;
	private Map<String, String> contextData = new HashMap<>();
	private Object listenerData;
	private ModelChangeType modelChangeType;
	private Schema schema;
	
	public ModelChangeContext(ModelChangeType modelChangeType) {
		super();
		this.modelChangeType = modelChangeType;
	}

	public ModelChangeContext copy() {
		ModelChangeContext copy = new ModelChangeContext(modelChangeType);
		copy.setSchema(schema);
		for (String key : contextData.keySet()) {
			copy.getContextData().put(key, contextData.get(key));
		}
		// don't copy the listener data and command execution mode
		return copy;
	}

	public CommandExecutionMode getCommandExecutionMode() {
		return commandExecutionMode;
	}

	public Map<String, String> getContextData() {
		return contextData;
	}

	public Object getListenerData() {
		return listenerData;
	}

	public ModelChangeType getModelChangeType() {
		return modelChangeType;
	}

	public Schema getSchema() {
		return schema;
	}

	/**
	 * This attribute should only be set by the model change dispatcher and NOT by the component 
	 * that creates the initial context before handling a model change command to the command stack.
	 * @param commandExecutionMode the command execution mode during which the context is passed to 
	 *        model change listeners
	 */
	public void setCommandExecutionMode(CommandExecutionMode commandExecutionMode) {
		this.commandExecutionMode = commandExecutionMode;
	}

	/**
	 * This attribute should NOT be set by the component that creates the initial context before 
	 * handling a model change command to the command stack.  It is intended <i>only</i> for model  
	 * change listeners, that can add a 'listener object' to the context <i>before</i> the model 
	 * change is carried out, which can then be retrieved <i>after</i> the model has been changed.
	 * @param listenerData an object containing listener specific data 
	 */
	public void setListenerData(Object listenerData) {
		this.listenerData = listenerData;
	}
	
	public void setSchema(Schema schema) {
		this.schema = schema;
	}

	public String toString() {
		return "ModelChangeContext [modelChangeType=" + modelChangeType + 
			   ", commandExecutionMode=" + commandExecutionMode + 
			   ", contextData=" + contextData + 
			   ", listenerData=" + listenerData + "]";
	}
	
}
