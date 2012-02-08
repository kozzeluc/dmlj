package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class RecordStorageModeSection extends AbstractSchemaRecordAttributeSection {	
	
	public RecordStorageModeSection() {
		super(SchemaPackage.eINSTANCE.getSchemaRecord_StorageMode(), true); // read-only
	}
	
	@Override
	protected String getDescription() {
		return "Indicates if the record has a fixed or variable length and " +
			   "whether or not it is compressed";
	}
	
	@Override
	protected String getLabel() {
		return "Storage mode";
	}
	
	@Override
	protected String getStringValue(Object value) {
		return value.toString().replaceAll("_", " ");
	}
	
}