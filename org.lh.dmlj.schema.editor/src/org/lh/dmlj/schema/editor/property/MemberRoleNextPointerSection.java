package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class MemberRoleNextPointerSection 
	extends AbstractMemberRoleAttributeSection {

	public MemberRoleNextPointerSection() {
		super(SchemaPackage.eINSTANCE.getMemberRole_NextDbkeyPosition(), true); // read-only		
	}
	
	@Override
	protected String getDescription() {
		return "The sequential position of the next set pointer within the " +
			   "member record's prefix";
	}
	
	@Override
	protected String getLabel() {
		return "Next Dbkey Position";
	}

}