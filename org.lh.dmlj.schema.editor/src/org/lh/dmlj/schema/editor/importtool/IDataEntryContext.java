package org.lh.dmlj.schema.editor.importtool;

import java.util.List;

public interface IDataEntryContext {
	
	public static final String SCHEMA_NAME = "schemaName";
	public static final String SCHEMA_VERSION = "schemaVersion";	
	
	void clear();
	
	void clearAttribute(String name);
	
	boolean containsAttribute(String name);
	
	<T extends Object> T getAttribute(String name);
	
	List<String> getAttributeNames();
	
	void setAttribute(String name, Object value);
	
}