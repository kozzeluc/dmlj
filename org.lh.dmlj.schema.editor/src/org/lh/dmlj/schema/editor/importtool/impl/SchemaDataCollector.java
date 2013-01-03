package org.lh.dmlj.schema.editor.importtool.impl;

import org.lh.dmlj.schema.editor.importtool.ISchemaDataCollector;

public class SchemaDataCollector implements ISchemaDataCollector {

	private String memoDate;
	private String schemaDescription;
	
	public SchemaDataCollector(String schemaDescription, String memoDate) {
		super();
		this.schemaDescription = schemaDescription;
		this.memoDate = memoDate;
	}

	@Override
	public String getSchemaDescription() {
		return schemaDescription;
	}

	@Override
	public String getSchemaMemoDate() {
		return memoDate;
	}

}