package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class RecordControlLengthSection extends AbstractSchemaRecordAttributeSection {	
	
	public RecordControlLengthSection() {
		super(SchemaPackage.eINSTANCE.getSchemaRecord_ControlLength(), true); // read-only
	}
	
	@Override
	protected String getDescription() {
		return "The length of the initial portion of the record that " +
			   "includes all bytes up to and including the last CALC, index, " +
			   "or sort control element";
	}
	
	@Override
	protected String getLabel() {
		return "Control Length";
	}
	
}