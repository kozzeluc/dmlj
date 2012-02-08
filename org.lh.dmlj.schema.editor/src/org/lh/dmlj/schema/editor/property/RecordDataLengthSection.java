package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class RecordDataLengthSection extends AbstractSchemaRecordAttributeSection {	
	
	public RecordDataLengthSection() {
		super(SchemaPackage.eINSTANCE.getSchemaRecord_DataLength(), true); // read-only
	}
	
	@Override
	protected String getDescription() {
		return "The length, in bytes, of the data portion of the record";
	}
	
	@Override
	protected String getLabel() {
		return "Data";
	}
	
}