package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class SetModeSection extends AbstractSetAttributeSection {	
	
	public SetModeSection() {
		super(SchemaPackage.eINSTANCE.getSet_Mode(), true); // read-only
	}
	
	@Override
	protected String getDescription() {
		return "Specifies the characteristic of the set that tells CA IDMS/DB " +
				"how pointers are to be maintained at runtime";
	}
	
}