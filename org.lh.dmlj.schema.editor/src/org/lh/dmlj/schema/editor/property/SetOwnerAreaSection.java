package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class SetOwnerAreaSection 
	extends AbstractAreaAttributeSection {

	public SetOwnerAreaSection() {
		super(SchemaPackage.eINSTANCE.getSchemaArea_Name(), true); // read-only		
	}
	
	@Override
	protected String getDescription() {
		return "Specifies the area in which the (system) owner (record) and, " +
			   "in the case of an indexed set, the index structure is to reside";		
	}
	
	@Override
	protected String getLabel() {
		return "Area";
	}

	@Override
	protected boolean isOwner() {
		return true;
	}
	
}