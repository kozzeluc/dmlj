package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class SchemaDescriptionSection extends AbstractSchemaAttributeSection {	
	
	public SchemaDescriptionSection() {
		super(SchemaPackage.eINSTANCE.getSchema_Description(), true); // read-only
	}
	
	@Override
	protected String getDescription() {
		return "Optionally specifies a name that is more descriptive than " +
			   "the 8-character schema name required by CA IDMS/DB, but can " +
			   "be used to store any type of information";
	}	
	
}