package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class RecordMinimumFragmentLengthSection extends AbstractSchemaRecordAttributeSection {	
	
	public RecordMinimumFragmentLengthSection() {
		super(SchemaPackage.eINSTANCE.getSchemaRecord_MinimumFragmentLength(), true); // read-only
	}
	
	@Override
	protected String getDescription() {
		return "Specifies the minimum length of subsequent segments " +
			   "(fragments) of a variable-length record";
	}
	
	@Override
	protected String getLabel() {
		return "Min. fragment length";
	}
	
}