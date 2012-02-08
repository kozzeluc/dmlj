package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class SetSortKeyDuplicatesOptionSection 
	extends AbstractKeyAttributeSection {

	public SetSortKeyDuplicatesOptionSection() {
		super(SchemaPackage.eINSTANCE.getKey_DuplicatesOption());
	}
	
	@Override
	protected String getDescription() {
		return "Specifies how CA IDMS/DB handles a record occurrence whose " +
			   "sort key duplicates an existing occurrence's sort key";
	}
	
	@Override
	protected String getLabel() {
		return "Duplicates";
	}
	
	@Override
	protected String getStringValue(Object value) {
		return value.toString().replaceAll("_", " ");
	}

}
