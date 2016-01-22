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
package org.lh.dmlj.schema.editor.importtool.elements;

import java.util.Collection;

import org.lh.dmlj.schema.Usage;

public interface IRecordElementsDataCollector<T> {
	
	String getBaseName(T context);

	String getDependsOnElementName(T context);

	Collection<String> getIndexElementBaseNames(T context);
	
	Collection<String> getIndexElementNames(T context);
	
	boolean getIsNullable(T context);

	short getLevel(T context);

	String getName(T context);

	short getOccurrenceCount(T context);	

	String getPicture(T context);

	String getRedefinedElementName(T context);	
	
	Usage getUsage(T context);
	
	String getValue(T context);
	
}
