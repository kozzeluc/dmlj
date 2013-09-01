package org.lh.dmlj.schema.editor.property.section;

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.editor.Plugin;

public abstract class AbstractSchemaPropertiesSection 
	extends AbstractAttributesBasedPropertiesSection<Schema> {	

	public AbstractSchemaPropertiesSection() {
		super(Plugin.getDefault());
	}

	@Override
	protected final Schema getTarget(Object modelObject) {				
		Assert.isTrue(modelObject instanceof Schema, "not a Schema");				
		return (Schema) modelObject;		
	}
	
}