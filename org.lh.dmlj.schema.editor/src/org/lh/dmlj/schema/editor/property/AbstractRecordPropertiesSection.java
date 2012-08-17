package org.lh.dmlj.schema.editor.property;

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.SchemaRecord;

public abstract class AbstractRecordPropertiesSection 
	extends AbstractPropertiesSection<SchemaRecord>
    implements IRecordProvider {

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