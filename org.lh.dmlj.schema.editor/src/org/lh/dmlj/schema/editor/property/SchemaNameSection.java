package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class SchemaNameSection extends AbstractSchemaAttributeSection {	
	
	public SchemaNameSection() {
		super(SchemaPackage.eINSTANCE.getSchema_Name(), true); // read-only
	}
	
	@Override
	protected String getDescription() {
		return "Identifies the schema";
	}	
	
}