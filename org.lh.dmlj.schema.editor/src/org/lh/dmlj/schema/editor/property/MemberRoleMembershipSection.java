package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class MemberRoleMembershipSection 
	extends AbstractMemberRoleAttributeSection {

	public MemberRoleMembershipSection() {
		super(SchemaPackage.eINSTANCE.getMemberRole_MembershipOption(), true); // read-only		
	}
	
	@Override
	protected String getDescription() {
		return "Specifies if occurrences of the member record type can be " +
			   "disconnected from the set other than through an ERASE " +
			   "function AND if they are connected implicitly to the set as " +
			   "part of the STORE function or only when the CONNECT function " +
			   "is issued";
	}
	
	@Override
	protected String getLabel() {
		return "Membership";
	}
	
	@Override
	protected String getStringValue(Object value) {
		if (value == null) {
			return super.getStringValue(value);
		} else {
			return value.toString().replaceAll("_", " ");
		}		
	}

}