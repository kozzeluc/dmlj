package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class RecordNameSection extends AbstractSchemaRecordAttributeSection {	
	
	public RecordNameSection() {
		super(SchemaPackage.eINSTANCE.getSchemaRecord_Name(), true); // read-only
	}
	
	@Override
	protected String getDescription() {
		return "Identifies the database record description";
	}
	
	@Override
	protected String getStringValue(Object value) {
		if (!(value instanceof String)) {
			return super.getStringValue(value);
		}
		
		String sValue = (String) value;
		
		// we need to manipulate the record name in the case of some dictionary
		// records (DDLCATLOD area, which has the same structure as DDLDCLOD)...
		if (sValue.endsWith("_")) {
			return sValue.substring(0, sValue.length() - 1);
		} else {
			return sValue;
		}
	}
	
}