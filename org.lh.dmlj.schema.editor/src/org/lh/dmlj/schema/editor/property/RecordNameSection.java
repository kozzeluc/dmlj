package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class RecordNameSection extends AbstractSchemaRecordAttributeSection {	
	
	public RecordNameSection() {
		super(SchemaPackage.eINSTANCE.getSchemaRecord_Name(), true); // read-only
	}
	
	@Override
	protected String getDescription() {
		return "Identifies the database record description";
	}	
	
}