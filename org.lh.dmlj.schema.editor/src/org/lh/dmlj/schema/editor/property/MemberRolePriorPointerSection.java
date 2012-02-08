package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class MemberRolePriorPointerSection 
	extends AbstractMemberRoleAttributeSection {

	public MemberRolePriorPointerSection() {
		super(SchemaPackage.eINSTANCE.getMemberRole_PriorDbkeyPosition(), true); // read-only		
	}
	
	@Override
	protected String getDescription() {
		return "The sequential position of the prior set pointer within the " +
			   "member record's prefix";
	}
	
	@Override
	protected String getLabel() {
		return "Prior Dbkey Position";
	}

}