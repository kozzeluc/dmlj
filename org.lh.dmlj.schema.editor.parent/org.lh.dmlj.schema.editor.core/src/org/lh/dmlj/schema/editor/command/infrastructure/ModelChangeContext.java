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

public class ModelChangeContext {

	private Map<String, String> contextData = new HashMap<>();
	private Object listenerData;
	private ModelChangeType modelChangeType;
	
	public ModelChangeContext(ModelChangeType modelChangeType) {
		super();
		this.modelChangeType = modelChangeType;
	}

	public ModelChangeContext copy() {
		ModelChangeContext copy = new ModelChangeContext(modelChangeType);
		for (String key : contextData.keySet()) {
			copy.getContextData().put(key, contextData.get(key));
		}
		// don't copy the listener data
		return copy;
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

	public void setListenerData(Object listenerData) {
		this.listenerData = listenerData;
	}
	
}
