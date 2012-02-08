package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class SetMemberAreaSection 
	extends AbstractAreaAttributeSection {

	public SetMemberAreaSection() {
		super(SchemaPackage.eINSTANCE.getSchemaArea_Name(), true); // read-only		
	}
	
	@Override
	protected String getDescription() {
		return "Identifies the area in which occurrences of the member " +
			   "record type will be located";
	}
	
	@Override
	protected String getLabel() {
		return "Area";
	}
	
	@Override
	protected boolean isOwner() {
		return false;
	}

}