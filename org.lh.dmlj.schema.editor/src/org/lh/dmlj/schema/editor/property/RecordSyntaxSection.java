package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.template.RecordTemplate;

public class RecordSyntaxSection extends AbstractSyntaxSection {
	
	private static final Class<?>[] VALID_EDIT_PART_MODEL_OBJECTS =
		new Class[] {SchemaRecord.class};
	
	public RecordSyntaxSection() {
		super(VALID_EDIT_PART_MODEL_OBJECTS, new RecordTemplate());
	}	
	
}