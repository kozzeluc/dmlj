package org.lh.dmlj.schema.editor.importtool.syntax;

import java.util.List;

import org.lh.dmlj.schema.editor.importtool.ISchemaDataCollector;

/**
 * This type is not a real data collector but merely a data object; all data
 * are passed to objects of this type upon construction.
 */
public class SchemaDataCollector implements ISchemaDataCollector {

	private List<String> comments;
	private String 		 memoDate;
	private String 		 schemaDescription;
	
	public SchemaDataCollector(String schemaDescription, String memoDate,
							   List<String> comments) {
		super();
		this.schemaDescription = schemaDescription;
		this.memoDate = memoDate;
		this.comments = comments;
	}

	@Override
	public List<String> getComments() {
		return comments;
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