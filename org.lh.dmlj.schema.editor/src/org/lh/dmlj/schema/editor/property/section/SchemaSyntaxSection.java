package org.lh.dmlj.schema.editor.property.section;

import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.editor.template.SchemaTemplate;

public class SchemaSyntaxSection extends AbstractSyntaxSection {
	
	private static final Class<?>[] VALID_EDIT_PART_MODEL_OBJECTS =
		new Class[] {Schema.class};
	
	public SchemaSyntaxSection() {
		super(VALID_EDIT_PART_MODEL_OBJECTS, new SchemaTemplate());
	}
	
	@Override
	protected Object[] getTemplateParameters() {
		return new Object[] {Boolean.FALSE}; // no full syntax
	}
	
}