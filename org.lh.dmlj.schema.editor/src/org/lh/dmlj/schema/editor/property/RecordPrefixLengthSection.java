package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class RecordPrefixLengthSection extends AbstractSchemaRecordAttributeSection {	
	
	public RecordPrefixLengthSection() {
		super(SchemaPackage.eINSTANCE.getSchemaRecord_PrefixLength(), true); // read-only
	}
	
	@Override
	protected String getDescription() {
		return "The length, in bytes, of the prefix portion of the record " +
			   "(including the pointers for the CALC set in the case of a " +
			   "CALC record)";
	}
	
	@Override
	protected String getLabel() {
		return "Prefix";
	}
	
}