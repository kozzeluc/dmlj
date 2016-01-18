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
package org.lh.dmlj.schema.editor.importtool;

import java.util.List;

public interface IDataEntryContext {
	
	// TODO move the standard context attribute names to an abstract class or interface (same package)
	
	// standard schema import tool context attribute names
	public static final String SCHEMA_NAME = "schemaName";
	public static final String SCHEMA_VERSION = "schemaVersion";	
	public static final String DIAGRAMDATA_SHOW_RULERS = "showRulers";
	public static final String DIAGRAMDATA_SHOW_GRID = "showGrid";
	public static final String DIAGRAMDATA_SNAP_TO_GUIDES = "snapToGuides";
	public static final String DIAGRAMDATA_SNAP_TO_GRID = "snapToGrid";
	public static final String DIAGRAMDATA_SNAP_TO_GEOMETRY = "snapToGeometry";
	
	// standard record elements import tool context attribute names
	public static final String CURRENT_SCHEMA_RECORD = "currentSchemaRecord";
	public static final String SCHEMA = "schema";
	public static final String RECORD = "record";
	
	void clear();
	
	void clearAttribute(String name);
	
	boolean containsAttribute(String name);
	
	<T extends Object> T getAttribute(String name);
	
	List<String> getAttributeNames();
	
	void setAttribute(String name, Object value);
	
}
