package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class SetNameSection extends AbstractSetAttributeSection {	
	
	public SetNameSection() {
		super(SchemaPackage.eINSTANCE.getSet_Name(), true); // read-only
	}
	
	@Override
	protected String getDescription() {
		return "Identifies the database set description";
	}	
	
}