package org.lh.dmlj.schema.editor.property.section;

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.property.IRecordProvider;

public abstract class AbstractRecordPropertiesSection 
	extends AbstractAttributesBasedPropertiesSection<SchemaRecord>
    implements IRecordProvider {

	public AbstractRecordPropertiesSection() {
		super(Plugin.getDefault());
	}

	@Override
	public final SchemaRecord getRecord() {
		return target;
	}	
	
	@Override
	protected final SchemaRecord getTarget(Object modelObject) {
		Assert.isTrue(modelObject instanceof SchemaRecord, "not a SchemaRecord");
		return (SchemaRecord) modelObject;
	}
	
}