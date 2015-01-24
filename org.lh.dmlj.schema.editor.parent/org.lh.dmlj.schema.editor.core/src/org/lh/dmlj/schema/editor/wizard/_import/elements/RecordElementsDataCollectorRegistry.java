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
package org.lh.dmlj.schema.editor.wizard._import.elements;

import java.util.HashMap;
import java.util.Map;

import org.lh.dmlj.schema.editor.importtool.elements.IRecordElementsDataCollector;
import org.lh.dmlj.schema.editor.importtool.elements.IRecordElementsDataCollectorRegistry;

public class RecordElementsDataCollectorRegistry implements IRecordElementsDataCollectorRegistry {

	private Map<Class<?>, IRecordElementsDataCollector<?>> dataCollectors = new HashMap<>();		
	
	RecordElementsDataCollectorRegistry() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> IRecordElementsDataCollector<T> getDataCollector(Class<T> _class) {
		if (dataCollectors.containsKey(_class)) {
			return (IRecordElementsDataCollector<T>) dataCollectors.get(_class);
		} else {
			for (Class<?> _interface : _class.getInterfaces()) {
				if (dataCollectors.containsKey(_interface)) {
					return (IRecordElementsDataCollector<T>) dataCollectors.get(_interface);
				}
			}
			return null;
		}
	}

	@Override
	public <T> void registerDataCollector(Class<T> _class, IRecordElementsDataCollector<T> dataCollector) {		
		dataCollectors.put(_class, dataCollector);
	}

}
