package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class OwnerRoleNextPointerSection 
	extends AbstractOwnerRoleAttributeSection {

	public OwnerRoleNextPointerSection() {
		super(SchemaPackage.eINSTANCE.getOwnerRole_NextDbkeyPosition(), true); // read-only
	}
	
	@Override
	protected String getDescription() {
		return "Represents the sequential position of the NEXT set pointer " +
			   "within the owner record's prefix";
	}
	
	@Override
	protected String getLabel() {
		return "Next Dbkey Position";
	}

}