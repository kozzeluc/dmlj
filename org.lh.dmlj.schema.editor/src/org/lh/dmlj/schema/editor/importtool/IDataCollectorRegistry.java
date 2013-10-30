/**
 * Copyright (C) 2013  Luc Hermans
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
