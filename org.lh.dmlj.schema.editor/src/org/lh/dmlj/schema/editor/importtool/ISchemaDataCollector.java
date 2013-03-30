package org.lh.dmlj.schema.editor.importtool;

import java.util.List;


public interface ISchemaDataCollector {

	List<String> getComments();
	
	String getSchemaDescription();

	String getSchemaMemoDate();
	
}