package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class MemberRoleOwnerPointerSection 
	extends AbstractMemberRoleAttributeSection {

	public MemberRoleOwnerPointerSection() {
		super(SchemaPackage.eINSTANCE.getMemberRole_OwnerDbkeyPosition(), true); // read-only
	}
	
	@Override
	protected String getDescription() {
		return "The relative position in the member record's prefix to be " +
			   "used for storing the database key of the owner record of the " +
			   "set";
	}
	
	@Override
	protected String getLabel() {
		return "Owner Dbkey Position";
	}

}