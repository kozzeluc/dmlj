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
package org.lh.dmlj.schema.editor.wizard._import.schema;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.lh.dmlj.schema.editor.importtool.IAreaDataCollector;
import org.lh.dmlj.schema.editor.importtool.IDataCollectorRegistry;
import org.lh.dmlj.schema.editor.importtool.IElementDataCollector;
import org.lh.dmlj.schema.editor.importtool.IRecordDataCollector;
import org.lh.dmlj.schema.editor.importtool.ISchemaDataCollector;
import org.lh.dmlj.schema.editor.importtool.ISetDataCollector;

class DataCollectorRegistry implements IDataCollectorRegistry {
	private final Map<Class<?>, IAreaDataCollector<?>> areaDataCollectors = new HashMap<>();
	private final Map<Class<?>, IElementDataCollector<?>> elementDataCollectors = new HashMap<>();
	private final Map<Class<?>, IRecordDataCollector<?>> recordDataCollectors = new HashMap<>();
	private ISchemaDataCollector schemaDataCollector;
	private final Map<Class<?>, ISetDataCollector<?>> setDataCollectors = new HashMap<>();			

	private static Object getDataCollector(Map<Class<?>, ?> targetDataCollectors, Class<?> type) {
		if (targetDataCollectors.containsKey(type)) {
			return targetDataCollectors.get(type);
		} else {
			return Arrays.stream(type.getInterfaces())
					.filter(targetDataCollectors::containsKey)
					.map(targetDataCollectors::get)
					.findFirst()
					.orElse(null);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> IAreaDataCollector<T> getAreaDataCollector(Class<T> type) {
		return (IAreaDataCollector<T>) getDataCollector(areaDataCollectors, type);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> IElementDataCollector<T> getElementDataCollector(Class<T> type) {
		return (IElementDataCollector<T>) getDataCollector(elementDataCollectors, type);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> IRecordDataCollector<T> getRecordDataCollector(Class<T> type) {
		return (IRecordDataCollector<T>) getDataCollector(recordDataCollectors, type);
	}

	@Override
	public ISchemaDataCollector getSchemaDataCollector() {
		return schemaDataCollector;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> ISetDataCollector<T> getSetDataCollector(Class<T> type) {
		return (ISetDataCollector<T>) getDataCollector(setDataCollectors, type);
	}

	@Override
	public <T> void registerAreaDataCollector(Class<T> type, IAreaDataCollector<T> dataCollector) {
		areaDataCollectors.put(type, dataCollector);		
	}

	@Override
	public <T> void registerElementDataCollector(Class<T> type, IElementDataCollector<T> dataCollector) {
		elementDataCollectors.put(type, dataCollector);
	}

	@Override
	public <T> void registerRecordDataCollector(Class<T> type, IRecordDataCollector<T> dataCollector) {
		recordDataCollectors.put(type, dataCollector);
	}

	@Override
	public void registerSchemaDataCollector(ISchemaDataCollector dataCollector) {
		schemaDataCollector = dataCollector; 
	}

	@Override
	public <T> void registerSetDataCollector(Class<T> type, ISetDataCollector<T> dataCollector) {
		setDataCollectors.put(type, dataCollector);
	}

}
