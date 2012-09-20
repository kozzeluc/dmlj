package org.lh.dmlj.schema.editor.importtool;

import org.lh.dmlj.schema.Schema;

public interface ISchemaImportTool {
	
	Schema performTask(IDataEntryContext context);
	
}