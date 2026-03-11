/**
 * Copyright (C) 2025  Luc Hermans
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
package org.lh.dmlj.schema.editor.importtool;


public interface IDataCollectorRegistry {

	<T> IAreaDataCollector<T> getAreaDataCollector(Class<T> type);
	
	<T> IElementDataCollector<T> getElementDataCollector(Class<T> type);
	
	<T> IRecordDataCollector<T> getRecordDataCollector(Class<T> type);
	
	ISchemaDataCollector getSchemaDataCollector();
	
	<T> ISetDataCollector<T> getSetDataCollector(Class<T> type);
	
	<T> void registerAreaDataCollector(Class<T> type, IAreaDataCollector<T> dataCollector);		
	
	<T> void registerElementDataCollector(Class<T> type, IElementDataCollector<T> dataCollector);		

	<T> void registerRecordDataCollector(Class<T> type, IRecordDataCollector<T> dataCollector);

	void registerSchemaDataCollector(ISchemaDataCollector dataCollector);		

	<T> void registerSetDataCollector(Class<T> type, ISetDataCollector<T> dataCollector);
	
}
