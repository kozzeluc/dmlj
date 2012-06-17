package org.lh.dmlj.schema.editor.property;

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.Schema;

public abstract class AbstractSchemaPropertiesSection 
	extends AbstractPropertiesSection<Schema> {	

	@Override
	protected final Schema getTarget(Object modelObject) {				
		Assert.isTrue(modelObject instanceof Schema, "not a Schema");				
		return (Schema) modelObject;		
	}
	
}