package org.lh.dmlj.schema.editor.property.section;

import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.editor.template.AreaTemplate;

public class AreaSyntaxSection extends AbstractSyntaxSection {
	
	private static final Class<?>[] VALID_EDIT_PART_MODEL_OBJECTS =
		new Class[] {SchemaArea.class};
	
	public AreaSyntaxSection() {
		super(VALID_EDIT_PART_MODEL_OBJECTS, new AreaTemplate());
	}	
	
}