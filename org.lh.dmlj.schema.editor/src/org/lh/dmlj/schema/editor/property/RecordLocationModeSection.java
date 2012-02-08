package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class RecordLocationModeSection extends AbstractSchemaRecordAttributeSection {	
	
	public RecordLocationModeSection() {
		super(SchemaPackage.eINSTANCE.getSchemaRecord_LocationMode(), true); // read-only
	}
	
	@Override
	protected String getDescription() {
		return "Defines the technique that CA IDMS/DB will use to physically " +
			   "store occurrences of the record type";
	}
	
	@Override
	protected String getLabel() {
		return "Location mode";
	}	
	
}