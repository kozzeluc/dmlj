package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class RecordCalcKeyDuplicatesOptionSection 
	extends AbstractKeyAttributeSection {

	public RecordCalcKeyDuplicatesOptionSection() {
		super(SchemaPackage.eINSTANCE.getKey_DuplicatesOption());
	}
	
	@Override
	protected String getDescription() {
		return "Specifies whether occurrences of a record type with " +
			   "duplicate CALC key values are allowed and, if allowed, how " +
			   "they are logically positioned relative to the duplicate " +
			   "record already stored";
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
