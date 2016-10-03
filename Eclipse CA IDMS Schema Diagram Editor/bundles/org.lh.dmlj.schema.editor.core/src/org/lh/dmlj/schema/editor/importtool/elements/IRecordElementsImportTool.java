/**
 * Copyright (C) 2016 Luc Hermans
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
package org.lh.dmlj.schema.editor.importtool.elements;

import java.util.Collection;
import java.util.Properties;

import org.lh.dmlj.schema.editor.importtool.elements.IRecordElementsDataCollectorRegistry;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;

public interface IRecordElementsImportTool {
	
	/**
	 * Disposes the import tool; this method is invoked only once and no methods will be invoked
	 * on the import tool after this method was invoked. 
	 */
	void dispose();

	Collection<?> getRootElementContexts();
	
	<T> Collection<T> getSubordinateElementContexts(T elementContext);
	
	/**
	 * Initializes the import tool; this method is invoked only once. 
	 */
	void init(Properties parameters, IRecordElementsDataCollectorRegistry recordElementDataCollectorRegistry);
	
	/**
	 * Sets the data entry context; this method is invoked each time immediately before the 
	 * getRootElementContexts() method.
	 * @param dataEntryContext the data entry context
	 */
	void setContext(IDataEntryContext dataEntryContext);
	
}
