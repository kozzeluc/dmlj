package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class RecordIdSection extends AbstractSchemaRecordAttributeSection {	
	
	public RecordIdSection() {
		super(SchemaPackage.eINSTANCE.getSchemaRecord_Id(), true); // read-only
	}
	
	@Override
	protected String getDescription() {
		return "Assigns a number that uniquely identifies each schema record " +
			   "type";
	}
	
	@Override
	protected String getLabel() {
		return "Record ID";
	}
	
}