package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class RecordMinimumRootLengthSection extends AbstractSchemaRecordAttributeSection {	
	
	public RecordMinimumRootLengthSection() {
		super(SchemaPackage.eINSTANCE.getSchemaRecord_MinimumRootLength(), true); // read-only
	}
	
	@Override
	protected String getDescription() {
		return "Specifies the minimum portion of a variable-length record " +
			   "that can be stored on a database page";
	}
	
	@Override
	protected String getLabel() {
		return "Min. root length";
	}
	
}