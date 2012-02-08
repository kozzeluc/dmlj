package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class SetOwnerNameSection extends AbstractSchemaRecordAttributeSection {	
	
	public SetOwnerNameSection() {
		super(SchemaPackage.eINSTANCE.getSchemaRecord_Name(), true); // read-only
	}
	
	@Override
	protected String getDescription() {
		return "Identifies the record type that owns the set";
	}
	
	@Override
	protected boolean isOwner() {
		return true;
	}
	
}