package org.lh.dmlj.schema.editor.importtool;


public interface IDataCollectorRegistry {

	<T> IAreaDataCollector<T> getAreaDataCollector(Class<T> _class);
	
	<T> IElementDataCollector<T> getElementDataCollector(Class<T> _class);
	
	<T> IRecordDataCollector<T> getRecordDataCollector(Class<T> _class);
	
	ISchemaDataCollector getSchemaDataCollector();
	
	<T> ISetDataCollector<T> getSetDataCollector(Class<T> _class);	
	
	<T> void registerAreaDataCollector(Class<T> _class, 
									   IAreaDataCollector<T> dataCollector);		
	
	<T> void registerElementDataCollector(Class<T> _class, 
										  IElementDataCollector<T> dataCollector);		

	<T> void registerRecordDataCollector(Class<T> _class, 
										 IRecordDataCollector<T> dataCollector);	

	void registerSchemaDataCollector(ISchemaDataCollector dataCollector);		

	<T> void registerSetDataCollector(Class<T> _class, 
									  ISetDataCollector<T> dataCollector);		
	
}