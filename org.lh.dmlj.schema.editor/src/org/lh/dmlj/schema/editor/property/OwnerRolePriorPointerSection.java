package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class OwnerRolePriorPointerSection 
	extends AbstractOwnerRoleAttributeSection {

	public OwnerRolePriorPointerSection() {
		super(SchemaPackage.eINSTANCE.getOwnerRole_PriorDbkeyPosition(), true); // read-only
	}
	
	@Override
	protected String getDescription() {
		return "Represents the sequential position of the PRIOR set pointer " +
			   "within the owner record's prefix";
	}
	
	@Override
	protected String getLabel() {
		return "Prior Dbkey Position";
	}

}