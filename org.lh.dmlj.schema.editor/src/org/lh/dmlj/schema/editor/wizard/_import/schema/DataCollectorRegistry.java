package org.lh.dmlj.schema.editor.wizard._import.schema;

import java.util.HashMap;
import java.util.Map;

import org.lh.dmlj.schema.editor.importtool.IAreaDataCollector;
import org.lh.dmlj.schema.editor.importtool.IDataCollectorRegistry;
import org.lh.dmlj.schema.editor.importtool.IElementDataCollector;
import org.lh.dmlj.schema.editor.importtool.IRecordDataCollector;
import org.lh.dmlj.schema.editor.importtool.ISchemaDataCollector;
import org.lh.dmlj.schema.editor.importtool.ISetDataCollector;

class DataCollectorRegistry implements IDataCollectorRegistry {

	private Map<Class<?>, IAreaDataCollector<?>>    areaDataCollectors = 
		new HashMap<>();
	private Map<Class<?>, IElementDataCollector<?>> elementDataCollectors = 
		new HashMap<>();
	private Map<Class<?>, IRecordDataCollector<?>>  recordDataCollectors = 
		new HashMap<>();
	private ISchemaDataCollector 					schemaDataCollector;
	private Map<Class<?>, ISetDataCollector<?>>     setDataCollectors = 
		new HashMap<>();		
	
	DataCollectorRegistry() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> IAreaDataCollector<T> getAreaDataCollector(Class<T> _class) {
		if (areaDataCollectors.containsKey(_class)) {
			return (IAreaDataCollector<T>) areaDataCollectors.get(_class);
		} else {
			for (Class<?> _interface : _class.getInterfaces()) {
				if (areaDataCollectors.containsKey(_interface)) {
					return (IAreaDataCollector<T>) areaDataCollectors.get(_interface);
				}
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> IElementDataCollector<T> getElementDataCollector(Class<T> _class) {
		if (elementDataCollectors.containsKey(_class)) {
			return (IElementDataCollector<T>)elementDataCollectors.get(_class);
		} else {
			for (Class<?> _interface : _class.getInterfaces()) {
				if (elementDataCollectors.containsKey(_interface)) {
					return (IElementDataCollector<T>) elementDataCollectors.get(_interface);
				}
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> IRecordDataCollector<T> getRecordDataCollector(Class<T> _class) {
		if (recordDataCollectors.containsKey(_class)) {
			return (IRecordDataCollector<T>)recordDataCollectors.get(_class);
		} else {
			for (Class<?> _interface : _class.getInterfaces()) {
				if (recordDataCollectors.containsKey(_interface)) {
					return (IRecordDataCollector<T>) recordDataCollectors.get(_interface);
				}
			}
			return null;
		}
	}

	@Override
	public ISchemaDataCollector getSchemaDataCollector() {
		return schemaDataCollector;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> ISetDataCollector<T> getSetDataCollector(Class<T> _class) {
		if (setDataCollectors.containsKey(_class)) {
			return (ISetDataCollector<T>)setDataCollectors.get(_class);
		} else {
			for (Class<?> _interface : _class.getInterfaces()) {
				if (setDataCollectors.containsKey(_interface)) {
					return (ISetDataCollector<T>) setDataCollectors.get(_interface);
				}
			}
			return null;
		}
	}

	@Override
	public <T> void registerAreaDataCollector(Class<T> _class,
											  IAreaDataCollector<T> dataCollector) {
		
		areaDataCollectors.put(_class, dataCollector);		
	}

	@Override
	public <T> void registerElementDataCollector(Class<T> _class,
												 IElementDataCollector<T> dataCollector) {
		
		elementDataCollectors.put(_class, dataCollector);
	}

	@Override
	public <T> void registerRecordDataCollector(Class<T> _class,
												IRecordDataCollector<T> dataCollector) {
		
		recordDataCollectors.put(_class, dataCollector);
	}

	@Override
	public void registerSchemaDataCollector(ISchemaDataCollector dataCollector) {
		schemaDataCollector = dataCollector; 
	}

	@Override
	public <T> void registerSetDataCollector(Class<T> _class,
											 ISetDataCollector<T> dataCollector) {
		
		setDataCollectors.put(_class, dataCollector);
	}

}