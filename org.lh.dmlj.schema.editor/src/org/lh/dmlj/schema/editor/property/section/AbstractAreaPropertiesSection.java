package org.lh.dmlj.schema.editor.property.section;

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.property.IAreaProvider;

public abstract class AbstractAreaPropertiesSection 
	extends AbstractAttributesBasedPropertiesSection<SchemaArea> implements IAreaProvider {

	public AbstractAreaPropertiesSection() {
		super(Plugin.getDefault());
	}

	@Override
	public final SchemaArea getArea() {
		return target;
	}	
	
	@Override
	protected final SchemaArea getTarget(Object modelObject) {
		Assert.isTrue(modelObject instanceof SchemaArea, "not a SchemaArea");
		return (SchemaArea) modelObject;
	}
	
}