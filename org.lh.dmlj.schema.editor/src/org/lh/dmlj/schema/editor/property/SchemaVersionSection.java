package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class SchemaVersionSection extends AbstractSchemaAttributeSection {	
	
	public SchemaVersionSection() {
		super(SchemaPackage.eINSTANCE.getSchema_Version(), true); // read-only
	}
	
	@Override
	protected String getDescription() {
		return "Qualifies the schema with a version number, which " +
			   "distinguishes this schema from others that have the same name";
	}	
	
}