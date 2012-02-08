package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class MemberRoleIndexPointerSection 
	extends AbstractMemberRoleAttributeSection {

	public MemberRoleIndexPointerSection() {
		super(SchemaPackage.eINSTANCE.getMemberRole_IndexDbkeyPosition(), true); // read-only		
	}
	
	@Override
	protected String getDescription() {
		return "The sequential position of the index set pointer within the " +
			   "member record's prefix";
	}
	
	@Override
	protected String getLabel() {
		return "Index Dbkey Position";
	}

}