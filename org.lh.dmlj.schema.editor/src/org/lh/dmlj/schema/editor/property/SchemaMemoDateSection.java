package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class SchemaMemoDateSection extends AbstractSchemaAttributeSection {	
	
	public SchemaMemoDateSection() {
		super(SchemaPackage.eINSTANCE.getSchema_MemoDate(), true); // read-only
	}
	
	@Override
	protected String getDescription() {
		return "Specifies any date the user wishes to supply";
	}	
	
}