package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class SetOrderSection extends AbstractSetAttributeSection {	
	
	public SetOrderSection() {
		super(SchemaPackage.eINSTANCE.getSet_Order(), true); // read-only
	}
	
	@Override
	protected String getDescription() {
		return "Specifies the logical order of adding new member record " +
			   "occurrences to a set occurrence at runtime";
	}
	
}