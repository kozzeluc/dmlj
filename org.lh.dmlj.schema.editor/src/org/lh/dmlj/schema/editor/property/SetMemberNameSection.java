package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class SetMemberNameSection extends AbstractSchemaRecordAttributeSection {	
	
	public SetMemberNameSection() {
		super(SchemaPackage.eINSTANCE.getSchemaRecord_Name(), true); // read-only
	}
	
	@Override
	protected String getDescription() {
		return "Identifies a record type that is to participate as a member " +
			   "of the set";
	}	
	
	@Override
	protected boolean isOwner() {
		return false;
	}
	
}