package org.lh.dmlj.schema.editor.importtool;

import java.util.List;

public interface IDataEntryContext {
	
	public static final String SCHEMA_NAME = "schemaName";
	public static final String SCHEMA_VERSION = "schemaVersion";
	
	public static final String DIAGRAMDATA_SHOW_RULERS = "showRulers";
	public static final String DIAGRAMDATA_SHOW_GRID = "showGrid";
	public static final String DIAGRAMDATA_SNAP_TO_GUIDES = "snapToGuides";
	public static final String DIAGRAMDATA_SNAP_TO_GRID = "snapToGrid";
	public static final String DIAGRAMDATA_SNAP_TO_GEOMETRY = "snapToGeometry";	
	
	void clear();
	
	void clearAttribute(String name);
	
	boolean containsAttribute(String name);
	
	<T extends Object> T getAttribute(String name);
	
	List<String> getAttributeNames();
	
	void setAttribute(String name, Object value);
	
}